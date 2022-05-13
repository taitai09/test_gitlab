package omc.mqm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import omc.mqm.model.ModelEntityType;
import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQtyChkSql;
import omc.mqm.model.ProjectModel;
import omc.mqm.model.QualityStdInfo;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Project;
import omc.spop.model.Users;
import omc.spop.service.CommonService;
import omc.spop.service.SQLAutomaticPerformanceCheckService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2020.04.29	명성태	최초작성
 * 2020.07.02	이재우	기능개선
 **********************************************************/

@RequestMapping(value="/MqmDesign/Setting")
@Controller
public class SettingDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(SettingDesignController.class);
	
	@Autowired
	private SQLAutomaticPerformanceCheckService sqlAutomaticPerformanceCheckService;

	@Autowired
	private CommonService commonService;
	
	/* DB 구조 품질점검 작업 */
	@RequestMapping(value = "/SettingDesign", method = RequestMethod.GET)
	public String SettingDesign(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "mqmDesign/setting/settingDesign";
	}
	
	/* 구조 품질점검 지표 관리 */
	@RequestMapping(value="/QualityCheckManagement", method=RequestMethod.GET)
	public String QualityCheckManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", qualityStdInfo.getMenu_id() );
		model.addAttribute("menu_nm", qualityStdInfo.getMenu_nm() );

		return "mqmDesign/setting/qualityCheckManagement";
	}
	
	/* 구조 품질점검 RULE 관리 */
	@RequestMapping(value="/QualityCheckSql", method=RequestMethod.GET)
	public String QualityCheckSql(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", openmQtyChkSql.getMenu_id() );
		model.addAttribute("menu_nm", openmQtyChkSql.getMenu_nm() );
		
		return "mqmDesign/setting/qualityCheckSql";
	}
	
	/* 업무분류체계 관리 */
	@RequestMapping(value="/BusinessClassMng", method=RequestMethod.GET)
	public String BusinessClassMng(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", openmBizCls.getMenu_id() );
		model.addAttribute("menu_nm", openmBizCls.getMenu_nm() );
		
		return "mqmDesign/setting/businessClassMng";
	}
	
	/*엔터티 유형 관리 */
	@RequestMapping(value="/EntityManagement", method=RequestMethod.GET)
	public String EntityManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", qualityStdInfo.getMenu_id() );
		model.addAttribute("menu_nm", qualityStdInfo.getMenu_nm() );

		return "mqmDesign/setting/entityManagement";
	}
	
	/*프로젝트 모델 관리 */
	@RequestMapping(value = "/ProjectModelManagement", method = RequestMethod.GET)
	public String ProjectModelMng(@ModelAttribute("projectModel") ProjectModel projectModel, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();
		List<Project> resultList = new ArrayList<Project>();
		
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
//		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
//		
//		sqlAutomaticPerformanceCheck.setProject_nm("");
//		sqlAutomaticPerformanceCheck.setDel_yn("N");
		try {
//			resultList = sqlAutomaticPerformanceCheckService.searchProjectList(sqlAutomaticPerformanceCheck);
			resultList = commonService.projectList( null );
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		model.addAttribute("projectList",resultList);
		model.addAttribute("menu_id", projectModel.getMenu_id());
		model.addAttribute("menu_nm", projectModel.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());
		
		return "mqmDesign/setting/projectModelManagement";
	}
	
	/* 프로젝트 구조 품질점검 지표/RULE 관리 */
	@RequestMapping(value="/ProjectQualityCheckRuleMng", method=RequestMethod.GET)
	public String ProjectQualityCheckRuleMng(@ModelAttribute("openmQtyChkSql") OpenmQtyChkSql openmQtyChkSql, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		List<Project> resultList = new ArrayList<Project>();
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
//		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
//		
//		sqlAutomaticPerformanceCheck.setProject_nm("");
//		sqlAutomaticPerformanceCheck.setDel_yn("N");
		try {
//			resultList = sqlAutomaticPerformanceCheckService.searchProjectList(sqlAutomaticPerformanceCheck);
			resultList = commonService.projectList( null );
		} catch(Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		model.addAttribute("projectList",resultList);
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", openmQtyChkSql.getMenu_id() );
		model.addAttribute("menu_nm", openmQtyChkSql.getMenu_nm() );
		
		return "mqmDesign/setting/projectQualityCheckRuleMng";
	}
	
	@RequestMapping(value = "/ModelEntityTypeManagement", method = RequestMethod.GET)
	public String ModelEntityTypeMng(@ModelAttribute("modelEntityType") ModelEntityType modelEntityType, Model model) {
		Users users = SessionManager.getLoginSession().getUsers();

		model.addAttribute("menu_id", modelEntityType.getMenu_id());
		model.addAttribute("menu_nm", modelEntityType.getMenu_nm());
		model.addAttribute("user_id", users.getUser_id());

		return "mqmDesign/setting/modelEntityTypeManagement";
	}
}