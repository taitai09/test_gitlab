package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import omc.spop.model.Auth;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.Result;
import omc.spop.model.Result;
import omc.spop.model.Result;
import omc.spop.model.UserAuth;
import omc.spop.model.UserDBPrivilege;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;

/***********************************************************
 * 2017.11.09	이원식	최초작성
 **********************************************************/

public interface UserMngService {
	/** 사용자 관리 - 사용자 관리 리스트 */
	List<Users> usersList(Users users) throws Exception;
	
	/** 사용자 관리 - 사용자 ID 중복 체크 */
	Users checkUserId(Users users) throws Exception;
	
	/** 사용자 관리 - 사용자 관리 저장 */
	void saveUsers(Users users) throws Exception;
	
	/** 사용자 관리 - 사용자 관리 - 승인 */
	void approveUser(Users users) throws Exception;
	
	/** 사용자 관리 - 사용자 관리 - 비밀번호 초기화 */
	void resetUserPassword(Users users) throws Exception;	
	
	/** 사용자 관리 - 사용자 권한 리스트 */
	List<UserAuth> usersAuthList(UserAuth userAuth) throws Exception;
	
	/** 사용자 관리 - 사용자 권한 저장 */
	void saveUsersAuth(UserAuth userAuth) throws Exception;	
	
	/** 사용자 관리 - 사용자 업무 리스트 */
	List<UserWrkjob> usersWrkJobList(UserWrkjob userWrkjob) throws Exception;
	
	/** 사용자 관리 - 사용자 업무 저장 */
	void saveUsersWrkJob(UserWrkjob userWrkjob) throws Exception;
	
	/** 사용자 관리 - 업무리더 존재여부  */
	UserWrkjob checkWorkJobLeader(UserWrkjob userWrkjob) throws Exception;
	
	/** 사용자 관리 - 사용자 부서 리스트 */
	//List<UserDept> usersDeptList(UserDept userDept) throws Exception;
	
	/** 사용자 관리 - 사용자 부서 저장 */
	//void saveUsersDept(UserDept userDept) throws Exception;	
	
	/** 사용자 관리 - 권한 관리 리스트 */
	List<Auth> authList(Auth auth) throws Exception;	
	
	/** 사용자 관리 - 권한 관리 저장 */
	void saveAuth(Auth auth) throws Exception;
	
	/** 사용자 관리 - 부서 관리 리스트 */
	//List<Dept> departmentList(Dept dept) throws Exception;
	
	/** 사용자 관리 - 부서 관리 저장 */
	//void saveDepartment(Dept dept) throws Exception;
	
	/** 사용자 관리 - 부서 TreeGrid 리스트 */
	//List<Dept> departmentTreeList(Dept dept) throws Exception;	
	
	/** 사용자 관리 - 부서 DB권한 관리 리스트 */
	//List<DeptDBPrivilege> departmentDBAuthList(DeptDBPrivilege deptDBPrivilege) throws Exception;
	
	/** 사용자 관리 - 부서  DB권한 관리 저장 */
	//void saveDepartmentDBAuth(HttpServletRequest req) throws Exception;
	
	/** 사용자 관리 - 부서 DB권한 관리 이력 리스트 */
	//List<DeptDBPrivilege> departmentDBAuthHistoryList(DeptDBPrivilege deptDBPrivilege) throws Exception;
	
	/** 사용자 관리 - 업무관리 TreeGrid 리스트 */
	List<WrkJobCd> workJobTreeList(WrkJobCd wrkJobCd) throws Exception;
	
	/** 사용자 관리 - 업무관리 - 업무별 사용자 리스트 */
	List<Users> workJobUsersList(Users users) throws Exception;
	
	/** 사용자 관리 - 업무 관리 저장 */
	void saveWorkJob(WrkJobCd wrkJobCd, String[] dbInfoArr) throws Exception;
	
	/** 사용자 관리 - 업무리더 저장 */
	void saveWorkJobLeader(UserWrkjob userWrkjob) throws Exception;
	
	/** 사용자 관리 - 사용자 DB권한 관리 리스트 */
	List<UserDBPrivilege> userDBAuthList(UserDBPrivilege userDBPrivilege) throws Exception;
	
	/** 사용자 관리 - 사용자  DB권한 관리 저장 */
	void saveUserDBAuth(HttpServletRequest req) throws Exception;
	
	/** 사용자 관리 - 사용자 DB권한 관리 이력 리스트 */
	List<UserDBPrivilege> userDBAuthHistoryList(UserDBPrivilege userDBPrivilege) throws Exception;	
	
	/** 사용자 관리 - 튜닝담당자 튜너(tuner) 리스트 */
	List<Users> performanceTunerList(Users users) throws Exception;
	
	/** 사용자 관리 - 튜닝담당자 관리 리스트 */
	List<DatabaseTuner> performanceOfficerList(DatabaseTuner databaseTuner) throws Exception;
	
	/** 사용자 관리 - 튜닝담당자관리 저장 */
	void savePerformanceOfficer(HttpServletRequest req) throws Exception;
	
	/** 사용자 관리 - 튜닝담당자관리 이력 리스트 */
	List<DatabaseTuner> performanceOfficerHistoryList(DatabaseTuner databaseTuner) throws Exception;

	int saveAuthBundle(Auth auth) throws Exception;

	Result uploadUserExcelFile(MultipartFile file) throws Exception;

	int deleteUsersAuth(UserAuth userAuth) throws Exception;

	int deleteUsersWrkJob(UserWrkjob userWrkjob) throws Exception;	

	int deleteWrkJobCd(WrkJobCd wrkJobCd) throws Exception;

	int saveUserApprove(Users users);

	List<LinkedHashMap<String, Object>> getUsersListByExcelDown(Users users);

	List<LinkedHashMap<String, Object>> getAuthUserNameByExcelDown(Users users);

	List<LinkedHashMap<String, Object>> workJobUsersListByExcelDown(Users users);

	int saveUserDBAuth2(UserDBPrivilege userDBPrivilege) throws Exception;

	List<UserDBPrivilege> usersDbAuth(UserDBPrivilege userDBPrivilege);

	int deleteUserDBAuth2(UserDBPrivilege userDBPrivilege);

	void updateUsers(Users users) throws Exception;

	void insertUsers(Users users) throws Exception;
}
