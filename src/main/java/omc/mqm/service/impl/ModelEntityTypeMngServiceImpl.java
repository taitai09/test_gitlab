package omc.mqm.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.mqm.dao.ModelEntityTypeMngDao;
import omc.mqm.model.ModelEntityType;
import omc.mqm.service.ModelEntityTypeMngService;

/***********************************************************
 * 2018.08.23 임호경 최초작성
 **********************************************************/

@Service("modelEntityTypeMngService")
public class ModelEntityTypeMngServiceImpl implements ModelEntityTypeMngService {
	private static final Logger logger = LoggerFactory.getLogger(ModelEntityTypeMngServiceImpl.class);

	@Autowired
	private ModelEntityTypeMngDao modelEntityTypeMngDao;

	@Override
	public List<ModelEntityType> getModelEntityTypeList(ModelEntityType modelEntityType) throws Exception {
		return modelEntityTypeMngDao.getModelEntityTypeList(modelEntityType);
	}

	@Override
	public int getDupCnt(ModelEntityType modelEntityType) throws Exception {
		return modelEntityTypeMngDao.getDupCnt(modelEntityType);
	}

	@Override
	public int insertModelEntityType(ModelEntityType modelEntityType) throws Exception {
		return modelEntityTypeMngDao.insertModelEntityType(modelEntityType);
	}

	@Override
	public int updateModelEntityType(ModelEntityType modelEntityType) throws Exception {
		return modelEntityTypeMngDao.updateModelEntityType(modelEntityType);
	}

	@Override
	public int deleteModelEntityType(ModelEntityType modelEntityType) throws Exception {
		return modelEntityTypeMngDao.deleteModelEntityType(modelEntityType);
	}

	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(ModelEntityType modelEntityType) {
		return modelEntityTypeMngDao.excelDownload(modelEntityType);
	}

}
