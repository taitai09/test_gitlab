package omc.spop.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.AccPath;
import omc.spop.model.AccPathIndexDesign;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.IdxAdMst;
import omc.spop.model.OdsIndexs;
import omc.spop.model.OdsTabColumns;
import omc.spop.model.OdsTables;
import omc.spop.model.Result;
import omc.spop.model.VsqlText;
import omc.spop.service.IndexDesignMaintenanceService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.ExcelWrite;
import omc.spop.utils.WriteOption;

/***********************************************************
 * 2018.03.15	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class IndexDesignMaintenanceController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexDesignMaintenanceController.class);
	
	@Autowired
	private IndexDesignMaintenanceService indexDesignMaintenanceService;
	

	/* 수집 SQL 인덱스 설계 */
	@RequestMapping(value="/CollectionIndexDesign", method={RequestMethod.GET, RequestMethod.POST})
	public String CollectionIndexDesign(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		String nowDate = DateUtil.getNowDate("yyyyMMdd");
		String accessPathValue = "";
		try{
			if((odsTables.getDbid() != null && !odsTables.getDbid().equals("")) &&
				(odsTables.getExec_seq() != null && !odsTables.getExec_seq().equals(""))){
				accessPathValue = indexDesignMaintenanceService.getAccessPathExec(odsTables);
				odsTables.setAccess_path_value(accessPathValue);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsTables.getMenu_id() );
		model.addAttribute("menu_nm", odsTables.getMenu_nm() );
			
		return "indexDesignMaintenance/collectionIndexDesign";
	}
	
	/* 수집 SQL 인덱스 설계 - ods_table list action */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/CollectionIndexDesign/OdsTable", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String CollectionOdsTableAction(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();
		
		int dataCount4NextBtn = 0;
		try{
			resultList = indexDesignMaintenanceService.collectionOdsTableList(odsTables);
			if (resultList != null && resultList.size() > odsTables.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(odsTables.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* 수집 SQL 인덱스 설계 - Access path 현황 list action */
	@RequestMapping(value="/CollectionIndexDesign/AccessPath", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String CollectionAccessPath(@ModelAttribute("accPath") AccPath accPath,  Model model) {
		List<AccPath> resultList = new ArrayList<AccPath>();
		
		try{
			resultList = indexDesignMaintenanceService.collectionAccessPathList(accPath);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 수집 SQL 인덱스 설계 - sql 현황 list action */
	@RequestMapping(value="/CollectionIndexDesign/SQLStatus", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String CollectionSQLStatus(@ModelAttribute("vsqlText") VsqlText vsqlText,  Model model) {
		List<VsqlText> resultList = new ArrayList<VsqlText>();
		
		try{
			resultList = indexDesignMaintenanceService.collectionSqlStatusList(vsqlText);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	

	/* 수집 SQL 인덱스 정비  */
	@RequestMapping(value="/CollectionIndexUsage", method=RequestMethod.GET)
	public String CollectionIndexUsage(@ModelAttribute("odsIndexs") OdsIndexs odsIndexs, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		model.addAttribute("startDate", startDate );
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsIndexs.getMenu_id() );
		model.addAttribute("menu_nm", odsIndexs.getMenu_nm() );
		
		return "indexDesignMaintenance/collectionIndexUsage";
	}

	/* 수집 SQL 인덱스 정비  action */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/CollectionIndexUsage", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String CollectionIndexUsageAction(@ModelAttribute("odsIndexs") OdsIndexs odsIndexs,  Model model) {
		List<OdsIndexs> resultList = new ArrayList<OdsIndexs>();
		
		int dataCount4NextBtn = 0;
		try{
			resultList = indexDesignMaintenanceService.collectionIndexUsageList(odsIndexs);
			if (resultList != null && resultList.size() > odsIndexs.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(odsIndexs.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	
	/* 적재SQL 인덱스 설계  */
	@RequestMapping(value="/LoadIndexDesign", method={RequestMethod.GET, RequestMethod.POST})
	public String LoadIndexDesign(@ModelAttribute("odsTables") OdsTables odsTables, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String accessPathValue = "";
		try{
			if((odsTables.getDbid() != null && !odsTables.getDbid().equals("")) &&
				(odsTables.getExec_seq() != null && !odsTables.getExec_seq().equals(""))){
				accessPathValue = indexDesignMaintenanceService.getAccessPathExec(odsTables);
				odsTables.setAccess_path_value(accessPathValue);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		model.addAttribute("nowDate", nowDate );	
		model.addAttribute("menu_id", odsTables.getMenu_id() );
		model.addAttribute("menu_nm", odsTables.getMenu_nm() );
		
		return "indexDesignMaintenance/loadIndexDesign";
	}
	
	
	/* 적재SQL 인덱스 설계 - ods_table list action */
	@RequestMapping(value="/LoadIndexDesign/OdsTable", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String LoadOdsTableAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		List<OdsTables> resultList = new ArrayList<OdsTables>();
		int dataCount4NextBtn = 0;
		
		try{
			resultList = indexDesignMaintenanceService.loadOdsTableList(odsTables);
			if (resultList != null && resultList.size() > odsTables.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(odsTables.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* 적재SQL 인덱스 설계 - acc_path list action */
	@RequestMapping(value="/LoadIndexDesign/AccessPath", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String LoadAccessPathAction(@ModelAttribute("accPath") AccPath accPath,  Model model) {
		List<AccPath> resultList = new ArrayList<AccPath>();
		
		try{
			resultList = indexDesignMaintenanceService.loadAccessPathList(accPath);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 적재SQL 인덱스 설계 - dbio_text list action */
	@RequestMapping(value="/LoadIndexDesign/SQLStatus", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String LoadSQLStatusAction(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql,  Model model) {
		List<DbioLoadSql> resultList = new ArrayList<DbioLoadSql>();
		
		try{
			resultList = indexDesignMaintenanceService.loadSqlStatusList(dbioLoadSql);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 적재SQL 인덱스 정비 */
	@RequestMapping(value="/LoadIndexUsage", method=RequestMethod.GET)
	public String LoadUsingIndex(@ModelAttribute("odsIndexs") OdsIndexs odsIndexs,Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsIndexs.getMenu_id() );
		model.addAttribute("menu_nm", odsIndexs.getMenu_nm() );
		
		return "indexDesignMaintenance/loadIndexUsage";
	}
	
	/* 적재SQL 인덱스 정비 action */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/LoadIndexUsage", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String LoadUsingIndexAction(@ModelAttribute("odsIndexs") OdsIndexs odsIndexs,  Model model) {
		List<OdsIndexs> resultList = new ArrayList<OdsIndexs>();
		
		int dataCount4NextBtn = 0;
		try{
			resultList = indexDesignMaintenanceService.loadUsingIndexList(odsIndexs);
			if (resultList != null && resultList.size() > odsIndexs.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(odsIndexs.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/* 공통 사용 함수 */
	/* 컬럼정보 list action */
	@RequestMapping(value="/IndexDesignMaintenance/Columns", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String Columns(@ModelAttribute("odsTabColumns") OdsTabColumns odsTabColumns,  Model model) {
		List<OdsTabColumns> resultList = new ArrayList<OdsTabColumns>();
		
		try{
			resultList = indexDesignMaintenanceService.columnsList(odsTabColumns);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 인덱스 현황 action */
	@RequestMapping(value="/IndexDesignMaintenance/Indexs", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String Indexs(@ModelAttribute("odsIndexs") OdsIndexs odsIndexs,  Model model) {
		List<OdsIndexs> resultList = new ArrayList<OdsIndexs>();
		
		try{
			resultList = indexDesignMaintenanceService.indexsList(odsIndexs);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}

	/* 인덱스 현황 action */
	/* 개발서버 */
	@RequestMapping(value="/IndexDesignMaintenance/IndexList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String IndexList(@ModelAttribute("odsIndexs") OdsIndexs odsIndexs,  Model model) {
		List<OdsIndexs> resultList = new ArrayList<OdsIndexs>();
		
		try{
			resultList = indexDesignMaintenanceService.indexList(odsIndexs);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();	
	}
	
	/* acc_path_index_design list action */
	@RequestMapping(value="/IndexDesignMaintenance/AccPathIndexDesign", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AccPathIndexDesign(@ModelAttribute("accPathIndexDesign") AccPathIndexDesign accPathIndexDesign,  Model model) {
		List<AccPathIndexDesign> resultList = new ArrayList<AccPathIndexDesign>();
		
		try{
			resultList = indexDesignMaintenanceService.accPathIndexDesignList(accPathIndexDesign);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* insert index design action */
	@RequestMapping(value="/IndexDesignMaintenance/InsertIndexDesign", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertIndexDesignAction(@ModelAttribute("accPathIndexDesign") AccPathIndexDesign accPathIndexDesign,  Model model) {
		Result result = new Result();
		
		try{
			indexDesignMaintenanceService.insertIndexDesign(accPathIndexDesign);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	

	/* index auto desing action */
	@RequestMapping(value="/IndexDesignMaintenance/StartIndexAutoDesign", method=RequestMethod.POST)
	@ResponseBody
	public Result StartIndexAutoDesignAction(@ModelAttribute("odsTables") OdsTables odsTables,  Model model) {
		Result result = new Result();
		List<IdxAdMst> resultList = new ArrayList<IdxAdMst>();
		
		try{
			resultList = indexDesignMaintenanceService.startIndexAutoDesign(odsTables);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setTxtValue(success(resultList).toJSONObject().toString());
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 인덱스 설계 excel down action */
	@RequestMapping(value="/IndexDesignMaintenance/IndexExcelDown", method=RequestMethod.POST)
	public void LoadIndexExcelDown(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("accPathIndexDesign") AccPathIndexDesign accPathIndexDesign, Model model) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException {
		List<AccPathIndexDesign> resultList = new ArrayList<AccPathIndexDesign>();
		WriteOption wo = new WriteOption();
		
		wo.setFileName("인덱스설계");
		wo.setSheetName("인덱스설계");
	    wo.setTitle("IndexDesign");
		
		try{
			resultList = indexDesignMaintenanceService.indexDesignList(accPathIndexDesign);			
			wo.setIndexDesignContents(resultList);			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

	    ExcelWrite.write(res, wo);		
	}
	/**
	 * 성능개선 > 인덱스설계/정비 > 수집SQL인덱스설계 --> 테이블목록
	 * @param req
	 * @param res
	 * @param odsTables
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/IndexDesignMaintenance/ExcelDownTableList", method = { RequestMethod.GET, RequestMethod.POST })
	 // @ResponseBody
	 public ModelAndView TuningHistoryExcelDown(HttpServletRequest req, HttpServletResponse res,
	   @ModelAttribute("odsTables") OdsTables odsTables, Model model)
	   throws Exception {

	  List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

	  try {
			resultList = indexDesignMaintenanceService.collectionOdsTableList4Excel(odsTables);
	  } catch (Exception ex) {
	   String methodName = new Object() {
	   }.getClass().getEnclosingMethod().getName();
	   logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
	  }
	  model.addAttribute("fileName", "테이블목록");
	  model.addAttribute("sheetName", "테이블목록");
	  model.addAttribute("excelId", "TABLE_LIST");
	  // return a view which will be resolved by an excel view resolver
	  return new ModelAndView("xlsxView", "resultList", resultList);
	 }
	/**
	 * 성능개선 > 인덱스설계/정비 > 수집SQL인덱스정비 엑셀 다운
	 * @param req
	 * @param res
	 * @param odsTables
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/CollectionIndexUsage/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	 // @ResponseBody
	 public ModelAndView CollectionIndexUsageExcelDown(HttpServletRequest req, HttpServletResponse res,
	   @ModelAttribute("odsIndexs") OdsIndexs odsIndexs, Model model)
	   throws Exception {

	  List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

	  try {
			resultList = indexDesignMaintenanceService.collectionIndexUsageList4Excel(odsIndexs);
	  } catch (Exception ex) {
	   String methodName = new Object() {
	   }.getClass().getEnclosingMethod().getName();
	   logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
	  }
	  model.addAttribute("fileName", "수집_SQL_인덱스정비");
	  model.addAttribute("sheetName", "수집_SQL_인덱스정비");
	  model.addAttribute("excelId", "GATHER_SQL_IDX_REPAIR");
	  // return a view which will be resolved by an excel view resolver
	  return new ModelAndView("xlsxView", "resultList", resultList);
	 }
	/**
	 * 성능개선 > 인덱스설계/정비 >  적재SQL 인덱스 정비 엑셀 다운
	 * @param req
	 * @param res
	 * @param odsTables
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/LoadIndexUsage/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	 // @ResponseBody
	 public ModelAndView LoadIndexUsageExcelDown(HttpServletRequest req, HttpServletResponse res,
	   @ModelAttribute("odsIndexs") OdsIndexs odsIndexs, Model model)
	   throws Exception {

	  List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

	  try {
			resultList = indexDesignMaintenanceService.loadUsingIndexList4Excel(odsIndexs);
	  } catch (Exception ex) {
	   String methodName = new Object() {
	   }.getClass().getEnclosingMethod().getName();
	   logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
	  }
	  model.addAttribute("fileName", "적재SQL_인덱스정비");
	  model.addAttribute("sheetName", "적재SQL_인덱스정비");
	  model.addAttribute("excelId", "LOAD_SQL_IDX_REPAIR");
	  // return a view which will be resolved by an excel view resolver
	  return new ModelAndView("xlsxView", "resultList", resultList);
	 }
	
	/* 적재SQL 인덱스 설계 */
	@RequestMapping(value="/LoadSQLIndexDesign", method=RequestMethod.GET)
	public String LoadSQLIndexDesign(@ModelAttribute("odsIndexs") OdsIndexs odsIndexs,Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", odsIndexs.getMenu_id() );
		model.addAttribute("menu_nm", odsIndexs.getMenu_nm() );
		
		return "indexDesign/loadSQL/loadSQLIndexDesign";
	}
}