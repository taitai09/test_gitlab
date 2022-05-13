package omc.spop.service.impl;

import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.WatchLogDao;
import omc.spop.model.Users;
import omc.spop.service.WatchLogService;
import omc.spop.utils.IPTraceUtils;
/***********************************************************
 * 2018.09.04 임호경 최초작성
 **********************************************************/

@Service("watchLogService")
public class WatchLogServiceImpl implements WatchLogService {
	private static final Logger logger = LoggerFactory.getLogger(WatchLogServiceImpl.class);
	/************************************
	 * 감사로그 예시 ***************************************** 01 dbmanager 127.0.0.1
	 * Login + ROLE_DBMANAGER Fail 001: Login + ROLE명 01 dbmanager 127.0.0.1
	 * Login + ROLE_DBMANAGER Success 02 opmanager1 255.2.11.1 Administrator
	 * account login Success 002: Administrator account login 03 dev1
	 * 255.255.123.1 Change user information + dev1 Success 003 UPDATE시: Change
	 * user information + 로그인사용자ID 03 dbmanager 127.0.0.1 User Information
	 * Registration + dev2 Success 003 INSERT시: User Information Registration +
	 * 등록대상사용자ID 04 dev1 255.255.123.1 Change Password Success 004: Change
	 * Password 04 dbmanager 255.255.123.1 Change Password + 비밀번호 초기화 + dev2
	 * Success 004: Change Password + 비밀번호 초기화 + 등록대상사용자ID 05 dev1 255.255.123.1
	 * Log out Success 005: Log out
	 *************************************************************************************/

	/****************************
	 * 감사로그 **************************** 감사로그구분코드(1077) 01: 로그인 > 로그인USER_ID 02:
	 * 관리자 계정 로그인 > Open POP관리자(ROLE_OPENPOPMANAGER) 03: 사용자 정보 변경 > UPDATE 시 >
	 * 사용자 정보 변경 사용자 정보 등록 > INSERT시 > 사용자 정보 등록 04: 비밀번호 변경 > 비밀번호 변경 05: 로그아웃
	 * > 로그아웃
	 ***************************************************************/

/*
	01	dbmanager	127.0.0.1	Login + ROLE_DBMANAGER	Fail	001: Login + ROLE명 
	01	dbmanager	127.0.0.1	Login + ROLE_DBMANAGER	Success	
	02	opmanager1	255.2.11.1	Administrator account login	Success	002: Administrator account login
	03	dev1	255.255.123.1	Change user information + dev1	Success	003 UPDATE시: Change user information + 로그인사용자ID
	03	dbmanager	127.0.0.1	User Information Registration + dev2	Success	003 INSERT시: User Information Registration + 등록대상사용자ID
	04	dev1	255.255.123.1	Change Password	Success	004: Change Password
	04	dbmanager	255.255.123.1	Change Password + 비밀번호 초기화 + dev2	Success	004: Change Password + 비밀번호 초기화 + 등록대상사용자ID
	05	dev1	255.255.123.1	Log out	Success	005: Log out
*/
	@Autowired
	private WatchLogDao watchLogDao;
	
	@Autowired(required=true) 
	private HttpServletRequest request;

		
	
	//	01	dbmanager	127.0.0.1	Login + ROLE_DBMANAGER	Fail	001: Login + ROLE명 
	//	01	dbmanager	127.0.0.1	Login + ROLE_DBMANAGER	Success	
	@Override
	public int WatchLoginUsersSuccess() throws Exception {
	Users users = SessionManager.getLoginSession().getUsers();;
	InetAddress local = InetAddress.getLocalHost();
	//String ip = StringUtils.defaultString(local.getHostAddress());
	String ip = IPTraceUtils.getRemoteAddr(request);
	
		if(!users.getAuth_cd().equals("ROLE_OPENPOPMANAGER")){
			users.setAudit_log_div_cd("01"); //감사로그발생일시
			users.setAudit_log_ip(ip);  //감사로그발생IP
			users.setAudit_log_desc("001: Login "+ users.getAuth_cd()); //감사로그내용
			users.setAudit_log_result_value("Success"); //감사로그실행결과값
			
			logger.debug("##########"+users.getUser_id()+" / "+users.getAudit_log_ip()+" / "+users.getAudit_log_desc()+"##########");
			return watchLogDao.insertWatchLog(users);
			
			
		}else{
			
			users.setAudit_log_div_cd("02"); //감사로그발생일시
			users.setAudit_log_ip(ip);  //감사로그발생IP
			users.setAudit_log_desc("002: Administrator account login"); //감사로그내용
			users.setAudit_log_result_value("Success"); //감사로그실행결과값
			
			return watchLogDao.insertWatchLog(users);
		}
	}
	
	@Override
	public int WatchLoginUsersFail() throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();;
		InetAddress local = InetAddress.getLocalHost();
		//String ip = StringUtils.defaultString(local.getHostAddress());
		String ip = IPTraceUtils.getRemoteAddr(request);

