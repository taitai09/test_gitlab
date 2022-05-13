package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import omc.spop.model.SQLStandards;

/** 
	* @packageName	:	omc.spop.service 
	* @fileName		:	SQLDiagnosisReportStatusService.java 
	* @author 		:	OPEN MADE (wonjae kim) 
	* @description	: 
	* @History		
	============================================================
	2021.11.16        wonjae kim 			최초작성
	============================================================
*/
public interface SQLDiagnosisReportStatusService {
	
	public List<LinkedHashMap<String,Object>> getReportStatus(SQLStandards sqlStandards) throws Exception;
	public List<LinkedHashMap<String,Object>> getReportHeaders() throws Exception;
	public boolean excelDownloadResult(SQLStandards sqlStandards, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception;

	
}
