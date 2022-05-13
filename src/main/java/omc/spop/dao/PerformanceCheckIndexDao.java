package omc.spop.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.DeployPerfChkIndc;
import omc.spop.model.SelftunPlanTable;

/***********************************************************
 * 2019.01.03 임호경 최초작성
 **********************************************************/

public interface PerformanceCheckIndexDao {

	List<DeployPerfChkIndc> getDeployPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckMethCd(DeployPerfChkIndc deployPerfChkIndc);

	int saveDeployPrefChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getDeployPerfChkIndcByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckProgramDivCd(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getYnDecideDivCd(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckIndcId(DeployPerfChkIndc deployPerfChkIndc);

	int insertWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	int updateWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	int insertWjPerfChkIndcHistory(DeployPerfChkIndc deployPerfChkIndc);

	int checkWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	int deleteWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getWjPerfChkIndcByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	int checkDeployPerfChkDetailResult(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getProPerfExcReq(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getBindTableList(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckResultTableList(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getDeployPerfChkStep(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getDeployPerfChkStepTestDB(DeployPerfChkIndc deployPerfChkIndc);

	int checkStepOrderingCnt(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkStep(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkStep(DeployPerfChkIndc deployPerfChkIndc);

	int checkDeployPerfChkCnt(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getDeployPerfChkStepByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkStepTestDB(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkStepTestDB(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getDeployPerfChkStepId(DeployPerfChkIndc deployPerfChkIndc);

	int checkDeployPerfChkStep(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkExcptHistory(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkExcptRequest(DeployPerfChkIndc deployPerfChkIndc);

	int checkExceptionRequestId(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkExcptRequest(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getDeployPerfChkDetailResultByTab(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getDeployPerfChkDetailResult(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getDeployPerfChkDetailResultByTabEspc(DeployPerfChkIndc deployPerfChkIndc);

	int insertDpPerfChkIndcExcptHistory(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkIndcExcpt(DeployPerfChkIndc deployPerfChkIndc);
	
	int deleteDpPerfChkIndcExcptHistory2(DeployPerfChkIndc deployPerfChkIndc);
	
	int insertDpPerfChkIndcExcptHistory2(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkIndcExcpt2(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkIndcExcpt(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkIndcExcpt2(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkAllPgm(DeployPerfChkIndc deployPerfChkIndc);

	int checkExceptionRequestCnt(DeployPerfChkIndc deployPerfChkIndc);

	String getProgramSourceDesc(DeployPerfChkIndc deployPerfChkIndc);

	DeployPerfChkIndc checkExceptionPrcStatusCd(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkExcptRequest2(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkTargetPgm(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getProPerfExcReqDel(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckResultDelTableList(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkTargetPgm2(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkExcptRequest3(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getProPerfExcReqByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckStep(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getProPerfExcReqDelByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getDeployPerfChkParsingSchema(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getDeployPerfChkParsingSchemaByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	int insertDeployPerfChkParsingSchema(DeployPerfChkIndc deployPerfChkIndc);

	int checkDeployPerfChkParsingSchemaName(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkParsingSchema(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getDeployPerfChkStepTestDBByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	int checkDeployPerfChkParsingSchemaPk(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfChkIndcListState(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfChkIndcListState2(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getPerfChkIndcListState2ByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getPerfChkIndcListStateByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getProPerfExcReqState(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckResultStateTableList(DeployPerfChkIndc deployPerfChkIndc);

	int updateDeployPerfChkParsingSchemaAll(DeployPerfChkIndc deployPerfChkIndc);

	int checkDeployPerfChkStepDelYnCnt(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getProPerfExcReqStateByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckState0(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckState1(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getPerfCheckState2(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getPerfCheckState0ByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getPerfCheckState1ByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	List<LinkedHashMap<String, Object>> getPerfCheckState2ByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	DeployPerfChkIndc checkExceptionPrcStatusCd2(DeployPerfChkIndc deployPerfChkIndc);

	int checkDeployPerfChkDetailResult2(DeployPerfChkIndc deployPerfChkIndc);

	HashMap<String, Object> checkDeployPerfChkCnt2(DeployPerfChkIndc deployPerfChkIndc);

	List<SelftunPlanTable> getDeployPerfChkPlanTableList(DeployPerfChkIndc deployPerfChkIndc);

}
