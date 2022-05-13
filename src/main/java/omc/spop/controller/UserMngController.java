package omc.spop.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Auth;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.Result;
import omc.spop.model.UserAuth;
import omc.spop.model.UserDBPrivilege;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;
import omc.spop.service.UserMngService;
import omc.spop.service.WatchLogService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelRead;
import omc.spop.utils.ExcelReadOption;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2017.11.09	이원식	최초작성
 * 2018.02.20	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class UserMngController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserMngController.class);
	
	@Autowired
	private UserMngService userMngService;
	
	@Autowired
	private WatchLogService watchLogService;

	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	

	/* 사용자 관리 - 사용자 관리 */
	@RequestMapping(value="/Users", method=RequestMethod.GET)
	public String User(@ModelAttribute("users") Users users, Model model) {
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", users.getMenu_id() );
		model.addAttribute("menu_nm", users.getMenu_nm() );
		model.addAttribute("user_auth_id", user_auth_id);
		
		return "userMng/users";
	}
	
	/* 사용자 관리 - 사용자 관리 action*/
	@RequestMapping(value="/Users", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AuthAction(@ModelAttribute("users") Users users, Model model) {
		List<Users> resultList = new ArrayList<Users>();
		int dataCount4NextBtn = 0;
		
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		users.setUser_auth_id(user_auth_id);
		
		try{
			resultList = userMngService.usersList(users);
			if (resultList != null && resultList.size() > users.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(users.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}

	/*설정 - 사용자관리 엑셀 다운*/
	@RequestMapping(value = "/Users/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PerformanceCheckMngListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("users") Users users, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = userMngService.getUsersListByExcelDown(users);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "사용자_관리");
		model.addAttribute("sheetName", "사용자_관리");
		model.addAttribute("excelId", "USERS");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/*설정 - 권한 관리 엑셀 다운*/
	@RequestMapping(value = "/UsersAuth/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getAuthUserNameByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("users") Users users, Model model)
					throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = userMngService.getAuthUserNameByExcelDown(users);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "권한_관리");
		model.addAttribute("sheetName", "권한_관리");
		model.addAttribute("excelId", "AUTH");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	
	
	/* 사용자 관리 - 사용자 ID 중복 체크*/
	@RequestMapping(value="/Users/CheckUserId", method=RequestMethod.POST)
	@ResponseBody
	public Result CheckUserId(@ModelAttribute("users") Users users,  Model model) {
		Result result = new Result();
		Users temp = new Users();

		try{
			temp = userMngService.checkUserId(users);
			result.setResult((temp == null || temp.getUser_id().equals("")) ? true : false);
			result.setMessage("[ "+users.getUser_id()+" ] 아이디는 사용중입니다. 다른 ID를 사용해 주세요.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 사용자 관리 - 사용자 관리 save */
	@RequestMapping(value="/Users/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveUsersAction(@ModelAttribute("users") Users users,  Model model) {
		Result result = new Result();
		Users temp = new Users();
		logger.debug("users★:::::"+users);
		try{
			temp = userMngService.checkUserId(users);
				if(users.getCrud_flag().equals("C") && temp == null || temp.getUser_id().equals("")){
//					logger.debug("1:::::"+ temp.getUser_id());
//					userMngService.saveUsers(users);
					userMngService.insertUsers(users);
					result.setResult(true);
				}else if(users.getCrud_flag().equals("U") && temp != null || !temp.getUser_id().equals("") ){
//					logger.debug("2:::::"+ temp.getUser_id());
					userMngService.updateUsers(users);
					result.setResult(true);
				}else{
					result.setResult(false);
					result.setMessage("[ "+users.getUser_id()+" ] 아이디는 사용중입니다. 다른 ID를 사용해 주세요.");
					return result;
				}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 사용자 관리 - 사용자 관리 - 승인 */
	@RequestMapping(value="/ApproveUser", method=RequestMethod.POST)
	@ResponseBody
	public Result ApproveUserAction(@ModelAttribute("users") Users users,  Model model) {
		Result result = new Result();
		try{
			//승인여부를 두개 이상 체크할시만 실행
			
//			if(users.getChk_user_id() != null && !users.getChk_user_id().equals("") && users.getChk_user_id().contains("/")){
//					int check = userMngService.saveUserApprove(users);
//			}
			
			for(int i = 0; i < users.getChk_user_id().split("/").length; i++){
				System.out.println("몇번째 실행되는지 확인:::::::"+i);
				users.setUser_id(users.getChk_user_id().split("/")[i]);
				userMngService.approveUser(users);
				result.setResult(false);
			}
			
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 사용자 관리 - 사용자 관리 - 비밀번호 초기화 */
	@RequestMapping(value="/ResetUserPassword", method=RequestMethod.POST)
	@ResponseBody
	public Result ResetUserPasswordAction(@ModelAttribute("users") Users users,  Model model) {
		Result result = new Result();
		try{
			userMngService.resetUserPassword(users);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 사용자 관리 - 사용자 권한 관리 action*/
	@RequestMapping(value="/UsersAuth", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String UsersAuthAction(@ModelAttribute("userAuth") UserAuth userAuth,  Model model) {
		List<UserAuth> resultList = new ArrayList<UserAuth>();

		try{
			resultList = userMngService.usersAuthList(userAuth);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 사용자 관리 - 사용자 권한 관리 save */
	@RequestMapping(value="/UsersAuth/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveUsersAuthAction(@ModelAttribute("userAuth") UserAuth userAuth,  Model model) {
		Result result = new Result();
		try{
			userMngService.saveUsersAuth(userAuth);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 사용자 관리 - 사용자 권한 관리 delete */
	@RequestMapping(value="/UsersAuth/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteUsersAuthAction(@ModelAttribute("userAuth") UserAuth userAuth,  Model model) {
		Result result = new Result();
		try{
			userMngService.deleteUsersAuth(userAuth);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	/* 사용자 관리 - 사용자 업무 관리 action*/
	@RequestMapping(value="/UsersWrkJob", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String UsersWrkJobAction(@ModelAttribute("userWrkjob") UserWrkjob userWrkjob,  Model model) {
		List<UserWrkjob> resultList = new ArrayList<UserWrkjob>();

		try{
			resultList = userMngService.usersWrkJobList(userWrkjob);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	/* 사용자 관리 - 사용자 DB권한 관리 action*/
	@RequestMapping(value="/Users/DBAuth", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String UsersDbAuth(@ModelAttribute("userWrkjob") UserDBPrivilege userDBPrivilege,  Model model) {
		List<UserDBPrivilege> resultList = new ArrayList<UserDBPrivilege>();
		
		try{
			resultList = userMngService.usersDbAuth(userDBPrivilege);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* 사용자 관리 - 사용자 DB권한 관리 save */
	@RequestMapping(value="/Users/DBAuth/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveUsersDBAuthAction(@ModelAttribute("userDBPrivilege") UserDBPrivilege userDBPrivilege,  Model model) {
		Result result = new Result();
		int check = 0;
		
		try{
			check = userMngService.saveUserDBAuth2(userDBPrivilege);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	/* 사용자 관리 - 사용자 DB권한 관리 save */
	@RequestMapping(value="/Users/DBAuth/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteUsersDBAuthAction(@ModelAttribute("userDBPrivilege") UserDBPrivilege userDBPrivilege,  Model model) {
		Result result = new Result();
		int check = 0;
		try{
			check = userMngService.deleteUserDBAuth2(userDBPrivilege);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	
	/* 사용자 관리 - 사용자 업무 관리 save */
	@RequestMapping(value="/UsersWrkJob/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveUsersWrkJobAction(@ModelAttribute("userWrkjob") UserWrkjob userWrkjob,  Model model) {
		Result result = new Result();
		try{
			userMngService.saveUsersWrkJob(userWrkjob);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	
	/* 사용자 관리 - 사용자 업무 관리 save */
	@RequestMapping(value="/UsersWrkJob/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result deleteUsersWrkJobLeader(@ModelAttribute("userWrkjob") UserWrkjob userWrkjob,  Model model) {
		Result result = new Result();
		try{
			userMngService.deleteUsersWrkJob(userWrkjob);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 사용자 관리 - 업무리더 존재여부 체크*/
	@RequestMapping(value="/CheckWorkJobLeader", method=RequestMethod.POST)
	@ResponseBody
	public Result CheckWorkJobLeader(@ModelAttribute("userWrkjob") UserWrkjob userWrkjob,  Model model) {
		Result result = new Result();
		UserWrkjob temp = new UserWrkjob();

		try{
			temp = userMngService.checkWorkJobLeader(userWrkjob);
			if(temp != null){
				if(temp.getCnt() > 1){
					result.setResult(true);
					result.setMessage("해당 업무의 리더로 변경됩니다.");
				}else{
					result.setResult(true);
					result.setMessage("해당 업무["+temp.getWrkjob_nm()+"]의 리더가<br/>"
							+ "[ "+temp.getUser_nm()+"("+temp.getUser_id()+") ] 에서  <br/>"+ 
							"[ "+ userWrkjob.getUser_nm()+"("+userWrkjob.getUser_id() +") ] 로 변경됩니다. ");
				}
			}else{
				result.setMessage(null);
				result.setResult(true);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 사용자 관리 - 사용자 부서 관리 action*/
	/*@RequestMapping(value="/UserMng/UsersDept", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String UsersDeptAction(@ModelAttribute("userDept") UserDept userDept,  Model model) {
		List<UserDept> resultList = new ArrayList<UserDept>();

		try{
			resultList = userMngService.usersDeptList(userDept);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}*/
	
	/* 사용자 관리 - 사용자 부서 관리 save */
	/*@RequestMapping(value="/UserMng/SaveUsersDept", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveUsersDeptAction(@ModelAttribute("userDept") UserDept userDept,  Model model) {
		Result result = new Result();
		try{
			userMngService.saveUsersDept(userDept);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}*/	
	
	/* 사용자 관리 - 권한 관리 */
	@RequestMapping(value="/Auth", method=RequestMethod.GET)
	public String Auth(@ModelAttribute("auth") Auth auth, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", auth.getMenu_id() );
		model.addAttribute("menu_nm", auth.getMenu_nm() );

		return "userMng/auth";
	}
	
	/* 사용자 관리 - 권한 관리 action*/
	@RequestMapping(value="/Auth", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AuthAction(@ModelAttribute("auth") Auth auth, Model model) {
		List<Auth> resultList = new ArrayList<Auth>();

		try{
			resultList = userMngService.authList(auth);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 사용자 관리 - 권한 관리 save */
	@RequestMapping(value="/Auth/SaveAuth", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveAuthAction(@ModelAttribute("auth") Auth auth,  Model model) {
		Result result = new Result();
		try{
			userMngService.saveAuth(auth);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 사용자 관리 - 권한 관리 save */
	@RequestMapping(value="/Auth/SaveAuthBundle", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveAuthBundleAction(@ModelAttribute("auth") Auth auth,  Model model) {
		Result result = new Result();
		String user_ids=auth.getUser_ids();
		String user_id[] = user_ids.split(",");
		
		String auth_start_day = StringUtils.defaultString(auth.getAuth_start_day()).replaceAll("-","");
		String auth_end_day = StringUtils.defaultString(auth.getAuth_end_day()).replaceAll("-","");
		auth.setAuth_start_day(auth_start_day);
		auth.setAuth_end_day(auth_end_day);
		
		try{
			int saveResult = 0;
			for(String s:user_id){
				auth.setUser_id(s);
				saveResult += userMngService.saveAuthBundle(auth);
			}
			result.setResultCount(saveResult);
			if(saveResult > 0){
				result.setResult(true);
			}else{
				result.setResult(false);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 사용자 관리 - 부서 관리 */
	/*@RequestMapping(value="/UserMng/Department", method=RequestMethod.GET)
	public String Department(Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("startDate", startDate );
		
		return "setUp/userMng/department";
	}*/
	
	/* 사용자 관리 - 부서 관리 action*/
	/*@RequestMapping(value="/UserMng/Department", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DepartmentAction(@ModelAttribute("dept") Dept dept,  Model model) {
		List<Dept> resultList = new ArrayList<Dept>();

		try{
			resultList = userMngService.departmentList(dept);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}*/
	
	/* 사용자 관리 - 부서관리 save */
	/*@RequestMapping(value="/UserMng/SaveDepartment", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveDepartmentAction(@ModelAttribute("dept") Dept dept,  Model model) {
		Result result = new Result();
		try{
			userMngService.saveDepartment(dept);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}*/

	/* 사용자 관리 - 부서DB권한관리 */
/*	@RequestMapping(value="/UserMng/DepartmentDBAuth", method=RequestMethod.GET)
	public String DepartmentDBAuth(Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		model.addAttribute("nowDate", nowDate );
		
		return "setUp/userMng/departmentDBAuth";
	}*/
	
	/* 사용자 관리 - 부서 TreeGrid action*/
/*	@RequestMapping(value="/UserMng/DepartmentTree", method=RequestMethod.GET, produces="application/text; charset=utf8")
	@ResponseBody
	public String DepartmentTreeAction(@ModelAttribute("dept") Dept dept,  Model model) {
		List<Dept> resultList = new ArrayList<Dept>();
		String returnValue = "";
		
		try{
			resultList = userMngService.departmentTreeList(dept);
			List<Dept> buildList = buildGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return returnValue;	
	}*/	
	
	/* 사용자 관리 - 부서DB권한관리 action*/
	/*@RequestMapping(value="/UserMng/DepartmentDBAuth", method=RequestMethod.POST)
	@ResponseBody
	public Result DepartmentDBAuthAction(@ModelAttribute("deptDBPrivilege") DeptDBPrivilege deptDBPrivilege,  Model model) {
		Result result = new Result();
		List<DeptDBPrivilege> resultList = new ArrayList<DeptDBPrivilege>();

		try{
			resultList = userMngService.departmentDBAuthList(deptDBPrivilege);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}*/
	
	/* 사용자 관리 - 부서DB권한관리 저장 */
	/*@RequestMapping(value="/UserMng/SaveDepartmentDBAuth", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveDepartmentDBAuthAction(HttpServletRequest req, HttpServletResponse res, Model model) {
		Result result = new Result();
		try{
			userMngService.saveDepartmentDBAuth(req);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}*/
	
	/* 사용자 관리 - 부서DB권한 관리 이력 action*/
	/*@RequestMapping(value="/UserMng/DepartmentDBAuthHistory", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DepartmentDBAuthHistoryAction(@ModelAttribute("deptDBPrivilege") DeptDBPrivilege deptDBPrivilege,  Model model) {
		List<DeptDBPrivilege> resultList = new ArrayList<DeptDBPrivilege>();

		try{
			resultList = userMngService.departmentDBAuthHistoryList(deptDBPrivilege);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}*/
	
	/* 사용자 관리 - 업무 관리 */
	@RequestMapping(value="/WorkJob", method=RequestMethod.GET)
	public String WorkJob(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("startDate", startDate );
		model.addAttribute("menu_id", wrkJobCd.getMenu_id() );
		model.addAttribute("menu_nm", wrkJobCd.getMenu_nm() );
	
		return "userMng/workJob";
	}
	
	/* 업무 관리 - 업무 Tree */
	@RequestMapping(value="/WorkJobTree", method=RequestMethod.GET, produces="application/text; charset=utf8")
	@ResponseBody
	public String WorkJobTreeAction(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd, Model model) {
		List<WrkJobCd> resultList = new ArrayList<WrkJobCd>();
		String returnValue = "";
		
		try{
			resultList = userMngService.workJobTreeList(wrkJobCd);
			List<WrkJobCd> buildList = TreeWrite.buildWorkJob(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
			
			returnValue = jsonArray.toString();
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return returnValue;	
	}
	
	/* 업무 관리 - 업무별 사용자 리스트 */
	@RequestMapping(value="/WorkJobUsers", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String WorkJobUsersAction(@ModelAttribute("users") Users users,  Model model) {
		List<Users> resultList = new ArrayList<Users>();
		int dataCount4NextBtn = 0;

		try{
			resultList = userMngService.workJobUsersList(users);
			if(resultList != null && resultList.size() > users.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(users.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}	
	
	/*설정 - 업무관리 엑셀 다운*/
	@RequestMapping(value = "/WorkJobUsers/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView workJobUsersListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("users") Users users, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = userMngService.workJobUsersListByExcelDown(users);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "업무_관리");
		model.addAttribute("sheetName", "업무_관리");
		model.addAttribute("excelId", "WRKJOB_CD");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 업무 관리 - 업무 저장 */
	@RequestMapping(value="/WorkJob/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveWorkJobAction(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd,
			@RequestParam("db") String[] dbidArr, Model model) throws Exception {
		
		Result result = new Result();
		try{
			userMngService.saveWorkJob(wrkJobCd, dbidArr);
			result.setResult(true);
			
		} catch (Exception ex){
			logger.error("/*********************/");
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			logger.error("/*********************/");
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 업무 관리 - 업무 삭제 */
	@RequestMapping(value="/WorkJob/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result deleteWorkJobAction(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd, Model model) throws Exception {
		Result result = new Result();
		try{
			int deleteResult = userMngService.deleteWrkJobCd(wrkJobCd);
			if(deleteResult > 0){
				result.setResult(true);
			}else{
				result.setResult(false);
			}
		} catch (Exception ex){
			logger.error("/*********************/");
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			logger.error("/*********************/");
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 업무 관리 - 업무리더 저장 */
	@RequestMapping(value="/WorkJobLeader/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveWorkJobLeader(@ModelAttribute("userWrkjob") UserWrkjob userWrkjob, Model model) {
		logger.debug("/WorkJobLeader/Save started");
		logger.debug("userWrkjob:"+ userWrkjob.toString());	
		logger.debug("userWrkjob.user_id:"+ userWrkjob.getUser_id());	
		logger.debug("userWrkjob.wrkjob_cd:"+ userWrkjob.getWrkjob_cd());	
		logger.debug("userWrkjob.leader_yn:"+ userWrkjob.getLeader_yn());	

		Result result = new Result();
		try{
			userMngService.saveWorkJobLeader(userWrkjob);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	

	/* 사용자 관리 - 사용자 DB권한관리 */
	@RequestMapping(value="/UserDBAuth", method=RequestMethod.GET)
	public String UserDBAuth(@ModelAttribute("userDBPrivilege") UserDBPrivilege userDBPrivilege, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", userDBPrivilege.getMenu_id() );
		model.addAttribute("menu_nm", userDBPrivilege.getMenu_nm() );

		return "userMng/userDBAuth";
	}
	
	/* 사용자 관리 - 사용자DB권한 action*/
	@RequestMapping(value="/UserDBAuth", method=RequestMethod.POST)
	@ResponseBody
	public Result UserDBAuthAction(@ModelAttribute("userDBPrivilege") UserDBPrivilege userDBPrivilege, Model model) {
		Result result = new Result();
		List<UserDBPrivilege> resultList = new ArrayList<UserDBPrivilege>();

		try{
			resultList = userMngService.userDBAuthList(userDBPrivilege);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	
	/* 사용자 관리 - 사용자DB권한관리 저장 */
	@RequestMapping(value="/UserDBAuth/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveUserDBAuthAction(HttpServletRequest req, HttpServletResponse res, Model model) {
		Result result = new Result();
		try{
			userMngService.saveUserDBAuth(req);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 사용자 관리 - 사용자DB권한 관리 이력 리스트 */
	@RequestMapping(value="/UserDBAuthHistory", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String UserDBAuthHistoryAction(@ModelAttribute("userDBPrivilege") UserDBPrivilege userDBPrivilege,  Model model) {
		List<UserDBPrivilege> resultList = new ArrayList<UserDBPrivilege>();

		try{
			resultList = userMngService.userDBAuthHistoryList(userDBPrivilege);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 사용자 관리 - 튜닝담당자 관리 */
	@RequestMapping(value="/PerformanceOfficer", method=RequestMethod.GET)
	public String PerformanceOfficer(@ModelAttribute("users") Users users, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", users.getMenu_id() );
		model.addAttribute("menu_nm", users.getMenu_nm() );
		
		return "userMng/performanceOfficer";
	}
	
	/* 사용자 관리 - 튜닝담당자 action*/
	@RequestMapping(value="/PerformanceOfficer", method=RequestMethod.POST)
	@ResponseBody
	public Result PerformanceOfficerAction(@ModelAttribute("databaseTuner") DatabaseTuner databaseTuner,  Model model) {
		Result result = new Result();
		List<DatabaseTuner> resultList = new ArrayList<DatabaseTuner>();

		try{
			resultList = userMngService.performanceOfficerList(databaseTuner);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 사용자 관리 - 튜너(tuner) 리스트*/
	@RequestMapping(value="/PerformanceTuner", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PerformanceTunerAction(@ModelAttribute("users") Users users, Model model) {
		List<Users> resultList = new ArrayList<Users>();

		try{
			resultList = userMngService.performanceTunerList(users);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	

	/* 사용자 관리 - 튜닝담당자 저장 */
	@RequestMapping(value="/PerformanceOfficer/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SavePerformanceOfficerAction(HttpServletRequest req, HttpServletResponse res, Model model) {
		Result result = new Result();
		try{
			userMngService.savePerformanceOfficer(req);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 사용자 관리 - 튜닝담당자관리 이력 리스트 */
	@RequestMapping(value="/PerformanceOfficerHistory", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PerformanceOfficerHistoryAction(@ModelAttribute("databaseTuner") DatabaseTuner databaseTuner,  Model model) {
		List<DatabaseTuner> resultList = new ArrayList<DatabaseTuner>();

		try{
			resultList = userMngService.performanceOfficerHistoryList(databaseTuner);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
    /*
     * Download a file from 
     *   - inside project, located in resources folder.
     *   - outside project, located in File system somewhere. 
     */
    private static final String INTERNAL_FILE="excelUploadForm/User_Upload_Form.xlsx";
    private static final String EXTERNAL_FILE_PATH="C:/OMCPROJECT/User_Upload_Form.xlsx";	
    @RequestMapping(value="/Users/excelFormDownload/{type}", method = {RequestMethod.POST})
    public void downloadFile(HttpServletResponse response, @PathVariable("type") String type) throws IOException {
     
        File file = null;
        logger.debug("type:"+type); 
        if(type.equalsIgnoreCase("internal")){
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            logger.debug("classloader:"+classloader);
            logger.debug("current directory:"+classloader.getResource(".").getPath());
            file = new File(classloader.getResource(INTERNAL_FILE).getFile());   
        }else{
            file = new File(EXTERNAL_FILE_PATH);
        }
         
        if(!file.exists()){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            logger.debug(errorMessage);
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
         
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
 
         
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
         
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
 
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
	
    @ResponseBody
    @RequestMapping(value = "/Users/excelUploadAjax", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView excelUploadAjax(MultipartHttpServletRequest request)  throws Exception{
        MultipartFile excelFile =request.getFile("excelFile");
        logger.debug("엑셀 파일 업로드 컨트롤러");
        if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("엑셀파일을 선택 해 주세요.");
        }
        
        logger.debug("originalFilename:"+excelFile.getOriginalFilename());
        
        File destFile = new File("D:\\"+excelFile.getOriginalFilename());
        try{
            excelFile.transferTo(destFile);
        }catch(IllegalStateException e){
            throw new RuntimeException(e.getMessage(),e);
        }catch(IOException e){
            throw new RuntimeException(e.getMessage(),e);
        }
        
        //Service 단에서 가져온 코드 
        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        excelReadOption.setOutputColumns("A","B","C","D","E","F");
        excelReadOption.setStartRow(2);
        
        
        List<Map<String, String>>excelContent =ExcelRead.read(excelReadOption);
        
        for(Map<String, String> article: excelContent){
            logger.debug(article.get("A"));
            logger.debug(article.get("B"));
            logger.debug(article.get("C"));
            logger.debug(article.get("D"));
            logger.debug(article.get("E"));
            logger.debug(article.get("F"));
        }
        
        //userService.excelUpload(destFile); //서비스 부분을 삭제한다.
        
        //FileUtils.forceDelete(destFile.getAbsolutePath());
        
        ModelAndView view = new ModelAndView();
        view.setViewName("/user/list");
        return view;
    }
    
	/* 성능점검 엑셀파일 업로드 */
	@RequestMapping(value = "/UserRegistExcel/Upload", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result UploadExcelFile(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("users") Users user, Model model) {
		Result result = new Result();
		
		if (!file.isEmpty()) {
			if(file.getSize() > maxUploadSize){
		    	result.setResult(false);
		    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
		    }else{
				try {
					result = userMngService.uploadUserExcelFile(file);
					
				}catch (Exception ex) {
					ex.printStackTrace();
					result.setResult(false);
					/*result.setMessage(ex.getMessage());*/
				}		
		    }
	    }
		
		return result;		
	}	
	    
	/**
	 * 파일 업로드 JSON
	 * @param model
	 * @param parameterMap
	 * @param request
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/Users/fileUpload.json")
	public ModelAndView fileUpload(Model model, @RequestParam Map<String,Object> param, HttpServletRequest request) throws Exception {
		logger.debug("fileUpload started");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
		
		MultipartHttpServletRequest multirequest = (MultipartHttpServletRequest)request;
		CommonsMultipartFile multipartFile =  null;
		
		Enumeration<?> enm = request.getParameterNames();
		while(enm.hasMoreElements()){
			String s = (String)enm.nextElement();
			String paramValues[] = request.getParameterValues(s);
			for(int i=0;i<paramValues.length;i++){
				logger.debug(s+":"+paramValues[i]);
				multipartFile = (CommonsMultipartFile) multirequest.getFile(paramValues[i]);
			}
		}
		
		List<MultipartFile> multiFile = multirequest.getFiles("excelFile");
		logger.debug("multiFile.size:"+multiFile.size());

//			MultipartFile multiFile1 = multirequest.getFile("file_btn_2");
//			logger.debug("multiFile1:"+multiFile1);
//	        logger.debug("originalFileName:"+multiFile1.getOriginalFilename());
//	        logger.debug("ContentType:"+multiFile1.getContentType());
//	        logger.debug("ContentType:"+multiFile1.getName());
//	        logger.debug("ContentType:"+multiFile1.getSize());

		String physicalFileName = "";
		String originalFileName = "";

//			int fileCount = Integer.parseInt(request.getParameter("file_count"));
//			for(int i=0;i<fileCount;i++){
//				multiFile1 = multirequest.getFile("file_btn_"+(i+1));
//		        logger.debug("originalFileName"+(i+1)+":"+multiFile1.getOriginalFilename());
//		        logger.debug("ContentType"+(i+1)+":"+multiFile1.getContentType());
//		        logger.debug("ContentType"+(i+1)+":"+multiFile1.getName());
//		        logger.debug("ContentType"+(i+1)+":"+multiFile1.getSize());
//			}

		for(int i=0;i<multiFile.size();i++){
	        multipartFile = (CommonsMultipartFile) multiFile.get(i)   ;

			// 한글 깨지는 문제를 해결하기위해
	        originalFileName = multipartFile.getOriginalFilename();
	        logger.debug("originalFileName:"+originalFileName);
	        originalFileName = new String(originalFileName.getBytes("8859_1"),"utf-8");
	        logger.debug("originalFileName:"+originalFileName);
	        if ( ! "".equals(originalFileName) ) {
	        
				String fileExt = "";
				if (originalFileName.lastIndexOf(".") > -1) {
					fileExt = originalFileName.substring(originalFileName.lastIndexOf("."));
				}
				sdf.applyPattern("yyyyMMddHHmmssSSS");
				String fileNamePostfix = sdf.format(new Date());	// 파일 중목을 막기위해 파일명에 시간 스트링 쳐붙임
				
				physicalFileName = fileNamePostfix + fileExt;
				logger.debug("physicalFileName:"+physicalFileName);
				logger.debug("contentType:"+multipartFile.getContentType());

				MultipartFile mf = multipartFile;
				if(mf != null){
					byte mfBytes[] = mf.getBytes();
					param.put("formula_file_bin", mfBytes);
				}
//				int saveResult = nfService.saveNumberFormatFile(param);
//				logger.debug("saveResult:"+saveResult);
				
	        }
		}		


		ModelAndView mav = new ModelAndView();		
		mav.setViewName("jsonView");		
		logger.debug("fileUpload ended");
		
		return mav;
	}
 
}