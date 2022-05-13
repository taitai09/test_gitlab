package omc.spop.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {
//	private static final Logger logger = LoggerFactory.getLogger(RequestWrapper.class);
	
	public RequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}
	
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		
		if (values==null)  {
			return null;
		}
		
		int count = values.length;
		String[] encodedValues = new String[count];
		
		for (int i = 0; i < count; i++) {
//			logger.debug("getParameterValues to " + values[i]);
			encodedValues[i] = cleanXSS(values[i]);
		}
		
		return encodedValues;
	}
	
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		
		if (value == null) {
			return null;
		}
		
//		logger.debug("getParameter to " + value);
		
		return cleanXSS(value);
	}
	
	public String getHeader(String name) {
		String value = super.getHeader(name);
		
		if (value == null)
			return null;
		
//		logger.debug("getHeader to " + value);
		return cleanXSS(value);
	}
	
	private String cleanXSS(String value) {
		//You'll need to remove the spaces from the html entities below
//		value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");		// menu_nm에서 치환이 되는 문제가 발생됨
//		value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");	// menu_nm에서 치환이 되는 문제가 발생됨
//		value = value.replaceAll("'", "& #39;");								// menu_nm에서 치환이 되는 문제가 발생됨
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("script", "");
//		value = value.toLowerCase().replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//		value = value.toLowerCase().replaceAll("script", "");
		return value;
	}
}
