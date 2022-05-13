package omc.spop.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;

/** 
	* @packageName	:	omc.spop.service 
	* @fileName		:	SqlDiagnosisReportService.java 
	* @author 		:	OPEN MADE (wonjae kim) 
	* @description	:	SQL 품질 진단 스케줄러 관리
	* @History		
	============================================================
	2021.09.23        wonjae kim 				최초 생성
	============================================================
*/
public interface SQLDiagnosisReportManageSchedulerService {
	
	/** 
		* @methodName	: 	selectSQLDiagnosisReportScheduler 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param jobSchedulerConfigDetail
		* @return SQL 품질 진단 스케줄러 리스트 조회(ArrayList[JobSchedulerConfigDetail])
		* @throws Exception
	*/
	public ArrayList<JobSchedulerBase> selectSQLDiagnosisReportScheduler(JobSchedulerBase jobSchedulerBase) throws Exception;
	
	/** 
		* @methodName	: 	insertSQLDiagnosisReportScheduler 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param jobSchedulerConfigDetail
		* @return SQL 품질 진단 스케줄러 데이터 삽입
		* @throws Exception
	*/
	public int insertSQLDiagnosisReportScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;
	
	/** 
		* @methodName	: 	insertJobSchedulerConfigDetail 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param jobSchedulerConfigDetail
		* @return insertJobSchedulerConfigDetail 데이터 삽입
		* @throws Exception
	*/
	public int insertJobSchedulerConfigDetail(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;

	/** 
		* @methodName	: 	updateSQLDiagnosisReportScheduler 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param jobSchedulerConfigDetail
		* @return SQL 품질 진단 스케줄러 데이터 수정
		* @throws Exception
	*/
	public int updateSQLDiagnosisReportScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;
	/** 
		* @methodName	: 	updateJobSchedulerConfigDetail 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param jobSchedulerConfigDetail
		* @return
		* @throws Exception
		* @History updateJobSchedulerConfigDetail 데이터 수정		
	*/
	public int updateJobSchedulerConfigDetail(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;

	public int deleteSQLDiagnosisReportScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception;
	
	public List<LinkedHashMap<String, Object>> excelDownload(JobSchedulerBase jobSchedulerBase) throws Exception;
	/** 
		* @methodName	: 	isUpdateY 
		* @author		: 	OPEN MADE (wonjae kim) 
		* @param jobSchedulerConfigDetail
		* @return
		* @throws ParseException
		* @History	현재시간과 크론탭에 비교하여 현재시간 포함 이후에 스케줄링이 되는지 확인	
	*/
	public boolean isUpdateY(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws ParseException;
	
}
