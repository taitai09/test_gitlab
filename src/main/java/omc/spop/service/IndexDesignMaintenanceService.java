package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.AccPath;
import omc.spop.model.AccPathIndexDesign;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.IdxAdMst;
import omc.spop.model.OdsIndexs;
import omc.spop.model.OdsTabColumns;
import omc.spop.model.OdsTables;
import omc.spop.model.VsqlText;

/***********************************************************
 * 2018.03.15 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface IndexDesignMaintenanceService {
	/** ACCESS_PATH_EXEC 정보 조회 */
	String getAccessPathExec(OdsTables odsTables) throws Exception;

	/** 수집 SQL 인덱스 설계 - ODS_TABLES 리스트 */
	List<OdsTables> collectionOdsTableList(OdsTables odsTables) throws Exception;

	List<LinkedHashMap<String, Object>> collectionOdsTableList4Excel(OdsTables odsTables);

	/** 수집 SQL 인덱스 설계 - ACCESS_PATH 리스트 */
	List<AccPath> collectionAccessPathList(AccPath accPath) throws Exception;

	/** 수집 SQL 인덱스 설계 - SQL 현황 리스트 */
	List<VsqlText> collectionSqlStatusList(VsqlText vsqlText) throws Exception;

	/** 수집 SQL 인덱스 정비 리스트 */
	List<OdsIndexs> collectionIndexUsageList(OdsIndexs odsIndexs) throws Exception;

	List<LinkedHashMap<String, Object>> collectionIndexUsageList4Excel(OdsIndexs odsIndexs);

	/** 적재SQL 인덱스 설계 - ODS_TABLES 리스트 */
	List<OdsTables> loadOdsTableList(OdsTables odsTables) throws Exception;

	/** 적재SQL 인덱스 설계 - ACCESS_PATH 리스트 */
	List<AccPath> loadAccessPathList(AccPath accPath) throws Exception;

	/** 적재SQL 인덱스 설계 - SQL 현황 리스트 */
	List<DbioLoadSql> loadSqlStatusList(DbioLoadSql dbioLoadSql) throws Exception;

	/** 적재SQL 인덱스 정비 리스트 */
	List<OdsIndexs> loadUsingIndexList(OdsIndexs odsIndexs) throws Exception;

	List<LinkedHashMap<String, Object>> loadUsingIndexList4Excel(OdsIndexs odsIndexs);

	/** 공통 함수 **/
	/** COLUMNS 리스트 */
	List<OdsTabColumns> columnsList(OdsTabColumns odsTabColumns) throws Exception;

	/** ODS_INDEXS 리스트 */
	List<OdsIndexs> indexsList(OdsIndexs odsIndexs) throws Exception;

	/** ACC_PATH_INDEX_DESIGN 리스트 */
	List<AccPathIndexDesign> accPathIndexDesignList(AccPathIndexDesign accPathIndexDesign) throws Exception;

	/** ACC_PATH_INDEX_DESIGN INSERT */
	void insertIndexDesign(AccPathIndexDesign accPathIndexDesign) throws Exception;

	/** IDX_AD_MST 리스트 [AUTO_INDEX_DESIGN Server모듈 실행] */
	List<IdxAdMst> startIndexAutoDesign(OdsTables odsTables) throws Exception;

	/** 인덱스설계 엑셀 리스트 */
	List<AccPathIndexDesign> indexDesignList(AccPathIndexDesign accPathIndexDesign) throws Exception;

	List<OdsIndexs> indexList(OdsIndexs odsIndexs) throws Exception;

}