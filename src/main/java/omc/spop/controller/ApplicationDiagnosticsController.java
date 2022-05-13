package omc.spop.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import omc.spop.model.Result;
import omc.spop.model.TrcdPerfSum;
import omc.spop.service.ApplicationDiagnosticsService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelWrite;
import omc.spop.utils.WriteOption;

/***********************************************************
 * 2018.04.11	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class ApplicationDiagnosticsController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationDiagnosticsController.class);
	
	@Autowired
	private ApplicationDiagnosticsService applicationDiagnosticsService;
	
	/* 애플리케이션 진단  */
	@RequestMapping(value="/ApplicationDiagnostics/Summary", method=RequestMethod.GET)
	public String ApplicationDiagnostics(@ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum, Model model) {
//		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String nowDate = DateUtil.getPlusDays("yyyyMMdd","yyyy-MM-dd", DateUtil.getNowDate("yyyyMMdd"),-1) ;
		logger.debug("nowDate ==> " + nowDate);
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", trcdPerfSum.getMenu_id() );
		model.addAttribute("menu_nm", trcdPerfSum.getMenu_nm() );
		model.addAttribute("call_from_parent", trcdPerfSum.getCall_from_parent());
		String gather_day = StringUtils.defaultString(trcdPerfSum.getGather_day());
		if(gather_day != "") {
			model.addAttribute("gather_day", gather_day);
		}
		String wrkjob_cd = StringUtils.defaultString(trcdPerfSum.getWrkjob_cd());
		if(wrkjob_cd != "") {
			model.addAttribute("wrkjob_cd", wrkjob_cd);
		}
		
		logger.debug("ApplicationDiagnostics gather_day:" + gather_day + " wrkjob_cd:" + wrkjob_cd);

		return "applicationDiagnostics/summary";
	}
	
	/* 애플리케이션 진단 - 개요 action */
	@RequestMapping(value="/ApplicationDiagnostics/Summary", method=RequestMethod.POST)
	@ResponseBody
	public Result ApplicationDiagnosticsAction(@ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum, Model model) {
		Result result = new Result();
		List<TrcdPerfSum> resultList = new ArrayList<TrcdPerfSum>();

		try{
			resultList = applicationDiagnosticsService.summaryList(trcdPerfSum);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setMessage("애플리케이션 진단 개요 조회에 실패하였습니다.");
			result.setTxtValue(trcdPerfSum.getDay_gubun());
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 애플리케이션 진단 - 개요[챠트] action */
	@RequestMapping(value="/ApplicationDiagnostics/SummaryChart", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ChartInfoAction(@ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum, Model model) {
		List<TrcdPerfSum> resultList = new ArrayList<TrcdPerfSum>();

		try{
			resultList = applicationDiagnosticsService.summaryChartList(trcdPerfSum);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 애플리케이션 진단 - 타임아웃거래  */
	@RequestMapping(value="/ApplicationDiagnostics/Timeout", method=RequestMethod.GET)
	public String Timeout(@ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum, Model model) {
		
		model.addAttribute("menu_id", trcdPerfSum.getMenu_id() );
		model.addAttribute("menu_nm", trcdPerfSum.getMenu_nm() );
		
		return "applicationDiagnostics/timeout";
	}	
	
	/* 거래상세현황 - 타임아웃거래 list action */
	@RequestMapping(value="/ApplicationDiagnostics/Timeout", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TimeoutAction(@ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum,  Model model) {
		List<TrcdPerfSum> resultList = new ArrayList<TrcdPerfSum>();

		try{
			resultList = applicationDiagnosticsService.timeoutList(trcdPerfSum);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 애플리케이션 진단 - 타임아웃거래 excel down action */
	@RequestMapping(value="/ApplicationDiagnostics/Timeout/ExcelDown", method=RequestMethod.POST)
	public void TimeoutExcelDown(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum, Model model) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {
		List<TrcdPerfSum> resultList = new ArrayList<TrcdPerfSum>();
		WriteOption wo = new WriteOption();

		wo.setFileName("타임아웃거래");
		wo.setSheetName("타임아웃거래");

	    wo.setTitle("Timeout");

		try{
			resultList = applicationDiagnosticsService.timeoutList(trcdPerfSum);			
			wo.setTimeoutDtlContents(resultList);			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

	    ExcelWrite.write(res, wo);		
	}		
	
	/* 애플리케이션 진단 - 응답시간지연  */
	@RequestMapping(value="/ApplicationDiagnostics/ElapsedTimeDelay", method=RequestMethod.GET)
	public String ElapsedTimeDelay(@ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum, Model model) {
		
		model.addAttribute("menu_id", trcdPerfSum.getMenu_id() );
		model.addAttribute("menu_nm", trcdPerfSum.getMenu_nm() );
		
		return "applicationDiagnostics/elapsedTimeDelay";
	}	
	
	/* 거래상세현황 - 응답시간지연 list action */
	@RequestMapping(value="/ApplicationDiagnostics/ElapsedTimeDelay", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ElapsedTimeDelayAction(@ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum,  Model model) {
		List<TrcdPerfSum> resultList = new ArrayList<TrcdPerfSum>();

		try{
			resultList = applicationDiagnosticsService.elapsedTimeDelayList(trcdPerfSum);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 애플리케이션 진단 - 응답시간지연 excel down action */
	@RequestMapping(value="/ApplicationDiagnostics/ElapsedTimeDelay/ExcelDown", method=RequestMethod.POST)
	public void ElapsedTimeDelayExcelDown(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("trcdPerfSum") TrcdPerfSum trcdPerfSum, Model model) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {
		List<TrcdPerfSum> resultList = new ArrayList<TrcdPerfSum>();
		WriteOption wo = new WriteOption();

		wo.setFileName("응답시간지연");
		wo.setSheetName("응답시간지연");

	    wo.setTitle("ElapsedTimeDelay");

		try{
			resultList = applicationDiagnosticsService.elapsedTimeDelayList(trcdPerfSum);			
			wo.setElapsedTimeDelayContents(resultList);			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

	    ExcelWrite.write(res, wo);		
	}			
}