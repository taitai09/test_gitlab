package omc.spop.base;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import omc.spop.model.Login;

/***********************************************************
 * 2017.02.01 이원식 최초작성
 **********************************************************/

public class SessionManager {

	/**
	 * 세션의 모든 정보를 가져온다.
	 */
	public static Login getLoginSession() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Object principal = securityContext.getAuthentication().getPrincipal();
		return (Login) principal;
	}
	
	public static Object getLoginSessionObject() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Object principal = securityContext.getAuthentication().getPrincipal();
		return principal;
	}
	
}