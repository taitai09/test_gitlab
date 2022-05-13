package omc.spop.view;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public class CreateNumberFormats {

	public static void main(String[] args) throws Exception {
		Workbook wb = new XSSFWorkbook();

		Sheet sheet = wb.createSheet("format sheet");
		CellStyle style;
		DataFormat format = wb.createDataFormat();
		Row row;
		Cell cell;
		short rowNum = 0;
		short colNum = 0;

		row = sheet.createRow(rowNum++);
		cell = row.createCell(colNum);
		cell.setCellValue(-337499.939437217); // general format

		style = wb.createCellStyle();
		style.setDataFormat(format.getFormat("#.###############")); // custom
																	// number
																	// format
		row = sheet.createRow(rowNum++);
		cell = row.createCell(colNum);
		cell.setCellValue(-337499.939437217);
		cell.setCellStyle(style);
		row = sheet.createRow(rowNum++);
		cell = row.createCell(colNum);
		cell.setCellValue(123.456789012345);
		cell.setCellStyle(style);
		row = sheet.createRow(rowNum++);
		cell = row.createCell(colNum);
		cell.setCellValue(123456789.012345);
		cell.setCellStyle(style);

		style = wb.createCellStyle();
		style.setDataFormat((short) 0x7); // builtin currency format
		row = sheet.createRow(rowNum++);
		cell = row.createCell(colNum);
		cell.setCellValue(-1234.5678);
		cell.setCellStyle(style);

		sheet.autoSizeColumn(0);

		FileOutputStream fileOut = new FileOutputStream("CreateNumberFormats.xlsx");
		wb.write(fileOut);
		fileOut.close();
		wb.close();
	}
}