package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.maven.artifact.ant.shaded.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.IndexDesignMaintenanceDao;
import omc.spop.model.AccPath;
import omc.spop.model.AccPathIndexDesign;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.IdxAdMst;
import omc.spop.model.OdsIndexs;
import omc.spop.model.OdsTabColumns;
import omc.spop.model.OdsTables;
import omc.spop.model.VsqlText;
import omc.spop.server.tune.PerfImprMgmt;
import omc.spop.server.tune.SPopIndexAutoDesign;
import omc.spop.service.IndexDesignMaintenanceService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.15	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("IndexDesignMaintenanceService")
public class IndexDesignMaintenanceServiceImpl implements IndexDesignMaintenanceService {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexDesignMaintenanceServiceImpl.class);
	
	@Autowired
	private IndexDesignMaintenanceDao indexDesignMaintenanceDao;

	@Override
	public String getAccessPathExec(OdsTables odsTables) throws Exception {
//		return indexDesignMaintenanceDao.getAccessPathExec(odsTables);
		return indexDesignMaintenanceDao.getAccessPathExecAddSec(odsTables);
	}
	
	@Override
	public List<OdsTables> collectionOdsTableList(OdsTables odsTables) throws Exception {
		return indexDesignMaintenanceDao.collectionOdsTableList(odsTables);
	}

	@Override
	public List<AccPath> collectionAccessPathList(AccPath accPath) throws Exception {
		return indexDesignMaintenanceDao.collectionAccessPathList(accPath);
	}
	
	@Override
	public List<VsqlText> collectionSqlStatusList(VsqlText vsqlText) throws Exception {
		return indexDesignMaintenanceDao.collectionSqlStatusList(vsqlText);
	}	

	@Override
	public List<OdsIndexs> collectionIndexUsageList(OdsIndexs odsIndexs) throws Exception {
		return indexDesignMaintenanceDao.collectionIndexUsageList(odsIndexs);
	}	
	
	@Override
	public List<OdsTables> loadOdsTableList(OdsTables odsTables) throws Exception {
		return indexDesignMaintenanceDao.loadOdsTableList(odsTables);
	}
	
	@Override
	public List<AccPath> loadAccessPathList(AccPath accPath) throws Exception {
		return indexDesignMaintenanceDao.loadAccessPathList(accPath);
	}
	
	@Override
	public List<DbioLoadSql> loadSqlStatusList(DbioLoadSql dbioLoadSql) throws Exception {
		return indexDesignMaintenanceDao.loadSqlStatusList(dbioLoadSql);
	}	
	
	@Override
	public List<OdsIndexs> loadUsingIndexList(OdsIndexs odsIndexs) throws Exception {
		return indexDesignMaintenanceDao.loadUsingIndexList(odsIndexs);
	}	
	
	/** 공통 함수 **/
	@Override
	public List<OdsTabColumns> columnsList(OdsTabColumns odsTabColumns) throws Exception {
		return indexDesignMaintenanceDao.columnsList(odsTabColumns);
	}	

	@Override
	public List<AccPathIndexDesign> accPathIndexDesignList(AccPathIndexDesign accPathIndexDesign) throws Exception {
		return indexDesignMaintenanceDao.accPathIndexDesignList(accPathIndexDesign);
	}
	
	@Override
	public void insertIndexDesign(AccPathIndexDesign accPathIndexDesign) throws Exception {
		AccPathIndexDesign temp = new AccPathIndexDesign();
		String[] indexColumnArry = null;
		String maxIndexSeq = "";
		// 1. 해당 index 정보 삭제
		indexDesignMaintenanceDao.deleteIndexDesign(accPathIndexDesign);
			
		// 2. indexColumn 배열만큼 반복 처리
		indexColumnArry = StringUtil.split(accPathIndexDesign.getIndexColumnArry(), "|");
		
		if(accPathIndexDesign.getIndexColumnArry().trim().length() > 1){				
			for(int i = 0 ; i < indexColumnArry.length ; i++){
				// 3. 일련번호 조회
				maxIndexSeq = indexDesignMaintenanceDao.getMaxIndexSeq(accPathIndexDesign);
				
				// 4. Index 정보 INSERT
				temp.setDbid(accPathIndexDesign.getDbid());
				temp.setExec_seq(accPathIndexDesign.getExec_seq());
				temp.setTable_owner(accPathIndexDesign.getTable_owner());
				temp.setTable_name(accPathIndexDesign.getTable_name());
				temp.setIndex_seq(maxIndexSeq);
				temp.setIndex_column_list(indexColumnArry[i]);
				
				indexDesignMaintenanceDao.insertIndexDesign(temp);
			}
		}
	}	
	
	@Override
	public List<IdxAdMst> startIndexAutoDesign(OdsTables odsTables) throws Exception {
		IdxAdMst temp = new IdxAdMst();
		String idxAdNo = "";
		try{
			/*idxAdNo = SPopIndexAutoDesign.startIndexDesign(StringUtil.parseLong(odsTables.getDbid(), 0),
					StringUtil.parseLong(odsTables.getExec_seq(), 0), odsTables.getOwner(), odsTables.getTable_name(),
					StringUtil.parseDouble(odsTables.getNdv_ratio(), 0),
					StringUtil.parseLong(odsTables.getCol_null(), 0));*/
			idxAdNo = SPopIndexAutoDesign.startIndexDesign2(StringUtil.parseLong(odsTables.getDbid(), 0),
					StringUtil.parseLong(odsTables.getExec_seq().toString(), 0), 
					odsTables.getOwner(), 
					odsTables.getTable_name(),
					StringUtils.defaultString(odsTables.getAccess_path_type()),
					odsTables.getSelectivity_calc_method());
		}catch(Exception ex){
			logger.error("error ==> " + ex.getMessage());
			throw ex;
		}

		temp.setDbid(odsTables.getDbid());
		temp.setExec_seq(odsTables.getExec_seq());
		temp.setIdx_ad_no(idxAdNo);
		temp.setTable_owner(odsTables.getOwner());
		temp.setTable_name(odsTables.getTable_name());
		
		return indexDesignMaintenanceDao.idxAdMstList(temp);
	}
	
	@Override
	public List<AccPathIndexDesign> indexDesignList(AccPathIndexDesign accPathIndexDesign) throws Exception {
		return indexDesignMaintenanceDao.indexDesignList(accPathIndexDesign);
	}
	/**
	 * master call get indexsList
	 */
	@Override
	public List<OdsIndexs> indexsList(OdsIndexs odsIndexs) throws Exception {
		return indexDesignMaintenanceDao.indexsList(odsIndexs);
	}
	/**
	 * collect call get indexList
	 */
	@Override
	public List<OdsIndexs> indexList(OdsIndexs odsIndexs) throws Exception {
		List<OdsIndexs> resultList = new ArrayList<OdsIndexs>();
		try {
			long dbid = StringUtil.parseLong(odsIndexs.getDbid(), 0);
			String tableOwner = odsIndexs.getOwner();
			String tableName = odsIndexs.getTable_name();
			logger.debug("dbid:"+dbid);
			logger.debug("tableOwner:"+tableOwner);
			logger.debug("tableName:"+tableName);
			//서버 오류 발생
			List<HashMap<String,Object>> indexInfoList = PerfImprMgmt.getIndexInfoList(dbid, tableOwner, tableName);
			//로컬 Test OK
//			List<HashMap<String,Object>> indexInfoList = indexDesignMaintenanceDao.getIndexInfoList(odsIndexs);
			for(int i=0;i<indexInfoList.size();i++){
				OdsIndexs odsIdx = new OdsIndexs();
				HashMap<String, Object> map = indexInfoList.get(i);
				Long mapRnum = (Long) map.get("RNUM");
				String mapTableName = (String) map.get("TABLE_NAME");
				String mapIndexName = (String) map.get("INDEX_NAME");
				String mapIndexColumn = (String) map.get("INDEX_COLUMN");
				String mapUniqueness = (String) map.get("UNIQUENESS");
				String mapPartitioned = (String) map.get("PARTITIONED");

				odsIdx.setRnum(mapRnum.toString());
				odsIdx.setTable_name(mapTableName);
				odsIdx.setIndex_name(mapIndexName);
				odsIdx.setIndex_column(mapIndexColumn);
				odsIdx.setUniqueness(mapUniqueness);
				odsIdx.setPartitioned(mapPartitioned);
				resultList.add(odsIdx);
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			throw ex;
		}

		return resultList;
	}

	@Override
	public List<LinkedHashMap<String, Object>> collectionOdsTableList4Excel(OdsTables odsTables) {
		return indexDesignMaintenanceDao.collectionOdsTableList4Excel(odsTables);
	}

	@Override
	public List<LinkedHashMap<String, Object>> collectionIndexUsageList4Excel(OdsIndexs odsIndexs) {
		return indexDesignMaintenanceDao.collectionIndexUsageList4Excel(odsIndexs);
	}

	@Override
	public List<LinkedHashMap<String, Object>> loadUsingIndexList4Excel(OdsIndexs odsIndexs) {
		return indexDesignMaintenanceDao.loadUsingIndexList4Excel(odsIndexs);
	}

}