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
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Result;
import omc.spop.model.TrCd;
import omc.spop.model.Users;
import omc.spop.service.ApplicationMngService;

/***********************************************************
 * 2018.11.13 임호경 최초작성
 **********************************************************/

@Controller
public class ApplicationMngController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationMngController.class);

	@Autowired
	private ApplicationMngService applicationMngService;
	
	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	

	/* 어플리케이션 코드 설정관리 */
	@RequestMapping(value="/ApplicationCode", method=RequestMethod.GET)
	public String ApplicationCode(@ModelAttribute("trCd") TrCd trCd, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", trCd.getMenu_id());
		model.addAttribute("menu_nm", trCd.getMenu_nm() );
		
		return "applicationMng/applicationCode";
	}
	
	/* 어플리케이션 코드 설정관리 list*/
	@RequestMapping(value="/ApplicationCode/List", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ApplicationCodeList(@ModelAttribute("trCd") TrCd trCd,  Model model) {
		List<TrCd> resultList = new ArrayList<TrCd>();
		int dataCount4NextBtn = 0;

		try{
			resultList = applicationMngService.applicationCodeList(trCd);
			if(resultList != null && resultList.size() > trCd.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(trCd.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
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
	
	/*설정 - 어플리케이션 코드  관리엑셀 다운*/
	@RequestMapping(value = "/ApplicationCode/List/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PerformanceCheckMngListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("trCd") TrCd trCd, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = applicationMngService.applicationCodeListByExcelDown(trCd);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "애플리케이션_코드_관리");
		model.addAttribute("sheetName","애플리케이션_코드_관리");
		model.addAttribute("excelId", "TR_CD");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	
	/* 어플리케이션 코드 설정관리 save */
	@RequestMapping(value="/ApplicationCode/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveApplicationCode(@ModelAttribute("trCd") TrCd trCd,  Model model) {
		Result result = new Result();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		trCd.setUser_id(user_id);
		try{
			int check = applicationMngService.SaveApplicationCode(trCd);
			if(check == -1){
				result.setMessage("[ 업무명, 애플리케이션 코드 ]가 중복됩니다. <br/>다시한번 확인 후 진행해주세요.");
				result.setResult(false);
				return result;
			}
			result.setResult(true);
			result.setResultCount(check);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 어플리케이션 코드 설정관리 delete*/
	@RequestMapping(value="/ApplicationCode/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteApplicationCode(@ModelAttribute("trCd") TrCd trCd,  Model model) {
		Result result = new Result();
		
		try{
			applicationMngService.deleteApplicationCode(trCd);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	
	   /*
     * Download a file from 
     *   - inside project, located in resources folder.
     *   - outside project, located in File system somewhere. 
     */
	/* 애플리케이션 코드 엑셀파일 다운로드 */
    private static final String INTERNAL_FILE="ApplicationCode_Upload_Form.xlsx";
    private static final String EXTERNAL_FILE_PATH="C:/OMCPROJECT/ApplicationCode_Upload_Form.xlsx";	
    @RequestMapping(value="/ApplicationCode/excelFormDownload/{type}", method = {RequestMethod.POST})
    public void downloadFile(HttpServletResponse response, @PathVariable("type") String type) throws IOException {

        File file = null;
        logger.debug("type:"+type); 
        //type:internal
        if(type.equalsIgnoreCase("internal")){
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            logger.debug("classloader:"+classloader);
            //classloader:WebappClassLoader
            //context: ROOT
            //delegate: false
            //----------> Parent Classloader:
            //java.net.URLClassLoader@76ed5528
            logger.debug("current directory:"+classloader.getResource(".").getPath());
    		//current directory:/C:/OMCPROJECT/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/OPENPOP_V2/WEB-INF/classes/
            file = new File(classloader.getResource(INTERNAL_FILE).getFile());   
        }else{
            file = new File(EXTERNAL_FILE_PATH);
        }
         
        if(!file.exists()){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            logger.debug(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
         
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            logger.debug("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
         
        logger.debug("mimetype : "+mimeType);
        //mimetype : application/octet-stream
         
        response.setContentType(mimeType);
         
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
 
         
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
         
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
 
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
    
	/* 애플리케이션코드 엑셀파일 업로드 */
	@RequestMapping(value = "/ApplicationCodeRegist/Upload", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result UploadExcelFile(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("trCd") TrCd trCd, Model model) {
		Result result = new Result();
		
		if (!file.isEmpty()) {
			if(file.getSize() > maxUploadSize){
		    	result.setResult(false);
		    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
		    }else{
				try {
					result = applicationMngService.uploadApplicationCodeExcelFile(file);
					
					/*result.setResult(resultCount > 0 ? true : false);
					result.setObject(resultCount);*/
					
				}catch (Exception ex) {
					ex.printStackTrace();
					result.setResult(false);
					result.setMessage(writeErrorMessage(ex.getMessage()));
				}		
		    }
	    }
		
		return result;		
	}
	
	private String writeErrorMessage(String message) {
		int lastIndex = message.lastIndexOf("ORA-");
		String errMessage = "엑셀에 입력한 값들을 확인해 주세요.";
		
		if(lastIndex != -1) {
			errMessage = message.substring(lastIndex);
		}
		
		return errMessage;
	}
	
	/* 사용자 관리 - 사용자 관리 - 승인 */
	@RequestMapping(value="/ApplicationCode/MultiDelete", method=RequestMethod.POST)
	@ResponseBody
	public Result deleteApplicationCodes(@ModelAttribute("trCd") TrCd trCd,  Model model) {
		Result result = new Result();
		try{
			
			if(trCd.getChk_tr_cd().split(",").length == trCd.getChk_wrkjob_cd().split(",").length){
				for(int i = 0; i < trCd.getChk_tr_cd().split(",").length; i++){
					trCd.setTr_cd(trCd.getChk_tr_cd().split(",")[i]);
					trCd.setWrkjob_cd(trCd.getChk_wrkjob_cd().split(",")[i]);
					applicationMngService.deleteApplicationCode(trCd);
					result.setResult(false);
				}
			}else{
				result.setResult(false);
				result.setMessage("indexOutOfException");
				return result;
			}
			
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
}
