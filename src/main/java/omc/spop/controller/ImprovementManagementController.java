package omc.spop.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.Config;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Cd;
import omc.spop.model.DownLoadFile;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.Result;
import omc.spop.model.SqlImprovementType;
import omc.spop.model.SqlTuning;
import omc.spop.model.SqlTuningAttachFile;
import omc.spop.model.SqlTuningHistory;
import omc.spop.model.SqlTuningStatusHistory;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.CommonService;
import omc.spop.service.ImprovementManagementService;
import omc.spop.service.SQLAnalysisService;
import omc.spop.service.TuningHistoryService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 * 2020.06.02	이재우	기능개선
 **********************************************************/

@Controller
public class ImprovementManagementController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ImprovementManagementController.class);

	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	@Autowired
	private TuningHistoryService tuningHistoryService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImprovementManagementService improvementManagementService;
	
	@Autowired
	private SQLAnalysisService sqlAnalysisService;
	
	/* 튜닝요청  */
	@RequestMapping(value="/RequestImprovement", method=RequestMethod.GET)
	public String RequestImprovement(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		String wrkjob_cd = StringUtils.defaultString(SessionManager.getLoginSession().getUsers().getWrkjob_cd());
		String wrkjob_nm = SessionManager.getLoginSession().getUsers().getWrkjob_nm();
		String ext_no = SessionManager.getLoginSession().getUsers().getExt_no();
		OdsHistSqlText odsHistSqlText = new OdsHistSqlText();
		TuningTargetSql initValues = new TuningTargetSql();

		String dbid = StringUtils.defaultString(tuningTargetSql.getDbid());
		logger.debug("======================================="+dbid);
		String sql_wrkjob_cd = StringUtils.defaultString(tuningTargetSql.getWrkjob_cd());
		String sql_hash = StringUtils.defaultString(tuningTargetSql.getSql_hash());
		//kb카드 요구사항
		//튜닝완료요청일자 오늘 + 7
		String tuning_complete_due_day = StringUtils.defaultString(improvementManagementService.getTuningCompleteDueDay(),"0");
		logger.debug("tuning_complete_due_day :"+tuning_complete_due_day);
		int i_tuning_complete_due_day = Integer.parseInt(tuning_complete_due_day);
		String tuning_complete_due_dt = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, i_tuning_complete_due_day);
		tuningTargetSql.setTuning_complete_due_dt(tuning_complete_due_dt);

//		if(dbid.equals("") && !wrkjob_cd.equals("")){
//			//dbid가 null일 경우 blank로 치환
//			dbid = StringUtils.defaultString(improvementManagementService.getUsersWrkjobCdDbid(wrkjob_cd));
//			logger.debug("getUsersWrkjobCdDbid dbid ===>"+dbid);
//			tuningTargetSql.setDbid(dbid);
//		}
		
		// sql_id 가 존재할 경우 SQL_TEXT 조회
//		if(!dbid.equals("") && !sql_wrkjob_cd.equals("")&& !sql_hash.equals("")){
		if(!sql_wrkjob_cd.equals("")&& !sql_hash.equals("")){
			OdsHistSqlText param = new OdsHistSqlText();
			
//			param.setDbid(tuningTargetSql.getDbid());
			param.setWrkjob_cd(sql_wrkjob_cd);
			param.setSql_hash(sql_hash);
			
			try{
				//TCPClient 통해 collect db에서 가져오던 것을 master db에서 조회
//				odsHistSqlText = sqlAnalysisService.sqlText(param);
				odsHistSqlText = sqlAnalysisService.getSqlText2(param);
				
			} catch (Exception ex){
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			}
			tuningTargetSql.setSql_text(odsHistSqlText.getSql_text());
			
		}
		
		//튜닝요청 초기화값을 세팅해줌
		initValues = improvementManagementService.getInitValues(user_id);
		//값이있다면 튜닝요청 초기화값
		if(initValues != null) {model.addAttribute("initValues", initValues);}
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("user_id", user_id );
		model.addAttribute("user_nm", user_nm );
		model.addAttribute("wrkjob_cd", wrkjob_cd );
		model.addAttribute("wrkjob_nm", wrkjob_nm );
		model.addAttribute("ext_no", ext_no );
