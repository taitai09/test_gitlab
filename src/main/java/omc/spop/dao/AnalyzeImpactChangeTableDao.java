package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.SqlPerfImplAnalTable;

/***********************************************************
 * 2021.02.16	황예지	최초작성
 **********************************************************/
@Repository
public class AnalyzeImpactChangeTableDao {
//	AutoPerformanceCompareBetweenDbServiceMapper
//	AnalyzeImpactChangeTableMapper
	
	@Autowired
	private SqlSession sqlSession;

	public List<SqlPerfImplAnalTable> loadTableCHGPerfChkTargetLeftList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare){
		
		return sqlSession.selectList(
				"omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.loadTableCHGPerfChkTargetLeftList",
				sqlAutoPerformanceCompare);
	}

	public List<SQLAutoPerformanceCompare> loadTableCHGPerfChkTargetRightList(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {

		return sqlSession.selectList(
				"omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.loadTableCHGPerfChkTargetRightList",
				sqlAutoPerformanceCompare);
	}

	public int updateSqlAutoPerformance(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		return sqlSession.update(
				"AnalyzeImpactChangeTableMapper.updateSqlAutoPerformance",sqlAutoPerformanceCompare);
	}
	
	public int insertTableCHGPerfChkTargetVSQL(SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) {

		return sqlSession.insert(
				"omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.insertTableCHGPerfChkTargetVSQL",sqlAutomaticPerformanceCheck);
	}
	
	public List<LinkedHashMap<String, Object>> excelDownload(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		return sqlSession.selectList(
				"omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.tableCHGPerfChkExcelDownload",sqlAutoPerformanceCompare);
	}

	public int getPerformanceResultCount(SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		return (Integer) sqlSession.selectOne(
				"omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.getTableCHGPerfChkResultCount",sqlAutoPerformanceCompare);
	}

	public List<LinkedHashMap<String, Object>> excelTuningDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		return sqlSession.selectList(
				"omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.excelTuningDownload_ex",sqlAutoPerformanceCompare);
	}

	public List<LinkedHashMap<String, Object>> excelTuningDetailDownload(
			SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		return sqlSession.selectList(
				"omc.spop.dao.AutoPerformanceCompareBetweenDbServiceDao.excelTuningDetailDownload_ex",sqlAutoPerformanceCompare);
	}
	
}
