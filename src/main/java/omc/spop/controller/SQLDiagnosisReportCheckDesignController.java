package omc.spop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2021.09.15	김원재	최초작성
 **********************************************************/

@RequestMapping("/SQLDiagnosisReportDesign")
@Controller
public class SQLDiagnosisReportCheckDesignController extends InterfaceController {
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String design(@RequestParam(required = false) Map<String, String> param, Model model) {
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "report/sqlDiagnosisReportCheckDesign";
	}
	@RequestMapping(value = "/SQLDiagnosisReport", method = RequestMethod.GET)
	public String sqlDiagnosisReport( @RequestParam(required = false) Map<String, String> param , Model model) {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -30);
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));

		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		
		model.addAttribute("call_from_parent",param.get("call_from_parent"));
		model.addAttribute("std_qty_target_dbid",param.get("std_qty_target_dbid"));
		model.addAttribute("sql_std_qty_scheduler_no",param.get("sql_std_qty_scheduler_no"));

		return "report/sqlDiagnosisReport";
	}
	@RequestMapping(value = "/SQLDiagnosisReportStatus", method = RequestMethod.GET)
	public String SQLDiagnosisReportStatus( @RequestParam(required = false) Map<String, String> param , Model model) {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -30);
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));

		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		
		return "report/sqlDiagnosisReportStatus";
	}

	@RequestMapping(value = "/SQLDiagnosisReportManageScheduler", method = RequestMethod.GET)
	public String SQLDiagnosisReportManageScheduler( @RequestParam(required = false) Map<String, String> param , Model model) {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String lastMonth = DateUtil.getLastMonth("yyyy-MM-dd");
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));

		model.addAttribute("lastMonth", lastMonth);
		model.addAttribute("nowDate", nowDate);
		
		return "report/sqlDiagnosisReportManageScheduler";
	}
}