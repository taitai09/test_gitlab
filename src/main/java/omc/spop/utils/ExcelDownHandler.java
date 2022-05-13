package omc.spop.utils;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import com.omc.util.CloseUtil;
import com.spop.utils.ExcelBuilder;

import omc.spop.view.ExcelDownHeaders;

public class ExcelDownHandler extends ExcelBuilder
{
	private static final Logger logger = LoggerFactory.getLogger(ExcelDownHandler.class);
	private final static int FLUSH_SIZE = 100;
	private final static int MAX_LINE = 15000;

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private Map<String, Object> model = null;
	private String sheetName;

	private SXSSFWorkbook workbook;
	private SXSSFSheet worksheet;

	private CellStyle styleHeader = null;
	private CellStyle styleDefault = null;
	private CellStyle styleString = null;
	private CellStyle styleNumber = null;
	private CellStyle styleFloatingPoint = null;
	private CellStyle styleTimestamp = null;

	public ExcelDownHandler()
	{
		this.model = new HashMap<String, Object>();
	}

	public ExcelDownHandler(Model model, HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
		this.model = model.asMap();
	}

	public void open() throws Exception
	{
		print("start");
		workbook = new SXSSFWorkbook(FLUSH_SIZE);
		workbook.setCompressTempFiles(true);

		styleHeader = ExcelCellRef.setCustomCellStyle(workbook, "HEADER", null);
		styleDefault = ExcelCellRef.setCustomCellStyle(workbook, "DEFAULT", null);
		styleString = ExcelCellRef.setCustomCellStyle(workbook, "STRING", null);
		styleNumber = ExcelCellRef.setCustomCellStyle(workbook, "NUMBER", null);
		styleFloatingPoint = ExcelCellRef.setCustomCellStyle(workbook, "FLOATING_POINT", null);
		styleTimestamp = ExcelCellRef.setCustomCellStyle(workbook, "TIMESTAMP", null);

		sheetName = (String) model.get("sheetName");
		String fileName = StringUtils.defaultString((String) model.get("fileName"), sheetName);
		String toDate = DateUtil.getNowDate("yyyyMMddhhmmssSSS");
		String sFileName = URLEncoder.encode(fileName + "_" + toDate + ".xlsx", "UTF-8");
		setInfoToResponse(response, request, sFileName, model);
	}

