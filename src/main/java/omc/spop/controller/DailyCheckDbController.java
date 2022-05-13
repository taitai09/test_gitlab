package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.DailyCheckDb;
import omc.spop.service.DailyCheckDbService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2020.03.17 명성태 OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class DailyCheckDbController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(DailyCheckDbController.class);
	
	@Autowired
	private DailyCheckDbService dailyCheckDbService;
	
	@RequestMapping(value = "/dailyFullCheckDesign", method = RequestMethod.GET)
	public String autoSelectionDesign(@RequestParam(required = false) Map<String, String> param, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		model.addAttribute("user_id", user_id);
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "dailyFullCheck/dailyFullCheckDesign";
	}
	
	@RequestMapping(value = "/DailyCheckDb", method = RequestMethod.GET)
	public String dailyDiagnosticsDbController(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb, Model model) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		model.addAttribute("user_id", user_id);
		
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -30);
		
		String today = DateUtil.getNowDate("yyyy-MM-dd");
		String yesterday = DateUtil.getPlusDays("yyyy-MM-dd", "yyyyMMdd", today,-1);
		int year  = Integer.parseInt(yesterday.substring(0, 4));
		int month = Integer.parseInt(yesterday.substring(5, 7));
		int date  = Integer.parseInt(yesterday.substring(8, 10));
		String beginDate = "";

		if(dailyCheckDb.getStart_first_analysis_day() == null || dailyCheckDb.getStart_first_analysis_day().equals("")){
			dailyCheckDb.setStart_first_analysis_day(beginDate);
		}
		
		if(dailyCheckDb.getEnd_first_analysis_day() == null || dailyCheckDb.getEnd_first_analysis_day().equals("")){
			dailyCheckDb.setEnd_first_analysis_day(yesterday);
		}

		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", dailyCheckDb.getMenu_id());
		model.addAttribute("menu_nm", dailyCheckDb.getMenu_nm());
		model.addAttribute("user_id", dailyCheckDb.getUser_id());
		
		return "dailyFullCheck/dailyCheckDb";
	}
	
