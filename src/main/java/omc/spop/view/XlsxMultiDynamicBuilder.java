package omc.spop.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import omc.spop.utils.DateUtil;
import oracle.sql.CLOB;

public class XlsxMultiDynamicBuilder extends AbstractXlsxView {
	private static final Logger logger = LoggerFactory.getLogger(XlsxMultiDynamicBuilder.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Sheet sheet = null;
	private static CellStyle styleHeader = null;
	private static CellStyle styleString = null;
	private static CellStyle styleDefault = null;
	private static CellStyle styleNumber = null;
	private static CellStyle styleFloatingPoint = null;
	private static CellStyle styleTimestamp = null;
	private static CellStyle styleRgb = null;
	public static final String HEAD_OPTIONS_EXCEL_ERROR_KEY = "EXCEL_ERROR";

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		buildDocument(model, (XSSFWorkbook) workbook, request, response);
	}

	@SuppressWarnings("null")
	public void buildDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug(this.getClass().getName() + ".buildDocument started");
		String excelId = StringUtils.defaultString((String) model.get("excelId"));
		logger.debug("excelId::" + excelId);
		List<String> sheetName = (List<String>) model.get("sheetName");
		int sheetNameLength = sheetName.size();
		logger.debug("sheetName Len::" + sheetNameLength);

		String fileName = StringUtils.defaultString((String) model.get("fileName"), sheetName.get(0) + "");
		
		StringBuffer fileNameSb = new StringBuffer();
		String[] menuNmArray = fileName.split(" ");
		
		for(int menuIndex = 0; menuIndex < menuNmArray.length; menuIndex++) {
			if(menuIndex > 0) {
				fileNameSb.append("_");
			}
			
			fileNameSb.append(menuNmArray[menuIndex]);
		}
		
		if(fileNameSb.length() > 0) {
			fileName = fileNameSb.toString();
		}

		String toDate = DateUtil.getNowDate("yyyyMMddhhmmssSSS");
		String sFileName = URLEncoder.encode(fileName + "_" + toDate + ".xlsx", "UTF-8");

		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		// response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + sFileName + ";");

		// get data model which is passed by the Spring container
		List multiDynamicResultList = (List) model.get("multiDynamicResultList");

		List<Map<String, Object>> resultList = null;
		Set<String> headerTitleSet = null;
		Map<String, Object> headOptions = null;
		String[] excelHeaders = null;
		
		for(int multiIndex = 0; multiIndex < multiDynamicResultList.size(); multiIndex++) {
			resultList = (List<Map<String, Object>>) multiDynamicResultList.get(multiIndex);
			
			headOptions = (Map<String, Object>) resultList.remove(resultList.size() - 1);
			
			if(headOptions != null) {
				String head = headOptions.get("HEAD") + "";
				String[] headSplit = head.split("\\;");
				
				//동적 엑셀 그리드를 위한 엑셀 헤더값 전달.
				String[] headers = new String[headSplit.length];
				
				for(int i = 0; i < headSplit.length; i++){
					headers[i] = headSplit[i].split("\\/")[0].replace("slash", "/");
				}
				
				excelHeaders =  headers;
			}
			
			// create a new Excel sheet
			sheet = workbook.createSheet(sheetName.get(multiIndex));
			sheet.setDefaultColumnWidth(30);
			
			styleHeader = setCellStyle(workbook, "HEADER", null);
			styleString = setCellStyle(workbook, "STRING", null);
			styleDefault = setCellStyle(workbook, "DEFAULT", null);
			styleNumber = setCellStyle(workbook, "NUMBER", null);
			styleFloatingPoint = setCellStyle(workbook, "FLOATING_POINT", null);
			styleTimestamp = setCellStyle(workbook, "TIMESTAMP", null);
			
			if (resultList != null && resultList.size() > 0) {
				headerTitleSet = resultList.get(0).keySet();

				// create header row
				Row headerRow = sheet.createRow(0);
				String titleArray[] = null;
				int i = 0;
				
				//동적으로 엑셀그리드를 만들어야 하는경우 AND excelHeaders가 있는 경우
				titleArray = new String[excelHeaders.length];
				Iterator itr = resultList.get(0).keySet().iterator();
				String key = "";
				for(String header : excelHeaders){
					if(itr.hasNext()){
						key = (String) itr.next();
						titleArray[i] = key;  //엑셀 데이터 로드를 위한 키값.
					}
					
					headerRow.createCell(i).setCellValue(header);
					headerRow.getCell(i).setCellStyle(styleHeader);
					i++;
				}
				
				// CREATE DATA ROWS
				createRow(resultList, titleArray, workbook);
				
				int lastCellNum = workbook.getSheetAt(0).getRow(0).getLastCellNum();
				for (int colNum = 0; colNum < lastCellNum; colNum++) {
					workbook.getSheetAt(0).autoSizeColumn(colNum);
				}
				
			} else {
				// create header row
				Row header = sheet.createRow(0);
				header.createCell(0);
				header.createCell(1);
				header.createCell(2);
				header.createCell(3);
				header.createCell(4);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
				
				if(headOptions.containsKey(HEAD_OPTIONS_EXCEL_ERROR_KEY)) {
					header.getCell(0).setCellValue(headOptions.get(HEAD_OPTIONS_EXCEL_ERROR_KEY) + "");
				} else {
					header.getCell(0).setCellValue("데이터가 없습니다.");
				}
				
				header.getCell(0).setCellStyle(styleHeader);
			}
		}
		
