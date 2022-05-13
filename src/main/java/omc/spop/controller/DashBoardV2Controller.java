package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.NewAppTimeoutPrediction;
import omc.spop.model.NewSQLTimeoutPrediction;
import omc.spop.model.NewSql;
import omc.spop.model.ResourceLimitPrediction;
import omc.spop.model.Result;
import omc.spop.model.SequenceLimitPoint;
import omc.spop.model.TablespaceLimitPoint;
import omc.spop.service.DashBoardV2Service;

/***********************************************************
 * 2018.08.21 DashBoard
 **********************************************************/

@Controller
public class DashBoardV2Controller extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(DashBoardV2Controller.class);

	@Autowired
	private DashBoardV2Service dashBoardV2Searvice;

	/* Dashboard - 자원한계점 예측 = 3월내 한계도래되는것만 Disp */
	@RequestMapping(value = "/DashboardV2/getResourceLimitPointPredictionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getResourceLimitPointPredictionList(
			@ModelAttribute("resourceLimitPrediction") ResourceLimitPrediction resourceLimitPrediction, Model model) {
		List<ResourceLimitPrediction> resultList = new ArrayList<ResourceLimitPrediction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		resourceLimitPrediction.setUser_id(user_id);

		try {
			resultList = dashBoardV2Searvice.getResourceLimitPointPredictionList(resourceLimitPrediction);
			//logger.debug("======================");
			//logger.debug("getResourceLimitPointPredictionList resultList:" + resultList);
			//logger.debug("getResourceLimitPointPredictionList resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - 자원한계점 예측 = 3월내 한계도래되는것만 Disp, chart */
//	@RequestMapping(value = "/DashboardV2/getResourceLimitPointPredictionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@RequestMapping(value = "/DashboardV2/getResourceLimitPointPredictionChart")
	@ResponseBody
	public Result getResourceLimitPointPredictionChart(
			@ModelAttribute("resourceLimitPrediction") ResourceLimitPrediction resourceLimitPrediction, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		resourceLimitPrediction.setUser_id(user_id);
		
		Result result = new Result();

		try {
			result = dashBoardV2Searvice.getResourceLimitPointPredictionChartResult(resourceLimitPrediction);
			//logger.debug("======================");
			//logger.debug("getResourceLimitPointPredictionChart result:" + result);
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;	
	}

	/* Dashboard - Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp,Tablespace DBList */
	@RequestMapping(value = "/DashboardV2/getTablespacePresentConditionDBList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getTablespacePresentConditionDBList(
			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
		List<TablespaceLimitPoint> resultList = new ArrayList<TablespaceLimitPoint>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tablespaceLimitPoint.setUser_id(user_id);

		try {
			resultList = dashBoardV2Searvice.getTablespacePresentConditionDBList(tablespaceLimitPoint);
			//logger.debug("======================");
			//logger.debug("getTablespacePresentConditionDBList resultList:" + resultList);
			//logger.debug("getTablespacePresentConditionDBList resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp,Tablespace */
	@RequestMapping(value = "/DashboardV2/getTablespacePresentConditionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getTablespacePresentConditionList(
			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
		List<TablespaceLimitPoint> resultList = new ArrayList<TablespaceLimitPoint>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tablespaceLimitPoint.setUser_id(user_id);
		String dbid = tablespaceLimitPoint.getDbid();
		logger.debug("dbid:"+dbid);

		try {
			resultList = dashBoardV2Searvice.getTablespacePresentConditionList(tablespaceLimitPoint);
			//logger.debug("======================");
			//logger.debug("getTablespacePresentConditionList resultList:" + resultList);
			//logger.debug("getTablespacePresentConditionList resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}
	
	/* Dashboard - Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp,Tablespace */
//	@RequestMapping(value = "/DashboardV2/getTablespacePresentConditionChartResult")
//	@ResponseBody
//	public Result getTablespacePresentConditionChartResult(
//			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
//		Result result = new Result();
//
//		try{
//			result = dashBoardV2Searvice.getTablespacePresentConditionChartResult(tablespaceLimitPoint);
//		} catch (Exception ex){
//			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
//			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
//			result.setResult(false);
//			result.setMessage(ex.getMessage());
//		}
//
//		return result;	
//	}	
	
	/* Dashboard - Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp, Tablespace chart */
//	@RequestMapping(value = "/DashboardV2/getTablespacePresentConditionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@RequestMapping(value = "/DashboardV2/getTablespacePresentConditionChart")
	@ResponseBody
	public Result getTablespacePresentConditionChart(
			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		tablespaceLimitPoint.setUser_id(user_id);
//		tablespaceLimitPoint.setDbid("212205444");
		Result result = new Result();

		try {
			result = dashBoardV2Searvice.getTablespacePresentConditionChartResult(tablespaceLimitPoint);
			//logger.debug("======================");
			//logger.debug("getTablespacePresentConditionChart result:" + result);
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;	
	}
	/* Dashboard - Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp, Sequence chart */
//	@RequestMapping(value = "/DashboardV2/getSequencePresentConditionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@RequestMapping(value = "/DashboardV2/getSequencePresentConditionChart")
	@ResponseBody
	public Result getSequencePresentConditionChart(
			@ModelAttribute("SequenceLimitPoint") SequenceLimitPoint SequenceLimitPoint, Model model) {
		List<SequenceLimitPoint> resultList = new ArrayList<SequenceLimitPoint>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		SequenceLimitPoint.setUser_id(user_id);
//		tablespaceLimitPoint.setDbid("212205444");
		Result result = new Result();

		try {
			result = dashBoardV2Searvice.getSequencePresentConditionChartResult(SequenceLimitPoint);
			//logger.debug("======================");
			//logger.debug("getSequencePresentConditionChart result:" + result);
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	/* Dashboard - Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp,Sequence DBList */
	@RequestMapping(value = "/DashboardV2/getSequencePresentConditionDBList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getSequencePresentConditionDBList(
			@ModelAttribute("sequenceLimitPoint") SequenceLimitPoint sequenceLimitPoint, Model model) {
		List<SequenceLimitPoint> resultList = new ArrayList<SequenceLimitPoint>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sequenceLimitPoint.setUser_id(user_id);

		try {
			resultList = dashBoardV2Searvice.getSequencePresentConditionDBList(sequenceLimitPoint);
			//logger.debug("======================");
			//logger.debug("getSequencePresentConditionDBList resultList:" + resultList);
			//logger.debug("getSequencePresentConditionDBList resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - Tablespace/SEQ 현황 = 3월내 한계도래되는것만 Disp,Sequence */
	@RequestMapping(value = "/DashboardV2/getSequencePresentConditionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getSequencePresentConditionList(
			@ModelAttribute("sequenceLimitPoint") SequenceLimitPoint sequenceLimitPoint, Model model) {
		List<SequenceLimitPoint> resultList = new ArrayList<SequenceLimitPoint>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sequenceLimitPoint.setUser_id(user_id);

		try {
			resultList = dashBoardV2Searvice.getSequencePresentConditionList(sequenceLimitPoint);
			//logger.debug("======================");
			//logger.debug("getSequencePresentConditionList resultList:" + resultList);
			//logger.debug("getSequencePresentConditionList resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - 신규SQL/App Timeout 예측 */
	@RequestMapping(value = "/DashboardV2/getNewSQLTimeoutPredictionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getNewSQLTimeoutPredictionList(@ModelAttribute("newSQLTimeoutPrediction") NewSQLTimeoutPrediction newSQLTimeoutPrediction, Model model) {
		List<NewSQLTimeoutPrediction> resultList = new ArrayList<NewSQLTimeoutPrediction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newSQLTimeoutPrediction.setUser_id(user_id);

		try {
			resultList = dashBoardV2Searvice.getNewSQLTimeoutPredictionList(newSQLTimeoutPrediction);
			//logger.debug("======================");
			//logger.debug("getNewSQLTimeoutPredictionList resultList:" + resultList);
			//logger.debug("getNewSQLTimeoutPredictionList resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - 신규SQL/App Timeout 예측 */
	@RequestMapping(value = "/DashboardV2/getNewAppTimeoutPredictList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getNewAppTimeoutPredictList(
			@ModelAttribute("newAppTimeoutPrediction") NewAppTimeoutPrediction newAppTimeoutPrediction, Model model) {
		List<NewAppTimeoutPrediction> resultList = new ArrayList<NewAppTimeoutPrediction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newAppTimeoutPrediction.setUser_id(user_id);

		try {
			resultList = dashBoardV2Searvice.getNewAppTimeoutPredictList(newAppTimeoutPrediction);
			//logger.debug("======================");
			//logger.debug("getNewAppTimeoutPredictList resultList:" + resultList);
			//logger.debug("getNewAppTimeoutPredictList resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - 신규SQL/App Timeout 예측, 신규SQL chart */
	@RequestMapping(value = "/DashboardV2/getNewSQLTimeoutPredictionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getNewSQLTimeoutPredictionChart(@ModelAttribute("newSQLTimeoutPrediction") NewSQLTimeoutPrediction newSQLTimeoutPrediction, Model model) {
		List<NewSQLTimeoutPrediction> resultList = new ArrayList<NewSQLTimeoutPrediction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newSQLTimeoutPrediction.setUser_id(user_id);

		try {
			resultList = dashBoardV2Searvice.getNewSQLTimeoutPredictionChart(newSQLTimeoutPrediction);
			//logger.debug("======================");
			//logger.debug("getNewSQLTimeoutPredictionChart resultList:" + resultList);
			//logger.debug("getNewSQLTimeoutPredictionChart resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	/* Dashboard - 신규SQL/App Timeout 예측, 신규SQL chart */
	@RequestMapping(value = "/DashboardV2/getAppTimeoutPredictionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getAppTimeoutPredictionChart(@ModelAttribute("newSql") NewAppTimeoutPrediction newAppTimeoutPrediction, Model model) {
		List<NewAppTimeoutPrediction> resultList = new ArrayList<NewAppTimeoutPrediction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newAppTimeoutPrediction.setUser_id(user_id);
		
		try {
			resultList = dashBoardV2Searvice.getNewAppTimeoutPredictionChart(newAppTimeoutPrediction);
			//logger.debug("======================");
			//logger.debug("getNewSQLTimeoutPredictionChart resultList:" + resultList);
			//logger.debug("getNewSQLTimeoutPredictionChart resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().toString();
	}

	/* Dashboard - 신규SQL/App Timeout 예측, App chart */
	@RequestMapping(value = "/DashboardV2/getNewAppTimeoutPredictionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getNewAppTimeoutPredictionChart(
			@ModelAttribute("newAppTimeoutPrediction") NewAppTimeoutPrediction newAppTimeoutPrediction, Model model) {
		List<NewAppTimeoutPrediction> resultList = new ArrayList<NewAppTimeoutPrediction>();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		newAppTimeoutPrediction.setUser_id(user_id);

		try {
			resultList = dashBoardV2Searvice.getNewAppTimeoutPredictionChart(newAppTimeoutPrediction);
			//logger.debug("======================");
			//logger.debug("getNewAppTimeoutPredictionChart resultList:" + resultList);
			//logger.debug("getNewAppTimeoutPredictionChart resultList:" + success(resultList).toJSONObject().toString());
			//logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

}