package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SQLPerformanceStatisticsDao;
import omc.spop.dao.UserMngDao;
import omc.spop.model.Result;
import omc.spop.model.SQLPerformanceStatistics;
import omc.spop.service.SQLPerformanceStatisticsService;

/***********************************************************
 * 2019.08.28 	임호경 	최초작성
 **********************************************************/

@Service("SQLPerformanceStatisticsService")
public class SQLPerformanceStatisticsServiceImpl implements SQLPerformanceStatisticsService {
	private static final Logger logger = LoggerFactory.getLogger(SQLPerformanceStatisticsServiceImpl.class);

	@Autowired
	private SQLPerformanceStatisticsDao sqlPerfStaticsDao;

	@Override
	public Result getChartTopSqlTrendStatus(SQLPerformanceStatistics sqlPerformanceStatistics) throws Exception {
		
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<SQLPerformanceStatistics> legendList = new ArrayList<SQLPerformanceStatistics>();
		List<SQLPerformanceStatistics> dataList = new  ArrayList<SQLPerformanceStatistics>();
		String tempDate = "";
		String tempLegend = "";
		
		dataList = sqlPerfStaticsDao.getChartTopSqlTrendStatus(sqlPerformanceStatistics);

		try{
			
			for(int index = 0; index < dataList.size(); index++) {
				SQLPerformanceStatistics temp = dataList.get(index);
				SQLPerformanceStatistics tempScrt = new SQLPerformanceStatistics();
				
				if(tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) { //날짜
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());
					data.put(temp.getInst_nm(), temp.getCnt());
				} else if(tempDate.equals(temp.getSnap_dt())) {
					data.put(temp.getInst_nm(), temp.getCnt());
				} else if(!tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {
					list.add(data);
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());
					data.put(temp.getInst_nm(), temp.getCnt());
				}
				
				tempDate = temp.getSnap_dt();
				tempDate = tempDate == null ? "" : tempDate;
				
				if(tempLegend.equals("") && !tempLegend.equals(temp.getInst_nm())) {
					tempScrt.setInst_nm(temp.getInst_nm());
					legendList.add(tempScrt);
				} else if(!tempLegend.equals("") && !tempLegend.equals(temp.getInst_nm())) {
					tempScrt.setInst_nm(temp.getInst_nm());
					legendList.add(tempScrt);
				}
				
				tempLegend = temp.getInst_nm();
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
	public Result getChartCpuUsage(SQLPerformanceStatistics sqlPerformanceStatistics) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<SQLPerformanceStatistics> legendList = new ArrayList<SQLPerformanceStatistics>();
		List<SQLPerformanceStatistics> dataList = new  ArrayList<SQLPerformanceStatistics>();
		String tempDate = "";
		String tempLegend = "";
		
		dataList = sqlPerfStaticsDao.getChartCpuUsage(sqlPerformanceStatistics);

		try{
			
			for(int index = 0; index < dataList.size(); index++) {
				SQLPerformanceStatistics temp = dataList.get(index);
				SQLPerformanceStatistics tempScrt = new SQLPerformanceStatistics();
				
				if(tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) { //날짜
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());
					data.put(temp.getInst_nm(), temp.getCnt());
				} else if(tempDate.equals(temp.getSnap_dt())) {
					data.put(temp.getInst_nm(), temp.getCnt());
				} else if(!tempDate.equals("") && !tempDate.equals(temp.getSnap_dt())) {
					list.add(data);
					data = new JSONObject();
					data.put("snap_dt", temp.getSnap_dt());
					data.put(temp.getInst_nm(), temp.getCnt());
				}
				
				tempDate = temp.getSnap_dt();
				tempDate = tempDate == null ? "" : tempDate;
				
				if(tempLegend.equals("") && !tempLegend.equals(temp.getInst_nm())) {
					tempScrt.setInst_nm(temp.getInst_nm());
					legendList.add(tempScrt);
				} else if(!tempLegend.equals("") && !tempLegend.equals(temp.getInst_nm())) {
					tempScrt.setInst_nm(temp.getInst_nm());
					legendList.add(tempScrt);
				}
				
				tempLegend = temp.getInst_nm();
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
		return result;	}

	@Override
	public List<SQLPerformanceStatistics> getGridModule(SQLPerformanceStatistics sqlPerformanceStatistics) {
		return sqlPerfStaticsDao.getGridModule(sqlPerformanceStatistics);
	}

	@Override
	public List<SQLPerformanceStatistics> getGridAction(SQLPerformanceStatistics sqlPerformanceStatistics) {
		return sqlPerfStaticsDao.getGridAction(sqlPerformanceStatistics);
	}

	@Override
	public List<SQLPerformanceStatistics> getGridParsingSchema(SQLPerformanceStatistics sqlPerformanceStatistics) {
		return sqlPerfStaticsDao.getGridParsingSchema(sqlPerformanceStatistics);
	}

	@Override
	public List<SQLPerformanceStatistics> getGridTopSqlResultList(SQLPerformanceStatistics sqlPerformanceStatistics) {
		return sqlPerfStaticsDao.getGridTopSqlResultList(sqlPerformanceStatistics);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getGridTopSqlResultListByExcelDown(
			SQLPerformanceStatistics sqlPerformanceStatistics) {
		return sqlPerfStaticsDao.getGridTopSqlResultListByExcelDown(sqlPerformanceStatistics);
	}
}
	
