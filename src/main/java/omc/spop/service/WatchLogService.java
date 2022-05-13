package omc.spop.service;

import javax.servlet.http.HttpServletRequest;

import omc.spop.model.Users;

public interface WatchLogService {
	
	/**************************** 감사로그 ****************************	
	감사로그구분코드(1077)
	01: 로그인 > 로그인USER_ID
	02: 관리자 계정 로그인 > Open POP관리자(ROLE_OPENPOPMANAGER)
	03: 사용자 정보 변경 > UPDATE 시  > 사용자 정보 변경
	      사용자 정보 등록 > INSERT시     > 사용자 정보 등록
	04: 비밀번호 변경 > 비밀번호 변경
	05: 로그아웃 > 로그아웃
 ***************************************************************/
	
	
	/**	 01: 로그인 > 로그인USER_ID / 02: 관리자 계정 로그인 > Open POP관리자(ROLE_OPENPOPMANAGER)*/
	int WatchLoginUsers(Users resultUser, Boolean result) throws Exception;
	
	int WatchLoginUsersSuccess()  throws Exception;
	/**	 01: 로그인 > Fail / 02: 관리자 계정 로그인 > Fail */
	int WatchLoginUsersFail()  throws Exception;
	
	/** 02: 관리자 계정 로그인 > Open POP관리자(ROLE_OPENPOPMANAGER) */
	//int WatchLoginAdministrator()  throws Exception;
	
	/** 03: 사용자 정보 변경 > UPDATE 시  > 사용자 정보 변경 */
	int WatchUpdateUsersInfo(String for_user_id)  throws Exception;
	
	/** 03: 사용자 정보 변경 > UPDATE 시  > 사용자 정보 변경  (DBMANAGER)*/
	int WatchInsertUsersInfoByDBmanager(String for_user_id)  throws Exception;
	
	/** 03: 사용자 정보 변경 > UPDATE 시  > 사용자 정보 변경  (DBMANAGER)*/
	int WatchInsertUsersInfo(String for_user_id)  throws Exception;

	/** 04: 비밀번호 변경 > 비밀번호 변경 */
	int WatchUpdateUsersPassword()  throws Exception;

	/** 04: 비밀번호 변경 > 비밀번호 변경 > 초기화 (DBMANAGER) */
	int WatchUpdateResetUsersPasswordByDBmanager(String for_user_id) throws Exception;
	
	/** 05: 로그아웃 > 로그아웃 */
	int WatchLoginLogout()  throws Exception;

	//int WatchLoginLogout(HttpServletRequest request) throws Exception;

}
