package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import omc.spop.base.SessionManager;
import omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao;
import omc.spop.dao.CommonDao;
import omc.spop.dao.ImprovementManagementDao;
import omc.spop.dao.SQLInformationDao;
import omc.spop.dao.SQLTuningTargetDao;
import omc.spop.dao.SqlsDao;
import omc.spop.model.DatabaseTuner;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.TuningTargetSql;
import omc.spop.server.tools.SPopTools;
import omc.spop.server.tune.ProjectPerfChk;
import omc.spop.server.tune.ProjectPerfChkSqlProfile;
import omc.spop.service.AutoPerformanceCompareBetweenDbService;
import omc.spop.utils.DBUtil;
import omc.spop.utils.ExcelDownHandler;
import omc.spop.utils.StringUtil;

@Service("AutoPerformanceCompareBetweenDbService")
public class AutoPerformanceCompareBetweenDbServiceImpl implements AutoPerformanceCompareBetweenDbService {
	private static final Logger logger = LoggerFactory.getLogger(
			AutoPerformanceCompareBetweenDbServiceImpl.class );

	@Autowired
	private AutoPerformanceCompareBetweenDbServiceDao autoPerformanceCompareBetweenDbServiceDao;

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private SqlsDao sqlsDao;
	
	@Autowired
	private SQLInformationDao sqlInformationDao;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private SQLTuningTargetDao sqlTuningTargetDao;

	@Autowired
	private ImprovementManagementDao improvementManagementDao;

