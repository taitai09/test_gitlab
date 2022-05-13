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

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.model.DbParameterHistory;
import omc.spop.model.OdsHistParameter;
import omc.spop.service.ParameterAnalysisService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 * 2018.03.30	이원식	명칭 변경  
 * 2018.05.14	이원식	파라미터 변경 이력 명칭 변경, 신규 기능 추가
 **********************************************************/

@Controller
public class ParameterAnalysisController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ParameterAnalysisController.class);
	
	@Autowired
	private ParameterAnalysisService parameterAnalysisService;
	
	/* 파라미터  최근 변경 내역  */
	@RequestMapping(value="/ParameterRecentChanges", method=RequestMethod.GET)
	public String ParameterRecentChanges(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", dbParameterHistory.getMenu_id() );
		model.addAttribute("menu_nm", dbParameterHistory.getMenu_nm() );
		
		return "parameterAnalysis/parameterRecentChanges";
	}
	
	/* 파라미터  최근 변경 내역 - Chart Action */
	@RequestMapping(value="/ParameterRecentChanges/Chart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ParameterRecentChangesChartAction(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		List<DbParameterHistory> resultList = new ArrayList<DbParameterHistory>();
		try{
			resultList = parameterAnalysisService.parameterRecentChangesChartList(dbParameterHistory);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* 파라미터  최근 변경 내역 - Detail List Action */
	@RequestMapping(value="/ParameterRecentChanges/Detail_20180820", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ParameterRecentChangesDetailAction_20180820(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		List<DbParameterHistory> resultList = new ArrayList<DbParameterHistory>();
		try{
			resultList = parameterAnalysisService.parameterRecentChangesDetailList(dbParameterHistory);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	@RequestMapping(value="/ParameterRecentChanges/Detail", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ParameterRecentChangesDetailAction(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		List<DbParameterHistory> resultList = new ArrayList<DbParameterHistory>();
		String returnValue = "";
		try{
			resultList = parameterAnalysisService.parameterRecentChangesDetailList(dbParameterHistory);
			List<DbParameterHistory> buildList = TreeWrite.buildDbParameterGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
			logger.debug("returnValue:"+returnValue);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return returnValue;	
	}
	
	/* 표준 파라미터 비교  */
	@RequestMapping(value="/StandardParameterComp", method=RequestMethod.GET)
	public String StandardParameterComp(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", dbParameterHistory.getMenu_id() );
		model.addAttribute("menu_nm", dbParameterHistory.getMenu_nm() );
		
		return "parameterAnalysis/standardParameterComp";
	}
	
	/* 표준 파라미터 비교 - Chart Action */
	@RequestMapping(value="/StandardParameterComp/Chart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String StandardParameterCompChartAction(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		List<DbParameterHistory> resultList = new ArrayList<DbParameterHistory>();
		try{
			resultList = parameterAnalysisService.standardParameterCompChartList(dbParameterHistory);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* 표준 파라미터 비교 - Detail List Action */
	@RequestMapping(value="/StandardParameterComp/Detail", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String StandardParameterCompDetailAction(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		List<DbParameterHistory> resultList = new ArrayList<DbParameterHistory>();
		try{
			resultList = parameterAnalysisService.standardParameterCompDetailList(dbParameterHistory);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 인스턴스 파라미터 비교  */
	@RequestMapping(value="/InstanceParameterComp", method=RequestMethod.GET)
	public String InstanceParameterComp(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", dbParameterHistory.getMenu_id() );
		model.addAttribute("menu_nm", dbParameterHistory.getMenu_nm() );
		
		return "parameterAnalysis/instanceParameterComp";
	}	
	
	/* 인스턴스 파라미터 비교 - Chart Action */
	@RequestMapping(value="/InstanceParameterComp/Chart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String InstanceParameterCompChartAction(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		List<DbParameterHistory> resultList = new ArrayList<DbParameterHistory>();
		try{
			resultList = parameterAnalysisService.instanceParameterCompChartList(dbParameterHistory);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* 인스턴스 파라미터 비교 - Detail List Action */
	@RequestMapping(value="/InstanceParameterComp/Detail", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String InstanceParameterCompDetailAction(@ModelAttribute("dbParameterHistory") DbParameterHistory dbParameterHistory, Model model) {
		List<DbParameterHistory> resultList = new ArrayList<DbParameterHistory>();
		try{
			resultList = parameterAnalysisService.instanceParameterCompDetailList(dbParameterHistory);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 파라미터 변경 이력  */
	@RequestMapping(value="/ParameterChangeHistory", method=RequestMethod.GET)
	public String ParameterChangeHistory(@ModelAttribute("odsHistParameter") OdsHistParameter odsHistParameter, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsHistParameter.getMenu_id() );
		model.addAttribute("menu_nm", odsHistParameter.getMenu_nm() );
		model.addAttribute("call_from_parent", odsHistParameter.getCall_from_parent() );
		model.addAttribute("odsHistParameter", odsHistParameter);
		
		return "parameterAnalysis/parameterChangeHistory";
	}
	
	/* 파라미터 변경 분석 Action */
	@RequestMapping(value="/ParameterChangeHistory", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ParameterChangeHistoryAction(@ModelAttribute("odsHistParameter") OdsHistParameter odsHistParameter,  Model model) {
		List<OdsHistParameter> resultList = new ArrayList<OdsHistParameter>();
		String returnValue = "";
		try{
			resultList = parameterAnalysisService.parameterChangeHistoryList(odsHistParameter);
			List<OdsHistParameter> buildList = TreeWrite.buildParameterGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return returnValue;	
	}
}