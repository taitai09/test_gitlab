package omc.spop.service;

import java.util.List;

import omc.spop.model.DbParameterHistory;
import omc.spop.model.OdsHistParameter;

/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface ParameterAnalysisService {
	/** 파라미터  최근 변경 내역 - Chart 리스트 */
	List<DbParameterHistory> parameterRecentChangesChartList(DbParameterHistory dbParameterHistory) throws Exception;
	
	/** 파라미터  최근 변경 내역 - Detail 리스트 */
	List<DbParameterHistory> parameterRecentChangesDetailList(DbParameterHistory dbParameterHistory) throws Exception;
	
	/** 표준 파라미터 비교 - Chart 리스트 */
	List<DbParameterHistory> standardParameterCompChartList(DbParameterHistory dbParameterHistory) throws Exception;
	
	/** 표준 파라미터 비교 - Detail 리스트 */
	List<DbParameterHistory> standardParameterCompDetailList(DbParameterHistory dbParameterHistory) throws Exception;
	
	/** 인스턴스 파라미터 비교 - Chart 리스트 */
	List<DbParameterHistory> instanceParameterCompChartList(DbParameterHistory dbParameterHistory) throws Exception;
	
	/** 인스턴스 파라미터 비교 - Detail 리스트 */
	List<DbParameterHistory> instanceParameterCompDetailList(DbParameterHistory dbParameterHistory) throws Exception;	
	
	/** 파라미터  변경 이력 리스트 */
	List<OdsHistParameter> parameterChangeHistoryList(OdsHistParameter odsHistParameter) throws Exception;	
}