	public void close()
	{
		try
		{
			workbook.dispose();
			workbook.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		print("end");
	}

	public void buildInit(String sheetName, String excelId)
	{
		this.sheetName = sheetName;
		ExcelDownHeaders headers = new ExcelDownHeaders();
		enList = getHeader(headers, excelId + "_EN");
		koList = getHeader(headers, excelId + "_KO");
		rmHtmlList = new ArrayList<String>();
	}

	public String getSql(SqlSessionFactory factory, String sqlId, Object object)
	{
		SqlSession session = factory.openSession();
		try
		{
			Configuration configuration = session.getConfiguration();
			MappedStatement ms = configuration.getMappedStatement(sqlId);
			BoundSql boundSql = ms.getBoundSql(object);
			return boundSql.getSql();
		}
		finally
		{
			session.close();
		}
	}

	public void buildDocument(SqlSessionFactory factory, String sql)
	{
		SqlSession session = factory.openSession();
		try
		{
			execute(session.getConnection(), sql);
		}
		finally
		{
			session.close();
		}
	}

	protected void addHeader()
	{
		if (workbook.getSheetIndex(sheetName) == -1)
			worksheet = workbook.createSheet(sheetName);
		else
			worksheet = workbook.createSheet(sheetName + "_" + workbook.getNumberOfSheets());
		worksheet.trackAllColumnsForAutoSizing();

		row = 0;
		Row heading = worksheet.createRow(0);

		int idx = 0;
		for (int i = 0; i < columnList.size(); i++)
		{
			HeaderMeta meta = columnList.get(i);
			int j = enList.indexOf(meta.name);
			if (j > -1)
			{
				meta.index = idx++;
				meta.header = koList.get(j);
			}
		}
		for (int i = 0; i < columnList.size(); i++)
		{
			HeaderMeta meta = columnList.get(i);
			if (meta.header != null)
			{
				Cell cell = heading.createCell(meta.index);
				cell.setCellValue(meta.header);
				cell.setCellStyle(styleHeader);
				worksheet.autoSizeColumn(meta.index);
			}
		}
	}

	protected void addRow(Map<String, Object> map)
	{
		if (row >= MAX_LINE)
			addHeader();
		row++;
		Row heading = worksheet.createRow(row);
		for (int i = 0; i < columnList.size(); i++)
		{
			HeaderMeta meta = columnList.get(i);
			if (meta.header != null)
			{
				Cell cell = heading.createCell(meta.index);
				Object obj = map.get(meta.name);
				setCellValue(meta, cell, obj);
			}
		}
		if (row == FLUSH_SIZE)
		{
			autoSizeColumn();			
			worksheet.untrackAllColumnsForAutoSizing();
		}
	}

	private void setCellValue(HeaderMeta meta, Cell cell, Object obj)
	{
		if (obj instanceof Boolean)
			cell.setCellValue((Boolean) obj);
		else if (obj instanceof Long)
		{
			cell.setCellValue((Long) obj);
			cell.setCellStyle(styleNumber);
		}
		else if (obj instanceof Double)
		{
			cell.setCellValue((Double) obj);
			cell.setCellStyle(styleFloatingPoint);
		}
		else if (obj instanceof Date)
			cell.setCellValue((Date) obj);
		else if (obj instanceof Timestamp)
		{
			cell.setCellValue(((Timestamp) obj).toString());
			cell.setCellStyle(styleTimestamp);
		}
		else if (obj instanceof String)
		{
			String str = (String) obj;
			if (meta.rmHtml)
			{
				str = ExcelCellRef.removeHTML(str);
				str = ExcelCellRef.removeSpecialChar(str);
			}
			cell.setCellValue(str);
			cell.setCellStyle(styleString);
		}
		else
		{
			cell.setCellValue((String) obj);
			cell.setCellStyle(styleDefault);
		}
	}

	protected void addEnd()
	{
		Row heading = worksheet.createRow(1);
		Cell cell = heading.createCell(0);
		cell.setCellValue("데이터가 없습니다.");
		cell.setCellStyle(styleString);
	}

	public void writeFile() throws Exception
	{
		FileOutputStream os = null;
		try
		{
			os = new FileOutputStream("D:\\Temp\\tmp.xlsx");
			workbook.write(os);
			os.flush();
		}
		finally
		{
			CloseUtil.close(os);
		}
	}

	public void writeDoc() throws Exception
	{
		ServletOutputStream fos = null;
		try
		{
			fos = response.getOutputStream();
			workbook.write(fos);
			fos.flush();
		}
		finally
		{
			CloseUtil.close(fos);
		}
	}

	private List<String> getHeader(Object obj, String excelId)
	{
		try
		{
			Field field = obj.getClass().getField(excelId);
			String[] arr = (String[]) field.get(obj);
			return new ArrayList<String>(Arrays.asList(arr));
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}
	}

	private void autoSizeColumn()
	{
		for (int i = 0; i < columnList.size(); i++)
		{
			HeaderMeta meta = columnList.get(i);
			if (meta.header != null)
				worksheet.autoSizeColumn(meta.index);
		}
	}

	private void setInfoToResponse(HttpServletResponse response, HttpServletRequest request, String sFileName, Map<String, Object> model) throws Exception{
		Cookie cookie = null;
		String cookieName = "";
		
		try {
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + sFileName + ";");
			
			cookieName = request.getServletPath();
			
			if (model.containsKey("cookieNm")) {
				cookieName = model.get("cookieNm").toString();
				logger.debug("cookie name is {}", cookieName);
			}
			
			cookie = new Cookie(cookieName, "TRUE");
			cookie.setPath("/");
			response.addCookie(cookie);
			
		} catch (Exception e) {
			logger.error("Error Occured ===> {}",e.getMessage());
			e.printStackTrace();
			
		} finally {
			cookie = null;
			cookieName = null;
		}
	}
}