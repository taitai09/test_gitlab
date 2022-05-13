package omc.spop.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
//import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;
import oracle.sql.CLOB;

public class XlsxBuilder extends AbstractXlsxView {

	private static final Logger logger = LoggerFactory.getLogger(XlsxBuilder.class);

	/*
	protected void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		buildDocument(model, workbook, request, response);
	}
	*/
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		buildDocument(model, (XSSFWorkbook) workbook, request, response);

	}

	@SuppressWarnings("null")
	@Value("#{defaultConfig['customer']}")
	private String customer;
	public void buildDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.debug(this.getClass().getName() + ".buildDocument started");
		String[] excelHeaders = (String[]) model.get("excelHeaders");
		String excelId = StringUtils.defaultString((String) model.get("excelId"));
		logger.debug("excelId::" + excelId);
		String sheetName = (String) model.get("sheetName");
		logger.debug("sheetName::" + sheetName);

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

		// create a new Excel sheet
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(30);

		CellStyle styleHeader = setCellStyle(workbook, "HEADER", null);
		CellStyle styleString = setCellStyle(workbook, "STRING", null);
		CellStyle styleDefault = setCellStyle(workbook, "DEFAULT", null);
		CellStyle styleNumber = setCellStyle(workbook, "NUMBER", null);
		CellStyle styleFloatingPoint = setCellStyle(workbook, "FLOATING_POINT", null);
		CellStyle styleTimestamp = setCellStyle(workbook, "TIMESTAMP", null);
		CellStyle styleRgb;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Set<String> headerTitleSet = null;
		if (resultList != null && resultList.size() > 0) {
			headerTitleSet = resultList.get(0).keySet();
			int cellCnt = headerTitleSet.size();

			// create header row
			Row headerRow0 = sheet.createRow(0);
			Iterator<String> titles = headerTitleSet.iterator();
			String titleArray[] = new String[cellCnt];
			int i = 0;
			boolean dynamicFlag = false;
			String word = "점검";

			// Controller에 excelId를 지정한 경우
			if (!excelId.equals("")) {
				if (excelId.endsWith("_ESPC")) {
					word = "검증";
				}
				
				if (excelId.equals("PERF_CHECK_STATE0") || excelId.equals("PERF_CHECK_STATE0_ESPC")) {
					//배포전 성능점검>성능 점검 현황>CM 점검 현황
					String column = "";
					int titleIndex = 0;
					while (titles.hasNext()) {
						column = (String) titles.next();
						titleArray[titleIndex++] = column;
					}
					
					Row headerRow1 = sheet.createRow(1);
					for (int j = 0; j < cellCnt; j++) {
						headerRow0.createCell(j);
						headerRow0.getCell(j).setCellStyle(styleHeader);
						headerRow1.createCell(j);
						headerRow1.getCell(j).setCellStyle(styleHeader);
					}

					headerRow0.getCell(0).setCellValue("업무");
					sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

					headerRow0.getCell(1).setCellValue("요청");
					sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));

					Cell cell2 = headerRow0.getCell(2);
					cell2.setCellValue("완료");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));

					Cell cell5 = headerRow0.getCell(5);
					cell5.setCellValue("미완료");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 9));

					headerRow1.getCell(2).setCellValue("소계");
					headerRow1.getCell(3).setCellValue("적합");
					headerRow1.getCell(4).setCellValue("부적합");
					headerRow1.getCell(5).setCellValue("소계");
					headerRow1.getCell(6).setCellValue("개발확정");
					headerRow1.getCell(7).setCellValue(word+"중");
					headerRow1.getCell(8).setCellValue("개발확정취소");
					headerRow1.getCell(9).setCellValue("배포삭제");

				} else if (excelId.equals("PERF_CHECK_STATE1") || excelId.equals("PERF_CHECK_STATE1_ESPC") ) {
					//배포전 성능점검>성능 점검 현황>프로그램 점검 현황
					String column = "";
					int titleIndex = 0;
					while (titles.hasNext()) {
						column = (String) titles.next();
						titleArray[titleIndex++] = column;
					}
					
					Row headerRow1 = sheet.createRow(1);
					for (int j = 0; j < cellCnt; j++) {
						headerRow0.createCell(j);
						headerRow0.getCell(j).setCellStyle(styleHeader);
						headerRow1.createCell(j);
						headerRow1.getCell(j).setCellStyle(styleHeader);
					}

					headerRow0.getCell(0).setCellValue("업무");
					sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

					headerRow0.getCell(1).setCellValue("전체");
					sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));

					Cell cell2 = headerRow0.getCell(2);
					cell2.setCellValue(word + "결과");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));

					Cell cell7 = headerRow0.getCell(7);
					cell7.setCellValue("개발구분");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 9));

					headerRow1.getCell(2).setCellValue("적합");
					headerRow1.getCell(3).setCellValue("부적합");
					headerRow1.getCell(4).setCellValue("오류");
					headerRow1.getCell(5).setCellValue("미수행");
					headerRow1.getCell(6).setCellValue(word + "제외");
					headerRow1.getCell(7).setCellValue("신규");
					headerRow1.getCell(8).setCellValue("변경");
					headerRow1.getCell(9).setCellValue("동일");
				} else if ( excelId.equals("PERFORMANCE_IMPROVEMENT_REPORT") ) {
					//성능 최적화 > 성능 개선 실적 > 성능개선현황 보고서
					String column = "";
					int titleIndex = 0;
					while (titles.hasNext()) {
						column = (String) titles.next();
						titleArray[titleIndex++] = column;
					}
					
					Row headerRow1 = sheet.createRow(1);
					Row headerRow2 = sheet.createRow(2);
					Row headerRow3 = sheet.createRow(3);
					for (int j = 0; j < cellCnt; j++) {
						headerRow0.createCell(j);
						headerRow0.getCell(j).setCellStyle(styleHeader);
						headerRow1.createCell(j);
						headerRow1.getCell(j).setCellStyle(styleHeader);
						headerRow2.createCell(j);
						headerRow2.getCell(j).setCellStyle(styleHeader);
						headerRow3.createCell(j);
						headerRow3.getCell(j).setCellStyle(styleHeader);
					}

					Cell cell0 = headerRow0.getCell(0);
					cell0.setCellValue("구분");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
					
					Cell cell2 = headerRow0.getCell(2);
					cell2.setCellValue("개선대상");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));
					
					Cell cell7 = headerRow0.getCell(7);
					cell7.setCellValue("처리단계");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 14));
					
					headerRow1.getCell(0).setCellValue("DB");
					sheet.addMergedRegion(new CellRangeAddress(1, 3, 0, 0));
					
					headerRow1.getCell(1).setCellValue("업무");
					sheet.addMergedRegion(new CellRangeAddress(1, 3, 1, 1));
					
					Cell cell2_2 = headerRow1.getCell(2);
					cell2_2.setCellValue("요청");
					sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
					
					Cell cell2_5 = headerRow1.getCell(4);
					cell2_5.setCellValue("선정");
					sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
					
					headerRow1.getCell(6).setCellValue("총계");
					sheet.addMergedRegion(new CellRangeAddress(1, 3, 6, 6));
					
					Cell cell2_7 = headerRow1.getCell(7);
					cell2_7.setCellValue("성능관리팀");
					sheet.addMergedRegion(new CellRangeAddress(1, 1, 7, 10));
					
					Cell cell2_11 = headerRow1.getCell(11);
					cell2_11.setCellValue("업무개발팀");
					sheet.addMergedRegion(new CellRangeAddress(1, 1, 11, 14));
					
					headerRow2.getCell(2).setCellValue("전일누적");
					sheet.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));
					
					headerRow2.getCell(3).setCellValue("당일추가");
					sheet.addMergedRegion(new CellRangeAddress(2, 3, 3, 3));
					
					headerRow2.getCell(4).setCellValue("전일누적");
					sheet.addMergedRegion(new CellRangeAddress(2, 3, 4, 4));
					
					headerRow2.getCell(5).setCellValue("당일추가");
					sheet.addMergedRegion(new CellRangeAddress(2, 3, 5, 5));
					
					headerRow2.getCell(7).setCellValue("처리중");
					sheet.addMergedRegion(new CellRangeAddress(2, 3, 7, 7));
					
					Cell cell3_8 = headerRow2.getCell(8);
					cell3_8.setCellValue("처리완료");
					sheet.addMergedRegion(new CellRangeAddress(2, 2, 8, 10));
					
					headerRow2.getCell(11).setCellValue("처리중");
					sheet.addMergedRegion(new CellRangeAddress(2, 3, 11, 11));
					
					Cell cell3_12 = headerRow2.getCell(12);
					cell3_12.setCellValue("처리완료");
					sheet.addMergedRegion(new CellRangeAddress(2, 2, 12, 14));
					
					headerRow3.getCell(8).setCellValue("개선완료");
					headerRow3.getCell(9).setCellValue("개선사항없음");
					headerRow3.getCell(10).setCellValue("오류");
					headerRow3.getCell(12).setCellValue("튜닝반려");
					headerRow3.getCell(13).setCellValue("적용완료");
					headerRow3.getCell(14).setCellValue("적용반려");
					
					sheet.setColumnWidth(0, 5000);
					sheet.setColumnWidth(1, 5000);
					sheet.setColumnWidth(2, 3000);
					sheet.setColumnWidth(3, 3000);
					sheet.setColumnWidth(4, 3000);
					sheet.setColumnWidth(5, 3000);
					sheet.setColumnWidth(6, 1500);
					sheet.setColumnWidth(7, 2000);
					sheet.setColumnWidth(8, 3000);
					sheet.setColumnWidth(9, 4000);
					sheet.setColumnWidth(10, 3000);
					sheet.setColumnWidth(11, 2000);
					sheet.setColumnWidth(12, 1500);
					sheet.setColumnWidth(13, 3000);
					sheet.setColumnWidth(14, 3000);
					
				} else {
					ExcelDownHeaders headers = new ExcelDownHeaders();
					String[] excel_headers_en = (String[]) getValueOf(headers, excelId + "_EN");
					String[] excel_headers_ko = (String[]) getValueOf(headers, excelId + "_KO");
					titleArray = new String[excel_headers_en.length];
					for (int j = 0; j < excel_headers_en.length; j++) {
						titles = headerTitleSet.iterator();
						String excel_header_en = excel_headers_en[j];
						String column = "";
						String header = "";
						while (titles.hasNext()) {
							column = (String) titles.next();
							if (column.equals(excel_header_en)) {
								header = excel_headers_ko[j];
								break;
							}
						}
						titleArray[i] = column;
						headerRow0.createCell(i).setCellValue(header);
						headerRow0.getCell(i).setCellStyle(styleHeader);
						i++;
					}
				}

				// 동적으로 엑셀그리드를 만들어야 하는경우 AND excelHeaders가 있는 경우
			} else if (excelHeaders != null && excelHeaders.length > 0 && excelId.equals("")) {
				titleArray = new String[excelHeaders.length];
				Iterator itr = resultList.get(0).keySet().iterator();
				String key = "";
				dynamicFlag = true;
				for (String header : excelHeaders) {
					if (itr.hasNext()) {
						key = (String) itr.next();
						titleArray[i] = key; // 엑셀 데이터 로드를 위한 키값.
					}
					headerRow0.createCell(i).setCellValue(header);
					headerRow0.getCell(i).setCellStyle(styleHeader);
					i++;
				}

				// Controller에 excelId 지정하지 않은 경우
			} else {
				titleArray = new String[headerTitleSet.size()];
				while (titles.hasNext()) {
					String title = (String) titles.next();
					titleArray[i] = title;
					headerRow0.createCell(i).setCellValue(title);
					headerRow0.getCell(i).setCellStyle(styleHeader);
					i++;
				}
			}

			int lastRowNum = workbook.getSheetAt(0).getLastRowNum();
			logger.debug("lastRowNum :" + lastRowNum);
