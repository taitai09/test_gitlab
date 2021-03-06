package omc.spop.controller;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.Result;
import omc.spop.model.SQLStandards;
import omc.spop.model.StandardComplianceRateTrend;
import omc.spop.service.SQLStandardsService;
import omc.spop.utils.CryptoUtil;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

@Controller
public class SQLStandardsController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(SQLStandardsController.class);
	
	@Autowired
	private SQLStandardsService sqlStandarsService;
	
	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	public static final String SCHEDULER_KEY = "_RSA_SCHEDULER_Key_"; // ????????? session key
	
	/* ?????? ???????????? ?????? */
	@RequestMapping(value = "/sqlStandards/loadAllIndex", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadAllIndex(@RequestParam HashMap<String, String> map) {
		
		List<SQLStandards> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = sqlStandarsService.loadAllIndex(map);
			jobj = success(resultList).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		return jobj.toString();
	}
	
	/* sql ?????? ?????? ??????(new) */
	@RequestMapping(value = "/sqlStandardCheckResult/loadSchedulerList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSchedulerList(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		
		List<JobSchedulerBase> resultList = Collections.emptyList();
		
		try {
			resultList = sqlStandarsService.loadSchedulerList(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* sql ?????? ?????? ?????? - ?????? ????????? ????????????(new) */
	@RequestMapping(value = "/sqlStandardCheckResult/loadIndexList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadIndexList(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		
		List<SQLStandards> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = sqlStandarsService.loadIndexList(sqlStandards);
			jobj = success(resultList).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}

		return jobj.toString();
	}
	
	/* sql ?????? ?????? ?????? - ?????? ????????? ?????? ?????? ?????????(new) */
	@RequestMapping(value = "/sqlStandardCheckResult/loadResultList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadResultList(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = sqlStandarsService.loadResultList(sqlStandards);
			returnStr = getJSONResult(resultList, true).toJSONObject().toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	/* sql ?????? ?????? ?????? - ?????? ?????? */
	@RequestMapping(value = "/sqlStandardCheckResult/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void excelDownloadResult(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model){
		
		try {
			model.addAttribute("fileName", "SQL_??????_??????_??????");
			sqlStandarsService.excelDownloadResult(sqlStandards, req, res, model);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
	}
	
	/* sql ?????? ?????? ???????????? ?????? (new) */
	@RequestMapping(value = "/manageScheduler/loadSchedulerList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSchedulerByManager(
			@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, HttpSession session) {
		
		jobSchedulerBase.setJob_scheduler_type_cd("37");
		List<JobSchedulerBase> resultList = Collections.emptyList();
		
		try {
			resultList = sqlStandarsService.loadSchedulerByManager(jobSchedulerBase);
			
			PublicKey publicKey = ( (KeyPair) session.getAttribute(SCHEDULER_KEY) ).getPublic();
			String encodedPw = "";
			String prePw = "";
			
			for (JobSchedulerBase jobScheduler : resultList) {
				prePw = jobScheduler.getSvn_os_user_password();
				
				if (StringUtil.isEmpty(prePw) == false) {
					encodedPw = encryptRSA(prePw, publicKey);
					jobScheduler.setSvn_os_user_password( encodedPw );
				}
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* ?????? ????????? SQL - ?????? ????????? SQL ?????? */
	@RequestMapping(value = "/sqlStandards/loadNonStdSql", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadNonStdSql(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		int dataCount4NextBtn = 0;
		
		JSONObject jobj = new JSONObject();
		String returnStr = "";
		
		try {
			resultList = sqlStandarsService.loadNonStdSqlList(sqlStandards);
			
			int pagePerCnt = sqlStandards.getPagePerCount();
			int resultSize = resultList.size();
			
			if ( resultList != null && resultSize > pagePerCnt ) {
				dataCount4NextBtn = resultSize;
				/* ???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ?????????????????? */
				resultList.remove(pagePerCnt);
			}
			jobj = getJSONResult(resultList, true).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
			returnStr = jobj.toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
			jobj = null;
		}
		return returnStr;
	}
	
	/* ???????????? ?????? ???????????? */
	@RequestMapping(value = "/manageScheduler/saveSetting", method = RequestMethod.POST)
	@ResponseBody
	public Result saveSetting(
			@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail,
			HttpServletRequest req, HttpSession session) {
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());
		
		Result result = new Result();
		boolean b_isUpdate = false;
		int count = 0;
		
		try {
			pwDecrypt(jobSchedulerConfigDetail, session);
			
			b_isUpdate = sqlStandarsService.isUpdateY(jobSchedulerConfigDetail);
			
			if ( b_isUpdate ) {
				jobSchedulerConfigDetail.setUse_yn("Y");
				
			}else {
				jobSchedulerConfigDetail.setUse_yn("N");
			}
			
			count = sqlStandarsService.saveSetting(jobSchedulerConfigDetail);
			
			if (count == 2) {
				result.setResult(true);
				result.setMessage("?????? ???????????????.");
			} else {
				result.setResult(false);
				result.setMessage("????????? ?????????????????????.");
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* ???????????? ?????? ?????? */
	@RequestMapping(value = "/manageScheduler/modifySetting", method = RequestMethod.POST)
	@ResponseBody
	public Result modifySetting(
			@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail,
			HttpSession session) {
		
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());
		
		Result result = new Result();
		boolean b_isUpdate = false;
		int count = 0;
		
		try {
			pwDecrypt(jobSchedulerConfigDetail, session);
			
			b_isUpdate = sqlStandarsService.isUpdateY(jobSchedulerConfigDetail);
			
			if ( b_isUpdate ) {
				jobSchedulerConfigDetail.setUse_yn("Y");
				
			}else {
				jobSchedulerConfigDetail.setUse_yn("N");
			}
			
			count = sqlStandarsService.modifySetting(jobSchedulerConfigDetail);
			
			if (count == 2) {
				result.setResult(true);
				result.setMessage("?????? ???????????????.");
			} else {
				result.setResult(false);
				result.setMessage("????????? ?????????????????????.");
			}
		} catch(BadPaddingException ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage("???????????? ???????????????. Open-POP ????????? ??????????????? ?????? ????????????.");	//test???
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* ???????????? ?????? ?????? */
	@RequestMapping(value = "/manageScheduler/deleteScheduler", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteScheduler(
			@ModelAttribute("jobSchedulerConfigDetail") JobSchedulerConfigDetail jobSchedulerConfigDetail) {
		
		jobSchedulerConfigDetail.setUpd_id(SessionManager.getLoginSession().getUsers().getUser_id());
		
		Result result = new Result();
		int count = 0;
		
		try {
			count = sqlStandarsService.deleteScheduler(jobSchedulerConfigDetail);
			
			if (count == 2) {
				result.setResult(true);
				result.setMessage("?????? ???????????????.");
			} else {
				result.setResult(false);
				result.setMessage("????????? ?????????????????????.");
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* ???????????? ?????? ???????????? */
	@RequestMapping(value = "/manageScheduler/excelDownload", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView excelDownload(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		
		jobSchedulerBase.setJob_scheduler_type_cd("37");
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		
		try {
			resultList = sqlStandarsService.excelDownload(jobSchedulerBase);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
		model.addAttribute("fileName", "SQL_??????_??????_????????????_??????");
		model.addAttribute("sheetName", "SQL_??????_??????_????????????_??????");
		model.addAttribute("excelId", "SQL_STD_QTY_SCHEDULER_MANAGER");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* ?????? ????????? */
	@RequestMapping(value = "/LoadQualityTable", method = RequestMethod.GET)
	public String LoadQualityTable(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm());
		
		return "sqlStandards/qualityTable";
	}
	
	/* ?????? ????????? action */
	@RequestMapping(value = "/loadQualityTable", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadQualityTable(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlStandarsService.loadQualityTable(sqlStandards);
			
			if (resultList != null && resultList.size() > sqlStandards.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlStandards.getPagePerCount());
				
			} else if (resultList != null && resultList.size() == 1 && resultList.get(0).containsKey("HEAD")) {
				/* ???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ?????????????????? */
				resultList.remove(resultList.size() - 1);
				dataCount4NextBtn = resultList.size();
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
		
		JSONObject jobj = getJSONResult(resultList, true).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		
		return jobj.toString();
	}
	
	/* ?????? ????????? - ?????? ?????? */
	@RequestMapping(value = "/excelDownQualityTable", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownQualityTable(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = null;
		Map<String, Object> headOptions = null;
		
		try {
			resultList = sqlStandarsService.excelDownQualityTable(sqlStandards);
			
			if (resultList != null) {
				headOptions = resultList.remove(resultList.size() - 1);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
		model.addAttribute("fileName", "SQL_????????????_??????_?????????");
		model.addAttribute("sheetName", "SQL_????????????_??????_?????????");
		
		if (headOptions != null) {
			String head = headOptions.get("HEAD") + "";
			String[] headSplit = head.split("\\;");
			
			// ?????? ?????? ???????????? ?????? ?????? ????????? ??????.
			String[] headers = new String[headSplit.length];
			
			for (int i = 0; i < headSplit.length; i++) {
				headers[i] = headSplit[i].split("\\/")[0];
			}
			
			model.addAttribute("excelHeaders", headers);
		}
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* ?????? ????????? action */
	@RequestMapping(value = "/loadProjectQualityTable", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadProjectQualityTable(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlStandarsService.loadProjectQualityTable(sqlStandards);
			
			if (resultList != null && resultList.size() > sqlStandards.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlStandards.getPagePerCount());
				/* ???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ?????????????????? */
			} else if (resultList != null && resultList.size() == 1 && resultList.get(0).containsKey("HEAD")) {
				resultList.remove(resultList.size() - 1);
				dataCount4NextBtn = resultList.size();
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = getJSONResult(resultList, true).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		
		return jobj.toString();
	}

	/* ?????? ?????????, ?????? ????????? SQL - ?????? ?????? */
	@RequestMapping(value = "/excelDownQualityTableMultiDynamic", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownQualityTableMultiDynamic(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		List<List<LinkedHashMap<String, Object>>> multiDynamicResultList = new ArrayList<List<LinkedHashMap<String, Object>>>();
		List<LinkedHashMap<String, Object>> resultListQulityTable = null;
		List<LinkedHashMap<String, Object>> resultListNonCompliantCode = null;
		List<String> sheetName = new ArrayList<String>();
		String menuNm = sqlStandards.getMenu_nm();
		Map<String, Object> headOptions = null;
		
		try {
			resultListQulityTable = sqlStandarsService.excelDownQualityTable(sqlStandards);
			
			sqlStandards.setQty_chk_idt_cd("003");
			resultListNonCompliantCode = sqlStandarsService.excelDownNonCompliantCode(sqlStandards);
			
			if (resultListQulityTable != null) {
				sheetName.add("SQL_????????????_??????_?????????");
				multiDynamicResultList.add(resultListQulityTable);
			}
			
			if (resultListNonCompliantCode.size() > 0) {
				sheetName.add("??????_?????????_SQL");
				multiDynamicResultList.add(resultListNonCompliantCode);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
		model.addAttribute("fileName", "SQL_????????????_?????????");
		model.addAttribute("sheetName", sheetName);
		
		return new ModelAndView("xlsxMultiDynamicView", "multiDynamicResultList", multiDynamicResultList);
	}
	
	/* ?????? ????????? SQL */
	@RequestMapping(value = "/LoadNonCompliantCode", method = RequestMethod.GET)
	public String LoadNonCompliantCode(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm());
		
		return "sqlStandards/nonCompliantCode";
	}
	
	/* ?????? ????????? SQL action */
	@RequestMapping(value = "/loadNonCompliantCode", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadNonCompliantCode(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlStandarsService.loadNonCompliantCode(sqlStandards);
			
			if (resultList != null && resultList.size() > sqlStandards.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlStandards.getPagePerCount());
				/* ???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ?????????????????? */
				
			} else if (resultList != null && resultList.size() == 1 && resultList.get(0).containsKey("HEAD")) {
				resultList.remove(resultList.size() - 1);
				dataCount4NextBtn = resultList.size();
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		logger.trace("list resultList:\n", resultList);
		
		JSONObject jobj = getJSONResult(resultList, true).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		
		return jobj.toString();
	}
	
	/* ?????? ????????? SQL - ?????? ?????? */
	@RequestMapping(value = "/excelDownNonCompliantCode", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownNonCompliantCode(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = null;
		Map<String, Object> headOptions = null;
		
		try {
			resultList = sqlStandarsService.excelDownNonCompliantCode(sqlStandards);
			if (resultList != null) {
				headOptions = resultList.remove(resultList.size() - 1);
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
		model.addAttribute("fileName", "??????_?????????_SQL");
		model.addAttribute("sheetName", "??????_?????????_SQL");
		
		if (headOptions != null) {
			String head = headOptions.get("HEAD") + "";
			String[] headSplit = head.split("\\;");
			
			// ?????? ?????? ???????????? ?????? ?????? ????????? ??????.
			String[] headers = new String[headSplit.length];
			
			for (int i = 0; i < headSplit.length; i++) {
				headers[i] = headSplit[i].split("\\/")[0];
			}
			
			model.addAttribute("excelHeaders", headers);
		}
		
		logger.trace("excel resultList:\n", resultList);
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* ?????? ?????? ?????? ?????? */
	@RequestMapping(value = "/MaintainQualityCheckIndicator", method = RequestMethod.GET)
	public String MaintainSqlQualityCheck(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm());
		
		return "sqlStandards/maintainQualityCheckIndicator";
	}
	
	/* ?????? ?????? ?????? ?????? action */
	@RequestMapping(value = "/maintainQualityCheckIndicator", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String maintainQualityCheckIndicator(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		
		try {
			resultList = sqlStandarsService.maintainQualityCheckIndicator(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}

	/* ?????? ?????? ?????? ?????? ?????? */
	@RequestMapping(value = "/saveMaintainQualityCheckIndicator", method = RequestMethod.POST)
	@ResponseBody
	public Result saveMaintainQualityCheckIndicator(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		Result result = new Result();
		try {
			result = sqlStandarsService.saveMaintainQualityCheckIndicator(sqlStandards);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* ?????? ?????? ?????? ?????? ?????? */
	@RequestMapping(value = "/deleteMaintainQualityCheckIndicator", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteMaintainQualityCheckIndicator(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		Result result = new Result();
		try {
			int deleteResult = sqlStandarsService.deleteMaintainQualityCheckIndicator(sqlStandards);
			if (deleteResult > 0) {
				result.setResult(true);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/**
	 * ???????????? ?????? ????????????
	 * 
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excelDownMaintainQualityCheckIndicator", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView excelDownMaintainQualityCheckIndicator(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlStandarsService.excelDownMaintainQualityCheckIndicator(sqlStandards);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ???????????? ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQL_??????_??????_??????_??????");
		model.addAttribute("sheetName", "SQL_??????_??????_??????_??????");
		model.addAttribute("excelId", "MAINTAIN_QUALITY_CHECK_INDICATOR");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* ?????? ?????? SQL ?????? */
	@RequestMapping(value = "/MaintainQualityCheckSql", method = RequestMethod.GET)
	public String MaintainQualityCheckSql(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm());
		
		return "sqlStandards/maintainQualityCheckSql";
	}
	
	/* ?????? ?????? ?????? ?????? ?????? */
	@RequestMapping(value = "/getQtyChkIdtCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getQtyChkIdtCd(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) throws Exception {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		List<SQLStandards> qtyChkIdCdList = sqlStandarsService.getQtyChkIdtCd(sqlStandards);
		SQLStandards tempSqlStandards = new SQLStandards();
		
		tempSqlStandards.setQty_chk_idt_cd("");
		tempSqlStandards.setQty_chk_idt_cd_nm("??????");
		
		resultList.add(tempSqlStandards);
		resultList.addAll(qtyChkIdCdList);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* ?????? ?????? ?????? ?????? ?????? */
	@RequestMapping(value = "/getQtyChkIdtCd2", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getQtyChkIdtCd2(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) throws Exception {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		List<SQLStandards> qtyChkIdCdList = sqlStandarsService.getQtyChkIdtCd2(sqlStandards);
		
		resultList.addAll(qtyChkIdCdList);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* ?????? ?????? SQL ?????? action */
	@RequestMapping(value = "/maintainQualityCheckSql", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String maintainQualityCheckSql(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlStandarsService.maintainQualityCheckSql(sqlStandards);
			
			if (resultList != null && resultList.size() > sqlStandards.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		
		return jobj.toString();
	}
	
	/* ?????? ?????? SQL ?????? save */
	@RequestMapping(value = "/saveMaintainQualityCheckSql", method = RequestMethod.POST)
	@ResponseBody
	public Result saveMaintainQualityCheckSql(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		return sqlStandarsService.saveMaintainQualityCheckSql(sqlStandards);
	}
	
	/* ?????? ?????? SQL ?????? delete */
	@RequestMapping(value = "/deleteMaintainQualityCheckSql", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteMaintainQualityCheckSql(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		Result result = new Result();
		
		try {
			int deleteResult = sqlStandarsService.deleteMaintainQualityCheckSql(sqlStandards);
			
			if (deleteResult > 0) {
				result.setResult(true);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/**
	 * ???????????? ?????? ????????????
	 * 
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excelDownMaintainQualityCheckSql", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownMaintainQualityCheckSql(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		try {
			resultList = sqlStandarsService.excelDownMaintainQualityCheckSql(sqlStandards);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
		model.addAttribute("fileName", "SQL_??????_??????_SQL_??????");
		model.addAttribute("sheetName", "SQL_??????_??????_SQL_??????");
		model.addAttribute("excelId", "MAINTAIN_QUALITY_CHECK_SQL");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* SQL ???????????? ?????? ?????? ?????? */
	@RequestMapping(value = "/MaintainQualityCheckException", method = RequestMethod.GET)
	public String MaintainQualityCheckException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm());
		
		return "sqlStandards/maintainQualityCheckException";
	}
	
	/* ?????? ?????? ?????? ?????? ?????? - SQL ???????????? ?????? ?????? ?????? */
	@RequestMapping(value = "/getQtyChkIdtCdFromException", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getQtyChkIdtCdFromException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards)
			throws Exception {
		
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		List<SQLStandards> qtyChkIdCdList = sqlStandarsService.getQtyChkIdtCdFromException(sqlStandards);
		if (sqlStandards.getDml_yn().equals("Y")) {
			SQLStandards tempSqlStandards = new SQLStandards();
			
			tempSqlStandards.setQty_chk_idt_cd("");
			tempSqlStandards.setQty_chk_idt_cd_nm("??????");
			
			resultList.add(tempSqlStandards);
		}
		
		resultList.addAll(qtyChkIdCdList);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* SQL ???????????? ?????? ?????? ?????? action */
	@RequestMapping(value = "/maintainQualityCheckException", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String maintainQualityCheckException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlStandarsService.maintainQualityCheckException(sqlStandards);
			
			if (resultList != null && resultList.size() > sqlStandards.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlStandards.getPagePerCount());
				/* ???????????? ????????? ???????????? ???????????? 0~9?????? ???10?????? ?????????????????? */
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* SQL ???????????? ?????? ?????? ?????? ?????? */
	@RequestMapping(value = "/saveMaintainQualityCheckException", method = RequestMethod.POST)
	@ResponseBody
	public Result saveMaintainQualityCheckException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		Result result = new Result();
		logger.info(sqlStandards.toString());
		
		try {
			result = sqlStandarsService.saveMaintainQualityCheckException(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL ???????????? ?????? ?????? ?????? ?????? */
	@RequestMapping(value = "/deleteMaintainQualityCheckException", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteMaintainQualityCheckException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		Result result = new Result();
		try {
			int deleteResult = sqlStandarsService.deleteMaintainQualityCheckException(sqlStandards);
			if (deleteResult > 0) {
				result.setResult(true);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL ???????????? ?????? ?????? ?????? ???????????? */
	@RequestMapping(value = "/excelDownMaintainQualityCheckException", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView excelDownMaintainQualityCheckException(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlStandarsService.excelDownMaintainQualityCheckException(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
		model.addAttribute("fileName", "SQL_????????????_??????_??????_??????");
		model.addAttribute("sheetName", "SQL_????????????_??????_??????_??????");
		model.addAttribute("excelId", "MAINTAIN_QUALITY_CHECK_EXCEPTION");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* SQL ???????????? ?????? ?????? ?????? ???????????? ????????? */
	@RequestMapping(value = "/excelUploadMaintainQualityCheckException", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public Result excelUploadMaintainQualityCheckException(@RequestParam("uploadFile") MultipartFile file,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		Result result = new Result();
		
		if ( file != null && file.isEmpty() == false ) {
			if (file.getSize() > maxUploadSize) {
				result.setResult(false);
				result.setMessage("?????? ????????? ?????? ?????????.\\n" + maxUploadMegaBytes + "?????? ????????? ????????? ?????????.");
				
			} else {
				try {
					result = sqlStandarsService.excelUploadMaintainQualityCheckException(file);
					
				} catch (Exception ex) {
					String methodName = new Object() {}.getClass().getEnclosingClass().getName();
					logger.error( "{} ???????????? ==> ", methodName, ex );
					
					result.setResult(false);
				}
			}
		}
		
		return result;
	}
	
	/* SQL ?????? ???????????? ?????? */
	@RequestMapping(value = "/QualityReviewWork", method = RequestMethod.GET)
	public String QualityReviewWork(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm());
		
		return "sqlStandards/qualityReviewWork";
	}
	
	/* ?????? ????????? action */
	@RequestMapping(value = "/loadQualityReviewWork", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadQualityReviewWork(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		
		try {
			resultList = sqlStandarsService.loadQualityReviewWork(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* SQL ??? */
	@RequestMapping(value = "/loadSqlCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqlCount(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		
		try {
			resultList = sqlStandarsService.loadSqlCount(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* ???????????? */
	@RequestMapping(value = "/loadWorkStatus", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadWorkStatus(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		
		try {
			resultList = sqlStandarsService.loadWorkStatus(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* ??????????????? */
	@RequestMapping(value = "/loadErrorMessage", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadErrorMessage(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		
		try {
			resultList = sqlStandarsService.loadErrorMessage(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* SQL ?????? ???????????? ?????? ???????????? */
	@RequestMapping(value = "/excelDownQualityReviewWork", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownQualityReviewWork(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlStandarsService.excelDownQualityReviewWork(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
		}
		model.addAttribute("fileName", "SQL_??????_????????????_??????");
		model.addAttribute("sheetName", "SQL_??????_????????????_??????");
		model.addAttribute("excelId", "QUALITY_REVIEW_WORK");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* ????????????????????? ??????????????? ?????? ?????? */
	@RequestMapping(value = "/checkQualityReviewWorkInRun", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String checkQualityReviewWorkInRun(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		
		try {
			resultList = sqlStandarsService.checkQualityReviewWorkInRun(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* PreProcess */
	@RequestMapping(value = "/preProcess", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String preProcess(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		StandardComplianceRateTrend scrt = new StandardComplianceRateTrend();
		SQLStandards temporaryModel = new SQLStandards();
		String parameter_list = "project_id:" + sqlStandards.getProject_id();
		String reportMsg = "";
		
		try {
			reportMsg = "start preProcess[" + printCurrentTime(true) + "]";
			
			// 1. check checkProc
			sqlStandards.setSql_std_gather_dt(printCurrentTime(false));
			sqlStandards.setWrk_end_dt("");
			sqlStandards.setErr_yn("");
			sqlStandards.setErr_sbst("");
			sqlStandards.setErr_table_name("");
			sqlStandards.setForce_close_yn("");
			
			sqlStandarsService.updateProc(sqlStandards);
			
			// 2. Pre Process
			scrt.setParameter_list(parameter_list);
			scrt.setQty_chk_idt_cd("012");
			
			// DecryptRule Process
			temporaryModel = sqlStandarsService.decryptRule(scrt);
			
			logger.info("Pre Process ***");
			sqlStandarsService.loadPreProcess(temporaryModel);
			
			logger.info(reportMsg + " end preProcess[" + printCurrentTime(true) + "]");
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* ?????????????????? */
	@RequestMapping(value = "/qualityReviewOperation", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String qualityReviewOperation(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		StandardComplianceRateTrend scrt = new StandardComplianceRateTrend();
		SQLStandards temporaryModel = new SQLStandards();
		String parameter_list = "project_id:" + sqlStandards.getProject_id();
		String reportMsg = "";
		String gatherDate = "";
		
		try {
			reportMsg = "start qualityReviewOperation[" + printCurrentTime(true) + "]";
			
			logger.info("go Main Process ***");
			// 3. Main Process
			for (int i = 1; i < 9; i++) {
				parameter_list = "project_id:" + sqlStandards.getProject_id() + ",wrk_thread_no:" + (i);
				
				scrt = new StandardComplianceRateTrend();
				
				scrt.setParameter_list(parameter_list);
				scrt.setQty_chk_idt_cd("013");
				scrt.setProject_id(sqlStandards.getProject_id());
				
				// DecryptRule Process
				temporaryModel = sqlStandarsService.decryptRuleProject(scrt);
				
				logger.trace("Main Process index[" + i + "] ***");
				sqlStandarsService.qualityReviewOperation(temporaryModel);
			}
			
			temporaryModel.setProject_id(sqlStandards.getProject_id());
			temporaryModel.setErr_yn("N");
			gatherDate = printCurrentTime(false);
			temporaryModel.setSql_std_gather_dt(gatherDate);
			
			sqlStandarsService.updateCheckMainProcess(temporaryModel);
			
			logger.info("end Main Process ***");
			
			boolean doCheckFlag = true;
			String wrkCompleteYn = "";
			
			while (doCheckFlag) {
				logger.trace(printCurrentTime(true));
				resultList = sqlStandarsService.checkMainProcess(sqlStandards);
				
				wrkCompleteYn = resultList.get(0).getWrk_complete_yn();
				logger.trace("wrkCompleteYn: {}", wrkCompleteYn);
				
				if (wrkCompleteYn.equalsIgnoreCase("Y") || wrkCompleteYn.equalsIgnoreCase("E")
						|| wrkCompleteYn.equalsIgnoreCase("F")) {
					doCheckFlag = false;
					continue;
				}
				
				Thread.sleep(5000);
			}
			
			if (wrkCompleteYn.equals("E")) {
				logger.warn("***** Error ******************");
				logger.warn("* wrk complete status Error. No more process");
				logger.warn("******************************");
				
			} else if (wrkCompleteYn.equals("F")) {
				logger.warn("***** Force Complete ******************");
				logger.warn("* wrk complete status Force Complete. No more process");
				logger.warn("******************************");
				
			} else {
				// 4. Summary Task
				parameter_list = "project_id:" + sqlStandards.getProject_id();
				scrt = new StandardComplianceRateTrend();
				scrt.setParameter_list(parameter_list);
				scrt.setQty_chk_idt_cd("014");
				
				// DecryptRule Process
				temporaryModel = sqlStandarsService.decryptRule(scrt);
				
				logger.info("Summary Task ***");
				sqlStandarsService.summaryTask(temporaryModel, sqlStandards);
				
				// 5. Summary Error Task
				parameter_list = "project_id:" + sqlStandards.getProject_id();
				scrt = new StandardComplianceRateTrend();
				scrt.setParameter_list(parameter_list);
				scrt.setQty_chk_idt_cd("004");
				
				// DecryptRule Process
				temporaryModel = sqlStandarsService.decryptRule(scrt);
				
				logger.info("Summary Error Task ***");
				sqlStandarsService.summaryErrorTask(temporaryModel, sqlStandards);
				
				// 6. check checkProc
				sqlStandards.setWrk_end_dt(printCurrentTime(false));
				sqlStandards.setSql_std_gather_dt(gatherDate);
				sqlStandarsService.updatingCompleteCheckMainProcess(sqlStandards);
				
				logger.trace("{} end qualityReviewOperation[{}]",reportMsg, printCurrentTime(true));
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	public String printCurrentTime(boolean printFlag) {
		SimpleDateFormat dateFormat = null;
		
		if (printFlag) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
		} else {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}
	
	/* ?????????????????? */
	@RequestMapping(value = "/forceProcessingCompleted", method = RequestMethod.POST)
	@ResponseBody
	public Result forceProcessingCompleted(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		Result result = new Result();
		
		try {
			result = sqlStandarsService.forceProcessingCompleted(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* Check Process */
	@RequestMapping(value = "/checkProc", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String checkProc(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		List<SQLStandards> resultList = new ArrayList<SQLStandards>();
		
		try {
			resultList = sqlStandarsService.checkProc(sqlStandards);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} ???????????? ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	private void pwDecrypt(JobSchedulerConfigDetail jobSchedulerConfigDetail, HttpSession session) throws Exception {
		String svn_os_user_password = jobSchedulerConfigDetail.getSvn_os_user_password();
		
		if (StringUtil.isEmpty(svn_os_user_password) == false) {
			PrivateKey privateKey = ( (KeyPair) session.getAttribute(SCHEDULER_KEY) ).getPrivate();
			
			String decodedPw = decryptRSA(svn_os_user_password, privateKey);
			jobSchedulerConfigDetail.setSvn_os_user_password(decodedPw);
		}
	}
	
	/* RSA ????????? */
	public String decryptRSA(String encrypted, PrivateKey privateKey) throws Exception{
		byte[] bytePlain = null;
		
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			byte[] byteEncrypted = CryptoUtil.hexToByteArray(encrypted);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			bytePlain = cipher.doFinal(byteEncrypted);
			
		} catch (BadPaddingException ex) {
			logger.error("????????? ?????? ?????? ==> ", ex );
			
			throw new Exception(ex);
		}
		return new String(bytePlain, "utf-8");
	}
	
	/* RSA ????????? */
	public String encryptRSA(String plainText, PublicKey publicKey){
		byte[] bytePlain = null;
		
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			bytePlain = cipher.doFinal(plainText.getBytes());
			
		} catch (Exception ex) {
			logger.error("????????? ?????? ?????? ==> ", ex );
		}
		return CryptoUtil.byteToHexString(bytePlain);
	}
}