package omc.mqm.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.mqm.model.ModelEntityType;
import omc.spop.model.Cd;

public interface ModelEntityTypeMngDao {

	List<ModelEntityType> getModelEntityTypeList(ModelEntityType modelEntityType);

	int getDupCnt(ModelEntityType modelEntityType);

	int insertModelEntityType(ModelEntityType modelEntityType);

	int updateModelEntityType(ModelEntityType modelEntityType);

	int deleteModelEntityType(ModelEntityType modelEntityType);

	List<LinkedHashMap<String, Object>> excelDownload(ModelEntityType modelEntityType);

}
