package omc.mqm.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.mqm.dao.MqmCommonDao;
import omc.mqm.model.ModelEntityType;
import omc.mqm.service.MqmCommonService;
import omc.spop.model.Cd;

/***********************************************************
 * 2018.08.23 임호경 최초작성
 **********************************************************/

@Service("mqmCommonService")
public class MqmCommonServiceImpl implements MqmCommonService {
	private static final Logger logger = LoggerFactory.getLogger(MqmCommonServiceImpl.class);

	@Autowired
	private MqmCommonDao mqmCommonDao;

	@Override
	public List<Cd> getAllLibNm(ModelEntityType modelEntityType) throws Exception {
		return mqmCommonDao.getAllLibNm(modelEntityType);
	}

	@Override
	public List<Cd> getAllModelNm(ModelEntityType modelEntityType) throws Exception {
		return mqmCommonDao.getAllModelNm(modelEntityType);
	}

	@Override
	public List<Cd> getAllSubNm(ModelEntityType modelEntityType) throws Exception {
		return mqmCommonDao.getAllSubNm(modelEntityType);
	}

	@Override
	public List<Cd> getLibNm(ModelEntityType modelEntityType) throws Exception {
		return mqmCommonDao.getLibNm(modelEntityType);
	}

	@Override
	public List<Cd> getModelNm(ModelEntityType modelEntityType) throws Exception {
		return mqmCommonDao.getModelNm(modelEntityType);
	}

	@Override
	public List<Cd> getSubNm(ModelEntityType modelEntityType) throws Exception {
		return mqmCommonDao.getSubNm(modelEntityType);
	}

}
