package omc.spop.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.model.Result;
import omc.spop.model.Users;
import omc.spop.service.CommonService;


@Controller
public class SSOController {
	private static final Logger logger = LoggerFactory.getLogger(SSOController.class);

	@Value("#{defaultConfig['customer']}")
	private String customer;
	
	@Autowired
	private CommonService commonService;

	//SSO SERVER 에서 승인을 받는 페이지
	@RequestMapping(value = "/sso/checkauth", method = {RequestMethod.GET, RequestMethod.POST })
	public String Checkauth(@RequestParam(value = "error", required = false) String error, Model model) {
		return "sso/checkauth";	
	}
	@RequestMapping(value = "/sso/error", method = RequestMethod.GET)
	public String Error(@RequestParam(value = "error", required = false) String error, Model model) {
		return "sso/error";	
	}
	//OPENPOP 으로 REDIRECT 하는 페이지
	@RequestMapping(value = "/sso/return", method = RequestMethod.GET)
	public String Return(@RequestParam(value = "error", required = false) String error, Model model) {
		return "sso/return";	
	}
	//SSO SERVER를 호출하는 페이지
	@RequestMapping(value = "/sso/business", method = RequestMethod.GET)
	public String Business(@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "deploy_id", required = false) String deploy_id,  
			Model model, HttpServletResponse response, HttpServletRequest request) {
	
		logger.debug("### for iqms deploy_id : ",deploy_id);
		Cookie cookie = new Cookie("deploy_id", deploy_id);
	    cookie.setMaxAge(60);                 	   // 쿠키 유지 기간 - 1분
	    cookie.setPath("/");                       // 모든 경로에서 접근 가능하도록 
	    response.addCookie(cookie);                // 쿠키저장
		
	    return "sso/business";	
	}
	
	@RequestMapping(value = "/sso/logout", method = RequestMethod.GET)
	public String Logout(@RequestParam(value = "error", required = false) String error, Model model) {
		return "sso/logout";	
	}
	
	@RequestMapping(value = "/sso/simpleLoginCheckBySSO", method = RequestMethod.GET)
	@ResponseBody
	public Result SimpleLoginCheckBySSO(Model model, @RequestParam("user_id") String user_id, 
			HttpServletRequest req, HttpServletResponse res) throws UnsupportedEncodingException {
		
		Result result = new Result();
		Users user = new Users();
		Users resultUser = new Users();
		//StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		//pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		//pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)

		try {
			user.setUser_id(user_id);
			resultUser = commonService.getSimpleUserInfo(user);
			if (resultUser != null && resultUser.getUser_id() != null) {
				//유저 정보가 있을 경우.
				//String userpwd = pbeEnc.decrypt(resultUser.getPassword());
				//req.setAttribute("userpassword", userpwd);
				//if (userpwd.equals(pbeEnc.decrypt(resultUser.getPassword()))) {
					
					result.setResult(true);
					result.setObject(resultUser);
				//}
			} else {
				result.setResult(false);
				result.setMessage("등록된 사용자가 없습니다.<br/>관리자에게 문의해주세요.");
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
}
