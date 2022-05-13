package omc.spop.controller;

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

import omc.spop.base.InterfaceController;
import omc.spop.model.AgentFailover;
import omc.spop.model.AlertConfig;
import omc.spop.model.Database;
import omc.spop.model.Instance;
import omc.spop.model.InstanceV2;
import omc.spop.model.Result;
import omc.spop.model.RgbColor;
import omc.spop.service.DatabaseMngService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2017.12.08	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업 
 **********************************************************/

@Controller
public class DatabaseMngController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(DatabaseMngController.class);
	
	@Autowired
	private DatabaseMngService databaseMngService;

	/* 데이터베이스 관리 - DATABASE 관리 */
	@RequestMapping(value="/Database", method=RequestMethod.GET)
	public String Database(@ModelAttribute("database") Database database, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", database.getMenu_id() );
		model.addAttribute("menu_nm", database.getMenu_nm() );

		return "databaseMng/database";
	}

	/* 데이터베이스 관리 - DATABASE 관리 action */
	@RequestMapping(value="/Database", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String DatabaseAction(@ModelAttribute("database") Database database, Model model) {
		List<Database> resultList = new ArrayList<Database>();
		int dataCount4NextBtn = 0;
		
		try{
			resultList = databaseMngService.databaseList(database);
			if(resultList != null && resultList.size() > database.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(database.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();	
	}
	
	/*설정 - 데이터베이스 관리 엑셀 다운*/
	@RequestMapping(value = "/Database/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView databaseListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("database") Database database, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = databaseMngService.databaseListByExcelDown(database);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "데이터베이스_관리");
		model.addAttribute("sheetName", "데이터베이스_관리");
		model.addAttribute("excelId", "DATABASE_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	/* 데이터베이스 관리 - DATABASE save */
	@RequestMapping(value="/Database/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveDatabaseAction(@ModelAttribute("database") Database database,  Model model) {
		Result result = new Result();
		try{
			databaseMngService.saveDatabase(database);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 데이터베이스 관리 - 인스턴스 관리 */
	@RequestMapping(value="/Instance", method=RequestMethod.GET)
	public String Instance(@ModelAttribute("instance") Instance instance, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", instance.getMenu_id() );
		model.addAttribute("menu_nm", instance.getMenu_nm() );
	
		return "databaseMng/instance";
	}	
	
	/* 데이터베이스 관리 - 인스턴스관리 action */
	@RequestMapping(value="/Instance", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String InstanceAction(@ModelAttribute("instance") Instance instance,  Model model) {
		List<Instance> resultList = new ArrayList<Instance>();
		int dataCount4NextBtn = 0;

		try{
			resultList = databaseMngService.instanceList(instance);
			if(resultList != null && resultList.size() > instance.getPagePerCount()){
				dataCount4NextBtn = resultList.size();
				resultList.remove(instance.getPagePerCount());
				/*리스트의 마지막 인덱스를 삭제해서 0~9까지 총10개를 보여주기위함*/
			}
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();		
	}	
	
	
	/*설정 - 인스턴스 관리 엑셀 다운*/
	@RequestMapping(value = "/Instance/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView databaseListByExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("instance") Instance instance, Model model)
			throws Exception {

		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = databaseMngService.instanceListByExcelDown(instance);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "인스턴스_관리");
		model.addAttribute("sheetName", "인스턴스_관리");
		model.addAttribute("excelId", "INSTANCE_MNG");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	
	
	/* 데이터베이스 관리 - 인스턴스 사용  RGB Color 조회 */
	@RequestMapping(value="/InstanceRgbColor", method=RequestMethod.GET)
	@ResponseBody
	public Result InstanceRgbColor(@ModelAttribute("rgbColor") RgbColor rgbColor,  Model model) {
		Result result = new Result();
		List<RgbColor> resultList = new ArrayList<RgbColor>();
		
		try{
			resultList = databaseMngService.rgbColorList(rgbColor);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
			result.setMessage("사용 가능한  RGB Color가 없습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 데이터베이스 관리 - RGB Color 조회 */
	@RequestMapping(value="/getRGBColor", method=RequestMethod.GET)
	@ResponseBody
	public Result getRGBColor(@ModelAttribute("rgbColor") RgbColor rgbColor,  Model model) {
		Result result = new Result();
		List<RgbColor> resultList = new ArrayList<RgbColor>();
		
		try{
			resultList = databaseMngService.getRGBColor();
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
			result.setMessage(resultList.size() == 0 ? "사용 가능한  RGB Color가 없습니다." : "RGB Color를 조회하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	/* 데이터베이스 관리 - 사용중인 RGB Color 조회 */
	@RequestMapping(value="/checkRGBColorDatabase", method=RequestMethod.GET)
	@ResponseBody
	public Result checkRGBColorDatabase(@ModelAttribute("rgbColor") RgbColor rgbColor, Model model) {
		Result result = new Result();
		List<RgbColor> resultList = new ArrayList<RgbColor>();
		
		try {
			resultList = databaseMngService.checkRGBColorDatabase();
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
			result.setMessage(resultList.size() == 0 ? "Database에는 RGB Color를 사용하지 않습니다." : "Database에서 RGB Color를 조회하였습니다.");
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 데이터베이스 관리 - 사용중인 RGB Color 조회 */
	@RequestMapping(value="/checkRGBColorInstance", method=RequestMethod.GET)
	@ResponseBody
	public Result checkRGBColorInstance(@ModelAttribute("rgbColor") RgbColor rgbColor, Model model) {
		Result result = new Result();
		List<RgbColor> resultList = new ArrayList<RgbColor>();
		
		try {
			resultList = databaseMngService.checkRGBColorInstance();
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
			result.setMessage(resultList.size() == 0 ? "Instance에는  RGB Color를 사용하지 않습니다." : "Instance에서 RGB Color를 조회하였습니다.");
		} catch(Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* Instance 관리 - Instance save */
	@RequestMapping(value="/Instance/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveInstanceAction(@ModelAttribute("instance") Instance instance,  Model model) {
		Result result = new Result();
		try{
			databaseMngService.saveInstance(instance);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
//	/* Instance 관리 - Instance save */
//	@RequestMapping(value="/Instance/Save", method=RequestMethod.POST)
//	@ResponseBody
//	public Result SaveInstanceAction(@ModelAttribute("instanceV2") InstanceV2 instance,  Model model) {
//		Result result = new Result();
//		try{
//			databaseMngService.saveInstance(instance);
//			result.setResult(true);
//		} catch (Exception ex){
//			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
//			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
//			result.setResult(false);
//			result.setMessage(ex.getMessage());
//		}
//		
//		return result;	
//	}	
	
	
	/* Instance 관리 - Instance delete */
	@RequestMapping(value="/Instance/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteInstanceAction(@ModelAttribute("instance") Instance instance,  Model model) {
		Result result = new Result();
		try{
			int deleteResult = databaseMngService.deleteInstance(instance);
			if(deleteResult > 0){
				result.setResult(true);
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 데이터베이스 관리 - 에이전트  Failover 관리 */
	/* 2018-07-10 기능삭제(메뉴삭제) */
	@RequestMapping(value="/AgentFailover", method=RequestMethod.GET)
	public String AgentFailover(@ModelAttribute("agentFailover") AgentFailover agentFailover, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", agentFailover.getMenu_id() );

		return "databaseMng/agentFailover";
	}	
	
	/* 데이터베이스 관리 - 에이전트  Failover 리스트 */
	@RequestMapping(value="/AgentFailover", method=RequestMethod.POST)
	@ResponseBody
	public Result AgentFailoverAction(@ModelAttribute("agentFailover") AgentFailover agentFailover, Model model) {
		Result result = new Result();
		List<AgentFailover> resultList = new ArrayList<AgentFailover>();
		try{
			resultList = databaseMngService.agentFailoverList(agentFailover);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 데이터베이스 관리 - 에이전트  Failover save*/
	@RequestMapping(value="/AgentFailover/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveAgentFailoverAction(HttpServletRequest req, HttpServletResponse res, Model model) {
		Result result = new Result();
		try {				
			databaseMngService.saveAgentFailover(req);
			result.setResult(true);
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 데이터베이스 관리 - ALERT 설정 */
	@RequestMapping(value="/AlertSetting", method=RequestMethod.GET)
	public String AlertSetting(@ModelAttribute("alertConfig") AlertConfig alertConfig, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", alertConfig.getMenu_id() );
		model.addAttribute("menu_nm", alertConfig.getMenu_nm() );

		return "databaseMng/alertSetting";
	}
	
	/* 데이터베이스 관리 - ALERT설정 리스트 */
	@RequestMapping(value="/AlertSetting", method=RequestMethod.POST)
	@ResponseBody
	public Result AlertSettingAction(@ModelAttribute("alertConfig") AlertConfig alertConfig, Model model) {
		Result result = new Result();
		List<AlertConfig> resultList = new ArrayList<AlertConfig>();
		try{
			resultList = databaseMngService.alertSettingList(alertConfig);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setObject(resultList);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 데이터베이스 관리 - ALERT설정 save */
	@RequestMapping(value="/AlertSetting/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveAlertSettingAction(HttpServletRequest req, HttpServletResponse res, Model model) {
		Result result = new Result();
		try {				
			databaseMngService.saveAlertSetting(req);
			result.setResult(true);
		}catch (Exception ex) {
			logger.error("error ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}
	
	/* 데이터베이스 추가시 이미 등록된 dbid인지를 판단하는 용도 */
	@RequestMapping(value="/Database/notExistDbid", method=RequestMethod.POST)
	@ResponseBody
	public Result notExistDbid(@ModelAttribute("database") Database database, Model model) {
		Result result = new Result();
		List<Database> resultList = new ArrayList<Database>();
		
		try{
			resultList = databaseMngService.notExistDbid(database);
			result.setResult(resultList.size() > 0 ? true : false);
			result.setMessage("검색된 데이터가 있습니다.");
			result.setObject(resultList);
		} catch (Exception ex){
			logger.error("error ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
}