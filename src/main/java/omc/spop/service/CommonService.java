package omc.spop.service;

import java.util.List;
import java.util.Map;

import omc.spop.model.AccPathExec;
import omc.spop.model.Auth;
import omc.spop.model.Cd;
import omc.spop.model.Database;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.DbioExplainExec;
import omc.spop.model.DbioLoadFile;
import omc.spop.model.GrpCd;
import omc.spop.model.Instance;
import omc.spop.model.Menu;
import omc.spop.model.MyMenu;
import omc.spop.model.OdsHistSnapshot;
import omc.spop.model.OdsUsers;
import omc.spop.model.PlanCompareResult;
import omc.spop.model.Project;
import omc.spop.model.RecvNote;
import omc.spop.model.Result;
import omc.spop.model.SQLStandards;
import omc.spop.model.SqlStatByPlan;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.UserTables;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;

public interface CommonService {
	/** My권한메뉴 리스트 조회 */
	List<MyMenu> getUserAuthMenuList(MyMenu myMenu) throws Exception;

	/** My메뉴 리스트 조회 */
	List<MyMenu> getMyMenuList(MyMenu myMenu) throws Exception;

	/** 메뉴 리스트 조회 */
	List<Menu> getMenuList(Menu menu) throws Exception;

	/** 상단 작업현황 조회 */
	TuningTargetSql getWorkStatusCount(TuningTargetSql tuningTargetSql) throws Exception;

	/** 상단 쪽지카운트 조회 */
	RecvNote getMessageCount(RecvNote recvNote) throws Exception;

	/** 코드그룹 리스트 */
	List<GrpCd> commonCodeGroupList(GrpCd grpCd) throws Exception;

	/** 공통코드 리스트 */
	List<Cd> commonCodeList(Cd cd) throws Exception;
	
	/** 공통코드(REF_VL_2) 리스트 */
	List<Cd> commonRef2CodeList(Cd cd) throws Exception;

	/** 데이터베이스 리스트 */
	List<Database> databaseList(Database database) throws Exception;

	/** 마스터 인스턴스 리스트 */
	List<Instance> masterInstanceList(Instance instance) throws Exception;

	/** 에이젼트 인스턴스 리스트 */
	List<Instance> agentInstanceList(Instance instance) throws Exception;

	/** WORK JOB 리스트 */
	List<WrkJobCd> wrkJobList(WrkJobCd wrkJobCd) throws Exception;

	/** WORK JOB - DEV권한 리스트 */
	List<WrkJobCd> wrkJobDevList(WrkJobCd wrkJobCd) throws Exception;

	/** ACCESS PATH 사용자 정보[Combo] 리스트 */
	List<OdsUsers> getUserName(OdsUsers odsUsers) throws Exception;

	/** 사용자명 목록 */
	List<Users> getAuthUserName(Users users) throws Exception;

	/** 튜닝담당자 리스트 */
	List<DatabaseTuner> getTuner(DatabaseTuner databaseTuner) throws Exception;

	/** ACCESS PATH ACC_PATH_EXEC[Combo] 리스트 */
	List<AccPathExec> getAccPathExec(AccPathExec accPathExec) throws Exception;

	/** DBIO LOAD File INFO [Combo] 리스트 */
	List<DbioLoadFile> getDBIOLoadFile(DbioLoadFile dbioLoadFile) throws Exception;

	/** DBIO LOAD File INFO [Combo] 정보 조회 */
	DbioLoadFile getDBIOLoadFileInfo(DbioLoadFile dbioLoadFile) throws Exception;

	/** DBIO 수행회차 [Combo] 리스트 */
	List<DbioExplainExec> getDBIOExplainExec(DbioExplainExec dbioExplainExec) throws Exception;

	/** DBIO 수행회차 [Combo] 상세정보 조회 */
	DbioExplainExec getDBIOExplainExecInfo(DbioExplainExec dbioExplainExec) throws Exception;

	/** 업무 조회 Tree 리스트 */
	List<WrkJobCd> getWrkJobCd(WrkJobCd wrkJobCd) throws Exception;
	