//	@RequestMapping(value = "/DailyCheckDb", method = RequestMethod.GET)
//	public String dailyDiagnosticsDbController(@RequestParam(required = false) Map<String, String> param, Model model) throws Exception {
//		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
//		model.addAttribute("user_id", user_id);
//		
//		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
//		String startDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, -30);
//
//		model.addAttribute("startDate", startDate);
//		model.addAttribute("nowDate", nowDate);
//		model.addAttribute("menu_id", param.get("menu_id"));
//		
//		return "dailyFullCheck/dailyCheckDb";
//	}
	
	/* DB 그룹 조회 */
	@RequestMapping(value = "/DailyCheckDb/dbGroupList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String dbGroupList(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		if(dailyCheckDb.getUser_id().equals("")) {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dailyCheckDb.setUser_id(user_id);
		}
		
		String isAll = StringUtils.defaultString(dailyCheckDb.getIsAll());
		String isChoice = StringUtils.defaultString(dailyCheckDb.getIsChoice());
		List<DailyCheckDb> dbList = new ArrayList<DailyCheckDb>();
		List<DailyCheckDb> databaseList = dailyCheckDbService.dbGroupList(dailyCheckDb);
		
		if(isAll.equals("") == false) {
			DailyCheckDb db = new DailyCheckDb();
			db.setGroup_id("");
			if (isAll.equals("Y")) {
				db.setGroup_nm("전체");
			} else if (isChoice.equals("Y")) {
				db.setGroup_nm("선택");
			} else {
				db.setGroup_nm("");
			}
			dbList.add(db);
		}
		
		dbList.addAll(databaseList);

		return success(dbList).toJSONObject().get("rows").toString();
	}
	
	
	/* 심각도 조회 */
	@RequestMapping(value = "/DailyCheckDb/severityList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String severityList(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		String isAll = StringUtils.defaultString(dailyCheckDb.getIsAll());
		String isChoice = StringUtils.defaultString(dailyCheckDb.getIsChoice());
		List<DailyCheckDb> dbList = new ArrayList<DailyCheckDb>();
		List<DailyCheckDb> databaseList = dailyCheckDbService.severityList(dailyCheckDb);
		
		if(isAll.equals("") == false) {
			DailyCheckDb db = new DailyCheckDb();
			db.setCd("");
			if (isAll.equals("Y")) {
				db.setCd_nm("전체");
			} else if (isChoice.equals("Y")) {
				db.setCd_nm("선택");
			} else {
				db.setCd_nm("");
			}
			dbList.add(db);
		}
		
		dbList.addAll(databaseList);
		
		return success(dbList).toJSONObject().get("rows").toString();
	}
	
	/* dbSeverityCount */
	@RequestMapping(value = "/DailyCheckDb/dbSeverityCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String dbSeverityCount(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		List<DailyCheckDb> dbSeverityCountList = new ArrayList<DailyCheckDb>();
		
		try {
			dbSeverityCountList = dailyCheckDbService.dbSeverityCount(dailyCheckDb);
		
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return "";
		}
		
		if ( dbSeverityCountList == null || dbSeverityCountList.size() == 0 || 
				(dbSeverityCountList.size() == 1 && dbSeverityCountList.get(0) == null) ) {
			return "";
		} else {
			return success(dbSeverityCountList).toJSONObject().get("rows").toString();
		}
	}
	
	/* dbMain */
	@RequestMapping(value = "/DailyCheckDb/dbMain", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String dbMain(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		List<DailyCheckDb> dbSeverityCountList = new ArrayList<DailyCheckDb>();
		
		try {
			dbSeverityCountList = dailyCheckDbService.dbMain(dailyCheckDb);
		
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return "";
		}
		
		if ( dbSeverityCountList == null || dbSeverityCountList.size() == 0 ) {
			return "";
		} else {
			return success(dbSeverityCountList).toJSONObject().get("rows").toString();
		}
	}
	
	@RequestMapping(value = "/DailyCheckDb/minuteDbStatus", method = RequestMethod.GET)
	public String minuteDbStatus(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		model.addAttribute("dbid", param.get("dbid"));
		model.addAttribute("check_day", param.get("check_day"));
		model.addAttribute("check_seq", param.get("check_seq"));
		
		model.addAttribute("severity_color_0", param.get("severity_color_0"));
		model.addAttribute("severity_color_1", param.get("severity_color_1"));
		model.addAttribute("severity_color_2", param.get("severity_color_2"));
		model.addAttribute("severity_color_3", param.get("severity_color_3"));
		model.addAttribute("db_status_tabs_severity", param.get("db_status_tabs_severity"));
		
		return "dailyFullCheck/minuteDbStatus";
	}
	
	/* 진단결과 요약 */
	@RequestMapping(value = "/DailyCheckDb/diagnosisResultSummary", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String diagnosisResultSummary(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		List<DailyCheckDb> diagnosisResultSummaryList = dailyCheckDbService.diagnosisResultSummary(dailyCheckDb);
		
		return success(diagnosisResultSummaryList).toJSONObject().get("rows").toString();
	}
	
	/* 진단결과 상세 */
	@RequestMapping(value = "/DailyCheckDb/diagnosisResultMinute", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String diagnosisResultMinute(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		List<DailyCheckDb> diagnosisResultMinuteList = dailyCheckDbService.diagnosisResultMinute(dailyCheckDb);
		
		return success(diagnosisResultMinuteList).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/DailyCheckDb/dailyCheckDbSituation", method = RequestMethod.GET)
	public String dailyCheckDbSituation(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		model.addAttribute("dbid", param.get("dbid"));
		model.addAttribute("check_day", param.get("check_day"));
		model.addAttribute("check_seq", param.get("check_seq"));
		
		model.addAttribute("severity_color_0", param.get("severity_color_0"));
		model.addAttribute("severity_color_1", param.get("severity_color_1"));
		model.addAttribute("severity_color_2", param.get("severity_color_2"));
		model.addAttribute("severity_color_3", param.get("severity_color_3"));
		model.addAttribute("db_status_tabs_severity", param.get("db_status_tabs_severity"));
		
		return "dailyFullCheck/dailyCheckDbSituation";
	}
	
	/* 기준일자 */
	@RequestMapping(value = "/DailyCheckDb/baseDate", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String baseDate(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		String today = "";
		String beginDay = "";
		
		if(dailyCheckDb.getEnd_first_analysis_day().length() != 0) {
			today = dailyCheckDb.getEnd_first_analysis_day();
		} else {
			today = DateUtil.getNowDate("yyyy-MM-dd");
		}
		
		int base_period_value = dailyCheckDb.getBase_period_value();
		
		switch(base_period_value) {
		case 1:			// 1주일
			beginDay = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", today, -6);
			break;
		case 2:			// 1개월
			int year  = Integer.parseInt(today.substring(0, 4));
			int month = Integer.parseInt(today.substring(5, 7));
			int date  = Integer.parseInt(today.substring(8));
			String addMonthDate = DateUtil.addMonthByOracle(year, month, date, -1);
			
			beginDay = addMonthDate.substring(0, 4) + "-" + addMonthDate.substring(4, 6) + "-" + addMonthDate.substring(6);
			
			break;
		}
		
		dailyCheckDb.setStart_first_analysis_day(beginDay);
		dailyCheckDb.setEnd_first_analysis_day(today);
		
		return success(dailyCheckDb).toJSONObject().get("rows").toString();
	}
	
	/* 기준일자 비교 */
	@RequestMapping(value = "/DailyCheckDb/compareBaseDate", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String compareBaseDate(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		String startDay = dailyCheckDb.getStart_first_analysis_day();
		String endDay = dailyCheckDb.getEnd_first_analysis_day();
		
		dailyCheckDb.setIsLargerThanBeginDate(DateUtil.isLargerThanBeginDate(startDay, endDay, -1));
		
		return success(dailyCheckDb).toJSONObject().get("rows").toString();
	}
	
	/* DB 상태 점검 현황 TOP */
	@RequestMapping(value = "/DailyCheckDb/dailyCheckDbSituationTop", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String dailyCheckDbSituationTop(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = null;
		
		try {
			resultList = dailyCheckDbService.dailyCheckDbSituationTop(dailyCheckDb);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
	
	/* DB 상태 점검 현황 BOTTOM */
	@RequestMapping(value = "/DailyCheckDb/dailyCheckDbSituationBottom", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String dailyCheckDbSituationBottom(@ModelAttribute("dailyCheckDb") DailyCheckDb dailyCheckDb) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = null;
		
		try {
			resultList = dailyCheckDbService.dailyCheckDbSituationBottom(dailyCheckDb);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
}