package omc.spop.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;

/** 
	* @packageName	:	omc.spop.dao 
	* @fileName		:	SQLDiagnosisReportManageSchedulerDao.java 
	* @author 		:	OPEN MADE (wonjae kim) 
	* @description	: 	김원재 SQL 품질 진단 스케줄러 관리 DAO
	* @History		
	============================================================
	2021.09.23        wonjae kim 			history
	============================================================
*/
public interface SQLDiagnosisReportManageSchedulerDao {
	public ArrayList<JobSchedulerBase> selectSQLDiagnosisReportSchedulerList(JobSchedulerBase jobSchedulerBase) throws Exception;
	public List<LinkedHashMap<String, Object>> excelDownload(JobSchedulerBase jobSchedulerBase) throws Exception;

}