		Cookie cookie = new Cookie("excelDownToken", "TRUE");
		response.addCookie(cookie);
		
		logger.debug("multi sheet file write finished. XlsxBuilder");
	}
	
	private static void createRow(List<Map<String, Object>> resultList, String titleArray[], Workbook workbook) {
		int rowCount = 1;
		Row aRow = null;
		for (Map<String, Object> map : resultList) {
			aRow = sheet.createRow(rowCount++);
			
			for (int j = 0; j < titleArray.length; j++) {
				String s = "";
				Object o = map.get(titleArray[j]);
				
				if(isInteger(o)) {
					o = Integer.valueOf(String.valueOf(o));
				} else if(isDouble(o)) {
					o = Double.valueOf(String.valueOf(o));
				}
				
				if (o instanceof BigDecimal) {
					s = String.valueOf(o);
					if (!s.contains(".")) {
						Long l = Long.parseLong(s);
						aRow.createCell(j).setCellValue(l);
						aRow.getCell(j).setCellStyle(styleNumber);
					} else {
						Double d = Double.parseDouble(s);
						aRow.createCell(j).setCellValue(d);
						aRow.getCell(j).setCellStyle(styleFloatingPoint);
					}
				} else if (o instanceof Byte) {
					s = String.valueOf(o);
					Byte d = Byte.parseByte(s);
					aRow.createCell(j).setCellValue(d.toString());
					aRow.getCell(j).setCellStyle(styleFloatingPoint);
				} else if (o instanceof Short) {
					s = String.valueOf(o);
					Short d = Short.parseShort(s);
					aRow.createCell(j).setCellValue(d);
					aRow.getCell(j).setCellStyle(styleFloatingPoint);
				} else if (o instanceof Integer) {
					s = String.valueOf(o);
					Integer d = Integer.parseInt(s);
					aRow.createCell(j).setCellValue(d);
					aRow.getCell(j).setCellStyle(styleNumber);
				} else if (o instanceof Long) {
					s = String.valueOf(o);
					Long l = Long.parseLong(s);
					aRow.createCell(j).setCellValue(l);
					aRow.getCell(j).setCellStyle(styleNumber);
				} else if (o instanceof Float) {
					s = String.valueOf(o);
					Float f = Float.parseFloat(s);
					aRow.createCell(j).setCellValue(f);
					aRow.getCell(j).setCellStyle(styleFloatingPoint);
				} else if (o instanceof Double) {
					s = String.valueOf(o);
					Double d = Double.parseDouble(s);
					aRow.createCell(j).setCellValue(d);
					aRow.getCell(j).setCellStyle(styleFloatingPoint);
				} else if (o instanceof Timestamp) {
					s = sdf.format((Timestamp) o);
					aRow.createCell(j).setCellValue(s);
					aRow.getCell(j).setCellStyle(styleTimestamp);
				} else if (o instanceof String) {
					s = StringUtils.defaultString((String) o);
					
					if(s.indexOf('#') == 0) {	// 셀의 채우기로 RGB 색상 지정
						XSSFCell cell = (XSSFCell) aRow.createCell(j);
						cell.setCellValue(s);
						
						styleRgb = setCellStyle(workbook, "RGB", s);
						
						aRow.getCell(j).setCellStyle(styleRgb);
					} else if(validationDate(s)) {
						aRow.createCell(j).setCellValue(s);
						aRow.getCell(j).setCellStyle(styleTimestamp);
					} else {
						aRow.createCell(j).setCellValue(s);
						aRow.getCell(j).setCellStyle(styleString);
					}
				} else {
					s = StringUtils.defaultString((String) o);
					aRow.createCell(j).setCellValue(s);
					aRow.getCell(j).setCellStyle(styleDefault);
				}
			}
		}
	}
	
	private static boolean validationDate(String str) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			sdf.setLenient(false);
			sdf.parse(str);
			
			return true;
		} catch(ParseException ex) {
			return false;
		}
	}
	
	private static boolean isInteger(Object obj) {
		try {
			Integer.parseInt(obj + "");
			return true;
		} catch(NumberFormatException ex) {
			return false;
		}
	}
	
	private static boolean isDouble(Object obj) {
		try {
			Double.parseDouble(obj + "");
			return true;
		} catch(NumberFormatException ex) {
			return false;
		}
	}
	
	public static String clobToStr(CLOB clob) throws IOException, SQLException{
		BufferedReader contentReader = new BufferedReader(clob.getCharacterStream());
		StringBuffer out = new StringBuffer();
		String aux;
		
		while ((aux=contentReader.readLine())!=null) {
			out.append(aux);
			out.append("<br>");
		}
		
		return out.toString();
	}

	@SuppressWarnings("rawtypes")
	public static Object getValueOf(Object obj, String excelId) throws Exception {
		Field field = obj.getClass().getField(excelId);
		Class clazzType = field.getType();
		if (clazzType.toString().equals("double"))
			return field.getDouble(obj);
		else if (clazzType.toString().equals("int"))
			return field.getInt(obj);
		// else other type ...
		// and finally
		return field.get(obj);
	}

	private static DataType dataType;
    
    public static void setDataType(DataType dType) {
        dataType = dType;
    }

	private static CellStyle setCellStyle(Workbook workbook, String type, String value) {
		if(type.equals("HEADER")) {
			setDataType(DataType.HEADER);
		}else if(type.equals("NUMBER")) {
			setDataType(DataType.NUMBER);
		}else if(type.equals("FLOATING_POINT")) {
			setDataType(DataType.FLOATING_POINT);
		}else if(type.equals("RGB")) {
			setDataType(DataType.RGB);
		}else if(type.equals("TIMESTAMP")) {
			setDataType(DataType.TIMESTAMP);
		}else if(type.equals("STRING")) {
			setDataType(DataType.STRING);
		}else if(type.equals("DEFAULT")) {
			setDataType(DataType.DEFAULT);
		}
		
		short headerBackgroundColor = IndexedColors.GREY_25_PERCENT.getIndex();
		short blackColor = HSSFColor.HSSFColorPredefined.BLACK.getIndex();		
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		DataFormat format = workbook.createDataFormat();
		
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		font.setFontName("맑은 고딕");
		font.setColor(blackColor);
		
		switch(dataType) {
		case HEADER:
			cellStyle.setFillForegroundColor(headerBackgroundColor);
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			font.setBold(true);
			cellStyle.setFont(font);
			
			break;
		case NUMBER:
			cellStyle.setAlignment(HorizontalAlignment.RIGHT);
			font.setBold(false);
			cellStyle.setFont(font);
			
			break;
		case FLOATING_POINT:	// float, double 소수일경우
			cellStyle.setAlignment(HorizontalAlignment.RIGHT);
			cellStyle.setDataFormat(format.getFormat("#,##0.###############"));
			font.setBold(false);
			cellStyle.setFont(font);
			
			break;
		case RGB:
			int r = Integer.valueOf(value.substring(1, 3), 16);
			int g = Integer.valueOf(value.substring(3, 5), 16);
			int b = Integer.valueOf(value.substring(5, 7), 16);
			
			XSSFColor color = new XSSFColor(new java.awt.Color(r,g,b));
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			((XSSFCellStyle) cellStyle).setFillForegroundColor(color);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			font.setBold(false);
			cellStyle.setFont(font);
			
			break;
		case TIMESTAMP:
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			font.setBold(false);
			cellStyle.setFont(font);
			
			break;
		case STRING:
		case DEFAULT:
			font.setBold(false);
			cellStyle.setFont(font);
			
			break;
		default:
			break;
		}
		
		return cellStyle;
	}

}