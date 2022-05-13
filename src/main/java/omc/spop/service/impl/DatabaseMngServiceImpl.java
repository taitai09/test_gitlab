package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.DatabaseMngDao;
import omc.spop.model.AgentFailover;
import omc.spop.model.AlertConfig;
import omc.spop.model.Database;
import omc.spop.model.Instance;
import omc.spop.model.InstanceV2;
import omc.spop.model.RgbColor;
import omc.spop.service.DatabaseMngService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.12.08	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("DatabaseMngService")
public class DatabaseMngServiceImpl implements DatabaseMngService {
	@Autowired
	private DatabaseMngDao databaseMngDao;

	@Override
	public List<Database> databaseList(Database database) throws Exception {
		return databaseMngDao.databaseList(database);
	}

	@Override
	public int saveDatabase(Database database) throws Exception {
//		databaseMngDao.saveDatabase(database);
		int check = 0 ;
		if(database.getCrud_flag().equals("C")){
				check = databaseMngDao.checkDbNmByDatabase(database);
				if(check > 0)
					throw new Exception("이미 [ " +database.getDb_name()+" ]은(는) 사용중 입니다.<br/>다른 DB명을 입력해 주세요.");
				
			check = databaseMngDao.insertDatabase(database);
				
		}else{
			if(!database.getOld_db_name().equals(database.getDb_name())){
				check = databaseMngDao.checkDbNmByDatabase(database);
				if(check > 0)
					throw new Exception("이미 [ " +database.getDb_name()+" ]은(는) 사용중 입니다.<br/>다른 DB명을 입력해 주세요.");
			}
			check = databaseMngDao.updateDatabase(database);
			
		}
		
		return check;
	}
	
	@Override
	public List<Instance> instanceList(Instance instance) throws Exception {
		return databaseMngDao.instanceList(instance);
	}
	
	@Override
	public List<RgbColor> rgbColorList(RgbColor rgbColor) throws Exception {
		return databaseMngDao.rgbColorList(rgbColor);
	}
	
	@Override
	public List<RgbColor> getRGBColor() throws Exception {
		return databaseMngDao.getRGBColor();
	}
	
	@Override
	public List<RgbColor> checkRGBColorDatabase() throws Exception {
		return databaseMngDao.checkRGBColorDatabase();
	}
	
	@Override
	public List<RgbColor> checkRGBColorInstance() throws Exception {
		return databaseMngDao.checkRGBColorInstance();
	}
	
	@Override
	public int saveInstance(Instance instance) throws Exception {
		int check = 0;
		
		if(instance.getCrud_flag().equals("C")){
			
			check = databaseMngDao.checkInstIdByInstance(instance);
			if(check > 0)
				throw new Exception("[ "+instance.getDb_name() + " ]에 동일한 인스턴스번호가 이 있습니다.<br/> 다른 인스턴스번호를 사용해 주세요.");
			
			check = databaseMngDao.checkInstNmByInstance(instance);
			if(check > 0)
				throw new Exception("[ " + instance.getInst_name() +" ]은(는) "+ "이미 사용중입니다. <br/> 다른 인스턴스명을 사용해 주세요.");
			
			check = databaseMngDao.insertInstance(instance);
		}else{
			if(!instance.getOld_inst_name().equals(instance.getInst_name())){
				check = databaseMngDao.checkInstNmByInstance(instance);
				if(check > 0)
					throw new Exception("[ " + instance.getInst_name() +" ]은(는) "+ "이미 사용중입니다. <br/> 다른 인스턴스명을 사용해 주세요.");

				check = databaseMngDao.updateInstance(instance);
			}else{
				//업데이트시 인스턴스명을 변경하지 않았을 시
				check = databaseMngDao.updateInstance(instance);
			}
		}
		
		return check;
	}
	
	@Override
	public List<AgentFailover> agentFailoverList(AgentFailover agentFailover) throws Exception {
		return databaseMngDao.agentFailoverList(agentFailover);
	}	
	
	@Override
	public void saveAgentFailover(HttpServletRequest req) throws Exception {
		String dbid = StringUtil.nvl(req.getParameter("dbid"));
		String[] agentTypeCdArr = req.getParameterValues("agent_type_cd");
		String[] firstInstIdArr = req.getParameterValues("first_inst_id");
		String[] secondaryInstIdArr = req.getParameterValues("secondary_inst_id");
		
		if(agentTypeCdArr != null){
			for(int i = 0; i < agentTypeCdArr.length ; i++){
				AgentFailover agentFailover = new AgentFailover();
				
				agentFailover.setDbid(dbid);
				agentFailover.setAgent_type_cd(agentTypeCdArr[i]);
				agentFailover.setFirst_inst_id(firstInstIdArr[i]);
				agentFailover.setSecondary_inst_id(secondaryInstIdArr[i]);

				databaseMngDao.saveAgentFailover(agentFailover);
			}
		}
	}		
	
	@Override
	public List<AlertConfig> alertSettingList(AlertConfig alertConfig) throws Exception {
		return databaseMngDao.alertSettingList(alertConfig);
	}
	
	@Override
	public void saveAlertSetting(HttpServletRequest req) throws Exception {
		String dbid = StringUtil.nvl(req.getParameter("dbid"));
		String inst_id = StringUtil.nvl(req.getParameter("inst_id"));
		String[] alertTypeCdArr = req.getParameterValues("alert_type_cd");
		String[] alertThresholdArr = req.getParameterValues("alert_threshold");
		String[] enableYnArr = req.getParameterValues("enable_yn");
		String[] smsSendYnArr = req.getParameterValues("sms_send_yn");
		
		if(alertTypeCdArr != null){
			for(int i = 0; i < alertTypeCdArr.length ; i++){
				AlertConfig alertConfig = new AlertConfig();
				
				alertConfig.setDbid(dbid);
				alertConfig.setInst_id(inst_id);
				alertConfig.setAlert_type_cd(alertTypeCdArr[i]);
				alertConfig.setAlert_threshold(alertThresholdArr[i]);
				alertConfig.setEnable_yn(enableYnArr[i]);
				alertConfig.setSms_send_yn(smsSendYnArr[i]);
				
				databaseMngDao.saveAlertSetting(alertConfig);
			}
		}
	}

	@Override
	public int deleteInstance(Instance instance) {
		return databaseMngDao.deleteInstance(instance);
	}

	@Override
	public List<LinkedHashMap<String, Object>> databaseListByExcelDown(Database database) {
		return databaseMngDao.databaseListByExcelDown(database);
	}

	@Override
	public List<LinkedHashMap<String, Object>> instanceListByExcelDown(Instance instance) {
		return databaseMngDao.instanceListByExcelDown(instance);
	}	

	@Override
	public List<Database> notExistDbid(Database database) throws Exception {
		return databaseMngDao.notExistDbid(database);
	}
}