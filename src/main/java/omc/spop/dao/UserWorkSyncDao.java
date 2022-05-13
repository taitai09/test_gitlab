package omc.spop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import omc.spop.model.UserWrkjob;
import omc.spop.model.Users;
import omc.spop.model.WrkJobCd;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2021.01.21 황예지 try~catch문 삭제, throws 상위 메소드에 오류 전가
 **********************************************************/

@Repository
public class UserWorkSyncDao {
	private static final Logger logger = LoggerFactory.getLogger(UserWorkSyncDao.class);

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private SqlSession eamSqlSession;

	@Autowired
	private SqlSession iqmsSqlSession;

	@Value("#{serverConfig['eam.jdbc.driverClass']}")
	private String eamJdbcDriverClass;

	@Value("#{serverConfig['eam.jdbc.url']}")
	private String eamJdbcUrl;

	@Value("#{serverConfig['eam.jdbc.user']}")
	private String eamJdbcUser;

	@Value("#{serverConfig['eam.jdbc.password']}")
	private String eamJdbcPassword;

	@Value("#{serverConfig['iqms.jdbc.driverClass']}")
	private String iqmsJdbcDriverClass;

	@Value("#{serverConfig['iqms.jdbc.url']}")
	private String iqmsJdbcUrl;

	@Value("#{serverConfig['iqms.jdbc.user']}")
	private String iqmsJdbcUser;

	@Value("#{serverConfig['iqms.jdbc.password']}")
	private String iqmsJdbcPassword;

	/**
	 * 업무코드 동기화
	 * 
	 * @param param
	 * @return
	 */
	public List<WrkJobCd> searchWorkJobCd() {
		return iqmsSqlSession.selectList("UserWorkSyncDao.searchWorkJobCd");
	}

	public int deleteKbcdWrkJobCd() {
		return sqlSession.delete("UserWorkSyncDao.deleteKbcdWrkJobCd");
	}

	public int insertKbcdWrkJobCd(WrkJobCd cd) {
		return sqlSession.insert("UserWorkSyncDao.insertKbcdWrkJobCd", cd);
	}

	public int insertKbcdWrkJobCdAtOnce(List<WrkJobCd> wrkJobCdList) {
		return sqlSession.insert("UserWorkSyncDao.insertKbcdWrkJobCdAtOnce", wrkJobCdList);
	}

	public int insertWrkjobCd() {
		return sqlSession.insert("UserWorkSyncDao.insertWrkjobCd");
	}

	/**
	 * 사용자 동기화
	 * 
	 * @param param
	 * @return
	 */
	public List<Users> searchUsers() {
		return eamSqlSession.selectList("UserWorkSyncDao.searchUsers");
	}

	public int deleteKbcdUsers() {
		return sqlSession.delete("UserWorkSyncDao.deleteKbcdUsers");
	}

	public int insertKbcdUser(Users user) {
		return sqlSession.insert("UserWorkSyncDao.insertKbcdUser", user);
	}

	public int insertKbcdUserAtOnce(List<Users> userList) {
		return sqlSession.insert("UserWorkSyncDao.insertKbcdUserAtOnce", userList);
	}

	public int mergeUsers() {
		return sqlSession.insert("UserWorkSyncDao.mergeUsers");
	}

	public int mergeUserAuth() {
		return sqlSession.insert("UserWorkSyncDao.mergeUserAuth");
	}
	
	/**
	 * 사용자 업무코드 동기화
	 * 
	 * @param param
	 * @return
	 */
	public List<UserWrkjob> searchUserWrkjob() {
		return eamSqlSession.selectList("UserWorkSyncDao.searchUserWrkjob");
	}

	public int deleteKbcdUserWrkjob() {
		return sqlSession.delete("UserWorkSyncDao.deleteKbcdUserWrkjob");
	}

	public int insertKbcdUserWrkjob(UserWrkjob cd) {
		return sqlSession.insert("UserWorkSyncDao.insertKbcdUserWrkjob", cd);
	}

	public int insertKbcdUserWrkjobAtOnce(List<UserWrkjob> userWrkjobList) {
		return sqlSession.insert("UserWorkSyncDao.insertKbcdUserWrkjobAtOnce", userWrkjobList);
	}

