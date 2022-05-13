package omc.spop.service;

import java.util.Map;

import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Status;
import omc.spop.model.server.Iqms;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public interface EspcService {
	Iqms cmConfirm(Iqms iqms) throws Exception;

	Iqms cmComplete(Iqms iqms) throws Exception;

	Iqms cmCancel(Iqms iqms) throws Exception;

	Iqms cmDelete(Iqms iqms) throws Exception;

	int cmStatusSync(Status status) throws Exception;

	int updateCheckResultAncYn(PerformanceCheckMng performanceCheckMng) throws Exception;

	String selectNextPerfCheckId() throws Exception;

	String selectBeforePerfCheckId(PerformanceCheckMng performanceCheckMng) throws Exception;

	int getValidateWrkjobDivCd(String wrkjobDivCd) throws Exception;
	
	String getValidateDeployID(String deployId) throws Exception;
	
	String generateIqmsResult(String cmStatus, String openpopStatus, Iqms iqms);
	
	Map<String,Object> setResponseData(Iqms iqms, String cmStatus);
}
