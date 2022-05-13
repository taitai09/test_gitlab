package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.AccPathExec;
import omc.spop.model.DbioExplainExec;
import omc.spop.model.DbioLoadFile;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.ProjectUnitLoadFile;
import omc.spop.model.Result;
import omc.spop.model.VsqlGatheringModule;
import omc.spop.model.VsqlParsingSchema;
import omc.spop.model.VsqlSnapshot;
import omc.spop.service.CommonService;
import omc.spop.service.IndexDesignPreProcessingService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2017.09.27	이원식	최초작성
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class IndexDesignPreProcessingController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexDesignPreProcessingController.class);
	
	@Autowired
	private IndexDesignPreProcessingService indexDesignPreProcessingService;
	
	@Autowired
	private CommonService commonService;
	
	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	

	/* 수집SQL 조건설정 */
	@RequestMapping(value="/SetCollectionCondition", method=RequestMethod.GET)
	public String AccessPathSetting(@ModelAttribute("vsqlParsingSchema") VsqlParsingSchema vsqlParsingSchema, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("startDate", startDate );
		model.addAttribute("menu_id", vsqlParsingSchema.getMenu_id());
		model.addAttribute("menu_nm", vsqlParsingSchema.getMenu_nm());
		
		return "indexDesignPreProcessing/setCollectionCondition";
	}
	
	/* 수집SQL 조건설정 - 수집대상 테이블 조회 */
	@RequestMapping(value="/SetCollectionCondition/CollectionTarget", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String CollectionTargetAction(@ModelAttribute("vsqlParsingSchema") VsqlParsingSchema vsqlParsingSchema,  Model model) {
		List<VsqlParsingSchema> resultList = new ArrayList<VsqlParsingSchema>();

		try{
			resultList = indexDesignPreProcessingService.collectionTargetList(vsqlParsingSchema);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 수집SQL 조건설정 - 적용대상 테이블 조회 */
	@RequestMapping(value="/SetCollectionCondition/ApplyTarget", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ApplyTargetAction(@ModelAttribute("vsqlParsingSchema") VsqlParsingSchema vsqlParsingSchema,  Model model) {
		List<VsqlParsingSchema> resultList = new ArrayList<VsqlParsingSchema>();

		try{
			resultList = indexDesignPreProcessingService.applyTargetList(vsqlParsingSchema);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 수집SQL 조건설정 - GV$ 정보 조회 */
	@RequestMapping(value="/SetCollectionCondition/GlobalViewInfo", method=RequestMethod.POST)
	@ResponseBody
	public Result GlobalViewInfoAction(@ModelAttribute("vsqlParsingSchema") VsqlParsingSchema vsqlParsingSchema,  Model model) {
		Result result = new Result();
		VsqlParsingSchema resultModel = new VsqlParsingSchema();
		try{
			resultModel = indexDesignPreProcessingService.glovalViewInfo(vsqlParsingSchema);
			result.setResult(true);
			result.setObject(resultModel);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 수집SQL 조건설정 - 수집모듈 리스트 조회 */
	@RequestMapping(value="/SetCollectionCondition/CollectionModule", method=RequestMethod.POST)
	@ResponseBody
	public Result CollectionModuleAction(@ModelAttribute("vsqlGatheringModule") VsqlGatheringModule vsqlGatheringModule,  Model model) {
		Result result = new Result();
		List<VsqlGatheringModule> resultList = new ArrayList<VsqlGatheringModule>();
		try{
			resultList = indexDesignPreProcessingService.collectionModuleList(vsqlGatheringModule);
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
	
	/* 수집SQL 조건설정 save */
	@RequestMapping(value="/SetCollectionCondition/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveSetCollectionCondition(@ModelAttribute("vsqlParsingSchema") VsqlParsingSchema vsqlParsingSchema,  Model model) {
		Result result = new Result();
		try{
			indexDesignPreProcessingService.saveSetCollectionCondition(vsqlParsingSchema);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 수집 SQL 조건절 파싱  */
	@RequestMapping(value="/ParsingCollectionTerms", method=RequestMethod.GET)
	public String ParsingCollectionTerms(@ModelAttribute("accPathExec") AccPathExec accPathExec, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		model.addAttribute("startDate", startDate );
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", accPathExec.getMenu_id() );
		model.addAttribute("menu_nm", accPathExec.getMenu_nm());
		
		return "indexDesignPreProcessing/parsingCollectionTerms";
	}
	
	/* 수집 SQL 조건절 파싱 action */
	@RequestMapping(value="/ParsingCollectionTerms", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ParsingCollectionTermsAction(@ModelAttribute("accPathExec") AccPathExec accPathExec,  Model model) {
		List<AccPathExec> resultList = new ArrayList<AccPathExec>();

		try{
			resultList = indexDesignPreProcessingService.parsingCollectionTermsList(accPathExec);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 수집 SQL 조건절 파싱  SNAP ID 조회 팝업 */
	@RequestMapping(value="/ParsingCollectionTerms/Popup/SnapShot", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ParsingCollectionTermsSnapList(@ModelAttribute("vsqlSnapshot") VsqlSnapshot vsqlSnapshot,  Model model) {
		List<VsqlSnapshot> resultList = new ArrayList<VsqlSnapshot>();
		
		try{
			resultList = indexDesignPreProcessingService.snapShotList(vsqlSnapshot);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 수집 SQL 조건절 파싱 Insert[CALL SP_SPOP_ACC_PATH_PARSING_ITG] */
	@RequestMapping(value="/ParsingCollectionTerms/Insert", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertParsingCollectionTerms(@ModelAttribute("vsqlSnapshot") VsqlSnapshot vsqlSnapshot,  Model model) {
		Result result = new Result();

		try{
			indexDesignPreProcessingService.insertParsingCollectionTerms(vsqlSnapshot);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}		
	
	/* SQL 적재  */
	@RequestMapping(value="/LoadSQL", method=RequestMethod.GET)
	public String LoadSQL(@ModelAttribute("dbioLoadFile") DbioLoadFile dbioLoadFile,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", dbioLoadFile.getMenu_id());
		model.addAttribute("menu_nm", dbioLoadFile.getMenu_nm());
		
		return "indexDesignPreProcessing/loadSQL";
	}
	
	/* SQL 적재 action */
	@RequestMapping(value="/LoadSQL", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String LoadSQLAction(@ModelAttribute("dbioLoadFile") DbioLoadFile dbioLoadFile,  Model model) {
		List<DbioLoadFile> resultList = new ArrayList<DbioLoadFile>();
		
		try{
			resultList = indexDesignPreProcessingService.loadSQLList(dbioLoadFile);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* SQL UPLOAD */
	@RequestMapping(value = "/LoadSQLFile", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result LoadSQLFile(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("dbioLoadFile") DbioLoadFile dbioLoadFile, Model model) {
		Result result = new Result();
		logger.debug("maxUploadSize:"+maxUploadSize);
		String file_name = "";

		try {				
			if (!file.isEmpty()) {
				if(file.getSize() > maxUploadSize){
			    	result.setResult(false);
			    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
			    }else{
		        	
			    	if(file.getOriginalFilename().indexOf(".") != -1){
		        		file_name = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
		        		List<String> noneFileList = new ArrayList<String>();
		        		noneFileList = commonService.getNoneFileList();
		        		if(noneFileList.contains(file_name)){
		        			result.setResult(false);
		        			result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
		        			return result;
		        		}
		        	}
						indexDesignPreProcessingService.loadSQLFile(file, dbioLoadFile);
						result.setResult(true);
						result.setMessage("SQL 적재가 완료되었습니다.");
			    }
		    } 
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		
		
		return result;		
	}	
	
	/* SQL Project Unit UPLOAD */
	@RequestMapping(value = "/ProjectUnitLoadSQLFile", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result ProjectUnitLoadSQLFile(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("projectUnitLoadFile") ProjectUnitLoadFile projectUnitLoadFile, Model model) {
		Result result = new Result();
		logger.debug("maxUploadSize:"+maxUploadSize);
		
		if (!file.isEmpty()) {
			logger.debug("file.getSize[" + file.getSize() + "]");
			if(file.getSize() > maxUploadSize){
				result.setResult(false);
				result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
			}else{
				try {
					int updateCount = indexDesignPreProcessingService.projectUnitLoadSQLFile(file, projectUnitLoadFile);
					result.setResult(true);
					result.setMessage("SQL 적재가 완료되었습니다.<br>적재된 SQL 수 : [" + updateCount + "]");
				}catch (Exception ex) {
					ex.printStackTrace();
					result.setResult(false);
					result.setMessage(ex.getMessage());
				}
		    }
		}
		
		return result;
	}
	
	/* 적재SQL 실행계획생성  */
	@RequestMapping(value="/LoadActionPlan")
	public String LoadActionPlan(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", dbioLoadSql.getMenu_id() );
		model.addAttribute("menu_nm", dbioLoadSql.getMenu_nm() );
		
		return "indexDesignPreProcessing/loadActionPlan";
	}
	
	/* 적재SQL 실행계획생성 action */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/LoadActionPlanAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String LoadActionPlanAction(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql,  Model model) {
		List<DbioLoadSql> resultList = new ArrayList<DbioLoadSql>();
		int dataCount4NextBtn = 0;
		try{
			logger.debug("plan_yn===>"+dbioLoadSql.getPlan_yn());
			resultList = indexDesignPreProcessingService.loadActionPlanList(dbioLoadSql);
			if(resultList != null && resultList.size() > dbioLoadSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(dbioLoadSql.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	/* 적재SQL 실행계획생성 - SQL_TEXT 조회 */
	@RequestMapping(value="/LoadActionPlan/ActionPlanSQLInfo", method=RequestMethod.POST)
	@ResponseBody
	public Result ActionPlanSQLInfo(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql,  Model model) {
		Result result = new Result();
		DbioLoadSql resultDbioLoadSql = new DbioLoadSql();
		
		try{
			resultDbioLoadSql = indexDesignPreProcessingService.actionPlanInfo(dbioLoadSql);
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
	
	/* 적재SQL 실행계획생성 - MAX ExplainExecSeq 조회 */
	@RequestMapping(value="/LoadActionPlan/MaxExplainExecSeq", method=RequestMethod.POST)
	@ResponseBody
	public Result MaxExplainExecSeqAction(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql,  Model model) {
		Result result = new Result();
		String explainExecSeq = "";
		try{
			explainExecSeq = indexDesignPreProcessingService.getMaxExplainExecSeq(dbioLoadSql);
			result.setResult(true);
			result.setTxtValue(explainExecSeq);
			result.setMessage("");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	@RequestMapping(value="/LoadActionPlan/isTaskLoadActionPlan", method=RequestMethod.POST)
	@ResponseBody
	public String isTaskLoadActionPlan(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql, Model model) {
		List<DbioLoadSql> resultList = new ArrayList<DbioLoadSql>();
		
		try {
			resultList = indexDesignPreProcessingService.isTaskLoadActionPlan(dbioLoadSql);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 적재SQL 실행계획생성 INSERT action */
	@RequestMapping(value="/LoadActionPlan/Insert", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertLoadActionPlanAction(@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql,  Model model) {
		Result result = new Result();
		int returnFlag = 0;
		try{
			returnFlag = indexDesignPreProcessingService.insertLoadActionPlan(dbioLoadSql);
			result.setResult(returnFlag > 0 ? true : false);
			result.setTxtValue(returnFlag > 0 ? "Y" : "N");
			result.setMessage("실행계획생성을 완료하였습니다.");
		}catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			String errMsg = StringUtils.defaultString(ex.getMessage(),"실행계획생성중 오류가 발생하였습니다.");
			result.setResult(false);
			result.setMessage(errMsg);
		}
		
		logger.debug("resultList:"+result);

		return result;	
	}
	
	@RequestMapping(value="/LoadActionPlan/planExecCnt", method=RequestMethod.POST)
	@ResponseBody
	public Result planExecCnt(@ModelAttribute("dbioExplainExec") DbioExplainExec dbioExplainExec, Model model) {
		Result result = new Result();
		DbioExplainExec resultDbioExplainExec = new DbioExplainExec();
		
		try {
			resultDbioExplainExec = indexDesignPreProcessingService.planExecCnt(dbioExplainExec);
			result.setResult(true);
			result.setObject(resultDbioExplainExec);
			result.setMessage("");
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 적재SQL 실행계획생성 - INSERT 로그 조회 */
	@RequestMapping(value="/LoadActionPlan/SelectActionPlanLog", method=RequestMethod.POST)
	@ResponseBody
	public Result SelectActionPlanLogAction(@ModelAttribute("dbioExplainExec") DbioExplainExec dbioExplainExec,  Model model) {
		Result result = new Result();
		DbioExplainExec resultDbioExplainExec = new DbioExplainExec();
		try{
			resultDbioExplainExec = indexDesignPreProcessingService.selectActionPlanLog(dbioExplainExec);
			result.setResult(true);
			result.setObject(resultDbioExplainExec);			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/**
	 * 선택한 DB를 대상으로 DBIO_EXPLAIN_EXEC(DBIO_SQL 실행계획수행내역) 테이블의 EXEC_END_DT(작업완료일시) 컬럼을 현재 시간으로 강제 업데이트한다.
	 * @param dbioLoadSql
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/LoadActionPlan/updateForceComplete", method=RequestMethod.POST)
	@ResponseBody
	public Result updateForceComplete(@ModelAttribute("dbioExplainExec") DbioExplainExec dbioExplainExec, Model model) {
		Result result = new Result();
		int updateCnt = -1;
		
		try {
			updateCnt = indexDesignPreProcessingService.updateForceComplete(dbioExplainExec);
			
			if(updateCnt > 0){
				result.setResult(true);
				result.setMessage("미완료된 실행계획생성 강제작업을 완료하였습니다");
				result.setResultCount(updateCnt);
			}else{
				result.setResult(false);
				result.setMessage("미완료된 실행계획생성 강제작업이 실패하였습니다");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 적재SQL 조건절 파싱  */
	@RequestMapping(value="/ParseLoadingCondition", method=RequestMethod.GET)
	public String ParseLoadingCondition(@ModelAttribute("dbioLoadFile") DbioLoadFile dbioLoadFile, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", dbioLoadFile.getMenu_id() );
		model.addAttribute("menu_nm", dbioLoadFile.getMenu_nm() );
		
		return "indexDesignPreProcessing/parseLoadingCondition";
	}	
	
	/* 적재SQL 조건절 파싱 - ExplainList action */
	@RequestMapping(value="/ParseLoadingCondition/ExplainList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ExplainListAction(@ModelAttribute("dbioLoadFile") DbioLoadFile dbioLoadFile,  Model model) {
		List<DbioLoadFile> resultList = new ArrayList<DbioLoadFile>();

		try{
			resultList = indexDesignPreProcessingService.explainList(dbioLoadFile);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 적재SQL 조건절 파싱 - AccessPathList action */
	@RequestMapping(value="/ParseLoadingCondition/AccessPathList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String AccessPathListAction(@ModelAttribute("accPathExec") AccPathExec accPathExec,  Model model) {
		List<AccPathExec> resultList = new ArrayList<AccPathExec>();

		try{
			resultList = indexDesignPreProcessingService.accessPathList(accPathExec);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	
	/* 적재SQL 조건절 파싱 Insert[CALL SP_SPOP_ACC_PATH_PARSING_ITG] */
	@RequestMapping(value="/ParseLoadingCondition/Insert", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertParseLoadingCondition(@ModelAttribute("accPathExec") AccPathExec accPathExec,  Model model) {
		Result result = new Result();
		try{
			indexDesignPreProcessingService.insertParseLoadingCondition(accPathExec);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}		
	
	@RequestMapping(value = "/loadActionPlan/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public void loadActionPlanExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("dbioLoadSql") DbioLoadSql dbioLoadSql, Model model) throws Exception {
		
		try {
			model.addAttribute("fileName", "적재SQL_실행_계획_생성");
			
			dbioLoadSql.setStrGubun("EXCEL");
			indexDesignPreProcessingService.downloadLargeExcel(dbioLoadSql, model, req, res);
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
	}
}