package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

import omc.spop.base.InterfaceController;
import omc.spop.model.Cd;
import omc.spop.model.FullscanSql;
import omc.spop.model.LiteralSql;
import omc.spop.model.NewSql;
import omc.spop.model.NonpredDeleteStmt;
import omc.spop.model.OffloadEffcReduceSql;
import omc.spop.model.OffloadSql;
import omc.spop.model.PlanChangeSql;
import omc.spop.model.Result;
import omc.spop.model.SqlDiagSummary;
import omc.spop.model.TempUsageSql;
import omc.spop.model.TopSql;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.CommonService;
import omc.spop.service.SQLDiagnosticsService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.04.11	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class SQLDiagnosticsController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLDiagnosticsController.class);
	
	@Autowired
	private SQLDiagnosticsService sqlDiagnosticsService;

	@Autowired
	private CommonService commonService;
	
	/* SQL 진단 - 개요 */
	@RequestMapping(value="/SQLDiagnostics/Summary", method=RequestMethod.GET)
	public String SQLDiagnostics(@ModelAttribute("sqlDiagSummary") SqlDiagSummary sqlDiagSummary, Model model) {
		String nowDate = DateUtil.getNextDay("M", "yyyy-MM-dd", DateUtil.getNowDate("yyyy-MM-dd"), "1");
		List<Cd> tableTitleList = new ArrayList<Cd>();
		try{
			tableTitleList = sqlDiagnosticsService.getTableTitleList();
		}catch(Exception e){
			logger.error("error => " + e.getMessage());
		}
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("tableTitleList", tableTitleList );
		model.addAttribute("menu_id", sqlDiagSummary.getMenu_id() );
		model.addAttribute("menu_nm", sqlDiagSummary.getMenu_nm() );
		model.addAttribute("call_from_parent", sqlDiagSummary.getCall_from_parent() );

		return "sqlDiagnostics/summary";
	}
	
	/* SQL 진단 - 개요 action */
	@RequestMapping(value="/SQLDiagnostics/Summary", method=RequestMethod.POST)
	@ResponseBody
	public Result SQLDiagnosticsAction(@ModelAttribute("sqlDiagSummary") SqlDiagSummary sqlDiagSummary, Model model) {
		Result result = new Result();
		List<SqlDiagSummary> resultList = new ArrayList<SqlDiagSummary>();

		try{
			resultList = sqlDiagnosticsService.summaryList(sqlDiagSummary);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setMessage("SQL 진단 개요 조회에 실패하였습니다.");
			result.setTxtValue(sqlDiagSummary.getDay_gubun());
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* SQL 진단 - 개요[챠트] action */
	@RequestMapping(value="/SQLDiagnostics/SummaryChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ChartInfoAction(@ModelAttribute("sqlDiagSummary") SqlDiagSummary sqlDiagSummary, Model model) {
		List<SqlDiagSummary> resultList = new ArrayList<SqlDiagSummary>();

		try{
			resultList = sqlDiagnosticsService.summaryChartList(sqlDiagSummary);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 진단 - PLAN 변경 SQL  */
	@RequestMapping(value="/SQLDiagnostics/PlanChangeSQL", method=RequestMethod.GET)
	public String PlanChangeSQL(@ModelAttribute("planChangeSql") PlanChangeSql planChangeSql, Model model) {
		
		model.addAttribute("menu_id", planChangeSql.getMenu_id() );
		model.addAttribute("menu_nm", planChangeSql.getMenu_nm() );
		
		return "sqlDiagnostics/planChangeSQL";
	}
	
	/* SQL 진단 - PLAN 변경 SQL action */
	@RequestMapping(value="/SQLDiagnostics/PlanChangeSQL", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PlanChangeSQLAction(@ModelAttribute("planChangeSql") PlanChangeSql planChangeSql,  Model model) {
		List<PlanChangeSql> resultList = new ArrayList<PlanChangeSql>();

		try{
			resultList = sqlDiagnosticsService.planChangeSqlList(planChangeSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	/* SQL 진단 - PLAN 변경 SQL - SQL Profile UPDATE*/
	@RequestMapping(value="/SQLDiagnostics/UpdatePlanChangeSQLProfile", method=RequestMethod.POST)
	@ResponseBody
	public Result UpdatePlanChangeSQLProfile(@ModelAttribute("planChangeSql") PlanChangeSql planChangeSql,  Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try{
			rowCnt = sqlDiagnosticsService.updatePlanChangeSqlProfile(planChangeSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("SQL 프로파일 UPDATE에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* SQL 진단 - 신규  SQL  */
	@RequestMapping(value="/SQLDiagnostics/NewSQL", method=RequestMethod.GET)
	public String NewSQL(@ModelAttribute("newSql") NewSql newSql, Model model) {
		model.addAttribute("menu_id", newSql.getMenu_id() );
		model.addAttribute("menu_nm", newSql.getMenu_nm() );
		
		return "sqlDiagnostics/newSQL";
	}

	/* SQL 진단 - 신규  SQL action */
	@RequestMapping(value="/SQLDiagnostics/NewSQL", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String NewSQLAction(@ModelAttribute("newSql") NewSql newSql,  Model model) {
		List<NewSql> resultList = new ArrayList<NewSql>();

		try{
			resultList = sqlDiagnosticsService.newSqlList(newSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* SQL 진단 - Literal SQL  */
	@RequestMapping(value="/SQLDiagnostics/LiteralSQL", method=RequestMethod.GET)
	public String LiteralSQL(@ModelAttribute("literalSql") LiteralSql literalSql, Model model) {
		model.addAttribute("menu_id", literalSql.getMenu_id() );
		model.addAttribute("menu_nm", literalSql.getMenu_nm() );
		
		if(literalSql.getLiteral_type_cd().equals("1")){ // SQL TEXT 기반
			return "sqlDiagnostics/literalSQL/sqlText";	
		}else{ // PLAN HASH VALUE 기반
			return "sqlDiagnostics/literalSQL/planHashValue";
		}
	}
	
	/* SQL 진단 - Literal SQL list action */
	@RequestMapping(value="/SQLDiagnostics/LiteralSQL", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String LiteralSQLAction(@ModelAttribute("literalSql") LiteralSql literalSql,  Model model) {
		List<LiteralSql> resultList = new ArrayList<LiteralSql>();

		try{
			resultList = sqlDiagnosticsService.literalSqlList(literalSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	/* SQL 진단 - Literal SQL list Status action */
	@RequestMapping(value="/SQLDiagnostics/LiteralSQLStatus", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String LiteralSQLStatusAction(@ModelAttribute("literalSql") LiteralSql literalSql,  Model model) {
		List<LiteralSql> resultList = new ArrayList<LiteralSql>();
		int dataCount4NextBtn = 0;

		try{
			resultList = sqlDiagnosticsService.literalSqlStatusList(literalSql);
			if (resultList != null && resultList.size() > literalSql.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(literalSql.getPagePerCount());
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
	
	/* SQL 진단 - TEMP과다사용 SQL  */
	@RequestMapping(value="/SQLDiagnostics/TempOveruseSQL", method=RequestMethod.GET)
	public String TempOveruseSQL(@ModelAttribute("tempUsageSql") TempUsageSql tempUsageSql, Model model) {
		model.addAttribute("menu_id", tempUsageSql.getMenu_id() );
		model.addAttribute("menu_nm", tempUsageSql.getMenu_nm() );
		
		return "sqlDiagnostics/tempOveruseSQL";
	}
	
	/* SQL 진단 - TEMP과다사용 SQL action */
	@RequestMapping(value="/SQLDiagnostics/TempOveruseSQL", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TempOveruseSQLAction(@ModelAttribute("tempUsageSql") TempUsageSql tempUsageSql,  Model model) {
		List<TempUsageSql> resultList = new ArrayList<TempUsageSql>();

		try{
			resultList = sqlDiagnosticsService.tempOveruseSqlList(tempUsageSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 진단 - FULL SCAN  */
	@RequestMapping(value="/SQLDiagnostics/FullScan", method=RequestMethod.GET)
	public String FullScan(@ModelAttribute("fullscanSql") FullscanSql fullscanSql, Model model) {

		
		Vector<String> temp_list = sqlDiagnosticsService.getExceptionList("1078");
		String exception_list = "";

		for(int i = 0; i < temp_list.size(); i++){
			if(temp_list.size() == i+1){	
				exception_list += temp_list.get(i).toString();
			break;
			}
			exception_list += temp_list.get(i).toString() + ", ";	
		}
		model.addAttribute("menu_id", fullscanSql.getMenu_id() );
		model.addAttribute("menu_nm", fullscanSql.getMenu_nm() );
		model.addAttribute("exception_list", exception_list);

		return "sqlDiagnostics/fullScan";
	}
	
	/* SQL 진단 - FULL SCAN list action */
	@RequestMapping(value="/SQLDiagnostics/FullScan", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String FullScanAction(@ModelAttribute("fullscanSql") FullscanSql fullscanSql,  Model model) {
		List<FullscanSql> resultList = new ArrayList<FullscanSql>();

		try{
			resultList = sqlDiagnosticsService.fullscanSqlList(fullscanSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* SQL 진단 - 조건절 없는 DELETE문  */
	@RequestMapping(value="/SQLDiagnostics/DeleteWithoutCondition", method=RequestMethod.GET)
	public String DeleteWithoutCondition(@ModelAttribute("nonpredDeleteStmt") NonpredDeleteStmt nonpredDeleteStmt, Model model) {
		model.addAttribute("menu_id", nonpredDeleteStmt.getMenu_id() );
		model.addAttribute("menu_nm", nonpredDeleteStmt.getMenu_nm() );
		
		return "sqlDiagnostics/deleteWithoutCondition";
	}
	
	/* SQL 진단 - 조건절 없는 DELETE문 list action */
	@RequestMapping(value="/SQLDiagnostics/DeleteWithoutCondition", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DeleteWithoutConditionAction(@ModelAttribute("nonpredDeleteStmt") NonpredDeleteStmt nonpredDeleteStmt,  Model model) {
		List<NonpredDeleteStmt> resultList = new ArrayList<NonpredDeleteStmt>();

		try{
			resultList = sqlDiagnosticsService.deleteWithoutConditionList(nonpredDeleteStmt);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	
	/*20191028 추가*/
	
	/* SQL 진단 - TOP SQL  */
	@RequestMapping(value="/SQLDiagnostics/TopSql", method=RequestMethod.GET)
	public String TopSql(@ModelAttribute("topSql") TopSql topSql, Model model) {
		model.addAttribute("menu_id", topSql.getMenu_id() );
		model.addAttribute("menu_nm", topSql.getMenu_nm() );
		
		return "sqlDiagnostics/topSql";
	}
	
	/* SQL 진단 - TOP SQL */
	@RequestMapping(value="/SQLDiagnostics/TopSql", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TopSqlAction(@ModelAttribute("topSql") TopSql topSql,  Model model) {
		List<TopSql> resultList = new ArrayList<TopSql>();
		
		try{
			resultList = sqlDiagnosticsService.topSqlList(topSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 진단 - OFF LOAD SQL  */
	@RequestMapping(value="/SQLDiagnostics/OffloadSql", method=RequestMethod.GET)
	public String OffloadSql(@ModelAttribute("offloadSql") OffloadSql offloadSql, Model model) {
		model.addAttribute("menu_id", offloadSql.getMenu_id() );
		model.addAttribute("menu_nm", offloadSql.getMenu_nm() );
		
		return "sqlDiagnostics/offloadSql";
	}
	
	/* SQL 진단 - OFF LOAD SQL */
	@RequestMapping(value="/SQLDiagnostics/OffloadSql", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String OffloadSqlAction(@ModelAttribute("offloadSql") OffloadSql offloadSql,  Model model) {
		List<OffloadSql> resultList = new ArrayList<OffloadSql>();
		
		try{
			resultList = sqlDiagnosticsService.offloadSqlList(offloadSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 진단 - OFF LOAD EFF REDUCE SQL  */
	@RequestMapping(value="/SQLDiagnostics/OffloadEffcReduceSql", method=RequestMethod.GET)
	public String OffloadEffcReduceSql(@ModelAttribute("offloadEffcReduceSql") OffloadEffcReduceSql offloadEffcReduceSql, Model model) {
		model.addAttribute("menu_id", offloadEffcReduceSql.getMenu_id() );
		model.addAttribute("menu_nm", offloadEffcReduceSql.getMenu_nm() );
		
		return "sqlDiagnostics/offloadEffcReduceSql";
	}
	
	/* SQL 진단 - OFF LOAD EFF REDUCE SQL  */
	@RequestMapping(value="/SQLDiagnostics/OffloadEffcReduceSql", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String OffloadEffcReduceSqlAction(@ModelAttribute("offloadEffcReduceSql") OffloadEffcReduceSql offloadEffcReduceSql,  Model model) {
		List<OffloadEffcReduceSql> resultList = new ArrayList<OffloadEffcReduceSql>();
		
		try{
			resultList = sqlDiagnosticsService.offloadEffcReduceSqlList(offloadEffcReduceSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}

	
	
	
	
	/* SQL 진단 - 튜닝요청 팝업  */
	@RequestMapping(value="/SQLDiagnostics/Popup/InsertTuningRequest", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertTuningRequest(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		Result result = new Result();
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		try{
			logger.debug("##### 튜닝요청 팝업 - 튜닝요청 진행 #####");
			resultList = sqlDiagnosticsService.insertTuningRequest(tuningTargetSql);
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
}