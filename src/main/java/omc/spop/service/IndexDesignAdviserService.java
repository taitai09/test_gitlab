package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.spop.model.AccPathExec;
import omc.spop.model.AccPathExecV2;
import omc.spop.model.IdxAdMst;
import omc.spop.model.IdxAdRecommendIndex;

/***********************************************************
 * 2018.03.20	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface IndexDesignAdviserService {
	/** 인덱스자동설계현황 리스트 */
	List<IdxAdMst> autoIndexStatusList(IdxAdMst idxAdMst) throws Exception;

	/** 인덱스자동설계현황 리스트 엑셀 다운로드*/
	List<LinkedHashMap<String, Object>> autoIndexStatusList4Excel(IdxAdMst idxAdMst) throws Exception;

	/** 인덱스Recommend현황 리스트 */
	List<IdxAdRecommendIndex> indexRecommendStatusList(IdxAdRecommendIndex idxAdRecommendIndex) throws Exception;
	
	/** 인덱스 자동설계 강제완료처리 */
	int updateForceComplete(Map<String, List<String>> paramList) throws Exception;
	int updateForceCompleteList(List<?> paramList) throws Exception;
	
	/** 수집SQL인덱스 자동설계 리스트 */
	List<AccPathExecV2> autoCollectionIndexDesignList(AccPathExec accPathExec) throws Exception;
	
	List<LinkedHashMap<String, Object>> autoCollectionIndexDesignList4Excel(AccPathExec accPathExec);

	/** 적재SQL 인덱스 자동설계 리스트 */
	List<AccPathExecV2> autoLoadIndexDesignList(AccPathExec accPathExec) throws Exception;
	
	/** isTask 인덱스 자동설계 */
	List<AccPathExecV2> isTaskStartIndexAutoDesign(AccPathExec accPathExec) throws Exception;
	
	/** 인덱스 자동설계 */
	void startIndexAutoDesign(AccPathExec accPathExec) throws Exception;

	/** 인덱스 자동설계2 - selectivity_calc_method */
	void startIndexAutoDesign2(AccPathExec accPathExec) throws Exception;


}
