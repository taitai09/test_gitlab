package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.APMConnectionDao;
import omc.spop.model.ApmConnection;
import omc.spop.service.APMConnectionService;

/***********************************************************
 * 2018.09.04	임호경	최초작성
 **********************************************************/

@Service("apmConnectionService")
public class APMConnectionServiceImpl implements APMConnectionService {
	@Autowired
	private APMConnectionDao apmConnectionDao;


	@Override
	public List<ApmConnection> getApmList(ApmConnection apmConnection) {
		return apmConnectionDao.getApmList(apmConnection);
	}


	@Override
	public List<ApmConnection> getOnlyWrkJobCd(ApmConnection apmConnection) {
		return apmConnectionDao.getOnlyWrkJobCd(apmConnection);
	}


	@Override
	public int checkApmCd(ApmConnection apmConnection) {
		// TODO Auto-generated method stub
		return apmConnectionDao.checkApmCd(apmConnection);
	}


	@Override
	public int updateApm(ApmConnection apmConnection) {
		// TODO Auto-generated method stub
		return apmConnectionDao.updateApm(apmConnection);
	}
	
	@Override
	public int updateApmOthers(ApmConnection apmConnection) {
		return apmConnectionDao.updateApmOthers(apmConnection);
	}


	@Override
	public int insertApm(ApmConnection apmConnection) {
		// TODO Auto-generated method stub
		return apmConnectionDao.insertApm(apmConnection);
	}


	@Override
	public int deleteApm(ApmConnection apmConnection) {
		return apmConnectionDao.deleteApm(apmConnection);
	}


	@Override
	public int checkApmOthers(ApmConnection apmConnection) {
		return apmConnectionDao.checkApmOthers(apmConnection);
	}





	
 
}
