package omc.mqm.controller;

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

import omc.mqm.service.MqmStatusTrendService;
import omc.spop.base.InterfaceController;
import omc.spop.model.Result;
import omc.spop.model.StandardComplianceRateTrend;
import omc.spop.model.StructuralQualityStatusTrend;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Controller
@RequestMapping(value = "/Mqm")
public class MqmStatusTrendController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(MqmStatusTrendController.class);
	
	@Autowired
	private MqmStatusTrendService sqstService;
	
	/* Page action */
	@RequestMapping(value = "/StructuralQualityStatusTrend", method = RequestMethod.GET)
	public String StructuralQualityStatusTrend(@ModelAttribute("sqst") StructuralQualityStatusTrend sqst, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -90);

		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", sqst.getMenu_id());
		model.addAttribute("menu_nm", sqst.getMenu_nm());
		
		return "mqm/structuralQualityStatusTrend";
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/ChartNonCompliantStandardizationRateTrend", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ChartStructuralQualityStatusTrend(@ModelAttribute("sqst") StructuralQualityStatusTrend sqst, Model model) {
		List<StructuralQualityStatusTrend> resultList = new ArrayList<StructuralQualityStatusTrend>();
		
		try {
			resultList = sqstService.getChartNonCompliantStandardizationRateTrend(sqst);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	/* Chart Data action */
	@RequestMapping(value = "/StatusByModelAll", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ChartStatusByModelAll(@ModelAttribute("sqst") StructuralQualityStatusTrend sqst, Model model) {
		List<StructuralQualityStatusTrend> resultList = new ArrayList<StructuralQualityStatusTrend>();
		
		try {
			resultList = sqstService.getStatusByModelAll(sqst);
		} catch(Exception ex) { 
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/ChartStatusByModel", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ChartStatusByModel(@ModelAttribute("sqst") StructuralQualityStatusTrend sqst, Model model) {
		List<StructuralQualityStatusTrend> resultList = new ArrayList<StructuralQualityStatusTrend>();
		
		try {
			resultList = sqstService.getChartStatusByModel(sqst);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/ChartStandardizationRateStatusByModel", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ChartStandardizationRateStatusByModel(@ModelAttribute("sqst") StructuralQualityStatusTrend sqst, Model model) {
		List<StructuralQualityStatusTrend> resultList = new ArrayList<StructuralQualityStatusTrend>();
		
		try {
			resultList = sqstService.getChartStandardizationRateStatusByModel(sqst);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}

	/* Chart Data action */
	@RequestMapping(value = "/ChartNonComplianceStatus", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ChartNonComplianceStatus(@ModelAttribute("sqst") StructuralQualityStatusTrend sqst, Model model) {
		List<StructuralQualityStatusTrend> resultList = new ArrayList<StructuralQualityStatusTrend>();
		
		try {
			resultList = sqstService.getChartNonComplianceStatus(sqst);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/ChartNumberNonCompliantByModel", method = RequestMethod.POST)
	@ResponseBody
	public Result ChartNumberNonCompliantByModel(@ModelAttribute("sqst") StructuralQualityStatusTrend sqst, Model model) {
		Result result = new Result();
		
		try {
			result = sqstService.getChartNumberNonCompliantByModel(sqst);
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
	@RequestMapping(value = "/ChartStandardComplianceRateTrend", method = RequestMethod.POST)
	@ResponseBody
	public Result ChartStandardComplianceRateTrend(@ModelAttribute("sqst") StructuralQualityStatusTrend sqst, Model model) {
		Result result = new Result();
		
		try {
			result = sqstService.getChartStandardComplianceRateTrend(sqst);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
}
