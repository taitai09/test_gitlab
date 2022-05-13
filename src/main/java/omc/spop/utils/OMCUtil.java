package omc.spop.utils;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.ui.Model;

import omc.spop.model.Result;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 * 2020.08.18 이재우 기능개선
 **********************************************************/
public class OMCUtil {
	public static Model convertMapToModel(Map<String, Object> map, Model model) {
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			model.addAttribute(key, map.get(key));
		}
		return model;
	}
	
	/**
	 * Check license as end.Date in default.properties
	 * Using the same module as the LoginController And MainController 
	 * @param model
	 * @param endDate
	 * @return model
	 */
	public static Model checkLicensePeried(Model model, String endDate) {
		
		if(endDate == null) {
			model.addAttribute("result", true);
			model.addAttribute("resultMessage", "");
			model.addAttribute("resultStatus", "");
		} else if ( endDate != null && endDate != "") {
			///////////////////////////////////////////////////////
			// You cannot log in if the license period is exceeded.
			///////////////////////////////////////////////////////
			Result result = new Result();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String sysDate = format.format(new java.util.Date());
			final String LICENSE_LIMIT_STATUS = "LICENSE_LIMIT";
			
			if ( sysDate.compareTo(endDate) > 0 ) {
				result.setResult(false);
				result.setStatus(LICENSE_LIMIT_STATUS);
				result.setMessage("라이센스가 종료되었습니다.<br> 제품 제공업체에 문의하시기 바랍니다.");
				
				model.addAttribute("result", result.getResult());
				model.addAttribute("resultMessage", result.getMessage());
				model.addAttribute("resultStatus", result.getStatus());
				
			} else {
				result.setResult(true);
				result.setStatus("");
				result.setMessage("");
				
				model.addAttribute("result", result.getResult());
				model.addAttribute("resultMessage", result.getMessage());
				model.addAttribute("resultStatus", result.getStatus());
			}
		}
		
		return model;
	}
}
