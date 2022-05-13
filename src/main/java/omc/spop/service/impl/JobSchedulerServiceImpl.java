package omc.spop.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.JobSchedulerDao;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.JobSchedulerConfigDetail;
import omc.spop.service.JobSchedulerService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.09.11 임호경 최초작성
 **********************************************************/

@Service("jobSchedulerService")
public class JobSchedulerServiceImpl implements JobSchedulerService {

	private static final Logger logger = LoggerFactory.getLogger(JobSchedulerServiceImpl.class);
	private static final String INST_EXEC_MIN = "2";

	@Autowired
	private JobSchedulerDao jobSchedulerDao;

	@Override
	public List<JobSchedulerBase> getJobSchedulerList(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.getJobSchedulerList(jobSchedulerBase);
	}

	@Override
	public List<JobSchedulerBase> getJobSchedulerTypeCd(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.getJobSchedulerTypeCd(jobSchedulerBase);
	}

	@Override
	public List<JobSchedulerBase> getJobSchedulerExecTypeCd(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.getJobSchedulerExecTypeCd(jobSchedulerBase);
	}

	@Override
	public int deleteJobSchedulerBase(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.deleteJobSchedulerBase(jobSchedulerBase);
	}

	@Override
	public int saveJobSchedulerBase(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.saveJobSchedulerBase(jobSchedulerBase);
	}