//		model.addAttribute("dbid", dbid);
//		model.addAttribute("program_type_cd", StringUtils.defaultString(tuningTargetSql.getProgram_type_cd()));
//		model.addAttribute("parsing_schema_name", StringUtils.defaultString(tuningTargetSql.getParsing_schema_name()));
		model.addAttribute("menu_id", tuningTargetSql.getMenu_id() );
		model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm() );
		model.addAttribute("maxUploadSize", maxUploadSize);
		List<String> noneFileList = commonService.getNoneFileList();
		model.addAttribute("noneFileList", noneFileList);
		
		return "requestImprovement";
	}

	@RequestMapping(value="/RequestImprovement/FromSqlPerformance", method=RequestMethod.GET)
	public String RequestImprovementFromSqlPerformance(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		String wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
		String wrkjob_nm = SessionManager.getLoginSession().getUsers().getWrkjob_nm();
		String ext_no = SessionManager.getLoginSession().getUsers().getExt_no();
		OdsHistSqlText odsHistSqlText = new OdsHistSqlText();

		String dbid = StringUtils.defaultString(tuningTargetSql.getDbid());
		String sql_wrkjob_cd = StringUtils.defaultString(tuningTargetSql.getWrkjob_cd());
		String sql_hash = StringUtils.defaultString(tuningTargetSql.getSql_hash());
		logger.debug("sql_wrkjob_cd:"+sql_wrkjob_cd);
		logger.debug("sql_hash:"+sql_hash);
		
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("user_id", user_id );
		model.addAttribute("user_nm", user_nm );
		model.addAttribute("wrkjob_cd", wrkjob_cd );
		model.addAttribute("wrkjob_nm", wrkjob_nm );
		model.addAttribute("ext_no", ext_no );
		model.addAttribute("menu_id", tuningTargetSql.getMenu_id() );
		model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm() );
		
		return "requestImprovement";
	}	
	
	@RequestMapping(value="/UploadCheck", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result UploadCheck(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, 
												MultipartHttpServletRequest req, Model model) throws IOException {
		Result result = new Result();
		
		List<MultipartFile> fileList = req.getFiles("uploadFile");
		
		int fileLength = 0;
		for (MultipartFile file : fileList) {
			logger.debug("fileSize =-----> "+ file.getSize() + "maxUploadSize ==>? "+ maxUploadSize);
			if (file.getSize() > maxUploadSize) {
				
				fileLength = file.getBytes().length/1024/1024;
				
				result.setResult(false);
				result.setMessage( file.getOriginalFilename()+" 파일 용량이 너무 큽니다.<br/>10메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
				
				return result;
			}
		}
		if ( fileList.size() > 0 && !fileList.get(0).getOriginalFilename().equals("")) {
			
			if ( fileList.size() < 4) {
				
				for (MultipartFile file : fileList) {
					
					logger.debug("########### 업로드하려는 파일의 이름 : "+ file.getOriginalFilename()+" ###########");
					
					String file_name = "";
					if (file.getOriginalFilename().indexOf(".") != -1) {
						file_name = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
						List<String> noneFileList = new ArrayList<String>();
						noneFileList = commonService.getNoneFileList();
						
						if (noneFileList.contains(file_name)) {
							result.setResult(false);
							result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
							
							return result;
						}
					}
					
				}
			} else {
				result.setMessage("첨부파일은 최대 3개까지 첨부가능 합니다.");
				result.setResult(false);
				return result;
			}
		}
		
		return result;
	}
	
	/* 튜닝요청 INSERT - Multi */
	@RequestMapping(value="/RequestImprovementMultiAction", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result RequestImprovementMultiAction( @ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
													MultipartHttpServletRequest req, Model model) throws IOException {
		Result result = new Result();
		result.setResult(true);
		
		List<MultipartFile> fileList = req.getFiles("uploadFile");
		String originalFileName = "";
		int fileLength = 0;
		int resultCnt = 0;
		
		if(fileList.size() > 0) {
			String file_name = "";
			
			if (!fileList.get(0).getOriginalFilename().equals("")) {
				if ( fileList.size() > 0 && fileList.size() > 3) {
					result.setMessage("첨부파일은 최대 3개까지 첨부가능 합니다.");
					result.setResult(false);
					
					return result;
				} else {
					List<String> noneFileList = commonService.getNoneFileList();
					
					for (MultipartFile file : fileList) {
						logger.debug("##### 업로드하려는 파일의 이름 : "+ file.getOriginalFilename()+" #####");
						
						if (file.getOriginalFilename().indexOf(".") != -1) {
							file_name = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
							
							if (noneFileList.contains(file_name)) {
								result.setResult(false);
								result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
								
								break;
							}
						}
					}
					
					noneFileList.clear();
					
					if(result.getResult() == false) {
						return result;
					}
				}
			}
		}
		
		for (MultipartFile file : fileList) {
			logger.debug("######## fileSize : "+ file.getSize() + " , maxUploadSize : "+ maxUploadSize);
			if (file.getSize() > maxUploadSize) {
				originalFileName = file.getOriginalFilename();
				fileLength = file.getBytes().length/1024/1024;
				
				result.setResult(false);
				result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>10메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
				
				return result;
			}
		}
		
		try {
			resultCnt = improvementManagementService.insertImprovement(req, tuningTargetSql);
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
	
	/* 튜닝요청 INSERT */
//	@RequestMapping(value="/RequestImprovementAction", method=RequestMethod.POST)
//	@ResponseBody
//	public Result RequestImprovementAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, HttpServletRequest req, HttpServletResponse res, Model model) {
//		Result result = new Result();
//		int resultCnt = 0;
//		try {				
//			resultCnt = improvementManagementService.insertImprovement(req);
//			if(resultCnt > 0){
//				result.setResult(true);
//				result.setMessage("튜닝요청에 성공하였습니다.");
//			}else{
//				result.setResult(false);
//				result.setMessage("튜닝요청에 실패하였습니다.");
//			}
//		}catch (Exception ex) {
//			result.setResult(false);
//			result.setMessage(ex.getMessage());
//		}
//		return result;		
//	}	
	
	/* 튜닝요청 변경 */
	@RequestMapping(value = "/UpdateImprovement")
	public String UpdateImprovement(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		TuningTargetSql initValues = new TuningTargetSql();
		try{
			String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
			model.addAttribute("nowDate", nowDate);
			model.addAttribute("tuningTargetSql", improvementManagementService.getImprovementInfo(tuningTargetSql));
			model.addAttribute("bindSetList", improvementManagementService.bindSetList(tuningTargetSql));
			model.addAttribute("sqlBindList", improvementManagementService.sqlBindList(tuningTargetSql));
			model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
			model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());

			//kb카드 요구사항
			//튜닝완료요청일자 오늘 + 7
			String tuning_complete_due_day = StringUtils.defaultString(improvementManagementService.getTuningCompleteDueDay(),"0");
			logger.debug("tuning_complete_due_day :"+tuning_complete_due_day);
			int i_tuning_complete_due_day = Integer.parseInt(tuning_complete_due_day);
			String tuning_complete_due_dt = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", nowDate, i_tuning_complete_due_day);
			tuningTargetSql.setTuning_complete_due_dt(tuning_complete_due_dt);

		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("user_id",user_id);
		//튜닝요청 초기화값을 세팅해줌
		initValues = improvementManagementService.getInitValues(user_id);
		//값이있다면 튜닝요청 초기화값
		if(initValues != null) {model.addAttribute("initValues", initValues);}
		
		
		return "updateImprovement";
	}
	
	/* 튜닝요청 변경 ACTION */
	@RequestMapping(value="/UpdateImprovementAction", method=RequestMethod.POST)
	@ResponseBody
	public Result UpdateImprovementAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
			HttpServletRequest req, HttpServletResponse res, Model model) {
		Result result = new Result();
		int resultCnt = 0;
		try {
			resultCnt = improvementManagementService.updateImprovement(req);
			if (resultCnt > 0) {
				result.setResult(true);
				result.setMessage("튜닝요청에 성공하였습니다.");
			} else {
				result.setResult(false);
				result.setMessage("튜닝요청에 실패하였습니다.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* 성능 개선 관리  */
	@RequestMapping(value="/ImprovementManagement", method={RequestMethod.GET, RequestMethod.POST})
	public String ImprovementManagement(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusMons("yyyy-MM-dd","yyyy-MM-dd", nowDate,-1) ;

		
		if(tuningTargetSql.getStrStartDt() == null || tuningTargetSql.getStrStartDt().equals("")){
			if(tuningTargetSql.getDay_gubun() != null){
				if(tuningTargetSql.getDay_gubun().equals("ALL")){
					startDate = Config.getString("setting.date"); // 서버 셋팅 시작일자..
				}else if(tuningTargetSql.getDay_gubun().equals("1")){
					startDate = DateUtil.getNextMonth("M", "yyyy-MM-dd", nowDate, "1");
				}else if(tuningTargetSql.getDay_gubun().equals("3")){
					startDate = DateUtil.getNextMonth("M", "yyyy-MM-dd", nowDate, "3");
				}else if(tuningTargetSql.getDay_gubun().equals("6")){
					startDate = DateUtil.getNextMonth("M", "yyyy-MM-dd", nowDate, "6");
				}else if(tuningTargetSql.getDay_gubun().equals("12")){
					startDate = DateUtil.getNextMonth("M", "yyyy-MM-dd", nowDate, "12");
				}
			}
			tuningTargetSql.setStrStartDt(startDate);
		}
		
		if(tuningTargetSql.getStrEndDt() == null || tuningTargetSql.getStrEndDt().equals("")){
			tuningTargetSql.setStrEndDt(nowDate);
		}
		
//		model.addAttribute("headerBtnIsClicked", tuningTargetSql.getHeaderBtnIsClicked());
		model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
		model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());

		return "improvementManagement";
	}
	
	/* 성능 개선 관리 list action */
	@RequestMapping(value="/ImprovementManagementAction", method=RequestMethod.POST)
	@ResponseBody
	public Result ImprovementManagementAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		TuningTargetSql resultTuningTargetSql = new TuningTargetSql();
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
		String leader_yn = SessionManager.getLoginSession().getUsers().getLeader_yn();
		
		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}else if(auth_cd.equals("ROLE_DEV")){
			tuningTargetSql.setLeader_yn(leader_yn);
			tuningTargetSql.setWrkjob_cd(wrkjob_cd);
			tuningTargetSql.setTuning_requester_id(user_id);
			tuningTargetSql.setWrkjob_mgr_id(user_id);
		}
		
		try{
			// 1. 성능개선 현황 요약			
			resultTuningTargetSql = improvementManagementService.getImprovementSummary(tuningTargetSql);
			result.setObject(resultTuningTargetSql);
			// 2. 진행현황 리스트
//			resultList = improvementManagementService.improvementStatusList(tuningTargetSql);
//			result.setTxtValue(success(resultList).toJSONObject().toString());
			
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	/* 성능 개선 관리 list action */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/ImprovementManagementAction2", method=RequestMethod.POST)
	@ResponseBody
	public Result ImprovementManagementAction2(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		TuningTargetSql resultTuningTargetSql = new TuningTargetSql();
		List<TuningTargetSql> resultList = new ArrayList<TuningTargetSql>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
		String leader_yn = SessionManager.getLoginSession().getUsers().getLeader_yn();
		
		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}else if(auth_cd.equals("ROLE_DEV")){
			tuningTargetSql.setLeader_yn(leader_yn);
			tuningTargetSql.setWrkjob_cd(wrkjob_cd);
			tuningTargetSql.setTuning_requester_id(user_id);
			tuningTargetSql.setWrkjob_mgr_id(user_id);
		}
		
		int dataCount4NextBtn = 0;
		try{
			// 1. 성능개선 현황 요약			
//			resultTuningTargetSql = improvementManagementService.getImprovementSummary(tuningTargetSql);
//			result.setObject(resultTuningTargetSql);
			// 2. 진행현황 리스트
			resultList = improvementManagementService.improvementStatusList(tuningTargetSql);
			if (resultList != null && resultList.size() > tuningTargetSql.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(tuningTargetSql.getPagePerCount());
			}
			
//			result.setTxtValue(success(resultList).toJSONObject().toString());

			JSONObject jobj = success(resultList).toJSONObject();
			jobj.put("dataCount4NextBtn", dataCount4NextBtn);
			result.setTxtValue(jobj.toString());
			
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	/* 성능 개선 관리 상세 */
	@RequestMapping(value = "/ImprovementManagementView")
	public String ImprovementManagementView(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		String choiceDivCd = tuningTargetSql.getChoice_div_cd();
		String operationYn = Config.getString("operation.yn"); // 개발,운영 구분 [Y : 운영, N : 개발]
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String leader_yn = SessionManager.getLoginSession().getUsers().getLeader_yn();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		model.addAttribute("auth_cd",auth_cd);

		try{
			// 0. 성능개선현황 상태 조회
			SqlTuning imprBeforeAfter = improvementManagementService.getImprBeforeAfter(tuningTargetSql);
			model.addAttribute("imprBeforeAfter", imprBeforeAfter);
			// 1. 성능개선현황 상태 조회
			SqlTuning sqlTuning = improvementManagementService.getSQLTuning(tuningTargetSql);
			logger.debug("after_tuning_no :"+sqlTuning.getAfter_tuning_no());
			model.addAttribute("sqlTuning", sqlTuning);
			// 튜닝 이력 건수 조회
			String dbid = StringUtils.defaultString(tuningTargetSql.getDbid());
			String dbio = StringUtils.defaultString(tuningTargetSql.getTemp_dbio());
			logger.debug("dbid ######:"+dbid);
			logger.debug("dbio ######:"+dbio);
			int sqlTuningHistoryCount = 0;
			if(!dbid.equals("") && !dbio.equals("")){
				tuningTargetSql.setDbio(tuningTargetSql.getTemp_dbio());
				sqlTuningHistoryCount = tuningHistoryService.tuningHistoryCount(tuningTargetSql);
			}
			logger.debug("sqlTuningHistoryCount ######:"+sqlTuningHistoryCount);
			model.addAttribute("sqlTuningHistoryCount", sqlTuningHistoryCount);
			
			// 요청 유형 코드 List
			Cd cd = new Cd();
			cd.setGrp_cd_id("1003");
			
			List<Cd> comCodeList = commonService.commonCodeList(cd);
			
			if ( comCodeList != null && !choiceDivCd.equals("")/* && choickDivCd != null */) {
				for (Cd code : comCodeList) {
					if ( choiceDivCd.equals( code.getCd() ) ) {
						logger.debug("VSQLCheck ========================> : "+ code.getRef_vl_1() );
						model.addAttribute( "VSQLCheck", code.getRef_vl_1() );
						break;
					}
				}
				
			}
			// 2-1. SQL (선정)(DB 변경 성능 영향도 분석)(튜닝 SQL 일괄 검증)(운영 SQL 성능 추적)(테이블 변경 성능 영향도 분석) 상세 : CHOICE_DIV_CD -> 1, 2, G , H , I , J
			// 2-2. SQL (요청) 상세 : CHOICE_DIV_CD -> 3
			// 2-3. SQL (FULL SCAN) 상세 : CHOICE_DIV_CD -> 4
			// 2-4. SQL (PLAN 변경) 상세 : CHOICE_DIV_CD -> 5
			// 2-5. SQL (신규SQL) 상세 : CHOICE_DIV_CD -> 6
			// 2-6. SQL (TEMP과다사용) 상세 : CHOICE_DIV_CD -> 7
			// 2-7. SQL (TOPSQL) 상세 : CHOICE_DIV_CD -> C
			// 2-8. SQL (OFFLOAD비효율SQL) 상세 : CHOICE_DIV_CD -> D
			// 2-9. SQL (OFFLOAD효율저하SQL) 상세 : CHOICE_DIV_CD -> E
			// 2-10. SQL (배포후성능점검)(사용자검증)(자동검증) 상세 : CHOICE_DIV_CD -> F , K , L
			
			if(choiceDivCd.equals("1") || choiceDivCd.equals("2") || choiceDivCd.equals("G") || choiceDivCd.equals("N") || choiceDivCd.equals("H") || choiceDivCd.equals("I") || choiceDivCd.equals("J")){
				model.addAttribute("selection", improvementManagementService.getSelection(tuningTargetSql));
				model.addAttribute("sqlBindList", improvementManagementService.sqlBindList(tuningTargetSql));
			}else if(choiceDivCd.equals("3")||choiceDivCd.equals("B") ){
				TuningTargetSql temp = improvementManagementService.getRequest(tuningTargetSql);
				model.addAttribute("request", temp);
				model.addAttribute("userInfo", improvementManagementService.getUsersInfo( temp.getTuning_requester_id() ));
				model.addAttribute("bindSetList", improvementManagementService.bindSetList(tuningTargetSql));
				model.addAttribute("sqlBindList", improvementManagementService.sqlBindList(tuningTargetSql));
			}else if(choiceDivCd.equals("4")){
				model.addAttribute("fullScan", improvementManagementService.getFullScan(tuningTargetSql));
			}else if(choiceDivCd.equals("5")){
				model.addAttribute("planChange", improvementManagementService.getPlanChange(tuningTargetSql));
			}else if(choiceDivCd.equals("6")){
				model.addAttribute("newSql", improvementManagementService.getNewSQL(tuningTargetSql));
			}else if(choiceDivCd.equals("7")){
				model.addAttribute("tempOver", improvementManagementService.getTempOver(tuningTargetSql));
			}else if(choiceDivCd.equals("C")){
				model.addAttribute("topSqlUnionOffloadSql", improvementManagementService.getTopSqlUnionOffloadSql(tuningTargetSql));
			}else if(choiceDivCd.equals("D")){
				model.addAttribute("topSqlUnionOffloadSql", improvementManagementService.getTopSqlUnionOffloadSql(tuningTargetSql));
			}else if(choiceDivCd.equals("E")){
				model.addAttribute("topSqlUnionOffloadSql", improvementManagementService.getTopSqlUnionOffloadSql(tuningTargetSql));
			}else if(choiceDivCd.equals("F") || choiceDivCd.equals("K") || choiceDivCd.equals("L")){
				model.addAttribute("deployAfter", improvementManagementService.getDeployAfterPerf(tuningTargetSql) );
				model.addAttribute("sqlBindList", improvementManagementService.sqlBindList(tuningTargetSql));
			}
			
			// 4-1. 완료 사유 구분 (GRP_CD_ID : 1036)
			// 4-2. 완료 사유상세 구분 (GRP_CD_ID : 1037)
			model.addAttribute("completeReasonList", improvementManagementService.completeReasonList());
			List<Cd> completeReasonDetailList = improvementManagementService.completeReasonDetailList();
			model.addAttribute("completeReasonDetailList", completeReasonDetailList);
			
			// 5. 완료 상세 정보 조회
			List<SqlImprovementType> sqlImprovementTypeList = improvementManagementService.sqlImprovementTypeList(tuningTargetSql);
			model.addAttribute("improvementList", sqlImprovementTypeList);
			model.addAttribute("choickDivCd", choiceDivCd);
			model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
			model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());
			model.addAttribute("operationYn", operationYn);
			model.addAttribute("loginUser", user_id);
			model.addAttribute("leader_yn",leader_yn);
			List<SqlTuningAttachFile>readTuningFiles = improvementManagementService.readTuningAttachFiles(tuningTargetSql);
			model.addAttribute("tuningFiles",readTuningFiles);
			logger.debug("menu ======> "+tuningTargetSql.getMenu_id()+", "+tuningTargetSql.getMenu_nm());
			// 3. SQL 개선사항
//			model.addAttribute("improvements", improvementManagementService.getImprovements(tuningTargetSql));
			TuningTargetSql sqlDetail = improvementManagementService.getImprovements(tuningTargetSql);
			String impr_sql_text = StringUtil.formatHTML(sqlDetail.getImpr_sql_text());
			String imprb_exec_plan = StringUtil.formatHTML(sqlDetail.getImprb_exec_plan());
			String impra_exec_plan = StringUtil.formatHTML(sqlDetail.getImpra_exec_plan());
			sqlDetail.setImpr_sql_text(impr_sql_text);
			sqlDetail.setImprb_exec_plan(imprb_exec_plan);
			sqlDetail.setImpra_exec_plan(impra_exec_plan);
			
			if( choiceDivCd.equals("G") ) {
				if ( sqlDetail.getAsis_elapsed_time().substring(0,1).equals(".") ) {
					sqlDetail.setAsis_elapsed_time( 0+sqlDetail.getAsis_elapsed_time() );
				}
			}
			
			/*System.out.println("####################################");
			System.out.println();
			System.out.println("SQL TEXT");
			System.out.println(sqlDetail.getSql_text());
			System.out.println("개선SQL");
			System.out.println(sqlDetail.getImpr_sql_text());
			System.out.println("개선전 실행계획");
			System.out.println(sqlDetail.getImprb_exec_plan());
			System.out.println("개선후 실행계획");
			System.out.println(sqlDetail.getImpra_exec_plan());
			System.out.println();
			System.out.println("####################################");*/
			
//			String imprb_exec_plan = StringUtils.defaultString(sqlDetail.getImprb_exec_plan());
//			imprb_exec_plan = imprb_exec_plan.replaceAll("(\r\n|\\r\\n|\\\\r\\\\n|\\n|\\\\n)", "<br/>");
//			imprb_exec_plan = imprb_exec_plan.replaceAll(" ", "&nbsp;");
//			sqlDetail.setImprb_exec_plan(imprb_exec_plan);

//			String impra_exec_plan = StringUtils.defaultString(sqlDetail.getImpra_exec_plan());
//			impra_exec_plan = impra_exec_plan.replaceAll("(\r\n|\\r\\n|\\\\r\\\\n|\\n|\\\\n)", "<br/>");
//			impra_exec_plan = impra_exec_plan.replaceAll(" ", "&nbsp;");
//			sqlDetail.setImpra_exec_plan(impra_exec_plan);

			model.addAttribute("sqlDetail", sqlDetail);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return "improvementManagementView";
	}
	
	@RequestMapping(value = "/ImprovementManagement/getPerfSourceType", method = RequestMethod.POST)
	@ResponseBody
	public int getPerfSourceType(
			@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model ) throws Exception {
		int sourceType = 0;
		
		try {
			sourceType = improvementManagementService.getPerfSourceType( tuningTargetSql );
			
		} catch ( NullPointerException ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return sourceType;
	}
	
	/* 성능 개선 관리 상세 - SQL 개선 이력 */
	@RequestMapping(value = "/ImprovementManagement/SQLImproveHistory")
	public String SQLImproveHistory(@ModelAttribute("sqlTuningHistory") SqlTuningHistory sqlTuningHistory, Model model) {
		model.addAttribute("menu_id", sqlTuningHistory.getMenu_id());
		model.addAttribute("menu_nm", sqlTuningHistory.getMenu_nm());
		return "sqlImproveHistory";
	}
	
	/* 성능 개선 관리 상세 - SQL 개선 이력 action */
	@RequestMapping(value = "/ImprovementManagement/SQLImproveHistoryAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SQLImproveHistoryAction(@ModelAttribute("sqlTuningHistory") SqlTuningHistory sqlTuningHistory, Model model) {
		List<SqlTuningHistory> resultList = new ArrayList<SqlTuningHistory>();

		try{
			resultList = improvementManagementService.sqlImproveHistoryList(sqlTuningHistory);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
		
	}
	
	/* 성능 개선 관리 상세 - SQL 개선 이력 - 상세 */
	@RequestMapping(value = "/ImprovementManagement/SQLImproveHistoryView")
	public String SQLImproveHistoryView(@ModelAttribute("sqlTuningHistory") SqlTuningHistory sqlTuningHistory, Model model) {
		SqlTuningHistory sqlDetail = null;
		try {
			logger.debug("update_dt:"+sqlTuningHistory.getUpdate_dt());
			sqlDetail =  improvementManagementService.getSQLImproveHistory(sqlTuningHistory);
			//중복페이지 제거를 위해 sqlImproveHistory를 sqlDetail로 변경
			//model.addAttribute("sqlImproveHistory", sqlDetail);
			model.addAttribute("sqlDetail", sqlDetail);
			model.addAttribute("update_dt", sqlTuningHistory.getUpdate_dt());
			model.addAttribute("menu_id", sqlTuningHistory.getMenu_id());
			model.addAttribute("menu_nm", sqlTuningHistory.getMenu_nm());
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return "sqlImproveHistoryView";
		
	}

	/* 성능 개선 관리 - 튜닝중 처리 */
	@RequestMapping(value="/ImprovementManagement/SaveTuning", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveTuningAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {
			String getTuningNoArry = StringUtils.defaultString(tuningTargetSql.getTuningNoArry());
			logger.debug("SaveTuningAction getTuningNoArry:"+getTuningNoArry);
			if(getTuningNoArry.equals("")){
				result.setResult(false);
				result.setMessage("튜닝중 처리중 오류가 발생하였습니다.<br/>항목을 다시 선택하여 주세요.");
			}else{
				rowCnt = improvementManagementService.saveTuning(tuningTargetSql);
				logger.debug("SaveTuningAction rowCnt:"+rowCnt);
				if(rowCnt > 0){
					result.setResult(true);
					result.setMessage("튜닝중으로 변경하였습니다.");
				}else{
					result.setResult(false);
					result.setMessage("튜닝중으로 변경에 실패하였습니다.");
				}
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
		
	}

	/* 성능 개선 관리 - 접수 취소 처리 */
	@RequestMapping(value="/ImprovementManagement/SaveReceiptCancel", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveReceiptCancelAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {
			rowCnt = improvementManagementService.saveReceiptCancel(tuningTargetSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("접수 취소로 변경에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
		
	}
	
	/* 성능 개선 관리 - 리스트 상에서 여러건을 튜닝취소 처리 */
	@RequestMapping(value="/ImprovementManagement/SaveTuningCancelAll", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveTuningCancelAllAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {
			rowCnt = improvementManagementService.saveTuningCancelAll(tuningTargetSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("튜닝 취소로 변경에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
		
	}	
	
	/* 성능 개선 관리 - 리스트 상에서 여러건을 튜닝요청 취소 처리 */
	@RequestMapping(value="/ImprovementManagement/CancelTuningRequestAction", method=RequestMethod.POST)
	@ResponseBody
	public Result CancelTuningRequestAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {
//			rowCnt = improvementManagementService.saveTuningCancelAll(tuningTargetSql);
			rowCnt = improvementManagementService.saveRequestCancelAll(tuningTargetSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("튜닝요청 취소로 변경에 실패하였습니다.");
		}catch (Exception ex) {
//			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}	
	
	/* 성능 개선 관리 - 튜닝취소 처리 */
	@RequestMapping(value="/ImprovementManagement/SaveTuningCancel", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveTuningCancelAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {				
			rowCnt = improvementManagementService.saveTuningCancel(tuningTargetSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("튜닝 취소로 변경에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
		
	}	
	
	/* 성능 개선 관리 - 반려 처리 */
	@RequestMapping(value="/ImprovementManagement/SaveCancel", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveCancelAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int returnValue = 0;
		try {
			logger.debug("rereqest:"+tuningTargetSql.getRerequest());
			returnValue = improvementManagementService.saveCancel(tuningTargetSql);
			if(returnValue > 0){
				result.setResult(true);
				result.setTxtValue(String.valueOf(returnValue));
				result.setMessage("반려처리로 변경에 성하였습니다.");
			}else{
				result.setResult(false);
				result.setTxtValue(String.valueOf(returnValue));
				result.setMessage("반려처리로 변경에 실패하였습니다.");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 성능 개선 관리 - 튜닝종료 처리 */
	@RequestMapping(value="/ImprovementManagement/SaveEnd", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveEndAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {				
			rowCnt = improvementManagementService.saveEnd(tuningTargetSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("튜닝종료 처리로 변경에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 성능 개선 관리 - 소스점검요청 처리 */
	@RequestMapping(value="/ImprovementManagement/SavePreCheck", method=RequestMethod.POST)
	@ResponseBody
	public Result SavePreCheckAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {				
			rowCnt = improvementManagementService.savePreCheck(tuningTargetSql);
			if(rowCnt > 0){
				result.setResult(true);
				result.setMessage("사전점검이 정상적으로 처리되었습니다.");
			}else{
				result.setResult(false);
				result.setMessage("사전점검 처리에 실패하였습니다.");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 성능 개선 관리 - 튜닝완료,튜닝 완료 처리 */
	@RequestMapping(value="/ImprovementManagement/SaveComplete", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveCompleteAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,HttpServletRequest req, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {				
			//tuningTargetSql.setTemporary_save_yn("N");
			logger.debug("Temporary_save_yn :"+tuningTargetSql.getTemporary_save_yn());
			rowCnt = improvementManagementService.completeSqlTuning(tuningTargetSql,req);
			if(rowCnt > 0){
				result.setResult(true);
				result.setMessage("튜닝완료 처리하였습니다.");
			}else{
				result.setResult(false);
				result.setMessage("튜닝완료 처리로 변경에 실패하였습니다.");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}

	/* 성능 개선 관리 - 임시 저장 */
	@RequestMapping(value="/ImprovementManagement/TempSaveComplete", method=RequestMethod.POST)
	@ResponseBody
	public Result TempSaveCompleteAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,HttpServletRequest req, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {
			//tuningTargetSql.setTemporary_save_yn("Y");
			logger.debug("Temporary_save_yn :"+tuningTargetSql.getTemporary_save_yn());
			rowCnt = improvementManagementService.completeSqlTuning(tuningTargetSql,req);
			if(rowCnt > 0){
				result.setResult(true);
				result.setMessage("임시 저장에 성공하였습니다.");
			}else{
				result.setResult(false);
				result.setMessage("임시 저장에 실패하였습니다.");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 성능 개선 관리 - 튜닝 결과값 수정,저장 */
	@RequestMapping(value="/ImprovementManagement/ModifyTuningResult", method=RequestMethod.POST)
	@ResponseBody
	public Result ModifyTuningResultAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {				
			rowCnt = improvementManagementService.modifyTuningResult(tuningTargetSql);
			boolean bResult = rowCnt > 0 ? true : false;
			result.setResult(bResult);
			if(bResult){
				result.setMessage("튜닝 결과값을 수정하였습니다.");
			}else{
				result.setMessage("튜닝 결과값을 수정중 오류가 발생하였습니다.");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 성능 개선 관리 - 튜닝담당자 일괄 지정 */
	@RequestMapping(value="/ImprovementManagement/SaveTunerAssignAll", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveTunerAssignAllAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		try {				
			result = improvementManagementService.saveTunerAssignAll(tuningTargetSql);
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 성능 개선 관리 - 튜닝담당자 지정 */
	@RequestMapping(value="/ImprovementManagement/SaveTunerAssign", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveTunerAssignAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {				
			rowCnt = improvementManagementService.saveTunerAssign(tuningTargetSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("튜닝담당자 지정에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}
	
	/* 성능 개선 관리 - 업무 담당자 변경 */
	@RequestMapping(value="/ImprovementManagement/ChangeWorkUser", method=RequestMethod.POST)
	@ResponseBody
	public Result ChangeWorkUserAction(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try {				
			rowCnt = improvementManagementService.changeWorkUser(tuningTargetSql);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("업무 담당자 변경에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 성능 개선 관리 - 프로세스 처리이력 팝업 action */
	@RequestMapping(value="/ImprovementManagement/Popup/ProcessHistoryList", method=RequestMethod.GET, produces="application/text; charset=utf8")
	@ResponseBody
	public String ProcessHistoryListAction(@ModelAttribute("sqlTuningStatusHistory") SqlTuningStatusHistory sqlTuningStatusHistory, Model model) {
		List<SqlTuningStatusHistory> resultList = new ArrayList<SqlTuningStatusHistory>();

		try{
			resultList = improvementManagementService.processHistoryList(sqlTuningStatusHistory);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	
	/**
	 * 성능 개선 관리 엑셀 다운로드
	 * @param req
	 * @param res
	 * @param module
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ImprovementManagement/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView ImprovementManagementExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		String wrkjob_cd = SessionManager.getLoginSession().getUsers().getWrkjob_cd();
		String leader_yn = SessionManager.getLoginSession().getUsers().getLeader_yn();

		logger.debug("user_id:"+user_id);
		logger.debug("auth_cd:"+auth_cd);
		logger.debug("wrkjob_cd:"+wrkjob_cd);
		logger.debug("leader_yn:"+leader_yn);

		if(auth_cd.equals("ROLE_TUNER")){
			tuningTargetSql.setPerfr_id(user_id);
		}else if(auth_cd.equals("ROLE_DEV")){
			tuningTargetSql.setLeader_yn(leader_yn);
			tuningTargetSql.setWrkjob_cd(wrkjob_cd);
			tuningTargetSql.setTuning_requester_id(user_id);
			tuningTargetSql.setWrkjob_mgr_id(user_id);
		}
		
		try {
			resultList = improvementManagementService.improvementStatusList4Excel(tuningTargetSql);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "성능_개선_관리");
		model.addAttribute("sheetName", "성능_개선_관리");
		model.addAttribute("excelId", "PERF_IMPR_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/* 튜닝요청 초기값 설정  */
	@RequestMapping(value="/SaveInitSettingPop", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveInitSetting(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,
										HttpServletRequest req, HttpServletResponse res, Model model) {
		Result result = new Result();
		try {				
			int check = improvementManagementService.saveInitSetting(tuningTargetSql);
			boolean returnval = check > 0 ? true : false;
			result.setResult(returnval);
		}catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}	
	
	/* 첨부파일 다운 이벤트 */
	@RequestMapping(value = "/Tuning/DownFile")
	@ResponseBody
	public ModelAndView tuningDownFile(@ModelAttribute("sqlTuningAttachFile") SqlTuningAttachFile sqlTuningAttachFile,
											HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		DownLoadFile download = new DownLoadFile();
		
		String filePath = Config.getString("upload.file.dir"); 
		
		String file_nm = StringUtils.defaultString(sqlTuningAttachFile.getFile_nm());
		String org_file_nm = StringUtils.defaultString(sqlTuningAttachFile.getOrg_file_nm());
		
		download.setFile_path(filePath);
		download.setFile_nm(file_nm);
		download.setOrg_file_nm(org_file_nm);
		
		File downloadFile = new File(filePath + sqlTuningAttachFile.getFile_nm());
		
		if (!downloadFile.canRead()) {
			download.setFile_path("");
			download.setFile_nm("");
			download.setOrg_file_nm("첨부파일이_존재하지_않습니다.");
		}
		
		return new ModelAndView("newFileDownload", "downloadFile", download);
		
	}
	
	/* 첨부파일 삭제 이벤트 */
	@RequestMapping(value = "/Tuning/DeleteFile")
	@ResponseBody
	public Result DeleteTuningFile(@ModelAttribute("sqlTuningAttachFile") SqlTuningAttachFile sqlTuningAttachFile,
									HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Result result = new Result();
		String filePath = Config.getString("upload.file.dir");
		
		String file_nm = StringUtils.defaultString(sqlTuningAttachFile.getFile_nm());
		
		if (file_nm.equals("")) {
			logger.debug("file_nm is '"+file_nm+"'");
			result.setResult(false);
			result.setMessage("파일이 존재하지 않습니다.");
			return result;
		}
		
		File attachfile = new File(filePath + file_nm);
		
		if (!attachfile.canRead()) {
			result.setResult(false);
			result.setMessage("File can't read(파일을 찾을 수 없습니다)");
			return result;
		}
		
		try {
			int deleteTuningAttachFileResult = improvementManagementService.deleteTuningAttachFile(sqlTuningAttachFile);
			logger.debug("deletePerfGuideAttachFileResult:"+deleteTuningAttachFileResult);
			if (deleteTuningAttachFileResult != 0) {
				boolean deleteResult = attachfile.delete();
				logger.debug("deleteResult:"+deleteResult);
				result.setResult(true);
				result.setMessage("파일을 삭제하였습니다.");
			} else {
				result.setResult(false);
				result.setMessage("파일 삭제에 실패하였습니다.");
				return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
			result.setResult(true);
			result.setMessage("파일 삭제에 실패하였습니다.");
		}
		return result;
	}	
}
