package omc.spop.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import omc.spop.view.DataType;
import oracle.sql.CLOB;

/***********************************************************
 * 2017.12.26	이원식	최초작성
 * 2021.06.24	황예지	메소드 추가
 **********************************************************/

public class ExcelCellRef { 
	private final static Logger logger = LoggerFactory.getLogger(ExcelCellRef.class);
	/**
	 * Cell에 해당하는 Column Name을 가져온다(A,B,C..)
	 * 만약 Cell이 Null이라면 int cellIndex의 값으로
	 * Column Name을 가져온다.
	 * @param cell
	 * @param cellIndex
	 * @return
	 */
	public static String getName(Cell cell, int cellIndex) {
		int cellNum = 0;
		if(cell != null) {
			cellNum = cell.getColumnIndex();
		}
		else {
			cellNum = cellIndex;
		}
		
		return CellReference.convertNumToColString(cellNum);
	}
	
	public static String getValue(Cell cell) {
		String value = "";
		
		if(cell == null) {
			value = "";
		}else{
			if( cell.getCellTypeEnum() == CellType.FORMULA ) {
				value = cell.getCellFormula();
			}else if( cell.getCellTypeEnum() == CellType.NUMERIC ) {
				value = (int)cell.getNumericCellValue() + "";
			}else if( cell.getCellTypeEnum() == CellType.STRING ) {
				value = cell.getStringCellValue();
			}else if( cell.getCellTypeEnum() == CellType.BOOLEAN ) {
				value = cell.getBooleanCellValue() + "";
			}else if( cell.getCellTypeEnum() == CellType.ERROR ) {
				value = cell.getErrorCellValue() + "";
			}else if( cell.getCellTypeEnum() == CellType.BLANK ) {
				value = "";
			}else {
				value = cell.getStringCellValue();
			}
		}
		
		return value;
	}
	
	/**
	* 파라미터로 받은 데이터타입과 데이터에 맞는
	* 엑셀 셀 스타일을 리턴.
	*
	* @param	workbook	셀 스타일이 적용될 Workbook
	* @param	type		적용할 타입 문자열
	* @param	value		type이 RGB일 경우 색상 헥사코드
	* @return 	String
	*/
	public static CellStyle setCustomCellStyle(Workbook workbook, String type, String value) {
		CellStyle cellStyle = null;
		
		DataType dataType = null;
		Font font = null;
		DataFormat format = null;
		XSSFColor color = null;
		
		short headerBackgroundColor = 0;
		short blackColor = 0;
		
		try {
			if (type.equals("HEADER")) {
				dataType = DataType.HEADER;
			} else if (type.equals("NUMBER")) {
				dataType = DataType.NUMBER;
			} else if (type.equals("FLOATING_POINT")) {
				dataType = DataType.FLOATING_POINT;
			} else if (type.equals("RGB")) {
				dataType = DataType.RGB;
			} else if (type.equals("TIMESTAMP")) {
				dataType = DataType.TIMESTAMP;
			} else if (type.equals("STRING")) {
				dataType = DataType.STRING;
			} else if (type.equals("DEFAULT")) {
				dataType = DataType.DEFAULT;
			}
			
			headerBackgroundColor = IndexedColors.GREY_25_PERCENT.getIndex();
			blackColor = HSSFColor.HSSFColorPredefined.BLACK.getIndex();
			cellStyle = workbook.createCellStyle();
			font = workbook.createFont();
			format = workbook.createDataFormat();
			
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
				
				color = new XSSFColor(new java.awt.Color(r, g, b));
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
				cellStyle.setDataFormat(format.getFormat("@"));
				font.setBold(false);
				cellStyle.setFont(font);
				break;
			 case DEFAULT:
				font.setBold(false);
				cellStyle.setFont(font);
				break;
			}
			
		} catch (Exception e) {
			logger.error("Error Occured ===> {}",e.getMessage());
			e.printStackTrace();
			
		} finally {
			dataType = null;
			font = null;
			format = null;
			color = null;
		}

		return cellStyle;
	}
	
	/**
	* 엑셀의 셀1개가 허용하는 최대 길이만큼 데이터를 잘라 리턴.
	*
	* @param	text		셀에 저장할 문자열
	* @return 	String
	*/
	public static String checkMaximumLength(String text) {
		int MAXIMUM_LENGTH = 32767;
		
		try {
			if (text.length() > MAXIMUM_LENGTH) {
				text = text.substring(0, MAXIMUM_LENGTH);
			}
		} catch (Exception e){
			logger.error("Error Occured ===> {}",e.getMessage());
			e.printStackTrace();
		}
		
		return text;
	}
	
	/**
	* SXSSFSheet 사용시 자동 컬럼 너비 조정
	*
	* @param	sheet		SXSSFSheet
	* @return 	
	*/
	public static void autoSizeColumn(SXSSFSheet sheet) {
		try {
			sheet.trackAllColumnsForAutoSizing();
			
			int lastCellNum = sheet.getRow(0).getLastCellNum();
			for (int colNum = 0; colNum < lastCellNum; colNum++) {
				sheet.autoSizeColumn(colNum);
			}
		} catch (Exception e){
			logger.error("Error Occured ===> {}",e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	* 파라미터로 받은 문자열이 java에서 유효한 날짜인지 체크
	*
	* @param	str		날짜  문자열
	* @return 	boolean
	*/
	public static boolean validationDate(String str) {
		SimpleDateFormat dateFormat = null;
		
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			dateFormat.setLenient(false);
			dateFormat.parse(str);
			
			return true;
			
		} catch(ParseException ex) {
			return false;
			
		}finally {
			dateFormat = null;
		}
	}
	
	/**
	 * 특수문자 제거
	 *
	 * @param strText  특수문자가 포함된 문자열
	 * @return 문자열
	 */
	public static String removeSpecialChar(String strText) {
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
	
	/**
	* HTML 태그 제거
	*
	* @param strText  HTML이 포함된 문자열
	* @return 문자열
	*/
	public static String removeHTML(String strText) {
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
	* 파라미터로 받은 객체가 Integer형으로 변환이 가능한지 체크
	*
	* @param	obj		Object
	* @return 	boolean
	*/
	public static boolean isInteger(Object obj) {
		try {
			Integer.parseInt(obj + "");
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	/**
	* 파라미터로 받은 객체가 Double 형으로 변환이 가능한지 체크
	*
	* @param	obj		Object
	* @return 	boolean
	*/
	public static boolean isDouble(Object obj) {
		try {
			Double.parseDouble(obj + "");
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	/**
	* Clob 형태의 데이터를 String 형으로 변환
	*
	* @param	clob		CLOB
	* @return 	String
	*/
	public static String clobToStr(CLOB clob) throws IOException, SQLException {
		BufferedReader contentReader = null;
		StringBuffer out = null;
		String aux = "";
		String returnStr = "";
		
		try {
			contentReader = new BufferedReader(clob.getCharacterStream());
			out = new StringBuffer();
			
			while ((aux = contentReader.readLine()) != null) {
				out.append(aux);
				out.append("<br>");
			}
			returnStr = out.toString();
			
		} catch (IOException ioEx) {
			logger.error("IOException Occured ===> {}",ioEx.getMessage());
			ioEx.printStackTrace();
			
		} catch (SQLException sqlEx){
			logger.error("SQLException Occured ===> {}",sqlEx.getMessage());
			sqlEx.printStackTrace();
			
		}catch (Exception e) {
			logger.error("Error Occured ===> {}",e.getMessage());
			e.printStackTrace();
			
		}finally {
			contentReader = null;
			out = null;
			aux = null;
		}

		return returnStr;
	}
}
