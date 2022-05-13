package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.dao.SQLStandardsDao;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.SQLDiagnosisReport;
import omc.spop.model.SQLStandards;
import omc.spop.service.SQLDiagnosisReportService;
import omc.spop.utils.CryptoUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2021.05.31 김원재 생성
 **********************************************************/

@Controller
@RequestMapping(value = "/SQLDiagnosisReport")
public class SQLDiagnosisReportController extends InterfaceController {
	
	@Autowired
	SQLDiagnosisReportService sqlDiagnosisreportService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(SQLDiagnosisReportController.class);
	
	
	@ResponseBody
	@RequestMapping(value = "selectSqlDiagnosisReportSchedulerNameList", method = {RequestMethod.POST , RequestMethod.GET} , produces = "application/text; charset=utf8")
	public String selectSqlDiagnosisReportSchedulerNameList(@ModelAttribute("sqlDiagnosisReport") SQLDiagnosisReport sqlDiagnosisReport) throws Exception {
		List<SQLDiagnosisReport> list =null;
		
		if(logger.isDebugEnabled()) {
			logger.debug("selectSqlDiagnosisReportSchedulerList > dbId = [{}]",sqlDiagnosisReport.getStd_qty_target_dbid());
		}
		
		try {
			list = sqlDiagnosisreportService.selectSqlDiagnosisReportSchedulerNameList(sqlDiagnosisReport);
			if(list.size() < 1) {
				return getErrorJsonString(new Exception("DB 조회 오류"));
			}
		}catch (Exception e) {
			logger.error(" selectSqlDiagnosisReportSchedulerList error [{}]" , e);
			return getErrorJsonString(e);
		}
		JSONObject jobj = success(list).toJSONObject();
		return jobj.get("rows").toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "selectSQLDiagnosisReportSchedulerSchedule", method = {RequestMethod.POST} , produces = "application/text; charset=utf8")
	public String selectSQLDiagnosisReportSchedulerSchedule(@ModelAttribute("sqlDiagnosisReport") SQLDiagnosisReport sqlDiagnosisReport) throws Exception {
		SQLDiagnosisReport result = null;
		
		if(logger.isDebugEnabled()) {
			logger.debug("selectSQLDiagnosisReportSchedulerSchedule > sqlDiagnosisReport.toString() = [{}]",sqlDiagnosisReport.toString());
		}
		
		try {
			result = sqlDiagnosisreportService.selectSQLDiagnosisReportSchedulerSchedule(sqlDiagnosisReport);
			if(result != null) {
				if( StringUtil.isEmpty(result.getDiag_dt()) 
						|| StringUtil.isEmpty(result.getGather_term())) {
					return getErrorJsonString(new Exception("DB 조회 오류"));
				}
			}else {
				result = new SQLDiagnosisReport();
			}
			
		}catch (Exception e) {
			logger.error(" selectSQLDiagnosisReportSchedulerSchedule error [{}]" , e.getMessage());
			return getErrorJsonString(e);
		}
		JSONObject jobj = success(result).toJSONObject();
		return jobj.get("rows").toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "selectSchedulerStatus", method = {RequestMethod.POST} , produces = "application/text; charset=utf8")
	public String selectSchedulerStatus(@ModelAttribute("sqlDiagnosisReport") SQLDiagnosisReport sqlDiagnosisReport) throws Exception {
		SQLDiagnosisReport result = null;
		
		if(logger.isDebugEnabled()) {
			logger.debug("selectSchedulerStatus > sqlDiagnosisReport.toString() = [{}]",sqlDiagnosisReport.toString());
		}
		
		try {
			result = sqlDiagnosisreportService.selectSchedulerStatus(sqlDiagnosisReport);
		}catch (Exception e) {
			logger.error(" selectSchedulerStatus error [{}]" , e.getMessage());
			return getErrorJsonString(e);
		}
		JSONObject jobj = success(result).toJSONObject();
		return jobj.get("rows").toString();
	}
	
