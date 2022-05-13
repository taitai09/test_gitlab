package omc.spop.service;

import java.util.List;

import omc.spop.model.BeforePerfExpect;
import omc.spop.model.BeforePerfExpectSqlStat;
import omc.spop.model.OdsUsers;
import omc.spop.model.SqlPerfImplAnalSql;
import omc.spop.model.SqlPerfImplAnalTable;
import omc.spop.model.SqlPerfImpluenceAnalysis;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface SQLInfluenceDiagnosticsService {
	/** SQL영향도 진단 - DB변경 SQL영향도 진단 리스트 */
	List<BeforePerfExpect> topSQLDiagnosticsList(BeforePerfExpect beforePerfExpect) throws Exception;
	
	/** SQL영향도 진단 - DB변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케줄 정보 조회 */
	BeforePerfExpect getTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect) throws Exception;	
	
	/** SQL영향도 진단 - DB변경 SQL영향도 진단 - 스케쥴 등록/수정 */
	int saveTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect) throws Exception;
	
	/** SQL영향도 진단 - DB변경 SQL영향도 진단 - 스케쥴 삭제 */
	void deleteTOPSQLDiagnostics(BeforePerfExpect beforePerfExpect) throws Exception;	
	
	/** SQL영향도 진단 - DB변경 SQL영향도 진단 - 상세 리스트 */
	List<BeforePerfExpectSqlStat> topSQLDiagnosticsDetailList(BeforePerfExpectSqlStat beforePerfExpectSqlStat) throws Exception;
	
	/** SQL영향도 진단 - DB변경 SQL영향도 진단 - 상세 - SQL Profile Update */
	int updateSQLProfile_TOPSQL(BeforePerfExpectSqlStat beforePerfExpectSqlStat) throws Exception;
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 리스트 */
	List<SqlPerfImpluenceAnalysis> objectImpactDiagnosticsList(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis) throws Exception;
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케줄 정보 조회  */
	SqlPerfImpluenceAnalysis getObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis) throws Exception;
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 분석대상 테이블목록[오른쪽] 조회  */
	List<SqlPerfImplAnalTable> getTargetTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable) throws Exception;	
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 테이블Owner 목록 조회  */
	List<OdsUsers> getTableOwner(OdsUsers odsUsers) throws Exception;
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 테이블목록[왼쪽] 조회  */
	List<SqlPerfImplAnalTable> getSelectTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable) throws Exception;	
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케쥴 등록/수정 */
	int saveObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis) throws Exception;
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케쥴 삭제 */
	void deleteObjectImpactDiagnostics(SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis) throws Exception;	
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 테이블 리스트 */
	List<SqlPerfImplAnalTable> objectImpactDiagnosticsTableList(SqlPerfImplAnalTable sqlPerfImplAnalTable) throws Exception;
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 테이블 리스트 - SQL 상세 리스트 */
	List<SqlPerfImplAnalSql> objectImpactDiagnosticsTableDetailList(SqlPerfImplAnalSql sqlPerfImplAnalSql) throws Exception;
	
	/** SQL영향도 진단 - 오브젝트변경 SQL영향도 진단 - 테이블 리스트 - SQL 상세 - SQL PROFILE UPDATE */
	int updateSQLProfile_Object(SqlPerfImplAnalSql sqlPerfImplAnalSql) throws Exception;
}
