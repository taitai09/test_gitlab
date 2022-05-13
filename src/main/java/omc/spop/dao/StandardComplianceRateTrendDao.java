package omc.spop.dao;

import java.util.List;

import omc.spop.model.SQLStandards;
import omc.spop.model.StandardComplianceRateTrend;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 * 2022.01.05	이재우		스케줄러 load 추가
 **********************************************************/

public interface StandardComplianceRateTrendDao {
	public List<StandardComplianceRateTrend> loadSQL(SQLStandards sqlStandards);

	public List<StandardComplianceRateTrend> loadSchedulerList(StandardComplianceRateTrend scrt);

}
