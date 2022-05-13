package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.OdsDataFiles;
import omc.spop.model.OdsSegments;
import omc.spop.model.OdsTabModifications;
import omc.spop.model.OdsTables;
import omc.spop.model.OdsTablespaces;
import omc.spop.model.Result;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 * 2018.04.27	이원식	오브젝트 분석 => SPACE 분석으로 변경
 **********************************************************/

public interface SpaceAnalysisService {
	/** Tablespace 분석 - 리스트 */
	List<OdsTablespaces> tablespaceAnalysisList(OdsTablespaces odsTablespaces) throws Exception;
	
	/** Tablespace 분석 - DB Size Growth Chart 리스트 */
	List<OdsTablespaces> dbSizeChartList(OdsTablespaces odsTablespaces) throws Exception;
	
	/** Tablespace 분석 - TOP Tablespace Growth Chart 리스트 */
	Result topTablespaceChartList(OdsTablespaces odsTablespaces) throws Exception;	
	
	/** Tablespace 분석 상세 - Segment 리스트 */
	List<OdsSegments> segmentList(OdsSegments odsSegments) throws Exception;
	
	/** Tablespace 분석 상세 - segment statistics chart 리스트 */
	List<OdsSegments> segmentStatisticsList(OdsSegments odsSegments) throws Exception;
	
	/** Tablespace 분석 상세 - segment size history chart 리스트 */
	List<OdsSegments> segmentSizeHistoryList(OdsSegments odsSegments) throws Exception;
	
	/** Tablespace 분석 상세 - dataFile 리스트 */
	List<OdsDataFiles> dataFileList(OdsDataFiles odsDataFiles) throws Exception;
	
	/** Tablespace 분석 상세 - datafile Statistics chart 리스트 */
	List<OdsDataFiles> datafileStatisticsList(OdsDataFiles odsDataFiles) throws Exception;
	
	/** Reorg 대상 분석 - 리스트 */
	List<OdsTables> reorgTargetAnalysisList(OdsTables odsTables) throws Exception;
	
	/** Reorg 대상 분석 - top dml table chart 리스트 */
	List<OdsTables> topDMLTableChartList(OdsTables odsTables) throws Exception;
	
	/** Reorg 대상 분석 - top dml table history chart 리스트 */
	Result topDMLTableHistoryChartList(OdsTables odsTables) throws Exception;
	
	/** Reorg 대상 분석 - 상세 - dml history chart 리스트 */
	List<OdsTables> dmlHistoryChartList(OdsTables odsTables) throws Exception;	
	
	/** 파티셔닝 대상 분석 - 리스트 */
	List<OdsTables> partitionTargetAnalysisList(OdsTables odsTables) throws Exception;
	
	/** 파티셔닝 대상 분석 - top size table chart 리스트 */
	List<OdsSegments> topSizeTableChartList(OdsSegments odsSegments) throws Exception;
	
	/** 파티셔닝 대상 분석 - top size table history chart 리스트 */
	Result topSizeTableHistoryChartList(OdsSegments odsSegments) throws Exception;
	
	/** 파티셔닝 대상 분석 - 상세 - table size history chart 리스트 */
	List<OdsTables> tableSizeHistoryChartList(OdsTables odsTables) throws Exception;
	
	/** 파티셔닝 대상 분석 - 상세 - access path 리스트 */
	List<OdsTables> accessPathList(OdsTables odsTables) throws Exception;
	
	/** 파티셔닝 대상 분석 - 상세 - partition Recommendation total query count */
	OdsTables getPartitionRecommendation(OdsTables odsTables) throws Exception;
	
	/** 파티셔닝 대상 분석 - 상세 - partition Recommendation 리스트 */
	List<OdsTables> partitionRecommendationList(OdsTables odsTables) throws Exception;	
	
	/** 데이터파일 I/O 분석 - 리스트 */
	List<OdsDataFiles> dataIOAnalysisList(OdsDataFiles odsDataFiles) throws Exception;
	
	/** 데이터파일 I/O 분석 - TOP I/O DataFIle chart 리스트 */
	List<OdsDataFiles> datafileIOChartList(OdsDataFiles odsDataFiles) throws Exception;
	
	/** 데이터파일 I/O 분석 - DataFIle I/O History chart 리스트 */
	List<OdsDataFiles> datafileIOHistoryChartList(OdsDataFiles odsDataFiles) throws Exception;	
	
	/** Table별 DML 변경 내역 - 테이블 통계정보 리스트 */
	List<OdsTables> dmlChangeTableList(OdsTables odsTables) throws Exception;
	
	/** Table별 DML 변경 내역 - DML 발생현황 리스트 */
	List<OdsTabModifications> dmlOccurrenceList(OdsTabModifications odsTabModifications) throws Exception;
	
	/** 일자별 DML 변경 내역 리스트 */
	List<OdsTabModifications> dmlChangeDailyList(OdsTabModifications odsTabModifications) throws Exception;

	/** Table별 DML 변경 내역 - 테이블 통계정보 리스트 엑셀 다운 */
	List<LinkedHashMap<String, Object>> dmlChangeDailyListByExcelDown(OdsTabModifications odsTabModifications);
}
