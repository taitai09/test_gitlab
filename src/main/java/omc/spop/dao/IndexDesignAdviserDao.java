package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import omc.spop.model.AccPathExec;
import omc.spop.model.AccPathExecV2;
import omc.spop.model.IdxAdMst;
import omc.spop.model.IdxAdRecommendIndex;

/***********************************************************
 * 2018.03.20	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface IndexDesignAdviserDao {	
	public List<IdxAdMst> autoIndexStatusList(IdxAdMst idxAdMst);
	
	public List<LinkedHashMap<String, Object>> autoIndexStatusList4Excel(IdxAdMst idxAdMst);

	public List<AccPathExecV2> autoCollectionIndexDesignList(AccPathExec accPathExec);
	
	public List<LinkedHashMap<String, Object>> autoCollectionIndexDesignList4Excel(AccPathExec accPathExec);

	public List<AccPathExecV2> autoLoadIndexDesignList(AccPathExec accPathExec);
	
	public List<IdxAdRecommendIndex> indexRecommendStatusList(IdxAdRecommendIndex idxAdRecommendIndex);
	
	public int updateForceComplete(Map<String, List<String>> param);
	
	public int updateForceCompleteList(List<?> paramList);
	
	public List<AccPathExecV2> isTaskStartIndexAutoDesign(AccPathExec accPathExec);
}
