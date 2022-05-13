package omc.spop.dao;

import omc.spop.model.PerformanceCheckMng;

/***********************************************************
 * 2018.09.11 	임호경	 최초작성
 **********************************************************/

public interface IqmsDao {
	/** 
	/***************************************************************
	인터페이스명 : 개발확정
	
	INPUT PARAM
	- cmid        : 배포ID(DEPLOY_ID)
	- cmpknm      : 배포명(DEPLOY_NM)
	- cmdepday    : 배포예정일자(DEPLOY_EXPECTED_DAY)
	- cmusrnum    : 배포요청자ID(DEPLOY_REQUESTER_ID)
	- jobcd       : 배포업무분류코드(DEPLOY_WRKJOB_DIV_CD)
	- cmcreateday : 배포요청일시(DEPLOY_REQUEST_DT)
	***************************************************************/
	/**
	GRP_CD_ID	CD	CD_NM
	1056	00	개발확정
	1056	01	점검중
	1056	02	점검완료
	1056	03	배포완료
	1056	04	개발확정취소
	1056	05	배포삭제
	 */
	
	/** 1 : 기업 업무코드로 OPENPOP 업무코드 조회  */
	PerformanceCheckMng selectWrkjobCd(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 2 : 이전 배포ID(CMID)가 존재하면 최종성능점검단계ID로 업데이트  */
	String selectBeforePerfCheckId(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 3 : 이전에 개발확정/점검중/점검완료상태의 이전 배포ID(CMID)가 존재하면 개발확정취소(04) 한다.  */
	/** 3.1 : 성능점검 상태 변경 이력 생성  */
	int insertIntoSelectDeployPerfChkStatusHistory(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 3.2 : 개발확정취소로 업데이트  */
	int updateDeployPerfChkCancel(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 4 : 최종성능점검단계ID 조회  */
	String selectLastPerfCheckStepId(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 5 : PERF_CHECK_ID 채번  */
	String selectNextPerfCheckId() throws Exception;
	
	/** 6 : 변수 세팅  */
	/** 배포성능점검상태코드
	DEPLOY_CHECK_STATUS_CD = '00';  개발확정
	 */
	/** 배포성능상태변경자ID
	DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
	 */
	/** 배포성능점검상태변경사유
	DEPLOY_CHECK_STATUS_CHG_WHY = '배포요청';
	 */
	/** 성능점검요청유형코드 
	PERF_CHECK_REQUEST_TYPE_CD = 'A';  일반
	 */
	/** 성능점검요청채널구분코드 
	PERF_CHECK_REQUEST_CN_DIV_CD = '01';  배포시스템(CM)
	 */
	/** 에이전트상태구분코드
	AGENT_STATUS_DIV_CD = '1';  대기
	 */
	/** 7 : 배포성능점검기본(DEPLOY_PERF_CHK) INSERT  */
	int insertDeployPerfChk(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 8 : 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT  */
	int insertDeployPerfChkStatusHistory(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 9 : 배포성능점검단계별수행내역(DEPLOY_PERF_CHK_STEP_EXEC) INSERT  */
	int insertDeployPerfChkStepExec(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 9 : 배포성능점검단계별수행내역(DEPLOY_PERF_CHK_STEP_EXEC) INSERT - ESPC */
	int insertDeployPerfChkStepExecEspc(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/***************************************************************
	인터페이스명 : CM완료
	
	INPUT PARAM
	- cmid        : 배포ID(DEPLOY_ID)
	- status      : 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	***************************************************************/
	
	/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회  */
	String selectMaxPerfCheckId(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 2 : 변수 세팅  */
	/** 배포성능점검상태코드
	DEPLOY_CHECK_STATUS_CD = '03';  배포완료
	 */
	/** 배포성능상태변경자ID
	DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
	 */
	/** 배포성능점검상태변경사유
	DEPLOY_CHECK_STATUS_CHG_WHY = '배포완료';
	 */
	
	/** 3 : 배포성능점검기본(DEPLOY_PERF_CHK) UPDATE  */
	int updateDeployPerfChk(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT
	 * insertDeployPerfChkStatusHistory
	 */

	
	/***************************************************************
	인터페이스명 : CM취소
	
	INPUT PARAM
	- cmid        : 배포ID(DEPLOY_ID)
	- status      : 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	***************************************************************/
	
	/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회  */
//	String selectMaxPerfCheckId(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 2 : 변수 세팅  */
	/** 배포성능점검상태코드
	DEPLOY_CHECK_STATUS_CD = '04';  배포완료
	 */
	/** 배포성능상태변경자ID
	DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
	 */
	/** 배포성능점검상태변경사유
	DEPLOY_CHECK_STATUS_CHG_WHY = '배포취소';
	 */
	
	/** 3 : 배포성능점검기본(DEPLOY_PERF_CHK) UPDATE
	 * updateDeployPerfChk
	 */

	/** 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT
	 * insertDeployPerfChkStatusHistory
	 */

	/**
	 * 2019-06-13  로직 추가
	 * SQL성능점검대상 프로그램 삭제처리함(다음 배포시에 성능점검대상으로 올라와야 함 
	 */
	int updateDeployPerfChkAllPgmDelYn(PerformanceCheckMng performanceCheckMng);

	/**
	 * 개발확정 취소 프로시저 콜
	 * @param performanceCheckMng
	 * @return
	 */
	String callSpSpopDeployStatusProc(PerformanceCheckMng performanceCheckMng);
	
	/***************************************************************
	인터페이스명 : CM삭제
	
	INPUT PARAM
	- cmid        : 배포ID(DEPLOY_ID)
	- status      : 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	***************************************************************/
	
	/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회  */
//	String selectMaxPerfCheckId(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 2 : 변수 세팅  */
	/** 배포성능점검상태코드
	DEPLOY_CHECK_STATUS_CD = '05';  배포삭제
	 */
	/** 배포성능상태변경자ID
	DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
	 */
	/** 배포성능점검상태변경사유
	DEPLOY_CHECK_STATUS_CHG_WHY = '배포삭제';
	 */
	
	/** 3 : 배포성능점검기본(DEPLOY_PERF_CHK) UPDATE
	 *  updateDeployPerfChk
	 */
	
	/** 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT
	 * insertDeployPerfChkStatusHistory
	 */

	/**
	 * 2019-06-13  로직 추가
	 * SQL성능점검대상 프로그램 삭제처리함(다음 배포시에 성능점검대상으로 올라와야 함 
	 */
//	int updateDeployPerfChkAllPgmDelYn(PerformanceCheckMng performanceCheckMng);

	/***************************************************************
	인터페이스명 : CM상태동기화
	
	INPUT PARAM
	- cmid        : 배포ID(DEPLOY_ID)
	- status      : 배포성능점검상태코드(DEPLOY_CHECK_STATUS_CD)
	***************************************************************/
	
	/** JSON 리스트 갯수만큼 반복수행  */
	/** LOOP  */ 
	
	/** 1 : 배포ID(CMID)로 최종 성능점검ID 조회  */
//	String selectMaxPerfCheckId(PerformanceCheckMng performanceCheckMng) throws Exception;
	
	/** 2 : 변수 세팅  */
	/** 배포성능점검상태코드, 배포성능점검상태변경사유  */
	/** 
	IF STATUS = '3' THEN
	    DEPLOY_CHECK_STATUS_CD = '03';  배포완료
	    DEPLOY_CHECK_STATUS_CHG_WHY = '배포완료';
	ELSIF STATUS = '4' THEN
	    DEPLOY_CHECK_STATUS_CD = '04';  배포취소
	    DEPLOY_CHECK_STATUS_CHG_WHY = '배포취소';
	ELSIF STATUS = '5' THEN
	    DEPLOY_CHECK_STATUS_CD = '05';  배포삭제
	    DEPLOY_CHECK_STATUS_CHG_WHY = '배포삭제';
	END IF;
	 */
	/** 배포성능상태변경자ID
	DEPLOY_CHECK_STATUS_UPDATER_ID = 'dbmanager';
	 */
	
	/** 3 : 배포성능점검기본(DEPLOY_PERF_CHK) UPDATE
 	 * updateDeployPerfChk
	 */

	/** 배포성능점검상태변경이력(DEPLOY_PERF_CHK_STATUS_HISTORY) INSERT
	 * insertDeployPerfChkStatusHistory
	 */

	/*
	 * 업무코드 유효성 검증
	 */
	int getValidateWrkjobDivCd(String wrkjobDivCd) throws Exception;
	
	/*
	 * 배포ID에 해당하는 성능점검ID가 존재하는지 검증
	 */
	String getValidateDeployID(String deployId) throws Exception;
}
