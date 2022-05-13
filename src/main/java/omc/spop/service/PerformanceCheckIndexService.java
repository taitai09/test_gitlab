package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.DeployPerfChkIndc;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.SelftunPlanTable;

/***********************************************************
 * 2019.01.03 임호경 최초작성
 **********************************************************/

public interface PerformanceCheckIndexService {

	/** 성능점검지표관리조회 */
	List<DeployPerfChkIndc> getDeployPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검지표관리 범위 조회 */
	List<DeployPerfChkIndc> getPerfCheckMethCd(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검지표관리 저장 */
	int saveDeployPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 성능점검지표관리 조회 후 엑셀다운 */
	List<LinkedHashMap<String, Object>> getDeployPerfChkIndcByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 점검지표 기준값 조회 */
	List<DeployPerfChkIndc> getWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	/** 프로그램 조회 */
	List<DeployPerfChkIndc> getPerfCheckProgramDivCd(DeployPerfChkIndc deployPerfChkIndc);

	/** 여부값 판정 구분 조회 */
	List<DeployPerfChkIndc> getYnDecideDivCd(DeployPerfChkIndc deployPerfChkIndc);

	/** 점검지표 조회 */
	List<DeployPerfChkIndc> getPerfCheckIndcId(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검 기준값 관리 저장 */
	int saveWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 성능점검 기준값 관리 중복 여부 */
	int checkWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검 기준값 관리 삭제 */
	int deleteWjPerfChkIndc(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검기준값관리 조회 후 엑셀다운 */
	List<LinkedHashMap<String, Object>> getWjPerfChkIndcByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/**
	 * 성능점검예외요청 조회
	 * 
	 * @throws Exception
	 */
	List<DeployPerfChkIndc> getProPerfExcReq(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 클릭시 바인드 리스트 조회 */
	List<DeployPerfChkIndc> getBindTableList(DeployPerfChkIndc deployPerfChkIndc);

	/** 클릭시 성능 점검 결과 리스트 조회 */
	List<DeployPerfChkIndc> getPerfCheckResultTableList(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검단계 조회 */
	List<DeployPerfChkIndc> getDeployPerfChkStep(DeployPerfChkIndc deployPerfChkIndc);

	/** 업무별 성능점검단계 조회 */
	List<DeployPerfChkIndc> getDeployPerfChkStepTestDB(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검단계 저장 */
	int saveDeployPerfChkStep(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 성능점검단계 조회 후 엑셀다운 */
	List<LinkedHashMap<String, Object>> getDeployPerfChkStepByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 업무별성능점검단계 저장 */
	int saveDeployPerfChkStepTestDB(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 셀렉트시 점검단계ID 조회 */
	List<DeployPerfChkIndc> getDeployPerfChkStepId(DeployPerfChkIndc deployPerfChkIndc);

	/** 프로그램 성능 점검 예외 요청취소 */
	int cancelDeployPerfChkExcptRequest(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 프로그램 성능 점검 예외 요청 */
	int requestDeployPerfChkExcptRequest(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 클릭시 예외처리란 체크박스 테이블 조회 */
	List<DeployPerfChkIndc> getDeployPerfChkDetailResult(DeployPerfChkIndc deployPerfChkIndc);

	List<DeployPerfChkIndc> getDeployPerfChkDetailResultByTabEspc(DeployPerfChkIndc deployPerfChkIndc);

	/** 클릭시 예외처리 탭 조회 */
	List<DeployPerfChkIndc> getDeployPerfChkDetailResultByTab(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검 예외 요청 - 예외처리 */
	int exceptionHandling(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 성능점검 예외 요청 - 반려 */
	int rejectRequest(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 성능점검 예외 요청 - 예외 삭제 - 그리드 갯수 */
	int checkExceptionRequestCnt(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검 예외 요청 - 프로그램 소스 - sqltext */
	String getProgramSourceDesc(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검 예외 요청 삭제 - ExceptionCd확인 */
	DeployPerfChkIndc checkExceptionPrcStatusCd(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검 예외 삭제 - 조회 */
	List<DeployPerfChkIndc> getProPerfExcReqDel(DeployPerfChkIndc deployPerfChkIndc);

	/** 클릭시 예외처리 삭제 탭 조회 */
	List<DeployPerfChkIndc> getPerfCheckResultDelTableList(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검 예외 요청 - 예외처리 */
	int exceptionDelete(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 성능점검예외요청 조회 */
	List<LinkedHashMap<String, Object>> getProPerfExcReqByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 최종점검단계 조회 */
	List<DeployPerfChkIndc> getPerfCheckStep(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능점검예외삭제 조회 후 엑셀다운 */
	List<LinkedHashMap<String, Object>> getProPerfExcReqDelByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 업무별 파싱 스키마 관리 조회 */
	List<DeployPerfChkIndc> getDeployPerfChkParsingSchema(DeployPerfChkIndc deployPerfChkIndc);

	/** 업무별 파싱 스키마 관리 엑셀 다운 */
	List<LinkedHashMap<String, Object>> getDeployPerfChkParsingSchemaByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 업무별 파싱 스키마 관리 저장 */
	int saveDeployPerfChkParsingSchema(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 업무별 성능점검단계 관리 엑셀 다운 */
	List<LinkedHashMap<String, Object>> getDeployPerfChkStepTestDBByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 업무별 성능점검단계 관리 파싱스키마 사용여부 체크 */
	int checkDeployPerfChkParsingSchemaName(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 예외 처리 현황 조회 */
	List<DeployPerfChkIndc> getPerfChkIndcListState(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 예외 처리 사유별 현황 조회 */
	List<DeployPerfChkIndc> getPerfChkIndcListState2(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 예외 처리 현황 조회 엑셀 다운 */
	List<LinkedHashMap<String, Object>> getPerfChkIndcListState2ByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 예외 처리 사유별 현황 엑셀 다운 */
	List<LinkedHashMap<String, Object>> getPerfChkIndcListStateByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 이력 조회 조회 */
	List<DeployPerfChkIndc> getProPerfExcReqState(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 이력 조회 테이블 조회 */
	List<DeployPerfChkIndc> getPerfCheckResultStateTableList(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 이력 조회 사유별 현황 엑셀 다운 */
	List<LinkedHashMap<String, Object>> getProPerfExcReqStateByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 현황 조회 */
	List<DeployPerfChkIndc> getPerfCheckState0(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 현황 조회 */
	List<DeployPerfChkIndc> getPerfCheckState1(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 현황 조회2 */
	List<DeployPerfChkIndc> getPerfCheckState2(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 현황> CM점검현황 엑셀 다운 */
	List<LinkedHashMap<String, Object>> getPerfCheckState0ByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 현황>프로그램점검현황 엑셀 다운 */
	List<LinkedHashMap<String, Object>> getPerfCheckState1ByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 현황>지표별부적합현황 엑셀 다운 */
	List<LinkedHashMap<String, Object>> getPerfCheckState2ByExcelDown(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 수행 */
	Result perfCheckExecute(DeployPerfChkIndc deployPerfChkIndc) throws Exception;

	/** 성능점검 예외 요청 - ExceptionCd확인 */
	DeployPerfChkIndc checkExceptionPrcStatusCd2(DeployPerfChkIndc deployPerfChkIndc);

	/** 일괄 프로그램 성능 점검 예외 요청 */
	int multiRequestDeployPerfChkExcptRequest(DeployPerfChkIndc deployPerfChkIndc);

	/** 일괄 프로그램 성능 점검 예외 처리 */
	int multiExceptionHandling(DeployPerfChkIndc deployPerfChkIndc);

	/** 성능 점검 예외 요청 플랜 트리값 */
	List<SelftunPlanTable> getDeployPerfChkPlanTableList(DeployPerfChkIndc deployPerfChkIndc);

}
