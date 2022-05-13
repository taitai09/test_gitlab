package omc.spop.dao;

import java.util.List;

import omc.spop.model.MotoringGroup;

/***********************************************************
 * 2018.10.05 	임호경	최초작성
 * 2020.05.20	이재우	기능개선
 **********************************************************/

public interface DashboardMngDao {

	int deleteMotoringGroup(MotoringGroup motoringGroup);

	int insertMotoringGroup(MotoringGroup motoringGroup);

	int updateMotoringGroup(MotoringGroup motoringGroup);

	int checkMotoringGroupId(MotoringGroup motoringGroup);

	List<MotoringGroup> getMotoringGroupList(MotoringGroup motoringGroup);

	int saveDatabaseGroup(MotoringGroup motoringGroup);

	List<MotoringGroup> getDatabaseGroupList(MotoringGroup motoringGroup);

	int deleteDatabaseGroup(MotoringGroup motoringGroup);

	List<MotoringGroup> getMotoringGroupId(MotoringGroup motoringGroup);

	int checkDatabaseGroupId(MotoringGroup motoringGroup);

	int checkMotoringDesplaySeq(MotoringGroup motoringGroup);

	int checkDatabaseDesplaySeq(MotoringGroup motoringGroup);

	MotoringGroup getMotoringOneGroupId(MotoringGroup motoringGroup);

	MotoringGroup getDatabaseOneGroupId(MotoringGroup motoringGroup);
}
