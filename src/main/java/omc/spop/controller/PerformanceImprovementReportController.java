package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.model.PerfList;
import omc.spop.model.Result;
import omc.spop.service.PerformanceImprovementReportService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 * 2021.09.27	이재우	성능개선현황 보고서 엑셀다운 추가
 **********************************************************/

@Controller
public class PerformanceImprovementReportController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(PerformanceImprovementReportController.class);

	@Autowired
	private PerformanceImprovementReportService performanceImprovementReportService;	
	
	/* 성능개선 - 성능개선현황 보고서  */
	@RequestMapping(value="/PerformanceImprovementReport")
	public String PerformanceImprovementReport(@ModelAttribute("perfList") PerfList perfList,  Model model) {
//		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", DateUtil.getNowDate("yyyy-MM-dd"), "31");
//		String endDate = DateUtil.getNextDay("M", "yyyy-MM-dd", DateUtil.getNowDate("yyyy-MM-dd"), "1");

		String endDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", endDate,-6) ;
		
		
		if(perfList.getStrStartDt() == null || perfList.getStrStartDt().equals("")){
			perfList.setStrStartDt(startDate);
		}
		if(perfList.getStrEndDt() == null || perfList.getStrEndDt().equals("")){
			perfList.setStrEndDt(endDate);
		}
		
		model.addAttribute("menu_id", perfList.getMenu_id());
		model.addAttribute("menu_nm", perfList.getMenu_nm());
		
		return "performanceImprovementReport";
	}
	
	/* 성능개선 - 성능개선현황 보고서 - list action */
	@RequestMapping(value="/PerformanceImprovementReportAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PerformanceImprovementReportAction(@ModelAttribute("perfList") PerfList perfList,  Model model) {
		List<PerfList> resultList = new ArrayList<PerfList>();

		try{
			resultList = performanceImprovementReportService.performanceImprovementReportList(perfList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 성능개선 - 성능개선현황 보고서 - 엑셀 다운로드  */
	@RequestMapping(value="/PerformanceImprovementReport/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PerformanceImprovementReportExcel(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("perfList") PerfList perfList,  Model model) {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = performanceImprovementReportService.getPerformanceImprovementReportByExcelDown( perfList );
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
		}
		model.addAttribute("fileName", "성능개선현황_보고서");
		model.addAttribute("sheetName", "성능개선현황_보고서");
		model.addAttribute("excelId", "PERFORMANCE_IMPROVEMENT_REPORT");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 성능리포트 - 프로그램유형별 성능개선현황  */
	@RequestMapping(value="/PerformanceImprovementStatus/ByProgramType", method = RequestMethod.GET)
	public String ByProgramType(@ModelAttribute("perfList") PerfList perfList,  Model model) {

		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6);
		
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", perfList.getMenu_id());
		model.addAttribute("menu_nm", perfList.getMenu_nm());
		
		
		return "performanceImprovementStatus/byProgramType";
	}
	
	/* 성능리포트 - 프로그램유형별 성능개선현황 검색  */
	@RequestMapping(value = "/PerformanceImprovementStatus/ByProgramType", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getByProgramTypeReport(@ModelAttribute("perfList") PerfList perfList, Model model) {
		List<PerfList> resultList = new ArrayList<PerfList>();

		try {
			resultList = performanceImprovementReportService.getByProgramTypeReport(perfList);
			System.out.println("resultList.size:"+resultList.size());
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* 성능리포트 - 프로그램유형별 성능개선현황 엑셀 다운 */
	@RequestMapping(value = "/PerformanceImprovementStatus/ByProgramType/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getByProgramTypeReportByExcelDown(HttpServletRequest req, HttpServletResponse res, 
			@ModelAttribute("perfList") PerfList perfList, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = performanceImprovementReportService.getByProgramTypeReportByExcelDown(perfList);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로그램_유형별 성능개선현황");
		model.addAttribute("sheetName", "프로그램_유형별 성능개선현황");
		model.addAttribute("excelId", "BY_PROGRAM_TYPE_REPORT");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	
	/* 성능리포트 - 요청 유형별 성능개선현황  */
	@RequestMapping(value="/PerformanceImprovementStatus/ByRequestType", method = RequestMethod.GET)
	public String ByRequestType(@ModelAttribute("perfList") PerfList perfList,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6);
		
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", perfList.getMenu_id());
		model.addAttribute("menu_nm", perfList.getMenu_nm());
		
		return "performanceImprovementStatus/byRequestType";
	}
	
	
	/* 성능리포트 - 요청 유형별 성능개선현황 리스트 조회 */
	@RequestMapping(value = "/PerformanceImprovementStatus/ByRequestType", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getByRequestTypeReport(@ModelAttribute("perfList") PerfList perfList, Model model) {
//		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String,String>>();
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String,Object>>();
		try {
			resultList = performanceImprovementReportService.getByRequestTypeReport(perfList);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONArray jsonArray = JSONArray.fromObject(resultList);
		return jsonArray.toString();
//		return successMap(resultList).toJSONObject().toString();
	}
	
	/* 성능리포트 - 요청유형별 그리드 생성 */
	@RequestMapping(value="/PerformanceImprovementStatus/makeColumnsValues", method=RequestMethod.POST)
	@ResponseBody
	public Result makeColumnsValues(@ModelAttribute("perfList") PerfList perfList,  Model model) throws Exception{
		Result result = new Result();
		List<PerfList> resultList = new ArrayList<PerfList>();
		try{
			resultList = performanceImprovementReportService.makeColumnsValues(perfList);
			
			result.setObject(resultList);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;	
	}	
	
	/* 성능리포트 - 요청유형별 성능개선현황 엑셀 다운 */
	@RequestMapping(value = "/PerformanceImprovementStatus/ByRequestType/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ByProgramTypeActionByExcelDown(HttpServletRequest req, HttpServletResponse res, 
			@ModelAttribute("perfList") PerfList perfList, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		List<PerfList> resultHeaders = new ArrayList<PerfList>();
		try {
			resultList = performanceImprovementReportService.getByRequestTypeReport(perfList);
			perfList.setGrp_cd_id("1003");
			resultHeaders = performanceImprovementReportService.makeColumnsValues(perfList);
					
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName","요청_유형별_성능개선현황");
		model.addAttribute("sheetName","요청_유형별_성능개선현황");
		
		//동적 엑셀 그리드를 위한 엑셀 헤더값 전달.
		String[] headers = new String[resultHeaders.size()+1];
		for(int i = 0; i <= resultHeaders.size(); i++){
			if(i == 0){
				headers[i] = "업무";
			}else{
				headers[i] = resultHeaders.get(i-1).getWrkjob_cd_nm();
			}
		}
		
		model.addAttribute("excelHeaders", headers);
//		model.addAttribute("excelId", "BY_REQUEST_TYPE_REPORT");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 성능리포트 - 개선 유형별 성능개선현황  */
	@RequestMapping(value="/PerformanceImprovementStatus/ByImprovementType", method = RequestMethod.GET)
	public String ByImprovementType(@ModelAttribute("perfList") PerfList perfList,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6);
		
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", perfList.getMenu_id());
		model.addAttribute("menu_nm", perfList.getMenu_nm());
		
		return "performanceImprovementStatus/byImprovementType";
	}
	
	/* 성능리포트 - 개선 유형별 성능개선현황 리스트 조회 */
	@RequestMapping(value = "/PerformanceImprovementStatus/ByImprovementType", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getByImprovementTypeReport(@ModelAttribute("perfList") PerfList perfList, Model model) {
//		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String,String>>();
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String,Object>>();
		try {
			resultList = performanceImprovementReportService.getByImprovementTypeReport(perfList);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONArray jsonArray = JSONArray.fromObject(resultList);
		return jsonArray.toString();
//		return successMap(resultList).toJSONObject().toString();
	}
	
	/* 성능리포트 - 개선유형별 성능개선현황 엑셀 다운 */
	@RequestMapping(value = "/PerformanceImprovementStatus/ByImprovementType/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getByImprovementTypeReportByExcelDown(HttpServletRequest req, HttpServletResponse res, 
			@ModelAttribute("perfList") PerfList perfList, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		List<PerfList> resultHeaders = new ArrayList<PerfList>();

		try {
			resultList = performanceImprovementReportService.getByImprovementTypeReport(perfList);
			perfList.setGrp_cd_id("1036");
			resultHeaders = performanceImprovementReportService.makeColumnsValues(perfList);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		
		//동적 엑셀 그리드를 위한 엑셀 헤더값 전달.
		String[] headers = new String[resultHeaders.size()+1];
		for(int i = 0; i <= resultHeaders.size(); i++){
			if(i == 0){
				headers[i] = "업무";
			}else{
				headers[i] = resultHeaders.get(i-1).getWrkjob_cd_nm();
			}
		}
		
		model.addAttribute("excelHeaders", headers);
		
		model.addAttribute("fileName", "개선_유형별_성능개선현황");
		model.addAttribute("sheetName", "개선_유형별_성능개선현황");
		
		
		
//		model.addAttribute("excelId", "BY_IMPROVEMENT_TYPE_REPORT");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
}