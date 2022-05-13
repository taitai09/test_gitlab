package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.DashboardMngDao;
import omc.spop.model.MotoringGroup;
import omc.spop.service.DashboardMngService;



/***********************************************************
 * 2018.10.05 	임호경 	최초작성
 * 2020.05.20	이재우	기능개선
 **********************************************************/

@Service("DashboardMngService")
public class DashboardMngServiceImpl implements DashboardMngService {


	@Autowired
	private DashboardMngDao DashboardMngDao;

	@Override
	public int deleteMotoringGroup(MotoringGroup motoringGroup) {
		return DashboardMngDao.deleteMotoringGroup(motoringGroup);
	}

	@Override
	public int insertMotoringGroup(MotoringGroup motoringGroup) {
		return DashboardMngDao.insertMotoringGroup(motoringGroup);
	}

	@Override
	public int updateMotoringGroup(MotoringGroup motoringGroup) {
		return DashboardMngDao.updateMotoringGroup(motoringGroup);
	}

	@Override
	public int checkMotoringGroupId(MotoringGroup motoringGroup) {
		return DashboardMngDao.checkMotoringGroupId(motoringGroup);
	}

	@Override
	public List<MotoringGroup> getMotoringGroupList(MotoringGroup motoringGroup) {
		return DashboardMngDao.getMotoringGroupList(motoringGroup);
	}

	@Override
	public int deleteDatabaseGroup(MotoringGroup motoringGroup) {
		return DashboardMngDao.deleteDatabaseGroup(motoringGroup);
	}

	@Override
	public int saveDatabaseGroup(MotoringGroup motoringGroup) {
		return DashboardMngDao.saveDatabaseGroup(motoringGroup);
	}

	@Override
	public List<MotoringGroup> getDatabaseGroupList(MotoringGroup motoringGroup) {
		return DashboardMngDao.getDatabaseGroupList(motoringGroup);
	}

	@Override
	public List<MotoringGroup> getMotoringGroupId(MotoringGroup motoringGroup) {
		return DashboardMngDao.getMotoringGroupId(motoringGroup);
	}

	@Override
	public int checkDatabaseGroupId(MotoringGroup motoringGroup) {
		return DashboardMngDao.checkDatabaseGroupId(motoringGroup);
	}

	@Override
	public int checkMotoringDesplaySeq(MotoringGroup motoringGroup) {
		return DashboardMngDao.checkMotoringDesplaySeq(motoringGroup);
	}

	@Override
	public int checkDatabaseDesplaySeq(MotoringGroup motoringGroup) { 
		return DashboardMngDao.checkDatabaseDesplaySeq(motoringGroup);
	}

	@Override
	public MotoringGroup getMotoringOneGroupId(MotoringGroup motoringGroup) {
		return DashboardMngDao.getMotoringOneGroupId(motoringGroup);
	}

	@Override
	public MotoringGroup getDatabaseOneGroupId(MotoringGroup motoringGroup) {
		return DashboardMngDao.getDatabaseOneGroupId(motoringGroup);
	}
	
}
