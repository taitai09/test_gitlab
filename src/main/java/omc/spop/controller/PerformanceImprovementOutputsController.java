package omc.spop.controller;

import java.util.ArrayList;
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
import omc.spop.base.SessionManager;
import omc.spop.model.DownLoadFile;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.ImprovementManagementService;
import omc.spop.service.PerformanceImprovementOutputsService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.06.01	이원식	최초작업
 * 2021.07.15	이재우	성능개선실적 > 산출물개선
 **********************************************************/

@Controller
public class PerformanceImprovementOutputsController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(PerformanceImprovementOutputsController.class);

	@Autowired
	private PerformanceImprovementOutputsService performanceImprovementOutputsService;
	
	@Autowired
	private ImprovementManagementService improvementManagementService;
	
	/* 성능개선결과 산출물 리스트  */
	@RequestMapping(value="/PerformanceImprovementOutputs")
	public String PerformanceImprovementOutputs(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		//String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6) ;

		if(tuningTargetSql.getStrStartDt() == null || tuningTargetSql.getStrStartDt().equals("")){
			tuningTargetSql.setStrStartDt(startDate);
		}
		
		if(tuningTargetSql.getStrEndDt() == null || tuningTargetSql.getStrEndDt().equals("")){
			tuningTargetSql.setStrEndDt(nowDate);
		}

		model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
		model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());
		model.addAttribute("call_from_parent", tuningTargetSql.getCall_from_parent());

		return "performanceImprovementOutputs";
	}
	
	/**
	 * 성능개선결과 산출물 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param tuningTargetSql
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/PerformanceImprovementOutputs/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PerformanceImprovementOutputsExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		
		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}

		try {
			resultList = performanceImprovementOutputsService.performanceImprovementOutputsList4Excel(tuningTargetSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능개선결과_산출물");
		model.addAttribute("sheetName", "성능개선결과_산출물");
		model.addAttribute("excelId", "TUNING_TARGET_SQL");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	@RequestMapping(value = "/PerformanceImprovementOutputs/ExcelDown_V2", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PerformanceImprovementOutputsExcelDown_V2(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		
		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}
		
		try {
			resultList = performanceImprovementOutputsService.performanceImprovementOutputsList4Excel_V2(tuningTargetSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능개선결과_산출물");
		model.addAttribute("sheetName", "성능개선결과_산출물");
		model.addAttribute("excelId", "TUNING_TARGET_SQL_V2");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 성능개선결과 산출물 리스트 - list action */
	@RequestMapping(value="/PerformanceImprovementOutputsAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PerformanceImprovementOutputsAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		
		int dataCount4NextBtn = 0;
		
		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}
		
		try{
			resultList = performanceImprovementOutputsService.performanceImprovementOutputsList(tuningTargetSql);
			
			if ( resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	/* 성능개선결과 산출물 다운로드 ALL*/
	@RequestMapping(value="/PerformanceImprovementOutputsListAll", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PerformanceImprovementOutputsListAll(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		
		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}
		
		try{
			resultList = performanceImprovementOutputsService.performanceImprovementOutputsListAll( tuningTargetSql );
		
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 성능개선결과 산출물 다운로드 ALL*/
	@RequestMapping(value="/PerformanceImprovementOutputsListAll_V2", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PerformanceImprovementOutputsListAll_V2(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		
		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}
		
		try{
			resultList = performanceImprovementOutputsService.performanceImprovementOutputsListAll_V2( tuningTargetSql );
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 성능개선결과 산출물 리스트 - list action 수협 */
	@RequestMapping(value="/PerformanceImprovementOutputsAction_V2", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PerformanceImprovementOutputsAction_V2(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		
		int dataCount4NextBtn = 0;
		
		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}
		
		try{
			resultList = performanceImprovementOutputsService.performanceImprovementOutputsList_V2(tuningTargetSql);
			
			if ( resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	/* 성능개선결과 산출물 리스트 상세 */
	@RequestMapping(value = "/PerformanceImprovementOutputsView")
	public String PerformanceImprovementOutputsView(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		String choickDivCd = tuningTargetSql.getChoice_div_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		try{
			// 1. 성능개선현황 상태 조회
			model.addAttribute("sqlTuning", improvementManagementService.getSQLTuning(tuningTargetSql));
			
			// 2-1. SQL (선정) 상세 : CHOICE_DIV_CD -> 1, 2
			// 2-2. SQL (요청) 상세 : CHOICE_DIV_CD -> 3
			// 2-3. SQL (FULL SCAN) 상세 : CHOICE_DIV_CD -> 4
			// 2-4. SQL (PLAN 변경) 상세 : CHOICE_DIV_CD -> 5
			// 2-5. SQL (신규SQL) 상세 : CHOICE_DIV_CD -> 6
			// 2-6. SQL (TEMP과다사용) 상세 : CHOICE_DIV_CD -> 7
			
			if(choickDivCd.equals("1") || choickDivCd.equals("2")){
				model.addAttribute("selection", improvementManagementService.getSelection(tuningTargetSql));
			}else if(choickDivCd.equals("3")||choickDivCd.equals("B")){
				model.addAttribute("request", improvementManagementService.getRequest(tuningTargetSql));
				model.addAttribute("bindSetList", improvementManagementService.bindSetList(tuningTargetSql));
				model.addAttribute("sqlBindList", improvementManagementService.sqlBindList(tuningTargetSql));
			}else if(choickDivCd.equals("4")){
				model.addAttribute("fullScan", improvementManagementService.getFullScan(tuningTargetSql));
			}else if(choickDivCd.equals("5")){
				model.addAttribute("planChange", improvementManagementService.getPlanChange(tuningTargetSql));
			}else if(choickDivCd.equals("6")){
				model.addAttribute("newSql", improvementManagementService.getNewSQL(tuningTargetSql));
			}else if(choickDivCd.equals("7")){
				model.addAttribute("tempOver", improvementManagementService.getTempOver(tuningTargetSql));
			} else {
				model.addAttribute("request", improvementManagementService.getRequest(tuningTargetSql));
				model.addAttribute("bindSetList", improvementManagementService.bindSetList(tuningTargetSql));
				model.addAttribute("sqlBindList", improvementManagementService.sqlBindList(tuningTargetSql));
			}

			// 3. SQL 개선사항
//			model.addAttribute("improvements", improvementManagementService.getImprovements(tuningTargetSql));
			TuningTargetSql sqlDetail = improvementManagementService.getImprovements(tuningTargetSql);
			String impr_sql_text = StringUtil.formatHTML(sqlDetail.getImpr_sql_text());
			String imprb_exec_plan = StringUtil.formatHTML(sqlDetail.getImprb_exec_plan());
			String impra_exec_plan = StringUtil.formatHTML(sqlDetail.getImpra_exec_plan());
			sqlDetail.setImpr_sql_text(impr_sql_text);
			sqlDetail.setImprb_exec_plan(imprb_exec_plan);
			sqlDetail.setImpra_exec_plan(impra_exec_plan);			
//			String imprb_exec_plan = StringUtils.defaultString(sqlDetail.getImprb_exec_plan());
//			imprb_exec_plan = imprb_exec_plan.replaceAll("(\r\n|\\r\\n|\\\\r\\\\n|\\n|\\\\n)", "<br/>");
//			imprb_exec_plan = imprb_exec_plan.replaceAll(" ", "&nbsp;");
//			sqlDetail.setImprb_exec_plan(imprb_exec_plan);
//
//			String impra_exec_plan = StringUtils.defaultString(sqlDetail.getImpra_exec_plan());
//			impra_exec_plan = impra_exec_plan.replaceAll("(\r\n|\\r\\n|\\\\r\\\\n|\\n|\\\\n)", "<br/>");
//			impra_exec_plan = impra_exec_plan.replaceAll(" ", "&nbsp;");
//			sqlDetail.setImpra_exec_plan(impra_exec_plan);
			
			model.addAttribute("sqlDetail", sqlDetail);

			model.addAttribute("choickDivCd", choickDivCd);
			model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
			model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());
			model.addAttribute("loginUser", user_id);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return "performanceImprovementOutputsView";
	}	
	
	@RequestMapping(value = "/getPerformanceImprovementOutputs", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView getPerformanceImprovementOutputs(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) throws Exception{
		DownLoadFile downloadFile = new DownLoadFile();
		
		try {
			downloadFile = performanceImprovementOutputsService.getPerformanceImprovementOutputs(tuningTargetSql);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}
	
	@RequestMapping(value = "/getPerformanceImprovementOutputs_V2", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView getPerformanceImprovementOutputs_V2(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) throws Exception{
		DownLoadFile downloadFile = new DownLoadFile();
//		if ( tuningTargetSql.getAsis_sql_id() != null && "".equals(tuningTargetSql.getAsis_sql_id()) == false ) {
//			tuningTargetSql.setSql_id( tuningTargetSql.getAsis_sql_id() );
//		}
		
		try {
			TuningTargetSql temp = new TuningTargetSql();
			
			temp.setTuning_no( tuningTargetSql.getTuning_no() );
			temp.setChoice_div_cd( tuningTargetSql.getChoice_div_cd() );
			
			downloadFile = performanceImprovementOutputsService.getPerformanceImprovementOutputs( tuningTargetSql );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}
	
	@RequestMapping(value = "/getPerformanceImprovementOutputsAll", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView getPerformanceImprovementOutputsAll(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) throws Exception{
		DownLoadFile downloadFile = new DownLoadFile();
		
		try {
			downloadFile = performanceImprovementOutputsService.getPerformanceImprovementOutputsAll(tuningTargetSql);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}
}