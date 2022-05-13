package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.PartitionManagementDao;
import omc.spop.model.PartitionTableManagement;
import omc.spop.service.PartitionManagementService;

/***********************************************************
 * 2018.09.07 임호경 최초작성
 *******************************/

@Service("partitionManagementService")
public class PartitionManagementServiceImpl implements PartitionManagementService {

	@Autowired
	private PartitionManagementDao partitionManagementDao;

	@Override
	public List<PartitionTableManagement> getPartitionList(PartitionTableManagement partitionTableManagement) {
		// TODO Auto-generated method stub
		return partitionManagementDao.getPartitionList(partitionTableManagement);
	}

	@Override
	public List<PartitionTableManagement> selectTableName(PartitionTableManagement partitionTableManagement) {
		return partitionManagementDao.selectTableName(partitionTableManagement);
	}

	@Override
	public int deletePartitionTable(PartitionTableManagement partitionTableManagement) {
		// TODO Auto-generated method stub
		return partitionManagementDao.deletePartitionTable(partitionTableManagement);
	}

	@Override
	public int updatePartitionTable(PartitionTableManagement partitionTableManagement) {
		// TODO Auto-generated method stub
		return partitionManagementDao.updatePartitionTable(partitionTableManagement);
	}

	@Override
	public int insertPartitionTable(PartitionTableManagement partitionTableManagement) {
		// TODO Auto-generated method stub
		return partitionManagementDao.insertPartitionTable(partitionTableManagement);
	}

	@Override
	public int checkTableName(PartitionTableManagement partitionTableManagement) {
		// TODO Auto-generated method stub
		return partitionManagementDao.checkTableName(partitionTableManagement);
	}

	@Override
	public List<PartitionTableManagement> selectType(PartitionTableManagement partitionTableManagement) {
		return partitionManagementDao.selectType(partitionTableManagement);
	}

}
