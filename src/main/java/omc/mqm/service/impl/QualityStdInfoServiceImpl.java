package omc.mqm.service.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import omc.mqm.dao.QualityStdInfoDao;
import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.OpenmQtyChkSql;
import omc.mqm.model.QualityStdInfo;
import omc.mqm.service.QualityStdInfoService;
import omc.spop.base.SessionManager;
import omc.spop.model.Result;
import omc.spop.utils.CryptoUtil;
import omc.spop.utils.ExcelRead;
import omc.spop.utils.ExcelReadOption;
import omc.spop.utils.FileMngUtil;
import oracle.sql.CLOB;

/***********************************************************
 * 2019.05.22 임호경 최초작성
 **********************************************************/

@Service("QualityStdInfoService")
public class QualityStdInfoServiceImpl implements QualityStdInfoService {
	private static final Logger logger = LoggerFactory.getLogger(QualityStdInfoServiceImpl.class);

	@Autowired
	private QualityStdInfoDao qualityStdInfoDao;

	@Override
	public List<QualityStdInfo> getTbOpenmEntType(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.getTbOpenmEntType(qualityStdInfo);
	}

	@Override
	public int saveTbOpenmEntType(QualityStdInfo qualityStdInfo) throws Exception {
		int check = 0;
		if(qualityStdInfo.getCrud_flag().equals("C")){
			check = qualityStdInfoDao.checkCdPkFromTbOpenmEntType(qualityStdInfo);
		}
		if(check > 0 ){
			throw new Exception(" 엔터티 유형 [ "+qualityStdInfo.getEnt_type_cd()+" ]은(는) 이미 존재합니다.");
		}else{
			check = qualityStdInfoDao.saveTbOpenmEntType(qualityStdInfo);
		}		
		return check; 
	}

	@Override
	public List<LinkedHashMap<String, Object>> getTbOpenmEntTypeByExcelDown(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.getTbOpenmEntTypeByExcelDown(qualityStdInfo);
	}

	@Override
	public int deleteTbOpenmEntType(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.deleteTbOpenmEntType(qualityStdInfo);
	}

	@Override
	public List<QualityStdInfo> getQualityCheckManagement(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.getQualityCheckManagement(qualityStdInfo);
	}

	@Override
	public int saveQualityCheckManagement(QualityStdInfo qualityStdInfo) throws Exception {
		int check = 0;
		if (qualityStdInfo.getCrud_flag().equals("C")) {
			check = qualityStdInfoDao.checkCdPkFromTbOpenmQaindi(qualityStdInfo);
		}
		if (check > 0) {
			throw new Exception(" 품질점검 지표코드 [ " + qualityStdInfo.getQty_chk_idt_cd() + " ]은(는) 이미 존재합니다.");
		} else {
			check = qualityStdInfoDao.saveQualityCheckManagement(qualityStdInfo);
		}
		return check;
	}

