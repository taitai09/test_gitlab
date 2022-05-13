package omc.mqm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.OpenmQtyChkSql;
import omc.mqm.model.QualityStdInfo;
import omc.spop.model.Result;

/***********************************************************
 * 2019.05.22	임호경	최초작성
 **********************************************************/

public interface QualityStdInfoService {

	/* 엔터티 유형 관리 - 리스트*/
	List<QualityStdInfo> getTbOpenmEntType(QualityStdInfo qualityStdInfo);

	/* 엔터티 유형 관리 - 저장 */
	int saveTbOpenmEntType(QualityStdInfo qualityStdInfo) throws Exception;
	
	/* 엔터티 유형 관리 - 삭제 */
	int deleteTbOpenmEntType(QualityStdInfo qualityStdInfo);

	/* 엔터티 유형 관리 - 엑셀다운 */
	List<LinkedHashMap<String, Object>> getTbOpenmEntTypeByExcelDown(QualityStdInfo qualityStdInfo);
	
	/* 품질검토 지표 관리 - 리스트*/
	List<QualityStdInfo> getQualityCheckManagement(QualityStdInfo qualityStdInfo);
	
	/* 품질검토 지표 관리 - 저장 */
	int saveQualityCheckManagement(QualityStdInfo qualityStdInfo) throws Exception;

	/* 품질검토 지표 관리 - 삭제 */
	int deleteQualityCheckManagement(QualityStdInfo qualityStdInfo);

	/* 품질검토 지표 관리 - 엑셀다운 */
	List<LinkedHashMap<String, Object>> getQualityCheckManagementByExcelDown(QualityStdInfo qualityStdInfo);
	
	/** 업무분류체계관리 - 엑셀 업로드 */
	Result businessClassMngByExcelUpload(MultipartFile file) throws Exception;

	/* 품질검토 예외 대상 관리  - 리스트*/
	List<QualityStdInfo> getQualityRevExcManagement(QualityStdInfo qualityStdInfo);
	
	/* 품질검토 예외 대상 관리  - 저장*/
	int saveQualityRevExcManagement(QualityStdInfo qualityStdInfo);

	/* 품질검토 예외 대상 관리  - 삭제*/
	int deleteQualityRevExcManagement(QualityStdInfo qualityStdInfo);
	
	/* 품질검토 예외 대상 관리  - 엑셀다운*/
	List<LinkedHashMap<String, Object>> getQualityRevExcManagementByExcelDown(QualityStdInfo qualityStdInfo);

	/* 품질검토 예외 대상 관리  - 콤보박스*/
	List<QualityStdInfo> selectCombobox(QualityStdInfo qualityStdInfo, String selectCombo);

	/* 품질검토 예외 대상 관리  - 엑셀 업로드*/
	Result qualityRevExcMngByExcelUpload(MultipartFile file) throws Exception;
	
	/** 업무 분류 체계 관리 리스트 */
	List<OpenmBizCls> businessClassMngList(OpenmBizCls openmBizCls) throws Exception;

	//** 업무 분류 체계 관리 save */
	int saveBusinessClassMng(OpenmBizCls openmBizCls) throws Exception;

	/** 업무 분류 체계 관리 삭제 */
	int deleteBusinessClassMng(OpenmBizCls openmBizCls);

	List<LinkedHashMap<String, Object>> businessClassMngListByExcelDown(OpenmBizCls openmBizCls);
	
	/** 모델품질지표기준 리스트 */
	List<OpenmQaindi> openmQaindiList(OpenmQaindi openmQaindi) throws Exception;
	
	/** 품질점검 SQL 관리 리스트 */
	List<OpenmQtyChkSql> qualityCheckSqlList(OpenmQtyChkSql openmQtyChkSql) throws Exception;
	
	//** 품질점검 SQL 관리 save */
	int saveQualityCheckSql(OpenmQtyChkSql openmQtyChkSql) throws Exception;

	/** 품질점검 SQL 관리 삭제 */
	int deleteQualityCheckSql(OpenmQtyChkSql openmQtyChkSql);

	List<Map<String, Object>> qualityCheckSqlListByExcelDown(OpenmQtyChkSql openmQtyChkSql);

	/** 프로젝트 구조 품질점검 지표/RULE 관리 
	 * @throws Exception */
	List<OpenmQtyChkSql> projectQualityCheckRuleMngList(OpenmQtyChkSql openmQtyChkSql) throws Exception;

	/** 프로젝트 구조 품질점검 지표/RULE 관리 저장 
	 * @param switchCode 
	 * @throws Exception */
	int saveProjectQualityCheckRuleMng(OpenmQtyChkSql openmQtyChkSql, String switchCode) throws Exception;

	/** 프로젝트 구조 품질점검 지표/RULE 관리 엑셀 다운 */
	List<Map<String, Object>> ProjectQualityCheckRuleMngByExcelDown(OpenmQtyChkSql openmQtyChkSql);

	/** 프로젝트 구조 품질점검 지표/RULE 관리 프로젝트 표준 적용 */
	int applyProjectQualityCheckRuleMng(OpenmQtyChkSql openmQtyChkSql) throws Exception;

	
}