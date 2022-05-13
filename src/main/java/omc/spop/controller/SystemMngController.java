package omc.spop.controller;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.json.simple.JSONObject;
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
import omc.spop.model.Auth;
import omc.spop.model.Database;
import omc.spop.model.Menu;
import omc.spop.model.Project;
import omc.spop.model.Result;
import omc.spop.model.Users;
import omc.spop.service.CommonService;
import omc.spop.service.SystemMngService;
import omc.spop.utils.SHA256Util;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class SystemMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(SystemMngController.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private SystemMngService systemMngService;

	@RequestMapping(value = "/MenuMng", method = RequestMethod.GET)
	public String MenuMng(@ModelAttribute("auth") Auth auth, Model model) throws Exception {

		List<Map<String, String>> authNmList = commonService.getAuthNmMapList(auth);

		model.addAttribute("authNmList", authNmList);
		model.addAttribute("menu_id", auth.getMenu_id() );
		model.addAttribute("menu_nm", auth.getMenu_nm() );
		
		return "systemManage/menu";
	}

	/* 업무 관리 - 업무 Tree */
	@RequestMapping(value = "/MenuTree", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String MenuTree(@ModelAttribute("menu") Menu menu, Model model) {
		List<Menu> resultList = new ArrayList<Menu>();
		String returnValue = "";

		try {
			resultList = systemMngService.menuTree(menu);
				
//			List<Menu> buildList = TreeWrite.buildMenu(resultList, "-1","Y", 0);
			List<Menu> buildList = TreeWrite.buildMenu(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);

			returnValue = jsonArray.toString();

			System.out.println("returnValue======"+returnValue);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return returnValue;
	}

	@RequestMapping(value = "/getMenuList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getMenuList(@ModelAttribute("menu") Menu menu) throws Exception {
		String returnValue = "";
		Menu menu_tmp = new Menu();
		List<Menu> returnList = new ArrayList<Menu>();
		try {
			List<Menu> resultList = systemMngService.getMenuList(menu);
			List<Menu> buildList = TreeWrite.buildMenu(resultList, "-1");
			menu_tmp.setText("없음");
			menu_tmp.setId("");
			returnList.add(menu_tmp);
			returnList.addAll(buildList);
//			JSONArray jsonArray = JSONArray.fromObject(buildList);
			JSONArray jsonArray = JSONArray.fromObject(returnList);
			returnValue = jsonArray.toString();
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		return returnValue;
	}

	@RequestMapping(value = "/getAuthNm", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuthNm(@ModelAttribute("menu") Menu menu) throws Exception {
		String returnValue = "";
		List<Integer> resultList = null;
		try {
			resultList = systemMngService.getAuthNm(menu);
//			logger.debug("resultList:" + resultList);
			JSONArray jsonArray = JSONArray.fromObject(resultList);
			returnValue = jsonArray.toString();
			System.out.println("getAuthNm Method's returnValue : " + returnValue);
			logger.debug("returnValue : " + returnValue);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		// JSONObject jso = new JSONObject();
		// jso.put("result", resultList);
		System.out.println("확인 :::: " + returnValue);

		return returnValue;
	}

	@RequestMapping(value = "/Menu/Save", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Result SaveMenuInfo(@ModelAttribute("menu") Menu menu,
			@RequestParam(value = "auth_id", required = false, defaultValue = "") List<String> auth_id_list,
			Model model) throws Exception {
		Result result = new Result();
		try {
			int check = systemMngService.saveMenuInfo(menu, auth_id_list);
			result.setResult(true);
			result.setMessage(menu.getCrud_flag().equals("C") ? "저장 되었습니다." : "수정 되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(ex.getMessage());
			result.setResult(false);
		}
		return result;
	}
	
	@RequestMapping(value = "/Menu/MultiSave", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Result SaveMultiMenuInfo(@ModelAttribute("menu") Menu menu,
			@RequestParam(value = "auth_id", required = false, defaultValue = "") List<String> auth_id_list,
			Model model) throws Exception {
		Result result = new Result();
		try {
			int check = systemMngService.saveMultiMenuInfo(menu, auth_id_list);
			result.setResult(true);
			result.setMessage("수정 되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(ex.getMessage());
			result.setResult(false);
		}
		return result;
	}
	
	@RequestMapping(value = "/Menu/Delete", method = RequestMethod.POST)
	@ResponseBody
	public Result DeleteMenuInfo(@ModelAttribute("menu") Menu menu, Model model) throws Exception {

		Result result = new Result();
		boolean menuIsEmpty = false;  //

		try {
				menuIsEmpty = systemMngService.menuIsEmpty(menu);
			if(!menuIsEmpty){  // empty가 아닐경우
				result.setMessage("하위 메뉴가 있을시에는 삭제가 불가능합니다.");
				result.setResult(false);
				result.setResultCount(0);
				return result;
			}
			
				int check = 0;
				check += systemMngService.deleteMenuInfo(menu);
				if (check > 0) {
					check += systemMngService.deleteMenuAuth(menu);
				}
				result.setResult(true);
				result.setMessage("삭제 되었습니다.");

		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(methodName + "예외발생 ==> " + ex.getMessage());
			result.setResultCount(0);
			result.setResult(false);
		}
		return result;
	}
	
	@RequestMapping(value = "/Menu/MultiDelete", method = RequestMethod.POST)
	@ResponseBody
	public Result MultiDeleteMenuInfo(@ModelAttribute("menu") Menu menu, Model model) throws Exception {

		Result result = new Result();
		boolean menuIsEmpty = false;  //
		int check = 0;
		
		try {
		
			for(int i = 0; i < menu.getMenu_id_list().split(",").length; i++){
				menu.setMenu_id(menu.getMenu_id_list().split(",")[i]);
				menuIsEmpty = systemMngService.menuIsEmpty(menu);
				if(!menuIsEmpty){
					result.setResult(false);
					result.setMessage("하위 메뉴가 있을시에는 삭제가 불가능합니다.");
					return result;
				}
			}
			
			
			for(int i = 0; i < menu.getMenu_id_list().split(",").length; i++){
				menu.setMenu_id(menu.getMenu_id_list().split(",")[i]);
				check += systemMngService.deleteMenuInfo(menu);
				if (check > 0) {
					check += systemMngService.deleteMenuAuth(menu);
				}
				check = 0;
			}
				result.setResult(true);
				result.setMessage("삭제 되었습니다.");

		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setMessage(methodName + "예외발생 ==> " + ex.getMessage());
			result.setResultCount(0);
			result.setResult(false);
		}
		return result;
	}

	
/*	@RequestMapping(value = "/deleteMenuInfo", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteMenuInfo(@ModelAttribute("menu") Menu menu, Model model) throws Exception {
		
		Result result = new Result();
		
		boolean menuIsEmpty = false;  //
		menuIsEmpty = systemMngService.menuIsEmpty(menu);
		if(!menuIsEmpty){  // empty가 아닐경우
			result.setMessage("하위 메뉴가 있을시에는 삭제가 불가능합니다.");
			result.setResult(false);
			result.setResultCount(0);
			return result;
		}
		
		
		try {
			boolean check = false;
			int saveResult = 0;
			saveResult += systemMngService.deleteMenuInfo(menu);
			if (saveResult > 0) {
				saveResult += systemMngService.deleteMenuAuth(menu);
			}
			check = (saveResult > 0) ? true : false;
			String msg;
			if (check) {
				msg = "메뉴 삭제가 완료 되었습니다.";
			} else {
				msg = "메뉴를 삭제하는 중 오류가 발생하였습니다.";
			}
			result.setResultCount(saveResult);
			result.setResult(check);
			result.setMessage(msg);
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			result.setResultCount(0);
			result.setMessage("데이터베이스 오류입니다");
			result.setResult(false);
		}
		return result;
	}
*/

	
	/* 시스템 관리 - 사용자 정보 
	@RequestMapping(value="/UserInfo", method=RequestMethod.GET)
	public String UserInfo(@ModelAttribute("users") Users users, Model model) throws Exception {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		model.addAttribute("menu_id", users.getMenu_id());
		model.addAttribute("menu_nm", users.getMenu_nm());
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");  
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)

		
		try {
			
			users.setUser_id(user_id);
			users = systemMngService.getUserInfo(users);
			//String user_auth_id = systemMngService.getUserAuthId(user_id);
			//users.setAuth_id(user_auth_id);
			
			if(SHA256Util.isAppliedSHA256(commonService.getUserPasswdChgDt(users))){
				//users.setPassword(users.getPassword());
			}else{
//				users.setPassword(pbeEnc.decrypt(users.getPassword()));
			}
		} catch (Exception ex) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error(methodName + "예외발생 ==> " + ex.getMessage());
			throw ex;
		}
		
		model.addAttribute("userInfo",users);
		model.addAttribute("menu_nm","사용자 정보 변경");
		return "systemManage/userInfo";
	}*/
	
	
	/* 사용자 관리 - 사용자 정보 수정*/
	@RequestMapping(value="/UserInfo/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveUserInfo(@ModelAttribute("users") Users users,  Model model) {
		Result result = new Result();

		try{
			systemMngService.saveUserInfo(users);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	/* 사용자 관리 - 사용자 업무 수정*/
	@RequestMapping(value="/UserWrkjob/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveUserWrkjob(@ModelAttribute("users") Users users,  Model model) {
		Result result = new Result();
		
		try{
			systemMngService.saveUserAuthWrkjob(users);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
/*	 사용자 관리 - 사용자 비밀번호 수정
	@RequestMapping(value="/UserPassword/Change", method=RequestMethod.POST)
	@ResponseBody
	public Result changeUserPassword(@ModelAttribute("users") Users users,  Model model) throws Exception {
		Result result = new Result();
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
    	pbeEnc.setAlgorithm("PBEWithMD5AndDES");  
        pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)

        try{
			if(commonService.userPasswordValidCheck(users) && SHA256Util.isAppliedSHA256()){
				logger.debug("########### NEW ALGO USER PASSWD CHANGE UPDATE ###########");
				String salt = SHA256Util.generateSalt();
				String newPassword = SHA256Util.getEncrypt(users.getNew_password(), salt) ;
				
				users.setSalt_value(pbeEnc.encrypt(salt));
				users.setPassword(newPassword);
			}else{
				logger.debug("########### OLD ALGO USER PASSWD CHANGE UPDATE ###########");
				users.setPassword(pbeEnc.encrypt(users.getNew_password()));
			}
			
			
			systemMngService.changeUserPassword(users);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	*/
	/* 사용자 관리 - 사용자 비밀번호 확인
	@RequestMapping(value="/UserPassword/Check", method=RequestMethod.POST)
	@ResponseBody
	public Result checkPassword(@ModelAttribute("users") Users users,  Model model, HttpServletRequest req) throws Exception {
		Result result = new Result();
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");  
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)

		
		
		HttpSession session = req.getSession();
        PrivateKey privateKey = (PrivateKey) session.getAttribute(LoginController.RSA_WEB_KEY);


        try {
        	System.out.println("############## 유저확인");
        	//userpwd = decryptRsa(privateKey, userpwd);
        	//System.out.println("user_pwd:::"+userpwd);
        	String new_user_id = decryptRsa(privateKey, users.getNew_user_id());
        	String new_password= decryptRsa(privateKey, users.getNew_password());
        	System.out.println("new_user_id:::"+new_user_id);
        	System.out.println("new_password:::"+new_password);
        	
			users.setNew_user_id(new_user_id);
			users.setNew_password(new_password);

			
			boolean check = systemMngService.checkPassword(users);
			result.setResult(check);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	*/

	/* 기본 권한*/
	@RequestMapping(value = "/UserInfo/DefaultAuth", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DefaultAuth(@ModelAttribute("users") Users users) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		List<Users> resultList = new ArrayList<Users>();
		try {
			users.setUser_id(user_id);
			resultList = systemMngService.defaultAuth(users);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}
		return success(resultList).toJSONObject().get("rows").toString();
//		return success(resultList).toJSONObject().toString();
	}
	
	/* 기본 업무*/
	@RequestMapping(value = "/UserInfo/DefaultWrkjobCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DefaultWrkjobCd(@ModelAttribute("users") Users users) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		List<Users> resultList = new ArrayList<Users>();
		try {
			users.setUser_id(user_id);
			resultList = systemMngService.defaultWrkjobCd(users);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}
		return success(resultList).toJSONObject().get("rows").toString();
//		return success(resultList).toJSONObject().toString();
	}
	
	
	@RequestMapping(value = "/ProjectMng", method = RequestMethod.GET)
	public String ProjectMng(@ModelAttribute("project") Project project, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();
		
		model.addAttribute("menu_id", project.getMenu_id());
		model.addAttribute("menu_nm", project.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "systemManage/projectMng";
	}
	
	
	/* 기준정보 설정 - 프로젝트 관리 - 리스트*/
	@RequestMapping(value = "/ProjectMng", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ProjectMngList(@ModelAttribute("project") Project project, Model model) {
		List<Project> resultList = new ArrayList<Project>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = systemMngService.getProjectMngList(project);
			if (resultList != null && resultList.size() > project.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(project.getPagePerCount());
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		// return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* 기준정보 설정 - 프로젝트 관리 - 엑셀다운로드 */
	@RequestMapping(value = "/ProjectMng/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getProjectMngListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("project") Project project, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = systemMngService.getProjectMngListByExcelDown(project);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로젝트_관리");
		model.addAttribute("sheetName", "프로젝트_관리");
		model.addAttribute("excelId", "PROJECT");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	
	
	/* 기준정보 설정 - 프로젝트 관리 - 저장 */
	@RequestMapping(value = "/ProjectMng/Save", method = RequestMethod.POST)
	@ResponseBody
	public Result ProjectMngSave(@ModelAttribute("project") Project project, Model model) {
		Result result = new Result();
		int check = 0;
		
		try {
			check = systemMngService.saveProjectMng(project);
			if(check == 0) result.setResult(false); else result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* 데이터베이스 조회 */
	@RequestMapping(value = "/Sysmng/getDatabase", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDatabase(@ModelAttribute("database") Database database) throws Exception {
		String isAll = StringUtils.defaultString(database.getIsAll());
		String isChoice = StringUtils.defaultString(database.getIsChoice());
		List<Database> dbList = new ArrayList<Database>();
		List<Database> databaseList = systemMngService.getDatabase(database);
		Database db = new Database();
		db.setDbid("");
		if (isAll.equals("Y")) {
			db.setDb_name("전체");
		}else if (isChoice.equals("Y")) {
			db.setDb_name("선택");
		} else {
			db.setDb_name("");
		}
		dbList.add(db);
		dbList.addAll(databaseList);

		return success(dbList).toJSONObject().get("rows").toString();
	}	

	
}
