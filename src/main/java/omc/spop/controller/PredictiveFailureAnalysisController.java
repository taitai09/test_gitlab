package omc.spop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import omc.spop.base.InterfaceController;
import omc.spop.model.CPUIncreaseUsage;
import omc.spop.model.CPULimitPrediction;
import omc.spop.model.CPULimitPredictionDetail;
import omc.spop.model.MEMORYLimitPrediction;
import omc.spop.model.MEMORYLimitPredictionDetail;
import omc.spop.model.NewAppTimeoutPrediction;
import omc.spop.model.NewAppTimeoutPredictionUpdate;
import omc.spop.model.NewSQLTimeoutPrediction;
import omc.spop.model.NewSQLTimeoutPredictionUpdate;
import omc.spop.model.OdsHistSqlText;
import omc.spop.model.RegularSQLFilterCase;
import omc.spop.model.Result;
import omc.spop.model.SequenceLimitPoint;
import omc.spop.model.TablespaceLimitPoint;
import omc.spop.model.UnknownSQLFaultPrediction;
import omc.spop.service.PredictiveFailureAnalysisService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.OMCUtil;

/***********************************************************
 * 2018.06.07 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class PredictiveFailureAnalysisController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(PredictiveFailureAnalysisController.class);

	@Autowired
	private PredictiveFailureAnalysisService predictiveFailureAnalysisService;

	/* CPU 사용량 증가 */
	@RequestMapping(value = "/CPUIncreaseUsage", method = RequestMethod.GET)
	public String CPUIncreaseUsage(@ModelAttribute("cpuIncreaseUsage") CPUIncreaseUsage cpuIncreaseUsage, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", cpuIncreaseUsage.getMenu_id());
		model.addAttribute("menu_nm", cpuIncreaseUsage.getMenu_nm());

		return "predictiveFailure/cpuIncreaseUsage";
	}

	/* CPU 사용량 증가 - 챠트 리스트 action */
	@RequestMapping(value = "/CPUIncreaseUsageChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String CPUIncreaseUsageChartAction(@ModelAttribute("cpuIncreaseUsage") CPUIncreaseUsage cpuIncreaseUsage,
			Model model) {
		List<CPUIncreaseUsage> resultList = new ArrayList<CPUIncreaseUsage>();

		try {
			resultList = predictiveFailureAnalysisService.cpuIncreaseUsageChartList(cpuIncreaseUsage);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* CPU 사용량 증가 - 상세 */
	@RequestMapping(value = "/CPUIncreaseUsageDetail", method = RequestMethod.GET)
	public String CPUIncreaseUsageDetail(@ModelAttribute("cpuIncreaseUsage") CPUIncreaseUsage cpuIncreaseUsage,
			Model model) {
		model.addAttribute("menu_id", cpuIncreaseUsage.getMenu_id());
		model.addAttribute("menu_nm", cpuIncreaseUsage.getMenu_nm());
		return "predictiveFailure/cpuIncreaseUsageDetail";
	}

	/* CPU 사용량 증가 - 상세 - CPU Usage 챠트 리스트 action */
	@RequestMapping(value = "/CPUIncreaseUsageDetail/CPUUsageChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String CPUUsageChartAction(@ModelAttribute("cpuIncreaseUsage") CPUIncreaseUsage cpuIncreaseUsage,
			Model model) {
		List<CPUIncreaseUsage> resultList = new ArrayList<CPUIncreaseUsage>();

		try {
			resultList = predictiveFailureAnalysisService.cpuUsageChartList(cpuIncreaseUsage);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* CPU 사용량 증가 - 상세 - User Time 챠트 리스트 action */
	@RequestMapping(value = "/CPUIncreaseUsageDetail/UserTimeChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String UserTimeChartAction(@ModelAttribute("cpuIncreaseUsage") CPUIncreaseUsage cpuIncreaseUsage,
			Model model) {
		List<CPUIncreaseUsage> resultList = new ArrayList<CPUIncreaseUsage>();

		try {
			resultList = predictiveFailureAnalysisService.userTimeChartList(cpuIncreaseUsage);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* CPU 사용량 증가 - 상세 - SYS Time 챠트 리스트 action */
	@RequestMapping(value = "/CPUIncreaseUsageDetail/SysTimeChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SysTimeChartAction(@ModelAttribute("cpuIncreaseUsage") CPUIncreaseUsage cpuIncreaseUsage,
			Model model) {
		List<CPUIncreaseUsage> resultList = new ArrayList<CPUIncreaseUsage>();

		try {
			resultList = predictiveFailureAnalysisService.sysTimeChartList(cpuIncreaseUsage);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* CPU 사용량 증가 - 상세 - Time Model 챠트 리스트 action */
	@RequestMapping(value = "/CPUIncreaseUsageDetail/TimeModelChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TimeModelChartAction(@ModelAttribute("cpuIncreaseUsage") CPUIncreaseUsage cpuIncreaseUsage,
			Model model) {
		List<CPUIncreaseUsage> resultList = new ArrayList<CPUIncreaseUsage>();

		try {
			resultList = predictiveFailureAnalysisService.timeModelChartList(cpuIncreaseUsage);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* CPU 사용량 증가 - 상세 - TOP SQL 리스트 action */
	@RequestMapping(value = "/CPUIncreaseUsageDetail/TopSQL", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TopSQLAction(@ModelAttribute("cpuIncreaseUsage") CPUIncreaseUsage cpuIncreaseUsage, Model model) {
		List<CPUIncreaseUsage> resultList = new ArrayList<CPUIncreaseUsage>();

		try {
			resultList = predictiveFailureAnalysisService.topSQLList(cpuIncreaseUsage);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* CPU 한계점 예측 */
	@RequestMapping(value = "/CPULimitPointPrediction", method = RequestMethod.GET)
	public String CPULimitPointPrediction(@ModelAttribute("cpuLimitPrediction") CPULimitPrediction cpuLimitPrediction,
			Model model) {
		
		model.addAttribute("dbid", cpuLimitPrediction.getDbid());
		model.addAttribute("resource_type", cpuLimitPrediction.getResource_type());
		model.addAttribute("predict_standard", cpuLimitPrediction.getPredict_standard());
		model.addAttribute("menu_id", cpuLimitPrediction.getMenu_id());
		model.addAttribute("menu_nm", cpuLimitPrediction.getMenu_nm());
		model.addAttribute("call_from_parent", cpuLimitPrediction.getCall_from_parent());

		return "predictiveFailure/cpuLimitPointPrediction";
	}

	/* CPU 한계점 예측 - 리스트 action */
	@RequestMapping(value = "/CPULimitPointPrediction", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String CPULimitPointPredictionAction(
			@ModelAttribute("cpuLimitPrediction") CPULimitPrediction cpuLimitPrediction, Model model) {
		List<CPULimitPrediction> resultList = new ArrayList<CPULimitPrediction>();

		try {
			resultList = predictiveFailureAnalysisService.cpuLimitPointPredictionList(cpuLimitPrediction);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* CPU 한계점 예측 - 상세 */
	@RequestMapping(value = "/CPULimitPointPredictionDetail", method = RequestMethod.GET)
	public String CPULimitPointPredictionDetail(
			@ModelAttribute("cpuLimitPrediction") CPULimitPrediction cpuLimitPrediction, Model model) {
		model.addAttribute("menu_id", cpuLimitPrediction.getMenu_id());
		model.addAttribute("menu_nm", cpuLimitPrediction.getMenu_nm());
		model.addAttribute("predict_standard", cpuLimitPrediction.getPredict_standard());

		return "predictiveFailure/cpuLimitPointPredictionDetail";
	}

	/* CPU 한계점 예측 - 상세 - 한계점 도달 챠트 리스트 action */
	@RequestMapping(value = "/CPULimitPredictionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String CPULimitPredictionChartAction(
			@ModelAttribute("cpuLimitPredictionDetail") CPULimitPredictionDetail cpuLimitPredictionDetail,
			Model model) {
		List<CPULimitPredictionDetail> resultList = new ArrayList<CPULimitPredictionDetail>();

		try {
			resultList = predictiveFailureAnalysisService.cpuLimitPredictionChartList(cpuLimitPredictionDetail);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/* MEMORY 한계점 예측 */
	@RequestMapping(value = "/MEMORYLimitPointPrediction", method = RequestMethod.GET)
	public String MEMORYLimitPointPrediction(@ModelAttribute("memoryLimitPrediction") MEMORYLimitPrediction memoryLimitPrediction,
			Model model) {
		
		model.addAttribute("dbid", memoryLimitPrediction.getDbid());
		model.addAttribute("resource_type", memoryLimitPrediction.getResource_type());
		model.addAttribute("predict_standard", memoryLimitPrediction.getPredict_standard());
		model.addAttribute("menu_id", memoryLimitPrediction.getMenu_id());
		model.addAttribute("menu_nm", memoryLimitPrediction.getMenu_nm());
		model.addAttribute("call_from_parent", memoryLimitPrediction.getCall_from_parent());
		
		return "predictiveFailure/memoryLimitPointPrediction";
	}

	/* MEMORY 한계점 예측 - 리스트 action */
	@RequestMapping(value = "/MEMORYLimitPointPrediction", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String MEMORYLimitPointPredictionAction(
			@ModelAttribute("memoryLimitPrediction") MEMORYLimitPrediction memoryLimitPrediction, Model model) {
		List<MEMORYLimitPrediction> resultList = new ArrayList<MEMORYLimitPrediction>();

		try {
			resultList = predictiveFailureAnalysisService.memoryLimitPointPredictionList(memoryLimitPrediction);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* MEMORY 한계점 예측 - 상세 */
	@RequestMapping(value = "/MEMORYLimitPointPredictionDetail", method = RequestMethod.GET)
	public String MEMORYLimitPointPredictionDetail(
			@ModelAttribute("memoryLimitPrediction") MEMORYLimitPrediction memoryLimitPrediction, Model model) {
		
		model.addAttribute("menu_id", memoryLimitPrediction.getMenu_id());
		model.addAttribute("menu_nm", memoryLimitPrediction.getMenu_nm());
		model.addAttribute("predict_standard", memoryLimitPrediction.getPredict_standard());
		model.addAttribute("prediction_dt", memoryLimitPrediction.getPrediction_dt());

		return "predictiveFailure/memoryLimitPointPredictionDetail";
	}

	/* MEMORY 한계점 예측 - 상세 - 한계점 도달 챠트 리스트 action */
	@RequestMapping(value = "/MEMORYLimitPredictionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String MEMORYLimitPredictionChartAction(
			@ModelAttribute("memoryLimitPredictionDetail") MEMORYLimitPredictionDetail memoryLimitPredictionDetail,
			Model model) {
		List<MEMORYLimitPredictionDetail> resultList = new ArrayList<MEMORYLimitPredictionDetail>();

		try {
			logger.debug("MEMORYLimitPredictionChart:" + memoryLimitPredictionDetail.toString());
			resultList = predictiveFailureAnalysisService.memoryLimitPredictionChartList(memoryLimitPredictionDetail);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	// 187 173 Sequence 한계점예측 /SequenceLimitPointPrediction fas fa-coins 3 Y
	// 188 173 Sequence 한계점예측 상세 Sequence 한계점예측 상세
	// /SequenceLimitPointPredictionDetail fas fa-broadcast-tower 4 Y
	// 189 173 Tablespace 한계점예측 Tablespace 한계점예측 /TablespaceLimitPointPrediction
	// fas fa-coins 5 Y
	// 190 173 Tablespace 한계점예측 상세 Tablespace 한계점예측 상세
	// /TablespaceLimitPointPredictionDetail fas fa-broadcast-tower 6 Y

	/* Sequence 한계점예측 */
	@RequestMapping(value = "/SequenceLimitPointPrediction", method = RequestMethod.GET)
	public String SequenceLimitPointPrediction(
			@ModelAttribute("sequenceLimitPoint") SequenceLimitPoint sequenceLimitPoint, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", sequenceLimitPoint.getMenu_id());
		model.addAttribute("menu_nm", sequenceLimitPoint.getMenu_nm());

		return "predictiveFailure/sequenceLimitPointPrediction";
	}

	/* Sequence 한계점예측 - 챠트 리스트 action */
	@RequestMapping(value = "/SequenceLimitPointPredictionChart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SequenceLimitPointPredictionChartAction(
			@ModelAttribute("sequenceLimitPoint") SequenceLimitPoint sequenceLimitPoint, Model model) {
		List<SequenceLimitPoint> resultList = new ArrayList<SequenceLimitPoint>();

		try {
			resultList = predictiveFailureAnalysisService.sequenceLimitPointPredictionChartList(sequenceLimitPoint);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}

	/* Sequence 한계점예측 상세 */
	@RequestMapping(value = "/SequenceLimitPointPredictionDetail", method = RequestMethod.GET)
	public String SequenceLimitPointPredictionDetail(
			@ModelAttribute("sequenceLimitPoint") SequenceLimitPoint sequenceLimitPoint, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", sequenceLimitPoint.getMenu_id());
		model.addAttribute("menu_nm", sequenceLimitPoint.getMenu_nm());
		model.addAttribute("sequenceLimitPoint", sequenceLimitPoint);

		return "predictiveFailure/sequenceLimitPointPredictionDetail";
	}
	
	/* Sequence 한계점예측 상세- 데이터 리스트 action */
	@RequestMapping(value = "/SequenceLimitPointArrivalList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SequenceLimitPointArrivalList(
			@ModelAttribute("sequenceLimitPoint") SequenceLimitPoint sequenceLimitPoint, Model model) {
		List<SequenceLimitPoint> resultList = new ArrayList<SequenceLimitPoint>();

		try {
			resultList = predictiveFailureAnalysisService.sequenceLimitPointArrivalList(sequenceLimitPoint);
			logger.debug("======================");
			logger.debug("Sequence 한계점예측 상세- 데이터 리스트:" + resultList);
			logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	@RequestMapping(value = "/SequenceLimitPointArrivalMapList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SequenceLimitPointArrivalMapList(@RequestParam(required = true) Map<String, Object> param, Model model) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();

		try {
			resultList = predictiveFailureAnalysisService.sequenceLimitPointArrivalMapList(param);
			logger.debug("======================");
			logger.debug("Sequence 한계점예측 상세- 데이터 리스트:" + resultList);
			logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return getJSONResult(resultList).toJSONObject().toString();
	}


	/* Sequence 한계점예측 상세- 챠트 리스트 action */
	@RequestMapping(value = "/sequenceLimitPointPredictionDetailChartList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String sequenceLimitPointPredictionDetailChartList(
			@ModelAttribute("sequenceLimitPoint") SequenceLimitPoint sequenceLimitPoint, Model model) {
		List<SequenceLimitPoint> resultList = new ArrayList<SequenceLimitPoint>();

		try {
			resultList = predictiveFailureAnalysisService.sequenceLimitPointPredictionDetailChartList(sequenceLimitPoint);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}

	/* Tablespace 한계점예측 */
	@RequestMapping(value = "/TablespaceLimitPointPrediction", method = RequestMethod.GET)
	public String TablespaceLimitPointPrediction(
			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", tablespaceLimitPoint.getMenu_id());
		model.addAttribute("menu_nm", tablespaceLimitPoint.getMenu_nm());

		return "predictiveFailure/tablespaceLimitPointPrediction";
	}

	/* Tablespace 한계점예측 - 챠트 리스트 action */
	@RequestMapping(value = "/tablespaceLimitPointPredictionChartList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String tablespaceLimitPointPredictionChartList(
			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
		List<TablespaceLimitPoint> resultList = new ArrayList<TablespaceLimitPoint>();

		try {
			resultList = predictiveFailureAnalysisService.tablespaceLimitPointPredictionChartList(tablespaceLimitPoint);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}

	/* Tablespace 한계점예측 상세 */
	@RequestMapping(value = "/TablespaceLimitPointPredictionDetail", method = RequestMethod.GET)
	public String TablespaceLimitPointPredictionDetail(
			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		model.addAttribute("nowDate", nowDate);
		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(tablespaceLimitPoint, Map.class);
		OMCUtil.convertMapToModel(map, model);

		return "predictiveFailure/tablespaceLimitPointPredictionDetail";
	}

	/* Tablespace 한계점예측 상세- 데이터 리스트 action */
	@RequestMapping(value = "/TablespaceLimitPointArrivalList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String TablespaceLimitPointArrivalList(
			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
		List<TablespaceLimitPoint> resultList = new ArrayList<TablespaceLimitPoint>();

		try {
			resultList = predictiveFailureAnalysisService.tablespaceLimitPointArrivalList(tablespaceLimitPoint);
			logger.debug("======================");
			logger.debug("한계점예측 상세- 데이터 리스트:" + resultList);
			logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}

	/* Tablespace 한계점예측 상세- 챠트 데이터 리스트 action */
	@RequestMapping(value = "/tablespaceLimitPointPredictionDetailChartList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String tablespaceLimitPointPredictionDetailChartList(
			@ModelAttribute("tablespaceLimitPoint") TablespaceLimitPoint tablespaceLimitPoint, Model model) {
		List<TablespaceLimitPoint> resultList = new ArrayList<TablespaceLimitPoint>();

		try {
			resultList = predictiveFailureAnalysisService.tablespaceLimitPointPredictionDetailChartList(tablespaceLimitPoint);
			logger.debug("======================");
			logger.debug("한계점예측 상세- 챠트 데이터 리스트:" + resultList);
			logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 신규 APP 타임아웃 예측 */
	@RequestMapping(value = "/NewAppTimeoutPrediction", method = RequestMethod.GET)
	public String newAppTimeoutPrediction(@ModelAttribute("newAppTimeoutPrediction") NewAppTimeoutPrediction newAppTimeoutPrediction, Model model) {
		
		
		String today = DateUtil.getNowDate("yyyy-MM-dd");
		String yesterday = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", today,-1);
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", yesterday,-6) ;
		String threeMonthPrevDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", yesterday,-90) ;

		String predict_standard = StringUtils.defaultString(newAppTimeoutPrediction.getPredict_standard());
		
		if(!predict_standard.equals("")){
			newAppTimeoutPrediction.setStart_first_exec_day(threeMonthPrevDate);
		}else {
			if(newAppTimeoutPrediction.getStart_first_exec_day() == null || newAppTimeoutPrediction.getStart_first_exec_day().equals("")){
				newAppTimeoutPrediction.setStart_first_exec_day(startDate);
			}
		}
		
		if(newAppTimeoutPrediction.getEnd_first_exec_day() == null || newAppTimeoutPrediction.getEnd_first_exec_day().equals("")){
			newAppTimeoutPrediction.setEnd_first_exec_day(yesterday);
		}
		
		model.addAttribute("menu_id", newAppTimeoutPrediction.getMenu_id());
		model.addAttribute("menu_nm", newAppTimeoutPrediction.getMenu_nm());
		
		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(newAppTimeoutPrediction, Map.class);
		OMCUtil.convertMapToModel(map, model);
		
		return "predictiveFailure/newAppTimeoutPrediction";
	}
	
	/* 신규 APP 타임아웃 예측 - list action */
	@RequestMapping(value = "/NewAppTimeoutPredictionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String newAppTimeoutPredictionList(@ModelAttribute("newAppTimeoutPrediction") NewAppTimeoutPrediction newAppTimeoutPrediction, Model model)
			throws Exception {
		

		List<NewAppTimeoutPrediction> resultList = new ArrayList<NewAppTimeoutPrediction>();

		try {
			resultList = predictiveFailureAnalysisService.newAppTimeoutPredictionList(newAppTimeoutPrediction);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/* 신규 APP 타임아웃 예측 - 챠트 데이터 리스트 action */
	@RequestMapping(value = "/newAppTimeoutPredictionChartList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String newAppTimeoutPredictionChartList(
			@ModelAttribute("newAppTimeoutPrediction") NewAppTimeoutPrediction newAppTimeoutPrediction, Model model) {
		List<NewAppTimeoutPrediction> resultList = new ArrayList<NewAppTimeoutPrediction>();

		try {
			resultList = predictiveFailureAnalysisService.newAppTimeoutPredictionChartList(newAppTimeoutPrediction);
			logger.debug("======================");
			logger.debug("신규 APP 타임아웃 예측 - 챠트 데이터 리스트:" + resultList);
			logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 신규 APP 타임아웃 예측 - 예측 예외 수정 */
	@RequestMapping(value = "/newAppTimeoutPredictionExceptYnUpdate", method = RequestMethod.GET)
	@ResponseBody
	public Result newAppTimeoutPredictionExceptYnUpdate(
			@ModelAttribute("newAppTimeoutPredictionUpdate") NewAppTimeoutPredictionUpdate newAppTimeoutPredictionUpdate, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		
		try {
			rowCnt = predictiveFailureAnalysisService.newAppTimeoutPredictionExceptYnUpdate(newAppTimeoutPredictionUpdate);
			
			logger.debug("newAppTimeoutPredictionUpdate rowCnt:"+rowCnt);
			if(rowCnt > 0){
				result.setResult(true);
				result.setMessage("예측 예외 수정이 변경하였습니다.");
			}else{
				result.setResult(false);
				result.setMessage("예측 예외 수정이 실패하였습니다.");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 신규 SQL 타임아웃 예측 */
	@RequestMapping(value = "/NewSQLTimeoutPrediction", method = RequestMethod.GET)
	public String newSQLTimeoutPrediction(@ModelAttribute("newSQLTimeoutPrediction") NewSQLTimeoutPrediction newSQLTimeoutPrediction, Model model) {
		String today = DateUtil.getNowDate("yyyy-MM-dd");
		String yesterday = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", today,-1);
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", yesterday,-6) ;
		String threeMonthPrevDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", yesterday,-90) ;

		String predict_standard = StringUtils.defaultString(newSQLTimeoutPrediction.getPredict_standard());
		
		if(!predict_standard.equals("")){
			newSQLTimeoutPrediction.setStart_first_exec_day(threeMonthPrevDate);
		}else {
			if(newSQLTimeoutPrediction.getStart_first_exec_day() == null || newSQLTimeoutPrediction.getStart_first_exec_day().equals("")){
				newSQLTimeoutPrediction.setStart_first_exec_day(startDate);
			}
		}	
		
		if(newSQLTimeoutPrediction.getEnd_first_exec_day() == null || newSQLTimeoutPrediction.getEnd_first_exec_day().equals("")){
			newSQLTimeoutPrediction.setEnd_first_exec_day(yesterday);
		}
		
		model.addAttribute("menu_id", newSQLTimeoutPrediction.getMenu_id());
		model.addAttribute("menu_nm", newSQLTimeoutPrediction.getMenu_nm());
		
		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(newSQLTimeoutPrediction, Map.class);
		OMCUtil.convertMapToModel(map, model);
	
		return "predictiveFailure/newSQLTimeoutPrediction";
	}
	
	/* 신규 SQL 타임아웃 예측 - list action */
	@RequestMapping(value = "/NewSQLTimeoutPredictionList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String newSQLTimeoutPredictionList(@ModelAttribute("newSQLTimeoutPrediction") NewSQLTimeoutPrediction newSQLTimeoutPrediction, Model model)
			throws Exception {
		List<NewSQLTimeoutPrediction> resultList = new ArrayList<NewSQLTimeoutPrediction>();

		try {
			resultList = predictiveFailureAnalysisService.newSQLTimeoutPredictionList(newSQLTimeoutPrediction);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/* 신규 SQL 타임아웃 예측 - 챠트 데이터 리스트 action */
	@RequestMapping(value = "/newSQLTimeoutPredictionChartList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String newSQLTimeoutPredictionChartList(
			@ModelAttribute("newSQLTimeoutPrediction") NewSQLTimeoutPrediction newSQLTimeoutPrediction, Model model) {
		List<NewSQLTimeoutPrediction> resultList = new ArrayList<NewSQLTimeoutPrediction>();

		try {
			resultList = predictiveFailureAnalysisService.newSQLTimeoutPredictionChartList(newSQLTimeoutPrediction);
			logger.debug("======================");
			logger.debug("신규 SQL 타임아웃 예측 - 챠트 데이터 리스트:" + resultList);
			logger.debug("======================");
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().toString();
	}
	
	/* 신규 SQL 타임아웃 예측 - 예측 예외 수정 */
	@RequestMapping(value = "/newSQLTimeoutPredictionExceptYnUpdate", method = RequestMethod.GET)
	@ResponseBody
	public Result newSQLTimeoutPredictionExceptYnUpdate(
			@ModelAttribute("newSQLTimeoutPredictionUpdate") NewSQLTimeoutPredictionUpdate newSQLTimeoutPredictionUpdate, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		
		try {
			rowCnt = predictiveFailureAnalysisService.newSQLTimeoutPredictionExceptYnUpdate(newSQLTimeoutPredictionUpdate);
			
			logger.debug("newSQLTimeoutPredictionUpdate rowCnt:"+rowCnt);
			if(rowCnt > 0){
				result.setResult(true);
				result.setMessage("예측 예외 수정이 변경하였습니다.");
			}else{
				result.setResult(false);
				result.setMessage("예측 예외 수정이 실패하였습니다.");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* SQL 정보 */
	@RequestMapping(value="/SQLInformationNSTP", method = RequestMethod.GET)
	public String sqlInformationNSTP(@ModelAttribute("odsHistSqlText") OdsHistSqlText odsHistSqlText, Model model) {
		return "predictiveFailure/sqlInformationNSTP";
	}
	
	/* Unknown SQL 장애 예측 */
	@RequestMapping(value = "/UnknownSQLFaultPrediction", method = RequestMethod.GET)
	public String UnknownSQLFaultPrediction(@ModelAttribute("unknownSQLFaultPrediction") UnknownSQLFaultPrediction unknownSQLFaultPrediction, Model model) {
		String today = DateUtil.getNowDate("yyyy-MM-dd");
		String yesterday = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", today,-1);
		String startDate = DateUtil.getPlusDays("yyyy-MM-dd","yyyy-MM-dd", yesterday,-6) ;

		if(unknownSQLFaultPrediction.getStart_first_analysis_day() == null || unknownSQLFaultPrediction.getStart_first_analysis_day().equals("")){
			unknownSQLFaultPrediction.setStart_first_analysis_day(startDate);
		}
		
		if(unknownSQLFaultPrediction.getEnd_first_analysis_day() == null || unknownSQLFaultPrediction.getEnd_first_analysis_day().equals("")){
			unknownSQLFaultPrediction.setEnd_first_analysis_day(yesterday);
		}
		
		model.addAttribute("menu_id", unknownSQLFaultPrediction.getMenu_id());
		model.addAttribute("menu_nm", unknownSQLFaultPrediction.getMenu_nm());
		
		return "predictiveFailure/unknownSQLFaultPrediction";
	}
	
	final String CPU_USAGE			= "CPU_USAGE";
	
	final String ELAPSED_TIME		= "ELAPSED_TIME";
	final String CPU_TIME			= "CPU_TIME";
	final String DISK_READS			= "DISK_READS";
	final String BUFFER_GETS		= "BUFFER_GETS";
	final String SORTS				= "SORTS";
	
	final String ADHOC_TOP_SQL		= "ADHOC_TOP_SQL";
	final String ADHOC_TOP_MODULE	= "ADHOC_TOP_MODULE";
	
	/* Unknown SQL 장애  예측 - 챠트 데이터 리스트 action */
	@RequestMapping(value = "/unknownSQLFaultPredictionChartList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String unknownSQLFaultPredictionChartList(
			@ModelAttribute("unknownSQLFaultPrediction") UnknownSQLFaultPrediction unknownSQLFaultPrediction, Model model) {
		Map param = new HashMap();
		List listall = new ArrayList();

		try {
			param.put("p_dbid", unknownSQLFaultPrediction.getDbid());
			param.put("p_inst_id", unknownSQLFaultPrediction.getInst_id());
			param.put("p_anal_begin_dt", unknownSQLFaultPrediction.getStart_first_analysis_day());
			param.put("p_anal_end_dt", unknownSQLFaultPrediction.getEnd_first_analysis_day());
			predictiveFailureAnalysisService.unknownSQLFaultPredictionChartList(param);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		Map stateDiffMap = separateStateDiff((List) param.get("p_cur_stat_diff"));
		
		String cpuUsageJson = getJSONResult((List<Map<String, Object>>) param.get("p_cur_cpuusage")).toJSONObject().toString();
		String elapsedTimeJson = getJSONResult((List<Map<String, Object>>) stateDiffMap.get(ELAPSED_TIME)).toJSONObject().toString();
		String cpuTimeJson = getJSONResult((List<Map<String, Object>>) stateDiffMap.get(CPU_TIME)).toJSONObject().toString();
		String diskReadsJson = getJSONResult((List<Map<String, Object>>) stateDiffMap.get(DISK_READS)).toJSONObject().toString();
		String bufferGetsJson = getJSONResult((List<Map<String, Object>>) stateDiffMap.get(BUFFER_GETS)).toJSONObject().toString();
		String sortsJson = getJSONResult((List<Map<String, Object>>) stateDiffMap.get(SORTS)).toJSONObject().toString();
		String adhocTopSqlJson = getJSONResult((List<Map<String, Object>>) param.get("p_cur_adhoc_top_sql")).toJSONObject().toString();
		String adhocTopModuleJson = getJSONResult((List<Map<String, Object>>) param.get("p_cur_adhoc_top_module")).toJSONObject().toString();
		
		
		Map aMap = new HashMap();
		aMap.put(CPU_USAGE, cpuUsageJson);
		aMap.put(ELAPSED_TIME, elapsedTimeJson);
		aMap.put(CPU_TIME, cpuTimeJson);
		aMap.put(DISK_READS, diskReadsJson);
		aMap.put(BUFFER_GETS, bufferGetsJson);
		aMap.put(SORTS, sortsJson);
		aMap.put(ADHOC_TOP_SQL, adhocTopSqlJson);
		aMap.put(ADHOC_TOP_MODULE, adhocTopModuleJson);
		
		JSONObject returnJson = new JSONObject();

		returnJson.put("unknownSqlStat", aMap);
		
		return returnJson.toString();
	}
	
	private Map<String, List<Map>> separateStateDiff(List stateDiff) {
		Map stateDiffMap = new HashMap();
		Map sectionMap = new HashMap();
		List elapsedTimeDiffList = new ArrayList();
		List cpuTimeDiffList = new ArrayList();
		List physicalReadsDiffList = new ArrayList();
		List logicalReadsDiffList = new ArrayList();
		List sortDiffList = new ArrayList();
		Map elapsedTimeDiffMap;
		Map cpuTimeDiffMap;
		Map physicalReadsDiffMap;
		Map logicalReadsDiffMap;
		Map sortDiffMap;
		int size = stateDiff.size();
		final String R_PGM_ELAPSED_TIME = "r_pgm_elapsed_time";
		final String A_PGM_ELAPSED_TIME = "a_pgm_elapsed_time";
		final String R_PGM_CPU_TIME = "r_pgm_cpu_time";
		final String A_PGM_CPU_TIME = "a_pgm_cpu_time";
		final String R_PGM_DISK_READS = "r_pgm_disk_reads";
		final String A_PGM_DISK_READS = "a_pgm_disk_reads";
		final String R_PGM_BUFFER_GETS = "r_pgm_buffer_gets";
		final String A_PGM_BUFFER_GETS = "a_pgm_buffer_gets";
		final String R_PGM_SORTS = "r_pgm_sorts";
		final String A_PGM_SORTS = "a_pgm_sorts";
		final String SNAP_TIME = "snap_time";
		
		String snapTime = "";
		
		for(int index = 0; index < size; index++) {
			elapsedTimeDiffMap = new HashMap();
			cpuTimeDiffMap = new HashMap();
			physicalReadsDiffMap = new HashMap();
			logicalReadsDiffMap = new HashMap();
			sortDiffMap = new HashMap();
			
			sectionMap = (Map) stateDiff.get(index);
			
			snapTime = sectionMap.get(SNAP_TIME) + "";
			
			if(snapTime == null || snapTime.equalsIgnoreCase("null")) {
				logger.debug("snapTime is null. index[" + index + "]");
			}
			
			elapsedTimeDiffMap.put(SNAP_TIME, snapTime);
			elapsedTimeDiffMap.put(R_PGM_ELAPSED_TIME, sectionMap.get(R_PGM_ELAPSED_TIME) + "");
			elapsedTimeDiffMap.put(A_PGM_ELAPSED_TIME, sectionMap.get(A_PGM_ELAPSED_TIME) + "");
			
			cpuTimeDiffMap.put(SNAP_TIME, snapTime);
			cpuTimeDiffMap.put(R_PGM_CPU_TIME, sectionMap.get(R_PGM_CPU_TIME) + "");
			cpuTimeDiffMap.put(A_PGM_CPU_TIME, sectionMap.get(A_PGM_CPU_TIME) + "");
			
			physicalReadsDiffMap.put(SNAP_TIME, snapTime);
			physicalReadsDiffMap.put(R_PGM_DISK_READS, sectionMap.get(R_PGM_DISK_READS) + "");
			physicalReadsDiffMap.put(A_PGM_DISK_READS, sectionMap.get(A_PGM_DISK_READS) + "");
			
			logicalReadsDiffMap.put(SNAP_TIME, snapTime);
			logicalReadsDiffMap.put(R_PGM_BUFFER_GETS, sectionMap.get(R_PGM_BUFFER_GETS) + "");
			logicalReadsDiffMap.put(A_PGM_BUFFER_GETS, sectionMap.get(A_PGM_BUFFER_GETS) + "");
			
			sortDiffMap.put(SNAP_TIME, snapTime);
			sortDiffMap.put(R_PGM_SORTS, sectionMap.get(R_PGM_SORTS) + "");
			sortDiffMap.put(A_PGM_SORTS, sectionMap.get(A_PGM_SORTS) + "");
			
			elapsedTimeDiffList.add(elapsedTimeDiffMap);
			cpuTimeDiffList.add(cpuTimeDiffMap);
			physicalReadsDiffList.add(physicalReadsDiffMap);
			logicalReadsDiffList.add(logicalReadsDiffMap);
			sortDiffList.add(sortDiffMap);
		}
		
		stateDiffMap.put(ELAPSED_TIME, elapsedTimeDiffList);
		stateDiffMap.put(CPU_TIME, cpuTimeDiffList);
		stateDiffMap.put(DISK_READS, physicalReadsDiffList);
		stateDiffMap.put(BUFFER_GETS, logicalReadsDiffList);
		stateDiffMap.put(SORTS, sortDiffList);
		
		return stateDiffMap;
	}
	
	/* 정규 SQL Parsing Schema Name 필터링 조건 */
	
	/* Parsing Schema Name 콤보박스 조회 */
	@RequestMapping(value = "/selectParsingSchemaNameComboBox", method = RequestMethod.GET)
	@ResponseBody
	public String selectParsingSchemaNameComboBox(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase) {
		List<RegularSQLFilterCase> resultList = new ArrayList<RegularSQLFilterCase>();
		
		try {
			resultList = predictiveFailureAnalysisService.selectParsingSchemaNameComboBox(regularSQLFilterCase);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 정규 SQL Parsing Schema Name 필터링 조건 조회 */
	@RequestMapping(value = "/selectRegularSQLParsingSchemaNameFilteringCase", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectRegularSQLParsingSchemaNameFilteringCase(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase, Model model) {
		List<RegularSQLFilterCase> resultList = new ArrayList<RegularSQLFilterCase>();
		
		try {
			resultList = predictiveFailureAnalysisService.selectRegularSQLParsingSchemaNameFilteringCase(regularSQLFilterCase);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/* 정규 SQL 대상 사용자 추가 */
	@RequestMapping(value = "/insertRegularSQLTargetUser", method = RequestMethod.POST)
	@ResponseBody
	public Result insertRegularSQLTargetUser(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase, Model model) {
		Result result = new Result();
		int insertRow = 0;
		
		try {
			insertRow = predictiveFailureAnalysisService.insertRegularSQLTargetUser(regularSQLFilterCase);
			
			result.setResult(insertRow > 0 ? true : false);
			result.setMessage("정규 SQL 대상 사용자 등록이 성공하였습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage("정규 SQL 대상 사용자 등록이 실패하였습니다.");
		}

		return result;
	}
	
	
	/* 삭제 */
	@RequestMapping(value = "/deleteRegularSQLTargetUser", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteRegularSQLTargetUser(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase, Model model) {
		Result result = new Result();
		int deleteRow = 0;
		
		try {
			deleteRow = predictiveFailureAnalysisService.deleteRegularSQLTargetUser(regularSQLFilterCase);
			
			result.setResult(deleteRow > 0 ? true : false);
			result.setMessage("정규 SQL 대상 사용자 삭제가 성공하였습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage("정규 SQL 대상 사용자 삭제가 실패하였습니다.");
		}

		return result;
	}
	
	/* 정규 SQL 모듈 필터링 조건 */
	
	/* 정규 SQL 모듈 필터링 조건 조회 */
	@RequestMapping(value = "/selectRegularSQLModuleFilterCombobox", method = RequestMethod.GET)
	@ResponseBody
	public String selectRegularSQLModuleFilterCombobox(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase) {
		List<RegularSQLFilterCase> resultList = new ArrayList<RegularSQLFilterCase>();
		
		try {
			resultList = predictiveFailureAnalysisService.selectRegularSQLModuleFilterCombobox();
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/* 정규 SQL 모듈 필터링 조건 조회 */
	@RequestMapping(value = "/selectRegularSQLModuleFilterCase", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectRegularSQLModuleFilterCase(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase, Model model) {
		List<RegularSQLFilterCase> resultList = new ArrayList<RegularSQLFilterCase>();
		
		try {
			resultList = predictiveFailureAnalysisService.selectRegularSQLModuleFilterCase(regularSQLFilterCase);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/* 
	2) 추가 
	- 추가하기전에 기 등록된 필터링조건이 있는제 체크
	- 기 등록된 필터링조건이 있으면 "필터링조건이 이미등록되었습니다." 메시지 출력
	- 기 등록되지 않은 필터링 조건이면 저장
	*/
	
	/* 기등록된 필터링 조건 체크  */
	@RequestMapping(value = "/checkRegisteredRegularSQLModuleFilterCase", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String checkRegisteredRegularSQLModuleFilterCase(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase, Model model) {
		List<RegularSQLFilterCase> resultList = new ArrayList<RegularSQLFilterCase>();
		
		try {
			resultList = predictiveFailureAnalysisService.checkRegisteredRegularSQLModuleFilterCase(regularSQLFilterCase);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/* 기 등록되지 않은 필터링 조건이면 저장 */
	@RequestMapping(value = "/insertRegularSQLModuleFilterCase", method = RequestMethod.POST)
	@ResponseBody
	public Result insertRegularSQLModuleFilterCase(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase, Model model) {
		Result result = new Result();
		int insertRow = 0;
		
		try {
			insertRow = predictiveFailureAnalysisService.insertRegularSQLModuleFilterCase(regularSQLFilterCase);
			logger.debug("insertRow:"+insertRow);
			
			result.setResult(insertRow > 0 ? true : false);
			result.setMessage("정규 SQL 대상 필터 조건 등록이 성공하였습니다.");
		} catch (Exception ex) {
			String methodName = new Object() { 
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage("정규 SQL 대상 필터 조건 등록이 실패하였습니다.");
		}

		return result;
	}
	
	/* 삭제 */
	@RequestMapping(value = "/deleteRegularSQLModuleFilterCase", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteRegularSQLModuleFilterCase(
			@ModelAttribute("regularSQLFilterCase") RegularSQLFilterCase regularSQLFilterCase, Model model) {
		Result result = new Result();
		int deleteRow = 0;
		
		try {
			deleteRow = predictiveFailureAnalysisService.deleteRegularSQLModuleFilterCase(regularSQLFilterCase);

			result.setResult(deleteRow > 0 ? true : false);
			result.setMessage("정규 SQL 대상 필터 조건 삭제가 성공하였습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage("정규 SQL 대상 사용자 삭제가 실패하였습니다.");
		}

		return result;
	}

}