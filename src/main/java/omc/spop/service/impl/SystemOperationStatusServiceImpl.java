package omc.spop.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.SystemOperationStatusDao;
import omc.spop.model.JobSchedulerExecDetailLog;
import omc.spop.model.JobSchedulerExecLog;
import omc.spop.service.SystemOperationStatusService;

/***********************************************************
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("SystemOperationStatusService")
public class SystemOperationStatusServiceImpl implements SystemOperationStatusService {
	@Autowired
	private SystemOperationStatusDao systemOperationStatusDao;

	@Override
	public List<JobSchedulerExecLog> performSchedulerList(JobSchedulerExecLog jobSchedulerExecLog) throws Exception {
		return systemOperationStatusDao.performSchedulerList(jobSchedulerExecLog);
	}
	
	@Override
	public List<JobSchedulerExecDetailLog> performSchedulerDetailList(JobSchedulerExecDetailLog jobSchedulerExecDetailLog) throws Exception {
		return systemOperationStatusDao.performSchedulerDetailList(jobSchedulerExecDetailLog);
	}

	@Override
	public List<LinkedHashMap<String, Object>> performSchedulerListByExcelDown(
			JobSchedulerExecDetailLog jobSchedulerExecDetailLog) {
		return systemOperationStatusDao.performSchedulerListByExcelDown(jobSchedulerExecDetailLog);
	}	
}