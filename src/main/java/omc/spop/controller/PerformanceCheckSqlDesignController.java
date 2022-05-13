package omc.spop.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import omc.spop.base.InterfaceController;
import omc.spop.model.PerformanceCheckSql;
import omc.spop.model.Sqls;

/***********************************************************
 * 2020.05.12	명성태	OPENPOP V2 최초작업
 **********************************************************/
@RequestMapping(value = "/PerformanceCheckSqlDesign")
@Controller
public class PerformanceCheckSqlDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckSqlDesignController.class);
	
	/* SQL 성능 추적 현황 > 성능 점검 SQL 메뉴 Design */
	@RequestMapping(value="/design", method=RequestMethod.GET)
	public String design(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "performanceCheckSqlDesign/design";
	}
	
	/* SQL 성능 추적 현황 탭 Design */
	@RequestMapping(value="/sqlPerformanceTraceStatusDesign", method=RequestMethod.GET)
	public String sqlPerformanceTraceStatusDesign(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql, Model model) {
		model.addAttribute("menu_id", performanceCheckSql.getMenu_id());
		model.addAttribute("menu_nm", performanceCheckSql.getMenu_nm());
		
		return "performanceCheckSqlDesign/sqlPerformanceTraceStatus/sqlPerformanceTraceStatusDesign";
	}
	
	/* SQLs 탭 */
	@RequestMapping(value="/sqls", method=RequestMethod.GET, produces = "application/text; charset=utf8")
	public String sqls(@ModelAttribute("sqls") Sqls sqls, Model model) {
		String isHandOff = sqls.getIsHandOff();
		String isCheckHighestRank = sqls.getIsCheckHighestRank();
		String wrkjob_cd = sqls.getWrkjob_cd();
		String begin_dt = sqls.getBegin_dt();
		String end_dt = sqls.getEnd_dt();
		int base_period_value = sqls.getBase_period_value();
		String isCheckFail = sqls.getIsCheckFail();
		String isCheckPass = sqls.getIsCheckPass();
		String selectSearchType = sqls.getSelectSearchType();
		String isRegressYn = sqls.getIsRegressYn();
		String selectPerfRegressedMetric = sqls.getSelectPerfRegressedMetric();
		
		String selectSqlPerfTrace = sqls.getSelectSqlPerfTrace();
		String selectElapsedTimeMetirc = sqls.getSelectElapsedTimeMetirc();
		
		model.addAttribute("isHandOff", isHandOff);
		model.addAttribute("isCheckHighestRank", isCheckHighestRank);
		model.addAttribute("wrkjob_cd", wrkjob_cd);
		model.addAttribute("begin_dt", begin_dt);
		model.addAttribute("end_dt", end_dt);
		model.addAttribute("base_period_value", base_period_value);
		model.addAttribute("isCheckFail", isCheckFail);
		model.addAttribute("isCheckPass", isCheckPass);
		model.addAttribute("selectSearchType", selectSearchType);
		model.addAttribute("isRegressYn", isRegressYn);
		model.addAttribute("selectPerfRegressedMetric", selectPerfRegressedMetric);
		
		model.addAttribute("selectSqlPerfTrace", selectSqlPerfTrace);
		model.addAttribute("selectElapsedTimeMetirc", selectElapsedTimeMetirc);
		
		model.addAttribute("menu_id", sqls.getMenu_id());
		model.addAttribute("menu_nm", sqls.getMenu_nm());
		
		return "performanceCheckSqlDesign/sqls/sqls";
	}
	
	/* SQLs Detail 탭 */
	@RequestMapping(value="/sqlsDetail", method=RequestMethod.GET, produces = "application/text; charset=utf8")
	public String PerformanceCheckSqlDesign(@ModelAttribute("sqls") Sqls sqls, Model model) {
		String dbid = sqls.getDbid();
		String sql_id = sqls.getSql_id();
		String plan_hash_value = sqls.getPlan_hash_value();
		String begin_dt = sqls.getBegin_dt();
		
		model.addAttribute("dbid", dbid);
		model.addAttribute("sql_id", sql_id);
		model.addAttribute("plan_hash_value", plan_hash_value);
		model.addAttribute("begin_dt", begin_dt);
		
		return "performanceCheckSqlDesign/sqls/sqlsDetail";
	}
	
	/* 실행 SQL 성능 변화 추적 > 성능 검증 SQL 메뉴 Design */
	@RequestMapping(value="/sqlPerformanceChangeTrackingDesign", method=RequestMethod.GET)
	public String sqlPerformanceChangeTrackingDesign(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "performanceCheckSqlDesign/sqlPerformanceChangeTrackingDesign";
	}
	

	/* SQL 성능 추적 현황 탭 Design */
	@RequestMapping(value="/sqlPerformanceTrackingStatusDesign", method=RequestMethod.GET)
	public String sqlPerformanceTrackingStatusDesign(@ModelAttribute("performanceCheckSql") PerformanceCheckSql performanceCheckSql, Model model) {
		model.addAttribute("menu_id", performanceCheckSql.getMenu_id());
		model.addAttribute("menu_nm", performanceCheckSql.getMenu_nm());
		
		return "performanceCheckSqlDesign/sqlPerformanceTrackingStatus/sqlPerformanceTrackingStatusDesign";
	}
	
	/* SQLs 탭 */
	@RequestMapping(value="/autoSqls", method=RequestMethod.GET, produces = "application/text; charset=utf8")
	public String autoSqls(@ModelAttribute("sqls") Sqls sqls, Model model) {
		String isHandOff = sqls.getIsHandOff();
		String isCheckHighestRank = sqls.getIsCheckHighestRank();
		String wrkjob_cd = sqls.getWrkjob_cd();
		String begin_dt = sqls.getBegin_dt();
		String end_dt = sqls.getEnd_dt();
		int base_period_value = sqls.getBase_period_value();
		String isCheckFail = sqls.getIsCheckFail();
		String isCheckPass = sqls.getIsCheckPass();
		String selectSearchType = sqls.getSelectSearchType();
		String isRegressYn = sqls.getIsRegressYn();
		String selectPerfRegressedMetric = sqls.getSelectPerfRegressedMetric();
		
		String selectSqlPerfTrace = sqls.getSelectSqlPerfTrace();
		String selectElapsedTimeMetirc = sqls.getSelectElapsedTimeMetirc();
		
		model.addAttribute("isHandOff", isHandOff);
		model.addAttribute("isCheckHighestRank", isCheckHighestRank);
		model.addAttribute("wrkjob_cd", wrkjob_cd);
		model.addAttribute("begin_dt", begin_dt);
		model.addAttribute("end_dt", end_dt);
		model.addAttribute("base_period_value", base_period_value);
		model.addAttribute("isCheckFail", isCheckFail);
		model.addAttribute("isCheckPass", isCheckPass);
		model.addAttribute("selectSearchType", selectSearchType);
		model.addAttribute("isRegressYn", isRegressYn);
		model.addAttribute("selectPerfRegressedMetric", selectPerfRegressedMetric);
		
		model.addAttribute("selectSqlPerfTrace", selectSqlPerfTrace);
		model.addAttribute("selectElapsedTimeMetirc", selectElapsedTimeMetirc);
		
		model.addAttribute("menu_id", sqls.getMenu_id());
		model.addAttribute("menu_nm", sqls.getMenu_nm());
		
		return "performanceCheckSqlDesign/sqls/autoSqls";
	}
	
}