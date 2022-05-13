package omc.mqm.dao;

import java.util.List;

import omc.mqm.model.ModelEntityType;
import omc.spop.model.Cd;

public interface MqmCommonDao {

	List<Cd> getAllLibNm(ModelEntityType modelEntityType);

	List<Cd> getAllModelNm(ModelEntityType modelEntityType);

	List<Cd> getAllSubNm(ModelEntityType modelEntityType);

	List<Cd> getLibNm(ModelEntityType modelEntityType);

	List<Cd> getModelNm(ModelEntityType modelEntityType);

	List<Cd> getSubNm(ModelEntityType modelEntityType);
}
