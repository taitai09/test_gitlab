package omc.spop.view;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.maven.artifact.ant.shaded.StringUtils;
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

public class XlsxBuilderTest extends AbstractXlsxView {
	private static final Logger logger = LoggerFactory.getLogger(XlsxBuilderTest.class);

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug(this.getClass().getName() + ".buildExcelDocument started");
		buildDocument(model, (XSSFWorkbook) workbook, request, response);
	}

	public void buildDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.debug(this.getClass().getName() + ".buildDocument started");
		String[] excelHeaders = (String[]) model.get("excelHeaders");
		
		String excelId = StringUtils.defaultString((String) model.get("excelId"));
		String sheetName = (String) model.get("sheetName");
		String fileName = StringUtils.defaultString((String) model.get("fileName"), sheetName);
		String toDate = DateUtil.getNowDate("yyyyMMddhhmmssSSS");
		String sFileName = URLEncoder.encode(fileName + "_" + toDate + ".xlsx", "UTF-8");

		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		// response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + sFileName + ";");

		// get data model which is passed by the Spring container
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) model.get("resultList");
		logger.debug("#################### RESULTLIST.SIZE::" + resultList.size()+"####################");

		DataFormat format = workbook.createDataFormat();
		// create a new Excel sheet
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(30);

		short headerBackgroundColor = IndexedColors.GREY_25_PERCENT.getIndex();
		short whiteColor = HSSFColor.HSSFColorPredefined.WHITE.getIndex();
		short blackColor = HSSFColor.HSSFColorPredefined.BLACK.getIndex();		
//		XSSFColor whiteColor = new XSSFColor(new java.awt.Color(255, 255, 255)); // #FFFFFF
//		XSSFColor blackColor = new XSSFColor(new java.awt.Color(0, 0, 0)); // #000000
//#################################################################################################################
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

		Font font3 = workbook.createFont();
		font3.setFontName("맑은 고딕");
		font3.setBold(false);
		font3.setColor(blackColor);
		style3.setFont(font3);

		// float, double 소수일경우
		CellStyle style4 = workbook.createCellStyle();
		style4.cloneStyleFrom(style3);
		style4.setDataFormat(format.getFormat("#,##0.###############"));
//#################################################################################################################
		
		
		Set<String> headerTitleSet = null;
		if (resultList != null && resultList.size() > 0) {
			headerTitleSet = resultList.get(0).keySet();

			// CREATE HEADER ROW
			Row headerRow = sheet.createRow(0);
			Iterator titles = headerTitleSet.iterator();
			String titleArray[] = null;
			int i = 0;
			
			// Controller에 excelId를 지정했을 경우
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
						logger.debug("########## excel_header_en :"+excel_header_en +" column :"+column);
						logger.debug("########## 동일여부:::"+column.equals(excel_header_en));
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
				
			//동적으로 엑셀그리드를 만들어야 하는경우 AND listForHeader가 있는 경우
			} else if(excelHeaders != null && excelHeaders.length > 0 && excelId.equals("")){
				for(String header : excelHeaders){
					System.out.println("######## EXCEL HEADER MAKING ##########"+ header);
					headerRow.createCell(i).setCellValue(header);
					headerRow.getCell(i).setCellStyle(style1);
				}
				
			// Controller에 excelId 지정하지 않은 경우
			} else {
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
			Row aRow = null;

			for (Map<String, Object> map : resultList) {
				aRow = sheet.createRow(rowCount++);
				
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
						
						if(s.indexOf('#') == 0) {	// 셀의 채우기로 RGB 색상 지정
							int r = Integer.valueOf(s.substring(1, 3), 16);
							int g = Integer.valueOf(s.substring(3, 5), 16);
							int b = Integer.valueOf(s.substring(5, 7), 16);
							
							XSSFColor color = new XSSFColor(new java.awt.Color(r,g,b));
							XSSFCell cell = (XSSFCell) aRow.createCell(j);
							CellStyle style = workbook.createCellStyle();
							
							cell.setCellValue(s);
							style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							((XSSFCellStyle) style).setFillForegroundColor(color);
							aRow.getCell(j).setCellStyle(style);
						} else {
							aRow.createCell(j).setCellValue(s);
							aRow.getCell(j).setCellStyle(contentLeftStyle);
						}
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
			
		//데이터가 없을 경우
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
	public static Object getValueOf(Object obj, String excelId) throws Exception {
		Field field = obj.getClass().getField(excelId);
		Class type = field.getType();
		System.out.println("#########type#########"+type.toString());
	/*	if (type.toString().equals("double"))
			return field.getDouble(obj);
		else if (type.toString().equals("int"))
			return field.getInt(obj);*/
		// else other type ...
		// and finally
		return field.get(obj);
	}

}