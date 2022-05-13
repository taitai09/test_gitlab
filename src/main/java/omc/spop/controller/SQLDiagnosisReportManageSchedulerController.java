package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.dao.SQLStandardsDao;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.ReportHtml;
import omc.spop.model.Result;
import omc.spop.service.SQLDiagnosisReportManageSchedulerService;
import omc.spop.service.SQLStandardsService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;


/** 
	* @packageName	:	omc.spop.controller 
	* @fileName		:	SQLDiagnosisReportManageSchedulerController.java 
	* @author 		:	OPEN MADE (wonjae kim) 
	* @description	: 	SQL 종합 진단 스케줄러 관리 컨트롤러
	* @History		
	============================================================
	2021.09.23        wonjae kim 			최초생성
	============================================================
*/
@Controller
@RequestMapping(value = "/SQLDiagnosisReportManageScheduler")
public class SQLDiagnosisReportManageSchedulerController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(SQLDiagnosisReportManageSchedulerController.class);

	@Autowired
	private SQLDiagnosisReportManageSchedulerService sqlDiagnosisReportManageSchedulerService;

	@RequestMapping(value = "selectSQLDiagnosisReportScheduler", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectSQLDiagnosisReportScheduler(@ModelAttribute("JobSchedulerBase") JobSchedulerBase jobSchedulerBase) throws Exception {
		
		int dataCount4NextBtn = 0;
		
		if(logger.isDebugEnabled() == true) {
			logger.debug("selectSQLDiagnosisReportSchedulerList JobSchedulerBase Log > {}", jobSchedulerBase.toString());
		}
		
		ArrayList<JobSchedulerBase> arr = new ArrayList<JobSchedulerBase>();
		
		try {
			arr = sqlDiagnosisReportManageSchedulerService.selectSQLDiagnosisReportScheduler(jobSchedulerBase);
			if(arr != null && arr.size() > jobSchedulerBase.getPagePerCount()){
				dataCount4NextBtn = arr.size();
				arr.remove(jobSchedulerBase.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}

		}catch (Exception e) {
			logger.error(" selectSQLDiagnosisReportScheduler error [{}]" , e);
			return getErrorJsonString(e);
		}
		JSONObject jobj = success(arr).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
		
	}	
	
	@RequestMapping(value = "insertSQLDiagnosisReportScheduler", method = RequestMethod.POST)
	@ResponseBody
	public Result insertSQLDiagnosisReportScheduler(@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		
		Result result = new Result();
		boolean b_isUpdate = false;

		if(logger.isDebugEnabled() == true) {
			logger.debug("insertSQLDiagnosisReportScheduler jobSchedulerConfigDetail Log > {}", jobSchedulerConfigDetail.toString());
		}
		
		//종료일시 23시 59분 59초
		jobSchedulerConfigDetail.setExec_end_dt(jobSchedulerConfigDetail.getExec_end_dt() + "235959");
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());

		try {
			result.setResult(true);
			result.setMessage("저장 되었습니다.");
			
			b_isUpdate = sqlDiagnosisReportManageSchedulerService.isUpdateY(jobSchedulerConfigDetail);
			
			jobSchedulerConfigDetail.setUse_yn("N");
			if(b_isUpdate) {
				jobSchedulerConfigDetail.setUse_yn("Y");
			}
			if(sqlDiagnosisReportManageSchedulerService.insertSQLDiagnosisReportScheduler(jobSchedulerConfigDetail) == 0) {
				throw new Exception("저장에 실패하였습니다.");
			}
			else if(sqlDiagnosisReportManageSchedulerService.insertJobSchedulerConfigDetail(jobSchedulerConfigDetail) == 0) {
				throw new Exception("저장에 실패하였습니다.");
			}
		}catch(Exception e) {
			logger.error("insertSQLDiagnosisReportScheduler ERROR [{}]" , e);
			result.setResult(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}	
	
	@RequestMapping(value = "updateSQLDiagnosisReportScheduler", method = RequestMethod.POST)
	@ResponseBody
	public Result updateSQLDiagnosisReportScheduler(@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		
		Result result = new Result();
		boolean b_isUpdate = false;
		
		if(logger.isDebugEnabled() == true) {
			logger.debug("updateSQLDiagnosisReportScheduler jobSchedulerConfigDetail Log > {}", jobSchedulerConfigDetail.toString());
		}
		
		//종료일시 23시 59분 59초
		jobSchedulerConfigDetail.setExec_end_dt(jobSchedulerConfigDetail.getExec_end_dt() + "235959");
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());

		try {
			result.setResult(true);
			result.setMessage("수정 되었습니다.");
			b_isUpdate = sqlDiagnosisReportManageSchedulerService.isUpdateY(jobSchedulerConfigDetail);
			
			jobSchedulerConfigDetail.setUse_yn("N");
			if(b_isUpdate) {
				jobSchedulerConfigDetail.setUse_yn("Y");
			}
			
			if(sqlDiagnosisReportManageSchedulerService.updateSQLDiagnosisReportScheduler(jobSchedulerConfigDetail) == 0) {
				throw new Exception("수정에 실패하였습니다.");
			}else if(sqlDiagnosisReportManageSchedulerService.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail) == 0) {
				throw new Exception("수정에 실패하였습니다.");
			}
		}catch(Exception e) {
			logger.error("updateSQLDiagnosisReportScheduler ERROR [{}]" , e);
			result.setResult(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}	
	
	@RequestMapping(value = "deleteSQLDiagnosisReportScheduler", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteSQLDiagnosisReportScheduler(@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail) throws Exception {
		
		Result result = new Result();
				
		if(logger.isDebugEnabled() == true) {
			logger.debug("deleteSQLDiagnosisReportScheduler jobSchedulerConfigDetail Log > {}", jobSchedulerConfigDetail.toString());
		}
		
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());

		try {
			result.setResult(true);
			result.setMessage("삭제 되었습니다.");
			
			jobSchedulerConfigDetail.setExec_cycle(null);
			
			if(sqlDiagnosisReportManageSchedulerService.deleteSQLDiagnosisReportScheduler(jobSchedulerConfigDetail) == 0) {
				throw new Exception("삭제에 실패하였습니다.");
			}else if(sqlDiagnosisReportManageSchedulerService.updateJobSchedulerConfigDetail(jobSchedulerConfigDetail) == 0) {
				throw new Exception("삭제에 실패하였습니다.");
			}
		}catch(Exception e) {
			logger.error("deleteQLDiagnosisReportScheduler ERROR [{}]" , e);
			result.setResult(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}	
	@RequestMapping(value = "excelDownload", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView excelDownload(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		
		if(logger.isDebugEnabled() == true) {
			logger.debug("excelDownload JobSchedulerBase Log > {}", jobSchedulerBase.toString());
		}
		
		jobSchedulerBase.setJob_scheduler_type_cd("37");
		List<LinkedHashMap<String, Object>> returnList = Collections.emptyList();

		try {
			returnList = sqlDiagnosisReportManageSchedulerService.excelDownload(jobSchedulerBase);

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQL_품질_진단_스케줄러_관리");
		model.addAttribute("sheetName", "SQL_품질_진단_스케줄러_관리");
		model.addAttribute("excelId", "SQL_DIAGNOSIS_REPORT_MANAGE_SCHEDULER");
		
		return new ModelAndView("xlsxView", "resultList", returnList);
	}
	
}