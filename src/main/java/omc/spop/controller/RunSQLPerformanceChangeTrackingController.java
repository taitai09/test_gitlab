package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import omc.spop.model.ExceptionHandlingSql;
import omc.spop.model.PerformanceCheckSql;
import omc.spop.model.SqlPerformanceTraceStatusChart;
import omc.spop.model.Sqls;
import omc.spop.service.RunSQLPerformanceChangeTrackingService;
import omc.spop.service.SqlsService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2021.08.25	이재우	최초작업(신규 배포 SQL 성능 변화 분석(자동검증))
 **********************************************************/
@RequestMapping(value = "/RunSqlPerformanceChangeTracking")
@Controller
public class RunSQLPerformanceChangeTrackingController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(SqlPerformanceTraceStatusChartController.class);
	
	@Autowired
	private RunSQLPerformanceChangeTrackingService runSqlPerformanceChangeTrackingService;

	@Autowired
	private SqlsService sqlsService;
	
	/* SQL 성능 추적 현황 > 성능 검증 SQL */
	@RequestMapping(value = "/PerformanceVerifySql", method=RequestMethod.GET)
	public String PerformanceCheckSql(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql, Model model) {
		return "performanceCheckSqlDesign/sqlPerformanceTrackingStatus/performanceVerifySql";
	}
	
	/* 예외처리 SQLs */
	@RequestMapping(value = "/ExceptionHandlingRunSql", method=RequestMethod.GET)
	public String Sqls(@ModelAttribute("sqls") Sqls sqls, Model model) {
		return "performanceCheckSqlDesign/sqlPerformanceTrackingStatus/exceptionHandlingRunSql";
	}
	
	@RequestMapping(value = "/SqlPerformanceTrackingStatusChart", method=RequestMethod.GET)
	public String SqlPerformanceTraceStatusCharts(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart, Model model) {
		return "performanceCheckSqlDesign/sqlPerformanceTrackingStatus/sqlPerformanceTrackingStatusCharts";
	}
	
	/* 성능 검증 SQL Tree 그리드 */
	@RequestMapping(value = "/loadPerformanceVerifySql", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceCheckSql(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql) throws Exception {
		List<PerformanceCheckSql> resultList = null;
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.loadPerformanceVerifySql( performanceCheckSql );
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/*  성능 검증 SQL 엑셀다운로드 */
	@RequestMapping(value = "/loadPerformanceVerifySqlExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView loadPerformanceVerifySqlExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.loadPerformanceVerifySqlExcelDown( performanceCheckSql );
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_검증_SQL");
		model.addAttribute("sheetName", "성능 검증 SQL");
		model.addAttribute("excelId", "PERFORMANCE_CHECK_SQL");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 예외 처리 SQL 그리드 */
	@RequestMapping(value = "/loadExceptionHandlingSql", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExceptionHandlingSql(@ModelAttribute("exceptionHandlingSql") ExceptionHandlingSql exceptionHandlingSql) throws Exception {
		List<ExceptionHandlingSql> resultList = null;
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.loadExceptionHandlingSql(exceptionHandlingSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 예외 처리 SQL 엑셀다운로드 */
	@RequestMapping(value = "/loadExceptionHandlingSqlExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView loadExceptionHandlingSqlExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("exceptionHandlingSql") ExceptionHandlingSql exceptionHandlingSql, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.loadExceptionHandlingSqlExcelDown(exceptionHandlingSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "예외_처리_SQL");
		model.addAttribute("sheetName", "예외 처리 SQL");
		model.addAttribute("excelId", "EXCEPTION_HANDLING_RUN_SQL");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* chart 조회 */
	@RequestMapping(value = "/chart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chart(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.chart(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* chartPerformance01 조회 */
	@RequestMapping(value = "/chartPerformance01", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chartPerformance01(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.chartPerformance01(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* chartPerformance02 조회 */
	@RequestMapping(value = "/chartPerformance02", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chartPerformance02(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.chartPerformance02(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* chartException01 조회 */
	@RequestMapping(value = "/chartException01", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chartException01(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.chartException01(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* chartException02 조회 */
	@RequestMapping(value = "/chartException02", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chartException02(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = runSqlPerformanceChangeTrackingService.chartException02(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 신규 배포 SQL 성능 변화 분석(자동검증) SQLs 그리드 */
	@RequestMapping(value = "/loadAutoSqls", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqls(@ModelAttribute("sqls") Sqls sqls) throws Exception {
		List<Sqls> resultList = Collections.emptyList();
		List<Sqls> buildList = null;
		int dataCount4NextBtn = 0;
		JSONObject jobj = new JSONObject();
		
		try {
			
			resultList = sqlsService.loadAutoSqls(sqls);
			
			if(resultList != null && resultList.size() > sqls.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqls.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			buildList = TreeWrite.buildSqlTree(resultList, "-1");
			
			jobj = success(buildList).toJSONObject();
			logger.debug("listSize ============> "+ buildList.size() + " , "+ dataCount4NextBtn);
			
			buildList = null;
			resultList = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	// SQLs Tab Excel Down
	@RequestMapping(value = "/excelAutoDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqls") Sqls sqls, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlsService.excelAutoDown(sqls);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQLs");
		model.addAttribute("sheetName", "SQLs");
		model.addAttribute("excelId", "AUTOSQLs");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	@RequestMapping(value="/sqlAutoPerformanceTrace", method=RequestMethod.GET)
	public String sqlPerformanceTrace(@ModelAttribute("sqls") Sqls sqls, Model model) {
		model.addAttribute("menu_id", sqls.getMenu_id());
		model.addAttribute("menu_nm", sqls.getMenu_nm());
		
		return "performanceCheckSqlDesign/sqls/sqlAutoPerformanceTrace";
	}
	

	@RequestMapping(value="/sqlAutoPerfInfo", method=RequestMethod.GET)
	public String sqlInfo(@ModelAttribute("sqls") Sqls sqls, Model model) {
		return "performanceCheckSqlDesign/sqls/sqlAutoPerfInfo";
	}
	

	/* SQLs탭 > SQL성능추적 > 성능검증 조회 */
	@RequestMapping(value = "/autoPerformanceCheck", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String autoPerformanceCheck(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.autoPerformanceCheck(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	

	@RequestMapping(value="/sqlsAutoPerformanceCheckResult", method=RequestMethod.GET)
	public String sqlsPerformanceCheckResult(@ModelAttribute("sqls") Sqls sqls, Model model) {
		return "performanceCheckSqlDesign/sqls/sqlsAutoPerformanceCheckResult";
	}

	/* SQLs탭 > 성능검증결과 > 조회*/
	@RequestMapping(value = "/autoPerformanceCheckResult", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String autoPerformanceCheckResult(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = new ArrayList<Sqls>();
		try {
			resultList = sqlsService.autoPerformanceCheckResult(sqls);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
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
		
		performanceCheckSql.setDeploy_complete_dt("N/A");
		
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
}
