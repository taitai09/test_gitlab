package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import omc.spop.model.AnalyzeDbChangeSqlImpact;
import omc.spop.model.Cd;

/***********************************************************
 * 2021.02.16	이재우	최초작성
 **********************************************************/

public interface AnalyzeDbChangeSqlImpactService {

	List<Cd> getOracleVersionList(Cd cd) throws Exception;

	List<AnalyzeDbChangeSqlImpact> loadAnalyzeDbChangeSqlImpactList(
			AnalyzeDbChangeSqlImpact analyzeDbChangeSqlImpact ) throws Exception;

	boolean excelDownload(
			AnalyzeDbChangeSqlImpact analyzeDbChangeSqlImpact, Model model, HttpServletRequest request, HttpServletResponse response ) throws Exception;
	
}
