package omc.spop.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;

import omc.spop.dao.CommonDao;
import omc.spop.dao.SQLDiagnosisReportManageSchedulerDao;
import omc.spop.dao.SQLStandardsDao;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.service.SQLDiagnosisReportManageSchedulerService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/** 
	* @packageName	:	omc.spop.service.impl 
	* @fileName		:	SQLDiagnosisReportManageSchedulerServiceImpl.java 
	* @author 		:	OPEN MADE (wonjae kim) 
	* @description	: 	SQL 품질 진단 스케줄러 
	* @History		
	============================================================
	2021.09.23        wonjae kim 			최초 생성
	============================================================
*/
@Service("SQLDiagnosisReportManageSchedulerService")
public class SQLDiagnosisReportManageSchedulerServiceImpl implements SQLDiagnosisReportManageSchedulerService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	SQLDiagnosisReportManageSchedulerDao sqlDiagnosisReportManageSchedulerDao;
	
	@Autowired
	SQLStandardsDao sqlStandardsDao;

	@Autowired
	CommonDao commonDao;
	
	@Override
	public ArrayList<JobSchedulerBase> selectSQLDiagnosisReportScheduler(JobSchedulerBase jobSchedulerBase) throws Exception {
		ArrayList<JobSchedulerBase> resultList = sqlDiagnosisReportManageSchedulerDao.selectSQLDiagnosisReportSchedulerList(jobSchedulerBase);
		for(JobSchedulerBase obj : resultList) {
			if(obj.getSql_source_type_cd() != null) {
				if("1".equals(obj.getSql_source_type_cd())) {
					obj.setSql_source_type_nm("TOP");
				}else if("2".equals(obj.getSql_source_type_cd())){
					obj.setSql_source_type_nm("전체SQL");
				}
			}
		}
		return resultList;
	}

	@Override
	public int insertSQLDiagnosisReportScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		jobSchedulerConfigDetail.setProject_id("0");
		jobSchedulerConfigDetail.setParse_code("00.000.00");
		jobSchedulerConfigDetail.setJob_scheduler_type_cd("37");
		
		jobSchedulerConfigDetail.setGather_term_start_day(jobSchedulerConfigDetail.getGather_term_start_day().replaceAll("-",""));
		jobSchedulerConfigDetail.setGather_term_end_day(jobSchedulerConfigDetail.getGather_term_end_day().replaceAll("-",""));

		return sqlStandardsDao.insertSqlStdQtyScheduler(jobSchedulerConfigDetail); 
	}
	
	@Override
	public int insertJobSchedulerConfigDetail(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		String nowDate = DateUtil.getNowDate("yyyyMMdd");
		String nowTime = StringUtil.replace(DateUtil.getNextTime("P", "0"), ":", "");
		jobSchedulerConfigDetail.setUpd_dt(StringUtil.getDateFormatString(nowDate, nowTime));
		
		jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(jobSchedulerConfigDetail.getSql_std_qty_scheduler_no());

		
		return commonDao.insertJobSchedulerConfigDetail(jobSchedulerConfigDetail); 
	}
	
	@Override
	public int updateSQLDiagnosisReportScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		jobSchedulerConfigDetail.setParse_code("00.000.00");
		jobSchedulerConfigDetail.setGather_term_start_day(jobSchedulerConfigDetail.getGather_term_start_day().replaceAll("-",""));
		jobSchedulerConfigDetail.setGather_term_end_day(jobSchedulerConfigDetail.getGather_term_end_day().replaceAll("-",""));

		return sqlStandardsDao.updateSqlStdQtyScheduler(jobSchedulerConfigDetail); 

	}
	
	@Override
	public int updateJobSchedulerConfigDetail(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		jobSchedulerConfigDetail.setJob_scheduler_wrk_target_id(jobSchedulerConfigDetail.getSql_std_qty_scheduler_no());
		return commonDao.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail); 
	}
	
	@Override
	public int deleteSQLDiagnosisReportScheduler(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		return sqlStandardsDao.deleteScheduler(jobSchedulerConfigDetail); 
	}	

	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(JobSchedulerBase jobSchedulerBase) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = sqlDiagnosisReportManageSchedulerDao.excelDownload(jobSchedulerBase);
		for(LinkedHashMap<String, Object> obj : resultList) {
			if(obj.get("SQL_SOURCE_TYPE_CD") != null) {
				if("1".equals(obj.get("SQL_SOURCE_TYPE_CD"))) {
					obj.put("SQL_SOURCE_TYPE_NM","TOP");
				}else if("2".equals(obj.get("SQL_SOURCE_TYPE_CD"))){
					obj.put("SQL_SOURCE_TYPE_NM","전체SQL");
				}
			}
		}
		return resultList;
	}
	
	@Override
	public boolean isUpdateY(JobSchedulerConfigDetail jobSchedulerConfigDetail) throws ParseException {
		String execCycle = jobSchedulerConfigDetail.getExec_cycle();
		String endTime = jobSchedulerConfigDetail.getExec_end_dt().replaceAll("-","");
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmssSSSS");

		Date now = new Date();

		String customTime = "";
		String strNow = sf.format(now);
		
		Long int_Endtime = Long.parseLong(endTime);
		Long int_now = Long.parseLong(strNow);
		
		if(int_Endtime == int_now) {
			customTime = new SimpleDateFormat("HHmmss").format(now);
		}else {
			if(int_Endtime < int_now) {
				return false;
			}
			customTime = "235959";
		}
		
		endTime = endTime + customTime;
		Date date_EndTime = sf2.parse(endTime);
		
		//스케줄러 종료일이 현재보다 과거일 경우
		CronExpression exp = new CronExpression(execCycle);
		Date afterDate = exp.getNextValidTimeAfter(now);

		//date == null => 현재 시간을 포함하여 스케줄링일정에 걸릴 시간이 없다는것.
		if(afterDate == null) {
			return false;
		}else if(afterDate.compareTo(date_EndTime) > 0) {

			// date.compareTo(date_EndTime) > 0 인경우
			// 종료일시와 스케줄러 예정일이 같은 날이면 금일 수행 건이므로 true
			// 다른 날이면 금일 수행건이 아니므로 false
			
			if(sf.format(afterDate).equals(sf.format(date_EndTime))) {
				return true;
			}
			
			return false;
		}else if(afterDate.compareTo(date_EndTime) == 0 ) {
			// date.compareTo(date_EndTime) == 0 인경우 => 스케줄러 예정일시가 종료일시와 같으므로 false
			return false;
		}
		return true;
	}
}
