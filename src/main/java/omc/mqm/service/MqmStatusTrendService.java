package omc.mqm.service;

import java.util.List;

import omc.spop.model.Result;
import omc.spop.model.StructuralQualityStatusTrend;

/***********************************************************
 * 2019.08.26	임호경		OPENPOP V2 최초작업
 **********************************************************/

public interface MqmStatusTrendService {

	List<StructuralQualityStatusTrend> getChartNonCompliantStandardizationRateTrend(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getChartStatusByModel(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getStatusByModelAll(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getChartStandardizationRateStatusByModel(StructuralQualityStatusTrend sqst);

	List<StructuralQualityStatusTrend> getChartNonComplianceStatus(StructuralQualityStatusTrend sqst);

	Result getChartNumberNonCompliantByModel(StructuralQualityStatusTrend sqst) throws Exception;

	Result getChartStandardComplianceRateTrend(omc.spop.model.StructuralQualityStatusTrend sqst) throws Exception;

	
}
