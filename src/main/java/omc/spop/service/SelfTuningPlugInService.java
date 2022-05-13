package omc.spop.service;

import java.util.List;

import omc.spop.model.ApmApplSql;
import omc.spop.model.ApmApplSqlPlugIn;
import omc.spop.model.Result;

/***********************************************************
 * 2018.03.23 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface SelfTuningPlugInService {
	/** 데이터베이스 리스트 of wrkjob_cd */
	List<ApmApplSql> databaseListOfWrkjobCd(ApmApplSql apmApplSql) throws Exception;
	
	/** 셀프튜닝 INSERT */
	String insertSelftunQuery(ApmApplSql apmApplSql) throws Exception;
	
	/** 셀프튜닝 - 셀프테스트 */
	Result startSelfTestNew(ApmApplSqlPlugIn apmApplSqlPlugIn, String selftunQuerySeq) throws Exception;
}
