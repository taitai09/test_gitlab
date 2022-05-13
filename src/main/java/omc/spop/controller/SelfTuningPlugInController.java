package omc.spop.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.model.AccPathExec;
import omc.spop.model.ApmApplSql;
import omc.spop.model.ApmApplSqlPlugIn;
import omc.spop.model.DbaTables;
import omc.spop.model.IdxAdMst;
import omc.spop.model.OdsUsers;
import omc.spop.model.PerformanceCheckMng;
import omc.spop.model.Result;
import omc.spop.model.SelftunExecutionPlan;
import omc.spop.model.SelftunExecutionPlan1;
import omc.spop.model.TuningTargetSqlPlugIn;
import omc.spop.model.WrkJobCd;
import omc.spop.service.CommonService;
import omc.spop.service.ImprovementManagementPlugInService;
import omc.spop.service.SelfTuningPlugInService;
import omc.spop.service.SelfTuningService;

/***********************************************************
 * 2018.03.23 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Controller
@RequestMapping(value = "/stpi")
public class SelfTuningPlugInController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(SelfTuningPlugInController.class);
	
	@Autowired
	private SelfTuningService selfTuningService;
	
	@Autowired
	private SelfTuningPlugInService selfTuningPlugInService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImprovementManagementPlugInService improvementManagementPlugInService;
	
	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	/* UUID 조회 */
	@RequestMapping(value = "/getUUID", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUUID(@ModelAttribute("apmApplSqlPlugIn") ApmApplSqlPlugIn apmApplSqlPlugIn) throws Exception {
		String uuid = UUID.randomUUID().toString();
		
		apmApplSqlPlugIn.setUuid(uuid);
		
		List<ApmApplSqlPlugIn> apmApplSqlPlugInList = new ArrayList<ApmApplSqlPlugIn>();
		
		apmApplSqlPlugInList.add(apmApplSqlPlugIn);
		
		return success(apmApplSqlPlugInList).toJSONObject().get("rows").toString();
	}
	
	/* Work Job 조회 */
	@RequestMapping(value = "/getWrkJob", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getWrkJob(@ModelAttribute("wrkJobCd") WrkJobCd wrkJobCd) throws Exception {
		List<WrkJobCd> workJobList = commonService.wrkJobList(wrkJobCd);
		
		return success(workJobList).toJSONObject().get("rows").toString();
	}
	
	/* 데이터베이스 조회 */
	@RequestMapping(value = "/getDatabaseOfWrkjobCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDatabase(@ModelAttribute("apmApplSql") ApmApplSql apmApplSql) throws Exception {
		List<ApmApplSql> databaseList = selfTuningPlugInService.databaseListOfWrkjobCd(apmApplSql);
		
		return success(databaseList).toJSONObject().get("rows").toString();
	}
	
	/* ODS_USERS 데이터 조회 */
	@RequestMapping(value = "/getUserName", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getUserName(@ModelAttribute("odsUsers") OdsUsers odsUsers) throws Exception {
		String dbid = odsUsers.getDbid();
		List<OdsUsers> resultList = new ArrayList<OdsUsers>();
		if(!dbid.equals("")) {
			resultList = commonService.getUserName(odsUsers);
		}
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	private ApmApplSql setApmApplSql(ApmApplSqlPlugIn apmApplSqlPlugIn) {
		ApmApplSql apmApplSql = new ApmApplSql();
		
		apmApplSql.setTable_owner(apmApplSqlPlugIn.getTable_owner());
		apmApplSql.setAccess_path_type(apmApplSqlPlugIn.getAccess_path_type());
		apmApplSql.setSelectivity_calc_method(apmApplSqlPlugIn.getSelectivity_calc_method());
		apmApplSql.setDbio(apmApplSqlPlugIn.getDbio());
		apmApplSql.setTr_cd(apmApplSqlPlugIn.getTr_cd());
		apmApplSql.setWrkjob_cd(apmApplSqlPlugIn.getWrkjob_cd());
		apmApplSql.setAppl_name(apmApplSqlPlugIn.getAppl_name());
		apmApplSql.setAppl_hash(apmApplSqlPlugIn.getAppl_hash());
		apmApplSql.setSql_hash(apmApplSqlPlugIn.getSql_hash());
		apmApplSql.setElapsed_time(apmApplSqlPlugIn.getElapsed_time());
		apmApplSql.setExec_cnt(apmApplSqlPlugIn.getExec_cnt());
		apmApplSql.setSql_text(apmApplSqlPlugIn.getSql_text());
		apmApplSql.setNdv_ratio(apmApplSqlPlugIn.getNdv_ratio());
		apmApplSql.setCol_null(apmApplSqlPlugIn.getCol_null());
		apmApplSql.setParsing_schema_name(apmApplSqlPlugIn.getParsing_schema_name());
		apmApplSql.setDb_operate_type_nm(apmApplSqlPlugIn.getDb_operate_type_nm());
		apmApplSql.setDefault_yn(apmApplSqlPlugIn.getDefault_yn());
		
		apmApplSql.setUuid(apmApplSqlPlugIn.getUuid());
		apmApplSql.setDbid(apmApplSqlPlugIn.getDbid());
//		apmApplSql.setDefaultText(apmApplSqlPlugIn.getDefaultText());
		apmApplSql.setDefaultText(replaceSqlText(apmApplSqlPlugIn));
		apmApplSql.setUuid(apmApplSqlPlugIn.getUuid());
		apmApplSql.setUser_id(apmApplSqlPlugIn.getUser_id());
		
		return apmApplSql;
	}
	
	private String replaceSqlText(ApmApplSqlPlugIn apmApplSqlPlugIn) {
		String sqlText = apmApplSqlPlugIn.getDefaultText();
		String[] bindVarNmArray = null;
		String bindVarNm = "";
		int startPSIdx = -1;	/* Generate PreparedStatement properties (e.g #{})*/
		int startUSIdx = -1;	/* Unmodifed String (e.g ${}) */
		int startUTIdx = -1;	/* Uniq Type string (e.g ##) */
		
		if(apmApplSqlPlugIn.getBind_var_nm() != null && apmApplSqlPlugIn.getBind_var_nm().indexOf(";") != -1) {
			bindVarNmArray = apmApplSqlPlugIn.getBind_var_nm().split(";");
			
			for(int arrayIdx = 0; arrayIdx < bindVarNmArray.length; arrayIdx++) {
				bindVarNm = bindVarNmArray[arrayIdx].substring(1);
				
				startPSIdx = sqlText.indexOf("#{" + bindVarNm);
				startUSIdx = sqlText.indexOf("${" + bindVarNm);
				
				if(startPSIdx > 0) {
					while( (startPSIdx = sqlText.indexOf("#{" + bindVarNm, startPSIdx)) > 0 ) {
						if(sqlText.indexOf("}", startPSIdx + 2) == (startPSIdx + 2 + bindVarNm.length()) ) {
							String beginText = sqlText.substring(0, startPSIdx);
							String afterText = sqlText.substring(startPSIdx + 2 + bindVarNm.length() + 1);
							
							sqlText = beginText + ":" + bindVarNm + afterText;
						}
						
						startPSIdx = startPSIdx + bindVarNm.length();
					}
				} else if(startUSIdx > 0) {
					while( (startUSIdx = sqlText.indexOf("${" + bindVarNm, startUSIdx)) > 0 ) {
						if(sqlText.indexOf("}", startUSIdx + 2) == (startUSIdx + 2 + bindVarNm.length()) ) {
							String beginText = sqlText.substring(0, startUSIdx);
							String afterText = sqlText.substring(startUSIdx + 2 + bindVarNm.length() + 1);
							
							sqlText = beginText + ":" + bindVarNm + afterText;
						}
						
						startUSIdx = startUSIdx + bindVarNm.length();
					}
				} else {
					startUTIdx = sqlText.indexOf("#" + bindVarNm);
					
					if(startUTIdx > 0) {
						while( (startUTIdx = sqlText.indexOf("#" + bindVarNm, startUTIdx)) > 0 ) {
							if(sqlText.indexOf("#", startUTIdx + 1) == (startUTIdx + 1 + bindVarNm.length()) ) {
								String beginText = sqlText.substring(0, startUTIdx);
								String afterText = sqlText.substring(startUTIdx + 1 + bindVarNm.length() + 1);
								
								sqlText = beginText + ":" + bindVarNm + afterText;
							}
							
							startUTIdx = startUTIdx + bindVarNm.length();
						}
					}
				}
			}
		}
		
		return sqlText;
	}
	
	/* Plan 생성이 완료되었는지 Polling으로 체크한다. */
	@RequestMapping(value = "/SelfTestNew", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public @ResponseBody Result SelfTestNew(@RequestBody ApmApplSqlPlugIn apmApplSqlPlugIn, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("apmApplSqlPlugIn : \n" + apmApplSqlPlugIn.toString());
		logger.debug("wrkjob_cd :" + apmApplSqlPlugIn.getWrkjob_cd());
		Result result = new Result();
		String selftunQuerySeq = "";
		
		ApmApplSql apmApplSql = null;
		
		try {
			apmApplSql = setApmApplSql(apmApplSqlPlugIn);
			
			// 1. SELFTUN_SQL INSERT
			selftunQuerySeq = selfTuningPlugInService.insertSelftunQuery(apmApplSql);
			logger.debug("selftunQuerySeq :" + selftunQuerySeq);
			
			// 2. 서버모듈 - 셀프테스트 이력 조회
			result = selfTuningPlugInService.startSelfTestNew(apmApplSqlPlugIn, selftunQuerySeq);
			result.setResult(true);
			result.setMessage("성능점검수행을 완료하였습니다.");
		} catch (SQLException se) {
			logger.error("SQL error ==> " + se.getMessage());
			result.setResult(false);
			result.setMessage(se.getMessage());
		} catch (Exception e) {
			logger.error("error ==> " + e.getMessage());
			result.setResult(false);
			result.setMessage(e.getMessage());
		}
		
		return result;
	}
	
	/* 셀프튜닝 >성능점검결과 grid1 */
	@RequestMapping(value = "/PerfCheckResultList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public @ResponseBody String PerfCheckResultList(@RequestBody ApmApplSqlPlugIn apmApplSqlPlugIn, Model model) {
		List<ApmApplSql> resultList = new ArrayList<ApmApplSql>();
		ApmApplSql apmApplSql = null;
		
		try {
			apmApplSql = setApmApplSql(apmApplSqlPlugIn);
			
			resultList = selfTuningService.selectPerfCheckResultList(apmApplSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 셀프튜닝 >성능점검결과 grid2 */
	@RequestMapping(value = "/selectDeployPerfChkDetailResultList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public @ResponseBody String selectDeployPerfChkDetailResultList(@RequestBody SelftunExecutionPlan selftunExecutionPlan, Model model) {
		logger.debug("/PerfCheckResultList/selectDeployPerfChkDetailResultList started");
		logger.debug("selftunExecutionPlan:" + selftunExecutionPlan);
		String dbid = StringUtils.defaultString(selftunExecutionPlan.getDbid());
		logger.debug("dbid:" + dbid);
		String selftun_query_seq = StringUtils.defaultString(selftunExecutionPlan.getSelftun_query_seq());
		logger.debug("selftun_query_seq:" + selftun_query_seq);
		List<PerformanceCheckMng> resultList = new ArrayList<PerformanceCheckMng>();
		
		if (dbid.equals("") || selftun_query_seq.equals("")) {
			return success(resultList).toJSONObject().toString();
		}
		
		try {
			resultList = selfTuningService.selectDeployPerfChkDetailResultList(selftunExecutionPlan);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/executionPlan1", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public @ResponseBody String executionPlan1(@RequestBody SelftunExecutionPlan1 selftunExecutionPlan1, Model model) {
		List<SelftunExecutionPlan1> resultList = new ArrayList<SelftunExecutionPlan1>();
		try {
			resultList = selfTuningService.executionPlan1(selftunExecutionPlan1);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* 셀프튜닝 - Explain Plan Detail Popup 조회 */
	@RequestMapping(value = "/popup/explanPlanDetail", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public @ResponseBody Result explanPlanDetail(@RequestBody DbaTables dbaTables, Model model) {
		Result result = new Result();
		try {
			result = selfTuningService.explainPlanDetailPopup(dbaTables);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 셀프튜닝 - Explain Plan Detail Popup [ access_path / change history ] 조회 */
	@RequestMapping(value = "/popup/explanPlanDetail2", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public @ResponseBody Result explanPlanDetailPopup2Action(@RequestBody DbaTables dbaTables, Model model) {
		Result result = new Result();
		List<String> stringResultList = new ArrayList<String>();
		List<AccPathExec> accpathList = new ArrayList<AccPathExec>();
		
		/* 이후부터 주석 처리된 코드는 Plugin에서 제공하지 않는 정보임 */
//		List<DbTabColumnHistory> columnHistoryList = new ArrayList<DbTabColumnHistory>();
//		List<DbIndexHistory> indexHistoryList = new ArrayList<DbIndexHistory>();
		
		try {
			accpathList = selfTuningService.accpathList(dbaTables);
//			columnHistoryList = selfTuningService.columnHistoryList(dbaTables);
//			indexHistoryList = selfTuningService.indexHistoryList(dbaTables);
			
			result.setResult(true);
			stringResultList.add(0, success(accpathList).toJSONObject().toString());
//			stringResultList.add(1, success(columnHistoryList).toJSONObject().toString());
//			stringResultList.add(2, success(indexHistoryList).toJSONObject().toString());
			
			result.setStringList(stringResultList);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 셀프튜닝 - 인덱스자동설계 action */
	@RequestMapping(value = "/popup/startSelfIndexAutoDesign", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public @ResponseBody Result startSelfIndexAutoDesign2(@RequestBody ApmApplSqlPlugIn apmApplSqlPlugIn, Model model) {
		Result result = new Result();
		List<IdxAdMst> resultList = new ArrayList<IdxAdMst>();
		String selftunQuerySeq = "";
		ApmApplSql apmApplSql = null;
		
		try {
			// 1. SELFTUN_SQL INSERT
			apmApplSql = setApmApplSql(apmApplSqlPlugIn);
			
			// 1. SELFTUN_SQL INSERT
			selftunQuerySeq = selfTuningPlugInService.insertSelftunQuery(apmApplSql);
			
			resultList = selfTuningService.startSelfIndexAutoDesign(apmApplSql, selftunQuerySeq);
			result.setResult(resultList.size() > 0 ? true : false);
//			result.setMessage("인덱스 자동설계 내역이 존재하지 않습니다.");
			result.setObject(resultList);
			result.setTxtValue(success(resultList).toJSONObject().toString());
		} catch (Exception ex) {
			result.setMessage(ex.getMessage());
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 튜닝요청 INSERT */
	@RequestMapping(value = "/requestImprovementMultiAction", method = RequestMethod.POST, consumes= {"application/json; charset=utf8"})
	public @ResponseBody Result requestImprovementMultiAction(@RequestBody TuningTargetSqlPlugIn tuningTargetSqlPlugIn,
													Model model) {
		Result result = new Result();
		int resultCnt = 0;
		
		try {
			resultCnt = improvementManagementPlugInService.insertImprovement(tuningTargetSqlPlugIn);
			
			if (resultCnt > 0) {
				result.setResult(true);
				result.setMessage("튜닝요청에 성공하였습니다.");
			} else {
				result.setResult(false);
				result.setMessage("튜닝요청에 실패하였습니다.");
			}
		} catch (Exception ex) {
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
}