package omc.mqm.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.OpenmQtyChkSql;
import omc.mqm.model.QualityStdInfo;
import omc.mqm.service.QualityStdInfoService;
import omc.spop.base.InterfaceController;
import omc.spop.model.Result;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2019.05.22	임호경	최초작성
 **********************************************************/

@RequestMapping(value="/Mqm")
@Controller
public class QualityStdInfoController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(QualityStdInfoController.class);
	
	@Autowired
	private QualityStdInfoService qualityStdInfoService;

	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	private static final String ROUTE ="/excelUploadForm/";	
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  엔터티유형관리 */
	@RequestMapping(value="/EntityManagement", method=RequestMethod.GET)
	public String EntityManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", qualityStdInfo.getMenu_id() );
		model.addAttribute("menu_nm", qualityStdInfo.getMenu_nm() );

		return "mqm/qualityStdInfo/entityManagement";
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  엔터티유형관리 - 리스트 */
	@RequestMapping(value="/EntityManagement", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GetTbOpenmEntType(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		List<QualityStdInfo> resultList = new ArrayList<QualityStdInfo>();
		int dataCount4NextBtn = 0;

		try{
			resultList = qualityStdInfoService.getTbOpenmEntType(qualityStdInfo);
			if(resultList != null && resultList.size() > qualityStdInfo.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(qualityStdInfo.getPagePerCount());
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
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  엔터티유형관리 - 저장 */
	@RequestMapping(value="/EntityManagement/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveTbOpenmEntType(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo,  Model model) {
		
		
		Result result = new Result();
		try{
			int check = qualityStdInfoService.saveTbOpenmEntType(qualityStdInfo);
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  엔터티유형관리 - 삭제 */
	@RequestMapping(value="/EntityManagement/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeletebOpenmEntType(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo,  Model model) {
		
		
		Result result = new Result();
		try{
			int check = qualityStdInfoService.deleteTbOpenmEntType(qualityStdInfo);
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  엔터티유형관리 - 엑셀다운 */
	@RequestMapping(value = "/EntityManagement/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView EntityManagementByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = qualityStdInfoService.getTbOpenmEntTypeByExcelDown(qualityStdInfo);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "엔터티_유형_관리");
		model.addAttribute("sheetName", "엔터티_유형_관리");
		model.addAttribute("excelId", "TB_OPENM_ENT_TYPE");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	
	/* 구조품질 관리 - 품질 기준정보 설정 - 품질점검 지표 관리 */
	@RequestMapping(value="/QualityCheckManagement", method=RequestMethod.GET)
	public String QualityCheckManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", qualityStdInfo.getMenu_id() );
		model.addAttribute("menu_nm", qualityStdInfo.getMenu_nm() );

		return "mqm/qualityStdInfo/qualityCheckManagement";
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 - 품질점검 지표 관리 - 리스트*/
	@RequestMapping(value="/QualityCheckManagement", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GetQualityCheckManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		List<QualityStdInfo> resultList = new ArrayList<QualityStdInfo>();
		int dataCount4NextBtn = 0;

		try{
			resultList = qualityStdInfoService.getQualityCheckManagement(qualityStdInfo);
			if(resultList != null && resultList.size() > qualityStdInfo.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(qualityStdInfo.getPagePerCount());
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
	/* 구조품질 관리 - 품질 기준정보 설정 -  품질점검 지표 관리 - 저장 */
	@RequestMapping(value="/QualityCheckManagement/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveQualityCheckManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo,  Model model) {
		
		
		Result result = new Result();
		try{
			int check = qualityStdInfoService.saveQualityCheckManagement(qualityStdInfo);
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  품질점검 지표 관리 - 삭제 */
	@RequestMapping(value="/QualityCheckManagement/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteQualityCheckManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo,  Model model) {
		
		
		Result result = new Result();
		try{
			int check = qualityStdInfoService.deleteQualityCheckManagement(qualityStdInfo);
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  품질점검 지표 관리 - 엑셀다운 */
	@RequestMapping(value = "/QualityCheckManagement/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView QualityCheckManagementByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = qualityStdInfoService.getQualityCheckManagementByExcelDown(qualityStdInfo);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "품질점검_지표_관리");
		model.addAttribute("sheetName", "품질점검_지표_관리");
		model.addAttribute("excelId", "TB_OPENM_QAINDI");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	
	
	/* 구조품질 관리 - 품질 기준정보 설정 - 품질검토 예외 대상 관리 */
	@RequestMapping(value="/QualityRevExcManagement", method=RequestMethod.GET)
	public String QualityReviewExclusionManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", qualityStdInfo.getMenu_id() );
		model.addAttribute("menu_nm", qualityStdInfo.getMenu_nm() );
		
		return "mqm/qualityStdInfo/qualityRevExcManagement";
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 - 품질검토 예외 대상 관리 - 리스트*/
	@RequestMapping(value="/QualityRevExcManagement", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String GetQualityReviewExclusionManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		List<QualityStdInfo> resultList = new ArrayList<QualityStdInfo>();
		int dataCount4NextBtn = 0;

		
		try{
			resultList = qualityStdInfoService.getQualityRevExcManagement(qualityStdInfo);
			if(resultList != null && resultList.size() > qualityStdInfo.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(qualityStdInfo.getPagePerCount());
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
	/* 구조품질 관리 - 품질 기준정보 설정 -  품질검토 예외 대상 관리 - 저장 */
	@RequestMapping(value="/QualityRevExcManagement/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveQualityReviewExclusionManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo,  Model model) {
		
		
		Result result = new Result();
		try{
			int check = qualityStdInfoService.saveQualityRevExcManagement(qualityStdInfo);
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  품질검토 예외 대상 관리 - 삭제 */
	@RequestMapping(value="/QualityRevExcManagement/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteQualityReviewExclusionManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo,  Model model) {
		
		
		Result result = new Result();
		try{
			int check = qualityStdInfoService.deleteQualityRevExcManagement(qualityStdInfo);
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	/* 구조품질 관리 - 품질 기준정보 설정 -  품질검토 예외 대상 관리 - 엑셀다운 */
	@RequestMapping(value = "/QualityRevExcManagement/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView QualityReviewExclusionManagementByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model)
					throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = qualityStdInfoService.getQualityRevExcManagementByExcelDown(qualityStdInfo);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "품질검토_예외_대상_관리");
		model.addAttribute("sheetName", "품질검토_예외_대상_관리");
		model.addAttribute("excelId", "TB_OPENQ_EXCEPT_OBJ");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 공통코드 조회 */
	@RequestMapping(value = "/QualityRevExcManagement/{SelectCombobox}", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCommonCode(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, 
			@PathVariable("SelectCombobox")String selectCombo, Model model) throws Exception {
		System.out.println("SELECTCOMBO::::::"+selectCombo);
		String isAll = StringUtils.defaultString(qualityStdInfo.getIsAll());
		String isChoice = StringUtils.defaultString(qualityStdInfo.getIsChoice());
		List<QualityStdInfo> resultList = new ArrayList<QualityStdInfo>();
		
		QualityStdInfo temp = new QualityStdInfo();
		if (isAll.equals("Y")) {
			temp.setCd("");
			temp.setCd_nm("전체");
			resultList.add(temp);
		} else if (isChoice.equals("Y")) {
			temp.setCd("");
			temp.setCd_nm("선택");
			resultList.add(temp);
		}

		try {
			List<QualityStdInfo> tempList = qualityStdInfoService.selectCombobox(qualityStdInfo, selectCombo);
			resultList.addAll(tempList);
		} catch (Exception ex) {
			logger.error("resultList Error ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}	


	@RequestMapping(value="/ExcelFormDownload/{name}", method = {RequestMethod.POST})
    public void downloadFile(HttpServletResponse response, @PathVariable("name") String excelName) throws IOException {
    
    	excelName = ROUTE + excelName +".xlsx";
    	
        File file = null;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        logger.debug("current directory:"+classloader.getResource(".").getPath());
        System.out.println("download directory::::::"+classloader.getResource(excelName).getFile().toString());
        System.out.println("dir::::::"+classloader.getClass().getName().toString());
        file = new File(classloader.getResource(excelName).getFile());   

        
        if(!file.exists()){
            String errorMessage = "파일을 찾을 수 없습니다.";
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
    
	/* 품질검토 예외 대상 관리 엑셀파일 업로드 */
	@RequestMapping(value = "/QualityRevExcManagement/ExcelUpload", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result UploadExcelFile(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		Result result = new Result();
		
		if (!file.isEmpty()) {
			if(file.getSize() > maxUploadSize){
		    	result.setResult(false);
		    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
		    }else{
				try {
					result = qualityStdInfoService.qualityRevExcMngByExcelUpload(file);
					
				}catch (Exception ex) {
					ex.printStackTrace();
					result.setResult(false);
					/*result.setMessage(ex.getMessage());*/
				}		
		    }
	    }
		
		return result;		
	}
	
	/* 품질기준정보 - 업무분류체계 관리 */
	@RequestMapping(value="/BusinessClassMng", method=RequestMethod.GET)
	public String BusinessClassMng(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", openmBizCls.getMenu_id() );
		model.addAttribute("menu_nm", openmBizCls.getMenu_nm() );
		
		return "mqm/qualityStdInfo/businessClassMng";
	}	
	
	/* 품질기준정보 - 업무분류체계 관리 action */
	@RequestMapping(value="/BusinessClassMng", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String BusinessClassMngAction(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls,  Model model) {
		List<OpenmBizCls> resultList = new ArrayList<OpenmBizCls>();
		int dataCount4NextBtn = 0;
		try{
			resultList = qualityStdInfoService.businessClassMngList(openmBizCls);
			if(resultList != null && resultList.size() > openmBizCls.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(openmBizCls.getPagePerCount());
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
	
	
	/*품질기준정보 - 업무분류체계 관리 엑셀 다운*/
	@RequestMapping(value = "/BusinessClassMng/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView BusinessClassMngListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls, Model model)
			throws Exception {
		logger.debug("BusinessClassMngListByExcelDown Call");
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = qualityStdInfoService.businessClassMngListByExcelDown(openmBizCls);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "업무분류체계_관리");
		model.addAttribute("sheetName", "업무분류체계_관리");
		model.addAttribute("excelId", "OPENM_BIZ_CLS_MNG");
		// return a view which will be resolved by an excel view resolver
		logger.debug("BusinessClassMngListByExcelDown resultList", resultList);
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/* 품질기준정보 - 업무분류체계 관리 save */
	@RequestMapping(value="/BusinessClassMng/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveBusinessClassMngAction(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls,  Model model) {
		Result result = new Result();
		try{
			logger.info("openmBizCls getLib_nm : " + openmBizCls.getLib_nm());
			logger.info("openmBizCls getModel_nm : " + openmBizCls.getModel_nm());
			qualityStdInfoService.saveBusinessClassMng(openmBizCls);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 품질기준정보 관리 - 업무분류체계 관리 delete */
	@RequestMapping(value="/BusinessClassMng/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteBusinessClassMngAction(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls,  Model model) {
		Result result = new Result();
		try{
			int deleteResult = qualityStdInfoService.deleteBusinessClassMng(openmBizCls);
			if(deleteResult > 0){
				result.setResult(true);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	/* 품질기준정보 관리 - 엑셀파일 업로드*/
	@RequestMapping(value = "/BusinessClassMng/ExcelUpload", method = RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result UploadExcelFile2(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("openmBizCls") OpenmBizCls openmBizCls, Model model) {
		Result result = new Result();
		
		if (!file.isEmpty()) {
			if(file.getSize() > maxUploadSize){
		    	result.setResult(false);
		    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
		    }else{
				try {
					result = qualityStdInfoService.businessClassMngByExcelUpload(file);
					
				}catch (Exception ex) {
					ex.printStackTrace();
					result.setResult(false);
					/*result.setMessage(ex.getMessage());*/
				}		
		    }
	    }
		
		return result;		
	}
	
	
	/* 품질기준정보 - 품질점검 SQL 관리 */
	@RequestMapping(value="/QualityCheckSql", method=RequestMethod.GET)
	public String QualityCheckSql(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", openmQtyChkSql.getMenu_id() );
		model.addAttribute("menu_nm", openmQtyChkSql.getMenu_nm() );
		
		return "mqm/qualityStdInfo/qualityCheckSql";
	}	
	
	/* 품질기준정보 - 품질점검 SQL 관리 action */
	@RequestMapping(value="/QualityCheckSql", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String QualityCheckSqlAction(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql,  Model model) {
		List<OpenmQtyChkSql> resultList = new ArrayList<OpenmQtyChkSql>();
		int dataCount4NextBtn = 0;
		try{
			resultList = qualityStdInfoService.qualityCheckSqlList(openmQtyChkSql);
			if(resultList != null && resultList.size() > openmQtyChkSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(openmQtyChkSql.getPagePerCount());
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
	
	/* 품질기준정보 - 품질지표기준  조회 */
	@RequestMapping(value = "/getQtyChkIdtCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getQtyChkIdtCd(@ModelAttribute("openmQaindi") OpenmQaindi openmQaindi) throws Exception {
		List<OpenmQaindi> qaindiList = new ArrayList<OpenmQaindi>();
		List<OpenmQaindi> openmQaindiList = qualityStdInfoService.openmQaindiList(openmQaindi);
		OpenmQaindi qaindi = new OpenmQaindi();
		qaindi.setQty_chk_idt_cd("");
		qaindi.setQty_chk_idt_cd_nm("전체");

		qaindiList.add(qaindi);
		qaindiList.addAll(openmQaindiList);

		return success(qaindiList).toJSONObject().get("rows").toString();
	}
	
	/* 품질기준정보 - 품질점검 SQL 관리 save */
	@RequestMapping(value="/QualityCheckSql/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveQualityCheckSqlAction(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql,  Model model) {
		Result result = new Result();
		try{
			qualityStdInfoService.saveQualityCheckSql(openmQtyChkSql);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 품질기준정보 관리 - 품질점검 SQL 관리 delete */
	@RequestMapping(value="/QualityCheckSql/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteQualityCheckSqlAction(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql,  Model model) {
		Result result = new Result();
		try{
			int deleteResult = qualityStdInfoService.deleteQualityCheckSql(openmQtyChkSql);
			if(deleteResult > 0){
				result.setResult(true);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/*품질기준정보 - 품질점검 SQL 관리 엑셀 다운*/
	@RequestMapping(value = "/QualityCheckSql/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView QualityCheckSqlListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql, Model model)
			throws Exception {
		logger.debug("QualityCheckSqlListByExcelDown Call");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			resultList = qualityStdInfoService.qualityCheckSqlListByExcelDown(openmQtyChkSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "품질점검RULE_관리");
		model.addAttribute("sheetName", "품질점검RULE_관리");
		model.addAttribute("excelId", "OPENM_QTY_CHK_SQL_MNG");
		// return a view which will be resolved by an excel view resolver
		logger.debug("QualityCheckSqlListByExcelDown resultList", resultList);
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	
	
	/* DB 구조 품질 점검 - 프로젝트 구조 품질점검 지표/RULE 관리 */
	@RequestMapping(value="/ProjectQualityCheckRuleMng", method=RequestMethod.GET)
	public String ProjectQualityCheckRuleMng(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", openmQtyChkSql.getMenu_id() );
		model.addAttribute("menu_nm", openmQtyChkSql.getMenu_nm() );
		
		return "mqm/qualityStdInfo/projectQualityCheckRuleMng";
	}	

	/* DB 구조 품질 점검 - 프로젝트 구조 품질점검 지표/RULE 관리 List  */
	@RequestMapping(value="/ProjectQualityCheckRuleMng", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ProjectQualityCheckRuleMngAction(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql,  Model model) {
		List<OpenmQtyChkSql> resultList = new ArrayList<OpenmQtyChkSql>();
		int dataCount4NextBtn = 0;
		try{
			resultList = qualityStdInfoService.projectQualityCheckRuleMngList(openmQtyChkSql);
			if(resultList != null && resultList.size() > openmQtyChkSql.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(openmQtyChkSql.getPagePerCount());
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
	
	
	/* DB 구조 품질 점검 - 프로젝트 구조 품질점검 지표/RULE 저장  */
	@RequestMapping(value="/ProjectQualityCheckRuleMng/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result ProjectQualityCheckRuleMngSave(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql,  
			@RequestParam(value="switchCode", required = true)String switchCode, Model model) {
		Result result = new Result();
		int check = 0;
		try{
			check = qualityStdInfoService.saveProjectQualityCheckRuleMng(openmQtyChkSql, switchCode);
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	/* DB 구조 품질 점검 - 프로젝트 구조 품질점검 지표/RULE 관리 List 엑셀 다운  */
	@RequestMapping(value = "/ProjectQualityCheckRuleMng/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView ProjectQualityCheckRuleMngByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql, Model model)
			throws Exception {
		logger.debug("ProjectQualityCheckRuleMngByExcelDown Call");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			resultList = qualityStdInfoService.ProjectQualityCheckRuleMngByExcelDown(openmQtyChkSql);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "프로젝트_구조 품질점검_지표RULE_관리");
		model.addAttribute("sheetName", "프로젝트_구조 품질점검_지표RULE_관리");
		model.addAttribute("excelId", "TB_OPENM_QTY_CHK_SQL");
		// return a view which will be resolved by an excel view resolver
		logger.debug("ProjectQualityCheckRuleMngByExcelDown resultList", resultList);
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* DB 구조 품질 점검 - 프로젝트 구조 품질점검 지표/RULE - 프로젝트 표준 적용 */
	@RequestMapping(value="/ProjectQualityCheckRuleMng/Apply", method=RequestMethod.POST)
	@ResponseBody
	public Result ProjectQualityCheckRuleMngApply(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql, Model model) {
		Result result = new Result();
		int check = 0;
		try{
			check = qualityStdInfoService.applyProjectQualityCheckRuleMng(openmQtyChkSql);
			result.setResult(check > 0 ? true : false);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
}