	/** 업무 조회 (개발자 권한 + 프로젝트 별) */
	List<WrkJobCd> getUserWrkJobCd(SQLStandards sqlStandards) throws Exception;

	List<WrkJobCd> getUsersWrkJobCdList(WrkJobCd wrkJobCd) throws Exception;

	/** 업무리더 존재여부 체크 */
	UserWrkjob checkWorkJobLeader(UserWrkjob userWrkjob) throws Exception;

	/** 사용자 리스트 */
	List<Users> getUsers(Users users) throws Exception;

	/** 권한 리스트 */
	List<Auth> getAuth(Auth auth) throws Exception;

	/** SNAP ID 조회 팝업 */
	List<OdsHistSnapshot> snapIdList(OdsHistSnapshot odsHistSnapshot) throws Exception;

	/** SQL PROFILE 적용 */
	void sqlProfileApply(SqlStatByPlan sqlStatByPlan) throws Exception;

	/** 사용자 정보 조회 */
	Users getSimpleUserInfo(Users users) throws Exception;

	/** 사용자 정보 조회 */
	Users getUserInfo(Users users) throws Exception;

	/** 최초 로그인으로 인한 비밀번호 변경 */
	int updateNewPwd(Users users) throws Exception;

	/** 사용자 ID 중복 체크 */
	Users checkUserId(Users users) throws Exception;

	/** 신규 사용자 등록 */
	int saveNewUser(Users users) throws Exception;

	/** 테이블명 조회 */
	List<OdsUsers> getTableName(OdsUsers odsUsers) throws Exception;

	int getChildMenuCnt(MyMenu myMenu) throws Exception;;

	int insertMyMenuAction(MyMenu myMenu) throws Exception;;

	int deleteAllMyMenuAction(MyMenu myMenu) throws Exception;;

	int deleteMyMenuAction(MyMenu myMenu) throws Exception;;

	String getDbidByDbName(String db_name) throws Exception;;

	List<Auth> getAuthNmList(Auth auth) throws Exception;;

	List<Auth> getUsersAuthList(Auth auth) throws Exception;

	List<Map<String, String>> getAuthNmMapList(Auth auth) throws Exception;

	List<UserTables> getTableList(UserTables userTables) throws Exception;

	int updateUserDefaultRole(Users users) throws Exception;

	List<Auth> getDefaultAuthGrpIdValidationList(Users user) throws Exception;

	int checkWrkjobLeaderCnt(UserWrkjob userWrkjob) throws Exception;

	List<Instance> instanceList(Instance instance);

	/** 프로젝트 리스트 */
	List<Project> projectList(Project project) throws Exception;

	/** 프로젝트 리스트 (개발자 로그인 시) */
	List<Project> getDevProjectList() throws Exception;
	
	List<AccPathExec> getAccPathExecAddSec(AccPathExec accPathExec);
	
    /**
     * 유저 패스워드 단방향 알고리즘 SHA256 적용해야하는지 여부를 리턴하는 함수 
     * @return
     */	
	boolean isToApplySHA256(Users users);

	String getUserPasswdChgDt(Users users);

	boolean userPasswordValidCheck(Users users) throws Exception;

	void updateErrCnt(String user_id);

	int passwordErrCnt(String user_id);

	void updateResetErrCnt(String user_id);

	List<String> getNoneFileList();

//	String getUserSaltValue(String user_id);

	/* WORK JOB 전체 리스트 
	 * 기준이 되는 WORK JOB 리스트에서 조건식(WHERE PREF_ID = '22001')을 제외하여 시스템 유형에 관계없이 가져오도록 한다.
	 */
	List<WrkJobCd> wrkJobTopLevel(WrkJobCd wrkJobCd) throws Exception;

	List<PlanCompareResult> getPlanCompareResult(
			PlanCompareResult planCompareResult) throws Exception;

	List<PlanCompareResult> getAsisPlanCompareResult(
			PlanCompareResult planCompareResult ) throws Exception;
}
