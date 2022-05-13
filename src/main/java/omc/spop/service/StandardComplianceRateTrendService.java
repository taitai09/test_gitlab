package omc.spop.service;

import java.util.List;

import omc.spop.model.Result;
import omc.spop.model.StandardComplianceRateTrend;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 * 2022.01.05	이재우		스케줄러 load 추가.
 **********************************************************/

public interface StandardComplianceRateTrendService {
	
	List<StandardComplianceRateTrend> loadChartStandardComplianceRateTrendTotal(StandardComplianceRateTrend scrt) throws Exception;
	
	List<StandardComplianceRateTrend> loadStatusByWork(StandardComplianceRateTrend scrt) throws Exception;
	
	List<StandardComplianceRateTrend> loadChartStatusByWork(StandardComplianceRateTrend scrt) throws Exception;
	
	List<StandardComplianceRateTrend> loadChartNonComplianceStatus(StandardComplianceRateTrend scrt) throws Exception;
	
	Result loadChartStandardComplianceRateTrend(StandardComplianceRateTrend scrt) throws Exception;
	
	Result loadChartNonStandardComplianceRateTrend(StandardComplianceRateTrend scrt) throws Exception;
	
	Result loadDynamicChartSample1(StandardComplianceRateTrend scrt) throws Exception;

	List<StandardComplianceRateTrend> loadSchedulerList(StandardComplianceRateTrend scrt) throws Exception;
}
