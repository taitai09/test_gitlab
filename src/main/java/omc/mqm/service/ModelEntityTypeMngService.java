package omc.mqm.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.mqm.model.ModelEntityType;

public interface ModelEntityTypeMngService {

	public List<ModelEntityType> getModelEntityTypeList(ModelEntityType modelEntityType) throws Exception;

	public int getDupCnt(ModelEntityType modelEntityType) throws Exception;

	public int insertModelEntityType(ModelEntityType modelEntityType) throws Exception;

	public int updateModelEntityType(ModelEntityType modelEntityType) throws Exception;

	public int deleteModelEntityType(ModelEntityType modelEntityType) throws Exception;

	public List<LinkedHashMap<String, Object>> excelDownload(ModelEntityType modelEntityType);
}
