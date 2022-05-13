package omc.spop.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Cd;
import omc.spop.model.PlanCompareResult;
import omc.spop.model.Project;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.TuningTargetSql;
import omc.spop.server.tools.SPopTools;
import omc.spop.server.tune.ProjectPerfChk;
import omc.spop.service.CommonService;
import omc.spop.service.DBChangePerformanceImpactAnalysisForTiberoService;
import omc.spop.service.SQLAutomaticPerformanceCheckService;
import omc.spop.utils.DBUtil;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2021.10.28	이재우	최초작성.
 **********************************************************/

@RequestMapping(value="/DBChangePerformanceImpactAnalysisForTibero")
@Controller
public class DBChangePerformanceImpactAnalysisForTiberoController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(
			DBChangePerformanceImpactAnalysisForTiberoController.class);

	@Autowired
	private DBChangePerformanceImpactAnalysisForTiberoService dbChangePerformanceImpactAnalysisForTiberoService;
	
	@Autowired
	private SQLAutomaticPerformanceCheckService sqlAutomaticPerformanceCheckService;
	
	@Autowired
	private CommonService commonService;
	
	private String dbcd = "TIBERO";
	
	@RequestMapping( value = "/dbChangePerformanceImpactAnalysisForTibero", method = RequestMethod.GET )
	public String autoPerformanceCompareBetweenDatabase( 
			@RequestParam(required = false) Map<String, String> param, Model model ) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "dbChangePerformanceImpactAnalysisForTibero/dbChangePerformanceImpactAnalysisForTiberoDesign";
	}
	
	/* DB간 자동성능비교 :: 성능비교 탭 */
	@RequestMapping( value = "/PerformanceCompareForTibero", method = RequestMethod.GET )
	public String performanceCompare2( 
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		String commonCode = "1082";
		String commonExecutionCode = "1086";
		
		String nowDate = DateUtil.getNowDate( "yyyy-MM-dd" );
		String startDate = DateUtil.getPlusDays( "yyyy-MM-dd", "yyyy-MM-dd", nowDate, -1 );
		String endDate = startDate;
		startDate =	DateUtil.getPlusMons( "yyyy-MM-dd", "yyyy-MM-dd", startDate, -1 );
		
		String startTime = "00:00";
		String endTime = "23:59";
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("commonCode", commonCode);
		model.addAttribute("database_kinds_cd", dbcd);
		model.addAttribute("commonExecutionCode", commonExecutionCode);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "dbChangePerformanceImpactAnalysisForTibero/performanceCompareForTibero";
	}
	
	/* DB간 자동성능비교 :: 성능비교 결과 탭 */
	@RequestMapping( value = "/PerformanceCompareResultForTibero", method = RequestMethod.GET )
	public String PerformanceCompareResult_V2(
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		
		model.addAttribute("database_kinds_cd", dbcd);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "dbChangePerformanceImpactAnalysisForTibero/performanceCompareResultForTibero";
	}
	
	/* DB간 자동성능비교 :: 튜닝 실적 탭 */
	@RequestMapping( value = "/TuningPerformanceForTibero", method = RequestMethod.GET )
	public String TuningPerformance(
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		
		model.addAttribute("database_kinds_cd", dbcd);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "dbChangePerformanceImpactAnalysisForTibero/tuningPerformanceForTibero";
	}
	
	/* DB간 자동성능비교 :: 튜닝 SQL 일괄 검증 탭 */
	@RequestMapping( value = "/TuningSqlBatchVerifyForTibero", method = RequestMethod.GET )
	public String TuningSqlBatchVerify(
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		
		model.addAttribute("commonCode", "1082");
		model.addAttribute("database_kinds_cd", dbcd);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "dbChangePerformanceImpactAnalysisForTibero/tuningSqlBatchVerifyForTibero";
	}
	
	/* DB간 자동성능비교 :: SQL 점검팩 탭 */
	@RequestMapping( value = "/SqlCheckForTibero", method = RequestMethod.GET )
	public String SqlCheck(
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		
		model.addAttribute("database_kinds_cd", dbcd);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "dbChangePerformanceImpactAnalysisForTibero/sqlCheckForTibero";
	}
	
	@RequestMapping( value="/getProjectList", method=RequestMethod.GET, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getProjectList() throws Exception {
		List<Project> resultList = new ArrayList<Project>();
		
		try {
			resultList = commonService.projectList( null );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			return getErrorJsonString( ex );
		}
		
		return success( resultList ).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping( value="/getSqlPerfPacName", method=RequestMethod.POST, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getSqlPerfPacName(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		List<SQLAutoPerformanceCompare> outerList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.getSqlPerfPacName( sqlAutoPerformanceCompare );
			
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

	@RequestMapping( value="/getOperationDB", method=RequestMethod.POST, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getOperationDB(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		List<SQLAutoPerformanceCompare> outerList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			sqlAutoPerformanceCompare.setUser_id( user_id );
			
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.getOperationDB( sqlAutoPerformanceCompare );
			
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
	
	@RequestMapping( value="/getTobeSqlPerfPacName", method=RequestMethod.POST, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getTobeSqlPerfPacName(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		List<SQLAutoPerformanceCompare> outerList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.getTobeSqlPerfPacName( sqlAutoPerformanceCompare );
			
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
	
	@RequestMapping( value="/getSqlPerfDetailInfo", method=RequestMethod.POST, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getSqlPerfDetailInfo(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.getSqlPerfDetailInfo( sqlAutoPerformanceCompare);
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			return getErrorJsonString( ex );
		}
		
		return success( resultList ).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping( value="/getSqlPerformanceInfo", method=RequestMethod.POST, produces="application/text; charset=utf8" )
	@ResponseBody
	public String getSqlPerformanceInfo(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.getSqlPerformanceInfo( sqlAutoPerformanceCompare );
			
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
		Result result = new Result();
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int count = 0;
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.getSqlPerfPacName( sqlAutoPerformanceCompare );
			
			if ( resultList.size() > 0) {
				for (SQLAutoPerformanceCompare sqlComp : resultList) {
					if ( sqlComp.getPerf_check_name().trim().equals( sqlAutoPerformanceCompare.getPerf_check_name().trim() ) ) {
						result.setResult( false );
						result.setMessage("동일한 SQL점검팩명으로 저장할 수 없습니다.");
						return result;
					}
				}
			}
			
			count = dbChangePerformanceImpactAnalysisForTiberoService.insertSqlPerformanceInfo( sqlAutoPerformanceCompare );
			
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
		Result result = new Result();
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int count = 0;
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.getSqlPerfPacName( sqlAutoPerformanceCompare );
			
			if ( resultList.size() > 0) {
				for (SQLAutoPerformanceCompare sqlComp : resultList) {
					if ( sqlComp.getPerf_check_name().trim().equals( sqlAutoPerformanceCompare.getPerf_check_name().trim() ) && 
							sqlComp.getSql_auto_perf_check_id().equals( sqlAutoPerformanceCompare.getSql_auto_perf_check_id() ) == false ) {
						result.setResult( false );
						result.setMessage("동일한 SQL점검팩명으로 저장할 수 없습니다.");
						return result;
					}
				}
			}
			count = dbChangePerformanceImpactAnalysisForTiberoService.updateSqlPerformanceInfo( sqlAutoPerformanceCompare );
			
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
	
	/* 프로젝트에 자동성능점검 수행중인 회차가 있는지 조회 */
	@RequestMapping(value = "/countExecuteTms", method = RequestMethod.POST)
	@ResponseBody
	public Result countExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare) throws Exception {
		Result result = new Result();
		
		try{
			// 수행중 count
			int Ecount = dbChangePerformanceImpactAnalysisForTiberoService.countExecuteTms( sqlAutoPerformanceCompare );
			
			// 수행중일 때
			if ( Ecount > 0 ) {
				result.setTxtValue( "false" );
			} else {
				result.setTxtValue( "true" );
				// 수행한 count
				int Rcount = dbChangePerformanceImpactAnalysisForTiberoService.countPerformanceRecord( sqlAutoPerformanceCompare );
				
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
	
	/* SQL-25,27  프로젝트에 자동성능점검 수행중인 회차가 있는지 조회 */
	@RequestMapping(value = "/countTuningExecuteTms", method = RequestMethod.POST)
	@ResponseBody
	public Result countTuningExecuteTms( SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) throws Exception {
		Result result = new Result();
//		sqlAutoPerformanceCompare.setProject_id( sqlAutoPerformanceCompare.getParent_project_id() );
		
		try{
			// 수행중 count (SQL-25)
			int Ecount = dbChangePerformanceImpactAnalysisForTiberoService.countTuningExecuteTms( sqlAutoPerformanceCompare );
			
			// 수행중일 때
			if ( Ecount > 0 ) {
				result.setTxtValue( "false" );
			} else {
				result.setTxtValue( "true" );
				
				// 부모의 점검팩번호가 아닌 자신의 점검팩번호로 조회
				sqlAutoPerformanceCompare.setSql_auto_perf_check_id( sqlAutoPerformanceCompare.getParent_sql_auto_perf_check_id() );
				// 수행한 count (SQL-27)
				int Rcount = dbChangePerformanceImpactAnalysisForTiberoService.countPerformanceRecord( sqlAutoPerformanceCompare );
				
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
	
	/* SQL 성능자동비교 Update*/
	@RequestMapping(value = "/updateSqlAutoPerformance", method = RequestMethod.POST)
	@ResponseBody
	public Result updateSqlAutoPerformance(
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck )
					throws Exception {
		Result result = new Result();
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			
			sqlAutomaticPerformanceCheck.setPerf_check_executer_id(user_id);
			
			result = dbChangePerformanceImpactAnalysisForTiberoService.updateSqlAutoPerformance(sqlAutomaticPerformanceCheck);
		} catch ( BadSqlGrammarException bsge ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + bsge.getMessage());
			result.setResult(false);
			result.setMessage("실행오류가 발생하였습니다.<br>필터링조건을 다시 확인해주세요.");
		} catch ( Exception ex ) {
			logger.error("Exception 종류 =========> "+ex.getCause() + " , class = "+ ex.getClass() + " , stack = " +ex.getStackTrace());
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 성능영향도분석 master call */
	@RequestMapping(value="/performanceCompareCall", method = RequestMethod.POST)
	@ResponseBody
	public Result performanceCompareCall( 
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck )
			throws Exception {
		Result result = new Result();
		
		try {
			result = dbChangePerformanceImpactAnalysisForTiberoService.performanceCompareCall(sqlAutomaticPerformanceCheck);
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 성능영향도분석결과 재실행 master call */
	@RequestMapping(value="/performanceCompareReCall", method = RequestMethod.POST)
	@ResponseBody
	public Result performanceCompareReCall( 
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
					throws Exception {
		Result result = new Result();
		
		try {
			result = dbChangePerformanceImpactAnalysisForTiberoService.performanceCompareReCall(sqlAutoPerformanceCompare);
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL 성능자동비교 Update*/
	@RequestMapping(value = "/updateTuningSqlAutoPerformance", method = RequestMethod.POST)
	@ResponseBody
	public Result updateTuningSqlAutoPerformance(
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck )
					throws Exception {
		Result result = new Result();
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			
			sqlAutomaticPerformanceCheck.setPerf_check_executer_id(user_id);
			sqlAutomaticPerformanceCheck.setPerf_check_name( "튜닝 SQL 일괄검증" );
			sqlAutomaticPerformanceCheck.setPerf_check_desc( "튜닝된 SQL에 대한 성능 일괄 검증을 수행합니다." );
			sqlAutomaticPerformanceCheck.setPerf_check_type_cd( "2" );
			
			result = dbChangePerformanceImpactAnalysisForTiberoService.updateTuningSqlAutoPerformance( sqlAutomaticPerformanceCheck );
			
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
		Result result = new Result();
		
		try {
			result = dbChangePerformanceImpactAnalysisForTiberoService.forceUpdateSqlAutoPerformance( sqlAutomaticPerformanceCheck );
//			result = sqlAutomaticPerformanceCheckService.forceUpdateSqlAutomaticPerformanceCheck( sqlAutomaticPerformanceCheck );
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 강제완료처리 UPDATE */
	@RequestMapping(value = "/forceUpdateTuningSqlAutoPerf", method = RequestMethod.POST)
	@ResponseBody
	public Result forceUpdateTuningSqlAutoPerf( SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck )
			throws Exception {
		Result result = new Result();
		
		try {
			result = dbChangePerformanceImpactAnalysisForTiberoService.forceUpdateTuningSqlAutoPerf( sqlAutomaticPerformanceCheck );
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value="/getMaxSqlCheckId", method = RequestMethod.POST)
	@ResponseBody
	public SQLAutoPerformanceCompare getMaxSqlCheckId( 
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ) 
					throws Exception {
		try {
			sqlAutoPerformanceCompare = dbChangePerformanceImpactAnalysisForTiberoService.getMaxSqlCheckId( sqlAutoPerformanceCompare );
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
		}
		return sqlAutoPerformanceCompare;
	}
	/* Chart Data action */
	@RequestMapping(value = "/loadPerfChartData", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadChartData(
			@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		logger.debug( sqlAutomaticPerformanceCheck.toString() );
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadPerfChartData( sqlAutomaticPerformanceCheck );
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success( resultList ).toJSONObject().toString();
	}
	
	/* 성능 영향도 분석 결과 List */
	@RequestMapping(value="/loadPerformanceResultList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceResultList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int dataCount4NextBtn = 0;
		logger.debug("result_list = "+sqlAutoPerformanceCompare.getPagePerCount());
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadPerformanceResultList( sqlAutoPerformanceCompare );
			
			if ( resultList != null && resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
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
		resultList = null;
		return jobj.toString();
	}
	
	// 성능영향도분석결과ListAll
	@RequestMapping(value="/loadPerformanceResultListAll", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceResultListAll (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int dataCount4NextBtn = 0;
		logger.debug("result_list = "+sqlAutoPerformanceCompare.getPagePerCount());
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadPerformanceResultListAll( sqlAutoPerformanceCompare );
			
//			if(resultList != null && resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
//				dataCount4NextBtn = resultList.size();
//				resultList.remove(sqlAutoPerformanceCompare.getPagePerCount());
//				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
//			}
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	// SQL-21 튜닝SQL일괄검증 > 상단 gridList
	@RequestMapping(value="/loadTuningBatchValidationNorthList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningBatchValidationNorthList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int dataCount4NextBtn = 0;
		logger.debug("result_list = "+sqlAutoPerformanceCompare.getPagePerCount());
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadTuningBatchValidationNorthList( sqlAutoPerformanceCompare );
			
			if ( resultList != null && resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
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
		resultList = null;
		return jobj.toString();
	}
	
	// SQL-21 튜닝SQL일괄검증 > 하단 gridList
	@RequestMapping(value="/loadTuningBatchValidationSouthList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningBatchValidationSouthList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int dataCount4NextBtn = 0;
		sqlAutoPerformanceCompare.setSql_id(" ");
		logger.debug("result_list = "+sqlAutoPerformanceCompare.getPagePerCount());
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadTuningBatchValidationSouthList( sqlAutoPerformanceCompare );
			
			if ( resultList != null && resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
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
		resultList = null;
		return jobj.toString();
	}

	@RequestMapping(value="/loadTuningBatchValidationSouthListAll", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningBatchValidationSouthListAll (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int dataCount4NextBtn = 0;
		sqlAutoPerformanceCompare.setSql_id(" ");
		logger.debug("result_list = "+sqlAutoPerformanceCompare.getPagePerCount());
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadTuningBatchValidationSouthListAll( sqlAutoPerformanceCompare );
			
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	
	/* 수행결과 action */
	/* SQL-16 수행결과 조회  */
	@RequestMapping(value = "/loadPerfResultCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerfResultCount(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadPerfResultCount( sqlAutoPerformanceCompare );
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* SQL-23 수행결과 action */
	@RequestMapping(value = "/loadTuningPerfResultCount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningPerfResultCount(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		
		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
		sqlAutomaticPerformanceCheck.setDatabase_kinds_cd( sqlAutoPerformanceCompare.getDatabase_kinds_cd() );
		
		try {
			
			List<SQLAutoPerformanceCompare> subList =
					dbChangePerformanceImpactAnalysisForTiberoService.getTobeSqlPerfPacName( sqlAutoPerformanceCompare );
			
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
			
			resultList = sqlAutomaticPerformanceCheckService.loadPerformanceCheckCount( sqlAutomaticPerformanceCheck );
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
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
		Result result = new Result();
		int count = 0;
		
		try {
			count = dbChangePerformanceImpactAnalysisForTiberoService.updateAutoPerfChkIsNull( sqlAutoPerformanceCompare );
			
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
	
	// 수행결과 - 전체 값이 0일 경우  update
	@RequestMapping( value="/getPerformanceResultCount", method=RequestMethod.POST )
	@ResponseBody
	public Result getPerformanceResultCount(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
			throws Exception {
		Result result = new Result();
		int count = 0;
		
		try {
			count = dbChangePerformanceImpactAnalysisForTiberoService.getPerformanceResultCount( sqlAutoPerformanceCompare );
			
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
	
	/* 튜닝대상선정 grid 개별선택 시 중복값 check */
	@RequestMapping( value="/getTargetEqualCount", method=RequestMethod.POST )
	@ResponseBody
	public Result getTargetEqualCount(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
			throws Exception {
		Result result = new Result();
		
		try {
			int count = dbChangePerformanceImpactAnalysisForTiberoService.getTargetEqualCount( sqlAutoPerformanceCompare );
			
			if ( count > 0 ) {
				result.setResult( false );
			} else {
				result.setResult( true );
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	/* 성능영향도분석결과 > 튜닝담당자지정 팝업 > 튜닝요청 및 담당자 지정 */
	@RequestMapping(value="/Popup/InsertTuningRequest", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertTuningRequest( @ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model ) {
		Result result = new Result();
		
		try {
			logger.debug("##### 튜닝대상선정 진행 #####");
			result = dbChangePerformanceImpactAnalysisForTiberoService.insertTuningRequest( tuningTargetSql );
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 튜닝SQL일괄검증 > 튜닝담당자지정 팝업 > 튜닝요청 및 담당자 지정 */
	@RequestMapping(value="/Popup/insertSelectedTuningTarget", method=RequestMethod.POST)
	@ResponseBody
	public Result insertSelectedTuningTarget( @ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model ) {
		Result result = new Result();
		
		try {
			logger.debug("##### 튜닝SQL 일괄검증 > 튜닝대상선정 진행 #####");
			result = dbChangePerformanceImpactAnalysisForTiberoService.insertSelectedTuningTarget( tuningTargetSql );
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	// 성능영향도분석결과 > SQL MEMO 조회
	@RequestMapping(value="/Popup/getSqlMemo", method=RequestMethod.POST)
	@ResponseBody
	public Result getSqlMemo(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare
			, Model model ) {
		Result result = new Result();
		
		try {
			String resultStr = dbChangePerformanceImpactAnalysisForTiberoService.getSqlMemo( sqlAutoPerformanceCompare );
			
			if ( resultStr != null && "".equals(resultStr) == false ) {
				result.setResult(true);
				result.setMessage( resultStr ); 
			} else {
				result.setResult(false);
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	// 성능영향도분석결과 > SQL MEMO 저장
	@RequestMapping(value="/Popup/updateSqlMemo", method=RequestMethod.POST)
	@ResponseBody
	public Result updateSqlMemo(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare
			, Model model ) {
		Result result = new Result();
		
		try {
			int cnt = dbChangePerformanceImpactAnalysisForTiberoService.updateSqlMemo( sqlAutoPerformanceCompare );
			
			if ( cnt > 0 ) {
				result.setResult(true);
				result.setMessage("저장 되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("저장하지 못했습니다.");
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	// 성능영향도분석결과 > SQL MEMO 
	@RequestMapping(value="/Popup/deleteSqlMemo", method=RequestMethod.POST)
	@ResponseBody
	public Result deleteSqlMemo(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare
			, Model model ) {
		Result result = new Result();
		
		try {
			int cnt = dbChangePerformanceImpactAnalysisForTiberoService.deleteSqlMemo( sqlAutoPerformanceCompare );
			
			if ( cnt > 0 ) {
				result.setResult(true);
				result.setMessage("삭제 되었습니다.");
			} else {
				result.setResult(false);
				result.setMessage("삭제하지 못했습니다.");
			}
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 튜닝실적 list */
	@RequestMapping(value="/loadTuningPerformanceList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningPerformanceList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadTuningPerformanceList( sqlAutoPerformanceCompare );
			
		} catch (Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		resultList = null;
		return jobj.toString();
	}
	
	/* 튜닝실적 상세 list */
	@RequestMapping(value="/loadTuningPerformanceDetailList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadTuningPerformanceDetailList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int dataCount4NextBtn = 0;
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadTuningPerformanceDetailList( sqlAutoPerformanceCompare );
			
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
		resultList = null;
		return jobj.toString();
	}
	
	/* 튜닝실적 상세 list */
	@RequestMapping(value="/loadSqlPerformancePacList", method = RequestMethod.POST , produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqlPerformancePacList (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		Cd cd = new Cd();
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		List<Cd> cdList = new ArrayList<Cd>();
		
		int dataCount4NextBtn = 0;
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadSqlPerformancePacList( sqlAutoPerformanceCompare );
			
			if(resultList != null && resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(sqlAutoPerformanceCompare.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			cd.setGrp_cd_id("1086");
			cdList = commonService.commonCodeList(cd);
			
			for ( SQLAutoPerformanceCompare sqlCom : resultList ) {
				if ( sqlCom.getPerf_compare_meth_cd() != null && "".equals( sqlCom.getPerf_compare_meth_cd() ) == false ) {
					sqlCom.setPerf_compare_meth_cd( cdList.get( Integer.parseInt( sqlCom.getPerf_compare_meth_cd() ) -1 ).getRef_vl_1() );
				}
			}
			
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	/* SQL점검팩 - 튜닝선정 조회 여부 */
	@RequestMapping(value="/getTuningTargetCount", method = RequestMethod.POST )
	@ResponseBody
	public Result getTuningTargetCount (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		Result result = new Result();
		try {
			int count = dbChangePerformanceImpactAnalysisForTiberoService.getTuningTargetCount( sqlAutoPerformanceCompare );
			
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
	
	/* SQL점검팩 - 튜닝선정 조회 여부 */
	@RequestMapping(value="/getTuningVerifyTargetCount", method = RequestMethod.POST )
	@ResponseBody
	public Result getTuningVerifyTargetCount (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		Result result = new Result();
		try {
			sqlAutoPerformanceCompare.setProject_id( sqlAutoPerformanceCompare.getParent_project_id() );
			sqlAutoPerformanceCompare.setSql_auto_perf_check_id( sqlAutoPerformanceCompare.getParent_sql_auto_perf_check_id() );
			
			int count = dbChangePerformanceImpactAnalysisForTiberoService.getTuningTargetCount( sqlAutoPerformanceCompare );
			
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
	
	/* SQL점검팩 - 튜닝선정 조회 여부 */
	@RequestMapping(value="/countTuningEndTms", method = RequestMethod.POST )
	@ResponseBody
	public Result countTuningEndTms (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		Result result = new Result();
		try {
			
			int count = dbChangePerformanceImpactAnalysisForTiberoService.countTuningEndTms( sqlAutoPerformanceCompare );
			
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
//			ex.printStackTrace();
		}
		return result;
	}
	
	/* SQL 점검팩 - 삭제 처리 */
	@RequestMapping(value="/deleteSqlAutoPerformanceChk", method = RequestMethod.POST )
	@ResponseBody
	public Result deleteSqlAutoPerformanceChk (
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model ) {
		Result result = new Result();
		try {
			int count = dbChangePerformanceImpactAnalysisForTiberoService.deleteSqlAutoPerformanceChk( sqlAutoPerformanceCompare );
			
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
	
	/* SQL-45: 성능비교 결과 탭 Bind Value 신규 */
	@RequestMapping(value = "/loadExplainBindValueNew", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExplainBindValueNew(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) throws Exception {
		
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadExplainBindValueNew( sqlAutoPerformanceCompare);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
//		return success(resultList).toJSONObject().toString();
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 성능비교 결과 탭 Bind Value 고도화 */
	@RequestMapping(value = "/loadExplainInfoBindValue", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadExplainInfoBindValue(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) throws Exception {
		
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		
		try {
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadExplainInfoBindValue( sqlAutoPerformanceCompare );
			for ( SQLAutoPerformanceCompare sqlCom : resultList ) {
				sqlCom.setPlanCompare("<a href='javascript:;' onclick='Btn_compare("+sqlCom.getPlan_hash_value()+
						");'><img src='/resources/images/fileCompare.jpg' style='height:15px;vertical-align:middle;'/></a>");
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																															
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value="/loadAfterSelectTextPlanListAll", method=RequestMethod.POST)
	@ResponseBody
	public Result loadAfterSelectTextPlanListAll(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) {
		Result result = new Result();
		
		try{
			result.setResult(true);
			result.setObject(dbChangePerformanceImpactAnalysisForTiberoService.loadAfterSelectTextPlanListAll( sqlAutoPerformanceCompare ));
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	@RequestMapping(value="/loadAfterDMLTextPlanListAll", method=RequestMethod.POST)
	@ResponseBody
	public Result loadAfterDMLTextPlanListAll(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) {
		Result result = new Result();
		
		try{
			result.setResult(true);
			result.setObject(dbChangePerformanceImpactAnalysisForTiberoService.loadAfterDMLTextPlanListAll( sqlAutoPerformanceCompare ));
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	@RequestMapping(value="/loadPerformanceList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String loadPerformanceList(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) {
		List<SQLAutoPerformanceCompare> resultList = new ArrayList<SQLAutoPerformanceCompare>();
		int dataCount4NextBtn = 0;
		
		try{
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadPerformanceList( sqlAutoPerformanceCompare );
			
			if ( resultList != null && resultList.size() > 0 ) {
				if( resultList.size() > sqlAutoPerformanceCompare.getPagePerCount()){
					dataCount4NextBtn = resultList.size();
					resultList.remove(sqlAutoPerformanceCompare.getPagePerCount());
					/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
				}
				
				for ( SQLAutoPerformanceCompare sqlCom : resultList ) {
					if ( "완료".equals(sqlCom.getPerf_check_force_close_yn() ) ) {
						sqlCom.setDefaultText("<a href='javascript:;' onclick='Btn_resultGo("+
								sqlAutoPerformanceCompare.getProject_id()+","+
								sqlCom.getSql_auto_perf_check_id()+","+
								sqlCom.getDatabase_kinds_cd()
								+");'><img src='/resources/images/report.png' style='height:15px;vertical-align:middle;'/></a>");
					}
				}
			}
		} catch(Exception ex) {
			String methodName = new Object() { }.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		resultList = null;
		return jobj.toString();
	}
	
	@RequestMapping(value="/callSqlProfileApply", method = RequestMethod.POST)
	@ResponseBody
	public Result callSqlProfileApply(
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		Result result = new Result();
		 
		try {
			result = dbChangePerformanceImpactAnalysisForTiberoService.callSqlProfileApply( sqlAutoPerformanceCompare );
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* 원천 DB */
	@RequestMapping(value = "/loadOriginalDb", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadOriginalDb(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck, Model model) {
		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
		List<SQLAutomaticPerformanceCheck> finalList = new ArrayList<SQLAutomaticPerformanceCheck>();
		SQLAutomaticPerformanceCheck temp = new SQLAutomaticPerformanceCheck();
		
		try {
			String isChoice = StringUtils.defaultString(sqlAutomaticPerformanceCheck.getIsChoice());
			String isAll = StringUtils.defaultString(sqlAutomaticPerformanceCheck.getIsAll());
			
			if (isChoice.equals("Y")) {
				temp.setOriginal_dbid("");
				temp.setOriginal_db_name("선택");
			} else if (isAll.equals("Y")) {
				temp.setOriginal_dbid("");
				temp.setOriginal_db_name("전체");
			}
			
			resultList = dbChangePerformanceImpactAnalysisForTiberoService.loadOriginalDb( sqlAutomaticPerformanceCheck );
			
			finalList.add(temp);
			finalList.addAll(resultList);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(finalList).toJSONObject().get("rows").toString();
	}
	
	/* 성능 영향도 분석 결과 엑셀 다운로드 */
	@RequestMapping(value = "/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public void excelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		
		model.addAttribute("fileName", "성능_영향도_분석_결과");
		
		try {
			dbChangePerformanceImpactAnalysisForTiberoService.excelDownload( sqlAutoPerformanceCompare, model, req, res );
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
	}
	
	/* 튜닝실적 엑셀 다운로드 */
	@RequestMapping(value = "/excelTuningDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public void excelTuningDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		
		model.addAttribute("fileName", "튜닝실적");
		
		if ( sqlAutoPerformanceCompare.getSql_auto_perf_check_id() == null ) {
			sqlAutoPerformanceCompare.setSql_auto_perf_check_id("");
		}
		
		try {
			dbChangePerformanceImpactAnalysisForTiberoService.excelTuningDownload( sqlAutoPerformanceCompare, model, req, res );
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	/* 튜닝실적 상세 엑셀 다운로드 */
	@RequestMapping(value = "/excelTuningDetailDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public void excelTuningDetailDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		
		model.addAttribute("fileName", "튜닝실적상세");
		
		try {
			dbChangePerformanceImpactAnalysisForTiberoService.excelTuningDetailDownload( sqlAutoPerformanceCompare, model, req, res );
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	/*튜닝 SQL일괄 검증 상단List Excel다운*/
	@RequestMapping(value = "/excelNorthDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public void excelNorthDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		model.addAttribute("fileName", "튜닝_SQL_일괄_검증");
		
		try {
			dbChangePerformanceImpactAnalysisForTiberoService.excelNorthDownload( sqlAutoPerformanceCompare, model, req, res  );
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	/*튜닝 SQL일괄 검증 하단List Excel다운*/
	@RequestMapping(value = "/excelSouthDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public void excelSouthDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare, Model model)
					throws Exception {
		
		model.addAttribute("fileName", "튜닝_SQL_일괄_검증_상세");
		
		try {
			dbChangePerformanceImpactAnalysisForTiberoService.excelSouthDownload( sqlAutoPerformanceCompare, model, req, res  );
		} catch ( Exception ex ) {
			String methodName = new Object() { }.getClass().getEnclosingMethod().getName();
			ex.printStackTrace();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
	
	/* plan 비교 기능 */
	@RequestMapping(value = "/getPlanCompareResultForTibero", method = RequestMethod.GET)
	@ResponseBody
	public String getPlanCompareResultForTibero(@ModelAttribute("planCompareResult") PlanCompareResult planCompareResult) {
		List<PlanCompareResult> compareResult = Collections.emptyList();
		
		try {
			compareResult = dbChangePerformanceImpactAnalysisForTiberoService.getPlanCompareResultForTibero(planCompareResult);
			
		} catch (SQLException e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + e.getMessage() );
			
			return getErrorJsonString( e );
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + e.getMessage() );
			
			return getErrorJsonString( e );
		}
		
		return success( compareResult ).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/getAuthUserNameTibero", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAuthUserNameTibero(String DBID) {
		String ret = "";
		logger.debug("######################dbid = "+ DBID);
		
		try {
			Long dbid = Long.valueOf(DBID);
			ret = ProjectPerfChk.getUsersTibero(dbid, StringUtil.getRandomJobKey());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			DBUtil.printErrMsg(SPopTools.isDebug, ex);
			return getErrorJsonString( ex );
		}
		
		return ret;
	}
	
	@RequestMapping(value = "/getGrantScriptsTibero", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getGrantScriptsTibero(String DBID, String obOwner, String obType ) {
		String ret = "";
		logger.debug("######################dbid = "+ DBID +", obOwner ="+ obOwner+" , obType = "+obType);

		try {
			Long dbid = Long.valueOf(DBID);
			
			ret = ProjectPerfChk.getGrantScriptsTibero(dbid, obOwner, obType, StringUtil.getRandomJobKey());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			
			DBUtil.printErrMsg(SPopTools.isDebug, ex);
			return getErrorJsonString( ex );
		}
		
		return ret;
	}
	
	@RequestMapping(value = "/getTiberoSqlId", method = RequestMethod.POST)
	@ResponseBody
	public Result getTiberoSqlId(String projectId, String sqlAutoPerfCheckId, String sqlId, String planHashValue ) {
		Result result = new Result();
		String ret = "";
		PlanCompareResult pcr = new PlanCompareResult();
		
		try {
			if ( projectId != null && sqlAutoPerfCheckId != null && sqlId != null && planHashValue != null 
					&& "".equals( projectId ) == false &&  "".equals( sqlAutoPerfCheckId ) == false
					&& "".equals( sqlId ) == false &&  "".equals( planHashValue ) == false ) {
				pcr.setProject_id( projectId );
				pcr.setSql_auto_perf_check_id( sqlAutoPerfCheckId );
				pcr.setSql_id( sqlId );
				pcr.setPlan_hash_value( planHashValue );
				
				ret = dbChangePerformanceImpactAnalysisForTiberoService.getTiberoSqlId( pcr );
			} else {
				ret = "";
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( methodName + " 예외발생 ==> " + ex.getMessage() );
			result.setResult(false);
			result.setMessage("예외발생. 관리자에게 문의해주세요.");
			
			return result;
		}
		
		if ( ret != null && "".equals( ret ) == false ) {
			result.setResult(true);
			result.setMessage( ret );
		} else {
			result.setResult(false);
			result.setMessage("SQL이 실행되지 않아 TIBERO SQL ID 가 없습니다.");
		}
		
		return result;
	}
}
