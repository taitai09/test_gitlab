package omc.spop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import omc.spop.base.InterfaceController;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.TopsqlAutoChoice;
import omc.spop.model.TopsqlHandopChoice;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2019.09.16 initiate
 **********************************************************/

@Controller
public class manualSelectionDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(manualSelectionDesignController.class);


	/* SQL 성능 점검 - 수동 선정 초기 */
	@RequestMapping(value="/sqlPerformanceDesign/manualSelection/manualSelectionDesign")
	public String ManualSelectionDesign(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, 
			Model model, HttpSession session) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("startDate", startDate );
		model.addAttribute("menu_id", odsHistSqlstat.getMenu_id() );
		model.addAttribute("menu_nm", odsHistSqlstat.getMenu_nm() );
		model.addAttribute("call_from_child", odsHistSqlstat.getCall_from_child() );
		
		if(odsHistSqlstat.getCall_from_parent() != null && odsHistSqlstat.getCall_from_parent() != "Y") {
			model.addAttribute("dbid", odsHistSqlstat.getDbid());
			model.addAttribute("start_snap_id", odsHistSqlstat.getStart_snap_id());
			model.addAttribute("end_snap_id", odsHistSqlstat.getEnd_snap_id());
			model.addAttribute("elap_time", odsHistSqlstat.getElapsed_time());
			model.addAttribute("buffer_gets_threshold", odsHistSqlstat.getBuffer_gets());
			model.addAttribute("executions_threshold", odsHistSqlstat.getExecutions());
			model.addAttribute("topn_cnt", odsHistSqlstat.getTopn_cnt());
			model.addAttribute("selectOrdered", odsHistSqlstat.getSelectOrdered());
			model.addAttribute("before_choice_sql_except_yn", odsHistSqlstat.getBefore_choice_sql_except_yn());
			model.addAttribute("call_from_parent", odsHistSqlstat.getCall_from_parent());
			
			logger.debug("selectOrdered[" + odsHistSqlstat.getSelectOrdered() + "] before_choice_sql_except_yn[" + odsHistSqlstat.getBefore_choice_sql_except_yn() + "]");
		}

		session.setAttribute("odsHistSqlstat", odsHistSqlstat);
		return "sqlPerformanceDesign/manualSelection/manualSelectionDesign";
	}
	/* SQL 성능 점검 - 수동 선정 */
	@RequestMapping(value="/sqlPerformanceDesign/manualSelection/manualSelection")
	public String ManualSelection(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, 
			Model model, HttpServletRequest request) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsHistSqlstat.getMenu_id() );
		model.addAttribute("menu_nm", odsHistSqlstat.getMenu_nm() );
		model.addAttribute("call_from_child", odsHistSqlstat.getCall_from_child() );
		
		if(odsHistSqlstat.getCall_from_parent() != null && odsHistSqlstat.getCall_from_parent() != "Y") {
			model.addAttribute("dbid", odsHistSqlstat.getDbid());
			model.addAttribute("start_snap_id", odsHistSqlstat.getStart_snap_id());
			model.addAttribute("end_snap_id", odsHistSqlstat.getEnd_snap_id());
			model.addAttribute("elap_time", odsHistSqlstat.getElapsed_time());
			model.addAttribute("buffer_gets_threshold", odsHistSqlstat.getBuffer_gets());
			model.addAttribute("executions_threshold", odsHistSqlstat.getExecutions());
			model.addAttribute("topn_cnt", odsHistSqlstat.getTopn_cnt());
			model.addAttribute("selectOrdered", odsHistSqlstat.getSelectOrdered());
			model.addAttribute("before_choice_sql_except_yn", odsHistSqlstat.getBefore_choice_sql_except_yn());
			model.addAttribute("call_from_parent", odsHistSqlstat.getCall_from_parent());
			
			logger.debug("selectOrdered[" + odsHistSqlstat.getSelectOrdered() + "] before_choice_sql_except_yn[" + odsHistSqlstat.getBefore_choice_sql_except_yn() + "]");
		}
		logger.debug("###############"+request.getSession().getAttribute("odsHistSqlstat"));
		if(request.getSession().getAttribute("odsHistSqlstat") != null){
			OdsHistSqlstat temp = (OdsHistSqlstat) request.getSession().getAttribute("odsHistSqlstat");
			model.addAttribute(request.getSession().getAttribute("odsHistSqlstat"));

		
		}
		
		return "sqlPerformanceDesign/manualSelection/manualSelection";
	}

	/*SQL 성능 점검 - 수동 선정 현황*/
	@RequestMapping(value="/sqlPerformanceDesign/manualSelection/manualSelectionStatus")
	public String ManualSelectionStatus(@ModelAttribute("topsqlHandopChoice") TopsqlHandopChoice topsqlHandopChoice, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(topsqlHandopChoice.getStrStartDt() == null || topsqlHandopChoice.getStrStartDt().equals("")){
			topsqlHandopChoice.setStrStartDt(startDate);
		}
		
		if(topsqlHandopChoice.getStrEndDt() == null || topsqlHandopChoice.getStrEndDt().equals("")){
			topsqlHandopChoice.setStrEndDt(nowDate);
		}		
		model.addAttribute("menu_id", topsqlHandopChoice.getMenu_id());
		model.addAttribute("menu_nm", topsqlHandopChoice.getMenu_nm() );
		return "sqlPerformanceDesign/manualSelection/manualSelectionStatus";
	}
	
}