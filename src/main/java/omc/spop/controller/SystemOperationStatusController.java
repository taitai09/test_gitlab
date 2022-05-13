package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import omc.spop.model.JobSchedulerExecDetailLog;
import omc.spop.model.JobSchedulerExecLog;
import omc.spop.model.Users;
import omc.spop.service.SystemOperationStatusService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.07	이원식	OPENPOP V2 최초작업 
 **********************************************************/

@Controller
public class SystemOperationStatusController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemOperationStatusController.class);
	
	@Autowired
	private SystemOperationStatusService systemOperationStatusService;

	/* 스케쥴러 수행내역 */
	@RequestMapping(value="/PerformScheduler")
	public String PerformScheduler(@ModelAttribute("jobSchedulerExecLog") JobSchedulerExecLog jobSchedulerExecLog, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(jobSchedulerExecLog.getStrStartDt() == null || jobSchedulerExecLog.getStrStartDt().equals("")){
			jobSchedulerExecLog.setStrStartDt(startDate);
		}
		
		if(jobSchedulerExecLog.getStrEndDt() == null || jobSchedulerExecLog.getStrEndDt().equals("")){
			jobSchedulerExecLog.setStrEndDt(nowDate);
		}		
		
		model.addAttribute("menu_id", jobSchedulerExecLog.getMenu_id() );
		model.addAttribute("menu_nm", jobSchedulerExecLog.getMenu_nm() );
		model.addAttribute("call_from_child", jobSchedulerExecLog.getCall_from_child() );

		return "performScheduler";
	}

	/* 스케쥴러 수행내역 action */
	@RequestMapping(value="/PerformSchedulerAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SchedulerAction(@ModelAttribute("jobSchedulerExecLog") JobSchedulerExecLog jobSchedulerExecLog,  Model model) {
		List<JobSchedulerExecLog> resultList = new ArrayList<JobSchedulerExecLog>();
		int dataCount4NextBtn = 0;

		try{
			resultList = systemOperationStatusService.performSchedulerList(jobSchedulerExecLog);
			if(resultList != null && resultList.size() > jobSchedulerExecLog.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(jobSchedulerExecLog.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	/* 스케쥴러 수행내역 - 상세내역 */
	@RequestMapping(value="/PerformSchedulerDetail")
	public String SchedulerDetail(@ModelAttribute("jobSchedulerExecDetailLog") JobSchedulerExecDetailLog jobSchedulerExecDetailLog, Model model) {
		model.addAttribute("menu_id", jobSchedulerExecDetailLog.getMenu_id());
		model.addAttribute("menu_nm", jobSchedulerExecDetailLog.getMenu_nm());
		
		return "performSchedulerDetail";
	}
	
	/* 스케쥴러 수행내역 - 상세내역 action */
	@RequestMapping(value="/PerformSchedulerDetailAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SchedulerDetailAction(@ModelAttribute("jobSchedulerExecDetailLog") JobSchedulerExecDetailLog jobSchedulerExecDetailLog,  Model model) {
		List<JobSchedulerExecDetailLog> resultList = new ArrayList<JobSchedulerExecDetailLog>();

		try{
			resultList = systemOperationStatusService.performSchedulerDetailList(jobSchedulerExecDetailLog);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	
	/*설정 - 스케쥴러 수행내역 엑셀 다운*/
	@RequestMapping(value = "/PerformSchedulerDetail/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView workJobUsersListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("jobSchedulerExecDetailLog") JobSchedulerExecDetailLog jobSchedulerExecDetailLog, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = systemOperationStatusService.performSchedulerListByExcelDown(jobSchedulerExecDetailLog);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "스케줄러_수행_내역");
		model.addAttribute("sheetName", "스케줄러_수행_내역");
		model.addAttribute("excelId", "JOB_SCHEDULER_EXEC_LOG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
}