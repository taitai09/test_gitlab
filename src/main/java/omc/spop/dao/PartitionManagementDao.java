package omc.spop.dao;

import java.util.List;

import omc.spop.model.PartitionTableManagement;

/***********************************************************
 * 2018.09.07 임호경 최초작성
 *******************************/

public interface PartitionManagementDao {

	public List<PartitionTableManagement> getPartitionList(PartitionTableManagement partitionTableManagement);

	public List<PartitionTableManagement> selectTableName(PartitionTableManagement partitionTableManagement);

	public int deletePartitionTable(PartitionTableManagement partitionTableManagement);

	public int updatePartitionTable(PartitionTableManagement partitionTableManagement);

	public int insertPartitionTable(PartitionTableManagement partitionTableManagement);

	public int checkTableName(PartitionTableManagement partitionTableManagement);

	public List<PartitionTableManagement> selectType(PartitionTableManagement partitionTableManagement);

}
