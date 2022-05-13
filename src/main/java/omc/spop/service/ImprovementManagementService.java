package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import omc.spop.model.Cd;
import omc.spop.model.FullscanSql;
import omc.spop.model.NewSql;
import omc.spop.model.PlanChangeSql;
import omc.spop.model.Result;
import omc.spop.model.SqlImprovementType;
import omc.spop.model.SqlTuning;
import omc.spop.model.SqlTuningAttachFile;
import omc.spop.model.SqlTuningHistory;
import omc.spop.model.SqlTuningStatusHistory;
import omc.spop.model.TempUsageSql;
import omc.spop.model.TopSqlUnionOffloadSql;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.model.Users;

/***********************************************************
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 * 2020.06.03	이재우	기능개선
 **********************************************************/

public interface ImprovementManagementService {
	/** 튜닝요청 INSERT*/
	int insertImprovement(MultipartHttpServletRequest req, TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 - 튜닝요청 정보조회 */
	TuningTargetSql getImprovementInfo(TuningTargetSql tuningTargetSql) throws Exception;

	/** 성능개선 요청 UPDATE */
	int updateImprovement(HttpServletRequest req) throws Exception;	
	
	/** 성능개선요약 */
	TuningTargetSql getImprovementSummary(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능개선 리스트 */
	List<TuningTargetSql> improvementStatusList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능개선 리스트 엑셀 */
	List<LinkedHashMap<String, Object>> improvementStatusList4Excel(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 INFO */
	SqlTuning getSQLTuning(TuningTargetSql tuningTargetSql) throws Exception;	
	
	/** 성능 개선 관리 상세 - SQL상세 (선정) */
	TuningTargetSql getSelection(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - SQL상세 (요청) */
	TuningTargetSql getRequest(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - SQL상세 (FULL SCAN) */
	FullscanSql getFullScan(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - SQL상세 (PLAN변경) */
	PlanChangeSql getPlanChange(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - SQL상세 (신규SQL) */
	NewSql getNewSQL(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - SQL상세 (TEMP과다사용) */
	TempUsageSql getTempOver(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - SQL 개선사항 */
	TuningTargetSql getImprovements(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - 완료 사유 구분 리스트 */
	List<Cd> completeReasonList() throws Exception;
	
	/** 성능 개선 관리 상세 - 완료 사유상세 구분 리스트 */
	List<Cd> completeReasonDetailList() throws Exception;
	
	/** 성능 개선 관리 상세 - 선택한 완료 사유상세 리스트 */
	List<SqlImprovementType> sqlImprovementTypeList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - SQL 개선 이력 리스트 */
	List<SqlTuningHistory> sqlImproveHistoryList(SqlTuningHistory sqlTuningHistory) throws Exception;
	
	/** 성능 개선 관리 상세 - SQL 개선 이력 상세 */
	SqlTuningHistory getSQLImproveHistory(SqlTuningHistory sqlTuningHistory) throws Exception;	

	/** 성능 개선 관리 상세 - BIND SET 리스트 */
	List<TuningTargetSqlBind> bindSetList(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - BIND 리스트 */
	List<TuningTargetSqlBind> sqlBindList(TuningTargetSql tuningTargetSql) throws Exception;	
	
	/** 성능 개선 관리 상세 - 튜닝중으로 변경 */
	int saveTuning(TuningTargetSql tuningTargetSql) throws Exception;

	/** 성능 개선 관리 상세 - 접수 취소로 변경 */
	int saveReceiptCancel(TuningTargetSql tuningTargetSql) throws Exception;	
	
	/** 성능 개선 관리 - 리스트에서 여러건을 튜닝취소로 변경 */
	int saveTuningCancelAll(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - 튜닝취소로 변경 */
	int saveTuningCancel(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - 반려처리로 변경 */
	int saveCancel(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - 튜닝종료 처리로 변경 */
	int saveEnd(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - 튜닝종료 처리로 변경 */
	int savePreCheck(TuningTargetSql tuningTargetSql) throws Exception;	
	
	/** 성능 개선 관리 상세 - 튜닝완료 처리로 변경 */
	int saveComplete(TuningTargetSql tuningTargetSql, HttpServletRequest req) throws Exception;

	/** 성능 개선 관리 상세 - 튜닝완료/임시 저장 처리로 변경 
	 * @param req */
	int completeSqlTuning(TuningTargetSql tuningTargetSql, HttpServletRequest req) throws Exception;
	
	/** 성능 개선 관리 상세 - 임시 저장 */
	int tempSaveComplete(TuningTargetSql tuningTargetSql, HttpServletRequest req) throws Exception;

	/** 성능 개선 관리 상세 - 결과값 변경 저장 */
	int modifyTuningResult(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - 튜닝담당자 일괄 지정 */
	Result saveTunerAssignAll(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - 튜닝담당자 지정 */
	int saveTunerAssign(TuningTargetSql tuningTargetSql) throws Exception;
	
	/** 성능 개선 관리 상세 - 업무 담당자 변경 */
	int changeWorkUser(TuningTargetSql tuningTargetSql) throws Exception;	
	
	/** 성능 개선 관리 상세 - 프로세스 처리이력 팝업 */
	List<SqlTuningStatusHistory> processHistoryList(SqlTuningStatusHistory sqlTuningStatusHistory) throws Exception;

//	String getUsersWrkjobCdDbid(String wrkjob_cd);

	/** 튜닝요청 - 초기값 설정 */
	int saveInitSetting(TuningTargetSql tuningTargetSql);
	
	/** 튜닝요청 - 초기값세팅 */
	TuningTargetSql getInitValues(String user_id);

	String getTuningCompleteDueDay();

	SqlTuning getImprBeforeAfter(TuningTargetSql tuningTargetSql);

	/** 성능 개선 관리 상세 - 튜닝요청 취소로 변경 */
	int saveRequestCancelAll(TuningTargetSql tuningTargetSql) throws Exception;

	/** 성능 개선 관리 상세 - SQL상세 (선정) */
	TopSqlUnionOffloadSql getTopSqlUnionOffloadSql(TuningTargetSql tuningTargetSql);

	/** 성능 개선 관리 상세 - 첨부파일 목록*/
	List<SqlTuningAttachFile> readTuningAttachFiles(TuningTargetSql tuningTargetSql);
	
	/** 성능 개선 관리 상세 - 첨부파일 삭제 */
	int deleteTuningAttachFile(SqlTuningAttachFile sqlTuningAttachFile);
	
	/** 성능 개선 관리 상세 - 배포후 성능점검 */
	TuningTargetSql getDeployAfterPerf(TuningTargetSql tuningTargetSql);
	
	/** 성능 개선 관리 상세 - AWR/VSQL 판단 */
	int getPerfSourceType(TuningTargetSql tuningTargetSql) throws Exception;
	
	Users getUsersInfo( String users ) throws Exception;
}
