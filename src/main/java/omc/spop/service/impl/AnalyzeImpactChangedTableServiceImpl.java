package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.AnalyzeImpactChangeTableDao;
import omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.SqlPerfImplAnalTable;
import omc.spop.service.AnalyzeImpactChangedTableService;

@Service("AnalyzeImpactChangedTableService")
public class AnalyzeImpactChangedTableServiceImpl implements AnalyzeImpactChangedTableService {
	
	private static final Logger logger = LoggerFactory.getLogger(
			AutoPerformanceCompareBetweenDbServiceImpl.class );
	
	@Autowired
	private AutoPerformanceCompareBetweenDbServiceDao autoPerformanceCompareBetweenDbServiceDao;
	
	@Autowired
	private AnalyzeImpactChangeTableDao analyzeImpactChangeTableDao;
	
	@Override
	public Result updateSqlAutoPerformance(
			SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) throws Exception {
		Result result = new Result();
		
		try {
			List<String> ownerStrList = new ArrayList<String>();
			List<String> moduleStrList = new ArrayList<String>();
			List<String> tableNameStrList = new ArrayList<String>();
			
			if ( "".equals(sqlAutomaticPerformanceCheck.getOwner_list()) ) {
				result.setMessage("OWNER 값을 확인 후 다시 성능비교 를 실행해 주세요.");
				result.setResult(false);
				return result;
			} else {
				ownerStrList.add(sqlAutomaticPerformanceCheck.getOwner_list());
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
			
			if ( sqlAutomaticPerformanceCheck.getTable_name_list().indexOf(',') != -1 ) {
				String[] strTableNameArr = sqlAutomaticPerformanceCheck.getTable_name_list().split(",");
				
				for ( String tableNameStr : strTableNameArr ) {
					if ( tableNameStr.trim().equals("") ) {
						result.setMessage("TABLE_NAME 값을 확인 후 다시 성능비교 를 실행해 주세요.");
						result.setResult(false);
						return result;
					} else {
						tableNameStrList.add( tableNameStr.trim() );
					}
				}
			}
			
			sqlAutomaticPerformanceCheck.setStrTableNameList( tableNameStrList );
			
			if(sqlAutomaticPerformanceCheck.getLiteral_except_yn() == null ||
				"".equals(sqlAutomaticPerformanceCheck.getLiteral_except_yn()) ) {
				sqlAutomaticPerformanceCheck.setLiteral_except_yn("N");
			}
			
			if(sqlAutomaticPerformanceCheck.getAll_sql_yn() == null ||
					"".equals(sqlAutomaticPerformanceCheck.getAll_sql_yn()) ) {
				sqlAutomaticPerformanceCheck.setAll_sql_yn("N");
			}
			
			if ( "".equals( sqlAutomaticPerformanceCheck.getSql_time_limt_cd() ) ) {
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
				
				String sqlSourceType = sqlAutomaticPerformanceCheck.getPerf_check_sql_source_type_cd();
				
				if( "1".equals(sqlSourceType) ) {
					//AWR인 경우
					autoPerformanceCompareBetweenDbServiceDao.insertSqlAutoPerformanceTargetForAWR(
							sqlAutomaticPerformanceCheck );//
					
					autoPerformanceCompareBetweenDbServiceDao.insertTableCHGPerfChkTarget(
							sqlAutomaticPerformanceCheck );
				}else {
					//전체SQL인 경우
					autoPerformanceCompareBetweenDbServiceDao.insertSqlAutoPerformanceTarget(
							sqlAutomaticPerformanceCheck );
					
					analyzeImpactChangeTableDao.insertTableCHGPerfChkTargetVSQL(
							sqlAutomaticPerformanceCheck );
				
				logger.debug("result 수 ====> "+ successResult);
				
				}
			} else {
				result.setResult(false);
				result.setMessage("성능비교를 실행하지 못했습니다. <br>조건 값을 다시 확인해주세요.");
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
	public int getPerformanceResultCount(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		return analyzeImpactChangeTableDao.getPerformanceResultCount(
				sqlAutoPerformanceCompare );
	}
	
	@Override
	public List<SqlPerfImplAnalTable> loadTableCHGPerfChkTargetLeftList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		
		return analyzeImpactChangeTableDao.loadTableCHGPerfChkTargetLeftList(sqlAutoPerformanceCompare);
	}
	
	@Override
	public List<SQLAutoPerformanceCompare> loadTableCHGPerfChkTargetRightList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		
		return analyzeImpactChangeTableDao.loadTableCHGPerfChkTargetRightList(sqlAutoPerformanceCompare);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		
		return analyzeImpactChangeTableDao.excelDownload(sqlAutoPerformanceCompare );
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> excelTuningDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		
		return analyzeImpactChangeTableDao.excelTuningDownload(
				sqlAutoPerformanceCompare );
	}
}
