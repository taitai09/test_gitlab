package omc.spop.dao;

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
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.model.Menu;
import omc.spop.model.MyMenu;
import omc.spop.model.OdsHistSnapshot;
import omc.spop.model.OdsUsers;
import omc.spop.model.PlanCompareResult;
import omc.spop.model.Project;
import omc.spop.model.RecvNote;
import omc.spop.model.SQLStandards;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.UserAuth;
import omc.spop.model.UserTables;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;

/***********************************************************
 * 2017.08.10 이원식 최초작성
 **********************************************************/

public interface CommonDao {
	public Users login(String user_id);

	public Users login(Users paramUsers);

	public List<MyMenu> getUserAuthMenuList(MyMenu myMenu);

	public List<MyMenu> getMyMenuList(MyMenu myMenu);

	public List<Menu> getMenuList(Menu menu);

	public TuningTargetSql getWorkStatusCount(TuningTargetSql tuningTargetSql);

	public RecvNote getMessageCount(RecvNote recvNote);

	public List<GrpCd> commonCodeGroupList(GrpCd grpCd);

	public List<Cd> commonCodeList(Cd cd);
	
	public List<Cd> commonRef2CodeList(Cd cd);

	public List<Database> databaseList(Database database);

	public List<Instance> masterInstanceList(Instance instance);

	public List<WrkJobCd> wrkJobList(WrkJobCd wrkJobCd);

	public List<WrkJobCd> wrkJobDevList(WrkJobCd wrkJobCd);

	public List<OdsUsers> getUserName(OdsUsers odsUsers);

	public List<DatabaseTuner> getTuner(DatabaseTuner databaseTuner);

	public List<AccPathExec> getAccPathExec(AccPathExec accPathExec);

	public List<DbioLoadFile> getDBIOLoadFile(DbioLoadFile dbioLoadFile);

	public DbioLoadFile getDBIOLoadFileInfo(DbioLoadFile dbioLoadFile);

	public List<DbioExplainExec> getDBIOExplainExec(DbioExplainExec dbioExplainExec);

	public DbioExplainExec getDBIOExplainExecInfo(DbioExplainExec dbioExplainExec);

	public List<WrkJobCd> getWrkJobCd(WrkJobCd wrkJobCd);
	
	public List<WrkJobCd> getUserWrkJobCd(SQLStandards sqlStandards);

	public List<WrkJobCd> getUsersWrkJobCdList(WrkJobCd wrkJobCd);

	public UserWrkjob checkWorkJobLeader(UserWrkjob userWrkjob);

	public List<Users> getUsers(Users users);

	public List<Auth> getAuth(Auth auth);

	public List<OdsHistSnapshot> snapIdList(OdsHistSnapshot odsHistSnapshot);

	public Users getSimpleUserInfo(Users users);

	public Users getUserInfo(Users users);

	public int updateNewPwd(Users users);

	public Users checkUserId(Users users);

	public int saveNewUser(Users users);

	public int insertUserAuth(UserAuth userAuth);

	public int insertUserWrkJob(UserWrkjob userWrkjob);

	public int insertJobSchedulerConfigDetail(JobSchedulerConfigDetail jobSchedulerConfigDetail);

	public int updateJobSchedulerConfigDetail(JobSchedulerConfigDetail jobSchedulerConfigDetail);

	public List<OdsUsers> getTableName(OdsUsers odsUsers);

	public int getChildMenuCnt(MyMenu myMenu);

	public int insertMyMenuAction(MyMenu myMenu);

	public int deleteAllMyMenuAction(MyMenu myMenu);

	public int deleteMyMenuAction(MyMenu myMenu);

	public String getDbidByDbName(String db_name);

	/** 권한명 목록 */
	public List<Auth> getAuthNmList(Auth auth);

	public List<Auth> getUsersAuthList(Auth auth);

	/** 사용자명 목록 */
	public List<Users> getAuthUserName(Users users);

	public List<Map<String, String>> getAuthNmMapList(Auth auth);

	public List<UserTables> getTableList(UserTables userTables);

	public int updateUserDefaultRole(Users users);

	public List<Auth> getDefaultAuthGrpIdValidationList(Users user);

	public int insertMenu();

	public int insertMyMenu();

	public String getNextMenuId();

	public int checkWrkjobLeaderCnt(UserWrkjob userWrkjob);

	public List<Instance> instanceList(Instance instance);

	public List<Project> projectList(Project project);
	
	public List<Project> getDevProjectList(Map<String, String> param);

	public List<AccPathExec> getAccPathExecAddSec(AccPathExec accPathExec);

	public int getUserPasswordChgDt(Users users);

	public int saveNewUserBySHA256(Users users);

	public String getUserPasswdChgDt(Users users);

	public List<String> userPasswordValidCheck(String string);

	public void updateErrCnt(String user_id);

	public int passwordErrCnt(String user_id);

	public void updateResetErrCnt(String user_id);

	public List<String> getNoneFileList();

	public List<WrkJobCd> wrkJobTopLevel(WrkJobCd wrkJobCd);

	public List<String> sqlTextPlanList(PlanCompareResult planCompareResult);
	
	public List<String> sqlTextPlanOption(PlanCompareResult planCompareResult);

	public List<String> sqlTextPlanListAll(PlanCompareResult planCompareResult);
	
	public List<String> sqlTextPlanAllOption(PlanCompareResult planCompareResult);

	public List<String> loadAfterSelectTextPlanListAll(PlanCompareResult planCompareResult);

	public List<String> loadAfterDMLTextPlanListAll(PlanCompareResult planCompareResult);

	public List<String> loadAfterSelectTextPlanListOption(PlanCompareResult planCompareResult);

	public List<String> loadAfterDMLTextPlanListOption(PlanCompareResult planCompareResult);

	public List<String> sqlTextPlanListNew(PlanCompareResult planCompareResult);

	public List<String> sqlTextPlanOptionNew(PlanCompareResult planCompareResult);
	
	public List<String> loadAfterSelectTextPlanListNew(PlanCompareResult planCompareResult);

	public List<String> loadAfterSelectTextPlanListOptionNew(PlanCompareResult planCompareResult);

	public List<String> loadAfterDMLTextPlanListNew(PlanCompareResult planCompareResult);
	
	public List<String> loadAfterDMLTextPlanListOptionNew(PlanCompareResult planCompareResult);
}
