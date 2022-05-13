package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.ScheduleDao;
import omc.spop.model.Schedule;
import omc.spop.service.ScheduleService;

/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleDao scheduleDao;

	@Override
	public List<Schedule> scheduleList(Schedule schedule) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		schedule.setUser_id(user_id);
		
		return scheduleDao.scheduleList(schedule);
	}

	@Override
	public Schedule getSchedule(Schedule schedule) throws Exception {
		return scheduleDao.getSchedule(schedule);
	}

	@Override
	public int insertSchedule(Schedule schedule) throws Exception {
		int resultCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String sched_seq = "";
		
		schedule.setUser_id(user_id);
		
		// 1. SCHEDULE MAX SCHED_SEQ 조회
		sched_seq = scheduleDao.getMaxSchedNo(schedule);
		schedule.setSched_seq(sched_seq);		
		
		// 2. SCHEDULE INSERT
		resultCnt = scheduleDao.insertSchedule(schedule);
		
		return resultCnt;
	}
	
	@Override
	public int updateSchedule(Schedule schedule) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		schedule.setUser_id(user_id);
		
		return scheduleDao.updateSchedule(schedule);
	}
	
	@Override
	public int deleteSchedule(Schedule schedule) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		schedule.setUser_id(user_id);

		return scheduleDao.deleteSchedule(schedule);
	}
}