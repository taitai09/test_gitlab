package omc.mqm.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.OpenmQtyChkSql;
import omc.mqm.model.QualityStdInfo;

/***********************************************************
 * 2019.05.22	임호경	최초작성
 **********************************************************/

public interface QualityStdInfoDao {

	List<QualityStdInfo> getTbOpenmEntType(QualityStdInfo qualityStdInfo);

	int saveTbOpenmEntType(QualityStdInfo qualityStdInfo);

	List<LinkedHashMap<String, Object>> getTbOpenmEntTypeByExcelDown(QualityStdInfo qualityStdInfo);

	int deleteTbOpenmEntType(QualityStdInfo qualityStdInfo);

	List<QualityStdInfo> getQualityCheckManagement(QualityStdInfo qualityStdInfo);

	int saveQualityCheckManagement(QualityStdInfo qualityStdInfo);

	int deleteQualityCheckManagement(QualityStdInfo qualityStdInfo);

	List<LinkedHashMap<String, Object>> getQualityCheckManagementByExcelDown(QualityStdInfo qualityStdInfo);

	List<QualityStdInfo> getQualityRevExcManagement(QualityStdInfo qualityStdInfo);

	int saveQualityRevExcManagement(QualityStdInfo qualityStdInfo);

	int deleteQualityRevExcManagement(QualityStdInfo qualityStdInfo);

	List<LinkedHashMap<String, Object>> getQualityRevExcManagementByExcelDown(QualityStdInfo qualityStdInfo);

	List<QualityStdInfo> selectCombobox1(QualityStdInfo qualityStdInfo);

	List<QualityStdInfo> selectCombobox3(QualityStdInfo qualityStdInfo);

	List<QualityStdInfo> selectCombobox4(QualityStdInfo qualityStdInfo);

	List<QualityStdInfo> selectCombobox5(QualityStdInfo qualityStdInfo);	
	
	public List<OpenmBizCls> businessClassMngList(OpenmBizCls openmBizCls);
	
	public int saveBusinessClassMng(OpenmBizCls openmBizCls);
	
	public int deleteBusinessClassMng(OpenmBizCls openmBizCls);

	public List<LinkedHashMap<String, Object>> businessClassMngListByExcelDown(OpenmBizCls openmBizCls);

	public List<OpenmQaindi> openmQaindiList(OpenmQaindi openmQaindi);
	
	public List<OpenmQtyChkSql> qualityCheckSqlList(OpenmQtyChkSql openmQtyChkSql);
	
	public OpenmQtyChkSql qualityCheckSql(OpenmQtyChkSql openmQtyChkSql);

	public int insertQualityCheckSql(HashMap<String, Object> param);
	
	public int saveQualityCheckSql(HashMap<String, Object> param);
	
	public int deleteQualityCheckSql(OpenmQtyChkSql openmQtyChkSql);

	public List<Map<String, Object>> qualityCheckSqlListByExcelDown(OpenmQtyChkSql openmQtyChkSql);

	int checkCdPkFromTbOpenmQaindi(QualityStdInfo qualityStdInfo);

	int checkCdPkFromTbOpenmEntType(QualityStdInfo qualityStdInfo);

	List<OpenmQtyChkSql> projectQualityCheckRuleMngList(OpenmQtyChkSql openmQtyChkSql);

	int saveProjectQualityCheckRuleMng1(OpenmQtyChkSql openmQtyChkSql);

	int saveProjectQualityCheckRuleMng2(OpenmQtyChkSql openmQtyChkSql);

	int saveProjectQualityCheckRuleMng3(OpenmQtyChkSql openmQtyChkSql);

	List<Map<String, Object>> projectQualityCheckRuleMngByExcelDown(OpenmQtyChkSql openmQtyChkSql);

	int checkTbOpenmQaindi(OpenmQtyChkSql openmQtyChkSql);

	int insertTbOpenmProjectQtyChkSql(OpenmQtyChkSql openmQtyChkSql);

	List<OpenmQtyChkSql> qualityCheckSqlList2(OpenmQtyChkSql openmQtyChkSql);
	
}
