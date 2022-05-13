package omc.spop.dao;

import java.util.List;

import omc.spop.model.ApmConnection;

/***********************************************************
 * 2018.09.04	임호경	최초작성
 **********************************************************/

public interface APMConnectionDao {

	List<ApmConnection> getApmList(ApmConnection apmConnection);

	List<ApmConnection> getOnlyWrkJobCd(ApmConnection apmConnection);

	int checkApmCd(ApmConnection apmConnection);

	int updateApm(ApmConnection apmConnection);

	int insertApm(ApmConnection apmConnection);

	int deleteApm(ApmConnection apmConnection);

	int checkApmOthers(ApmConnection apmConnection);

	int updateApmOthers(ApmConnection apmConnection);


}
