package omc.spop.dao;

import java.util.List;
import java.util.Map;

import omc.spop.model.ObjectChange;

/***********************************************************
 * 2018.06.28	bks	OPENPOP V2 최초작업
 **********************************************************/

public interface ObjectAnalysisDao {		

	public List<Map<String,Object>> objectChangeChartList(Map<String, Object> param);

	public List<Map<String, Object>> ObjectChangeAnalysisChartList(Map<String, Object> param);

	public List<ObjectChange> tableChangeList(Map<String, Object> param);

	public List<ObjectChange> columnChangeList(Map<String, Object> param);

	public List<ObjectChange> indexChangeList(Map<String, Object> param);


}
