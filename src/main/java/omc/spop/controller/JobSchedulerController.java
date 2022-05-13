package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONSerializer;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.JobSchedulerBase;
import omc.spop.model.Result;
import omc.spop.service.JobSchedulerService;

/***********************************************************
 * 2018.09.11	 임호경	 최초작성
 **********************************************************/

@Controller
public class JobSchedulerController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(JobSchedulerController.class);

	@Autowired
	private JobSchedulerService jobSchedulerService;

	/*JobSchedulerBase*/
	@RequestMapping(value = "/JobScheduler/JobSchedulerBase", method = RequestMethod.GET)
	public String JobSchedulerBase(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", jobSchedulerBase.getMenu_id() );
		model.addAttribute("menu_nm", jobSchedulerBase.getMenu_nm() );
		
		return "jobScheduler/jobSchedulerBase";
	}

	/* getJobSchedulerList -리스트 */
	@RequestMapping(value = "/getJobSchedulerList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerList(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();

		try {
			resultList = jobSchedulerService.getJobSchedulerList(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* getJobSchedulerTypeCd -셀렉트 */
	@RequestMapping(value = "/getJobSchedulerTypeCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerTypeCd(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();

		try {
			resultList = jobSchedulerService.getJobSchedulerTypeCd(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}

	/* getJobSchedulerTypeCd2 -셀렉트 */
	@RequestMapping(value = "/getJobSchedulerTypeCd2", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerTypeCd2(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();
		
		try {
			resultList = jobSchedulerService.getJobSchedulerTypeCd2(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}

	/* getJobSchedulerExecTypeCd -셀렉트 */
	@RequestMapping(value = "/getJobSchedulerExecTypeCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerExecTypeCd(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();

		try {
			resultList = jobSchedulerService.getJobSchedulerExecTypeCd(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}
	/* selectWrkTargetId -셀렉트 */
	@RequestMapping(value = "/selectWrkTargetId", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectWrkTargetId(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();
		
		logger.debug("셀렉트값확인 : " + jobSchedulerBase.getSelect());
		try {
			resultList = jobSchedulerService.selectWrkTargetId(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		return success(resultList).toJSONObject().get("rows").toString();
	}

	/* saveJobSchedulerBase 저장 */
	@RequestMapping(value = "/saveJobSchedulerBase", method = RequestMethod.POST)
	@ResponseBody
	public Result saveJobSchedulerBase(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {

		Result result = new Result();
		logger.debug("!!jobSchedulerBase : " + jobSchedulerBase);
		try {
			jobSchedulerService.saveJobSchedulerBase(jobSchedulerBase);
			result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* deleteJobSchedulerBase 삭제 - deleteJobSchedulerBase */
	@RequestMapping(value = "/deleteJobSchedulerBase", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteJobSchedulerBase(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		Result result = new Result();

		try {
//			logger.debug("!!jobSchedulerBase : " + jobSchedulerBase);

			jobSchedulerService.deleteJobSchedulerBase(jobSchedulerBase);
			result.setResult(true);

			result.setMessage("성공적으로 [ " + jobSchedulerBase.getJob_scheduler_type_cd_nm() + " ] 이(가) 삭제되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	
	
	
	
	
	/*JobSchedulerDetail*/
	@RequestMapping(value = "/JobScheduler/JobSchedulerDetail", method = RequestMethod.GET)
	public String JobSchedulerDetail(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", jobSchedulerBase.getMenu_id() );
		model.addAttribute("menu_nm", jobSchedulerBase.getMenu_nm() );

		return "jobScheduler/jobSchedulerDetail";
	}
	
	
	/* getJobShcedulerConfigDetailList -리스트 */
	@RequestMapping(value = "/getJobSchedulerDetailList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerDetailList(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();

		try {
			resultList = jobSchedulerService.getJobSchedulerDetailList(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	/* selectJobShcedulerConfigDetailList -셀렉트 */
	@RequestMapping(value = "/selectJobSchedulerTypeCd", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectJobSchedulerTypeCd(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
//		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();
		JobSchedulerBase resultObj = new JobSchedulerBase(); 
		try {
//			resultList = jobSchedulerService.selectJobSchedulerTypeCd(jobSchedulerBase);
			resultObj = jobSchedulerService.selectJobSchedulerTypeCd(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		
		
//		JSONResult result = success(resultObj);
//		JSONObject object = result.toJSONObject();
		
		return success(resultObj).toJSONObject().toString();
	}


	/* saveJobSchedulerDetail 저장 */
	@RequestMapping(value = "/saveJobSchedulerDetail", method = RequestMethod.POST)
	@ResponseBody
	public Result saveJobSchedulerDetail(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {

		Result result = new Result();
		logger.debug("!!jobSchedulerBase : " + jobSchedulerBase);

		try {
			jobSchedulerBase.setJob_scheduler_wrk_target_id(jobSchedulerBase.getJob_scheduler_wrk_target_id().replaceAll(",", ""));
			jobSchedulerService.saveJobSchedulerDetail(jobSchedulerBase);
			result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}

	/* deleteJobSchedulerDetail 삭제 - deleteJobSchedulerDetail */
	@RequestMapping(value = "/deleteJobSchedulerDetail", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteJobSchedulerDetail(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		Result result = new Result();

		try {
			logger.debug("!!jobSchedulerBase : " + jobSchedulerBase);
			jobSchedulerBase.setJob_scheduler_wrk_target_id(jobSchedulerBase.getJob_scheduler_wrk_target_id().replaceAll(",", ""));

			jobSchedulerService.deleteJobSchedulerDetail(jobSchedulerBase);
			result.setResult(true);

			result.setMessage("성공적으로 [ " + jobSchedulerBase.getJob_scheduler_type_cd_nm() + " ] 이(가) 삭제되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	
	/*JobSchedulerDetailTypeCd*/
	@RequestMapping(value = "/JobScheduler/JobSchedulerDetailTypeCd", method = RequestMethod.GET)
	public String JobSchedulerDetailTypeCd(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", jobSchedulerBase.getMenu_id() );
		model.addAttribute("menu_nm", jobSchedulerBase.getMenu_nm() );

		return "jobScheduler/jobSchedulerDetailTypeCd";
	}
	
	
	/* getJobSchedulerdetailTypeCd -리스트 */
	@RequestMapping(value = "/getJobSchedulerdetailTypeCdList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerDetailTypeCd(@ModelAttribute("jobSchedulerDetailTypeCd") JobSchedulerBase jobSchedulerBase,
			Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();

		try {
			resultList = jobSchedulerService.getJobSchedulerdetailTypeCdList(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		logger.debug("★★★check 확인★★★ : " +  success(resultList).toJSONObject().toString());
		
		return success(resultList).toJSONObject().toString();
	}
	
	
	
	
	/* deleteJobSchedulerDetailTypeCd 삭제 - deleteJobSchedulerDetail */
	@RequestMapping(value = "/deleteJobSchedulerDetailTypeCd", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteJobSchedulerDetailTypeCd(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		Result result = new Result();

		try {

			jobSchedulerService.deleteJobSchedulerDetailTypeCd(jobSchedulerBase);
			result.setResult(true);

			result.setMessage("성공적으로 [ " + jobSchedulerBase.getJob_scheduler_detail_type_nm() + " ] 이(가) 삭제되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	
	/* saveJobSchedulerDetailTypeCd 저장 */
	@RequestMapping(value = "/saveJobSchedulerDetailTypeCd", method = RequestMethod.POST)
	@ResponseBody
	public Result saveJobSchedulerDetailTypeCd(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {

		Result result = new Result();
		logger.debug("!!jobSchedulerBase : " + jobSchedulerBase);

		try {
			
			if(jobSchedulerBase.getCrud_flag().equals("U")){
				jobSchedulerService.saveJobSchedulerDetailTypeCd(jobSchedulerBase);
			}else{
				int count = jobSchedulerService.checkPkForDetail(jobSchedulerBase);
				boolean check = (count == 0) ? true: false;
				
				if(check){
					jobSchedulerService.saveJobSchedulerDetailTypeCd(jobSchedulerBase);
				}else{
					result.setMessage("[ 작업스케쥴러 유형/상세유형 ]이 중복되었습니다. 다시 확인해 주세요" );
					result.setResult(false);
					return result;
				}
			}
			result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	
	
	
	
	
	/*JobSchedulerDependency*/
	@RequestMapping(value = "/JobScheduler/JobSchedulerDependency", method = RequestMethod.GET)
	public String JobSchedulerDependency(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", jobSchedulerBase.getMenu_id() );
		model.addAttribute("menu_nm", jobSchedulerBase.getMenu_nm() );

		return "jobScheduler/jobSchedulerDependency";
	}
	/* getJobSchedulerDependency -리스트 */
	@RequestMapping(value = "/getJobSchedulerDependency", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerDependency(@ModelAttribute("jobSchedulerDetailTypeCd") JobSchedulerBase jobSchedulerBase,
			Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();

		try {
			resultList = jobSchedulerService.getJobSchedulerDependency(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	/* deleteJobSchedulerDependency 삭제 - deleteJobSchedulerDetail */
	@RequestMapping(value = "/deleteJobSchedulerDependency", method = RequestMethod.POST)
	@ResponseBody
	public Result deleteJobSchedulerDependency(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {
		Result result = new Result();

		try {

			jobSchedulerService.deleteJobSchedulerDependency(jobSchedulerBase);
			result.setResult(true);

			result.setMessage("성공적으로 [ " + jobSchedulerBase.getDpnd_job_sched_detail_type_nm() + " ] 이(가) 삭제되었습니다.");
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	/* updateJobSchedulerDependency 저장 */
	@RequestMapping(value = "/saveJobSchedulerDependency", method = RequestMethod.POST)
	@ResponseBody
	public Result updateJobSchedulerDependency(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase,
			Model model) {

		Result result = new Result();
		logger.debug("!!jobSchedulerBase : " + jobSchedulerBase);
		int count = 0;
		boolean check = false;

		try {
			count = jobSchedulerService.checkPkForDependency(jobSchedulerBase);
			check = (count == 0) ? true: false;
			
			if(check){

				if(jobSchedulerBase.getCrud_flag().equals("C")){
					count = jobSchedulerService.insertJobSchedulerDependency(jobSchedulerBase);
					result.setResult(true); 
				}else{
					count = jobSchedulerService.updateJobSchedulerDependency(jobSchedulerBase);
					result.setResult(true);
				}
			
			}else{
				result.setMessage("[ 종속작업 스케쥴러 유형/상세유형 ]이 중복되었습니다. 다시 확인해 주세요" );
				result.setResult(false);
				return result;
			}
			
//			result.setMessage("[ 종속작업스케쥴러유형/상세유형 ] 이 추가/수정되었습니다.");
			result.setResult(true);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;
	}
	
	
	
	/* getDpndJobSchedTypeCd -셀렉트 */
	@RequestMapping(value = "/getDpndJobSchedTypeCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getDpndJobSchedTypeCd(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();

		System.out.println("★★★★★★★★★★★job_scheduler_type_cd : " + jobSchedulerBase.getJob_scheduler_type_cd());
		try {
			resultList = jobSchedulerService.getDpndJobSchedTypeCd(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	
	
	
	
	/*JobSchedulerDetailTypeCd_extjs*/
	@RequestMapping(value = "/JobScheduler/JobSchedulerDetailTypeCd_extjs", method = RequestMethod.GET)
	public String JobSchedulerDetailTypeCd_extjs(@ModelAttribute("jobSchedulerBase") JobSchedulerBase jobSchedulerBase, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);

		return "jobScheduler/jobSchedulerDetailTypeCd_extjs";
	}
	
	

	
	/* getJobSchedulerdetailTypeCd_extjs -리스트 */
	@RequestMapping(value = "/getJobSchedulerdetailTypeCdList_extjs", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getJobSchedulerDetailTypeCd_extjs(@ModelAttribute("jobSchedulerDetailTypeCd") JobSchedulerBase jobSchedulerBase,
			Model model) {
		List<JobSchedulerBase> resultList = new ArrayList<JobSchedulerBase>();
		System.out.println("여기 들어옴 확인 ★★★★★");
		try {
			resultList = jobSchedulerService.getJobSchedulerdetailTypeCdList(jobSchedulerBase);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		JSONObject jso = new JSONObject();
		jso.put("row", (JSONSerializer.toJSON(resultList)));
		jso.put("totalCount", resultList.size());

		logger.debug("★★★check 확인★★★ : " +  success(resultList).toJSONObject().toString());
		logger.debug("★★★check2 확인★★★ : " + (JSONSerializer.toJSON(resultList)).toString());
		logger.debug("★★★check3 확인★★★ : " + jso.toString());
		
//		return jso.toString();
		return (JSONSerializer.toJSON(resultList)).toString();
//		return success(resultList).toJSONObject().toString();
	}
	
	
}
