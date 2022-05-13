package omc.spop.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.AlertErrorCheck;
import omc.spop.model.AsmDiskgroupCheck;
import omc.spop.model.BlockCorruptionCheck;
import omc.spop.model.ChainedRowCheck;
import omc.spop.model.ConstraintCheck;
import omc.spop.model.DailyCheck;
import omc.spop.model.DatabaseDsbCheck;
import omc.spop.model.DbCacheAdvisor;
import omc.spop.model.DbCheckException;
import omc.spop.model.DbCheckExec;
import omc.spop.model.DbFileCheck;
import omc.spop.model.DbUserCheck;
import omc.spop.model.DiagIncident;
import omc.spop.model.DiagProblem;
import omc.spop.model.FilesystemSpaceCheck;
import omc.spop.model.FkIndexCheck;
import omc.spop.model.FraSpaceCheck;
import omc.spop.model.FraSpaceUsage;
import omc.spop.model.HmRecommendation;
import omc.spop.model.InstanceDsbCheck;
import omc.spop.model.InstanceEfficiencyCheck;
import omc.spop.model.ListenerCheck;
import omc.spop.model.LongRunningOperationCheck;
import omc.spop.model.LongRunningSchedulerCheck;
import omc.spop.model.NewCreateObjectCheck;
import omc.spop.model.ObjectCheck;
import omc.spop.model.OutstandingAlerts;
import omc.spop.model.ParameterChangeCheck;
import omc.spop.model.PgaTargetAdvisor;
import omc.spop.model.RecyclebinCheck;
import omc.spop.model.ResourceLimitCheck;
import omc.spop.model.Result;
import omc.spop.model.SchedulerJobFailedCheck;
import omc.spop.model.SegmentAdvisor;
import omc.spop.model.SequenceCheck;
import omc.spop.model.SgaTargetAdvisor;
import omc.spop.model.SharedPoolAdvisor;
import omc.spop.model.SqlAutoTuningRecommendation;
import omc.spop.model.TableStatisticsCheck;
import omc.spop.model.TableStatisticsLockCheck;
import omc.spop.model.TablespaceCheck;
import omc.spop.model.UnusableIndexCheck;
import omc.spop.service.CommonService;
import omc.spop.service.PreventiveCheckService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.04.18 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class PreventiveCheckController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(PreventiveCheckController.class);

	@Autowired
	private PreventiveCheckService preventiveCheckService;

	@Autowired
	private CommonService commonService;

	/* 일 예방 점검 */
	@RequestMapping(value = "/PreventiveCheck", method = RequestMethod.GET)
	public String PreventiveCheck(@ModelAttribute("dailyCheck") DailyCheck dailyCheck, Model model) throws Exception {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
//		String nowDate = DateUtil.getPlusDays("yyyy-MM-dd", "yyyy-MM-dd", DateUtil.getNowDate("yyyy-MM-dd"), -1);
		String menu_id = StringUtils.defaultString(dailyCheck.getMenu_id());
		String menu_nm = StringUtils.defaultString(dailyCheck.getMenu_nm());
		String check_day = StringUtils.defaultString(dailyCheck.getCheck_day());
		if (check_day.equals("")) {
			model.addAttribute("nowDate", nowDate);
		} else {
			model.addAttribute("nowDate", check_day);
		}
		String check_seq = StringUtils.defaultString(dailyCheck.getCheck_seq());
		if (check_seq.length() > 0) {
			model.addAttribute("check_seq", check_seq);
		}
		model.addAttribute("menu_id", menu_id);
		model.addAttribute("menu_nm", menu_nm);
		model.addAttribute("check_day", check_day);
		model.addAttribute("call_from_parent", dailyCheck.getCall_from_parent());

		String db_name = StringUtils.defaultString(dailyCheck.getDb_name());
		String dbid = StringUtils.defaultString(dailyCheck.getDbid());
		logger.debug("PreventiveCheck dbid:" + dbid);
		if (!db_name.equals("") && dbid.equals("")) {
			dbid = commonService.getDbidByDbName(db_name);
		}
		model.addAttribute("dbid", dbid);

		logger.debug("PreventiveCheck check_day:" + check_day + " dbid:" + dbid + " check_seq:" + check_seq);

		return "dailyCheck/summary";
	}

	/* 일 예방 점검 - 점검회차 조회 */
	@RequestMapping(value = "/PreventiveCheck/GetCheckSeq", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String GetCheckSeq(@ModelAttribute("dbCheckExec") DbCheckExec dbCheckExec) throws Exception {
		String dbid = StringUtils.defaultString(dbCheckExec.getDbid());
		List<DbCheckExec> checkSeqList = new ArrayList<DbCheckExec>();
		try {
			if (dbid.equals("")) {
				checkSeqList = preventiveCheckService.checkSeqListAll(dbCheckExec);
			} else {
				checkSeqList = preventiveCheckService.checkSeqList(dbCheckExec);
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return success(checkSeqList).toJSONObject().get("rows").toString();
	}

	/* 일 예방 점검 - 요약 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/Summary", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String Summary(@ModelAttribute("dailyCheck") DailyCheck dailyCheck, Model model) {
		List<DailyCheck> resultList = new ArrayList<DailyCheck>();

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		dailyCheck.setUser_id(user_id);

		try {
			resultList = preventiveCheckService.summaryList(dailyCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - 상세 점검 내역 */
	@RequestMapping(value = "/PreventiveCheck/DetailCheckInfo", method = RequestMethod.GET)
	public String DetailCheckInfo(@ModelAttribute("dailyCheck") DailyCheck dailyCheck, Model model) {
		logger.debug("일 예방 점검 :" + "dailyCheck/" + dailyCheck.getCheck_item_name());
		model.addAttribute("menu_id", dailyCheck.getMenu_id());
		model.addAttribute("menu_nm", dailyCheck.getMenu_nm());
		return "dailyCheck/" + dailyCheck.getCheck_item_name();
	}

	/* 일 예방 점검 - Advisor Recommendations - SQL Tuning ADVISOR 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AdvisorRecommendations/SQLTuningAdvisor", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SQLTuningAdvisor(
			@ModelAttribute("sqlAutoTuningRecommendation") SqlAutoTuningRecommendation sqlAutoTuningRecommendation,
			Model model) {
		List<SqlAutoTuningRecommendation> resultList = new ArrayList<SqlAutoTuningRecommendation>();

		try {
			resultList = preventiveCheckService.sqlTuningAdvisorList(sqlAutoTuningRecommendation);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Advisor Recommendations - SGA ADVISOR 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AdvisorRecommendations/SgaAdvisor", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SgaAdvisor(@ModelAttribute("sgaTargetAdvisor") SgaTargetAdvisor sgaTargetAdvisor, Model model) {
		List<SgaTargetAdvisor> resultList = new ArrayList<SgaTargetAdvisor>();

		try {
			resultList = preventiveCheckService.sgaAdvisorList(sgaTargetAdvisor);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Advisor Recommendations - Buffer Cache ADVISOR 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AdvisorRecommendations/BufferCacheAdvisor", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String BufferCacheAdvisor(@ModelAttribute("dbCacheAdvisor") DbCacheAdvisor dbCacheAdvisor, Model model) {
		List<DbCacheAdvisor> resultList = new ArrayList<DbCacheAdvisor>();

		try {
			resultList = preventiveCheckService.bufferCacheAdvisorList(dbCacheAdvisor);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Advisor Recommendations - Shared Pool ADVISOR 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AdvisorRecommendations/SharedPoolAdvisor", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SharedPoolAdvisor(@ModelAttribute("sharedPoolAdvisor") SharedPoolAdvisor sharedPoolAdvisor,
			Model model) {
		List<SharedPoolAdvisor> resultList = new ArrayList<SharedPoolAdvisor>();

		try {
			resultList = preventiveCheckService.sharedPoolAdvisorList(sharedPoolAdvisor);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Advisor Recommendations - PGA ADVISOR 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AdvisorRecommendations/PgaAdvisor", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String PgaAdvisor(@ModelAttribute("pgaTargetAdvisor") PgaTargetAdvisor pgaTargetAdvisor, Model model) {
		List<PgaTargetAdvisor> resultList = new ArrayList<PgaTargetAdvisor>();

		try {
			resultList = preventiveCheckService.pgaAdvisorList(pgaTargetAdvisor);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Advisor Recommendations - SEGMENT ADVISOR 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AdvisorRecommendations/SegmentAdvisor", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String SegmentAdvisor(@ModelAttribute("segmentAdvisor") SegmentAdvisor segmentAdvisor, Model model) {
		List<SegmentAdvisor> resultList = new ArrayList<SegmentAdvisor>();

		try {
			resultList = preventiveCheckService.segmentAdvisorList(segmentAdvisor);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/*
	 * 일 예방 점검 - Advisor Recommendations - Health Monitor Recommendation 리스트 action
	 */
	@RequestMapping(value = "/PreventiveCheck/AdvisorRecommendations/HealthMonitor", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String HealthMonitor(@ModelAttribute("hmRecommendation") HmRecommendation hmRecommendation, Model model) {
		List<HmRecommendation> resultList = new ArrayList<HmRecommendation>();

		try {
			resultList = preventiveCheckService.healthMonitorList(hmRecommendation);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - IDatabaseStatus 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/DatabaseStatus", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DatabaseStatus(@ModelAttribute("databaseDsbCheck") DatabaseDsbCheck databaseDsbCheck, Model model) {
		List<DatabaseDsbCheck> resultList = new ArrayList<DatabaseDsbCheck>();

		try {
			resultList = preventiveCheckService.databaseStatusList(databaseDsbCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ExpiredGraceAccount 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ExpiredGraceAccount", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ExpiredGraceAccount(@ModelAttribute("dbUserCheck") DbUserCheck dbUserCheck, Model model) {
		List<DbUserCheck> resultList = new ArrayList<DbUserCheck>();

		try {
			resultList = preventiveCheckService.expiredGraceAccountList(dbUserCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ModifiedParameter 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ModifiedParameter", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ModifiedParameter(@ModelAttribute("parameterChangeCheck") ParameterChangeCheck parameterChangeCheck,
			Model model) {
		List<ParameterChangeCheck> resultList = new ArrayList<ParameterChangeCheck>();

		try {
			resultList = preventiveCheckService.modifiedParameterList(parameterChangeCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - NewCreatedObject 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/NewCreatedObject", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String NewCreatedObject(@ModelAttribute("newCreateObjectCheck") NewCreateObjectCheck newCreateObjectCheck,
			Model model) {
		List<NewCreateObjectCheck> resultList = new ArrayList<NewCreateObjectCheck>();

		try {
			resultList = preventiveCheckService.newCreatedObjectList(newCreateObjectCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - InstanceStatus 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/InstanceStatus", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String InstanceStatus(@ModelAttribute("instanceDsbCheck") InstanceDsbCheck instanceDsbCheck, Model model) {
		List<InstanceDsbCheck> resultList = new ArrayList<InstanceDsbCheck>();

		try {
			resultList = preventiveCheckService.instanceStatusList(instanceDsbCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ListenerStatus 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ListenerStatus", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ListenerStatus(@ModelAttribute("listenerCheck") ListenerCheck listenerCheck, Model model) {
		List<ListenerCheck> resultList = new ArrayList<ListenerCheck>();

		try {
			resultList = preventiveCheckService.listenerStatusList(listenerCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Dbfiles 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/Dbfiles", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String Dbfiles(@ModelAttribute("dbFileCheck") DbFileCheck dbFileCheck, Model model) {
		List<DbFileCheck> resultList = new ArrayList<DbFileCheck>();

		try {
			resultList = preventiveCheckService.dbfilesList(dbFileCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - LibraryCacheHit 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/LibraryCacheHit", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String LibraryCacheHit(
			@ModelAttribute("instanceEfficiencyCheck") InstanceEfficiencyCheck instanceEfficiencyCheck, Model model) {
		List<InstanceEfficiencyCheck> resultList = new ArrayList<InstanceEfficiencyCheck>();

		try {
			resultList = preventiveCheckService.libraryCacheHitList(instanceEfficiencyCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - DictionaryCacheHit 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/DictionaryCacheHit", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DictionaryCacheHit(
			@ModelAttribute("instanceEfficiencyCheck") InstanceEfficiencyCheck instanceEfficiencyCheck, Model model) {
		List<InstanceEfficiencyCheck> resultList = new ArrayList<InstanceEfficiencyCheck>();

		try {
			resultList = preventiveCheckService.dictionaryCacheHitList(instanceEfficiencyCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - BufferCacheHit 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/BufferCacheHit", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String BufferCacheHit(
			@ModelAttribute("instanceEfficiencyCheck") InstanceEfficiencyCheck instanceEfficiencyCheck, Model model) {
		List<InstanceEfficiencyCheck> resultList = new ArrayList<InstanceEfficiencyCheck>();

		try {
			resultList = preventiveCheckService.bufferCacheHitList(instanceEfficiencyCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - LatchHit 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/LatchHit", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String LatchHit(@ModelAttribute("instanceEfficiencyCheck") InstanceEfficiencyCheck instanceEfficiencyCheck,
			Model model) {
		List<InstanceEfficiencyCheck> resultList = new ArrayList<InstanceEfficiencyCheck>();

		try {
			resultList = preventiveCheckService.latchHitList(instanceEfficiencyCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ParseCpuToParseElapsd 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ParseCpuToParseElapsd", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ParseCpuToParseElapsd(
			@ModelAttribute("instanceEfficiencyCheck") InstanceEfficiencyCheck instanceEfficiencyCheck, Model model) {
		List<InstanceEfficiencyCheck> resultList = new ArrayList<InstanceEfficiencyCheck>();

		try {
			resultList = preventiveCheckService.parseCpuToParseElapsdList(instanceEfficiencyCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - DiskSort 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/DiskSort", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DiskSort(@ModelAttribute("instanceEfficiencyCheck") InstanceEfficiencyCheck instanceEfficiencyCheck,
			Model model) {
		List<InstanceEfficiencyCheck> resultList = new ArrayList<InstanceEfficiencyCheck>();

		try {
			resultList = preventiveCheckService.diskSortList(instanceEfficiencyCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - MemoryUsage 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/MemoryUsage", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String MemoryUsage(
			@ModelAttribute("instanceEfficiencyCheck") InstanceEfficiencyCheck instanceEfficiencyCheck, Model model) {
		List<InstanceEfficiencyCheck> resultList = new ArrayList<InstanceEfficiencyCheck>();

		try {
			resultList = preventiveCheckService.memoryUsageList(instanceEfficiencyCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ResourceLimit 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ResourceLimit", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ResourceLimit(@ModelAttribute("resourceLimitCheck") ResourceLimitCheck resourceLimitCheck,
			Model model) {
		List<ResourceLimitCheck> resultList = new ArrayList<ResourceLimitCheck>();

		try {
			resultList = preventiveCheckService.resourceLimitList(resourceLimitCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - BackgroundDumpSpace 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/BackgroundDumpSpace", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String BackgroundDumpSpace(@ModelAttribute("filesystemSpaceCheck") FilesystemSpaceCheck filesystemSpaceCheck,
			Model model) {
		List<FilesystemSpaceCheck> resultList = new ArrayList<FilesystemSpaceCheck>();

		try {
			resultList = preventiveCheckService.backgroundDumpSpaceList(filesystemSpaceCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ArchiveLogSpace 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ArchiveLogSpace", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ArchiveLogSpace(@ModelAttribute("filesystemSpaceCheck") FilesystemSpaceCheck filesystemSpaceCheck,
			Model model) {
		List<FilesystemSpaceCheck> resultList = new ArrayList<FilesystemSpaceCheck>();

		try {
			resultList = preventiveCheckService.archiveLogSpaceList(filesystemSpaceCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - AlertLogSpace 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AlertLogSpace", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String AlertLogSpace(@ModelAttribute("filesystemSpaceCheck") FilesystemSpaceCheck filesystemSpaceCheck,
			Model model) {
		List<FilesystemSpaceCheck> resultList = new ArrayList<FilesystemSpaceCheck>();

		try {
			resultList = preventiveCheckService.alertLogSpaceList(filesystemSpaceCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - FraSpace 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/FraSpace", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String FraSpace(@ModelAttribute("fraSpaceCheck") FraSpaceCheck fraSpaceCheck, Model model) {
		List<FraSpaceCheck> resultList = new ArrayList<FraSpaceCheck>();

		try {
			resultList = preventiveCheckService.fraSpaceList(fraSpaceCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Fra Usage 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/FraUsage", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String FraUsage(@ModelAttribute("fraSpaceUsage") FraSpaceUsage fraSpaceUsage, Model model) {
		List<FraSpaceUsage> resultList = new ArrayList<FraSpaceUsage>();

		try {
			resultList = preventiveCheckService.fraUsageList(fraSpaceUsage);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - AsmDiskgroupSpace 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AsmDiskgroupSpace", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String AsmDiskgroupSpace(@ModelAttribute("asmDiskgroupCheck") AsmDiskgroupCheck asmDiskgroupCheck,
			Model model) {
		List<AsmDiskgroupCheck> resultList = new ArrayList<AsmDiskgroupCheck>();

		try {
			resultList = preventiveCheckService.asmDiskgroupSpaceList(asmDiskgroupCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Tablespace 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/Tablespace", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String Tablespace(@ModelAttribute("tablespaceCheck") TablespaceCheck tablespaceCheck, Model model) {
		List<TablespaceCheck> resultList = new ArrayList<TablespaceCheck>();

		try {
			resultList = preventiveCheckService.tablespaceList(tablespaceCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - RecyclebinObject count/size action */
	@RequestMapping(value = "/PreventiveCheck/RecyclebinObjectCount", method = RequestMethod.POST)
	@ResponseBody
	public Result RecyclebinObjectCount(@ModelAttribute("recyclebinCheck") RecyclebinCheck recyclebinCheck,
			Model model) {
		Result result = new Result();
		RecyclebinCheck resultObject = new RecyclebinCheck();

		try {
			resultObject = preventiveCheckService.getRecyclebinObjectCount(recyclebinCheck);
			result.setResult(true);
			result.setObject(resultObject);
		} catch (Exception ex) {
			logger.error("error ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* 일 예방 점검 - RecyclebinObject 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/RecyclebinObject", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String RecyclebinObject(@ModelAttribute("recyclebinCheck") RecyclebinCheck recyclebinCheck, Model model) {
		List<RecyclebinCheck> resultList = new ArrayList<RecyclebinCheck>();

		try {
			resultList = preventiveCheckService.recyclebinObjectList(recyclebinCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - InvalidObject 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/InvalidObject", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String InvalidObject(@ModelAttribute("objectCheck") ObjectCheck objectCheck, Model model) {
		List<ObjectCheck> resultList = new ArrayList<ObjectCheck>();

		try {
			resultList = preventiveCheckService.invalidObjectList(objectCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - NologgingObject 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/NologgingObject", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String NologgingObject(@ModelAttribute("objectCheck") ObjectCheck objectCheck, Model model) {
		List<ObjectCheck> resultList = new ArrayList<ObjectCheck>();

		try {
			resultList = preventiveCheckService.nologgingObjectList(objectCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ParallelObject 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ParallelObject", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ParallelObject(@ModelAttribute("objectCheck") ObjectCheck objectCheck, Model model) {
		List<ObjectCheck> resultList = new ArrayList<ObjectCheck>();

		try {
			resultList = preventiveCheckService.parallelObjectList(objectCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - UnusableIndex 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/UnusableIndex", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String UnusableIndex(@ModelAttribute("unusableIndexCheck") UnusableIndexCheck unusableIndexCheck,
			Model model) {
		List<UnusableIndexCheck> resultList = new ArrayList<UnusableIndexCheck>();

		try {
			resultList = preventiveCheckService.unusableIndexList(unusableIndexCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ChainedRows 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ChainedRows", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ChainedRows(@ModelAttribute("chainedRowCheck") ChainedRowCheck chainedRowCheck, Model model) {
		List<ChainedRowCheck> resultList = new ArrayList<ChainedRowCheck>();

		try {
			resultList = preventiveCheckService.chainedRowsList(chainedRowCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - CorruptBlock 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/CorruptBlock", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String CorruptBlock(@ModelAttribute("blockCorruptionCheck") BlockCorruptionCheck blockCorruptionCheck,
			Model model) {
		List<BlockCorruptionCheck> resultList = new ArrayList<BlockCorruptionCheck>();

		try {
			resultList = preventiveCheckService.corruptBlockList(blockCorruptionCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - Sequence 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/Sequence", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String Sequence(@ModelAttribute("sequenceCheck") SequenceCheck sequenceCheck, Model model) {
		List<SequenceCheck> resultList = new ArrayList<SequenceCheck>();

		try {
			resultList = preventiveCheckService.sequenceList(sequenceCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ForeignkeysWithoutIndex 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ForeignkeysWithoutIndex", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ForeignkeysWithoutIndex(@ModelAttribute("fkIndexCheck") FkIndexCheck fkIndexCheck, Model model) {
		List<FkIndexCheck> resultList = new ArrayList<FkIndexCheck>();

		try {
			resultList = preventiveCheckService.foreignkeysWithoutIndexList(fkIndexCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - DisabledConstraint 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/DisabledConstraint", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DisabledConstraint(@ModelAttribute("constraintCheck") ConstraintCheck constraintCheck, Model model) {
		List<ConstraintCheck> resultList = new ArrayList<ConstraintCheck>();

		try {
			resultList = preventiveCheckService.disabledConstraintList(constraintCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - MissingOrStaleStatistics 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/MissingOrStaleStatistics", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String MissingOrStaleStatistics(
			@ModelAttribute("tableStatisticsCheck") TableStatisticsCheck tableStatisticsCheck, Model model) {
		List<TableStatisticsCheck> resultList = new ArrayList<TableStatisticsCheck>();

		int dataCount4NextBtn = 0;
		try {
			resultList = preventiveCheckService.missingOrStaleStatisticsList(tableStatisticsCheck);
			if (resultList != null && resultList.size() > tableStatisticsCheck.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(tableStatisticsCheck.getPagePerCount());
			}
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}

	@RequestMapping(value = "/PreventiveCheck/MissingOrStaleStatistics/ExcelDown")
	@ResponseBody
	public ModelAndView MissingOrStaleStatistics4Excel(
			@ModelAttribute("tableStatisticsCheck") TableStatisticsCheck tableStatisticsCheck, Model model)
			throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();

		try {
			resultList = preventiveCheckService.missingOrStaleStatisticsList4Excel(tableStatisticsCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "MISSING_OR_STALE_STATISTICS");
		model.addAttribute("sheetName", "MISSING OR STALE STATISTICS");
		model.addAttribute("excelId", "MISSING_OR_STALE_STATISTICS");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}

	/* 일 예방 점검 - StatisticsLockedTable 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/StatisticsLockedTable", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String StatisticsLockedTable(
			@ModelAttribute("tableStatisticsLockCheck") TableStatisticsLockCheck tableStatisticsLockCheck,
			Model model) {
		List<TableStatisticsLockCheck> resultList = new ArrayList<TableStatisticsLockCheck>();

		try {
			resultList = preventiveCheckService.statisticsLockedTableList(tableStatisticsLockCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - LongRunningOperation 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/LongRunningOperation", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String LongRunningOperation(
			@ModelAttribute("longRunningOperationCheck") LongRunningOperationCheck longRunningOperationCheck,
			Model model) {
		List<LongRunningOperationCheck> resultList = new ArrayList<LongRunningOperationCheck>();

		try {
			resultList = preventiveCheckService.longRunningOperationList(longRunningOperationCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - LongRunningJob 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/LongRunningJob", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String LongRunningJob(
			@ModelAttribute("longRunningSchedulerCheck") LongRunningSchedulerCheck longRunningSchedulerCheck,
			Model model) {
		List<LongRunningSchedulerCheck> resultList = new ArrayList<LongRunningSchedulerCheck>();

		try {
			resultList = preventiveCheckService.longRunningJobList(longRunningSchedulerCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - AlertLogError 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/AlertLogError", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String AlertLogError(@ModelAttribute("alertErrorCheck") AlertErrorCheck alertErrorCheck, Model model) {
		List<AlertErrorCheck> resultList = new ArrayList<AlertErrorCheck>();

		try {
			resultList = preventiveCheckService.alertLogErrorList(alertErrorCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ActiveIncident-Problem 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ActiveIncident/Problem", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ActiveIncidentProblem(@ModelAttribute("diagProblem") DiagProblem diagProblem, Model model) {
		List<DiagProblem> resultList = new ArrayList<DiagProblem>();

		try {
			resultList = preventiveCheckService.activeIncidentProblemList(diagProblem);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - ActiveIncident-Incident 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/ActiveIncident/Incident", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String ActiveIncidentIncident(@ModelAttribute("diagIncident") DiagIncident diagIncident, Model model) {
		List<DiagIncident> resultList = new ArrayList<DiagIncident>();

		try {
			resultList = preventiveCheckService.activeIncidentIncidentList(diagIncident);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - OutstandingAlert 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/OutstandingAlert", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String OutstandingAlert(@ModelAttribute("outstandingAlerts") OutstandingAlerts outstandingAlerts,
			Model model) {
		List<OutstandingAlerts> resultList = new ArrayList<OutstandingAlerts>();

		try {
			resultList = preventiveCheckService.outstandingAlertList(outstandingAlerts);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - DbmsSchedulerJobFailed 리스트 action */
	@RequestMapping(value = "/PreventiveCheck/DbmsSchedulerJobFailed", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String DbmsSchedulerJobFailed(
			@ModelAttribute("schedulerJobFailedCheck") SchedulerJobFailedCheck schedulerJobFailedCheck, Model model) {
		List<SchedulerJobFailedCheck> resultList = new ArrayList<SchedulerJobFailedCheck>();

		try {
			resultList = preventiveCheckService.dbmsSchedulerJobFailedList(schedulerJobFailedCheck);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* 일 예방 점검 - 예외처리 - DB_CHECK_EXCEPTION 저장 action */
	@RequestMapping(value = "/PreventiveCheck/saveDbCheckException", method = RequestMethod.POST)
	@ResponseBody
	public Result saveDbCheckException(@ModelAttribute("dbCheckException") DbCheckException dbCheckException,
			Model model) {
		Result result = new Result();
		int saveResult = 0;

		try {
			saveResult = preventiveCheckService.saveDbCheckException(dbCheckException);
			if (saveResult > 0) {
				result.setResult(true);
			} else {
				result.setResult(false);
			}
		} catch (Exception ex) {
			logger.error("error ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

}