package omc.spop.controller;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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
import omc.spop.base.Config;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.AccPathExec;
import omc.spop.model.ApmApplSql;
import omc.spop.model.DbIndexHistory;
import omc.spop.model.DbTabColumnHistory;
import omc.spop.model.DbaObjects;
import omc.spop.model.DbaTables;
import omc.spop.model.DbaUsers;
import omc.spop.model.IdxAdMst;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.SelftunExecutionPlan1;
import omc.spop.model.SelftunExecutionPlan;
import omc.spop.model.SelftunPlanTable;
import omc.spop.model.SelftunSql;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.service.ImprovementManagementService;
import omc.spop.service.SelfTuningService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelWrite;
import omc.spop.utils.TreeWrite;
import omc.spop.utils.WriteOption;

/***********************************************************
 * 2018.03.23 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class SelfTuningController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(SelfTuningController.class);

	@Autowired
	private SelfTuningService selfTuningService;

	@Autowired
	private ImprovementManagementService improvementManagementService;

	/* 셀프튜닝 */
	@RequestMapping(value = "/SelfTuning")
	public String SelfTuning(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", apmApplSql.getMenu_id());
		model.addAttribute("menu_nm", apmApplSql.getMenu_nm());
		model.addAttribute("dbio_search_yn", Config.getString("dbio_search_yn")); // APM연동 체크하여 기능 사용여부 체크
																					// (사용[dbio_search_yn =
																					// 'Y']/미사용[dbio_search_yn = 'N'])
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		model.addAttribute("auth_cd", auth_cd);
		model.addAttribute("apmApplSql",apmApplSql);

//		return "selfTuning";
		return "selfTuningNew";
	}
	
	/* 데이터베이스 조회 */
	@RequestMapping(value = "/SelfTuning/getDatabaseOfWrkjobCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDatabase(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql) throws Exception {
		List<ApmApplSql> databaseList = selfTuningService.databaseListOfWrkjobCd(apmApplSql);
		
		return success(databaseList).toJSONObject().get("rows").toString();
	}
	
	/* 셀프튜닝 list action */
	@RequestMapping(value = "/SelfTuningAction", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SelfTuningAction(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		List<ApmApplSql> resultList = new ArrayList<ApmApplSql>();

		try {
			resultList = selfTuningService.selfTuningList(apmApplSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 튜닝요청 action sql_text를 세션에 담는 역할 */
	@RequestMapping(value = "/RequestTuningAction", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String RequestTuningAction(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model,
			HttpServletRequest req) {
		List<ApmApplSql> resultList = new ArrayList<ApmApplSql>();
		try {
			String sql_text = apmApplSql.getSql_text();
			logger.debug("1.sql_text :::"+sql_text);
			HttpSession session = req.getSession(true);
			session.setAttribute("sql_text", sql_text);
			logger.debug("2.sql_text :::"+session.getAttribute("sql_text"));

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 튜닝요청 INSERT */
	@RequestMapping(value = "/SelfTuning/RequestTuning", method = { RequestMethod.GET, RequestMethod.POST })
	public String SelfTuningRequest(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		logger.debug("nowDate :"+nowDate);
		List<TuningTargetSqlBind> sqlBindList = new ArrayList<TuningTargetSqlBind>();
		TuningTargetSql initValues = new TuningTargetSql();

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		String wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
		String wrkjob_nm = SessionManager.getLoginSession().getUsers().getWrkjob_nm();
		String ext_no = SessionManager.getLoginSession().getUsers().getExt_no();

		String[] bindSetSeqArr = req.getParameterValues("bind_set_seq");
		String[] bindSeqArr = req.getParameterValues("bind_seq");
		String[] bindVarNmArr = req.getParameterValues("bind_var_nm");
		String[] bindVarValueArr = req.getParameterValues("bind_var_value");
		String[] bindVarTypeArr = req.getParameterValues("bind_var_type");
		String[] mandatoryYnArr = req.getParameterValues("mandatory_yn");

		// 세션에 담긴 sql_text를 꺼내서 tuningTargetSql class에 세팅한다.
//		HttpSession session = req.getSession(true);
//		String sql_text = (String) session.getAttribute("sql_text");
//		logger.debug("3.sql_text :"+sql_text);
//		tuningTargetSql.setSql_text(sql_text);
		
		String sql_text = tuningTargetSql.getSql_text();
		logger.debug("sql_text===>"+sql_text);

		if (bindSetSeqArr != null) {
			for (int i = 0; i < bindSetSeqArr.length; i++) {
				TuningTargetSqlBind result = new TuningTargetSqlBind();

				result.setBind_set_seq(bindSetSeqArr[i]);
				result.setBind_seq(bindSeqArr[i]);
				result.setBind_var_nm(bindVarNmArr[i]);
				result.setBind_var_value(bindVarValueArr[i]);
				result.setBind_var_type(bindVarTypeArr[i]);
				result.setMandatory_yn(mandatoryYnArr[i]);

				sqlBindList.add(result);
			}
		}

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("wrkjob_cd", wrkjob_cd);
		model.addAttribute("wrkjob_nm", wrkjob_nm);
		model.addAttribute("ext_no", ext_no);
		model.addAttribute("sqlBindList", sqlBindList);
		model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
		model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());

		// kb카드 요구사항
		// 튜닝완료요청일자 오늘 + 7
		String tuning_complete_due_day = StringUtils
				.defaultString(improvementManagementService.getTuningCompleteDueDay(), "0");
		logger.debug("tuning_complete_due_day :" + tuning_complete_due_day);
		int i_tuning_complete_due_day = Integer.parseInt(tuning_complete_due_day);
		String tuning_complete_due_dt = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate,
				i_tuning_complete_due_day);
		tuningTargetSql.setTuning_complete_due_dt(tuning_complete_due_dt);

		model.addAttribute("user_id", user_id);
		// 튜닝요청 초기화값을 세팅해줌
		initValues = improvementManagementService.getInitValues(user_id);
		// 값이있다면 튜닝요청 초기화값
		if (initValues != null) {
			model.addAttribute("initValues", initValues);
		}

		return "selfTuningRequest";
	}

	/* 셀프튜닝 - 인덱스자동설계 action */
	@RequestMapping(value = "/SelfTuning/StartSelfIndexAutoDesign", method = RequestMethod.POST)
	@ResponseBody
	public Result StartSelfIndexAutoDesignAction(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		Result result = new Result();
		List<IdxAdMst> resultList = new ArrayList<IdxAdMst>();
		String selftunQuerySeq = "";
		try {
			// 1. SELFTUN_SQL INSERT
			selftunQuerySeq = selfTuningService.insertSelftunQuery(apmApplSql);

			resultList = selfTuningService.startSelfIndexAutoDesign(apmApplSql, selftunQuerySeq);
			result.setResult(resultList.size() > 0 ? true : false);
//			result.setMessage("인덱스 자동설계 내역이 존재하지 않습니다.");
			result.setObject(resultList);
			result.setTxtValue(success(resultList).toJSONObject().toString());
		} catch (Exception ex) {
			result.setMessage(ex.getMessage());
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	/* 셀프튜닝 - 인덱스자동설계 엑셀 다운로드 action */
	@RequestMapping(value = "/SelfTuning/SelfIndexAutoDesign/ExcelDown", method = RequestMethod.POST)
	public ModelAndView SelfIndexAutoDesignExcelDownAction(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("idxAdMst") IdxAdMst idxAdMst, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = selfTuningService.excelDownloadIdxAdMstList(idxAdMst);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQL_셀프_성능_점검_인덱스_자동설계");
		model.addAttribute("sheetName", "SQL_셀프_성능_점검_인덱스_자동설계");
		model.addAttribute("excelId", "SQL_SELF_TUNING_INDEX_AUTO_DESIGN");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/* 셀프튜닝 - 셀프테스트 action */
	@RequestMapping(value = "/SelfTuning/SelfTest", method = RequestMethod.POST)
	@ResponseBody
	public Result SelfTestAction(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		logger.debug("wrkjob_cd :" + apmApplSql.getWrkjob_cd());
		Result result = new Result();
		String selftunQuerySeq = "";
		try {
			// 1. SELFTUN_SQL INSERT
			selftunQuerySeq = selfTuningService.insertSelftunQuery(apmApplSql);
			logger.debug("selftunQuerySeq :" + selftunQuerySeq);

			// 2. 서버모듈 - 셀프테스트 이력 조회
			result = selfTuningService.startSelfTest(req, apmApplSql, selftunQuerySeq);
		} catch (SQLException se) {
			logger.error("SQL error ==> " + se.getMessage());
			result.setResult(false);
			result.setMessage(se.getMessage());
		} catch (Exception e) {
			logger.error("error ==> " + e.getMessage());
			result.setResult(false);
			result.setMessage(e.getMessage());
		}

		return result;
	}

	/* 셀프튜닝 - 성능점검수행 action */
	@RequestMapping(value = "/SelfTuning/SelfTestNew", method = RequestMethod.POST)
	@ResponseBody
	public Result SelfTestNewAction(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		logger.debug("wrkjob_cd :" + apmApplSql.getWrkjob_cd());
		Result result = new Result();
		String selftunQuerySeq = "";
		try {
			// 1. SELFTUN_SQL INSERT
			selftunQuerySeq = selfTuningService.insertSelftunQuery(apmApplSql);
			logger.debug("selftunQuerySeq :" + selftunQuerySeq);

			// 2. 서버모듈 - 셀프테스트 이력 조회
			result = selfTuningService.startSelfTestNew(req, apmApplSql, selftunQuerySeq);
			result.setResult(true);
			result.setMessage("성능점검수행을 완료하였습니다.");
		} catch (SQLException se) {
			logger.error("SQL error ==> " + se.getMessage());
			result.setResult(false);
			result.setMessage(se.getMessage());
		} catch (Exception e) {
			logger.error("error ==> " + e.getMessage());
			result.setResult(false);
			result.setMessage(e.getMessage());
		}

		return result;
	}

	/* 셀프튜닝 - Explain Plan Tree */
	@RequestMapping(value = "/SelfTuning/ExplainPlanTree", method = RequestMethod.POST)
	@ResponseBody
	public Result ExplainPlanTreeAction(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		List<SelftunPlanTable> resultList = new ArrayList<SelftunPlanTable>();
		Result result = new Result();
		String returnValue = "";
		try {
			resultList = selfTuningService.explainPlanTreeList(apmApplSql);
			List<SelftunPlanTable> buildList = TreeWrite.buildExplainPlanTree(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
			result.setResult(true);
			result.setTxtValue(returnValue);
		} catch (SQLException se) {
			logger.error("SQL error ==> " + se.getMessage());
			result.setResult(false);
			result.setMessage(se.getMessage());
		} catch (Exception e) {
			logger.error("error ==> " + e.getMessage());
			result.setResult(false);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/* 셀프튜닝 - Parsing_schema_name 조회 */
	@RequestMapping(value = "/SelfTuning/getParsingSchemaName", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getParsingSchemaNameAction(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		List<DbaUsers> resultList = new ArrayList<DbaUsers>();
		try {
			resultList = selfTuningService.parsingSchemaNameList(apmApplSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}

	/* 셀프튜닝 - Explain Plan Detail Popup 조회 */
	@RequestMapping(value = "/SelfTuning/Popup/ExplanPlanDetail", method = RequestMethod.POST)
	@ResponseBody
	public Result ExplanPlanDetailPopupAction(@ModelAttribute("dbaTables") DbaTables dbaTables, Model model) {
		Result result = new Result();
		try {
			result = selfTuningService.explainPlanDetailPopup(dbaTables);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* 셀프튜닝 - Explain Plan Detail Popup [ access_path / change history ] 조회 */
	@RequestMapping(value = "/SelfTuning/Popup/ExplanPlanDetail2", method = RequestMethod.POST)
	@ResponseBody
	public Result ExplanPlanDetailPopup2Action(@ModelAttribute("dbaTables") DbaTables dbaTables, Model model) {
		Result result = new Result();
		List<String> stringResultList = new ArrayList<String>();
		List<AccPathExec> accpathList = new ArrayList<AccPathExec>();
		List<DbTabColumnHistory> columnHistoryList = new ArrayList<DbTabColumnHistory>();
		List<DbIndexHistory> indexHistoryList = new ArrayList<DbIndexHistory>();

		try {
			accpathList = selfTuningService.accpathList(dbaTables);
			columnHistoryList = selfTuningService.columnHistoryList(dbaTables);
			indexHistoryList = selfTuningService.indexHistoryList(dbaTables);

			result.setResult(true);
			stringResultList.add(0, success(accpathList).toJSONObject().toString());
			stringResultList.add(1, success(columnHistoryList).toJSONObject().toString());
			stringResultList.add(2, success(indexHistoryList).toJSONObject().toString());

			result.setStringList(stringResultList);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* 셀프튜닝 - Explain Plan Statistics Popup 조회 */
	@RequestMapping(value = "/SelfTuning/Popup/ExplanPlanStatistics", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ExplanPlanStatisticsPopupAction(@ModelAttribute("dbaObjects") DbaObjects dbaObjects, Model model) {
		List<DbaObjects> resultList = new ArrayList<DbaObjects>();

		try {
			resultList = selfTuningService.explainPlanStatisticsPopup(dbaObjects);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 셀프튜닝 - 수행 Sql Info Popup 조회 */
	@RequestMapping(value = "/SelfTuning/Popup/SelfTuningSqlInfo", method = RequestMethod.POST)
	@ResponseBody
	public Result SelfTuningSqlInfoPopupAction(@ModelAttribute("selftunSql") SelftunSql selftunSql, Model model) {
		Result result = new Result();
		try {
			result = selfTuningService.selfTuningSqlInfoPopup(selftunSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* 셀프튜닝 >성능점검결과 grid1 */
	@RequestMapping(value = "/SelfTuning/PerfCheckResultList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String PerfCheckResultList(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql, Model model) {
		List<ApmApplSql> resultList = new ArrayList<ApmApplSql>();

		try {
			resultList = selfTuningService.selectPerfCheckResultList(apmApplSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 셀프튜닝 >성능점검결과 grid2 */
	@RequestMapping(value = "/SelfTuning/selectDeployPerfChkDetailResultList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectDeployPerfChkDetailResultList(
			@ModelAttribute("selftunExecutionPlan") SelftunExecutionPlan selftunExecutionPlan, Model model) {
		logger.debug("/PerfCheckResultList/selectDeployPerfChkDetailResultList started");
		logger.debug("selftunExecutionPlan:" + selftunExecutionPlan);
		String dbid = StringUtils.defaultString(selftunExecutionPlan.getDbid());
		logger.debug("dbid:" + dbid);
		String selftun_query_seq = StringUtils.defaultString(selftunExecutionPlan.getSelftun_query_seq());
		logger.debug("selftun_query_seq:" + selftun_query_seq);
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		if (dbid.equals("") || selftun_query_seq.equals("")) {
			return success(resultList).toJSONObject().toString();
		}
		try {
			resultList = selfTuningService.selectDeployPerfChkDetailResultList(selftunExecutionPlan);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* 셀프튜닝 >성능점검결과 grid3 */
	@RequestMapping(value = "/SelfTuning/selectImprovementGuideList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectImprovementGuideList(
			@ModelAttribute("selftunExecutionPlan") SelftunExecutionPlan selftunExecutionPlan, Model model) {
		logger.debug("/PerfCheckResultList/selectImprovementGuideList started");
		logger.debug("selftunExecutionPlan:" + selftunExecutionPlan);
		String dbid = StringUtils.defaultString(selftunExecutionPlan.getDbid());
		logger.debug("dbid:" + dbid);
		String selftun_query_seq = StringUtils.defaultString(selftunExecutionPlan.getSelftun_query_seq());
		logger.debug("selftun_query_seq:" + selftun_query_seq);
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		if (dbid.equals("") || selftun_query_seq.equals("")) {
			return success(resultList).toJSONObject().toString();
		}
		try {
			resultList = selfTuningService.selectImprovementGuideList(selftunExecutionPlan);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* 셀프튜닝 >성능점검결과 grid4 */
	@RequestMapping(value = "/SelfTuning/selectPerfCheckResultBasisWhy", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectPerfCheckResultBasisWhy(
			@ModelAttribute("selftunExecutionPlan") SelftunExecutionPlan selftunExecutionPlan, Model model) {
		logger.debug("/PerfCheckResultList/selectPerfCheckResultBasisWhy started");
		logger.debug("selftunExecutionPlan:" + selftunExecutionPlan);
		String dbid = StringUtils.defaultString(selftunExecutionPlan.getDbid());
		logger.debug("dbid:" + dbid);
		String selftun_query_seq = StringUtils.defaultString(selftunExecutionPlan.getSelftun_query_seq());
		logger.debug("selftun_query_seq:" + selftun_query_seq);
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		if (dbid.equals("") || selftun_query_seq.equals("")) {
			return success(resultList).toJSONObject().toString();
		}
		try {
			resultList = selfTuningService.selectPerfCheckResultBasisWhy(selftunExecutionPlan);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/SelfTuning/executionPlan1", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String executionPlan1(@ModelAttribute("selftunExecutionPlan1") SelftunExecutionPlan1 selftunExecutionPlan1, Model model) {
		List<SelftunExecutionPlan1> resultList = new ArrayList<SelftunExecutionPlan1>();
		try {
			resultList = selfTuningService.executionPlan1(selftunExecutionPlan1);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

}