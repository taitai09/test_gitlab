package omc.spop.service;

import java.util.List;

import omc.spop.model.Schedule;

/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface ScheduleService {
	/** 일정관리 리스트 */
	List<Schedule> scheduleList(Schedule schedule) throws Exception;

	/** 일정관리 정보 조회 */
	Schedule getSchedule(Schedule schedule) throws Exception;

	/** 일정관리 - INSERT */
	int insertSchedule(Schedule schedule) throws Exception;
	
	/** 일정관리 - UPDATE */
	int updateSchedule(Schedule schedule) throws Exception;
	
	/** 일정관리 - DELETE */
	int deleteSchedule(Schedule schedule) throws Exception;
}
