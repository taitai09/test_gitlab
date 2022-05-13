package omc.spop.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.model.ObjectChange;
import omc.spop.model.Result;
import omc.spop.service.ObjectAnalysisService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.06.28 bks OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class ObjectAnalysisController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(ObjectAnalysisController.class);

	@Autowired
	private ObjectAnalysisService objectAnalysisService;

	/* 오브젝트변경분석 */
	@RequestMapping(value = "/ObjectChangeAnalysis_20180709", method = RequestMethod.GET)
	public String ObjectChangeAnalysis_20180709(@ModelAttribute("objectChange") ObjectChange objectChange, Model model,
			@RequestParam(required = true) Map<String, Object> param) {

		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");

		model.addAttribute("nowDate", nowDate);
		model.addAttribute("menu_id", objectChange.getMenu_id());
		model.addAttribute("menu_nm", objectChange.getMenu_nm());

		return "objectAnalysis/objectChangeAnalysis";
		
	}
	
	@RequestMapping(value = "/ObjectChangeAnalysis", method = RequestMethod.GET)
	public ModelAndView ObjectChangeAnalysis(@ModelAttribute("objectChange") ObjectChange objectChange, 
			@RequestParam(required = true) Map<String, Object> param) {

		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		Map<String, Object> model = param;

		model.put("nowDate", nowDate);
		model.put("menu_id", objectChange.getMenu_id());
		model.put("menu_nm", objectChange.getMenu_nm());

		String rstUrl = "objectAnalysis/objectChangeAnalysis";
		return new ModelAndView(rstUrl, model);		
	}	

	/* 오브젝트변경분석 action 차트 */
	@RequestMapping(value = "/ObjectChangeAnalysis/Chart1", method = RequestMethod.POST)
	@ResponseBody
	public Result ObjectChangeAnalysisChartAction1(Model model,
			@RequestParam(required = true) Map<String, Object> param) {
		String base_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		param.put("base_day", base_day);

		Result result = new Result();
		String base_day_gubun = (String) param.get("base_day_gubun");
		String dbid = (String) param.get("dbid");
		logger.debug("param:" + param);
		if (!dbid.equals("") && !base_day_gubun.equals("")) {
			try {
				result = objectAnalysisService.objectChangeChartList(param);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}
		}

		return result;
	}

	/* 오브젝트변경분석 action 차트 */
	@RequestMapping(value = "/ObjectChangeAnalysis/Chart", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ObjectChangeAnalysisChartAction(Model model,
			@RequestParam(required = true) Map<String, Object> param) {
		String base_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		param.put("base_day", base_day);

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String base_day_gubun = (String) param.get("base_day_gubun");
		String dbid = (String) param.get("dbid");
		logger.debug("param:" + param);
		if (!dbid.equals("") && !base_day_gubun.equals("")) {
			try {
				resultList = objectAnalysisService.ObjectChangeAnalysisChartList(param);
				logger.debug("======================");
				// logger.debug("오브젝트 변경 현황 차트
				// 목록:"+success(resultList).toJSONObject().toString());
				logger.debug("======================");
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}

		return getJSONResult(resultList).toJSONObject().toString();
	}

	/* 오브젝트변경분석 action 테이블 변경 */
	@RequestMapping(value = "/ObjectChangeAnalysis/Table", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ObjectChangeAnalysisTableAction(Model model,
			@RequestParam(required = true) Map<String, Object> param) {
		String base_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		param.put("base_day", base_day);
		List<ObjectChange> resultList = new ArrayList<ObjectChange>();
		String base_day_gubun = (String) param.get("base_day_gubun");
		String dbid = (String) param.get("dbid");
		logger.debug("param:" + param);
		if (!dbid.equals("") && !base_day_gubun.equals("")) {
			try {
				resultList = objectAnalysisService.tableChangeList(param);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 오브젝트변경분석 action 테이블 변경 */
	@RequestMapping(value = "/ObjectChangeAnalysis/Column", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ObjectChangeAnalysisColumnAction(Model model,
			@RequestParam(required = true) Map<String, Object> param) {
		String base_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		param.put("base_day", base_day);
		List<ObjectChange> resultList = new ArrayList<ObjectChange>();
		String base_day_gubun = (String) param.get("base_day_gubun");
		String dbid = (String) param.get("dbid");
		logger.debug("param:" + param);
		if (!dbid.equals("") && !base_day_gubun.equals("")) {

			try {
				resultList = objectAnalysisService.columnChangeList(param);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}
		return success(resultList).toJSONObject().toString();
	}

	/* 오브젝트변경분석 action 인덱스 변경 */
	@RequestMapping(value = "/ObjectChangeAnalysis/Index", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ObjectChangeAnalysisIndexAction(Model model,
			@RequestParam(required = true) Map<String, Object> param) {
		String base_day = DateUtil.getPlusDays("yyyyMMdd", "yyyyMMdd", DateUtil.getNowDate("yyyyMMdd"), -1);
		param.put("base_day", base_day);
		List<ObjectChange> resultList = new ArrayList<ObjectChange>();
		String base_day_gubun = (String) param.get("base_day_gubun");
		String dbid = (String) param.get("dbid");
		logger.debug("param:" + param);
		if (!dbid.equals("") && !base_day_gubun.equals("")) {

			try {
				resultList = objectAnalysisService.indexChangeList(param);
			} catch (Exception ex) {
				String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
				logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				return getErrorJsonString(ex);
			}
		}
		return success(resultList).toJSONObject().toString();
	}

}