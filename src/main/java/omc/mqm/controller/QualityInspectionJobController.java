package omc.mqm.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.Result;
import omc.spop.model.Users;
import omc.spop.utils.DateUtil;
import omc.spop.utils.JSONResult.Jsonable;
import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.OpenqErrCnt;
import omc.mqm.model.QualityStdInfo;
import omc.mqm.service.QualityInspectionJobService;

/***********************************************************
 * 2019.05.20	임승률	최초작성
 **********************************************************/

@Controller
@RequestMapping(value="/Mqm")
public class QualityInspectionJobController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(QualityInspectionJobController.class);
	
	@Autowired
	private QualityInspectionJobService qualityInspectionJobService;
	
	/* 품질 검토 작업 */
	@RequestMapping(value="/QualityInspectionJob", method=RequestMethod.GET)
	public String qualityInspectionJob(@ModelAttribute("openmQaindi") OpenmQaindi openmQaindi, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", openmQaindi.getMenu_id() );
		model.addAttribute("menu_nm", openmQaindi.getMenu_nm() );
		return "mqm/qualityInspectionJob";
		
	}	
	
	/* 품질 검토 작업 action */
	@RequestMapping(value="/QualityInspectionJob", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String qualityInspectionJobAction(@ModelAttribute("openmQaindi") OpenmQaindi openmQaindi,  Model model) {
		List<OpenmQaindi> resultList = new ArrayList<OpenmQaindi>();
		try{
//			resultList = qualityInspectionJobService.selectQualityInspectionJobList(openmQaindi);
			resultList = qualityInspectionJobService.getQualityInspectionJob(openmQaindi);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();		
	}
	
	/* 품질 검토 작업 - 모델정보수집 */
	@RequestMapping(value="/QualityInspectionJob/ModelCollecting", method=RequestMethod.POST)
	@ResponseBody
	public Result qualityInspectionJobModelCollectingAction(@ModelAttribute("openmQaindi") OpenmQaindi openmQaindi,  Model model) {
		Result result = new Result();
		try{
			logger.debug("openmQaindi getQty_chk_idt_cd : " + openmQaindi.getQty_chk_idt_cd());
			logger.debug("openmQaindi getQty_chk_result_tbl_nm : " + openmQaindi.getQty_chk_result_tbl_nm());
			qualityInspectionJobService.qualityInspectionJobModelCollectingAction(openmQaindi);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	/* 품질 검토 작업 - save(품질검토작업) */
	@RequestMapping(value="/QualityInspectionJob/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result saveQualityInspectionJobAction(@ModelAttribute("openmQaindi") OpenmQaindi openmQaindi,  Model model) {
		Result result = new Result();
		try{
			logger.debug("openmQaindi getQty_chk_idt_cd : " + openmQaindi.getQty_chk_idt_cd());
			logger.debug("openmQaindi getQty_chk_result_tbl_nm : " + openmQaindi.getQty_chk_result_tbl_nm());
			qualityInspectionJobService.saveQualityInspectionJob(openmQaindi);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	/* 품질 검토 작업  - 엑셀다운 */
	@RequestMapping(value = "/QualityInspectionJob/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView qualityInspectionJobByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("openmQaindi") OpenmQaindi openmQaindi, Model model)
					throws Exception {
		
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
//			resultList = qualityInspectionJobService.selectQualityInspectionJobListByExcelDown(openmQaindi);
			resultList = qualityInspectionJobService.getQualityInspectionJobByExcelDown(openmQaindi);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "구조_품질검토_작업");
		model.addAttribute("sheetName", "구조_품질검토_작업");
		model.addAttribute("excelId", "OPENM_INSPECTION_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	/* 구조 품질 집계표 */
	@RequestMapping(value="/QualityInspectionJobSheet", method=RequestMethod.GET)
	public String qualityInspectionJobSheet(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", openmBizCls.getMenu_id() );
		model.addAttribute("menu_nm", openmBizCls.getMenu_nm() );
		return "mqm/qualityInspectionJobSheet";
		
	}
	/* 구조 품질 집계표 action */
	@RequestMapping(value="/QualityInspectionJobSheet", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String qualityInspectionJobSheetAction(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls,  Model model) {
		//List<OpenmQaindi> resultList = new ArrayList<OpenmQaindi>();
		
		
		logger.debug("lib_nm::::"+openmBizCls.getLib_nm());
		logger.debug("model_nm::::"+openmBizCls.getModel_nm());
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try{
			resultList = qualityInspectionJobService.selectQualityInspectionJobSheetList(openmBizCls);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		//return success((List<? extends Jsonable>) resultList).toJSONObject().toString();
		//return success((List<? extends Jsonable>) resultList).toJSONObject().toString();	
		return getJSONResult(resultList).toJSONObject().toString();
	}
	/* 구조 품질 집계표 - Grid 타이틀  조회 */
	@RequestMapping(value = "/QualityInspectionJobSheet/GetQualityInspectionJobSheetHeadTitleList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String qetQualityInspectionJobSheetHeadTitleList(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls) throws Exception {
		List<OpenmQaindi> resultList = new ArrayList<OpenmQaindi>();
		
		try{
			resultList = qualityInspectionJobService.getQualityInspectionJobSheetHeadTitleList(openmBizCls);
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();		
	}
	/* 구조 품질 집계표 - 라이브러리명 콤보박스 */
	@RequestMapping(value = "/GetOpenqErrCntLibNm", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String qetOpenqErrCntLibNm(@ModelAttribute("openmQErrCnt") OpenqErrCnt openqErrCnt) throws Exception {
		List<OpenqErrCnt> libNmList = new ArrayList<OpenqErrCnt>();
		List<OpenqErrCnt> openqErrCntList = qualityInspectionJobService.getOpenqErrCntLibNmList(openqErrCnt);
		OpenqErrCnt libNm = new OpenqErrCnt();
		libNm.setLib_cd("");
		libNm.setLib_nm("전체");

		libNmList.add(libNm);
		libNmList.addAll(openqErrCntList);

		return success(libNmList).toJSONObject().get("rows").toString();
	}
	/* 구조 품질 집계표 - 모델명 콤보박스 */
	@RequestMapping(value = "/GetOpenqErrCntModelNm", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String qetOpenqErrCntModelNm(@ModelAttribute("openmQErrCnt") OpenqErrCnt openqErrCnt) throws Exception {
		List<OpenqErrCnt> modelNmList = new ArrayList<OpenqErrCnt>();
		List<OpenqErrCnt> openqErrCntList = new ArrayList<OpenqErrCnt>();
		if(openqErrCnt.getLib_nm() != null && ! openqErrCnt.getLib_nm().equals("")) {
			openqErrCntList = qualityInspectionJobService.getOpenqErrCntModelNmList(openqErrCnt);
		}
		// List<OpenqErrCnt> openqErrCntList = qualityInspectionJobService.getOpenqErrCntModelNmList(openqErrCnt);
		OpenqErrCnt modelNm = new OpenqErrCnt();
		modelNm.setModel_cd("");
		modelNm.setModel_nm("전체");

		modelNmList.add(modelNm);
		if (openqErrCntList != null) modelNmList.addAll(openqErrCntList);

		return success(modelNmList).toJSONObject().get("rows").toString();
	}
	/* 품질 검토 작업  - 엑셀다운 */
	@RequestMapping(value = "/QualityInspectionJobSheet/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public void qualityInspectionJobSheetByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("openqErrCnt") OpenqErrCnt openqErrCnt, Model model)
					throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		String fileName = "";
		String FilePath = "";
		File file = null;
		
		try {
			result = qualityInspectionJobService.selectQualityInspectionJobSheetListByExcelDown(openqErrCnt);
			logger.debug("result : " + result);
			if (result == null) throw new Exception("다운로드 파일정보를 찾을 수 없습니다.");
			
			fileName = result.get("FILE").toString();
			logger.debug("fileName : " + fileName);
			FilePath = result.get("PATH").toString();
			logger.debug("FilePath : " + FilePath);
			
			file = new File(FilePath + fileName);
	        if(!file.exists()){
	            String errorMessage = "파일을 찾을 수 없습니다.";
	            logger.debug(errorMessage);
	            OutputStream outputStream = res.getOutputStream();
	            outputStream.write(errorMessage.getBytes("UTF-8"));
	            outputStream.close();
	            return;
	        }
	        
	        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
	        if(mimeType==null){
	            logger.debug("mimetype is not detectable, will take default");
	            mimeType = "application/octet-stream";
	        }
	        
	        logger.debug("mimetype : "+mimeType);
	        //res.setContentType(mimeType);
	        String sFileName = URLEncoder.encode(file.getName(),"UTF-8");
	        //res.setHeader("Content-Disposition", String.format("inline; filename=\"" + sFileName +"\""));
	        
	        //res.setContentLength((int)file.length());
	        
			res.setHeader("Pragma", "no-cache;");
			res.setHeader("Expires", "-1;");
			res.setHeader("Content-Transfer-Encoding", "binary;");
			res.setContentType("application/smnet; charset=utf-8");		
			res.setHeader("Content-Disposition", "attachment; filename="+sFileName+";");
			logger.debug("getExtrac_dt() : "+ openqErrCnt.getExtrac_dt());
			Cookie cookie = new Cookie("excelDownToken" + openqErrCnt.getExtrac_dt(), "TRUE");
			res.addCookie(cookie);
	        
	        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	        
	        //Copy bytes from source to destination(outputstream in this example), closes both streams.
	        FileCopyUtils.copy(inputStream, res.getOutputStream());
        

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		// return a view which will be resolved by an excel view resolver
	}
}