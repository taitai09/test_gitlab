package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.DashBoardV2Dao;
import omc.spop.model.DashboardV2Left;
import omc.spop.model.NewAppTimeoutPrediction;
import omc.spop.model.NewSQLTimeoutPrediction;
import omc.spop.model.ResourceLimitPrediction;
import omc.spop.model.Result;
import omc.spop.model.SequenceLimitPoint;
import omc.spop.model.TablespaceLimitPoint;
import omc.spop.service.DashBoardV2Service;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.08.21 DashBoard
 **********************************************************/

@Service("DashBoardV2Service")
public class DashBoardV2ServiceImpl implements DashBoardV2Service {

	private static final Logger logger = LoggerFactory.getLogger(DashBoardV2ServiceImpl.class);
	
	@Autowired
	private DashBoardV2Dao dashBoardV2Dao;
	
	// Left
	@Override
	public List<DashboardV2Left> totalCntGrade(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.totalCntGrade(param);
	}
	
	@Override
	public List<DashboardV2Left> cntGradePerDb(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.cntGradePerDb(param);
	}
	
	@Override
	public List<DashboardV2Left> reloadDbCheckResultGrid01(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.reloadDbCheckResultGrid01(param);
	}
	
	@Override
	public List<DashboardV2Left> listGradeForDb(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.listGradeForDb(param);
	}
	
	@Override
	public List<DashboardV2Left> listSqlAppCheckDb(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.listSqlAppCheckDb(param);
	}
	
	@Override
	public Result chartSqlAppCheckDb(DashboardV2Left param) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<DashboardV2Left> legendList = new ArrayList<DashboardV2Left>();
		List<DashboardV2Left> dataList = new  ArrayList<DashboardV2Left>();
		String tempDate = "";
		
