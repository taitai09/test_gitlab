package omc.spop.dao;

import java.util.List;

import omc.spop.model.OdsAsaRecommendations;
import omc.spop.model.PartitionRecommendation;
import omc.spop.model.ReorgRecommendation;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 * 2018.04.27	이원식	오브젝트 진단 => SPACE 진단으로 변경 
 * 2018.05.04	이원식	파티셔닝 대상 진단 추가
 **********************************************************/

public interface SpaceDiagnosticsDao {	
	public List<OdsAsaRecommendations> oracleReorgTargetTableList(OdsAsaRecommendations odsAsaRecommendations);
	
	public List<ReorgRecommendation> openPOPReorgTargetTableList(ReorgRecommendation reorgRecommendation);
	
	public OdsAsaRecommendations getOracleRecommendation(OdsAsaRecommendations odsAsaRecommendations);
	
	public ReorgRecommendation getOpenPOPRecommendation(ReorgRecommendation reorgRecommendation);
	
	public List<PartitionRecommendation> partitionRecommendationList(PartitionRecommendation partitionRecommendation);
	
	public List<PartitionRecommendation> accessPathList(PartitionRecommendation partitionRecommendation);
	
	public PartitionRecommendation getPartitionKeyRecommendation(PartitionRecommendation partitionRecommendation);
	
	public List<PartitionRecommendation> partitionKeyRecommendationList(PartitionRecommendation partitionRecommendation);
}
