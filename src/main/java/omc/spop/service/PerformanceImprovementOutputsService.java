package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.DownLoadFile;
import omc.spop.model.TuningTargetSql;

/***********************************************************
 * 2018.06.01	이원식	최초작업
 * 2021.07.14	이재우	성능개선실적 > 산출물 상세조회 추가.
 **********************************************************/

public interface PerformanceImprovementOutputsService {
	/** 성능개선결과 산출물 리스트 */
	List<TuningTargetSql> performanceImprovementOutputsList( TuningTargetSql tuningTargetSql ) throws Exception;
	
	/** 성능개선결과 산출물 리스트 수협*/
	List<TuningTargetSql> performanceImprovementOutputsList_V2( TuningTargetSql tuningTargetSql ) throws Exception;
	
	/** 성능개선결과 산출물 엑셀 다운로드 */
	List<LinkedHashMap<String, Object>> performanceImprovementOutputsList4Excel( TuningTargetSql tuningTargetSql ) throws Exception;
	
	/** 성능개선결과 산출물 엑셀 다운로드 수협*/
	List<LinkedHashMap<String, Object>> performanceImprovementOutputsList4Excel_V2( TuningTargetSql tuningTargetSql ) throws Exception;
	
	/** 성능개선결과 산출물 다운로드 */
	DownLoadFile getPerformanceImprovementOutputs( TuningTargetSql tuningTargetSql ) throws Exception;
	
	/** 성능개선결과 산출물 다운로드 전체 */
	DownLoadFile getPerformanceImprovementOutputsAll( TuningTargetSql tuningTargetSql ) throws Exception;

	/** 성능개선결과 산출물 상세조회 */
	TuningTargetSql getPerformanceImprovementOutputsViewDetail( TuningTargetSql tuningTargetSql ) throws Exception;

	/** 산출물 다운로드 ListAll 조회 */
	List<TuningTargetSql> performanceImprovementOutputsListAll( TuningTargetSql tuningTargetSql ) throws Exception;

	/** 산출물 다운로드 ListAll 조회 수협 */
	List<TuningTargetSql> performanceImprovementOutputsListAll_V2(TuningTargetSql tuningTargetSql);
}