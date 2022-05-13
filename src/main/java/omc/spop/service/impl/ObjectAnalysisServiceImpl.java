package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.dao.ObjectAnalysisDao;
import omc.spop.model.ObjectChange;
import omc.spop.model.Result;
import omc.spop.service.ObjectAnalysisService;

/***********************************************************
 * 2018.06.28 bks OPENPOP V2 최초작업
 **********************************************************/

@Service("ObjectAnalysisService")
public class ObjectAnalysisServiceImpl implements ObjectAnalysisService {

	private static final Logger logger = LoggerFactory.getLogger(ObjectAnalysisServiceImpl.class);

	@Autowired
	private ObjectAnalysisDao objectAnalysisDao;

	@Override
	@SuppressWarnings("unchecked")
	public Result objectChangeChartList(Map<String, Object> param) throws Exception {
		Result result = new Result();
		JSONObject jsonResult = new JSONObject();
		JSONArray list = new JSONArray();
		JSONObject data = null;

		try {
			List<Map<String, Object>> dataList = objectAnalysisDao.objectChangeChartList(param);
			logger.debug("dataList:::::"+dataList);
			List<String> legendList = new ArrayList<String>();
			if (dataList != null && dataList.size() > 0) {
				Map<String, Object> map = dataList.get(0);
				logger.debug("map:::::"+map);
				Set<String> set = map.keySet();
				legendList = new ArrayList(set);
				
				for (Map<String, Object> subMap : dataList) {
					Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
					String strJson = gson.toJson(subMap);
					try {
						data = (JSONObject) new JSONParser().parse(strJson);
					} catch (ParseException ex) {
						ex.printStackTrace();
					}
					list.add(data);
				}
			}
			jsonResult.put("rows", list);

			result.setResult(legendList.size() > 0 ? true : false);
			result.setObject(legendList);
			result.setTxtValue(jsonResult.toString());
			
			JSONObject test = (JSONObject) new JSONParser().parse(result.toString());
			
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> " + ex.getMessage());
			throw ex;
		}

		return result;
	}

	@Override
	public List<ObjectChange> tableChangeList(Map<String, Object> param) throws Exception {
		return objectAnalysisDao.tableChangeList(param);
	}

	@Override
	public List<ObjectChange> columnChangeList(Map<String, Object> param) throws Exception {
		return objectAnalysisDao.columnChangeList(param);
	}

	@Override
	public List<ObjectChange> indexChangeList(Map<String, Object> param) throws Exception {
		return objectAnalysisDao.indexChangeList(param);
	}

	@Override
	public List<Map<String, Object>> ObjectChangeAnalysisChartList(Map<String, Object> param) {
		return objectAnalysisDao.ObjectChangeAnalysisChartList(param);
	}

}
