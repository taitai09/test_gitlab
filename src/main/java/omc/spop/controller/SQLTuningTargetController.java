package omc.spop.controller;

import java.util.ArrayList;
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
import omc.spop.model.OdsHistSnapshot;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.Result;
import omc.spop.model.SqlTuning;
import omc.spop.model.TopsqlAutoChoice;
import omc.spop.model.TopsqlHandopChoice;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.SQLTuningTargetService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.12	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class SQLTuningTargetController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLTuningTargetController.class);
	
	@Autowired
	private SQLTuningTargetService sqlTuningTargetService;

	/* 자동선정 */
//	@RequestMapping(value="/AutoSelection", method=RequestMethod.GET)
	@RequestMapping(value="/AutoSelection")
	public String AutoSelection(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		String thatMonth = DateUtil.getMonth();
		
		

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("startDate", startDate );
		model.addAttribute("menu_id", topsqlAutoChoice.getMenu_id() );
		model.addAttribute("menu_nm", topsqlAutoChoice.getMenu_nm() );
		model.addAttribute("call_from_child", topsqlAutoChoice.getCall_from_child() );
		
		return "sqlTuningTarget/autoSelection";
	}
	
	/* 자동선정 action */
	@RequestMapping(value="/AutoSelectionAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AutoSelectionAction(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) {
		List<TopsqlAutoChoice> resultList = new ArrayList<TopsqlAutoChoice>();

		try{
			resultList = sqlTuningTargetService.autoSelectionList(topsqlAutoChoice);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	/* DBID 조건의 선정조건번호  조회 */
	@RequestMapping(value = "/AutoSelection/getChoiceCondNo", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getChoiceConNo(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice) throws Exception {
		List<TopsqlAutoChoice> outerList = new ArrayList<TopsqlAutoChoice>();
		TopsqlAutoChoice tsAutoChoice = new TopsqlAutoChoice();
		List<TopsqlAutoChoice> resultList = new ArrayList<TopsqlAutoChoice>();
		String isAll = StringUtils.defaultString(topsqlAutoChoice.getIsAll());
		String isChoice = StringUtils.defaultString(topsqlAutoChoice.getIsChoice());
		
		resultList = sqlTuningTargetService.getChoiceCondNo(topsqlAutoChoice);
			
		if (isAll.equals("Y")) {
			tsAutoChoice.setAuto_choice_cond_no("전체");
		} else if (isChoice.equals("Y")) {
			tsAutoChoice.setAuto_choice_cond_no("선택");
		} else {
			tsAutoChoice.setAuto_choice_cond_no("");
		}
		
		outerList.add(tsAutoChoice);
		outerList.addAll(resultList);
		
		return success(outerList).toJSONObject().get("rows").toString();
	}
	
	/* 자동선정 save */
	@RequestMapping(value="/AutoSelection/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveAutoSelection(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice,  Model model) {
		Result result = new Result();
		try{
			sqlTuningTargetService.saveAutoSelection(topsqlAutoChoice);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 자동선정 save */
	@RequestMapping(value="/AutoSelection/BundleSave", method=RequestMethod.POST)
	@ResponseBody
	public Result bundleSaveAutoSelection(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice,  Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {
			String autoChoiceCondNoArray = StringUtils.defaultString(topsqlAutoChoice.getAutoChoiceCondNoArray());
			logger.debug("bundleSaveAutoSelection autoChoiceCondNoArray:"+autoChoiceCondNoArray);
			if(autoChoiceCondNoArray.equals("")){
				result.setResult(false);
				result.setMessage("자동 선정 일괄 수정 처리중 오류가 발생하였습니다.<br/>항목을 다시 선택하여 주세요.");
			}else{
				rowCnt = sqlTuningTargetService.bundleSaveAutoSelection(topsqlAutoChoice);
				logger.debug("bundleSaveAutoSelection rowCnt:"+rowCnt);
				if(rowCnt > 0){
					result.setResult(true);
					result.setMessage("자동 선정 일괄 수정에 성공하였습니다.");
				}else{
					result.setResult(false);
					result.setMessage("자동 선정 일괄 수정에 실패하였습니다.");
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 자동선정 - 이력 조회 */
	@RequestMapping(value="/AutoSelectionHistory")
	public String AutoSelectionHistory(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) {
		model.addAttribute("menu_id", topsqlAutoChoice.getMenu_id());
		model.addAttribute("menu_nm", topsqlAutoChoice.getMenu_nm() );
		return "sqlTuningTarget/autoSelectionHistory";
	}
	
	/* 자동선정 - 이력조회 action */
	@RequestMapping(value="/AutoSelectionHistoryAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AutoSelectionHistoryAction(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice,  Model model) {
		List<TopsqlAutoChoice> resultList = new ArrayList<TopsqlAutoChoice>();

		try{
			resultList = sqlTuningTargetService.autoSelectionHistoryList(topsqlAutoChoice);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 자동선정 - 자동선정현황  */
	@RequestMapping(value="/AutoSelectionStatus")
	public String AutoSelectionStatus(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(topsqlAutoChoice.getStrStartDt() == null || topsqlAutoChoice.getStrStartDt().equals("")){
			topsqlAutoChoice.setStrStartDt(startDate);
		}
		
		if(topsqlAutoChoice.getStrEndDt() == null || topsqlAutoChoice.getStrEndDt().equals("")){
			topsqlAutoChoice.setStrEndDt(nowDate);
		}

		model.addAttribute("menu_id", topsqlAutoChoice.getMenu_id());
		model.addAttribute("menu_nm", topsqlAutoChoice.getMenu_nm() );
		return "sqlTuningTarget/autoSelectionStatus";
	}
	
	/* 자동선정  - list action */
	@RequestMapping(value="/AutoSelectionStatusAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AutoSelectionStatusListAction(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice,  Model model) {
		List<TopsqlAutoChoice> resultList = new ArrayList<TopsqlAutoChoice>();

		try{
			resultList = sqlTuningTargetService.autoSelectionStatusList(topsqlAutoChoice);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 자동선정  - detail list action */
	@RequestMapping(value="/AutoSelectionStatus/Detail", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AutoSelectionStatusDetailListAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();

		try{
			resultList = sqlTuningTargetService.autoSelectionStatusDetailList(tuningTargetSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 수동선정  */
//	@RequestMapping(value="/ManualSelection", method=RequestMethod.GET)
	@RequestMapping(value="/ManualSelection", produces="application/text; charset=utf8")
	public String ManualSelection(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model) {
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
		
		return "sqlTuningTarget/manualSelection";
	}
	
	/* 수동선정 Action  */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/ManualSelectionAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ManualSelectionAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		int dataCount4NextBtn = 0;
		try{
			if(odsHistSqlstat.getFilter_sql() != null && odsHistSqlstat.getFilter_sql().indexOf("/") != -1){
				odsHistSqlstat.setFilter_sql(odsHistSqlstat.getFilter_sql().replace("/", "%"));
			}
			resultList = sqlTuningTargetService.manualSelectionList(odsHistSqlstat);
			
			logger.debug("ManualSelection size:" + resultList.size());
			
			if(resultList != null && resultList.size() > odsHistSqlstat.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(odsHistSqlstat.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
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
	
	/* 수동선정현황  */
	@RequestMapping(value="/ManualSelectionStatus")
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

		return "sqlTuningTarget/manualSelectionStatus";
	}
	
	/* 수동선정현황  - list action */
	@RequestMapping(value="/ManualSelectionStatusAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ManualSelectionStatusListAction(@ModelAttribute("topsqlHandopChoice") TopsqlHandopChoice topsqlHandopChoice,  Model model) {
		List<TopsqlHandopChoice> resultList = new ArrayList<TopsqlHandopChoice>();

		try{
			resultList = sqlTuningTargetService.manualSelectionStatusList(topsqlHandopChoice);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 수동선정현황  - detail list action */
	@RequestMapping(value="/ManualSelectionStatus/Detail", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ManualSelectionStatusDetailListAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();

		try{
			resultList = sqlTuningTargetService.manualSelectionStatusDetailList(tuningTargetSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 수동선정 - 이력 팝업*/
	@RequestMapping(value="/ManualSelection/Popup/History", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ManualSelectionHistory(@ModelAttribute("odsHistSnapshot") OdsHistSnapshot odsHistSnapshot,  Model model) {
		List<OdsHistSnapshot> resultList = new ArrayList<OdsHistSnapshot>();

		try{
			resultList = sqlTuningTargetService.manualSelectionHistoryList(odsHistSnapshot);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 튜닝담당자지정 팝업  */
	@RequestMapping(value="/SQLTuningTarget/Popup/InsertTuningRequest", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertTuningRequest(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		Result result = new Result();
		try{
			logger.debug("##### 튜닝대상선정 진행 #####");
			result = sqlTuningTargetService.insertTuningRequest(tuningTargetSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 수동선정 튜닝대상 저장  */
	@RequestMapping(value="/ManualSelection/InsertTopsqlHandopChoiceExec", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertTopsqlHandopChoiceExec(@ModelAttribute("topsqlHandopChoice") TopsqlHandopChoice topsqlHandopChoice,  Model model) {
		String isOk = "N";
		Result result = new Result();
		try{
			
			isOk = sqlTuningTargetService.insertTopsqlHandopChoiceExec(topsqlHandopChoice);
			result.setResult(isOk.equals("Y") ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 일괄튜닝종료 팝업  */
	@RequestMapping(value="/SQLTuningTarget/Popup/EndBatchTuning", method=RequestMethod.POST)
	@ResponseBody
	public Result EndBatchTuning(@ModelAttribute("sqlTuning") SqlTuning sqlTuning,  Model model) {
		String isOk = "N";
		Result result = new Result();
		try{
			isOk = sqlTuningTargetService.endBathTuning(sqlTuning);
			result.setResult(isOk.equals("Y") ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 일괄튜닝종료 팝업 (자동 선정 현황 - 검색) */
	@RequestMapping(value="/SQLTuningTarget/Popup/EndBatchTuningBundle", method=RequestMethod.POST)
	@ResponseBody
	public Result EndBatchTuningBundle(@ModelAttribute("sqlTuning") SqlTuning sqlTuning,  Model model) {
		String isOk = "N";
		Result result = new Result();
		try{
			isOk = sqlTuningTargetService.endBatchTuningBundle(sqlTuning);
			result.setResult(isOk.equals("Y") ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/**
	 * 자동선정 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/AutoSelection/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView AutoSelectionExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = sqlTuningTargetService.autoSelectionList4Excel(topsqlAutoChoice);
			//resultList = improvementManagementService.improvementStatusList4Excel(tuningTargetSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "자동선정");
		model.addAttribute("sheetName", "자동선정");
		model.addAttribute("excelId", "AUTO_SELECTION");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	/**
	 * 자동선정현황 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/AutoSelectionStatus/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView AutoSelectionMExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlTuningTargetService.autoSelectionStatusList4Excel(topsqlAutoChoice);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "자동_선정_현황(선정회차)");
		model.addAttribute("sheetName", "자동_선정_현황(선정회차)");
		model.addAttribute("excelId", "AUTO_SELECTION_STATUS");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	/**
	 * 자동선정현황(검색) 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/AutoSelectionStatusSearch/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView AutoSelectionSearchMExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlTuningTargetService.autoSelectionStatusSearchList4Excel(topsqlAutoChoice);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "자동_선정_현황(검색)");
		model.addAttribute("sheetName", "자동_선정_현황(검색)");
		model.addAttribute("excelId", "AUTO_SELECTION_STATUS");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	/**
	 * 자동선정현황 상세 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/AutoSelectionStatusDetail/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView AutoSelectionSExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			tuningTargetSql.setStrGubun("EXCEL");
			resultList = sqlTuningTargetService.autoSelectionStatusDetailList4Excel(tuningTargetSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "자동선정현황_상세(선정회차)");
		model.addAttribute("sheetName", "자동선정현황_상세(선정회차)");
		model.addAttribute("excelId", "AUTO_SELECTION_STATUS_DETAIL");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/**
	 * 자동선정현황 상세(검색) 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/AutoSelectionStatusSearchDetail/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView AutoSelectionSSearchExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			tuningTargetSql.setStrGubun("EXCEL");
			resultList = sqlTuningTargetService.autoSelectionStatusDetailList4Excel(tuningTargetSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "자동선정현황_상세(검색)");
		model.addAttribute("sheetName", "자동선정현황_상세(검색)");
		model.addAttribute("excelId", "AUTO_SELECTION_STATUS_DETAIL");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/**
	 * 수동선정 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ManualSelectionAction/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView ManualSelectionActionExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			if(odsHistSqlstat.getFilter_sql() != null && odsHistSqlstat.getFilter_sql().indexOf("/") != -1){
				odsHistSqlstat.setFilter_sql(odsHistSqlstat.getFilter_sql().replace("/", "%"));
			}
			
			resultList = sqlTuningTargetService.manualSelectionList4Excel(odsHistSqlstat);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "수동선정");
		model.addAttribute("sheetName", "수동선정");
		model.addAttribute("excelId", "MANUAL_SELECTION_STATUS");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 수동선정현황  - list action - 엑셀다운 */
	@RequestMapping(value = "/ManualSelectionStatusAction/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ManualSelectionStatusListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("topsqlHandopChoice") TopsqlHandopChoice topsqlHandopChoice, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = sqlTuningTargetService.manualSelectionStatusListByExcelDown(topsqlHandopChoice);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "수동_선정_현황");
		model.addAttribute("sheetName", "수동_선정_현황");
		model.addAttribute("excelId", "MANUAL_SELECTION_STATUS2");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
}