package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.AnalyzeDbChangeSqlImpact;
import omc.spop.model.Cd;

/***********************************************************
 * 2021.02.16	이재우	최초작성
 **********************************************************/

public interface AnalyzeDbChangeSqlImpactDao {

	public List<Cd> getOracleVersionList(Cd cd);

	public List<AnalyzeDbChangeSqlImpact> loadAnalyzeDbChangeSqlImpactList(
			AnalyzeDbChangeSqlImpact analyzeDbChangeSqlImpact );

	public List<LinkedHashMap<String, Object>> excelDownload(
			AnalyzeDbChangeSqlImpact analyzeDbChangeSqlImpact);
	
}