	@Override
	public List<SQLAutoPerformanceCompare> getSqlPerfPacName(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		
		if ( sqlAutoPerformanceCompare.getPerf_check_type_cd() == null || "".equals(sqlAutoPerformanceCompare.getPerf_check_type_cd() ) ) {
			sqlAutoPerformanceCompare.setPerf_check_type_cd("1");
		}
		logger.debug("perf_check_type_cd =============================> "+sqlAutoPerformanceCompare.getPerf_check_type_cd() );
		
		List<SQLAutoPerformanceCompare> resultList = autoPerformanceCompareBetweenDbServiceDao.getSqlPerfPacName( sqlAutoPerformanceCompare );
		try {
			for ( SQLAutoPerformanceCompare sqlCom : resultList ) {
				String sqlPerfPac = sqlCom.getPerf_check_name();
				
				if ( sqlPerfPac != null && "".equals( sqlPerfPac ) == false ) {
					sqlPerfPac = removeHTML( sqlPerfPac );
					sqlPerfPac = removeSpecialChar( sqlPerfPac );
					//sqlPerfPac = unescapeHtml( sqlPerfPac );
					
					sqlCom.setPerf_check_name( sqlPerfPac.toString() );
				}
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultList;
	}

	@Override
	public List<SQLAutoPerformanceCompare> getSqlPerfDetailInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.getSqlPerfDetailInfo( sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> getSqlPerformanceInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {

		List<SQLAutoPerformanceCompare> resultList = autoPerformanceCompareBetweenDbServiceDao.getSqlPerformanceInfo( sqlAutoPerformanceCompare );
		try {
			for ( SQLAutoPerformanceCompare sqlCom : resultList ) {
				String sqlPerfPac = sqlCom.getPerf_check_name();
				
				if ( sqlPerfPac != null && "".equals( sqlPerfPac ) == false ) {
					sqlPerfPac = removeHTML( sqlPerfPac );
					sqlPerfPac = removeSpecialChar( sqlPerfPac );
					//sqlPerfPac = unescapeHtml( sqlPerfPac );
					
					sqlCom.setPerf_check_name( sqlPerfPac.toString() );
				}
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultList;
		
	}

	@Override
	public int insertSqlPerformanceInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.insertSqlPerformanceInfo( sqlAutoPerformanceCompare );
	}

	@Override
	public int updateSqlPerformanceInfo(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.updateSqlPerformanceInfo( sqlAutoPerformanceCompare );
	}

	@Override
	public int countExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) {
		return autoPerformanceCompareBetweenDbServiceDao.countExecuteTms( sqlAutoPerformanceCompare );
	}

	@Override
	public int countPerformanceRecord(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.countPerformanceRecord( sqlAutoPerformanceCompare );
	}

	@Override
	public Result updateSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		Result result = new Result();
		String filterSql = "";
		
		try {
			filterSql = sqlAutomaticPerformanceCheck.getExtra_filter_predication();
			
			if( filterSql != null && filterSql.isEmpty() == false ) {
				filterSql = StringUtil.removeSpecialChar( filterSql );
				sqlAutomaticPerformanceCheck.setExtra_filter_predication(filterSql);
			}
			
			List<String> ownerStrList = new ArrayList<String>();
			List<String> moduleStrList = new ArrayList<String>();
			
			if ( sqlAutomaticPerformanceCheck.getOwner_list().indexOf(',') != -1 ) {
				String[] strOwnerArr = sqlAutomaticPerformanceCheck.getOwner_list().split(",");
				
				for ( String ownerStr : strOwnerArr ) {
					if ( ownerStr.trim().equals("") ) {
						result.setMessage("OWNER 값을 확인 후 다시 성능비교 를 실행해 주세요.");
						result.setResult(false);
						return result;
					} else {
						ownerStrList.add( ownerStr.trim() );
					}
				}
			}
			
			sqlAutomaticPerformanceCheck.setStrOwnerList( ownerStrList );
			
			if ( sqlAutomaticPerformanceCheck.getModule_list().indexOf(',') != -1 ) {
				String[] strModuleArr = sqlAutomaticPerformanceCheck.getModule_list().split(",");
				
				for ( String moduleStr : strModuleArr ) {
					if ( moduleStr.trim().equals("") ) {
						result.setMessage("MODULE 값을 확인 후 다시 성능비교 를 실행해 주세요.");
						result.setResult(false);
						return result;
					} else {
						moduleStrList.add( moduleStr.trim() );
					}
				}
			}
			
			sqlAutomaticPerformanceCheck.setStrModuleList( moduleStrList );
			
			logger.debug(
					"SQL_TIME_LIMIT ======>" + sqlAutomaticPerformanceCheck.getSql_time_limt_cd()+" , "+sqlAutomaticPerformanceCheck.getSql_time_direct_pref_value()
					);
			
			if ( "99".equals( sqlAutomaticPerformanceCheck.getSql_time_direct_pref_value() ) ) {
				if ( "'99'".equals( sqlAutomaticPerformanceCheck.getSql_time_limt_cd() ) ) {
					sqlAutomaticPerformanceCheck.setSql_time_direct_pref_value( "99" );
				} else {
					sqlAutomaticPerformanceCheck.setSql_time_direct_pref_value( sqlAutomaticPerformanceCheck.getSql_time_limt_cd() );
				}
				logger.debug("===============직접입력============"+sqlAutomaticPerformanceCheck.getSql_time_direct_pref_value() );
				sqlAutomaticPerformanceCheck.setSql_time_limt_cd( "99" );
			}
			
			int successResult = autoPerformanceCompareBetweenDbServiceDao.updateSqlAutoPerformance(
					sqlAutomaticPerformanceCheck );
			
			if ( successResult > 0 ) {
				result.setResult(true);
				result.setMessage("SQL_AUTO_PERF_CHK에 등록되었습니다.");
				
				SQLAutoPerformanceCompare sqlAutoPerformanceCompare = new SQLAutoPerformanceCompare();
				sqlAutoPerformanceCompare.setProject_id( sqlAutomaticPerformanceCheck.getProject_id() );
				sqlAutoPerformanceCompare.setSql_auto_perf_check_id( sqlAutomaticPerformanceCheck.getSql_auto_perf_check_id() );
				
				autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceTarget(
						sqlAutoPerformanceCompare );
						
				autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceResult(
						sqlAutoPerformanceCompare );
						
				autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceError(
						sqlAutoPerformanceCompare );
						
				autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformancePlanTable(
						sqlAutoPerformanceCompare );
						
				autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceStat(
						sqlAutoPerformanceCompare );
						
				autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformancePlan(
						sqlAutoPerformanceCompare );
				
				autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceBind(
						sqlAutoPerformanceCompare );
				
				autoPerformanceCompareBetweenDbServiceDao.deleteTableCHGPerfTargetSql(
						sqlAutoPerformanceCompare );
				
				autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerfChkBindPlan(
						sqlAutoPerformanceCompare );
				
				if ( sqlAutomaticPerformanceCheck.getPerf_check_sql_source_type_cd().equals("2") ) {
					successResult = autoPerformanceCompareBetweenDbServiceDao.insertSqlAutoPerformanceTarget(
						sqlAutomaticPerformanceCheck );
				} else {
					successResult = autoPerformanceCompareBetweenDbServiceDao.insertSqlAutoPerformanceTargetForAWR(
							sqlAutomaticPerformanceCheck );
				}
				
				logger.debug("result 수 ====> "+ successResult);
				result.setTxtValue( String.valueOf(successResult) );
			} else {
				result.setResult(false);
				result.setMessage("성능비교를 실행하지 못했습니다. <br>조건 값을 다시 확인해주세요.");
			}
		} catch (BadSqlGrammarException bex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + bex.getMessage());
			result.setResult(false);
			result.setMessage("실행오류가 발생하였습니다.<br>필터링조건을 다시 확인해주세요.");
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			
			throw new Exception(ex);
			
		}finally {
			filterSql = null;
		}
		
		return result;
	}

	@Override
	public Result performanceCompareCall(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		try {
			String projectId = sqlAutomaticPerformanceCheck.getProject_id();
			String sqlAutoPerfCheckId = sqlAutomaticPerformanceCheck.getSql_auto_perf_check_id();
			
			String jsonResult = ProjectPerfChk.getPerfChk( Long.valueOf(projectId), Long.valueOf(sqlAutoPerfCheckId) );
		} catch (InterruptedException e) {
			if ( e.getMessage().equals("sleep interrupted") ) {
				logger.error(" 강제완료 complate force ");
			} else {
				throw new Exception( e );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			throw new Exception(ex);
		}
		return null;
	}

	@Override
	public Result performanceCompareReCall( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		Result result = new Result();
		
		JSONArray jArray = null;
		JSONObject jObj = null;
		try {
			jArray = new JSONArray();
			String projectId = sqlAutoPerformanceCompare.getProject_id();
			String sqlAutoPerfCheckId = sqlAutoPerformanceCompare.getSql_auto_perf_check_id();
			
			String[] sqlIdArr = sqlAutoPerformanceCompare.getSqlIdList().split(",");
			
			for ( String str : sqlIdArr ) {
				jObj = new JSONObject();
				jObj.put("sqlId", str);
				jArray.add(jObj);
			}
			
			String jsonResult = ProjectPerfChk.getPerfChkSelectionSQL( Long.valueOf(projectId), Long.valueOf(sqlAutoPerfCheckId), jArray );
			logger.debug("jsonResult ========> "+ jsonResult );
			JSONObject rtnJson = strToJSONObject(jsonResult);
			
			String err_msg = (String)rtnJson.get("err_msg");
			String is_error = (String)rtnJson.get("is_error");
			logger.debug("err_msg ===> ["+err_msg+"] , is_error ===> ["+is_error+"]"+ ("Complete".equals(err_msg)) +" , "+("false".equals(is_error)) );
			
			if ( err_msg != null && "Complete".equals(err_msg)&&
					is_error != null && "false".equals(is_error) ) {
				result.setResult( true );
				result.setMessage( jsonResult );
			} else {
				result.setResult( false );
				result.setMessage( "재실행 중 오류가 발생했습니다.<br>Open-POP 관리자에게 문의하세요!" );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			throw new Exception(ex);
		}
		return result;
	}

	@Override
	public Result updateTuningSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception {
		Result result = new Result();
		
		try {
			
			if ( "".equals( sqlAutomaticPerformanceCheck.getSql_time_limt_cd() ) ) {
				sqlAutomaticPerformanceCheck.setSql_time_limt_cd( "99" );
			}
			
			result.setResult(true);
			result.setMessage("SQL_AUTO_PERF_CHK에 등록되었습니다.");
			
//			if ( sqlAutomaticPerformanceCheck.getVerify_sql_auto_perf_check_id() != null &&
//					!"".equals(sqlAutomaticPerformanceCheck.getVerify_sql_auto_perf_check_id()) ) {
				// Table 삭제
				deleteAutoPerfChkTables( sqlAutomaticPerformanceCheck.getVerify_project_id()
										, sqlAutomaticPerformanceCheck.getVerify_sql_auto_perf_check_id()
										, sqlAutomaticPerformanceCheck.getDatabase_kinds_cd() );
//			}
			
			// SQL-30 SQL_AUTO_PERF_CHK INSERT(튜닝SQL 일괄검증)-신규
			int successResult = autoPerformanceCompareBetweenDbServiceDao.insertTuningSqlAutoPerfChk(
					sqlAutomaticPerformanceCheck );
			
			// 마스터에서 parent_project_id & parent_sql_auto_perf_check_id 를 업데이트한 후 다시 불러오는 과정.
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare = 
					autoPerformanceCompareBetweenDbServiceDao.getVeritySqlAutoPerfCheckId( sqlAutomaticPerformanceCheck );
			
			sqlAutomaticPerformanceCheck.setVerify_project_id( sqlAutoPerformanceCompare.getProject_id() );
			sqlAutomaticPerformanceCheck.setVerify_sql_auto_perf_check_id( sqlAutoPerformanceCompare.getSql_auto_perf_check_id() );
			logger.debug("Verify(child) ===========================> "+sqlAutoPerformanceCompare.getProject_id()
							+" , "+sqlAutoPerformanceCompare.getSql_auto_perf_check_id()
							+" , Parent ==> "+sqlAutomaticPerformanceCheck.getSql_auto_perf_check_id());
			
			successResult = autoPerformanceCompareBetweenDbServiceDao.insertTuningSqlAutoPerfChkTarget(
					sqlAutomaticPerformanceCheck );
			
			logger.debug("result 수 ====> "+ successResult);
			
			String projectId = sqlAutomaticPerformanceCheck.getVerify_project_id();
			String sqlAutoPerfCheckId = sqlAutomaticPerformanceCheck.getVerify_sql_auto_perf_check_id();
			
			String jsonResult = ProjectPerfChk.getPerfChk( Long.valueOf(projectId), Long.valueOf(sqlAutoPerfCheckId) );
			
		} catch (InterruptedException e) {
			if ( e.getMessage().equals("sleep interrupted") ) {
				logger.error(" 강제완료 complate force ");
				result.setResult( true );
			} else {
				throw new Exception( e );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
			ex.printStackTrace();
			
			throw new Exception(ex);
		}
		
		return result;
	}

	@Override
	public SQLAutoPerformanceCompare getMaxSqlCheckId(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.getMaxSqlCheckId(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadPerformanceResultList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			sqlAutoPerformanceCompare.setSql_id(" ");
			resultList = autoPerformanceCompareBetweenDbServiceDao.loadPerformanceResultList(
				sqlAutoPerformanceCompare );
			
			for ( int sqlIdx = 0; sqlIdx < resultList.size(); sqlIdx++ ) {
				String sqlText = resultList.get(sqlIdx).getSql_text_web();
				
				if ( sqlText != null && "".equals( sqlText ) == false ) {
					sqlText = removeHTML(sqlText);
					sqlText = removeSpecialChar(sqlText);
//					sqlText = unescapeHtml( sqlText );
					
					if ( sqlText.length() > 40 ) {
						resultList.get(sqlIdx).setSql_text_web( sqlText.substring(0, 40) );
					} else {
						resultList.get(sqlIdx).setSql_text_web( sqlText );
					}
				}
				
				if ( resultList.get(sqlIdx).getSql_profile_nm() != null &&
						!"".equals(resultList.get(sqlIdx).getSql_profile_nm()) &&
							resultList.get(sqlIdx).getSql_profile_nm().length() > 30) {
					resultList.get(sqlIdx).setSql_profile_nm(
							resultList.get(sqlIdx).getSql_profile_nm().substring(0,30) );
				}
				
			}
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultList;
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadPerfResultCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadPerfResultCount(
				sqlAutoPerformanceCompare );
	}

	@Override
	public boolean excelDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.excelDownload";
		String sql = "";
		
		try {
			handler.open();
			
			handler.buildInit("성능_영향도_분석_결과", "SQL_AUTO_PERF_COMP");
			handler.addRemoveHTML( new String[] { "SQL_TEXT_EXCEL" } );
			
			sqlAutoPerformanceCompare.setSql_id(" ");
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			handler.buildDocument(sqlSessionFactory, sql);
				
			handler.writeDoc();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			resultSuccess = false;
			
		}finally {
			handler.close();
		}
		
		return resultSuccess;
	}

	@Override
	public List<SQLAutomaticPerformanceCheck> loadPerfChartData(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadPerfChartData(
				sqlAutomaticPerformanceCheck );
	}

	@Override
	public int updateAutoPerfChkIsNull(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.updateAutoPerfChkIsNull(
				sqlAutoPerformanceCompare );
	}

	@Override
	public int getPerformanceResultCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
			sqlAutoPerformanceCompare.setSql_id(" ");
		return autoPerformanceCompareBetweenDbServiceDao.getPerformanceResultCount(
				sqlAutoPerformanceCompare );
	}

	@Override
	public int getTargetEqualCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.getTargetEqualCount(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutomaticPerformanceCheck> loadPerformanceCheckCount(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadPerformanceCheckCount(sqlAutomaticPerformanceCheck);
	}

	// ( DB변경성능영향도분석 / 테이블변경성능영향도분석 ) 성능영향도분석결과 > 튜닝대상선정
	@Override
	public Result insertTuningRequest( TuningTargetSql tuningTargetSql ) throws Exception {
		Result result = new Result();
		DatabaseTuner tuner = new DatabaseTuner();
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String wrkjob_cd = StringUtil.nvl(SessionManager.getLoginSession().getUsers().getWrkjob_cd(),"");
		String ext_no = SessionManager.getLoginSession().getUsers().getExt_no();
		
		List<DatabaseTuner> tunerList = new ArrayList<DatabaseTuner>();
		List<TuningTargetSql> perfrIdList = new ArrayList<TuningTargetSql>();
		
		TuningTargetSql temp = null;
		OdsHistSqlText odsText = null;
		
		String[] sqlIdArry = null;
		String[] moduleArry = null;
		String[] parsingSchemaNameArry = null;
		String[] asisPlanHashValueArry = null;
		String[] tobePlanHashValueArry = null;
		
		StringBuffer strBuf = null;
		List<OdsHistSqlText> odsList = null;
		List<String> listStr = null;
		
		String auto_tuner = "";
		String choiceTms = "";
		String tuningNo = "";
		String tuningNoArray = "";
		int tunerCnt = 0;
		
		try {
			// 1. 자동선정일 경우 Tuner 조회
			if ( tuningTargetSql.getAuto_share().equals("Y") ) {
				tuner.setDbid(tuningTargetSql.getDbid());
				tunerList = commonDao.getTuner(tuner);
				
				tunerCnt = tunerList.size();
			}
			
			// 성능 영향도 분석 결과 튜닝대상선정
			logger.debug("##### 성능 영향도 분석 결과 튜닝대상선정으로 진행 - project_id :" +tuningTargetSql.getProject_id() + ",Sql_auto_perf_check_id :"+ tuningTargetSql.getSql_auto_perf_check_id() );
			sqlIdArry = StringUtil.split( tuningTargetSql.getSqlIdArry(), "|" );
			moduleArry = StringUtil.split( tuningTargetSql.getModule(), "|" );
			parsingSchemaNameArry = StringUtil.split( tuningTargetSql.getParsing_schema_name(), "|" );
			asisPlanHashValueArry = StringUtil.split( tuningTargetSql.getAsisPlanHashValueArry(), "|" );
			tobePlanHashValueArry = StringUtil.split( tuningTargetSql.getPlanHashValueArry(), "|" );
			
			for ( int sqlIdx = 0; sqlIdx < sqlIdArry.length; sqlIdx++ ) {
				temp = new TuningTargetSql();
				odsText = new OdsHistSqlText();
				
				temp.setProject_id( tuningTargetSql.getProject_id() );
				temp.setSql_id( sqlIdArry[sqlIdx].substring( sqlIdArry[sqlIdx].lastIndexOf(']')+1 ) );
				temp.setDatabase_kinds_cd( tuningTargetSql.getDatabase_kinds_cd() );
				
				int cnt = 0;
				
				if ( "J".equals(tuningTargetSql.getChoice_div_cd()) ) {
					// 테이블 변경 성능 영향도 분석
					temp.setTuning_status_change_why("테이블 변경 성능 영향도 분석 튜닝대상선정 및 접수");
					logger.debug("#######테이블 변경 성능 영향도 분석 튜닝대상선정 및 접수########");
				} else {
					// DB변경 성능 영향도 분석
					cnt = autoPerformanceCompareBetweenDbServiceDao.getDuplicateSQLTuningTargetByProjectCount( temp );
					temp.setTuning_status_change_why("DB 변경 성능 영향도 분석 튜닝대상선정 및 접수");
					logger.debug("#######DB 변경 성능 영향도 분석 튜닝대상선정 및 접수########");
				}
				
				if ( tuningTargetSql.getChkExcept() != null && "".equals(tuningTargetSql.getChkExcept()) == false  && cnt > 0 ) {
					continue;
				} else {
					temp.setModule( moduleArry[sqlIdx].substring( moduleArry[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setParsing_schema_name( parsingSchemaNameArry[sqlIdx].substring(parsingSchemaNameArry[sqlIdx].lastIndexOf(']')+1 ) );
					
					if ( tuningTargetSql.getAuto_share().equals("Y") ) {
						logger.debug("##### 튜닝담당자 자동 할당 #####");
						auto_tuner = tunerList.get(sqlIdx%tunerCnt).getTuner_id();
					} else {
						auto_tuner = tuningTargetSql.getPerfr_id();
					}
					
					// 4-1. TOPSQL_HANDOP_CHOICE_EXEC MAX CHOICE_TMS & TUNING_TARGET_SQL MAX TUNING_NO 조회
					choiceTms = sqlTuningTargetDao.getMaxChoiceTms( tuningTargetSql );
					tuningNo = improvementManagementDao.getNextTuningNo();
					
					temp.setTuning_no( tuningNo );
					temp.setChoice_tms( choiceTms );
					temp.setPerfr_id( auto_tuner );
					
					temp.setDbid( tuningTargetSql.getDbid() );
					temp.setTuning_requester_id( user_id );
					temp.setTuning_requester_wrkjob_cd( wrkjob_cd );
					temp.setTuning_requester_tel_num( ext_no );
					temp.setChoice_div_cd( tuningTargetSql.getChoice_div_cd());
					
					temp.setSql_auto_perf_check_id( tuningTargetSql.getSql_auto_perf_check_id() );
					temp.setTuning_status_cd( tuningTargetSql.getTuning_status_cd());
					
					tuningNoArray += tuningNo + ",";
					
					// 4-2. TUNING_TARGET_SQL INSERT
					autoPerformanceCompareBetweenDbServiceDao.insertTuningTargetSql( temp );
					
					// 5. SQL_TUNING_STATUS_HISTORY INSERT
					temp.setTuning_status_changer_id( user_id );
					
					improvementManagementDao.insertTuningStatusHistory( temp );
					
					// 바이드값 삭제
					autoPerformanceCompareBetweenDbServiceDao.deleteTuningTargetSqlBind( tuningNo );
					// tuningNO로 Bind값 MAX순번 가져오기
					temp.setBind_set_seq( autoPerformanceCompareBetweenDbServiceDao.getBindSetSeq( tuningNo ) );
					// 신규 바인드 추가.
					//					autoPerformanceCompareBetweenDbServiceDao.insertTuningTargetSqlBindFromVsqlBindCapture( temp );
					autoPerformanceCompareBetweenDbServiceDao.insertTuningTargetSqlBindNew( temp );
					
					odsText.setSql_id( sqlIdArry[sqlIdx].substring( sqlIdArry[sqlIdx].lastIndexOf(']')+1 ) );
					odsText.setPlan_hash_value( asisPlanHashValueArry[sqlIdx].substring( asisPlanHashValueArry[sqlIdx].lastIndexOf(']')+1 ) );
					odsText.setDbid( tuningTargetSql.getDbid() );
					
					if ( tuningTargetSql.getPerf_check_sql_source_type_cd().equals("1") ) {
						//AWR
						odsList = sqlInformationDao.sqlTextPlanList( odsText );
					} else {
						//VSQL
						odsList = sqlInformationDao.sqlTextPlanListAll( odsText );
					}
					
					strBuf = new StringBuffer();
					//if( odsList != null && odsList.get(0).getExecution_plan() != null ) {
					if( odsList != null && odsList.size() > 0) {
						if( odsList.get(0).getExecution_plan() != null ) {
							strBuf.append("[ASIS Plan]").append("\r\n");
							strBuf.append("-----------------------------------------------------------------------------------------------------------").append("\r\n");
							
							for (OdsHistSqlText odsSqlText : odsList) {
								strBuf.append( odsSqlText.getExecution_plan() ).append("\r\n");
							}
							
							strBuf.append("-----------------------------------------------------------------------------------------------------------");
							logger.debug("ASIS getExecutions ==============>\r\n"+strBuf.toString());
						}
					}
					temp.setExecutions( strBuf.toString() );
					
					strBuf.setLength(0);
					
					String tobePlanHashValue = tobePlanHashValueArry[sqlIdx].substring( tobePlanHashValueArry[sqlIdx].lastIndexOf(']')+1 ).trim();
					
					if ( tobePlanHashValue != null && "".equals( tobePlanHashValue ) == false && "null".equals( tobePlanHashValue ) == false) {
						temp.setPlan_hash_value( tobePlanHashValue );
						
						int planCnt = autoPerformanceCompareBetweenDbServiceDao.getTobeSQLPlanCnt(temp);
						
						if ( planCnt > 0 ) {
							
							strBuf.append("\n").append("\r\n").append("\r\n").append("[TOBE Plan]").append("\r\n");
							listStr = autoPerformanceCompareBetweenDbServiceDao.getTobeSQLPlan( temp );
							
							if ( listStr != null ) {
								for ( String str : listStr) {
									if ( str != null ) {
										strBuf.append( str ).append("\r\n");
									} else {
										strBuf.append("\r\n");
									}
								}
							}
							logger.debug("TOBE getExecutions ==============>\r\n"+strBuf.toString());
							temp.setExecutions( temp.getExecutions() + strBuf.toString() );
						}
					}
					
					if ( temp.getExecutions().length() > 0) {
						autoPerformanceCompareBetweenDbServiceDao.insertPlanInSQLTuning( temp );
					}
					
					temp.setExecutions("");
				}
			}
			
			// 튜닝담당자 할당 건수 조회
			if ( tuningNoArray != null && tuningNoArray != "" ) {
				tuningNoArray = StringUtil.Right( tuningNoArray,1 );
				tuningTargetSql.setTuning_no_array(tuningNoArray);
				perfrIdList = sqlTuningTargetDao.perfrIdAssignCountList( tuningTargetSql );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		result.setMessage( tuningTargetSql.getSqlExclude() );
		result.setResult( perfrIdList.size() > 0 ? true : false );
		result.setTxtValue( choiceTms );
		result.setObject( perfrIdList );
		
		tuningNoArray = null;
		tuningTargetSql = null;
		perfrIdList = null;
		moduleArry = null;
		sqlIdArry = null;
		parsingSchemaNameArry = null;
		asisPlanHashValueArry = null;
		tobePlanHashValueArry = null;
		listStr = null;
		odsList = null;
		temp = null;
		auto_tuner = null;
		choiceTms = null;
		tuningNo = null;
		strBuf = null;
		odsText = null;
		
		return result;
	}

	// 튜닝SQL일괄검증 > 튜닝대상선정
	@Override
	public Result insertSelectedTuningTarget( TuningTargetSql tuningTargetSql ) throws Exception {
		Result result = new Result();
		DatabaseTuner tuner = new DatabaseTuner();
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String wrkjob_cd = StringUtil.nvl(SessionManager.getLoginSession().getUsers().getWrkjob_cd(),"");
		String ext_no = SessionManager.getLoginSession().getUsers().getExt_no();
		
		List<DatabaseTuner> tunerList = new ArrayList<DatabaseTuner>();
		List<TuningTargetSql> perfrIdList = new ArrayList<TuningTargetSql>();
		
		TuningTargetSql temp = null;
		String[] parsingSchemaNameArry = null;
		String[] tuningNoArry = null;
		String[] moduleArry = null;
		String[] sqlIdArry = null;
		
		String auto_tuner = "";
		String choiceTms = "";
		String tuningNo = "";
		String tuningNoArray = "";
		
		int tunerCnt = 0;
		
		try {
			// 1. 튜닝담당자 자동선정일 경우 Tuner 조회
			if ( tuningTargetSql.getAuto_share().equals("Y") ) {
				tuner.setDbid(tuningTargetSql.getDbid());
				tunerList = commonDao.getTuner(tuner);
				
				tunerCnt = tunerList.size();
			}
			
			// DB 변경 성능 영향도 분석 > 튜닝SQL일괄검증
			logger.debug("##### 튜닝대상선정 진행 - project_id :" +tuningTargetSql.getProject_id() + ",Sql_auto_perf_check_id :"+ tuningTargetSql.getSql_auto_perf_check_id() );
			sqlIdArry = StringUtil.split( tuningTargetSql.getSqlIdArry(), "|" );
			moduleArry = StringUtil.split( tuningTargetSql.getModule(), "|" );
			tuningNoArry = StringUtil.split( tuningTargetSql.getTuningNoArry(), "|" );
			parsingSchemaNameArry = StringUtil.split( tuningTargetSql.getParsing_schema_name(), "|" );
			
			for ( int sqlIdx = 0; sqlIdx < sqlIdArry.length; sqlIdx++ ) {
				temp = new TuningTargetSql();
				
				temp.setProject_id( tuningTargetSql.getProject_id() );
				temp.setSql_id( sqlIdArry[sqlIdx].substring( sqlIdArry[sqlIdx].lastIndexOf(']')+1 ) );
				temp.setDatabase_kinds_cd( tuningTargetSql.getDatabase_kinds_cd() );
				
				int cnt = autoPerformanceCompareBetweenDbServiceDao.getDuplicateSQLTuningTargetByProjectCount( temp );
				
				if ( tuningTargetSql.getChkExcept() != null && "".equals(tuningTargetSql.getChkExcept()) == false  && cnt > 0 ) {
					continue;
				} else {
					temp.setModule( moduleArry[sqlIdx].substring( moduleArry[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setBefore_tuning_no( tuningNoArry[sqlIdx].substring(tuningNoArry[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setParsing_schema_name( parsingSchemaNameArry[sqlIdx].substring( parsingSchemaNameArry[sqlIdx].lastIndexOf(']')+1 ) );
					logger.debug("tuningNo==========>"+ temp.getBefore_tuning_no() + " , "+ temp.getSql_id() + " , "+temp.getParsing_schema_name()+" , "+temp.getModule());
					
					if ( tuningTargetSql.getAuto_share().equals("Y") ) {
						logger.debug("##### 튜닝담당자 자동 할당 #####");
						auto_tuner = tunerList.get(sqlIdx%tunerCnt).getTuner_id();
					} else {
						auto_tuner = tuningTargetSql.getPerfr_id();
					}
					
					// 4-1. TOPSQL_HANDOP_CHOICE_EXEC MAX CHOICE_TMS & TUNING_TARGET_SQL MAX TUNING_NO 조회
					choiceTms = sqlTuningTargetDao.getMaxChoiceTms( tuningTargetSql );
					tuningNo = improvementManagementDao.getNextTuningNo();
					
					temp.setDbid( tuningTargetSql.getDbid() );
					temp.setTuning_no( tuningNo );
					temp.setChoice_tms( choiceTms );
					temp.setPerfr_id( auto_tuner );
					temp.setTuning_status_change_why("튜닝SQL 일괄 검증 튜닝대상선정 및 접수");
					temp.setTuning_requester_id( user_id );
					temp.setTuning_requester_wrkjob_cd( wrkjob_cd );
					temp.setTuning_requester_tel_num( ext_no );
					temp.setChoice_div_cd( tuningTargetSql.getChoice_div_cd() );
					
					temp.setSql_auto_perf_check_id( tuningTargetSql.getSql_auto_perf_check_id() );
					temp.setTuning_status_cd( tuningTargetSql.getTuning_status_cd());
					
					temp.setVerify_project_id( tuningTargetSql.getVerify_project_id() );
					temp.setVerify_sql_auto_perf_check_id( tuningTargetSql.getVerify_sql_auto_perf_check_id() );
					
					tuningNoArray += tuningNo + ",";
					
					// 4-2. TUNING_TARGET_SQL INSERT (SQL-32) 
					autoPerformanceCompareBetweenDbServiceDao.insertTobeTuningTargetSql( temp );
					
					// 5. SQL_TUNING_STATUS_HISTORY INSERT
					temp.setTuning_status_changer_id( user_id );
					
					// SQL-33
					improvementManagementDao.insertTuningStatusHistory( temp );
					
					// 바이드값 삭제
					autoPerformanceCompareBetweenDbServiceDao.deleteTuningTargetSqlBind( tuningNo );
					
					// tuningNO로 Bind값 MAX순번 가져오기
					// temp.setBind_set_seq( autoPerformanceCompareBetweenDbServiceDao.getBindSetSeq( tuningNo ) );
					temp.setBind_set_seq("1");
					// SQL-34 신규 바인드 추가.
					autoPerformanceCompareBetweenDbServiceDao.insertTuningTargetSqlBindForVerify( temp );
				}
			}
			// 튜닝담당자 할당 건수 조회
			if ( tuningNoArray != null && "".equals( tuningNoArray ) == false ) {
				tuningNoArray = StringUtil.Right( tuningNoArray,1 );
				tuningTargetSql.setTuning_no_array(tuningNoArray);
				
				perfrIdList = sqlTuningTargetDao.perfrIdAssignCountList( tuningTargetSql );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		result.setMessage( tuningTargetSql.getSqlExclude() );
		result.setResult( perfrIdList.size() > 0 ? true : false );
		result.setTxtValue( choiceTms );
		result.setObject( perfrIdList );
		
		tuningNoArray = null;
		tuningNoArry = null;
		moduleArry = null;
		sqlIdArry = null;
		parsingSchemaNameArry = null;
		auto_tuner = null;
		choiceTms = null;
		tuningNo = null;
		tuningTargetSql = null;
		perfrIdList = null;
		temp = null;
		
		return result;
	}
	
	@Override
	public List<SQLAutoPerformanceCompare> loadTuningPerformanceList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadTuningPerformanceList(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadTuningPerformanceDetailList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			sqlAutoPerformanceCompare.setSql_id(" ");
			
			resultList = autoPerformanceCompareBetweenDbServiceDao.loadTuningPerformanceDetailList(
					sqlAutoPerformanceCompare );
			
			for ( int sqlIdx = 0; sqlIdx < resultList.size(); sqlIdx++ ) {
				String sqlText = resultList.get(sqlIdx).getSql_text_web();
				
				if ( sqlText != null && "".equals( sqlText ) == false ) {
					sqlText = removeHTML(sqlText);
					sqlText = removeSpecialChar(sqlText);
//					sqlText = unescapeHtml( sqlText );
					
					if ( sqlText.length() > 40 ) {
						resultList.get(sqlIdx).setSql_text_web( sqlText.substring(0, 40) );
					} else {
						resultList.get(sqlIdx).setSql_text_web( sqlText );
					}
				}
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultList;
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadPerformanceResultListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			sqlAutoPerformanceCompare.setSql_id(" ");
			String choice_div_cd = sqlAutoPerformanceCompare.getChoice_div_cd();
			
			if( choice_div_cd != null ) {
				if( "J".equals(choice_div_cd) ) {
					resultList = autoPerformanceCompareBetweenDbServiceDao.tableCHGPerfChkTargetAll(
							sqlAutoPerformanceCompare );
					
				}else {
					resultList = autoPerformanceCompareBetweenDbServiceDao.loadPerformanceResultListAll(
							sqlAutoPerformanceCompare );
				}
			}
			
			choice_div_cd = null;
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultList;
	}
	
	@Override
	public boolean excelTuningDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare , Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.excelTuningDownload";
		String sql = "";
		
		try {
			handler.open();
			
			handler.buildInit("튜닝실적", "TUNING_PERFORMANCE");
			
			sqlAutoPerformanceCompare.setSql_id(" ");
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.writeDoc();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			resultSuccess = false;
			
		}finally {
			handler.close();
		}
		
		return resultSuccess;
	}

	
	@Override
	public boolean excelTuningDetailDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model, HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.excelTuningDetailDownload";
		String sql = "";
		
		try {
			handler.open();
			
			handler.buildInit("튜닝실적상세", "TUNING_PERFORMANCE_DETAIL");
			handler.addRemoveHTML( new String[] { "SQL_TEXT_EXCEL" } );
			
			sqlAutoPerformanceCompare.setSql_id(" ");
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.writeDoc();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			resultSuccess = false;
			
		}finally {
			handler.close();
		}
		
		return resultSuccess;
	}


	@Override
	public List<SQLAutoPerformanceCompare> loadSqlPerformancePacList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadSqlPerformancePacList(
				sqlAutoPerformanceCompare );
	}

	@Override
	public int getTuningTargetCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.getTuningTargetCount(
				sqlAutoPerformanceCompare );
	}

	@Override
	public int deleteSqlAutoPerformanceChk(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		int count = autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceChk(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceTarget(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceResult(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceError(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformancePlanTable(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceStat(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformancePlan(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceBind(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteTableCHGPerfTargetSql(
				sqlAutoPerformanceCompare );
		
		count += autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerfChkBindPlan(
				sqlAutoPerformanceCompare );
		return count;
	}

	@Override
	public Result forceUpdateSqlAutoPerformance( 
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception {
		Result result = new Result();
		
		String projectId = sqlAutomaticPerformanceCheck.getProject_id();
		String sqlAutoPerfCheckId = sqlAutomaticPerformanceCheck.getSql_auto_perf_check_id();
		
		try {
			String rtnVal = ProjectPerfChk.threadForceKill(Long.valueOf(projectId), Long.valueOf(sqlAutoPerfCheckId), StringUtil.getRandomJobKey());
			JSONObject rtnJson = strToJSONObject(rtnVal);
			
			String err_msg = (String)rtnJson.get("err_msg");
			String is_error = (String)rtnJson.get("is_error");
			logger.debug("err_msg ===> ["+err_msg+"] , is_error ===> ["+is_error+"]");
			
			if ( err_msg.equals("Complete") == true && is_error.equals("false") == true ) {
				result.setResult( true );
				autoPerformanceCompareBetweenDbServiceDao.forceUpdateSqlAutoPerformance( sqlAutomaticPerformanceCheck );
			} else {
				logger.debug("err_msg ====> "+err_msg+" , is_error ====> "+ is_error);
				result.setMessage( rtnVal );
				result.setResult( false );
			}
			
			err_msg = null;
			is_error = null;
			rtnVal = null;
			rtnJson = null;
			sqlAutoPerfCheckId = null;
			projectId = null;
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return result;
	}

	@Override
	public Result forceUpdateTuningSqlAutoPerf( 
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck ) throws Exception {
		Result result = new Result();
		
		String projectId = sqlAutomaticPerformanceCheck.getVerify_project_id();
		String sqlAutoPerfCheckId = sqlAutomaticPerformanceCheck.getVerify_sql_auto_perf_check_id();
		
		try {
			// 일괄검증 강제완료처리
			String rtnVal = ProjectPerfChk.threadForceKill(Long.valueOf(projectId), Long.valueOf(sqlAutoPerfCheckId),"2", StringUtil.getRandomJobKey());
			
			JSONObject rtnJson = strToJSONObject(rtnVal);
			
			String err_msg = (String)rtnJson.get("err_msg");
			String is_error = (String)rtnJson.get("is_error");
			logger.debug("err_msg ===> ["+err_msg+"] , is_error ===> ["+is_error+"]");
			
			if ( err_msg.equals("Complete") && is_error.equals("false") ) {
				result.setResult( true );
				sqlAutomaticPerformanceCheck.setProject_id( projectId );
				sqlAutomaticPerformanceCheck.setSql_auto_perf_check_id( sqlAutoPerfCheckId );
				autoPerformanceCompareBetweenDbServiceDao.forceUpdateSqlAutoPerformance( sqlAutomaticPerformanceCheck );
			} else {
				logger.debug("err_msg ====> "+err_msg+" , is_error ====> "+ is_error);
				result.setResult( false );
			}
			
			err_msg = null;
			is_error = null;
			rtnVal = null;
			rtnJson = null;
			sqlAutoPerfCheckId = null;
			projectId = null;
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public List<SQLAutoPerformanceCompare> getTobeSqlPerfPacName(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		
		List<SQLAutoPerformanceCompare> resultList = autoPerformanceCompareBetweenDbServiceDao.getTobeSqlPerfPacName(
				sqlAutoPerformanceCompare );
		try {
			for ( SQLAutoPerformanceCompare sqlCom : resultList ) {
				String sqlPerfPac = sqlCom.getPerf_check_name();
				
				if ( sqlPerfPac != null && "".equals( sqlPerfPac ) == false ) {
					sqlPerfPac = removeHTML( sqlPerfPac );
					sqlPerfPac = removeSpecialChar( sqlPerfPac );
					//sqlPerfPac = unescapeHtml( sqlPerfPac );
					
					sqlCom.setPerf_check_name( sqlPerfPac.toString() );
				}
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultList;
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadTuningBatchValidationNorthList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadTuningBatchValidationNorthList(
				sqlAutoPerformanceCompare );
	}
	
	@Override
	public List<SQLAutoPerformanceCompare> loadTuningBatchValidationSouthList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		sqlAutoPerformanceCompare.setSql_id(" ");
		try {
			resultList = autoPerformanceCompareBetweenDbServiceDao.loadTuningBatchValidationSouthList(
					sqlAutoPerformanceCompare );
			
			for ( int sqlIdx = 0; sqlIdx < resultList.size(); sqlIdx++ ) {
				String sqlText = resultList.get(sqlIdx).getSql_text_web();
				
				if ( sqlText != null && "".equals( sqlText ) == false ) {
					sqlText = removeHTML(sqlText);
					sqlText = removeSpecialChar(sqlText);
//					sqlText = unescapeHtml( sqlText );
					
					if ( sqlText.length() > 40 ) {
						resultList.get(sqlIdx).setSql_text_web( sqlText.substring(0, 40) );
					} else {
						resultList.get(sqlIdx).setSql_text_web( sqlText );
					}
				}
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultList;
	}

	@Override
	public int countTuningEndTms(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.countTuningEndTms(
				sqlAutoPerformanceCompare );
	}
	

	@Override
	public boolean excelNorthDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model, HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.excelNorthDownload";
		String sql = "";
		
		try {
			handler.open();
			
			handler.buildInit("튜닝_SQL_일괄_검증", "TUNING_SQL_PERFORMANCE");
			
			sqlAutoPerformanceCompare.setSql_id(" ");
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.writeDoc();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			resultSuccess = false;
			
		} finally {
			handler.close();
		}
		
		return resultSuccess;
	}


	@Override
	public boolean excelSouthDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model, HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.excelSouthDownload";
		String sql = "";
		
		try {
			handler.open();
			
			handler.buildInit("튜닝_SQL_일괄_검증_상세", "TUNING_SQL_PERF_DETAIL");
			handler.addRemoveHTML( new String[] { "SQL_TEXT_EXCEL" } );
			
			sqlAutoPerformanceCompare.setSql_id(" ");
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			handler.buildDocument(sqlSessionFactory, sql);
				
			handler.writeDoc();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			resultSuccess = false;
			
		} finally {
			handler.close();
		}
		
		return resultSuccess;
	}


	@Override
	public List<SQLAutoPerformanceCompare> loadTuningBatchValidationSouthListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadTuningBatchValidationSouthListAll(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> getOperationDB(SQLAutoPerformanceCompare sqlAutoPerformanceCompare)
			throws Exception {
		
		if ( sqlAutoPerformanceCompare.getDb_operate_type_cd() == null || "".equals( sqlAutoPerformanceCompare.getDb_operate_type_cd() ) ) {
			sqlAutoPerformanceCompare.setDb_operate_type_cd("3");
		}
		logger.debug("db_operate_type_cd =======================>"+ sqlAutoPerformanceCompare.getDb_operate_type_cd() );
		
		return autoPerformanceCompareBetweenDbServiceDao.getOperationDB(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadOperationSqlPerfTrackList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		sqlAutoPerformanceCompare.setSql_id(" ");
		try {
			resultList =  autoPerformanceCompareBetweenDbServiceDao.loadOperationSqlPerfTrackList(
					sqlAutoPerformanceCompare );
			
			for ( int sqlIdx = 0; sqlIdx < resultList.size(); sqlIdx++ ) {
				String sqlText = resultList.get(sqlIdx).getSql_text_web();
				
				if ( sqlText != null && "".equals( sqlText ) == false ) {
					sqlText = removeHTML(sqlText);
					sqlText = removeSpecialChar(sqlText);
//					sqlText = unescapeHtml( sqlText );
					
					if ( sqlText.length() > 40 ) {
						resultList.get(sqlIdx).setSql_text_web( sqlText.substring(0, 40) );
					} else {
						resultList.get(sqlIdx).setSql_text_web( sqlText );
					}
				}
				
				if ( resultList.get(sqlIdx).getSql_profile_nm() != null &&
						!"".equals( resultList.get(sqlIdx).getSql_profile_nm() ) &&
						resultList.get(sqlIdx).getSql_profile_nm().length() > 30 ) {
					resultList.get(sqlIdx).setSql_profile_nm(
							resultList.get(sqlIdx).getSql_profile_nm().substring(0,30) );
				}
				
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultList;
	}

	@Override
	public Result insertOperationTuningTarget( TuningTargetSql tuningTargetSql ) throws Exception {
		Result result = new Result();
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String wrkjob_cd = StringUtil.nvl(SessionManager.getLoginSession().getUsers().getWrkjob_cd(),"");
		String ext_no = SessionManager.getLoginSession().getUsers().getExt_no();
		
		DatabaseTuner tuner = new DatabaseTuner();
		List<DatabaseTuner> tunerList = new ArrayList<DatabaseTuner>();
		int tunerCnt = 0;
		
		String auto_tuner = "";
		String choiceTms = "";
		String tuningNo = "";
		String tuningNoArray = "";
		
		String[] dbids = null;
		String[] sqlIdArry = null;
		String[] moduleArry = null;
		String[] planHashValues = null;
		String[] parsingSchemaName = null;
		String[] operationExecutions = null;
		String[] operationBufferGets = null;
		String[] operationElapsedTimes = null;
		String[] operationRowsProcesseds = null;
		
		TuningTargetSql temp;
		List<TuningTargetSql> perfrIdList = new ArrayList<TuningTargetSql>();
		
		try {
			dbids = StringUtil.split( tuningTargetSql.getDbid(), "|" );
			
			// 1. 자동선정일 경우 Tuner 조회
			if ( tuningTargetSql.getAuto_share().equals("Y") ) {
				tuner.setDbid( dbids[0] );
				tunerList = commonDao.getTuner(tuner);
				
				tunerCnt = tunerList.size();
			}
			
			 // DB간 자동성능비교 > 운영SQL성능추적
			moduleArry = StringUtil.split( tuningTargetSql.getModule(), "|" );
			sqlIdArry = StringUtil.split( tuningTargetSql.getSqlIdArry(), "|" );
			planHashValues = StringUtil.split( tuningTargetSql.getPlan_hash_value(), "|" );
			parsingSchemaName = StringUtil.split( tuningTargetSql.getParsing_schema_name(), "|" );
			operationExecutions = StringUtil.split( tuningTargetSql.getOperation_executions(), "|" );
			operationBufferGets = StringUtil.split( tuningTargetSql.getOperation_buffer_gets(), "|" );
			operationElapsedTimes = StringUtil.split( tuningTargetSql.getOperation_elapsed_time(), "|" );
			operationRowsProcesseds = StringUtil.split( tuningTargetSql.getOperation_rows_processed(), "|" );
			
			for ( int sqlIdx = 0; sqlIdx < sqlIdArry.length; sqlIdx++ ) {
				temp = new TuningTargetSql();
				temp.setProject_id( tuningTargetSql.getProject_id() );
				temp.setDbid( tuningTargetSql.getOperation_dbid() );
				temp.setSql_id( sqlIdArry[sqlIdx].substring( sqlIdArry[sqlIdx].lastIndexOf(']')+1 ) );
				temp.setDatabase_kinds_cd( tuningTargetSql.getDatabase_kinds_cd() );
				
				int cnt = autoPerformanceCompareBetweenDbServiceDao.getDuplicateSQLTuningTargetByProjectAndDBCount(temp);
				
				if ( tuningTargetSql.getChkExcept() != null && "".equals(tuningTargetSql.getChkExcept()) == false  && cnt > 0 ) {
					continue;
				} else {
					temp.setDbid( dbids[sqlIdx].substring( dbids[sqlIdx].lastIndexOf(']')+1 )  );
					temp.setModule( moduleArry[sqlIdx].substring( moduleArry[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setPlan_hash_value( planHashValues[sqlIdx].substring( planHashValues[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setExecutions( operationExecutions[sqlIdx].substring( operationExecutions[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setParsing_schema_name( parsingSchemaName[sqlIdx].substring( parsingSchemaName[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setAvg_buffer_gets( operationBufferGets[sqlIdx].substring( operationBufferGets[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setAvg_elapsed_time( operationElapsedTimes[sqlIdx].substring( operationElapsedTimes[sqlIdx].lastIndexOf(']')+1 ) );
					temp.setAvg_row_processed( operationRowsProcesseds[sqlIdx].substring( operationRowsProcesseds[sqlIdx].lastIndexOf(']')+1 ));
					
					if ( tuningTargetSql.getAuto_share().equals("Y") ) {
						logger.debug("##### 튜닝담당자 자동 할당 #####");
						auto_tuner = tunerList.get(sqlIdx%tunerCnt).getTuner_id();
					} else {
						auto_tuner = tuningTargetSql.getPerfr_id();
					}
					
					// 4-1. TOPSQL_HANDOP_CHOICE_EXEC MAX CHOICE_TMS & TUNING_TARGET_SQL MAX TUNING_NO 조회
					choiceTms = sqlTuningTargetDao.getMaxChoiceTms( temp );
					tuningNo = improvementManagementDao.getNextTuningNo();
					
					temp.setTuning_no( tuningNo );
					temp.setPerfr_id( auto_tuner );
					temp.setChoice_tms( choiceTms );
					temp.setTuning_status_cd( tuningTargetSql.getTuning_status_cd());
					temp.setChoice_div_cd( tuningTargetSql.getChoice_div_cd() );
					temp.setTuning_requester_id( user_id );
					temp.setTuning_requester_wrkjob_cd( wrkjob_cd );
					temp.setTuning_requester_tel_num( ext_no );
					
					temp.setSql_auto_perf_check_id( tuningTargetSql.getSql_auto_perf_check_id() );
					temp.setTuning_status_cd( tuningTargetSql.getTuning_status_cd());
					temp.setTuning_status_change_why("운영 SQL 성능 추적 튜닝대상선정 및 접수");
					
					temp.setMax_buffer_gets("");
					temp.setTotal_buffer_gets("");
					temp.setMax_elapsed_time("");
					temp.setAvg_cpu_time("");
					temp.setAvg_disk_reads("");
					temp.setRatio_buffer_get_total("");
					temp.setRatio_cpu_total("");
					temp.setRatio_cpu_per_executions("");
					temp.setPerf_check_id("");
					temp.setProgram_id("");
					temp.setTuning_prgrs_step_seq("");
					temp.setDbio("");
					
					tuningNoArray += tuningNo + ",";
					
					// 4-2. TUNING_TARGET_SQL INSERT (SQL-37(1)) 
					sqlsDao.insertTuningTargetSql( temp );
					
					// 5. SQL_TUNING_STATUS_HISTORY INSERT
					temp.setTuning_status_changer_id( user_id );
					
					// SQL-37(2)
					improvementManagementDao.insertTuningStatusHistory( temp );
					
					// 바이드값 삭제
					autoPerformanceCompareBetweenDbServiceDao.deleteTuningTargetSqlBind( tuningNo );
					
					// tuningNO로 Bind값 MAX순번 가져오기
					temp.setBind_set_seq( autoPerformanceCompareBetweenDbServiceDao.getBindSetSeq( tuningNo ) );
					
					// SQL-37(3) 재사용.
					autoPerformanceCompareBetweenDbServiceDao.insertTuningTargetSqlBindFromVsqlBindCapture( temp );
//				autoPerformanceCompareBetweenDbServiceDao.insertTuningTargetSqlBindNew( temp );
				}
			}
			// 튜닝담당자 할당 건수 조회
			if ( tuningNoArray != null && "".equals( tuningNoArray ) == false ) {
				tuningNoArray = StringUtil.Right( tuningNoArray, 1 );
				tuningTargetSql.setTuning_no_array(tuningNoArray);
				
				perfrIdList = sqlTuningTargetDao.perfrIdAssignCountList( tuningTargetSql );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		result.setMessage( tuningTargetSql.getSqlExclude() );
		result.setResult( perfrIdList.size() > 0 ? true : false );
		result.setTxtValue( choiceTms );
		result.setObject( perfrIdList );
		
		auto_tuner = null;
		choiceTms = null;
		tuningNo = null;
		tuningNoArray = null;
		
		dbids = null;
		sqlIdArry = null;
		moduleArry = null;
		planHashValues = null;
		parsingSchemaName = null;
		operationExecutions = null;
		operationBufferGets = null;
		operationElapsedTimes = null;
		operationRowsProcesseds = null;
		tuningTargetSql = null;
		perfrIdList = null;
		temp = null;
		
		return result;
	}

	@Override
	public boolean operationExcelDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model, HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.operationExcelDownload";
		String sql = "";
		
		try {
			handler.open();
			
			handler.buildInit("운영_SQL_성능_추적", "OPERATION_SQL_PERF_TRACK");
			handler.addRemoveHTML(new String[] { "SQL_TEXT_EXCEL" });
			
			sqlAutoPerformanceCompare.setSql_id(" ");
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			handler.buildDocument(sqlSessionFactory, sql);
				
			handler.writeDoc();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			resultSuccess = false;
			
		} finally {
			handler.close();
		}
		
		return resultSuccess;
	}


	@Override
	public List<SQLAutoPerformanceCompare> loadOperationSqlPerfTrackListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadOperationSqlPerfTrackListAll(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadPerfCheckResultList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare)
			throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadPerfCheckResultList(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadExplainBindValueNew(SQLAutoPerformanceCompare sqlAutoPerformanceCompare)
			throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadExplainBindValueNew(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadExplainInfoBindValue(SQLAutoPerformanceCompare sqlAutoPerformanceCompare)
			throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadExplainInfoBindValue(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadAfterSelectTextPlanListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadAfterSelectTextPlanListAll(
				sqlAutoPerformanceCompare );
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadAfterDMLTextPlanListAll(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadAfterDMLTextPlanListAll(
				sqlAutoPerformanceCompare );
	}

	@Override
	public int countTuningExecuteTms(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.countTuningExecuteTms(
				sqlAutoPerformanceCompare );
	}

	@Override
	public Result callSqlProfileApply(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		Result result = new Result();
		JSONArray jArray = new JSONArray();
		JSONObject jObj = null;
		StringBuffer arrSqlId = new StringBuffer();
		StringBuffer arrHashValue = new StringBuffer();
		
		// 전체 선택 시
		if ( sqlAutoPerformanceCompare.getIsAll() != null && sqlAutoPerformanceCompare.getIsAll().equals("A") == true ) {
			List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
			
			try {
				sqlAutoPerformanceCompare.setSql_id(" ");
				resultList = autoPerformanceCompareBetweenDbServiceDao.loadPerformanceResultListSqlIdAndHashValue(
					sqlAutoPerformanceCompare );
				
				for ( SQLAutoPerformanceCompare sqlComp : resultList ) {
					arrSqlId.append(",").append( sqlComp.getSql_id() );
					arrHashValue.append(",").append( sqlComp.getAsis_plan_hash_value() );
				}
				
				sqlAutoPerformanceCompare.setSqlIdList( arrSqlId.toString() );
				sqlAutoPerformanceCompare.setPlanHashValueList( arrHashValue.toString() );
			} catch ( Exception ex ) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " 예외 발생 ==> ", ex);
				
				throw ex;
			}
		}
		
		String projectId = sqlAutoPerformanceCompare.getProject_id();
		String sqlAutoPerfCheckId = sqlAutoPerformanceCompare.getSql_auto_perf_check_id();
		String type = sqlAutoPerformanceCompare.getSearchKey2();
		String ret = "";
		String perfCheckTargetDbid = null;
		String migrateTargetDbid = null;
		String is_error = null;
		String strMsg = null;
		StringBuffer msg = new StringBuffer();
		
		String[] strRet = new String[0];
		String[] sqlIdArr = new String[0];
		String[] hashValueArr = new String[0];
		
		if ( type.equals("3") ) {
			perfCheckTargetDbid = sqlAutoPerformanceCompare.getPerf_check_target_dbid() ;
			migrateTargetDbid = sqlAutoPerformanceCompare.getTransfer_dbid();
		}
		
		try {
			sqlIdArr = sqlAutoPerformanceCompare.getSqlIdList().split(",");
			hashValueArr = sqlAutoPerformanceCompare.getPlanHashValueList().split(",");
			
			if ( sqlIdArr.length > 1 ) {
				
				for ( int dataIdx = 1; dataIdx < sqlIdArr.length; dataIdx++ ) {
					
					jObj = new JSONObject();
					jObj.put( "sqlId" , sqlIdArr[dataIdx] );
					jObj.put( "profileName" , "OP_"+sqlIdArr[dataIdx]+"_"+hashValueArr[dataIdx] );
					jArray.add( jObj );
					logger.debug("sqlID,profileName =============> "+jObj.get("sqlId")+","+jObj.get("profileName"));
				}
				jObj = null;
				if ( type.equals("1") ) {
					ret = ProjectPerfChkSqlProfile.projectPerfChkSqlProfileCreate( Long.parseLong(projectId), Long.parseLong(sqlAutoPerfCheckId), jArray );
				} else if ( type.equals("2") ) {
					ret = ProjectPerfChkSqlProfile.projectPerfChkSqlProfileDrop( Long.parseLong(projectId), Long.parseLong(sqlAutoPerfCheckId), jArray);
				} else {
					ret = ProjectPerfChkSqlProfile.projectPerfChkSqlProfileMigrate(
							Long.parseLong(projectId), Long.parseLong(sqlAutoPerfCheckId),
							Long.parseLong(perfCheckTargetDbid), Long.parseLong(migrateTargetDbid), jArray );
				}
				
				logger.debug("com-lib return =====================> "+ ret);
			}
			
			jObj = strToJSONObject( ret );
			is_error = jObj.get("is_error").toString();
			strMsg = jObj.get("message").toString();
			
			if ( is_error.equals("false") == true ) {
				
				if ( strMsg.equals("Complete") == false ) {
					logger.debug("-적용/삭제-");
					
					strRet =  strMsg.split(",");
					msg.append( strRet[1] ).append(" , ").append( strRet[2] );
					
					result.setTxtValue( msg.toString() );
				} else {
					logger.debug("-이관-");
					result.setTxtValue( "" );
				}
				
				result.setResult( true );
				logger.debug( "msg =====>"+msg.toString() );
			} else { 
				result.setResult( false );
			}
			
		} catch ( Exception ex ) {
			DBUtil.printErrMsg(SPopTools.isDebug, ex);
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> ", ex);
		}
		
		jArray = null;
		jObj = null;
		strMsg = null;
		is_error = null;
		
		projectId = null;
		sqlAutoPerfCheckId = null;
		type = null;
		ret = null;
		msg = null;
		perfCheckTargetDbid = null;
		migrateTargetDbid = null;
		
		strRet = null;
		sqlIdArr = null;
		hashValueArr = null;
		
		return result;
	}

	@Override
	public List<SQLAutoPerformanceCompare> loadPerformanceList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare )throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadPerformanceList( sqlAutoPerformanceCompare );
	}

	private void deleteAutoPerfChkTables( String project_id, String sql_auto_perf_check_id, String dbcd ) {
		SQLAutoPerformanceCompare sqlAutoPerformanceCompare = new SQLAutoPerformanceCompare();
		sqlAutoPerformanceCompare.setProject_id( project_id);
		sqlAutoPerformanceCompare.setSql_auto_perf_check_id( sql_auto_perf_check_id );
		sqlAutoPerformanceCompare.setDatabase_kinds_cd( dbcd );
		
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceTarget(
				sqlAutoPerformanceCompare );
				
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceResult(
				sqlAutoPerformanceCompare );
				
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceError(
				sqlAutoPerformanceCompare );
				
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformancePlanTable(
				sqlAutoPerformanceCompare );
				
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceStat(
				sqlAutoPerformanceCompare );
				
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformancePlan(
				sqlAutoPerformanceCompare );
		
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceBind(
				sqlAutoPerformanceCompare );
		
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerformanceChk(
				sqlAutoPerformanceCompare );
		
		autoPerformanceCompareBetweenDbServiceDao.deleteTableCHGPerfTargetSql(
				sqlAutoPerformanceCompare );
		
		autoPerformanceCompareBetweenDbServiceDao.deleteSqlAutoPerfChkBindPlan(
				sqlAutoPerformanceCompare );
		
	}
	
	@Override
	public String getSqlMemo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		String resultStr = autoPerformanceCompareBetweenDbServiceDao.getSqlMemo(
				sqlAutoPerformanceCompare );
		try {
			if ( resultStr != null && "".equals( resultStr ) == false ) {
				resultStr = removeHTML( resultStr );
				resultStr = removeSpecialChar( resultStr );
			}
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> ", ex);
			throw ex;
		}
		
		return resultStr;
	}

	@Override
	public int updateSqlMemo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.updateSqlMemo(
				sqlAutoPerformanceCompare );
	}

	@Override
	public int deleteSqlMemo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.deleteSqlMemo(
				sqlAutoPerformanceCompare );
	}
	

	@Override
	public List<SQLAutomaticPerformanceCheck> loadOriginalDb(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck)
			throws Exception {
		return autoPerformanceCompareBetweenDbServiceDao.loadOriginalDb(sqlAutomaticPerformanceCheck);
	}

	/**
	* HTML 테크 제거후 문자열 리턴
	*
	* @param strText  HTML이 포함된 문자열
	* @return 문자열
	*/
	private String removeHTML(String strText) {
		String returnValue = strText;
		
		if ( strText != null && "".equals( strText ) == false ) {
//			returnValue = returnValue.replaceAll("\r\n","{[n");
//			returnValue = returnValue.replaceAll("\\s"," ");
//			returnValue = returnValue.replaceAll("\\{\\[n","\r\n");
			returnValue = returnValue.replaceAll("&nbsp;"," ");
			returnValue = returnValue.replaceAll("&#39;","'");	
			returnValue = returnValue.replaceAll("<>","@@");
//			returnValue = returnValue.replaceAll("(<br>)|(<br\\/>)|(<br \\/>)","\n");
			returnValue = returnValue.replaceAll("<(\\/br|br|br\\/|br \\/| br\\/[^>]*)>","\n");
			returnValue = returnValue.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>","");
			returnValue = returnValue.replaceAll("@@","<>");
			returnValue = returnValue.replaceAll("\n","\r\n");
			returnValue = returnValue.replaceAll("\r\r\n","\r\n");
		}
		
		return returnValue;
	}
	
	
	/**
	 * 특수문자 제거
	 *
	 * @param strText  특수문자가 포함된 문자열
	 * @return 문자열
	 */
	private String removeSpecialChar(String strText) {
		String returnValue = strText;
		
		if ( strText != null && "".equals( strText ) == false ) {
			returnValue = returnValue.replaceAll("(<P>)|(</P>)","");
			returnValue = returnValue.replaceAll("&lt;","<");
			returnValue = returnValue.replaceAll("&gt;",">");
			returnValue = returnValue.replaceAll("&amp;","&");
			returnValue = returnValue.replaceAll("&quot;","\"");
			returnValue = returnValue.replaceAll("&#035;","#");
		}
		
		return returnValue;
	}
	/**
	 * html형식문자 제거
	 * @param str html형식이 포함된 문자 제거
	 * @return 문자열
	 */
//	private String unescapeHtml(String str) {
//		String result = null;
//		if (str == null || "".equals(str) ) {
//			return result;
//		}
//		try {
//			StringWriter writer = new StringWriter ((int)(str.length() * 1.5));
//			unescapeHtml(writer,str);
//			logger.debug("unescapeHtml =======> "+ writer.toString() );
//			result = writer.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
//	public static void unescapeHtml(Writer writer, String string) throws IOException {
//        if (writer == null ) {
//            throw new IllegalArgumentException ("The Writer must not be null.");
//        }
//        if (string == null) {
//            return;
//        }
//        Entities.HTML40.unescape(writer, string);
//    }
	
	/**
	 * JsonParse
	 */
	public JSONObject strToJSONObject(String strMsg) throws Exception{
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) jsonParser.parse(strMsg);
		}catch(Exception e) {
			throw e;
		}
		return jsonObj;
	
	}
}