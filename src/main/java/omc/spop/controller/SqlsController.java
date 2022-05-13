package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.PerformanceCheckSql;
import omc.spop.model.Result;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.SqlsDetail;
import omc.spop.service.PerformanceCheckSqlService;
import omc.spop.service.SqlsService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2020.05.12	명성태	OPENPOP V2 최초작업
 **********************************************************/
@RequestMapping(value = "/Sqls")
@Controller
public class SqlsController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(SqlsController.class);
	
	@Autowired
	private PerformanceCheckSqlService performanceCheckSqlService;
	
	@Autowired
	private SqlsService sqlsService;
	
	/* SQLs */
	@RequestMapping(value="", method=RequestMethod.GET)
	@ResponseBody
	public String Sqls(@ModelAttribute("sqls") Sqls sqls, Model model) {
		logger.debug("삭제하거나  이관받아야 하는데...");
		
		return "performanceCheckSqlDesign/sqls/sqls";
	}
	
	/* 기준일자 */
	@RequestMapping(value = "/baseDate", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
//	public String baseDate(@ModelAttribute("sqls") Sqls sqls) throws Exception {
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
	
	/* 성능 점검 SQL 그리드 */
	@RequestMapping(value = "/loadSqls", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqls(@ModelAttribute("sqls") Sqls sqls) throws Exception {
		List<Sqls> resultList = Collections.emptyList();
		int dataCount4NextBtn = 0;
		JSONObject jobj = new JSONObject();
		
		try {
			resultList = sqlsService.loadSqls(sqls);
			
			if(resultList != null){
				if(resultList.size() > sqls.getPagePerCount()) {
					dataCount4NextBtn = resultList.size();
					resultList.remove(sqls.getPagePerCount());
					/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
				}
				List<Sqls> buildList = TreeWrite.buildSqlTree(resultList, "-1");
				jobj = success(buildList).toJSONObject();
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	@RequestMapping(value = "/excelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView excelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqls") Sqls sqls, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlsService.excelDown(sqls);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQLs");
		model.addAttribute("sheetName", "SQLs");
		model.addAttribute("excelId", "SQLs");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 튜닝요청 팝업  */
	@RequestMapping(value="/Popup/InsertTuningRequest", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertTuningRequest(@ModelAttribute("sqls") Sqls sqls,  Model model) {
		Result result = new Result();
		
		try{
			logger.debug("##### 튜닝요청 팝업 - 튜닝요청 진행 #####");
			result = sqlsService.insertTuningRequest(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/sqlPerformanceTrace", method=RequestMethod.GET)
	public String sqlPerformanceTrace(@ModelAttribute("sqls") Sqls sqls, Model model) {
		model.addAttribute("menu_id", sqls.getMenu_id());
		model.addAttribute("menu_nm", sqls.getMenu_nm());
		
		return "performanceCheckSqlDesign/sqls/sqlPerformanceTrace";
	}
	
	/* 이전 운영 성능 */
	@RequestMapping(value = "/beforeOperationPerformance", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String beforeOperationPerformance(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.beforeOperationPerformance(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 성능점검 */
	@RequestMapping(value = "/performanceCheck", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String performanceCheck(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.performanceCheck(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 배포후 운영성능 */
	@RequestMapping(value = "/afterDistributionOperationPerformance", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String afterDistributionOperationPerformance(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.afterDistributionOperationPerformance(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value="/sqlInfo", method=RequestMethod.GET)
	public String sqlInfo(@ModelAttribute("sqls") Sqls sqls, Model model) {
		return "performanceCheckSqlDesign/sqls/sqlInfo";
	}
	
	/* 성능 점검 SQL > SQL_TEXT */
	@RequestMapping(value = "/sqlTextPerformanceCheck", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlTextPerformanceCheck(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.sqlTextPerformanceCheck(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 성능 점검 SQL > SQL_BIND */
	@RequestMapping(value = "/sqlBindPerformanceCheck", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlBindPerformanceCheck(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.sqlBindPerformanceCheck(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 성능 점검 SQL > SQL_PLAN */
	@RequestMapping(value = "/sqlPlanPerformanceCheck", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlPlanPerformanceCheck(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.sqlPlanPerformanceCheck(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 운영 SQL > SQL_TEXT */
	@RequestMapping(value = "/sqlTextAll", method = RequestMethod.POST)
	@ResponseBody
	public Result sqlTextAll(@ModelAttribute("sqls") Sqls sqls, Model model) {
		Result result = new Result();
		
		try {
			result.setResult(true);
			result.setObject(sqlsService.sqlTextAll(sqls));
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 운영 SQL > SQL_BIND */
	@RequestMapping(value = "/sqlBindOperation", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlBindOperation(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.sqlBindOperation(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 운영 SQL > SQL_PLAN */
	@RequestMapping(value = "/sqlPlanOperation", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlPlanOperation(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.sqlPlanOperation(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value="/sqlsPerformanceCheckResult", method=RequestMethod.GET)
	public String sqlsPerformanceCheckResult(@ModelAttribute("sqls") Sqls sqls, Model model) {
		return "performanceCheckSqlDesign/sqls/sqlsPerformanceCheckResult";
	}
	
	/* 성능점검결과 탭 > 대용량 테이블 기준 */
	@RequestMapping(value = "/bigTableThresholdCnt", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String bigTableThresholdCnt(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = null;
		
		try {
			resultList = sqlsService.bigTableThresholdCnt(sqls);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping("/loadPerfCheckAllPgm")
	// public ModelAndView createPerfChkResultTab2(@RequestParam(required =
	// true) Map<String, Object> param, HttpSession httpSession,
	// HttpServletRequest request) throws Exception {
	public ModelAndView loadPerfCheckAllPgm(@ModelAttribute("sqls") Sqls sqls) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Sqls perfCheckAllPgmSqls = sqlsService.loadPerfCheckAllPgm(sqls);
		
		model.put("sqls", perfCheckAllPgmSqls);
		
		String rstUrl = "performanceCheckSqlDesign/sqls/sqlsProgramInfo";
		return new ModelAndView(rstUrl, model);
	}
	
	@RequestMapping(value = "/convertPerfChkResult", method = RequestMethod.POST)
	@ResponseBody
	public Result convertPerfChkResult(@ModelAttribute("sqls") Sqls sqls, HttpServletRequest request, Model model) {
		Result result = new Result();
		String html = sqls.getHtml();
		
		try {
			Document doc = Jsoup.parseBodyFragment(html);	// jsoup 1.10.3
//			Document doc = Jsoup.parse(html);				// jsoup 1.11.3
//			Element body = doc.selectFirst("body");			// jsoup 1.11.3
			
			result.setResult(true);
			result.setMessage("HTML BODY 추출완료");
//			result.setTxtValue(body.html());			// jsoup 1.11.3
			result.setTxtValue(doc.html());				// jsoup 1.10.3
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setStatus("exception");
			result.setMessage("프로그램 정보 조회중 오류가 발생하였습니다.");
		}
		
		return result;
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/sqlStatTrend", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlStatTrend(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = new ArrayList<Sqls>();
		
		try {
			resultList = sqlsService.sqlStatTrend(sqls);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/performanceCheckResult", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String performanceCheckResult(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = new ArrayList<Sqls>();
		try {
			resultList = sqlsService.performanceCheckResult(sqls);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/performanceCheckResultException", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String performanceCheckResultException(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = new ArrayList<Sqls>();
		try {
			resultList = sqlsService.performanceCheckResultException(sqls);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/bindValueListAll", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String bindValueListAll(@ModelAttribute("sqlsDetail") SqlsDetail sqlsDetail, Model model) {
		List<SqlsDetail> resultList = new ArrayList<SqlsDetail>();
		try {
			resultList = sqlsService.bindValueListAll(sqlsDetail);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* SQL 정보  - Bind Value Next action */
	@RequestMapping(value="/BindValueNext", method=RequestMethod.POST)
	@ResponseBody
	public Result BindValueListNextAction(@ModelAttribute("sqlsDetail") SqlsDetail sqlsDetail,  Model model) {
		Result result = new Result();
		List<SqlsDetail> resultList = new ArrayList<SqlsDetail>();

		try{
			resultList = sqlsService.bindValueListAll(sqlsDetail);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/sqlTreePlanListAll", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlTreePlanListAll(@ModelAttribute("sqlGrid") SqlGrid sqlGrid, Model model) {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		
		try {
			resultList = sqlsService.sqlTreePlanListAll(sqlGrid);
			
			List<SqlGrid> buildList = TreeWrite.buildSQLTree(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
			
			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		return returnValue;	
	}
	
	@RequestMapping(value = "/sqlTextPlanListAll", method = RequestMethod.POST)
	@ResponseBody
	public Result sqlTextPlanListAll(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model) {
		Result result = new Result();
		
		try {
			result.setResult(true);
			result.setObject(sqlsService.sqlTextPlanListAll(odsHistSqlText));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/sqlGridPlanListAll", method = RequestMethod.POST)
	@ResponseBody
	public String sqlGridPlanListAll(@ModelAttribute("sqlGrid") SqlGrid sqlGrid, Model model) {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		
		try {
			resultList = sqlsService.sqlGridPlanListAll(sqlGrid);
			List<SqlGrid> buildList = TreeWrite.buildSqlGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
			
			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return returnValue;
	}
	
	@RequestMapping(value = "/outLineListAll", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String outLineListAll(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		try {
			resultList = sqlsService.outLineListAll(odsHistSqlstat);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/sqlPerformHistoryList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlPerformHistoryList(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = new ArrayList<Sqls>();
		try {
			resultList = sqlsService.sqlPerformHistoryList(sqls);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
}