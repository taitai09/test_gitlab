package omc.spop.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import omc.spop.model.Login;

/**
 * AJAX 요청시 권한 관련 오류가 생기면 redirect 시키는데, AJAX는 HTTP 상태 코드를 이용해서 에러를 확인해야 하므로,
 * redirect 되기전에 상태 코드를 전송하게함.
 * 
 * @author Miracle
 * 
 */
public class AjaxSessionTimeoutFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(AjaxSessionTimeoutFilter.class);

	/**
	 * Default AJAX request Header
	 */
	private String ajaxHeader = "AJAX";

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		response.setContentType("application/octet-stream");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// logger.debug("log out
		// start=====================================================================");
		// logger.debug("isAjaxRequest(req):" + isAjaxRequest(req));
		String[] str1 = { "/", "/favicon.ico" };
		// String[]
		// str2={"/auth/","/login/","/WEB-INF/jsp/login/","/WebSocket/","/resources/","/error/"};
		String[] str2 = {"/sso/", "/html/", "/auth/", "/Common/", "/login/", "/WEB-INF/jsp/sso/", "/WEB-INF/jsp/login/","/WEB-INF/jsp/performanceCheckMng/", "/resources/", "/error/",
				"/WEB-INF/jsp/include/", "/Popup","/iqms","/wbn", "/PerfCheckResultList", "/ssopi", "/stpi"};
		// logger.debug("path="+req.getRequestURI());

		String requestURI = req.getRequestURI();
		logger.debug("requestURI===>" + requestURI);
		String queryString = StringUtils.defaultString(req.getQueryString());
		if(!queryString.equals("")) queryString = "?"+queryString;
		logger.debug("queryString===>" + queryString);

		Enumeration<String> paramNames = req.getParameterNames();
		HttpSession session = req.getSession(true);
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String paramValue = req.getParameter(paramName);
//			logger.debug("paramName=="+paramName+" paramValue=="+paramValue);
			session.setAttribute(paramName, paramValue);
		}

		for (int i = 0; i < str1.length; i++) {
			if (requestURI.equals(str1[i])) {
				 logger.debug("str11="+str1[i]);
				chain.doFilter(req, res);
				return;
			}
		}

		for (int i = 0; i < str2.length; i++) {
			if (requestURI.startsWith(str2[i])) {
				logger.info("str2 ="+requestURI);
				if (requestURI.contains("toplogout.jsp")) {
					res.sendRedirect("/"+queryString);
					return;
				} else {
					 logger.debug("str22 ="+requestURI);
					chain.doFilter(req, res);
					return;
				}
			}
		}

		// logger.debug("usrcheck ="+requestURI);
		SecurityContext securityContext = null;
		securityContext = SecurityContextHolder.getContext();
		Login login = null;
		try {
			login = (Login) securityContext.getAuthentication().getPrincipal();
			 logger.debug("login="+login);
			String user_id = login.getUsers().getUser_id();
			// logger.debug("user_id="+user_id);
			if (user_id == null) {
				logger.info("user_id is null >> /auth/toplogout");
				res.sendRedirect("/auth/toplogout");
			} else {
				logger.debug("user_id >> chain.doFilter");
				chain.doFilter(req, res);
			}

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error("doFilter : ", sw.toString());
			// logger.debug("2/auth/toplogout");
//			res.sendRedirect("/auth/toplogout");
			res.sendRedirect("/auth/logout");
			// e.printStackTrace();
		}

		// try {
		// chain.doFilter(req, res);
		// }catch(AccessDeniedException e){
		// res.sendError(HttpServletResponse.SC_FORBIDDEN);
		// } catch (AuthenticationException e) {
		// res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		// }

	}

	private boolean isAjaxRequest(HttpServletRequest req) {
		return req.getHeader(ajaxHeader) != null && req.getHeader(ajaxHeader).equals(Boolean.TRUE.toString());
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * Set AJAX Request Header (Default is AJAX)
	 * 
	 * @param ajaxHeader
	 */
	public void setajaxHeader(String ajaxHeader) {
		this.ajaxHeader = ajaxHeader;
	}
}
