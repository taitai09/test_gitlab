package omc.spop.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrossScriptingFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(CrossScriptingFilter.class);
	
	public FilterConfig filterConfig;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hRequest = (HttpServletRequest) request;
		String uri = hRequest.getRequestURI();
		
		if(ignoreURI(uri)) {
			logger.debug("This URI [" + uri + "] is ignored.");
			chain.doFilter(request, response);
		} else {
			if(!requestDataChk(request)) {
				
				logger.debug("====================requestDataChk Fail");
				
				HttpServletResponse res = (HttpServletResponse) response;
				res.setStatus(500);
				res.setContentType("application/json");
				
				PrintWriter out = res.getWriter();
				out.print("{'result':'error','msg':'test'}");
				out.flush();
				
				return;
				
			}
			
			ServletRequest req = new RequestWrapper((HttpServletRequest) request);
			
			logger.debug("req[" + req + "]");
			
			if( req != null && "FAIL".equals(req.toString())) {
				HttpServletResponse res = (HttpServletResponse) response;
				res.setStatus(200);
				res.setContentType("application/json");
				
				PrintWriter out = res.getWriter();
				out.print("{'result':'error','msg':'test'}");
				out.flush();
				
				return;
			}
			
			chain.doFilter(req, response);
		}
	}
	
	@Override
	public void destroy() {
		this.filterConfig = null;
	}
	
	private boolean ignoreURI(String uri) {
		List ignoreUriList = new ArrayList();
		boolean flag = false;
		
		ignoreUriList.add("/PerfCheckResultList/createPerfChkResultTab2BodySelect");	// 배포전 성능점검 > 성능 점검 관리 > 성능 점검 결과 탭 > 프로그램 정보 탭
		ignoreUriList.add("/PerfCheckResultList/selectDeployPerfChkExecBindList");		// 배포전 성능점검 > 성능 점검 관리 > 성능 점검 결과 탭 > 프로그램 정보 탭 > 바인드 변수 정보
		ignoreUriList.add("/Mqm/QualityInspectionJob/Save");							// DB 구조 품질 점검 > 구조 품질점검 작업 > 품질검토작업 버튼
		ignoreUriList.add("/Sqls/convertPerfChkResult");		//  성능 검증 > SQL 성능 추적 현황 > 성능 점검 SQL > SQLs 탭 > 프로그램 정보 탭
		ignoreUriList.add("/perfInspectMng/programInfoBodySelect");						// 실행 SQL 성능 검증 > 성능 검증 관리 > 성능 검증 결과 탭 > 프로그램 정보 탭
		
		for(int index = 0; index < ignoreUriList.size(); index++) {
			if(ignoreUriList.get(index).equals(uri)) {
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	@SuppressWarnings("unused")
	private boolean requestDataChk(ServletRequest request) {
		String name="";
		Enumeration<?> params = request.getParameterNames();
		
		while(params.hasMoreElements()) {
			name = (String) params.nextElement();
			System.out.println(name + " : " + request.getParameter(name) + "     ");
			
			if(!isCheck(request.getParameter(name))) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isCheck(String value) {
		boolean flag = true;
		String evalue = value.replace(" ", "");
		
		if(value.indexOf("eval\\((.*)\\)") >= 0) {
			flag = false;
//		} else if(value.toLowerCase().indexOf("[\\\\\\\"\\\\\\'][\\\\s]*javascript:(.*)[\\\\\\\"\\\\\\']") > 0) {
		} else if(value.toLowerCase().indexOf("javascript:") >= 0) {
			flag = false;
		} else if(evalue.toLowerCase().indexOf("<script") >= 0) {
			flag = false;
		} else if(value.toLowerCase().indexOf(" onmouseover") >= 0) {
			flag = false;
		} else if(evalue.toLowerCase().indexOf("'onmouseover") >= 0) {
			flag = false;
		} else if(evalue.toLowerCase().indexOf("\"onmouseover") >= 0) {
			flag = false;
		} else if(evalue.toLowerCase().indexOf("'+netsparker") >= 0) {
			flag = false;
		}
		
		return flag;
	}	
}
