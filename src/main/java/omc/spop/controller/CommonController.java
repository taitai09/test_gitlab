package omc.spop.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.AccPathExec;
import omc.spop.model.Auth;
import omc.spop.model.Cd;
import omc.spop.model.Database;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.DbioExplainExec;
import omc.spop.model.DbioLoadFile;
import omc.spop.model.GrpCd;
import omc.spop.model.Instance;
import omc.spop.model.Menu;
import omc.spop.model.MyMenu;
import omc.spop.model.OdsHistSnapshot;
import omc.spop.model.OdsUsers;
import omc.spop.model.PlanCompareResult;
import omc.spop.model.Project;
import omc.spop.model.RecvNote;
import omc.spop.model.Result;
import omc.spop.model.SQLStandards;
import omc.spop.model.SqlStatByPlan;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.UserTables;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;
import omc.spop.service.CommonService;
import omc.spop.utils.TreeWrite;

@Controller
@RequestMapping(value = "/Common")
public class CommonController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	private CommonService commonService;
	
	private static final String ROUTE ="/excelUploadForm/";
	
	/* 마이 메뉴 설정 */
	@RequestMapping(value = "/MyMenuSet", method = RequestMethod.GET)
	public String MyMenuSet(@RequestParam(required = true) Map<String, Object> param, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("user_id", user_id);
		
		return "common/myMenuSet";
	}
	
	/* 마이 메뉴 설정 */
	@RequestMapping(value = "/MyMenuSetting", method = RequestMethod.GET)
	public String MyMenuSetting(@RequestParam(required = true) Map<String, Object> param, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("menu_nm", "My Menu 설정");
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("user_id", user_id);
		
		return "common/myMenuSet";
	}
	
	/* MY권한메뉴 조회 */
	@RequestMapping(value = "/UserAuthMenuList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String UserAuthMenuList(@ModelAttribute("myMenu") MyMenu myMenu, Model model) {
		String auth_grp_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		myMenu.setAuth_grp_id(auth_grp_id);
		
		List<MyMenu> resultList = new ArrayList<MyMenu>();
		String returnValue = "";
		
		try {
			resultList = commonService.getUserAuthMenuList(myMenu);
			List<MyMenu> buildList = TreeWrite.buildMyMenuTree2(resultList, "0");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
			
			returnValue = jsonArray.toString();
			returnValue = returnValue.replaceAll("\"checked\":\"false\"", "\"checked\":false");
			returnValue = returnValue.replaceAll("\"checked\":\"true\"", "\"checked\":true");
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return returnValue;
	}
	
	/* MY메뉴 조회 */
	@RequestMapping(value = "/MyMenuList", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/text; charset=utf8")
	@ResponseBody
	public String MyMenuList(@ModelAttribute("myMenu") MyMenu myMenu, Model model) {
		List<MyMenu> resultList = new ArrayList<MyMenu>();
		String returnValue = "";
		
		try {
			resultList = commonService.getMyMenuList(myMenu);
			List<MyMenu> buildList = TreeWrite.buildMyMenuTree2(resultList, "0");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
			
			returnValue = jsonArray.toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		return returnValue;
	}
	
	/* MY메뉴 조회 */
	@RequestMapping(value = "/MyMenu", method = RequestMethod.GET)
	@ResponseBody
	public Result MyMenu(@ModelAttribute("myMenu") MyMenu myMenu) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd =  SessionManager.getLoginSession().getUsers().getAuth_cd();
		Result result = new Result();
		List<MyMenu> resultList = new ArrayList<MyMenu>();
		
		try {
			myMenu.setAuth_cd(auth_cd);
			
			resultList = commonService.getMyMenuList(myMenu);
			result.setResult(resultList.size() > 0 ? true : false);
			List<MyMenu> buildList = TreeWrite.buildMyMenuTree(resultList, "0");
			
			result.setObject(buildList);
			result.setMessage("My메뉴 조회 완료");
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 메뉴 조회 */
	@RequestMapping(value = "/Menu", method = RequestMethod.GET)
	@ResponseBody
	public Result Menu(@ModelAttribute("menu") Menu menu) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		Result result = new Result();
		List<Menu> resultList = new ArrayList<Menu>();
		
		try {
			resultList = commonService.getMenuList(menu);
			result.setResult(resultList.size() > 0 ? true : false);
			List<Menu> buildList = TreeWrite.buildMenuTree(resultList, menu.getMenu_id());
			
			result.setObject(buildList);
			result.setMessage("메뉴 조회 완료");
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 상단 작업현황 조회 */
	@RequestMapping(value = "/WorkStatusCount", method = RequestMethod.GET)
	@ResponseBody
	public Result WorkStatusCount(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql) throws Exception {
		Result result = new Result();
		
		try {
			tuningTargetSql = commonService.getWorkStatusCount(tuningTargetSql);
			result.setResult(true);
			result.setObject(tuningTargetSql);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 상단 쪽지카운트 조회 */
	@RequestMapping(value = "/MessageCount", method = RequestMethod.GET)
	@ResponseBody
	public Result MessageCount(@ModelAttribute("recvNote") RecvNote recvNote) throws Exception {
		Result result = new Result();
		
		try {
			recvNote = commonService.getMessageCount(recvNote);
			result.setResult(true);
			result.setObject(recvNote);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 코드그룹 조회 */
	@RequestMapping(value = "/getCodeGroup", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCodeGroup(@ModelAttribute("grpCd") GrpCd grpCd) throws Exception {
		List<GrpCd> commonList = commonService.commonCodeGroupList(grpCd);
		
		return success(commonList).toJSONObject().get("rows").toString();
	}
	
	/* 공통코드 조회 */
	@RequestMapping(value = "/getCommonCode", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCommonCode(@ModelAttribute("cd") Cd cd) throws Exception {
		String isAll = StringUtils.defaultString(cd.getIsAll());
		String isChoice = StringUtils.defaultString(cd.getIsChoice());
		List<Cd> commonList = new ArrayList<Cd>();
		
		Cd temp = new Cd();
		if (isAll.equals("Y")) {
			temp.setCd("");
			temp.setCd_nm("전체");
			commonList.add(temp);
		} else if (isChoice.equals("Y")) {
			temp.setCd("");
			temp.setCd_nm("선택");
			commonList.add(temp);
		}
		
		try {
			List<Cd> commonList2 = commonService.commonCodeList(cd);
			commonList.addAll(commonList2);
			
		} catch (Exception ex) {
			logger.error("Common Error ==> ", ex);
			return getErrorJsonString(ex);
		}
		
		return success(commonList).toJSONObject().get("rows").toString();
	}
	
	/* 공통코드(REF_VL_2) 조회 */
	@RequestMapping(value = "/getCommonCodeRef2", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCommonCodeRef2(@ModelAttribute("cd") Cd cd) throws Exception {
		String isAll = StringUtils.defaultString(cd.getIsAll());
		String isChoice = StringUtils.defaultString(cd.getIsChoice());
		List<Cd> commonList = new ArrayList<Cd>();
		
		Cd temp = new Cd();
		if (isAll.equals("Y")) {
			temp.setCd("");
			temp.setCd_nm("전체");
			commonList.add(temp);
		} else if (isChoice.equals("Y")) {
			temp.setCd("");
			temp.setCd_nm("선택");
			commonList.add(temp);
		}
		
		try {
			List<Cd> commonList2 = commonService.commonRef2CodeList(cd);
			commonList.addAll(commonList2);
			
		} catch (Exception ex) {
			logger.error("Common Error ==> ", ex);
			return getErrorJsonString(ex);
		}
		
		return success(commonList).toJSONObject().get("rows").toString();
	}
	
	/* 데이터베이스 조회 */
	@RequestMapping(value = "/getDatabase", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDatabase(@ModelAttribute("database") Database database) throws Exception {
		String isAll = StringUtils.defaultString(database.getIsAll());
		String isChoice = StringUtils.defaultString(database.getIsChoice());
		String isBase = StringUtils.defaultString(database.getIsBase());
		List<Database> dbList = new ArrayList<Database>();
		List<Database> databaseList = commonService.databaseList(database);
		
		if(isAll.equals("") == false) {
			Database db = new Database();
			db.setDbid("");
			if (isAll.equals("Y")) {
				db.setDb_name("전체");
			} else if (isChoice.equals("Y")) {
				db.setDb_name("선택");
			} else if (isBase.equals("Y")) {
				db.setDb_name("기본");
			} else {
				db.setDb_name("");
			}
			dbList.add(db);
		}
		
		dbList.addAll(databaseList);
		
		return success(dbList).toJSONObject().get("rows").toString();
	}
	
	/* 테이블 조회 */
	@RequestMapping(value = "/getTableList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getTableList(@ModelAttribute("") UserTables userTables) throws Exception {
		List<UserTables> tableList = commonService.getTableList(userTables);
		
		return success(tableList).toJSONObject().get("rows").toString();
	}
	
	/* 마스터 인스턴스 조회 */
	@RequestMapping(value = "/getMasterInstance", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getMasterInstance(@ModelAttribute("instance") Instance instance) throws Exception {
		List<Instance> instanceList = commonService.masterInstanceList(instance);
		
		return success(instanceList).toJSONObject().get("rows").toString();
	}
	
	/* 에이젼트 인스턴스 조회 */
	@RequestMapping(value = "/getAgentInstance", method = RequestMethod.GET)
	@ResponseBody
	public String getAgentInstance(@ModelAttribute("instance") Instance instance) throws Exception {
		List<Instance> instanceList = null;
		try {
			// 통신을 통해 collect에서 가져오던 instance를 master db에서 가져오는 것으로 변경
			instanceList = commonService.masterInstanceList(instance);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			throw ex;
		}
		return success(instanceList).toJSONObject().get("rows").toString();
	}
	
	/* ODS_USERS 데이터 조회 */
	@RequestMapping(value = "/getUserName", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUserName(@ModelAttribute("odsUsers") OdsUsers odsUsers) throws Exception {
		String dbid = odsUsers.getDbid();
		List<OdsUsers> resultList = new ArrayList<OdsUsers>();
		if(!dbid.equals("")) {
			resultList = commonService.getUserName(odsUsers);
		}
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* ajaxcall = post */
	@RequestMapping(value = "/getAuthUserName", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuthUserName(@ModelAttribute("users") Users users) throws Exception {
		List<Users> resultList = commonService.getAuthUserName(users);
		int dataCount4NextBtn = 0;
		
		try {
			if (resultList != null && resultList.size() > users.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(users.getPagePerCount());
				/* 리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함 */
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* 테이블명 데이터 조회 */
	@RequestMapping(value = "/getTableName", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getTableName(@ModelAttribute("odsUsers") OdsUsers odsUsers) throws Exception {
		List<OdsUsers> resultList = commonService.getTableName(odsUsers);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}

	/* Work Job 조회 */
	@RequestMapping(value = "/getWrkJob", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getWrkJob(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd) throws Exception {
		String isAll = StringUtils.defaultString(wrkJobCd.getIsAll());
		String isChoice = StringUtils.defaultString(wrkJobCd.getIsChoice());
		
		WrkJobCd workjob = new WrkJobCd();
		List<WrkJobCd> wjList = new ArrayList<WrkJobCd>();
		List<WrkJobCd> workJobList = commonService.wrkJobList(wrkJobCd);
		
		if (isAll.equals("Y")) {
			workjob.setId("");
			workjob.setWrkjob_cd("");
			workjob.setWrkjob_cd_nm("전체");
		} else if (isChoice.equals("Y")) {
			workjob.setId("");
			workjob.setParent_id("");
			workjob.setText("선택");
		} else {
			workjob.setId("");
			workjob.setParent_id("");
			workjob.setText("");
		}
		wjList.add(workjob);
		wjList.addAll(workJobList);
		
		return success(wjList).toJSONObject().get("rows").toString();
	}
	
	/* Work Job 최상위 조회 */
	/* 기준이 되는 "Work Job 조회"에서 조건식(WHERE PREF_ID = '22001')을 제외하여 시스템 유형에 관계없이 가져오도록 한다. */
	@RequestMapping(value = "/getWrkJobTopLevel", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getWrkJobTopLevel(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd) throws Exception {
		String isAll = StringUtils.defaultString(wrkJobCd.getIsAll());
		String isChoice = StringUtils.defaultString(wrkJobCd.getIsChoice());
		List<WrkJobCd> wjList = new ArrayList<WrkJobCd>();
		List<WrkJobCd> workjobList = commonService.wrkJobTopLevel(wrkJobCd);
		
		if(isAll.equals("") == false) {
			WrkJobCd workjob = new WrkJobCd();
			workjob.setDbid("");
			if (isAll.equals("Y")) {
				workjob.setId("");
				workjob.setParent_id("");
				workjob.setText("전체");
			} else if (isChoice.equals("Y")) {
				workjob.setId("");
				workjob.setParent_id("");
				workjob.setText("선택");
			} else {
				workjob.setId("");
				workjob.setParent_id("");
				workjob.setText("");
			}
			wjList.add(workjob);
		}
		
		wjList.addAll(workjobList);
		
		return success(wjList).toJSONObject().get("rows").toString();
	}
	
	/* Work Job - Dev권한 조회 */
	@RequestMapping(value = "/getWrkJobDev", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getWrkJobDev(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd) throws Exception {
		List<WrkJobCd> workJobList = commonService.wrkJobDevList(wrkJobCd);
		
		return success(workJobList).toJSONObject().get("rows").toString();
	}
	
	/* 튜닝담당자업무 조회 */
	@RequestMapping(value = "/getWrkJobCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getWrkJobCd(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd) throws Exception {
		String returnValue = "";
		List<WrkJobCd> resultList = commonService.getWrkJobCd(wrkJobCd);
		List<WrkJobCd> buildList = TreeWrite.buildWrkJobTree(resultList, "-1");
		List<WrkJobCd> returnList = new ArrayList<WrkJobCd>();
		WrkJobCd wrkjobCd_temp = new WrkJobCd();
		String isChoice = StringUtils.defaultString(wrkJobCd.getIsChoice());
		String isAll = StringUtils.defaultString(wrkJobCd.getIsAll());
		
		if(isChoice.equals("Y")){
			wrkjobCd_temp.setId("");
			wrkjobCd_temp.setParent_id("");
			wrkjobCd_temp.setText("선택");
			returnList.add(wrkjobCd_temp);
		}else if(isChoice.equals("N")){
			wrkjobCd_temp.setId("");
			wrkjobCd_temp.setParent_id("");
			wrkjobCd_temp.setText("없음");
			returnList.add(wrkjobCd_temp);
		}else if(isAll.equals("Y")){
			wrkjobCd_temp.setId("");
			wrkjobCd_temp.setParent_id("");
			wrkjobCd_temp.setText("전체");
			returnList.add(wrkjobCd_temp);
		}
		
		returnList.addAll(buildList);
		JSONArray jsonArray = JSONArray.fromObject(returnList);
		
		returnValue = jsonArray.toString();
		
		return returnValue;
	}
	
	/* 담당 업무 조회 */
	@RequestMapping(value = "/getUserWrkJobCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUserWrkJobCd(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		String returnValue = "";
		List<WrkJobCd> resultList = Collections.emptyList();
		WrkJobCd wrkjobCd_temp = new WrkJobCd();
		String isChoice = "";
		String isAll = "";
		
		try {
			sqlStandards.setUser_id(SessionManager.getLoginSession().getUsers().getUser_id());
			
			resultList = commonService.getUserWrkJobCd(sqlStandards);
			
			isChoice = StringUtils.defaultString(sqlStandards.getIsChoice());
			isAll = StringUtils.defaultString(sqlStandards.getIsAll());
			
			if("Y".equals(isChoice)){
				wrkjobCd_temp.setId("");
				wrkjobCd_temp.setText("선택");
				resultList.add(0, wrkjobCd_temp);
				
			}else if("N".equals(isChoice)){
				wrkjobCd_temp.setId("");
				wrkjobCd_temp.setText("없음");
				resultList.add(0, wrkjobCd_temp);
				
			}else if("Y".equals(isAll)){
				wrkjobCd_temp.setId("");
				wrkjobCd_temp.setText("전체");
				resultList.add(0, wrkjobCd_temp);
			}
			
			JSONArray jsonArray = JSONArray.fromObject(resultList);
			returnValue = jsonArray.toString();
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnValue;
	}
	
	/* ACC_PATH_EXEC 데이터 조회 */
	@RequestMapping(value = "/getAccPathExec", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAccPathExec(@ModelAttribute("accPathExec") AccPathExec accPathExec) throws Exception {
		List<AccPathExec> resultList = commonService.getAccPathExec(accPathExec);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	/* ACC_PATH_EXEC 데이터 조회 */
	@RequestMapping(value = "/getAccPathExecAddSec", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAccPathExecAddSec(@ModelAttribute("accPathExec") AccPathExec accPathExec) throws Exception {
		List<AccPathExec> resultList = commonService.getAccPathExecAddSec(accPathExec);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* DBIO LOAD File 데이터 조회 */
	@RequestMapping(value = "/getDBIOLoadFile", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDBIOLoadFile(@ModelAttribute("dbioLoadFile") DbioLoadFile dbioLoadFile) throws Exception {
		List<DbioLoadFile> resultList = commonService.getDBIOLoadFile(dbioLoadFile);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* DBIO LOAD File 데이터정보 조회 */
	@RequestMapping(value = "/getDBIOLoadFileInfo", method = RequestMethod.GET)
	@ResponseBody
	public Result getDBIOLoadFileInfo(@ModelAttribute("dbioLoadFile") DbioLoadFile dbioLoadFile) throws Exception {
		Result result = new Result();
		
		try {
			DbioLoadFile resultDbioLoadFile = commonService.getDBIOLoadFileInfo(dbioLoadFile);
			
			result.setResult(true);
			result.setObject(resultDbioLoadFile);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* DBIO 수행회차 데이터 조회 */
	@RequestMapping(value = "/getDBIOExplainExec", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDBIOExplainExec(@ModelAttribute("dbioExplainExec") DbioExplainExec dbioExplainExec)
			throws Exception {
		List<DbioExplainExec> resultList = commonService.getDBIOExplainExec(dbioExplainExec);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* DBIO 수행회차 데이터 조회 */
	@RequestMapping(value = "/getDBIOExplainExecInfo", method = RequestMethod.GET)
	@ResponseBody
	public Result getDBIOExplainExecInfo(@ModelAttribute("dbioExplainExec") DbioExplainExec dbioExplainExec)
			throws Exception {
		Result result = new Result();
		try {
			DbioExplainExec resultDbioExplainExec = commonService.getDBIOExplainExecInfo(dbioExplainExec);
			
			result.setResult(true);
			result.setObject(resultDbioExplainExec);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 튜닝담당자 조회 */
	@RequestMapping(value = "/getTuner", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getTuner(@ModelAttribute("databaseTuner") DatabaseTuner databaseTuner) throws Exception {
		List<DatabaseTuner> resultList = commonService.getTuner(databaseTuner);
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 튜닝담당자 조회 */
	@RequestMapping(value = "/getTunerCondition", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getTunerCondition(@ModelAttribute("databaseTuner") DatabaseTuner databaseTuner) throws Exception {
		List<DatabaseTuner> outerList = new ArrayList<DatabaseTuner>();
		DatabaseTuner dbTuner = new DatabaseTuner();
		List<DatabaseTuner> resultList = new ArrayList<DatabaseTuner>();
		String isAll = StringUtils.defaultString(databaseTuner.getIsAll());
		String isChoice = StringUtils.defaultString(databaseTuner.getIsChoice());
		
		dbTuner.setTuner_id("");
		
		if(databaseTuner.getDbid().equals("")) {
			if (isAll.equals("Y")) {
				dbTuner.setTuner_nm("전체");
			} else if (isChoice.equals("Y")) {
				dbTuner.setTuner_nm("선택");
			}
			
			outerList.add(dbTuner);
		} else {
			resultList = commonService.getTuner(databaseTuner);
			
			if (isAll.equals("Y")) {
				dbTuner.setTuner_nm("전체");
			} else if (isChoice.equals("Y")) {
				dbTuner.setTuner_nm("선택");
			} else {
				dbTuner.setTuner_nm("");
			}
			
			outerList.add(dbTuner);
			outerList.addAll(resultList);
		}
		
		return success(outerList).toJSONObject().get("rows").toString();
	}
	
	/* 튜닝담당자 조회 */
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUsers(@ModelAttribute("users") Users users) throws Exception {
		List<Users> resultList = new ArrayList<Users>();
		
		try {
			resultList = commonService.getUsers(users);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 권한 조회 */
	@RequestMapping(value = "/getAuth", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuth(@ModelAttribute("auth") Auth auth) throws Exception {
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		
		List<Auth> resultList = new ArrayList<Auth>();
		List<Auth> tempList = new ArrayList<Auth>();
		Auth auth_temp = new Auth();
		String isChoice = StringUtils.defaultString(auth.getIsChoice());
		
		if(isChoice.equals("Y")){
			auth_temp.setAuth_id("");
			auth_temp.setAuth_nm("선택");
			resultList.add(auth_temp);
		}
		
		auth.setUser_auth_id(user_auth_id);
		
		try {
			tempList = commonService.getAuth(auth);
			resultList.addAll(tempList);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* SNAP ID 조회 팝업 */
	@RequestMapping(value = "/SnapIdList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SnapIdList(@ModelAttribute("odsHistSnapshot") OdsHistSnapshot odsHistSnapshot, Model model) {
		List<OdsHistSnapshot> resultList = new ArrayList<OdsHistSnapshot>();
		
		try {
			resultList = commonService.snapIdList(odsHistSnapshot);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* SQL PROFILE 적용 */
	@RequestMapping(value = "/SQLProfileApply", method = RequestMethod.POST)
	@ResponseBody
	public Result SQLProfileApply(@ModelAttribute("sqlStatByPlan") SqlStatByPlan sqlStatByPlan, Model model) {
		Result result = new Result();
		
		try {
			commonService.sqlProfileApply(sqlStatByPlan);
			result.setResult(true);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/down", method = RequestMethod.GET)
	public ModelAndView fileDown(@RequestParam("fileName") String fileName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		File downloadFile = new File(rootPath + "/upload/authnum/" + fileName);
		
		if (!downloadFile.canRead()) {
			throw new Exception("File can't read(파일을 찾을 수 없습니다)");
		}
		
		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}
	
	/* 마이 메뉴 설정 */
	@RequestMapping(value = "/getMethodName", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void getMethodName(@RequestParam(required = true) Map<String, Object> param, Model model,
			HttpServletRequest request) {
		try {
			String className = (String) param.get("className");
			Class c = Class.forName(className);
			Method m[] = c.getDeclaredMethods();
			
			for (int i = 0; i < m.length; i++)
				logger.trace(m[i].toString());
			
		} catch (Throwable e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, e );
		}
	}
	
	/* 마이 메뉴 저장 */
	@RequestMapping(value = "/insertMyMenuAction", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Result insertMyMenuAction(@ModelAttribute("myMenu") MyMenu myMenu, Model model) {
		Result result = new Result();
		int insertRow = 0;
		String menu_id_array = myMenu.getMenu_id();
		StringTokenizer st = new StringTokenizer(menu_id_array, "^");
		String menu_id = "";
		try {
			commonService.deleteAllMyMenuAction(myMenu);
			
			while (st.hasMoreTokens()) {
				menu_id = st.nextToken();
				logger.debug("menu_id===>" + menu_id);
				myMenu.setMenu_id(menu_id);
				
				int childMenuCnt = commonService.getChildMenuCnt(myMenu);
				if (childMenuCnt <= 0) {
					insertRow = commonService.insertMyMenuAction(myMenu);
				} else {
					commonService.deleteMyMenuAction(myMenu);
				}
			}
			
			result.setResult(insertRow > 0 ? true : false);
			result.setMessage("마이메뉴 저장에 성공하였습니다.");
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage("마이메뉴 저장에 실패하였습니다.");
		}
		
		return result;
	}
	
	/* 마이 메뉴 삭제 */
	@RequestMapping(value = "/deleteMyMenuAction", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Result deleteMyMenuAction(@ModelAttribute("myMenu") MyMenu myMenu, Model model) {
		Result result = new Result();
		int deleteRow = 0;
		
		try {
			deleteRow = commonService.deleteMyMenuAction(myMenu);
			
			result.setResult(deleteRow > 0 ? true : false);
			result.setMessage("마이메뉴 delete 성공");
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult(false);
			result.setMessage("마이메뉴 delete 실패");
		}
		
		return result;
	}
	
	@RequestMapping(value="/ExcelFormDownload/{name}", method = {RequestMethod.POST})
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("name") String excelName) throws IOException {
		excelName = ROUTE + excelName +".xlsx";
		
		File file = null;
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		logger.debug("current directory:"+classloader.getResource(".").getPath());
		file = new File(classloader.getResource(excelName).getFile());
		
		if(!file.exists()){
			String errorMessage = "파일을 찾을 수 없습니다.";
			logger.error(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		
		String mimeType= URLConnection.guessContentTypeFromName(file.getName());
		if(mimeType==null){
			logger.debug("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}
		
		logger.debug("mimetype : "+mimeType);
		response.setContentType(mimeType);
		
		String contentDisposition = contentDisposition(request, file.getName());
		
		/* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
		 * while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
		//response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
		response.setHeader("Content-Disposition", contentDisposition);
		
		/* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
		//response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		
		response.setContentLength((int)file.length());
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		
		//Copy bytes from source to destination(outputstream in this example), closes both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}
	
	private String contentDisposition(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
		String header = getBrowser(request.getHeader("User-Agent"));
		String docName = "";
		
		if (header.equalsIgnoreCase("MSIE")) {
			docName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
			
		} else if (header.equalsIgnoreCase("Firefox") || header.equalsIgnoreCase("Opera") || header.equalsIgnoreCase("Chrome")) {
			docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		return "inline;filename=\"" + docName + "\"";
	}
	
	private String getBrowser(String header) {
		logger.debug("header:" + header);
		if (header.contains("MSIE") || header.contains("Edge")) {
			return "MSIE";
		} else if(header.contains("Chrome")) {
			return "Chrome";
		} else if(header.contains("Opera")) {
			return "Opera";
		} else if(header.contains("Firefox")) {
			return "Firefox";
		}
		
		return "MSIE";
	}
	/* 데이터베이스 조회 */
	@RequestMapping(value = "/getInstance", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getInstance(@ModelAttribute("instance") Instance instance) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String isAll = StringUtils.defaultString(instance.getIsAll());
		String isChoice = StringUtils.defaultString(instance.getIsChoice());
		String isBase = StringUtils.defaultString(instance.getIsBase());
		List<Instance> dbList = new ArrayList<Instance>();
		
		instance.setUser_id(user_id);
		List<Instance> instanceList = commonService.instanceList(instance);
		Instance ins = new Instance();
		
		ins.setInst_id("");
		if (isAll.equals("Y")) {
			ins.setInst_nm("전체");
		} else if (isChoice.equals("Y")) {
			ins.setInst_nm("선택");
		} else if (isBase.equals("Y")) {
			ins.setInst_nm("기본");
		} else {
			ins.setInst_nm("");
		}
		dbList.add(ins);
		dbList.addAll(instanceList);
		
		return success(dbList).toJSONObject().get("rows").toString();
	}
	
	/* 프로젝트 조회 */
	@RequestMapping(value = "/getProject", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProject(@ModelAttribute("project") Project project) throws Exception {
		String isAll = StringUtils.defaultString(project.getIsAll());
		String isChoice = StringUtils.defaultString(project.getIsChoice());
		String isNotApplicable = StringUtils.defaultString(project.getIsNotApplicable());
		List<Project> outerList = new ArrayList<Project>();
		List<Project> projectList = commonService.projectList(project);
		Project prj = new Project();
		
		prj.setProject_id("");
		
		if (isAll.equals("Y")) {
			prj.setProject_nm("전체");
		} else if (isChoice.equals("Y")) {
			prj.setProject_nm("선택");
		} else if (isNotApplicable.equals("Y")) {
			prj.setProject_nm("해당없음");
		} else {
			prj.setProject_nm("");
		}
		
		outerList.add(prj);
		outerList.addAll(projectList);
		
		return success(outerList).toJSONObject().get("rows").toString();
	}
	
	/* plan 비교 기능 */
	@RequestMapping(value = "/getPlanCompareResult", method = RequestMethod.GET)
	@ResponseBody
	public String getPlanCompareResult(@ModelAttribute("planCompareResult") PlanCompareResult planCompareResult) {
		List<PlanCompareResult> compareResult = Collections.emptyList();
		
		try {
			compareResult = commonService.getPlanCompareResult(planCompareResult);
			
		} catch (SQLException e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, e );
			
			return getErrorJsonString( e );
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, e );
			
			return getErrorJsonString( e );
		}
		
		return success( compareResult ).toJSONObject().get("rows").toString();
	}
	
	/* plan 비교 기능 */
	@RequestMapping(value = "/getAsisPlanCompareResult", method = RequestMethod.GET)
	@ResponseBody
	public String getAsisPlanCompareResult(@ModelAttribute("planCompareResult") PlanCompareResult planCompareResult) {
		List<PlanCompareResult> compareResult = Collections.emptyList();
		
		try {
			compareResult = commonService.getAsisPlanCompareResult(planCompareResult);
			
		} catch (SQLException e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, e );
			
			return getErrorJsonString( e );
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, e );
			
			return getErrorJsonString( e );
		}
		
		return success( compareResult ).toJSONObject().get("rows").toString();
	}
}