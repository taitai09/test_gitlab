package omc.spop.service.impl.AISQLPV;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import omc.spop.dao.AISQLPVAnalyzeDao;
import omc.spop.dao.AISQLPVDao;
import omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.VsqlText;
import omc.spop.service.AISQLPVService;
import omc.spop.utils.ExcelDownHandler;
import omc.spop.utils.StringUtil;

/***********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationServiceImpl
 **********************************************************/

@Service("AISQLPVService")
public class AISQLPVServiceImpl implements AISQLPVService {
	private static final Logger logger = LoggerFactory.getLogger(AISQLPVServiceImpl.class );
	
	@Autowired
	private AISQLPVDao aisqlpvDao;
	
	@Autowired
	private AISQLPVAnalyzeDao aisqlAnalyzeDao;
	
	@Autowired
	private AutoPerformanceCompareBetweenDbServiceDao spsDao;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	public List<SQLAutoPerformanceCompare> getSqlPerformancePacList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.getSqlPerformancePacList(sqlAutoPerformanceCompare);
	}
	
	public List<SQLAutoPerformanceCompare> loadSqlPerformancePacList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadSqlPerformancePacList(sqlAutoPerformanceCompare);
	}
	
	public void insertSqlPerformanceInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) throws Exception{
		boolean isNotExist = false;
		int count = 0;
		
		isNotExist = checkExistPac(sqlAutoPerformanceCompare, result);
		
		if ( isNotExist ) {
			count = spsDao.insertSqlPerformanceInfo( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( true );
				result.setMessage( "저장 되었습니다." );
				
			} else {
				result.setResult( false );
				result.setMessage( "저장에 실패하였습니다." );
			}
		}
	}
	
	public void updateSqlPerformanceInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) throws Exception{
		int existCount = 0;
		int count = 0;
		
		String sqlPerfPac = sqlAutoPerformanceCompare.getPerf_check_name();
		
		if ( sqlPerfPac != null && "".equals( sqlPerfPac ) == false ) {
			sqlPerfPac = StringUtil.removeHTML( sqlPerfPac );
			sqlPerfPac = StringUtil.removeSpecialChar( sqlPerfPac );
			
			sqlAutoPerformanceCompare.setPerf_check_name( sqlPerfPac.toString() );
		}
		
		existCount = aisqlpvDao.getExistCount( sqlAutoPerformanceCompare );
		
		if ( existCount > 0) {
			result.setResult( false );
			result.setMessage("해당 프로젝트에 동일한 이름의 SQL점검팩이 존재합니다.");
			
		}else {
			count = spsDao.updateSqlPerformanceInfo( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( true );
				result.setMessage( "저장 되었습니다." );
				
			} else {
				result.setResult( false );
				result.setMessage( "저장에 실패하였습니다." );
			}
		}
	}
	
	public void checkUnfinishedCount(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) throws Exception{
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		SQLAutoPerformanceCompare resultObj = new SQLAutoPerformanceCompare();
		
		try {
			result.setMessage(null);
			resultList = aisqlAnalyzeDao.getExecutionConstraint(sqlAutoPerformanceCompare);
			
			if( resultList != null && resultList.size() > 0 && resultList.get(0) != null) {
				resultObj = resultList.get(0);
				
				if ( isPrecessing(resultObj.getAcces_path_exec_yn()) ) {
					result.setMessage("현재 인덱스 자동 분석 작업이 실행(ACCESS PATH 분석) 중 입니다.<br>작업 완료 후 삭제 바랍니다.");
					
				}else if ( isPrecessing(resultObj.getIndex_recommend_exec_yn()) ) {
					result.setMessage("현재 인덱스 자동 분석 작업이 실행(인덱스 RECOMMEND 분석) 중 입니다.<br>작업 완료 후 삭제 바랍니다.");
					
				}else if ( isPrecessing(resultObj.getIndex_db_create_exec_yn()) ) {
					result.setMessage("인덱스 자동 생성 작업이 실행 중입니다.<br>작업 완료 후 삭제 바랍니다.");
					
				}else if ( isPrecessing(resultObj.getIndex_db_drop_exec_yn()) ) {
					result.setMessage("인덱스 자동 제거 작업이 실행 중입니다.<br>작업 완료 후 삭제 바랍니다.");
					
				}else if ( isPrecessing(resultObj.getPerf_check_exec_yn()) ) {
					result.setMessage("성능 영향도 분석 작업이 실행 중입니다.<br>작업 완료 후 삭제 바랍니다.");
					
				}else {
					result.setResult(true);
					result.setMessage("선택된 SQL점검팩의 성능 분석 정보가 모두 삭제됩니다. 삭제할 경우 성능 분석 정보 조회가 불가합니다. 삭제하시겠습니까?");
				}
			}else {
				//바로 삭제 가능
				result.setResult(true);
			}
		} catch ( Exception ex ) {
			result.setStatus("break");
			throw ex;
			
		}finally {
			resultObj = null;
			resultList = null;
		}
	}
	
	public void deleteSqlPerfInfo(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) throws Exception{
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		try {
			String database_kinds_cd = sqlAutoPerformanceCompare.getDatabase_kinds_cd();
			
			aisqlpvDao.deleteIdxAdAsisIndex(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteIdxAdColNdv(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteIdxAdProcLog(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteIdxAdRecommendIndex(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteIdxDbWorkDeatil(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteIdxDbWork(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteIdxAdMst(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteAccPathIndexDesign(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteAccPath(sqlAutoPerformanceCompare);
			
			aisqlpvDao.deleteTableChgPerfChkTargetSql(sqlAutoPerformanceCompare);
			
			resultList = aisqlpvDao.getDeleteTargets(sqlAutoPerformanceCompare);
			
			for( SQLAutoPerformanceCompare loopObj : resultList ) {
				loopObj.setDatabase_kinds_cd(database_kinds_cd);
				
				spsDao.deleteTableCHGPerfTargetSql( loopObj );
				
				spsDao.deleteSqlAutoPerformanceResult( loopObj );
				
				spsDao.deleteSqlAutoPerformanceBind( loopObj );
				
				spsDao.deleteSqlAutoPerformanceError( loopObj );
				
				spsDao.deleteSqlAutoPerformancePlanTable( loopObj );
				
				spsDao.deleteSqlAutoPerformanceStat( loopObj );
				
				spsDao.deleteSqlAutoPerformancePlan( loopObj );
				
				spsDao.deleteSqlAutoPerfChkBindPlan( loopObj );
				
				spsDao.deleteSqlAutoPerformanceTarget(loopObj );
				
				spsDao.deleteSqlAutoPerformanceChk( loopObj );
			}
			result.setResult( true );
			
		} catch ( Exception ex ) {
			throw ex;
			
		}finally {
			resultList = null;
		}
	}
	
	public List<SQLAutoPerformanceCompare> loadSummaryData(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadSummaryData(sqlAutoPerformanceCompare);
	}
	
	public List<SQLAutoPerformanceCompare> loadIndexList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadIndexList(sqlAutoPerformanceCompare);
	}
	
	public List<SQLAutoPerformanceCompare> loadSqlListByIndex(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadSqlListByIndex(sqlAutoPerformanceCompare);
	}
	
	public boolean loadSqlListExcel(SQLAutoPerformanceCompare sqlAutoPerformanceCompare
			, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "";
		String sql = "";
		
		try {
			handler.open();
			
			sqlId = "omc.spop.dao.AISQLPVDao.loadIndexListExcel";
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			
			handler.buildInit("인덱스별_성능_영향도_분석_현황", "AISQLPV_RESULT_STATE");
			handler.buildDocument(sqlSessionFactory, sql);
			
			
			sqlId = "omc.spop.dao.AISQLPVDao.loadSqlListByIndexExcel";
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			
			handler.buildInit("인덱스별_성능_영향도_분석_결과", "AISQLPV_RESULT_BY_INDX");
			handler.addRemoveHTML( new String[] { "SQL_TEXT_EXCEL" } );
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.writeDoc();
			
		} catch ( Exception e ) {
			String methodName = new Object() {}.getClass().getName();
			logger.error("{} Excel Build Error ", methodName, e);
			
			resultSuccess = false;
			
		}finally {
			handler.close();
		}
		
		return resultSuccess;
	}
	
	public List<SQLAutoPerformanceCompare> loadAfterSelectTextPlanListAll(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		
		String tobe_plan_hash_value = sqlAutoPerformanceCompare.getTobe_plan_hash_value();
		sqlAutoPerformanceCompare.setPlan_hash_value( tobe_plan_hash_value );
		
		return spsDao.loadAfterSelectTextPlanListAll(sqlAutoPerformanceCompare );
	}
	
	public List<SqlGrid> loadAfterPlanTree(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadAfterPlanTree(sqlAutoPerformanceCompare);
	}
	
	public List<SqlGrid> loadNoExecAfterPlanTree(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadNoExecAfterPlanTree(sqlAutoPerformanceCompare);
	}
	
	public List<SQLAutoPerformanceCompare> loadResultCount(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadResultCount(sqlAutoPerformanceCompare);
	}
	
	public int loadNumberOfSearch(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadNumberOfSearch(sqlAutoPerformanceCompare);
	}
	
	public List<SQLAutomaticPerformanceCheck> loadPerfChartData(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception{
		return aisqlpvDao.loadPerfChartData(sqlAutomaticPerformanceCheck);
	}
	
	public List<SQLAutoPerformanceCompare> loadResultList(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception{
		return aisqlpvDao.loadResultList(sqlAutoPerformanceCompare);
	}
	
	public boolean loadResultListExcel(SQLAutoPerformanceCompare sqlAutoPerformanceCompare
			, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean resultSuccess = true;
		ExcelDownHandler handler = new ExcelDownHandler(model, request, response);
		String sqlId = "";
		String sql = "";
		
		try {
			handler.open();
			
			sqlId = "omc.spop.dao.AISQLPVDao.loadResultListExcel";
			sql = handler.getSql(sqlSessionFactory, sqlId, sqlAutoPerformanceCompare);
			
			handler.buildInit("SQL별_성능_영향도_분석_결과", "AISQLPV_RESULT");
			handler.addRemoveHTML( new String[] { "SQL_TEXT_EXCEL" } );
			handler.buildDocument(sqlSessionFactory, sql);
			
			handler.writeDoc();
			
		} catch ( Exception e ) {
			String methodName = new Object() {}.getClass().getName();
			logger.error("{} Excel Build Error ", methodName, e);
			
			resultSuccess = false;
			
		}finally {
			handler.close();
		}
		
		return resultSuccess;
	}
	
	public List<VsqlText> loadSqlIdList(VsqlText vsqlText) throws Exception{
		return aisqlpvDao.loadSqlIdList(vsqlText);
	}
	
	public List<Sqls> sqlStateTrend(Sqls sqls) throws Exception{
		return aisqlpvDao.sqlStateTrend(sqls);
	}
	
	private boolean checkExistPac(SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Result result) {
		String sqlPerfPac = sqlAutoPerformanceCompare.getPerf_check_name();
		
		if ( sqlPerfPac != null && "".equals( sqlPerfPac ) == false ) {
			sqlPerfPac = StringUtil.removeHTML( sqlPerfPac );
			sqlPerfPac = StringUtil.removeSpecialChar( sqlPerfPac );
			
			sqlAutoPerformanceCompare.setPerf_check_name( sqlPerfPac.toString() );
		}
		
		int existCount = aisqlpvDao.getExistCount( sqlAutoPerformanceCompare );
		
		if ( existCount > 0) {
			result.setResult( false );
			result.setMessage("해당 프로젝트에 동일한 이름의 SQL점검팩이 존재합니다.");
			
			return false;
			
		}else {
			return true;
		}
	}
	
	private boolean isPrecessing(String yn) throws Exception{
		return ( "Y".equals(yn) ) ? true : false ;
	}
}