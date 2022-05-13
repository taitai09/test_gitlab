package omc.spop.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelDownHeaders {
	private static final Logger logger = LoggerFactory.getLogger(ExcelDownHeaders.class);
	/**
	 * 엑셀 헤더 영문명은 쿼리와 동일하게 대문자로 한다.
	 */
	/**
	 * 성능점검관리
	 */
	public String[] PERF_CHECK_MNG_KO = { "배포ID", "배포명", "업무명", "점검상태", "최종점검단계", "최종점검단계결과","전체(신규/변경/동일)","완료방법",  "배포요청자",
			"배포요청일시", "점검요청일시", "점검완료일시", "점검결과통보여부", "점검결과통보일시", "점검결과통보횟수" , "성능점검ID"};
	public String[] PERF_CHECK_MNG_EN = { "DEPLOY_ID", "DEPLOY_NM", "WRKJOB_CD_NM", "DEPLOY_CHECK_STATUS_NM",
			"PERF_CHECK_STEP_NM", "PERF_CHECK_RESULT_DIV_NM", "CHECK_TGT_CNT", "PERF_CHECK_COMPLETE_METH_NM",
			"DEPLOY_REQUESTER_NM", "DEPLOY_REQUEST_DT", "DEPLOY_CHECK_STATUS_UPDATE_DT", "PERF_CHECK_COMPLETE_DT",
			"CHECK_RESULT_ANC_YN", "LAST_CHECK_RESULT_ANC_DT", "CHECK_RESULT_ANC_CNT", "PERF_CHECK_ID" };
	/**
	 * 성능점검단계
	 */
	public String[] PERF_CHECK_STEP_KO = { "점검단계ID", "점검단계", "점검완료", "점검대상DB", "점검요청일시", "점검완료일시", "점검결과", "전체건수",
			"점검제외건수", "적합건수", "부적합건수", "오류건수", "미수행건수" };
	public String[] PERF_CHECK_STEP_EN = { "PERF_CHECK_STEP_ID", "PERF_CHECK_STEP_NM", "PERF_TEST_COMPLETE_YN",
			"PERF_TEST_DB_NAME", "PERF_CHECK_REQUEST_DT", "PERF_CHECK_COMPLETE_DT", "PERF_CHECK_RESULT_DIV_NM",
			"TOTAL_CNT", "EXCEPTION_CNT", "PASS_CNT", "FAIL_CNT", "ERR_CNT", "TEST_MISS_CNT" };
	/**
	 * 성능점검결과
	 */
	public String[] PERF_CHECK_RESULT_KO = { "점검결과", "개발구분", "SQL명령유형", "프로그램명", "SQL식별자(DBIO)", "프로그램유형", "수행일시",
			"성능점검ID", "프로그램ID", };
	public String[] PERF_CHECK_RESULT_EN = { "PERF_CHECK_RESULT_DIV_NM", "PROGRAM_DVLP_DIV_NM", "SQL_COMMAND_TYPE_NM",
			"PROGRAM_NM", "DBIO", "PROGRAM_TYPE_NM", "PROGRAM_EXEC_DT", "PERF_CHECK_ID", "PROGRAM_ID", };
	/**
	 * 성능검증관리
	 */
	public String[] PERF_INSPECT_MNG_KO = { "배포ID", "배포명", "업무명", "검증상태", "최종검증단계", "최종검증단계결과","전체(신규/변경/동일)","완료방법",  "배포요청자",
			"배포요청일시", "검증요청일시", "검증완료일시", "검증결과통보여부", "검증결과통보일시", "검증결과통보횟수" , "성능검증ID"};
	public String[] PERF_INSPECT_MNG_EN = { "DEPLOY_ID", "DEPLOY_NM", "WRKJOB_CD_NM", "DEPLOY_CHECK_STATUS_NM",
			"PERF_CHECK_STEP_NM", "PERF_CHECK_RESULT_DIV_NM", "CHECK_TGT_CNT", "PERF_CHECK_COMPLETE_METH_NM",
			"DEPLOY_REQUESTER_NM", "DEPLOY_REQUEST_DT", "DEPLOY_CHECK_STATUS_UPDATE_DT", "PERF_CHECK_COMPLETE_DT",
			"CHECK_RESULT_ANC_YN", "LAST_CHECK_RESULT_ANC_DT", "CHECK_RESULT_ANC_CNT", "PERF_CHECK_ID" };
	/**
	 * 성능점검단계
	 */
	public String[] PERF_INSPECT_STEP_KO = { "검증단계ID", "검증단계", "검증완료", "검증대상DB", "검증요청일시", "검증완료일시", "검증결과", "전체건수",
			"검증제외건수", "적합건수", "부적합건수", "미수행건수" };
	public String[] PERF_INSPECT_STEP_EN = { "PERF_CHECK_STEP_ID", "PERF_CHECK_STEP_NM", "PERF_TEST_COMPLETE_YN",
			"PERF_TEST_DB_NAME", "PERF_CHECK_REQUEST_DT", "PERF_CHECK_COMPLETE_DT", "PERF_CHECK_RESULT_DIV_NM",
			"TOTAL_CNT", "EXCEPTION_CNT", "PASS_CNT", "FAIL_CNT", "TEST_MISS_CNT" };
	/**
	 * 성능검증결과
	 */
	public String[] PERF_INSPECT_RESULT_KO = { "검증결과", "개발구분", "SQL명령유형", "프로그램", "SQL식별자(DBIO)", "프로그램유형", "수행일시",
			"성능검증ID", "프로그램ID", };
	public String[] PERF_INSPECT_RESULT_EN = { "PERF_CHECK_RESULT_DIV_NM", "PROGRAM_DVLP_DIV_NM", "SQL_COMMAND_TYPE_NM",
			"PROGRAM_NM", "DBIO", "PROGRAM_TYPE_NM", "PROGRAM_EXEC_DT", "PERF_CHECK_ID", "PROGRAM_ID", };
	/**
	 * 성능점검예외요청
	 */
	public String[] PRO_PERF_EXC_REQ_KO = { "배포ID", " 배포명", " 프로그램명", " SQL식별자(DBIO)", " 업무", " 성능점검단계", " 성능 점검 상태",
			" 영구 점검제외여부", " 성능점검결과", " 예외처리방법", " 예외처리상태", " 점검제외삭제여부", " 예외요청일시", " 예외요청자", " 예외요청사유", " 예외요청상세사유",
			" 예외처리일시", " 예외처리자", " 예외처리사유", " 파일명", " 디렉토리명", " 프로그램설명", " 배포요청일시", " 배포요청자", " 성능점검ID", " 프로그램ID" };
	
	public String[] PRO_PERF_EXC_REQ_ESPC_KO = { "배포ID", " 배포명", " 프로그램명", " SQL식별자(DBIO)", " 업무", " 성능검증단계", " 성능검증상태",
			" 영구검증제외여부", " 성능검증결과", " 예외처리방법", " 예외처리상태", " 검증제외삭제여부", " 예외요청일시", " 예외요청자", " 예외요청사유", " 예외요청상세사유",
			" 예외처리일시", " 예외처리자", " 예외처리사유", " 파일명", " 디렉토리명", " 프로그램설명", " 배포요청일시", " 배포요청자", " 성능검증ID", " 프로그램ID" };
	
	public String[] PRO_PERF_EXC_REQ_EN = { "DEPLOY_ID", "DEPLOY_NM", "PROGRAM_NM", "DBIO", "WRKJOB_CD_NM",
			"PERF_CHECK_STEP_NM", "DEPLOY_CHECK_STATUS_CD_NM", "PERF_CHECK_AUTO_PASS_YN", "PERF_CHECK_RESULT_DIV_CD_NM",
			"EXCEPTION_PRC_METH_CD_NM", "EXCEPTION_PRC_STATUS_CD_NM", "PERF_CHECK_AUTO_PASS_DEL_YN",
			"EXCEPTION_REQUEST_DT", "EXCEPTION_REQUESTER", "EXCEPTION_REQUEST_WHY_CD_NM",
			"EXCEPTION_REQUEST_DETAIL_WHY", "EXCEPTION_PRC_DT", "EXCEPT_PROCESSOR", "EXCEPTION_PRC_WHY", "FILE_NM",
			"DIR_NM", "PROGRAM_DESC", "DEPLOY_REQUEST_DT", "DEPLOY_REQUESTER", "PERF_CHECK_ID", "PROGRAM_ID"};

	public String[] PRO_PERF_EXC_REQ_ESPC_EN = { "DEPLOY_ID", "DEPLOY_NM", "PROGRAM_NM", "DBIO", "WRKJOB_CD_NM",
			"PERF_CHECK_STEP_NM", "DEPLOY_CHECK_STATUS_CD_NM", "PERF_CHECK_AUTO_PASS_YN", "PERF_CHECK_RESULT_DIV_CD_NM",
			"EXCEPTION_PRC_METH_CD_NM", "EXCEPTION_PRC_STATUS_CD_NM", "PERF_CHECK_AUTO_PASS_DEL_YN",
			"EXCEPTION_REQUEST_DT", "EXCEPTION_REQUESTER", "EXCEPTION_REQUEST_WHY_CD_NM",
			"EXCEPTION_REQUEST_DETAIL_WHY", "EXCEPTION_PRC_DT", "EXCEPT_PROCESSOR", "EXCEPTION_PRC_WHY", "FILE_NM",
			"DIR_NM", "PROGRAM_DESC", "DEPLOY_REQUEST_DT", "DEPLOY_REQUESTER", "PERF_CHECK_ID", "PROGRAM_ID"};
	
	/* 성능 검증 설정 > 성능 검증 지표 관리 */
	public String[] DEPLOY_PERF_CHK_INDC_KO = { "점검지표ID", "점검지표", "점검방법", "지표설명", "부적합가이드", "사용여부" };
	public String[] DEPLOY_PERF_CHK_INDC_EN = { "PERF_CHECK_INDC_ID", "PERF_CHECK_INDC_NM", "PERF_CHECK_METH_CD_NM",
			"PERF_CHECK_INDC_DESC", "PERF_CHECK_FAIL_GUIDE_SBST", "INDC_USE_YN" };
	
	public String[] DEPLOY_PERF_CHK_INDC_ESPC_KO = { "검증지표ID", "검증지표", "검증방법", "지표설명", "부적합가이드", "사용여부" };
	public String[] DEPLOY_PERF_CHK_INDC_ESPC_EN = { "PERF_CHECK_INDC_ID", "PERF_CHECK_INDC_NM", "PERF_CHECK_METH_CD_NM",
			"PERF_CHECK_INDC_DESC", "PERF_CHECK_FAIL_GUIDE_SBST", "INDC_USE_YN" };

	public String[] WJ_PERF_CHK_INDC_KO = { "등록구분", "업무", "점검지표", "프로그램", "점검 방법", "적합", "부적합", "여부값 판정구분", "지표적용여부",
			"지표 설명", "부적합가이드", "변경일시" };
	public String[] WJ_PERF_CHK_INDC_EN = { "REG_DIV", "WRKJOB_CD_NM", "PERF_CHECK_INDC_NM",
			"PERF_CHECK_PROGRAM_DIV_CD_NM", "PERF_CHECK_METH_CD_NM", "PASS_MAX_VALUE", "NOT_PASS_PASS",
			"YN_DECIDE_DIV_CD_NM", "INDC_APPLY_YN", "PERF_CHECK_INDC_DESC", "PERF_CHECK_FAIL_GUIDE_SBST", "UPDATE_DT" };
	
	public String[] WJ_PERF_CHK_INDC_ESPC_KO = { "등록구분", "업무", "검증지표", "프로그램", "검증방법", "적합", "부적합", "여부값 판정구분", "지표적용여부",
			"지표 설명", "부적합가이드", "변경일시" };
	public String[] WJ_PERF_CHK_INDC_ESPC_EN = { "REG_DIV", "WRKJOB_CD_NM", "PERF_CHECK_INDC_NM",
			"PERF_CHECK_PROGRAM_DIV_CD_NM", "PERF_CHECK_METH_CD_NM", "PASS_MAX_VALUE", "NOT_PASS_PASS",
			"YN_DECIDE_DIV_CD_NM", "INDC_APPLY_YN", "PERF_CHECK_INDC_DESC", "PERF_CHECK_FAIL_GUIDE_SBST", "UPDATE_DT" };

	/**
	 * 성능 점검 예외 삭제
	 */
	public String[] PRO_PERF_EXC_REQ_DEL_KO = { "프로그램명", "SQL식별자(DBIO)", "업무", "영구 점검제외여부", "예외요청ID", "예외요청일시", "예외요청자",
			"예외요청사유", "예외요청상세사유", "예외처리일시", "예외처리자", "예외처리방법", "예외처리결과", "예외삭제요청일시", "예외삭제요청상세사유", "파일명", "디렉토리명",
			"프로그램구분", "프로그램설명", "프로그램ID" };
	
	public String[] PRO_PERF_EXC_REQ_DEL_ESPC_KO = { "프로그램명", "SQL식별자(DBIO)", "업무", "영구검증제외여부", "예외요청ID", "예외요청일시", "예외요청자",
			"예외요청사유", "예외요청상세사유", "예외처리일시", "예외처리자", "예외처리방법", "예외처리결과", "예외삭제요청일시", "예외삭제요청상세사유", "파일명", "디렉토리명",
			"프로그램구분", "프로그램설명", "프로그램ID" };
	
	
	public String[] PRO_PERF_EXC_REQ_DEL_EN = { "PROGRAM_NM", "DBIO", "WRKJOB_CD_NM", "PERF_CHECK_AUTO_PASS_YN",
			"EXCEPTION_REQUEST_ID", "EXCEPTION_REQUEST_DT", "EXCEPTION_REQUESTER", "EXCEPTION_REQUEST_WHY_CD_NM",
			"EXCEPTION_REQUEST_WHY", "EXCEPTION_PRC_DT", "EXCEPT_PROCESSOR", "EXCEPTION_PRC_METH_CD_NM",
			"EXCEPTION_PRC_WHY", "EXCEPTION_DEL_REQUEST_DT", "EXCEPTION_DEL_REQUEST_WHY", "FILE_NM", "DIR_NM",
			"PROGRAM_DIV_CD_NM", "PROGRAM_DESC", "PROGRAM_ID" };
	
	public String[] PRO_PERF_EXC_REQ_DEL_ESPC_EN = { "PROGRAM_NM", "DBIO", "WRKJOB_CD_NM", "PERF_CHECK_AUTO_PASS_YN",
			"EXCEPTION_REQUEST_ID", "EXCEPTION_REQUEST_DT", "EXCEPTION_REQUESTER", "EXCEPTION_REQUEST_WHY_CD_NM",
			"EXCEPTION_REQUEST_WHY", "EXCEPTION_PRC_DT", "EXCEPT_PROCESSOR", "EXCEPTION_PRC_METH_CD_NM",
			"EXCEPTION_PRC_WHY", "EXCEPTION_DEL_REQUEST_DT", "EXCEPTION_DEL_REQUEST_WHY", "FILE_NM", "DIR_NM",
			"PROGRAM_DIV_CD_NM", "PROGRAM_DESC", "PROGRAM_ID" };

	public String[] DEPLOY_PERF_CHK_PARSING_SCHEMA_KO = { "업무", "점검 단계", "단계순서", "삭제여부", "DB", "스키마" };
	public String[] DEPLOY_PERF_CHK_PARSING_SCHEMA_EN = { "WRKJOB_CD_NM", "PERF_CHECK_STEP_NM", "STEP_ORDERING",
			"DEL_YN", "DB_NAME", "PARSING_SCHEMA_NAME" };
	
	public String[] DEPLOY_PERF_CHK_PARSING_SCHEMA_ESPC_KO = { "업무", "검증단계", "단계순서", "삭제여부", "DB", "스키마" };
	public String[] DEPLOY_PERF_CHK_PARSING_SCHEMA_ESPC_EN = { "WRKJOB_CD_NM", "PERF_CHECK_STEP_NM", "STEP_ORDERING",
			"DEL_YN", "DB_NAME", "PARSING_SCHEMA_NAME" };
	
	public String[] DEPLOY_PERF_CHK_STEP_KO = {"단계순서","점검 단계","성능점검자동 실행여부","점검 단계 설명","삭제여부"};
	public String[] DEPLOY_PERF_CHK_STEP_EN = {"STEP_ORDERING","PERF_CHECK_STEP_NM","PERF_CHECK_AUTO_EXEC_YN","PERF_CHECK_STEP_DESC","DEL_YN"};
	
	public String[] DEPLOY_PERF_CHK_STEP_ESPC_KO = {"단계순서","검증단계","성능검증자동 실행여부","검증단계 설명","삭제여부"};
	public String[] DEPLOY_PERF_CHK_STEP_ESPC_EN = {"STEP_ORDERING","PERF_CHECK_STEP_NM","PERF_CHECK_AUTO_EXEC_YN","PERF_CHECK_STEP_DESC","DEL_YN"};
	
	public String[] DEPLOY_PERF_CHK_STEP_TEST_DB_KO = { "업무", "점검 단계", "단계 순서", "삭제여부", "DB", "프로그램", "점검평가방법"};
	public String[] DEPLOY_PERF_CHK_STEP_TEST_DB_EN = { "WRKJOB_CD_NM", "PERF_CHECK_STEP_NM", "STEP_ORDERING", "DEL_YN",
			"DB_NAME", "PERF_CHECK_PROGRAM_DIV_CD_NM", "PERF_CHECK_EVALUATION_METH_NM"};
	
	public String[] DEPLOY_PERF_CHK_STEP_TEST_DB_ESPC_KO = { "업무", "검증단계", "단계 순서", "삭제여부", "DB", "프로그램", "검증평가방법"};
	public String[] DEPLOY_PERF_CHK_STEP_TEST_DB_ESPC_EN = { "WRKJOB_CD_NM", "PERF_CHECK_STEP_NM", "STEP_ORDERING", "DEL_YN",
			"DB_NAME", "PERF_CHECK_PROGRAM_DIV_CD_NM", "PERF_CHECK_EVALUATION_METH_NM"};

	public String[] PERF_CHK_INDC_LIST_STATE_KO = { "업무", "전체", "SQL-점검제외", "SQL-지표단위", "프로그램-점검제외", "거래-점검제외" };
	public String[] PERF_CHK_INDC_LIST_STATE_EN = { "WRKJOB_CD_NM", "TOTAL_EXCPT_CNT", "SQL_AUTO_PASS_CNT",
			"SQL_INDC_UNIT_CNT", "PROGRAM_AUTO_PASS_CNT", "TR_AUTO_PASS_CNT" };
	
	public String[] PERF_CHK_INDC_LIST_STATE_ESPC_KO = { "업무", "전체", "SQL-검증제외", "SQL-지표단위", "프로그램-검증제외", "거래-검증제외" };
	public String[] PERF_CHK_INDC_LIST_STATE_ESPC_EN = { "WRKJOB_CD_NM", "TOTAL_EXCPT_CNT", "SQL_AUTO_PASS_CNT",
			"SQL_INDC_UNIT_CNT", "PROGRAM_AUTO_PASS_CNT", "TR_AUTO_PASS_CNT" };
	
	
	public String[] PERF_CHK_INDC_LIST_STATE2_KO = { "업무", "합계", "테스트불가", "배치 전용", "센터컷 전용", "임시처리", "오류처리 전용",
			"에러처리 전용", "데몬 전용", "내부거래 전용", "MQ 전용", "기타" };
	public String[] PERF_CHK_INDC_LIST_STATE2_EN = { "WRKJOB_CD_NM", "TOTAL_CNT", "EXCEPTION_REQUEST_WHY_CD01",
			"EXCEPTION_REQUEST_WHY_CD02", "EXCEPTION_REQUEST_WHY_CD03", "EXCEPTION_REQUEST_WHY_CD04",
			"EXCEPTION_REQUEST_WHY_CD05", "EXCEPTION_REQUEST_WHY_CD06",
			"EXCEPTION_REQUEST_WHY_CD08", "EXCEPTION_REQUEST_WHY_CD09", "EXCEPTION_REQUEST_WHY_CD10",
			"EXCEPTION_REQUEST_WHY_CD_ETC" };
	/**
	 * 성능점검예외처리이력
	 */
	public String[] PRO_PERF_EXC_REQ_STATE_KO = { "예외처리방법", "예외처리사유","예외처리상태", "예외요청일시",
			"예외요청자", "예외요청상세사유", "예외처리일시", "예외처리자", "예외처리결과", "배포ID", "배포명",
			"프로그램명", "SQL식별자(DBIO)", "업무", "성능점검상태", "영구 점검제외여부","성능점검결과",
			"점검제외삭제여부", "배포요청일시", "배포요청자", "파일명", "디렉토리명", "프로그램설명", "성능점검ID", "프로그램ID" };
	
	public String[] PRO_PERF_EXC_REQ_STATE_ESPC_KO = { "예외처리방법", "예외처리사유","예외처리상태", "예외요청일시",
			"예외요청자", "예외요청상세사유", "예외처리일시", "예외처리자", "예외처리결과", "배포ID", "배포명",
			"프로그램명", "SQL식별자(DBIO)", "업무", "성능검증상태", "영구검증제외여부","성능검증결과",
			"검증제외삭제여부", "배포요청일시", "배포요청자", "파일명", "디렉토리명", "프로그램설명", "성능검증ID", "프로그램ID" };
	
	public String[] PRO_PERF_EXC_REQ_STATE_EN = { "EXCEPTION_PRC_METH_CD_NM", "EXCEPTION_REQUEST_WHY_CD_NM", "EXCEPTION_PRC_STATUS_CD_NM",
			 "EXCEPTION_REQUEST_DT", "EXCEPTION_REQUESTER", "EXCEPTION_REQUEST_WHY", "EXCEPTION_PRC_DT","EXCEPT_PROCESSOR","EXCEPTION_PRC_WHY", "DEPLOY_ID",
			"DEPLOY_NM", "PROGRAM_NM", "DBIO", "WRKJOB_CD_NM","DEPLOY_CHECK_STATUS_CD_NM", "PERF_CHECK_AUTO_PASS_YN", "PERF_CHECK_RESULT_DIV_CD_NM",
			 "PERF_CHECK_AUTO_PASS_DEL_YN","DEPLOY_REQUEST_DT", "DEPLOY_REQUESTER", "FILE_NM", "DIR_NM", "PROGRAM_DESC", "PERF_CHECK_ID", "PROGRAM_ID" };

	public String[] PRO_PERF_EXC_REQ_STATE_ESPC_EN = { "EXCEPTION_PRC_METH_CD_NM", "EXCEPTION_REQUEST_WHY_CD_NM", "EXCEPTION_PRC_STATUS_CD_NM",
			 "EXCEPTION_REQUEST_DT", "EXCEPTION_REQUESTER", "EXCEPTION_REQUEST_WHY", "EXCEPTION_PRC_DT","EXCEPT_PROCESSOR","EXCEPTION_PRC_WHY", "DEPLOY_ID",
			"DEPLOY_NM", "PROGRAM_NM", "DBIO", "WRKJOB_CD_NM","DEPLOY_CHECK_STATUS_CD_NM", "PERF_CHECK_AUTO_PASS_YN", "PERF_CHECK_RESULT_DIV_CD_NM",
			 "PERF_CHECK_AUTO_PASS_DEL_YN","DEPLOY_REQUEST_DT", "DEPLOY_REQUESTER", "FILE_NM", "DIR_NM", "PROGRAM_DESC", "PERF_CHECK_ID", "PROGRAM_ID" };
//	CM점검현황
//	public String[] PERF_CHECK_STATE0_KO = { "업무", "요청", "소계", "적합", "부적합", "소계", "개발확정",
//			"점검중", "개발확정취소" , "배포삭제" };
//	
//	public String[] PERF_CHECK_STATE0_EN = { "WRKJOB_CD_NM", "REQUEST_CM_CNT", "COMPLETE_CM_CNT", "PASS_CM_CNT",
//			"FAIL_CM_CNT", "NONE_COMPLETE_CM_CNT", "DEV_CONFIRM_CM_CNT", "CHECKING_CM_CNT", "DEV_CONFIRM_CANCEL_CM_CNT","DELETE_CM_CNT" };
//	
//	public String[] PERF_CHECK_STATE0_ESPC_KO = { "업무", "요청", "소계", "적합", "부적합", "소계", "개발확정",
//			"검증중", "개발확정취소" , "배포삭제" };
//	
//	public String[] PERF_CHECK_STATE0_ESPC_EN = { "WRKJOB_CD_NM", "REQUEST_CM_CNT", "COMPLETE_CM_CNT", "PASS_CM_CNT",
//			"FAIL_CM_CNT", "NONE_COMPLETE_CM_CNT", "DEV_CONFIRM_CM_CNT", "CHECKING_CM_CNT", "DEV_CONFIRM_CANCEL_CM_CNT","DELETE_CM_CNT" };
//
//	public String[] PERF_CHECK_STATE1_KO = { "업무", "전체 프로그램", "부적합 프로그램", "수행시간", "블럭수", "처리건수", "메모리사용량(MB)",
//			"대용량테이블 Full Scan", "전체 파티션 액세스" };
//	
//	public String[] PERF_CHECK_STATE1_EN = { "WRKJOB_CD_NM", "ALL_PGM_CNT", "FAIL_PGM_CNT", "ELASPSED_TIME",
//			"BUFFER_GETS", "ROW_PROCESSED", "PGA", "FULLSCAN", "PARTITION_RANGE_ALL" };
//	
//	public String[] PERF_CHECK_STATE1_ESPC_KO = { "업무", "전체", "적합", "부적합", "오류", "미수행", "검증제외",
//			"신규", "변경" , "동일" };
//	
//	public String[] PERF_CHECK_STATE1_ESPC_EN = { "WRKJOB_CD_NM", "ALL_PGM_CNT", "PASS_PGM_CNT", "FAIL_PGM_CNT",
//			"ERROR_PGM_CNT", "NONE_EXEC_PGM_CNT", "AUTO_PASS_CNT", "NEW_PGM_CNT", "CHANGE_PGM_CNT","SAME_PGM_CNT" };
//	
	public String[] PERF_CHECK_STATE2_KO = { "업무", "전체 프로그램", "부적합 프로그램", "수행시간", "블럭수", "처리건수", "메모리사용량(MB)",
			"대용량테이블 Full Scan", "전체 파티션 액세스" };
	public String[] PERF_CHECK_STATE2_EN = { "WRKJOB_CD_NM", "ALL_PGM_CNT", "FAIL_PGM_CNT", "ELASPSED_TIME",
			"BUFFER_GETS", "ROW_PROCESSED", "PGA", "FULLSCAN", "PARTITION_RANGE_ALL" };
	/** 튜닝이력조회 */
	public String[] TUNING_HIST_SEARCH_KO = { "튜닝번호", "DB", "소스파일명(Full Path)", "SQL식별자(DBIO)", "MODULE", "튜닝요청일",
			"업무담당자", "담당업무", "요청유형", "튜닝완료일", "튜닝담당자", "튜닝진행상태", "일괄튜닝종료여부","개선유형", "프로그램유형","프로젝트","튜닝진행단계" };
	public String[] TUNING_HIST_SEARCH_EN = { "TUNING_NO", "DB_NAME", "TR_CD", "DBIO", "MODULE", "TUNING_REQUEST_DT",
			"WRKJOB_MGR_NM", "WRKJOB_MGR_WRKJOB_NM", "CHOICE_DIV_CD_NM", "TUNING_COMPLETE_DT", "PERFR_NM",
			"TUNING_STATUS_NM","ALL_TUNING_END_YN", "TUNING_COMPLETE_WHY_NM", "PROGRAM_TYPE_NM","PROJECT_NM","TUNING_PRGRS_STEP_NM"};
	/** 테이블 사용 SQL 분석 */
	public String[] TABLE_USE_SQL_ANALYSIS_KO = {"SQL_ID", "PLAN_HASH_VALUE", "ELAPSED_TIME", "CPU_TIME", "BUFFER_GETS", "EXECUTIONS", "DISK_READS", "ROWS_PROCESSED", "MODULE", "ACTION", "SQL_TEXT"};
	public String[] TABLE_USE_SQL_ANALYSIS_EN = {"SQL_ID", "PLAN_HASH_VALUE", "AVG_ELAP", "AVG_CPU", "AVG_BGET", "EXECUTIONS", "AVG_DRDS", "AVG_ROWS", "MODULE", "ACTION", "SQL_TEXT"};
	/** 인덱스 사용 SQL 분석 */
	public String[] INDEX_USE_SQL_ANALYSIS_KO = {"SQL_ID", "PLAN_HASH_VALUE", "ELAPSED_TIME", "CPU_TIME", "BUFFER_GETS", "EXECUTIONS", "DISK_READS", "ROWS_PROCESSED", "MODULE", "ACTION", "SQL_TEXT"};
	public String[] INDEX_USE_SQL_ANALYSIS_EN = {"SQL_ID", "PLAN_HASH_VALUE", "AVG_ELAP", "AVG_CPU", "AVG_BGET", "EXECUTIONS", "AVG_DRDS", "AVG_ROWS", "MODULE", "ACTION", "SQL_TEXT"};
	/** 적재SQL 실행 계회 생성 */
	public String[] LOAD_ACTION_PLAN_KO = {"SQL순번", "SQL_TEXT", "PLAN생성여부", "특이사항"};
	public String[] LOAD_ACTION_PLAN_EN = {"QUERY_SEQ", "SQL_TEXT", "PLAN_YN", "NOTE"};
	/** 인덱스자동설계현황 */
	public String[] INDEX_AUTO_DESIGN_STAT_KO = { "NO", "소스구분", "OWNER", "시작일시", "종료일시", "대상 테이블수", "추천 테이블수", "추천 인덱스수",
			"시작 수집일시", "종료 수집일시", "ACCESS-PATH수", "진행상태" };
	public String[] INDEX_AUTO_DESIGN_STAT_EN = { "IDX_AD_NO", "ACCESS_PATH_TYPE_NM", "TABLE_OWNER", "START_DT",
			"END_DT", "TABLE_CNT", "RUNNING_TABLE_CNT", "RECOMMEND_INDEX_CNT", "START_COLLECT_DT", "END_COLLECT_DT",
			"ANALYSIS_SQL_CNT", "STATUS" };
	/** 수집_SQL_인덱스자동설계 */
	public String[] GATHER_SQL_IDX_AUTO_DESIGN_KO = { "파싱순번", "시작 수집일시", "종료 수집일시", "SQL수", "테이블수", "ACCESS-PATH수",
			"ACCESS-PATH 파싱일시" };
	public String[] GATHER_SQL_IDX_AUTO_DESIGN_EN = { "EXEC_SEQ", "START_COLLECT_DT", "END_COLLECT_DT", "ANALYSIS_SQL_CNT",
			"TABLE_COUNT", "ACCESS_PATH_COUNT", "ACCESS_PATH_EXEC_DT" };
	/** 테이블목록 */
	public String[] TABLE_LIST_KO = { "TABLE_NAME", "ACCESS-PATH수", "LAST_ANALYZED", "NUM_ROWS", "BLOCKS",
			"PARTITIONED", "PART_KEY_COLUMNS", "SUBPART_KEY_COLUMNS", "PARTITION_TYPE", "COMMENTS" };
	public String[] TABLE_LIST_EN = { "TABLE_NAME", "ACC_CNT", "LAST_ANALYZED", "NUM_ROWS", "BLOCKS", "PARTITIONED",
			"PART_KEY_COLUMN", "SUBPART_KEY_COLUMN", "PARTITIONING_TYPE", "TABLE_H_NAME" };

	/** 수집SQL인덱스정비 */
	public String[] GATHER_SQL_IDX_REPAIR_KO = { "NO", "TABLE_NAME", "INDEX_NAME", "PK여부", "사용횟수", "COMMENTS" };
	public String[] GATHER_SQL_IDX_REPAIR_EN = { "NO", "TABLE_NAME", "INDEX_NAME", "PK_YN", "USAGE_COUNT",
			"TABLE_HNAME" };

	/** 성능개선 > 인덱스설계/정비 > 적재SQL인덱스정비 */
	public String[] LOAD_SQL_IDX_REPAIR_KO = { "NO", "테이블 영문명", "인덱스명", "PK여부", "사용횟수", "COMMENTS" };
	public String[] LOAD_SQL_IDX_REPAIR_EN = { "NO", "TABLE_NAME", "INDEX_NAME", "PK_YN", "USAGE_COUNT",
			"TABLE_HNAME" };

	/** 성능개선 > 사례/가이드 > 가이드 */
	public String[] SQL_TUNING_GUIDE_KO = { "번호", "제목", "등록자", "등록일시", "수정자", "수정일시", "조회수", "다운로드수" };
	public String[] SQL_TUNING_GUIDE_EN = { "GUIDE_NO", "GUIDE_TITLE_NM", "REG_USER_NM", "REG_DT", "UPD_NM", "UPD_DT", "RETV_CNT", "DOWNLOAD_CNT" };

	/** 성능개선 > 사례/가이드 > 사례 */
	public String[] SQL_TUNING_PRECEDENT_KO = { "번호", "제목", "등록자", "등록일시", "조회수"};
	public String[] SQL_TUNING_PRECEDENT_EN = { "GUIDE_NO", "GUIDE_TITLE_NM","REG_USER_NM", "REG_DT", "RETV_CNT"};

	/** 성능개선관리 */
	public String[] PERF_IMPR_MNG_KO = { "튜닝번호", "이전튜닝번호", "요청유형", "DB", "SQL_ID", "소스파일명(Full Path)", "SQL식별자(DBIO)",
			"MODULE", "튜닝담당자", "업무담당자", "진행상태", "완료/종료 사유", "튜닝요청일시", "튜닝요청자", "튜닝완료일시", "적용일시","프로젝트","튜닝진행단계","SQL점검팩" };
	public String[] PERF_IMPR_MNG_EN = { "TUNING_NO", "BEFORE_TUNING_NO", "CHOICE_DIV_CD_NM", "DB_NAME", "SQL_ID",
			"TR_CD", "DBIO", "MODULE", "PERFR_NM", "WRKJOB_MGR_NM", "TUNING_STATUS_NM", "TUNING_WHY_NM",
			"TUNING_REQUEST_DT", "TUNING_REQUESTER_NM", "TUNING_COMPLETE_DT","TUNING_APPLY_DT","PROJECT_NM","TUNING_PRGRS_STEP_NM","SQL_AUTO_PERF_CHECK_ID" };

	public String[] BY_PROGRAM_TYPE_REPORT_KO = {"기준일","온라인","배치","기타","전체"};
	public String[] BY_PROGRAM_TYPE_REPORT_EN = {"BASE_DAY","ONLINE_CNT","BATCH_CNT","ETC_CNT","ALL_CNT"};
	
//	public String[] BY_REQUEST_TYPE_REPORT_KO = {"업무","자동선정","수동선정","요청","FULLSCAN","플랜변경","신규SQL","TEMP과다사용","APP타임아웃","APP응답시간지연","성능분석"};
//	public String[] BY_REQUEST_TYPE_REPORT_EN = {"wrkjob_cd_nm","coulmn_0","coulmn_1","coulmn_2","coulmn_3","coulmn_4","coulmn_5","coulmn_6","coulmn_7","coulmn_8","coulmn_9"}; //데이터를 직접 만듬으로써 소문자로 해야함 (차후 동적으로 변경해야함)

	public String[] BY_IMPROVEMENT_TYPE_REPORT_KO = {"업무","SQL","INDEX","HINT"," 오류","개선점없음","부분튜닝","기타"};
	public String[] BY_IMPROVEMENT_TYPE_REPORT_EN = {"wrkjob_cd_nm","coulmn_0","coulmn_1","coulmn_2","coulmn_3","coulmn_4","coulmn_5","coulmn_6"};  //데이터를 직접 만듬으로써 소문자로 해야함(차후 동적으로 변경해야함)
	
	public String[] DML_CHANGED_AILYLIST_KO = {"TABLE_NAME","PARTITION_NAME","SUBPARTITION_NAME","INSERTS","UPDATES","DELETES","TRUNCATED","변경일시","수집일자","전체변경건수","변경순위"};
	public String[] DML_CHANGED_AILYLIST_EN = {"TABLE_NAME","PARTITION_NAME","SUBPARTITION_NAME","INSERTS","UPDATES","DELETES","TRUNCATED","TIMESTAMP","BASE_DAY","ALL_CHANGE_CNT","RANK"};
	
	
	/*사용자 관리*/
	public String[] USERS_KO = {"사용자ID","사용자명","내선번호","휴대폰번호","이메일주소","기본비밀번호여부","비밀번호변경일시","승인여부","승인일시","승인자","등록일시","기본권한","기본업무","사용여부"};
	public String[] USERS_EN = {"USER_ID","USER_NM","EXT_NO","HP_NO","EMAIL","DEFAULT_PASSWORD_YN","PASSWORD_CHG_DT","APPROVE_YN","APPROVE_DT","APPROVE_NM","REG_DT","DEFAULT_AUTH_GRP_ID_NM","DEFAULT_WRKJOB_CD_NM","USE_YN"};

	public String[] AUTH_KO = {"권한명","사용자ID","사용자명","권한시작일자","권한종료일자"};
	public String[] AUTH_EN = {"AUTH_NM","USER_ID","USER_NM","AUTH_START_DAY","AUTH_END_DAY"};
	
	public String[] WRKJOB_CD_KO = {"사용자ID","사용자명","내선번호","휴대폰번호","사용여부","업무리더 여부"};
	public String[] WRKJOB_CD_EN = {"USER_ID","USER_NM","EXT_NO","HP_NO","USE_YN","LEADER_YN"};
	
	public String[] TR_CD_KO = {"업무코드","업무명","애플리케이션 코드","애플리케이션 코드이름","담당자ID","등록일시"};
	public String[] TR_CD_EN = {"WRKJOB_CD","WRKJOB_CD_NM","TR_CD","TR_CD_NM","MGR_ID","REG_DT"};
	
	public String[] JOB_SCHEDULER_EXEC_LOG_KO = {"수행번호","스케쥴러 유형","기준일자","작업시작일시","작업종료일시","작업상태코드","DB","업무명"};
	public String[] JOB_SCHEDULER_EXEC_LOG_EN = {"JOB_EXEC_NO","JOB_SCHEDULER_TYPE_NM","BASE_DAY","JOB_START_DT","JOB_END_DT","JOB_STATUS_CD","DB_NAME","WRKJOB_CD_NM"};
	
	/*데이터베이스 관리*/
	public String[] DATABASE_MNG_KO = {"DBID","DB명","DB 한글명","DBMS종류","Exadata여부","운영유형","CHARACTERSET","컬러","컬렉터인스턴스","수집인스턴스","정렬순서","사용여부"};
	public String[] DATABASE_MNG_EN = {"DBID","DB_NAME","DB_ABBR_NM","DATABASE_KINDS_CD","EXADATA_YN","DB_OPERATE_TYPE_NM","CHARACTERSET","RGB_COLOR_VALUE","COLLECT_INST_ID","GATHER_INST_ID","ORDERING","USE_YN"};
	
	/*인스턴스 관리*/
	public String[] INSTANCE_MNG_KO = {"DBID","DB명","인스턴스 번호","인스턴스명","화면 표시명","호스트명","인스턴스 설명","컬러","COLLECT IP","COLLECT PORT","COLLECT 이중화 IP 그룹","COLLECT 이중화 PORT 그룹","COLLECT AGENT ID","COLLECT PATH","COLLECT 인스턴스 순번","GATHER PORT","GATHER 이중화 PORT 그룹"};
	public String[] INSTANCE_MNG_EN = {"DBID","DB_NAME","INST_ID","INST_NAME","DISPLAY_NM","HOST_NM","INSTANCE_DESC","RGB_COLOR_VALUE","AGENT_IP","AGENT_PORT","DPLX_AGENT_IPS","DPLX_GATHER_AGENT_PORTS","COLLECT_AGENT_ID","COLLECT_AGENT_PATH","COLLECT_INSTANCE_SEQ","GATHER_AGENT_PORT","DPLX_AGENT_PORTS"};
	
	/*공지사항*/
	public String[] BOARD_NOTICE_KO = {"NO","제목","댓글수","조회수","등록일시","등록자"};
	public String[] BOARD_NOTICE_EN = {"RNUM","TITLE","COMMENT_CNT","HIT_CNT","UPD_DT","UPD_NM"};

	/*일 예방 점검 - MISSING OR STALE STATISTICS */
	public String[] MISSING_OR_STALE_STATISTICS_KO = { "OWNER", "TABLE_NAME", "PARTITION_NAME", "PARTITIONED",
			"INSERTS", "UPDATES", "DELETES", "TRUNCATED", "TIMESTAMP", "CHANGE_PERCENT", "NUM_ROWS", "LAST_ANALYZED" };
	public String[] MISSING_OR_STALE_STATISTICS_EN = { "OWNER", "TABLE_NAME", "PARTITION_NAME", "PARTITIONED",
			"INSERTS", "UPDATES", "DELETES", "TRUNCATED", "TIMESTAMP", "CHANGE_PERCENT", "NUM_ROWS", "LAST_ANALYZED" };

	/** 구조 품질 관리 **/
	/* 품질 기준정보 설정 - 엔티티 유형 관리 */
	public String[] TB_OPENM_ENT_TYPE_KO = {"엔터티 유형","참조 엔터티 PREFIX","엔터티 SUFFIX","테이블 유형","테이블 유형 코드","설명"};
	public String[] TB_OPENM_ENT_TYPE_EN = {"ENT_TYPE_CD","REF_ENT_TYPE_NM","ENT_TYPE_NM","TBL_TYPE_NM","TBL_TYPE_CD","ENT_TYPE_DESC"};
	
	/* 품질 기준정보 설정 - 품질검토 예외 대상관리*/
	public String[] TB_OPENQ_EXCEPT_OBJ_KO = {"품질점검 지표코드","품질점검 지표명","예외대상 객체구분","라이브러리명","모델명","주제영역명","엔터티명","속성명","비    고","요청자","등록일시"};
	public String[] TB_OPENQ_EXCEPT_OBJ_EN = {"QTY_CHK_IDT_CD","QTY_CHK_IDT_NM","OBJ_TYPE","LIB_NM","MODEL_NM","SUB_NM","ENT_NM","ATT_NM","REMARK","RQPN","RGDTTI"};
	/* 품질 기준정보 설정 - 품질점검 지표 관리 */
	public String[] TB_OPENM_QAINDI_KO = {"모델링 단계","품질점검 지표코드","품질점검 지표명","품질지표 유형","품질점검 대상여부","품질점검 유형","정렬순서","품질점검내용","해결방안내용","품질점검결과 테이블명","엑셀출력 대상여부","엑셀출력 시작행","엑셀출력 시작열"};
	public String[] TB_OPENM_QAINDI_EN = {"MDI_PCS_NM","QTY_CHK_IDT_CD","QTY_CHK_IDT_NM","QTY_IDT_TP_NM","QTY_CHK_TG_YN","QTY_CHK_TP_NM","SRT_ORD","QTY_CHK_CONT","SLV_RSL_CONT","QTY_CHK_RESULT_TBL_NM","EXCEL_OUTPUT_YN","OUTPUT_START_ROW","OUTPUT_START_COL"};
	
	/* SQL 표준 - SQL 표준 점검 결과 */
	public String[] SQL_STD_QTY_RESULT_001_KO = {"프로젝트", "업무", "전회차 총본수", "금회차 총본수", "전회차 미준수본수", "금회차 미준수본수",
			"전회차 준수율", "금회차 준수율", "SQL 파싱 오류", "금회차 작업일자", "전회차 작업일자"};
	public String[] SQL_STD_QTY_RESULT_001_EN = {"PROJECT_NM", "WRKJOB_CD_NM", "PREVIOUS_PROGRAM_CNT", "CURRENT_PROGRAM_CNT", "PREVIOUS_TOT_ERR_CNT", "CURRENT_TOT_ERR_CNT",
			"PREVIOUS_COMPIANCE_RATE", "CURRENT_COMPIANCE_RATE", "SQL_PARSING_ERR_CNT", "CURRENT_SQL_STD_GATHER_DAY", "PREVIOUS_SQL_STD_GATHER_DAY"};
	
	public String[] SQL_STD_QTY_RESULT_002_KO = {"업무", "개발자명", "개발자ID","디렉토리", "파일명", "ID", "SQL명령유형", "SQL식별자(DBIO)", "SQL 파싱 오류", "SQL Text", "프로젝트", "작업일자"};
	public String[] SQL_STD_QTY_RESULT_002_EN = {"WRKJOB_CD_NM", "DEVELOPER_NM", "DEVELOPER_ID", "DIR_NM", "FILE_NM", "PROGRAM_NM", "SQL_COMMAND_TYPE_CD", "DBIO", "SQL_PARSING_ERR_YN", "SQL_TEXT", "PROJECT_NM", "SQL_STD_GATHER_DAY"};
	
	public String[] SQL_STD_QTY_RESULT_003_KO = {"업무", "개발자명", "미준수 SQL 수"};
	public String[] SQL_STD_QTY_RESULT_003_EN = {"WRKJOB_CD_NM", "DEVELOPER_NM", "SQL_ERR_CNT"};
	
	/* SQL 표준 - SQL 품질 점검 지표 관리 */
	public String[] MAINTAIN_QUALITY_CHECK_INDICATOR_KO = {"품질 점검 지표 코드", "품질 점검 지표명", "품질 점검 지표 여부", "정렬 순서", "품질 점검 내용", "해결 방안 내용"};
	public String[] MAINTAIN_QUALITY_CHECK_INDICATOR_EN = {"QTY_CHK_IDT_CD", "QTY_CHK_IDT_NM", "QTY_CHK_IDT_YN", "SRT_ORD", "QTY_CHK_CONT", "SLV_RSL_CONT"};
	
	/* SQL 표준 - SQL 품질 점검 SQL 관리 */
	public String[] MAINTAIN_QUALITY_CHECK_SQL_KO = {"품질 점검 지표 코드", "품질 점검 지표명", "품질 점검 RULE", "DML 여부","프로젝트 단위 관리 여부"};
	public String[] MAINTAIN_QUALITY_CHECK_SQL_EN = {"QTY_CHK_IDT_CD", "QTY_CHK_IDT_NM", "QTY_CHK_SQL", "DML_YN","PROJECT_BY_MGMT_YN"};
	
	/* SQL 표준 - SQL 품질검토 예외 대상 관리 */
	public String[] MAINTAIN_QUALITY_CHECK_EXCEPTION_KO = {"품질 점검 지표 코드", "품질 점검 지표명", "업무 코드", "디렉토리명", "SQL 식별자(DBIO)", "예외사유", "요청자", "등록일시", "등록자"};
	public String[] MAINTAIN_QUALITY_CHECK_EXCEPTION_EN = {"QTY_CHK_IDT_CD", "QTY_CHK_IDT_NM", "WRKJOB_CD", "DIR_NM", "DBIO", "EXCEPT_SBST", "REQUESTER", "REG_DT", "USER_ID"};
	
	/* SQL 표준 - SQL_표준_점검_스케쥴러_관리 */
	public String[] SQL_STD_QTY_SCHEDULER_MANAGER_KO = {"표준진단DB", "시작일자", "종료일자", "실행주기", "타겟DB", "DB 유저 ID", "Parser Code", "프로젝트", "스케줄러 설명", "연동방법", "형상관리 시스템 디렉토리", "IP", "PORT", "사용자 ID"};
	public String[] SQL_STD_QTY_SCHEDULER_MANAGER_EN = {"JOB_SCHEDULER_NM", "EXEC_START_DT", "EXEC_END_DT", "EXEC_CYCLE", "STD_QTY_TARGET_DB_NAME", "STD_QTY_TARGET_DB_USER_ID", "PARSE_CODE", "PROJECT_NM", "JOB_SCHEDULER_DESC", "SVN_IF_METH_CD", "SVN_DIR_NM", "SVN_IP", "SVN_PORT", "SVN_OS_USER_ID"};
	
	/* 실행기반 SQL - SQL 표준 점검 결과 */
	public String[] EXEC_SQL_STD_CHK_RESULT_001_KO = {"프로젝트", "업무", "전회차 총본수", "금회차 총본수", "전회차 미준수본수", "금회차 미준수본수",
			"전회차 준수율", "금회차 준수율", "금회차 작업일자", "전회차 작업일자", "표준점검DB", "수집기간"};
	public String[] EXEC_SQL_STD_CHK_RESULT_001_EN = {"PROJECT_NM", "WRKJOB_CD_NM", "PREVIOUS_PROGRAM_CNT", "CURRENT_PROGRAM_CNT", "PREVIOUS_TOT_ERR_CNT", "CURRENT_TOT_ERR_CNT",
			"PREVIOUS_COMPIANCE_RATE", "CURRENT_COMPIANCE_RATE", "CURRENT_SQL_STD_GATHER_DAY", "PREVIOUS_SQL_STD_GATHER_DAY", "DB_NAME", "GATHER_TERM"};
	
	public String[] EXEC_SQL_STD_CHK_RESULT_002_KO = {"프로젝트", "업무", "SQL ID", "Module", "SQL명령유형", "SQL식별자(DBIO)", "개발자명", "SQL Text", "작업일자", "표준점검DB"};
	public String[] EXEC_SQL_STD_CHK_RESULT_002_EN = {"PROJECT_NM", "WRKJOB_CD_NM", "SQL_ID", "PROGRAM_NM", "SQL_COMMAND_TYPE_CD", "DBIO", "DEVELOPER_NM", "SQL_TEXT", "SQL_STD_GATHER_DAY", "DB_NAME"};

	public String[] EXEC_SQL_STD_CHK_RESULT_003_KO = {"업무", "개발자명", "미준수 SQL 수"};
	public String[] EXEC_SQL_STD_CHK_RESULT_003_EN = {"WRKJOB_CD_NM", "DEVELOPER_NM", "SQL_ERR_CNT"};
	
	/* 실행기반 SQL 표준 일괄 점검 - SQL 표준 점검 예외 대상 관리 */
	public String[] SQL_STD_QTY_EXCEPTION_KO = {"품질 점검 지표 코드", "품질 점검 지표명", "표준점검DB", "SQL ID", "SQL 식별자(DBIO)", "업무 코드", "디렉토리명", "예외사유", "요청자", "등록일시", "등록자"};
	public String[] SQL_STD_QTY_EXCEPTION_EN = {"QTY_CHK_IDT_CD", "QTY_CHK_IDT_NM", "STD_QTY_TARGET_DB_NAME", "SQL_ID", "DBIO", "WRKJOB_CD", "DIR_NM", "EXCEPT_SBST", "REQUESTER", "REG_DT", "USER_ID"};
	
	/* 실행기반 SQL 표준 일괄 점검 - 스케쥴러_관리 */
	public String[] EXEC_SQL_STD_CHK_SCHEDULER_KO = {"스케줄러명", "표준점검DB", "스케줄러 시작일자", "스케줄러 종료일자", "실행주기", "Parser Code", "프로젝트", "스케줄러 설명", "SQL 소스", "수집기간", "Table Owner", "Module", "Filter SQL"};
	public String[] EXEC_SQL_STD_CHK_SCHEDULER_EN = {"JOB_SCHEDULER_NM", "STD_QTY_TARGET_DB_NAME", "EXEC_START_DT", "EXEC_END_DT", "EXEC_CYCLE", "PARSE_CODE", "PROJECT_NM", "JOB_SCHEDULER_DESC", "SQL_SOURCE_TYPE_CD", "GATHER_RANGE", "OWNER_LIST", "MODULE_LIST", "EXTRA_FILTER_PREDICATION"};
	
	/* 종합진단 - SQL_종합_진단_SQL 품질진단 집계표 */
	public String[] SQL_DIAGNOSIS_REPORT_021_KO = {"진단명", "개수"};
	public String[] SQL_DIAGNOSIS_REPORT_021_EN = {"QTY_CHK_IDT_NM" , "ERR_CNT"};
	
	public String[] SQL_DIAGNOSIS_REPORT_022_KO = {"SQL_ID"};
	public String[] SQL_DIAGNOSIS_REPORT_022_EN = {"SQL_ID"};

	/* SQL 품질 진단 - SQL 품질 진단 현황 */
	public String[] SQL_DIAGNOSIS_REPORT_STATUS_KO = {"품질진단DB", "스케줄러", "진행상태", "진단일시", "미준수 SQL 합계"};
	public String[] SQL_DIAGNOSIS_REPORT_STATUS_EN = {"DB_NMAE", "JOB_SCHEDULER_NM", "EXEC_STATUS", "DIAG_DT", "SQL_ID_CNT"};

	
	/* SQL 표준 - SQL_품질_진단_스케쥴러_관리 */
	public String[] SQL_DIAGNOSIS_REPORT_MANAGE_SCHEDULER_KO = {"표준진단DB", "스케줄러명", "스케줄러 시작일자", "스케줄러 종료일자", "실행주기", "스케줄러 설명", "SQL 소스", "수집기간", "TABLE_OWNER", "MODULE", "FILTER SQL"};
	public String[] SQL_DIAGNOSIS_REPORT_MANAGE_SCHEDULER_EN = {"DB_NAME", "JOB_SCHEDULER_NM", "EXEC_START_DT", "EXEC_END_DT", "EXEC_CYCLE", "JOB_SCHEDULER_DESC", "SQL_SOURCE_TYPE_NM", "GATHER_TERM", "OWNER_LIST", "MODULE_LIST", "EXTRA_FILTER_PREDICATION"};
	
	/* 업무분류체계 관리 */
	public String[] OPENM_BIZ_CLS_MNG_KO = {"라이브러리명","모델명","주제영역명","시스템명","시스템코드","업무대분류명","업무대분류코드","업무중분류명","업무중분류코드","주제영역정의","비고"};
	public String[] OPENM_BIZ_CLS_MNG_EN = {"LIB_NM","MODEL_NM","SUB_NM","SYS_NM","SYS_CD","MAIN_BIZ_CLS_NM","MAIN_BIZ_CLS_CD","MID_BIZ_CLS_NM","MID_BIZ_CLS_CD","BIZ_DESC","REMARK"};
	
	/* 품질점검 RULE 관리 */
	public String[] OPENM_QTY_CHK_SQL_MNG_KO = {"품질점검 지표코드","품질점검 지표명","품질점검 RULE","DML여부"};
	public String[] OPENM_QTY_CHK_SQL_MNG_EN = {"QTY_CHK_IDT_CD","QTY_CHK_IDT_NM","QTY_CHK_SQL","DML_YN"};
	
	/* 구조 품질검토 작업 */
	public String[] OPENM_INSPECTION_MNG_KO = {"품질점검지표", "품질점검지표명", "작업결과(건수)", "출력시작행", "Sheet명"};
	public String[] OPENM_INSPECTION_MNG_EN = {"QTY_CHK_IDT", "QTY_CHK_IDT_NM", "QTY_INSPECTION_CNT", "OUTPUT_START_ROW", "QTY_RESULT_SHEET_NM"};

//	public String[] OPENM_INSPECTION_MNG_KO = {"품질점검 대상여부", "품질점검지표", "품질점검지표명", "작업결과(건수)", "출력시작행", "Sheet명"};
//	public String[] OPENM_INSPECTION_MNG_EN = {"QTY_CHK_TG_YN", "QTY_CHK_IDT", "QTY_CHK_IDT_NM", "QTY_INSPECTION_CNT", "OUTPUT_START_ROW", "QTY_RESULT_SHEET_NM"};
	
	/* 프로젝트관리*/
	public String[] PROJECT_KO = {"프로젝트ID","프로젝트명","프로젝트 설명","등록일시","등록자ID","등록자명","종료여부","종료일시"};
	public String[] PROJECT_EN = {"PROJECT_ID","PROJECT_NM","PROJECT_DESC","PROJECT_CREATE_DT","PROJECT_CREATER_ID","USER_NM","DEL_YN","DEL_DT"};
	
	/* SQL 자동 성능 점검 */
	public String[] SQL_AUTOMATIC_PERFORMANCE_CHECK_KO = {"프로젝트명", "수행회차", "성능임팩트유형", "버퍼 임팩트(%)", "수행시간 임팩트(%)", "Plan 변경여부", "SQL ID",
			"BEFORE PLAN HASH VALUE", "BEFORE EXECUTIONS", "BEFORE ROWS PROCESSED",
			"BEFORE ELAPSED TIME", "BEFORE BUFFER GETS", "BEFORE DISK READS", "BEFORE FULLSCAN YN", "BEFORE PARTITION ALL ACCESS YN",
			"AFTER PLAN HASH VALUE", "AFTER EXECUTIONS", "AFTER ROWS PROCESSED", "AFTER ELAPSED TIME", "AFTER_BUFFER_GETS",
			"AFTER DISK READS", "ALTER FULLSCAN YN", "AFTER PARTITION ALL ACCESS YN", "SQL 명령유형", "에러코드", "에러메시지", "SQL TEXT"};
	
	public String[] SQL_AUTOMATIC_PERFORMANCE_CHECK_EN = {"PROJECT_NM", "SQL_AUTO_PERF_CHECK_ID", "SQL_IDFY_COND_TYPE_NM", "BUFFER_INCREASE_RATIO", "ELAPSED_TIME_INCREASE_RATIO", "PLAN_CHANGE_YN", "SQL_ID",
			"BEFORE_PLAN_HASH_VALUE", "BEFORE_EXECUTIONS", "BEFORE_ROWS_PROCESSED",
			"BEFORE_ELAPSED_TIME", "BEFORE_BUFFER_GETS", "BEFORE_DISK_READS", "BEFORE_FULLSCAN_YN", "BEFORE_PARTITION_ALL_ACCESS_YN",
			"AFTER_PLAN_HASH_VALUE", "AFTER_EXECUTIONS", "AFTER_ROWS_PROCESSED", "AFTER_ELAPSED_TIME", "AFTER_BUFFER_GETS",
			"AFTER_DISK_READS", "ALTER_FULLSCAN_YN", "AFTER_PARTITION_ALL_ACCESS_YN", "SQL_COMMAND_TYPE_CD", "ERR_CODE", "ERR_MSG", "SQL_TEXT"};
	
	/* SQL 자동 성능점검 대상관리 */
	public String[] SQL_AUTOMATIC_PERFORMANCE_CHECK_TARGET_MNG_KO = {"프로젝트명", "DB", "성능점검대상범위", "OWNER", "테이블명", "MODULE"};
	public String[] SQL_AUTOMATIC_PERFORMANCE_CHECK_TARGET_MNG_EN = {"PROJECT_NM", "DB_NAME", "SQL_IDFY_COND_TYPE_NM", "OWNER", "TABLE_NAME", "MODULE"};
	
	/* 자동 선정 */
	public String[] AUTO_SELECTION_KO = {"선정조건번호", "DB", "프로그램유형", "자동할당여부", "튜닝담당자", "수집주기", "수집범위", "선정시작일", "선정종료일", "이전 선정 SQL 제외", "이전 튜닝 SQL 제외", "Elapsed Time(sec)", "Buffer Gets", "Executions", "TOP N", "Ordered", "Module명 1", "Module명 2", "Parsing Schema Name", "SQL TEXT", "선정자", "선정일시", "애플리케이션 필터 여부", "사용 여부", "삭제 여부", "프로젝트", "튜닝진행단계"};
	public String[] AUTO_SELECTION_EN = {"AUTO_CHOICE_COND_NO", "DB_NAME", "PROGRAM_TYPE_CD_NM", "PERFR_AUTO_ASSIGN_YN", "PERFR_NM", "GATHER_CYCLE_DIV_NM", "GATHER_RANGE_DIV_NM", "CHOICE_START_DAY", "CHOICE_END_DAY", "BEFORE_CHOICE_SQL_EXCEPT_YN", "BEFORE_TUNING_SQL_EXCEPT_YN", "ELAP_TIME", "BUFFER_CNT", "EXEC_CNT", "TOPN_CNT", "ORDER_DIV_NM", "MODULE1", "MODULE2", "PARSING_SCHEMA_NAME", "SQL_TEXT", "CHOICER_NM", "CHOICE_DT", "APPL_FILTER_YN", "USE_YN", "DEL_YN", "PROJECT_NM", "TUNING_PRGRS_STEP_NM"};
	
	/* 자동 선정 현황 */
	public String[] AUTO_SELECTION_STATUS_KO = {"DB", "선정조건번호", "선정회차", "선정일자", "선정건수", "수집주기", "수집범위", "이전 선정 SQL 제외", "이전 튜닝 SQL 제외", "Elapsed Time(sec)", "Buffer Gets", "Executions", "TOP N", "Ordered", "프로젝트", "튜닝진행단계"};
	public String[] AUTO_SELECTION_STATUS_EN = {"DB_NAME", "AUTO_CHOICE_COND_NO", "CHOICE_TMS", "CHOICE_DT", "CHOICE_CNT", "GATHER_CYCLE_DIV_NM", "GATHER_RANGE_DIV_NM", "BEFORE_CHOICE_SQL_EXCEPT_YN", "BEFORE_TUNING_SQL_EXCEPT_YN", "ELAP_TIME", "BUFFER_CNT", "EXEC_CNT", "TOPN_CNT", "ORDER_DIV_NM", "PROJECT_NM", "TUNING_PRGRS_STEP_NM"};
	
	/* 자동 선정 상세 현황 */
	public String[] AUTO_SELECTION_STATUS_DETAIL_KO = {"튜닝번호", "튜닝담당자", "튜닝상태", "SQL_ID", "PLAN_HASH_VALUE", "PARSING_SCHEMA_NAME", "ELAPSED_TIME", "BUFFER_GETS", "EXECUTIONS", "DISK_READS", "ROWS_PROCESSED", "MODULE", "RATIO_BUFFER_GET_TOTAL", "RATIO_CPU_TOTAL", "RATIO_CPU_PER_EXECUTIONS", "SQL_TEXT"};
	public String[] AUTO_SELECTION_STATUS_DETAIL_EN = {"TUNING_NO", "PERFR_NM", "TUNING_STATUS_NM", "SQL_ID", "PLAN_HASH_VALUE", "PARSING_SCHEMA_NAME", "AVG_ELAPSED_TIME", "AVG_BUFFER_GETS", "EXECUTIONS", "AVG_DISK_READS", "AVG_ROW_PROCESSED", "MODULE", "RATIO_BUFFER_GET_TOTAL", "RATIO_CPU_TOTAL", "RATIO_CPU_PER_EXECUTIONS", "SQL_TEXT"};
	
	/* 성능개선결과 산출물 */
	public String[] TUNING_TARGET_SQL_KO = {"TUNING_NO","요청유형","DB","SQL_ID","소스파일명(Full Path)","SQL식별자(DBIO)","MODULE","튜닝담당자","진행상태","완료/종료 사유","요청일","요청자","업무담당자","튜닝완료일","ELAPSED_TIME(개선전)","BUFFER_GETS(개선전)","ELAPSED_TIME(개선후)","BUFFER_GETS(개선후)",
			"ELAPSED_TIME(개선율)","BUFFER_GETS(개선율)","적용일","프로젝트","튜닝진행단계","문제점","개선내용"};
	public String[] TUNING_TARGET_SQL_EN = {"TUNING_NO","CHOICE_DIV_CD_NM","DB_NAME","SQL_ID","TR_CD","DBIO","MODULE","PERFR_NM","TUNING_STATUS_NM","TUNING_WHY_NM","TUNING_REQUEST_DT","TUNING_REQUESTER_NM","WRKJOB_MGR_NM","TUNING_COMPLETE_DT","IMPRB_ELAP_TIME","IMPRB_BUFFER_CNT","IMPRA_ELAP_TIME",
			"IMPRA_BUFFER_CNT","ELAP_TIME_IMPR_RATIO","BUFFER_IMPR_RATIO","TUNING_APPLY_DT","PROJECT_NM","TUNING_PRGRS_STEP_NM","CONTROVERSIALIST","IMPR_SBST"};

	/* 성능개선결과 산출물_수협 */
	public String[] TUNING_TARGET_SQL_V2_KO = {"TUNING_NO","요청유형","DB","SQL_ID","소스파일명(Full Path)","SQL식별자(DBIO)","MODULE","튜닝담당자","진행상태","튜닝 완료 사유","튜닝 종료 사유","요청일","요청자","업무담당자ID","업무담당자명","업무명","튜닝완료일","ELAPSED_TIME(개선전)","BUFFER_GETS(개선전)","ELAPSED_TIME(개선후)","BUFFER_GETS(개선후)",
			"ELAPSED_TIME(개선율)","BUFFER_GETS(개선율)","적용일","프로젝트","튜닝진행단계","문제점","개선내용"};
	public String[] TUNING_TARGET_SQL_V2_EN = {"TUNING_NO","CHOICE_DIV_CD_NM","DB_NAME","SQL_ID","TR_CD","DBIO","MODULE","PERFR_NM","TUNING_STATUS_NM","TUNING_COMPLETE_WHY_NM","TUNING_END_WHY_NM","TUNING_REQUEST_DT","TUNING_REQUESTER_NM","WRKJOB_MGR_ID","WRKJOB_MGR_NM","WRKJOB_MGR_WRKJOB_NM","TUNING_COMPLETE_DT","IMPRB_ELAP_TIME","IMPRB_BUFFER_CNT","IMPRA_ELAP_TIME",
			"IMPRA_BUFFER_CNT","ELAP_TIME_IMPR_RATIO","BUFFER_IMPR_RATIO","TUNING_APPLY_DT","PROJECT_NM","TUNING_PRGRS_STEP_NM","CONTROVERSIALIST","IMPR_SBST"};

	/* 프로젝트 구조 품질점검 지표/RULE 관리 */
	public String[] TB_OPENM_QTY_CHK_SQL_KO = {"적용여부","프로젝트","모델링 단계","품질점검 지표코드","품질점검 지표명","품질지표 유형","품질점검 대상여부","품질점검 유형","정렬순서","품질점검 내용","해결방안 내용","품질점검 RULE"};
	public String[] TB_OPENM_QTY_CHK_SQL_EN = {"APPLY_YN","PROJECT_NM","MDI_PCS_NM","QTY_CHK_IDT_CD","QTY_CHK_IDT_NM","QTY_IDT_TP_NM","QTY_CHK_TG_YN","QTY_CHK_TP_NM","SRT_ORD","QTY_CHK_CONT","SLV_RSL_CONT","QTY_CHK_SQL"};
	
	
	/* 수동 선정 현황 */
	public String[] MANUAL_SELECTION_STATUS_KO = {"순번", "DBID", "SQL_ID", "PLAN_HASH_VALUE", "MODULE", "PARSING_SCHEMA_NAME", "EXECUTIONS", "BUFFER_GETS", "MAX_BUFFER_GETS", "TOTAL_BUFFER_GETS", "ELAPSED_TIME", "MAX_ELAPSED_TIME", "CPU_TIME", "DISK_READS", "ROWS_PROCESSED", "RATIO_BGET_TOTAL(%)", "RATIO_CPU_TOTAL(%)", "RATIO_CPU_PER_EXECUTION(%)", "SQL_TEXT"};
	public String[] MANUAL_SELECTION_STATUS_EN = {"RNUM", "DBID", "SQL_ID", "PLAN_HASH_VALUE", "MODULE", "PARSING_SCHEMA_NAME", "EXECUTIONS", "AVG_BUFFER_GETS", "MAX_BUFFER_GETS", "TOTAL_BUFFER_GETS", "AVG_ELAPSED_TIME", "MAX_ELAPSED_TIME", "AVG_CPU_TIME", "AVG_DISK_READS", "AVG_ROWS_PROCESSED", "RATIO_BGET_TOTAL", "RATIO_CPU_TOTAL", "RATIO_CPU_PER_EXECUTION", "SQL_TEXT"};

	/* 수동 선정 현황 */
	public String[] MANUAL_SELECTION_STATUS2_KO = {"DB","선정회차","선정자","선정일자","선정건수","시작 SNAP ID","종료 SNAP ID","이전선정SQL제외","이전튜닝SQL제외","ELAPSED_TIME","BUFFER_GETS","EXECUTIONS","TOP N","ORDERED","MODULE","PARSING_SCHEMA_NAME","SQL_TEXT","EXCPT_MODULE_LIST","EXCPT_PARSING_SCHEMA_NAME_LIST","EXCPT_SQL_ID_LIST","EXTRA_FILTER_PREDICATION","프로젝트","튜닝진행단계"};
	public String[] MANUAL_SELECTION_STATUS2_EN = {"DB_NAME","CHOICE_TMS","CHOICER_NM","CHOICE_DT","CHOICE_CNT","START_SNAP_ID","END_SNAP_ID","BEFORE_CHOICE_SQL_EXCEPT_YN","BEFORE_TUNING_SQL_EXCEPT_YN","ELAP_TIME","BUFFER_CNT","EXEC_CNT","TOPN_CNT","ORDER_DIV_NM","MODULE1","PARSING_SCHEMA_NAME","SQL_TEXT","EXCPT_MODULE_LIST","EXCPT_PARSING_SCHEMA_NAME_LIST","EXCPT_SQL_ID_LIST","EXTRA_FILTER_PREDICATION","PROJECT_NM","TUNING_PRGRS_STEP_NM"};

	
	/* 프로젝트 업무 관리 */
	public String[] PROJECT_WRKJOB_MNG_KO = {"프로젝트", "업무"};
	public String[] PROJECT_WRKJOB_MNG_EN = {"PROJECT_NM", "WRKJOB_CD_NM"};
	
	/* 프로젝트 DB 관리 */
	public String[] PROJECT_DB_MNG_KO = {"프로젝트", "성능점검원천 DB", "성능점검대상 DB"};
	public String[] PROJECT_DB_MNG_EN = {"PROJECT_NM", "PERF_CHECK_ORIGINAL_DB_NAME", "PERF_CHECK_TARGET_DB_NAME"};
	
	/* 프로젝트 모델 관리 */
	public String[] PROJECT_MODEL_MNG_KO = {"프로젝트", "라이브러리명", "모델명", "주제영역명"};
	public String[] PROJECT_MODEL_MNG_EN = {"PROJECT_NM", "LIB_NM", "MODEL_NM", "SUB_NM"};
	
	/* 프로젝트 튜닝 진행단계 */
	public String[] PROJECT_TUNING_PRGRS_STEP_KO = {"프로젝트", "튜닝진행단계일련번호", "튜닝진행단계명", "튜닝진행단계 설명", "삭제여부"};
	public String[] PROJECT_TUNING_PRGRS_STEP_EN = {"PROJECT_NM", "TUNING_PRGRS_STEP_SEQ", "TUNING_PRGRS_STEP_NM", "TUNING_PRGRS_STEP_DESC", "DEL_YN"};
	
	/* 모델 엔터티 유형 관리 */
//	public String[] MODEL_ENTITY_TYPE_MNG_KO = { "라이브러리명", "모델명", "주제영역명", "엔터티유형", "참조엔터티명prefix", "엔터티유형명", "테이블유형명", "테이블유형코드", "엔터티유형설명" };
	public String[] MODEL_ENTITY_TYPE_MNG_KO = { "라이브러리명", "모델명", "주제영역명", "엔터티 유형", "참조 엔터티 PREFIX", "엔터티 SUFFIX", "테이블 유형", "테이블 유형 코드", "설명" };
	public String[] MODEL_ENTITY_TYPE_MNG_EN = { "LIB_NM", "MODEL_NM", "SUB_NM", "ENT_TYPE_CD", "REF_ENT_TYPE_NM", "ENT_TYPE_NM", "TBL_TYPE_NM", "TBL_TYPE_CD", "ENT_TYPE_DESC" };
	
	/* 프로젝트 SQL 품질점검 지표 관리 */
	public String[] PROJECT_SQL_QTY_CHK_IDX_MNG_KO = { "적용여부", "프로젝트", "품질 점검 지표 코드", "품질 점검 지표명", "품질 점검 지표 여부", "정렬 순서", "품질 점검 내용", "해결 방안 내용" };
	public String[] PROJECT_SQL_QTY_CHK_IDX_MNG_EN = { "APPLY_YN", "PROJECT_NM", "QTY_CHK_IDT_CD", "QTY_CHK_IDT_NM", "QTY_CHK_IDT_YN", "SRT_ORD", "QTY_CHK_CONT", "SLV_RSL_CONT" };

	/*  프로젝트 SQL 품질점검 RULE 관리 */
	public String[] PROJECT_SQL_QTY_CHK_RULE_MNG_KO = { "적용여부", "프로젝트", "품질 점검 지표 코드", "품질 점검 지표명", "품질 점검 RULE", "DML 여부" };
	public String[] PROJECT_SQL_QTY_CHK_RULE_MNG_EN = { "APPLY_YN", "PROJECT_NM", "QTY_CHK_IDT_CD", "QTY_CHK_IDT_NM", "QTY_CHK_SQL", "DML_YN" };
	
	/* SQL 표준 품질점검 작업 */
	public String[] QUALITY_REVIEW_WORK_KO = { "프로젝트", "품질 점검 지표", "품질 점검 지표명", "작업결과(건수)" };
	public String[] QUALITY_REVIEW_WORK_EN = { "PROJECT_NM", "QTY_CHK_IDT_CD", "QTY_CHK_IDT_NM", "ERR_CNT" };
	
	/* TOP SQL 추이/현황 하단 (top sql) */
	public String[] TOP_SQL_TREND_STATUS_KO = {"SQL_ID","PLAN_HASH_VALUE","MODULE","ACTION","PARSING_SCHEMA_NAME","ELAPSED_TIME","CPU_TIME","EXECUTIONS","DISK_READS","BUFFER_GETS","ROWS_PROCESSED","SQL_TEXT","ELAPSED_TIME_RATIO(%)","CPU_TIME_RATIO(%)","EXECUTIONS_RATIO(%)","DISK_READS_RATIO(%)","BUFFER_GETS_RATIO(%)","ROWS_PROCESSED_RATIO(%)"};
	public String[] TOP_SQL_TREND_STATUS_EN = {"SQL_ID","PLAN_HASH_VALUE","MODULE","ACTION","PARSING_SCHEMA_NAME","ELAPSED_TIME","CPU_TIME","EXECUTIONS","DISK_READS","BUFFER_GETS","ROWS_PROCESSED","SQL_TEXT","ELAPSED_TIME_RATIO","CPU_TIME_RATIO","EXECUTIONS_RATIO","DISK_READS_RATIO","BUFFER_GETS_RATIO","ROWS_PROCESSED_RATIO"};
	
	/* 솔루션 프로그램 관리 - 솔루션 프로그램 RULE 관리 */
	public String[] SLT_PROGRAM_SQL_KO = {"솔루션프로그램구분","프로그램 RULE번호","프로그램 RULE명","목차명","설명","프로그램 RULE"};
	public String[] SLT_PROGRAM_SQL_EN = {"SLT_PROGRAM_DIV_NM","SLT_PROGRAM_SQL_NUMBER","SLT_PROGRAM_SQL_NAME","CONTENTS_NAME","SLT_PROGRAM_SQL_DESC","SLT_PROGRAM_CHK_SQL"};
	
	/*  SQL 셀프 성능 점검_인덱스자동설계 */
	public String[] SQL_SELF_TUNING_INDEX_AUTO_DESIGN_KO = {"NO", "테이블명", "추천 유형", "인덱스 컬럼"};
	public String[] SQL_SELF_TUNING_INDEX_AUTO_DESIGN_EN = {"SEQ", "TABLE_NAME", "RECOMMEND_TYPE", "ACCESS_PATH_COLUMN_LIST"};
	
	/*  성능 점검 SQL */
	public String[] PERFORMANCE_CHECK_SQL_KO = {"업무","SQL수","성능 향상","성능 저하","부적합", "< 2x", "< 5x", "< 10x", "< 30x", "< 50x", "< 100x", ">= 100"};
	public String[] PERFORMANCE_CHECK_SQL_EN = {"WRKJOB_CD_NM_EXCEL","SQL_CNT","IMPROVE","REGRESS","FAIL", "LESS_THAN_2", "LESS_THAN_5", "LESS_THAN_10", "LESS_THAN_30", "LESS_THAN_50", "LESS_THAN_100", "MORE_THAN_100"};
	
	/* SQLs */
	public String[] EXCEPTION_HANDLING_SQL_KO = {"업무","SQL수","적합","부적합", "<= 0.1s", "< 0.3s", "< 1s", "< 3s", "< 10s", "< 60s", ">= 60s"};
	public String[] EXCEPTION_HANDLING_SQL_EN = {"WRKJOB_CD_NM_EXCEL","SQL_CNT","PASS","FAIL", "LESS_THAN_0_DOT_1", "LESS_THAN_0_DOT_3", "LESS_THAN_1", "LESS_THAN_3", "LESS_THAN_10", "LESS_THAN_60", "MORE_THAN_60"};
	
	/* 신규배포SQL 성능 변화 분석(자동검증) 예외처리 SQL */
	public String[] EXCEPTION_HANDLING_RUN_SQL_KO = {"업무","배포SQL수","실행SQL수","적합","부적합", "<= 0.1s", "< 0.3s", "< 1s", "< 3s", "< 10s", "< 60s", ">= 60s"};
	public String[] EXCEPTION_HANDLING_RUN_SQL_EN = {"WRKJOB_CD_NM_EXCEL","DEPLOY_SQL_CNT","SQL_CNT","PASS","FAIL", "LESS_THAN_0_DOT_1", "LESS_THAN_0_DOT_3", "LESS_THAN_1", "LESS_THAN_3", "LESS_THAN_10", "LESS_THAN_60", "MORE_THAN_60"};
	
	/* SQLs */
	public String[] SQLs_KO = {"SQL 식별자(DBIO)","프로그램","SQL ID","PLAN HASH VALUE","Plan 변경여부", "배포-평균 수행시간(초)", "운영-평균 수행시간(초)", "Activity(%)-평균 수행시간(초)", "배포-평균 블럭수", "운영-평균 블럭수", "Activity(%)-평균 블럭수", "배포-평균 처리건수", "운영-평균 처리건수", "수행 횟수", "예외 방법", "부적합 여부", "이전 부적합 여부", "성능점검일자", "배포일자"};
	public String[] SQLs_EN = {"DBIO","PROGRAM_NM","AFTER_PRD_SQL_ID","AFTER_PRD_PLAN_HASH_VALUE","PRD_PLAN_CHANGE_YN", "TEST_ELAPSED_TIME", "AFTER_PRD_ELAPSED_TIME", "ELAPSED_TIME_ACTIVITY", "TEST_BUFFER_GETS", "AFTER_PRD_BUFFER_GETS", "BUFFER_GETS_ACTIVITY", "TEST_ROWS_PROCESSED", "AFTER_PRD_ROWS_PROCESSED", "AFTER_EXECUTIONS", "EXCEPTION_PRC_METH_NM","AFTER_FAIL_YN", "BEFORE_FAIL_YN", "PROGRAM_EXEC_DT", "DEPLOY_COMPLETE_DT"};
	
	/* SQLs */
	public String[] AUTOSQLs_KO = {"SQL 식별자(DBIO)","프로그램","SQL ID","PLAN HASH VALUE", "배포-평균 수행시간(초)", "운영-평균 수행시간(초)", "Activity(%)-평균 수행시간(초)", "배포-평균 블럭수", "운영-평균 블럭수", "Activity(%)-평균 블럭수", "배포-평균 처리건수", "운영-평균 처리건수", "수행 횟수", "예외 방법", "부적합 여부", "성능검증일자", "배포일자"};
	public String[] AUTOSQLs_EN = {"DBIO","PROGRAM_NM","AFTER_PRD_SQL_ID","AFTER_PRD_PLAN_HASH_VALUE", "TEST_ELAPSED_TIME", "AFTER_PRD_ELAPSED_TIME", "ELAPSED_TIME_ACTIVITY", "TEST_BUFFER_GETS", "AFTER_PRD_BUFFER_GETS", "BUFFER_GETS_ACTIVITY", "TEST_ROWS_PROCESSED", "AFTER_PRD_ROWS_PROCESSED", "AFTER_EXECUTIONS", "EXCEPTION_PRC_METH_NM","AFTER_FAIL_YN", "PROGRAM_EXEC_DT", "DEPLOY_COMPLETE_DT"};
	
	/* DB간 성능자동비교 > 성능 영향도 분석 결과 */
	public String[] SQL_AUTO_PERF_COMP_KO = {"튜닝상태","성능임팩트 유형","버퍼 임팩트(배)","수행시간 임팩트(배)","성능점검결과","Plan 변경여부","SQL ID","ASIS PLAN HASH VALUE",
											 "TOBE PLAN HASH VALUE","ASIS EXECUTIONS","TOBE EXECUTIONS","ASIS ROWS PROCESSED","TOBE ROWS PROCESSED","ASIS ELAPSED TIME",
											 "TOBE ELAPSED TIME","ASIS BUFFER GETS","TOBE BUFFER GETS","ASIS FULLSCAN YN","TOBE FULLSCAN YN","ASIS PARTITION ALL ACCESS_YN","TOBE PARTITION ALL ACCESS YN",
											 "SQL 명령 유형","PARSING SCHEMA NAME","에러코드","에러메시지","검토결과","PROFILE NAME","SQL TEXT","튜닝번호","프로젝트명","SQL점검팩명"};
	
	public String[] SQL_AUTO_PERF_COMP_EN = {"TUNING_STATUS_NM","PERF_IMPACT_TYPE_NM","BUFFER_INCREASE_RATIO",
											 "ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN","PLAN_CHANGE_YN","SQL_ID",
											 "ASIS_PLAN_HASH_VALUE","TOBE_PLAN_HASH_VALUE","ASIS_EXECUTIONS","TOBE_EXECUTIONS",
											 "ASIS_ROWS_PROCESSED","TOBE_ROWS_PROCESSED","ASIS_ELAPSED_TIME","TOBE_ELAPSED_TIME",
											 "ASIS_BUFFER_GETS","TOBE_BUFFER_GETS","ASIS_FULLSCAN_YN","TOBE_FULLSCAN_YN","ASIS_PARTITION_ALL_ACCESS_YN","TOBE_PARTITION_ALL_ACCESS_YN",
											 "SQL_COMMAND_TYPE_CD","PARSING_SCHEMA_NAME","ERR_CODE","ERR_MSG","REVIEW_SBST","SQL_PROFILE_NM","SQL_TEXT_EXCEL","TUNING_NO","PROJECT_NM","PERF_CHECK_NAME"};
	
	/* DB간 SQL성능비교 > 튜닝실적 */
	public String[] TUNING_PERFORMANCE_KO = {"SQL점검팩명","전체 SQL 수","튜닝선정","Plan변경","성능저하 SQLs - 수행시간","성능저하 SQLs - 버퍼","튜닝 SQLs - 완료","튜닝 SQLs - 진행중","개선 실적(평균 %) - 수행시간","개선 실적(평균 %) - 버퍼" ,"프로젝트명"};
	public String[] TUNING_PERFORMANCE_EN = {"PERF_CHECK_NAME","SQL_ALL_CNT","TUNING_SELECTION_CNT","PLAN_CHANGE_CNT", "ELAPSED_TIME_STD_CNT", "BUFFER_STD_CNT", "TUNING_END_CNT", "TUNING_CNT", "ELAP_TIME_IMPR_RATIO", "BUFFER_IMPR_RATIO","PROJECT_NM"};
	
	/* DB간 SQL성능비교 > 튜닝실적 상세 */
	public String[] TUNING_PERFORMANCE_DETAIL_KO = {"튜닝상태","버퍼 임팩트(배)","수행시간 임팩트(배)","성능점검결과","Plan변경여부","SQL ID","ASIS PLAN HASH VALUE","ASIS ELAPSED TIME","ASIS BUFFER GETS","TOBE PLAN HASH VALUE" ,"TOBE ELAPSED TIME" ,"TOBE BUFFER GETS","TUNING ELAPSED TIME","TUNING BUFFER GETS" ,"SQL TEXT","튜닝번호","프로젝트명","SQL점검팩명"};
	public String[] TUNING_PERFORMANCE_DETAIL_EN = {"TUNING_STATUS_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN", "PLAN_CHANGE_YN", "SQL_ID", "ASIS_PLAN_HASH_VALUE", "ASIS_ELAPSED_TIME", "ASIS_BUFFER_GETS", "TOBE_PLAN_HASH_VALUE","TOBE_ELAPSED_TIME","TOBE_BUFFER_GETS","IMPRA_ELAP_TIME","IMPRA_BUFFER_CNT","SQL_TEXT_EXCEL","TUNING_NO","PROJECT_NM","PERF_CHECK_NAME"};
	
	/* DB간 SQL성능비교 > 튜닝 SQL 일괄 검증  */
	public String[] TUNING_SQL_PERFORMANCE_KO = {"전체 SQL수","튜닝선정","Plan 변경","수행시간(성능저하 SQLs)","버퍼(성능저하 SQLs)","완료(튜닝 SQLs)","진행중(튜닝 SQLs)","수행시간(개선 실적(평균%))","버퍼(개선 실적(평균%))","수행시간(일괄검증 성능(평균%))","버퍼(일괄검증 성능(평균%))","프로젝트명","SQL점검팩명" };
	public String[] TUNING_SQL_PERFORMANCE_EN = {"SQL_ALL_CNT","TUNING_SELECTION_CNT","PLAN_CHANGE_CNT","ELAPSED_TIME_STD_CNT","BUFFER_STD_CNT","TUNING_END_CNT","TUNING_CNT","TUNING_ELAP_TIME_IMPR_RATIO","TUNING_BUFFER_IMPR_RATIO","VERIFY_ELAP_TIME_IMPR_RATIO","VERIFY_BUFFER_IMPR_RATIO","PROJECT_NM","PERF_CHECK_NAME" };

	/* DB간 SQL성능비교 > 튜닝 SQL 일괄 검증 상세 */
	public String[] TUNING_SQL_PERF_DETAIL_KO = {"튜닝상태","버퍼 임팩트(배)-일괄검증결과","수행시간 임팩트(배)-일괄검증결과","성능점검결과-일괄검증결과","Plan 변경여부-일괄검증결과","ASIS SQL ID","ASIS PLAN HASH VALUE","ASIS ELAPSED TIME","ASIS BUFFER GETS","TOBE PLAN HASH VALUE","TOBE ELAPSED TIME","TOBE BUFFER GETS","TUNING ELAPSED TIME","TUNING BUFFER GETS","SQL ID-일괄검증 ","ELAPSED TIME-일괄검증","BUFFER GETS-일괄검증","에러코드","에러메시지","SQL_TEXT","일괄검증 튜닝번호","성능비교 튜닝번호","프로젝트명","SQL점검팩명" };
	public String[] TUNING_SQL_PERF_DETAIL_EN = {"TUNING_STATUS_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN","PLAN_CHANGE_YN","ASIS_SQL_ID","ASIS_PLAN_HASH_VALUE","ASIS_ELAPSED_TIME","ASIS_BUFFER_GETS","TOBE_PLAN_HASH_VALUE","TOBE_ELAPSED_TIME","TOBE_BUFFER_GETS","TUNING_ELAPSED_TIME","TUNING_BUFFER_GETS","VERIFY_SQL_ID","VERIFY_ELAPSED_TIME","VERIFY_BUFFER_GETS","ERR_CODE","ERR_MSG","SQL_TEXT_EXCEL","VERIFY_TUNING_NO","BEFORE_TUNING_NO","PROJECT_NM","PERF_CHECK_NAME"};
	
	/* DB간 SQL성능비교 > 운영 SQL 성능 추적*/
	public String[] OPERATION_SQL_PERF_TRACK_KO = {"SQL ID","성능점검결과","Plan변경여부"," ASIS PLAN HASH VALUE","운영 PLAN HASH VALUE","ASIS-평균수행시간(초)","운영-평균수행시간(초)","Activity(%)-평균수행시간(초)","ASIS-평균블럭수","운영-평균블럭수","Activity(%)-평균블럭수","운영 수행횟수","운영 수행건수","SQL TEXT","PROFILE NAME","튜닝번호","프로젝트명","SQL점검팩명"};
	public String[] OPERATION_SQL_PERF_TRACK_EN = {"SQL_ID","PERF_CHECK_RESULT","PLAN_CHANGE_YN","ASIS_PLAN_HASH_VALUE","OPERATION_PLAN_HASH_VALUE","ASIS_ELAPSED_TIME","OPERATION_ELAPSED_TIME","ELAPSED_TIME_ACTIVITY","ASIS_BUFFER_GETS","OPERATION_BUFFER_GETS","BUFFER_GETS_ACTIVITY","OPERATION_EXECUTIONS","OPERATION_ROWS_PROCESSED","SQL_TEXT_EXCEL","SQL_PROFILE_NM","TUNING_NO","PROJECT_NM","PERF_CHECK_NAME"};
	
	/* DB 변경 SQL 영향도 분석*/
	public String[] ANALYZE_DB_CHANGE_SQL_IMPACT_KO = {"NO","SQL ID","미지원 힌트","ORA-01719","ORA-00979","ORA-30563","WM_CONCAT","성능임팩트유형","버퍼임팩트(배)","수행시간임팩트(배)","Plan변경여부","ASIS EXECUTIONS","ASIS ELAPSED TIME","TOBE ELAPSED TIME","ASIS BUFFER GETS","TOBE BUFFER GETS","SQL TEXT","BYPASS_UJVC","AND_EQUAL","MERGE_AJ","HASH_AJ","NL_AJ","MERGE_SJ","HASH_SJ","NL_SJ","ORDERED_PREDICATES","ROWID","STAR","NOPARALLEL","NOPARALLEL_INDEX","NOREWRITE"};
	public String[] ANALYZE_DB_CHANGE_SQL_IMPACT_EN = {"NO","SQL_ID","DEPRECATED_YN","ORA_01719_YN","ORA_00979_YN","ORA_30563_YN","WM_CONCAT_YN","PERF_IMPACT_TYPE_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","PLAN_CHANGE_YN","ASIS_EXECUTIONS","ASIS_ELAPSED_TIME","TOBE_ELAPSED_TIME","ASIS_BUFFER_GETS","TOBE_BUFFER_GETS","SQL_TEXT_EXCEL","BYPASS_UJVC_CNT","AND_EQUAL_CNT","MERGE_AJ_CNT","HASH_AJ_CNT","NL_AJ_CNT","HASH_SJ_CNT","MERGE_SJ_CNT","NL_SJ_CNT","ORDERED_PREDICATES_CNT","ROWID_CNT","STAR_CNT","NOPARALLEL_CNT","NOPARALLEL_INDEX_CNT","NOREWRITE_CNT"};
	
	/* 테이블 변경 성능 영향도 분석 > 성능영향도분석결과 */
	public String[] CHANGED_TABLE_PERF_COMP_KO = {"TABLE_NAME","튜닝상태","성능임팩트 유형","버퍼 임팩트(배)","수행시간 임팩트(배)","성능점검결과","Plan 변경여부","SQL ID","ASIS PLAN HASH VALUE",
			 "TOBE PLAN HASH VALUE","ASIS EXECUTIONS","TOBE EXECUTIONS","ASIS ROWS PROCESSED","TOBE ROWS PROCESSED","ASIS ELAPSED TIME",
			 "TOBE ELAPSED TIME","ASIS BUFFER GETS","TOBE BUFFER GETS","ASIS FULLSCAN YN","TOBE FULLSCAN YN","ASIS PARTITION ALL ACCESS_YN","TOBE PARTITION ALL ACCESS YN",
			 "SQL 명령 유형","에러코드","에러메시지","SQL TEXT","튜닝번호","SQL점검팩명"};

	public String[] CHANGED_TABLE_PERF_COMP_EN = {"TABLE_NAME","TUNING_STATUS_NM","PERF_IMPACT_TYPE_NM","BUFFER_INCREASE_RATIO",
				 "ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN","PLAN_CHANGE_YN","SQL_ID",
				 "ASIS_PLAN_HASH_VALUE","TOBE_PLAN_HASH_VALUE","ASIS_EXECUTIONS","TOBE_EXECUTIONS",
				 "ASIS_ROWS_PROCESSED","TOBE_ROWS_PROCESSED","ASIS_ELAPSED_TIME","TOBE_ELAPSED_TIME",
				 "ASIS_BUFFER_GETS","TOBE_BUFFER_GETS","ASIS_FULLSCAN_YN","TOBE_FULLSCAN_YN","ASIS_PARTITION_ALL_ACCESS_YN","TOBE_PARTITION_ALL_ACCESS_YN",
				 "SQL_COMMAND_TYPE_CD","ERR_CODE","ERR_MSG","SQL_TEXT_EXCEL","TUNING_NO","PERF_CHECK_NAME"};
	
	/* 테이블 변경 성능 영향도 분석 > 튜닝실적 */
	public String[] CHANGED_TABLE_TUNING_PERFORMANCE_KO = {"SQL점검팩명","전체 SQL 수","튜닝선정","Plan변경","성능저하 SQLs - 수행시간","성능저하 SQLs - 버퍼","튜닝 SQLs - 완료","튜닝 SQLs - 진행중","개선 실적(평균 %) - 수행시간","개선 실적(평균 %) - 버퍼" };
	public String[] CHANGED_TABLE_TUNING_PERFORMANCE_EN = {"PERF_CHECK_NAME","SQL_ALL_CNT","TUNING_SELECTION_CNT","PLAN_CHANGE_CNT", "ELAPSED_TIME_STD_CNT", "BUFFER_STD_CNT", "TUNING_END_CNT", "TUNING_CNT", "ELAP_TIME_IMPR_RATIO", "BUFFER_IMPR_RATIO"};
	
	/* 테이블 변경 성능 영향도 분석 > 튜닝실적 상세 */
	public String[] CHANGED_TABLE_TUNING_PERFORMANCE_DETAIL_KO = {"SQL점검팩명","튜닝상태","버퍼 임팩트(배)","수행시간 임팩트(배)","성능점검결과","Plan변경여부","SQL ID","ASIS PLAN HASH VALUE","ASIS ELAPSED TIME","ASIS BUFFER GETS","TOBE PLAN HASH VALUE" ,"TOBE ELAPSED TIME" ,"TOBE BUFFER GETS","TUNING ELAPSED TIME","TUNING BUFFER GETS" ,"SQL TEXT","튜닝번호"};
	public String[] CHANGED_TABLE_TUNING_PERFORMANCE_DETAIL_EN = {"PERF_CHECK_NAME","TUNING_STATUS_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN", "PLAN_CHANGE_YN", "SQL_ID", "ASIS_PLAN_HASH_VALUE", "ASIS_ELAPSED_TIME", "ASIS_BUFFER_GETS", "TOBE_PLAN_HASH_VALUE","TOBE_ELAPSED_TIME","TOBE_BUFFER_GETS","IMPRA_ELAP_TIME","IMPRA_BUFFER_CNT","SQL_TEXT_EXCEL","TUNING_NO"};

	/* TIBERO DB 변경 성능 영향도 분석  > 성능 영향도 분석 결과 */
	public String[] SQL_AUTO_PERF_COMP_TIBERO_KO = {"튜닝상태","성능임팩트 유형","버퍼 임팩트(배)","수행시간 임팩트(배)","성능점검결과","SQL ID","ASIS PLAN HASH VALUE",
			"TOBE PLAN HASH VALUE","ASIS EXECUTIONS","TOBE EXECUTIONS","ASIS ROWS PROCESSED","TOBE ROWS PROCESSED","ASIS ELAPSED TIME","TOBE ELAPSED TIME",
			"ASIS BUFFER GETS","TOBE BUFFER GETS","ASIS FULLSCAN YN","TOBE FULLSCAN YN","ASIS PARTITION ALL ACCESS_YN","TOBE PARTITION ALL ACCESS YN",
			"SQL 명령 유형","PARSING SCHEMA NAME","에러코드","에러메시지","검토결과","SQL TEXT","튜닝번호","프로젝트명","SQL점검팩명"};
	
	public String[] SQL_AUTO_PERF_COMP_TIBERO_EN = {"TUNING_STATUS_NM","PERF_IMPACT_TYPE_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN","SQL_ID","ASIS_PLAN_HASH_VALUE",
			"TOBE_PLAN_HASH_VALUE","ASIS_EXECUTIONS","TOBE_EXECUTIONS","ASIS_ROWS_PROCESSED","TOBE_ROWS_PROCESSED","ASIS_ELAPSED_TIME","TOBE_ELAPSED_TIME",
			"ASIS_BUFFER_GETS","TOBE_BUFFER_GETS","ASIS_FULLSCAN_YN","TOBE_FULLSCAN_YN","ASIS_PARTITION_ALL_ACCESS_YN","TOBE_PARTITION_ALL_ACCESS_YN",
			"SQL_COMMAND_TYPE_CD","PARSING_SCHEMA_NAME","ERR_CODE","ERR_MSG","REVIEW_SBST","SQL_TEXT_EXCEL","TUNING_NO","PROJECT_NM","PERF_CHECK_NAME"};
	
	/* TIBERO DB 변경 성능 영향도 분석  > 튜닝실적 */
	public String[] TUNING_PERFORMANCE_TIBERO_KO = {"SQL점검팩명","전체 SQL 수","튜닝선정","성능저하 SQLs - 수행시간","성능저하 SQLs - 버퍼","튜닝 SQLs - 완료","튜닝 SQLs - 진행중","개선 실적(평균 %) - 수행시간","개선 실적(평균 %) - 버퍼" ,"프로젝트명"};
	public String[] TUNING_PERFORMANCE_TIBERO_EN = {"PERF_CHECK_NAME","SQL_ALL_CNT","TUNING_SELECTION_CNT","ELAPSED_TIME_STD_CNT", "BUFFER_STD_CNT", "TUNING_END_CNT", "TUNING_CNT", "ELAP_TIME_IMPR_RATIO", "BUFFER_IMPR_RATIO","PROJECT_NM"};
	
	/* TIBERO DB 변경 성능 영향도 분석  > 튜닝실적 상세 */
	public String[] TUNING_PERFORMANCE_DETAIL_TIBERO_KO = {"튜닝상태","버퍼 임팩트(배)","수행시간 임팩트(배)","성능점검결과","SQL ID","ASIS PLAN HASH VALUE","ASIS ELAPSED TIME","ASIS BUFFER GETS","TOBE PLAN HASH VALUE" ,"TOBE ELAPSED TIME" ,"TOBE BUFFER GETS","TUNING ELAPSED TIME","TUNING BUFFER GETS" ,"SQL TEXT","튜닝번호","프로젝트명","SQL점검팩명"};
	public String[] TUNING_PERFORMANCE_DETAIL_TIBERO_EN = {"TUNING_STATUS_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN", "SQL_ID", "ASIS_PLAN_HASH_VALUE", "ASIS_ELAPSED_TIME", "ASIS_BUFFER_GETS", "TOBE_PLAN_HASH_VALUE","TOBE_ELAPSED_TIME","TOBE_BUFFER_GETS","IMPRA_ELAP_TIME","IMPRA_BUFFER_CNT","SQL_TEXT_EXCEL","TUNING_NO","PROJECT_NM","PERF_CHECK_NAME"};
	
	/* TIBERO DB 변경 성능 영향도 분석  > 튜닝 SQL 일괄 검증  */
	public String[] TUNING_SQL_PERFORMANCE_TIBERO_KO = {"전체 SQL수","튜닝선정","수행시간(성능저하 SQLs)","버퍼(성능저하 SQLs)","완료(튜닝 SQLs)","진행중(튜닝 SQLs)","수행시간(개선 실적(평균%))","버퍼(개선 실적(평균%))","수행시간(일괄검증 성능(평균%))","버퍼(일괄검증 성능(평균%))","프로젝트명","SQL점검팩명" };
	public String[] TUNING_SQL_PERFORMANCE_TIBERO_EN = {"SQL_ALL_CNT","TUNING_SELECTION_CNT","ELAPSED_TIME_STD_CNT","BUFFER_STD_CNT","TUNING_END_CNT","TUNING_CNT","TUNING_ELAP_TIME_IMPR_RATIO","TUNING_BUFFER_IMPR_RATIO","VERIFY_ELAP_TIME_IMPR_RATIO","VERIFY_BUFFER_IMPR_RATIO","PROJECT_NM","PERF_CHECK_NAME" };
	
	/* TIBERO DB 변경 성능 영향도 분석  > 튜닝 SQL 일괄 검증 상세  */
	public String[] TUNING_SQL_PERF_DETAIL_TIBERO_KO = {"튜닝상태","버퍼 임팩트(배)","수행시간 임팩트(배)","성능점검결과","ASIS SQL ID","ASIS PLAN HASH VALUE","ASIS ELAPSED TIME","ASIS BUFFER GETS","TOBE PLAN HASH VALUE","TOBE ELAPSED TIME","TOBE BUFFER GETS","TUNING ELAPSED TIME","TUNING BUFFER GETS","SQL ID","ELAPSED TIME","BUFFER GETS","에러코드","에러메시지","SQL_TEXT","튜닝번호","성능비교 튜닝번호","프로젝트명","SQL점검팩명" };
	public String[] TUNING_SQL_PERF_DETAIL_TIBERO_EN = {"TUNING_STATUS_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN","ASIS_SQL_ID","ASIS_PLAN_HASH_VALUE","ASIS_ELAPSED_TIME","ASIS_BUFFER_GETS","TOBE_PLAN_HASH_VALUE","TOBE_ELAPSED_TIME","TOBE_BUFFER_GETS","TUNING_ELAPSED_TIME","TUNING_BUFFER_GETS","VERIFY_SQL_ID","VERIFY_ELAPSED_TIME","VERIFY_BUFFER_GETS","ERR_CODE","ERR_MSG","SQL_TEXT_EXCEL","VERIFY_TUNING_NO","BEFORE_TUNING_NO","PROJECT_NM","PERF_CHECK_NAME"};
	
	/* 자동 인덱싱 SQL 성능 검증 > 인덱스별 성능 영향도 분석 결과 */
	public String[] AISQLPV_RESULT_STATE_KO = {"OWNER","테이블명","추천 인덱스명","SQL수","ASIS ELAPSED TIME","TOBE ELAPSED TIME","ELAPSED TIME 임팩트(배)","ASIS BUFFER GETS","TOBE BUFFER GETS","BUFFER GETS 임팩트(배)", "추천 인덱스 칼럼"};
	public String[] AISQLPV_RESULT_STATE_EN = {"TABLE_OWNER","TABLE_NAME","INDEX_NAME","SQL_CNT","ASIS_ELAPSED_TIME","TOBE_ELAPSED_TIME","ELAPSED_TIME_INCREASE_RATIO","ASIS_BUFFER_GETS","TOBE_BUFFER_GETS","BUFFER_INCREASE_RATIO","ACCESS_PATH_COLUMN_LIST"};
	
	public String[] AISQLPV_RESULT_BY_INDX_KO = {"성능임팩트 유형","버퍼 임팩트(배)","수행시간 임팩트(배)","SQL ID",
			"ASIS PLAN HASH VALUE","TOBE PLAN HASH VALUE","ASIS ELAPSED TIME","TOBE ELAPSED TIME","ASIS BUFFER GETS","TOBE BUFFER GETS",
			"ASIS EXECUTIONS","TOBE EXECUTIONS","ASIS ROWS PROCESSED","TOBE ROWS PROCESSED","ASIS FULLSCAN YN","TOBE FULLSCAN YN",
			"ASIS PARTITION ALL ACCESS_YN","TOBE PARTITION ALL ACCESS YN","SQL 명령 유형","PARSING SCHEMA NAME",
			"SQL TEXT","OWNER","테이블명","추천 인덱스명","프로젝트명","SQL점검팩명"};
	
	public String[] AISQLPV_RESULT_BY_INDX_EN = {"PERF_IMPACT_TYPE_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","SQL_ID",
			"ASIS_PLAN_HASH_VALUE","TOBE_PLAN_HASH_VALUE","ASIS_ELAPSED_TIME","TOBE_ELAPSED_TIME","ASIS_BUFFER_GETS","TOBE_BUFFER_GETS",
			"ASIS_EXECUTIONS","TOBE_EXECUTIONS","ASIS_ROWS_PROCESSED","TOBE_ROWS_PROCESSED","ASIS_FULLSCAN_YN","TOBE_FULLSCAN_YN",
			"ASIS_PARTITION_ALL_ACCESS_YN","TOBE_PARTITION_ALL_ACCESS_YN","SQL_COMMAND_TYPE_CD","PARSING_SCHEMA_NAME",
			"SQL_TEXT_EXCEL","TABLE_OWNER","TABLE_NAME","INDEX_NAME","PROJECT_NM","PERF_CHECK_NAME"};
	
	public String[] AISQLPV_RESULT_KO = {"성능임팩트 유형","버퍼 임팩트(배)","수행시간 임팩트(배)","성능점검결과","SQL ID",
			"ASIS PLAN HASH VALUE","TOBE PLAN HASH VALUE","ASIS EXECUTIONS","TOBE EXECUTIONS","ASIS ROWS PROCESSED","TOBE ROWS PROCESSED",
			"ASIS ELAPSED TIME","TOBE ELAPSED TIME","ASIS BUFFER GETS","TOBE BUFFER GETS","ASIS FULLSCAN YN","TOBE FULLSCAN YN",
			"ASIS PARTITION ALL ACCESS_YN","TOBE PARTITION ALL ACCESS YN","SQL 명령 유형","PARSING SCHEMA NAME",
			"에러코드","에러메시지","검토결과","SQL TEXT","OWNER","테이블명","추천 인덱스명","프로젝트명","SQL점검팩명"};
	
	public String[] AISQLPV_RESULT_EN = {"PERF_IMPACT_TYPE_NM","BUFFER_INCREASE_RATIO","ELAPSED_TIME_INCREASE_RATIO","PERF_CHECK_RESULT_YN","SQL_ID",
			"ASIS_PLAN_HASH_VALUE","TOBE_PLAN_HASH_VALUE","ASIS_EXECUTIONS","TOBE_EXECUTIONS","ASIS_ROWS_PROCESSED","TOBE_ROWS_PROCESSED",
			"ASIS_ELAPSED_TIME","TOBE_ELAPSED_TIME","ASIS_BUFFER_GETS","TOBE_BUFFER_GETS","ASIS_FULLSCAN_YN","TOBE_FULLSCAN_YN",
			"ASIS_PARTITION_ALL_ACCESS_YN","TOBE_PARTITION_ALL_ACCESS_YN","SQL_COMMAND_TYPE_CD","PARSING_SCHEMA_NAME",
			"ERR_CODE","ERR_MSG","REVIEW_SBST","SQL_TEXT_EXCEL","TABLE_OWNER","TABLE_NAME","INDEX_NAME","PROJECT_NM","PERF_CHECK_NAME"};
	
	/* 자동 인덱싱 SQL 성능 검증 > 인덱스별 성능 영향도 분석 결과 */
	public String[] AISQLPV_INDEX_RECOMMEND_KO = {"인덱스생성","성능 분석 결과 최종 추천", "DB - ASIS", "DB - TOBE", "OWNER",
			"테이블명", "추천유형", "추천 인덱스 컬럼", "ACCESS PATH 수", "ASIS 인덱스 명", "ASIS 인덱스 컬럼"
	};
	public String[] AISQLPV_INDEX_RECOMMEND_EN = {"INDEX_CREATE_YN", "LAST_RECOMMEND_TYPE_NM", "ASIS_DB_NAME", "TOBE_DB_NAME", "TABLE_OWNER",
			"TABLE_NAME", "RECOMMEND_TYPE", "ACCESS_PATH_COLUMN_LIST", "ACCESS_PATH_USED_CNT", "SOURCE_INDEX_NAME", "SOURCE_INDEX_COLUMN_LIST"
	};



	public ExcelDownHeaders() {}

	public static void main(String[] args) {
		ExcelDownHeaders headers = new ExcelDownHeaders();
		logger.debug("##########################################");
		logger.debug("PERF_CHECK_MNG_KO:" + headers.PERF_CHECK_MNG_KO.toString());
		logger.debug("PERF_CHECK_MNG_EN:" + headers.PERF_CHECK_MNG_EN.toString());
		logger.debug("##########################################");

	}

}
