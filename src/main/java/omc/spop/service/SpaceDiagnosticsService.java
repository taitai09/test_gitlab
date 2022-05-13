package omc.spop.service;

import java.util.List;

import omc.spop.model.OdsAsaRecommendations;
import omc.spop.model.PartitionRecommendation;
import omc.spop.model.ReorgRecommendation;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 * 2018.04.27	이원식	오브젝트 진단 => SPACE 진단으로 변경
 * 2018.05.04	이원식	파티셔닝 대상 진단 추가 
 **********************************************************/

public interface SpaceDiagnosticsService {
	/** Oracle Reorg 대상 테이블 리스트 */
	List<OdsAsaRecommendations> oracleReorgTargetTableList(OdsAsaRecommendations odsAsaRecommendations) throws Exception;
	
	/** OpenPOP Reorg 대상 테이블 리스트 */
	List<ReorgRecommendation> openPOPReorgTargetTableList(ReorgRecommendation reorgRecommendation) throws Exception;	
	
	/** Oracle Reorg 대상 테이블 - Recommendation 팝업 */
	OdsAsaRecommendations getOracleRecommendation(OdsAsaRecommendations odsAsaRecommendations) throws Exception;
	
	/** OpenPOP Reorg 대상 테이블 - Recommendation 팝업 */
	ReorgRecommendation getOpenPOPRecommendation(ReorgRecommendation reorgRecommendation) throws Exception;
	
	/** 파티셔닝 대상 테이블  - partition recommendation 리스트 */
	List<PartitionRecommendation> partitionRecommendationList(PartitionRecommendation partitionRecommendation) throws Exception;
	
	/** 파티셔닝 대상 테이블  - access path list 리스트 */
	List<PartitionRecommendation> accessPathList(PartitionRecommendation partitionRecommendation) throws Exception;
	
	/** 파티셔닝 대상 테이블  - partition Key Recommendation */
	PartitionRecommendation getPartitionKeyRecommendation(PartitionRecommendation partitionRecommendation) throws Exception;
	
	/** 파티셔닝 대상 테이블  - partition Key Recommendation 리스트 */
	List<PartitionRecommendation> partitionKeyRecommendationList(PartitionRecommendation partitionRecommendation) throws Exception;	
}
