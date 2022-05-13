package omc.spop.controller;

import java.lang.reflect.Method;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.DbCheckConfig;
import omc.spop.model.DbCheckException;
import omc.spop.model.DbCheckExceptionHeadTitle;
import omc.spop.model.Result;
import omc.spop.service.CommonService;
import omc.spop.service.ExceptionManagementService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2017.09.20 이원식 최초작성
 **********************************************************/

@Controller
@RequestMapping(value = "/ExceptionManagement")
public class ExceptionManagementController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionManagementController.class);

	@Autowired
	private ExceptionManagementService exceptionManagementService;

	@Autowired
	private CommonService commonService;

	/* 예외 관리 */
	@RequestMapping(value = "/ExceptionManagement", method = RequestMethod.GET)
	public String ExceptionManage(@ModelAttribute("dbCheckException") DbCheckException dbCheckException, Model model)
			throws Exception {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", dbCheckException.getMenu_id());
		model.addAttribute("menu_nm", dbCheckException.getMenu_nm());
		String db_name = StringUtils.defaultString(dbCheckException.getDb_name());
		String dbid = StringUtils.defaultString(dbCheckException.getDbid());
		String menu_id = StringUtils.defaultString(dbCheckException.getMenu_id());
		String menu_nm = StringUtils.defaultString(dbCheckException.getMenu_nm());
		if (!db_name.equals("") && dbid.equals("")) {
			dbid = commonService.getDbidByDbName(db_name);
		}
		model.addAttribute("dbid", dbid);

		return "preventiveCheck/exceptionManagement";
	}

	/* 점검항목 조회 */
	@RequestMapping(value = "/getCheckItem", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getCheckItem(@ModelAttribute("dbCheckConfig") DbCheckConfig dbCheckConfig) throws Exception {
		List<DbCheckConfig> checkItemList = exceptionManagementService.getCheckItemList(dbCheckConfig);
		return success(checkItemList).toJSONObject().get("rows").toString();
	}

	/* 점검항목 검색조건 콤보 데이터 조회 */
	@RequestMapping(value = "/getDbCheckExceptionHeadTitleList", produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDbCheckExceptionHeadTitleList(
			@ModelAttribute("dbCheckExceptionHeadTitle") DbCheckExceptionHeadTitle dbCheckExceptionHeadTitle)
			throws Exception {

		String check_pref_id = StringUtils.defaultString(dbCheckExceptionHeadTitle.getCheck_pref_id());

		if (check_pref_id.equals("")) {
			throw new Exception("점검항목이 선택되지 않았습니다.");
		}

		List<DbCheckExceptionHeadTitle> lst = new ArrayList<DbCheckExceptionHeadTitle>();
		try {
			DbCheckExceptionHeadTitle ci = exceptionManagementService
					.getDbCheckExceptionHeadTitleList(dbCheckExceptionHeadTitle);
			int cnt = Integer.parseInt(StringUtils.defaultString(ci.getCheck_except_key_cnt(), "0"));

			Method method = null;
			String methodName = "";
			for (int i = 0; i < cnt; i++) {
				methodName = "getCheck_except_head_title_nm_" + (i + 1);
				method = ci.getClass().getMethod(methodName);
				String check_except_head_title_nm = (String) method.invoke(ci);
				String check_except_head_title_cd = check_except_head_title_nm;

				DbCheckExceptionHeadTitle titleObj = new DbCheckExceptionHeadTitle();
				titleObj.setNo(String.valueOf(i + 1));
				titleObj.setCheck_except_head_title_cd(check_except_head_title_cd);
				titleObj.setCheck_except_head_title_nm(check_except_head_title_nm);
				lst.add(titleObj);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(lst).toJSONObject().get("rows").toString();
	}

	/* 예외관리 검색 목록 */
	@RequestMapping(value = "/getDbCheckExceptionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDbCheckExceptionList(@ModelAttribute("dbCheckException") DbCheckException dbCheckException,
			Model model) {
		List<DbCheckException> resultList = new ArrayList<DbCheckException>();

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		dbCheckException.setUser_id(user_id);
		int dataCount4NextBtn = 0;

		try {
			String check_except_object_index = StringUtils
					.defaultString(dbCheckException.getCheck_except_object_index());
			logger.debug("check_except_object_index :" + check_except_object_index);

			resultList = exceptionManagementService.getDbCheckExceptionList(dbCheckException);
			if (resultList != null && resultList.size() > dbCheckException.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(dbCheckException.getPagePerCount());
				/* 리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함 */
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* 예외관리 예외 삭제 */
	@RequestMapping(value = "/DeleteException", method = RequestMethod.POST)
	@ResponseBody
	public Result DeleteException(@ModelAttribute("dbCheckException") DbCheckException dbCheckException, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		dbCheckException.setUser_id(user_id);

		Result result = new Result();

		try {
			int updateResult = exceptionManagementService.updateExceptDelYn(dbCheckException);
			if (updateResult > 0) {
				result.setResult(true);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
		}
		return result;
	}

	/* 예외등록 */
	@RequestMapping(value = "/RegistException", method = RequestMethod.POST)
	@ResponseBody
	public Result RegistException(@ModelAttribute("dbCheckException") DbCheckException dbCheckException, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		dbCheckException.setUser_id(user_id);

		Result result = new Result();

		try {
			int insertResult = exceptionManagementService.registException(dbCheckException);
			if (insertResult > 0) {
				result.setResult(true);
				result.setMessage("예외등록되었습니다.");
			} else {
				result.setResult(true);
				result.setMessage("이미 예외등록되어 있습니다.");
				result.setMessage("예외등록되었습니다.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage("예외 등록중 오류가 발생했습니다. 관리자에게 문의하세요!");
		}
		return result;
	}

	/**
	 * 예외관리 엑셀 다운로드
	 * 
	 * @param req
	 * @param res
	 * @param dbCheckException
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getDbCheckExceptionList/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	// @ResponseBody
	public ModelAndView getDbCheckExceptionListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("dbCheckException") DbCheckException dbCheckException,
			@ModelAttribute("dbCheckExceptionHeadTitle") DbCheckExceptionHeadTitle dbCheckExceptionHeadTitle,
			Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		dbCheckException.setUser_id(user_id);

		String check_pref_id = StringUtils.defaultString(dbCheckException.getCheck_pref_id());

		if (check_pref_id.equals("")) {
			throw new Exception("점검항목이 선택되지 않았습니다.");
		}

		int check_except_key_cnt = 0;
		String excelHeaders[] = null;

		try {
			resultList = exceptionManagementService.getDbCheckExceptionList4Excel(dbCheckException);

			DbCheckExceptionHeadTitle ci = exceptionManagementService
					.getDbCheckExceptionHeadTitleList(dbCheckExceptionHeadTitle);
			check_except_key_cnt = Integer.parseInt(StringUtils.defaultString(ci.getCheck_except_key_cnt(), "0"));

			for (LinkedHashMap<String, Object> map : resultList) {
				for (int i = check_except_key_cnt + 1; i <= 10; i++) {
					map.remove("CHECK_EXCEPT_OBJECT_NAME_" + i);
				}
			}

			logger.debug("resultList :" + resultList);
			excelHeaders = new String[check_except_key_cnt + 2];

			Method method = null;
			String methodName = "";
			for (int i = 0; i < check_except_key_cnt; i++) {
				methodName = "getCheck_except_head_title_nm_" + (i + 1);
				method = ci.getClass().getMethod(methodName);
				String check_except_head_title_nm = (String) method.invoke(ci);
				excelHeaders[i] = check_except_head_title_nm;
				logger.debug("check_except_head_title_nm :" + check_except_head_title_nm);
			}
			excelHeaders[check_except_key_cnt] = "예외등록일시";
			excelHeaders[check_except_key_cnt + 1] = "예외등록자";

		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "예외_관리");
		model.addAttribute("sheetName", "예외_관리");

		logger.debug("excelHeaders :" + excelHeaders);

		model.addAttribute("excelHeaders", excelHeaders);
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

}