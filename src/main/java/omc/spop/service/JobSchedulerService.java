package omc.spop.service;

import java.util.List;

import omc.spop.model.JobSchedulerBase;

/***********************************************************
 * 2018.09.11 	임호경	 최초작성
 **********************************************************/

public interface JobSchedulerService {

	List<JobSchedulerBase> getJobSchedulerList(JobSchedulerBase jobSchedulerBase);

	List<JobSchedulerBase> getJobSchedulerTypeCd(JobSchedulerBase jobSchedulerBase);

	List<JobSchedulerBase> getJobSchedulerExecTypeCd(JobSchedulerBase jobSchedulerBase);

	int deleteJobSchedulerBase(JobSchedulerBase jobSchedulerBase);

	int saveJobSchedulerBase(JobSchedulerBase jobSchedulerBase);

	List<JobSchedulerBase> getJobSchedulerDetailList(omc.spop.model.JobSchedulerBase jobSchedulerBase);

	int saveJobSchedulerDetail(JobSchedulerBase jobSchedulerBase);

	int deleteJobSchedulerDetail(JobSchedulerBase jobSchedulerBase);

	JobSchedulerBase selectJobSchedulerTypeCd(JobSchedulerBase jobSchedulerBase);
	
	List<JobSchedulerBase> getJobSchedulerTypeCd2(JobSchedulerBase jobSchedulerBase);

	List<JobSchedulerBase> selectWrkTargetId(JobSchedulerBase jobSchedulerBase);

	List<JobSchedulerBase> getJobSchedulerdetailTypeCdList(JobSchedulerBase jobSchedulerBase);

	int deleteJobSchedulerDetailTypeCd(JobSchedulerBase jobSchedulerBase);

	int saveJobSchedulerDetailTypeCd(JobSchedulerBase jobSchedulerBase);

	int checkPkForDetail(JobSchedulerBase jobSchedulerBase);

	List<JobSchedulerBase> getJobSchedulerDependency(JobSchedulerBase jobSchedulerBase);

	int deleteJobSchedulerDependency(JobSchedulerBase jobSchedulerBase);

	int updateJobSchedulerDependency(JobSchedulerBase jobSchedulerBase);

	int checkPkForDependency(JobSchedulerBase jobSchedulerBase);

	int insertJobSchedulerDependency(JobSchedulerBase jobSchedulerBase);

	List<JobSchedulerBase> getDpndJobSchedTypeCd(JobSchedulerBase jobSchedulerBase);
	



	

	
}
