package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.base.InterfaceController;
import omc.spop.model.BeforeAccidentCheck;
import omc.spop.model.DeployPerfCheck;
import omc.spop.model.Result;
import omc.spop.service.PreDeploymentCheckService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class PreDeploymentCheckController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(PreDeploymentCheckController.class);
	
	@Autowired
	private PreDeploymentCheckService preDeploymentCheckService;

	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	

	/* 소스점검  */
	@RequestMapping(value="/SourceCheck")
	public String SourceCheck(@ModelAttribute("beforeAccidentCheck") BeforeAccidentCheck beforeAccidentCheck, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(beforeAccidentCheck.getStrStartDt() == null || beforeAccidentCheck.getStrStartDt().equals("")){
			beforeAccidentCheck.setStrStartDt(startDate);
		}
		
		if(beforeAccidentCheck.getStrEndDt() == null || beforeAccidentCheck.getStrEndDt().equals("")){
			beforeAccidentCheck.setStrEndDt(nowDate);
		}
		
		model.addAttribute("menu_id", beforeAccidentCheck.getMenu_id());
		model.addAttribute("menu_nm", beforeAccidentCheck.getMenu_nm());
		
		return "preDeploymentCheck/sourceCheck";
	}	
	
	/* 소스점검 list action */
	@RequestMapping(value="/SourceCheckAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SourceCheckAction(@ModelAttribute("beforeAccidentCheck") BeforeAccidentCheck beforeAccidentCheck, Model model) {
		List<BeforeAccidentCheck> resultList = new ArrayList<BeforeAccidentCheck>();

		try{
			resultList = preDeploymentCheckService.sourceCheckList(beforeAccidentCheck);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 소스점검 - 상세  */
	@RequestMapping(value="/SourceCheck/View")
	public String SourceCheckView(@ModelAttribute("beforeAccidentCheck") BeforeAccidentCheck beforeAccidentCheck, Model model) {
		BeforeAccidentCheck result = null;
		try{
			result = preDeploymentCheckService.sourceCheckView(beforeAccidentCheck);
			model.addAttribute("result", result);
			model.addAttribute("menu_id", result.getMenu_id());
			model.addAttribute("menu_nm", beforeAccidentCheck.getMenu_nm());
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}		
		return "preDeploymentCheck/sourceCheckView";
	}
	
	/* 소스점검 - 점검결과 UPDATE */
	@RequestMapping(value="/SourceCheck/Update", method=RequestMethod.POST)
	@ResponseBody
	public Result SourceCheckUpdate(@ModelAttribute("beforeAccidentCheck") BeforeAccidentCheck beforeAccidentCheck, Model model) {
		Result result = new Result();
		
		try {				
			preDeploymentCheckService.updateSourceCheck(beforeAccidentCheck);
			result.setResult(true);
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 애플리케이션 성능점검  */
	@RequestMapping(value="/ApplicationCheck")
	public String ApplicationCheck(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(deployPerfCheck.getStrStartDt() == null || deployPerfCheck.getStrStartDt().equals("")){
			deployPerfCheck.setStrStartDt(startDate);
		}
		
		if(deployPerfCheck.getStrEndDt() == null || deployPerfCheck.getStrEndDt().equals("")){
			deployPerfCheck.setStrEndDt(nowDate);
		}
		
		model.addAttribute("menu_id", deployPerfCheck.getMenu_id() );
		model.addAttribute("call_from_parent", deployPerfCheck.getCall_from_parent() );
		model.addAttribute("call_from_child", deployPerfCheck.getCall_from_child() );

		return "preDeploymentCheck/applicationCheck";
	}
	
	/* 애플리케이션 성능점검 Action */
	@RequestMapping(value="/ApplicationCheckAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ApplicationCheckAction(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		List<DeployPerfCheck> resultList = new ArrayList<DeployPerfCheck>();

		try{
			resultList = preDeploymentCheckService.applicationCheckList(deployPerfCheck);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 애플리케이션 성능점검요청 */
	@RequestMapping(value="/ApplicationRequestCheck", method=RequestMethod.POST)
	@ResponseBody
	public Result ApplicationRequestCheck(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		Result result = new Result();
		
		//logger.debug("deployPerfCheck:"+success(deployPerfCheck).toJSONObject().toString());
		deployPerfCheck.setDeploy_perf_check_type_cd("1");

		try{
			preDeploymentCheckService.applicationRequestCheck(deployPerfCheck);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 애플리케이션 성능점검  상세내역 */
	@RequestMapping(value="/ApplicationDetailCheck")
	public String ApplicationDetailCheck(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		model.addAttribute("call_from_parent", deployPerfCheck.getCall_from_parent() );
		model.addAttribute("call_from_child", deployPerfCheck.getCall_from_child() );
		model.addAttribute("menu_id", deployPerfCheck.getMenu_id());
		return "preDeploymentCheck/applicationDetailCheck";
	}	
	
	/* 애플리케이션 성능점검 상세내역 Action */
	@RequestMapping(value="ApplicationDetailCheckAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ApplicationDetailCheckAction(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		List<DeployPerfCheck> resultList = new ArrayList<DeployPerfCheck>();

		try{
			resultList = preDeploymentCheckService.applicationDetailCheckList(deployPerfCheck);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 애플리케이션 DBIO 성능점검  상세내역 */
	@RequestMapping(value="/ApplicationDBIOCheck")
	public String ApplicationDBIOCheck(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {	
		model.addAttribute("call_from_parent", deployPerfCheck.getCall_from_parent() );
		model.addAttribute("call_from_child", deployPerfCheck.getCall_from_child() );
		model.addAttribute("menu_id", deployPerfCheck.getMenu_id());
		return "preDeploymentCheck/applicationDbioCheck";
	}	
	
	/* 애플리케이션 DBIO 성능점검 상세내역 Action */
	@RequestMapping(value="/ApplicationDBIOCheckAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ApplicationDBIOCheckAction(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		List<DeployPerfCheck> resultList = new ArrayList<DeployPerfCheck>();

		try{
			resultList = preDeploymentCheckService.applicationDBIOCheckList(deployPerfCheck);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* DBIO 성능점검  */
	@RequestMapping(value="/DBIOCheck")
	public String DBIOCheck(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(deployPerfCheck.getStrStartDt() == null || deployPerfCheck.getStrStartDt().equals("")){
			deployPerfCheck.setStrStartDt(startDate);
		}
		
		if(deployPerfCheck.getStrEndDt() == null || deployPerfCheck.getStrEndDt().equals("")){
			deployPerfCheck.setStrEndDt(nowDate);
		}
		
		model.addAttribute("menu_id", deployPerfCheck.getMenu_id() );
		model.addAttribute("call_from_child", deployPerfCheck.getCall_from_child());
		
		return "preDeploymentCheck/dbioCheck";
	}
	
	/* DBIO 성능점검 Action */
	@RequestMapping(value="/DBIOCheckAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DBIOCheckAction(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		List<DeployPerfCheck> resultList = new ArrayList<DeployPerfCheck>();

		try{
			resultList = preDeploymentCheckService.dbioCheckList(deployPerfCheck);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* DBIO 성능점검요청 */
	@RequestMapping(value="/DBIORequestCheck", method=RequestMethod.POST)
	@ResponseBody
	public Result DBIORequestCheck(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		Result result = new Result();
		
		//logger.debug("deployPerfCheck:"+success(deployPerfCheck).toJSONObject().toString());
		deployPerfCheck.setDeploy_perf_check_type_cd("2");
		
		try{
			preDeploymentCheckService.dbioRequestCheck(deployPerfCheck);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* DBIO 성능점검  상세내역 */
	@RequestMapping(value="/DBIODetailCheck")
	public String DBIODetailCheck(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		model.addAttribute("menu_id", deployPerfCheck.getMenu_id());
		return "preDeploymentCheck/dbioDetailCheck";
	}	
	
	/* DBIO 성능점검 상세내역 Action */
	@RequestMapping(value="/DBIODetailCheckAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DBIODetailCheckAction(@ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck,  Model model) {
		List<DeployPerfCheck> resultList = new ArrayList<DeployPerfCheck>();

		try{
			resultList = preDeploymentCheckService.dbioDetailCheckList(deployPerfCheck);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 성능점검 엑셀파일 업로드 */
	@RequestMapping(value = "/ExcelPerformanceCheck/Upload", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result UploadExcelFile(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("deployPerfCheck") DeployPerfCheck deployPerfCheck, Model model) {
		Result result = new Result();
		
		if (!file.isEmpty()) {
			if(file.getSize() > maxUploadSize){
		    	result.setResult(false);
		    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
		    }else{
				try {
					List<DeployPerfCheck> resultList = new ArrayList<DeployPerfCheck>();
					
					resultList = preDeploymentCheckService.uploadExcelPerformanceFile(file);
					result.setResult(resultList.size() > 0 ? true : false);
					result.setObject(resultList);
					
				}catch (Exception ex) {
					ex.printStackTrace();
					result.setResult(false);
					result.setMessage(ex.getMessage());
				}		
		    }
	    }
		
		return result;		
	}	
	
	/* 배포전 점검 - 성능점검 최종 저장 */
	@RequestMapping(value="/ExcelPerformanceCheck/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveExcelPerformanceCheckAction(HttpServletRequest req, HttpServletResponse res, Model model) {
		Result result = new Result();
		int insertRow = 0;
		try{
			insertRow = preDeploymentCheckService.insertExcelPerformanceCheck(req);
			result.setResult(insertRow > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
}