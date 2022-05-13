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

import net.sf.json.JSONArray;
import omc.spop.base.InterfaceController;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.DbioPlanTable;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.OdsHistSqlstat;
import omc.spop.model.Result;
import omc.spop.model.SqlGrid;
import omc.spop.service.SQLInformationService;
import omc.spop.utils.TreeWrite;

/***********************************************************
 * 2018.04.10	이원식	OPENPOP V2 최초작업 (SQL 정보 탭구성 호출)
 * 2021.02.04	이재우	기능개선
 **********************************************************/

@Controller
@RequestMapping(value = "/SQLInformation")
public class SQLInformationController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLInformationController.class);
	
	@Autowired
	private SQLInformationService sqlInformationService;
	
	/* SQL 정보 */
	@RequestMapping(value="")
	public String Info(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model) {
		
		if(StringUtils.defaultString(odsHistSqlText.getIsvsql()).equals("Y")){
			return "sqlInformationVSQLAll";
		}
		return "sqlInformation";
	}	

	
	/* SQL 정보 - SQL TEXT */
	@RequestMapping(value="/SQLText", method=RequestMethod.POST)
	@ResponseBody
	public Result SQLTextAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText,  Model model) {
		Result result = new Result();

		try{
			result.setResult(true);
			result.setObject(sqlInformationService.sqlText(odsHistSqlText));
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* SQL 정보 - SQL TEXT */
	@RequestMapping(value="/SQLTextAll", method=RequestMethod.POST)
	@ResponseBody
	public Result SQLTextAllAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText,  Model model) {
		Result result = new Result();
		
		try{
			result.setResult(true);
			result.setObject(sqlInformationService.sqlTextAll(odsHistSqlText));
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* SQL 정보 - SQL Tree */
	@RequestMapping(value="/TreePlan", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TreePlanAction(@ModelAttribute("sqlGrid") SqlGrid sqlGrid,  Model model) {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		try{
			resultList = sqlInformationService.sqlTreePlanList(sqlGrid);
			List<SqlGrid> buildList = TreeWrite.buildSQLTree(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		logger.error("returnValue ->" + returnValue);

		return returnValue;	
	}	

	/* SQL 정보 - SQL Tree */
	@RequestMapping(value="/TreePlanAll", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String TreePlanAllAction(@ModelAttribute("sqlGrid") SqlGrid sqlGrid,  Model model) {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		try{
			resultList = sqlInformationService.sqlTreePlanListAll(sqlGrid);
			List<SqlGrid> buildList = TreeWrite.buildSQLTree(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
			
			returnValue = jsonArray.toString();
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		logger.error("returnValue ->" + returnValue);
		
		return returnValue;	
	}	
	
	/* SQL 정보 - Text Plan */
	@RequestMapping(value="/TextPlan", method=RequestMethod.POST)
	@ResponseBody
	public Result TextPlanAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText,  Model model) {
		Result result = new Result();
		
		try{
			result.setResult(true);
			result.setObject(sqlInformationService.sqlTextPlanList(odsHistSqlText));
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}	
	
	/* SQL 정보 - Text Plan */
	@RequestMapping(value="/TextPlanAll", method=RequestMethod.POST)
	@ResponseBody
	public Result TextPlanAllAction(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText,  Model model) {
		Result result = new Result();

		try{
			result.setResult(true);
			result.setObject(sqlInformationService.sqlTextPlanListAll(odsHistSqlText));
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* SQL 정보 - SQL Grid Plan */
	@RequestMapping(value="/GridPlan", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GridPlanAction(@ModelAttribute("sqlGrid") SqlGrid sqlGrid,  Model model) {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		try{
			resultList = sqlInformationService.sqlGridPlanList(sqlGrid);
			List<SqlGrid> buildList = TreeWrite.buildSqlGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		return returnValue;	
	}	
	/* SQL 정보 - SQL Grid Plan */
	@RequestMapping(value="/GridPlanAll", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GridPlanAllAction(@ModelAttribute("sqlGrid") SqlGrid sqlGrid,  Model model) {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		try{
			resultList = sqlInformationService.sqlGridPlanListAll(sqlGrid);
			List<SqlGrid> buildList = TreeWrite.buildSqlGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
			
			returnValue = jsonArray.toString();
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		return returnValue;	
	}	
	
	/* SQL 정보 - Bind Value */
	@RequestMapping(value="/BindValue", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String BindValueAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.bindValueList(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 정보  - Bind Value Next action */
	@RequestMapping(value="/BindValueNext", method=RequestMethod.POST)
	@ResponseBody
	public Result BindValueListNextAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		Result result = new Result();
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();

		try{
			resultList = sqlInformationService.bindValueList(odsHistSqlstat);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* SQL 정보 - Bind Value All*/
	@RequestMapping(value="/BindValueAll", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String BindValueAllAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.bindValueListAll(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 정보 - Bind Value All*/
	@RequestMapping(value="/BindValueNewList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String BindValueNewList(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.bindValueNewList(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 정보  - Bind Value Next action All */
	@RequestMapping(value="/BindValueNextAll", method=RequestMethod.POST)
	@ResponseBody
	public Result BindValueListNextAllAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		Result result = new Result();
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.bindValueListAll(odsHistSqlstat);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* SQL 정보 - Out Line */
	@RequestMapping(value="/OutLine", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String OutLineAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.outLineList(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	

	/* SQL 정보 - Out Line */
	@RequestMapping(value="/OutLineAll", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String OutLineAllAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.outLineListAll(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}	
	
	/* SQL 정보 - 유사 SQL */
	@RequestMapping(value="/SimilaritySql", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SimilaritySqlAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.similaritySqlList(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}		
	/* SQL 정보 - 유사 SQL */
	@RequestMapping(value="/SimilaritySqlAll", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SimilaritySqlAllAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.similaritySqlListAll(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}		
	
	/* SQL 정보 - sql 수행이력 action */
	@RequestMapping(value="/SQLPerformHistory", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SQLPerformHistoryAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();

		try{
			resultList = sqlInformationService.sqlPerformHistoryList(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 정보 - VSQL 수행이력 action */
	@RequestMapping(value="/SQLPerformNewHistory", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SQLPerformNewHistory(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();
		
		try{
			resultList = sqlInformationService.sqlPerformNewHistoryList(odsHistSqlstat);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* SQL 정보  - sql 수행이력 Next action */
	@RequestMapping(value="/SQLPerformHistoryNext", method=RequestMethod.POST)
	@ResponseBody
	public Result SQLPerformHistoryNextAction(@ModelAttribute("odsHistSqlstat") OdsHistSqlstat odsHistSqlstat,  Model model) {
		Result result = new Result();
		List<OdsHistSqlstat> resultList = new ArrayList<OdsHistSqlstat>();

		try{
			resultList = sqlInformationService.sqlPerformHistoryList(odsHistSqlstat);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	/* 적재SQL 인덱스 설계 - SQL 정보 */
	@RequestMapping(value="/Load")
	public String LoadList(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql, Model model) {
		return "loadInformation";
	}
	
	
	/* 적재SQL 인덱스 설계 - SQL 정보 - sql text action */
	@RequestMapping(value="/Load/loadSqlText", method=RequestMethod.POST)
	@ResponseBody
	public Result SQLTextInfoAction(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql,  Model model) {
		Result result = new Result();
		DbioLoadSql resultDbioLoadSql = new DbioLoadSql();
		
		try{
			resultDbioLoadSql = sqlInformationService.loadSqlText(dbioLoadSql);
			result.setResult(true);
			result.setObject(resultDbioLoadSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 적재SQL 인덱스 설계 - SQL 정보 - Graphic list action */
	@RequestMapping(value="/Load/SQLGraphicList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SQLGraphicListAction(@ModelAttribute("dbioPlanTable") DbioPlanTable dbioPlanTable,  Model model) {
		List<SqlGrid> resultList = new ArrayList<SqlGrid>();
		String returnValue = "";
		
		try{
			resultList = sqlInformationService.sqlGraphicList(dbioPlanTable);
			List<SqlGrid> buildList = TreeWrite.buildSqlGrid(resultList, "-1");
			JSONArray jsonArray = JSONArray.fromObject(buildList);
            
			returnValue = jsonArray.toString();
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		return returnValue;	
	}
	
	/* 적재SQL 인덱스 설계 - SQL 정보 - Text list action */
	@RequestMapping(value="/Load/SQLTextList", method=RequestMethod.POST)
	@ResponseBody
	public Result SQLTextListAction(@ModelAttribute("dbioPlanTable") DbioPlanTable dbioPlanTable,  Model model) {
		Result result = new Result();
		List<DbioPlanTable> resultList = new ArrayList<DbioPlanTable>();
		
		try{
			resultList = sqlInformationService.sqlTextList(dbioPlanTable);
			result.setResult(true);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
}