package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.UserWorkSyncDao;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;
import omc.spop.service.UserWorkSyncService;

/***********************************************************
 * 2019.07.01 developer1
 * 2021.01.21 throws 추가하여 상위 메소드에 오류 전가
 * 2021.11.30	이재우	스케쥴러 동작 100건씩 동기화 되도록 수정.
 **********************************************************/

@Service("userWorkSyncService")
public class UserWorkSyncServiceImpl implements UserWorkSyncService {
	private static final Logger logger = LoggerFactory.getLogger(UserWorkSyncServiceImpl.class);

	@Autowired
	private UserWorkSyncDao userWorkSyncDao;

	@Override
	public int syncWrkJobCd() {
		List<WrkJobCd> wrkJobCdList = userWorkSyncDao.searchWorkJobCd();
		logger.debug("wrkJobCdList :" + wrkJobCdList);
		int deleteResult = userWorkSyncDao.deleteKbcdWrkJobCd();
		logger.debug("deleteResult :" + deleteResult);
		int insertResult = 0;
//		for(WrkJobCd cd:wrkjobcd) {
//			insertResult += userWorkSyncDao.insertKbcdWrkJobCd(cd);
//			logger.debug("insertResult :"+insertResult);
//		}
		insertResult += userWorkSyncDao.insertKbcdWrkJobCdAtOnce(wrkJobCdList);

		int insertWrkjobCdResult = userWorkSyncDao.insertWrkjobCd();
		logger.debug("insertWrkjobCdResult :" + insertWrkjobCdResult);
		return insertResult;
	}

	@Override
	public int syncUsers() {
		List<Users> userList = userWorkSyncDao.searchUsers();
		logger.debug("userList :" + userList);
		int deleteResult = userWorkSyncDao.deleteKbcdUsers();
		logger.debug("deleteResult :" + deleteResult);
		int insertResult = 0;
//		for(Users user:users) {
//			insertResult += userWorkSyncDao.insertKbcdUser(user);
//			logger.debug("insertResult :"+insertResult);
//		}
		insertResult += userWorkSyncDao.insertKbcdUserAtOnce(userList);

		int mergeUsersResult = userWorkSyncDao.mergeUsers();
		logger.debug("mergeUsersResult :" + mergeUsersResult);
		
		int mergeUserAuthResult = userWorkSyncDao.mergeUserAuth();
		logger.debug("mergeUserAuthResult :" + mergeUserAuthResult);
				
		return insertResult;
	}

