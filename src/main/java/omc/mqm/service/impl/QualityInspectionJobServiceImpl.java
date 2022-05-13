package omc.mqm.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import omc.spop.utils.CryptoUtil;
import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.OpenmQtyChkSql;
import omc.mqm.model.OpenqErrCnt;
import omc.mqm.model.QualityStdInfo;
import omc.mqm.dao.QualityInspectionJobDao;
import omc.mqm.dao.QualityStdInfoDao;
import omc.mqm.service.QualityInspectionJobService;

import omc.spop.utils.FileType;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/***********************************************************
 * 2019.05.08	임승률	최초작성
 **********************************************************/

@Service("QualityInspectionJobService")
public class QualityInspectionJobServiceImpl implements QualityInspectionJobService {
	
	private static final Logger logger = LoggerFactory.getLogger(QualityInspectionJobServiceImpl.class);
	
	@Value("#{defaultConfig['download.outputs.dir']}")
	private String downloadOutputsDir;
	
	@Autowired
	private QualityInspectionJobDao qualityInspectionJobDao;
	
	@Autowired
	private QualityStdInfoDao qualityStdInfoDao;
	
	private String key = "openmade";

	@Override
	public List<OpenmQaindi> selectQualityInspectionJobList(OpenmQaindi openmQaindi) throws Exception {
		logger.debug("openmQaindi.getSearchKey() : " + openmQaindi.getSearchKey());
		if (openmQaindi.getSearchKey().equals("1")) {
			return qualityInspectionJobDao.selectQualityInspectionJobList(openmQaindi);
		}
		else {
			return qualityInspectionJobDao.selectQualityInspectionJobResultList(openmQaindi);
		}
	}
	@Override
	@Transactional
	public int qualityInspectionJobModelCollectingAction(OpenmQaindi openmQaindi) throws Exception {

		OpenmQtyChkSql jobSql = qualityInspectionJobDao.selectQualityInspectionJobSql(openmQaindi);
		OpenmQtyChkSql runSql =  new OpenmQtyChkSql();
		String[] runSqlTextList = null;
		String regex = ";";
		int i_ret = 0;
		int result_cnt = 0;
		
		if (jobSql == null || jobSql.getQty_chk_sql().trim().equals("")) {
			throw new Exception("모델정보수집을 위한 시스템 연결 정보가 없습니다.<br/>관리자에게 문의해 주시기 바랍니다.");
		}
		
		String qty_chk_sql = CryptoUtil.decryptAES128(jobSql.getQty_chk_sql(),key).trim();
		logger.debug("qty_chk_sql : " + qty_chk_sql);
		runSqlTextList = qty_chk_sql.split(regex);
		
		for (String  runSqlText : runSqlTextList) {
			runSqlText = runSqlText.replace(";", "");
			logger.debug("runSqlText : " + runSqlText);
			
			if (runSqlText == null || runSqlText.trim().equals("")) continue;
			
			runSql.setQty_chk_sql(runSqlText);
			runSql.setProject_id(StringUtils.defaultString(openmQaindi.getProject_id()));  // 프로젝트 아이디 추가 20190816
			
			i_ret = qualityInspectionJobDao.insertQualityInspectionJob(runSql);
			logger.debug("insertQualityInspectionJob i_ret : " + i_ret);
			
			result_cnt ++;
			
		}
		return result_cnt;
	}
	
