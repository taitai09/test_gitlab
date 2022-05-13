package omc.spop.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 엑셀 파일을 서버의 디스크에 작성한다.
 */

public class ExcelWrite {
    /**
     * 엑셀 문서에 만들어질 Sheet
     */
    private static XSSFSheet sheet;

    /**
     * 엑셀 문서에 Row를 작성할 때 몇 번째에 Row를 만들 것인지 지정하기 위한 변수
     * 엑셀 문서에 Row를 작성할 때마다 증가함.
     */
    private static int rowIndex;

	private static XSSFWorkbook wb;
	private static XSSFCellStyle styleTitle;
	private static XSSFCellStyle styleBody;
	private static XSSFColor backTitle = new XSSFColor(new java.awt.Color(238,238,238));
	private static XSSFColor topBorder = new XSSFColor(new java.awt.Color(53,121,212));
	private static XSSFColor otherBorder = new XSSFColor(new java.awt.Color(211,211,211));
    /**
     * 엑셀 파일을 작성한다.
     * @param WriteOption
     * @return Excel 파일의 File 객체
     * @throws UnsupportedEncodingException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
	public static void init(HttpServletResponse response, WriteOption writeOption) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {		
		String toDate = DateUtil.getNowDate("yyyyMMddhhmmssSSS");
		String sFileName = URLEncoder.encode(writeOption.getFileName() + "_" + toDate + ".xlsx","UTF-8");
		
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setContentType("application/smnet; charset=utf-8");		
		response.setHeader("Content-Disposition", "attachment; filename="+sFileName+";");
		wb = new XSSFWorkbook();
    }
	
    public static void write(HttpServletResponse response, WriteOption writeOption) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {		
		String toDate = DateUtil.getNowDate("yyyyMMddhhmmssSSS");
		String sFileName = URLEncoder.encode(writeOption.getFileName() + "_" + toDate + ".xlsx","UTF-8");
		
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setContentType("application/smnet; charset=utf-8");		
		response.setHeader("Content-Disposition", "attachment; filename="+sFileName+";");
		
		wb = new XSSFWorkbook();
		sheet = wb.createSheet(writeOption.getSheetName());
		rowIndex = 0;
		setTitle(writeOption.getTitles());
        setContents(writeOption.getContents());
        
        sheet.autoSizeColumn(0);
        
        try {
    		OutputStream fileOut = response.getOutputStream();
    		wb.write(fileOut);
    	} catch (IOException e) {
            System.out.println("error ==> " + e.getMessage());
        }
    }
    
    public static void multiWrite(HttpServletResponse response, WriteOption writeOption) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {		
		sheet = wb.createSheet(writeOption.getSheetName());
		rowIndex = 0;
        setTitle(writeOption.getTitles());
        setContents(writeOption.getContents());
    }
    
    public static void end(HttpServletResponse response, WriteOption writeOption) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {
        try {
    		OutputStream fileOut = response.getOutputStream();
    		wb.write(fileOut);
    	} catch (IOException e) {
            System.out.println("error ==> " + e.getMessage());
        }
    }    

	private static void setTitle(List<String> values) {
        styleTitle =  wb.createCellStyle();
        
        styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitle.setFillForegroundColor(backTitle);        
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTitle.setBorderTop(BorderStyle.MEDIUM);
        styleTitle.setTopBorderColor(topBorder);
        styleTitle.setBorderLeft(BorderStyle.THIN);
        styleTitle.setBottomBorderColor(otherBorder);
        styleTitle.setBorderRight(BorderStyle.THIN);
        styleTitle.setRightBorderColor(otherBorder);
        styleTitle.setBorderBottom(BorderStyle.THIN);
        styleTitle.setLeftBorderColor(otherBorder);

        XSSFRow row = null;
        XSSFCell cell = null;
        int cellIndex = 0;

        if( values != null && values.size() > 0 ) {
            row = sheet.createRow(rowIndex++);
            for(String value : values) {
                cell = row.createCell(cellIndex++);
                cell.setCellValue(value);
                cell.setCellStyle(styleTitle);
            }
        }
    }

    private static void setContents(List<String[]> values) {
    	styleBody =  wb.createCellStyle();
    	styleBody.setVerticalAlignment(VerticalAlignment.CENTER);
    	styleBody.setBorderTop(BorderStyle.THIN);
    	styleBody.setTopBorderColor(otherBorder);
        styleBody.setBorderLeft(BorderStyle.THIN);
        styleBody.setBottomBorderColor(otherBorder);
        styleBody.setBorderRight(BorderStyle.THIN);
        styleBody.setRightBorderColor(otherBorder);
        styleBody.setBorderBottom(BorderStyle.THIN);
        styleBody.setLeftBorderColor(otherBorder);
        
        XSSFRow row = null;
        XSSFCell cell = null;
        int cellIndex = 0;
        Double d = null;

        if( values != null && values.size() > 0 ) {
            for(String[] arr : values) {
                row = sheet.createRow(rowIndex++);
                cellIndex = 0;
                for(String value : arr) {
                    cell = row.createCell(cellIndex++);
                    
                    if (value != null && value.indexOf(".") == 0) {
                        d = Double.parseDouble(value);
                        cell.setCellValue(Double.toString(d));
                    } else {
                        cell.setCellValue(value);
                    }
                    cell.setCellStyle(styleBody);
                }
            }
        }
    }
}