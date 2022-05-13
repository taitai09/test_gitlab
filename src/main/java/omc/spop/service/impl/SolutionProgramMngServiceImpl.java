package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.SolutionProgramMngDao;
import omc.spop.model.SQLStandards;
import omc.spop.model.SolutionProgramMng;
import omc.spop.service.SolutionProgramMngService;
import omc.spop.utils.ConvertRecord;
import omc.spop.utils.CryptoUtil;
import oracle.sql.CLOB;

/***********************************************************
 * 2017.11.09	이원식	최초작성
 * 2018.10.17	임호경	유저권한 수정
 **********************************************************/

@Service("SolutionProgramMngService")
public class SolutionProgramMngServiceImpl implements SolutionProgramMngService {
	private static final Logger logger = LoggerFactory.getLogger(SolutionProgramMngServiceImpl.class);

	@Autowired
	private SolutionProgramMngDao solutionProgramMngDao;

	private String key = "openmade";
	
	@Override
	public List<SolutionProgramMng> programListTree(SolutionProgramMng solutionProgramMng) {
		return solutionProgramMngDao.programListTree(solutionProgramMng);
	}

	@Override
	public int saveSolutionProgramListMng(SolutionProgramMng solutionProgramMng) throws Exception {
		int check = 0; boolean keepGoing = false;
		List<String> parentSolutionProgramMngIdList = new ArrayList<String>();
		List<String> finalParentSolutionProgramMngIdList = null;
		List<String> checkSolutionProgramMngIdList = null;
		List<String> haveToCheckList = null;
		logger.debug("파라미터값 확인 ::: " + solutionProgramMng);

		if(!StringUtils.defaultString(solutionProgramMng.getContents_id()).equals("")){ // CONTENTS_ID가 있다면 수정하는경우
		
			if(StringUtils.defaultString(solutionProgramMng.getContents_id()).equals(StringUtils.defaultString(solutionProgramMng.getParent_contents_id()))){
				throw new Exception("해당목차를 상위목차로 저장할 수 없습니다.<br/>관리자에게 문의해주세요.");
			}
			parentSolutionProgramMngIdList = solutionProgramMngDao.getParentContentsIdList(solutionProgramMng.getContents_id());
			if(parentSolutionProgramMngIdList.size() > 0){
				keepGoing = true;
				finalParentSolutionProgramMngIdList = new ArrayList<String>();
				haveToCheckList = new ArrayList<String>();
				finalParentSolutionProgramMngIdList.addAll(parentSolutionProgramMngIdList);
			}
			
			while(keepGoing){
				for(int i = 0; i < parentSolutionProgramMngIdList.size(); i++){
					checkSolutionProgramMngIdList = new ArrayList<String>();
					checkSolutionProgramMngIdList = solutionProgramMngDao.getParentContentsIdList(parentSolutionProgramMngIdList.get(i));
					if(checkSolutionProgramMngIdList.size() > 0){
						finalParentSolutionProgramMngIdList.addAll(checkSolutionProgramMngIdList);
						haveToCheckList.addAll(checkSolutionProgramMngIdList);
					}
				}
			
				if(haveToCheckList.size() > 0){
					parentSolutionProgramMngIdList = haveToCheckList;
					haveToCheckList = new ArrayList<String>();
				}else{
					break;
				}
			}
			if(keepGoing){
				logger.debug("###### 자신의 하위목차 :"+finalParentSolutionProgramMngIdList.toString());
				logger.debug("###### 변경할 상위목차:"+solutionProgramMng.getParent_contents_id());
				if(finalParentSolutionProgramMngIdList.contains(solutionProgramMng.getParent_contents_id())){
					throw new Exception("자신의 하위목차를 상위목차로 지정 할 수 없습니다.");
				}
			}
		
			check += solutionProgramMngDao.updateSolutionProgramListMng(solutionProgramMng);
			
		}else{
			check += solutionProgramMngDao.insertSolutionProgramListMng(solutionProgramMng);
		}
		
		return check;
	}

