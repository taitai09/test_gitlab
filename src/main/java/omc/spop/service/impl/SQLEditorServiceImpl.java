package omc.spop.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import omc.spop.model.Result;
import omc.spop.model.SQLEditor;
import omc.spop.service.SQLEditorService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2019.12.05	명성태	OPENPOP V2 최초작업
 **********************************************************/

@Service("SQLEditorService")
public class SQLEditorServiceImpl implements SQLEditorService {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLEditorServiceImpl.class);
	
	@Value("#{serverConfig['jdbc.driverClass']}")
	private String jdbcDriverClass;

	@Value("#{serverConfig['jdbc.url']}")
	private String jdbcUrl;

	@Value("#{serverConfig['jdbc.user']}")
	private String jdbcUser;

	@Value("#{serverConfig['jdbc.password']}")
	private String jdbcPassword;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	Connection _conn = null;
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		logger.debug("jdbcDriverClass :" + jdbcDriverClass);
		logger.debug("jdbcUrl :" + jdbcUrl);
		logger.debug("jdbcUser :" + jdbcUser);
		logger.debug("jdbcPassword :" + jdbcPassword);
		
		jdbcUser = desEnc(jdbcUser);
		jdbcPassword = desEnc(jdbcPassword);
		
		Class.forName(jdbcDriverClass);
		
		return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
	}
	
	@Override
	public Result disConn(SQLEditor sqlEditor) {
		Result result = new Result();
		
		if(_conn != null) {
			try {
				_conn.rollback();
				_conn.close();
				
				_conn = null;
				
				sqlEditor.setConn_info("");
				
				result.setResult(true);
				result.setMessage("Success to close the connection.");
				logger.debug("Success to close the connection.");
			} catch(SQLException sqle) {
				result.setResult(false);
				result.setMessage("Failed to close the connection.");
				logger.error("Failed to close the connection.");
			}
		} else {
			result.setResult(true);
			result.setMessage("Connection information is missing.");
			logger.debug("Connection information is missing.");
		}
		
		return result;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> retrieve(SQLEditor sqlEditor) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		SqlSession session = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSetMetaData metaData = null;
		ResultSet rs = null;
		boolean isExecute = false;
		
		try {
			if(_conn == null) {
				session = sqlSessionFactory.openSession();
				conn = session.getConnection();
				conn.setAutoCommit(false);
				
				_conn = conn;
			}
			
			sqlEditor.setConn_info(_conn + "");
			logger.debug("_conn[" + _conn + "]");
			
			//pstmt = conn.prepareStatement(sqlEditor.getSql_text());
			pstmt = _conn.prepareStatement(sqlEditor.getSql_text());
			isExecute = pstmt.execute();
			
			if(isExecute) {
				// ResultSet
				resultList = getResultSet(sqlEditor, pstmt, metaData, rs);
			} else {
				// update count
				resultList = getUpdateCount(sqlEditor, pstmt);
//				conn.commit();
			}
		} catch(Exception ex) {
//			if(conn != null) {
//				try {
//					conn.rollback();
//				} catch(SQLException sqle) {}
//			}
			
			logger.error("JDBC Exception.\n" + ex);
			
			throw ex;
		} finally {
			if(rs != null) {
				rs.close();
			}
			
			if(pstmt != null) {
				pstmt.close();
			}
			
//			if(conn != null) {
//				conn.close();
//			}
		}
		
		return resultList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownRetrieve(SQLEditor sqlEditor) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		SqlSession session = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSetMetaData metaData = null;
		ResultSet rs = null;
		boolean isExecute = false;
		
		try {
			session = sqlSessionFactory.openSession();
			conn = session.getConnection();
			
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sqlEditor.getSql_text());
			isExecute = pstmt.execute();
			
			if(isExecute) {
				// ResultSet
				resultList = getResultSet(sqlEditor, pstmt, metaData, rs);
			} else {
				// update count
				resultList = getUpdateCount(sqlEditor, pstmt);
				conn.commit();
			}
		} catch(Exception ex) {
			if(conn != null) {
				try {
					conn.rollback();
				} catch(SQLException sqle) {}
			}
			
			logger.error("JDBC Exception.\n" + ex);
			
			throw ex;
		} finally {
			if(rs != null) {
				rs.close();
			}
			
			if(pstmt != null) {
				pstmt.close();
			}
			
			if(conn != null) {
				conn.close();
			}
		}
		
		return resultList;
	}
	
	private List<LinkedHashMap<String, Object>> getResultSet(SQLEditor sqlEditor, PreparedStatement pstmt, ResultSetMetaData metaData, ResultSet rs) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> resultMeta = null;
		LinkedHashMap<String, Object> resultData = null;
		String clmLabel = "";
		String clmValue = "";
		String clmTypeName = "";
		int clmCount = -1;
		int rownum = 0;
		int rsCount = 0;
		LinkedHashMap<String, Object> clmTypeMap = new LinkedHashMap<String, Object>();
		String tmpType = "";
		
		try {
			rs = pstmt.getResultSet();
			metaData = rs.getMetaData();
			clmCount = metaData.getColumnCount();
			rownum = sqlEditor.getRownum();
			
			resultMeta = new LinkedHashMap<String, Object>();
			String head = "";
			
			logger.debug("clmCount[" + clmCount + "]");
			for(int clmIndex = 1; clmIndex <= clmCount; clmIndex++) {
				clmLabel = metaData.getColumnLabel(clmIndex);
				clmTypeName = metaData.getColumnTypeName(clmIndex);
				
				clmTypeMap.put(clmLabel, clmTypeName);
				
				head += clmLabel + "/" + clmTypeName + "/AUTO";
				
				if(clmIndex < clmCount) {
					head += ";";
				}
			}
			
			logger.debug("head[" + head + "]");
			resultMeta.put("HEAD", head);
			
			while(rs.next()) {
				if(rsCount == rownum) {
					break;
				}
				
				resultData = new LinkedHashMap<String, Object>();
				
				for(int clmIndex = 1; clmIndex <= clmCount; clmIndex++) {
					clmLabel = metaData.getColumnLabel(clmIndex);
					clmValue = rs.getString(clmLabel);
					
					if(clmValue == null) {
						clmValue = "";
					}
					
					if(clmTypeMap.containsKey(clmLabel) && (clmTypeMap.get(clmLabel) + "").equalsIgnoreCase("NUMBER") && clmValue.indexOf(".") == 0) {
						clmValue = "0" + clmValue;
					}
					
					resultData.put(clmLabel, clmValue);
				}
				
				resultList.add(resultData);
				
				rsCount++;
			}
			
			resultList.add(resultMeta);
			
			resultData = new LinkedHashMap<String, Object>();
			
			resultData.put("CONN_INFO", sqlEditor.getConn_info());
			resultList.add(resultData);
		} catch(Exception ex) {
			throw ex;
		}
		
		return resultList;
	}
	
	private List<LinkedHashMap<String, Object>> getUpdateCount(SQLEditor sqlEditor, PreparedStatement pstmt) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> resultData = null;
		int updateCount = -1;
		
		try {
			resultData = new LinkedHashMap<String, Object>();
			updateCount = pstmt.getUpdateCount();
			
			if(sqlEditor.getSql_text().equalsIgnoreCase("commit")) {
				resultData.put("INFO", "Commit이 반영되었습니다.");
			} else if(sqlEditor.getSql_text().equalsIgnoreCase("rollback")) {
				resultData.put("INFO", "Rollback이 반영되었습니다.");
			} else {
				resultData.put("INFO", updateCount + "건이 반영되었습니다.");
			}
			
			resultList.add(resultData);
			
			resultData.put("HEAD", "INFO/center/AUTO");
			resultList.add(resultData);
			
			resultData = new LinkedHashMap<String, Object>();
			
			resultData.put("CONN_INFO", sqlEditor.getConn_info());
			resultList.add(resultData);
		} catch(Exception ex) {
			throw ex;
		}
		
		return resultList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> executeQuery(SQLEditor sqlEditor) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> resultData = null;
		
		SqlSession session = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSetMetaData metaData = null;
		ResultSet rs = null;
		String clmLabel = "";
		String clmTypeName = "";
		int clmCount = -1;
		
		try {
			session = sqlSessionFactory.openSession();
			conn = session.getConnection();
			
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sqlEditor.getSql_text());
			rs = pstmt.executeQuery();
			metaData = rs.getMetaData();
			clmCount = metaData.getColumnCount();
			
			while(rs.next()) {
				resultData = new LinkedHashMap<String, Object>();
				
				for(int clmIndex = 1; clmIndex <= clmCount; clmIndex++) {
					clmLabel = metaData.getColumnLabel(clmIndex);
					
					resultData.put(clmLabel, rs.getString(clmLabel));
				}
				
				resultList.add(resultData);
			}
			
			resultData = new LinkedHashMap<String, Object>();
			String head = "";
			
			for(int clmIndex = 1; clmIndex <= clmCount; clmIndex++) {
				clmLabel = metaData.getColumnLabel(clmIndex);
				clmTypeName = metaData.getColumnTypeName(clmIndex);
				
				head += clmLabel + "/" + clmTypeName + "/AUTO";
				
				if(clmIndex < clmCount) {
					head += ";";
				}
			}
			
			resultData.put("HEAD", head);
			resultList.add(resultData);
		} catch(Exception ex) {
			if(conn != null) {
				try {
					conn.rollback();
				} catch(SQLException sqle) {}
			}
			
			logger.error("JDBC Exception.\n" + ex);
			
			throw ex;
		} finally {
			if(rs != null) {
				rs.close();
			}
			
			if(pstmt != null) {
				pstmt.close();
			}
			
			if(conn != null) {
				conn.close();
			}
		}
		
		return resultList;
	}
	
	public Result executeUpdate(SQLEditor sqlEditor) throws Exception {
		SqlSession session = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int updateCount = -1;
		Result result = new Result();
		
		try {
			session = sqlSessionFactory.openSession();
			conn = session.getConnection();
			
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(sqlEditor.getSql_text());
			updateCount = pstmt.executeUpdate();
			result.setMessage(updateCount + "건이 반영되었습니다.");
			result.setResult(true);
			conn.commit();
		} catch(Exception ex) {
			if(conn != null) {
				try {
					conn.rollback();
				} catch(SQLException sqle) {}
			}
			
			result.setResult(false);
			logger.error("JDBC Exception.\n" + ex);
			
			throw ex;
		} finally {
			if(pstmt != null) {
				pstmt.close();
			}
			
			if(conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	public Result execute(SQLEditor sqlEditor) throws Exception {
		SqlSession session = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isExecute = false;
		Result result = new Result();
		
		try {
			session = sqlSessionFactory.openSession();
			conn = session.getConnection();
			pstmt = conn.prepareStatement(sqlEditor.getSql_text());
			isExecute = pstmt.execute();
			
			if(isExecute) {
				result.setResult(true);
				result.setMessage("반영되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("반영되지 않았습니다.");
			}
		} catch(Exception ex) {
			result.setResult(false);
			logger.error("JDBC Exception.\n" + ex);
			
			throw ex;
		} finally {
			if(pstmt != null) {
				pstmt.close();
			}
			
			if(conn != null) {
				conn.close();
			}
		}
		
		return result;
	}
	
	private String desEnc(String data) {
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
//		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("madeopen");	// It's key.
		try {
			if (StringUtil.isNotEmpty(data)) {
				if (data.startsWith("ENC(")) {
					data = data.substring("ENC(".length());
					data = data.substring(0, data.lastIndexOf(")"));
					data = pbeEnc.decrypt(data);
					return data;
				}
			} else {
				logger.debug("!!! The data is Empty");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("!!! The pass data err. Exception:\n" + e);
		}
		
		return data;
	}
}
