package omc.spop.view;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import omc.spop.utils.DateUtil;

/**
 * This class builds an Excel spreadsheet document using Apache POI library.
 * 
 * @author www.codejava.net Open Declaration
 *         org.springframework.web.servlet.view.document.AbstractExcelView
 * 
 * 
 * @Deprecated
 * 
 * 
 * 			Deprecated. as of Spring 4.2, in favor of AbstractXlsView and its
 *             AbstractXlsxView and AbstractXlsxStreamingView variants
 * 
 *             Convenient superclass for Excel document views. Compatible with
 *             Apache POI 3.5 and higher, as of Spring 4.0.
 * 
 *             Properties:
 * 
 *             url (optional): The url of an existing Excel document to pick as
 *             a starting point. It is done without localization part nor the
 *             ".xls" extension. The file will be searched with locations in the
 *             following order:
 * 
 *             [url]_[language]_[country].xls [url]_[language].xls [url].xls For
 *             working with the workbook in the subclass, see Apache's POI site
 * 
 *             As an example, you can try this snippet:
 * 
 *             protected void buildExcelDocument( Map<String, Object> model,
 *             HSSFWorkbook workbook, HttpServletRequest request,
 *             HttpServletResponse response) {
 * 
 *             // Go to the first sheet. // getSheetAt: only if workbook is
 *             created from an existing document // HSSFSheet sheet =
 *             workbook.getSheetAt(0); HSSFSheet sheet =
 *             workbook.createSheet("Spring"); sheet.setDefaultColumnWidth(12);
 * 
 *             // Write a text at A1. HSSFCell cell = getCell(sheet, 0, 0);
 *             setText(cell, "Spring POI test");
 * 
 *             // Write the current date at A2. HSSFCellStyle dateStyle =
 *             workbook.createCellStyle();
 *             dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
 *             cell = getCell(sheet, 1, 0); cell.setCellValue(new Date());
 *             cell.setCellStyle(dateStyle);
 * 
 *             // Write a number at A3 getCell(sheet, 2, 0).setCellValue(458);
 * 
 *             // Write a range of numbers. HSSFRow sheetRow =
 *             sheet.createRow(3); for (short i = 0; i < 10; i++) {
 *             sheetRow.createCell(i).setCellValue(i * 10); } } This class is
 *             similar to the AbstractPdfView class in usage style1.
 *             org.springframework.web.servlet.view.document.AbstractXlsView
 */
@SuppressWarnings("deprecation")
public class ExcelBuilder extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug(this.getClass().getName() + ".buildExcelDocument started");

		String sheetName = (String) model.get("sheetName");
		logger.debug("sheetName::" + sheetName);

		String fileName = StringUtils.defaultString((String) model.get("fileName"),sheetName);

		String toDate = DateUtil.getNowDate("yyyyMMddhhmmssSSS");
		String sFileName = URLEncoder.encode(fileName + "_" + toDate + ".xls", "UTF-8");

		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		//response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setContentType("application/smnet; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + sFileName + ";");

		// get data model which is passed by the Spring container
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> resultList = (List<Map<String, Object>>) model.get("resultList");
		logger.debug("resultList.size::" + resultList.size());

		DataFormat format = workbook.createDataFormat();
		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet(sheetName);
		sheet.setDefaultColumnWidth(30);

		// header style
		CellStyle style1 = workbook.createCellStyle();
		style1.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style1.setAlignment(HorizontalAlignment.CENTER);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderTop(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);

		Font font1 = workbook.createFont();
		font1.setFontName("맑은 고딕");
		font1.setBold(true);
		font1.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		style1.setFont(font1);

		// content String style
		CellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);

		Font font2 = workbook.createFont();
		font2.setFontName("맑은 고딕");
		font2.setBold(false);
		font2.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
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
		font3.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
		style3.setFont(font3);

		// float, double 소수일경우
		CellStyle style4 = workbook.createCellStyle();
		style4.cloneStyleFrom(style3);
		style4.setDataFormat(format.getFormat("#,##0.###############"));

		Set<String> headerTitleSet = null;
		if (resultList != null && resultList.size() > 0) {
			headerTitleSet = resultList.get(0).keySet();

			// create header row
			HSSFRow header = sheet.createRow(0);
			Iterator titles = headerTitleSet.iterator();
			String titleArray[] = new String[headerTitleSet.size()];
			int i = 0;
			while (titles.hasNext()) {
				String title = (String) titles.next();
				titleArray[i] = title;
				header.createCell(i).setCellValue(title);
				header.getCell(i).setCellStyle(style1);
				i++;
			}

			// create data rows
			int rowCount = 1;

			for (Map<String, Object> map : resultList) {
				HSSFRow aRow = sheet.createRow(rowCount++);
				for (int j = 0; j < map.size(); j++) {
					String s = "";
					Object o = map.get(titleArray[j]);
					if (!(o instanceof BigDecimal)) {
						s = StringUtils.defaultString((String) o);
						aRow.createCell(j).setCellValue(s);
						aRow.getCell(j).setCellStyle(style2);
					} else {
						s = String.valueOf(o);

						if (!s.contains(".")) {
							Long l = Long.parseLong(s);
							aRow.createCell(j).setCellValue(l);
							aRow.getCell(j).setCellStyle(style3);
						} else {
							Double d = Double.parseDouble(s);
							aRow.createCell(j).setCellValue(d);
							aRow.getCell(j).setCellStyle(style4);
						}
					}
				}
			}
			int lastCellNum = workbook.getSheetAt(0).getRow(0).getLastCellNum();
			for (int colNum = 0; colNum < lastCellNum; colNum++) {
				workbook.getSheetAt(0).autoSizeColumn(colNum);
			}
		} else {
			// create header row
			HSSFRow header = sheet.createRow(0);
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

}