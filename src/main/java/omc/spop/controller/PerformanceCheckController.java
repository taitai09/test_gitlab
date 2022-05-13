package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.BasicCheckConfig;
import omc.spop.model.DbCheckException;
import omc.spop.model.Result;
import omc.spop.service.PerformanceCheckService;

/***********************************************************
 * 2018.09.11 	임호경	 최초작성
 **********************************************************/

@Controller
public class PerformanceCheckController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckController.class);

	@Autowired
	private PerformanceCheckService performanceCheckService;


	@RequestMapping(value = "/PerformanceCheck", method = RequestMethod.GET)
	public String PerformanceCheck(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", basicCheckConfig.getMenu_id() );
		model.addAttribute("menu_nm", basicCheckConfig.getMenu_nm() );
		
		return "performanceCheckMng/performanceCheck";
	}
	
	
	
	/*  BASICCHECK -리스트 */
	@RequestMapping(value="/getPerformanceBasicCheckList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getPerformanceBasicCheckList(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig, Model model) {
		List<BasicCheckConfig> resultList = new ArrayList<BasicCheckConfig>();

		try{
			resultList = performanceCheckService.getPerformanceBasicCheckList(basicCheckConfig);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/*  DBCHECK -리스트 */
	@RequestMapping(value="/getPerformanceDBCheckList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String getPerformanceDBCheckList(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig, Model model) {
		List<BasicCheckConfig> resultList = new ArrayList<BasicCheckConfig>();
		
		try{
			resultList = performanceCheckService.getPerformanceDBCheckList(basicCheckConfig);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/*  getGradeCd -셀렉트 */
	@RequestMapping(value="/getGradeCd", method=RequestMethod.GET, produces="application/text; charset=utf8")
	@ResponseBody
	public String getGradeCd(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig, Model model) {
		List<BasicCheckConfig> resultList = new ArrayList<BasicCheckConfig>();
		
		try{
			resultList = performanceCheckService.getGradeCd(basicCheckConfig);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	/*  getClassDivCd -셀렉트 */
	@RequestMapping(value="/getClassDivCd", method=RequestMethod.GET, produces="application/text; charset=utf8")
	@ResponseBody
	public String getClassDivCd(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig, Model model) {
		List<BasicCheckConfig> resultList = new ArrayList<BasicCheckConfig>();
		
		try{
			resultList = performanceCheckService.getClassDivCd(basicCheckConfig);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	/*  getCheckPrefValue -셀렉트 */
	@RequestMapping(value="/getCheckPrefValue", method=RequestMethod.GET, produces="application/text; charset=utf8")
	@ResponseBody
	public String getCheckPrefValue(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig, Model model) {
		List<BasicCheckConfig> resultList = new ArrayList<BasicCheckConfig>();
		
		try{
			resultList = performanceCheckService.getCheckPrefValue(basicCheckConfig);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	
	/* BasicCheckConfig 관리 - SaveBasicCheckConfig */
	@RequestMapping(value="/SaveBasicCheckConfig", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveBasicCheckConfig(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig,  Model model) {
		
		
		Result result = new Result();
		try{
			basicCheckConfig.setCheck_pref_nm(basicCheckConfig.getCheck_pref_nm().replaceAll(",", ""));
			performanceCheckService.SaveBasicCheckConfig(basicCheckConfig);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* DBCheckConfig 관리 - SaveDBCheckConfig */
	@RequestMapping(value="/SaveDBCheckConfig", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveDBCheckConfig(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig,  Model model) {
		Result result = new Result();
//		basicCheckConfig.setCheck_class_div_cd(StringUtils.defaultString(basicCheckConfig.getDefault_threshold_value(),"0"));

		try{
			performanceCheckService.SaveDBCheckConfig(basicCheckConfig);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		
		return result;	
	}
	/* DBCheckConfig 삭제 - deleteDBCheckConfig */
	@RequestMapping(value="/deleteDBCheckConfig", method=RequestMethod.POST)
	@ResponseBody
	public Result deleteDBCheckConfig(@ModelAttribute("basicCheckConfig") BasicCheckConfig basicCheckConfig,  Model model) {
		Result result = new Result();
		
		logger.debug("!!DBCheckConfig : " + basicCheckConfig);
		try{
			performanceCheckService.deleteDBCheckConfig(basicCheckConfig);
			result.setResult(true);
			
			basicCheckConfig.setCheck_pref_nm(basicCheckConfig.getCheck_pref_nm().replaceAll(",", ""));
			result.setMessage("성공적으로 [ "+ basicCheckConfig.getCheck_pref_nm() + " ] 이(가) 삭제되었습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	
	@RequestMapping(value = "/PerformanceCheckException", method = RequestMethod.GET)
	public String PerformanceCheckException(@ModelAttribute("dbCheckException") DbCheckException dbCheckException, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);

		return "performanceCheckMng/performanceCheckException";
	}
	
	

}