	/**
	 * 사용자 업무코드 동기화
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public int syncUserWrkjob() {
		List<UserWrkjob> userWrkjobList = userWorkSyncDao.searchUserWrkjob();
		logger.debug("userWrkjobList :" + userWrkjobList);
		int deleteResult = userWorkSyncDao.deleteKbcdUserWrkjob();
		logger.debug("deleteResult :" + deleteResult);
		int insertResult = 0;
//		for(UserWrkjob cd:userWrkjob) {
//			insertResult += userWorkSyncDao.insertKbcdUserWrkjob(cd);
//			logger.debug("insertResult :"+insertResult);
//		}
		insertResult += userWorkSyncDao.insertKbcdUserWrkjobAtOnce(userWrkjobList);

		int insertUserWrkjobResult = userWorkSyncDao.insertUserWrkjob();
		logger.debug("insertUserWrkjobResult :" + insertUserWrkjobResult);
		return insertResult;
	}

	/**
	 * 업무코드, 사용자, 사용자 업무코드 동기화
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public int syncWrkRelatedTable() {
		int syncResult = 0;
		syncResult += syncWrkJobCd();
		syncResult += syncUsers();
		syncResult += syncUserWrkjob();
		return syncResult;
	}

	@Override
	public int scheduledSyncWrkjob() throws Exception {
		int insertResult = 0,insetCnt = 0;
		
		List<WrkJobCd> tmpWrkJobCdList = new ArrayList<WrkJobCd>();
		List<WrkJobCd> wrkJobCdList = userWorkSyncDao.searchWorkJobCd2();
		logger.debug("wrkJobCdList :" + wrkJobCdList);
		
		if ( wrkJobCdList != null && wrkJobCdList.size() > 0 ) {
			int wrkJobCdListCnt = wrkJobCdList.size();
			int deleteResult = userWorkSyncDao.deleteKbcdWrkJobCd();
			logger.debug( "deleteResult :" + deleteResult );
			
			for ( WrkJobCd tmpWrkjobCd : wrkJobCdList ) {
				insetCnt++;
				tmpWrkJobCdList.add( tmpWrkjobCd );
				
				//tmpWrkJobCdList 가 100개 채워지거나, tmpWrkJobCdList의 최종 리스트 인경우 insert all 수행
				if ( insetCnt%100 == 0 || insetCnt == wrkJobCdListCnt ) {
					insertResult += userWorkSyncDao.insertKbcdWrkJobCdAtOnce( tmpWrkJobCdList );
					tmpWrkJobCdList.clear();
				}
			}
			
			int insertWrkjobCdResult = userWorkSyncDao.insertWrkjobCd();
			logger.debug("insertWrkjobCdResult :" + insertWrkjobCdResult);
		}
		
		return insertResult;
	}

	@Override
	public int scheduledSyncUser() throws Exception {
		int insertResult = 0,insetCnt = 0;
		
		List<Users> tmpUserList = new ArrayList<Users>();
		List<Users> userList = userWorkSyncDao.searchUsers2();
		logger.debug("userList :" + userList);
		
		if ( userList != null && userList.size() > 0 ) {
			int userListCnt = userList.size();
			int deleteResult = userWorkSyncDao.deleteKbcdUsers();
			logger.debug("deleteResult :" + deleteResult);
			
			for ( Users tmpUser : userList ) {
				insetCnt++;
				tmpUserList.add( tmpUser );
				
				//tmpUserList 가 100개 채워지거나, tmpUserList의 최종 리스트 인경우 insert all 수행
				if ( insetCnt%100 == 0 || insetCnt == userListCnt ) {
					insertResult += userWorkSyncDao.insertKbcdUserAtOnce( tmpUserList );
					tmpUserList.clear();
				}
			}
			
			int mergeUsersResult = userWorkSyncDao.mergeUsers();
			logger.debug("mergeUsersResult :" + mergeUsersResult);
			
			int mergeUserAuthResult = userWorkSyncDao.mergeUserAuth();
			logger.debug("mergeUserAuthResult :" + mergeUserAuthResult);
		}
		
		return insertResult;
	}

	@Override
	public int scheduledSyncUserWrkjob() throws Exception {
		int insertResult = 0,insetCnt = 0;
		
		List<UserWrkjob> userWrkjobList = userWorkSyncDao.searchUserWrkjob2();
		List<UserWrkjob> tmpUserWrkjobList = new ArrayList<UserWrkjob>();
		logger.debug("userWrkjobList :" + userWrkjobList);
		
		if ( userWrkjobList != null && userWrkjobList.size() > 0 ) {
			int userWrkjobListCnt = userWrkjobList.size();
			int deleteResult = userWorkSyncDao.deleteKbcdUserWrkjob();
			logger.debug("deleteResult :" + deleteResult);
			
			for ( UserWrkjob tmpUserWrkjob : userWrkjobList ) {
				insetCnt++;
				tmpUserWrkjobList.add( tmpUserWrkjob );
				
				//tmpUserWrkjobList 가 100개 채워지거나, tmpUserWrkjobList의 최종 리스트 인경우 insert all 수행
				if ( insetCnt%100 == 0 || insetCnt == userWrkjobListCnt ) {
					insertResult += userWorkSyncDao.insertKbcdUserWrkjobAtOnce( tmpUserWrkjobList );
					tmpUserWrkjobList.clear();
				}
			}
			
			int insertUserWrkjobResult = userWorkSyncDao.insertUserWrkjob();
			logger.debug("insertUserWrkjobResult :" + insertUserWrkjobResult);
		}
		
		return insertResult;
	}

	@Override
	public int scheduledSyncWrkRelatedTable() throws Exception {
		int syncResult = 0;
		syncResult += scheduledSyncWrkjob();
		syncResult += scheduledSyncUser();
		syncResult += scheduledSyncUserWrkjob();
		return syncResult;
	}
}
