package omc.spop.controller;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Project;
import omc.spop.model.SQLStandards;
import omc.spop.model.Users;
import omc.spop.service.CommonService;
import omc.spop.service.ExecSqlStdChkService;
import omc.spop.service.SQLStandardsService;
import omc.spop.utils.DateUtil;

@RequestMapping(value="/sqlStandardOperationDesign")
@Controller
public class SqlStandardOperationDesignController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(SqlStandardOperationDesignController.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ExecSqlStdChkService execSqlStdChkService;
	
	@Autowired
	private SQLStandardsService sqlStandarsService;
	
	/* SQL 표준 일괄 점검 */
	@RequestMapping(value = "/designRenewal", method = RequestMethod.GET)
	public String designRenewal(@RequestParam(required = false) Map<String, String> param
			, Model model) {
		
		Users loginUser = SessionManager.getLoginSession().getUsers();
		
		model.addAttribute("auth_cd", loginUser.getAuth_cd());
		model.addAttribute("leader_yn", loginUser.getLeader_yn());
		model.addAttribute("wrkjob_cd", param.get("wrkjob_cd"));
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "sqlStandardDesign/operation/sqlStandardBatchDesign";
	}
	
	/* SQL 표준 점검 결과 */
	@RequestMapping(value = "/sqlStandardCheckResult", method = RequestMethod.GET)
	public String sqlStandardCheckResult(@RequestParam(required = false) Map<String, String> param
			, Model model) {
		
		Users loginUser = SessionManager.getLoginSession().getUsers();
		
		model.addAttribute("auth_cd", loginUser.getAuth_cd());
		model.addAttribute("leader_yn", loginUser.getLeader_yn());
		model.addAttribute("wrkjob_cd", param.get("wrkjob_cd"));
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "sqlStandardDesign/operation/sqlStandardCheckResult";
	}
	
	/* 표준 미준수 SQL */
	@RequestMapping(value = "/nonStandardSql", method = RequestMethod.GET)
	public String nonStandardSql(@RequestParam(required = false) HashMap<String, String> param
			, Model model) {
		
		List<SQLStandards> resultList = Collections.emptyList();
		SQLStandards sqlStandards = new SQLStandards();
		Users loginUser = new Users();
		String resultStr = "";
		
		try {
			if( param.containsKey("project_id") && param.get("project_id").isEmpty() == false ) {
				String project_id = param.get("project_id");
				sqlStandards.setProject_id(project_id);
				
				resultList = execSqlStdChkService.loadIndexList(sqlStandards);
				resultStr = success(resultList).toJSONObject().toString();
				
				model.addAttribute("indexList", resultStr );
				model.addAttribute("project_id", project_id );
				model.addAttribute("wrkjob_cd", param.get("wrkjob_cd"));
			}
			
			resultList = sqlStandarsService.loadAllIndex(param);
			resultStr = success(resultList).toJSONObject().toString();
			
			model.addAttribute("allIndexList", resultStr);
			
			loginUser = SessionManager.getLoginSession().getUsers();
			
			String auth_cd = loginUser.getAuth_cd();
			String leader_yn = loginUser.getLeader_yn();
			model.addAttribute("auth_cd", auth_cd);
			model.addAttribute("leader_yn", leader_yn);
			
			if( "ROLE_DEV".equals(auth_cd) && ("Y".equals(leader_yn) == false) ) {
				model.addAttribute("user_nm", loginUser.getUser_nm());
				model.addAttribute("developer_id", loginUser.getUser_id());
			}
			
			model.addAttribute("menu_id", param.get("menu_id"));
			model.addAttribute("menu_nm", param.get("menu_nm"));
		
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			resultStr = getErrorJsonString(ex);
			
		}finally {
			resultList = null;
			sqlStandards = null;
		}
		
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		return "sqlStandardDesign/operation/nonStandardSql";
	}
	
	/* SQL 표준 점검 예외 대상 관리 */
	@RequestMapping(value="/MaintainQualityCheckException", method=RequestMethod.GET)
	public String MaintainQualityCheckException(@ModelAttribute("sqlStandards") SQLStandards sqlStandards
			, Model model) {
		
		try {
			String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
			model.addAttribute("nowDate", nowDate );
			
			model.addAttribute("user_id", SessionManager.getLoginSession().getUsers().getUser_id());
			model.addAttribute("menu_id", sqlStandards.getMenu_id() );
			model.addAttribute("menu_nm", sqlStandards.getMenu_nm() );
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
		}
		
		return "sqlStandardDesign/operation/maintainQualityCheckException";
	}
	
	/* 스케줄러 관리 */
	@RequestMapping(value = "/manageScheduler", method = RequestMethod.GET)
	public String manageScheduler(@RequestParam(required = false) Map<String, String> param,
			HttpSession session, Model model) {
		
		model.addAttribute("auth_cd", SessionManager.getLoginSession().getUsers().getAuth_cd());
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		
		generateRSAKeyPair(session);
		
		return "sqlStandardDesign/operation/manageScheduler";
	}
	
	/* SQL 표준 품질점검 작업 */
	@RequestMapping(value = "/design", method = RequestMethod.GET)
	public String design(@RequestParam(required = false) Map<String, String> param, Model model) {
		model.addAttribute("menu_id", param.get("menu_id"));
		model.addAttribute("menu_nm", param.get("menu_nm"));
		return "sqlStandardDesign/operation/design";
	}
	
	/* SQL 표준 품질검토 작업 */
	@RequestMapping(value = "/QualityReviewWork", method = RequestMethod.GET)
	public String QualityReviewWork(@ModelAttribute("sqlStandards") SQLStandards sqlStandards,
			Model model) {
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm() );
		
		return "sqlStandardDesign/operation/qualityReviewWork";
	}
	
	/* projectList */
	@RequestMapping(value = "/getProjectList" , method = RequestMethod.GET , produces = "application/text; charset=utf8")
	@ResponseBody
	public String getProjectList() throws Exception{
		List<Project> resultList = new ArrayList<Project>();
		
		try {
			resultList = commonService.projectList( null );
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* projectList (개발자 로그인 시) */
	@RequestMapping(value = "/getDevProjectList" , method = RequestMethod.GET , produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDevProjectList() throws Exception{
		
		List<Project> resultList = Collections.emptyList();
		
		try {
			resultList = commonService.getDevProjectList();
			
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingClass().getName();
			logger.error( "{} 예외발생 ==> ", methodName, ex );
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 품질 집계표 */
	@RequestMapping(value = "/LoadQualityTable", method = RequestMethod.GET)
	public String LoadQualityTable(@ModelAttribute("sqlStandards") SQLStandards sqlStandards, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String user_nm = SessionManager.getLoginSession().getUsers().getUser_nm();
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("user_nm", user_nm);
		model.addAttribute("user_auth_id", user_auth_id);
		model.addAttribute("menu_id", sqlStandards.getMenu_id());
		model.addAttribute("menu_nm", sqlStandards.getMenu_nm() );
		
		return "sqlStandardDesign/operation/qualityTable";
	}
	
	private void generateRSAKeyPair(HttpSession session) {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024, new SecureRandom());
			
			KeyPair secureKey = generator.genKeyPair();
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = secureKey.getPublic();
			PrivateKey privateKey = secureKey.getPrivate();
			
			RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
			RSAPrivateKeySpec privateSpec = (RSAPrivateKeySpec) keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			
			Map<String,String> RSAKeys = new HashMap<String,String>();
			RSAKeys.put("Modulus", publicSpec.getModulus().toString(16));
			RSAKeys.put("PublicExp", publicSpec.getPublicExponent().toString(16));
			RSAKeys.put("PrivateExp", privateSpec.getPrivateExponent().toString(16));
			
			session.setAttribute("RSAKeys", RSAKeys);
			session.setAttribute(SQLStandardsController.SCHEDULER_KEY, secureKey);
			
		} catch (Exception e) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error( "{} 예외발생 ==> ", methodName, e );
		}
	}
}