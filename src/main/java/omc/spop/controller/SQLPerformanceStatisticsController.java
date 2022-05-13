package omc.spop.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import omc.spop.model.Result;
import omc.spop.model.SQLPerformanceStatistics;
import omc.spop.service.SQLPerformanceStatisticsService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.08.29	임호경	OPENPOP V2 최초작업
 **********************************************************/
@RequestMapping(value = "/SQLPerformanceStatistics")
@Controller
public class SQLPerformanceStatisticsController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLPerformanceStatisticsController.class);
	
	@Autowired
	private SQLPerformanceStatisticsService sqlPerfStaticsService;


	/* 통계현황 - SQL 성능 - TOP SQL 추이/현황 */
	@RequestMapping(value="/TopSqlTrendStatus", method=RequestMethod.GET)
	public String User(@ModelAttribute("sqlPerformanceStatistics") SQLPerformanceStatistics sqlPerformanceStatistics, Model model) {

	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.MONTH, -1);    // 한달 전
	    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	    String aMonthAgo = dateFormatter.format(cal.getTime());
	    String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String aWeekAgo= DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-7);
		String aDayAgo= DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-1);
		
		
		model.addAttribute("aDayAgo", aDayAgo);
		model.addAttribute("aWeekAgo", aWeekAgo);
		model.addAttribute("aMonthAgo", aMonthAgo);
		model.addAttribute("nowDate", nowDate);
//		model.addAttribute("startTime", "00:00");
//		model.addAttribute("endTime", "23:59");
		
		model.addAttribute("menu_id", sqlPerformanceStatistics.getMenu_id() );
		model.addAttribute("menu_nm", sqlPerformanceStatistics.getMenu_nm() );
		
		return "sqlPerformanceStatistics/topSqlTrendStatus";
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/ChartTopSqlTrendStatus", method = RequestMethod.POST)
	@ResponseBody
	public Result getChartTopSqlTrendStatus(@ModelAttribute("sqlPerformanceStatistics") SQLPerformanceStatistics sqlPerformanceStatistics, 
			Model model) {
		Result result = new Result();
		
		try {
			
			if(sqlPerformanceStatistics.getWhatChartIs().equals("cpu_usage"))
				result = sqlPerfStaticsService.getChartCpuUsage(sqlPerformanceStatistics);
			else
				result = sqlPerfStaticsService.getChartTopSqlTrendStatus(sqlPerformanceStatistics);
			
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 통계현황 - SQL성능 - TOP SQL 추이/현황 (MODULE) */
	@RequestMapping(value="/GridModule", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GridModule(@ModelAttribute("sqlPerformanceStatistics") SQLPerformanceStatistics sqlPerformanceStatistics, Model model) {
		List<SQLPerformanceStatistics> resultList = new ArrayList<SQLPerformanceStatistics>();
		/*int dataCount4NextBtn = 0;*/

		try{
			resultList = sqlPerfStaticsService.getGridModule(sqlPerformanceStatistics);
			/*if (resultList != null && resultList.size() > sqlPerformanceStatistics.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlPerformanceStatistics.getPagePerCount());
			}*/
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
		/*JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();*/	
	}
	
	/* 통계현황 - SQL성능 - TOP SQL 추이/현황 (ACTION)*/
	@RequestMapping(value="/GridAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GridAction(@ModelAttribute("sqlPerformanceStatistics") SQLPerformanceStatistics sqlPerformanceStatistics, Model model) {
		List<SQLPerformanceStatistics> resultList = new ArrayList<SQLPerformanceStatistics>();
		
		try{
			resultList = sqlPerfStaticsService.getGridAction(sqlPerformanceStatistics);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	/* 통계현황 - SQL성능 - TOP SQL 추이/현황 (ParsingSchemna)*/
	@RequestMapping(value="/GridGridParsingSchema", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GridParsingSchema(@ModelAttribute("sqlPerformanceStatistics") SQLPerformanceStatistics sqlPerformanceStatistics, Model model) {
		List<SQLPerformanceStatistics> resultList = new ArrayList<SQLPerformanceStatistics>();
		
		try{
			resultList = sqlPerfStaticsService.getGridParsingSchema(sqlPerformanceStatistics);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}

	/* 통계현황 - SQL성능 - TOP SQL 추이/현황 하단 (top sql)*/
	@RequestMapping(value="/GridTopSqlResultList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GridTopSql(@ModelAttribute("sqlPerformanceStatistics") SQLPerformanceStatistics sqlPerformanceStatistics, Model model) {
		List<SQLPerformanceStatistics> resultList = new ArrayList<SQLPerformanceStatistics>();
		
		try{
			resultList = sqlPerfStaticsService.getGridTopSqlResultList(sqlPerformanceStatistics);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}

	/* 통계현황 - SQL성능 - TOP SQL 추이/현황 하단 (top sql) - EXCEL DOWNLOAD*/
	@RequestMapping(value = "/GridTopSqlResultList/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView GridTopSql(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlPerformanceStatistics") SQLPerformanceStatistics sqlPerformanceStatistics, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		
		try {
			resultList = sqlPerfStaticsService.getGridTopSqlResultListByExcelDown(sqlPerformanceStatistics);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		
		model.addAttribute("fileName", "TOP_SQL_추이_현황_"+sqlPerformanceStatistics.getExcel_inst_nm());
		model.addAttribute("sheetName", "TOP_SQL_추이_현황_"+sqlPerformanceStatistics.getExcel_inst_nm());
		model.addAttribute("excelId", "TOP_SQL_TREND_STATUS");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
}