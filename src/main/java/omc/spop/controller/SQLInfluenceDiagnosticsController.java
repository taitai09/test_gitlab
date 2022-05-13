package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.model.BeforePerfExpect;
import omc.spop.model.BeforePerfExpectSqlStat;
import omc.spop.model.OdsUsers;
import omc.spop.model.Result;
import omc.spop.model.SqlPerfImplAnalSql;
import omc.spop.model.SqlPerfImplAnalTable;
import omc.spop.model.SqlPerfImpluenceAnalysis;
import omc.spop.service.SQLInfluenceDiagnosticsService;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class SQLInfluenceDiagnosticsController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLInfluenceDiagnosticsController.class);
	
	@Autowired
	private SQLInfluenceDiagnosticsService sqlInfluenceDiagnosticsService;
	
	/* DB변경 SQL영향도 진단  */
	@RequestMapping(value="/TOPSQLDiagnostics")
	public String TOPSQLDiagnostics(@ModelAttribute("beforePerfExpect") BeforePerfExpect beforePerfExpect,  Model model) {
		model.addAttribute("menu_id", beforePerfExpect.getMenu_id() );
		model.addAttribute("menu_nm", beforePerfExpect.getMenu_nm() );
		model.addAttribute("call_from_child", beforePerfExpect.getCall_from_child());
		
		return "sqlInfluenceDiagnostics/topSqlDiagnostics";
	}
	
	/* DB변경 SQL영향도 진단 Action */
	@RequestMapping(value="/TOPSQLDiagnosticsAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TOPSQLDiagnosticsAction(@ModelAttribute("beforePerfExpect") BeforePerfExpect beforePerfExpect,  Model model) {
		List<BeforePerfExpect> resultList = new ArrayList<BeforePerfExpect>();

		try{
			if(beforePerfExpect.getDbid() != null && !beforePerfExpect.getDbid().equals("")){
				resultList = sqlInfluenceDiagnosticsService.topSQLDiagnosticsList(beforePerfExpect);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* DB변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케줄 정보 조회 */
	@RequestMapping(value = "/TOPSQLDiagnostics/Popup/GetInfo", method = RequestMethod.POST)
	@ResponseBody
	public Result GetTOPSQLDiagnostics(@ModelAttribute("beforePerfExpect") BeforePerfExpect beforePerfExpect, Model model) throws Exception {
		Result result = new Result();
		BeforePerfExpect temp = new BeforePerfExpect();
		
		String before_perf_expect_no = StringUtils.defaultString(beforePerfExpect.getBefore_perf_expect_no());

		try{
			if(!before_perf_expect_no.equals("")){
				temp = sqlInfluenceDiagnosticsService.getTOPSQLDiagnostics(beforePerfExpect);
				if(temp != null){
					result.setResult(true);
				}else{
					result.setResult(false);
				}
				result.setObject(temp);
			}else{
				result.setResult(false);
				result.setObject(temp);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* DB변경 SQL영향도 진단 - 스케줄등록 팝업  - 스케쥴 등록/수정 */
	@RequestMapping(value="/TOPSQLDiagnostics/Popup/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveTOPSQLDiagnostics(@ModelAttribute("beforePerfExpect") BeforePerfExpect beforePerfExpect,  Model model) {
		Result result = new Result();
		int insertCnt = 0;
		
		try{
			insertCnt = sqlInfluenceDiagnosticsService.saveTOPSQLDiagnostics(beforePerfExpect);
			result.setResult(insertCnt > 0 ? true : false);
			result.setMessage("DB변경 SQL영향도 진단 스케쥴 등록에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* DB변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케쥴 삭제 */
	@RequestMapping(value="/TOPSQLDiagnostics/Popup/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteTOPSQLDiagnostics(@ModelAttribute("beforePerfExpect") BeforePerfExpect beforePerfExpect,  Model model) {
		Result result = new Result();
		
		try{
			sqlInfluenceDiagnosticsService.deleteTOPSQLDiagnostics(beforePerfExpect);
			result.setResult(true);
			result.setMessage("DB변경 SQL영향도 진단 스케쥴 삭제에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	

	/* DB변경 SQL영향도 진단 - 상세내역 */
	@RequestMapping(value="/TOPSQLDiagnostics/Detail")
	public String TOPSQLDiagnosticsDetail(@ModelAttribute("beforePerfExpectSqlStat") BeforePerfExpectSqlStat beforePerfExpectSqlStat,  Model model) {
		model.addAttribute("menu_id", beforePerfExpectSqlStat.getMenu_id());
		model.addAttribute("menu_nm", beforePerfExpectSqlStat.getMenu_nm());
		
		return "sqlInfluenceDiagnostics/topSqlDiagnosticsDetail";
	}
	
	/* DB변경 SQL영향도 진단 - 상세내역 Action */
	@RequestMapping(value="/TOPSQLDiagnostics/DetailAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TOPSQLDiagnosticsDetailAction(@ModelAttribute("beforePerfExpectSqlStat") BeforePerfExpectSqlStat beforePerfExpectSqlStat,  Model model) {
		List<BeforePerfExpectSqlStat> resultList = new ArrayList<BeforePerfExpectSqlStat>();

		try{
			resultList = sqlInfluenceDiagnosticsService.topSQLDiagnosticsDetailList(beforePerfExpectSqlStat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* DB변경 SQL영향도 진단 - 상세내역 - SQL Profile Update */
	@RequestMapping(value="/TOPSQLDiagnostics/UpdateSQLProfile", method=RequestMethod.POST)
	@ResponseBody
	public Result UpdateSQLProfile(@ModelAttribute("beforePerfExpectSqlStat") BeforePerfExpectSqlStat beforePerfExpectSqlStat,  Model model) {
		Result result = new Result();
		int rowCnt = 0;

		try{
			rowCnt = sqlInfluenceDiagnosticsService.updateSQLProfile_TOPSQL(beforePerfExpectSqlStat);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("SQL PRFILE UPDATE에 실패하였습니다[TOP SQL]");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 오브젝트변경 SQL영향도 진단 */
	@RequestMapping(value="/ObjectImpactDiagnostics")
	public String ObjectImpactDiagnostics(@ModelAttribute("sqlPerfImpluenceAnalysis") SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis,  Model model) {
		model.addAttribute("menu_id", sqlPerfImpluenceAnalysis.getMenu_id() );
		model.addAttribute("menu_nm", sqlPerfImpluenceAnalysis.getMenu_nm());
		model.addAttribute("call_from_child", sqlPerfImpluenceAnalysis.getCall_from_child());
		
		return "sqlInfluenceDiagnostics/objectImpactDiagnostics";
	}
	
	/* 오브젝트변경 SQL영향도 진단 Action */
	@RequestMapping(value="/ObjectImpactDiagnosticsAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ObjectImpactDiagnosticsAction(@ModelAttribute("sqlPerfImpluenceAnalysis") SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis,  Model model) {
		List<SqlPerfImpluenceAnalysis> resultList = new ArrayList<SqlPerfImpluenceAnalysis>();

		try{
			if(sqlPerfImpluenceAnalysis.getDbid() != null && !sqlPerfImpluenceAnalysis.getDbid().equals("")){
				resultList = sqlInfluenceDiagnosticsService.objectImpactDiagnosticsList(sqlPerfImpluenceAnalysis);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케줄 정보 조회 */
	@RequestMapping(value = "/ObjectImpactDiagnostics/Popup/GetInfo", method = RequestMethod.POST)
	@ResponseBody
	public Result GetObjectImpactDiagnostics(@ModelAttribute("sqlPerfImpluenceAnalysis") SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis, Model model) throws Exception {
		Result result = new Result();
		SqlPerfImpluenceAnalysis temp = new SqlPerfImpluenceAnalysis();

		try{
			temp = sqlInfluenceDiagnosticsService.getObjectImpactDiagnostics(sqlPerfImpluenceAnalysis);
			result.setResult(true);
			result.setObject(temp);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 분석대상 테이블 목록[오른쪽] 조회 */
	@RequestMapping(value = "/ObjectImpactDiagnostics/Popup/GetTargetTable", method = RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GetTargetTable(@ModelAttribute("sqlPerfImplAnalTable") SqlPerfImplAnalTable sqlPerfImplAnalTable) throws Exception {
		List<SqlPerfImplAnalTable> resultList = new ArrayList<SqlPerfImplAnalTable>();

		try{
			resultList = sqlInfluenceDiagnosticsService.getTargetTableList(sqlPerfImplAnalTable);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 테이블Owner 목록 조회 */
	@RequestMapping(value = "/ObjectImpactDiagnostics/Popup/GetTableOwner", method = RequestMethod.GET, produces="application/text; charset=utf8")
	@ResponseBody
	public String GetTableOwner(@ModelAttribute("odsUsers") OdsUsers odsUsers) throws Exception {
		String dbid = StringUtils.defaultString(odsUsers.getDbid());
		
		List<OdsUsers> resultList = new ArrayList<OdsUsers>();
		if(!dbid.equals("")){
			resultList = sqlInfluenceDiagnosticsService.getTableOwner(odsUsers);
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 테이블 목록[왼쪽] 조회 */
	@RequestMapping(value = "/ObjectImpactDiagnostics/Popup/GetSelectTable", method = RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GetSelectTable(@ModelAttribute("sqlPerfImplAnalTable") SqlPerfImplAnalTable sqlPerfImplAnalTable) throws Exception {
		List<SqlPerfImplAnalTable> resultList = new ArrayList<SqlPerfImplAnalTable>();

		try{
			resultList = sqlInfluenceDiagnosticsService.getSelectTableList(sqlPerfImplAnalTable);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}		
	
	/* 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케쥴 등록/수정  */
	@RequestMapping(value="/ObjectImpactDiagnostics/Popup/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveObjectImpactDiagnostics(@ModelAttribute("sqlPerfImpluenceAnalysis") SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis,  Model model) {
		Result result = new Result();
		int insertCnt = 0;
		
		try{
			insertCnt = sqlInfluenceDiagnosticsService.saveObjectImpactDiagnostics(sqlPerfImpluenceAnalysis);
			result.setResult(insertCnt > 0 ? true : false);
			result.setMessage("오브젝트변경 SQL영향도 진단<br/>스케쥴 등록에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 오브젝트변경 SQL영향도 진단 - 스케줄등록 팝업 - 스케쥴 삭제 */
	@RequestMapping(value="/ObjectImpactDiagnostics/Popup/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteObjectImpactDiagnostics(@ModelAttribute("sqlPerfImpluenceAnalysis") SqlPerfImpluenceAnalysis sqlPerfImpluenceAnalysis,  Model model) {
		Result result = new Result();
		
		try{
			sqlInfluenceDiagnosticsService.deleteObjectImpactDiagnostics(sqlPerfImpluenceAnalysis);
			result.setResult(true);
			result.setMessage("오브젝트변경 SQL영향도 진단<br/>스케쥴 삭제에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 오브젝트변경 SQL영향도 진단 - 테이블 내역 */
	@RequestMapping(value="/ObjectImpactDiagnostics/Table")
	public String ObjectImpactDiagnosticsTable(@ModelAttribute("sqlPerfImplAnalTable") SqlPerfImplAnalTable sqlPerfImplAnalTable, Model model) {
		model.addAttribute("call_from_parent", sqlPerfImplAnalTable.getCall_from_parent());
		model.addAttribute("call_from_child", sqlPerfImplAnalTable.getCall_from_child());
		model.addAttribute("menu_id", sqlPerfImplAnalTable.getMenu_id());
		model.addAttribute("menu_nm", sqlPerfImplAnalTable.getMenu_nm());
		return "sqlInfluenceDiagnostics/objectImpactDiagnosticsTable";
	}
	
	/* 오브젝트변경 SQL영향도 진단 - 테이블 내역 Action */
	@RequestMapping(value="/ObjectImpactDiagnostics/TableAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ObjectImpactDiagnosticsTableAction(@ModelAttribute("sqlPerfImplAnalTable") SqlPerfImplAnalTable sqlPerfImplAnalTable,  Model model) {
		List<SqlPerfImplAnalTable> resultList = new ArrayList<SqlPerfImplAnalTable>();

		try{
			resultList = sqlInfluenceDiagnosticsService.objectImpactDiagnosticsTableList(sqlPerfImplAnalTable);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 오브젝트변경 SQL영향도 진단 - 테이블 - SQL상세내역 */
	@RequestMapping(value="/ObjectImpactDiagnostics/TableDetail")
	public String ObjectImpactDiagnosticsTableDetail(@ModelAttribute("sqlPerfImplAnalSql") SqlPerfImplAnalSql sqlPerfImplAnalSql, Model model) {
		model.addAttribute("menu_id", sqlPerfImplAnalSql.getMenu_id());
		model.addAttribute("menu_nm", sqlPerfImplAnalSql.getMenu_nm());
		return "sqlInfluenceDiagnostics/objectImpactDiagnosticsTableDetail";
	}
	
	/* 오브젝트변경 SQL영향도 진단 - 테이블 - SQL상세내역 Action */
	@RequestMapping(value="/ObjectImpactDiagnostics/TableDetailAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ObjectImpactDiagnosticsTableDetailAction(@ModelAttribute("sqlPerfImplAnalSql") SqlPerfImplAnalSql sqlPerfImplAnalSql, Model model) {
		List<SqlPerfImplAnalSql> resultList = new ArrayList<SqlPerfImplAnalSql>();

		try{
			resultList = sqlInfluenceDiagnosticsService.objectImpactDiagnosticsTableDetailList(sqlPerfImplAnalSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 오브젝트변경 SQL영향도 진단 - 테이블 - SQL상세내역 - SQL PROFLE UPDATE */
	@RequestMapping(value="/ObjectImpactDiagnostics/UpdateSQLProfile", method=RequestMethod.POST)
	@ResponseBody
	public Result UpdateSQLProfile(@ModelAttribute("sqlPerfImplAnalSql") SqlPerfImplAnalSql sqlPerfImplAnalSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;

		try{
			rowCnt = sqlInfluenceDiagnosticsService.updateSQLProfile_Object(sqlPerfImplAnalSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("[오브젝트변경 SQL영향도 진단]<br/>SQL PROFILE UPDATE에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
}