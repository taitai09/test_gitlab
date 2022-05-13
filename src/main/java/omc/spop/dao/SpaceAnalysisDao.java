package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.OdsDataFiles;
import omc.spop.model.OdsSegments;
import omc.spop.model.OdsTabModifications;
import omc.spop.model.OdsTables;
import omc.spop.model.OdsTablespaces;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 * 2018.04.27	이원식	오브젝트 분석 => SPACE 분석으로 변경
 **********************************************************/

public interface SpaceAnalysisDao {		
	public List<OdsTablespaces> tablespaceAnalysisList(OdsTablespaces odsTablespaces);
	
	public List<OdsTablespaces> dbSizeChartList(OdsTablespaces odsTablespaces);
	
	public List<OdsTablespaces> topTablespaceChartLegendList(OdsTablespaces odsTablespaces);
	
	public List<OdsTablespaces> topTablespaceChartList(OdsTablespaces odsTablespaces);
	
	public List<OdsSegments> segmentList(OdsSegments odsSegments);
	
	public List<OdsSegments> segmentStatisticsList(OdsSegments odsSegments);
	
	public List<OdsSegments> segmentSizeHistoryList(OdsSegments odsSegments);
	
	public List<OdsDataFiles> dataFileList(OdsDataFiles odsDataFiles);
	
	public List<OdsDataFiles> datafileStatisticsList(OdsDataFiles odsDataFiles);
	
	public List<OdsTables> reorgTargetAnalysisList(OdsTables odsTables);
	
	public List<OdsTables> topDMLTableChartList(OdsTables odsTables);
	
	public List<OdsTables> topDMLTableHistoryChartLegendList(OdsTables odsTables);
	
	public List<OdsTables> topDMLTableHistoryChartList(OdsTables odsTables);
	
	public List<OdsTables> dmlHistoryChartList(OdsTables odsTables);
	
	public List<OdsTables> partitionTargetAnalysisList(OdsTables odsTables);
	
	public List<OdsSegments> topSizeTableChartList(OdsSegments odsSegments);
	
	public List<OdsSegments> topSizeTableHistoryChartLegendList(OdsSegments odsSegments);
	
	public List<OdsSegments> topSizeTableHistoryChartList(OdsSegments odsSegments);	
	
	public List<OdsTables> tableSizeHistoryChartList(OdsTables odsTables);
	
	public List<OdsTables> accessPathList(OdsTables odsTables);
	
	public OdsTables getPartitionRecommendation(OdsTables odsTables);
	
	public List<OdsTables> partitionRecommendationList(OdsTables odsTables);
	
	public List<OdsDataFiles> dataIOAnalysisList(OdsDataFiles odsDataFiles);
	
	public List<OdsDataFiles> datafileIOChartList(OdsDataFiles odsDataFiles);
	
	public List<OdsDataFiles> datafileIOHistoryChartList(OdsDataFiles odsDataFiles);
	
	public List<OdsTables> dmlChangeTableList(OdsTables odsTables);
	
	public List<OdsTabModifications> dmlOccurrenceList(OdsTabModifications odsTabModifications);
	
	public List<OdsTabModifications> dmlChangeDailyList(OdsTabModifications odsTabModifications);

	public List<LinkedHashMap<String, Object>> dmlChangeDailyListByExcelDown(OdsTabModifications odsTabModifications);
}
