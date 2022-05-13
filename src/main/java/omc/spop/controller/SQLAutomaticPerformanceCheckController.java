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

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.AuthoritySQL;
import omc.spop.model.ExplainPlanTree;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.OdsUsers;
import omc.spop.model.Project;
import omc.spop.model.ProjectSqlIdfyCondition;
import omc.spop.model.Result;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.service.CommonService;
import omc.spop.service.SQLAutomaticPerformanceCheckService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 * 2020.07.01	이재우		기능개선
 **********************************************************/

@Controller
@RequestMapping(value = "/SQLAutomaticPerformanceCheck")
public class SQLAutomaticPerformanceCheckController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(SQLAutomaticPerformanceCheckController.class);
	
	@Autowired
	private SQLAutomaticPerformanceCheckService sqlAutomaticPerformanceCheckService;

	@Autowired
	private CommonService commonService;
	
	/* SQLAutomaticPerformanceCheck */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String SQLAutomaticPerformanceCheck(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -6);
		
		List<Project> resultList = new ArrayList<Project>();
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
//		sqlAutomaticPerformanceCheck.setProject_nm("");
//		sqlAutomaticPerformanceCheck.setDel_yn("N");
		try {
//			resultList = sqlAutomaticPerformanceCheckService.searchProjectList(sqlAutomaticPerformanceCheck);
			resultList = commonService.projectList( null );
			
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		model.addAttribute("projectList",resultList);
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "sqlAutomaticPerformanceCheck/sqlAutomaticPerformanceCheck";
	}
	
	/* 프로젝트 리스트 action */
	@RequestMapping(value = "/searchProjectList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String searchProjectList(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.searchProjectList(sqlAutomaticPerformanceCheck);
			
			if(resultList != null && resultList.size() > sqlAutomaticPerformanceCheck.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlAutomaticPerformanceCheck.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* 수행회차 action */
	@RequestMapping(value = "/searchPerformanceCheckId", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String searchPerformanceCheckId(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.searchPerformanceCheckId(sqlAutomaticPerformanceCheck);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 수행결과 action */
	@RequestMapping(value = "/loadPerformanceCheckCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceCheckCount(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.loadPerformanceCheckCount(sqlAutomaticPerformanceCheck);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 원천 DB */
	@RequestMapping(value = "/loadOriginalDb", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadOriginalDb(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		List<SQLAutomaticPerformanceCheck> finalList = new ArrayList<SQLAutomaticPerformanceCheck>();
		SQLAutomaticPerformanceCheck temp = new SQLAutomaticPerformanceCheck();
		
		try {
			String isChoice = StringUtils.defaultString(sqlAutomaticPerformanceCheck.getIsChoice());
			String isAll = StringUtils.defaultString(sqlAutomaticPerformanceCheck.getIsAll());
			
			if (isChoice.equals("Y")) {
				temp.setOriginal_dbid("");
				temp.setOriginal_db_name("선택");
			} else if (isAll.equals("Y")) {
				temp.setOriginal_dbid("");
				temp.setOriginal_db_name("전체");
			}
			
			resultList = sqlAutomaticPerformanceCheckService.loadOriginalDb(sqlAutomaticPerformanceCheck);
			
			finalList.add(temp);
			finalList.addAll(resultList);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(finalList).toJSONObject().get("rows").toString();
	}
	
	/* Chart Data action */
	@RequestMapping(value = "/loadChartData", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadChartData(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		logger.debug(sqlAutomaticPerformanceCheck.toString());
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.loadChartData(sqlAutomaticPerformanceCheck);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 자동성능점검 리스트 action */
	@RequestMapping(value = "/loadPerformanceCheckList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceCheckList(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		int dataCount4NextBtn = 0;
		logger.debug(sqlAutomaticPerformanceCheck.toString());
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.loadPerformanceCheckList(sqlAutomaticPerformanceCheck);
			
			if(resultList != null && resultList.size() > sqlAutomaticPerformanceCheck.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlAutomaticPerformanceCheck.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* 프로젝트에 자동성능점검 수행중인 회차가 있는지 조회 */
	@RequestMapping(value = "/countExecuteTms", method = RequestMethod.POST)
	@ResponseBody
	public Result countExecuteTms(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		Result result = new Result();
		
		try{
			int count = sqlAutomaticPerformanceCheckService.countExecuteTms(sqlAutomaticPerformanceCheck);
			
			if(count > 0){
				result.setResult(false);
			} else {
				result.setResult(true);
			}
			
			result.setTxtValue(String.valueOf(count));
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* Max 수행회차 + 1 action */
	@RequestMapping(value = "/maxPerformanceCheckId", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String maxPerformanceCheckId(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.maxPerformanceCheckId(sqlAutomaticPerformanceCheck);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* SQL자동성능점검기본 INSERT */
	@RequestMapping(value = "/insertSqlAutomaticPerformanceCheck", method = RequestMethod.POST)
	@ResponseBody
	public Result insertSqlAutomaticPerformanceCheck(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		Result result = new Result();
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			
			sqlAutomaticPerformanceCheck.setPerf_check_executer_id(user_id);
			
			result = sqlAutomaticPerformanceCheckService.insertSqlAutomaticPerformanceCheck(sqlAutomaticPerformanceCheck);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 강제완료처리 UPDATE */
	@RequestMapping(value = "/forceUpdateSqlAutomaticPerformanceCheck", method = RequestMethod.POST)
	@ResponseBody
	public Result forceUpdateSqlAutomaticPerformanceCheck(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		Result result = new Result();
		
		try {
			result = sqlAutomaticPerformanceCheckService.forceUpdateSqlAutomaticPerformanceCheck(sqlAutomaticPerformanceCheck);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 엑셀 다운로드 */
	@RequestMapping(value = "/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model)
					throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.excelDownload(sqlAutomaticPerformanceCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQL_자동_성능_점검");
		model.addAttribute("sheetName", "SQL_자동_성능_점검");
		model.addAttribute("excelId", "SQL_AUTOMATIC_PERFORMANCE_CHECK");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* SQL성능분석 - SQL TEXT */
	@RequestMapping(value = "/loadExplainSqlText", method = RequestMethod.POST)
	@ResponseBody
	public Result loadExplainSqlText(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model)
			throws Exception {
		logger.debug("SQLTextAction >>\n"+ odsHistSqlText.toString());
		logger.debug("dbid["+ odsHistSqlText.getDbid() + "] sql_id[" + odsHistSqlText.getSql_id() +
				"] before_plan_hash_value[" + odsHistSqlText.getPlan_hash_value() + "]");
		
		Result result = new Result();
		
		
		try {
			result.setResult(false);
			result.setObject(sqlAutomaticPerformanceCheckService.loadExplainSqlText(odsHistSqlText));
			
			if(result.getObject() != null) {
				result.setResult(true);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	/* 튜닝요청 - SQL BIND */
	@RequestMapping(value = "/loadExplainBindValue", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExplainBindValue(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,
			Model model) throws Exception {
		logger.debug("loadExplainBindValue >>\n"+ odsHistSqlstat.toString());
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.loadExplainBindValue(odsHistSqlstat);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
//	/* SQL성능분석 - Bind Value Next action */
//	@RequestMapping(value = "/loadExplainBindValueNextAction", method = RequestMethod.POST)
//	@ResponseBody
//	public Result BindValueListNextAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat, Model model)
//			throws Exception {
//
//		Result result = new Result();
//		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
//
//		try {
//			resultList = sqlAnalysisService.bindValueList(odsHistSqlstat);
//			result.setResult(resultList.size() > 0 ? true : false);
//			result.setObject(resultList);
//		} catch (Exception ex) {
//			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
//			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
//			result.setResult(false);
//			result.setMessage(ex.getMessage());
//		}
//
//		return result;
//	}
//	
//
	/* SQL성능분석 - SQL Tree */
	@RequestMapping(value = "/loadExplainBeforePlan", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExplainBeforePlan(@ModelAttribute("explainPlanTree") ExplainPlanTree explainPlanTree, Model model)
			throws Exception {
		
		List<ExplainPlanTree> resultList = new ArrayList<ExplainPlanTree>();
		String returnValue = "";
		try {
			resultList = sqlAutomaticPerformanceCheckService.loadExplainBeforePlan(explainPlanTree);
			List<ExplainPlanTree> buildList = TreeWrite.buildExplainPlanTreeAutoPerfCheck(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return returnValue;
	}
	
	/* 성능개선상세 - 개선전실행계획 */
	@RequestMapping(value = "/loadExplainBeforePlanList", method = RequestMethod.POST, produces = "text/html; charset=utf8")
	@ResponseBody
	public String loadExplainBeforePlanList(@ModelAttribute("explainPlanTree") ExplainPlanTree explainPlanTree, Model model)
			throws Exception {
		
		List<ExplainPlanTree> resultList = new ArrayList<ExplainPlanTree>();
		String returnValue = "";
		StringBuffer strBuf  = new StringBuffer();
		try {
			resultList = sqlAutomaticPerformanceCheckService.loadExplainBeforePlan(explainPlanTree);
			
			int cnt = 0;
			int prev = 0;
			for ( ExplainPlanTree exPlan : resultList ) {
				
				strBuf.append( exPlan.getId() ).append("&nbsp;");
				
				int ID = Integer.parseInt( exPlan.getId() ); 
				
				if ( ID < 10 ) {
					strBuf.append("&nbsp;");
				}
				
				int parentId = Integer.parseInt( exPlan.getParent_id() );
				for ( int idx = 0; idx < cnt; idx++ ) {
					strBuf.append(" ");
					if ( ID-1 != parentId ) {
						if ( prev != parentId) {
							cnt = parentId+1;
						}
					}
				}
				
				logger.debug("==================================================!id : "+exPlan.getId() +" , cnt :"+ cnt +" , parentId : "+parentId);
				cnt++;
				strBuf.append( exPlan.getText() ).append(",");
				prev = parentId;
			}
			
			returnValue = strBuf.toString();
			logger.debug("resultValue =====================================> "+returnValue );
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			return getErrorJsonString(ex);
		}
		
		return returnValue;
	}
	
	/* SQL성능분석 - SQL Tree */
	@RequestMapping(value = "/loadExplainAfterSelectPlan", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExplainAfterSelectPlan(@ModelAttribute("explainPlanTree") ExplainPlanTree explainPlanTree, Model model)
			throws Exception {
		
		List<ExplainPlanTree> resultList = new ArrayList<ExplainPlanTree>();
		String returnValue = "";
		try {
			resultList = sqlAutomaticPerformanceCheckService.loadExplainAfterSelectPlan(explainPlanTree);
			List<ExplainPlanTree> buildList = TreeWrite.buildExplainPlanTreeAutoPerfCheck(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return returnValue;
	}
	
	/* SQL성능분석 - SQL Tree */
	@RequestMapping(value = "/loadExplainAfterNotSelectPlan", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExplainAfterNotSelectPlan(@ModelAttribute("explainPlanTree") ExplainPlanTree explainPlanTree, Model model)
			throws Exception {
		
		List<ExplainPlanTree> resultList = new ArrayList<ExplainPlanTree>();
		String returnValue = "";
		try {
			resultList = sqlAutomaticPerformanceCheckService.loadExplainAfterNotSelectPlan(explainPlanTree);
			List<ExplainPlanTree> buildList = TreeWrite.buildExplainPlanTreeAutoPerfCheck(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return returnValue;
	}
	
	/* SQLAutomaticPerformanceCheckTargetMng SQL 자동 성능 점검 대상 관리 */
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng", method = RequestMethod.GET)
	public String SQLAutomaticPerformanceCheckTargetMng(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -6);
		
		List<Project> resultList = new ArrayList<Project>();
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
//		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
		
//		sqlAutomaticPerformanceCheck.setProject_nm("");
//		sqlAutomaticPerformanceCheck.setDel_yn("N");
		try {
//			resultList = sqlAutomaticPerformanceCheckService.searchProjectList(sqlAutomaticPerformanceCheck);
			resultList = commonService.projectList( null );
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		model.addAttribute("projectList",resultList);
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", projectSqlIdfyCondition.getMenu_id());
		model.addAttribute("menu_nm", projectSqlIdfyCondition.getMenu_nm());
		logger.error("SQLAutomaticPerformanceCheckTargetMng Call");
		
		return "sqlAutomaticPerformanceCheck/sqlAutomaticPerformanceCheckTargetMng";
	}
	
	/* SQL 자동 성능 점검 대상 관리 action */
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng/ProjectSqlIdfyConditionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProjectSqlIdfyConditionList(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition, Model model) {
		List<ProjectSqlIdfyCondition> resultList = new ArrayList<ProjectSqlIdfyCondition>();
		
		logger.error("ProjectSqlIdfyConditionList Call");
		
		int dataCount4NextBtn = 0;
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.getProjectSqlIdfyConditionList(projectSqlIdfyCondition);
			
			if(resultList != null && resultList.size() > projectSqlIdfyCondition.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(projectSqlIdfyCondition.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		logger.debug(" dataCount4NextBtn : " + dataCount4NextBtn);
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* ODS_USERS 데이터 조회 */
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng/GetUserNameComboBox", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUserName(@ModelAttribute("odsUsers") OdsUsers odsUsers) throws Exception {
		List<OdsUsers> resultList = sqlAutomaticPerformanceCheckService.getUserNameComboBox(odsUsers);
		List<OdsUsers> userNameList = new ArrayList<OdsUsers>();
		OdsUsers userName = new OdsUsers();
//		String isChoice = StringUtils.defaultString(odsUsers.getIsChoice());
//		String isAll = StringUtils.defaultString(odsUsers.getIsAll());
//		
//		if(isChoice.equals("Y")) {
//			userName.setUser_id("");
//			userName.setUser_nm("선택");
//		} else if(isAll.equals("Y")) {
//			userName.setUser_id("");
//			userName.setUser_nm("전체");
//		}
		
		userNameList.add(userName);
		if (resultList != null) userNameList.addAll(resultList);

		return success(userNameList).toJSONObject().get("rows").toString();
	}
	/* ODS_USERS 데이터 조회 */
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng/GetProjectUserNameComboBox", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProjectUserName(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		List<OdsUsers> resultList = sqlAutomaticPerformanceCheckService.getProjectUserNameComboBox(projectSqlIdfyCondition);
		List<OdsUsers> userNameList = new ArrayList<OdsUsers>();
		OdsUsers userName = new OdsUsers();
		
		userName.setUser_id("");
		userName.setUser_nm("전체");

		userNameList.add(userName);
		
		if (resultList != null && resultList.size() > 0) {
			for(int index = 0; index < resultList.size(); index++) {
				Object tempObj = resultList.get(index);
				
				if(tempObj instanceof OdsUsers) {
					userNameList.add((OdsUsers) tempObj);
				}
			}
		}

		return success(userNameList).toJSONObject().get("rows").toString();
	}
	
	/* SQL 자동 성능 점검 대상 관리 save */
	@RequestMapping(value="/SQLAutomaticPerformanceCheckTargetMng/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveProjectSqlIdfyConditionAction(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition,  Model model) {
		Result result = new Result();
		try{
			sqlAutomaticPerformanceCheckService.saveProjectSqlIdfyCondition(projectSqlIdfyCondition);
			
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* SQL 자동 성능 점검 대상 관리 delete */
	@RequestMapping(value="/SQLAutomaticPerformanceCheckTargetMng/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result deleteProjectSqlIdfyConditionAction(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition,  Model model) {
		Result result = new Result();
		try{
			logger.debug("projectSqlIdfyCondition getProject_id : " + projectSqlIdfyCondition.getProject_id());
			sqlAutomaticPerformanceCheckService.deleteProjectSqlIdfyCondition(projectSqlIdfyCondition);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}


	/* SQL 자동 성능 점검 대상 관리 - SQL 성능점검대상 테이블 일괄등록 : Table Owner */
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng/Popup/GetTableOwner", method = RequestMethod.GET, produces="application/text; charset=utf8")
	@ResponseBody
	public String getTableOwner(@ModelAttribute("odsUsers") OdsUsers odsUsers) throws Exception {
		List<OdsUsers> resultList = new ArrayList<OdsUsers>();

		try{
			resultList = sqlAutomaticPerformanceCheckService.getTableOwner(odsUsers);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		//return success(resultList).toJSONObject().toString();	
		return success(resultList).toJSONObject().get("rows").toString();
	}
	/* SQL 자동 성능 점검 대상 관리 - SQL 성능점검대상 테이블 일괄등록  : Owner Table List*/
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng/Popup/GetSelectTable", method = RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getSelectTable(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		List<OdsUsers> resultList = new ArrayList<OdsUsers>();

		try{
			resultList = sqlAutomaticPerformanceCheckService.getSelectTable(projectSqlIdfyCondition);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 자동 성능 점검 대상 관리 - SQL 성능점검대상 테이블 일괄등록  : Owner  Table(ProjectSqlIdfyCondition) List*/
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng/Popup/GetSelectProjectSqlIdfyConditionTable", method = RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getSelectProjectSqlIdfyConditionTable(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		List<OdsUsers> resultList = new ArrayList<OdsUsers>();
		logger.debug("getSelectProjectSqlIdfyConditionTable Caqll");
		try{
			projectSqlIdfyCondition.setSql_idfy_cond_type_cd("2");
			resultList = sqlAutomaticPerformanceCheckService.getSelectProjectSqlIdfyConditionTable(projectSqlIdfyCondition);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 자동 성능 점검 대상 관리 - SQL 성능점검대상 테이블 일괄등록 save */
	@RequestMapping(value="/SQLAutomaticPerformanceCheckTargetMng/Popup/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveProjectSqlIdfyConditionPopupAction(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition,  Model model) {
		Result result = new Result();
		try{
			logger.debug("projectSqlIdfyCondition Popup getProject_id : " + projectSqlIdfyCondition.getProject_id());
			sqlAutomaticPerformanceCheckService.saveProjectSqlIdfyConditionPopup(projectSqlIdfyCondition);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* SQL 자동 성능 점검 대상 관리 - SQL 성능점검대상 테이블 일괄등록  : Owner Table Check */
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng/Popup/GetProjectSqlIdfyConditionCheck", method = RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getProjectSqlIdfyConditionCheck(@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition) throws Exception {
		List<ProjectSqlIdfyCondition> resultList = new ArrayList<ProjectSqlIdfyCondition>();

		try{
			resultList = sqlAutomaticPerformanceCheckService.getProjectSqlIdfyConditionCheck(projectSqlIdfyCondition);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 자동 성능 점검 대상 관리 - 엑셀 다운로드 */
	@RequestMapping(value = "/SQLAutomaticPerformanceCheckTargetMng/ExcelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelDownloadProjectSqlIdfyCondition(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("projectSqlIdfyCondition") ProjectSqlIdfyCondition projectSqlIdfyCondition, Model model)
					throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.excelDownloadProjectSqlIdfyCondition(projectSqlIdfyCondition);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQL_자동_성능_점검_대상_관리");
		model.addAttribute("sheetName", "SQL_자동_성능_점검_대상_관리");
		model.addAttribute("excelId", "SQL_AUTOMATIC_PERFORMANCE_CHECK_TARGET_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* getAuthoritySQL action */
	@RequestMapping(value = "/getAuthoritySQL", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuthoritySQL(@ModelAttribute("authoritySQL") AuthoritySQL authoritySQL, Model model) {
		List<AuthoritySQL> resultList = new ArrayList<AuthoritySQL>();
		int dataCount4NextBtn = 0;
		logger.debug(authoritySQL.toString());
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.getAuthoritySQL(authoritySQL);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* getRoundingInfo action */
	@RequestMapping(value = "/getRoundingInfo", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getRoundingInfo(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.getRoundingInfo(sqlAutomaticPerformanceCheck);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
}
