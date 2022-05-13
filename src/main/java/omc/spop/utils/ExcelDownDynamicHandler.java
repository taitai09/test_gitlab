package omc.spop.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

/***********************************************************
 * 2021.06.25	황예지	최초작업
 **********************************************************/

public class ExcelDownDynamicHandler{
	private final Logger logger = LoggerFactory.getLogger(ExcelDownDynamicHandler.class);
	public final int rowsLimit = 1048575;
	
	private CellStyle styleHeader = null;
	private CellStyle styleString = null;
	private CellStyle styleDefault = null;
	private CellStyle styleNumber = null;
	private CellStyle styleFloatingPoint = null;
	private CellStyle styleTimestamp = null;
	private CellStyle styleRgb;
	
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	
	private Map<String, Object> model = null;
	private SXSSFWorkbook workbook = null;
	private SXSSFSheet sheet = null;
	
	private String[] titleArray = null;
	private String methodName = null;
	private int sheetCount = 0;
	private int rowCount = 0;
	
	public ExcelDownDynamicHandler() {}
	
	public ExcelDownDynamicHandler(Model model, HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.model = model.asMap();
		this.workbook = new SXSSFWorkbook();
		buildBascDoc();
	}
	
	private void buildBascDoc(){
		String fileName = null;
		String sheetName = null;
		String toDate = null;
		String sFileName = null;
		String excelId = null;
		
		try {
			workbook.setCompressTempFiles(true);		// 임시파일 압축 설정
			
			logger.debug(this.getClass().getName() + ".buildDocument started");
			
			excelId = StringUtils.defaultString((String) model.get("excelId"));
			logger.debug("excelId::" + excelId);
			
			sheetName = (String) model.get("sheetName");
			logger.debug("sheetName::" + sheetName);
			
			fileName = StringUtils.defaultString((String) model.get("fileName"), sheetName);
			
			toDate = DateUtil.getNowDate("yyyyMMddhhmmssSSS");
			sFileName = URLEncoder.encode(fileName + "_" + toDate + ".xlsx", "UTF-8");
			
			setInfoToResponse(response, request, sFileName, model);
			
			createSheet(sheetName);
			
			setCellStyle(workbook);
			logger.debug("Make Base Complete. SxssfDynamicBuilder");
			
		}catch(Exception e) {
			methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			e.printStackTrace();
			
		}finally{
			fileName = null;
			sheetName = null;
			toDate = null;
			sFileName = null;
			excelId = null;
		}
	}
	
