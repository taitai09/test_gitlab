package omc.spop.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.Config;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.DownLoadFile;
import omc.spop.model.PerfGuide;
import omc.spop.model.PerfGuideAttachFile;
import omc.spop.model.PerfGuideUse;
import omc.spop.model.Result;
import omc.spop.model.TuningTargetSql;
import omc.spop.model.TuningTargetSqlBind;
import omc.spop.service.CommonService;
import omc.spop.service.ImprovementManagementService;
import omc.spop.service.PrecedentService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class PrecedentController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(PrecedentController.class);
	
	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	

	@Autowired
	private PrecedentService precedentService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private ImprovementManagementService improvementManagementService;
	
	/* 사례/가이드  */
	@RequestMapping(value="/Precedent")
	public String Precedent(@ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "365");
		
		if(perfGuide.getStrStartDt() == null || perfGuide.getStrStartDt().equals("")){
			perfGuide.setStrStartDt(startDate);
		}
		
		if(perfGuide.getStrEndDt() == null || perfGuide.getStrEndDt().equals("")){
			perfGuide.setStrEndDt(nowDate);
		}		
		
		model.addAttribute("menu_id", perfGuide.getMenu_id());
		model.addAttribute("menu_nm", perfGuide.getMenu_nm());
		model.addAttribute("searchBtnClickCount1", perfGuide.getSearchBtnClickCount1());
		model.addAttribute("searchBtnClickCount2", perfGuide.getSearchBtnClickCount2());
		model.addAttribute("call_from_parent", perfGuide.getCall_from_parent());
		model.addAttribute("call_from_child", perfGuide.getCall_from_child());

		return "precedent/precedentList";
	}
	
	/* 사례/가이드 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/SqlTuningGuideAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SqlTuningGuideAction(@ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		List<PerfGuide> resultList = new ArrayList<PerfGuide>();

		int dataCount4NextBtn = 0;
		try{
			resultList = precedentService.sqlTuningGuideList(perfGuide);
			if (resultList != null && resultList.size() > perfGuide.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(perfGuide.getPagePerCount());
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/PrecedentAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String PrecedentAction(@ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		List<PerfGuide> resultList = new ArrayList<PerfGuide>();
		
		int dataCount4NextBtn = 0;
		try{
			resultList = precedentService.precedentList(perfGuide);
			if (resultList != null && resultList.size() > perfGuide.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(perfGuide.getPagePerCount());
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
	
	@RequestMapping(value = "/Precedent/Insert", method=RequestMethod.GET)
	public String PrecedentInsert(@ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		model.addAttribute("searchBtnClickCount1", perfGuide.getSearchBtnClickCount1());
		model.addAttribute("searchBtnClickCount2", perfGuide.getSearchBtnClickCount2());
		model.addAttribute("menu_id", perfGuide.getMenu_id());
		model.addAttribute("menu_nm", perfGuide.getMenu_nm());
	
		return "precedent/insert";
	}	
	
	/* 사례/가이드 INSERT */
	@RequestMapping(value="/Precedent/Insert_20181205", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result PrecedentInsertAction_20181205(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		Result result = new Result();
		
		if(file.getSize() > maxUploadSize){
	    	result.setResult(false);
	    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
	    }else{
			try {				
				precedentService.insertPrecedentGuide(file, perfGuide);
				result.setResult(true);
			}catch (Exception ex) {
				ex.printStackTrace();
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}		
	    }
		
		return result;		
	}		
	
	/* 사례/가이드 INSERT */
	@RequestMapping(value="/Precedent/Insert", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result PrecedentInsertAction(MultipartHttpServletRequest multipartRequest, @ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) throws IOException {
		List<MultipartFile> fileList = multipartRequest.getFiles("uploadFile");
		Result result = new Result();
		String file_name = "";
        for (MultipartFile file : fileList) {
        	
        	logger.debug("########### 업로드하려는 파일의 이름 : "+ file.getOriginalFilename()+" ###########");
        	
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
        	
        	if(file.getSize() > maxUploadSize){
        		String originalFileName = file.getOriginalFilename();
				int fileLength = file.getBytes().length/1024/1024;
        		result.setResult(false);
//        		result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
        		result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>10메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
        		return result;		
        	}
		}
        try {				
        	precedentService.insertPrecedentGuide(multipartRequest, perfGuide);
        	result.setResult(true);
        }catch (Exception ex) {
        	ex.printStackTrace();
        	result.setResult(false);
        	result.setMessage(ex.getMessage());
        }		
		
		return result;		
	}		
	
	@RequestMapping(value = "/Precedent/Update")
	public String PrecedentUpdate(@ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		model.addAttribute("auth_cd", auth_cd);
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		model.addAttribute("user_id", user_id);
		String reg_user_id = perfGuide.getReg_user_id();
		model.addAttribute("reg_user_id", reg_user_id);
		model.addAttribute("searchBtnClickCount1", perfGuide.getSearchBtnClickCount1());
		model.addAttribute("searchBtnClickCount2", perfGuide.getSearchBtnClickCount2());

		try{
			// 사용내역 저장 
			String useSeq = precedentService.updatePrecedentUse(perfGuide);			
			String tuning_no = StringUtils.defaultString(perfGuide.getTuning_no());
			if(!tuning_no.equals("")){
				TuningTargetSql tuningTargetSql = new TuningTargetSql();
				tuningTargetSql.setTuning_no(perfGuide.getTuning_no());
//				TuningTargetSql sqlDetail = tuningHistoryService.readSqlDetail(tuningTargetSql);
				TuningTargetSql sqlDetail = improvementManagementService.getImprovements(tuningTargetSql);
				String impr_sql_text = StringUtil.formatHTML(sqlDetail.getImpr_sql_text());
				String imprb_exec_plan = StringUtil.formatHTML(sqlDetail.getImprb_exec_plan());
				String impra_exec_plan = StringUtil.formatHTML(sqlDetail.getImpra_exec_plan());
				sqlDetail.setImpr_sql_text(impr_sql_text);
				sqlDetail.setImprb_exec_plan(imprb_exec_plan);
				sqlDetail.setImpra_exec_plan(impra_exec_plan);				
				model.addAttribute("sqlDetail", sqlDetail);
				
				model.addAttribute("bindSetList", precedentService.bindSetList(perfGuide));
				model.addAttribute("sqlBindList", precedentService.sqlBindList(perfGuide));
			}else{
				model.addAttribute("sqlDetail", new TuningTargetSql());
				model.addAttribute("bindSetList", new ArrayList<TuningTargetSqlBind>());
				model.addAttribute("sqlBindList", new ArrayList<TuningTargetSqlBind>());
			}
			
			model.addAttribute("guide", precedentService.readPerfGuide(perfGuide));
//			model.addAttribute("guideFile", precedentService.readPerfGuideFile(perfGuide));
			model.addAttribute("guideFiles", precedentService.readPerfGuideFiles(perfGuide));
			model.addAttribute("useSeq", useSeq);
			model.addAttribute("menu_id", perfGuide.getMenu_id());
			model.addAttribute("menu_nm", perfGuide.getMenu_nm());
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return "precedent/update";
	}	

	/* 사례/가이드 UPDATE */
	@RequestMapping(value="/Precedent/UpdateAction_20181205", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result PrecedentUpdateAction_20181205(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		Result result = new Result();
		
		if(file.getSize() > maxUploadSize){
	    	result.setResult(false);
	    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
	    }else{
			try {				
				precedentService.updatePrecedentGuide(file, perfGuide);
				result.setResult(true);
			}catch (Exception ex) {
				ex.printStackTrace();
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}		
	    }
		
		return result;		
	}
	
	/* 사례/가이드 UPDATE */
	@RequestMapping(value="/Precedent/UpdateAction", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result PrecedentUpdateAction(MultipartHttpServletRequest multipartRequest, @ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) throws IOException {
		List<MultipartFile> fileList = multipartRequest.getFiles("uploadFile");
		Result result = new Result();
		String file_name = "";
		try {		
	        for (MultipartFile file : fileList) {
	        	logger.debug("########### 업로드하려는 파일의 이름 : "+ file.getOriginalFilename()+" ###########");
	        	
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
	        	
	        	if(file.getSize() > maxUploadSize){
	        		String originalFileName = file.getOriginalFilename();
	        		logger.debug("fileName:"+file.getName());
					int fileLength = file.getBytes().length/1024/1024;
	        		result.setResult(false);
	//        		result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
	        		result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>10메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
	        		return result;		
	        	}
	        	
			}
        	precedentService.updatePrecedentGuide(multipartRequest, perfGuide);
        	result.setResult(true);
        }catch (Exception ex) {
        	ex.printStackTrace();
        	result.setResult(false);
        	result.setMessage(ex.getMessage());
        }	
		return result;		
	}
	
	/* 사례/가이드 DELTE */
	@RequestMapping(value="/Precedent/DeleteAction", method=RequestMethod.POST)
	@ResponseBody
	public Result PrecedentDeleteAction(@ModelAttribute("perfGuide") PerfGuide perfGuide, Model model) {
		Result result = new Result();
		try {				
			precedentService.deletePrecedentGuide(perfGuide);
			result.setResult(true);
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;		
	}	
	
	@RequestMapping(value = "/Precedent/DownGuideFile")
//	@RequestMapping(value = "/Precedent/DownGuideFile", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView fileDown(@ModelAttribute("perfGuideAttachFile") PerfGuideAttachFile perfGuideAttachFile,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		DownLoadFile download = new DownLoadFile();
		//String rootPath = request.getSession().getServletContext().getRealPath("/");
		//String filePath = rootPath + guideRoot;
		
		String guideRoot = Config.getString("upload.guide.dir");
		String filePath = guideRoot;

		String file_nm = StringUtils.defaultString(perfGuideAttachFile.getFile_nm());
		String org_file_nm = StringUtils.defaultString(perfGuideAttachFile.getOrg_file_nm());
		
		if(file_nm.equals("") || org_file_nm.equals("")){
			logger.debug("file_nm is '"+file_nm+"' and org_file_nm is '"+org_file_nm+"'");
			Map<String, String> m = new HashMap<String, String>();
			m.put("msg", "파일이 존재하지 않습니다.");
			return null;//new ModelAndView("result",m);
		}
		
		download.setFile_path(filePath);
		download.setFile_nm(file_nm);		
		download.setOrg_file_nm(org_file_nm);

		File downloadFile = new File(filePath + file_nm);
        
        if(!downloadFile.canRead()){
            throw new Exception("File can't read(파일을 찾을 수 없습니다)");
        }
        
        PerfGuideUse perfGuideUse = new PerfGuideUse();
        perfGuideUse.setGuide_no(perfGuideAttachFile.getGuide_no());
        perfGuideUse.setUse_seq(perfGuideAttachFile.getUse_seq());
        
		try {				
			precedentService.updatePerfGuideUse(perfGuideUse);
		}catch (Exception e) {
			e.printStackTrace();
		}

        return new ModelAndView("fileDownload", "downloadFile", download);
    }	
	
	
	@RequestMapping(value = "/Precedent/DownFile")
	@ResponseBody
    public ModelAndView DownFile(@ModelAttribute("perfGuideAttachFile") PerfGuideAttachFile perfGuideAttachFile, HttpServletRequest request, HttpServletResponse response) throws Exception{
		DownLoadFile download = new DownLoadFile();

			
			String guideRoot = Config.getString("upload.guide.dir");
			String filePath = guideRoot;
	
			download.setFile_path(filePath);
			download.setFile_nm(perfGuideAttachFile.getFile_nm());		
			download.setOrg_file_nm(perfGuideAttachFile.getOrg_file_nm());
	
			File downloadFile = new File(filePath + perfGuideAttachFile.getFile_nm());
	        
	        if(!downloadFile.canRead()){
	            download.setFile_path("");
	            download.setFile_nm("");
	            download.setOrg_file_nm("첨부파일이_존재하지_않습니다.");
	        }
			
			return new ModelAndView("newFileDownload", "downloadFile", download);
    }
	
	@RequestMapping(value = "/Precedent/NewDownGuideFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute("perfGuideAttachFile") PerfGuideAttachFile perfGuideAttachFile,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
     
		DownLoadFile download = new DownLoadFile();
		//String rootPath = request.getSession().getServletContext().getRealPath("/");
		//String filePath = rootPath + guideRoot;
		
		String guideRoot = Config.getString("upload.guide.dir");
		String filePath = guideRoot;

		String file_nm = StringUtils.defaultString(perfGuideAttachFile.getFile_nm());
		String org_file_nm = StringUtils.defaultString(perfGuideAttachFile.getOrg_file_nm());
		
		logger.debug("file_nm is '"+file_nm+"' and org_file_nm is '"+org_file_nm+"'");
		if(file_nm.equals("") || org_file_nm.equals("")){
            String errorMessage = "파일이 존재하지 않습니다.";
            logger.debug(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return; 			
		}
		
		download.setFile_path(filePath);
		download.setFile_nm(file_nm);		
		download.setOrg_file_nm(org_file_nm);

		File downloadFile = new File(filePath + file_nm);
        
        if(!downloadFile.canRead()){
            String errorMessage = "File can't read(파일을 찾을 수 없습니다)";
            logger.debug(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;            
        }
        
        PerfGuideUse perfGuideUse = new PerfGuideUse();
        perfGuideUse.setGuide_no(perfGuideAttachFile.getGuide_no());
        perfGuideUse.setUse_seq(perfGuideAttachFile.getUse_seq());
        
		try {				
			precedentService.updatePerfGuideUse(perfGuideUse);
		}catch (Exception e) {
			e.printStackTrace();
		}

//        return new ModelAndView("fileDownload", "downloadFile", download);
        
        if(!downloadFile.exists()){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            logger.debug(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
         
		String mimeType = URLConnection.guessContentTypeFromName(downloadFile.getName());
		logger.debug("mimeType1 :" + mimeType);
		if (mimeType == null) {
			logger.debug("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}

		logger.debug("mimetype2 : " + mimeType);
		String org_file_nm_enc = java.net.URLEncoder.encode(download.getOrg_file_nm(), "utf-8");
		logger.debug("org_file_nm_enc : " + org_file_nm_enc);
         
        response.setContentType(mimeType);
         
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
//        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + org_file_nm_enc +"\""));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(download.getOrg_file_nm(), "utf-8") + "\";");

         
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
         
        response.setContentLength((int)downloadFile.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(downloadFile));
 
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }	
	
	@RequestMapping(value = "/Precedent/DeleteGuideFile")
	@ResponseBody
	public Result DeleteGuideFile(@ModelAttribute("perfGuideAttachFile") PerfGuideAttachFile perfGuideAttachFile, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Result result = new Result();
		String filePath = Config.getString("upload.guide.dir");
		
		String file_nm = StringUtils.defaultString(perfGuideAttachFile.getFile_nm());
		
		if(file_nm.equals("")){
			logger.debug("file_nm is '"+file_nm+"'");
			result.setResult(false);
			result.setMessage("파일이 존재하지 않습니다.");
			return result;
		}
		
		File attachfile = new File(filePath + file_nm);
		
		if(!attachfile.canRead()){
			result.setResult(false);
			result.setMessage("File can't read(파일을 찾을 수 없습니다)");
			return result;			
		}
		boolean deleteResult = attachfile.delete();
		logger.debug("deleteResult:"+deleteResult);
		
		try {
			int deletePerfGuideAttachFileResult = precedentService.deletePerfGuideAttachFile(perfGuideAttachFile);
			logger.debug("deletePerfGuideAttachFileResult:"+deletePerfGuideAttachFileResult);
			result.setResult(true);
			result.setMessage("파일을 삭제하였습니다.");
		}catch (Exception e) {
			e.printStackTrace();
			result.setResult(true);
			result.setMessage("파일 삭제에 실패하였습니다.");
		}
		return result;
	}	
	
	/**
	 * 성능개선 > 사례/가이드 > 엑셀다운
	 * 가이드
	 * @param req
	 * @param res
	 * @param odsTables
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/Precedent/SqlTuningGuideExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	 // @ResponseBody
	 public ModelAndView SqlTuningGuideExcelDown(HttpServletRequest req, HttpServletResponse res,
	   @ModelAttribute("perfGuide") PerfGuide perfGuide, Model model)
	   throws Exception {

	  List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

	  try {
			resultList = precedentService.sqlTuningGuideList4Excel(perfGuide);
	  } catch (Exception ex) {
	   String methodName = new Object() {
	   }.getClass().getEnclosingMethod().getName();
	   logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
	  }
	  model.addAttribute("fileName", "SQL_튜닝_가이드");
	  model.addAttribute("sheetName", "SQL_튜닝_가이드");
	  model.addAttribute("excelId", "SQL_TUNING_GUIDE");
	  // return a view which will be resolved by an excel view resolver
	  return new ModelAndView("xlsxView", "resultList", resultList);
	 }
	
	/**
	 * 성능개선 > 사례/가이드 > 엑셀다운
	 * 사례
	 * @param req
	 * @param res
	 * @param odsTables
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/Precedent/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView PrecedentExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("perfGuide") PerfGuide perfGuide, Model model)
					throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = precedentService.precedentList4Excel(perfGuide);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "SQL_튜닝_사례");
		model.addAttribute("sheetName", "SQL_튜닝_사례");
		model.addAttribute("excelId", "SQL_TUNING_PRECEDENT");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
}