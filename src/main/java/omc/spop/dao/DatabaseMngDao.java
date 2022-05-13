package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.AgentFailover;
import omc.spop.model.AlertConfig;
import omc.spop.model.Database;
import omc.spop.model.Instance;
import omc.spop.model.InstanceV2;
import omc.spop.model.RgbColor;

/***********************************************************
 * 2017.12.08	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업 
 **********************************************************/

public interface DatabaseMngDao {	
	public List<Database> databaseList(Database database);
	
	public int saveDatabase(Database database);
	
	public List<Instance> instanceList(Instance instance);
	
	public List<RgbColor> rgbColorList(RgbColor rgbColor);
	
	public List<RgbColor> getRGBColor();
	
	public List<RgbColor> checkRGBColorDatabase();
	
	public List<RgbColor> checkRGBColorInstance();
	
	public int saveInstance(InstanceV2 instance);
	
	public List<AgentFailover> agentFailoverList(AgentFailover agentFailover);
	
	public void saveAgentFailover(AgentFailover agentFailover);
	
	public List<AlertConfig> alertSettingList(AlertConfig alertConfig);
	
	public void saveAlertSetting(AlertConfig alertConfig);

	public int deleteInstance(Instance instance);

	public List<LinkedHashMap<String, Object>> databaseListByExcelDown(Database database);

	public List<LinkedHashMap<String, Object>> instanceListByExcelDown(Instance instance);
	
	public List<Database> notExistDbid(Database database);

	public int checkInstIdByInstance(Instance instance);

	public int checkInstNmByInstance(Instance instance);

	public int insertInstance(Instance instance);

	public int updateInstance(Instance instance);

	public int checkDbNmByDatabase(Database database);

	public int insertDatabase(Database database);

	public int updateDatabase(Database database);
}
