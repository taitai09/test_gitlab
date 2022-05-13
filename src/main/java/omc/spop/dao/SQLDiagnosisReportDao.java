package omc.spop.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.spop.model.SQLDiagnosisReport;
import omc.spop.model.SQLStandards;
import omc.spop.model.SqlStandardOperationPlugIn;

/***********************************************************
 * 2021.06.21	김원재 OPENPOP V2 최초작업
 **********************************************************/
public interface SQLDiagnosisReportDao {
	
	public List<SqlStandardOperationPlugIn> selectSelfSqlStdQtyChkPgm(Map<String , String> map) throws Exception;
	/** 
		* @methodName	: 	selectSqlDiagnosisReportSchedulerNameList 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param jobSchedulerBase
		* @return
		* @throws Exception
		* @History 스케줄러 리스트 조회
	*/
	public ArrayList<SQLDiagnosisReport> selectSqlDiagnosisReportSchedulerNameList(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;
	/** 
		* @methodName	: 	selectSQLDiagnosisReportSchedulerSchedule 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param jobSchedulerBase
		* @return
		* @throws Exception
		* @History	진단일시, 수집 기간 조회
	*/
	public SQLDiagnosisReport selectSQLDiagnosisReportSchedulerSchedule(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;
	
	/** 
		* @methodName	: 	selectSqlDiagnosisRuleData 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param sqlDiagnosisReport
		* @return
		* @throws Exception
		* @History	좌측 그리드 -  RULE DB 등록 필요	
	*/
	public ArrayList<SQLDiagnosisReport> selectSqlDiagnosisRuleData(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;

	/** 
		* @methodName	: 	selectSqlDiagnosisReportDetailInfo 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param sqlDiagnosisReport
		* @return
		* @throws Exception
		* @History	우측 그리드 -  RULE DB 등록 필요
	*/
	public ArrayList<SQLDiagnosisReport> selectSqlDiagnosisReportDetailInfo(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;

	
	/** 
		* @methodName	: 	selectProgramSourceDesc 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param sqlDiagnosisReport
		* @return
		* @throws Exception
		* @History	우측 그리드 로우 선택시 사용자에게 보여줄 SQL TEXT SELECT
	*/
	public List<LinkedHashMap<String, Object>> selectProgramSourceDesc(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;

	public List<SQLStandards> loadQtyIdxByProject(SQLStandards sqlStandards) throws Exception;

	public SQLDiagnosisReport selectSchedulerStatus(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;
}
