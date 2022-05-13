package omc.mqm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.OpenqErrCnt;


/***********************************************************
 * 2019.05.20 임승률 최초작성 
 **********************************************************/

public interface QualityInspectionJobService {
	
	/** 모델구조품질검토작업 리스트 */
	List<OpenmQaindi> selectQualityInspectionJobList(OpenmQaindi openmQaindi) throws Exception;
	
	/** 모델구조품질검토작업 - 품질검토작업 */
	int saveQualityInspectionJob(OpenmQaindi openmQaindi) throws Exception;

	List<LinkedHashMap<String, Object>> selectQualityInspectionJobListByExcelDown(OpenmQaindi openmQaindi) throws Exception;

	/** 모델구조품질검토작업 - 모델정보수집 */
	int qualityInspectionJobModelCollectingAction(OpenmQaindi openmQaindi) throws Exception;

	/** 구조 품질 집계표 - 집계표 Grid 해드 타이틀 정보 */
	List<OpenmQaindi> getQualityInspectionJobSheetHeadTitleList(OpenmBizCls openmBizCls) throws Exception;
	
	/** 구조 품질 집계표 - 집계표  */
	List<Map<String, Object>> selectQualityInspectionJobSheetList(OpenmBizCls openmBizCls) throws Exception;

	/** 구조 품질 집계표 - 라이브러리명 */
	List<OpenqErrCnt> getOpenqErrCntLibNmList(OpenqErrCnt openqErrCnt);
	
	/** 구조 품질 집계표 - 모델명명 */
	List<OpenqErrCnt> getOpenqErrCntModelNmList(OpenqErrCnt openqErrCnt);
	
	Map<String, Object> selectQualityInspectionJobSheetListByExcelDown(OpenqErrCnt openqErrCnt) throws Exception;
	
	/** 구조 품질검토 작업 - 검색 */
	List<OpenmQaindi> getQualityInspectionJob(OpenmQaindi openmQaindi);

	/** 구조 품질검토 작업 - 엑셀다운*/
	List<LinkedHashMap<String, Object>> getQualityInspectionJobByExcelDown(OpenmQaindi openmQaindi);
	
}
