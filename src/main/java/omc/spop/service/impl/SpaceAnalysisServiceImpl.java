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

import omc.spop.dao.SpaceAnalysisDao;
import omc.spop.model.OdsDataFiles;
import omc.spop.model.OdsSegments;
import omc.spop.model.OdsTabModifications;
import omc.spop.model.OdsTables;
import omc.spop.model.OdsTablespaces;
import omc.spop.model.Result;
import omc.spop.service.SpaceAnalysisService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 * 2018.04.27	이원식	오브젝트 분석 => SPACE 분석으로 변경
 **********************************************************/

@Service("SpaceAnalysisService")
public class SpaceAnalysisServiceImpl implements SpaceAnalysisService {
	
	private static final Logger logger = LoggerFactory.getLogger(SpaceAnalysisServiceImpl.class);

	@Autowired
	private SpaceAnalysisDao spaceAnalysisDao;

	@Override
	public List<OdsTablespaces> tablespaceAnalysisList(OdsTablespaces odsTablespaces) throws Exception {
		return spaceAnalysisDao.tablespaceAnalysisList(odsTablespaces);
	}
	
	@Override
	public List<OdsTablespaces> dbSizeChartList(OdsTablespaces odsTablespaces) throws Exception {
		return spaceAnalysisDao.dbSizeChartList(odsTablespaces);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Result topTablespaceChartList(OdsTablespaces odsTablespaces) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<OdsTablespaces> legendList = new  ArrayList<OdsTablespaces>();
		List<OdsTablespaces> dataList = new  ArrayList<OdsTablespaces>();
		String tempDate = "";
		
		try{
			legendList = spaceAnalysisDao.topTablespaceChartLegendList(odsTablespaces);
			
			dataList = spaceAnalysisDao.topTablespaceChartList(odsTablespaces);
			
			for(int i = 0 ; i < dataList.size() ; i++){
				OdsTablespaces temp = dataList.get(i);

				if(tempDate.equals("") && !tempDate.equals(temp.getBase_day())){
					data = new JSONObject();
					data.put("base_day", temp.getBase_day());
					data.put(temp.getTablespace_name(), StringUtil.parseFloat(temp.getUsed_space_mb(),0));
				}else if(tempDate.equals(temp.getBase_day())){
					data.put(temp.getTablespace_name(), StringUtil.parseFloat(temp.getUsed_space_mb(),0));
				}else if(!tempDate.equals("") && !tempDate.equals(temp.getBase_day())){
					list.add(data);
					data = new JSONObject();
					data.put("base_day", temp.getBase_day());
					data.put(temp.getTablespace_name(), StringUtil.parseFloat(temp.getUsed_space_mb(),0));
				}

				tempDate = temp.getBase_day();
			}
			
			list.add(data);			
			jsonResult.put("rows",list);
			
			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			logger.debug(jsonResult.toString());
			result.setTxtValue(jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public List<OdsSegments> segmentList(OdsSegments odsSegments) throws Exception {
		return spaceAnalysisDao.segmentList(odsSegments);
	}
	
	@Override
	public List<OdsSegments> segmentStatisticsList(OdsSegments odsSegments) throws Exception {
		return spaceAnalysisDao.segmentStatisticsList(odsSegments);
	}
	
	@Override
	public List<OdsSegments> segmentSizeHistoryList(OdsSegments odsSegments) throws Exception {
		return spaceAnalysisDao.segmentSizeHistoryList(odsSegments);
	}
	
	@Override
	public List<OdsDataFiles> dataFileList(OdsDataFiles odsDataFiles) throws Exception {
		return spaceAnalysisDao.dataFileList(odsDataFiles);
	}
	
	@Override
	public List<OdsDataFiles> datafileStatisticsList(OdsDataFiles odsDataFiles) throws Exception {
		return spaceAnalysisDao.datafileStatisticsList(odsDataFiles);
	}
	
	@Override
	public List<OdsTables> reorgTargetAnalysisList(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.reorgTargetAnalysisList(odsTables);
	}
	
	@Override
	public List<OdsTables> topDMLTableChartList(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.topDMLTableChartList(odsTables);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Result topDMLTableHistoryChartList(OdsTables odsTables) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<OdsTables> legendList = new  ArrayList<OdsTables>();
		List<OdsTables> dataList = new  ArrayList<OdsTables>();
		String tempDate = "";
		
		try{
			legendList = spaceAnalysisDao.topDMLTableHistoryChartLegendList(odsTables);
			
			dataList = spaceAnalysisDao.topDMLTableHistoryChartList(odsTables);
			
			for(int i = 0 ; i < dataList.size() ; i++){
				OdsTables temp = dataList.get(i);

				if(tempDate.equals("") && !tempDate.equals(temp.getBase_day())){
					data = new JSONObject();
					data.put("base_day", temp.getBase_day());
					data.put(temp.getTable_name(), StringUtil.parseDouble(temp.getDml_cnt(),0));
				}else if(tempDate.equals(temp.getBase_day())){
					data.put(temp.getTable_name(), StringUtil.parseDouble(temp.getDml_cnt(),0));
				}else if(!tempDate.equals("") && !tempDate.equals(temp.getBase_day())){
					list.add(data);
					data = new JSONObject();
					data.put("base_day", temp.getBase_day());
					data.put(temp.getTable_name(), StringUtil.parseDouble(temp.getDml_cnt(),0));
				}

				tempDate = temp.getBase_day();
			}
			
			list.add(data);			
			jsonResult.put("rows",list);
			
			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
			
			logger.debug("==> " + jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public List<OdsTables> dmlHistoryChartList(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.dmlHistoryChartList(odsTables);
	}	
	
	@Override
	public List<OdsTables> partitionTargetAnalysisList(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.partitionTargetAnalysisList(odsTables);
	}
	
	@Override
	public List<OdsSegments> topSizeTableChartList(OdsSegments odsSegments) throws Exception {
		return spaceAnalysisDao.topSizeTableChartList(odsSegments);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Result topSizeTableHistoryChartList(OdsSegments odsSegments) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;
		List<OdsSegments> legendList = new  ArrayList<OdsSegments>();
		List<OdsSegments> dataList = new  ArrayList<OdsSegments>();
		String tempDate = "";
		
		try{
			legendList = spaceAnalysisDao.topSizeTableHistoryChartLegendList(odsSegments);
			
			dataList = spaceAnalysisDao.topSizeTableHistoryChartList(odsSegments);
			
			for(int i = 0 ; i < dataList.size() ; i++){
				OdsSegments temp = dataList.get(i);

				if(tempDate.equals("") && !tempDate.equals(temp.getBase_day())){
					data = new JSONObject();
					data.put("base_day", temp.getBase_day());
					data.put(temp.getSegment_name(), StringUtil.parseDouble(temp.getBytes(),0));
				}else if(tempDate.equals(temp.getBase_day())){
					data.put(temp.getSegment_name(), StringUtil.parseDouble(temp.getBytes(),0));
				}else if(!tempDate.equals("") && !tempDate.equals(temp.getBase_day())){
					list.add(data);
					data = new JSONObject();
					data.put("base_day", temp.getBase_day());
					data.put(temp.getSegment_name(), StringUtil.parseDouble(temp.getBytes(),0));
				}

				tempDate = temp.getBase_day();
			}
			
			list.add(data);			
			jsonResult.put("rows",list);
			
			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
			
			logger.debug("==> " + jsonResult.toString());
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> ==> " + ex.getMessage());
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public List<OdsTables> tableSizeHistoryChartList(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.tableSizeHistoryChartList(odsTables);
	}
	
	@Override
	public List<OdsTables> accessPathList(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.accessPathList(odsTables);
	}
	
	@Override
	public OdsTables getPartitionRecommendation(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.getPartitionRecommendation(odsTables);
	}
	
	@Override
	public List<OdsTables> partitionRecommendationList(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.partitionRecommendationList(odsTables);
	}
	
	@Override
	public List<OdsDataFiles> dataIOAnalysisList(OdsDataFiles odsDataFiles) throws Exception {
		return spaceAnalysisDao.dataIOAnalysisList(odsDataFiles);
	}
	
	@Override
	public List<OdsDataFiles> datafileIOChartList(OdsDataFiles odsDataFiles) throws Exception {
		return spaceAnalysisDao.datafileIOChartList(odsDataFiles);
	}
	
	@Override
	public List<OdsDataFiles> datafileIOHistoryChartList(OdsDataFiles odsDataFiles) throws Exception {
		return spaceAnalysisDao.datafileIOHistoryChartList(odsDataFiles);
	}
	
	@Override
	public List<OdsTables> dmlChangeTableList(OdsTables odsTables) throws Exception {
		return spaceAnalysisDao.dmlChangeTableList(odsTables);
	}
	
	@Override
	public List<OdsTabModifications> dmlOccurrenceList(OdsTabModifications odsTabModifications) throws Exception {
		return spaceAnalysisDao.dmlOccurrenceList(odsTabModifications);
	}
	
	@Override
	public List<OdsTabModifications> dmlChangeDailyList(OdsTabModifications odsTabModifications) throws Exception {
		return spaceAnalysisDao.dmlChangeDailyList(odsTabModifications);
	}

	@Override
	public List<LinkedHashMap<String, Object>> dmlChangeDailyListByExcelDown(OdsTabModifications odsTabModifications) {
		return spaceAnalysisDao.dmlChangeDailyListByExcelDown(odsTabModifications);
	}		
}
