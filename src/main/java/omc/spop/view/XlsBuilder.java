package omc.spop.view;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import omc.spop.utils.DateUtil;

/***********************************************************
 * 2021.09.15	황예지	현재 사용하지 않는 클래스로 확인
 **********************************************************/

public class XlsBuilder extends AbstractXlsView {
	private static final Logger logger = LoggerFactory.getLogger(XlsBuilder.class);

	// @Override
	// protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook
	// workbook, HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// buildDocument(model, workbook, request, response);
	// }

	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		buildDocument(model, (HSSFWorkbook) workbook, request, response);
	}

	public void buildDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		logger.debug(this.getClass().getName() + ".buildDocument started");

		String excelId = StringUtils.defaultString((String) model.get("excelId"));
		logger.debug("excelId::" + excelId);
		String sheetName = (String) model.get("sheetName");
		logger.debug("sheetName::" + sheetName);

		String fileName = StringUtils.defaultString((String) model.get("fileName"), sheetName);

		String toDate = DateUtil.getNowDate("yyyyMMddhhmmssSSS");
		String sFileName = URLEncoder.encode(fileName + "_" + toDate + ".xls", "UTF-8");

		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		// response.setHeader("Content-Type", "application/octet-stream;");
		// response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setContentType("application/octet-stream; charset=utf-8");
		// response.setContentType("application/smnet; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + sFileName + ";");

		// get data model which is passed by the Spring container
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) model.get("resultList");
		logger.debug("resultList.size::" + resultList.size());

		DataFormat format = workbook.createDataFormat();
		// create a new Excel sheet
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(30);

		short headerBackgroundColor = HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex();
		short whiteColor = HSSFColor.HSSFColorPredefined.WHITE.getIndex();
		short blackColor = HSSFColor.HSSFColorPredefined.BLACK.getIndex();