		try {
			legendList = dashBoardV2Dao.chartLegendSqlAppCheckDb(param);
			dataList = dashBoardV2Dao.chartSqlAppCheckDb(param);
			
			for(int index = 0; index < dataList.size(); index++) {
				DashboardV2Left temp = dataList.get(index);
				
				if(tempDate.equals("") && !tempDate.equals(temp.getBase_day())) {
					data = new JSONObject();
					data.put("base_day", temp.getBase_day());
					data.put(temp.getDiag_type_nm(), temp.getCnt());
				} else if(tempDate.equals(temp.getBase_day())) {
					data.put(temp.getDiag_type_nm(), temp.getCnt());
				} else if(!tempDate.equals("") && !tempDate.equals(temp.getBase_day())) {
					list.add(data);
					data = new JSONObject();
					data.put("base_day", temp.getBase_day());
					data.put(temp.getDiag_type_nm(), temp.getCnt());
				}
				
				tempDate = temp.getBase_day();
			}
			
			list.add(data);
			jsonResult.put("rows", list);
			
			result.setResult(dataList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public List<DashboardV2Left> listSqlAppDiagStatus(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.listSqlAppDiagStatus(param);
	}
	
	@Override
	public List<DashboardV2Left> listTopSqlPerDb(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.listTopSqlPerDb(param);
	}
	
	@Override
	public List<DashboardV2Left> listTopSql(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.listTopSql(param);
	}
	
	@Override
	public List<DashboardV2Left> chartTopSql(DashboardV2Left param) throws Exception {
		return dashBoardV2Dao.chartTopSql(param);
	}
	
	// Right

	@Override
	public List<ResourceLimitPrediction> getResourceLimitPointPredictionList(
			ResourceLimitPrediction resourceLimitPrediction) {
		return dashBoardV2Dao.getResourceLimitPointPredictionList(resourceLimitPrediction);
	}

	@Override
	public List<TablespaceLimitPoint> getTablespacePresentConditionDBList(TablespaceLimitPoint tablespaceLimitPoint) {
		return dashBoardV2Dao.getTablespacePresentConditionDBList(tablespaceLimitPoint);

	}

	@Override
	public List<TablespaceLimitPoint> getTablespacePresentConditionList(TablespaceLimitPoint tablespaceLimitPoint) {
		return dashBoardV2Dao.getTablespacePresentConditionList(tablespaceLimitPoint);

	}

	@Override
	public List<SequenceLimitPoint> getSequencePresentConditionDBList(SequenceLimitPoint sequenceLimitPoint) {
		return dashBoardV2Dao.getSequencePresentConditionDBList(sequenceLimitPoint);

	}

	@Override
	public List<SequenceLimitPoint> getSequencePresentConditionList(SequenceLimitPoint sequenceLimitPoint) {
		return dashBoardV2Dao.getSequencePresentConditionList(sequenceLimitPoint);

	}

	@Override
	public List<NewSQLTimeoutPrediction> getNewSQLTimeoutPredictionList(NewSQLTimeoutPrediction newSQLTimeoutPrediction) {
		return dashBoardV2Dao.getNewSQLTimeoutPredictionList(newSQLTimeoutPrediction);

	}

	@Override
	public List<NewAppTimeoutPrediction> getNewAppTimeoutPredictList(NewAppTimeoutPrediction newAppTimeoutPrediction) {
		return dashBoardV2Dao.getNewAppTimeoutPredictList(newAppTimeoutPrediction);

	}

	@Override
	public List<ResourceLimitPrediction> getResourceLimitPointPredictionChart(
			ResourceLimitPrediction resourceLimitPrediction) {
		return dashBoardV2Dao.getResourceLimitPointPredictionChart(resourceLimitPrediction);

	}

	@Override
	public Result getResourceLimitPointPredictionChartResult(ResourceLimitPrediction resourceLimitPrediction) throws Exception {
		String strGubun = resourceLimitPrediction.getStrGubun();
		Result result = new Result();
		List<ResourceLimitPrediction> legendList = new ArrayList<ResourceLimitPrediction>();
		List<ResourceLimitPrediction> dataList = new ArrayList<ResourceLimitPrediction>();

		JSONObject jsonResult = new JSONObject();
		JSONObject data = null;
		JSONArray list = new JSONArray();
		String tempPeriod = "";

		try {
			legendList = dashBoardV2Dao.getResourceLimitPointPredictionChartLegendList(resourceLimitPrediction);
			dataList = dashBoardV2Dao.getResourceLimitPointPredictionChart(resourceLimitPrediction);

			for (int i = 0; i < dataList.size(); i++) {
				ResourceLimitPrediction temp = dataList.get(i);
				data = new JSONObject();
				data.put("period", temp.getPeriod());
				data.put("inst_id", temp.getInst_id());
				data.put("inst_nm", temp.getInst_nm());
				data.put("dbid", temp.getDbid());
				if(strGubun.equals("CPU")) {
					data.put(temp.getInst_nm(), StringUtil.parseFloat(temp.getMonth_cpu_usage(), 0));
					data.put("month_usage", StringUtil.parseFloat(temp.getMonth_cpu_usage(), 0));
				}else if(strGubun.equals("MEMORY")) {
					data.put(temp.getInst_nm(), StringUtil.parseFloat(temp.getMonth_mem_usage(), 0));
					data.put("month_usage", StringUtil.parseFloat(temp.getMonth_mem_usage(), 0));
				}
				list.add(data);
			}
			
			jsonResult.put("rows", list);

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}

		return result;
	}

	@Override
	public List<TablespaceLimitPoint> getTablespacePresentConditionChart(TablespaceLimitPoint tablespaceLimitPoint) {
		return dashBoardV2Dao.getTablespacePresentConditionChart(tablespaceLimitPoint);

	}

	@Override
	public Result getTablespacePresentConditionChartResult(TablespaceLimitPoint tablespaceLimitPoint) throws Exception {
		Result result = new Result();
		List<TablespaceLimitPoint> legendList = new ArrayList<TablespaceLimitPoint>();
		List<TablespaceLimitPoint> dataList = new ArrayList<TablespaceLimitPoint>();

		JSONObject jsonResult = new JSONObject();
		JSONObject data = null;
		JSONArray list = new JSONArray();

		try {
			legendList = dashBoardV2Dao.getTablespacePresentConditionChartLegendList(tablespaceLimitPoint);
			dataList = dashBoardV2Dao.getTablespacePresentConditionChart(tablespaceLimitPoint);

			for (int i = 0; i < dataList.size(); i++) {
				TablespaceLimitPoint temp = dataList.get(i);
				data = new JSONObject();
				data.put("period", temp.getPeriod());
				data.put(temp.getTablespace_name(), StringUtil.parseFloat(temp.getMonth_ts_used_percent(), 0));				
				list.add(data);
			}

			jsonResult.put("rows", list);

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}
	
	@Override
	public Result getSequencePresentConditionChartResult(SequenceLimitPoint sequenceLimitPoint) throws Exception {
		Result result = new Result();
		List<SequenceLimitPoint> legendList = new ArrayList<SequenceLimitPoint>();
		List<SequenceLimitPoint> dataList = new ArrayList<SequenceLimitPoint>();

		JSONObject jsonResult = new JSONObject();
		JSONObject data = null;
		JSONArray list = new JSONArray();

		try {
			legendList = dashBoardV2Dao.getSequencePresentConditionChartLegendList(sequenceLimitPoint);
			dataList = dashBoardV2Dao.getSequencePresentConditionChart(sequenceLimitPoint);
			
			for (int i = 0; i < dataList.size(); i++) {
				SequenceLimitPoint temp = dataList.get(i);
				data = new JSONObject();
				data.put("period", temp.getPeriod());
				data.put(temp.getSequence_name(), StringUtil.parseFloat(temp.getMonth_sequence_ratio(), 0));				
				list.add(data);
			}
			
			jsonResult.put("rows", list);

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}

		return result;
	}
	
	@Override
	public List<SequenceLimitPoint> getSequencePresentConditionChart(SequenceLimitPoint sequenceLimitPoint) {
		return dashBoardV2Dao.getSequencePresentConditionChart(sequenceLimitPoint);

	}

	@Override
	public List<NewSQLTimeoutPrediction> getNewSQLTimeoutPredictionChart(
			NewSQLTimeoutPrediction newSQLTimeoutPrediction) {
		return dashBoardV2Dao.getNewSQLTimeoutPredictionChart(newSQLTimeoutPrediction);

	}

	@Override
	public List<NewAppTimeoutPrediction> getNewAppTimeoutPredictionChart(
			NewAppTimeoutPrediction newAppTimeoutPrediction) {
		return dashBoardV2Dao.getNewAppTimeoutPredictionChart(newAppTimeoutPrediction);

	}

	@Override
	public List<DashboardV2Left> chartTopSql2(DashboardV2Left dashboardV2Left) {
		return dashBoardV2Dao.chartTopSql2(dashboardV2Left);
	}

}
