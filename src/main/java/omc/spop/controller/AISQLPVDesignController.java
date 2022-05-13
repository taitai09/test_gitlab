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
import omc.spop.model.SQLAutomaticPerformanceCheck;
import omc.spop.utils.DateUtil;

@RequestMapping(value="/AISQLPVDesign")
@Controller
public class AISQLPVDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(AISQLPVDesignController.class);
	
	private final String dbcd = "ORACLE";
	
	@RequestMapping( value = "/AISQLPV", method = RequestMethod.GET )
	public String autoISQLBVDesign( @RequestParam(required = false) Map<String, String> param,Model model ) {

		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "autoIndexSQLPerformanceVerification/autoISQLPVDesign";
	}
	
	/* 인덱스 자동검증 */
	@RequestMapping( value = "/AutoIndexAnalys", method = RequestMethod.GET )
	public String autoIndexAnalys(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck 
									sqlAutomaticPerformanceCheck,Model model ) {
		String commonCode = "1082";
		
		String nowDate = DateUtil.getNowDate( "yyyy-MM-dd" );
		String startDate = DateUtil.getPlusDays( "yyyy-MM-dd", "yyyy-MM-dd", nowDate, -1 );
		String endDate = startDate;
		startDate =	DateUtil.getPlusMons( "yyyy-MM-dd", "yyyy-MM-dd", startDate, -1 );
		
		String startTime = "00:00";
		String endTime = "23:59";
		
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("nowDate", nowDate);
		model.addAttribute("commonCode", commonCode);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "autoIndexSQLPerformanceVerification/autoIndexAnalys";
	}
	
	/* 인덱스 Recommend */
	@RequestMapping( value = "/IndexRecommend", method = RequestMethod.GET )
	public String IndexRecommend(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck 
									sqlAutomaticPerformanceCheck, Model model ) {
		
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "autoIndexSQLPerformanceVerification/indexRecommend";
	}
	
	/* 인덱스별 성능 영향도 분석 결과 */
	@RequestMapping( value = "/perfImpactByIndex", method = RequestMethod.GET )
	public String perfImpactByIndex(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck param
			, Model model ) {
		
		try {
			String project_id = param.getProject_id();
			
			if ( project_id != null && (project_id).isEmpty() == false ) {
				
				model.addAttribute("project_id", project_id);
				model.addAttribute("sql_auto_perf_check_id", param.getSql_auto_perf_check_id());
				model.addAttribute("database_kinds_cd", param.getDatabase_kinds_cd());
				model.addAttribute("table_owner", param.getOwner_list());
				model.addAttribute("table_name", param.getTable_name_list());
				
			}else {
				model.addAttribute("database_kinds_cd", dbcd);
			}
			
			model.addAttribute("menu_id", param.getMenu_id());
			model.addAttribute("menu_nm", param.getMenu_nm());
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
		}
		
		return "autoIndexSQLPerformanceVerification/perfImpactByIndex";
	}
	
	/* 성능 영향도 분석 결과 */
	@RequestMapping( value = "/PerformanceCompareResult", method = RequestMethod.GET )
	public String PerformanceCompareResult(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		
		model.addAttribute("database_kinds_cd", dbcd);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "autoIndexSQLPerformanceVerification/performanceCompareResult";
	}
	
	/* 튜닝 실적 탭 */
	@RequestMapping( value = "/TuningPerformance", method = RequestMethod.GET )
	public String TuningPerformance(@ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		
		model.addAttribute("database_kinds_cd", dbcd);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "autoIndexSQLPerformanceVerification/tuningPerformance";
	}
	
	/* SQL 점검팩 탭 */
	@RequestMapping( value = "/SqlCheck", method = RequestMethod.GET )
	public String SqlCheck( @ModelAttribute("sqlAutomaticPerformanceCheck") SQLAutomaticPerformanceCheck sqlAutomaticPerformanceCheck,
			Model model ) {
		
		model.addAttribute("database_kinds_cd", dbcd);
		model.addAttribute("menu_id", sqlAutomaticPerformanceCheck.getMenu_id());
		model.addAttribute("menu_nm", sqlAutomaticPerformanceCheck.getMenu_nm());
		
		return "autoIndexSQLPerformanceVerification/sqlCheck";
	}
}
