package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.Sqls;
import omc.spop.model.server.Iqms;
import omc.spop.server.tune.DeployPerfChk;
import omc.spop.service.PerfInspectMngService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2021.07.28	황예지	최초작성
 **********************************************************/

@RequestMapping("/perfInspectMng")
@Controller
public class PerfInspectMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(PerfInspectMngController.class);

	@Autowired
	private PerfInspectMngService perfInspectMngSvc;
	
	@Value("#{defaultConfig['iqmsip']}")
	private String iqmsip;

	@Value("#{defaultConfig['iqmsport']}")
	private String iqmsport;

	@Value("#{defaultConfig['iqms_server_uri']}")
	private String IQMS_SERVER_URI;

	@Value("#{defaultConfig['customer']}")
	private String CUSTOMER;
	
	// 최종검증단계 조회
	@RequestMapping(value = "/getPerfCheckStep", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckStep(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng)
			throws Exception {
		List<PerformanceCheckMng> list = new ArrayList<PerformanceCheckMng>();
		String returnStr = "";
		PerformanceCheckMng temp = new PerformanceCheckMng();
		List<PerformanceCheckMng> perfCheckStepList = Collections.emptyList();

		try {
			temp.setPerf_check_step_id("");
			temp.setPerf_check_step_nm("전체");
			list.add(temp);
			
			perfCheckStepList = perfInspectMngSvc.getPerfCheckStep(performanceCheckMng);
			list.addAll(perfCheckStepList);
			returnStr = success(list).toJSONObject().get("rows").toString();
			
		} catch (Exception ex) {
			logger.error("Common Error ==> " + ex.getMessage());
			ex.printStackTrace();
			return getErrorJsonString(ex);
			
		}finally {
			list = null;
			temp = null;
			perfCheckStepList = null;
		}
		
		return returnStr;
	}
	
	// 상단 그리드 조회
	@RequestMapping(value = "/loadInspectionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getInspectionList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {

		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		performanceCheckMng.setAuth_cd(auth_cd);
		performanceCheckMng.setUser_id(user_id);
		
		List<PerformanceCheckMng> resultList = Collections.emptyList();
		int dataCount4NextBtn = 0;
		JSONObject jObj = null;
		
		try {
			resultList = perfInspectMngSvc.getInspectionList(performanceCheckMng);
			
			if (resultList != null && resultList.size() > performanceCheckMng.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(performanceCheckMng.getPagePerCount());
			}
			jObj = success( resultList ).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			return getErrorJsonString( ex );
			
		}finally {
			resultList = null;
		}
		
		jObj.put("dataCount4NextBtn", dataCount4NextBtn);
		
		return jObj.toString();
	}
	
	// 상단 그리드 엑셀 다운로드
	@RequestMapping(value = "/loadInspectionList/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView loadInspectionListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		
		try {
			resultList = perfInspectMngSvc.getInspectionListExcelDown(performanceCheckMng);
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("fileName", "성능_검증_관리");
		model.addAttribute("sheetName", "성능_검증_관리");
		model.addAttribute("excelId", "PERF_INSPECT_MNG");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	//하단 그리드 조회
	@RequestMapping(value = "/loadInspectionStep", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getInspectionStep(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		
		List<PerformanceCheckMng> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = perfInspectMngSvc.getInspectionStep(performanceCheckMng);
			jobj = success(resultList).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	//하단 그리드 엑셀 다운로드
	@RequestMapping(value = "/loadInspectionStep/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView loadInspectionStepExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		
		try {
			resultList = perfInspectMngSvc.getInspectionStepExcelDown(performanceCheckMng);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("fileName", "성능_검증_단계");
		model.addAttribute("sheetName", "성능_검증_단계");
		model.addAttribute("excelId", "PERF_INSPECT_STEP");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	// 성능검증 재수행
	@ResponseBody
	@RequestMapping(value="/reExecution", method = RequestMethod.POST )
	public Result reExecution(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) throws Exception {
		
		Result result = new Result();
		
		try {
			boolean isNormal = perfInspectMngSvc.reExecute(performanceCheckMng);
			System.out.println("결과는요 "+isNormal);
			result.setResult( isNormal );
			
		}catch(Exception e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e.getMessage());
			e.printStackTrace();
			
			result.setResult(false);
		}
		return result;
	}
	
	// 성능 검증 결과 탭 이동
	@RequestMapping(value = "/inspectionResult")
	public String inspectionResult(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) throws Exception {
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -6);
		String deployId = StringUtils.defaultString(performanceCheckMng.getDeploy_id());
		String perf_check_id = StringUtils.defaultString(performanceCheckMng.getPerf_check_id());
		
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("startDate", startDate);
		model.addAttribute("menu_id", performanceCheckMng.getMenu_id());
		model.addAttribute("menu_nm", performanceCheckMng.getMenu_nm());
		
		model.addAttribute("auth_cd", SessionManager.getLoginSession().getUsers().getAuth_cd());
		model.addAttribute("user_id", SessionManager.getLoginSession().getUsers().getUser_id());
		model.addAttribute("call_from_parent", performanceCheckMng.getCall_from_parent());
		model.addAttribute("program_id", StringUtils.defaultString(performanceCheckMng.getProgram_id()));
		model.addAttribute("refresh", StringUtils.defaultString(performanceCheckMng.getRefresh()));
		
		PerformanceCheckMng perfCheckIdEtc = null;
		PerformanceCheckMng perfCheckResultBasicInfo = null;
		
		try {
			if (!deployId.equals("") && perf_check_id.equals("")) {
				perfCheckIdEtc = perfInspectMngSvc.getPerfCheckIdFromDeployId(performanceCheckMng);
				
				if (perfCheckIdEtc != null) {
					perf_check_id = StringUtils.defaultString(perfCheckIdEtc.getPerf_check_id());
					String perf_check_step_id = StringUtils.defaultString(perfCheckIdEtc.getPerf_check_step_id());
					String wrkjob_cd = StringUtils.defaultString(perfCheckIdEtc.getWrkjob_cd());
					String top_wrkjob_cd = StringUtils.defaultString(perfCheckIdEtc.getTop_wrkjob_cd());
					
					performanceCheckMng.setPerf_check_id(perf_check_id);
					performanceCheckMng.setPerf_check_step_id(perf_check_step_id);
					performanceCheckMng.setWrkjob_cd(wrkjob_cd);
					performanceCheckMng.setTop_wrkjob_cd(top_wrkjob_cd);
					
					model.addAttribute("perf_check_id", perf_check_id);
					model.addAttribute("perf_check_step_id", perf_check_step_id);
					model.addAttribute("wrkjob_cd", wrkjob_cd);
					model.addAttribute("top_wrkjob_cd", top_wrkjob_cd);
					
					perfCheckResultBasicInfo = perfInspectMngSvc.getPerfCheckResultBasicInfo(performanceCheckMng);
					
				} else {
					performanceCheckMng.setError_message("배포ID가 존재하지 않습니다.</br>관리자에게 문의 바랍니다.");
					model.addAttribute("error_message", performanceCheckMng.getError_message());
					
					perfCheckResultBasicInfo = perfInspectMngSvc.getPerfCheckResultBasicInfo(performanceCheckMng);
					
					return "performanceCheckMng/performanceCheckResult";
				}
				
			} else {
				perfCheckResultBasicInfo = perfInspectMngSvc.getPerfCheckResultBasicInfo(performanceCheckMng);
			}
			model.addAttribute("perfCheckResultBasicInfo", perfCheckResultBasicInfo);
			
			PerformanceCheckMng deployCheckStatus = perfInspectMngSvc.selectDeployCheckStatus(performanceCheckMng);
			model.addAttribute("deployCheckStatus", deployCheckStatus);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
		} finally {
			nowDate = null;
			startDate = null;
			deployId = null;
			perf_check_id = null;
			perfCheckIdEtc = null;
			perfCheckResultBasicInfo = null;
		}
		
		return "execSqlPerfCheck/inspectionResult";
	}
	
	// 성능 검증 결과 > 프로그램 정보
	@RequestMapping(value = "/programInfoTab", method = RequestMethod.POST)
	public ModelAndView programInfoTab(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng) throws Exception {
		
		Map<String, Object> model = new HashMap<String, Object>();
		PerformanceCheckMng perfCheckAllPgm = null;
		String program_source_desc2 = null;
		
		try {
			perfCheckAllPgm = perfInspectMngSvc.getPerfCheckAllPgm(performanceCheckMng);
			
			program_source_desc2 = perfCheckAllPgm.getProgram_source_desc();
			program_source_desc2 = program_source_desc2.replace("${", ":").replace("#{", ":").replace("}", "");
			perfCheckAllPgm.setProgram_source_desc2(program_source_desc2);
			
			model.put("perfCheckAllPgm", perfCheckAllPgm);
			
		}catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
		}finally {
			perfCheckAllPgm = null;
			program_source_desc2 = null;
		}
		
		return new ModelAndView("execSqlPerfCheck/programInfoTab", model);
	}
	
	@RequestMapping(value = "/programInfoBodySelect", method = RequestMethod.POST)
	@ResponseBody
	public Result createProgramInfoBodySelect(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, HttpServletRequest request,
			Model model) {
		Result result = new Result();
		String html = performanceCheckMng.getHtml();
		Document doc = null;
		
		try {
			doc = Jsoup.parseBodyFragment(html);	// jsoup 1.10.3
			
			result.setResult(true);
			result.setMessage("HTML BODY 추출완료");
			result.setTxtValue(doc.html());				// jsoup 1.10.3
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setStatus("exception");
			result.setMessage("프로그램 정보 조회중 오류가 발생하였습니다.");
			
		}finally {
			html = null;
			doc = null;
		}
		return result;
	}
	
	@RequestMapping(value = "/selectDeployPerfChkDetailResult", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectDeployPerfChkDetailResult(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		String programExecuteTms = StringUtils.defaultString(performanceCheckMng.getHidden_program_execute_tms());
		String returnStr = "";
		
		if (!programExecuteTms.equals("")) {
			performanceCheckMng.setProgram_execute_tms(programExecuteTms);
		}
		
		List<PerformanceCheckMng> resultList = Collections.emptyList();
		
		try {
			resultList = perfInspectMngSvc.selectDeployPerfChkDetailResultList(performanceCheckMng);
			returnStr = success(resultList).toJSONObject().toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		return returnStr;
	}
	
	// 성능 검증 결과 > 개선가이드, 검증 SQL 목록 팝업 > 개선가이드
	@RequestMapping(value = "/selectImprovementGuide", produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectImprovementGuideList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng) throws Exception {
		
		List<PerformanceCheckMng> improvementGuide = Collections.emptyList();
		String returnStr = "";
		
		try {
			improvementGuide = perfInspectMngSvc.selectImprovementGuideList(performanceCheckMng);
			
			if (improvementGuide != null) {
				returnStr = success(improvementGuide).toJSONObject().toString();
			}
			
		} catch (Exception e) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + e.getMessage());
			e.printStackTrace();
			
		}finally {
			improvementGuide = null;
		}
		
		return returnStr;
	}
	
	@RequestMapping(value = "/getPerfCheckResultBasicInfo", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckResultBasicInfo(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		PerformanceCheckMng returnPerfChckMng = null;
		String returnStr = "";
		
		try {
			returnPerfChckMng = perfInspectMngSvc.getPerfCheckResultBasicInfo(performanceCheckMng);
			returnStr = success(returnPerfChckMng).toJSONObject().toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
			
		}finally {
			returnPerfChckMng = null;
		}
		
		return returnStr;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/perfCheckResultList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfCheckResultList(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) {
		List<PerformanceCheckMng> resultList = Collections.emptyList();
		int dataCount4NextBtn = 0;
		JSONObject jobj = new JSONObject();
		
		try {
			resultList = perfInspectMngSvc.getPerfCheckResultList(performanceCheckMng);
			
			if (resultList != null && resultList.size() > performanceCheckMng.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(performanceCheckMng.getPagePerCount());
			}
			jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	@RequestMapping(value = "/perfInspectResult/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView PerfCheckResultListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model)
			throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		
		try {
			resultList = perfInspectMngSvc.getPerfCheckResultList4Excel(performanceCheckMng);
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_검증_결과");
		model.addAttribute("sheetName", "성능_검증_결과");
		model.addAttribute("excelId", "PERF_INSPECT_RESULT");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 튜닝요청 action */
	@RequestMapping(value = "/perfChkRequestTuning", method = RequestMethod.POST)
	@ResponseBody
	public Result PerfChkRequestTuning(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model, HttpServletRequest req) {
		Result result = new Result();
		int resultList = 0;
		int resultCount = 0;
		
		try {
			resultCount = perfInspectMngSvc.perfChkRequestTuningDupChkEspc(performanceCheckMng);
			if(resultCount > 0) {
				result.setResult(false);
				result.setResultCount(resultCount);
				result.setMessage("이미 튜닝 요청된 SQL ID 입니다.");
				return result;
			}
			
			resultList = perfInspectMngSvc.perfChkRequestTuning(performanceCheckMng, req);
			if (resultList > 0) {
				result.setResult(true);
				result.setMessage("튜닝 요청을 완료하였습니다.");
				
			} else {
				result.setResult(false);
				result.setMessage("튜닝 요청 중 오류가 발생하였습니다.");
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage("튜닝 요청 중 오류가 발생하였습니다.");
		}
		return result;
	}
	
	// 검증 SQL 목록 팝업 - 좌측 그리드
	@RequestMapping(value = "/getInspectSqlList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getInspectSqlList(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		
		List<PerformanceCheckMng> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = perfInspectMngSvc.getInspectSqlList(performanceCheckMng);
			jobj = success(resultList).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}
	
	// 검증 SQL 목록 팝업 - 상세 검증 결과
	@RequestMapping(value = "/getInspectResultDetail", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getInspectResultDetail(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		
		List<PerformanceCheckMng> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = perfInspectMngSvc.getInspectResultDetail(performanceCheckMng);
			returnStr = success(resultList).toJSONObject().toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	// 검증 SQL 목록 팝업 - Execution plan
	@RequestMapping(value = "/getExecutionPlan", method = RequestMethod.POST)
	@ResponseBody
	public String getExecutionPlan(@ModelAttribute("sqls") Sqls sqls, Model model) throws Exception {
		
		List<Sqls> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = perfInspectMngSvc.getExecutionPlan(sqls);
			returnStr = success(resultList).toJSONObject().toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	// 검증 SQL 목록 팝업 - bind value
	@RequestMapping(value = "/getBindValue", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectDeployPerfChkExecBindValue(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		
		List<PerformanceCheckMng> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = perfInspectMngSvc.selectDeployPerfChkExecBindValue(performanceCheckMng);
			returnStr = success(resultList).toJSONObject().toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	// 검증 SQL 목록 팝업 - SQL Text
	@RequestMapping(value = "/getSqlInfo", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView getSqlInfo(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) throws Exception {
		
		PerformanceCheckMng sqlTextInfo = null;
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");
		
		try {
			sqlTextInfo = perfInspectMngSvc.getSqlInfo(performanceCheckMng);
			
			mav.addObject("result", true);
			mav.addObject("sqlTextInfo", sqlTextInfo);
			
		} catch (Exception e) {
			mav.addObject("result", false);
			mav.addObject("sqlTextInfo", "");
		}
		return mav;
	}
	
	// 성능 검증 결과 통보
	@RequestMapping(value = "/inspectRsltNoti", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Result inspectRsltNoti(PerformanceCheckMng performanceCheckMng, @RequestParam(required = false) String pageName) {
		Result result = new Result();
		
		try {
			result = perfInspectMngSvc.inspectRsltNoti(performanceCheckMng, pageName);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(false);
			result.setMessage("성능검증결과 통보중 오류가 발생하였습니다.");
		}
		return result;
	}
	
	// 강제검증완료,강제 검증 완료
	@RequestMapping(value = "/inspectForceFinish", method = RequestMethod.POST)
	@ResponseBody
	public Result inspectForceFinish(@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng,
			Model model) {
		Result result = new Result();
		
		try {
			result = perfInspectMngSvc.inspectForceFinish(performanceCheckMng);
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/getPerfInspectResultCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPerfInspectResultCount(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		PerformanceCheckMng returnPerfChckMng = new PerformanceCheckMng();
		JSONObject jobj = new JSONObject();
		
		try {
			int programCnt = perfInspectMngSvc.getProgramCnt(performanceCheckMng);
			
			if (programCnt > 0) {
				returnPerfChckMng = perfInspectMngSvc.getPerfCheckResultCount(performanceCheckMng);
			}
			jobj = success(returnPerfChckMng).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
			
		}finally {
			returnPerfChckMng = null;
		}
		
		return jobj.toString();
	}
	
	/* 성능검증완료,성능 검증 완료 */
	@RequestMapping(value = "/perfInspectComplete", method = RequestMethod.POST)
	@ResponseBody
	public Result perfInspectComplete(
			@ModelAttribute("performanceCheckMng") PerformanceCheckMng performanceCheckMng, Model model) {
		Result result = new Result();
		
		try {
			result = perfInspectMngSvc.performanceCheckComplete(performanceCheckMng);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
}
