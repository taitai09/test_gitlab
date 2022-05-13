package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.AnalyzeDbChangeSqlImpact;
import omc.spop.model.Cd;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.service.AnalyzeDbChangeSqlImpactService;

/***********************************************************
 * 2021.02.16	이재우	최초작성.
 **********************************************************/
@RequestMapping(value="/AnalyzeDbChangeSqlImpact")
@Controller
public class AnalyzeDbChangeSqlImpactController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(
			AnalyzeDbChangeSqlImpactController.class);
	
	@Autowired
	private AnalyzeDbChangeSqlImpactService analyzeDbChangeSqlImpactService;

	private String dbcd = "ORACLE";
	
	@RequestMapping( value = "/AnalyzeDbChangeSqlImpact", method = RequestMethod.GET )
	public String analyzeDbChangeSqlImpact( 
			@RequestParam(required = false) Map<String, String> param, Model model ) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		model.addAttribute("database_kinds_cd", dbcd);
		
		return "analyzeDbChangeSqlImpact/analyzeDbChangeSqlImpact";
	}
	
	@RequestMapping( value="/getOracleVersionList", method = RequestMethod.GET , produces="application/text; charset=utf8" )
	@ResponseBody
	public String getOracleVersionList(Cd cd) throws Exception {
		
		List<Cd> resultList = new ArrayList<Cd>();
		try {
			
			resultList = analyzeDbChangeSqlImpactService.getOracleVersionList(cd);
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			ex.printStackTrace();
			return getErrorJsonString( ex );
		}
		
		return success( resultList ).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value="/loadAnalyzeDbChangeSqlImpactList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadAnalyzeDbChangeSqlImpactList (
			@ModelAttribute("analyzeDbChangeSqlImpact") AnalyzeDbChangeSqlImpact analyzeDbChangeSqlImpact,
			Model model ) {
		List<AnalyzeDbChangeSqlImpact> resultList = new ArrayList<AnalyzeDbChangeSqlImpact>();
		int dataCount4NextBtn = 0;
		logger.debug("result_list = "+analyzeDbChangeSqlImpact.getPagePerCount());
		
		try {
			resultList = analyzeDbChangeSqlImpactService.loadAnalyzeDbChangeSqlImpactList( analyzeDbChangeSqlImpact );
			
			if ( resultList != null && resultList.size() > analyzeDbChangeSqlImpact.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove( analyzeDbChangeSqlImpact.getPagePerCount() );
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* 엑셀 다운로드 */
	@RequestMapping(value = "/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public void excelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("analyzeDbChangeSqlImpact") AnalyzeDbChangeSqlImpact analyzeDbChangeSqlImpact, Model model)
					throws Exception {
		
		model.addAttribute("fileName", "DB_변경_SQL_영향도_분석");
		try {
			analyzeDbChangeSqlImpactService.excelDownload( analyzeDbChangeSqlImpact , model, req, res );
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		// return a view which will be resolved by an excel view resolver
//		return new ModelAndView("xlsxView", "resultList", resultList);
	}
}
