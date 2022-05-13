package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.TuningTargetSql;

/***********************************************************
 * 2018.06.01	이원식	최초작업
 * 2021.07.14	이재우	성능개선실적 > 산출물 상세조회 추가.
 **********************************************************/

public interface PerformanceImprovementOutputsDao {	
	public List<TuningTargetSql> performanceImprovementOutputsList(TuningTargetSql tuningTargetSql);

	public List<LinkedHashMap<String, Object>> performanceImprovementOutputsList4Excel(TuningTargetSql tuningTargetSql);
	
	public TuningTargetSql getPerformanceImprovementOutputsViewDetail( TuningTargetSql tuningTargetSql );

	public List<TuningTargetSql> performanceImprovementOutputsList_V2( TuningTargetSql tuningTargetSql );

	public List<LinkedHashMap<String, Object>> performanceImprovementOutputsList4Excel_V2(
			TuningTargetSql tuningTargetSql );

	public List<TuningTargetSql> performanceImprovementOutputsListAll(TuningTargetSql tuningTargetSql);

	public List<TuningTargetSql> performanceImprovementOutputsListAll_V2(TuningTargetSql tuningTargetSql);
}
