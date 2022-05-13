package omc.spop.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import omc.spop.base.InterfaceController;
import omc.spop.model.TopsqlAutoChoice;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.12	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class AutoSelectionDesignController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(AutoSelectionDesignController.class);
	
	/* SQL 성능 점검 -> 자동 선정 */
	@RequestMapping(value = "/sqlPerformanceDesign/autoSelection/autoSelectionDesign", method = RequestMethod.GET)
	public String autoSelectionDesign(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "sqlPerformanceDesign/autoSelection/autoSelectionDesign";
	}
	
	/* 자동선정 */
//	@RequestMapping(value="/AutoSelection", method=RequestMethod.GET)
	@RequestMapping(value="/sqlPerformanceDesign/autoSelection/autoSelection", method = RequestMethod.GET)
	public String AutoSelection(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String firstDay = DateUtil.getFirstDayOfThatMonth("yyyy-MM-dd", nowDate);
		String lastDay = DateUtil.getLastDayOfThatMonth("yyyy-MM-dd", nowDate);
		
		model.addAttribute("nowDate", lastDay );
		model.addAttribute("startDate", firstDay );
		model.addAttribute("menu_id", topsqlAutoChoice.getMenu_id() );
		model.addAttribute("menu_nm", topsqlAutoChoice.getMenu_nm() );
		model.addAttribute("call_from_child", topsqlAutoChoice.getCall_from_child() );
		
		return "sqlPerformanceDesign/autoSelection/autoSelection";
	}

	/* 자동선정 - 이력 조회 */
	@RequestMapping(value="/sqlPerformanceDesign/autoSelection/autoSelectionHistory", method = RequestMethod.GET)
	public String AutoSelectionHistory(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) {
		model.addAttribute("menu_id", topsqlAutoChoice.getMenu_id());
		model.addAttribute("menu_nm", topsqlAutoChoice.getMenu_nm() );
		return "sqlPerformanceDesign/autoSelection/autoSelectionHistory";
	}
	
	/* 자동선정 - 자동선정현황(선정회차) */
	@RequestMapping(value="/sqlPerformanceDesign/autoSelection/autoSelectionStatus", method = RequestMethod.GET)
	public String AutoSelectionStatus(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(topsqlAutoChoice.getStrStartDt() == null || topsqlAutoChoice.getStrStartDt().equals("")){
			topsqlAutoChoice.setStrStartDt(startDate);
		}
		
		if(topsqlAutoChoice.getStrEndDt() == null || topsqlAutoChoice.getStrEndDt().equals("")){
			topsqlAutoChoice.setStrEndDt(nowDate);
		}
		
		model.addAttribute("menu_id", topsqlAutoChoice.getMenu_id());
		model.addAttribute("menu_nm", topsqlAutoChoice.getMenu_nm() );
		return "sqlPerformanceDesign/autoSelection/autoSelectionStatus";
	}
	
	/* 자동선정 - 자동선정현황(검색)  */
	@RequestMapping(value="/sqlPerformanceDesign/autoSelection/autoSelectionStatusSearch", method = RequestMethod.GET)
	public String AutoSelectionStatusHistory(@ModelAttribute("topsqlAutoChoice") TopsqlAutoChoice topsqlAutoChoice, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		
		if(topsqlAutoChoice.getStrStartDt() == null || topsqlAutoChoice.getStrStartDt().equals("")){
			topsqlAutoChoice.setStrStartDt(startDate);
		}
		
		if(topsqlAutoChoice.getStrEndDt() == null || topsqlAutoChoice.getStrEndDt().equals("")){
			topsqlAutoChoice.setStrEndDt(nowDate);
		}

		model.addAttribute("menu_id", topsqlAutoChoice.getMenu_id());
		model.addAttribute("menu_nm", topsqlAutoChoice.getMenu_nm() );
		return "sqlPerformanceDesign/autoSelection/autoSelectionStatusSearch";
	}
}