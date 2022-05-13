package omc.mqm.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.mqm.model.ModelEntityType;
import omc.mqm.service.MqmCommonService;
import omc.spop.base.InterfaceController;
import omc.spop.model.Cd;

/***********************************************************
 * 2018.08.21
 **********************************************************/

@Controller
public class MqmCommonController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ModelEntityTypeMngController.class);

	@Autowired
	private MqmCommonService mqmCommonService;

	/* 라이브러리명 조회 */
	@RequestMapping(value = "/Common/getAllLibNm", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAllLibNm(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType) throws Exception {
		String isAll = StringUtils.defaultString(modelEntityType.getIsAll());
		String isChoice = StringUtils.defaultString(modelEntityType.getIsChoice());
		List<Cd> retMdlLst = new ArrayList<Cd>();
		List<Cd> mdlLst = mqmCommonService.getAllLibNm(modelEntityType);
		Cd mdl = new Cd();
		mdl.setCd("");
		if (isAll.equals("Y")) {
			mdl.setCd_nm("전체");
		} else if (isChoice.equals("Y")) {
			mdl.setCd_nm("선택");
		} else {
			mdl.setCd_nm("");
		}
		retMdlLst.add(mdl);
		retMdlLst.addAll(mdlLst);

		logger.debug(success(retMdlLst).toJSONObject().get("rows").toString());
		return success(retMdlLst).toJSONObject().get("rows").toString();
	}

	/* 모델명 조회 */
	@RequestMapping(value = "/Common/getAllModelNm", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAllModelNm(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType) throws Exception {
		String isAll = StringUtils.defaultString(modelEntityType.getIsAll());
		String isChoice = StringUtils.defaultString(modelEntityType.getIsChoice());
		List<Cd> retMdlLst = new ArrayList<Cd>();
		List<Cd> mdlLst = mqmCommonService.getAllModelNm(modelEntityType);
		Cd mdl = new Cd();
		mdl.setCd("");
		if (isAll.equals("Y")) {
			mdl.setCd_nm("전체");
		} else if (isChoice.equals("Y")) {
			mdl.setCd_nm("선택");
		} else {
			mdl.setCd_nm("");
		}
		retMdlLst.add(mdl);
		retMdlLst.addAll(mdlLst);
		logger.debug(success(retMdlLst).toJSONObject().get("rows").toString());

		return success(retMdlLst).toJSONObject().get("rows").toString();
	}

	/* 주제영역명 조회 */
	@RequestMapping(value = "/Common/getAllSubNm", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAllSubNm(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType) throws Exception {
		String isAll = StringUtils.defaultString(modelEntityType.getIsAll());
		String isChoice = StringUtils.defaultString(modelEntityType.getIsChoice());
		List<Cd> retMdlLst = new ArrayList<Cd>();
		List<Cd> mdlLst = mqmCommonService.getAllSubNm(modelEntityType);
		Cd mdl = new Cd();
		mdl.setCd("");
		if (isAll.equals("Y")) {
			mdl.setCd_nm("전체");
		} else if (isChoice.equals("Y")) {
			mdl.setCd_nm("선택");
		} else {
			mdl.setCd_nm("");
		}
		retMdlLst.add(mdl);
		retMdlLst.addAll(mdlLst);

		logger.debug(success(retMdlLst).toJSONObject().get("rows").toString());
		return success(retMdlLst).toJSONObject().get("rows").toString();
	}

	/* 라이브러리명 조회 */
	@RequestMapping(value = "/Common/getLibNm", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getLibNm(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType) throws Exception {
		String isAll = StringUtils.defaultString(modelEntityType.getIsAll());
		String isChoice = StringUtils.defaultString(modelEntityType.getIsChoice());
		List<Cd> retMdlLst = new ArrayList<Cd>();
		List<Cd> mdlLst = mqmCommonService.getLibNm(modelEntityType);
		Cd mdl = new Cd();
		mdl.setCd("");
		if (isAll.equals("Y")) {
			mdl.setCd_nm("전체");
		} else if (isChoice.equals("Y")) {
			mdl.setCd_nm("선택");
		} else {
			mdl.setCd_nm("");
		}
		retMdlLst.add(mdl);
		retMdlLst.addAll(mdlLst);

		logger.debug(success(retMdlLst).toJSONObject().get("rows").toString());
		return success(retMdlLst).toJSONObject().get("rows").toString();
	}

	/* 모델명 조회 */
	@RequestMapping(value = "/Common/getModelNm", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getModelNm(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType) throws Exception {
		String isAll = StringUtils.defaultString(modelEntityType.getIsAll());
		String isChoice = StringUtils.defaultString(modelEntityType.getIsChoice());
		List<Cd> retMdlLst = new ArrayList<Cd>();
		List<Cd> mdlLst = mqmCommonService.getModelNm(modelEntityType);
		Cd mdl = new Cd();
		mdl.setCd("");
		if (isAll.equals("Y")) {
			mdl.setCd_nm("전체");
		} else if (isChoice.equals("Y")) {
			mdl.setCd_nm("선택");
		} else {
			mdl.setCd_nm("");
		}
		retMdlLst.add(mdl);
		retMdlLst.addAll(mdlLst);
		logger.debug(success(retMdlLst).toJSONObject().get("rows").toString());

		return success(retMdlLst).toJSONObject().get("rows").toString();
	}

	/* 주제영역명 조회 */
	@RequestMapping(value = "/Common/getSubNm", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getSubNm(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType) throws Exception {
		String isAll = StringUtils.defaultString(modelEntityType.getIsAll());
		String isChoice = StringUtils.defaultString(modelEntityType.getIsChoice());
		List<Cd> retMdlLst = new ArrayList<Cd>();
		List<Cd> mdlLst = mqmCommonService.getSubNm(modelEntityType);
		Cd mdl = new Cd();
		mdl.setCd("");
		if (isAll.equals("Y")) {
			mdl.setCd_nm("전체");
		} else if (isChoice.equals("Y")) {
			mdl.setCd_nm("선택");
		} else {
			mdl.setCd_nm("");
		}
		retMdlLst.add(mdl);
		retMdlLst.addAll(mdlLst);

		logger.debug(success(retMdlLst).toJSONObject().get("rows").toString());
		return success(retMdlLst).toJSONObject().get("rows").toString();
	}

}
