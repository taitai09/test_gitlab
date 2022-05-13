package omc.spop.controller;

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
import omc.spop.model.PerformanceCheckSql;
import omc.spop.model.SqlPerformanceTraceStatusChart;
import omc.spop.model.Sqls;
import omc.spop.service.PerformanceCheckSqlService;
import omc.spop.service.SqlPerformanceTraceStatusChartService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2020.06.24	명성태	OPENPOP V2 최초작업
 **********************************************************/
@RequestMapping(value = "/SqlPerformanceTraceStatusChart")
@Controller
public class SqlPerformanceTraceStatusChartController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(SqlPerformanceTraceStatusChartController.class);
	
	@Autowired
	private PerformanceCheckSqlService performanceCheckSqlService;
	
	@Autowired
	private SqlPerformanceTraceStatusChartService sqlPerformanceTraceStatusChartService;
	
	@RequestMapping(value="", method=RequestMethod.GET)
	public String SqlPerformanceTraceStatusCharts(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart, Model model) {
		return "performanceCheckSqlDesign/sqlPerformanceTraceStatus/sqlPerformanceTraceStatusCharts";
	}
	
	/* 기준일자 */
	@RequestMapping(value = "/baseDate", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String baseDate(@ModelAttribute("sqls") Sqls sqls) {
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
	
	@RequestMapping(value = "/chart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chart(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = sqlPerformanceTraceStatusChartService.chart(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/chartPerformance01", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chartPerformance01(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = sqlPerformanceTraceStatusChartService.chartPerformance01(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/chartPerformance02", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chartPerformance02(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = sqlPerformanceTraceStatusChartService.chartPerformance02(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/chartException01", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chartException01(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = sqlPerformanceTraceStatusChartService.chartException01(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/chartException02", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String chartException02(@ModelAttribute("sqlPerformanceTraceStatusChart") SqlPerformanceTraceStatusChart sqlPerformanceTraceStatusChart) throws Exception {
		List<SqlPerformanceTraceStatusChart> resultList = null;
		
		try {
			resultList = sqlPerformanceTraceStatusChartService.chartException02(sqlPerformanceTraceStatusChart);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
}