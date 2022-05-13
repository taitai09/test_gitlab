package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.ExamineAgent;
import omc.spop.model.ExamineScheduler;
import omc.spop.model.JobSchedulerExecDetailLog;
import omc.spop.model.JobSchedulerExecLog;
import omc.spop.service.ExamineOpenPOPService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2019.12.23 initiate
 **********************************************************/

@Controller
@RequestMapping(value = "/ExamineOpenPOP")
public class ExamineOpenPOPDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(ExamineOpenPOPDesignController.class);
	
	@Autowired
	private ExamineOpenPOPService examineOpenPOPService;
	
	@Value("#{defaultConfig['openpop_arch_ver']}")
	private String openpopArchVer;

	/* OPEN-POP 점검 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String examineOpenPOPDesign(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		String jspAddress = "";
		
		logger.debug("openpopArchVer[" + Integer.parseInt(openpopArchVer) + "]");
		
		switch(Integer.parseInt(openpopArchVer)) {
		case 1:
			jspAddress = "examineOpenPOP/examineOpenPOPDesign1";
			break;
		case 2:
			jspAddress = "examineOpenPOP/examineOpenPOPDesign2";
			break;
		}
		
		return jspAddress;
	}

	/* Agent 점검 */
	@RequestMapping(value = "/examineAgent1", method = RequestMethod.GET)
	public String examineAgent1(@ModelAttribute("examineAgent") ExamineAgent examineAgent, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		String nowTime = DateUtil.getNextTime("M", "30");
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("startTime", "00:00:00");
		
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("nowTime", nowTime);
		
		model.addAttribute("menu_id", "144");
		model.addAttribute("menu_nm", examineAgent.getMenu_nm());

		return "examineOpenPOP/examineAgent1";
	}
	
	/* Agent 상태 수집 */
	@RequestMapping(value = "/rtrvAgentStatus", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String rtrvAgentStatus(@ModelAttribute("examineAgent") ExamineAgent examineAgent, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = null;
		resultList = examineOpenPOPService.rtrvAgentStatus();
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
	
	/* Scheduler 점검 */
	@RequestMapping(value = "/examineScheduler2", method = RequestMethod.GET)
	public String examineScheduler2(@ModelAttribute("examineScheduler") ExamineScheduler examineScheduler, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		model.addAttribute("endDate", nowDate);
		model.addAttribute("menu_id", "149");
		model.addAttribute("menu_nm", examineScheduler.getMenu_nm());

		return "examineOpenPOP/examineScheduler";
	}
	
	/* Scheduler 점검 */
	@RequestMapping(value = "/examineScheduler", method = RequestMethod.GET)
	public String examineScheduler(@ModelAttribute("jobSchedulerExecLog") JobSchedulerExecLog jobSchedulerExecLog, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(jobSchedulerExecLog.getStrStartDt() == null || jobSchedulerExecLog.getStrStartDt().equals("")){
			jobSchedulerExecLog.setStrStartDt(startDate);
		}
		
		if(jobSchedulerExecLog.getStrEndDt() == null || jobSchedulerExecLog.getStrEndDt().equals("")){
			jobSchedulerExecLog.setStrEndDt(nowDate);
		}
		
		model.addAttribute("endDate", nowDate);
		model.addAttribute("menu_id", "149");
		model.addAttribute("menu_nm", jobSchedulerExecLog.getMenu_nm());
		
		return "examineOpenPOP/examineScheduler";
	}
	
	@RequestMapping(value = "/rtrvSchedulerStatusHistory", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String rtrvSchedulerStatusHistory(@ModelAttribute("jobSchedulerExecLog") JobSchedulerExecLog jobSchedulerExecLog, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		
		try {
			resultList = examineOpenPOPService.rtrvSchedulerStatusHistory(jobSchedulerExecLog);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
	
	/* 스케쥴러 수행내역 action */
	@RequestMapping(value="/PerformSchedulerAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SchedulerAction(@ModelAttribute("jobSchedulerExecLog") JobSchedulerExecLog jobSchedulerExecLog,  Model model) {
		List<JobSchedulerExecLog> resultList = new ArrayList<JobSchedulerExecLog>();
		int dataCount4NextBtn = 0;
		
		logger.debug("jobSchedulerExecLog[\n" + jobSchedulerExecLog + "\n]");
		
		try{
			resultList = examineOpenPOPService.performSchedulerList(jobSchedulerExecLog);
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
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	/* 스케쥴러 수행내역 - 상세내역 action */
	@RequestMapping(value="/PerformSchedulerDetailAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SchedulerDetailAction(@ModelAttribute("jobSchedulerExecDetailLog") JobSchedulerExecDetailLog jobSchedulerExecDetailLog,  Model model) {
		List<JobSchedulerExecDetailLog> resultList = new ArrayList<JobSchedulerExecDetailLog>();
		
		try{
			resultList = examineOpenPOPService.performSchedulerDetailList(jobSchedulerExecDetailLog);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/*Open-POP 설정 - 스케쥴러 수행내역 엑셀 다운*/
	@RequestMapping(value = "/PerformSchedulerDetail/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView workJobUsersListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("jobSchedulerExecLog") JobSchedulerExecLog jobSchedulerExecLog, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = examineOpenPOPService.performSchedulerListByExcelDown(jobSchedulerExecLog);
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
	
	@RequestMapping(value = "/instanceList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String instanceList(@ModelAttribute("examineAgent") ExamineAgent examineAgent, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		
		try {
			resultList = examineOpenPOPService.instanceList();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
	
	/* Agent2 점검 */
	@RequestMapping(value = "/examineAgent2", method = RequestMethod.GET)
	public String examineAgent2(@ModelAttribute("examineAgent") ExamineAgent examineAgent, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		String nowTime = DateUtil.getNextTime("M", "30");
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("startTime", "00:00:00");
		
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("nowTime", nowTime);
		
		model.addAttribute("menu_id", "144");
		model.addAttribute("menu_nm", examineAgent.getMenu_nm());
		
		return "examineOpenPOP/examineAgent2";
	}
	
	/* Agent 상태 수집 */
	@RequestMapping(value = "/rtrvAgentStatusV2", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String rtrvAgentStatusV2(@ModelAttribute("examineAgent") ExamineAgent examineAgent, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = null;
		resultList = examineOpenPOPService.rtrvAgentStatusV2();
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/instanceListV2", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String instanceListV2(@ModelAttribute("examineAgent") ExamineAgent examineAgent, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		
		try {
			resultList = examineOpenPOPService.instanceListV2();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
}