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
import omc.spop.model.Cd;
import omc.spop.model.GrpCd;
import omc.spop.model.Result;
import omc.spop.service.CodeMngService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2017.11.10	이원식	최초작성
 * 2018.04.09	이원식	OPENPOP V2 최초작업  (오픈메이드 관리자 메뉴로 분리)
 **********************************************************/

@Controller
@RequestMapping(value = "/Config")
public class CodeMngController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(CodeMngController.class);
	
	@Autowired
	private CodeMngService codeMngService;

	/* 환경설정 - 코드관리 - 코드그룹관리 */
	@RequestMapping(value="/CodeMng/CodeGroup", method=RequestMethod.GET)
	public String CodeGroup(Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("startDate", startDate );
		
		return "config/codeMng/codeGroup";
	}

	/* 환경설정 - 코드관리 - 코드그룹관리 action */
	@RequestMapping(value="/CodeMng/CodeGroup", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String CodeGroupAction(@ModelAttribute("grpCd") GrpCd grpCd,  Model model) {
		List<GrpCd> resultList = new ArrayList<GrpCd>();

		try{
			resultList = codeMngService.codeGroupList(grpCd);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 환경설정 - 코드관리 - 코드그룹관리 save */
	@RequestMapping(value="/CodeMng/SaveCodeGroup", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveCodeGroupAction(@ModelAttribute("grpCd") GrpCd grpCd,  Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try{
			rowCnt = codeMngService.saveCodeGroup(grpCd);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 환경설정 - 코드관리 - 코드관리 */
	@RequestMapping(value="/CodeMng/Code", method=RequestMethod.GET)
	public String Code(Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("startDate", startDate );
		
		return "config/codeMng/code";
	}
	
	/* 환경설정 - 코드관리 - 코드관리 action */
	@RequestMapping(value="/CodeMng/Code", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String CodeAction(@ModelAttribute("cd") Cd cd,  Model model) {
		List<Cd> resultList = new ArrayList<Cd>();

		try{
			resultList = codeMngService.codeList(cd);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 환경설정 - 코드관리 - 코드관리 save */
	@RequestMapping(value="/CodeMng/SaveCode", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveCodeAction(@ModelAttribute("cd") Cd cd,  Model model) {
		Result result = new Result();
		try{
			codeMngService.saveCode(cd);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
}