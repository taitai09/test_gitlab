package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import omc.spop.dao.ExamineOpenPOPDao;
import omc.spop.model.Instance;
import omc.spop.model.JobSchedulerExecDetailLog;
import omc.spop.model.JobSchedulerExecLog;
import omc.spop.server.tools.OpenPopUtil;
import omc.spop.service.ExamineOpenPOPService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2019.12.21	명성태		OPENPOP V2 최초작업
 **********************************************************/

@Service("ExamineOpenPOPService")
public class ExamineOpenPOPServiceImpl implements ExamineOpenPOPService {
	private static final Logger logger = LoggerFactory.getLogger(ExamineOpenPOPServiceImpl.class);
	
	@Autowired
	private ExamineOpenPOPDao examineOpenPOPDao;
	
	@Value("#{defaultConfig['masterip']}")
	private String masterip;

	@Value("#{defaultConfig['masterport']}")
	private String masterport;
	
	@Value("#{defaultConfig['schedulerip']}")
	private String schedulerip;
	
	@Value("#{defaultConfig['schedulerport']}")
	private String schedulerport;
	
	@Value("#{defaultConfig['gather_server_ip']}")
	private String gather_server_ip;
	
	@Value("#{defaultConfig['gather_server_port']}")
	private String gather_server_port;
	
	@Value("#{defaultConfig['agent_timeout']}")
	private String agent_timeout;
	
	@Override
	public String getEcho(String ip, int port) {
		String status = "";
		
		try {
			status = OpenPopUtil.getEcho(ip, port);
			logger.debug("echo status:" + status);
			status = "true";
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			status = "false";
		}
		
		logger.debug("echo status ip[" + ip + "] port[" + port + "] status[" + status + "]");
		
		return status;
	}
	
	@Override
	public String getPing(String ip, int port) {
		String status = "";
		
		try {
			status = OpenPopUtil.getPing(ip, port);
			logger.debug("ping status:" + status);
			status = "true";
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			status = "false";
		}
		
		logger.debug("ping status ip[" + ip + "] port[" + port + "] status[" + status + "]");
		
		return status;
	}
	
	private String getFlowEcho(Long dbid) {
		String status = "";
		
		try {
			status = OpenPopUtil.getFlowEcho(dbid, Integer.parseInt(agent_timeout));
			logger.debug("flow echo status:" + status);
			status = "true";
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			status = "false";
		}
		
		logger.debug("echo status dbid[" + dbid + "] timeout[" + agent_timeout + "] status[" + status + "]");
		
		return status;
	}
	
	private String getFlowPing(Long dbid) {
		String status = "";
		
		try {
			status = OpenPopUtil.getFlowPing(dbid, Integer.parseInt(agent_timeout));
			logger.debug("flow echo status:" + status);
			status = "true";
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			status = "false";
		}
		
		logger.debug("echo status dbid[" + dbid + "] timeout[" + agent_timeout + "] status[" + status + "]");
		
		return status;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> rtrvSchedulerStatusHistory(JobSchedulerExecLog jobSchedulerExecLog) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> resultData = null;
		LinkedHashMap<String, Object> resultMeta = new LinkedHashMap<String, Object>();
		List<JobSchedulerExecLog> tempList = new ArrayList<JobSchedulerExecLog>();
		String tempDate = "";
		int schedulerStatus = -1; 
		String dbName = "";
		String head = "";
		
		tempList = examineOpenPOPDao.rtrvSchedulerStatusHistory(jobSchedulerExecLog);
		
		head = "base_day/기준일/Date;";
		
		for(int index = 0; index < tempList.size(); index++) {
			JobSchedulerExecLog temp = tempList.get(index);
			
			schedulerStatus = temp.getScheduler_job_status_cd();
			
			dbName = temp.getDb_name();
			
			if(head.indexOf(dbName) < 0) {
				head += dbName + "/" + dbName + "/Etc/false;";
				head += dbName + "_ID" + "/" + dbName + "_ID" + "/Etc/true;";
			}
			
			if(tempDate.equals("") && !tempDate.equals(temp.getBase_day())) {
				resultData = new LinkedHashMap<String, Object>();
				resultData.put("base_day", temp.getBase_day());
				resultData.put(dbName, schedulerStatus);
				resultData.put(dbName + "_ID", temp.getDbid());
			} else if(tempDate.equals(temp.getBase_day())) {
				resultData.put(temp.getDb_name(), schedulerStatus);
				resultData.put(temp.getDb_name() + "_ID", temp.getDbid());
			} else if(!tempDate.equals("") && !tempDate.equals(temp.getBase_day())) {
				resultList.add(resultData);
				resultData = new LinkedHashMap<String, Object>();
				resultData.put("base_day", temp.getBase_day());
				resultData.put(dbName, schedulerStatus);
				resultData.put(dbName + "_ID", temp.getDbid());
			}
			
			tempDate = temp.getBase_day();
		}
		
		if(head.lastIndexOf(";") == (head.length() - 1)) {
			head = head.substring(0, head.length() - 1);
		}
		
		resultMeta.put("HEAD", head);
		
		resultList.add(resultData);
		resultList.add(resultMeta);
		
		return resultList;
	}
	
