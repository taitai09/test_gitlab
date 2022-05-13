package omc.spop.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import omc.spop.base.Config;
import omc.spop.model.AdvancedAnalysis;
import omc.spop.model.AwrEventStat;
import omc.spop.model.AwrOsStat;
import omc.spop.model.AwrWaitClassStat;
import omc.spop.model.DbaHistBaseline;
import omc.spop.model.DbaHistSnapshot;
import omc.spop.model.DownLoadFile;
import omc.spop.model.Result;
import omc.spop.server.tools.SPopTools;
import omc.spop.service.AWRAdvancedAnalysisService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.05 이원식 OPENPOP V2 최초작업
 **********************************************************/

@Service("AWRAdvancedAnalysisService")
public class AWRAdvancedAnalysisServiceImpl implements AWRAdvancedAnalysisService {

	private static final Logger logger = LoggerFactory.getLogger(AWRAdvancedAnalysisServiceImpl.class);

	@Override
	public List<AdvancedAnalysis> selectAWRReport(AdvancedAnalysis advancedAnalysis) throws Exception {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		try {
			// AWR Report 모듈 호출
			if (advancedAnalysis.getGubun().equals("html")) {
				resultList = SPopTools.CreateAWRHtml(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
			} else {
				resultList = SPopTools.CreateAWRText(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<AwrOsStat> oSStatChartList(AdvancedAnalysis advancedAnalysis) throws Exception {
		List<AwrOsStat> resultList = new ArrayList<AwrOsStat>();
		try {
			resultList = SPopTools.getAWROSStat(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}
		return resultList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result foreWaitClassChartList(AdvancedAnalysis advancedAnalysis) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<AwrWaitClassStat> legendList = new ArrayList<AwrWaitClassStat>();
		List<AwrWaitClassStat> dataList = new ArrayList<AwrWaitClassStat>();
		String tempDate = "";
		try {
			legendList = SPopTools.getAWRFGWaitClassStatLegend(
					StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));

			dataList = SPopTools.getAWRFGWaitClassStat(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));

			for (int i = 0; i < dataList.size(); i++) {
				AwrWaitClassStat temp = dataList.get(i);

				if (tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());

					data.put(temp.getWait_class(), temp.getWaits_delta());
				} else if (tempDate.equals(temp.getSnap_dt())) {
					data.put(temp.getWait_class(), temp.getWaits_delta());
				} else if (!tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {

					list.add(data);
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());
					data.put(temp.getWait_class(), temp.getWaits_delta());
				}

				tempDate = temp.getSnap_dt();
			}

			list.add(data);
			jsonResult.put("rows", list);

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result backWaitClassChartList(AdvancedAnalysis advancedAnalysis) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<AwrWaitClassStat> legendList = new ArrayList<AwrWaitClassStat>();
		List<AwrWaitClassStat> dataList = new ArrayList<AwrWaitClassStat>();
		String tempDate = "";
		try {
			legendList = SPopTools.getAWRBGWaitClassStatLegend(
					StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));

			dataList = SPopTools.getAWRBGWaitClassStat(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));

			for (int i = 0; i < dataList.size(); i++) {
				AwrWaitClassStat temp = dataList.get(i);

				if (tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());

					data.put(temp.getWait_class(), temp.getWaits());
				} else if (tempDate.equals(temp.getSnap_dt())) {
					data.put(temp.getWait_class(), temp.getWaits());
				} else if (!tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {

					list.add(data);
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());
					data.put(temp.getWait_class(), temp.getWaits());
				}

				tempDate = temp.getSnap_dt();
			}

			list.add(data);
			jsonResult.put("rows", list);

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result foreTOPEventChartList(AdvancedAnalysis advancedAnalysis) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<AwrEventStat> legendList = new ArrayList<AwrEventStat>();
		List<AwrEventStat> dataList = new ArrayList<AwrEventStat>();
		String tempDate = "";
		try {
			legendList = SPopTools.getAWRFGEventStatLegend(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));

			dataList = SPopTools.getAWRFGEventStat(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));

			for (int i = 0; i < dataList.size(); i++) {
				AwrEventStat temp = dataList.get(i);

				if (tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());

					data.put(temp.getEvent_name(), temp.getWaits());
				} else if (tempDate.equals(temp.getSnap_dt())) {
					data.put(temp.getEvent_name(), temp.getWaits());
				} else if (!tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {

					list.add(data);
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());
					data.put(temp.getEvent_name(), temp.getWaits());
				}

				tempDate = temp.getSnap_dt();
			}

			list.add(data);
			jsonResult.put("rows", list);

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Result backTOPEventChartList(AdvancedAnalysis advancedAnalysis) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<AwrEventStat> legendList = new ArrayList<AwrEventStat>();
		List<AwrEventStat> dataList = new ArrayList<AwrEventStat>();
		String tempDate = "";
		try {
			legendList = SPopTools.getAWRBGEventStatLegend(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));

			dataList = SPopTools.getAWRBGEventStat(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));

