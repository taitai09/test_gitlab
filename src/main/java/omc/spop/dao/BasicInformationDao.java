package omc.spop.dao;

import java.util.List;

import omc.spop.model.SpopPreferences;

/***********************************************************
 * 2017.12.05	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface BasicInformationDao {	
	public List<SpopPreferences> selectMenuGroupList(SpopPreferences spopPreferences);
	
	public List<SpopPreferences> selectBasicSystemInfoList(SpopPreferences spopPreferences);
	
	public List<SpopPreferences> selectBasicDBInfoList(SpopPreferences spopPreferences);
	
	public List<SpopPreferences> selectBasicWorkJobInfoList(SpopPreferences spopPreferences);
	
	public String getDBMaxPrefSeq(SpopPreferences spopPreferences);
	
	public void insertBasicDBInformation(SpopPreferences spopPreferences);
	
	public String getWorkJobMaxPrefSeq(SpopPreferences spopPreferences);
	
	public void insertBasicWorkJobInformation(SpopPreferences spopPreferences);	
	
	public void updateBasicInformation(SpopPreferences spopPreferences);
}