	public int insertUserWrkjob() {
		return sqlSession.insert("UserWorkSyncDao.insertUserWrkjob");
	}

	public List<WrkJobCd> searchWorkJobCd2() throws Exception {
		logger.debug("iqmsJdbcDriverClass :" + iqmsJdbcDriverClass);
		logger.debug("iqmsJdbcUrl :" + iqmsJdbcUrl);
		logger.debug("iqmsJdbcUser :" + iqmsJdbcUser);
		//logger.debug("iqmsJdbcPassword :" + iqmsJdbcPassword);
		
		iqmsJdbcUser = desEnc(iqmsJdbcUser);
		iqmsJdbcPassword = desEnc(iqmsJdbcPassword);
		logger.debug("iqmsJdbcUser :" + iqmsJdbcUser);
		//logger.debug("iqmsJdbcPassword :" + iqmsJdbcPassword);
		
		Connection conn = null;
		Statement stmt = null;

		List<WrkJobCd> wrkJobCdList = new ArrayList<WrkJobCd>();
		// STEP 2: Register JDBC driver
		Class.forName(iqmsJdbcDriverClass);
		
		// STEP 3: Open a connection
		logger.debug("Connecting to database...");
		conn = DriverManager.getConnection(iqmsJdbcUrl, iqmsJdbcUser, iqmsJdbcPassword);
		
		// STEP 4: Execute a query
		logger.debug("Creating statement...");
		stmt = conn.createStatement();
		String sql;
		sql = "	SELECT 레벨 LVL, 단위업무코드 WRKJOB_CD , 상위단위업무코드 UPPER_WRKJOB_CD , 단위업무명 WRKJOB_CD_NM\r\n"
				+ "	 , 시작년월일 START_YMD, 종료년월일 END_YMD, 순서 LINE_UP, 단위업무표시명 WRKJOB_CD_DESC_NM \r\n"
				+ "	FROM INSTC.VSSKIHR57";
		sql = "SELECT 레벨, 단위업무코드, 상위단위업무코드, 단위업무명, 시작년월일, 종료년월일, 순서, 단위업무표시명 FROM INSTC.VSSKIHR57";
		ResultSet rs = stmt.executeQuery(sql);
		
		// STEP 5: Extract data from result set
		WrkJobCd wrkJobCd = null;
		while (rs.next()) {
			wrkJobCd = new WrkJobCd();
			// Retrieve by column name
			int level = rs.getInt("레벨");
			String wrkjob_cd = rs.getString("단위업무코드");
			String upper_wrkjob_cd = rs.getString("상위단위업무코드");
			String wrkjob_cd_nm = rs.getString("단위업무명");
			String start_ymd = rs.getString("시작년월일");
			String end_ymd = rs.getString("종료년월일");
			int line_up = rs.getInt("순서");
			String wrkjob_cd_desc_nm = rs.getString("단위업무표시명");
			
			wrkJobCd.setLvl(level);
			wrkJobCd.setWrkjob_cd(wrkjob_cd);
			wrkJobCd.setUpper_wrkjob_cd(upper_wrkjob_cd);
			wrkJobCd.setWrkjob_cd_nm(wrkjob_cd_nm);
			wrkJobCd.setStart_ymd(start_ymd);
			wrkJobCd.setEnd_ymd(end_ymd);
			wrkJobCd.setLine_up(line_up);
			wrkJobCd.setWrkjob_cd_desc_nm(wrkjob_cd_desc_nm);
			
			// Display values
			logger.debug("Level		:" + (level));
			logger.debug("Wrkjob_cd 		:" + (wrkjob_cd));
			logger.debug("Upper_wrkjob_cd 	:" + (upper_wrkjob_cd));
			logger.debug("Wrkjob_cd_nm 	:" + (wrkjob_cd_nm));
			logger.debug("Start_ymd 		:" + (start_ymd));
			logger.debug("End_ymd 		:" + (end_ymd));
			logger.debug("Line_up 		:" + (line_up));
			logger.debug("Wrkjob_cd_desc_nm 	:" + (wrkjob_cd_desc_nm));
			
			wrkJobCdList.add(wrkJobCd);
		}
		
		// STEP 6: Clean-up environment
		rs.close();
		stmt.close();
		conn.close();
		logger.debug(rs+" rs.closed");
		logger.debug(stmt+" stmt.closed");
		logger.debug(conn+" conn.closed");
		
		return wrkJobCdList;
	}
	