	@Override
	@Transactional
	public int saveQualityInspectionJob(OpenmQaindi openmQaindi) throws Exception {
		

		HashMap<String, Object> param = new HashMap<String, Object>();
		String[]  	qty_chk_idt_cd = null;
		String 	  	regex = ",";
		int			i_ret = 0;
		
		param.put("qty_chk_idt_cd", openmQaindi.getQty_chk_idt_cd());
		logger.debug("qty_chk_idt_cd : " + openmQaindi.getQty_chk_idt_cd());
		logger.debug("project_id = :" + openmQaindi.getProject_id());
		logger.debug("openmQaindi getQty_chk_result_tbl_nm : " + openmQaindi.getQty_chk_result_tbl_nm());
		
		// [기능-0074-02]
//		i_ret = qualityInspectionJobDao.updateTbOpenmQaindi(param);
		
		logger.debug("updateTbOpenmQaindi i_ret : " + i_ret);
		
		qty_chk_idt_cd = openmQaindi.getQty_chk_idt_cd().split(regex);
		
		System.out.println("확인00");
		List<OpenmQtyChkSql> jobSqlList = qualityInspectionJobDao.selectQualityInspectionJobSqlList(openmQaindi);
		
		String curDate = qualityInspectionJobDao.selectTbOpenmCurDate(); 
		if (curDate == null || curDate.equals("")) {
			throw new Exception("시스템 일자를 조회할 수 없습니다.");
		}
		param.clear();
		param.put("extrac_dt", curDate);
		param.put("qty_chk_idt_cd", openmQaindi.getQty_chk_idt_cd());
		param.put("project_id", openmQaindi.getProject_id());
		logger.debug("param : " + param);
		
		if ( qty_chk_idt_cd != null ) {
			logger.debug("확인0 : "+qty_chk_idt_cd.toString());
		}
		
		i_ret = qualityInspectionJobDao.deleteTbOpenqErrCnt(param);
		// [기능-0074-03]
		for (OpenmQtyChkSql openmQtyChkSql : jobSqlList) {
			if (openmQtyChkSql.getQty_chk_idt_cd().equals("100")) {
				continue;
			}
			if(openmQtyChkSql.getQty_chk_result_tbl_nm() == null || openmQtyChkSql.getQty_chk_result_tbl_nm().equals("")) continue;
			openmQtyChkSql.setProject_id(openmQaindi.getProject_id());

			System.out.println("확인1 : "+openmQtyChkSql.getQty_chk_idt_cd());
			i_ret = qualityInspectionJobDao.deleteQtyChkResultTblNm(openmQtyChkSql);
		}
		for (OpenmQtyChkSql openmQtyChkSql : jobSqlList) {
			if(openmQtyChkSql.getQty_chk_result_tbl_nm() == null || openmQtyChkSql.getQty_chk_result_tbl_nm().equals("")) continue;
			String qty_chk_sql = CryptoUtil.decryptAES128(openmQtyChkSql.getQty_chk_sql(),key);
			openmQtyChkSql.setQty_chk_sql(qty_chk_sql);
			openmQtyChkSql.setProject_id(openmQaindi.getProject_id());
			System.out.println("확인2 : "+openmQtyChkSql.getQty_chk_idt_cd());
			i_ret = qualityInspectionJobDao.insertQualityInspectionJob(openmQtyChkSql);
		}
		
		// [기능-0074-04]
		for (OpenmQtyChkSql openmQtyChkSql : jobSqlList) {
			if (openmQtyChkSql.getQty_chk_idt_cd().equals("100")) {
				continue;
			}
			if(openmQtyChkSql.getQty_chk_result_tbl_nm() == null || openmQtyChkSql.getQty_chk_result_tbl_nm().equals("")) continue;
			param.clear();
			
			param.put("extrac_dt", curDate);
			param.put("qty_chk_idt_cd", openmQtyChkSql.getQty_chk_idt_cd());
			param.put("qty_chk_result_tbl_nm", openmQtyChkSql.getQty_chk_result_tbl_nm());
			param.put("project_id", openmQaindi.getProject_id());
			try {
				System.out.println("확인3 : "+openmQtyChkSql.getQty_chk_idt_cd());
				i_ret = qualityInspectionJobDao.insertTbOpenqErrCnt(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return qty_chk_idt_cd.length;
	}
	
	@Override
	public List<OpenmQaindi> getQualityInspectionJobSheetHeadTitleList(OpenmBizCls openmBizCls) throws Exception {
		logger.debug("getQualityInspectionJobSheetHeadTitleList Start");
		List<OpenmQaindi> openmQaindiList =  qualityInspectionJobDao.selectQualityInspectionJobSheetHeadTitleList(openmBizCls);
		//List<OpenmQaindi> openmQaindiListOut = new ArrayList<OpenmQaindi>();
		//for (OpenmQaindi Row : openmQaindiList) {
		//  Row.setQty_chk_idt_cd_nm(Row.getQty_chk_idt_cd_nm().replace(" ", "<br>"));
		//	openmQaindiList.get(0).setGather_day("");
			//openmQaindiListOut.add(Row);
		//}
		logger.debug("getQualityInspectionJobSheetHeadTitleList selectQualityInspectionJobSheetHeadTitleList Call End");
		if(openmQaindiList != null && openmQaindiList.size() > 0) {
			String ExtrecDt = qualityInspectionJobDao.selectTbOpenmEntExtrecDt();
			openmQaindiList.get(0).setGather_day(ExtrecDt);
			logger.debug("impl openmQaindiList Size : " + openmQaindiList.size());
		}
		return openmQaindiList;
		//return qualityInspectionJobDao.selectQualityInspectionJobSheetHeadTitleList(openmQaindi);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> selectQualityInspectionJobListByExcelDown(OpenmQaindi openmQaindi) {
		logger.debug("openmQaindi.getSearchKey() : " + openmQaindi.getSearchKey());
		return qualityInspectionJobDao.selectQualityInspectionJobListByExcelDown(openmQaindi);
	}
	
	private static final String INTERNAL_FILE="/excelUploadForm/Form_데이터구조품질보고서.xlsx";
	@SuppressWarnings("resource")
	@Override
	public Map<String, Object> selectQualityInspectionJobSheetListByExcelDown(OpenqErrCnt openqErrCnt) throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> outParam = new HashMap<String, Object>();
		File file = null;
		String outFileName = "";
		String downloadOutputsDirFileName = "";
		FileInputStream fis = null;
		Workbook wb = null;
		List<Map<String, Object>> sqlResultList = new ArrayList<Map<String, Object>>();
		Set<String> dataColumnSet = null;
		int sheetIndex = 0;
		
		if (downloadOutputsDir == null || downloadOutputsDir.equals("")){
			throw new Exception("download.outputs.dir 설정이 필요합니다.");
		}
		
		logger.debug("downloadOutputsDir : " + downloadOutputsDir);
		
		String extrac_dt = qualityInspectionJobDao.selectTbOpenmEntExtrecDt();

		if (extrac_dt == null || extrac_dt.equals("")) {
			throw new Exception("모델정보수집일자를 조회할 수 없습니다.");
		}
		
		String curDate = qualityInspectionJobDao.selectTbOpenmCurDate();

		if (curDate == null || curDate.equals("")) {
			throw new Exception("모델정보수집일자를 조회할 수 없습니다.");
		}

		
		String fileCreateBaseDt   = curDate.replaceAll("[/: ]", "");
		logger.debug("fileCreateBaseDt : " + fileCreateBaseDt);
		
		
		List<OpenmQaindi> excelOutList = qualityInspectionJobDao.selectQualityInspectionJobExcelOutSqlList(openqErrCnt);

		String qty_chk_sql = "";
		
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        logger.debug("current directory:"+classloader.getResource(".").getPath());
        //System.out.println("download directory::::::"+classloader.getResource(INTERNAL_FILE).getFile().toString());
        //System.out.println("dir::::::"+classloader.getClass().getName().toString());
        
        file = new File(classloader.getResource(INTERNAL_FILE).getFile());
        outFileName =file.getName();
		outFileName = outFileName.substring(5 , outFileName.lastIndexOf(".xlsx")) + "_" + fileCreateBaseDt + ".xlsx"; 
		logger.debug("outFileName : " + outFileName);
		
		if (downloadOutputsDir.endsWith("/")) {
			//downloadOutputsDir += outFileName;
			downloadOutputsDirFileName = downloadOutputsDir + outFileName;
		}
		else {
			//downloadOutputsDir += "/" + outFileName;
			downloadOutputsDirFileName = downloadOutputsDir + "/" + outFileName;
		}
        
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new Exception(e.getMessage(), e);
		}
		
		try {
			wb = new XSSFWorkbook(fis);
		} catch (IOException e) {
			throw new Exception(e.getMessage(), e);
		}
		
		/*
		for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
			Sheet sheet = wb.getSheetAt(numSheet);
			logger.debug(numSheet + " getSheetName : " + wb.getSheetName(numSheet));

			if (sheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
				
				Row row = sheet.getRow(rowNum);
				if (row != null) {
					Cell no = row.getCell(0);
					Cell name = row.getCell(1);
					//sb.append(no + ":" + name);
					logger.debug("row : " + rowNum + " : " + no + " : " + name);
				}
				//logger.debug("row : " + rowNum + " : " + sb.toString());
			}
			//logger.debug("sb : " + sb.toString());
		}
		*/

		for (OpenmQaindi excelOutRow : excelOutList) {
			try {

				//CLOB clob = (oracle.sql.CLOB)excelOutRow.getQty_chk_sql();
				//logger.debug("qualityCheckSqlListByExcelDown Start 3 : " + clob.stringValue());
				logger.debug("getQty_chk_idt_cd : " + excelOutRow.getQty_chk_idt_cd());
				Sheet st = wb.getSheet(excelOutRow.getQty_result_sheet_nm());
				
				if(excelOutRow.getQty_chk_idt_cd().equals("001")) {
					qty_chk_sql = CryptoUtil.decryptAES128(excelOutRow.getQty_chk_sql(),key);
					excelOutRow.setQty_chk_sql(qty_chk_sql);
					param.clear();
					param.put("qty_chk_sql", qty_chk_sql);
					param.put("project_id", StringUtils.defaultString(openqErrCnt.getProject_id()));
					param.put("lib_nm", openqErrCnt.getLib_nm());
					param.put("model_nm", openqErrCnt.getModel_nm());
					sqlResultList = qualityInspectionJobDao.selectQualityInspectionJobSqlResultList(param);
					if (sqlResultList.size() > 0){
						dataColumnSet = sqlResultList.get(0).keySet();
						logger.debug("dataColumnSet : " + dataColumnSet.toString());
						setSummarySheet(wb, st, excelOutRow, sqlResultList, excelOutList, extrac_dt);
					}
					else {
						throw new Exception("001집계표 데이터가 없습니다.");
					}
					logger.debug("QTY_CHK_SQL : " + qty_chk_sql);
					
				}
				else {
					if(excelOutRow.getExcel_output_yn().equals("Y")) {
						param.clear();
						param.put("qty_chk_result_tbl_nm", excelOutRow.getQty_chk_result_tbl_nm());
						param.put("lib_nm", openqErrCnt.getLib_nm());
						param.put("model_nm", openqErrCnt.getModel_nm());
						param.put("project_id", StringUtils.defaultString(openqErrCnt.getProject_id()));
//						logger.debug("확인테이블:::::"+param.get("qty_chk_result_tbl_nm"));
						sqlResultList = qualityInspectionJobDao.selectQtyChkResultTblNmResultList(param);
						if (sqlResultList != null && sqlResultList.size() > 0){
							dataColumnSet = sqlResultList.get(0).keySet();
							logger.debug("엑셀대상 : " + sqlResultList.size() + "건 : "+ excelOutRow.getQty_result_sheet_nm());
							setSheet(wb, st, excelOutRow, sqlResultList);
						} else {
							logger.debug("엑셀대상 : 0건 : " + excelOutRow.getQty_result_sheet_nm());
							continue;
						}
						
					} else {
						logger.debug("삭제대상 : " + excelOutRow.getQty_result_sheet_nm());
						sheetIndex = wb.getSheetIndex(excelOutRow.getQty_result_sheet_nm());
						logger.debug("삭제대상 sheetIndex : " + sheetIndex);
						
						if(sheetIndex < 0) {
							logger.debug("삭제대상 sheet 정보가 존재하지 않아서 삭제 작업은 무시///");
						} else {
							wb.removeSheetAt(sheetIndex);
						}
					}
				}
				logger.debug("getQty_result_sheet_nm() : " + excelOutRow.getQty_result_sheet_nm());
				
				//Sheet st = wb.getSheet(excelOutRow.getQty_result_sheet_nm());
				/*
				logger.debug("st.getLastRowNum() : " + st.getLastRowNum());
				int lastRowNum = st.getLastRowNum();
				
				for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
					Row row = st.getRow(rowNum);
					if (row != null) {
						for (int colNum = 0; colNum < row.getLastCellNum(); colNum++){
							logger.debug("row : " + rowNum + ", col : " + colNum + " : value [" + row.getCell(colNum) +"]");
						}
						//Cell no = row.getCell(0);
						//Cell name = row.getCell(1);
						//sb.append(no + ":" + name);
						//logger.debug("row : " + rowNum + " : " + no + " : " + name);
					}
					logger.debug("row : " + rowNum);
					if (excelOutRow.getQty_chk_idt_cd().equals("001") && rowNum > 2 ) {
						//logger.debug("copyRow Start");
						//copyRowStyle(wb, st, 2, rowNum+1);
						//logger.debug("copyRow End");
					}
					
				}
				*/
			} catch (InvalidKeyException e) {
				logger.error("Error : " + e.getMessage());
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				logger.error("Error : " + e.getMessage());
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				logger.error("Error : " + e.getMessage());
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				logger.error("Error : " + e.getMessage());
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				logger.error("Error : " + e.getMessage());
				e.printStackTrace();
			} catch (BadPaddingException e) {
				logger.error("Error : " + e.getMessage());
				e.printStackTrace();
			} catch (IllegalBlockSizeException  e) {
				logger.error("Error : " + e.getMessage());
				e.printStackTrace();
			}
		}
			
		//String downloadOutputsFile = downloadOutputsDir + "/" + ".xlsx";
		logger.debug("downloadOutputsDir + FileName : " + downloadOutputsDirFileName);
        File outWorkBook = new File(downloadOutputsDirFileName);
        if(!(new File(downloadOutputsDir).exists())){
        	new File(downloadOutputsDir).mkdirs();
        }
        OutputStream out = new FileOutputStream(outWorkBook);
        wb.write(out);
        out.flush();
        out.close();
        wb.close();
        
        outParam.put("FILE", outFileName);
        outParam.put("PATH", downloadOutputsDir);
		//wb.write(arg0);

		return outParam;
		//return qualityInspectionJobDao.selectQualityInspectionJobListByExcelDown(openmQaindi);
	}
	// 001집계표 Sheet 작업
	private void setSummarySheet(Workbook wb, Sheet st, OpenmQaindi openmQaindi, List<Map<String, Object>> sqlResultList,
			List<OpenmQaindi> excelOutList, String extrac_dt) {
		int rowCount = 1;
		int sqlResultListCount = sqlResultList.size();
		int startRow = openmQaindi.getOutput_start_row();
		Iterator itr = sqlResultList.get(0).keySet().iterator();
		String columns[] = new String[sqlResultList.get(0).keySet().size()];
		Row aRow = null;
		int iRow = 0;
		// 숫자 > 0 : 255,204,0
		// 합계        : 218,238,243
		CellStyle cellStyle = wb.createCellStyle();
		XSSFColor numberCellColor = new XSSFColor(new java.awt.Color(255,204,0));

		// 기준일자 Set
		aRow = st.getRow(0);
		aRow.getCell(0).setCellValue(extrac_dt + " 기준");
		
		while (itr.hasNext()) {
			String column = (String) itr.next();
			if (column.equals("SORT_ORD")) continue;
			columns[iRow] = column;
			iRow ++;
		}
		
		logger.debug("columns : " + columns.toString());
		try {
			for (Map<String, Object> map : sqlResultList) {
				if(rowCount < sqlResultListCount) {
					aRow = copyRowStyle(wb, st, startRow -1, startRow + rowCount);
				} else {
					aRow = copyRowStyle(wb, st, startRow, startRow + rowCount);
				}
				for (int j = 0; j < columns.length; j++) {
					//logger.debug("createSummarySheet Start 2(row,col) : " + rowCount + " , " + j);
					String s = "";
					Object o = map.get(columns[j]);
					if (o instanceof BigDecimal) {
						s = String.valueOf(o);
						//logger.debug("instance of BigDecimal:" + s);
						if (!s.contains(".")) {
							Long l = Long.parseLong(s);
							aRow.getCell(j).setCellValue(l);
						} else {
							Double d = Double.parseDouble(s);
							aRow.getCell(j).setCellValue(d);
						}
						if ((aRow.getCell(j).getNumericCellValue() > 0) && (rowCount < sqlResultListCount)){
							cellStyle = aRow.getCell(j).getCellStyle();
							((XSSFCellStyle) cellStyle).setFillForegroundColor(new XSSFColor(new byte[] {(byte) 255,(byte) 204,(byte) 0}, null));
							aRow.getCell(j).setCellStyle(cellStyle);
						}
					} else if (o instanceof Byte) {
						s = String.valueOf(o);
						//logger.debug("instance of Byte:" + s);
						Byte d = Byte.parseByte(s);
						aRow.getCell(j).setCellValue(d.toString());
					} else if (o instanceof Short) {
						s = String.valueOf(o);
						//logger.debug("instance of Short:" + s);
						Short d = Short.parseShort(s);
						aRow.getCell(j).setCellValue(d);
					} else if (o instanceof Integer) {
						s = String.valueOf(o);
						//logger.debug("instance of Integer:" + s);
						Integer d = Integer.parseInt(s);
						aRow.getCell(j).setCellValue(d);
						if ((aRow.getCell(j).getNumericCellValue() > 0) && (rowCount < sqlResultListCount)){
							cellStyle = aRow.getCell(j).getCellStyle();
							((XSSFCellStyle) cellStyle).setFillForegroundColor(new XSSFColor(new byte[] {(byte) 255,(byte) 204,(byte) 0}, null));
							aRow.getCell(j).setCellStyle(cellStyle);
						}
					} else if (o instanceof Long) {
						s = String.valueOf(o);
						//logger.debug("instance of Long:" + s);
						Long l = Long.parseLong(s);
						aRow.getCell(j).setCellValue(l);
						if ((aRow.getCell(j).getNumericCellValue() > 0) && (rowCount < sqlResultListCount)){
							cellStyle = aRow.getCell(j).getCellStyle();
							((XSSFCellStyle) cellStyle).setFillForegroundColor(new XSSFColor(new byte[] {(byte) 255,(byte) 204,(byte) 0}, null));
							aRow.getCell(j).setCellStyle(cellStyle);
						}
					} else if (o instanceof Float) {
						s = String.valueOf(o);
						//logger.debug("instance of Float:" + s);
						Float f = Float.parseFloat(s);
						aRow.getCell(j).setCellValue(f);
						if ((aRow.getCell(j).getNumericCellValue() > 0) && (rowCount < sqlResultListCount)){
							cellStyle = aRow.getCell(j).getCellStyle();
							((XSSFCellStyle) cellStyle).setFillForegroundColor(new XSSFColor(new byte[] {(byte) 255,(byte) 204,(byte) 0}, null));
							aRow.getCell(j).setCellStyle(cellStyle);
						}
					} else if (o instanceof Double) {
						s = String.valueOf(o);
						//logger.debug("instance of Double:" + s);
						Double d = Double.parseDouble(s);
						aRow.getCell(j).setCellValue(d);
						if ((aRow.getCell(j).getNumericCellValue() > 0) && (rowCount < sqlResultListCount)){
							cellStyle = aRow.getCell(j).getCellStyle();
							((XSSFCellStyle) cellStyle).setFillForegroundColor(new XSSFColor(new byte[] {(byte) 255,(byte) 204,(byte) 0}, null));
							aRow.getCell(j).setCellStyle(cellStyle);
						}
					} else if (o instanceof String) {
						s = StringUtils.defaultString((String) o);
						//logger.debug("instance of String:" + s);
						
						if(s.indexOf('#') == 0) {	// 셀의 채우기로 RGB 색상 지정
							int r = Integer.valueOf(s.substring(1, 3), 16);
							int g = Integer.valueOf(s.substring(3, 5), 16);
							int b = Integer.valueOf(s.substring(5, 7), 16);
							
							XSSFColor color = new XSSFColor(new java.awt.Color(r,g,b));
							XSSFCell cell = (XSSFCell) aRow.createCell(j);
							CellStyle style = wb.createCellStyle();
							
							cell.setCellValue(s);
							style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
							((XSSFCellStyle) style).setFillForegroundColor(color);
							aRow.getCell(j).setCellStyle(style);
						} else {
							aRow.getCell(j).setCellValue(s);
						}
					} else if (o instanceof Timestamp) {
						s = StringUtils.defaultString(((Timestamp) o).toString());
						//logger.debug("instance of Timestamp:" + s);
						aRow.getCell(j).setCellValue(s);
					} else {
						s = StringUtils.defaultString((String) o);
						//logger.debug("instance of others:" + s);
						if(s != null && !s.equals(""))
						aRow.getCell(j).setCellValue(s);
					}
				}
				rowCount ++;
			}
			// Temp Row Remove
	        removeRow(st, startRow);
	        removeRow(st, startRow -1);
	        
	        // setColumnHidden
	        for (OpenmQaindi hiddenRow : excelOutList) {
	        	if (hiddenRow.getExcel_output_yn().equals("N")) {
	        		logger.debug("hiddenRow.getQty_chk_idt_cd() : " + hiddenRow.getQty_chk_idt_cd() + " : " + hiddenRow.getOutput_start_col());
	        		st.setColumnHidden(hiddenRow.getOutput_start_col() -1, true);
	        	}
	        }
		} catch (Exception e) {
				e.printStackTrace();
				logger.error("에러발생 :::::" + e.getMessage());
		}
	}
	// 002~ Sheet 작업
	private void setSheet(Workbook wb, Sheet st, OpenmQaindi openmQaindi, List<Map<String, Object>> sqlResultList) {
		SimpleDateFormat sdfTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int sqlResultListCount = sqlResultList.size();
		int startRow = openmQaindi.getOutput_start_row();
		int rowCount = startRow - 1;
		Iterator itr = sqlResultList.get(0).keySet().iterator();
		String columns[] = new String[sqlResultList.get(0).keySet().size()-1];		// PROJECT_ID 는 엑셀데이터에서 보여주지 않아야 함으로 -1 을 해준다. 
		Row aRow = null;
		int iRow = 0;
		
		short whiteColor = HSSFColor.HSSFColorPredefined.WHITE.getIndex();
		short blackColor = HSSFColor.HSSFColorPredefined.BLACK.getIndex();
		DataFormat format = wb.createDataFormat();
		
		// content String style
		CellStyle contentLeftStyle = wb.createCellStyle();
		contentLeftStyle.setBorderBottom(BorderStyle.THIN);
		contentLeftStyle.setBorderTop(BorderStyle.THIN);
		contentLeftStyle.setBorderRight(BorderStyle.THIN);
		contentLeftStyle.setBorderLeft(BorderStyle.THIN);

		Font contentLeftfont = wb.createFont();
		contentLeftfont.setFontName("맑은 고딕");
		contentLeftfont.setBold(false);
		contentLeftfont.setColor(blackColor);
		contentLeftStyle.setFont(contentLeftfont);

		CellStyle style2 = wb.createCellStyle();
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);

		Font font2 = wb.createFont();
		font2.setFontName("맑은 고딕");
		font2.setBold(false);
		font2.setColor(blackColor);
		style2.setFont(font2);

		// content number style
		CellStyle style3 = wb.createCellStyle();
		style3.setBorderBottom(BorderStyle.THIN);
		style3.setBorderTop(BorderStyle.THIN);
		style3.setBorderRight(BorderStyle.THIN);
		style3.setBorderLeft(BorderStyle.THIN);
		style3.setAlignment(HorizontalAlignment.RIGHT);
		// custom number format
		// style3.setDataFormat(format.getFormat("#.###############"));
		// builtin currency format
		// style3.setDataFormat((short) 0x7); // return $1,458.11
		// style3.setDataFormat((short) 8); // 8 =
		// "($#,##0.00_);[Red]($#,##0.00)"
		// CreationHelper ch = workbook.getCreationHelper();
		// style3.setDataFormat(ch.createDataFormat().getFormat("#,##0;\\-#,##0"));

		Font font3 = wb.createFont();
		font3.setFontName("맑은 고딕");
		font3.setBold(false);
		font3.setColor(blackColor);
		style3.setFont(font3);

		// float, double 소수일경우
		CellStyle style4 = wb.createCellStyle();
		style4.cloneStyleFrom(style3);
		style4.setDataFormat(format.getFormat("#,##0.###############"));
		
		
		
		while (itr.hasNext()) {
			String column = (String) itr.next();
			logger.debug("만들어진 컬럼명 확인 ::: " + column);
			if(column.equals("PROJECT_ID")) continue;
			columns[iRow] = column;
			iRow ++;
		}
		//logger.debug("columns : " + columns.toString());
		for (Map<String, Object> map : sqlResultList) {
			aRow = st.createRow(rowCount);
			for (int j = 0; j < columns.length; j++) {
				//logger.debug("createSheet Start 2(row,col) : " + rowCount + " , " + j);
				String s = "";
				Object o = map.get(columns[j]);
				if (o instanceof BigDecimal) {
					s = String.valueOf(o);
					//logger.debug("instance of BigDecimal:" + s);
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
					//logger.debug("instance of Byte:" + s);
					Byte d = Byte.parseByte(s);
					aRow.createCell(j).setCellValue(d.toString());
					aRow.getCell(j).setCellStyle(style4);
				} else if (o instanceof Short) {
					s = String.valueOf(o);
					//logger.debug("instance of Short:" + s);
					Short d = Short.parseShort(s);
					aRow.createCell(j).setCellValue(d);
					aRow.getCell(j).setCellStyle(style4);
				} else if (o instanceof Integer) {
					s = String.valueOf(o);
					//logger.debug("instance of Integer:" + s);
					Integer d = Integer.parseInt(s);
					aRow.createCell(j).setCellValue(d);
					aRow.getCell(j).setCellStyle(style4);
				} else if (o instanceof Long) {
					s = String.valueOf(o);
					//logger.debug("instance of Long:" + s);
					Long l = Long.parseLong(s);
					aRow.createCell(j).setCellValue(l);
					aRow.getCell(j).setCellStyle(style3);
				} else if (o instanceof Float) {
					s = String.valueOf(o);
					//logger.debug("instance of Float:" + s);
					Float f = Float.parseFloat(s);
					aRow.createCell(j).setCellValue(f);
					aRow.getCell(j).setCellStyle(style4);
				} else if (o instanceof Double) {
					s = String.valueOf(o);
					//logger.debug("instance of Double:" + s);
					Double d = Double.parseDouble(s);
					aRow.createCell(j).setCellValue(d);
					aRow.getCell(j).setCellStyle(style4);
				} else if (o instanceof String) {
					s = StringUtils.defaultString((String) o);
					//logger.debug("instance of String:" + s);
					
					if(s.indexOf('#') == 0) {	// 셀의 채우기로 RGB 색상 지정
						int r = Integer.valueOf(s.substring(1, 3), 16);
						int g = Integer.valueOf(s.substring(3, 5), 16);
						int b = Integer.valueOf(s.substring(5, 7), 16);
						
						XSSFColor color = new XSSFColor(new java.awt.Color(r,g,b));
						XSSFCell cell = (XSSFCell) aRow.createCell(j);
						CellStyle style = wb.createCellStyle();
						
						cell.setCellValue(s);
						style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
						((XSSFCellStyle) style).setFillForegroundColor(color);
						aRow.getCell(j).setCellStyle(style);
					} else {
						aRow.createCell(j).setCellValue(s);
						aRow.getCell(j).setCellStyle(contentLeftStyle);
					}
				} else if (o instanceof Timestamp) {
					s = StringUtils.defaultString(((Timestamp) o).toString());
					
					s = sdfTimestamp.format((Timestamp) o);
					//logger.debug("instance of Timestamp:" + s);
					aRow.createCell(j).setCellValue(s);
					aRow.getCell(j).setCellStyle(style2);
				} else {
					s = StringUtils.defaultString((String) o);
					//logger.debug("instance of others:" + s);
					aRow.createCell(j).setCellValue(s);
					aRow.getCell(j).setCellStyle(style2);
				}
			}
			rowCount ++;
		}
	}
	
