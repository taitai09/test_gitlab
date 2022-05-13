package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.spop.model.Auth;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.UserAuth;
import omc.spop.model.UserDBPrivilege;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;

/***********************************************************
 * 2017.11.09	이원식	최초작성
 **********************************************************/

public interface UserMngDao {	
	public List<Users> usersList(Users users);
	
	public Users checkUserId(Users users);
	
	public void saveUsers(Users users);
	
	public void insertUserAuth(UserAuth userAuth);
	
	public void insertUserWrkJob(UserWrkjob userWrkjob);	
	
	public void approveUser(Users users);
	
	public void updateUserAuth(UserAuth userAuth);
	
	public void updateUserWrkJob(UserWrkjob userWrkjob);
	
	public void resetUserPassword(Users users);
	
	public List<UserAuth> usersAuthList(UserAuth userAuth);
	
	public void saveUsersAuth(UserAuth userAuth);
	
	public void updatePastUsersAuth(UserAuth userAuth);
	
	public List<UserWrkjob> usersWrkJobList(UserWrkjob userWrkjob);
	
	public void saveUsersWrkJob(UserWrkjob userWrkjob);
	
	public void updatePastUsersWrkJob(UserWrkjob userWrkjob);
	
	public UserWrkjob checkWorkJobLeader(UserWrkjob userWrkjob);
	
	//public List<UserDept> usersDeptList(UserDept userDept);
	
	//public void saveUsersDept(UserDept userDept);
	
	//public void updatePastUsersDept(UserDept userDept);
	
	public List<Auth> authList(Auth auth);
	
	public String getMaxAuthId(Auth auth);
	
	public void saveAuth(Auth auth);
	
	//public List<Dept> departmentList(Dept dept);
	
	//public void saveDepartment(Dept dept);
	
	//public List<Dept> departmentTreeList(Dept dept);
	
	//public List<DeptDBPrivilege> departmentDBAuthList(DeptDBPrivilege deptDBPrivilege);
	
	//public void saveDepartmentDBAuth(DeptDBPrivilege deptDBPrivilege);
	
	//public List<DeptDBPrivilege> departmentDBAuthHistoryList(DeptDBPrivilege deptDBPrivilege);
	
	public List<WrkJobCd> workJobTreeList(WrkJobCd wrkJobCd);	
	
	public List<Users> workJobUsersList(Users users);
	
	public String getMaxWrkJobCd(WrkJobCd wrkJobCd);
	
	public void saveWrkJobCd(WrkJobCd wrkJobCd);
	
	public void updateUserWrkJobLeader(UserWrkjob userWrkjob);
	
	public List<UserDBPrivilege> userDBAuthList(UserDBPrivilege userDBPrivilege);
	
	public void saveUserDBAuth(UserDBPrivilege userDBPrivilege);
	
	public List<UserDBPrivilege> userDBAuthHistoryList(UserDBPrivilege userDBPrivilege);
	
	public List<Users> performanceTunerList(Users users);
	
	public List<DatabaseTuner> performanceOfficerList(DatabaseTuner databaseTuner);
	
	public void savePerformanceOfficer(DatabaseTuner databaseTuner);
	
	public List<DatabaseTuner> performanceOfficerHistoryList(DatabaseTuner databaseTuner);

	public int saveAuthBundle(Auth auth);

	public int saveUserByExcelUpload(Users users);

	public int saveUserAuthByExcelUpload(Auth auth);

	public int saveUserWrkjobByExcelUpload(UserWrkjob userWrkjob);

	public int deleteUsersAuth(UserAuth userAuth);

	public int updateUsersWrkJobLeader(UserWrkjob userWrkjob);

	public int deleteUsersWrkJob(UserWrkjob userWrkjob);	

	public int deleteWrkJobCd(WrkJobCd wrkJobCd);

	public int saveUserApprove(Users users);

	public int deleteUserDbAuth(UserDBPrivilege userDBPrivilege);

	public int checkForSaveUserAuth(UserAuth userAuth);

	public void updateUserAuthForApprove(UserAuth auth);

	public List<LinkedHashMap<String, Object>> getUsersListByExcelDown(Users users);

	public List<LinkedHashMap<String, Object>> getAuthUserNameByExcelDown(Users users);

	public List<LinkedHashMap<String, Object>> workJobUsersListByExcelDown(Users users);

	public void updateUsersDefaultAuth(UserAuth userAuth);

	public void updateUsersDefaultWrkjobCd(UserWrkjob userWrkjob);

	public void insertUsersWrkJob(UserWrkjob userWrkjob);

	public void updateUsersWrkJob(UserWrkjob userWrkjob);

	public int checkUsersWrkjobCd(UserWrkjob userWrkjob);

	public int checkDefaultWrkjob(UserWrkjob userWrkjob);

	public int checkDefaultAuth(WrkJobCd wrkJobCd);

	public int checkDefaultAuth(UserAuth userAuth);

	public int cntkUserId(String user_id);

	public String getOmcWrkjobCdByWrkjobDivCd(String string);

	public int cntAuthId(String auth_id);

	public int cntWrkjobCd(String wrkjob_cd);

	public List<UserDBPrivilege> usersDbAuth(UserDBPrivilege userDBPrivilege);

	public int insertUserDBAuth2(UserDBPrivilege userDBPrivilege);

	public int updateUserDBAuth2(UserDBPrivilege userDBPrivilege);

	public int checkForUserDBAuth2(UserDBPrivilege userDBPrivilege);

	public int deleteUserDBAuth2(UserDBPrivilege userDBPrivilege);

	public int cntWorkJobLeader(UserWrkjob userWrkjob);

	public void deletePerformanceOfficer(String tuner_id);

	public List<String> getWrkjobCdList(String wrkjob_cd);

	public WrkJobCd checkWrkjobDivCd(String wrkjob_div_cd);

	public int resetUserPasswordBySHA256(Users users);

	public void updateUsers(Users users);

	public void insertUsers(Users users);

	public void insertUsersBySHA256(Users users);

	public int saveUserByExcelUploadBySHA256(Users users);

	public String[] getWrkjobCdIncludeChild(String wrkjob_cd);
	
	public int deletePreData(String[] wrkjob_cd);
	
	public int saveWrkJobDb(Map<String, Object> dbInfoMap);
}