	@Override
	public int deleteQualityCheckManagement(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.deleteQualityCheckManagement(qualityStdInfo);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getQualityCheckManagementByExcelDown(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.getQualityCheckManagementByExcelDown(qualityStdInfo);
	}

	@Override
	public List<QualityStdInfo> getQualityRevExcManagement(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.getQualityRevExcManagement(qualityStdInfo);
	}

	@Override
	public int saveQualityRevExcManagement(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.saveQualityRevExcManagement(qualityStdInfo);
	}

	@Override
	public int deleteQualityRevExcManagement(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.deleteQualityRevExcManagement(qualityStdInfo);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getQualityRevExcManagementByExcelDown(QualityStdInfo qualityStdInfo) {
		return qualityStdInfoDao.getQualityRevExcManagementByExcelDown(qualityStdInfo);
	}

	@Override
	public List<QualityStdInfo> selectCombobox(QualityStdInfo qualityStdInfo, String selectCombo) {
		if (selectCombo.equals("SelectCombobox1")) {
			return qualityStdInfoDao.selectCombobox1(qualityStdInfo);
		} else if (selectCombo.equals("SelectCombobox3")) {
			return qualityStdInfoDao.selectCombobox3(qualityStdInfo);
		} else if (selectCombo.equals("SelectCombobox4")) {
			return qualityStdInfoDao.selectCombobox4(qualityStdInfo);
		} else if (selectCombo.equals("SelectCombobox5")) {
			return qualityStdInfoDao.selectCombobox5(qualityStdInfo);
		}
		return null;
	}

	@Override
	public Result qualityRevExcMngByExcelUpload(MultipartFile file) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		ExcelReadOption option = new ExcelReadOption();
		Result result = new Result();
		int resultCount = 0;
		String filePath = "";

		// 1. 엑셀 파일 업로드
		try {
			filePath = fileMng.uploadExcel(file);
		} catch (Exception ex) {
			logger.error("엑셀 파일 업로드 error => " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}

		option.setFilePath(filePath);
		option.setStartRow(2);

		int errCnt = 0;
		int totalCnt = 0;
		String errMsg = null;
		Map<String, Object> errObj = new HashMap<String, Object>(); // 최종 err
																	// 정보를 담을
																	// obj
		List<QualityStdInfo> errList = new ArrayList<QualityStdInfo>();

		Vector<Integer> errIndexs = new Vector<Integer>(); // err 로우를 담을 obj
		int errIndex = 1; // 엑셀의 몇번째 로우에서 에러나는지 알려주기위한 용도.
		QualityStdInfo qualityStdInfo = null;
		try {
			List<Map<String, String>> excelContent = ExcelRead.read(option);
			totalCnt = excelContent.size();

			for (Map<String, String> article : excelContent) {
				errIndex += 1;

				qualityStdInfo = new QualityStdInfo();
				qualityStdInfo.setQty_chk_idt_cd(StringUtils.defaultString(article.get("A")));
				qualityStdInfo.setObj_type_cd(StringUtils.defaultString(article.get("B")));
				qualityStdInfo.setLib_nm(StringUtils.defaultString(article.get("C")));
				qualityStdInfo.setModel_nm(StringUtils.defaultString(article.get("D")));
				qualityStdInfo.setSub_nm(StringUtils.defaultString(article.get("E")));
				qualityStdInfo.setEnt_nm(StringUtils.defaultString(article.get("F")));
				qualityStdInfo.setAtt_nm(StringUtils.defaultString(article.get("G")));
				qualityStdInfo.setRemark(StringUtils.defaultString(article.get("H")));
				qualityStdInfo.setRqpn(StringUtils.defaultString(article.get("I")));

				int check = checkByteMethodForQualityRevExcMng(qualityStdInfo);

				if (check > 0) {
					System.out.println("################# 에러발생시 ################# ");
					System.out.println(article.get("A") + "\t" + article.get("B") + "\t" + article.get("C") + "\t"
							+ article.get("D") + "\t" + article.get("E") + "\t" + article.get("F") + "\t"
							+ article.get("G") + "\t" + article.get("H") + "\t" + article.get("I") + "\t");
					errList.add(qualityStdInfo);
					errIndexs.add(errIndex);
					errCnt += 1;
				} else if (check == -1) {
					System.out.println("################# 에러발생시 ################# ");
					System.out.println(article.get("A") + "\t" + article.get("B") + "\t" + article.get("C") + "\t"
							+ article.get("D") + "\t" + article.get("E") + "\t" + article.get("F") + "\t"
							+ article.get("G") + "\t" + article.get("H") + "\t" + article.get("I") + "\t");
					errList.add(qualityStdInfo);
					errIndexs.add(errIndex);
					errMsg = "[품질점검 지표코드], [예외대상 객체구분]은 필수 입력값입니다.";
					errCnt += 1;
				} else {
					resultCount += qualityStdInfoDao.saveQualityRevExcManagement(qualityStdInfo);
				}
			}

			errObj.put("totalCnt", totalCnt); // 사이즈
			errObj.put("isErr", errCnt > 0 ? true : false);
			errObj.put("errCnt", errCnt);
			errObj.put("succCnt", totalCnt - errCnt);
			errObj.put("errList", errList);
			errObj.put("errIndex", errIndexs.toString());
			if (errMsg != null && !errMsg.equals("")) {
				errObj.put("errMsg", errMsg);
			}
			result.setObject(errObj);
			result.setMessage("success");
			result.setResult(true);
		} catch (Exception ex) {
			logger.error("엑셀 파일 조회 error => " + ex.getMessage());
			ex.printStackTrace();
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", true);
			errObj.put("errList", errList);
			errObj.put("errCnt", errCnt);
			errObj.put("succCnt", totalCnt - errCnt);
			errObj.put("errIndex", errIndexs.toString());
			result.setObject(errObj);
			result.setMessage("엑셀 업로드가 실패하였습니다. <BR/>총 [ " + totalCnt + " ] 건 중  [ " + (errIndex - 2) + " ] 건 성공 <BR/>"
					+ "[ " + errIndex + " ] 번째 행에서 에러가 발생하였습니다.<BR/>" + "[ 품질점검 지표코드 : "
					+ qualityStdInfo.getQty_chk_idt_cd() + ", " + "예외대상 객체구분 : " + qualityStdInfo.getObj_type_cd()
					+ " ]<BR/> " + ex.getMessage());
			result.setResult(false);
			// throw new Exception();
			// throw ex;
		}

		return result;
	}

	private int checkByteMethodForQualityRevExcMng(QualityStdInfo qualityStdInfo) {
		int check = 0;
		check += qualityStdInfo.getQty_chk_idt_cd().getBytes().length <= 10 ? 0 : 1;
		check += qualityStdInfo.getObj_type_cd().getBytes().length <= 1 ? 0 : 1;
		check += qualityStdInfo.getLib_nm().getBytes().length <= 100 ? 0 : 1;
		check += qualityStdInfo.getModel_nm().getBytes().length <= 100 ? 0 : 1;
		check += qualityStdInfo.getSub_nm().getBytes().length <= 100 ? 0 : 1;
		check += qualityStdInfo.getEnt_nm().getBytes().length <= 100 ? 0 : 1;
		check += qualityStdInfo.getAtt_nm().getBytes().length <= 100 ? 0 : 1;
		check += qualityStdInfo.getRemark().getBytes().length <= 4000 ? 0 : 1;
		check += qualityStdInfo.getRqpn().getBytes().length <= 100 ? 0 : 1;

		check = qualityStdInfo.getQty_chk_idt_cd().trim().equals("") ? -1 : check;
		check = qualityStdInfo.getObj_type_cd().trim().equals("") ? -1 : check;
		return check;
	}

	private int checkByteMethodForBusinessClassMng(OpenmBizCls openmBizCls) {
		int check = 0;
		check += openmBizCls.getLib_nm().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getModel_nm().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getSub_nm().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getSys_nm().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getSys_cd().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getMain_biz_cls_nm().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getMain_biz_cls_cd().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getMid_biz_cls_nm().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getMid_biz_cls_cd().getBytes().length <= 100 ? 0 : 1;
		check += openmBizCls.getBiz_desc().getBytes().length <= 4000 ? 0 : 1;
		check += openmBizCls.getRemark().getBytes().length <= 4000 ? 0 : 1;

		check = openmBizCls.getLib_nm().trim().equals("") ? -1 : check;
		check = openmBizCls.getModel_nm().trim().equals("") ? -1 : check;
		check = openmBizCls.getSub_nm().trim().equals("") ? -1 : check;
		return check;
	}

	private String key = "openmade";

	@Override
	public List<OpenmBizCls> businessClassMngList(OpenmBizCls openmBizCls) throws Exception {
		return qualityStdInfoDao.businessClassMngList(openmBizCls);
	}

	@Override
	public int saveBusinessClassMng(OpenmBizCls openmBizCls) throws Exception {
		return qualityStdInfoDao.saveBusinessClassMng(openmBizCls);
	}

	@Override
	public int deleteBusinessClassMng(OpenmBizCls openmBizCls) {
		return qualityStdInfoDao.deleteBusinessClassMng(openmBizCls);
	}

	@Override
	public List<LinkedHashMap<String, Object>> businessClassMngListByExcelDown(OpenmBizCls openmBizCls) {
		return qualityStdInfoDao.businessClassMngListByExcelDown(openmBizCls);
	}

	@Override
	public Result businessClassMngByExcelUpload(MultipartFile file) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		ExcelReadOption option = new ExcelReadOption();
		Result result = new Result();
		int resultCount = 0;
		String filePath = "";

		// 1. 엑셀 파일 업로드
		try {
			filePath = fileMng.uploadExcel(file);
		} catch (Exception ex) {
			logger.error("엑셀 파일 업로드 error => " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}

		option.setFilePath(filePath);
		option.setStartRow(2);

		int errCnt = 0;
		int totalCnt = 0;
		String errMsg = null;
		Map<String, Object> errObj = new HashMap<String, Object>(); // 최종 err
																	// 정보를 담을
																	// obj
		List<OpenmBizCls> errList = new ArrayList<OpenmBizCls>();

		Vector<Integer> errIndexs = new Vector<Integer>(); // err 로우를 담을 obj
		int errIndex = 1; // 엑셀의 몇번째 로우에서 에러나는지 알려주기위한 용도.
		OpenmBizCls openmBizCls = null;
		try {
			List<Map<String, String>> excelContent = ExcelRead.read(option);
			totalCnt = excelContent.size();

			for (Map<String, String> article : excelContent) {
				errIndex += 1;

				openmBizCls = new OpenmBizCls();
				openmBizCls.setLib_nm(StringUtils.defaultString(article.get("A")));
				openmBizCls.setModel_nm(StringUtils.defaultString(article.get("B")));
				openmBizCls.setSub_nm(StringUtils.defaultString(article.get("C")));
				openmBizCls.setSys_nm(StringUtils.defaultString(article.get("D")));
				openmBizCls.setSys_cd(StringUtils.defaultString(article.get("E")));
				openmBizCls.setMain_biz_cls_nm(StringUtils.defaultString(article.get("F")));
				openmBizCls.setMain_biz_cls_cd(StringUtils.defaultString(article.get("G")));
				openmBizCls.setMid_biz_cls_nm(StringUtils.defaultString(article.get("H")));
				openmBizCls.setMid_biz_cls_cd(StringUtils.defaultString(article.get("I")));
				openmBizCls.setBiz_desc(StringUtils.defaultString(article.get("J")));
				openmBizCls.setRemark(StringUtils.defaultString(article.get("K")));

				int check = checkByteMethodForBusinessClassMng(openmBizCls);

				if (check > 0) {
					System.out.println("################# 에러발생시 ################# ");
					System.out.println(article.get("A") + "\t" + article.get("B") + "\t" + article.get("C") + "\t"
							+ article.get("D") + "\t" + article.get("E") + "\t" + article.get("F") + "\t"
							+ article.get("G") + "\t" + article.get("H") + "\t" + article.get("I") + "\t");
					errList.add(openmBizCls);
					errIndexs.add(errIndex);
					errCnt += 1;
				} else if (check == -1) {
					System.out.println("################# 에러발생시 ################# ");
					System.out.println(article.get("A") + "\t" + article.get("B") + "\t" + article.get("C") + "\t"
							+ article.get("D") + "\t" + article.get("E") + "\t" + article.get("F") + "\t"
							+ article.get("G") + "\t" + article.get("H") + "\t" + article.get("I") + "\t");
					errList.add(openmBizCls);
					errIndexs.add(errIndex);
					errCnt += 1;
					errMsg = "[라이브러리명], [모델명], [주제영역명]은 필수입력값입니다.";
				} else {
					resultCount += qualityStdInfoDao.saveBusinessClassMng(openmBizCls);
				}
			}

			errObj.put("totalCnt", totalCnt); // 사이즈
			errObj.put("isErr", errCnt > 0 ? true : false);
			errObj.put("errCnt", errCnt);
			errObj.put("succCnt", totalCnt - errCnt);
			errObj.put("errList", errList);
			errObj.put("errIndex", errIndexs.toString());
			if (errMsg != null && !errMsg.equals("")) {
				errObj.put("errMsg", errMsg);
			}
			result.setObject(errObj);
			result.setMessage("success");
			result.setResult(true);
		} catch (Exception ex) {
			logger.error("엑셀 파일 조회 error => " + ex.getMessage());
			ex.printStackTrace();
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", true);
			errObj.put("errList", errList);
			errObj.put("errCnt", errCnt);
			errObj.put("succCnt", totalCnt - errCnt);
			errObj.put("errIndex", errIndexs.toString());
			result.setObject(errObj);
			result.setMessage("엑셀 업로드가 실패하였습니다. <BR/>총 [ " + totalCnt + " ] 건 중  [ " + (errIndex - 2) + " ] 건 성공 <BR/>"
					+ "[ " + errIndex + " ] 번째 행에서 에러가 발생하였습니다.<BR/>" + "[ 라이브러리명 : " + openmBizCls.getLib_nm() + ", "
					+ "모델명 : " + openmBizCls.getModel_nm() + ", " + "주제영역명" + openmBizCls.getSub_nm() + " ]<BR/> "
					+ ex.getMessage());
			result.setResult(false);
			// throw new Exception();
			// throw ex;
		}

		return result;
	}

	@Override
	public List<OpenmQaindi> openmQaindiList(OpenmQaindi openmQaindi) throws Exception {
		return qualityStdInfoDao.openmQaindiList(openmQaindi);
	}

	@Override
	public List<OpenmQtyChkSql> qualityCheckSqlList(OpenmQtyChkSql openmQtyChkSql) throws Exception {
		List<OpenmQtyChkSql> openmQtyChkSqlList = null;
		List<OpenmQtyChkSql> openmQtyChkSqlListOut = null;
		// openmQtyChkSqlListOut = openmQtyChkSqlList;
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();

		if(user_auth_id.equals("9")){
			//OPENPOP-MANAGER 일 경우
			openmQtyChkSqlList = qualityStdInfoDao.qualityCheckSqlList(openmQtyChkSql);
			openmQtyChkSqlListOut = new ArrayList<OpenmQtyChkSql>();
			for (OpenmQtyChkSql openmQtyChkSqlRow : openmQtyChkSqlList) {
				// String qty_chk_sql =
				// CryptoUtil.decryptAES256(openmQtyChkSqlRow.getQty_chk_sql(),key);
				String qty_chk_sql = null;
				try {
					qty_chk_sql = CryptoUtil.decryptAES128(openmQtyChkSqlRow.getQty_chk_sql(), key);
				} catch (Exception e) {
					qty_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
				}
				openmQtyChkSqlRow.setQty_chk_sql(qty_chk_sql);
				openmQtyChkSqlListOut.add(openmQtyChkSqlRow);
			}
		}else{
			openmQtyChkSqlListOut = qualityStdInfoDao.qualityCheckSqlList(openmQtyChkSql);
		}

		return openmQtyChkSqlListOut;
	}

	@Override
	public int saveQualityCheckSql(OpenmQtyChkSql openmQtyChkSql) throws Exception {

		OpenmQtyChkSql openmQtyChkSqlCheck = null;
		HashMap<String, Object> param = new HashMap<String, Object>();

		param.put("qty_chk_idt_cd", openmQtyChkSql.getQty_chk_idt_cd());

		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String qty_chk_sql = null;
		
		if(user_auth_id.equals("9")){
			// String qty_chk_sql =
			// CryptoUtil.encryptAES256(openmQtyChkSql.getQty_chk_sql(),key);
			qty_chk_sql = CryptoUtil.encryptAES128(openmQtyChkSql.getQty_chk_sql(), key);
		} else {
			qty_chk_sql = openmQtyChkSql.getQty_chk_sql().trim().replaceAll("\n","");
		}

		// System.out.println("qty_chk_sql : encryptAES256 : " + qty_chk_sql);
		// System.out.println("qty_chk_sql : decryptAES256 : " +
		// CryptoUtil.decryptAES256(qty_chk_sql,key));

		// String qty_chk_sql_hex =
		// CryptoUtil.byteToHexString(qty_chk_sql.getBytes());

		// System.out.println("qty_chk_sql_hex : " +
		// CryptoUtil.decryptAES256(qty_chk_sql,key));

		param.put("qty_chk_sql", qty_chk_sql);
		param.put("dml_yn", openmQtyChkSql.getDml_yn());

		// openmQtyChkSql.setQty_chk_sql(CryptoUtil.encryptAES256(openmQtyChkSql.getQty_chfk_sql(),key));

		if (openmQtyChkSql.getCrud_flag().equals("C")){
				openmQtyChkSqlCheck = qualityStdInfoDao.qualityCheckSql(openmQtyChkSql);
			if (openmQtyChkSqlCheck != null && openmQtyChkSqlCheck.getQty_chk_idt_cd().equals(openmQtyChkSql.getQty_chk_idt_cd())) {
				throw new Exception(" 품질점검 지표코드 [ " + openmQtyChkSql.getQty_chk_idt_cd() + " ]은(는) 이미 존재합니다.");
			}
			return qualityStdInfoDao.insertQualityCheckSql(param);
		} else {
			return qualityStdInfoDao.saveQualityCheckSql(param);
		}

	}

	@Override
	public int deleteQualityCheckSql(OpenmQtyChkSql openmQtyChkSql) {
		return qualityStdInfoDao.deleteQualityCheckSql(openmQtyChkSql);
	}

	@Override
	public List<Map<String, Object>> qualityCheckSqlListByExcelDown(OpenmQtyChkSql openmQtyChkSql) {
		List<Map<String, Object>> openmQtyChkSqlList = null;
		List<Map<String, Object>> resultList = null;
		String qty_chk_sql = "";
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();

			//OPENPOP-MANAGER 일 경우
			resultList = new ArrayList<Map<String, Object>>();
			openmQtyChkSqlList = qualityStdInfoDao.qualityCheckSqlListByExcelDown(openmQtyChkSql);
			
			for (Map<String, Object> openmQtyChkSqlRow : openmQtyChkSqlList) {
				try {

					CLOB clob = (oracle.sql.CLOB) openmQtyChkSqlRow.get("QTY_CHK_SQL");

					// logger.debug("qualityCheckSqlListByExcelDown Start 3 : " +
					// clob.stringValue());

					if(user_auth_id.equals("9")){
					//OPENPOP-MANAGER 일 경우	
						
						// qty_chk_sql =
						// CryptoUtil.decryptAES256(clob.stringValue(),key);
						qty_chk_sql = CryptoUtil.decryptAES128(clob.stringValue(), key);
						//logger.debug("QTY_CHK_SQL : " + qty_chk_sql);
					}else{
						qty_chk_sql = clob.stringValue();
					}
					
				} catch (Exception e) {
					qty_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
					logger.error("Error : " + e.getMessage());
				}
				openmQtyChkSqlRow.put("QTY_CHK_SQL", qty_chk_sql);
				resultList.add(openmQtyChkSqlRow);
			}
	
		// return
		// qualityStdInfoDao.qualityCheckSqlListByExcelDown(openmQtyChkSql);
		return resultList;
	}

	@Override
	public int saveProjectQualityCheckRuleMng(OpenmQtyChkSql openmQtyChkSql, String switchCode) throws Exception {
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String qty_chk_sql = null;
		int check = 0;
		logger.debug("switchCode : "+switchCode);
		/**********
		 신규(그리드의 적용여부 = ‘미적용’) and ‘적용여부‘ 콤보 ‘적용’ 선택 후 등록
			switchCode = 1
		변경(그리드의 적용여부 = ‘적용’) and ‘적용여부‘ 콤보 ‘적용’ 선택 후 등록
			switchCode = 2
		변경(그리드의 적용여부 = ‘적용’ and ‘적용여부‘ 콤보 ‘미적용’ 선택 후 등록
			switchCode = 3
		***********/
		
		if(user_auth_id.equals("9")){
			// String qty_chk_sql =
			// CryptoUtil.encryptAES256(openmQtyChkSql.getQty_chk_sql(),key);
			qty_chk_sql = CryptoUtil.encryptAES128(openmQtyChkSql.getQty_chk_sql(), key);
			openmQtyChkSql.setQty_chk_sql(qty_chk_sql);
		} else {
			qty_chk_sql = openmQtyChkSql.getQty_chk_sql().trim().replaceAll("\n","");
			openmQtyChkSql.setQty_chk_sql(qty_chk_sql);
		}
		
		if(switchCode.equals("1"))
			check = qualityStdInfoDao.saveProjectQualityCheckRuleMng1(openmQtyChkSql);
		else if(switchCode.equals("2"))
			check = qualityStdInfoDao.saveProjectQualityCheckRuleMng2(openmQtyChkSql);
		else if(switchCode.equals("3"))
			check = qualityStdInfoDao.saveProjectQualityCheckRuleMng3(openmQtyChkSql);
		else
			throw new Exception("정의되지 않은 작업 유형입니다. <br/>시스템 관리자에게 문의바랍니다.");
		
		return check;
	}

	@Override
	public List<OpenmQtyChkSql> projectQualityCheckRuleMngList(OpenmQtyChkSql openmQtyChkSql) throws Exception {
		List<OpenmQtyChkSql> openmQtyChkSqlList = null;
		List<OpenmQtyChkSql> openmQtyChkSqlListOut = null;
		// openmQtyChkSqlListOut = openmQtyChkSqlList;
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();

		if(user_auth_id.equals("9")){
			//OPENPOP-MANAGER 일 경우
			openmQtyChkSqlList = qualityStdInfoDao.projectQualityCheckRuleMngList(openmQtyChkSql);
			openmQtyChkSqlListOut = new ArrayList<OpenmQtyChkSql>();
			for (OpenmQtyChkSql openmQtyChkSqlRow : openmQtyChkSqlList) {
				// String qty_chk_sql =
				// CryptoUtil.decryptAES256(openmQtyChkSqlRow.getQty_chk_sql(),key);
				String qty_chk_sql = "";
				try {
					qty_chk_sql = CryptoUtil.decryptAES128(openmQtyChkSqlRow.getQty_chk_sql(), key);	
				} catch (Exception e) {
					qty_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
				}
				openmQtyChkSqlRow.setQty_chk_sql(qty_chk_sql);
				openmQtyChkSqlListOut.add(openmQtyChkSqlRow);
			}
		}else{
			openmQtyChkSqlListOut = qualityStdInfoDao.projectQualityCheckRuleMngList(openmQtyChkSql);
		}

		return openmQtyChkSqlListOut;
	}

	@Override
	public List<Map<String, Object>> ProjectQualityCheckRuleMngByExcelDown(OpenmQtyChkSql openmQtyChkSql) {
		List<Map<String, Object>> openmQtyChkSqlList = null;
		List<Map<String, Object>> resultList = null;
		String qty_chk_sql = "";
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();

			//OPENPOP-MANAGER 일 경우
			resultList = new ArrayList<Map<String, Object>>();
			openmQtyChkSqlList = qualityStdInfoDao.projectQualityCheckRuleMngByExcelDown(openmQtyChkSql);
			
			for (Map<String, Object> openmQtyChkSqlRow : openmQtyChkSqlList) {
				try {

					CLOB clob = (oracle.sql.CLOB) openmQtyChkSqlRow.get("QTY_CHK_SQL");

					// logger.debug("qualityCheckSqlListByExcelDown Start 3 : " +
					// clob.stringValue());

					if(user_auth_id.equals("9")){
					//OPENPOP-MANAGER 일 경우	
						
						// qty_chk_sql =
						// CryptoUtil.decryptAES256(clob.stringValue(),key);
						qty_chk_sql = CryptoUtil.decryptAES128(clob.stringValue(), key);
						//logger.debug("QTY_CHK_SQL : " + qty_chk_sql);
					}else{
						qty_chk_sql = clob.stringValue();
					}
					
				} catch (Exception e) {
					logger.error("Error : " + e.getMessage());
					qty_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
				}
				openmQtyChkSqlRow.put("QTY_CHK_SQL", qty_chk_sql);
				resultList.add(openmQtyChkSqlRow);
			}
	
		// return
		// qualityStdInfoDao.qualityCheckSqlListByExcelDown(openmQtyChkSql);
		return resultList;
	}

	@Override
	public int applyProjectQualityCheckRuleMng(OpenmQtyChkSql openmQtyChkSql) throws Exception {
		int check = 0;
		check = qualityStdInfoDao.checkTbOpenmQaindi(openmQtyChkSql);
		
		if(check > 0){
			throw new Exception("이미 프로젝트 품질점검 지표 및 RULE이 등록되어 있습니다.");
		}else{
			check = 0;
			check = qualityStdInfoDao.insertTbOpenmProjectQtyChkSql(openmQtyChkSql);
		}
		return check;
	}
	
}