	@Override
	public List<JobSchedulerExecLog> performSchedulerList(JobSchedulerExecLog jobSchedulerExecLog) throws Exception {
		return examineOpenPOPDao.performSchedulerList(jobSchedulerExecLog);
	}
	
	@Override
	public List<JobSchedulerExecDetailLog> performSchedulerDetailList(JobSchedulerExecDetailLog jobSchedulerExecDetailLog) throws Exception {
		return examineOpenPOPDao.performSchedulerDetailList(jobSchedulerExecDetailLog);
	}

	@Override
	public List<LinkedHashMap<String, Object>> performSchedulerListByExcelDown(JobSchedulerExecLog jobSchedulerExecLog) {
		return examineOpenPOPDao.performSchedulerListByExcelDown(jobSchedulerExecLog);
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> rtrvAgentStatus() throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
		int i_masterport = Integer.parseInt(masterport);
		
		data.put("agent", "DB");
		data.put("status", getStatusImg(getEcho(masterip, i_masterport)));
		resultList.add(data);
		
		data = new LinkedHashMap<String, Object>();
		data.put("agent", "Master");
		data.put("status", getStatusImg(getEcho(masterip, i_masterport)));
		resultList.add(data);
		
		if(schedulerip != null) {
			data = new LinkedHashMap<String, Object>();
			data.put("agent", "Scheduler");
			data.put("status", getStatusImg(getPing(schedulerip, Integer.parseInt(schedulerport))));
			resultList.add(data);
		}
		
		data = new LinkedHashMap<String, Object>();
		data.put("BASE_DAY", DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		resultList.add(data);
		
		return resultList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> instanceList() throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> resultData = null;
		List<Instance> instanceList = examineOpenPOPDao.instanceList();
		Instance instance = null;
		String dbName = "";
		String dbId= "";
		String instId= "";
		String instanceStatus = "";
		String collectStatus = "";
		String gatherStatus = "";
		
		for(int instanceIndex = 0; instanceIndex < instanceList.size(); instanceIndex++) {
			resultData = new LinkedHashMap<String, Object>();
			
			instance = instanceList.get(instanceIndex);
			dbName = instance.getDb_name();
			dbId = instance.getDbid();
			instId = instance.getInst_id();
			
			logger.debug("///// Go to Status [" + dbName + "]////////////////////////////");
			
			instanceStatus = getFlowPing(Long.parseLong(dbId));
			collectStatus = getFlowEcho(Long.parseLong(dbId));
			gatherStatus = getFlowEcho(Long.parseLong(dbId));
			
			logger.debug("instanceStatus[" + instanceStatus + "] collectStatus[" + collectStatus + "] gatherStatus[" + gatherStatus + "]");
			logger.debug("///// End of Status ////////////////////////////");
			
			resultData.put("db_name", dbName);
			resultData.put("dbid", dbId);
			resultData.put("instance", instId);
			resultData.put("instance_status", instanceStatus);
			resultData.put("collect_status", collectStatus);
			resultData.put("gather_status", gatherStatus);
			
			resultList.add(resultData);
		}
		
		return resultList;
	}
	
	private ArrayList<ArrayList> instanceArrayV2;
	
	@Override
	public String getGatherServer(String ip, int port) {
		String status = "";
		String result = "";
		String[] resultArray;
		String unitResult;
		ArrayList unitData;
		
		try {
			result = OpenPopUtil.getEcho(ip, port);
			logger.debug("getGatherServer result:" + result);
			
			instanceArrayV2 = new ArrayList();
			
			resultArray = result.split(";");
			
			for(int index = 0; index < resultArray.length; index++) {
				unitResult = resultArray[index];
				
				unitData = splitUnitResult(unitResult);
				
				if(divideUnit(Integer.parseInt(unitData.get(2) + "")) == 1) {
					status = unitData.get(3) + "".toLowerCase();
					
					continue;
				}
				
				instanceArrayV2.add(unitData);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			status = "false";
		}
		
		logger.debug("getGatherServer status ip[" + ip + "] port[" + port + "] status[" + status + "]");
		
		return status;
	}
	
	private ArrayList splitUnitResult(String unitResult) {
		ArrayList unitData = new ArrayList();
		String tmpUnitResult = unitResult;
		boolean processFlag = true;
		int index = -1;
		int preIndex = 0;
		
		while(processFlag) {
			index = tmpUnitResult.indexOf("|", preIndex);
			
			if(index == -1) {
				if(preIndex > 0 && tmpUnitResult.substring(preIndex).length() > 0) {
					unitData.add(tmpUnitResult.substring(preIndex));
				}
				
				processFlag = false;
				continue;
			}
			
			unitData.add(tmpUnitResult.substring(preIndex, index));
			
			preIndex = index + 1;
		}
		
		return unitData;
	}
	
	private int divideUnit(int divideCode) {
		int divideFlag = 0;
		
		if(divideCode >= 1000 && divideCode < 2000) {			// Gather Server
			divideFlag = 1;
		} else if(divideCode >= 2000 && divideCode < 3000) {	// DMA Agent
			divideFlag = 2;
		} else if(divideCode >= 3000 && divideCode < 4000) {	// Gather Agent
			divideFlag = 3;
		}
		
		return divideFlag;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> rtrvAgentStatusV2() throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
		int i_masterport = Integer.parseInt(masterport);
		
		data.put("agent", "DB");
		data.put("status", getStatusImg(getEcho(masterip, i_masterport)));
		resultList.add(data);
		
		data = new LinkedHashMap<String, Object>();
		data.put("agent", "Master");
		data.put("status", getStatusImg(getEcho(masterip, i_masterport)));
		resultList.add(data);
		
		if(schedulerip != null) {
			data = new LinkedHashMap<String, Object>();
			data.put("agent", "Scheduler");
			data.put("status", getStatusImg(getPing(schedulerip, Integer.parseInt(schedulerport))));
			resultList.add(data);
		}
		
		data = new LinkedHashMap<String, Object>();
		data.put("agent", "Gather");
		data.put("status", getStatusImg(getGatherServer(gather_server_ip, Integer.parseInt(gather_server_port))));
		resultList.add(data);
		
		data = new LinkedHashMap<String, Object>();
		data.put("BASE_DAY", DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		resultList.add(data);
		
		return resultList;
	}
	
	private String getStatusImg(String status) {
		if(status.equalsIgnoreCase("true")) {		// 정상
			return "<img src='/resources/images/examine_scheduler/normal_status.png' style='vertical-align:bottom;'/>";	
		} else if(status.equalsIgnoreCase("false")) {	// 오류
			return "<img src='/resources/images/examine_scheduler/error_status.png' style='vertical-align:bottom;'/>";
		} else {
			return status;
		}
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> instanceListV2() throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> resultData = null;
		List<Instance> instanceList = examineOpenPOPDao.instanceList();
		Instance instance = null;
		String dbName = "";
		String dbId= "";
		String instId= "";
		String instanceStatus = "";
		String dmaStatus = "";
		String gatherStatus = "";
		
		for(int instanceIndex = 0; instanceIndex < instanceList.size(); instanceIndex++) {
			resultData = new LinkedHashMap<String, Object>();
			
			instance = instanceList.get(instanceIndex);
			dbName = instance.getDb_name();
			dbId = instance.getDbid();
			instId = instance.getInst_id();
			
			logger.debug("///// Go to Status [" + dbName + "]////////////////////////////");
			
			instanceStatus = getFlowPing(Long.parseLong(dbId));
			dmaStatus = instanceStatus(dbId, 2);
			gatherStatus = instanceStatus(dbId, 3);
			
			logger.debug("instanceStatus[" + instanceStatus + "] dmaStatus[" + dmaStatus + "] gatherStatus[" + gatherStatus + "]");
			logger.debug("///// End of Status ////////////////////////////");
			
			resultData.put("db_name", dbName);
			resultData.put("dbid", dbId);
			resultData.put("instance", instId);
			resultData.put("instance_status", instanceStatus);
			resultData.put("dma_status", dmaStatus);
			resultData.put("gather_status", gatherStatus);
			
			resultList.add(resultData);
		}
		
		return resultList;
	}
	
	private String instanceStatus(String dbId, int statusFlag) {
		String status = "false";
		ArrayList dataList;
		
		try {
			for(int loopIndex = 0; loopIndex < instanceArrayV2.size(); loopIndex++) {
				dataList = instanceArrayV2.get(loopIndex);
				
				if(dbId.equalsIgnoreCase(dataList.get(0) + "") && divideUnit(Integer.parseInt(dataList.get(2) + "")) == statusFlag) {
					status = dataList.get(3) + "";
					break;
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		logger.debug("Status dbid[" + dbId + "] statusFlag[" + statusFlag + "] status[" + status + "]");
		
		return status;
	}
}
