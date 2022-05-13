package omc.mqm.service;

import java.util.List;

import omc.mqm.model.ModelEntityType;
import omc.spop.model.Cd;

public interface MqmCommonService {
	public List<Cd> getAllLibNm(ModelEntityType modelEntityType) throws Exception;

	public List<Cd> getAllModelNm(ModelEntityType modelEntityType) throws Exception;

	public List<Cd> getAllSubNm(ModelEntityType modelEntityType) throws Exception;

	public List<Cd> getLibNm(ModelEntityType modelEntityType) throws Exception;

	public List<Cd> getModelNm(ModelEntityType modelEntityType) throws Exception;

	public List<Cd> getSubNm(ModelEntityType modelEntityType) throws Exception;
}