//			System.out.println("lastRowNum :" + lastRowNum);

			// CREATE DATA ROWS
			int rowCount = lastRowNum + 1;
			Row aRow = null;
			for (Map<String, Object> map : resultList) {
				aRow = sheet.createRow(rowCount++);

				for (int j = 0; j < titleArray.length; j++) {
					String s = "";
					Object o = map.get(titleArray[j]);

					if (dynamicFlag) {
						if (isInteger(o)) {
							o = Integer.valueOf(String.valueOf(o));
						} else if (isDouble(o)) {
							o = Double.valueOf(String.valueOf(o));
						}
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

						if (s.indexOf('#') == 0) { // 셀의 채우기로 RGB 색상 지정

							XSSFCell cell = (XSSFCell) aRow.createCell(j);
							cell.setCellValue(s);
							if (s.length() == 7) {
								try {
									int r = Integer.valueOf(s.substring(1, 3), 16);
									int g = Integer.valueOf(s.substring(3, 5), 16);
									int b = Integer.valueOf(s.substring(5, 7), 16);

									styleRgb = setCellStyle(workbook, "RGB", s);
									aRow.getCell(j).setCellStyle(styleRgb);
								} catch (Exception e) {
//									System.out.println("This string is not color table");
									logger.error("==== This string is not color table =====");
								}
							}
						} else if (validationDate(s)) {
							s = s.trim();
							aRow.createCell(j).setCellValue(s);
							aRow.getCell(j).setCellStyle(styleTimestamp);
						} else {
							s = s.trim();
							aRow.createCell(j).setCellValue(maximumLengthCheck(s));
							aRow.getCell(j).setCellStyle(styleString);
						}
					} else if ( o instanceof CLOB ) {
						StringBuilder sb = new StringBuilder();
						Reader reader = ((CLOB) o).getCharacterStream();
						BufferedReader br = new BufferedReader( reader );
						
						String line;
						while(null != (line = br.readLine())) {
							line = removeHTML( line );
							line = line.replaceAll("\r\n","  ");
							line = line.replaceAll("\n"," ");
							line = removeSpecialChar( line );
//							line = StringEscapeUtils.unescapeHtml( line );
							sb.append(line).append("\n");
						}
						br.close();
//						logger.debug("CLOB ======================> "+ sb.toString() );
						
						s = StringUtils.defaultString( sb.toString() );
						s = s.trim();
						aRow.createCell(j).setCellValue(maximumLengthCheck(s));
						aRow.getCell(j).setCellStyle(styleDefault);
					} else {
						s = StringUtils.defaultString((String) o);
						aRow.createCell(j).setCellValue(s);
						aRow.getCell(j).setCellStyle(styleDefault);
					}
				}
			}
			int lastCellNum = workbook.getSheetAt(0).getRow(0).getLastCellNum();
			
			// Column auto size 
			if ( excelId.equals("PERFORMANCE_IMPROVEMENT_REPORT") == false ) {
				// (성능개선현황 보고서 제외 )
				for (int colNum = 0; colNum < lastCellNum; colNum++) {
					workbook.getSheetAt(0).autoSizeColumn(colNum);
				}
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
			header.getCell(0).setCellStyle(styleHeader);

		}

		Cookie cookie = new Cookie("excelDownToken", "TRUE");
		response.addCookie(cookie);

		logger.debug("file write finished. XlsxBuilder");
	}

	private static String maximumLengthCheck(String text) {
		int MAXIMUM_LENGTH = 32767;
		
		if (text.length() > MAXIMUM_LENGTH) {
			text = text.substring(0, MAXIMUM_LENGTH);
		}
		
		return text;
	}

	private static boolean validationDate(String str) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			sdf.setLenient(false);
			sdf.parse(str);

			return true;
		} catch (ParseException ex) {
			return false;
		}
	}

	private static boolean isInteger(Object obj) {
		try {
			Integer.parseInt(obj + "");
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private static boolean isDouble(Object obj) {
		try {
			Double.parseDouble(obj + "");
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static String clobToStr(CLOB clob) throws IOException, SQLException {
		BufferedReader contentReader = new BufferedReader(clob.getCharacterStream());
		StringBuffer out = new StringBuffer();
		String aux;

		while ((aux = contentReader.readLine()) != null) {
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

	DataType dataType;

	public void setDataType(DataType dType) {
		dataType = dType;
	}

	private CellStyle setCellStyle(Workbook workbook, String type, String value) {
		if (type.equals("HEADER")) {
			setDataType(DataType.HEADER);
		} else if (type.equals("NUMBER")) {
			setDataType(DataType.NUMBER);
		} else if (type.equals("FLOATING_POINT")) {
			setDataType(DataType.FLOATING_POINT);
		} else if (type.equals("RGB")) {
			setDataType(DataType.RGB);
		} else if (type.equals("TIMESTAMP")) {
			setDataType(DataType.TIMESTAMP);
		} else if (type.equals("STRING")) {
			setDataType(DataType.STRING);
		} else if (type.equals("DEFAULT")) {
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

		switch (dataType) {
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
		case FLOATING_POINT: // float, double 소수일경우
			cellStyle.setAlignment(HorizontalAlignment.RIGHT);
			cellStyle.setDataFormat(format.getFormat("#,##0.###############"));
			font.setBold(false);
			cellStyle.setFont(font);

			break;
		case RGB:
			int r = Integer.valueOf(value.substring(1, 3), 16);
			int g = Integer.valueOf(value.substring(3, 5), 16);
			int b = Integer.valueOf(value.substring(5, 7), 16);

			XSSFColor color = new XSSFColor(new java.awt.Color(r, g, b));
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
	/**
	* HTML 테크 제거후 문자열 리턴
	*
	* @param strText  HTML이 포함된 문자열
	* @return 문자열
	*/
	private String removeHTML(String strText) {
		String returnValue = strText;
		
		if ( strText != null && "".equals( strText ) == false ) {
			returnValue = returnValue.replaceAll("\\s"," ");
			returnValue = returnValue.replaceAll("&nbsp;"," ");
			returnValue = returnValue.replaceAll("&#39;","'");	
			returnValue = returnValue.replaceAll("<>","@@");
			returnValue = returnValue.replaceAll("(<br>)|(<br\\/>)|(<br \\/>)","\n");
			returnValue = returnValue.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>","");
			returnValue = returnValue.replaceAll("@@","<>");
			returnValue = returnValue.replaceAll("\n","\r\n");
		}
		
		return returnValue;
	}
	
	
	/**
	 * 특수문자 제거
	 *
	 * @param strText  특수문자가 포함된 문자열
	 * @return 문자열
	 */
	private String removeSpecialChar(String strText) {
		String returnValue = strText;
		
		if ( strText != null && "".equals( strText ) == false ) {
			returnValue = returnValue.replaceAll("(<P>)|(</P>)","");
			returnValue = returnValue.replaceAll("&lt;","<");
			returnValue = returnValue.replaceAll("&gt;",">");
			returnValue = returnValue.replaceAll("&amp;","&");
			returnValue = returnValue.replaceAll("&quot;","\"");
			returnValue = returnValue.replaceAll("&#035;","#");
		}
		
		return returnValue;
	}
}