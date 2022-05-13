package omc.spop.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.BasicInformationDao;
import omc.spop.model.SpopPreferences;
import omc.spop.service.BasicInformationService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.12.05	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 * 2020.12.22	황예지	InsertWorkJobInfo 추가(업무기준정보 설정 변경)
 **********************************************************/

@Service("BasicInformationService")
public class BasicInformationServiceImpl implements BasicInformationService {
	private static final Logger logger = LoggerFactory.getLogger(BasicInformationServiceImpl.class);
	@Autowired
	private BasicInformationDao basicInformationDao;

	@Override
	public List<SpopPreferences> selectMenuGroupList(SpopPreferences spopPreferences) throws Exception {
		return basicInformationDao.selectMenuGroupList(spopPreferences);
	}
	
	@Override
	public List<SpopPreferences> selectBasicSystemInfoList(SpopPreferences spopPreferences) throws Exception {
		return basicInformationDao.selectBasicSystemInfoList(spopPreferences);
	}
	
	@Override
	public List<SpopPreferences> selectBasicDBInfoList(SpopPreferences spopPreferences) throws Exception {
		return basicInformationDao.selectBasicDBInfoList(spopPreferences);
	}
	
	@Override
	public List<SpopPreferences> selectBasicWorkJobInfoList(SpopPreferences spopPreferences) throws Exception {
		return basicInformationDao.selectBasicWorkJobInfoList(spopPreferences);
	}

	@Override
	public String insertBasicInfomation(SpopPreferences spopPreferences) throws Exception {
		String isOk = "N";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String[] prefIdArry = null;
		String[] prefValueArry = null;
		String prefSeq = "";
		try{
			prefIdArry = StringUtil.split(spopPreferences.getPrefIdArry(), "|");	
			prefValueArry = StringUtil.split(spopPreferences.getPrefValueArry(), "|");

			if(!spopPreferences.getPrefIdArry().equals("")){
				for (int i = 0; i < prefIdArry.length; i++) {
					SpopPreferences temp = new SpopPreferences();
					
					if(spopPreferences.getPref_mgmt_type_cd().equals("1")){ // 시스템 기준정보 저장
						temp.setPref_id(prefIdArry[i]);
						temp.setPref_value(prefValueArry[i]);
						
						//1. SPOP_PREFERENCES UPDATE
						basicInformationDao.updateBasicInformation(temp);
					}else if(spopPreferences.getPref_mgmt_type_cd().equals("2")){ // DB 기준정보 저장
						temp.setDbid(spopPreferences.getDbid());
						temp.setPref_id(prefIdArry[i]);
						temp.setPref_value(prefValueArry[i]);
						temp.setPref_reg_id(user_id);
						
						// 1. DB_PREFERENCES MAX PREF_SEQ 조회
						prefSeq = basicInformationDao.getDBMaxPrefSeq(temp);
						
						temp.setPref_seq(prefSeq);
						
						// 2. DB_PREFERENCES INSERT				
						basicInformationDao.insertBasicDBInformation(temp);
					}else{ // 업무기준정보 저장
						temp.setWrkjob_cd(spopPreferences.getWrkjob_cd());
						temp.setPref_id(prefIdArry[i]);
						temp.setPref_value(prefValueArry[i]);
						temp.setPref_reg_id(user_id);
						
						// 1. WRKJOB_PREFERENCES MAX PREF_SEQ 조회
						prefSeq = basicInformationDao.getWorkJobMaxPrefSeq(temp);
						
						temp.setPref_seq(prefSeq);
						
						// 2. WRKJOB_PREFERENCES INSERT				
						basicInformationDao.insertBasicWorkJobInformation(temp);						
					}
				}
			}
			isOk = "Y";
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ERROR ==> " + ex.getMessage());
			throw ex;
		}
		
		return isOk;
	}
	
	@Override
	public String insertBasicInfomation(List<SpopPreferences> spopPreferencesList) throws Exception {
		String isOk = "N";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String prefSeq = "";
		
		try{
			if(spopPreferencesList != null){
				for (SpopPreferences spopPreference : spopPreferencesList) {
					
					spopPreference.setPref_reg_id(user_id);
					if( "2".equals( spopPreference.getPref_mgmt_type_cd() )){ // DB 기준정보 저장
						// 1. DB_PREFERENCES MAX PREF_SEQ 조회
						prefSeq = basicInformationDao.getDBMaxPrefSeq(spopPreference);

						spopPreference.setPref_seq(prefSeq);
						
						// 2. DB_PREFERENCES INSERT				
						basicInformationDao.insertBasicDBInformation(spopPreference);
						
					}else if( "3".equals( spopPreference.getPref_mgmt_type_cd() )){// 업무기준정보 저장
						// 1. WRKJOB_PREFERENCES MAX PREF_SEQ 조회
						prefSeq = basicInformationDao.getWorkJobMaxPrefSeq(spopPreference);
						spopPreference.setPref_seq(prefSeq);
						
						// 2. WRKJOB_PREFERENCES INSERT
						basicInformationDao.insertBasicWorkJobInformation(spopPreference);
					}
					
				}
			}
			isOk = "Y";
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " ERROR ==> " + ex.getMessage());
			throw ex;
			
		}finally {
			user_id = null;
			prefSeq = null;
			spopPreferencesList = null;
		}
		
		return isOk;
	}	
	
}
