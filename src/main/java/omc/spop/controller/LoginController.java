package omc.spop.controller;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
import omc.spop.model.Result;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;
import omc.spop.service.CommonService;
import omc.spop.service.SystemMngService;
import omc.spop.service.TestService;
import omc.spop.service.WatchLogService;
import omc.spop.utils.OMCUtil;
import omc.spop.utils.SHA256Util;
import omc.spop.utils.TreeWrite;;
/***********************************************************
 * 2017.08.10 이원식 최초작성
 **********************************************************/

@Controller
@RequestMapping(value = "/auth")
public class LoginController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private SystemMngService systemMngService;
	
	@Autowired
	private WatchLogService watchLogService;
	
	@Value("#{defaultConfig['version.date']}")
	private String settingDate;
	
	@Value("#{defaultConfig['customer']}")
	private String customer;
	
	@Value("#{defaultConfig['customer.sso_yn']}")
	private String sso_yn;
	
	@Value("#{defaultConfig['256_updated_yn']}")
	private String sha256_updated_yn;
	
	@Value("#{defaultConfig['256_updated_dt']}")
	private String sha256_updated_dt;
	
	@Value("#{defaultConfig['end.date']}")
	private String endDate;
	
	private static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	private static String RSA_INSTANCE = "RSA"; // rsa transformation
	
	private String url_path_name = "";
	
	private static final String FLAG_KEY = "flag_key";
	private static final String MSG_KEY = "msg_key";
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginBySSO(@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "user_id", required = true) String user_id, 
			@RequestParam(value = "secureSessionId", required = true) String secureSessionId,
			@RequestParam(value = "isConnectedBySSO", required = true) String isConnectedBySSO, 
			Model model) {
			System.out.println("POST SUCCESSED !! ");
		
		url_path_name = "/auth/login.post";
		
		model.addAttribute("user_id", user_id); // user_id
		model.addAttribute("customer", customer); // kbcd
		model.addAttribute("sso_yn", sso_yn); // Y
		model.addAttribute("error", error); // null
		model.addAttribute("isConnectedBySSO", isConnectedBySSO); //Y
		
		return "login/loginBySSO";	
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "user_id", required = false) String user_id, 
			@RequestParam(value = "inc_user_id", required = false) String inc_user_id, HttpServletRequest req, HttpServletResponse res, 
			Model model) {
		
/*		if(customer.equals("kbcd") && sso_yn.equals("Y")){
			StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
			pbeEnc.setAlgorithm("PBEWithMD5AndDES");
			pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
			user_id = pbeEnc.decrypt(inc_user_id);
			if(user_id.equals(inc_user_id)){
				model.addAttribute("user_id", user_id);
			}else{
				user_id = "not try hacking!";
				model.addAttribute("user_id", user_id);
			}
			
		}else{
			model.addAttribute("user_id", user_id);
		}*/
		
		url_path_name = "/auth/login";
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("customer", customer);
		model.addAttribute("sso_yn", sso_yn);
		model.addAttribute("error", error);
		initRsa(req);
		initCSRF(req);
		
		model = OMCUtil.checkLicensePeried(model, endDate);
		
		return "login/login";
	}
	
	/// auth/process
	// HTTP Status 405 - Request method 'POST' not supported
	@RequestMapping(value = "/process", method = { RequestMethod.GET, RequestMethod.POST })
	public String process(@RequestParam(value = "error", required = false) String error, 
			HttpServletRequest req, HttpServletResponse res, 
			Model model) {
		System.out.println("######### Just It's Not Passing This #########");
		
		url_path_name = "/auth/process";
		
		model.addAttribute("error", error);
		model.addAttribute("customer", customer);
		
		initRsa(req);
		initCSRF(req);
		
		return "login/login";
	}
	
	@RequestMapping(value = "/toplogout", method = RequestMethod.GET)
	public String toplogout(HttpServletResponse response) {
		System.out.println("쿠키 제거 확인");
		Cookie kc = new Cookie("deploy_id", null); // choiceCookieName(쿠키 이름)에 대한 값을 null로 지정
		
		kc.setMaxAge(0); // 유효시간을 0으로 설정
		
		response.addCookie(kc); // 응답 헤더에 추가해서 없어지도록 함
		
		return "login/toplogout";
	}
	
	@RequestMapping(value = "/checkPage", method = RequestMethod.GET)
	public String checkPage(@RequestParam(value = "error", required = false) String error, 
			HttpServletRequest req, HttpServletResponse res, 
			Model model) {
		
		url_path_name = "/auth/checkPage";
		
		model.addAttribute("error", error);
		
		return "login/checkPage";
	}
	
	private HashMap<String, Object> isNormalCaseCsrf(HttpServletRequest req) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String param = "";
		HttpSession session = null;
		final String FLAG_KEY = "flag_key";
		final String MSG_KEY = "msg_key";
		final String MSG_STR = "세션이 만료되었습니다. 계속하려면 다시 로그인 하세요.";
		
		try {
			param = req.getParameter("_csrf");
			session = req.getSession();
			
			logger.debug("///isNormalCaseCsrf ////////////////////////////////////////////");
			logger.debug("param[" + param + "] getAttr[" + session.getAttribute("CSRF_TOKEN") + "]");
			if(session.getAttribute("CSRF_TOKEN").equals(param)) {
				// 정상 로그인
				resultMap.put(FLAG_KEY, true);
			} else {
				resultMap.put(FLAG_KEY, false);
				logger.debug("mismatch between param[" + param + "] and CSRF_TOKEN[" + session.getAttribute("CSRF_TOKEN") + "]");
			}
		} catch(Exception e) {
			e.printStackTrace();
			
			resultMap.put(FLAG_KEY, false);
			resultMap.put(MSG_KEY, MSG_STR);
		} finally {
			;
		}
		
		return resultMap;
	}
	
	//로그인로직
	@RequestMapping(value = "/simpleLoginCheck", method = RequestMethod.GET)
	@ResponseBody
	public Result simpleLoginCheck(Model model, @RequestParam("user_id") String user_id,
			@RequestParam("userpassword") String userpwd, HttpServletRequest req, HttpServletResponse res) {
		Result result = new Result();
		Users user = new Users();
		Users resultUser = new Users();
		int passwordErrCnt = 0;
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		HashMap resultMap = null;
		boolean resultFlag = false;
		
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		
		try {
			resultMap = isNormalCaseCsrf(req);
			resultFlag = Boolean.parseBoolean(resultMap.get(FLAG_KEY) + "");
			
			if(resultFlag) {
				// 정상 로그인
			} else {
				// 비정상 로그인
				result.setResult(false);
				
				if(resultMap.containsKey(MSG_KEY) == true) {
					result.setMessage(resultMap.get(MSG_KEY) + "");
				} else {
					result.setMessage("인증되지 않은 사용자가 로그인을 시도하였습니다.<br/>확인 후 다시 시도해 주세요.");
				}
				
				return result;
			}
			
			HttpSession session = req.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute(LoginController.RSA_WEB_KEY);
			
			userpwd = decryptRsa(privateKey, userpwd);
			user_id = decryptRsa(privateKey, user_id);
			
			user.setUser_id(user_id);
			resultUser = commonService.getSimpleUserInfo(user);
			passwordErrCnt = commonService.passwordErrCnt(user_id);
			
			if(passwordErrCnt >= 5){
				result.setResult(false);
				result.setMessage("비밀번호 오류 횟수를 초과하여 계정 잠금이 설정되었습니다. 계정 잠금을 해제하려면 관리자에게 문의하시기 바랍니다.");
			}else{
				//##################### 단방향 알고리즘이 적용된 유저라면 #####################
				if(resultUser != null && SHA256Util.isAppliedSHA256(StringUtils.defaultString(resultUser.getPassword_chg_dt()))){
					logger.debug("########## NEW ALGO UPDATED USER LOGIN ##########");
					
					if (resultUser != null && resultUser.getUser_id() != null && resultUser.getSalt_value() != null) {
						
						String salt = pbeEnc.decrypt(StringUtils.defaultString(resultUser.getSalt_value()));
						String user_passwd = SHA256Util.getEncrypt(userpwd, salt);
						
						if (user_passwd.equals(resultUser.getPassword())) {
							if(passwordErrCnt > 0) {
								commonService.updateResetErrCnt(user_id); //update errCnt = 0
							}
							
							result.setResult(true);
							result.setObject(resultUser);
						} else {
							commonService.updateErrCnt(user_id);
							result.setResult(false);
							
							if(passwordErrCnt != 4) {
								result.setMessage("로그인을 5회 이상 실패하시면 계정 잠금이 설정됩니다. (로그인 실패 "+ (passwordErrCnt+1) + ")");
							} else {
								result.setMessage("비밀번호 오류 횟수를 초과하여 계정 잠금이 설정되었습니다. 계정 잠금을 해제하려면 관리자에게 문의하시기 바랍니다.");
							}
						}
					} else {
						result.setResult(false);
						result.setMessage("등록된 사용자가 없습니다!<br/>확인 후 다시 시도해 주세요.");
					}
				}else{  
					// #####################  양방향 알고리즘이 적용된 유저라면 #####################
					logger.debug("########## OLD ALGO UPDATED USER LOGIN ##########");
					
					if (resultUser != null && resultUser.getUser_id() != null && StringUtils.defaultString(resultUser.getSalt_value()).equals("")) {
						if (userpwd.equals(pbeEnc.decrypt(resultUser.getPassword()))) {
							if(passwordErrCnt > 0) {
								commonService.updateResetErrCnt(user_id); //update errCnt = 0
							}
							
							result.setResult(true);
							result.setObject(resultUser);
						} else {
							commonService.updateErrCnt(user_id);
							result.setResult(false);
							result.setMessage("로그인을 5회 이상 실패하시면 계정 잠금이 설정됩니다. (로그인 실패 "+ (passwordErrCnt+1) + ")");
						}
					} else {
						result.setResult(false);
						result.setMessage("등록된 사용자가 없습니다!!<br/>확인 후 다시 시도해 주세요.");
					}
				}
			}
			
			if(resultUser != null && !result.getResult()){
				watchLogService.WatchLoginUsers(resultUser, result.getResult());
			}else if (resultUser == null && !result.getResult()){
				Users newUser = new Users();
				
				newUser.setUser_id(user_id);
				watchLogService.WatchLoginUsers(newUser, result.getResult());
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	
	//두번째 들어옴.
	@RequestMapping(value = "/loginCheck", method = RequestMethod.GET)
	@ResponseBody
	public Result loginCheck(Model model, @RequestParam("user_id") String user_id,
			@RequestParam("userpassword") String userpwd, @ModelAttribute("users") Users users, HttpServletRequest req, HttpServletResponse res) throws Exception {
		Result result = new Result();
		Users resultUser = new Users();
		HttpSession session = req.getSession();
		PrivateKey privateKey = (PrivateKey) session.getAttribute(LoginController.RSA_WEB_KEY);
		String change_user_id = "";
		
		try {
			logger.debug("///// [loginCheck] /////////////////////////////////////////////////////////////");
			logger.debug("///// users.getPass_decrypt[" + users.getPass_decrypt() +
					"] url_path_name[" + url_path_name + "] user_id[" + user_id + "]");
			
			if(StringUtils.defaultString(users.getPass_decrypt()).equals("Y") == true) {
				change_user_id = user_id;
			} else if(sso_yn.equals("Y") == true && url_path_name.equalsIgnoreCase("/auth/process") == false) {
				change_user_id = user_id;
			} else {
				change_user_id = decryptRsa(privateKey, user_id);
			}
			
			users.setUser_id(change_user_id);
			resultUser = commonService.getUserInfo(users);
			
			if (resultUser != null && resultUser.getUser_id() != null) {
					result.setResult(true);
					result.setObject(resultUser);
					model.addAttribute("settingDate", settingDate);
			} else {
				result.setResult(false);
				result.setMessage("등록된 사용자가 없습니다!!!<br/>확인 후 다시 시도해 주세요.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		if(StringUtils.defaultString(resultUser.getDefault_auth_grp_cd()).equals("")) {
			resultUser.setDefault_auth_grp_cd(resultUser.getAuth_cd());
		}
		
		if(StringUtils.defaultString(resultUser.getApprove_yn()).equals("Y")) {
			watchLogService.WatchLoginUsers(resultUser, result.getResult());
		}
		
		return result;
	}
	
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logoutPage(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			watchLogService.WatchLoginLogout();
			//watchLogService.WatchLoginLogout(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		HttpSession session = request.getSession(false);
		if (request.isRequestedSessionIdValid() && session != null) {
			session.invalidate();
		}
		
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/updateNewPwd")
	@ResponseBody
	public Result updateNewPwd(@ModelAttribute("users") Users users, HttpServletRequest req) {
		Result result = new Result();
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		int resultCnt = 0;
		
		HttpSession session = req.getSession();
		PrivateKey privateKey = (PrivateKey) session.getAttribute(LoginController.RSA_WEB_KEY);
		
		
		try {
			String new_user_id = decryptRsa(privateKey, users.getNew_user_id());
			String new_password= decryptRsa(privateKey, users.getNew_password());
			
			users.setNew_user_id(new_user_id);
			users.setNew_password(new_password);
			
			if(SHA256Util.isAppliedSHA256()){   
				// 단방향 알고리즘 SHA256 알고리즘이 적용되었을 때
				// 비밀번호 암호화
				logger.debug("##################### NEW ALGO UPDATE USER PASSWD #####################");
				String salt = SHA256Util.generateSalt();
				String password = SHA256Util.getEncrypt(users.getNew_password(), salt);
				users.setSalt_value(pbeEnc.encrypt(salt));
				users.setNew_password(password);
				
			}else{ 
				logger.debug("##################### OLD ALGO UPDATE USER PASSWD #####################");
				// 양방향 알고리즘 SHA256 알고리즘이 적용되었을 때
				// 비밀번호 암호화
				users.setNew_password(pbeEnc.encrypt(users.getNew_password()));
			}
			
			resultCnt = commonService.updateNewPwd(users);
			result.setResult(resultCnt > 0 ? true : false);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/updateUserDefaultRole")
	@ResponseBody
	public Result updateUserDefaultRole(@ModelAttribute("users") Users users) {
		Result result = new Result();
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		int resultCnt = 0;
		
		try {
			resultCnt = commonService.updateUserDefaultRole(users);
			result.setResult(resultCnt > 0 ? true : false);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 사용자 ID 중복 체크 */
	@RequestMapping(value = "/CheckUserId", method = RequestMethod.POST)
	@ResponseBody
	public Result CheckUserId(@ModelAttribute("users") Users users, Model model) {
		Result result = new Result();
		Users temp = new Users();
		
		try {
			temp = commonService.checkUserId(users);
			result.setResult((temp == null || temp.getUser_id().equals("")) ? true : false);
			result.setMessage("[ " + users.getUser_id() + " ] 아이디는 사용중입니다. 다른 ID를 사용해 주세요.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/SaveNewUser")
	@ResponseBody
	public Result SaveNewUser(@ModelAttribute("users") Users users, HttpServletRequest req) throws Exception {
		Result result = new Result();
		int resultCnt = 0;
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		HashMap resultMap = null;
		boolean resultFlag = false;
		
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		//int updated_dt = Integer.parseInt(StringUtils.defaultString(sha256_updated_dt,"999999"));
		
		try {
			resultMap = isNormalCaseCsrf(req);
			resultFlag = Boolean.parseBoolean(resultMap.get(FLAG_KEY) + "");
			
			if(resultFlag) {
				// 정상 로그인
			} else {
				// 비정상 로그인
				result.setResult(false);
				
				if(resultMap.containsKey(MSG_KEY) == true) {
					result.setMessage(resultMap.get(MSG_KEY) + "");
				} else {
					result.setMessage("인증되지 않은 사용자가 로그인을 시도하였습니다.<br/>확인 후 다시 시도해 주세요.");
				}
				
				return result;
			}	
			
			if(users.getPassword() == null) throw new Exception("사용자등록하는 과정에서 알수없는 에러가 발생하였 습니다.");
			
			HttpSession session = req.getSession();
			PrivateKey privateKey = (PrivateKey) session.getAttribute(LoginController.RSA_WEB_KEY);
			
			String password = decryptRsa(privateKey, users.getPassword());
			users.setPassword(password);
			
			if(commonService.userPasswordValidCheck(users) && SHA256Util.isAppliedSHA256()){
				logger.debug("#### SAVE NEW USER BY NEW ALGO #####");
				
				// 비밀번호 암호화 BY SHA256
				String salt = SHA256Util.generateSalt();
				users.setPassword(SHA256Util.getEncrypt(users.getPassword(), salt));
				users.setSalt_value(pbeEnc.encrypt(salt));
				
			}else{
				logger.debug("#### SAVE NEW USER BY OLD ALGO #####");
				
				// 비밀번호 암호화
				users.setPassword(pbeEnc.encrypt(users.getPassword()));
			}
			
			resultCnt = commonService.saveNewUser(users);
			result.setResult(resultCnt > 0 ? true : false);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
			System.out.println("ex.getMessage :::::::::" +ex.getMessage());
		}
		
		watchLogService.WatchInsertUsersInfo(users.getUser_id());
		
		return result;
	}
	
	/* 권한 조회 */
	@RequestMapping(value = "/getAuthNmList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuthNmList(@ModelAttribute("auth") Auth auth) throws Exception {
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String isChoice = StringUtils.defaultString(auth.getIsChoice());
		List<Auth> authList = new ArrayList<Auth>();
		if (isChoice.equals("Y")) {
			Auth db = new Auth();
			db.setAuth_id("");
			db.setAuth_nm("선택");
			authList.add(db);
		}
		String defaultText = StringUtils.defaultString(auth.getDefaultText());
		if (!defaultText.equals("")) {
			Auth db = new Auth();
			db.setAuth_id("");
			db.setAuth_nm(defaultText);
			authList.add(db);
		}
		
		auth.setUser_auth_id(user_auth_id);
		List<Auth> authNmList = commonService.getAuthNmList(auth);
		authList.addAll(authNmList);
		
		logger.debug(success(authList).toJSONObject().get("rows").toString());
		
		return success(authList).toJSONObject().get("rows").toString();
	}
	
	// 권한목록
	@RequestMapping(value = "/getUsersAuthList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUsersAuthList(@ModelAttribute("auth") Auth auth) throws Exception {
		List<Auth> authList = new ArrayList<Auth>();
		Auth db = new Auth();
		db.setAuth_cd("");
		db.setAuth_nm("권한선택");
		authList.add(db);
		
		List<Auth> authNmList = commonService.getUsersAuthList(auth);
		authList.addAll(authNmList);
		
		logger.debug(success(authList).toJSONObject().get("rows").toString());
		
		return success(authList).toJSONObject().get("rows").toString();
	}
	
	/* 권한목록 */
	@RequestMapping(value = "/getUsersAuthListObject", method = RequestMethod.GET)
	@ResponseBody
	public Result getUsersAuthListObject(@ModelAttribute("auth") Auth auth) throws Exception {
		Result result = new Result();
		
		try {
			List<Auth> authNmList = commonService.getUsersAuthList(auth);
			logger.debug("authNmList.size():" + authNmList.size());
			
			if (authNmList.size() > 0) {
				result.setResult(true);
				result.setObject(authNmList);
				result.setMessage("권한목록 조회 완료");
			} else {
				result.setResult(false);
				result.setObject(authNmList);
				result.setMessage("시스템 접근 권한이 없습니다.<br/>관리자에게 문의바랍니다.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	// /* 튜닝담당자업무 조회 */
	// @RequestMapping(value = "/getWrkJobCd", method = RequestMethod.GET,
	// produces = "application/text; charset=utf8")
	// @ResponseBody
	// public String getWrkJobCd(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd)
	// throws Exception {
	// String returnValue = "";
	// List<WrkJobCd> resultList = commonService.getWrkJobCd(wrkJobCd);
	// List<WrkJobCd> buildList = TreeWrite.buildWrkJobTree(resultList, "-1");
	// JSONArray jsonArray = JSONArray.fromObject(buildList);
	//
	// returnValue = jsonArray.toString();
	//
	// return returnValue;
	// }
	/* 튜닝담당자업무 조회 */
	@RequestMapping(value = "/getWrkJobCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getWrkJobCd(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd) throws Exception {
		String returnValue = "";
		
		List<WrkJobCd> wrkJobCdList = new ArrayList<WrkJobCd>();
		String defaultText = StringUtils.defaultString(wrkJobCd.getDefaultText());
		
		if (!defaultText.equals("")) {
			WrkJobCd cd = new WrkJobCd();
			cd.setId("");
			cd.setText(defaultText);
			wrkJobCdList.add(cd);
		}
		
		List<WrkJobCd> resultList = commonService.getWrkJobCd(wrkJobCd);
		List<WrkJobCd> buildList = TreeWrite.buildWrkJobTree(resultList, "-1");
		wrkJobCdList.addAll(buildList);
		
		JSONArray jsonArray = JSONArray.fromObject(wrkJobCdList);
		
		returnValue = jsonArray.toString();
		
		return returnValue;
	}
	
	@RequestMapping(value = "/getUsersWrkJobCdList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUsersWrkJobCdList(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd) throws Exception {
		String returnValue = "";
		List<WrkJobCd> wrkJobCdList = new ArrayList<WrkJobCd>();
		WrkJobCd cd = new WrkJobCd();
		
		cd.setWrkjob_cd("");
		cd.setWrkjob_cd_nm("업무선택");
		wrkJobCdList.add(cd);
		logger.debug("wrkJobCdList:" + wrkJobCdList);
		
		List<WrkJobCd> resultList = commonService.getUsersWrkJobCdList(wrkJobCd);
		wrkJobCdList.addAll(resultList);
		
		JSONArray jsonArray = JSONArray.fromObject(wrkJobCdList);
		
		returnValue = jsonArray.toString();
		
		return returnValue;
	}
	
	/* 업무목록 */
	@RequestMapping(value = "/getUsersWrkJobCdObject", method = RequestMethod.GET)
	@ResponseBody
	public Result getUsersWrkJobCdObject(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd) throws Exception {
		Result result = new Result();
		try {
			List<WrkJobCd> wrkJobCdList = commonService.getUsersWrkJobCdList(wrkJobCd);
			if(wrkJobCdList == null || wrkJobCdList.size() == 0)
				throw new Exception("담당업무가 없습니다.<BR/>Open-POP 관리자에게 문의하시기 바랍니다.");
			result.setResult(wrkJobCdList.size() > 0 ? true : false);
			result.setObject(wrkJobCdList);
			result.setMessage("업무목록 조회 완료");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* 업무리더 존재여부 체크 */
	@RequestMapping(value = "/CheckWorkJobLeader", method = RequestMethod.POST)
	@ResponseBody
	public Result CheckWorkJobLeader(@ModelAttribute("userWrkjob") UserWrkjob userWrkjob, Model model) {
		Result result = new Result();
		UserWrkjob temp = new UserWrkjob();
		
		try {
			int check = 0;
			check = commonService.checkWrkjobLeaderCnt(userWrkjob);
			
			if(check > 0){
				//유저 정보가 있는경우
				if(check > 1){ //업무리더가 다수일경우는 있으면 안되겠지만 있을 경우를 대비.
					throw new Exception("해당업무에 업무리더가 이미 존재하여<BR/>업무리더를 선택할 수 없습니다");
				}
				
				temp = commonService.checkWorkJobLeader(userWrkjob);
				result.setResult(false);
				result.setTxtValue(temp.getWrkjob_cd());
				result.setMessage("해당 업무[" + temp.getWrkjob_nm() + "]의<br/>리더는 [ " + temp.getUser_nm() + "("
						+ temp.getUser_id() + ") ] 입니다.");
			}else{
				//유저가 정보가 없는경우
				result.setResult(true);
				result.setTxtValue("");
				result.setMessage("해당 업무[" + userWrkjob.getWrkjob_nm() + "]의 리더는<BR/> 아직 존재하지 않습니다.<BR/> 리더로 요청하시겠습니까?");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/CheckWorkJobLeader2", method = RequestMethod.POST)
	@ResponseBody
	public Result CheckWorkJobLeader2(@ModelAttribute("userWrkjob") UserWrkjob userWrkjob, Model model) {
		Result result = new Result();
		UserWrkjob temp = new UserWrkjob();
		
		try {
			int check = 0;
			check = commonService.checkWrkjobLeaderCnt(userWrkjob);
			
			if(check > 0){
				//유저 정보가 있는경우
					throw new Exception("해당업무에 업무리더가 이미 존재하여<BR/>업무리더를 선택할 수 없습니다.");
			}else{
				//유저가 정보가 없는경우
				result.setResult(true);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 권한 조회 */
	@RequestMapping(value = "/getAuth", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuth(@ModelAttribute("auth") Auth auth) throws Exception {
		List<Auth> resultList = new ArrayList<Auth>();
		
		try {
			resultList = commonService.getAuth(auth);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " exception ==> " + ex.getMessage());
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String denied(Principal user, Model model) {
		String error = "";
		
		if (user != null) {
			error = "Hi " + user.getName() + ", you do not have permission to access this page!";
		} else {
			error = "You do not have permission to access this page!";
		}
		
		model.addAttribute("error", error);
		
		return "error/error_403";
	}
	
	/* 로그인 권한 변경 */
	@RequestMapping(value="/defaultLoginRole", method=RequestMethod.GET)
	public String defaultLoginRole(@ModelAttribute("users") Users users, Model model) {
		
		String login_user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String userpwd = SessionManager.getLoginSession().getUsers().getPassword();
		model.addAttribute("login_user_id", login_user_id);
		model.addAttribute("userpwd", userpwd);
		model.addAttribute("menu_id", users.getMenu_id());
		model.addAttribute("menu_nm", users.getMenu_nm());
		model.addAttribute("pass_decrypt","Y");
		
		return "login/defaultLoginRole";
	}
	
	@Autowired
	private TestService testService;
	
//	@RequestMapping(value = "/urlTest", produces="application/text; charset=utf8")
	@RequestMapping(value = "/urlTest", produces = "application/json; charset=utf8")
	@ResponseBody
	public String urlTest(Model model) {
		
		try {
			int result = testService.urlTest();
			logger.error("result ==> " + result);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
		}
		return "{\"success\":1}";
	}
	
	@RequestMapping(value = "/SessionOut")
	@ResponseBody
	public Result SessionOut(@ModelAttribute("users") Users users, HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		int check = 0;
		try {
			
			check = watchLogService.WatchLoginUsersFail();
			result.setResult(check > 0 ? true : false);
			
			HttpSession session = request.getSession(false);
			if (request.isRequestedSessionIdValid() && session != null) {
				logger.debug("############## Session Out ##############");
				session.invalidate();
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			result.setResult(false);
			result.setMessage(ex.getMessage());
			System.out.println("ex.getMessage :::::::::" +ex.getMessage());
		}
		
		return result;
	}
	
	/* 사용자 관리 - 사용자 비밀번호 확인*/
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
			String new_user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			String new_password= decryptRsa(privateKey, users.getPassword());
			
			users.setUser_id(new_user_id);
			users.setPassword(new_password);
			
			
			boolean check = systemMngService.checkPassword(users);
			result.setResult(check);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	
	/* 시스템 관리 - 사용자 정보 */
	@RequestMapping(value="/UserInfo", method=RequestMethod.GET)
	public String UserInfo(@ModelAttribute("users") Users users, Model model, HttpServletRequest req) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		model.addAttribute("menu_id", users.getMenu_id());
		model.addAttribute("menu_nm", users.getMenu_nm());
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");  
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		
		initRsa(req);
		
		try {
			
			users.setUser_id(user_id);
			users = systemMngService.getUserInfo(users);
			
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
	}
	
	/* 사용자 관리 - 사용자 비밀번호 수정*/
	@RequestMapping(value="/UserPassword/Change", method=RequestMethod.POST)
	@ResponseBody
	public Result changeUserPassword(@ModelAttribute("users") Users users,  Model model, HttpServletRequest req) throws Exception {
		Result result = new Result();
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");  
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		
		HttpSession session = req.getSession();
		PrivateKey privateKey = (PrivateKey) session.getAttribute(LoginController.RSA_WEB_KEY);
		
		try {
			String new_user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			String new_password= decryptRsa(privateKey, users.getNew_password());
			
			users.setUser_id(new_user_id);
			users.setNew_password(new_password);
			
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
	}	
	
	/********************* GS 인증 RSA 보안 이슈 해결 **********************/
	
	/**
	 * 복호화
	 * 
	 * @param privateKey
	 * @param securedValue
	 * @return
	 * @throws Exception
	 */
	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
		Cipher cipher = Cipher.getInstance(LoginController.RSA_INSTANCE);
		byte[] encryptedBytes = hexToByteArray(securedValue);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
		return decryptedValue;
	}
	
	/**
	 * 16진 문자열을 byte 배열로 변환한다.
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }
		
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}
	
	/**
	 * rsa 공개키, 개인키 생성
	 * 
	 * @param request
	 */
	public void initRsa(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		KeyPairGenerator generator;
		String publicKeyModulus = "";
		String publicKeyExponent = "";
		
		try {
			generator = KeyPairGenerator.getInstance(LoginController.RSA_INSTANCE);
			generator.initialize(1024);
			
			KeyPair keyPair = generator.genKeyPair();
			KeyFactory keyFactory = KeyFactory.getInstance(LoginController.RSA_INSTANCE);
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			
			session.setAttribute(LoginController.RSA_WEB_KEY, privateKey); // session에 RSA 개인키를 세션에 저장
			
			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			publicKeyModulus = publicSpec.getModulus().toString(16);
			publicKeyExponent = publicSpec.getPublicExponent().toString(16);
			
			//System.out.println("################ GS 인증 RSAModulus/RSAExponent :::::"+ publicKeyModulus + "/" + publicKeyExponent);
			request.setAttribute("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
			request.setAttribute("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
			
			session.setAttribute("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
			session.setAttribute("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return publicKeyModulus+"/"+publicKeyExponent;
	}
	
	private void initCSRF(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		String uuid = UUID.randomUUID().toString();
		
		request.setAttribute("CSRF_TOKEN", uuid);
		session.setAttribute("CSRF_TOKEN", uuid);
		
		logger.debug("//////////////////////////////////////////////////////////");
		logger.debug("session CSRF_TOKEN[" + session.getAttribute("CSRF_TOKEN") + "] request CSRF_TOKEN[" + request.getAttribute("CSRF_TOKEN") + "]");
	}
}