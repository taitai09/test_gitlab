package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import omc.spop.model.ExceptionHandlingSql;
import omc.spop.model.PerformanceCheckSql;
import omc.spop.model.Sqls;
import omc.spop.service.ExceptionHandlingSqlService;
import omc.spop.service.PerformanceCheckSqlService;
import omc.spop.service.SqlsService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2020.05.12	명성태	OPENPOP V2 최초작업
 **********************************************************/
@RequestMapping(value = "/ExceptionHandlingSql")
@Controller
public class ExceptionHandlingSqlController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingSqlController.class);
	
	@Autowired
	private PerformanceCheckSqlService performanceCheckSqlService;
	
	@Autowired
	private SqlsService sqlsService;
	
	@Autowired
	private ExceptionHandlingSqlService exceptionHandlingSqlService;
	
	/* SQLs */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String Sqls(@ModelAttribute("sqls") Sqls sqls, Model model) {
		return "performanceCheckSqlDesign/sqlPerformanceTraceStatus/exceptionHandlingSql";
	}
	
	/* 기준일자 */
	@RequestMapping(value = "/baseDate", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String baseDate(@ModelAttribute("sqls") Sqls sqls) throws Exception {
		String today = "";
		
		if(sqls.getEnd_dt() != null && sqls.getEnd_dt() != "") {
			today = sqls.getEnd_dt();
		} else {
			today = DateUtil.getNowDate("yyyy-MM-dd");
		}
		
		String beginDay = "";
		
		int base_period_value = sqls.getBase_period_value();
		
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
		
		sqls.setBegin_dt(beginDay);
		sqls.setEnd_dt(today);
		
		return success(sqls).toJSONObject().get("rows").toString();
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
					today = today.substring(0, 5) + deployCompleteDt.substring(0, 2) + "-" + deployCompleteDt.substring(3);
					
					performanceCheckSql.setDeploy_complete_dt(deployCompleteDt);
				} else {
					performanceCheckSql.setDeploy_complete_dt("N/A");
				}
			}
		}
		
		return success(performanceCheckSql).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/exceptionHandlingMethod", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String exceptionHandlingMethod(@ModelAttribute("exceptionHandlingSql") ExceptionHandlingSql exceptionHandlingSql) throws Exception {
		String isAll = StringUtils.defaultString(exceptionHandlingSql.getIsAll());
		String isChoice = StringUtils.defaultString(exceptionHandlingSql.getIsChoice());
		List<ExceptionHandlingSql> list = new ArrayList<ExceptionHandlingSql>();
		List<ExceptionHandlingSql> exceptionHandlingMethod = exceptionHandlingSqlService.exceptionHandlingMethod(exceptionHandlingSql);
		
		if(isAll.equals("") == false) {
			ExceptionHandlingSql temp = new ExceptionHandlingSql();
			temp.setDbid("");
			if (isAll.equals("Y")) {
				temp.setCd("");
				temp.setCd_nm("전체");
			} else if (isChoice.equals("Y")) {
				temp.setCd("");
				temp.setCd_nm("선택");
//			} else if (isBase.equals("Y")) {
//				workjob.setId("");
//				workjob.setParent_id("");
//				workjob.setText("기본");
			} else {
				temp.setCd("");
				temp.setCd_nm("");
			}
			list.add(temp);
		}
		
		list.addAll(exceptionHandlingMethod);
		
		return success(list).toJSONObject().get("rows").toString();
	}
	
	/* 성능 점검 SQL 그리드 */
	@RequestMapping(value = "/loadExceptionHandlingSql", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExceptionHandlingSql(@ModelAttribute("exceptionHandlingSql") ExceptionHandlingSql exceptionHandlingSql) throws Exception {
		List<ExceptionHandlingSql> resultList = null;
		
		try {
			resultList = exceptionHandlingSqlService.loadExceptionHandlingSql(exceptionHandlingSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/loadExceptionHandlingSqlExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView loadExceptionHandlingSqlExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("exceptionHandlingSql") ExceptionHandlingSql exceptionHandlingSql, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = exceptionHandlingSqlService.loadExceptionHandlingSqlExcelDown(exceptionHandlingSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "예외_처리_SQL");
		model.addAttribute("sheetName", "예외 처리 SQL");
		model.addAttribute("excelId", "EXCEPTION_HANDLING_SQL");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
}