	private void removeRow(Sheet st, int rowIndex) {
	    int lastRowNum = st.getLastRowNum();
	    if (rowIndex >= 0 && rowIndex < lastRowNum) {
	        st.shiftRows(rowIndex + 1, lastRowNum, -1);
	    }
	    if (rowIndex == lastRowNum) {
	        Row removingRow = st.getRow(rowIndex);
	        if (removingRow != null) {
	            st.removeRow(removingRow);
	        }
	    }
	}
    private void copyRow(Workbook workbook, Sheet worksheet, String extrac_dt, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
    	Row newRow = worksheet.getRow(destinationRowNum);
    	Row sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        //logger.debug("sourceRow.getLastCellNum() : " + sourceRow.getLastCellNum());
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
        	Cell oldCell = sourceRow.getCell(i);
        	Cell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            ;
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            //newCell.setCellType(oldCell.getCellType());
            newCell.setCellType(oldCell.getCellTypeEnum());

            if (oldCell.getCellTypeEnum() == CellType.STRING){
            	newCell.setCellValue(oldCell.getStringCellValue());
            }
            else if (oldCell.getCellTypeEnum() == CellType.NUMERIC){
            	newCell.setCellValue(oldCell.getNumericCellValue());
            }
            else if (oldCell.getCellTypeEnum() == CellType.BLANK){
            	newCell.setCellValue(oldCell.getStringCellValue());
            }
            else if (oldCell.getCellTypeEnum() == CellType.BOOLEAN){
            	newCell.setCellValue(oldCell.getBooleanCellValue());
            }
            else if (oldCell.getCellTypeEnum() == CellType.FORMULA){
            	newCell.setCellFormula(oldCell.getCellFormula());
            }
            
            /*
            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
            */
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                        )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }
    
    private Row copyRowStyle(Workbook workbook, Sheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
    	Row newRow = worksheet.getRow(destinationRowNum);
    	Row sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
            newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        logger.debug("sourceRow.getLastCellNum() : " + sourceRow.getLastCellNum());
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
        	Cell oldCell = sourceRow.getCell(i);
        	Cell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            ;
            newCell.setCellStyle(newCellStyle);

            // If there is a cell comment, copy
            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }
        }
        // If there are are any merged regions in the source row, copy to new row
        /*
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                        )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
        */
        return newRow;
    }
    
	public List<Map<String, Object>> selectQualityInspectionJobSheetList(OpenmBizCls openmBizCls) throws Exception {
		//List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		OpenmQtyChkSql openmQtyChkSql = new OpenmQtyChkSql();
		HashMap<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> sqlResultList = new ArrayList<Map<String, Object>>();
		
		openmQtyChkSql.setQty_chk_idt_cd("001");
		openmQtyChkSql.setProject_id(StringUtils.defaultString(openmBizCls.getProject_id()));
		logger.debug("project_id ::::::" +StringUtils.defaultString(openmBizCls.getProject_id()));
		logger.debug("lib_nm ::::::" +StringUtils.defaultString(openmBizCls.getLib_nm()));
		logger.debug("model_nm ::::::" +StringUtils.defaultString(openmBizCls.getModel_nm()));
		List<OpenmQtyChkSql> openmQtyChkSqlList = qualityStdInfoDao.qualityCheckSqlList2(openmQtyChkSql);
		
		for (OpenmQtyChkSql openmQtyChkSqlRow : openmQtyChkSqlList) {
			String qty_chk_sql = CryptoUtil.decryptAES128(openmQtyChkSqlRow.getQty_chk_sql(), key);
			param.clear();
			param.put("project_id", StringUtils.defaultString(openmBizCls.getProject_id()));
			System.out.println("######### ::::"+ qty_chk_sql);
			param.put("qty_chk_sql", qty_chk_sql);
			param.put("lib_nm", openmBizCls.getLib_nm());
			param.put("model_nm", openmBizCls.getModel_nm());
			sqlResultList = qualityInspectionJobDao.selectQualityInspectionJobSqlResultList(param);
		}
		
		return sqlResultList;
	}
	
	/* 구조품질집계표  - 라이브러리명 콤보박스*/
	public List<OpenqErrCnt> getOpenqErrCntLibNmList(OpenqErrCnt openmQaindi){  
		return qualityInspectionJobDao.selectOpenqErrCntLibNmList(openmQaindi);
	}
	
	/* 구조품질집계표  - 모델명 콤보박스*/
	public List<OpenqErrCnt> getOpenqErrCntModelNmList(OpenqErrCnt OpenqErrCnt){  
		return qualityInspectionJobDao.selectOpenqErrCntModelNmList(OpenqErrCnt);
	}
	@Override
	public List<OpenmQaindi> getQualityInspectionJob(OpenmQaindi openmQaindi) {
		return qualityInspectionJobDao.getQualityInspectionJob(openmQaindi);
	}
	@Override
	public List<LinkedHashMap<String, Object>> getQualityInspectionJobByExcelDown(OpenmQaindi openmQaindi) {
		return qualityInspectionJobDao.getQualityInspectionJobByExcelDown(openmQaindi);
	}
}