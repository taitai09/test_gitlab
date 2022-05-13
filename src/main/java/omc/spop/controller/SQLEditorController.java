package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
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
import omc.spop.base.SessionManager;
import omc.spop.model.Result;
import omc.spop.model.SQLEditor;
import omc.spop.service.SQLEditorService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2019.03.23 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Controller
@RequestMapping(value = "/SQLEditor")
public class SQLEditorController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(SQLEditorController.class);
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private SQLEditorService sqlEditorService;

	/* SQL Editor */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String SQLEditor(@ModelAttribute("sqlEditor") SQLEditor sqlEditor, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", sqlEditor.getMenu_id());
		model.addAttribute("menu_nm", sqlEditor.getMenu_nm());
		
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		model.addAttribute("auth_cd", auth_cd);

		return "SQLEditor/SQLEditor";
	}
	
	@RequestMapping(value = "/Retrieve", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String retrieve(@ModelAttribute("sqlEditor") SQLEditor sqlEditor, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		
		try {
			resultList = sqlEditorService.retrieve(sqlEditor);
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/ExcelRetrieve", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public ModelAndView ExcelRetrieve(@ModelAttribute("sqlEditor") SQLEditor sqlEditor, Model model) {
		List<LinkedHashMap<String, Object>> resultList = null;
		Map<String, Object> headOptions = null;
		
		try {
			resultList = sqlEditorService.retrieve(sqlEditor);
			
			if(resultList != null){
				resultList.remove(resultList.size() - 1);
				headOptions = resultList.remove(resultList.size() - 1);
			}
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("fileName", "SQL_Editor");
		model.addAttribute("sheetName", "SQL_Editor");
		
		if(headOptions != null) {
			String head = headOptions.get("HEAD") + "";
			String[] headSplit = head.split("\\;");
			
			//동적 엑셀 그리드를 위한 엑셀 헤더값 전달.
			String[] headers = new String[headSplit.length];
			
			for(int i = 0; i < headSplit.length; i++){
				headers[i] = headSplit[i].split("\\/")[0];
			}
			
			model.addAttribute("excelHeaders", headers);
		}
		
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	@RequestMapping(value="/DisConn", method = RequestMethod.POST)
	@ResponseBody
	public Result disConn(@ModelAttribute("sqlEditor") SQLEditor sqlEditor, Model model) {
		Result result = new Result();
		try{
			result = sqlEditorService.disConn(sqlEditor);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/ExecuteQuery", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String executeQuery(@ModelAttribute("sqlEditor") SQLEditor sqlEditor, Model model) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = sqlEditorService.executeQuery(sqlEditor);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return getJSONResult(resultList, true).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/ExecuteUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Result executeUpdate(@ModelAttribute("sqlEditor") SQLEditor sqlEditor, Model model) {
		Result result = new Result();
		
		try {
			result = sqlEditorService.executeUpdate(sqlEditor);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/Execute", method = RequestMethod.POST)
	@ResponseBody
	public Result execute(@ModelAttribute("sqlEditor") SQLEditor sqlEditor, Model model) {
		Result result = new Result();
		
		try {
			result = sqlEditorService.execute(sqlEditor);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
}