	/* sql 표준 점검 결과 - 엑셀 다운 */
	@RequestMapping(value = "excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void excelDownloadResult(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		
		try {
			model.addAttribute("fileName", "SQL_품질_진단_결과");
			sqlDiagnosisreportService.excelDownloadResult(sqlStandards, req, res, model);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}

	
	@ResponseBody
	@RequestMapping(value = "selectSqlDiagnosisRuleData", method = {RequestMethod.POST} , produces = "application/text; charset=utf8")
	public String selectSqlDiagnosisRuleData(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) throws Exception {

		List<LinkedHashMap<String, Object>> list = null;
				
		if(logger.isDebugEnabled()) {
			logger.debug("selectSqlDiagnosisRuleData > sqlDiagnosisReport.toString() = [{}]",sqlStandards.toString());
		}
		
		try {
			list = sqlDiagnosisreportService.selectSqlDiagnosisRuleData(sqlStandards);
			if(logger.isDebugEnabled()) {
				logger.debug("selectSqlDiagnosisRuleData > list.toString() = [{}]",list.toString());
			}
		
		}catch (Exception e) {
			logger.error(" selectSqlDiagnosisRuleData error [{}]" , e);
			return getErrorJsonString(e);
		}
		JSONObject jobj = getJSONResult(list, true).toJSONObject();
		return jobj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "selectSqlDiagnosisReportDetailInfo", method = {RequestMethod.POST} , produces = "application/text; charset=utf8")
	public String selectSqlDiagnosisReportDetailInfo(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> list = null;
		int dataCount4NextBtn = 0;

		if(logger.isDebugEnabled()) {
			logger.debug("selectSqlDiagnosisReportDetailInfo > sqlDiagnosisReport.toString() = [{}]",sqlStandards.toString());
		}
		
		try {
			list = sqlDiagnosisreportService.selectSqlDiagnosisReportDetailInfo(sqlStandards);
			
			if(list != null && list.size() > sqlStandards.getPagePerCount()){
				dataCount4NextBtn = list.size();
				list.remove(sqlStandards.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		}catch (Exception e) {
			logger.error(" selectSqlDiagnosisReportDetailInfo error [{}]" , e);
			return getErrorJsonString(e);
		}
		JSONObject jobj = getJSONResult(list, true).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		
		if(logger.isDebugEnabled()) {
			logger.debug("selectSqlDiagnosisReportDetailInfo > jobj.toString() = [{}]",jobj.toString());
		}
		
		
		return jobj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "selectProgramSourceDesc", method = {RequestMethod.POST} , produces = "application/text; charset=utf8")
	public String selectProgramSourceDesc(@ModelAttribute("sqlDiagnosisReport") SQLDiagnosisReport sqlDiagnosisReport) throws Exception {

		List<LinkedHashMap<String, Object>> list = null;
				
		if(logger.isDebugEnabled()) {
			logger.debug("selectProgramSourceDesc > sqlDiagnosisReport.toString() = [{}]",sqlDiagnosisReport.toString());
		}
		
		try {
			list = sqlDiagnosisreportService.selectProgramSourceDesc(sqlDiagnosisReport);
			if(logger.isDebugEnabled()) {
				logger.debug("selectProgramSourceDesc > list.toString() = [{}]",list.toString());
			}
		
		}catch (Exception e) {
			logger.error(" selectProgramSourceDesc error [{}]" , e);
			return getErrorJsonString(e);
		}
		JSONObject jobj = getJSONResult(list, true).toJSONObject();
		return jobj.toString();
	}
	
	/* sql 표준 점검 결과 - 프로젝트별 점검지표(new) */
	@RequestMapping(value = "/loadIndexList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadIndexList(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) {
		
		List<SQLStandards> resultList = Collections.emptyList();
		JSONObject jobj = null;
		
		try {
			resultList = sqlDiagnosisreportService.loadIndexList(sqlStandards);
			jobj = success(resultList).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			return getErrorJsonString(ex);
			
		} finally {
			resultList = null;
		}
		
		return jobj.toString();
	}

}