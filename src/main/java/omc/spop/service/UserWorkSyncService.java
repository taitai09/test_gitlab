package omc.spop.service;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public interface UserWorkSyncService {

	int syncWrkJobCd();

	int syncUsers();

	int syncUserWrkjob();

	int syncWrkRelatedTable();

	int scheduledSyncWrkjob() throws Exception;

	int scheduledSyncUser() throws Exception;

	int scheduledSyncUserWrkjob() throws Exception;

	int scheduledSyncWrkRelatedTable() throws Exception;

}