		if(StringUtils.defaultString(users.getApprove_yn()).equals("N"))
			users.setAuth_cd("기본권한 없음");
		
		if(!users.getAuth_cd().equals("ROLE_OPENPOPMANAGER")){
			users.setAudit_log_div_cd("01"); //감사로그발생일시
			users.setAudit_log_ip(ip);  //감사로그발생IP
			users.setAudit_log_desc("001: Login "+ users.getAuth_cd()); //감사로그내용
			users.setAudit_log_result_value("Fail"); //감사로그실행결과값
			
			return watchLogDao.insertWatchLog(users);
			
			
		}else{
			
			users.setAudit_log_div_cd("02"); //감사로그발생일시
			users.setAudit_log_ip(ip);  //감사로그발생IP
			users.setAudit_log_desc("002: Administrator account login"); //감사로그내용
			users.setAudit_log_result_value("Fail"); //감사로그실행결과값
			
			return watchLogDao.insertWatchLog(users);
		}
	}
	
	/*//	01	dbmanager	127.0.0.1	Login + ROLE_DBMANAGER	Fail	
	@Override
	public int WatchLoginUsersFail(Users users) throws Exception {
		try {
			InetAddress local = InetAddress.getLocalHost();
			String ip = StringUtils.defaultString(local.getHostAddress());
			
			if(!StringUtils.defaultString(users.getDefault_auth_grp_cd()).equals("ROLE_OPENPOPMANAGER")){
				users.setAudit_log_div_cd("01"); //감사로그발생일시
				users.setAudit_log_ip(ip);  //감사로그발생IP
				users.setAudit_log_desc("001: Login "+ StringUtils.defaultString(users.getDefault_auth_grp_cd(),"기본권한 없음")); //감사로그내용
				users.setAudit_log_result_value("Fail"); //감사로그실행결과값
				
				logger.debug("##########"+users.getUser_id()+" / "+users.getAudit_log_ip()+" / "+users.getAudit_log_desc()+"##########");
				return watchLogDao.insertWatchLog(users);
				
				
			}else{
				
				users.setAudit_log_div_cd("02"); //감사로그발생일시
				users.setAudit_log_ip(ip);  //감사로그발생IP
				users.setAudit_log_desc("002: Administrator account login"); //감사로그내용
				users.setAudit_log_result_value("Fail"); //감사로그실행결과값
				
				logger.debug("########## "+users.getUser_id()+" / "+users.getAudit_log_ip()+" / "+users.getAudit_log_desc()+"##########");
				return watchLogDao.insertWatchLog(users);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}*/
	
	//	01	dbmanager	127.0.0.1	Login + ROLE_DBMANAGER	Fail	
	@Override
	public int WatchLoginUsers(Users users, Boolean result) throws Exception {
		
		try {
			InetAddress local = InetAddress.getLocalHost();
			//String ip = StringUtils.defaultString(local.getHostAddress());
			String ip = IPTraceUtils.getRemoteAddr(request);
			
			if(!StringUtils.defaultString(users.getDefault_auth_grp_cd()).equals("ROLE_OPENPOPMANAGER")){
				users.setAudit_log_div_cd("01"); //감사로그발생일시
				users.setAudit_log_ip(ip);  //감사로그발생IP
				
				users.setAudit_log_desc("001: Login "+ StringUtils.defaultString(users.getDefault_auth_grp_cd(),"기본권한 없음")); //감사로그내용
				users.setAudit_log_desc("001: Login "+ StringUtils.defaultString(users.getDefault_auth_grp_cd(),"기본권한 없음")); //감사로그내용

				//감사로그실행결과값
				if(result)
					users.setAudit_log_result_value("Success");
				else
					users.setAudit_log_result_value("Fail");
				
				return watchLogDao.insertWatchLog(users);
				
			}else{
				
				users.setAudit_log_div_cd("02"); //감사로그발생일시
				users.setAudit_log_ip(ip);  //감사로그발생IP
				users.setAudit_log_desc("002: Administrator account login"); //감사로그내용
				//감사로그실행결과값
				if(result)
					users.setAudit_log_result_value("Success");
				else
					users.setAudit_log_result_value("Fail");				
				return watchLogDao.insertWatchLog(users);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	

	//	03	dev1	255.255.123.1	Change user information + dev1	Success	003 UPDATE시: Change user information + 로그인사용자ID
	@Override
	public int WatchUpdateUsersInfo(String for_user_id) throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();;
		InetAddress local = InetAddress.getLocalHost();
		//String ip = StringUtils.defaultString(local.getHostAddress());
		String ip = IPTraceUtils.getRemoteAddr(request);
		String desc = "";
		
		users.setAudit_log_div_cd("03"); //감사로그발생일시
		users.setAudit_log_ip(ip);  //감사로그발생IP
		users.setAudit_log_desc("003: Change user information "+for_user_id); //감사로그내용
		users.setAudit_log_result_value("Success"); //감사로그실행결과값
		
		return watchLogDao.insertWatchLog(users);
	}
	
	//	03	dbmanager	127.0.0.1	User Information Registration + dev2	Success	003 INSERT시: User Information Registration + 등록대상사용자ID
	@Override
	public int WatchInsertUsersInfoByDBmanager(String for_user_id) throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();;
		InetAddress local = InetAddress.getLocalHost();
		String ip = IPTraceUtils.getRemoteAddr(request);
		//String ip = StringUtils.defaultString(local.getHostAddress());
		String desc = "";
			
		users.setAudit_log_div_cd("03"); //감사로그발생일시
		users.setAudit_log_ip(ip);  //감사로그발생IP
		users.setAudit_log_desc("003: User Information Registration "+for_user_id); //감사로그내용
		users.setAudit_log_result_value("Success"); //감사로그실행결과값
		
		return watchLogDao.insertWatchLog(users);
	}
	//	03	dbmanager	127.0.0.1	User Information Registration + dev2	Success	003 INSERT시: User Information Registration + 등록대상사용자ID
	@Override
	public int WatchInsertUsersInfo(String for_user_id) throws Exception {
		Users users = new Users();
		InetAddress local = InetAddress.getLocalHost();
		String ip = IPTraceUtils.getRemoteAddr(request);
		//String ip = StringUtils.defaultString(local.getHostAddress());
		String desc = "";
		
		users.setUser_id(for_user_id);
		users.setAudit_log_div_cd("03"); //감사로그발생일시
		users.setAudit_log_ip(ip);  //감사로그발생IP
		users.setAudit_log_desc("003: User Information Registration "+for_user_id); //감사로그내용
		users.setAudit_log_result_value("Success"); //감사로그실행결과값
		
		return watchLogDao.insertWatchLog(users);
	}

	//	04	dev1	255.255.123.1	Change Password	Success	004: Change Password
	@Override
	public int WatchUpdateUsersPassword() throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();;
		InetAddress local = InetAddress.getLocalHost();
		String ip = IPTraceUtils.getRemoteAddr(request);
		//String ip = StringUtils.defaultString(local.getHostAddress());
		String desc = "";
			
		users.setAudit_log_div_cd("04"); //감사로그발생일시
		users.setAudit_log_ip(ip);  //감사로그발생IP
		users.setAudit_log_desc("004: Change Password"); //감사로그내용
		users.setAudit_log_result_value("Success"); //감사로그실행결과값
		
		return watchLogDao.insertWatchLog(users);
	}
	
	//	04	dbmanager	255.255.123.1	Change Password + 비밀번호 초기화 + dev2	Success	004: Change Password + 비밀번호 초기화 + 등록대상사용자ID
	@Override
	public int WatchUpdateResetUsersPasswordByDBmanager(String for_user_id) throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();;
		InetAddress local = InetAddress.getLocalHost();
		String ip = IPTraceUtils.getRemoteAddr(request);
		//String ip = StringUtils.defaultString(local.getHostAddress());
		String desc = "";
		
		users.setAudit_log_div_cd("04"); //감사로그발생일시
		users.setAudit_log_ip(ip);  //감사로그발생IP
		users.setAudit_log_desc("004: Change Password 비밀번호 초기화 "+for_user_id); //감사로그내용
		users.setAudit_log_result_value("Success"); //감사로그실행결과값
		
		return watchLogDao.insertWatchLog(users);
	}

	// 	05	dev1	255.255.123.1	Log out	Success	005: Log out
	@Override
	public int WatchLoginLogout() throws Exception {
		Users users = SessionManager.getLoginSession().getUsers();;
		InetAddress local = InetAddress.getLocalHost();
		//String ip = StringUtils.defaultString(local.getHostAddress());
		String ip = IPTraceUtils.getRemoteAddr(request);
		String desc = "";
			
		users.setAudit_log_div_cd("05"); //감사로그발생일시
		users.setAudit_log_ip(ip);  //감사로그발생IP
		users.setAudit_log_desc("005: Log out "+desc); //감사로그내용
		users.setAudit_log_result_value("Success"); //감사로그실행결과값
		
			return watchLogDao.insertWatchLog(users);
	}

	/*@Override
	public int WatchLoginLogout(HttpServletRequest request) {
		Users users = SessionManager.getLoginSession().getUsers();;
		String ip = IPTraceUtils.getRemoteAddr(request);
		//InetAddress local = InetAddress.getLocalHost();
		//String ip = StringUtils.defaultString(local.getHostAddress());
		String desc = "";
		
		
		users.setAudit_log_div_cd("05"); //감사로그발생일시
		users.setAudit_log_ip(ip);  //감사로그발생IP
		users.setAudit_log_desc("005: Log out "+desc); //감사로그내용
		users.setAudit_log_result_value("Success"); //감사로그실행결과값
		
		logger.debug("########## "+users.getUser_id()+" / "+users.getAudit_log_ip()+" / "+users.getAudit_log_desc()+"##########");

			return watchLogDao.insertWatchLog(users);
	}*/




}
