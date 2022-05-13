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
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.AdvancedAnalysis;
import omc.spop.model.AwrOsStat;
import omc.spop.model.DbaHistBaseline;
import omc.spop.model.DbaHistSnapshot;
import omc.spop.model.DownLoadFile;
import omc.spop.model.Result;
import omc.spop.service.AWRAdvancedAnalysisService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.05	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class AWRAdvancedAnalysisController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(AWRAdvancedAnalysisController.class);
	
	@Autowired
	private AWRAdvancedAnalysisService awrAdvancedAnalysisService;
	
	/* AWR Report */
	@RequestMapping(value="/AWRReport", method=RequestMethod.GET)
	public String AWRReport(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", advancedAnalysis.getMenu_id() );
		model.addAttribute("menu_nm", advancedAnalysis.getMenu_nm() );
		
		return "awrAdvancedAnalysis/awrReport";
	}	
	
	/* AWR Report action  */
	@RequestMapping(value="/AWRReport", method=RequestMethod.POST)
	@ResponseBody
	public Result AWRReportAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis, Model model) {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		Result result = new Result();
		try{
			resultList = awrAdvancedAnalysisService.selectAWRReport(advancedAnalysis);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* AWR Report  - OS Stat Chart */
	@RequestMapping(value="/AWRReport/OSStatChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String OSStatChartAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		List<AwrOsStat> resultList = new ArrayList<AwrOsStat>();

		try{
			resultList = awrAdvancedAnalysisService.oSStatChartList(advancedAnalysis);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	

	/* AWR Report  - Foreground Wait Class Chart action */
	@RequestMapping(value="/AWRReport/ForeWaitClassChart", method=RequestMethod.POST)
	@ResponseBody
	public Result ForeWaitClassChartAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		Result result = new Result();

		try{
			result = awrAdvancedAnalysisService.foreWaitClassChartList(advancedAnalysis);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* AWR Report - Background Wait Class Chart action */
	@RequestMapping(value="/AWRReport/BackWaitClassChart", method=RequestMethod.POST)
	@ResponseBody
	public Result BackWaitClassChartAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		Result result = new Result();

		try{
			result = awrAdvancedAnalysisService.backWaitClassChartList(advancedAnalysis);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}	
	
	/* AWR Report - Foreground TOP Event Chart action */
	@RequestMapping(value="/AWRReport/ForeTOPEventChart", method=RequestMethod.POST)
	@ResponseBody
	public Result ForeTOPEventChartAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		Result result = new Result();

		try{
			result = awrAdvancedAnalysisService.foreTOPEventChartList(advancedAnalysis);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}	
	
	/* AWR Report - Background TOP Event Chart Legend action */
	@RequestMapping(value="/AWRReport/BackTOPEventChart", method=RequestMethod.POST)
	@ResponseBody
	public Result BackTOPEventChartAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		Result result = new Result();

		try{
			result = awrAdvancedAnalysisService.backTOPEventChartList(advancedAnalysis);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}	
	
	/* AWR Report - save */
	@RequestMapping(value = "/AWRReport/Save", method = RequestMethod.POST)
	public ModelAndView AWRReportSave(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis) throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		try{
			advancedAnalysis.setModule_name("AWRReport");
			downloadFile = awrAdvancedAnalysisService.getReportSave(advancedAnalysis);
		} catch (Exception ex){
			logger.error("filrName error ==> " + ex.getMessage());
		}

		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}
	
	/* ADDM Report */
	@RequestMapping(value="/ADDMReport", method=RequestMethod.GET)
	public String ADDMReport(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", advancedAnalysis.getMenu_id() );
		model.addAttribute("menu_nm", advancedAnalysis.getMenu_nm() );
		
		return "awrAdvancedAnalysis/addmReport";
	}	
	
	/* ADDM Report action  */
	@RequestMapping(value="/ADDMReport", method=RequestMethod.POST)
	@ResponseBody
	public Result ADDMReportAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		Result result = new Result();
		try{
			resultList = awrAdvancedAnalysisService.selectADDMReport(advancedAnalysis);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* ADDM Report - save */
	@RequestMapping(value = "/ADDMReport/Save", method = RequestMethod.POST)
	public ModelAndView ADDMReportSave(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis) throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		try{
			advancedAnalysis.setModule_name("ADDMReport");
			downloadFile = awrAdvancedAnalysisService.getReportSave(advancedAnalysis);
		} catch (Exception ex){
			logger.error("filrName error ==> " + ex.getMessage());
		}

		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}	
	
	/* ASH Report */
	@RequestMapping(value="/ASHReport", method=RequestMethod.GET)
	public String ASHReport(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String nowTime = DateUtil.getNowFulltime();
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("nowTime", nowTime );
		model.addAttribute("menu_id", advancedAnalysis.getMenu_id() );
		model.addAttribute("menu_nm", advancedAnalysis.getMenu_nm() );
		
		return "awrAdvancedAnalysis/ashReport";
	}
	
	/* ASH Report action  */
	@RequestMapping(value="/ASHReport", method=RequestMethod.POST)
	@ResponseBody
	public Result ASHReportAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		Result result = new Result();
		try{
			resultList = awrAdvancedAnalysisService.selectASHReport(advancedAnalysis);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* ASH Report - save */
	@RequestMapping(value = "/ASHReport/Save", method = RequestMethod.POST)
	public ModelAndView ASHReportSave(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis) throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		try{
			advancedAnalysis.setModule_name("ASHReport");
			downloadFile = awrAdvancedAnalysisService.getReportSave(advancedAnalysis);
		} catch (Exception ex){
			logger.error("filrName error ==> " + ex.getMessage());
		}

		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}	
	
	/* AWR SQL Report */
	@RequestMapping(value="/AWRSQLReport", method=RequestMethod.GET)
	public String AWRSQLReport(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", advancedAnalysis.getMenu_id() );
		model.addAttribute("menu_nm", advancedAnalysis.getMenu_nm() );
		
		return "awrAdvancedAnalysis/awrSqlReport";
	}	
	
	/* AWR SQL Report action  */
	@RequestMapping(value="/AWRSQLReport", method=RequestMethod.POST)
	@ResponseBody
	public Result AWRSQLReportAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		Result result = new Result();
		try{
			resultList = awrAdvancedAnalysisService.selectAWRSQLReport(advancedAnalysis);
			System.out.println("resultList ::::: " + resultList.toString());
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* AWR SQL Report - save */
	@RequestMapping(value = "/AWRSQLReport/Save", method = RequestMethod.POST)
	public ModelAndView AWRSQLReportSave(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis) throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		try{
			advancedAnalysis.setModule_name("AWRSQLReport");
			downloadFile = awrAdvancedAnalysisService.getReportSave(advancedAnalysis);
		} catch (Exception ex){
			logger.error("filrName error ==> " + ex.getMessage());
		}

		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}	
	
	/* AWR Diff Report */
	@RequestMapping(value="/AWRDiffReport", method=RequestMethod.GET)
	public String AWRDiffReport(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", advancedAnalysis.getMenu_id() );
		model.addAttribute("menu_nm", advancedAnalysis.getMenu_nm() );
		
		return "awrAdvancedAnalysis/awrDiffReport";
	}
	
	/* AWR Diff Report action  */
	@RequestMapping(value="/AWRDiffReport", method=RequestMethod.POST)
	@ResponseBody
	public Result AWRDiffReportAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		Result result = new Result();
		try{
			resultList = awrAdvancedAnalysisService.selectAWRDiffReport(advancedAnalysis);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* AWR Diff Report - save */
	@RequestMapping(value = "/AWRDiffReport/Save", method = RequestMethod.POST)
	public ModelAndView AWRDiffReportSave(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis) throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		try{
			advancedAnalysis.setModule_name("AWRDiffReport");
			downloadFile = awrAdvancedAnalysisService.getReportSave(advancedAnalysis);
		} catch (Exception ex){
			logger.error("filrName error ==> " + ex.getMessage());
		}

		return new ModelAndView("fileDownload", "downloadFile", downloadFile);
	}	
	
	/* AWR 고급분석  - SNAP_ID 조회 팝업 */
	@RequestMapping(value="/AWRAdvancedAnalysis/Popup/SnapShotList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SnapShotListAction(@ModelAttribute("dbaHistSnapshot") DbaHistSnapshot dbaHistSnapshot,  Model model) {
		List<DbaHistSnapshot> resultList = new ArrayList<DbaHistSnapshot>();

		try{
			resultList = awrAdvancedAnalysisService.snapShotList(dbaHistSnapshot);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* AWR 고급분석 - BASELINE 조회 팝업 */
	@RequestMapping(value="/AWRAdvancedAnalysis/Popup/BaseLineList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String BaseLineListAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		List<DbaHistBaseline> resultList = new ArrayList<DbaHistBaseline>();

		try{
			resultList = awrAdvancedAnalysisService.baseLineList(advancedAnalysis);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* ASH Report MinMaxDateTime 조회*/
	@RequestMapping(value="/ASHReport/Popup/SetMinMaxDateTime", method=RequestMethod.POST)
	@ResponseBody
	public Result SetMinMaxDateTimeAction(@ModelAttribute("advancedAnalysis") AdvancedAnalysis advancedAnalysis,  Model model) {
		Result result = new Result();
		AdvancedAnalysis returnModel = new AdvancedAnalysis();
		
		try{
			returnModel = awrAdvancedAnalysisService.setMinMaxDateTime(advancedAnalysis);
			result.setResult(true);
			result.setObject(returnModel);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
}