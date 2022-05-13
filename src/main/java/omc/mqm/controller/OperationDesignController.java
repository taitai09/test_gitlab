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

import omc.mqm.model.OpenmBizCls;
import omc.mqm.model.OpenmQaindi;
import omc.mqm.model.QualityStdInfo;
import omc.spop.base.InterfaceController;
import omc.spop.model.Project;
import omc.spop.service.CommonService;
import omc.spop.service.SQLAutomaticPerformanceCheckService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2020.04.27	명성태	최초작성
 * 2020.07.02	이재우	기능개선
 **********************************************************/

@RequestMapping(value="/MqmDesign/Operation")
@Controller
public class OperationDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(OperationDesignController.class);
	
	@Autowired
	private SQLAutomaticPerformanceCheckService sqlAutomaticPerformanceCheckService;
	
	@Autowired
	private CommonService commonService;
	
	/* DB 구조 품질점검 작업 */
	@RequestMapping(value = "/OperationDesign", method = RequestMethod.GET)
	public String DbStructuralDesign(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "mqmDesign/operation/operationDesign";
	}
	
	/* 구조 품질검토 작업 */
	@RequestMapping(value="/QualityInspectionJob", method=RequestMethod.GET)
	public String QualityInspectionJob(@ModelAttribute("openmQaindi") OpenmQaindi openmQaindi, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		List<Project> resultList = new ArrayList<Project>();
		
//		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
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
		model.addAttribute("menu_id", openmQaindi.getMenu_id() );
		model.addAttribute("menu_nm", openmQaindi.getMenu_nm() );
		
		return "mqmDesign/operation/qualityInspectionJob";
	}
	
	/* 구조 품질 집계표 */
	@RequestMapping(value="/QualityInspectionJobSheet", method=RequestMethod.GET)
	public String QualityInspectionJobSheet(@ModelAttribute("openmBizCls") OpenmBizCls openmBizCls, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		List<Project> resultList = new ArrayList<Project>();
		
//		SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck = new SQLAutomaticPerformanceCheck();
//		List<SQLAutomaticPerformanceCheck> resultList = new ArrayList<SQLAutomaticPerformanceCheck>();
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
		model.addAttribute("menu_id", openmBizCls.getMenu_id() );
		model.addAttribute("menu_nm", openmBizCls.getMenu_nm() );
		
		return "mqmDesign/operation/qualityInspectionJobSheet";
	}
	
	/* 구조 품질검토 예외 대상 관리 */
	@RequestMapping(value="/QualityRevExcManagement", method=RequestMethod.GET)
	public String QualityReviewExclusionManagement(@ModelAttribute("qualityStdInfo") QualityStdInfo qualityStdInfo, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", qualityStdInfo.getMenu_id() );
		model.addAttribute("menu_nm", qualityStdInfo.getMenu_nm() );
		
		return "mqmDesign/operation/qualityRevExcManagement";
	}
}