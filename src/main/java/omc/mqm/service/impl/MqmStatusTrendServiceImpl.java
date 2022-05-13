package omc.mqm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.mqm.dao.MqmStatusTrendDao;
import omc.mqm.service.MqmStatusTrendService;
import omc.spop.model.Result;
import omc.spop.model.StandardComplianceRateTrend;
import omc.spop.model.StructuralQualityStatusTrend;

/***********************************************************
 * 2019.08.26	임호경		OPENPOP V2 최초작업
 **********************************************************/

@Service("MqmStatusTrendService")
public class MqmStatusTrendServiceImpl implements MqmStatusTrendService {
	private static final Logger logger = LoggerFactory.getLogger(MqmStatusTrendServiceImpl.class);

	
	@Autowired
	private MqmStatusTrendDao sqstDao;
	
	
	@Override
	public List<StructuralQualityStatusTrend> getChartNonCompliantStandardizationRateTrend(StructuralQualityStatusTrend sqst) {
		return sqstDao.getChartNonCompliantStandardizationRateTrend(sqst);
	}


	@Override
	public List<StructuralQualityStatusTrend> getChartStatusByModel(StructuralQualityStatusTrend sqst) {
		return sqstDao.getChartStatusByModel(sqst);
	}


	@Override
	public List<StructuralQualityStatusTrend> getStatusByModelAll(StructuralQualityStatusTrend sqst) {
		return sqstDao.getStatusByModelAll(sqst);
	}


	@Override
	public List<StructuralQualityStatusTrend> getChartStandardizationRateStatusByModel(StructuralQualityStatusTrend sqst) {
		return sqstDao.getChartStandardizationRateStatusByModel(sqst);
	}


	@Override
	public List<StructuralQualityStatusTrend> getChartNonComplianceStatus(StructuralQualityStatusTrend sqst) {
		return sqstDao.getChartNonComplianceStatus(sqst);
	}



	@Override
	public Result getChartNumberNonCompliantByModel(StructuralQualityStatusTrend sqst) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<StructuralQualityStatusTrend> legendList = new ArrayList<StructuralQualityStatusTrend>();
		List<StructuralQualityStatusTrend> dataList = new  ArrayList<StructuralQualityStatusTrend>();
		String tempDate = "";
		String tempLegend = "";
		
		dataList = sqstDao.getChartNumberNonCompliantByModel(sqst);
		try{
			
			for(int index = 0; index < dataList.size(); index++) {
				StructuralQualityStatusTrend temp = dataList.get(index);
				StructuralQualityStatusTrend tempScrt = new StructuralQualityStatusTrend();
				
				if(tempDate.equals("") && !tempDate.equals(temp.getStd_inspect_day())) {
					data = new JSONObject();
					data.put("std_inspect_day", temp.getStd_inspect_day());
					data.put(temp.getModel_nm(), temp.getErr_cnt());
				} else if(tempDate.equals(temp.getStd_inspect_day())) {
					data.put(temp.getModel_nm(), temp.getErr_cnt());
				} else if(!tempDate.equals("") && !tempDate.equals(temp.getStd_inspect_day())) {
					list.add(data);
					data = new JSONObject();
					data.put("std_inspect_day", temp.getStd_inspect_day());
					data.put(temp.getModel_nm(), temp.getErr_cnt());
				}
				
				tempDate = temp.getStd_inspect_day();
				tempDate = tempDate == null ? "" : tempDate;
				
				if(tempLegend.equals("") && !tempLegend.equals(temp.getModel_nm())) {
					tempScrt.setModel_nm(temp.getModel_nm());
					legendList.add(tempScrt);
				} else if(!tempLegend.equals("") && !tempLegend.equals(temp.getModel_nm())) {
					tempScrt.setModel_nm(temp.getModel_nm());
					legendList.add(tempScrt);
				}
				
				tempLegend = temp.getModel_nm();
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
	public Result getChartStandardComplianceRateTrend(StructuralQualityStatusTrend sqst) throws Exception{
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<StructuralQualityStatusTrend> legendList = new ArrayList<StructuralQualityStatusTrend>();
		List<StructuralQualityStatusTrend> dataList = new  ArrayList<StructuralQualityStatusTrend>();
		String tempDate = "";
		String tempLegend = "";
		
		dataList = sqstDao.getChartStandardComplianceRateTrend(sqst);
		try{
			
			for(int index = 0; index < dataList.size(); index++) {
				StructuralQualityStatusTrend temp = dataList.get(index);
				StructuralQualityStatusTrend tempScrt = new StructuralQualityStatusTrend();
				
				if(tempDate.equals("") && !tempDate.equals(temp.getStd_inspect_day())) {
					data = new JSONObject();
					data.put("std_inspect_day", temp.getStd_inspect_day());
					data.put(temp.getModel_nm(), temp.getStd_rate());
				} else if(tempDate.equals(temp.getStd_inspect_day())) {
					data.put(temp.getModel_nm(), temp.getStd_rate());
				} else if(!tempDate.equals("") && !tempDate.equals(temp.getStd_inspect_day())) {
					list.add(data);
					data = new JSONObject();
					data.put("std_inspect_day", temp.getStd_inspect_day());
					data.put(temp.getModel_nm(), temp.getStd_rate());
				}
				
				tempDate = temp.getStd_inspect_day();
				tempDate = tempDate == null ? "" : tempDate;
				
				if(tempLegend.equals("") && !tempLegend.equals(temp.getModel_nm())) {
					tempScrt.setModel_nm(temp.getModel_nm());
					legendList.add(tempScrt);
				} else if(!tempLegend.equals("") && !tempLegend.equals(temp.getModel_nm())) {
					tempScrt.setModel_nm(temp.getModel_nm());
					legendList.add(tempScrt);
				}
				
				tempLegend = temp.getModel_nm();
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
	
}
