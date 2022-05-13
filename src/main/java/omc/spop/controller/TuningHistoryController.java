package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import omc.spop.model.SqlTuningAttachFile;
import omc.spop.model.SqlTuningIndexHistory;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.service.ImprovementManagementService;
import omc.spop.service.TuningHistoryService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

@Controller
public class TuningHistoryController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(TuningHistoryController.class);

	@Autowired
	private TuningHistoryService tuningHistoryService;
	
	@Autowired
	private ImprovementManagementService improvementManagementService;
	
	/* 튜닝이력조회 */
	@RequestMapping(value = "/TuningHistory")
	public String TuningHistory(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
		model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());
		model.addAttribute("call_from_parent", tuningTargetSql.getCall_from_parent());

		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6) ;
		
		String start_tuning_complete_dt = StringUtils.defaultString(tuningTargetSql.getStart_tuning_complete_dt());
		String end_tuning_complete_dt = StringUtils.defaultString(tuningTargetSql.getEnd_tuning_complete_dt());
		if(!start_tuning_complete_dt.equals("")){
			tuningTargetSql.setStart_tuning_complete_dt(start_tuning_complete_dt);
		}else{
			tuningTargetSql.setStart_tuning_complete_dt(startDate);
		}
		if(!end_tuning_complete_dt.equals("")){
			tuningTargetSql.setEnd_tuning_complete_dt(end_tuning_complete_dt);
		}else{
			tuningTargetSql.setEnd_tuning_complete_dt(nowDate);
		}
		String call_from_parent = StringUtils.defaultString(tuningTargetSql.getCall_from_parent());
		String call_from_child = StringUtils.defaultString(tuningTargetSql.getCall_from_child());
		model.addAttribute("call_from_parent", call_from_parent);
		model.addAttribute("call_from_child", call_from_child);
		model.addAttribute("tuningTargetSql", tuningTargetSql);
		
		return "tuningHistory";
	}

	/* 튜닝이력조회 list action */
	@RequestMapping(value = "/TuningHistoryAction", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TuningHistoryListAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();

		int dataCount4NextBtn = 0;
		try {
			resultList = tuningHistoryService.tuningHistoryList(tuningTargetSql);
			if (resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* 튜닝이력조회 - 상세 */
	@RequestMapping(value = "/TuningHistory/View")
	public String TuningHistoryView(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql
			, Model model) {
		
		List<SqlTuningIndexHistory> sqlTuningIndexHistoryList = Collections.emptyList();
		List<SqlTuningAttachFile> readTuningFiles = Collections.emptyList();
		List<TuningTargetSqlBind> bindSetList = Collections.emptyList();
		List<TuningTargetSqlBind> sqlBindList = Collections.emptyList();
		
		try {
			TuningTargetSql sqlDetail = improvementManagementService.getImprovements(tuningTargetSql);
			
			String impr_sql_text = StringUtil.formatHTML(sqlDetail.getImpr_sql_text());
			String imprb_exec_plan = StringUtil.formatHTML(sqlDetail.getImprb_exec_plan());
			String impra_exec_plan = StringUtil.formatHTML(sqlDetail.getImpra_exec_plan());
			
			sqlDetail.setImpr_sql_text(impr_sql_text);
			sqlDetail.setImprb_exec_plan(imprb_exec_plan);
			sqlDetail.setImpra_exec_plan(impra_exec_plan);
			model.addAttribute("sqlDetail", sqlDetail);
			
			bindSetList = tuningHistoryService.bindSetList(tuningTargetSql);
			model.addAttribute("bindSetList", bindSetList);
			
			sqlBindList = tuningHistoryService.sqlBindList(tuningTargetSql);
			model.addAttribute("sqlBindList", sqlBindList);
			
			//튜닝이력조회상세-SQL개선상세-인덱스이력조회
			String update_dt = StringUtils.defaultString(sqlDetail.getTuning_complete_dt());
			logger.debug("update_dt:"+update_dt);
			tuningTargetSql.setUpdate_dt(update_dt);
			
			sqlTuningIndexHistoryList = tuningHistoryService.sqlTuningIndexHistoryList(tuningTargetSql);
			model.addAttribute("indexHistList", sqlTuningIndexHistoryList);
			
			readTuningFiles = improvementManagementService.readTuningAttachFiles(tuningTargetSql);
			model.addAttribute("tuningFiles",readTuningFiles);
			
			model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
			model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());
		
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
		}finally {
			sqlTuningIndexHistoryList = null;
			readTuningFiles = null;
			bindSetList = null;
			sqlBindList = null;
		}
		
		return "tuningHistoryView";
	}
	/* 인덱스이력조회 list action */
	@RequestMapping(value = "/SqlTuningIndexHistoryAction", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SqlTuningIndexHistoryAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			Model model) {
		List<SqlTuningIndexHistory> resultList = new ArrayList<SqlTuningIndexHistory>();

		try {
			String update_dt = StringUtils.defaultString(tuningTargetSql.getTuning_complete_dt());
			tuningTargetSql.setUpdate_dt(update_dt);
			resultList = tuningHistoryService.sqlTuningIndexHistoryList(tuningTargetSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/TuningHistory/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView TuningHistoryExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = tuningHistoryService.tuningHistoryList4Excel(tuningTargetSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "튜닝이력조회");
		model.addAttribute("sheetName", "튜닝이력조회");
		model.addAttribute("excelId", "TUNING_HIST_SEARCH");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

}