	@Override
	public int saveMultiSolutionProgramListMng(SolutionProgramMng solutionProgramMng)  throws Exception {
		int check = 0;
		boolean keepGoing = false;
		List<String> parentContentsIdList = new ArrayList<String>();
		List<String> finalParentContentsIdList = null;
		List<String> checkContentsIdList = null;
		List<String> haveToCheckList = null;
		finalParentContentsIdList = new ArrayList<String>();
		logger.debug("파라미터값 확인 ::: " + solutionProgramMng);
		
		for(int x = 0; x < solutionProgramMng.getContents_id_list().split(",").length; x++){

			if(solutionProgramMng.getContents_id_list().split(",")[x].equals(StringUtils.defaultString(solutionProgramMng.getParent_contents_id()))){
				throw new Exception("해당메뉴를 상위메뉴로 저장할 수 없습니다.");
			}
			
			parentContentsIdList = solutionProgramMngDao.getParentContentsIdList(StringUtils.defaultString(solutionProgramMng.getContents_id_list().split(",")[x]));
			if(parentContentsIdList.size() > 0){
				keepGoing = true;
//				finalParentContentsIdList = new ArrayList<String>();
				haveToCheckList = new ArrayList<String>();
				finalParentContentsIdList.addAll(parentContentsIdList);
			}
			
			while(keepGoing){
				for(int i = 0; i < parentContentsIdList.size(); i++){
					checkContentsIdList = new ArrayList<String>();
					checkContentsIdList = solutionProgramMngDao.getParentContentsIdList(parentContentsIdList.get(i));
					if(checkContentsIdList.size() > 0){
						haveToCheckList.addAll(checkContentsIdList);
						finalParentContentsIdList.addAll(checkContentsIdList);
					}
				}
			
				if(haveToCheckList.size() > 0){
					parentContentsIdList = haveToCheckList;
					haveToCheckList = new ArrayList<String>();
				}else{
					break;
				}
			}
			
		}
		
		if(keepGoing){
			logger.debug("###### 자신의 하위메뉴 :"+finalParentContentsIdList.toString()+" [n]번째 ######");
			logger.debug("###### 변경할 상위메뉴:"+solutionProgramMng.getParent_contents_id()+" [n]번째 ######");
			if(finalParentContentsIdList.contains(solutionProgramMng.getParent_contents_id())){
				throw new Exception("자신의 하위메뉴를 상위메뉴로 지정 할 수 없습니다.");
			}
		}
		
		for(int i = 0; i < solutionProgramMng.getContents_id_list().split(",").length; i++){
				solutionProgramMng.setContents_id(solutionProgramMng.getContents_id_list().split(",")[i]);
				solutionProgramMng.setContents_ordering(solutionProgramMng.getContents_ordering_list().split(",")[i]);
				solutionProgramMng.setContents_name(null);
				solutionProgramMng.setContents_desc(null);
				solutionProgramMng.setContents_url_addr(null);
				solutionProgramMng.setExadata_contents_yn(null);
				
				check += solutionProgramMngDao.updateSolutionProgramListMng(solutionProgramMng);

		}
		return check;
	}

	@Override
	public int deleteSolutionProgramListMng(SolutionProgramMng solutionProgramMng) {
		return solutionProgramMngDao.deleteSolutionProgramListMng(solutionProgramMng);
	}

	@Override
	public boolean contentsIsEmpty(SolutionProgramMng solutionProgramMng) {
		int checkList = solutionProgramMngDao.contentsIsEmpty(solutionProgramMng);
		System.out.println("하위메뉴 갯수 확인 : " + checkList);
		return (checkList == 0) ? true : false;    // true면 삭제가능 ,
	}

	@Override
	public List<SolutionProgramMng> programRule(SolutionProgramMng solutionProgramMng) {

		List<SolutionProgramMng> SolutionProgramMngList = null;
		List<SolutionProgramMng> SolutionProgramMngListOut = new ArrayList<SolutionProgramMng>();
		// sltProgramChkSqlOut = sltProgramChkSql;
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();

		if(user_auth_id.equals("9")){
			//OPENPOP-MANAGER 일 경우
			SolutionProgramMngList = solutionProgramMngDao.programRule(solutionProgramMng);
			for (SolutionProgramMng programRule : SolutionProgramMngList) {
				// String qty_chk_sql =
				// CryptoUtil.decryptAES256(sltProgramChkSqlRow.getQty_chk_sql(),key);
				String slt_program_chk_sql = null;
				try {
					slt_program_chk_sql = programRule.getSlt_program_chk_sql();
					programRule.setSlt_encrypted_sql(slt_program_chk_sql);
					
					slt_program_chk_sql = CryptoUtil.decryptAES128(slt_program_chk_sql, key);
					
				} catch (Exception e) {
					slt_program_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
				}
				programRule.setSlt_program_chk_sql(slt_program_chk_sql);
				SolutionProgramMngListOut.add(programRule);
			}
		}else{
			SolutionProgramMngListOut = solutionProgramMngDao.programRule(solutionProgramMng);
		}

		return SolutionProgramMngListOut;
	}

