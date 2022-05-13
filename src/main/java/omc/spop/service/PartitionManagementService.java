package omc.spop.service;

import java.util.List;

import omc.spop.model.PartitionTableManagement;

/***********************************************************
 * 2018.09.07 	임호경	 최초작성
 *******************************/

public interface PartitionManagementService {

	List<PartitionTableManagement> getPartitionList(PartitionTableManagement partitionTableManagement);

	List<PartitionTableManagement> selectTableName(PartitionTableManagement partitionTableManagement);

	int deletePartitionTable(PartitionTableManagement partitionTableManagement);

	int updatePartitionTable(PartitionTableManagement partitionTableManagement);

	int insertPartitionTable(PartitionTableManagement partitionTableManagement);

	int checkTableName(PartitionTableManagement partitionTableManagement);

	List<PartitionTableManagement> selectType(PartitionTableManagement partitionTableManagement);


}
