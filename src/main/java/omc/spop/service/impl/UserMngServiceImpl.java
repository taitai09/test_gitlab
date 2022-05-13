package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.base.Config;
import omc.spop.base.SessionManager;
import omc.spop.dao.CommonDao;
import omc.spop.dao.UserMngDao;
import omc.spop.model.Auth;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.Result;
import omc.spop.model.UserAuth;
import omc.spop.model.UserDBPrivilege;
import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;
import omc.spop.service.UserMngService;
import omc.spop.service.WatchLogService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelRead;
import omc.spop.utils.ExcelReadOption;
import omc.spop.utils.FileMngUtil;
import omc.spop.utils.SHA256Util;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.11.09	이원식	최초작성
 * 2018.10.17	임호경	유저권한 수정
 * 2021.11.11	황예지	업무 관리 메뉴에 DB 설정 추가
 **********************************************************/

@Service("UserMngService")
public class UserMngServiceImpl implements UserMngService {
	private static final Logger logger = LoggerFactory.getLogger(UserMngServiceImpl.class);

	@Autowired
	private UserMngDao userMngDao;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private WatchLogService watchLogService;
	
	@Override
	public List<Users> usersList(Users users) throws Exception {
		return userMngDao.usersList(users);
	}
	
	@Override
	public Users checkUserId(Users users) throws Exception {
		return userMngDao.checkUserId(users);
	}	
	
	@Override
	public void saveUsers(Users users) throws Exception {
		UserAuth auth = new UserAuth(); 
		UserWrkjob wrkJob = new UserWrkjob();
		String defaultPwd = Config.getString("default.password");		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		users.setPassword(defaultPwd);

		if(users.getIs_new().equals("Y")){
			users.setUse_yn("Y");
		}
		
		userMngDao.saveUsers(users);
		//watchLogService.WatchInsertUsersInfoByDBmanager();
		
	}
	
	@Override
	public void updateUsers(Users users) throws Exception {
//		UserAuth auth = new UserAuth(); 
//		UserWrkjob wrkJob = new UserWrkjob();
		String defaultPwd = Config.getString("default.password");
//		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		users.setPassword(defaultPwd);
		
		if(users.getIs_new().equals("Y")){
			users.setUse_yn("Y");
		}
		userMngDao.updateUsers(users);
		
		watchLogService.WatchUpdateUsersInfo(users.getUser_id());
		
	}
	
	@Override
	public void insertUsers(Users users) throws Exception {
		String defaultPwd = Config.getString("default.password");		
//		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		
		
		users.setPassword(defaultPwd);
		
		if(users.getIs_new().equals("Y")){
			users.setUse_yn("Y");
		}
		
		if(SHA256Util.isAppliedSHA256()){
			logger.debug("######################## NEW ALGO SAVE SAVED ########################");
			
			String temp_password = pbeEnc.decrypt(defaultPwd);
			String salt = SHA256Util.generateSalt();
			String user_passwd = SHA256Util.getEncrypt(temp_password, salt);
			users.setPassword(user_passwd);
			users.setSalt_value(pbeEnc.encrypt(salt));
			
			userMngDao.insertUsersBySHA256(users);
		}else{
			logger.debug("######################## OLD ALGO SAVE SAVED ########################");
			userMngDao.insertUsers(users);
		}
		watchLogService.WatchInsertUsersInfoByDBmanager(users.getUser_id());
	}
	
	@Override
	public void approveUser(Users users) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		UserAuth auth = new UserAuth(); 
		UserWrkjob wrkJob = new UserWrkjob();
		
		
		users.setApprove_id(user_id);
		
		userMngDao.approveUser(users);		
		// 2. 권한 시작일 승일일자로 변경.
		auth.setUser_id(users.getUser_id());
		auth.setAuth_start_day(DateUtil.getNowDate("yyyyMMdd"));
		
		userMngDao.updateUserAuthForApprove(auth);
		// 3. 업무 시작일 승인일자로 변경.
		wrkJob.setUser_id(users.getUser_id());
		wrkJob.setWorkjob_start_day(DateUtil.getNowDate("yyyyMMdd"));
		
