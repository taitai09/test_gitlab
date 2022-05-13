package omc.spop.controller;

import java.util.Collections;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.SQLStandards;
import omc.spop.model.Users;
import omc.spop.service.SQLDiagnosisReportStatusService;

/** 
	* @packageName	:	omc.spop.controller 
	* @fileName		:	SQLDiagnosisReportStatusController.java 
	* @author 		:	OPEN MADE (wonjae kim) 
	* @description	: 
	* @History		
	============================================================
	2021.11.16        wonjae kim 			최초작성
	============================================================
*/
@Controller
@RequestMapping(value = "/SQLDiagnosisReportStatus")
public class SQLDiagnosisReportStatusController extends InterfaceController {
	
	@Autowired
	SQLDiagnosisReportStatusService sqlDiagnosisreportStatusService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(SQLDiagnosisReportStatusController.class);

	@ResponseBody
	@RequestMapping(value = "status", method = {RequestMethod.POST} , produces = "application/text; charset=utf8")
	public String getReportStatus(@ModelAttribute("sqlStandards") SQLStandards sqlStandards) throws Exception {
		List<LinkedHashMap<String, Object>> result = null;
		Users users = SessionManager.getLoginSession().getUsers();
		sqlStandards.setUser_id(users.getUser_id());
		
		
		if(logger.isDebugEnabled()) {
			logger.debug("getSqlDiagnosisReportStatus > sqlStandards.toString() = [{}]",sqlStandards.toString());
		}
		
		try {
			result = sqlDiagnosisreportStatusService.getReportStatus(sqlStandards);
		}catch (Exception e) {
			logger.error(" getSqlDiagnosisReportStatus error [{}]" , e.getMessage());
			return getErrorJsonString(e);
		}
		JSONObject jobj = getJSONResult(result, true).toJSONObject();
		return jobj.toString();
	}
	
	/* 전체 품질점검 지표 */
	@RequestMapping(value = "headers", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getReportHeaders() {
		
		List<LinkedHashMap<String, Object>> result = null;
		JSONObject jobj = null;
		
		try {
			result = sqlDiagnosisreportStatusService.getReportHeaders();
			jobj = getJSONResult(result, true).toJSONObject();
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			return getErrorJsonString(ex);
			
		} finally {
			result = null;
		}
		
		return jobj.toString();
	}
	
	/* sql 표준 점검 결과 - 엑셀 다운 */
	@RequestMapping(value = "excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public void excelDownloadResult(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) throws Exception {
		
		Users users = SessionManager.getLoginSession().getUsers();
		sqlStandards.setUser_id(users.getUser_id());

		try {
			model.addAttribute("fileName", "SQL_품질_진단_현황");
			sqlDiagnosisreportStatusService.excelDownloadResult(sqlStandards, req, res, model);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}

	
}