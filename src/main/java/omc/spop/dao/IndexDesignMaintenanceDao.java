package omc.spop.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.AccPath;
import omc.spop.model.AccPathIndexDesign;
import omc.spop.model.DbioLoadSql;
import omc.spop.model.IdxAdMst;
import omc.spop.model.OdsIndexs;
import omc.spop.model.OdsTabColumns;
import omc.spop.model.OdsTables;
import omc.spop.model.VsqlText;

/***********************************************************
 * 2018.03.15	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface IndexDesignMaintenanceDao {
	public String getAccessPathExec(OdsTables odsTables);
	
	public List<OdsTables> collectionOdsTableList(OdsTables odsTables);

	public List<AccPath> collectionAccessPathList(AccPath accPath);
	
	public List<VsqlText> collectionSqlStatusList(VsqlText vsqlText);

	public List<OdsIndexs> collectionIndexUsageList(OdsIndexs odsIndexs);	
	
	public List<OdsTables> loadOdsTableList(OdsTables odsTables);
	
	public List<AccPath> loadAccessPathList(AccPath accPath);
	
	public List<DbioLoadSql> loadSqlStatusList(DbioLoadSql dbioLoadSql);
	
	public List<OdsIndexs> loadUsingIndexList(OdsIndexs odsIndexs);
	
	/** 공통함수 **/
	public List<OdsTabColumns> columnsList(OdsTabColumns odsTabColumns);
	
	public List<OdsIndexs> indexsList(OdsIndexs odsIndexs);
	
	public List<AccPathIndexDesign> accPathIndexDesignList(AccPathIndexDesign accPathIndexDesign);
	
	public void deleteIndexDesign(AccPathIndexDesign accPathIndexDesign);
	
	public String getMaxIndexSeq(AccPathIndexDesign accPathIndexDesign);
	
	public void insertIndexDesign(AccPathIndexDesign accPathIndexDesign);

	public List<IdxAdMst> idxAdMstList(IdxAdMst idxAdMst);
	
	public List<AccPathIndexDesign> indexDesignList(AccPathIndexDesign accPathIndexDesign);

	public List<HashMap<String, Object>> getIndexInfoList(OdsIndexs odsIndexs);

	public List<LinkedHashMap<String, Object>> collectionOdsTableList4Excel(OdsTables odsTables);

	public List<LinkedHashMap<String, Object>> collectionIndexUsageList4Excel(OdsIndexs odsIndexs);

	public List<LinkedHashMap<String, Object>> loadUsingIndexList4Excel(OdsIndexs odsIndexs);

	public String getAccessPathExecAddSec(OdsTables odsTables);
}