	public void createSheet(String sheetName) {
		try {
			sheet = workbook.createSheet(sheetName);
			sheet.setDefaultColumnWidth(30);
			
			sheetCount++;
			rowCount = 0;
			
		}catch(Exception e) {
			methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void makeHeader(LinkedHashMap<String, Object> result) {
		SXSSFRow headerRow = null;
		Iterator<String> itr = null;
		String[] headers = null;
		String[] headSplit = null;
		String[] excelHeaders = null;
		
		int i = 0;
		String key = "";
		
		try {
			headers = (String[]) model.get("headers");
			headSplit = headers[sheetCount-1].split("\\;");
			
			//동적 엑셀 그리드를 위한 엑셀 헤더값 전달.
			excelHeaders = new String[headSplit.length];
			titleArray = new String[excelHeaders.length];
			
			for(String str : headSplit) {
				excelHeaders[i] = str.split("\\/")[0].replace("slash", "/");
				i++;
			}
			
			headerRow = sheet.createRow(0);
			
			//동적으로 엑셀그리드를 만들어야 하는경우 AND excelHeaders가 있는 경우
			itr = result.keySet().iterator();
			
			i = 0;
			for(String header : excelHeaders){
				if(itr.hasNext()){
					key = (String) itr.next();
					titleArray[i] = key;  //엑셀 데이터 로드를 위한 키값.
				}
				headerRow.createCell(i).setCellValue(header);
				headerRow.getCell(i).setCellStyle(styleHeader);
				i++;
			}
			rowCount++;
			
		} catch (Exception e){
			methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			e.printStackTrace();
			
		} finally {
			headerRow = null;
			itr = null;
			excelHeaders = null;
			itr = null;
			key = null;
		}
	}
	
	public void buildDocument(List<LinkedHashMap<String, Object>> resultList) {
		try {
			for (LinkedHashMap<String, Object> map : resultList) {
				if( rowCount == 0 ) {
					makeHeader(map);
				}
				
				createRowAndCell(map);
				
				if(rowCount >= 100) {
					if(rowCount == 99) {
						ExcelCellRef.autoSizeColumn(sheet);
					}
					
				}else {
					ExcelCellRef.autoSizeColumn(sheet);
				}
				
				sheet.flushRows(100);
			}
		}catch(Exception e) {
			methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			e.printStackTrace();
			
		}finally {
			resultList = null;
		}
	}
	
	private void createRowAndCell(LinkedHashMap<String, Object> map) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SXSSFRow aRow = null;
		SXSSFCell cell = null;
		Object o = null;
		String s = "";
		
		try {
			aRow = sheet.createRow(rowCount);
			
			for (int j = 0; j < titleArray.length; j++) {
				s = "";
				o = map.get(titleArray[j]);
				
				if(ExcelCellRef.isInteger(o)) {
					o = Integer.valueOf(String.valueOf(o));
				} else if(ExcelCellRef.isDouble(o)) {
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
					s = dateFormat.format((Timestamp) o);
					aRow.createCell(j).setCellValue(s);
					aRow.getCell(j).setCellStyle(styleTimestamp);
				} else if (o instanceof String) {
					s = StringUtils.defaultString((String) o);
					
					if(s.indexOf('#') == 0) {	// 셀의 채우기로 RGB 색상 지정
						cell = (SXSSFCell) aRow.createCell(j);
						cell.setCellValue(s);
						
						styleRgb = ExcelCellRef.setCustomCellStyle(workbook, "RGB", s);
						
						aRow.getCell(j).setCellStyle(styleRgb);
					} else if(ExcelCellRef.validationDate(s)) {
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
			rowCount++;
			
		}catch(Exception e) {
			methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			e.printStackTrace();
			
		}finally {
			dateFormat = null;
			aRow = null;
			cell = null;
			o = null;
			s = null;
		}
	}
	
	public boolean writeDoc() {
		ServletOutputStream fos = null;
		boolean returnResult = true;
		
		try {
			fos = response.getOutputStream();
			workbook.write(fos);
			
			fos.flush();
			fos.close();
			
		}catch (Exception e) {
			returnResult = false;
			
			methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			e.printStackTrace();
			
		}finally {
			if (fos != null) {
				try {
					fos.close();
				}
				catch (IOException e) {}
			}
			
			if (workbook != null) {
				try {
					((SXSSFWorkbook) workbook).dispose();
				}
				catch (Exception e) {}
			}
			workbook.dispose();		//임시파일, 경로 삭제
			logger.debug("file write finished. SxssfDynamicBuilder");
			
			fos = null;
			
			this.styleHeader = null;
			this.styleString = null;
			this.styleDefault = null;
			this.styleNumber = null;
			this.styleFloatingPoint = null;
			this.styleTimestamp = null;
			
			this.model = null;
			this.workbook = null;
			this.sheet = null;
			this.titleArray = null;
		}
		
		return returnResult;
	}
	
	private void setCellStyle(SXSSFWorkbook workbook) throws Exception{
		styleHeader = ExcelCellRef.setCustomCellStyle(workbook, "HEADER", null);
		styleString = ExcelCellRef.setCustomCellStyle(workbook, "STRING", null);
		styleDefault = ExcelCellRef.setCustomCellStyle(workbook, "DEFAULT", null);
		styleNumber = ExcelCellRef.setCustomCellStyle(workbook, "NUMBER", null);
		styleFloatingPoint = ExcelCellRef.setCustomCellStyle(workbook, "FLOATING_POINT", null);
		styleTimestamp = ExcelCellRef.setCustomCellStyle(workbook, "TIMESTAMP", null);
	}
	
	public void setInfoToResponse(HttpServletResponse response, HttpServletRequest request, String sFileName, Map<String, Object> model) throws Exception{
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
	
	public void makeEmptyDoc() {
		SXSSFRow header = null;
		
		try {
			header = sheet.createRow(0);
			header.createCell(0);
			header.createCell(1);
			header.createCell(2);
			header.createCell(3);
			header.createCell(4);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			header.getCell(0).setCellValue("데이터가 없습니다.");
			header.getCell(0).setCellStyle(styleHeader);
			
		}catch(Exception e) {
			methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			
		}finally {
			header = null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Object getValueOf(Object obj, String excelId) throws Exception {
		Field field = null;
		Class clazzType = null;
		Object returnObj = null;
		
		try {
			field = obj.getClass().getField(excelId);
			clazzType = field.getType();
			
			if (clazzType.toString().equals("double")) {
				returnObj = field.getDouble(obj);
				
			} else if (clazzType.toString().equals("int")) {
				returnObj = field.getInt(obj);
			
			} else {
				returnObj = field.get(obj);
			}
			
		} catch (Exception e) {
			methodName = new Object() {}.getClass().getName();
			logger.error(methodName+" Excel Build Error {}", e.getMessage());
			e.printStackTrace();
			
		} finally {
			field = null;
			clazzType = null;
		}
		
		return returnObj;
	}
}