		userMngDao.updateUserWrkJob(wrkJob);
	}
	
	@Override
	public void resetUserPassword(Users users) throws Exception {
		String defaultPwd = Config.getString("default.password");
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)

		if(SHA256Util.isAppliedSHA256()){
			logger.debug("########## NEW ALGO UPDATED USER LOGIN ##########");

			String temp_password = pbeEnc.decrypt(defaultPwd);
			String salt = SHA256Util.generateSalt();
			String user_passwd = SHA256Util.getEncrypt(temp_password, salt);
			users.setPassword(user_passwd);
			users.setSalt_value(pbeEnc.encrypt(salt));
			userMngDao.resetUserPasswordBySHA256(users);
			
		}else{
			logger.debug("########## OLD ALGO UPDATED USER LOGIN ##########");

			users.setPassword(defaultPwd);
			userMngDao.resetUserPassword(users);
		}
		
		watchLogService.WatchUpdateResetUsersPasswordByDBmanager(users.getUser_id());
	}
	
	@Override
	public List<UserAuth> usersAuthList(UserAuth userAuth) throws Exception {
		return userMngDao.usersAuthList(userAuth);
	}
	
	@Override
	public void saveUsersAuth(UserAuth userAuth) throws Exception {
//		userMngDao.saveUsersAuth(userAuth);
		
		
		if(userAuth.getCrud_flag().equals("C")){
			int check = userMngDao.checkForSaveUserAuth(userAuth);
			if(check == 0){
				userMngDao.insertUserAuth(userAuth);
					//기본권한여부 체크Y 기본권한 업데이트
					if(userAuth.getDefault_auth_yn().equals("Y")){
						userMngDao.updateUsersDefaultAuth(userAuth);
					}
			}else{
				throw new Exception("동일한 권한을 두개이상 가질 수 없습니다.");
			}
		
		}else{//업데이트
			
			//업데이트 권한을 변경한경우
			if(!userAuth.getOld_auth_grp_id().equals(userAuth.getAuth_grp_id())){ // 업데이트시 권한을 바꾼경우
				int checkTwo = userMngDao.checkForSaveUserAuth(userAuth);
					if(checkTwo == 0){
						userMngDao.updateUserAuth(userAuth);
						//기본권한여부 체크Y 기본권한 업데이트
						if(userAuth.getOld_default_auth_yn().equals("N") && userAuth.getDefault_auth_yn().equals("Y")){
							userMngDao.updateUsersDefaultAuth(userAuth);
						}else if(userAuth.getOld_default_auth_yn().equals("Y") && userAuth.getDefault_auth_yn().equals("N")){
							userAuth.setAuth_grp_id("");
							userMngDao.updateUsersDefaultAuth(userAuth);
						}else if(userAuth.getOld_default_auth_yn().equals("Y") && userAuth.getDefault_auth_yn().equals("Y")){
							userMngDao.updateUsersDefaultAuth(userAuth);
						}
					}else{
						throw new Exception("동일한 권한을 두개이상 가질 수 없습니다.");
					}
				
			}else{ //업데이트시 권한을 변경하지 않은경우
				userMngDao.updateUserAuth(userAuth);
				//기본권한여부 체크Y 기본권한 업데이트
				if(userAuth.getOld_default_auth_yn().equals("N") && userAuth.getDefault_auth_yn().equals("Y")){
					userMngDao.updateUsersDefaultAuth(userAuth);
				}else if(userAuth.getOld_default_auth_yn().equals("Y") && userAuth.getDefault_auth_yn().equals("N")){
					userAuth.setAuth_grp_id("");
					userMngDao.updateUsersDefaultAuth(userAuth);
				}
			}
		
		}
		
		// 시작일자가 변경시 기존 종료일자를 입력받은 시작일자 -1일로  UPDATE
	/*	if(!userAuth.getAuth_comp_day().equals("")){
			if(!userAuth.getAuth_comp_day().equals(userAuth.getAuth_start_day())){
				UserAuth temp = new UserAuth();
				
				temp.setUser_id(userAuth.getUser_id());
				temp.setAuth_start_day(userAuth.getAuth_comp_day());
				
				String newDate = DateUtil.getNextDay("M", "yyyyMMdd", userAuth.getAuth_start_day(), "1");
				temp.setAuth_end_day(newDate);
				
				userMngDao.updatePastUsersAuth(temp);				
			}
		}*/  //알수 없는 로직이여서 일단 사용 안함 2018-10-17. 문제있을 시 다시 로직 실행
	}	
	
	@Override
	public List<UserWrkjob> usersWrkJobList(UserWrkjob userWrkjob) throws Exception {
		return userMngDao.usersWrkJobList(userWrkjob);
	}
	
	@Override
	public void saveUsersWrkJob(UserWrkjob userWrkjob) throws Exception {
//		userMngDao.checkWorkJobLeader(userWrkjob)
		int check = 0;
//		userMngDao.saveUsersWrkJob(userWrkjob);

		
		if(userWrkjob.getCrud_flag().equals("C")){  //인설트
			check = userMngDao.checkUsersWrkjobCd(userWrkjob);
			if(check == 0){
				userMngDao.insertUsersWrkJob(userWrkjob);
			}else{
				throw new Exception("동일한 업무을 두개이상 가질 수 없습니다.");
			}
			
			if(userWrkjob.getDefault_wrkjob_yn().equals("Y")){
				userMngDao.updateUsersDefaultWrkjobCd(userWrkjob);
			}
			
		}else{  //업데이트시
			
			//업무가 변경되었을시 
			if(!userWrkjob.getOld_wrkjob_cd().equals(userWrkjob.getWrkjob_cd())){
				check = userMngDao.checkUsersWrkjobCd(userWrkjob);
				if(check == 0){
					//리더를 선택한 경우 리더는 업무당 한명만 있어야하기때문에 그업무의 리더를 지운다.
					if(userWrkjob.getLeader_yn().equals("Y")){
						userMngDao.updateUsersWrkJobLeader(userWrkjob);
					}
					userMngDao.updateUsersWrkJob(userWrkjob);  //
					if(userWrkjob.getOld_default_wrkjob_yn().equals("N") && userWrkjob.getDefault_wrkjob_yn().equals("Y")){
						userMngDao.updateUsersDefaultWrkjobCd(userWrkjob);
					}else if(userWrkjob.getOld_default_wrkjob_yn().equals("Y") && userWrkjob.getDefault_wrkjob_yn().equals("N")){
						userWrkjob.setWrkjob_cd("");
						userMngDao.updateUsersDefaultWrkjobCd(userWrkjob);
					}else if(userWrkjob.getOld_default_wrkjob_yn().equals("Y") && userWrkjob.getDefault_wrkjob_yn().equals("Y")){
						userMngDao.updateUsersDefaultWrkjobCd(userWrkjob);
					}
				}else{
					throw new Exception("동일한 업무을 두개이상 가질 수 없습니다.");
				}
				
			}else{ //업무가 변경 안되었을 시
				//리더를 선택한 경우 리더는 업무당 한명만 있어야하기때문에 그업무의 리더를 지운다.
				if(userWrkjob.getLeader_yn().equals("Y")){
					userMngDao.updateUsersWrkJobLeader(userWrkjob);
				}
				userMngDao.updateUsersWrkJob(userWrkjob);  //
				if(userWrkjob.getOld_default_wrkjob_yn().equals("N") && userWrkjob.getDefault_wrkjob_yn().equals("Y")){
					userMngDao.updateUsersDefaultWrkjobCd(userWrkjob);
				}else if(userWrkjob.getOld_default_wrkjob_yn().equals("Y") && userWrkjob.getDefault_wrkjob_yn().equals("N")){
					userWrkjob.setWrkjob_cd("");
					userMngDao.updateUsersDefaultWrkjobCd(userWrkjob);
				}
			}
			
		}
		
		/*// 시작일자가 변경시 기존 종료일자를 입력받은 시작일자 -1일로  UPDATE
		if(!userWrkjob.getWork_comp_day().equals("")){
			if(!userWrkjob.getWork_comp_day().equals(userWrkjob.getWorkjob_start_day())){
				UserWrkjob temp = new UserWrkjob();
				
				temp.setUser_id(userWrkjob.getUser_id());
				temp.setWorkjob_start_day(userWrkjob.getWork_comp_day());
				
				String newDate = DateUtil.getNextDay("M", "yyyyMMdd", userWrkjob.getWorkjob_start_day(), "1");
				temp.setWorkjob_end_day(newDate);
				
				userMngDao.updatePastUsersWrkJob(temp);				
			}
		}*/ //알수 없는 로직이여서 일단 사용 안함 2018-10-17. 문제있을 시 다시 로직 실행
	}	
	
	@Override
	public UserWrkjob checkWorkJobLeader(UserWrkjob userWrkjob) throws Exception {
		int cnt= 0 ;
		cnt = userMngDao.cntWorkJobLeader(userWrkjob);
		if(cnt > 1){
//		if(check > 0){
			return new UserWrkjob(cnt);
		}else{
			return userMngDao.checkWorkJobLeader(userWrkjob);
		}
	}
	
	/*public List<UserDept> usersDeptList(UserDept userDept) throws Exception {
		return userMngDao.usersDeptList(userDept);
	}*/
	
	/*public void saveUsersDept(UserDept userDept) throws Exception {
		userMngDao.saveUsersDept(userDept);
		
		// 시작일자가 변경시 기존 종료일자를 입력받은 시작일자 -1일로  UPDATE
		if(!userDept.getWork_comp_day().equals("")){
			if(!userDept.getWork_comp_day().equals(userDept.getWork_start_day())){
				UserDept temp = new UserDept();
				
				temp.setUser_id(userDept.getUser_id());
				temp.setWork_start_day(userDept.getWork_comp_day());
				
				String newDate = DateUtil.getNextDay("M", "yyyyMMdd", userDept.getWork_start_day(), "1");
				temp.setWork_end_day(newDate);
				
				userMngDao.updatePastUsersDept(temp);				
			}
		}
	}*/	

	@Override
	public List<Auth> authList(Auth auth) throws Exception {
		return userMngDao.authList(auth);
	}

	@Override
	public void saveAuth(Auth auth) throws Exception {
		
		if(auth.getAuth_id() == null || auth.getAuth_id().equals("")){
			String authId = userMngDao.getMaxAuthId(auth);
			auth.setAuth_id(authId);
		}
				
		userMngDao.saveAuth(auth);
	}
	
	/*public List<Dept> departmentList(Dept dept) throws Exception {
		return userMngDao.departmentList(dept);
	}*/
	
	/*public void saveDepartment(Dept dept) throws Exception {
		userMngDao.saveDepartment(dept);
	}*/
	
	/*public List<Dept> departmentTreeList(Dept dept) throws Exception {
		return userMngDao.departmentTreeList(dept);
	}*/	
	
	/*public List<DeptDBPrivilege> departmentDBAuthList(DeptDBPrivilege deptDBPrivilege) throws Exception {
		return userMngDao.departmentDBAuthList(deptDBPrivilege);
	}*/
	
	/*public void saveDepartmentDBAuth(HttpServletRequest req) throws Exception {
		String dept_cd = StringUtil.nvl(req.getParameter("dept_cd"));
		String[] useFlagArr = (String[])req.getParameterValues("use_flag");
		String[] dbidArr = (String[])req.getParameterValues("dbid");
		String[] privilegeStartDayArr = (String[])req.getParameterValues("privilege_start_day");
		String[] privilegeEndDayArr = (String[])req.getParameterValues("privilege_end_day");
		
		if(useFlagArr != null){
			for(int i = 0; i < useFlagArr.length ; i++){
				DeptDBPrivilege deptDBPrivilege = new DeptDBPrivilege();
				
				// 사용자가 체크한 값만 저장처리 한다.
				if(useFlagArr[i].equals("Y")){
					deptDBPrivilege.setDept_cd(dept_cd);
					deptDBPrivilege.setDbid(dbidArr[i]);
					deptDBPrivilege.setPrivilege_start_day(privilegeStartDayArr[i]);
					deptDBPrivilege.setPrivilege_end_day(privilegeEndDayArr[i]);					
					
					userMngDao.saveDepartmentDBAuth(deptDBPrivilege);		
				}
			}
		}
	}*/
	
	/*public List<DeptDBPrivilege> departmentDBAuthHistoryList(DeptDBPrivilege deptDBPrivilege) throws Exception {
		return userMngDao.departmentDBAuthHistoryList(deptDBPrivilege);
	}*/
	
	@Override
	public List<WrkJobCd> workJobTreeList(WrkJobCd wrkJobCd) throws Exception {
		return userMngDao.workJobTreeList(wrkJobCd);
	}
	
	@Override
	public List<Users> workJobUsersList(Users users) throws Exception {
		return userMngDao.workJobUsersList(users);
	}
	
	@Override
	public void saveWorkJob(WrkJobCd wrkJobCd, String[] dbidArr) throws Exception {
		boolean keepGoing = false;
		List<String> finalUpperWrkjobCdList = null; //최종으로 파라미터를 체크할 리스트
		List<String> checkWrkjobCdList = null; //여러개의 하위트리 체크하는 리스트
		List<String> haveToCheckList = null;  //체크해서 있다면 루프를 계속 돌수있도록 하는 리스트
		List<String> upperWrkjobCdList = new ArrayList<String>(); 
		WrkJobCd wrkjob =  userMngDao.checkWrkjobDivCd(wrkJobCd.getWrkjob_div_cd());
		
		if(wrkJobCd.getCrud_flag().equals("U")
				&& !wrkJobCd.getWrkjob_div_cd().equals(wrkJobCd.getOld_wrkjob_div_cd())){
			
			if(wrkjob != null){
				String msg = "해당 업무코드는 이미 등록되어 있습니다.<BR/>";
				msg += "[ 업무명 : "+ wrkjob.getWrkjob_cd_nm() +" ]";
				throw new Exception(msg);
			}
			
		}else if(wrkJobCd.getCrud_flag().equals("C")){
			if(wrkjob != null){
				String msg = "해당 업무코드는 이미 등록되어 있습니다.<BR/>";
				msg += "[ 업무명 : "+ wrkjob.getWrkjob_cd_nm() +" ]";
				throw new Exception(msg);
			}
		}

		if(wrkJobCd.getCrud_flag().equals("U")
				&& wrkJobCd.getWrkjob_cd().equals(wrkJobCd.getUpper_wrkjob_cd())){
			
			throw new Exception("해당업무를 상위업무로 지정할 수 없습니다.");
		}
		upperWrkjobCdList = userMngDao.getWrkjobCdList(wrkJobCd.getWrkjob_cd());
		
		if(upperWrkjobCdList.size() > 0) {
			keepGoing = true; 
			finalUpperWrkjobCdList = new ArrayList<String>();
			haveToCheckList = new ArrayList<String>();
			finalUpperWrkjobCdList.addAll(upperWrkjobCdList);
		}
		
		while(keepGoing){
			for(int i = 0; i < upperWrkjobCdList.size(); i++){
				checkWrkjobCdList = new ArrayList<String>();

				checkWrkjobCdList = userMngDao.getWrkjobCdList(upperWrkjobCdList.get(i));
				if(checkWrkjobCdList.size() > 0){
					finalUpperWrkjobCdList.addAll(checkWrkjobCdList);
					haveToCheckList.addAll(checkWrkjobCdList);
				}
			}
			
			if(haveToCheckList.size() > 0){
				upperWrkjobCdList = haveToCheckList;
				haveToCheckList = new ArrayList<String>();
			}else{
				break;
			}
		}
		if(keepGoing){
			logger.debug("###### 자신의 하위업무 :"+finalUpperWrkjobCdList.toString());
			logger.debug("###### 변경할 상위업무:"+wrkJobCd.getUpper_wrkjob_cd());
//			userMngDao.checkUnderWrkjobCd(finalUpperWrkjobCdList);
			
			if(finalUpperWrkjobCdList.contains(wrkJobCd.getUpper_wrkjob_cd())){
				throw new Exception("자신의 하위업무를 상위업무로 지정 할 수 없습니다.");
			}
		}
		
		// 1. wrkjob_cd 값이 없을 경우 MAX WRKJOB_CD 조회
		String wrkJob_cd = "";
		
		if(wrkJobCd.getWrkjob_cd() == null || wrkJobCd.getWrkjob_cd().equals("")){
			wrkJob_cd = userMngDao.getMaxWrkJobCd(wrkJobCd);
			wrkJobCd.setWrkjob_cd(wrkJob_cd);
		}
		
		// 2. WRKJOB_CD MERGE
		userMngDao.saveWrkJobCd(wrkJobCd);
		saveWrkJobDb(dbidArr, wrkJobCd);	// 2021.11.11
	}
	
	@Override
	public void saveWorkJobLeader(UserWrkjob userWrkjob) throws Exception {
		String leaderYn = userWrkjob.getLeader_yn();
		
		if( "Y".equals(leaderYn) ) {
			UserWrkjob temp = new UserWrkjob();
			temp.setWrkjob_cd(userWrkjob.getWrkjob_cd());
			temp.setLeader_yn("N");
			
			userMngDao.updateUserWrkJobLeader(temp);
			
			temp = null;
		}
		
		userMngDao.updateUserWrkJobLeader(userWrkjob);
	}	
	
	@Override
	public List<UserDBPrivilege> userDBAuthList(UserDBPrivilege userDBPrivilege) throws Exception {
		return userMngDao.userDBAuthList(userDBPrivilege);
	}
	
	@Override
	public void saveUserDBAuth(HttpServletRequest req) throws Exception {
		String chk_user_id = req.getParameter("chk_user_id");
		String[] useFlagArr = req.getParameterValues("use_flag");
		String[] dbidArr = req.getParameterValues("dbid");
		String[] privilegeStartDayArr = req.getParameterValues("privilege_start_day");
		String[] privilegeEndDayArr = req.getParameterValues("privilege_end_day");
		
		UserDBPrivilege userDBPrivilege = new UserDBPrivilege();
		
		if(useFlagArr != null){
			for(int j = 0; j < chk_user_id.split(",").length; j++){
				
				userDBPrivilege.setUser_id(chk_user_id.split(",")[j]);					
				userMngDao.deleteUserDbAuth(userDBPrivilege);
				for(int i = 0; i < useFlagArr.length ; i++){
					
					// 사용자가 체크한 값만 저장처리 한다.
					if(useFlagArr[i].equals("Y")){
						userDBPrivilege.setDbid(dbidArr[i]);
						userDBPrivilege.setPrivilege_start_day(privilegeStartDayArr[i]);
						userDBPrivilege.setPrivilege_end_day(privilegeEndDayArr[i]);					
						
						userMngDao.saveUserDBAuth(userDBPrivilege);		
					}
				}
			}
		}
	}
	
	@Override
	public List<UserDBPrivilege> userDBAuthHistoryList(UserDBPrivilege userDBPrivilege) throws Exception {
		return userMngDao.userDBAuthHistoryList(userDBPrivilege);
	}
	
	@Override
	public List<Users> performanceTunerList(Users users) throws Exception {
		return userMngDao.performanceTunerList(users);
	}	
	
	@Override
	public List<DatabaseTuner> performanceOfficerList(DatabaseTuner databaseTuner) throws Exception {
		return userMngDao.performanceOfficerList(databaseTuner);
	}
	
	@Override
	public void savePerformanceOfficer(HttpServletRequest req) throws Exception {
		String tuner_id = StringUtil.nvl(req.getParameter("tuner_id"));
		String[] useFlagArr = req.getParameterValues("use_flag");
		String[] dbidArr = req.getParameterValues("dbid");
		String[] tunStartDayArr = req.getParameterValues("tun_start_day");
		String[] tunEndDayArr = req.getParameterValues("tun_end_day");
		
		//20190712 추가 문제가 될시 삭제
		userMngDao.deletePerformanceOfficer(tuner_id);
		
		if(useFlagArr != null){
			for(int i = 0; i < useFlagArr.length ; i++){
				DatabaseTuner databaseTuner = new DatabaseTuner();
				
				// 사용자가 체크한 값만 저장처리 한다.
				if(useFlagArr[i].equals("Y")){
					databaseTuner.setTuner_id(tuner_id);
					databaseTuner.setDbid(dbidArr[i]);
					databaseTuner.setTun_start_day(tunStartDayArr[i]);
					databaseTuner.setTun_end_day(tunEndDayArr[i]);					
					
					userMngDao.savePerformanceOfficer(databaseTuner);		
				}
			}
		}
	}
	
	@Override
	public List<DatabaseTuner> performanceOfficerHistoryList(DatabaseTuner databaseTuner) throws Exception {
		return userMngDao.performanceOfficerHistoryList(databaseTuner);
	}

	@Override
	public int saveAuthBundle(Auth auth) throws Exception {
		return userMngDao.saveAuthBundle(auth);
	}
