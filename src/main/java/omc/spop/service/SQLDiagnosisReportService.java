package omc.spop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import omc.spop.model.JobSchedulerBase;
import omc.spop.model.SQLDiagnosisReport;
import omc.spop.model.SQLStandards;

/** 
	* @packageName	:	omc.spop.service 
	* @fileName		:	SqlDiagnosisReportService.java 
	* @author 		:	OPEN MADE (wonjae kim) 
	* @description	:	SQL 종합 진단 Service
	* @History		
	============================================================
	2021.06.09        wonjae kim 				최초 생성
	============================================================
*/
public interface SQLDiagnosisReportService {
	public ArrayList<SQLDiagnosisReport> selectSqlDiagnosisReportSchedulerNameList(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;
	public SQLDiagnosisReport selectSQLDiagnosisReportSchedulerSchedule(SQLDiagnosisReport sqlDiagnosisReport)throws Exception;
	public List<LinkedHashMap<String, Object>> selectSqlDiagnosisRuleData(SQLStandards sqlStandards) throws Exception;
	public List<LinkedHashMap<String, Object>> selectSqlDiagnosisReportDetailInfo(SQLStandards sqlStandards) throws Exception;
	public boolean excelDownloadResult(SQLStandards sqlStandards, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception;
	public List<LinkedHashMap<String, Object>> selectProgramSourceDesc(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;
	List<SQLStandards> loadIndexList(SQLStandards sqlStandards) throws Exception;
	public SQLDiagnosisReport selectSchedulerStatus(SQLDiagnosisReport sqlDiagnosisReport) throws Exception;
	public List<LinkedHashMap<String, Object>> loadQualityTable(SQLStandards sqlStandards , String dbNum , boolean paging) throws Exception;
}