	@Override
	public List<JobSchedulerBase> getJobSchedulerDetailList(JobSchedulerBase jobSchedulerBase) {
		List<JobSchedulerBase> list = jobSchedulerDao.getJobSchedulerDetailList(jobSchedulerBase);

		logger.debug("★list★:" + list);

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getExec_start_dt() != null) {

				String start_dt = StringUtils.defaultString(list.get(i).getExec_start_dt());
				if (start_dt != null && !start_dt.equals("")) {
					int index = list.get(i).getExec_start_dt().indexOf(" ") + 1;
//					String start_time = list.get(i).getExec_start_dt().substring(index,list.get(i).getExec_start_dt().lastIndexOf(":"));
					String start_time = list.get(i).getExec_start_dt().substring(index,list.get(i).getExec_start_dt().length());
//					String end_time = list.get(i).getExec_end_dt().substring(index,list.get(i).getExec_end_dt().lastIndexOf(":"));
					String end_time = list.get(i).getExec_end_dt().substring(index,list.get(i).getExec_end_dt().length());
//					System.out.println("★★★★★체크확인 : " + start_time + " / " + end_time + "★★★★★");
					
					list.get(i).setExec_start_time(start_time);
					list.get(i).setExec_end_time(end_time);
				}
			}
		}

		return list;
	}

	@Override
	public int saveJobSchedulerDetail(JobSchedulerBase jobSchedulerBase) {
		String nowTime = StringUtil.replace(DateUtil.getNextTime("P", INST_EXEC_MIN), ":", "");
		JobSchedulerConfigDetail temp = new JobSchedulerConfigDetail();

		if (jobSchedulerBase.getExec_start_time().equals("") || jobSchedulerBase.getExec_start_time() == null) {
			jobSchedulerBase.setExec_start_time(nowTime);
		} else if (jobSchedulerBase.getExec_end_time().equals("") || jobSchedulerBase.getExec_end_time() == null) {
			jobSchedulerBase.setExec_end_time(nowTime);
		} else if (jobSchedulerBase.getExec_start_time().equals("")
				|| jobSchedulerBase.getExec_start_time() == null && jobSchedulerBase.getExec_end_time().equals("")
				|| jobSchedulerBase.getExec_end_time() == null) {
			jobSchedulerBase.setExec_start_time(nowTime);
			jobSchedulerBase.setExec_end_time(nowTime);
		} else {// 값을 입력한 때
			jobSchedulerBase.setExec_start_time(StringUtil.replace(jobSchedulerBase.getExec_start_time(), ":", ""));
			jobSchedulerBase.setExec_end_time(StringUtil.replace(jobSchedulerBase.getExec_end_time(), ":", "")); // + 상황에 따라 '00' 을더해주어야함
		}

		jobSchedulerBase.setExec_start_dt(StringUtil.replace(jobSchedulerBase.getExec_start_dt(), "-", ""));
		jobSchedulerBase.setExec_end_dt(StringUtil.replace(jobSchedulerBase.getExec_end_dt(), "-", ""));
		jobSchedulerBase.setExec_start_dt(jobSchedulerBase.getExec_start_dt()); // temp
		jobSchedulerBase.setExec_end_dt(jobSchedulerBase.getExec_end_dt());
		// 오라클 업데이트시 리터럴 형태에 맞추어서 업데이트 해야 하기때문에 시간을 넣어서 파람전달

		jobSchedulerBase.setExec_start_dt(StringUtil.getDateFormatString(jobSchedulerBase.getExec_start_dt(),jobSchedulerBase.getExec_start_time()));
		jobSchedulerBase.setExec_end_dt(StringUtil.getDateFormatString(jobSchedulerBase.getExec_end_dt(), jobSchedulerBase.getExec_end_time()));

		System.out.println("값 확인 !!!! : " + jobSchedulerBase.getExec_start_dt());
		System.out.println("값 확인 !!!!! : " + jobSchedulerBase.getExec_end_dt());

		return jobSchedulerDao.saveJobSchedulerDetail(jobSchedulerBase);
	}

	@Override
	public int deleteJobSchedulerDetail(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.deleteJobSchedulerDetail(jobSchedulerBase);
	}
	@Override
	public JobSchedulerBase selectJobSchedulerTypeCd(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.selectJobSchedulerTypeCd(jobSchedulerBase);
	}

	@Override
	public List<JobSchedulerBase> getJobSchedulerTypeCd2(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.getJobSchedulerTypeCd2(jobSchedulerBase);
	}

	@Override
	public List<JobSchedulerBase> selectWrkTargetId(JobSchedulerBase jobSchedulerBase) {

		if(jobSchedulerBase.getSelect().equals("DB")){
			return jobSchedulerDao.chooseDB(jobSchedulerBase);
		}else if(jobSchedulerBase.getSelect().equals("Work")){
			return jobSchedulerDao.chooseWork(jobSchedulerBase);
		}else if(jobSchedulerBase.getSelect().equals("UserAsk")){
			return jobSchedulerDao.chooseUserAsk(jobSchedulerBase);
		}else{
			return null;
		}
		
	}

	@Override
	public List<JobSchedulerBase> getJobSchedulerdetailTypeCdList(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.getJobSchedulerdetailTypeCdList(jobSchedulerBase);
	}

	@Override
	public int deleteJobSchedulerDetailTypeCd(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.deleteJobSchedulerDetailTypeCd(jobSchedulerBase);
	}

	@Override
	public int saveJobSchedulerDetailTypeCd(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.saveJobSchedulerDetailTypeCd(jobSchedulerBase);
	}

	@Override
	public int checkPkForDetail(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.checkPkForDetail(jobSchedulerBase);
	}

	@Override
	public List<JobSchedulerBase> getJobSchedulerDependency(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.getJobSchedulerDependency(jobSchedulerBase);
	}

	@Override
	public int deleteJobSchedulerDependency(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.deleteJobSchedulerDependency(jobSchedulerBase);
	}

	@Override
	public int updateJobSchedulerDependency(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.updateJobSchedulerDependency(jobSchedulerBase);
	}

	@Override
	public int checkPkForDependency(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.checkPkForDependency(jobSchedulerBase);
	}

	@Override
	public int insertJobSchedulerDependency(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.insertJobSchedulerDependency(jobSchedulerBase);
	}

	@Override
	public List<JobSchedulerBase> getDpndJobSchedTypeCd(JobSchedulerBase jobSchedulerBase) {
		return jobSchedulerDao.getDpndJobSchedTypeCd(jobSchedulerBase);
	}

}
