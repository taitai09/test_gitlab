package omc.spop.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.model.Result;
import omc.spop.model.SQLAutoPerformanceCompare;
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.model.SqlGrid;
import omc.spop.model.Sqls;
import omc.spop.model.VsqlText;
import omc.spop.service.AISQLPVService;
import omc.spop.utils.TreeWrite;

/********************************************************
 * Full Name	AutoIndexSQLPerformanceVerificationController
 ********************************************************/

@RequestMapping(value = "/AISQLPV")
@Controller
public class AISQLPVController extends InterfaceController{
	private static final Logger logger = LoggerFactory.getLogger(AISQLPVController.class);
	
	@Autowired
	private AISQLPVService aisqlpvService;
	
	/* 공통 - SQL점검팩 리스트 */
	@RequestMapping(value = "/getSqlPerformancePacList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getSqlPerformancePacList(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare ){
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = aisqlpvService.getSqlPerformancePacList( sqlAutoPerformanceCompare );
			returnStr = success( resultList ).toJSONObject().get("rows").toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			returnStr = getErrorJsonString( ex );
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	/* SQL점검팩 - SQL점검팩 검색 */
	@RequestMapping(value = "/loadSqlPerformancePacList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqlPerformancePacList(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		JSONObject jobj = null;
		String returnStr = "";
		int pagePerCount = 0;
		int dataCount4NextBtn = 0;
		
		try {
			resultList = aisqlpvService.loadSqlPerformancePacList( sqlAutoPerformanceCompare );
			pagePerCount = sqlAutoPerformanceCompare.getPagePerCount();
			
			if( resultList != null && resultList.size() > pagePerCount ){
				dataCount4NextBtn = resultList.size();
				resultList.remove( pagePerCount );
			}
			jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
			returnStr = jobj.toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			returnStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
			jobj = null;
		}
		
		return returnStr;
	}
	
	/* SQL점검팩 - SQL점검팩 신규등록 */
	@RequestMapping( value="/insertSqlPerformanceInfo", method=RequestMethod.POST )
	@ResponseBody
	public Result insertSqlPerformanceInfo(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		Result result = new Result();
		
		try {
			aisqlpvService.insertSqlPerformanceInfo( sqlAutoPerformanceCompare, result );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	/* SQL점검팩 - SQL점검팩 수정 */
	@RequestMapping( value="/updateSqlPerformanceInfo", method=RequestMethod.POST )
	@ResponseBody
	public Result updateSqlPerformanceInfo(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		Result result = new Result();
		
		try {
			aisqlpvService.updateSqlPerformanceInfo( sqlAutoPerformanceCompare, result );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	/* SQL점검팩 - 성능 분석 진행 중인 건 조회 */
	@RequestMapping( value="/checkUnfinishedCount", method=RequestMethod.POST )
	@ResponseBody
	public Result checkUnfinishedCount(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		Result result = new Result();
		
		try {
			aisqlpvService.checkUnfinishedCount( sqlAutoPerformanceCompare, result );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	/* SQL점검팩 - SQL점검팩 삭제 */
	@RequestMapping( value="/deleteSqlPerfInfo", method=RequestMethod.POST )
	@ResponseBody
	public Result deleteSqlPerfInfo(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		Result result = new Result();
		
		try {
			aisqlpvService.deleteSqlPerfInfo( sqlAutoPerformanceCompare, result );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			result.setResult( false );
			result.setMessage( ex.getMessage() );
		}
		
		return result;
	}
	
	/* 인덱스별 성능 영향도 분석 - 검색(집계 데이터) */
	@RequestMapping(value = "/loadSummaryData", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSummaryData(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		JSONObject jobj = null;
		String returnStr = "";
		
		try {
			resultList = aisqlpvService.loadSummaryData( sqlAutoPerformanceCompare );
			
			jobj = success(resultList).toJSONObject();
			returnStr = jobj.toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			returnStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
			jobj = null;
		}
		
		return returnStr;
	}
	
	/* 인덱스별 성능 영향도 분석 - 검색(좌측 테이블) */
	@RequestMapping(value = "/loadIndexList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadIndexList(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		JSONObject jobj = null;
		String returnStr = "";
		int pagePerCount = 0;
		int dataCount4NextBtn = 0;
		
		try {
			resultList = aisqlpvService.loadIndexList( sqlAutoPerformanceCompare );
			pagePerCount = sqlAutoPerformanceCompare.getPagePerCount();
			
			if( resultList != null && resultList.size() > pagePerCount ){
				dataCount4NextBtn = resultList.size();
				resultList.remove( pagePerCount );
			}
			jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
			returnStr = jobj.toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			returnStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
			jobj = null;
		}
		
		return returnStr;
	}
	
	/* 인덱스별 성능 영향도 분석 - 검색(우측 테이블) */
	@RequestMapping(value = "/loadSqlListByIndex", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqlListByIndex(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		JSONObject jobj = null;
		String returnStr = "";
		int pagePerCount = 0;
		int dataCount4NextBtn = 0;
		
		try {
			resultList = aisqlpvService.loadSqlListByIndex( sqlAutoPerformanceCompare );
			pagePerCount = sqlAutoPerformanceCompare.getPagePerCount();
			
			if( resultList != null && resultList.size() > pagePerCount ){
				dataCount4NextBtn = resultList.size();
				resultList.remove( pagePerCount );
			}
			jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
			returnStr = jobj.toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			returnStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
			jobj = null;
		}
		
		return returnStr;
	}
	
	/* 인덱스별 성능 영향도 분석 - 엑셀다운로드 */
	@RequestMapping(value = "/loadSqlListByIndex/excelDownload", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public void loadSqlListExcel(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare
			, Model model, HttpServletRequest req, HttpServletResponse res) {
		
		try {
			model.addAttribute("fileName", "인덱스별_성능_영향도_분석_결과");
			aisqlpvService.loadSqlListExcel( sqlAutoPerformanceCompare, model, req, res );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
		}
	}
	
	@RequestMapping(value="/loadAfterSelectTextPlanListAll", method=RequestMethod.POST)
	@ResponseBody
	public Result loadAfterSelectTextPlanListAll(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare,
			Model model) {
		
		Result result = new Result();
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		
		try{
			resultList = aisqlpvService.loadAfterSelectTextPlanListAll( sqlAutoPerformanceCompare );
			
			result.setResult(true);
			result.setObject(resultList);
			
		} catch  ( Exception ex ){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/loadAfterPlanTree", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadAfterPlanTree(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		List<SqlGrid> resultList = Collections.emptyList();
		List<SqlGrid> buildList = Collections.emptyList();
		JSONArray jsonArray = new JSONArray();
		String returnValue = "";
		
		try {
			resultList = aisqlpvService.loadAfterPlanTree( sqlAutoPerformanceCompare );
			buildList = TreeWrite.buildSQLTree(resultList, "-1");
			
			jsonArray = JSONArray.fromObject(buildList);
			returnValue = jsonArray.toString();
			
		} catch  ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
		} finally {
			resultList = null;
			buildList = null;
			jsonArray = null;
		}
		
		return returnValue;	
	}
	
	@RequestMapping(value = "/loadNoExecAfterPlanTree", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadNoExecAfterPlanTree(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		List<SqlGrid> resultList = Collections.emptyList();
		List<SqlGrid> buildList = Collections.emptyList();
		JSONArray jsonArray = new JSONArray();
		String returnValue = "";
		
		try {
			resultList = aisqlpvService.loadNoExecAfterPlanTree( sqlAutoPerformanceCompare );
			buildList = TreeWrite.buildSQLTree(resultList, "-1");
			
			jsonArray = JSONArray.fromObject(buildList);
			returnValue = jsonArray.toString();
			
		} catch  ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
		} finally {
			resultList = null;
			buildList = null;
			jsonArray = null;
		}
		
		return returnValue;	
	}
	
	/* SQL별 성능 영향도 분석 - 검색(전체 수행 결과 건수) */
	@RequestMapping(value = "/loadResultCount", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadResultCount(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = aisqlpvService.loadResultCount( sqlAutoPerformanceCompare );
			returnStr = success(resultList).toJSONObject().get("rows").toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex);
			returnStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	/* SQL별 성능 영향도 분석 - 검색 건수 */
	@RequestMapping( value = "/loadNumberOfSearch", method=RequestMethod.GET )
	@ResponseBody
	public Result loadNumberOfSearch(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare )
			throws Exception {
		
		Result result = new Result();
		int count = 0;
		
		try {
			count = aisqlpvService.loadNumberOfSearch( sqlAutoPerformanceCompare );
			
			result.setResult( true );
			result.setTxtValue( String.valueOf(count) );
				
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL별 성능 영향도 분석 - 검색(chart 데이터) */
	@RequestMapping(value = "/loadPerfChartData", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadPerfChartData(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck) {
		
		List<SQLAutomaticPerformanceCheck> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = aisqlpvService.loadPerfChartData( sqlAutomaticPerformanceCheck );
			returnStr = success(resultList).toJSONObject().toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			returnStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	/* SQL별 성능 영향도 분석 - 검색(그리드 데이터) */
	@RequestMapping(value = "/loadResultList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadResultList(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare) {
		
		List<SQLAutoPerformanceCompare> resultList = Collections.emptyList();
		JSONObject jobj = null;
		String returnStr = "";
		int pagePerCount = 0;
		int dataCount4NextBtn = 0;
		
		try {
			resultList = aisqlpvService.loadResultList( sqlAutoPerformanceCompare );
			pagePerCount = sqlAutoPerformanceCompare.getPagePerCount();
			
			if( resultList != null && resultList.size() > pagePerCount ){
				dataCount4NextBtn = resultList.size();
				resultList.remove( pagePerCount );
			}
			jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			
			returnStr = jobj.toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			returnStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
			jobj = null;
		}
		
		return returnStr;
	}
	
	/* SQL별 성능 영향도 분석 - 엑셀다운로드 */
	@RequestMapping(value = "/loadResultList/excelDownload", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public void loadResultListExcel(@ModelAttribute("sqlAutoPerformanceCompare") SQLAutoPerformanceCompare sqlAutoPerformanceCompare
			, Model model, HttpServletRequest req, HttpServletResponse res) {
		
		try {
			model.addAttribute("fileName", "SQL별_성능_영향도_분석_결과");
			aisqlpvService.loadResultListExcel( sqlAutoPerformanceCompare, model, req, res );
			
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
		}
	}
	
	/* 인덱스recommend - 팝업 좌측 리스트 */
	@RequestMapping(value = "/loadSqlIdList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadSqlIdList( @ModelAttribute("vsqlText") VsqlText vsqlText ) {
		List<VsqlText> resultList = Collections.emptyList();
		String returnStr = "";
		
		try{
			resultList = aisqlpvService.loadSqlIdList(vsqlText);
			returnStr = success(resultList).toJSONObject().toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object(){}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			returnStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
	
	/* 인덱스recommend - 팝업 차트 데이터 */
	@RequestMapping(value = "/sqlStateTrend", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sqlStateTrend(@ModelAttribute("sqls") Sqls sqls, Model model) {
		List<Sqls> resultList = Collections.emptyList();
		String returnStr = "";
		
		try {
			resultList = aisqlpvService.sqlStateTrend(sqls);
			returnStr = success(resultList).toJSONObject().toString();
			
		} catch ( Exception ex ) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
			
		}finally {
			resultList = null;
		}
		
		return returnStr;
	}
}