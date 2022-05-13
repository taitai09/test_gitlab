package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SpaceDiagnosticsDao;
import omc.spop.model.OdsAsaRecommendations;
import omc.spop.model.PartitionRecommendation;
import omc.spop.model.ReorgRecommendation;
import omc.spop.service.SpaceDiagnosticsService;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 * 2018.04.27	이원식	오브젝트 진단 => SPACE 진단으로 변경 
 * 2018.05.04	이원식	파티셔닝 대상 진단 추가
 **********************************************************/

@Service("SpaceDiagnosticsService")
public class SpaceDiagnosticsServiceImpl implements SpaceDiagnosticsService {
	@Autowired
	private SpaceDiagnosticsDao spaceDiagnosticsDao;

	@Override
	public List<OdsAsaRecommendations> oracleReorgTargetTableList(OdsAsaRecommendations odsAsaRecommendations) throws Exception {
		return spaceDiagnosticsDao.oracleReorgTargetTableList(odsAsaRecommendations);
	}
	
	@Override
	public List<ReorgRecommendation> openPOPReorgTargetTableList(ReorgRecommendation reorgRecommendation) throws Exception {
		return spaceDiagnosticsDao.openPOPReorgTargetTableList(reorgRecommendation);
	}
	
	@Override
	public OdsAsaRecommendations getOracleRecommendation(OdsAsaRecommendations odsAsaRecommendations) throws Exception {
		return spaceDiagnosticsDao.getOracleRecommendation(odsAsaRecommendations);
	}
	
	@Override
	public ReorgRecommendation getOpenPOPRecommendation(ReorgRecommendation reorgRecommendation) throws Exception {
		return spaceDiagnosticsDao.getOpenPOPRecommendation(reorgRecommendation);
	}
	
	@Override
	public List<PartitionRecommendation> partitionRecommendationList(PartitionRecommendation partitionRecommendation) throws Exception {
		return spaceDiagnosticsDao.partitionRecommendationList(partitionRecommendation);
	}
	
	@Override
	public List<PartitionRecommendation> accessPathList(PartitionRecommendation partitionRecommendation) throws Exception {
		return spaceDiagnosticsDao.accessPathList(partitionRecommendation);
	}
	
	@Override
	public PartitionRecommendation getPartitionKeyRecommendation(PartitionRecommendation partitionRecommendation) throws Exception {
		return spaceDiagnosticsDao.getPartitionKeyRecommendation(partitionRecommendation);
	}
	
	@Override
	public List<PartitionRecommendation> partitionKeyRecommendationList(PartitionRecommendation partitionRecommendation) throws Exception {
		return spaceDiagnosticsDao.partitionKeyRecommendationList(partitionRecommendation);
	}
}