	public List<Users> searchUsers2() throws Exception {
		logger.debug("eamJdbcDriverClass :" + eamJdbcDriverClass);
		logger.debug("eamJdbcUrl :" + eamJdbcUrl);
		logger.debug("eamJdbcUser :" + eamJdbcUser);
		//logger.debug("eamJdbcPassword :" + eamJdbcPassword);
		
		eamJdbcUser = desEnc(eamJdbcUser);
		eamJdbcPassword = desEnc(eamJdbcPassword);
		logger.debug("eamJdbcUser :" + eamJdbcUser);
		//logger.debug("eamJdbcPassword :" + eamJdbcPassword);
		
		Connection conn = null;
		Statement stmt = null;
		List<Users> userList = new ArrayList<Users>();
		
		// STEP 2: Register JDBC driver
		Class.forName(eamJdbcDriverClass);
		
		// STEP 3: Open a connection
		logger.debug("Connecting to database...");
		conn = DriverManager.getConnection(eamJdbcUrl, eamJdbcUser, eamJdbcPassword);
		
		// STEP 4: Execute a query
		logger.debug("Creating statement...");
		stmt = conn.createStatement();
		String sql;
		sql = "		SELECT 한글부점명 BRANCH_NM\r\n" + 
				"	, 직원번호 USER_ID, 한글직원명 USER_NM, 한글인사직위구분명 GRADE_DIV_NM\r\n" + 
				"	, 팀장직원번호 SENIOR_USER_ID, 한글팀장명 SENIOR_USER_NM\r\n" + 
				"	, IT부서전입년월일 IN_YMD, IT부서전출년월일 OUT_YMD, 외주지원팀명 SUPPORT_TEAM_NM, 외주소속회사명 BELONG_COM_NM \r\n" + 
				"	FROM INSTC.VWWDBIT01";
		sql = "SELECT 한글부점명, 직원번호, 한글직원명, 한글인사직위구분명, 팀장직원번호, 한글팀장명, IT부서전입년월일, IT부서전출년월일, 외주지원팀명, 외주소속회사명  FROM INSTC.VWWDBIT01";
		ResultSet rs = stmt.executeQuery(sql);
		
		// STEP 5: Extract data from result set
		Users users = null;
		while (rs.next()) {
			users = new Users();
			// Retrieve by column name
			String branch_nm = rs.getString("한글부점명");
			String user_id = rs.getString("직원번호");
			String user_nm = rs.getString("한글직원명");
			String grade_div_nm = rs.getString("한글인사직위구분명");
			String senior_user_id = rs.getString("팀장직원번호");
			String senior_user_nm = rs.getString("한글팀장명");
			String in_ymd = rs.getString("IT부서전입년월일");
			String out_ymd = rs.getString("IT부서전출년월일");
			String support_team_nm = StringUtils.defaultString(rs.getString("외주지원팀명"),"");
			String belong_com_nm = StringUtils.defaultString(rs.getString("외주소속회사명"),"");
			
			users.setBranch_nm	(	branch_nm       );
			users.setUser_id(		user_id         );
			users.setUser_nm(		user_nm         );
			users.setGrade_div_nm(		grade_div_nm    );
			users.setSenior_user_id(	senior_user_id  );
			users.setSenior_user_nm(	senior_user_nm  );
			users.setIn_ymd(		in_ymd          );
			users.setOut_ymd(		out_ymd         );
			users.setSupport_team_nm(	support_team_nm );
			users.setBelong_com_nm(		belong_com_nm   );
			
			// Display values
			logger.debug("Branch_nm	:"+	branch_nm       );
			logger.debug("User_id:"+		user_id         );
			logger.debug("User_nm:"+		user_nm         );
			logger.debug("Grade_div_nm:"+		grade_div_nm    );
			logger.debug("Senior_user_id:"+	senior_user_id  );
			logger.debug("Senior_user_nm:"+	senior_user_nm  );
			logger.debug("In_ymd:"+		in_ymd          );
			logger.debug("Out_ymd:"+		out_ymd         );
			logger.debug("Support_team_nm:"+	support_team_nm );
			logger.debug("Belong_com_nm:"+		belong_com_nm   );
			
			userList.add(users);
		}
		
		// STEP 6: Clean-up environment
		rs.close();
		stmt.close();
		conn.close();			
		logger.debug(rs+" rs.closed");
		logger.debug(stmt+" stmt.closed");
		logger.debug(conn+" conn.closed");
		
		return userList;
	}
	
