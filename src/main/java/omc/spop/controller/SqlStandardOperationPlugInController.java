package omc.spop.controller;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.model.Database;
import omc.spop.model.ProjectSqlQtyChkIdx;
import omc.spop.model.ProjectSqlQtyChkRule;
import omc.spop.model.Result;
import omc.spop.model.SqlStandardOperationPlugIn;
import omc.spop.model.SqlStandardOperationPlugInResponse;
import omc.spop.service.ProjectSqlQtyChkIdxMngService;
import omc.spop.service.SqlStandardOperationPlugInService;

/***********************************************************
 * 2019.06.11	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Controller
@RequestMapping(value = "/ssopi")
public class SqlStandardOperationPlugInController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(SqlStandardOperationPlugInController.class);
	
	@Autowired
	private SqlStandardOperationPlugInService sqlStandardOperationPlugInService;
	
	@Autowired
	private ProjectSqlQtyChkIdxMngService projectSqlQtyChkIdxMngService;
	
	@Value("#{defaultConfig['sql_cnt_list']}")
	private String sqlCntList;
	
	private final String ERROR_TRACE = "Error Trace :";
	
	/* MAX(SQL_STD_QTY_CHKT_ID) action */
	@RequestMapping(value = "/getMaxSqlStdQtyChktId", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getMaxSqlStdQtyChktId(@ModelAttribute("sqlStandardOperationPlugIn") SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception {
		List<SqlStandardOperationPlugIn> resultList = new ArrayList<SqlStandardOperationPlugIn>();
		
		try {
			resultList = sqlStandardOperationPlugInService.getMaxSqlStdQtyChktId();
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* DBID action */
	@RequestMapping(value = "/findDbid", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String findDbid(@ModelAttribute("sqlStandardOperationPlugIn") SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception {
		List<Database> resultList = new ArrayList<Database>();
		
		try {
			resultList = sqlStandardOperationPlugInService.findDbid(sqlStandardOperationPlugIn);
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/deleteTableList", method = RequestMethod.POST, produces = "application/json; utf8")
	public @ResponseBody Result deleteTableList(@RequestBody SqlStandardOperationPlugIn model, HttpServletRequest request, HttpServletResponse response) throws SQLSyntaxErrorException, Exception {
		Result result = new Result();
		boolean flag = false;
		
		try {
			flag = sqlStandardOperationPlugInService.deleteTableList(model);
			
			if(flag == true) {
				result.setResult(true);
				result.setMessage("Success deleteTableList");
			} else {
				result.setResult(false);
				result.setMessage("Failed deleteTableList");
			}
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/insertSelfsqlStdQtyPlanExec", method = RequestMethod.POST, produces = "application/json; utf8")
	public @ResponseBody Result insertSelfsqlStdQtyPlanExec(@RequestBody SqlStandardOperationPlugIn model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Result result = new Result();
		boolean flag = false;
		
		try {
			flag = sqlStandardOperationPlugInService.insertSelfsqlStdQtyPlanExec(model);
			
			if(flag == true) {
				result.setResult(true);
				result.setMessage("Success insertSelfsqlStdQtyPlanExec");
			} else {
				result.setResult(false);
				result.setMessage("Failed insertSelfsqlStdQtyPlanExec");
			}
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/getCountCreatePlan", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCountCreatePlan(@ModelAttribute("projectSqlQtyChkRule") ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception {
		List<ProjectSqlQtyChkRule> resultList = new ArrayList<ProjectSqlQtyChkRule>();
		
		try {
			resultList = sqlStandardOperationPlugInService.getCountCreatePlan(projectSqlQtyChkRule);
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/getSqlCntList", method = RequestMethod.GET, produces = "application/text; utf-8")
	@ResponseBody
	public String getSqlCntList(@ModelAttribute("sqlStandardOperationPlugIn") SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception {
		List<SqlStandardOperationPlugIn> resultList = new ArrayList<SqlStandardOperationPlugIn>();
		SqlStandardOperationPlugIn model = new SqlStandardOperationPlugIn();
		int sql_cnt_list = 0;
		
		try {
			if(sqlCntList == null) {
				logger.debug("getSqlCntList >> sqlCntList is null. set default sql_cnt_list[50]");
				sql_cnt_list = 50;
			} else {
				logger.debug("getSqlCntList >> sql_cnt_list[" + sqlCntList + "]");
				sql_cnt_list = Integer.parseInt(sqlCntList);
			}
			model.setSql_cnt_list(sql_cnt_list);
			resultList.add(model);
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/getWrkJobCdList", method = RequestMethod.GET, produces = "application/text; utf-8")
	@ResponseBody
	public String getWrkJobCdList(@ModelAttribute("sqlStandardOperationPlugIn") SqlStandardOperationPlugIn sqlStandardOperationPlugIn) throws Exception {
		List<SqlStandardOperationPlugIn> resultList = new ArrayList<SqlStandardOperationPlugIn>();
		HashMap<String, String> wrkJobCdMap = new HashMap<String, String>();
		SqlStandardOperationPlugIn model = new SqlStandardOperationPlugIn();
		int sql_cnt_list = 0;
		
		try {
			wrkJobCdMap = sqlStandardOperationPlugInService.getWrkJobCdList();
			
			if(wrkJobCdMap == null) {
				logger.debug("getWrkJobCdList >> wrkJobCdMap is null.");
			}
			
			model.setWrkJobCdMap(wrkJobCdMap);
			resultList.add(model);
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	@RequestMapping(value = "/receiveBox", method = RequestMethod.POST, produces = "application/json; utf-8")
	public @ResponseBody Result receiveBox(@RequestBody SqlStandardOperationPlugIn sqlStandardOperationPlugIn, HttpServletRequest request, HttpServletResponse response) {
		long beforeTime = System.currentTimeMillis();
		Result result = new Result();
		
		try {
			result = sqlStandardOperationPlugInService.receiveBoxBatch(sqlStandardOperationPlugIn);
		} catch(SQLSyntaxErrorException e) {
			logger.error(ERROR_TRACE, e);
			result.setResult(false);
			result.setMessage(e.getMessage());
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		long afterTime = System.currentTimeMillis();
		double secDiffTime = (afterTime - beforeTime) / 1000.0;
		logger.debug("receiveBox controller [" + secDiffTime + "]");
		return result;
	}
	
	/* Master 통보 */
	@RequestMapping(value = "/createPlan", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Result createPlan(@RequestBody SqlStandardOperationPlugIn sqlStandardOperationPlugIn, HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();
		
		try {
			logger.debug("createPlan model:\n" + sqlStandardOperationPlugIn);
			sqlStandardOperationPlugInService.createPlan(sqlStandardOperationPlugIn);
			
			result.setResult(true);
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* Plan 생성이 완료되었는지 Polling으로 체크한다. */
	@RequestMapping(value = "/getNonComplianceData", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public @ResponseBody String getNonComplianceData(@RequestBody SqlStandardOperationPlugIn sqlStandardOperationPlugIn, HttpServletRequest request, HttpServletResponse response) {
		List<SqlStandardOperationPlugInResponse> resultList = new ArrayList<SqlStandardOperationPlugInResponse>();
		
		try {
			resultList = sqlStandardOperationPlugInService.getNonComplianceDataResponse(sqlStandardOperationPlugIn);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* SQL 표준 품질점검 지표 관리 정보를 보여준다. */
	@RequestMapping(value = "/getProjectSqlQtyChkIdxList", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	public @ResponseBody String getProjectSqlQtyChkIdxList(@RequestBody SqlStandardOperationPlugIn sqlStandardOperationPlugIn, HttpServletRequest request, HttpServletResponse response) {
		ProjectSqlQtyChkIdx projectSqlQtyChkIdx = new ProjectSqlQtyChkIdx();
		List<ProjectSqlQtyChkIdx> resultList = new ArrayList<ProjectSqlQtyChkIdx>();
		
		try {
			projectSqlQtyChkIdx.setProject_id(sqlStandardOperationPlugIn.getProject_id());
			resultList = projectSqlQtyChkIdxMngService.getProjectSqlQtyChkIdxList(projectSqlQtyChkIdx);
		} catch (Exception ex) {
			logger.error(ERROR_TRACE, ex);
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		
		return jobj.toString();
	}
	
	@RequestMapping(value = "/executeSql", method = RequestMethod.POST, produces = "application/json; utf-8")
	public @ResponseBody Result executeSql(@RequestBody SqlStandardOperationPlugIn sqlStandardOperationPlugIn, HttpServletRequest request, HttpServletResponse response) {
		long beforeTime = System.currentTimeMillis();
		Result result = new Result();
		
		try {
			result = sqlStandardOperationPlugInService.executeSql(sqlStandardOperationPlugIn);
		} catch(SQLSyntaxErrorException e) {
			logger.error(ERROR_TRACE, e);
			result.setResult(false);
			result.setMessage(e.getMessage());
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		long afterTime = System.currentTimeMillis();
		double secDiffTime = (afterTime - beforeTime) / 1000.0;
		logger.debug("sendExecuteSql controller [" + secDiffTime + "]");
		return result;
	}
	
	
	@RequestMapping(value = "/updateSelfsqlStdQtyPlanExecForceComplete", method = RequestMethod.POST, produces = "application/json; utf8")
	public @ResponseBody Result updateSelfsqlStdQtyPlanExecForceComplete(@RequestBody SqlStandardOperationPlugIn model, HttpServletRequest request, HttpServletResponse response) throws SQLSyntaxErrorException, Exception {
		Result result = new Result();
		boolean flag = false;
		
		try {
			flag = sqlStandardOperationPlugInService.updateSelfsqlStdQtyPlanExecForceComplete(model);
			
			if(flag == true) {
				result.setResult(true);
				result.setMessage("Success updateSelfsqlStdQtyPlanExecForceComplete");
			} else {
				result.setResult(false);
				result.setMessage("Failed updateSelfsqlStdQtyPlanExecForceComplete");
			}
		} catch(Exception ex) {
			logger.error(ERROR_TRACE, ex);
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
}
