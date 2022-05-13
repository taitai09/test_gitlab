package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import omc.spop.model.PerformanceCheckSql;
import omc.spop.service.PerformanceCheckSqlService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2020.05.12	명성태	OPENPOP V2 최초작업
 **********************************************************/
@RequestMapping(value = "/PerformanceCheckSql")
@Controller
public class PerformanceCheckSqlController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckSqlController.class);
	
	@Autowired
	private PerformanceCheckSqlService performanceCheckSqlService;
	
	/* SQL 성능 추적 현황 > 성능 점검 SQL */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String PerformanceCheckSql(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql, Model model) {
		return "performanceCheckSqlDesign/sqlPerformanceTraceStatus/performanceCheckSql";
	}
	
	/* 기준일자 주기만 조회 */
	@RequestMapping(value = "/baseDatePeriodOnly", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String baseDatePeriodOnly(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql) throws Exception {
		String today = "";
		
		if(performanceCheckSql.getEnd_dt() != null && performanceCheckSql.getEnd_dt() != "") {
			today = performanceCheckSql.getEnd_dt();
		} else {
			today = DateUtil.getNowDate("yyyy-MM-dd");
		}
		
		String beginDay = "";
		
		int base_period_value = performanceCheckSql.getBase_period_value();
		
		switch(base_period_value) {
		case 1:			// 1일
			beginDay = today;
			break;
		case 2:			// 1주일
			beginDay = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", today, -6);
			break;
		case 3:			// 1개월
			int year  = Integer.parseInt(today.substring(0, 4));
			int month = Integer.parseInt(today.substring(5, 7));
			int date  = Integer.parseInt(today.substring(8));
			String addMonthDate = DateUtil.addMonthByOracle(year, month, date, -1);
			
			beginDay = addMonthDate.substring(0, 4) + "-" + addMonthDate.substring(4, 6) + "-" + addMonthDate.substring(6);
			
			break;
		}
		
		performanceCheckSql.setBegin_dt(beginDay);
		performanceCheckSql.setEnd_dt(today);
		
		return success(performanceCheckSql).toJSONObject().get("rows").toString();
	}
	
	/* 기준일자 */
	@RequestMapping(value = "/baseDate", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String baseDate(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql) throws Exception {
		String today = "";
		
		if(performanceCheckSql.getEnd_dt() != null && performanceCheckSql.getEnd_dt() != "") {
			today = performanceCheckSql.getEnd_dt();
		} else {
			today = DateUtil.getNowDate("yyyy-MM-dd");
		}
		
		String beginDay = "";
		List<PerformanceCheckSql> tempData = performanceCheckSqlService.getInitialFinalDistributionDate(performanceCheckSql);
		String deployCompleteDt = "";
		
		if(tempData != null && tempData.size() > 0) {
			if(tempData.get(0) == null) {
				performanceCheckSql.setDeploy_complete_dt("N/A");
			} else {
				deployCompleteDt = tempData.get(0).getDeploy_complete_dt();
				
				if(deployCompleteDt.length() > 0) {
					today = deployCompleteDt;
					
					performanceCheckSql.setDeploy_complete_dt(deployCompleteDt);
				} else {
					performanceCheckSql.setDeploy_complete_dt("N/A");
				}
			}
		}
		
		int base_period_value = performanceCheckSql.getBase_period_value();
		
		switch(base_period_value) {
		case 1:			// 1일
			beginDay = today;
			break;
		case 2:			// 1주일
			beginDay = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", today, -6);
			break;
		case 3:			// 1개월
			int year  = Integer.parseInt(today.substring(0, 4));
			int month = Integer.parseInt(today.substring(5, 7));
			int date  = Integer.parseInt(today.substring(8));
			String addMonthDate = DateUtil.addMonthByOracle(year, month, date, -1);
			
			beginDay = addMonthDate.substring(0, 4) + "-" + addMonthDate.substring(4, 6) + "-" + addMonthDate.substring(6);
			
			break;
		}
		
		performanceCheckSql.setBegin_dt(beginDay);
		performanceCheckSql.setEnd_dt(today);
		
		return success(performanceCheckSql).toJSONObject().get("rows").toString();
	}
	
	/* 기준일자 주기 */
	@RequestMapping(value = "/baseDatePeriod", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String baseDatePeriod(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql) throws Exception {
		String today = "";
		
		if(performanceCheckSql.getEnd_dt() != null && performanceCheckSql.getEnd_dt() != "") {
			today = performanceCheckSql.getEnd_dt();
		} else {
			today = DateUtil.getNowDate("yyyy-MM-dd");
		}
		
		String beginDay = "";
		
		int base_period_value = performanceCheckSql.getBase_period_value();
		
		switch(base_period_value) {
		case 1:			// 1일
			beginDay = today;
			break;
		case 2:			// 1주일
			beginDay = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", today, -6);
			break;
		case 3:			// 1개월
			int year  = Integer.parseInt(today.substring(0, 4));
			int month = Integer.parseInt(today.substring(5, 7));
			int date  = Integer.parseInt(today.substring(8));
			String addMonthDate = DateUtil.addMonthByOracle(year, month, date, -1);
			
			beginDay = addMonthDate.substring(0, 4) + "-" + addMonthDate.substring(4, 6) + "-" + addMonthDate.substring(6);
			
			break;
		}
		
		performanceCheckSql.setBegin_dt(beginDay);
		performanceCheckSql.setEnd_dt(today);
		
		List<PerformanceCheckSql> tempData = performanceCheckSqlService.getConditionFinalDistributionDate(performanceCheckSql);
		String deployCompleteDt = "";
		
		if(tempData != null && tempData.size() > 0) {
			if(tempData.get(0) == null) {
				performanceCheckSql.setDeploy_complete_dt("N/A");
			} else {
				deployCompleteDt = tempData.get(0).getDeploy_complete_dt();
				
				if(deployCompleteDt.length() > 0) {
					today = deployCompleteDt;
					
					performanceCheckSql.setEnd_dt(today);
					performanceCheckSql.setDeploy_complete_dt(deployCompleteDt);
				} else {
					performanceCheckSql.setDeploy_complete_dt("N/A");
				}
			}
		}
		
		return success(performanceCheckSql).toJSONObject().get("rows").toString();
	}
	
	/* 검색 기준일자 */
	@RequestMapping(value = "/baseDateCondition", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String baseDateFromSearch(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql) throws Exception {
		String today = "";
		
		if(performanceCheckSql.getEnd_dt() != null && performanceCheckSql.getEnd_dt() != "") {
			today = performanceCheckSql.getEnd_dt();
		} else {
			today = DateUtil.getNowDate("yyyy-MM-dd");
		}
		
		String beginDay = "";
		List<PerformanceCheckSql> tempData = performanceCheckSqlService.getConditionFinalDistributionDate(performanceCheckSql);
		String deployCompleteDt = "";
		
		if(tempData != null && tempData.size() > 0) {
			if(tempData.get(0) == null) {
				performanceCheckSql.setDeploy_complete_dt("N/A");
			} else {
				deployCompleteDt = tempData.get(0).getDeploy_complete_dt();
				
				if(deployCompleteDt.length() > 0) {
					today = deployCompleteDt;
					
					performanceCheckSql.setDeploy_complete_dt(deployCompleteDt);
				} else {
					performanceCheckSql.setDeploy_complete_dt("N/A");
				}
			}
		}
		
		return success(performanceCheckSql).toJSONObject().get("rows").toString();
	}
	
	
	/* 성능 점검 SQL Tree 그리드 */
	@RequestMapping(value = "/loadPerformanceCheckSql", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceCheckSql(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql) throws Exception {
		List<PerformanceCheckSql> resultList = null;
		
		try {
			resultList = performanceCheckSqlService.loadPerformanceCheckSql(performanceCheckSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 기준일자 비교 */
	@RequestMapping(value = "/compareBaseDate", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String compareBaseDate(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql) throws Exception {
		String startDay = performanceCheckSql.getBegin_dt();
		String endDay = performanceCheckSql.getEnd_dt();
		
		performanceCheckSql.setIsLargerThanBeginDate(DateUtil.isLargerThanBeginDate(startDay, endDay, -1));
		
		return success(performanceCheckSql).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/loadPerformanceCheckSqlExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView loadPerformanceCheckSqlExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = performanceCheckSqlService.loadPerformanceCheckSqlExcelDown(performanceCheckSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_점검_SQL");
		model.addAttribute("sheetName", "성능 점검 SQL");
		model.addAttribute("excelId", "PERFORMANCE_CHECK_SQL");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
}