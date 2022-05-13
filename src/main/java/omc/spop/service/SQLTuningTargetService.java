package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.OdsHistSnapshot;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.Result;
import omc.spop.model.SqlTuning;
import omc.spop.model.TopsqlAutoChoice;
import omc.spop.model.TopsqlHandopChoice;
import omc.spop.model.TuningTargetSql;

/***********************************************************
 * 2018.03.12	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface SQLTuningTargetService {
	/** 자동선정 리스트 */
	List<TopsqlAutoChoice> autoSelectionList(TopsqlAutoChoice topsqlAutoChoice) throws Exception;

	/** 자동선정 리스트 엑셀 다운로드
	 * @throws Exception */
	List<LinkedHashMap<String, Object>> autoSelectionList4Excel(TopsqlAutoChoice topsqlAutoChoice) throws Exception;	

	/** 자동선정 저장 */
	void saveAutoSelection(TopsqlAutoChoice topsqlAutoChoice) throws Exception;
	
	/** 자동선정 일괄 수정 */
	int bundleSaveAutoSelection(TopsqlAutoChoice topsqlAutoChoice) throws Exception;
	
	/** 자동선정 이력 리스트 */
	List<TopsqlAutoChoice> autoSelectionHistoryList(TopsqlAutoChoice topsqlAutoChoice) throws Exception;
	
	/** 자동 선정 현황 선정조건번호 리스트 */
	List<TopsqlAutoChoice> getChoiceCondNo(TopsqlAutoChoice topsqlAutoChoice) throws Exception;
	
	/** 자동선정현황 리스트 */
	List<TopsqlAutoChoice> autoSelectionStatusList(TopsqlAutoChoice topsqlAutoChoice) throws Exception;
	List<LinkedHashMap<String, Object>> autoSelectionStatusList4Excel(TopsqlAutoChoice topsqlAutoChoice) throws Exception;
	List<LinkedHashMap<String, Object>> autoSelectionStatusSearchList4Excel(TopsqlAutoChoice topsqlAutoChoice) throws Exception;

	/** 자동선정현황 상세 리스트 */
	List<TuningTargetSql> autoSelectionStatusDetailList(TuningTargetSql tuningTargetSql) throws Exception;
	List<LinkedHashMap<String, Object>> autoSelectionStatusDetailList4Excel(TuningTargetSql tuningTargetSql) throws Exception;

	/** 수동선정 리스트 */
	List<OdsHistSqlstat> manualSelectionList(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** 수동선정 리스트 엑셀 다운로드 */
	List<LinkedHashMap<String, Object>> manualSelectionList4Excel(OdsHistSqlstat odsHistSqlstat) throws Exception;

	/** 수동선정현황 리스트 */
	List<TopsqlHandopChoice> manualSelectionStatusList(TopsqlHandopChoice topsqlHandopChoice) throws Exception;
	
	/** 수동선정현황 상세 리스트 */
	List<TuningTargetSql> manualSelectionStatusDetailList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 수동선정 - 선정이력 팝업 */
	List<OdsHistSnapshot> manualSelectionHistoryList(OdsHistSnapshot odsHistSnapshot) throws Exception;
	
	/** 수동선정 - 튜닝담당자지정 팝업 */
	Result insertTuningRequest(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 수동선정 - 튜닝대상 저장 */
	String insertTopsqlHandopChoiceExec(TopsqlHandopChoice topsqlHandopChoice) throws Exception;	
	
	/** 튜닝일괄종료 팝업 */
	String endBathTuning(SqlTuning sqlTuning) throws Exception;
	
	/** 튜닝일괄종료 팝업 - (자동 선정 현황  - 검색) */
	String endBatchTuningBundle(SqlTuning sqlTuning) throws Exception;

	/** 수동선정현황 리스트 엑셀 다운*/
	List<LinkedHashMap<String, Object>> manualSelectionStatusListByExcelDown(TopsqlHandopChoice topsqlHandopChoice);

}