			for (int i = 0; i < dataList.size(); i++) {
				AwrEventStat temp = dataList.get(i);

				if (tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());

					data.put(temp.getEvent_name(), temp.getWaits());
				} else if (tempDate.equals(temp.getSnap_dt())) {
					data.put(temp.getEvent_name(), temp.getWaits());
				} else if (!tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {

					list.add(data);
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());
					data.put(temp.getEvent_name(), temp.getWaits());
				}

				tempDate = temp.getSnap_dt();
			}

			list.add(data);
			jsonResult.put("rows", list);

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	public List<AdvancedAnalysis> selectADDMReport(AdvancedAnalysis advancedAnalysis) throws Exception {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		try {
			// ADDM Report 모듈 호출
			resultList = SPopTools.CreateADDMReport(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
					StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<AdvancedAnalysis> selectASHReport(AdvancedAnalysis advancedAnalysis) throws Exception {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		try {
			// ASH Report 모듈 호출
			if (advancedAnalysis.getGubun().equals("html")) {
				resultList = SPopTools.CreateASHHtml(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseInt(advancedAnalysis.getInst_id(), 0), advancedAnalysis.getStart_dateTime(),
						advancedAnalysis.getEnd_dateTime());
			} else {
				resultList = SPopTools.CreateASHText(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseInt(advancedAnalysis.getInst_id(), 0), advancedAnalysis.getStart_dateTime(),
						advancedAnalysis.getEnd_dateTime());
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<AdvancedAnalysis> selectAWRSQLReport(AdvancedAnalysis advancedAnalysis) throws Exception {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		try {
			// AWR SQL Report 모듈 호출
			if (advancedAnalysis.getGubun().equals("html")) {
				resultList = SPopTools.CreateAWRSQLHtml(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseInt(advancedAnalysis.getInst_id(), 0), advancedAnalysis.getSql_id(),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
			} else {
				resultList = SPopTools.CreateAWRSQLText(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseInt(advancedAnalysis.getInst_id(), 0), advancedAnalysis.getSql_id(),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<AdvancedAnalysis> selectAWRDiffReport(AdvancedAnalysis advancedAnalysis) throws Exception {
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();
		try {
			// AWR Diff Report 모듈 호출
			if (advancedAnalysis.getGubun().equals("html")) {
				resultList = SPopTools.CreateAWRDiffHtml(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id2(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id2(), 0));
			} else {
				resultList = SPopTools.CreateAWRDiffText(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id2(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id2(), 0));
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public DownLoadFile getReportSave(AdvancedAnalysis advancedAnalysis) throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		String fileName = "";
		String orgFileName = "";
		List<AdvancedAnalysis> resultList = new ArrayList<AdvancedAnalysis>();

		try {
			if (advancedAnalysis.getModule_name().equals("AWRReport")) {
				if (advancedAnalysis.getGubun().equals("html")) {
					fileName = "AWRReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".html";
					orgFileName = "AWRReport.html";
				} else {
					fileName = "AWRReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".txt";
					orgFileName = "AWRReport.txt";
				}

				// AWR Report 모듈 호출
				if (advancedAnalysis.getGubun().equals("html")) {
					resultList = SPopTools.CreateAWRHtml(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
							StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
							StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
							StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
				} else {
					resultList = SPopTools.CreateAWRText(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
							StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
							StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
							StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
				}
			} else if (advancedAnalysis.getModule_name().equals("ADDMReport")) {
				fileName = "ADDMReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".txt";
				orgFileName = "ADDMReport.html";

				// ADDM Report 모듈 호출
				resultList = SPopTools.CreateADDMReport(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
						StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
						StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
			} else if (advancedAnalysis.getModule_name().equals("ASHReport")) {
				if (advancedAnalysis.getGubun().equals("html")) {
					fileName = "ASHReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".html";
					orgFileName = "ASHReport.html";
				} else {
					fileName = "ASHReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".txt";
					orgFileName = "ASHReport.txt";
				}

				// ASH Report 모듈 호출
				if (advancedAnalysis.getGubun().equals("html")) {
					resultList = SPopTools.CreateASHHtml(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
							StringUtil.parseInt(advancedAnalysis.getInst_id(), 0), advancedAnalysis.getStart_dateTime(),
							advancedAnalysis.getEnd_dateTime());
				} else {
					resultList = SPopTools.CreateASHText(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
							StringUtil.parseInt(advancedAnalysis.getInst_id(), 0), advancedAnalysis.getStart_dateTime(),
							advancedAnalysis.getEnd_dateTime());
				}
			} else if (advancedAnalysis.getModule_name().equals("AWRSQLReport")) {
				if (advancedAnalysis.getGubun().equals("html")) {
					fileName = "AWRSQLReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".html";
					orgFileName = "AWRSQLReport.html";
				} else {
					fileName = "AWRSQLReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".txt";
					orgFileName = "AWRSQLReport.txt";
				}

				// AWR SQL Report 모듈 호출
				if (advancedAnalysis.getGubun().equals("html")) {
					resultList = SPopTools.CreateAWRSQLHtml(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
							StringUtil.parseInt(advancedAnalysis.getInst_id(), 0), advancedAnalysis.getSql_id(),
							StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
							StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
				} else {
					resultList = SPopTools.CreateAWRSQLText(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
							StringUtil.parseInt(advancedAnalysis.getInst_id(), 0), advancedAnalysis.getSql_id(),
							StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
							StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0));
				}
			} else if (advancedAnalysis.getModule_name().equals("AWRDiffReport")) {
				if (advancedAnalysis.getGubun().equals("html")) {
					fileName = "AWRDiffReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".html";
					orgFileName = "AWRDiffReport.html";
				} else {
					fileName = "AWRDiffReport_" + DateUtil.getNowDate("yyMMddHHmmssSSS") + ".txt";
					orgFileName = "AWRDiffReport.txt";
				}

				// AWR Diff Report 모듈 호출
				if (advancedAnalysis.getGubun().equals("html")) {
					resultList = SPopTools.CreateAWRDiffHtml(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
							StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
							StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
							StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0),
							StringUtil.parseLong(advancedAnalysis.getStart_snap_id2(), 0),
							StringUtil.parseLong(advancedAnalysis.getEnd_snap_id2(), 0));
				} else {
					resultList = SPopTools.CreateAWRDiffText(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
							StringUtil.parseInt(advancedAnalysis.getInst_id(), 0),
							StringUtil.parseLong(advancedAnalysis.getStart_snap_id1(), 0),
							StringUtil.parseLong(advancedAnalysis.getEnd_snap_id1(), 0),
							StringUtil.parseLong(advancedAnalysis.getStart_snap_id2(), 0),
							StringUtil.parseLong(advancedAnalysis.getEnd_snap_id2(), 0));
				}
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 호출 에러 ==> " + ex.getMessage());
			throw ex;
		}

		if (resultList != null) {
			downloadFile = WriteFile(fileName, orgFileName, resultList);
		}

		return downloadFile;
	}

	public DownLoadFile WriteFile(String fileName, String orgFileName, List<AdvancedAnalysis> resultList)
			throws Exception {
		DownLoadFile downloadFile = new DownLoadFile();
		String toolsDownloadRoot = Config.getString("download.tools.dir");
		PrintWriter printWriter = null;
		FileWriter fw = null;
		File saveFolder = new File(toolsDownloadRoot);

		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		try {
			fw = new FileWriter(toolsDownloadRoot + fileName);
			printWriter = new PrintWriter(fw, true);

			for (int i = 0; i < resultList.size(); i++) {
				AdvancedAnalysis result = resultList.get(i);
				printWriter.println(result.getRow_value());
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " FILE WRITE ERROR ==> " + ex.getMessage());
			throw ex;
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (Exception ex) {
			}
		}

		downloadFile.setFile_path(toolsDownloadRoot);
		downloadFile.setFile_nm(fileName);
		downloadFile.setOrg_file_nm(orgFileName);

		return downloadFile;
	}

	@Override
	public List<DbaHistSnapshot> snapShotList(DbaHistSnapshot dbaHistSnapshot) throws Exception {
		List<DbaHistSnapshot> resultList = new ArrayList<DbaHistSnapshot>();

		try {
			long dbid = StringUtil.parseLong(dbaHistSnapshot.getDbid(), 0);
			int instance_number = StringUtil.parseInt(dbaHistSnapshot.getInstance_number(), 0);
			String startDt = dbaHistSnapshot.getStrStartDt();
			String endDt = dbaHistSnapshot.getStrEndDt();
			long snap_id = StringUtil.parseLong(dbaHistSnapshot.getSnap_id(), 0);

			resultList = SPopTools.getSnapID(dbid, instance_number, startDt, endDt, snap_id);
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 ERROR ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
		// return toolsDao.snapShotList(dbaHistSnapshot);
	}

	@Override
	public List<DbaHistBaseline> baseLineList(AdvancedAnalysis advancedAnalysis) throws Exception {
		List<DbaHistBaseline> resultList = new ArrayList<DbaHistBaseline>();

		try {
			resultList = SPopTools.searchBASELINE(StringUtil.parseLong(advancedAnalysis.getDbid(), 0),
					StringUtil.parseInt(advancedAnalysis.getInst_id(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 ERROR ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public AdvancedAnalysis setMinMaxDateTime(AdvancedAnalysis advancedAnalysis) throws Exception {
		AdvancedAnalysis resultModel = new AdvancedAnalysis();

		try {
			resultModel = SPopTools.getMinMaxDateTime(StringUtil.parseLong(advancedAnalysis.getDbid(), 0));
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 모듈 ERROR ==> " + ex.getMessage());
			throw ex;
		}

		return resultModel;
	}
}
