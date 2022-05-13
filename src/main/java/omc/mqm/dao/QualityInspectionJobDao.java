package omc.mqm.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.OpenmQtyChkSql;
import omc.mqm.model.OpenqErrCnt;

/***********************************************************
 * 2019.05.20	임승률	최초작성
 **********************************************************/

public interface QualityInspectionJobDao {
	public List<OpenmQaindi> selectQualityInspectionJobList(OpenmQaindi openmQaindi);
	
	public List<OpenmQaindi> selectQualityInspectionJobResultList(OpenmQaindi openmQaindi);
	
	public List<LinkedHashMap<String, Object>> selectQualityInspectionJobListByExcelDown(OpenmQaindi openmQaindi);
	
	public int updateTbOpenmQaindi(HashMap<String, Object> param);
	
	public int deleteQtyChkResultTblNm(OpenmQtyChkSql openmQtyChkSql);
	
	public int deleteTbOpenqErrCnt(HashMap<String, Object> param);
	
	public OpenmQtyChkSql selectQualityInspectionJobSql(OpenmQaindi openmQaindi);
	
	public List<OpenmQtyChkSql> selectQualityInspectionJobSqlList(OpenmQaindi openmQaindi);
	
	public List<OpenmQaindi> selectQualityInspectionJobExcelOutSqlList(OpenqErrCnt openqErrCnt);
	
	public int insertQualityInspectionJob(OpenmQtyChkSql openmQtyChkSql);
	
	public int insertTbOpenqErrCnt(HashMap<String, Object> param);
	
	public String selectTbOpenmEntExtrecDt();
	
	public String selectTbOpenmCurDate();
	
	public List<Map<String, Object>> selectQualityInspectionJobSqlResultList(HashMap<String, Object> param);
	
	public List<Map<String, Object>> selectQtyChkResultTblNmResultList(HashMap<String, Object> param);
	
	public List<OpenmQaindi> selectQualityInspectionJobSheetHeadTitleList(OpenmBizCls openmBizCls);
	
	public List<OpenqErrCnt> selectOpenqErrCntLibNmList(OpenqErrCnt openqErrCnt);
	
	public List<OpenqErrCnt> selectOpenqErrCntModelNmList(OpenqErrCnt openqErrCnt);

	public List<OpenmQaindi> getQualityInspectionJob(OpenmQaindi openmQaindi);

	public List<LinkedHashMap<String, Object>> getQualityInspectionJobByExcelDown(OpenmQaindi openmQaindi);
}