	@Override
	public int saveProgramRule(SolutionProgramMng solutionProgramMng) throws Exception{

		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();
		String slt_program_chk_sql = "";
		
		if(user_auth_id.equals("9")){
			slt_program_chk_sql = CryptoUtil.encryptAES128(solutionProgramMng.getSlt_program_chk_sql(), key);
			solutionProgramMng.setSlt_program_chk_sql(slt_program_chk_sql);
		} else {
			slt_program_chk_sql = solutionProgramMng.getSlt_program_chk_sql().trim().replaceAll("\n","");
			solutionProgramMng.setSlt_program_chk_sql(slt_program_chk_sql);
		}


		if (solutionProgramMng.getCrud_flag().equals("C")){
			return solutionProgramMngDao.insertProgramRule(solutionProgramMng);
		} else {
			return solutionProgramMngDao.updateProgramRule(solutionProgramMng);

		}
	}
	
	
/*	@Override
	public int saveProgramRule(SolutionProgramMng solutionProgramMng) throws Exception{
		
		String slt_program_chk_sql = "";
		int check = 0;

		
		List<SolutionProgramMng> resultList = new ArrayList<SolutionProgramMng>();
		resultList = solutionProgramMngDao.getSolutionProgramListBack();

		
		for(int i =0; i < resultList.size(); i++){
			slt_program_chk_sql = CryptoUtil.encryptAES128(resultList.get(i).getSlt_program_chk_sql(), key);
			resultList.get(i).setSlt_program_chk_sql(slt_program_chk_sql);
			check += solutionProgramMngDao.updateProgramRule(resultList.get(i));

			System.out.println("updateRule :::: " + check);
		}
			
		return check;
	}*/

	@Override
	public int deleteProgramRule(SolutionProgramMng solutionProgramMng) {
		return solutionProgramMngDao.deleteProgramRule(solutionProgramMng);
	}

	@Override
	public List<Map<String, Object>> programRuleByExcelDown(SolutionProgramMng solutionProgramMng) {
		List<Map<String, Object>> sltProgramChkSql = null;
		List<Map<String, Object>> resultList = null;
		String slt_program_chk_sql = "";
		String user_auth_id = SessionManager.getLoginSession().getUsers().getAuth_grp_id();

			//OPENPOP-MANAGER 일 경우
			resultList = new ArrayList<Map<String, Object>>();
			sltProgramChkSql = solutionProgramMngDao.programRuleByExcelDown(solutionProgramMng);
			
			for (Map<String, Object> sltProgramChkSqlRow : sltProgramChkSql) {
				try {

					CLOB clob = (oracle.sql.CLOB) sltProgramChkSqlRow.get("SLT_PROGRAM_CHK_SQL");

					// logger.debug("qualityCheckSqlListByExcelDown Start 3 : " +
					// clob.stringValue());

					if(user_auth_id.equals("9")){
					//OPENPOP-MANAGER 일 경우	
						
						// slt_program_chk_sql =
						// CryptoUtil.decryptAES256(clob.stringValue(),key);
						slt_program_chk_sql = CryptoUtil.decryptAES128(clob.stringValue(), key);
						//logger.debug("slt_program_chk_sql : " + slt_program_chk_sql);
					}else{
						slt_program_chk_sql = clob.stringValue();
					}
					
				} catch (Exception e) {
					slt_program_chk_sql = "복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
					logger.error("Error : " + e.getMessage());
				}
				sltProgramChkSqlRow.put("SLT_PROGRAM_CHK_SQL", slt_program_chk_sql);
				resultList.add(sltProgramChkSqlRow);
			}
	
		// return
		// qualityStdInfoDao.qualityCheckSqlListByExcelDown(openmQtyChkSql);
		return resultList;
	}

	@Override
	public List<LinkedHashMap<String, Object>> dataCollectionStatus(SolutionProgramMng solutionProgramMng) throws Exception {
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		//int wrkjob_cd = sqlStandards.getWrkjob_cd();
		String base_day = solutionProgramMng.getBase_day();
		String dbid = solutionProgramMng.getDbid();
		List<SolutionProgramMng> sltProgramChkSql = solutionProgramMngDao.getSolutionProgramChkSql(solutionProgramMng);
		
		for (SolutionProgramMng SQLStandardsRow : sltProgramChkSql) {
			String slt_program_chk_sql = CryptoUtil.decryptAES128(SQLStandardsRow.getSlt_program_chk_sql(),key);
			
			SQLStandardsRow.setSlt_program_chk_sql(slt_program_chk_sql);
			SQLStandardsRow.setBase_day(base_day);
			SQLStandardsRow.setDbid(dbid);
			
			dataList = solutionProgramMngDao.dataCollectionStatusLoadSql(SQLStandardsRow);
		}
		
		return convertRecord(dataList);	
	}
	
	private List<LinkedHashMap<String, Object>> convertRecord(List<LinkedHashMap<String, Object>> dataList) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		ConvertRecord convertRecord = ConvertRecord.getInstance();
		
		convertRecord.converRecord(dataList);
		
		resultList = convertRecord.getResultData();
		//logger.debug("convertRecord resultList:" + resultList);
		
		return resultList;
	}
	

}
