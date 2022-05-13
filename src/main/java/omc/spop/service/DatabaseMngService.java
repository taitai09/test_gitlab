package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import omc.spop.model.AgentFailover;
import omc.spop.model.AlertConfig;
import omc.spop.model.Database;
import omc.spop.model.Instance;
import omc.spop.model.InstanceV2;
import omc.spop.model.RgbColor;

/***********************************************************
 * 2017.12.08 이원식 최초작성 
 * 2018.03.07 이원식 OPENPOP V2 최초작업
 **********************************************************/

public interface DatabaseMngService {
	/** Database 관리 리스트 */
	List<Database> databaseList(Database database) throws Exception;

	/** Database save */
	int saveDatabase(Database database) throws Exception;

	/** 인스턴스 관리 리스트 */
	List<Instance> instanceList(Instance instance) throws Exception;

	/** 인스턴스 사용 RGB Color 리스트 */
	List<RgbColor> rgbColorList(RgbColor rgbColor) throws Exception;

	/** RGB Color 리스트 */
	List<RgbColor> getRGBColor() throws Exception;

	/** Database에서 사용중인 RGB Color 리스트 */
	List<RgbColor> checkRGBColorDatabase() throws Exception;
	
	/** Instance에서 사용중인 RGB Color 리스트 */
	List<RgbColor> checkRGBColorInstance() throws Exception;

//	/** 인스턴스 save */
//	int saveInstance(InstanceV2 instance) throws Exception;

	/** agentFailover 리스트 */
	List<AgentFailover> agentFailoverList(AgentFailover agentFailover) throws Exception;

	/** agentFailover save */
	void saveAgentFailover(HttpServletRequest req) throws Exception;

	/** alert 설정 리스트 */
	List<AlertConfig> alertSettingList(AlertConfig alertConfig) throws Exception;

	/** alert 설정 save */
	void saveAlertSetting(HttpServletRequest req) throws Exception;

	/** instance 삭제 */
	int deleteInstance(omc.spop.model.Instance instance);

	List<LinkedHashMap<String, Object>> databaseListByExcelDown(Database database);

	List<LinkedHashMap<String, Object>> instanceListByExcelDown(omc.spop.model.Instance instance);
	
	/** Database의 dbid 존재 개수 파악 */
	List<Database> notExistDbid(Database database) throws Exception;

	int saveInstance(Instance instance) throws Exception;
}
