package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import omc.spop.base.InterfaceController;
import omc.spop.model.Result;
import omc.spop.model.Schedule;
import omc.spop.service.ScheduleService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
@RequestMapping(value = "/Schedule")
public class ScheduleController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);
	
	@Autowired
	private ScheduleService scheduleService;

	/* 일정 관리 - 리스트 */
	@RequestMapping(value="/List")
	public String List(@ModelAttribute("schedule") Schedule schedule, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "7");

		if(schedule.getStrStartDt() == null || schedule.getStrStartDt().equals("")){
			schedule.setStrStartDt(startDate);
		}
		
		if(schedule.getStrEndDt() == null || schedule.getStrEndDt().equals("")){
			schedule.setStrEndDt(nowDate);
		}			

		return "schedule/list";
	}
	
	/* 일정 관리 - 리스트 Action */
	@RequestMapping(value="/ListAction", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String ListAction(@ModelAttribute("schedule") Schedule schedule, Model model) {
		List<Schedule> resultList = new ArrayList<Schedule>();

		try{
			resultList = scheduleService.scheduleList(schedule);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 일정 관리 - 등록 */
	@RequestMapping(value="/Insert", method=RequestMethod.POST)
	public String Insert(@ModelAttribute("schedule") Schedule schedule, Model model) {
		String startDate = DateUtil.getNowDate("yyyy-MM-dd");
		String endDate = DateUtil.getNextDay("P", "yyyy-MM-dd", startDate, "7");
		String nowTime = DateUtil.getNowFulltime();
		
		model.addAttribute("startDate", startDate );
		model.addAttribute("endDate", endDate );
		model.addAttribute("nowTime", nowTime );
		model.addAttribute("menu_id", schedule.getMenu_id());
		model.addAttribute("menu_nm", schedule.getMenu_nm());
		
		return "schedule/insert";
	}	
	
	/* 일정 관리 - 등록 Action */
	@RequestMapping(value="/InsertAction", method=RequestMethod.POST)
	@ResponseBody
	public Result InsertAction(@ModelAttribute("schedule") Schedule schedule, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		
		try {				
			rowCnt = scheduleService.insertSchedule(schedule);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("일정 등록에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		

		return result;		
	}	
	
	/* 일정 관리 - 보기 */
	@RequestMapping(value="/View", method=RequestMethod.POST)
	public String View(@ModelAttribute("schedule") Schedule schedule, Model model) {
		Schedule result = null;
		try{
			result = scheduleService.getSchedule(schedule);
			model.addAttribute("result", result);
			model.addAttribute("menu_id", schedule.getMenu_id());
			model.addAttribute("menu_nm", schedule.getMenu_nm());
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return "schedule/view";
	}
	
	/* 일정 관리 - 수정 */
	@RequestMapping(value="/Update", method=RequestMethod.POST)
	public String Update(@ModelAttribute("schedule") Schedule schedule, Model model) {
		Schedule result = null;
		try{
			result = scheduleService.getSchedule(schedule);
			model.addAttribute("result", result);
			model.addAttribute("menu_id", schedule.getMenu_id());
			model.addAttribute("menu_nm", schedule.getMenu_nm());
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}

		return "schedule/update";
	}
	
	/* 일정 관리 - 수정 Action */
	@RequestMapping(value="/UpdateAction", method=RequestMethod.POST)
	@ResponseBody
	public Result UpdateAction(@ModelAttribute("schedule") Schedule schedule, Model model) {
		Result result = new Result();
		int rowCnt = 0;
		
		try {				
			rowCnt = scheduleService.updateSchedule(schedule);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("일정 수정에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}	
		
		return result;		
	}	
	
	/* 일정 관리 - 삭제 */
	@RequestMapping(value="/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result Delete(@ModelAttribute("schedule") Schedule schedule,  Model model) {
		Result result = new Result();
		int rowCnt = 0;
		
		try {				
			rowCnt = scheduleService.deleteSchedule(schedule);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("일정 삭제에 실패하였습니다.");
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}	

		return result;	
	}
}