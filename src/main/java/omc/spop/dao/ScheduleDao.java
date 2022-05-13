package omc.spop.dao;

import java.util.List;

import omc.spop.model.Schedule;

/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface ScheduleDao {	
	public List<Schedule> scheduleList(Schedule schedule);

	public Schedule getSchedule(Schedule schedule);
	
	public String getMaxSchedNo(Schedule schedule);
	
	public int insertSchedule(Schedule schedule);
	
	public int updateSchedule(Schedule schedule);
	
	public int deleteSchedule(Schedule schedule);
}