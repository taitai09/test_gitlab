package omc.mqm.controller;

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

import omc.mqm.model.ModelEntityType;
import omc.mqm.service.ModelEntityTypeMngService;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Result;
import omc.spop.model.Users;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class ModelEntityTypeMngController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ModelEntityTypeMngController.class);

	@Autowired
	private ModelEntityTypeMngService modelEntityTypeMngService;

	@RequestMapping(value = "/Mqm/ModelEntityTypeManagement", method = RequestMethod.GET)
	public String ModelEntityTypeMng(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();

		model.addAttribute("menu_id", modelEntityType.getMenu_id());
		model.addAttribute("menu_nm", modelEntityType.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "mqm/modelEntityTypeManagement";
	}

	/* 품질점검 - DB 구조 품질 점검 - 프로젝트 관리 - 리스트 */
	@RequestMapping(value = "/ModelEntityTypeList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ModelEntityTypeList(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType, Model model) {
		List<ModelEntityType> resultList = new ArrayList<ModelEntityType>();
		int dataCount4NextBtn = 0;

		try {
			logger.debug("modelEntityType :" + modelEntityType);

			resultList = modelEntityTypeMngService.getModelEntityTypeList(modelEntityType);
			if (resultList != null && resultList.size() > modelEntityType.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(modelEntityType.getPagePerCount());
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		// return success(resultList).toJSONObject().toString();
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	/* 품질점검 - DB 구조 품질 점검 - 모델 엔터티 유형 관리 - 저장 */
	@RequestMapping(value = "/ModelEntityType/Save", method = RequestMethod.POST)
	@ResponseBody
	public Result ModelEntityTypeSave(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType, Model model) {
		Result result = new Result();
		int check = 0;

		try {
			String crudFlag = modelEntityType.getCrud_flag();
			if (crudFlag.equals("C")) {
				int dupCnt = modelEntityTypeMngService.getDupCnt(modelEntityType);
				if (dupCnt > 0) {
					result.setMessage("이미 등록된 데이터입니다.");
					result.setResult(false);
				} else {
					check = modelEntityTypeMngService.insertModelEntityType(modelEntityType);
					if (check == 0) {
						result.setMessage("등록중 오류가 발생하였습니다.");
						result.setResult(false);
					} else {
						result.setMessage("등록하였습니다.");
						result.setResult(true);
					}
				}
			} else if (crudFlag.equals("U")) {
				check = modelEntityTypeMngService.updateModelEntityType(modelEntityType);
				if (check == 0) {
					result.setMessage("수정된 데이터가 없습니다.");
					result.setResult(false);
				} else {
					result.setMessage("수정하였습니다.");
					result.setResult(true);
				}
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	/* 품질점검 - DB 구조 품질 점검 - 모델 엔터티 유형 관리 - 저장 */
	@RequestMapping(value = "/ModelEntityType/Insert", method = RequestMethod.POST)
	@ResponseBody
	public Result ModelEntityTypeInsert(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType,
			Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = modelEntityTypeMngService.insertModelEntityType(modelEntityType);
			if (check == 0) {
				result.setResult(false);
				result.setMessage("등록중 오류가 발생하였습니다.");
			}else {
				result.setResult(true);
				result.setMessage("등록하였습니다.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	/* 품질점검 - DB 구조 품질 점검 - 모델 엔터티 유형 관리 - 수정 */
	@RequestMapping(value = "/ModelEntityType/Update", method = RequestMethod.POST)
	@ResponseBody
	public Result ModelEntityTypeUpdate(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType,
			Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = modelEntityTypeMngService.updateModelEntityType(modelEntityType);
			if (check == 0) {
				result.setResult(false);
				result.setMessage("수정중 오류가 발생하였습니다.");
			}else {
				result.setResult(true);
				result.setMessage("수정하였습니다.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	/* 품질점검 - DB 구조 품질 점검 - 모델 엔터티 유형 관리 - 삭제 */
	@RequestMapping(value = "/ModelEntityType/Delete", method = RequestMethod.POST)
	@ResponseBody
	public Result ModelEntityTypeDelete(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType,
			Model model) {
		Result result = new Result();
		int check = 0;

		try {
			check = modelEntityTypeMngService.deleteModelEntityType(modelEntityType);
			if (check == 0) {
				result.setResult(false);
				result.setMessage("삭제중 오류가 발생했습니다.");
			}else {
				result.setResult(true);
				result.setMessage("삭제하였습니다.");
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}

	/* 엑셀 다운로드 */
	@RequestMapping(value = "/ModelEntityType/excelDownload", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView modelEntityTypeExcelDownload(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("modelEntityType") ModelEntityType modelEntityType, Model model) throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = modelEntityTypeMngService.excelDownload(modelEntityType);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "모델_엔터티_유형_관리");
		model.addAttribute("sheetName", "모델_엔터티_유형_관리");
		model.addAttribute("excelId", "MODEL_ENTITY_TYPE_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

}
