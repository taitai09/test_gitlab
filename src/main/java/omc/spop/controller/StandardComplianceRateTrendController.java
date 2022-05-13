package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.model.Project;
import omc.spop.model.Result;
import omc.spop.model.StandardComplianceRateTrend;
import omc.spop.service.CommonService;
import omc.spop.service.SQLAutomaticPerformanceCheckService;
import omc.spop.service.StandardComplianceRateTrendService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 * 2020.07.01	이재우		기능개선
 **********************************************************/

@Controller
@RequestMapping(value = "/StandardComplianceRateTrend")
public class StandardComplianceRateTrendController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(StandardComplianceRateTrendController.class);
	
	@Autowired
	private StandardComplianceRateTrendService scrtService;
	
	/* Page action */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String chartExample(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -90);
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", scrt.getMenu_id());
		model.addAttribute("menu_nm", scrt.getMenu_nm());
		
		return "standardComplianceRateTrend/standardComplianceRateTrend";
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/loadChartStandardComplianceRateTrendTotal", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadChartStandardComplianceRateTrendTotal(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		List<StandardComplianceRateTrend> resultList = new ArrayList<StandardComplianceRateTrend>();
		
		try {
			resultList = scrtService.loadChartStandardComplianceRateTrendTotal(scrt);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Data action */
	@RequestMapping(value = "/loadStatusByWork", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadStatusByWork(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		List<StandardComplianceRateTrend> resultList = new ArrayList<StandardComplianceRateTrend>();
		
		try {
			resultList = scrtService.loadStatusByWork(scrt);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/loadChartStatusByWork", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadChartStatusByWork(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		List<StandardComplianceRateTrend> resultList = new ArrayList<StandardComplianceRateTrend>();
		
		try {
			resultList = scrtService.loadChartStatusByWork(scrt);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/loadChartNonComplianceStatus", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadChartNonComplianceStatus(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		List<StandardComplianceRateTrend> resultList = new ArrayList<StandardComplianceRateTrend>();
		
		try {
			resultList = scrtService.loadChartNonComplianceStatus(scrt);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/loadChartStandardComplianceRateTrend", method = RequestMethod.POST)
	@ResponseBody
	public Result loadChartStandardComplianceRateTrend(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		Result result = new Result();
		
		try {
			result = scrtService.loadChartStandardComplianceRateTrend(scrt);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/loadChartNonStandardComplianceRateTrend", method = RequestMethod.POST)
	@ResponseBody
	public Result loadChartNonStandardComplianceRateTrend(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		Result result = new Result();
		
		try {
			result = scrtService.loadChartNonStandardComplianceRateTrend(scrt);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* Dynamic Chart Data Sample action */
	@RequestMapping(value = "/loadDynamicChartSample1", method = RequestMethod.POST)
	@ResponseBody
	public Result loadDynamicChartSample1(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		Result result = new Result();
		
		try {
			result = scrtService.loadDynamicChartSample1(scrt);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/loadSchedulerList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSchedulerList(@ModelAttribute("standardComplianceRateTrend") StandardComplianceRateTrend scrt, Model model) {
		List<StandardComplianceRateTrend> resultList = new ArrayList<StandardComplianceRateTrend>();
		
		try {
			resultList = scrtService.loadSchedulerList(scrt);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
}
