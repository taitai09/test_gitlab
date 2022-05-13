package omc.mqm.dao;

import java.util.List;

import omc.spop.model.StructuralQualityStatusTrend;

/***********************************************************
 * 2019.08.26	임호경		OPENPOP 	V2 최초작업
 **********************************************************/

public interface MqmStatusTrendDao {

	List<StructuralQualityStatusTrend> getChartNonCompliantStandardizationRateTrend(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getChartStatusByModel(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getStatusByModelAll(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getChartStandardizationRateStatusByModel(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getChartNonComplianceStatus(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getChartNumberNonCompliantByModel(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getChartStandardComplianceRateTrend(StructuralQualityStatusTrend sqst);
}