	public List<UserWrkjob> searchUserWrkjob2() throws Exception {
		logger.debug("eamJdbcDriverClass :" + eamJdbcDriverClass);
		logger.debug("eamJdbcUrl :" + eamJdbcUrl);
		logger.debug("eamJdbcUser :" + eamJdbcUser);
		//logger.debug("eamJdbcPassword :" + eamJdbcPassword);
		
		eamJdbcUser = desEnc(eamJdbcUser);
		eamJdbcPassword = desEnc(eamJdbcPassword);
		logger.debug("eamJdbcUser :" + eamJdbcUser);
		//logger.debug("eamJdbcPassword :" + eamJdbcPassword);
		
		Connection conn = null;
		Statement stmt = null;
		List<UserWrkjob> userWrkjobList = new ArrayList<UserWrkjob>();
		
		// STEP 2: Register JDBC driver
		Class.forName(eamJdbcDriverClass);
		
		// STEP 3: Open a connection
		logger.debug("Connecting to database...");
		conn = DriverManager.getConnection(eamJdbcUrl, eamJdbcUser, eamJdbcPassword);
		
		// STEP 4: Execute a query
		logger.debug("Creating statement...");
		stmt = conn.createStatement();
		String sql;
		sql = "SELECT 직원번호 USER_ID, 담당어플리케이션코드 CHARGE_APP_CD, 단위업무구분코드 WRKJOB_CD, 적용시작년월일 WORKJOB_START_DAY, 적용종료년월일 WORKJOB_END_DAY \r\n" + 
				"	FROM INSTC.VWWDBIT02";
		sql = "SELECT 직원번호, 담당어플리케이션코드, 단위업무구분코드, 적용시작년월일, 적용종료년월일 FROM INSTC.VWWDBIT02";
		ResultSet rs = stmt.executeQuery(sql);
		
		// STEP 5: Extract data from result set
		UserWrkjob userWrkjob = null;
		while (rs.next()) {
			userWrkjob = new UserWrkjob();
			// Retrieve by column name
			String user_id = rs.getString("직원번호");
			String charge_app_cd = rs.getString("담당어플리케이션코드");
			String wrkjob_cd = rs.getString("단위업무구분코드");
			String workjob_start_day = rs.getString("적용시작년월일");
			String workjob_end_day = rs.getString("적용종료년월일");
			
			userWrkjob.setUser_id	(	user_id       );
			userWrkjob.setCharge_app_cd(		charge_app_cd         );
			userWrkjob.setWrkjob_cd(wrkjob_cd);
			userWrkjob.setWorkjob_start_day(workjob_start_day);
			userWrkjob.setWorkjob_end_day(workjob_end_day);
			
			// Display values
			logger.debug("user_id	:"+	user_id       );
			logger.debug("charge_app_cd:"+		charge_app_cd         );
			logger.debug("wrkjob_cd:"+		wrkjob_cd         );
			logger.debug("workjob_start_day:"+		workjob_start_day    );
			logger.debug("workjob_end_day:"+	workjob_end_day  );
			userWrkjobList.add(userWrkjob);
		}
		
		// STEP 6: Clean-up environment
		rs.close();
		stmt.close();
		conn.close();
		logger.debug(rs+" rs.closed");
		logger.debug(stmt+" stmt.closed");
		logger.debug(conn+" conn.closed");
		
		return userWrkjobList;
	}

	public String desEnc(String data) throws Exception {
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
//		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("madeopen");
		if (StringUtil.isNotEmpty(data)) {
			if (data.startsWith("ENC(")) {
				data = data.substring("ENC(".length());
				data = data.substring(0, data.lastIndexOf(")"));
				data = pbeEnc.decrypt(data);
				return data;
			}
		} else {
			logger.debug("!!! The pass data err ");
			// System.exit(0);
		}

		return data;
	}

}
