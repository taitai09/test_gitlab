package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import difflib.DiffRow;
import difflib.DiffRow.Tag;
import difflib.DiffRowGenerator;
import omc.spop.base.Config;
import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
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
import omc.spop.model.SQLStandards;
import omc.spop.model.SqlStatByPlan;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.UserAuth;
import omc.spop.model.UserTables;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;
import omc.spop.server.perfmon.PerfMonSess;
import omc.spop.server.tune.SqlProfileAwr;
import omc.spop.service.CommonService;
import omc.spop.service.WatchLogService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

@Service("CommonService")
public class CommonServiceImpl implements CommonService {

	private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private WatchLogService watchLogService;

	@Override
	public List<MyMenu> getUserAuthMenuList(MyMenu myMenu) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		myMenu.setUser_id(user_id);
		return commonDao.getUserAuthMenuList(myMenu);
	}

	@Override
	public List<MyMenu> getMyMenuList(MyMenu myMenu) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		myMenu.setUser_id(user_id);
		return commonDao.getMyMenuList(myMenu);
	}

	@Override
	public List<Menu> getMenuList(Menu menu) throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();

		menu.setAuth_cd(auth_cd);
		return commonDao.getMenuList(menu);
	}

	@Override
	public TuningTargetSql getWorkStatusCount(TuningTargetSql tuningTargetSql) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
		String leader_yn = SessionManager.getLoginSession().getUsers().getLeader_yn();

		if (auth_cd.equals("ROLE_TUNER")) {
			tuningTargetSql.setPerfr_id(user_id);
		} else if (auth_cd.equals("ROLE_DEV")) {
			tuningTargetSql.setLeader_yn(leader_yn);
			tuningTargetSql.setWrkjob_cd(wrkjob_cd);
			tuningTargetSql.setTuning_requester_id(user_id);
			tuningTargetSql.setWrkjob_mgr_id(user_id);
		}

		return commonDao.getWorkStatusCount(tuningTargetSql);
	}

	@Override
	public RecvNote getMessageCount(RecvNote recvNote) throws Exception {
		return commonDao.getMessageCount(recvNote);
	}

	@Override
	public List<GrpCd> commonCodeGroupList(GrpCd grpCd) throws Exception {
		return commonDao.commonCodeGroupList(grpCd);
	}

	@Override
	public List<Cd> commonCodeList(Cd cd) throws Exception {
		return commonDao.commonCodeList(cd);
	}
	
	@Override
	public List<Cd> commonRef2CodeList(Cd cd) throws Exception {
		return commonDao.commonRef2CodeList(cd);
	}

	@Override
	public List<Database> databaseList(Database database) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		database.setUser_id(user_id);

		return commonDao.databaseList(database);
	}

	@Override
	public List<Instance> masterInstanceList(Instance instance) throws Exception {
		return commonDao.masterInstanceList(instance);
	}

	@Override
	public List<Instance> agentInstanceList(Instance instance) throws Exception {
		List<Instance> resultList = new ArrayList<Instance>();

		try {
			// DBID, Background 제외 여부, auto refresh(10 sec)여부, status(All,
			// Active ..), :inst_id, :sql_id :program :module :event :machine
			// :osuser
			// PerfMonSess.setParam(Long.parseLong(instance.getDbid()), "NO",
			// "NO", "ALL", 1, null, null, null, null, null, null);

			// List<String> rowData =
			// StringUtil.stringLineRead(StringUtil.nvl(PerfMonSess.SEARCH_INST_ID()));
			List<String> rowData = StringUtil
					.stringLineRead(StringUtil.nvl(PerfMonSess.SEARCH_INST_ID(Long.parseLong(instance.getDbid()), "NO",
							"NO", "ALL", 1, null, null, null, null, null, null)));

			if (rowData.size() > 0) {
				for (int i = 0; i < rowData.size(); i++) {
					Instance result = new Instance();
					String temp = rowData.get(i);
					String[] tempArry = temp.split("\\&#");

					result.setInst_id(StringUtil.replace(tempArry[0], "null", ""));
					result.setInst_name(StringUtil.replace(tempArry[1], "null", ""));

					resultList.add(result);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<WrkJobCd> wrkJobList(WrkJobCd wrkJobCd) throws Exception {
		return commonDao.wrkJobList(wrkJobCd);
	}

	@Override
	public List<WrkJobCd> wrkJobDevList(WrkJobCd wrkJobCd) throws Exception {
		return commonDao.wrkJobDevList(wrkJobCd);
	}

	@Override
	public List<OdsUsers> getUserName(OdsUsers odsUsers) throws Exception {
		return commonDao.getUserName(odsUsers);
	}

	/** 사용자명 목록 */
	@Override
	public List<Users> getAuthUserName(Users users) throws Exception {
		return commonDao.getAuthUserName(users);
	}

	@Override
	public List<DatabaseTuner> getTuner(DatabaseTuner databaseTuner) throws Exception {
		return commonDao.getTuner(databaseTuner);
	}

	@Override
	public List<AccPathExec> getAccPathExec(AccPathExec accPathExec) throws Exception {
		return commonDao.getAccPathExec(accPathExec);
	}

	@Override
	public List<DbioLoadFile> getDBIOLoadFile(DbioLoadFile dbioLoadFile) throws Exception {
		return commonDao.getDBIOLoadFile(dbioLoadFile);
	}

	@Override
	public DbioLoadFile getDBIOLoadFileInfo(DbioLoadFile dbioLoadFile) throws Exception {
		return commonDao.getDBIOLoadFileInfo(dbioLoadFile);
	}

	@Override
	public List<DbioExplainExec> getDBIOExplainExec(DbioExplainExec dbioExplainExec) throws Exception {
		return commonDao.getDBIOExplainExec(dbioExplainExec);
	}

	@Override
	public DbioExplainExec getDBIOExplainExecInfo(DbioExplainExec dbioExplainExec) throws Exception {
		return commonDao.getDBIOExplainExecInfo(dbioExplainExec);
	}

	@Override
	public List<WrkJobCd> getWrkJobCd(WrkJobCd wrkJobCd) throws Exception {
		return commonDao.getWrkJobCd(wrkJobCd);
	}
	
	@Override
	public List<WrkJobCd> getUserWrkJobCd(SQLStandards sqlStandards) throws Exception {
		return commonDao.getUserWrkJobCd(sqlStandards);
	}

	@Override
	public UserWrkjob checkWorkJobLeader(UserWrkjob userWrkjob) throws Exception {
		return commonDao.checkWorkJobLeader(userWrkjob);
	}
	
	@Override
	public int checkWrkjobLeaderCnt(UserWrkjob userWrkjob) {
		return commonDao.checkWrkjobLeaderCnt(userWrkjob);
	}
	
	@Override
	public List<Users> getUsers(Users users) throws Exception {
		return commonDao.getUsers(users);
	}

	@Override
	public List<Auth> getAuth(Auth auth) throws Exception {
		return commonDao.getAuth(auth);
	}

	@Override
	public List<OdsHistSnapshot> snapIdList(OdsHistSnapshot odsHistSnapshot) throws Exception {
		return commonDao.snapIdList(odsHistSnapshot);
	}

	@Override
	public void sqlProfileApply(SqlStatByPlan sqlStatByPlan) throws Exception {
		try {
			SqlProfileAwr.startProfile(StringUtil.parseLong(sqlStatByPlan.getDbid(), 0), sqlStatByPlan.getSql_id(),
					StringUtil.parseLong(sqlStatByPlan.getPlan_hash_value(), 0), sqlStatByPlan.getSql_profile());
		} catch (Exception ex) {
			logger.error("SERVER ERROR(startProfile) ==> " + ex.getMessage());
			throw ex;
		}
	}

	@Override
	public Users getSimpleUserInfo(Users users) throws Exception {
		return commonDao.getSimpleUserInfo(users);
	}
	@Override
	public Users getUserInfo(Users users) throws Exception {
		return commonDao.getUserInfo(users);
	}

	@Override
	public int updateNewPwd(Users users) throws Exception {
		int check = 0;
		
		try {
			check = commonDao.updateNewPwd(users);
			watchLogService.WatchUpdateUsersPassword();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return check;
	}

	@Override
	public Users checkUserId(Users users) throws Exception {
		return commonDao.checkUserId(users);
	}

	@Override
	public int saveNewUser(Users users) throws Exception {
		int insertRow = 0;
		UserAuth auth = new UserAuth();
		UserWrkjob wrkJob = new UserWrkjob();

		// 1. 기본정보 저장 (USERS INSERT)
		if(!StringUtils.defaultString(users.getSalt_value()).equals("")){  //SALT_VALUE 가 있다는건 단방향 알고리즘 적용대상
			insertRow = commonDao.saveNewUserBySHA256(users);
		}else{
			insertRow = commonDao.saveNewUser(users);
			
		}

		
		// 2. 권한 저장 (USER_AUTH INSERT)
		auth.setUser_id(users.getUser_id());
		auth.setAuth_grp_id(users.getAuth_cd());
		auth.setAuth_start_day(DateUtil.getNowDate("yyyyMMdd"));

		commonDao.insertUserAuth(auth);

		// 3. 업무 저장 (USER_WRKJOB INSERT) => 개발자(ROLE_DEV)만 업무 저장.
		if (users.getWrkjob_cd() != null && !users.getWrkjob_cd().equals("")) {
			wrkJob.setUser_id(users.getUser_id());
			wrkJob.setWrkjob_cd(users.getWrkjob_cd());
			wrkJob.setWorkjob_start_day(DateUtil.getNowDate("yyyyMMdd"));
			wrkJob.setLeader_yn(users.getLeader_yn());

			commonDao.insertUserWrkJob(wrkJob);
		}

		return insertRow;
	}

	@Override
	public List<OdsUsers> getTableName(OdsUsers odsUsers) throws Exception {
		return commonDao.getTableName(odsUsers);
	}

	@Override
	public int getChildMenuCnt(MyMenu myMenu) throws Exception {
		return commonDao.getChildMenuCnt(myMenu);
	}

	@Override
	public int insertMyMenuAction(MyMenu myMenu) throws Exception {
		return commonDao.insertMyMenuAction(myMenu);
	}

	@Override
	public int deleteAllMyMenuAction(MyMenu myMenu) throws Exception {
		return commonDao.deleteAllMyMenuAction(myMenu);
	}

	@Override
	public int deleteMyMenuAction(MyMenu myMenu) throws Exception {
		return commonDao.deleteMyMenuAction(myMenu);
	}

	@Override
	public String getDbidByDbName(String db_name) throws Exception {
		return commonDao.getDbidByDbName(db_name);
	}

	@Override
	public List<Auth> getAuthNmList(Auth auth) throws Exception {
		return commonDao.getAuthNmList(auth);
	}

	@Override
	public List<Auth> getUsersAuthList(Auth auth) throws Exception {
		return commonDao.getUsersAuthList(auth);
	}

	@Override
	public List<Map<String, String>> getAuthNmMapList(Auth auth) {
		return commonDao.getAuthNmMapList(auth);
	}

	@Override
	public List<UserTables> getTableList(UserTables userTables) throws Exception {
		return commonDao.getTableList(userTables);
	}

	@Override
	public int updateUserDefaultRole(Users users) throws Exception {
		return commonDao.updateUserDefaultRole(users);
	}

	@Override
	public List<WrkJobCd> getUsersWrkJobCdList(WrkJobCd wrkJobCd) throws Exception {
		return commonDao.getUsersWrkJobCdList(wrkJobCd);
	}

	@Override
	public List<Auth> getDefaultAuthGrpIdValidationList(Users user) throws Exception {
		return commonDao.getDefaultAuthGrpIdValidationList(user);
	}

	@Override
	public List<Instance> instanceList(Instance instance) {
		return commonDao.instanceList(instance);
	}

	@Override
	public List<Project> projectList(Project project) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		return commonDao.projectList(project);
	}
	
	@Override
	public List<Project> getDevProjectList() throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("user_id", SessionManager.getLoginSession().getUsers().getUser_id());
		
		return commonDao.getDevProjectList(param);
	}

	@Override
	public List<AccPathExec> getAccPathExecAddSec(AccPathExec accPathExec) {
		return commonDao.getAccPathExecAddSec(accPathExec);
	}

	@Override
	public boolean isToApplySHA256(Users users) {
	   	String updated_dt = StringUtils.defaultString(Config.getString("256_updated_dt"),"0");
	   	users.setApplied_dt(updated_dt);
	   	int passwd_chg_dt = commonDao.getUserPasswordChgDt(users);
		return passwd_chg_dt > Integer.parseInt(updated_dt) ? true : false;
	}

	@Override
	public String getUserPasswdChgDt(Users users) {
		return commonDao.getUserPasswdChgDt(users);
	}

	//비밀번호 validation 체크해주는 로직
	@Override
	public boolean userPasswordValidCheck(Users users) throws Exception {
		logger.debug("############ PASSWD VALIDATION CHECK IN ############");
		//db에서 유저 패스워드 특수문자 확인하여 어떤것이 사용되면 안되는지 result.message 로 반환
		List<String> checkList = new ArrayList<String>();
		checkList = commonDao.userPasswordValidCheck(users.getPassword());
		String valid_list = "";
		int check = 0;
		
		if(!StringUtils.defaultString(users.getNew_password()).equals("")){
			users.setPassword(users.getNew_password());
		}
		for(int i = 0; i < checkList.size(); i++){
			if(users.getPassword().indexOf(checkList.get(i)) != -1){
				check += 1;
			}
			valid_list += checkList.get(i) + " ";
		}
		if(check > 0)
			throw new Exception("비밀번호에 특수문자 [ " +valid_list+"]는 사용할 수 없습니다.");
		
		return true;
	}

	@Override
	public void updateErrCnt(String user_id) {
		commonDao.updateErrCnt(user_id);
	}

	@Override
	public int passwordErrCnt(String user_id) {
		return commonDao.passwordErrCnt(user_id);
	}

	@Override
	public void updateResetErrCnt(String user_id) {
		commonDao.updateResetErrCnt(user_id);
	}

	@Override
	public List<String> getNoneFileList() {
		return commonDao.getNoneFileList();
	}

	@Override
	public List<WrkJobCd> wrkJobTopLevel(WrkJobCd wrkJobCd) throws Exception {
		return commonDao.wrkJobTopLevel(wrkJobCd);
	}
	
	@Override
	public List<PlanCompareResult> getPlanCompareResult(PlanCompareResult planCompareResult) throws Exception {
		
		List<PlanCompareResult> compareList = new ArrayList<PlanCompareResult>();
		
		List<String> original = Collections.emptyList();
		List<String> revised = Collections.emptyList();
		
		List<String> originalOption = Collections.emptyList();
		List<String> revisedOption = Collections.emptyList();
		
		if( "1".equals(planCompareResult.getPerf_check_sql_source_type_cd()) ) {
			original = commonDao.sqlTextPlanList(planCompareResult);
			originalOption = commonDao.sqlTextPlanOption(planCompareResult);
			
		}else {
			original = commonDao.sqlTextPlanListAll(planCompareResult);
			originalOption = commonDao.sqlTextPlanAllOption(planCompareResult);
		}
		logger.debug("PlanHashValue ======================>"+planCompareResult.getPlan_hash_value() + " , " + planCompareResult.getTobe_executions());
		if ( planCompareResult.getTobe_plan_hash_value() != null && "".equals(planCompareResult.getTobe_plan_hash_value()) == false && 
				(planCompareResult.getTobe_executions() == null || "".equals( planCompareResult.getTobe_executions() ) ) ) {
					revised = commonDao.loadAfterDMLTextPlanListNew(planCompareResult);
					revisedOption = commonDao.loadAfterDMLTextPlanListOptionNew(planCompareResult);
		} else if ( planCompareResult.getTobe_plan_hash_value() != null && "".equals(planCompareResult.getTobe_plan_hash_value()) == false &&
				planCompareResult.getTobe_executions() != null && "".equals( planCompareResult.getTobe_executions() ) == false ) {
					revised = commonDao.loadAfterSelectTextPlanListNew(planCompareResult);
					revisedOption = commonDao.loadAfterSelectTextPlanListOptionNew(planCompareResult);
		}
		
//		if( "SELECT".equals(planCompareResult.getSql_command_type_cd()) ) {
//			revised = commonDao.loadAfterSelectTextPlanListAll(planCompareResult);
//			revisedOption = commonDao.loadAfterSelectTextPlanListOption(planCompareResult);
//			
//		}else {
//			revised = commonDao.loadAfterDMLTextPlanListAll(planCompareResult);
//			revisedOption = commonDao.loadAfterDMLTextPlanListOption(planCompareResult);
//		}
		
		DiffRowGenerator generator = new DiffRowGenerator.Builder()
									.showInlineDiffs(true)
									.ignoreWhiteSpaces(true)
									.ignoreBlankLines(true)
									.build();
		
		List<DiffRow> rows = generator.generateDiffRows(original, revised);
		
		int oldNum = 0;
		int newNum = 0;
		logger.debug("|type--|type(web)|original-|new------|");

		for (int i = 0; i<rows.size(); i++) {
			planCompareResult = new PlanCompareResult();
			
			planCompareResult.setTag( rows.get(i).getTag().toString() );
			
			if ( "EQUAL".equals(rows.get(i).getTag().toString()) == true ) {
				planCompareResult.setOriginLine( original.get(oldNum)+originalOption.get(oldNum) );
				oldNum++;
				
				planCompareResult.setNewLine( revised.get(newNum)+revisedOption.get(newNum) );
				newNum++;
				
			}else if( "INSERT".equals(rows.get(i).getTag().toString()) == true ){
				// insert인경우 (text가 newLine에만 있는 경우)
				planCompareResult.setOriginLine("");
				planCompareResult.setNewLine( revised.get(newNum)+revisedOption.get(newNum) );
				
				newNum++;
				
			}else if( "DELETE".equals(rows.get(i).getTag().toString()) == true ){
				// delete인 경우 (text가 orininLine에만 있는 경우)
				planCompareResult.setOriginLine( original.get(oldNum)+originalOption.get(oldNum) );
				planCompareResult.setNewLine("");
				
				oldNum++;
				
			}else {
				// change인 경우 insert와 delete를 다시한번 구분하여 세팅
				if( "".equals(rows.get(i).getOldLine()) == false ) {
					planCompareResult.setOriginLine( original.get(oldNum)+originalOption.get(oldNum) );
					oldNum++;
					
				}else {
					planCompareResult.setOriginLine("");
					planCompareResult.setTag(Tag.INSERT.toString());
				}
				
				if( "".equals(rows.get(i).getNewLine()) == false ) {
					planCompareResult.setNewLine( revised.get(newNum)+revisedOption.get(newNum) );
					newNum++;
					
				}else {
					planCompareResult.setNewLine("");
					planCompareResult.setTag(Tag.DELETE.toString());
				}
			}
			compareList.add(planCompareResult);
			
			logger.debug("|" +rows.get(i).getTag()+"|"+compareList.get(i).getTag()+"|" + compareList.get(i).getOriginLine() + "|" + compareList.get(i).getNewLine() + "|");
		}
		// return 하지 않는 리스트 데이터 비움
		original = null;
		revised = null;
		originalOption = null;
		revisedOption = null;
		rows = null;
		
		return compareList;
	}
	
	@Override
	public List<PlanCompareResult> getAsisPlanCompareResult( PlanCompareResult planCompareResult ) throws Exception {
		
		List<PlanCompareResult> compareList = new ArrayList<PlanCompareResult>();
		
		List<String> original = Collections.emptyList();
		List<String> revised = Collections.emptyList();
		
		List<String> originalOption = Collections.emptyList();
		List<String> revisedOption = Collections.emptyList();
		logger.debug( "asis : "+ planCompareResult.getAsis_plan_hash_value() + " , original : "+planCompareResult.getPlan_hash_value() +
				" , Type(AWR:1,전체SQL:2) : "+planCompareResult.getPerf_check_sql_source_type_cd() );
		
		revised = commonDao.sqlTextPlanListNew(planCompareResult);
		revisedOption = commonDao.sqlTextPlanOptionNew(planCompareResult);
		
		planCompareResult.setPlan_hash_value( planCompareResult.getAsis_plan_hash_value() );
		
		// AWR
		if ( "1".equals(planCompareResult.getPerf_check_sql_source_type_cd()) ) {
			original = commonDao.sqlTextPlanList(planCompareResult);
			originalOption = commonDao.sqlTextPlanOption(planCompareResult);
		} else { //전체SQL
			original = commonDao.sqlTextPlanListAll(planCompareResult);
			originalOption = commonDao.sqlTextPlanAllOption(planCompareResult);
		}
		
//		if( "SELECT".equals(planCompareResult.getSql_command_type_cd()) ) {
//			revised = commonDao.loadAfterSelectTextPlanListAll(planCompareResult);
//			revisedOption = commonDao.loadAfterSelectTextPlanListOption(planCompareResult);
//			
//		}else {
//			revised = commonDao.loadAfterDMLTextPlanListAll(planCompareResult);
//			revisedOption = commonDao.loadAfterDMLTextPlanListOption(planCompareResult);
//		}
		DiffRowGenerator generator = new DiffRowGenerator.Builder()
				.showInlineDiffs(true)
				.ignoreWhiteSpaces(true)
				.ignoreBlankLines(true)
				.build();
		
		List<DiffRow> rows = generator.generateDiffRows(original, revised);
		
		int oldNum = 0;
		int newNum = 0;
		logger.debug("|type--|type(web)|original-|new------|");
		
		for (int i = 0; i<rows.size(); i++) {
			planCompareResult = new PlanCompareResult();
			
			planCompareResult.setTag( rows.get(i).getTag().toString() );
			
			if ( "EQUAL".equals(rows.get(i).getTag().toString()) == true ) {
				planCompareResult.setOriginLine( original.get(oldNum)+originalOption.get(oldNum) );
				oldNum++;
				
				planCompareResult.setNewLine( revised.get(newNum)+revisedOption.get(newNum) );
				newNum++;
				
			}else if( "INSERT".equals(rows.get(i).getTag().toString()) == true ){
				// insert인경우 (text가 newLine에만 있는 경우)
				planCompareResult.setOriginLine("");
				planCompareResult.setNewLine( revised.get(newNum)+revisedOption.get(newNum) );
				
				newNum++;
				
			}else if( "DELETE".equals(rows.get(i).getTag().toString()) == true ){
				// delete인 경우 (text가 orininLine에만 있는 경우)
				planCompareResult.setOriginLine( original.get(oldNum)+originalOption.get(oldNum) );
				planCompareResult.setNewLine("");
				
				oldNum++;
				
			}else {
				// change인 경우 insert와 delete를 다시한번 구분하여 세팅
				if( "".equals(rows.get(i).getOldLine()) == false ) {
					planCompareResult.setOriginLine( original.get(oldNum)+originalOption.get(oldNum) );
					oldNum++;
					
				}else {
					planCompareResult.setOriginLine("");
					planCompareResult.setTag(Tag.INSERT.toString());
				}
				
				if( "".equals(rows.get(i).getNewLine()) == false ) {
					planCompareResult.setNewLine( revised.get(newNum)+revisedOption.get(newNum) );
					newNum++;
					
				}else {
					planCompareResult.setNewLine("");
					planCompareResult.setTag(Tag.DELETE.toString());
				}
			}
			compareList.add(planCompareResult);
			
			logger.debug("|" +rows.get(i).getTag()+"|"+compareList.get(i).getTag()+"|" + compareList.get(i).getOriginLine() + "|" + compareList.get(i).getNewLine() + "|");
		}
		// return 하지 않는 리스트 데이터 비움
		original = null;
		revised = null;
		originalOption = null;
		revisedOption = null;
		rows = null;
		
		return compareList;
	}
	
}