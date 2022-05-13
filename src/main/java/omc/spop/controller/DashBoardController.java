package omc.spop.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.ApplicationCheckCombined;
import omc.spop.model.Database;
import omc.spop.model.DbCheck;
import omc.spop.model.DbCheckConfig;
import omc.spop.model.DbEmergencyAction;
import omc.spop.model.ObjectChange;
import omc.spop.model.PerfImprCondition;
import omc.spop.model.PerfImprWorkCond;
import omc.spop.model.ReorgTargetCondition;
import omc.spop.model.ResourceLimitPrediction;
import omc.spop.model.Result;
import omc.spop.model.SqlCheckCombined;
import omc.spop.model.WrkJobCd;
import omc.spop.service.DashBoardService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2018.08.21 DashBoard
 **********************************************************/

@Controller
public class DashBoardController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(DashBoardController.class);
	
	@Autowired
	private DashBoardService dashBoardSearvice;	

	@RequestMapping(value = "/DashboardV2/objectChangeCondChart", method = RequestMethod.GET)
	public String objectChangeCondChart(Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		
		String jsp = "";
		
		if(auth_cd.equals("ROLE_ITMANAGER")||auth_cd.equals("ROLE_DBMANAGER")){ // IT관리자,DB관리자
			jsp = "dashboard/objectChangeCondChart";
		}
		return jsp;
	}

	@RequestMapping(value = "/DashboardV2/reorgTargetCondChart", method = RequestMethod.GET)
	public String reorgTargetCondChart(Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		
		String jsp = "";
		
		if(auth_cd.equals("ROLE_ITMANAGER")||auth_cd.equals("ROLE_DBMANAGER")){ // IT관리자,DB관리자
			jsp = "dashboard/reorgTargetCondChart";
		}
		return jsp;
	}
	//자원한계예측현황 차트
	@RequestMapping(value = "/DashboardV2/resourceLimitPredictChart", method = RequestMethod.GET)
	public String resourceLimitPredictChart(Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		
		String jsp = "";
		
		if(auth_cd.equals("ROLE_ITMANAGER")||auth_cd.equals("ROLE_DBMANAGER")){ // IT관리자,DB관리자
			jsp = "dashboard/resourceLimitPredictChart";
		}
		return jsp;
	}
	
	@RequestMapping(value = "/DashboardV2/urgentActionTargetCondChart", method = RequestMethod.GET)
	public String urgentActionTargetCondChart(Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		
		String jsp = "";
		
		if(auth_cd.equals("ROLE_ITMANAGER")||auth_cd.equals("ROLE_DBMANAGER")){ // IT관리자,DB관리자
			jsp = "dashboard/urgentActionTargetCondChart";
		}
		return jsp;
	}
	
	@RequestMapping(value = "/DashboardV2/appPerfCheckChart", method = RequestMethod.GET)
	public String appPerfCheckChart(Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		
		String jsp = "";
		
		if(auth_cd.equals("ROLE_ITMANAGER")||auth_cd.equals("ROLE_DBMANAGER")){ // IT관리자,DB관리자
			jsp = "dashboard/appPerfCheckChart";
		}
		return jsp;
	}
	
	@RequestMapping(value = "/DashboardV2/urgentActionTarget", method = RequestMethod.GET)
	public String urgentActionTarget(@ModelAttribute("dbCheckConfig") DbCheckConfig dbCheckConfig,Model model) throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);

		String check_pref_id = dbCheckConfig.getCheck_pref_id();
		model.addAttribute("check_pref_id",check_pref_id);
			
		model.addAttribute("check_day", dbCheckConfig.getCheck_day());
		
		String jsp = "";
		
		if(auth_cd.equals("ROLE_ITMANAGER")||auth_cd.equals("ROLE_DBMANAGER")){ // IT관리자,DB관리자
			jsp = "dashboard/urgentActionTarget";
		}
		return jsp;
	}

	/* Dashboard - DB점검결과(파이그래프) 점검일 구하기 */
	@RequestMapping(value="/DashboardV2/getMaxCheckDay", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getMaxCheckDay(@ModelAttribute("dbCheck") DbCheck dbCheck, Model model) {
		String maxCheckDay = "";
		try{
			maxCheckDay = dashBoardSearvice.getMaxCheckDay();
			logger.debug("======================");
			logger.debug("maxCheckDay:"+maxCheckDay);
			logger.debug("======================");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return maxCheckDay;	
	}
	
	/* Dashboard - DB점검결과(파이그래프) */
	@RequestMapping(value="/DashboardV2/getDbCheckResult", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getDbCheckResult(@ModelAttribute("dbCheck") DbCheck dbCheck, Model model) {
		List<DbCheck> resultList = new ArrayList<DbCheck>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		dbCheck.setUser_id(user_id);
		String check_day = DateUtil.getNowDate("yyyyMMdd");
		dbCheck.setCheck_day(check_day);

		try{
			resultList = dashBoardSearvice.getDbCheckResult(dbCheck);
			logger.debug("======================");
			logger.debug("getDbCheckResult resultList:"+resultList);
			logger.debug("getDbCheckResult resultList:"+success(resultList).toJSONObject().toString());
			logger.debug("======================");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	/* Dashboard -애플리케이션진단(통합) 화면 */
	@RequestMapping(value = "/DashboardV2/applicationCheckCombined", method = RequestMethod.GET)
	public String applicationCheckCombined(@RequestParam(required = true) Map<String, Object> param, Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.mergeAttributes(param);
		
		String jsp = "";
		
		jsp = "dashboard/applicationCheckCombined";
		return jsp;
	}	

	/* Dashboard -애플리케이션진단(통합) */
	@RequestMapping(value="/DashboardV2/getApplicationCheckCombined", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getApplicationCheckCombined(@ModelAttribute("ApplicationCheckCombined") ApplicationCheckCombined applicationCheckCombined, Model model) {
		List<ApplicationCheckCombined> resultList = new ArrayList<ApplicationCheckCombined>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		applicationCheckCombined.setUser_id(user_id);

		String returnValue = "";
		try{
			resultList = dashBoardSearvice.getApplicationCheckCombined(applicationCheckCombined);
			
			List<ApplicationCheckCombined> buildList = TreeWrite.buildApplicationCheckCombinedGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
			logger.debug("returnValue:"+returnValue);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return returnValue;	
	}	
	
	/* Dashboard -애플리케이션진단(통합) */
	@RequestMapping(value="/DashboardV2/getApplicationCheckCombinedResult", method=RequestMethod.POST)
	@ResponseBody
	public Result getApplicationCheckCombinedResult(@ModelAttribute("ApplicationCheckCombined") ApplicationCheckCombined applicationCheckCombined, Model model) {
		List<ApplicationCheckCombined> resultList = new ArrayList<ApplicationCheckCombined>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		applicationCheckCombined.setUser_id(user_id);
		Result result = new Result();
		String returnValue = "";
		try{
			resultList = dashBoardSearvice.getApplicationCheckCombined(applicationCheckCombined);
			List<ApplicationCheckCombined> buildList = TreeWrite.buildApplicationCheckCombinedGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
			result.setResult(true);
			result.setTxtValue(returnValue);
		} catch (SQLException se){
			logger.error("SQL error ==> " + se.getMessage());
			result.setResult(false);
			result.setMessage(se.getMessage());
		} catch (Exception e){
			logger.error("error ==> " + e.getMessage());
			result.setResult(false);
			result.setMessage(e.getMessage());
		}
		return result;	
	}
	
	
	/* Dashboard - APP성능진단 */
	@RequestMapping(value="/DashboardV2/getAppPerfCheck", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getAppPerfCheck(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd, Model model) {
		List<WrkJobCd> resultList = new ArrayList<WrkJobCd>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		wrkJobCd.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getAppPerfCheck(wrkJobCd);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	/**
	 * dashboard에서 object현황 차트를 클릭하였을때 이동하는 화면
	 * @param objectChange
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/DashboardV2/objectChangeAnalysisCondition", method = RequestMethod.GET)
	public ModelAndView objectChangeAnalysisCondition(@ModelAttribute("objectChange") ObjectChange objectChange, 
			@RequestParam(required = true) Map<String, Object> param) {

		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		Map<String, Object> model = param;

		model.put("nowDate", nowDate);
		model.put("menu_id", objectChange.getMenu_id());
		model.put("menu_nm", objectChange.getMenu_nm());

		String rstUrl = "dashboard/objectChangeAnalysisCondition";
		return new ModelAndView(rstUrl, model);		
	}	
	
	@RequestMapping(value = "/DashboardV2/incompleteTuning", method = RequestMethod.GET)
	public ModelAndView incompleteTuning(@ModelAttribute("objectChange") ObjectChange objectChange, 
			@RequestParam(required = true) Map<String, Object> param) {

		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		Map<String, Object> model = param;

		model.put("nowDate", nowDate);
		model.put("menu_id", objectChange.getMenu_id());
		model.put("menu_nm", objectChange.getMenu_nm());

		String rstUrl = "dashboard/incompleteTuning";
		return new ModelAndView(rstUrl, model);		
	}	
	
	/* Dashboard - 오브젝트변경분석(통합) */
	@RequestMapping(value="/DashboardV2/getObjectChangeCheckCombined", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getObjectChangeCheckCombined(@ModelAttribute("objectChange") ObjectChange objectChange, Model model) {
		List<ObjectChange> resultList = new ArrayList<ObjectChange>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		objectChange.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getObjectChangeCheckCombined(objectChange);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Dashboard - 오브젝트 변경현황(최근일주일) */
	@RequestMapping(value="/DashboardV2/getObjectChangeCondition", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getObjectChangeCondition(@ModelAttribute("objectChange") ObjectChange objectChange, Model model) {
		List<ObjectChange> resultList = new ArrayList<ObjectChange>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		objectChange.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getObjectChangeCondition(objectChange);
			logger.debug("======================");
			//logger.debug("resultList:"+success(resultList).toJSONObject().toString());
			logger.debug("======================");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* Dashboard - 성능개선작업현황 */
	@RequestMapping(value="/DashboardV2/getPerfImprWorkCondition", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getPerfImprWorkCondition(@ModelAttribute("perfImprWorkCond") PerfImprWorkCond perfImprWorkCond, Model model) {
		List<PerfImprWorkCond> resultList = new ArrayList<PerfImprWorkCond>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		perfImprWorkCond.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getPerfImprWorkCondition(perfImprWorkCond);
			logger.debug("======================");
			logger.debug("getPerfImprWorkCondition resultList:"+resultList);
			logger.debug("getPerfImprWorkCondition resultList:"+success(resultList).toJSONObject().toString());
			logger.debug("======================");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Dashboard - 튜닝 미완료 */
	@RequestMapping(value="/DashboardV2/getIncompleteTuningList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getIncompleteTuningList(@ModelAttribute("database") Database database, Model model) {
		List<Database> resultList = new ArrayList<Database>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		database.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getIncompleteTuningList(database);
			logger.debug("======================");
			logger.debug("database resultList:"+resultList);
			logger.debug("database resultList:"+success(resultList).toJSONObject().toString());
			logger.debug("======================");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* Dashboard - Reorg대상현황, Reorg 대상현황 */
	@RequestMapping(value="/DashboardV2/getReorgTargetCond", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getReorgTargetCond(@ModelAttribute("reorgTargetCondition") ReorgTargetCondition reorgTargetCondition, Model model) {
		List<ReorgTargetCondition> resultList = new ArrayList<ReorgTargetCondition>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		reorgTargetCondition.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getReorgTargetCond(reorgTargetCondition);
			logger.debug("======================");
			//logger.debug("resultList:"+success(resultList).toJSONObject().toString());
			logger.debug("======================");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* Dashboard - 자원한계점예측 */
	@RequestMapping(value="/DashboardV2/getResourceLimitPredict", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getResourceLimitPredict(@ModelAttribute("resourceLimitPrediction") ResourceLimitPrediction resourceLimitPrediction, Model model) {
		List<ResourceLimitPrediction> resultList = new ArrayList<ResourceLimitPrediction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		resourceLimitPrediction.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getResourceLimitPredict(resourceLimitPrediction);
			logger.debug("======================");
			//logger.debug("resultList:"+success(resultList).toJSONObject().toString());
			logger.debug("======================");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	//자원한계예측현황 통합
	@RequestMapping(value = "/DashboardV2/resourceLimitPredictCondition", method = RequestMethod.GET)
	public String resourceLimitPredictCondition(Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		
		String jsp = "";
		
		if(auth_cd != null){
			jsp = "dashboard/resourceLimitPredictCondition";
		}
		return jsp;
	}
	
	@RequestMapping(value = "/extjs/DashboardV2/resourceLimitPredictCondition", method = RequestMethod.GET)
	public String extjsResourceLimitPredictCondition(Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		
		String jsp = "";
		
		if(auth_cd != null){
			jsp = "extjs/dashboard/resourceLimitPredictCondition";
		}
		return jsp;
	}
	
	@RequestMapping(value = "/DashboardV2/sqlCheckCombined", method = RequestMethod.GET)
	public String sqlCheckCombined(@RequestParam(required = true) Map<String, Object> param, Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.mergeAttributes(param);
	
		String jsp = "";
		
		if(auth_cd != null){
			jsp = "dashboard/sqlCheckCombined";
		}
		return jsp;
	}
	
	/* Dashboard - SQL진단(통합) */
	@RequestMapping(value="/DashboardV2/getSqlCheckCombined", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getSqlCheckCombined(@ModelAttribute("sqlCheckCombined") SqlCheckCombined sqlCheckCombined, Model model) {
		List<SqlCheckCombined> resultList = new ArrayList<SqlCheckCombined>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sqlCheckCombined.setUser_id(user_id);
		String gather_day = sqlCheckCombined.getGather_day();
		gather_day = gather_day.replaceAll("\\.","");
		gather_day = gather_day.replaceAll("-","");
		sqlCheckCombined.setGather_day(gather_day);

		String returnValue = "";
		try{
			resultList = dashBoardSearvice.getSqlCheckCombined(sqlCheckCombined);
			
			List<SqlCheckCombined> buildList = TreeWrite.buildSqlCheckCombinedGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
			logger.debug("returnValue:"+returnValue);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return returnValue;	
	}	
		
	/* Dashboard - SQL진단(통합) */
	@RequestMapping(value="/DashboardV2/getSqlCheckCombinedResult", method=RequestMethod.POST)
	@ResponseBody
	public Result getSqlCheckCombinedResult(@ModelAttribute("sqlCheckCombined") SqlCheckCombined sqlCheckCombined, Model model) {
		List<SqlCheckCombined> resultList = new ArrayList<SqlCheckCombined>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sqlCheckCombined.setUser_id(user_id);
		Result result = new Result();
		String returnValue = "";
		try{
			resultList = dashBoardSearvice.getSqlCheckCombined(sqlCheckCombined);
			List<SqlCheckCombined> buildList = TreeWrite.buildSqlCheckCombinedGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
			result.setResult(true);
			result.setTxtValue(returnValue);
		} catch (SQLException se){
			logger.error("SQL error ==> " + se.getMessage());
			result.setResult(false);
			result.setMessage(se.getMessage());
		} catch (Exception e){
			logger.error("error ==> " + e.getMessage());
			result.setResult(false);
			result.setMessage(e.getMessage());
		}
		return result;	
	}
	
	/* Dashboard - 자원한계점예측(통합) */
	@RequestMapping(value="/DashboardV2/getResourceLimitPredictCombined", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getResourceLimitPredictCombined(@ModelAttribute("resourceLimitPrediction") ResourceLimitPrediction resourceLimitPrediction, Model model) {
		List<ResourceLimitPrediction> resultList = new ArrayList<ResourceLimitPrediction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		resourceLimitPrediction.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getResourceLimitPredictCombined(resourceLimitPrediction);
			logger.debug("======================");
			logger.debug("getResourceLimitPredictCombined resultList:"+resultList);
			logger.debug("getResourceLimitPredictCombined resultList:"+success(resultList).toJSONObject().toString());
			logger.debug("======================");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	

	/* Dashboard - SQL성능진단 */
	@RequestMapping(value="/DashboardV2/getSqlPerfCheck", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getSqlPerfCheck(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd, Model model) {
		List<WrkJobCd> resultList = new ArrayList<WrkJobCd>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		wrkJobCd.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getSqlPerfCheck(wrkJobCd);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	@RequestMapping(value="/DashboardV2/getSqlPerfCheck.json")
	@ResponseBody
	public ModelAndView getSqlPerfCheckJson(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd, Model model) {
		List<WrkJobCd> resultList = new ArrayList<WrkJobCd>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		wrkJobCd.setUser_id(user_id);

		ModelAndView mav = new ModelAndView();		
		mav.setViewName("jsonView");		
		try{
			resultList = dashBoardSearvice.getSqlPerfCheck(wrkJobCd);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			mav.addObject("data", getErrorJsonString(ex));
			return mav;
		}
		
		mav.addObject("data",success(resultList).toJSONObject().get("rows").toString());

		return mav;
	}
	
	@RequestMapping(value="data.json")
	@ResponseBody
	public ModelAndView dataJson(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd, Model model) {
		ModelAndView mav = new ModelAndView();		
		mav.setViewName("jsonView");		
		
		StringBuilder jsonString = new StringBuilder();
		jsonString.append("").append("[{").append("name: 'metric one',")
				.append("data3: 5").append("}, {").append("name: 'metric two',").append("data3: 6").append("}]")
				.append("");
		mav.addObject("data",jsonString.toString());

		logger.debug("mav=======>"+mav.toString());
		logger.debug("viewName=======>"+mav.getViewName());
		logger.debug("view=======>"+mav.getView());
		logger.debug("model=======>"+mav.getModel());
		logger.debug("modelmap=======>"+mav.getModelMap());
		
		return mav;
	}	
	
	/* Dashboard - 긴급조치현황(막대그래프) */
	@RequestMapping(value="/DashboardV2/getUrgentActionCondition", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getUrgentActionCondition(@ModelAttribute("dbCheckConfig") DbCheckConfig dbCheckConfig, Model model) {
		List<DbCheckConfig> resultList = new ArrayList<DbCheckConfig>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		dbCheckConfig.setUser_id(user_id);

		try{
			resultList = dashBoardSearvice.getUrgentActionCondition(dbCheckConfig);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* Dashboard - 긴급조치대상현황(통합) */
	@RequestMapping(value="/DashboardV2/getUrgentActionTargetCondCombined", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getUrgentActionTargetCondCombined(@ModelAttribute("dbEmergencyAction") DbEmergencyAction dbEmergencyAction, Model model) {
		List<DbEmergencyAction> resultList = new ArrayList<DbEmergencyAction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		dbEmergencyAction.setUser_id(user_id);
		String check_day = DateUtil.getNowDate("yyyyMMdd");
		dbEmergencyAction.setCheck_day(check_day);

		try{
			resultList = dashBoardSearvice.getUrgentActionTargetCondCombined(dbEmergencyAction);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* BASIC_CHECK_CONFIG 조회 */
	@RequestMapping(value = "/DashboardV2/getBasicCheckPref", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCommonCode() throws Exception {
		List<DbCheckConfig> commonList = new ArrayList<DbCheckConfig>();

		DbCheckConfig temp = new DbCheckConfig();
		temp.setCheck_pref_id("");
		temp.setCheck_pref_nm("선택");

		commonList.add(temp);

		try {
			List<DbCheckConfig> commonList2 = dashBoardSearvice.getBasicCheckPref();
			commonList.addAll(commonList2);
		} catch (Exception ex) {
			logger.error("Common Error ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(commonList).toJSONObject().get("rows").toString();
	}

	/* Dashboard -성능개선현황 */
	@RequestMapping(value="/DashboardV2/getPerfImprCondition", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getPerfImprCondition(@ModelAttribute("database") Database database, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		database.setUser_id(user_id);
		
		List<PerfImprCondition> resultList = new ArrayList<PerfImprCondition>();

		try{
			resultList = dashBoardSearvice.getPerfImprCondition(database);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
		
}