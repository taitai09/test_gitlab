package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.DashboardV2Left;
import omc.spop.model.Result;
import omc.spop.service.DashBoardV2Service;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2017.08.10	이원식	최초작성
 * 2018.05.15	이원식	권한별 Dashboard 기능 정의로 변경
 **********************************************************/

@Controller
public class MainControllerV2 extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainControllerV2.class);
	
	@Autowired
	private DashBoardV2Service dashBoardV2Service;	

	@Value("#{defaultConfig['version.date']}")
	private String settingDate;
	
	/* Dashboard - DB 점검 결과(등급별 전체 개수) */
	@RequestMapping(value="/DashboardV2/totalCntGrade", method=RequestMethod.POST)
	@ResponseBody
	public Result totalCntGrade(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) {
		Result result = new Result();
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			resultList = dashBoardV2Service.totalCntGrade(dashboardV2Left);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
			result.setMessage(resultList.size() > 0 ? "DB 점검 결과가 존재합니다." : "DB 점검 결과가 존재하지 않습니다.");
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* Dashboard - DB 점검 결과(DB별 등급 개수) */
	@RequestMapping(value="/DashboardV2/cntGradePerDb", method=RequestMethod.POST)
	@ResponseBody
	public String cntGradePerDb(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			resultList = dashBoardV2Service.cntGradePerDb(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - DB 점검 결과(DB별 등급 개수) */
	@RequestMapping(value="/DashboardV2/reloadDbCheckResultGrid01", method=RequestMethod.POST)
	@ResponseBody
	public String reloadDbCheckResultGrid01(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			logger.debug("dashboardV2Left reloadDbCheckResultGrid01.check_grade_cd: " +dashboardV2Left.getCheck_grade_cd());
			logger.debug("dashboardV2Left reloadDbCheckResultGrid01: " +dashboardV2Left.toString());
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			resultList = dashBoardV2Service.reloadDbCheckResultGrid01(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - DB 점검 결과(DB의 등급 정보 리스트) */
	@RequestMapping(value="/DashboardV2/listGradeForDb", method=RequestMethod.POST)
	@ResponseBody
	public String listGradeForDb(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			logger.debug("dashboardV2Left : " +dashboardV2Left.toString());
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			resultList = dashBoardV2Service.listGradeForDb(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - SQL/APP진단 DB목록 */
	@RequestMapping(value="/DashboardV2/listSqlAppCheckDb", method=RequestMethod.POST)
	@ResponseBody
	public String listSqlAppCheckDb(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			logger.debug("dashboardV2Left : " +dashboardV2Left.toString());
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			String base_day = DateUtil.getPlusDays("yyyyMMdd","yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"),-1) ;
			dashboardV2Left.setBase_day(base_day);
			
			resultList = dashBoardV2Service.listSqlAppCheckDb(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - SQL/APP 현황 챠트 */
	@RequestMapping(value="/DashboardV2/chartSqlAppCheckDb", method=RequestMethod.POST)
	@ResponseBody
	public Result chartSqlAppCheckDb(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) {
		Result result = new Result();
		
		try {
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			String base_day = DateUtil.getPlusDays("yyyyMMdd","yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"),-1) ;
			dashboardV2Left.setBase_day(base_day);
			
			result = dashBoardV2Service.chartSqlAppCheckDb(dashboardV2Left);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* Dashboard - SQL/APP 진단현황 */
	@RequestMapping(value="/DashboardV2/listSqlAppDiagStatus", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String listSqlAppDiagStatus(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			logger.debug("dashboardV2Left : " +dashboardV2Left.toString());
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			String base_day = DateUtil.getPlusDays("yyyyMMdd","yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"),-1) ;
			dashboardV2Left.setBase_day(base_day);
			
			resultList = dashBoardV2Service.listSqlAppDiagStatus(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - TOPSQL DB목록 */
	@RequestMapping(value="/DashboardV2/listTopSqlPerDb", method=RequestMethod.POST)
	@ResponseBody
	public String listTopSqlPerDb(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			logger.debug("dashboardV2Left : " +dashboardV2Left.toString());
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			String base_day = DateUtil.getPlusDays("yyyyMMdd","yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"),-1) ;
			dashboardV2Left.setBase_day(base_day);
			
			logger.debug("dashboardV2Left after : " +dashboardV2Left.toString());
			
			resultList = dashBoardV2Service.listTopSqlPerDb(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - TOPSQL 목록 */
	@RequestMapping(value="/DashboardV2/listTopSql", method=RequestMethod.POST)
	@ResponseBody
	public String listTopSql(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			logger.debug("dashboardV2Left : " +dashboardV2Left.toString());
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			String base_day = DateUtil.getPlusDays("yyyyMMdd","yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"),-1) ;
			dashboardV2Left.setBase_day(base_day);
			
			resultList = dashBoardV2Service.listTopSql(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - TOPSQL 챠트 */
	@RequestMapping(value="/DashboardV2/chartTopSql", method=RequestMethod.POST)
	@ResponseBody
	public String chartTopSql(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			logger.debug("dashboardV2Left : " +dashboardV2Left.toString());
			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			dashboardV2Left.setUser_id(user_id);
			
			String base_day = DateUtil.getPlusDays("yyyyMMdd","yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"),-1) ;
			dashboardV2Left.setBase_day(base_day);
			
			resultList = dashBoardV2Service.chartTopSql(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - TOPSQL 챠트2 */
	@RequestMapping(value="/DashboardV2/chartTopSql2", method=RequestMethod.POST)
	@ResponseBody
	public String chartTopSql2(@ModelAttribute("dashboardV2Left") DashboardV2Left dashboardV2Left, Model model) throws Exception {
		List<DashboardV2Left> resultList = new ArrayList<DashboardV2Left>();
		
		try {
			logger.debug("dashboardV2Left : " +dashboardV2Left.toString());
			
			resultList = dashBoardV2Service.chartTopSql2(dashboardV2Left);
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
}