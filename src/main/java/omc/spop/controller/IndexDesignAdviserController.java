package omc.spop.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.AccPathExec;
import omc.spop.model.AccPathExecV2;
import omc.spop.model.IdxAdMst;
import omc.spop.model.IdxAdRecommendIndex;
import omc.spop.model.Result;
import omc.spop.service.IndexDesignAdviserService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelWrite;
import omc.spop.utils.WriteOption;

/***********************************************************
 * 2018.03.20	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class IndexDesignAdviserController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexDesignAdviserController.class);
	
	@Autowired
	private IndexDesignAdviserService indexDesignAdviserService;
	
	/* 인덱스자동설계현황 */
	@RequestMapping(value="/AutoIndexStatus")
	public String AutoIndexStatus(@ModelAttribute("idxAdMst") IdxAdMst idxAdMst, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		logger.debug("//////////////////////////");
		logger.debug("idxAdMst :"+idxAdMst.toString());
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", idxAdMst.getMenu_id() );
		model.addAttribute("menu_nm", idxAdMst.getMenu_nm() );
		model.addAttribute("call_from_child", idxAdMst.getCall_from_child());
		
		String fromStartDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -365);
		
		idxAdMst.setEnd_dt(nowDate);
		idxAdMst.setStart_dt(fromStartDate);
		
		return "indexDesignAdviser/autoIndexStatus";
	}	
	
	/* 인덱스자동설계현황 action */
	@RequestMapping(value="/AutoIndexStatusAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AutoIndexStatusAction(@ModelAttribute("idxAdMst") IdxAdMst idxAdMst,  Model model) {
		List<IdxAdMst> resultList = new ArrayList<IdxAdMst>();

		int dataCount4NextBtn = 0;
		try{
			if(idxAdMst.getDbid() != null && !idxAdMst.getDbid().equals("")){
				resultList = indexDesignAdviserService.autoIndexStatusList(idxAdMst);
				if (resultList != null && resultList.size() > idxAdMst.getPagePerCount()) {
					dataCount4NextBtn = resultList.size();
					resultList.remove(idxAdMst.getPagePerCount());
				}
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* 인덱스Recommend현황 */
	@RequestMapping(value="/IndexRecommendStatus")
	public String IndexRecommendStatus(@ModelAttribute("idxAdRecommendIndex") IdxAdRecommendIndex idxAdRecommendIndex, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", idxAdRecommendIndex.getMenu_id() );
		model.addAttribute("menu_nm", idxAdRecommendIndex.getMenu_nm() );
		
		return "indexDesignAdviser/indexRecommendStatus";
	}
	
	/**
	 * 인덱스 자동설계 강제완료처리
	 * @param request
	 * @param paramData 강제완료 처리할 idx_ad_no
	 * @return
	 */
	@RequestMapping(value="/indexDesignAdviser/updateForceComplete", method = RequestMethod.POST)
	@ResponseBody
	public Result updateForceComplete(HttpServletRequest request, @RequestBody String paramData) {
		Result result = new Result();
		int updateCnt = 0;
		List<Map<String, Object>> paramList = new ArrayList();
		String delimiter = ",";
		
		try {
			
			paramList = (List<Map<String, Object>>) makeForeach(paramData, delimiter);
			
			//updateCnt = indexDesignAdviserService.updateForceComplete(param);
			
			updateCnt = indexDesignAdviserService.updateForceCompleteList(paramList);
			logger.debug("///// updateForceComplete");
			logger.debug("updateCnt:" + updateCnt);
			
			if(updateCnt > 0){
				result.setResult(true);
				result.setMessage("미완료된 인덱스 자동설계작업을 완료하였습니다");
				result.setResultCount(updateCnt);
			}else{
				result.setResult(false);
				result.setMessage("미완료된 인덱스 자동설계작업이 실패하였습니다");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	private List<?> makeForeach(String paramData, String delimiter) {
		List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();
		Map<String, Object> codeMap = new HashMap<String, Object>();
		String[] paramDataSplit = paramData.split(delimiter);
		
		for(int i = 0; i < paramDataSplit.length; i++) {
			codeMap = new HashMap<String, Object>();
			codeMap.put("idx_ad_no", paramDataSplit[i].toString());
			
			codeList.add(codeMap);
		}
		
		return codeList;
	}
	
	/* 인덱스Recommend현황 action */
	@RequestMapping(value="/IndexRecommendStatusAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String IndexRecommendStatusAction(@ModelAttribute("idxAdRecommendIndex") IdxAdRecommendIndex idxAdRecommendIndex,  Model model) {
		List<IdxAdRecommendIndex> resultList = new ArrayList<IdxAdRecommendIndex>();

		try{
			resultList = indexDesignAdviserService.indexRecommendStatusList(idxAdRecommendIndex);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 인덱스Recommend현황 excel down */
	@RequestMapping(value="/IndexRecommendStatus/ExcelDown", method=RequestMethod.POST)
	public void ExcelDown(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("idxAdRecommendIndex") IdxAdRecommendIndex idxAdRecommendIndex, Model model) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {
		List<IdxAdRecommendIndex> resultList = new ArrayList<IdxAdRecommendIndex>();
		WriteOption wo = new WriteOption();
		
		wo.setFileName("인덱스Recommend현황");
		wo.setSheetName("인덱스Recommend현황");
	    wo.setTitle("IndexRecommend");
		
		try{
			resultList = indexDesignAdviserService.indexRecommendStatusList(idxAdRecommendIndex);			
			wo.setIndexRecommendContents(resultList);			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

	    ExcelWrite.write(res, wo);		
	}			
	
	/* 수집SQL인덱스 자동설계 */
	@RequestMapping(value="/AutoCollectionIndexDesign", method=RequestMethod.GET)
	public String AutoCollectionIndexDesign(@ModelAttribute("accPathExec") AccPathExec accPathExec, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", accPathExec.getMenu_id() );
		model.addAttribute("menu_nm", accPathExec.getMenu_nm() );
		
		return "indexDesignAdviser/autoCollectionIndexDesign";
	}
	
	/* 수집SQL인덱스 자동설계 action */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/AutoCollectionIndexDesign", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AutoCollectionIndexDesignAction(@ModelAttribute("accPathExec") AccPathExec accPathExec,  Model model) {
		List<AccPathExecV2> resultList = new ArrayList<AccPathExecV2>();

		int dataCount4NextBtn = 0;
		try{
			resultList = indexDesignAdviserService.autoCollectionIndexDesignList(accPathExec);
			if (resultList != null && resultList.size() > accPathExec.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(accPathExec.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}	
	
	/* 적재SQL 인덱스 자동설계 */
	@RequestMapping(value="/AutoLoadIndexDesign", method=RequestMethod.GET)
	public String DBIOAutoIndexDesign(@ModelAttribute("accPathExec") AccPathExec accPathExec, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", accPathExec.getMenu_id() );
		model.addAttribute("menu_nm", accPathExec.getMenu_nm() );
		
		return "indexDesignAdviser/autoLoadIndexDesign";
	}
	
	/* 적재SQL 인덱스 자동설계 action */
	@RequestMapping(value="/AutoLoadIndexDesign", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DBIOAction(@ModelAttribute("accPathExec") AccPathExec accPathExec,  Model model) {
		List<AccPathExecV2> resultList = new ArrayList<AccPathExecV2>();

		try{
			resultList = indexDesignAdviserService.autoLoadIndexDesignList(accPathExec);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* is task- index auto desing action */
	@RequestMapping(value="/isTaskStartIndexAutoDesign", method=RequestMethod.POST)
	@ResponseBody
	public String isTaskStartIndexAutoDesign(@ModelAttribute("accPathExec") AccPathExec accPathExec,  Model model) {
		List<AccPathExecV2> resultList = new ArrayList<AccPathExecV2>();
		
		try {
			resultList = indexDesignAdviserService.isTaskStartIndexAutoDesign(accPathExec);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/* index auto desing action */
	@RequestMapping(value="/StartIndexAutoDesign", method=RequestMethod.POST)
	@ResponseBody
	public Result StartIndexAutoDesignAction(@ModelAttribute("accPathExec") AccPathExec accPathExec,  Model model) {
		Result result = new Result();

		try{
//			indexDesignAdviserService.startIndexAutoDesign(accPathExec);
			indexDesignAdviserService.startIndexAutoDesign2(accPathExec);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	/**
	 * 인덱스 자동설계현황 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param tuningTargetSql
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/indexDesignAdviser/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView indexDesignAdviserExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("idxAdMst") IdxAdMst idxAdMst, Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			String dbid = StringUtils.defaultString(idxAdMst.getDbid());
			logger.debug("dbid:"+dbid);
			if(!dbid.equals("")){
				resultList = indexDesignAdviserService.autoIndexStatusList4Excel(idxAdMst);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "인덱스_자동설계현황");
		model.addAttribute("sheetName", "인덱스_자동설계현황");
		model.addAttribute("excelId", "INDEX_AUTO_DESIGN_STAT");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/**
	 * 성능개선 > 인덱스설계어드바이저 > 수집 SQL 인덱스 자동설계 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param tuningTargetSql
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/AutoCollectionIndexDesign/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView AutoCollectionIndexDesignExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("accPathExec") AccPathExec accPathExec, Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			String dbid = StringUtils.defaultString(accPathExec.getDbid());
			logger.debug("dbid:"+dbid);
			if(!dbid.equals("")){
				resultList = indexDesignAdviserService.autoCollectionIndexDesignList4Excel(accPathExec);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "수집_SQL_인덱스자동설계");
		model.addAttribute("sheetName", "수집_SQL_인덱스자동설계");
		model.addAttribute("excelId", "GATHER_SQL_IDX_AUTO_DESIGN");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
		
}