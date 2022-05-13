package omc.spop.controller;

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

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.DeployPerfChkIndc;
import omc.spop.model.PerfList;
import omc.spop.model.TuningTargetSql;
import omc.spop.service.ImprovementManagementService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2020.05.06	명성태	최초작성
 * 2021.07.15	이재우	산출물 다운로드 수정
 **********************************************************/

@RequestMapping(value="/PerformanceImprovementDesign")
@Controller
public class PerformanceImprovementDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(PerformanceImprovementDesignController.class);
	
	@Autowired
	private ImprovementManagementService improvementManagementService;
	
	/* 성능 개선 실적 */
	@RequestMapping(value = "/situation", method = RequestMethod.GET)
	public String situation(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "performanceImprovementDesign/situation/design";
	}
	
	/* 성능 개선 실적 (수협)*/
	@RequestMapping(value = "/situation_V2", method = RequestMethod.GET)
	public String situation_V2(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "performanceImprovementDesign/situation/design_V2";
	}
	
	/* 성능개선 - 성능개선현황 보고서  */
	@RequestMapping(value="/PerformanceImprovementReport")
	public String PerformanceImprovementReport(@ModelAttribute("perfList") PerfList perfList,  Model model) {
//		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", DateUtil.getNowDate("yyyy-MM-dd"), "31");
//		String endDate = DateUtil.getNextDay("M", "yyyy-MM-dd", DateUtil.getNowDate("yyyy-MM-dd"), "1");

		String endDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", endDate,-6) ;
		
		
		if(perfList.getStrStartDt() == null || perfList.getStrStartDt().equals("")){
			perfList.setStrStartDt(startDate);
		}
		if(perfList.getStrEndDt() == null || perfList.getStrEndDt().equals("")){
			perfList.setStrEndDt(endDate);
		}
		
		model.addAttribute("menu_id", perfList.getMenu_id());
		model.addAttribute("menu_nm", perfList.getMenu_nm());
		
		return "performanceImprovementDesign/situation/performanceImprovementReport";
	}
	
	/* 성능개선결과 산출물 리스트  */
	@RequestMapping(value="/PerformanceImprovementOutputs")
	public String PerformanceImprovementOutputs(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		//String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6) ;

		if(tuningTargetSql.getStrStartDt() == null || tuningTargetSql.getStrStartDt().equals("")){
			tuningTargetSql.setStrStartDt(startDate);
		}
		
		if(tuningTargetSql.getStrEndDt() == null || tuningTargetSql.getStrEndDt().equals("")){
			tuningTargetSql.setStrEndDt(nowDate);
		}

		model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
		model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());
		model.addAttribute("call_from_parent", tuningTargetSql.getCall_from_parent());

		return "performanceImprovementDesign/situation/performanceImprovementOutputs";
	}
	
	/* 성능개선결과 산출물 리스트  */
	@RequestMapping(value="/PerformanceImprovementOutputs_V2")
	public String PerformanceImprovementOutputs_V2(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		//String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6) ;
		
		if(tuningTargetSql.getStrStartDt() == null || tuningTargetSql.getStrStartDt().equals("")){
			tuningTargetSql.setStrStartDt(startDate);
		}
		
		if(tuningTargetSql.getStrEndDt() == null || tuningTargetSql.getStrEndDt().equals("")){
			tuningTargetSql.setStrEndDt(nowDate);
		}
		
		model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
		model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());
		model.addAttribute("tuningTargetSql", tuningTargetSql );
		model.addAttribute("call_from_parent", tuningTargetSql.getCall_from_parent());
		
		return "performanceImprovementDesign/situation/performanceImprovementOutputs_V2";
	}
	
	/* 성능개선결과 산출물 리스트 상세 */
	@RequestMapping(value = "/PerformanceImprovementOutputsView")
	public String PerformanceImprovementOutputsView(@ModelAttribute("tuningTargetSql") TuningTargetSql tuningTargetSql, Model model) {
		String choickDivCd = tuningTargetSql.getChoice_div_cd();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		try{
			// 1. 성능개선현황 상태 조회
			model.addAttribute("sqlTuning", improvementManagementService.getSQLTuning(tuningTargetSql));
			
			// 2-1. SQL (선정) 상세 : CHOICE_DIV_CD -> 1, 2
			// 2-2. SQL (요청) 상세 : CHOICE_DIV_CD -> 3
			// 2-3. SQL (FULL SCAN) 상세 : CHOICE_DIV_CD -> 4
			// 2-4. SQL (PLAN 변경) 상세 : CHOICE_DIV_CD -> 5
			// 2-5. SQL (신규SQL) 상세 : CHOICE_DIV_CD -> 6
			// 2-6. SQL (TEMP과다사용) 상세 : CHOICE_DIV_CD -> 7
			
			if(choickDivCd.equals("1") || choickDivCd.equals("2")){
				model.addAttribute("selection", improvementManagementService.getSelection(tuningTargetSql));
			}else if(choickDivCd.equals("3")||choickDivCd.equals("B")){
				model.addAttribute("request", improvementManagementService.getRequest(tuningTargetSql));
				model.addAttribute("bindSetList", improvementManagementService.bindSetList(tuningTargetSql));
				model.addAttribute("sqlBindList", improvementManagementService.sqlBindList(tuningTargetSql));
			}else if(choickDivCd.equals("4")){
				model.addAttribute("fullScan", improvementManagementService.getFullScan(tuningTargetSql));
			}else if(choickDivCd.equals("5")){
				model.addAttribute("planChange", improvementManagementService.getPlanChange(tuningTargetSql));
			}else if(choickDivCd.equals("6")){
				model.addAttribute("newSql", improvementManagementService.getNewSQL(tuningTargetSql));
			}else if(choickDivCd.equals("7")){
				model.addAttribute("tempOver", improvementManagementService.getTempOver(tuningTargetSql));
			}else {
				model.addAttribute("request", improvementManagementService.getRequest(tuningTargetSql));
				model.addAttribute("bindSetList", improvementManagementService.bindSetList(tuningTargetSql));
				model.addAttribute("sqlBindList", improvementManagementService.sqlBindList(tuningTargetSql));
			}

			// 3. SQL 개선사항
//			model.addAttribute("improvements", improvementManagementService.getImprovements(tuningTargetSql));
			TuningTargetSql sqlDetail = improvementManagementService.getImprovements(tuningTargetSql);
			String impr_sql_text = StringUtil.formatHTML(sqlDetail.getImpr_sql_text());
			String imprb_exec_plan = StringUtil.formatHTML(sqlDetail.getImprb_exec_plan());
			String impra_exec_plan = StringUtil.formatHTML(sqlDetail.getImpra_exec_plan());
			sqlDetail.setImpr_sql_text(impr_sql_text);
			sqlDetail.setImprb_exec_plan(imprb_exec_plan);
			sqlDetail.setImpra_exec_plan(impra_exec_plan);
			sqlDetail.setOperation_dbid( tuningTargetSql.getDbid() );
			
			model.addAttribute("sqlDetail", sqlDetail);

			model.addAttribute("choickDivCd", choickDivCd);
			model.addAttribute("menu_id", tuningTargetSql.getMenu_id());
			model.addAttribute("menu_nm", tuningTargetSql.getMenu_nm());
			model.addAttribute("loginUser", user_id);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return "performanceImprovementDesign/situation/performanceImprovementOutputsView";
	}
	
	/* 성능리포트 - 프로그램유형별 성능개선현황  */
	@RequestMapping(value="/ByProgramType", method = RequestMethod.GET)
	public String ByProgramType(@ModelAttribute("perfList") PerfList perfList,  Model model) {

		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6);
		
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", perfList.getMenu_id());
		model.addAttribute("menu_nm", perfList.getMenu_nm());
		
		
		return "performanceImprovementDesign/situation/byProgramType";
	}
	
	/* 성능리포트 - 요청 유형별 성능개선현황  */
	@RequestMapping(value="/ByRequestType", method = RequestMethod.GET)
	public String ByRequestType(@ModelAttribute("perfList") PerfList perfList,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6);
		
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", perfList.getMenu_id());
		model.addAttribute("menu_nm", perfList.getMenu_nm());
		
		return "performanceImprovementDesign/situation/byRequestType";
	}
	
	/* 성능리포트 - 개선 유형별 성능개선현황  */
	@RequestMapping(value="/ByImprovementType", method = RequestMethod.GET)
	public String ByImprovementType(@ModelAttribute("perfList") PerfList perfList,  Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", nowDate,-6);
		
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", perfList.getMenu_id());
		model.addAttribute("menu_nm", perfList.getMenu_nm());
		
		return "performanceImprovementDesign/situation/byImprovementType";
	}
}