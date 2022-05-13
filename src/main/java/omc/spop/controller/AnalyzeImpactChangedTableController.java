package omc.spop.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.OdsUsers;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.SqlPerfImplAnalTable;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.AnalyzeImpactChangedTableService;
import omc.spop.service.AutoPerformanceCompareBetweenDbService;
import omc.spop.service.SQLAutomaticPerformanceCheckService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2021.02.08	황예지	최초작성
 **********************************************************/

@RequestMapping(value="/AnalyzeImpactChangedTable")
@Controller
public class AnalyzeImpactChangedTableController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(
			AnalyzeImpactChangedTableController.class);
	
	@Autowired
	private AnalyzeImpactChangedTableService analyzeImpactChangedTableService;
	
	@Autowired
	private SQLAutomaticPerformanceCheckService sqlAutomaticPerformanceCheckService;
	
	@Autowired
	private AutoPerformanceCompareBetweenDbService autoPerformanceCompareBetweenDbService;
	
	private String projectId = "0";
	private String perfCheckTypeCd= "3";
	private String choiceDivCd = "J";

	@RequestMapping( value = "", method = RequestMethod.GET )
	public String analyzeImpactChangedTable( 
			@RequestParam(required = false) String menu_id, @RequestParam(required = false) String menu_nm,
			Model model ) {
		
		model.addAttribute("menu_id", menu_id); 
		model.addAttribute("menu_nm", menu_nm);
		
		return "analyzeImpactChangedTable/analyzeImpactChangedTableDesign";
	}
	
	/* 테이블 변경 성능 영향도 분석 :: 성능 영향도 분석 */
	@RequestMapping( value = "/PerformanceImpactAnalysis", method = RequestMethod.GET )
	public String performanceImpactAnalysis( 
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		
		getDates(model);
		
		String commonCode = "1082";
		
		model.addAttribute("commonCode", commonCode);
		model.addAttribute("database_kinds_cd", "ORACLE");
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "analyzeImpactChangedTable/performanceImpactAnalysis";
	}
	
	/* 테이블 변경 성능 영향도 분석 :: 성능 영향도 분석 결과 */
	@RequestMapping( value = "/PerformanceImpactAnalysisResult", method = RequestMethod.GET )
	public String performanceImpactAnalysisResult(
			@RequestParam(required = false) String menu_id, @RequestParam(required = false) String menu_nm,
			Model model ) {
		
		getDates(model);
		
		model.addAttribute("database_kinds_cd", "ORACLE");
		model.addAttribute("menu_id", menu_id); 
		model.addAttribute("menu_nm", menu_nm);
		
		return "analyzeImpactChangedTable/performanceImpactAnalysisResult";
	}
	
	/* 테이블 변경 성능 영향도 분석 :: 튜닝실적 */
	@RequestMapping( value = "/TuningPerformance", method = RequestMethod.GET )
	public String tuningPerformance(
			@RequestParam(required = false) String menu_id, @RequestParam(required = false) String menu_nm,
			Model model ) {
		
		getDates(model);
		
		model.addAttribute("database_kinds_cd", "ORACLE");
		model.addAttribute("menu_id", menu_id); 
		model.addAttribute("menu_nm", menu_nm);
		
		return "analyzeImpactChangedTable/tuningPerformance";
	}
	
	@RequestMapping( value = "/SqlCheck", method = RequestMethod.GET )
	public String SqlCheck(
			@RequestParam(required = false) String menu_id, @RequestParam(required = false) String menu_nm,
			Model model ) {
		
		getDates(model);
		
		model.addAttribute("database_kinds_cd", "ORACLE");
		model.addAttribute("menu_id", menu_id); 
		model.addAttribute("menu_nm", menu_nm);
		
		return "analyzeImpactChangedTable/sqlCheck";
	}
	
	/* API Controller */
	/* SQL점검팩 콤보박스 조회 */
	@RequestMapping(value="/getSqlPerfPacName", produces="application/text; charset=utf8" )
	@ResponseBody
	public String getSqlPerfPacName(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) {
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		sqlAutoPerformanceCompare.setPerf_check_type_cd(perfCheckTypeCd);
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.getSqlPerfPacName( sqlAutoPerformanceCompare );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			return getErrorJsonString( ex );
		}
		return success( resultList ).toJSONObject().get("rows").toString();
	}
	
	/* SQL점검팩 선택 시  최종 성능비교 정보 조회 */
	@RequestMapping(value="/getSqlPerfDetailInfo", method=RequestMethod.GET, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getSqlPerfDetailInfo(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) {
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.getSqlPerfDetailInfo( sqlAutoPerformanceCompare);
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			return getErrorJsonString( ex );
		}
		
		return success( resultList ).toJSONObject().get("rows").toString();
	}
	
	//소스DB, 목표DB 불러오기
	@RequestMapping( value="/getOperationDB", method=RequestMethod.GET, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getOperationDB(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		List<SQLAutoPerformanceCompare> outerList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			sqlAutoPerformanceCompare.setUser_id( user_id );
			
			resultList = autoPerformanceCompareBetweenDbService.getOperationDB( sqlAutoPerformanceCompare );
			
			String isAll = StringUtils.defaultString(sqlAutoPerformanceCompare.getIsAll());
			String isChoice = StringUtils.defaultString(sqlAutoPerformanceCompare.getIsChoice());
			
			if ( isAll.equals("Y") ) {
				sqlAutoPerformanceCompare.setPerf_check_name("전체");
				sqlAutoPerformanceCompare.setSql_auto_perf_check_id("");
			} else if ( isChoice.equals("Y") ) {
				sqlAutoPerformanceCompare.setPerf_check_name("선택");
			} else {
			}
			
			outerList.add( sqlAutoPerformanceCompare );
			outerList.addAll( resultList );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			return getErrorJsonString( ex );
		}
		
		return success( outerList ).toJSONObject().get("rows").toString();
	}
	
	/* OWNER 리스트 조회 */
	@RequestMapping(value="/getTableOwner", method=RequestMethod.GET, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getTableOwner(@ModelAttribute("odsUsers") OdsUsers odsUsers) {
		
		List<OdsUsers> resultList = Collections.emptyList();
		
		try {
			resultList = sqlAutomaticPerformanceCheckService.getTableOwner(odsUsers);
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			return getErrorJsonString( ex );
		}
		return success( resultList ).toJSONObject().get("rows").toString();
	}
	
	/* 튜닝선정 조회 여부 */
	@RequestMapping(value="/getTuningTargetCount", method = RequestMethod.GET )
	@ResponseBody
	public Result getTuningTargetCount(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		Result result = new Result();
		
		try {
			int count = autoPerformanceCompareBetweenDbService.getTuningTargetCount( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( false );
			} else {
				result.setResult( true );
			}
			
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult( false );
			result.setMessage( ex.getMessage() );
			ex.printStackTrace();
		}
		return result;
	}
	
	/* 자동성능점검 수행중인 회차가 있는지 조회 */
	@RequestMapping(value = "/countExecuteTms", method = RequestMethod.GET)
	@ResponseBody
	public Result countExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		Result result = new Result();
		
		try{
			// 수행중 count
			int Ecount = autoPerformanceCompareBetweenDbService.countExecuteTms( sqlAutoPerformanceCompare );
			int Rcount = -1;
			// 수행중일 때
			if ( Ecount > 0 ) {
				result.setTxtValue( "false" );
			} else {
				result.setTxtValue( "true" );
				// 수행한 count
				Rcount = autoPerformanceCompareBetweenDbService.countPerformanceRecord( sqlAutoPerformanceCompare );
				
				// 수행한 내역이 1개 이상
				if ( Rcount > 0 ) {
					result.setResult( false );
				} else {
					result.setResult( true );
				}
			}
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		return result;
	}
	
	/* 성능 영향도 분석 Update*/
	@RequestMapping(value = "/updateSqlAutoPerformance", method = RequestMethod.POST)
	@ResponseBody
	public Result updateSqlAutoPerformance(
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck )
					throws Exception {
		
		sqlAutomaticPerformanceCheck.setProject_id(projectId);
		
		Result result = new Result();
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			sqlAutomaticPerformanceCheck.setPerf_check_executer_id(user_id);
			
			result = analyzeImpactChangedTableService.updateSqlAutoPerformance(sqlAutomaticPerformanceCheck);
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 성능 영향도 분석 MasterCall*/
	@RequestMapping(value="/performanceCompareCall", method = RequestMethod.POST)
	@ResponseBody
	public Result performanceCompareCall( 
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck )
			throws Exception {
		
		sqlAutomaticPerformanceCheck.setProject_id(projectId);
		
		Result result = new Result();
		
		try {
			result = autoPerformanceCompareBetweenDbService.performanceCompareCall(sqlAutomaticPerformanceCheck);
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 강제완료처리 UPDATE */
	@RequestMapping(value = "/forceUpdateSqlAutoPerformance", method = RequestMethod.POST)
	@ResponseBody
	public Result forceUpdateSqlAutoPerformance( SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck )
			throws Exception {
		
		sqlAutomaticPerformanceCheck.setProject_id(projectId);
		
		Result result = new Result();
		
		try {
			result = autoPerformanceCompareBetweenDbService.forceUpdateSqlAutoPerformance( sqlAutomaticPerformanceCheck );
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}

	/* SQL-3 수행결과 조회 */
	@RequestMapping(value = "/loadTuningPerfResultCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningPerfResultCount(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutomaticPerformanceCheck> resultList = Collections.emptyList();
		List<SQLAutoPerformanceCompare> subList = Collections.emptyList();
		
		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
		
		try {
			subList = autoPerformanceCompareBetweenDbService.getTobeSqlPerfPacName( sqlAutoPerformanceCompare );
			
			for ( SQLAutoPerformanceCompare sqlCom : subList ) {
				if ( sqlCom.getProject_id().equals( sqlAutoPerformanceCompare.getProject_id() ) &&
					 sqlCom.getSql_auto_perf_check_id().equals( sqlAutoPerformanceCompare.getSql_auto_perf_check_id() )) {
					sqlAutomaticPerformanceCheck.setProject_id( sqlCom.getVerify_project_id() );
					sqlAutomaticPerformanceCheck.setSql_auto_perf_check_id( sqlCom.getVerify_sql_auto_perf_check_id() );
					break;
				}
			}
			
			if ( sqlAutomaticPerformanceCheck.getSql_auto_perf_check_id() == null ) {
				sqlAutomaticPerformanceCheck.setProject_id( "" );
				sqlAutomaticPerformanceCheck.setSql_auto_perf_check_id( "" );
			}
			
			resultList = autoPerformanceCompareBetweenDbService.loadPerformanceCheckCount( sqlAutomaticPerformanceCheck );
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* SQL-16 수행결과 조회  */
	@RequestMapping(value = "/loadPerfResultCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerfResultCount(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.loadPerfResultCount( sqlAutoPerformanceCompare );
			
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	// 수행결과 action
	@RequestMapping(value = "/loadPerformanceCheckCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceCheckCount(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = Collections.emptyList();
		
		sqlAutomaticPerformanceCheck.setProject_id(projectId);
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.loadPerformanceCheckCount(sqlAutomaticPerformanceCheck);
			
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	// 수행결과 - 전체 값이 0일 경우  update
	@RequestMapping( value="/updateAutoPerfChkIsNull", method=RequestMethod.POST )
	@ResponseBody
	public Result updateAutoPerfChkIsNull(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		Result result = new Result();
		int count = 0;
		
		try {
			count = autoPerformanceCompareBetweenDbService.updateAutoPerfChkIsNull( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( true );
				result.setMessage( "저장 되었습니다." );
			} else {
				result.setResult( false );
				result.setMessage( "저장하지 못했습니다." );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	/*검색 결과 건수 조회*/
	@RequestMapping( value="/getPerformanceResultCount", method=RequestMethod.POST )
	@ResponseBody
	public Result getPerformanceResultCount(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
			throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);

		Result result = new Result();
		
		try {
			int count = 0;
			count = analyzeImpactChangedTableService.getPerformanceResultCount( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( true );
				result.setMessage( "저장 되었습니다." );
				result.setTxtValue(String.valueOf(count));
			} else {
				result.setResult( false );
				result.setMessage( "저장하지 못했습니다." );
				result.setTxtValue("0");
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	// N-1: 좌측 그리드(테이블별 현황)
	@RequestMapping(value="/loadLeftTableList", method = RequestMethod.GET , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadLeftTableList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) throws ParseException {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		List<SqlPerfImplAnalTable> resultList = Collections.emptyList();
		
		try {
			resultList = analyzeImpactChangedTableService.loadTableCHGPerfChkTargetLeftList(sqlAutoPerformanceCompare);
			
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		
		return jobj.toString();
	}
	
	// N-2: 우측 그리드(테이블별 현황 상세)
	@RequestMapping(value="/loadRightTableList", method = RequestMethod.GET , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadRightTableList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model) throws ParseException {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		int dataCount4NextBtn = 0;
		
		try {
			resultList = analyzeImpactChangedTableService.loadTableCHGPerfChkTargetRightList(sqlAutoPerformanceCompare);
			
			if(resultList != null && resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlAutoPerformanceCompare.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* 성능 영향도 분석 결과 - 엑셀 다운로드 */
	@RequestMapping(value = "/excelDownload")
	public ModelAndView excelDownload(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		
		try {
			resultList = analyzeImpactChangedTableService.excelDownload( sqlAutoPerformanceCompare );
			
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "테이블_변경_성능_영향도_분석_결과");
		model.addAttribute("sheetName", "테이블_변경_성능_영향도_분석_결과");
		model.addAttribute("excelId", "CHANGED_TABLE_PERF_COMP");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 튜닝실적 - 검색 */
	@RequestMapping(value="/loadTuningPerformanceList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningPerformanceList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		sqlAutoPerformanceCompare.setChoice_div_cd(choiceDivCd);
		sqlAutoPerformanceCompare.setPerf_check_type_cd(perfCheckTypeCd);
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.loadTuningPerformanceList( sqlAutoPerformanceCompare );
			
		} catch (Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		
		return jobj.toString();
	}
	
	/* 튜닝실적 - 상세(상단 그리드 클릭 시) */
	@RequestMapping(value="/loadTuningPerformanceDetailList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningPerformanceDetailList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		sqlAutoPerformanceCompare.setChoice_div_cd(choiceDivCd);
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		int dataCount4NextBtn = 0;
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.loadTuningPerformanceDetailList( sqlAutoPerformanceCompare );
			
			if(resultList != null && resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlAutoPerformanceCompare.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* 튜닝실적 엑셀 다운로드 */
	@RequestMapping(value = "/excelTuningDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView excelTuningDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		sqlAutoPerformanceCompare.setChoice_div_cd(choiceDivCd);
		sqlAutoPerformanceCompare.setPerf_check_type_cd(perfCheckTypeCd);
		
		List<LinkedHashMap<String, Object>> resultList = Collections.emptyList();
		
		try {
			resultList = analyzeImpactChangedTableService.excelTuningDownload( sqlAutoPerformanceCompare );
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "튜닝실적");
		model.addAttribute("sheetName", "튜닝실적");
		model.addAttribute("excelId", "CHANGED_TABLE_TUNING_PERFORMANCE");
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 튜닝실적 상세 엑셀 다운로드 */
	@RequestMapping(value = "/excelTuningDetailDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public void excelTuningDetailDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		sqlAutoPerformanceCompare.setChoice_div_cd(choiceDivCd);
		model.addAttribute("fileName", "튜닝실적상세");
		
		try {
			autoPerformanceCompareBetweenDbService.excelTuningDetailDownload( sqlAutoPerformanceCompare, model, req, res );
			
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	/* SQL점검팩 - SQL점검팩 리스트 조회 */
	@RequestMapping(value="/loadSqlPerformancePacList", method = RequestMethod.GET , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqlPerformancePacList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		sqlAutoPerformanceCompare.setChoice_div_cd(choiceDivCd);
		sqlAutoPerformanceCompare.setPerf_check_type_cd(perfCheckTypeCd);
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		int dataCount4NextBtn = 0;
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.loadSqlPerformancePacList( sqlAutoPerformanceCompare );
			
			if(resultList != null && resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlAutoPerformanceCompare.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	// GridListAll
	@RequestMapping(value="/loadPerformanceResultListAll", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceResultListAll (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		int dataCount4NextBtn = 0;
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.loadPerformanceResultListAll( sqlAutoPerformanceCompare );
			
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* SQL-45: 성능비교 결과 탭 Bind Value 신규 */
	@RequestMapping(value = "/loadExplainBindValueNew", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExplainBindValueNew(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.loadExplainBindValueNew( sqlAutoPerformanceCompare);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* tobe plan - select */
	@RequestMapping(value="/loadAfterSelectTextPlanListAll", method=RequestMethod.POST)
	@ResponseBody
	public Result loadAfterSelectTextPlanListAll(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		Result result = new Result();
		
		try{
			result.setResult(true);
			result.setObject(autoPerformanceCompareBetweenDbService.loadAfterSelectTextPlanListAll( sqlAutoPerformanceCompare ));
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* tobe plan - dml */
	@RequestMapping(value="/loadAfterDMLTextPlanListAll", method=RequestMethod.POST)
	@ResponseBody
	public Result loadAfterDMLTextPlanListAll(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) {

		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		Result result = new Result();
		
		try{
			result.setResult(true);
			result.setObject(autoPerformanceCompareBetweenDbService.loadAfterDMLTextPlanListAll( sqlAutoPerformanceCompare ));
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	/* 튜닝담당자지정 팝업 > 튜닝요청 및 담당자 지정 */
	@RequestMapping(value="/Popup/InsertTuningRequest", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertTuningRequest(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql) {
		
		tuningTargetSql.setProject_id(projectId);
		
		Result result = new Result();
		
		try {
			logger.debug("##### 튜닝대상선정 진행 #####");
			result = autoPerformanceCompareBetweenDbService.insertTuningRequest( tuningTargetSql );
			result.setResult(true);
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL 점검팩 - 삭제 처리 */
	@RequestMapping(value="/deleteSqlAutoPerformanceChk", method = RequestMethod.POST )
	@ResponseBody
	public Result deleteSqlAutoPerformanceChk (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		Result result = new Result();
		try {
			int count = autoPerformanceCompareBetweenDbService.deleteSqlAutoPerformanceChk( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( true );
			} else {
				result.setResult( false );
			}
			
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult( false );
			result.setMessage( ex.getMessage() );
			ex.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping( value="/getSqlPerformanceInfo", method=RequestMethod.POST, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getSqlPerformanceInfo(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		try {
			resultList = autoPerformanceCompareBetweenDbService.getSqlPerformanceInfo( sqlAutoPerformanceCompare );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			return getErrorJsonString( ex );
		}
		
		return success( resultList ).toJSONObject().get("rows").toString();
	}
	
	// 점검팩 insert
	@RequestMapping( value="/insertSqlPerformanceInfo", method=RequestMethod.POST )
	@ResponseBody
	public Result insertSqlPerformanceInfo(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		sqlAutoPerformanceCompare.setPerf_check_type_cd(perfCheckTypeCd);
		
		Result result = new Result();
		
		int count = 0;
		
		try {
			count = autoPerformanceCompareBetweenDbService.insertSqlPerformanceInfo( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( true );
				result.setMessage( "저장 되었습니다." );
			} else {
				result.setResult( false );
				result.setMessage( "저장하지 못했습니다." );
			}
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	// 점검팩 update
	@RequestMapping( value="/updateSqlPerformanceInfo", method=RequestMethod.POST )
	@ResponseBody
	public Result updateSqlPerformanceInfo(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		sqlAutoPerformanceCompare.setPerf_check_type_cd(perfCheckTypeCd);
		
		Result result = new Result();
		
		int count = 0;
		
		try {
			count = autoPerformanceCompareBetweenDbService.updateSqlPerformanceInfo( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( true );
				result.setMessage( "저장 되었습니다." );
			} else {
				result.setResult( false );
				result.setMessage( "저장하지 못했습니다." );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	@RequestMapping(value="/getMaxSqlCheckId", method = RequestMethod.POST)
	@ResponseBody
	public SQLAutoPerformanceCompare getMaxSqlCheckId( 
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) 
					throws Exception {
		
		sqlAutoPerformanceCompare.setProject_id(projectId);
		
		try {
			sqlAutoPerformanceCompare = autoPerformanceCompareBetweenDbService.getMaxSqlCheckId( sqlAutoPerformanceCompare );
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
		}
		return sqlAutoPerformanceCompare;
	}
	
	private void getDates(Model model) {
		String nowDate = DateUtil.getNowDate( "yyyy-MM-dd" );	//오늘 날짜
		String aMonthAgo = DateUtil.getPlusMons( "yyyy-MM-dd", "yyyy-MM-dd", nowDate, -1 );	//오늘부터 한달 전
		String endDate = DateUtil.getPlusDays( "yyyy-MM-dd", "yyyy-MM-dd", nowDate, -1 );	//어제날짜
		String startDate = DateUtil.getPlusMons( "yyyy-MM-dd", "yyyy-MM-dd", endDate, -1 );	//어제부터 한달 전
		
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("aMonthAgo", aMonthAgo);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
	}
}
