package omc.spop.service;

import java.util.List;
import java.util.Map;

import omc.spop.model.ObjectChange;
import omc.spop.model.Result;

/***********************************************************
 * 2018.06.28	bks	OPENPOP V2 최초작업
 **********************************************************/

public interface ObjectAnalysisService {
	Result objectChangeChartList(Map<String, Object> param) throws Exception;
	List<Map<String, Object>> ObjectChangeAnalysisChartList(Map<String, Object> param);
	List<ObjectChange> tableChangeList(Map<String, Object> param) throws Exception;
	List<ObjectChange> columnChangeList(Map<String, Object> param) throws Exception;
	List<ObjectChange> indexChangeList(Map<String, Object> param) throws Exception;
}
