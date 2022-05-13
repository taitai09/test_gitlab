package omc.spop.service;

import java.util.List;

import omc.spop.model.SpopPreferences;

/***********************************************************
 * 2017.12.05	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업 
 * 2020.12.22	황예지	InsertWorkJobInfo 추가(업무기준정보 설정 변경)
 **********************************************************/

public interface BasicInformationService {
	/** Menu Group 리스트 */
	List<SpopPreferences> selectMenuGroupList(SpopPreferences spopPreferences) throws Exception;
	
	/** 시스템기준정보 리스트 */
	List<SpopPreferences> selectBasicSystemInfoList(SpopPreferences spopPreferences) throws Exception;
	
	/** DB기준정보 리스트 */
	List<SpopPreferences> selectBasicDBInfoList(SpopPreferences spopPreferences) throws Exception;
	
	/** 업무기준정보 리스트 */
	List<SpopPreferences> selectBasicWorkJobInfoList(SpopPreferences spopPreferences) throws Exception;	
	
	/** 기준정보 저장 */
	String insertBasicInfomation(SpopPreferences spopPreferences) throws Exception;

	String insertBasicInfomation(List<SpopPreferences> spopPreferencesList) throws Exception;	
}