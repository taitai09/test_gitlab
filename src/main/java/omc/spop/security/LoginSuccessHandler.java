package omc.spop.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import omc.spop.base.SessionManager;

/***********************************************************
 * 2017.08.10	이원식	최초작성
 **********************************************************/

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler { 
	private static final Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
		String loginid = SessionManager.getLoginSession().getUsers().getUser_id();
		String defaultPwd = SessionManager.getLoginSession().getUsers().getDefault_password_yn();
		String approveYn = SessionManager.getLoginSession().getUsers().getApprove_yn();
		
		if(approveYn.equals("Y")){
			if(defaultPwd.equals("Y")){
				logger.debug("# LoginSuccessHandler approveYn is Y ########");
				getRedirectStrategy().sendRedirect(request, response, "/auth/login?error=chgPwd&tempId="+loginid);
			}else{
				logger.debug("# LoginSuccessHandler 로그인 성공 ########");
				logger.info("# 로그인 성공 ########");
				super.onAuthenticationSuccess(request, response, auth);				
			}			
		}else{ // 미승인 경우
			logger.debug("# LoginSuccessHandler 미승인 접속 ########");
			logger.info("# 미승인 접속 # : " + loginid+"[ "+loginid+" ]");			
			getRedirectStrategy().sendRedirect(request, response, "/auth/login?error=access");						
		}
	}
} 