//    //Service 단에서 가져온 코드 
//    ExcelReadOption excelReadOption = new ExcelReadOption();
//    excelReadOption.setFilePath(destFile.getAbsolutePath());
//    excelReadOption.setOutputColumns("A","B","C","D","E","F");
//    excelReadOption.setStartRow(2);
//    
//    
//    List<Map<String, String>>excelContent =ExcelRead.read(excelReadOption);
//    
//    for(Map<String, String> article: excelContent){
//        System.out.println(article.get("A"));
//        System.out.println(article.get("B"));
//        System.out.println(article.get("C"));
//        System.out.println(article.get("D"));
//        System.out.println(article.get("E"));
//        System.out.println(article.get("F"));
//    }

	@Override
	public Result uploadUserExcelFile(MultipartFile file) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		ExcelReadOption option = new ExcelReadOption();
		Result result = new Result();
		int resultCount = 0;
		String filePath = "";
		String approve_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		// 1. 엑셀 파일 업로드
		try {
			filePath = fileMng.uploadExcel(file);
			
		}catch (Exception ex) {
			logger.error("엑셀 파일 업로드 error => " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
		
		option.setFilePath(filePath);
		option.setStartRow(2);
		
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");  
		pbeEnc.setPassword("madeopen"); // PBE 값(XML PASSWORD설정)
		String encDefaultPasswd = Config.getString("default.password");
		int errCnt = 0;
		int totalCnt = 0;
		Map<String, Object> errObj = new HashMap<String, Object>();  //err 정보를 담을 obj
		Vector<String> errUsers = new Vector<String>(); //err 유저를 담을 obj
		Vector<Integer> errIndexs = new Vector<Integer>(); //err 로우를 담을 obj
		int errIndex = 1; //엑셀의 몇번째 로우에서 에러나는지 알려주기위한 용도.
		
		try{
			List<Map<String, String>> excelContent = ExcelRead.read(option);
			totalCnt = excelContent.size();
			
			Users users = null;
			Auth auth = null;
			UserWrkjob userWrkjob = null;
			String user_id, auth_id, wrkjob_cd = ""; 
			int userCheck, authCheck, wrkjobCdCheck = 0;
			for(Map<String, String> article : excelContent){
				errIndex += 1;
				System.out.println("############## 엑셀 업로드 데이터 ##############");
		        System.out.print(article.get("A")+"\t");
		        System.out.print(article.get("B")+"\t");
		        System.out.print(article.get("C")+"\t");
		        System.out.print(article.get("D")+"\t");
		        System.out.print(article.get("E")+"\t");
		        System.out.print(article.get("F")+"\t");				
		        System.out.print(article.get("G")+"\t");				
		        System.out.print(article.get("H")+"\t");				
		        System.out.print(article.get("I")+"\t");				
		        System.out.print(article.get("J")+"\t");				
		        System.out.println(article.get("K"));				
				
				users = new Users();
				user_id = StringUtils.defaultString(article.get("A"));
				auth_id = StringUtils.defaultString(article.get("F"));
				wrkjob_cd = StringUtils.defaultString(article.get("I"));
				
				userCheck = userMngDao.cntkUserId(user_id);
				authCheck = userMngDao.cntAuthId(auth_id);
				//다른 고객사는 OPENPOP고유 업무코드를 모른다.
				//wrkjob_div_cd = wrkjob_cd 는 1:1 맵핑으로 wrkjob_div를 체크한다.
				if(!StringUtils.defaultString(article.get("I")).equals("")){
					//개발자 일경우에만
					wrkjobCdCheck = userMngDao.cntWrkjobCd(wrkjob_cd);
				}else{
					wrkjobCdCheck = 99;
				}
				
				//중복시 아래로직을 실행안하고 에러 객체에 에러를 추가함.
				if(userCheck > 0 || authCheck == 0 || wrkjobCdCheck == 0){
					errCnt += 1;
					errUsers.add(user_id);
					errIndexs.add(errIndex);
					System.out.println("실패 행 ::"+errIndex);
					continue;
				}
				
				users.setApprove_id(approve_id);
				users.setUser_id(StringUtils.defaultString(article.get("A")));
				users.setUser_nm(StringUtils.defaultString(article.get("B")));
				users.setExt_no(StringUtils.defaultString(article.get("C")));
				users.setHp_no(StringUtils.defaultString(article.get("D")));
				users.setEmail(StringUtils.defaultString(article.get("E")));
				users.setWrkjob_cd(StringUtils.defaultString(article.get("I")));

				if(StringUtils.defaultString(article.get("F")).equals("4") 
						|| StringUtils.defaultString(article.get("F")).equals("6")){
					users.setDefault_auth_grp_id(StringUtils.defaultString(article.get("F")));
					users.setDefault_wrkjob_cd(StringUtils.defaultString(article.get("I")));
				}
				
				users.setPassword(encDefaultPasswd);
				if(!StringUtils.defaultString(article.get("A")).equals("") &&
					!StringUtils.defaultString(article.get("B")).equals("")){
					
					if(SHA256Util.isAppliedSHA256()){
						logger.debug("################# NEW ALGO USER UPLOAD #################");
						String salt = SHA256Util.generateSalt();
						String decDefaultPasswd = pbeEnc.decrypt(encDefaultPasswd);
						String newPassword = SHA256Util.getEncrypt(decDefaultPasswd, salt);
						
						users.setSalt_value(pbeEnc.encrypt(salt));
						users.setPassword(newPassword);
						
						resultCount += userMngDao.saveUserByExcelUploadBySHA256(users);
						
						salt = null; decDefaultPasswd= null; newPassword = null;
					}else{
						logger.debug("################# OLD ALGO USER UPLOAD #################");
						resultCount += userMngDao.saveUserByExcelUpload(users);
					}
				}

				//감사로그에 저장
				try {
					watchLogService.WatchInsertUsersInfoByDBmanager(String.valueOf(StringUtils.defaultString(article.get("A"))));	
				} catch (Exception e) {	e.printStackTrace();}

				
				auth = new Auth();
				auth.setUser_id(StringUtils.defaultString(article.get("A")));
				auth.setAuth_grp_id(StringUtils.defaultString(article.get("F")));
				auth.setAuth_start_day(StringUtils.defaultString(article.get("G")));
				auth.setAuth_end_day(StringUtils.defaultString(article.get("H")));
				
				if(!StringUtils.defaultString(article.get("F")).equals("") &&  
					!StringUtils.defaultString(article.get("G")).equals("") && 
					!StringUtils.defaultString(article.get("H")).equals("")){
					resultCount += userMngDao.saveUserAuthByExcelUpload(auth);
				}

				userWrkjob = new UserWrkjob();
				userWrkjob.setUser_id(StringUtils.defaultString(article.get("A")));
//				userWrkjob.setWrkjob_cd(userMngDao.getOmcWrkjobCdByWrkjobDivCd(article.get("I")));
				userWrkjob.setWrkjob_cd(StringUtils.defaultString(article.get("I")));
				userWrkjob.setWorkjob_start_day(StringUtils.defaultString(article.get("J")));
				userWrkjob.setWorkjob_end_day(StringUtils.defaultString(article.get("K")));
//				userWrkjob.setLeader_yn(article.get("L"));
				
				if(!StringUtils.defaultString(article.get("I")).equals("") && 
					!StringUtils.defaultString(article.get("J")).equals("") &&
					!StringUtils.defaultString(article.get("K")).equals("")){
					//개발자 일경우에만 실행
					resultCount += userMngDao.saveUserWrkjobByExcelUpload(userWrkjob);
				}
				
			}
			
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", errCnt > 0 ? true : false);
			errObj.put("errCnt", errCnt);
			errObj.put("errUserId", errUsers.toString());
			errObj.put("errIndex", errIndexs.toString());
			result.setObject(errObj);			
			result.setMessage("success");
			result.setResult(true);
		}catch (Exception ex) {
			logger.error("엑셀 파일 조회 error => " + ex.getMessage());
			ex.printStackTrace();
			errObj.put("totalCnt", totalCnt);
			errObj.put("isErr", true);
			errObj.put("errIndex",errIndexs.toString());
			errObj.put("errUserId", errUsers.toString());
			result.setObject(errObj);			
			result.setMessage("엑셀 업로드가 실패하였습니다. <BR/>총 [ "+totalCnt+" ] 건 중 [ "+errIndex+ " ] 번째 행에서 에러가 발생하였습니다.<BR/>"+ex.getMessage());
			result.setResult(false);
//			throw new Exception();
//			throw ex;
		}

		return result;
	}

	@Override
	public int deleteUsersAuth(UserAuth userAuth) throws Exception {
		int check = userMngDao.checkDefaultAuth(userAuth);
		
		if(check == 0){
			userMngDao.deleteUsersAuth(userAuth);
			}else{
			throw new Exception("기본권한은 삭제할 수 없습니다.<br/>기본권한여부를 해지하고 삭제바랍니다.");
		}
		return check;
	}

	@Override
	public int deleteUsersWrkJob(UserWrkjob userWrkjob) throws Exception {
		int check = userMngDao.checkDefaultWrkjob(userWrkjob);
		
		if(check == 0){
			check = userMngDao.deleteUsersWrkJob(userWrkjob);
		}else{
			throw new Exception("기본업무는 삭제할 수 없습니다.<br/>기본업무여부를 해지하고 삭제바랍니다.");
		}
		
		return check; 
	}
		
	@Override
	public int deleteWrkJobCd(WrkJobCd wrkJobCd) {
		return userMngDao.deleteWrkJobCd(wrkJobCd);
	}

	@Override
	public int saveUserApprove(Users users) {
		int check = 0;
		for(int i = 0; i < users.getChk_user_id().split("/").length; i++){
			users.setUser_id(users.getChk_user_id().split("/")[i]);
			check += userMngDao.saveUserApprove(users);
		}
		return check;
	}

	@Override
	public List<LinkedHashMap<String, Object>> getUsersListByExcelDown(Users users) {
		return userMngDao.getUsersListByExcelDown(users);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getAuthUserNameByExcelDown(Users users) {
		return userMngDao.getAuthUserNameByExcelDown(users);
	}

	@Override
	public List<LinkedHashMap<String, Object>> workJobUsersListByExcelDown(Users users) {
		return userMngDao.workJobUsersListByExcelDown(users);
	}

	@Override
	public int saveUserDBAuth2(UserDBPrivilege userDBPrivilege) throws Exception {
		int check = 0;
		check = userMngDao.checkForUserDBAuth2(userDBPrivilege);
		System.out.println("crud_flag ::: "+userDBPrivilege.getCrud_flag());

		if(userDBPrivilege.getCrud_flag().equals("C")){
			if(check > 0){
				throw new Exception("동일한 DB권한을 두개이상 가질수 없습니다.");
			}else{
				check = userMngDao.insertUserDBAuth2(userDBPrivilege);
			}
		}else{
			if(check > 0 && !userDBPrivilege.getOld_dbid().equals(userDBPrivilege.getDbid())){
				throw new Exception("동일한 DB권한을 두개이상 가질수 없습니다.");
			}else{
				check = userMngDao.updateUserDBAuth2(userDBPrivilege);
			}
		}
		
		return check;
	}

	@Override
	public List<UserDBPrivilege> usersDbAuth(UserDBPrivilege userDBPrivilege) {
		return userMngDao.usersDbAuth(userDBPrivilege);
	}

	@Override
	public int deleteUserDBAuth2(UserDBPrivilege userDBPrivilege) {
		return userMngDao.deleteUserDBAuth2(userDBPrivilege);
	}
	
	private void saveWrkJobDb(String[] dbidArr, WrkJobCd wrkjob_cd) throws Exception {
		String wrkjob_cd_str = wrkjob_cd.getWrkjob_cd_target();
		String[] wrkjob_cd_arr = wrkjob_cd_str.split(",");
		
		userMngDao.deletePreData( wrkjob_cd_arr );
		
		List<Map<String, Object>> compareList = new ArrayList<Map<String, Object>>();
		Map<String, Object> innerMap = Collections.emptyMap();
		
		for(String dbid : dbidArr) {
			if( dbid != null && dbid.isEmpty() == false ) {
				
				for(String wrkjobCd : wrkjob_cd_arr) {
					if( wrkjobCd != null && wrkjobCd.isEmpty() == false) {
						
						innerMap = new HashMap<String, Object>();
						innerMap.put("dbid", dbid);
						innerMap.put("wrkjob_cd", wrkjobCd);
						
						if( compareList.contains(innerMap) == false ) {
							userMngDao.saveWrkJobDb(innerMap);
							compareList.add(innerMap);
						}
					}
				}
			}
		}
		
		wrkjob_cd_str = null;
		wrkjob_cd_arr = null;
		innerMap = null;
		compareList = null;
	}
}