//		HSSFColor whiteColor = new HSSFColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex(),
//				HSSFColor.HSSFColorPredefined.WHITE.getIndex(), new java.awt.Color(255, 255, 255)); // #FFFFFF
//		HSSFColor blackColor = new HSSFColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex(),
//				HSSFColor.HSSFColorPredefined.BLACK.getIndex(), new java.awt.Color(0, 0, 0)); // #000000

		// header style
		CellStyle style1 = workbook.createCellStyle();
		style1.setFillForegroundColor(headerBackgroundColor);
		style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style1.setAlignment(HorizontalAlignment.CENTER);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderTop(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);

		Font font1 = workbook.createFont();
		font1.setFontName("맑은 고딕");
		font1.setBold(true);
		font1.setColor(blackColor);
		style1.setFont(font1);

		// content String style
		CellStyle contentLeftStyle = workbook.createCellStyle();
		contentLeftStyle.setBorderBottom(BorderStyle.THIN);
		contentLeftStyle.setBorderTop(BorderStyle.THIN);
		contentLeftStyle.setBorderRight(BorderStyle.THIN);
		contentLeftStyle.setBorderLeft(BorderStyle.THIN);

		Font contentLeftfont = workbook.createFont();
		contentLeftfont.setFontName("맑은 고딕");
		contentLeftfont.setBold(false);
		contentLeftfont.setColor(blackColor);
		contentLeftStyle.setFont(contentLeftfont);

		CellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);

		Font font2 = workbook.createFont();
		font2.setFontName("맑은 고딕");
		font2.setBold(false);
		font2.setColor(blackColor);
		style2.setFont(font2);

		// content number style
		CellStyle style3 = workbook.createCellStyle();
		style3.setBorderBottom(BorderStyle.THIN);
		style3.setBorderTop(BorderStyle.THIN);
		style3.setBorderRight(BorderStyle.THIN);
		style3.setBorderLeft(BorderStyle.THIN);
		style3.setAlignment(HorizontalAlignment.RIGHT);
		// custom number format
		// style3.setDataFormat(format.getFormat("#.###############"));
		// builtin currency format
		style3.setDataFormat((short) 0x7); // return $1,458.11
		style3.setDataFormat((short) 8); // 8 = "($#,##0.00_);[Red]($#,##0.00)"
		CreationHelper ch = workbook.getCreationHelper();
		style3.setDataFormat(ch.createDataFormat().getFormat("#,##0;\\-#,##0"));

		Font font3 = workbook.createFont();
		font3.setFontName("맑은 고딕");
		font3.setBold(false);
		font3.setColor(blackColor);
		style3.setFont(font3);

		// float, double 소수일경우
		CellStyle style4 = workbook.createCellStyle();
		style4.cloneStyleFrom(style3);
		style4.setDataFormat(format.getFormat("#,##0.###############"));

		Set<String> headerTitleSet = null;
		if (resultList != null && resultList.size() > 0) {
			headerTitleSet = resultList.get(0).keySet();

			// create header row
			Row headerRow = sheet.createRow(0);
			Iterator titles = headerTitleSet.iterator();
			String titleArray[];
			int i = 0;
			// excelId가 있으면...
			if (!excelId.equals("")) {
				ExcelDownHeaders headers = new ExcelDownHeaders();
				String[] excel_headers_en = (String[]) getValueOf(headers, excelId + "_EN");
				String[] excel_headers_ko = (String[]) getValueOf(headers, excelId + "_KO");
				logger.debug("excel_headers_en:" + excel_headers_en);
				logger.debug("excel_headers_ko:" + excel_headers_ko);
				titleArray = new String[excel_headers_en.length];
				for (int j = 0; j < excel_headers_en.length; j++) {
					titles = headerTitleSet.iterator();
					String excel_header_en = excel_headers_en[j];
					logger.debug("excel_header_en :"+excel_header_en);
					String column = "";
					String header = "";
					while (titles.hasNext()) {
						column = (String) titles.next();
						logger.debug("excel_header_en :"+excel_header_en +" column :"+column);
						logger.debug("같냐?"+column.equals(excel_header_en));
						if (column.equals(excel_header_en)) {
							header = excel_headers_ko[j];
							logger.debug("header:" + header);
							break;
						}
					}
					titleArray[i] = column;
					headerRow.createCell(i).setCellValue(header);
					headerRow.getCell(i).setCellStyle(style1);
					i++;
				}
			} else {
				// excelId가 없으면...
				titleArray  = new String[headerTitleSet.size()];
				while (titles.hasNext()) {
					String title = (String) titles.next();
					logger.debug("title:"+title);
					titleArray[i] = title;
					headerRow.createCell(i).setCellValue(title);
					headerRow.getCell(i).setCellStyle(style1);
					i++;
				}
			}

			// create data rows
			int rowCount = 1;

			for (Map<String, Object> map : resultList) {
				Row aRow = sheet.createRow(rowCount++);
				for (int j = 0; j < titleArray.length; j++) {
					String s = "";
					Object o = map.get(titleArray[j]);
					if (o instanceof BigDecimal) {
						s = String.valueOf(o);
						logger.debug("instance of BigDecimal:" + s);
						if (!s.contains(".")) {
							Long l = Long.parseLong(s);
							aRow.createCell(j).setCellValue(l);
							aRow.getCell(j).setCellStyle(style3);
						} else {
							Double d = Double.parseDouble(s);
							aRow.createCell(j).setCellValue(d);
							aRow.getCell(j).setCellStyle(style4);
						}
					} else if (o instanceof Byte) {
						s = String.valueOf(o);
						logger.debug("instance of Byte:" + s);
						Byte d = Byte.parseByte(s);
						aRow.createCell(j).setCellValue(d.toString());
						aRow.getCell(j).setCellStyle(style4);
					} else if (o instanceof Short) {
						s = String.valueOf(o);
						logger.debug("instance of Short:" + s);
						Short d = Short.parseShort(s);
						aRow.createCell(j).setCellValue(d);
						aRow.getCell(j).setCellStyle(style4);
					} else if (o instanceof Integer) {
						s = String.valueOf(o);
						logger.debug("instance of Integer:" + s);
						Integer d = Integer.parseInt(s);
						aRow.createCell(j).setCellValue(d);
						aRow.getCell(j).setCellStyle(style4);
					} else if (o instanceof Long) {
						s = String.valueOf(o);
						logger.debug("instance of Long:" + s);
						Long l = Long.parseLong(s);
						aRow.createCell(j).setCellValue(l);
						aRow.getCell(j).setCellStyle(style3);
					} else if (o instanceof Float) {
						s = String.valueOf(o);
						logger.debug("instance of Float:" + s);
						Float f = Float.parseFloat(s);
						aRow.createCell(j).setCellValue(f);
						aRow.getCell(j).setCellStyle(style4);
					} else if (o instanceof Double) {
						s = String.valueOf(o);
						logger.debug("instance of Double:" + s);
						Double d = Double.parseDouble(s);
						aRow.createCell(j).setCellValue(d);
						aRow.getCell(j).setCellStyle(style4);
					} else if (o instanceof String) {
						s = StringUtils.defaultString((String) o);
						logger.debug("instance of String:" + s);
						aRow.createCell(j).setCellValue(s);
						aRow.getCell(j).setCellStyle(contentLeftStyle);
					} else {
						s = StringUtils.defaultString((String) o);
						logger.debug("instance of others:" + s);
						aRow.createCell(j).setCellValue(s);
						aRow.getCell(j).setCellStyle(style2);
					}
				}
			}
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
			header.getCell(0).setCellValue("데이터가 없습니다.");
			header.getCell(0).setCellStyle(style1);

		}
		logger.debug("file write finished");
	}

	@SuppressWarnings("rawtypes")
	public static Object getValueOf(Object clazz, String lookingForValue) throws Exception {
		Field field = clazz.getClass().getField(lookingForValue);
		Class clazzType = field.getType();
		if (clazzType.toString().equals("double"))
			return field.getDouble(clazz);
		else if (clazzType.toString().equals("int"))
			return field.getInt(clazz);
		// else other type ...
		// and finally
		return field.get(clazz);
	}

}