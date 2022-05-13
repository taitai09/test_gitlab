package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.model.AccPathExec;
import omc.spop.model.DbioExplainExec;
import omc.spop.model.DbioLoadFile;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.ProjectUnitLoadFile;
import omc.spop.model.VsqlGatheringModule;
import omc.spop.model.VsqlParsingSchema;
import omc.spop.model.VsqlSnapshot;

/***********************************************************
 * 2017.09.27	이원식	최초작성
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface IndexDesignPreProcessingService {
	/** 수집SQL 조건설정 - 수집대상 테이블 리스트 */
	List<VsqlParsingSchema> collectionTargetList(VsqlParsingSchema vsqlParsingSchema) throws Exception;
	
	/** 수집SQL 조건설정 - 적용대상 테이블 리스트 */
	List<VsqlParsingSchema> applyTargetList(VsqlParsingSchema vsqlParsingSchema) throws Exception;
	
	/** 수집SQL 조건설정 - GV$ 정보 */
	VsqlParsingSchema glovalViewInfo(VsqlParsingSchema vsqlParsingSchema) throws Exception;

	/** 수집SQL 조건설정 - 수집모듈 리스트 */
	List<VsqlGatheringModule> collectionModuleList(VsqlGatheringModule vsqlGatheringModule) throws Exception;
	
	/** 수집SQL 조건설정 저장 */
	void saveSetCollectionCondition(VsqlParsingSchema vsqlParsingSchema) throws Exception;
	
	/** 수집 SQL 조건절 파싱 리스트 */
	List<AccPathExec> parsingCollectionTermsList(AccPathExec accPathExec) throws Exception;
	
	/** 수집 SQL 조건절 파싱 - SNAPSHOT 팝업 리스트 */
	List<VsqlSnapshot> snapShotList(VsqlSnapshot vsqlSnapshot) throws Exception;	
	
	/** 수집 SQL 조건절 파싱 - 저장  */
	void insertParsingCollectionTerms(VsqlSnapshot vsqlSnapshot) throws Exception;	
	
	/** SQL 적재 리스트 */
	List<DbioLoadFile> loadSQLList(DbioLoadFile dbioLoadFile) throws Exception;
	
	/** SQL 적재 파일 UPLOAD 및 파싱 후 INSERT */
	void loadSQLFile(MultipartFile file, DbioLoadFile dbioLoadFile) throws Exception;
	
	/** SQL 적재 파일 UPLOAD 및 파싱 후 INSERT - project unit */
	int projectUnitLoadSQLFile(MultipartFile file, ProjectUnitLoadFile projectLoadFile) throws Exception;
	
	/** 적재SQL 실행계획생성 리스트 */
	List<DbioLoadSql> loadActionPlanList(DbioLoadSql dbioLoadSql) throws Exception;

	/** 적재SQL 실행계획생성 리스트 엑셀 다운로드 */
	List<LinkedHashMap<String, Object>> loadActionPlanList4Excel(DbioLoadSql dbioLoadSql) throws Exception;	
	
	/** 적재SQL 실행계획생성 - SQL_TEXT 조회 */
	DbioLoadSql actionPlanInfo(DbioLoadSql dbioLoadSql) throws Exception;	
	
	/** 적재SQL 실행계획생성 - MAX ExplainExecSeq 조회 */
	String getMaxExplainExecSeq(DbioLoadSql dbioLoadSql) throws Exception;	
	
	/** 적재SQL 실행계획생성 - 선행작업 */
	List<DbioLoadSql> isTaskLoadActionPlan(DbioLoadSql dbioLoadSql) throws Exception;
	
	/** 적재SQL 실행계획생성 INSERT */
	int insertLoadActionPlan(DbioLoadSql dbioLoadSql) throws Exception;	
	
	/** 적재SQL 실행계획생성 개숫 조회 */
	DbioExplainExec planExecCnt(DbioExplainExec dbioExplainExec) throws Exception;
	
	/** 적재SQL 실행계획생성 - 로그 건수 정보 */
	DbioExplainExec selectActionPlanLog(DbioExplainExec dbioExplainExec) throws Exception;
	
	/** 적재SQL 실행계획생성 - 강제 업데이트 */
	int updateForceComplete(DbioExplainExec dbioExplainExec) throws Exception;
	
	/** 적재SQL 조건절 파싱 - ExplainList 리스트 */
	List<DbioLoadFile> explainList(DbioLoadFile dbioLoadFile) throws Exception;
	
	/** 적재SQL 조건절 파싱 - AccessPathList 리스트 */
	List<AccPathExec> accessPathList(AccPathExec accPathExec) throws Exception;
	
	/** 적재SQL 조건절 파싱 insert */
	void insertParseLoadingCondition(AccPathExec accPathExec) throws Exception;

	boolean downloadLargeExcel(DbioLoadSql dbioLoadSql, Model model, HttpServletRequest request,
			HttpServletResponse response);
}
