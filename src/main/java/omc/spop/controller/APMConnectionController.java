package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.ApmConnection;
import omc.spop.model.Result;
import omc.spop.service.APMConnectionService;

/***********************************************************
 * 2018.09.03 	임호경	 최초작성
 **********************************************************/

@Controller
public class APMConnectionController extends InterfaceController {

	private static final Logger logger = LoggerFactory.getLogger(APMConnectionController.class);

	@Autowired
	private APMConnectionService apmConnectionService;

	@RequestMapping(value = "/APMConnection", method = RequestMethod.GET)
	public String APMConnection(@ModelAttribute("apmConnection") ApmConnection apmConnection, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", apmConnection.getMenu_id() );
		model.addAttribute("menu_nm", apmConnection.getMenu_nm() );
		
		return "apmConn/apmConnection";
	}

	
	/*검색시 힌트리스트 조회*/
	@RequestMapping(value = "/getApmList", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getApmList(@ModelAttribute("apmConnection") ApmConnection apmConnection, HttpServletRequest res,
			Model model) {
		List<ApmConnection> resultList = new ArrayList<ApmConnection>();

		try {
			resultList = apmConnectionService.getApmList(apmConnection);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}

	/* APM Connection WrkjobCd 조회 */
	@RequestMapping(value = "/getOnlyWrkJobCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getOnlyWrkJobCd(@ModelAttribute("apmConnection") ApmConnection apmConnection) throws Exception {
		List<ApmConnection> apmList = apmConnectionService.getOnlyWrkJobCd(apmConnection);
		List<ApmConnection> resultList = apmConnectionService.getOnlyWrkJobCd(apmConnection);
		ApmConnection apm = new ApmConnection();
		if(apmConnection.getIsChoice().equals("Y")){
			apm.setWrkjob_cd("");
			apm.setWrkjob_cd_nm("선택");
			resultList.add(apm);
		}
		resultList.addAll(apmList);
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	/*APM 수정및 저장*/
	@RequestMapping(value = "/saveApmConnection", method = RequestMethod.POST)
	@ResponseBody
	public Result saveApmConnection(@ModelAttribute("apmConnection") ApmConnection apmConnection)
			throws Exception {

		String crud_flag = StringUtils.defaultString(apmConnection.getCrud_flag());
		Result result = new Result();
		String resultMsg = null;
		boolean resultCheck = false;
		int resultCount = 0;
		String callback_msg = apmConnection.getApm_operate_type_cd_nm(); 
		
		logger.debug("apmConnection:"+apmConnection);
		
		try {
			int checkPk = 0;
			
			if(crud_flag.equals("C")){  // insert
				checkPk = apmConnectionService.checkApmCd(apmConnection);  //check == 0 : 중복안됨
				if(checkPk == 0){
					resultCount = apmConnectionService.insertApm(apmConnection);
					resultCheck = (resultCount > 0) ? true : false;
//					resultMsg = "APM운영유형 " + "[ "+callback_msg+" ]"+ "이(가) 성공적으로 추가되었습니다.";
				}else{

					resultCount = 0;
					resultCheck = false;
					resultMsg = "[ 업무명 : "+apmConnection.getWrkjob_cd_nm()+", 운영유형 : "+ apmConnection.getApm_operate_type_cd_nm()+" ]"+"이 중복되었습니다. 다시 확인해 주세요.";
				}
				
			}else{ //update
				
				boolean goHeadUpdate = true;
				
				if(apmConnection.getUpdateIsAll().equals("N")){
				checkPk = apmConnectionService.checkApmCd(apmConnection);  //check == 0 ? go : no
				}
//				checkOthers = apmConnectionService.checkApmOthers(apmConnection); //checkOthers == 0 ? go : no
//				goHeadUpdateOthers = (checkOthers == 0) ? true : false; 
				goHeadUpdate = (checkPk == 0 )? true : false;
				
				if(goHeadUpdate){
					resultCount = apmConnectionService.updateApm(apmConnection);
					resultCheck = (resultCount > 0) ? true : false;
//					resultMsg = "APM운영유형 [ "+callback_msg+" ] 이(가) 성공적으로 수정되었습니다.";
					
				}else{
					
					result.setResult(false);
					result.setMessage("[ 업무명 : "+apmConnection.getWrkjob_cd_nm()+", 운영유형 : "+ apmConnection.getApm_operate_type_cd_nm()+" ]"+"이 중복되었습니다. 다시 확인해 주세요.");
					result.setResultCount(0);
					return result;
				}
				
			}
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			resultCheck = false;
			resultCount = 0;
			result.setMessage("데이터베이스 오류입니다 : " + methodName);
		}
		result.setMessage(resultMsg);
		result.setResultCount(resultCount);
		result.setResult(resultCheck);

		return result;
	}
	
	
	
	
	/*셀렉트시 selectApmOperateType 조회*/        
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectApmOperateType", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectApmOperateType(@ModelAttribute("apmConnection") ApmConnection apmConnection, HttpServletRequest res, Model model) {
		
		JSONObject jso1 = new JSONObject();
		JSONObject jso2 = new JSONObject();
		JSONObject jso3 = new JSONObject();
		JSONArray jsoArray = new JSONArray();
		
		jso1.put("apm_operate_type_cd", 1);
		jso1.put("apm_operate_type_cd_nm", "운영");
		jso2.put("apm_operate_type_cd", 2);
		jso2.put("apm_operate_type_cd_nm", "스테이징");
		jso3.put("apm_operate_type_cd", 3);
		jso3.put("apm_operate_type_cd_nm", "개발");
		jsoArray.add(jso1);
		jsoArray.add(jso2);
		jsoArray.add(jso3);
		
		logger.debug("jsoArray"+jsoArray);
		return jsoArray.toString();

	}	
	
	/*APM 삭제*/
	@RequestMapping(value = "/deletApmConnection", method = RequestMethod.POST)
	@ResponseBody
	public Result deletApmConnection(@ModelAttribute("apmConnection") ApmConnection apmConnection, HttpServletRequest res, Model model)
			throws Exception {

		Result result = new Result(); String resultMsg, callback_msg = null;	boolean resultCheck = false;int resultCount = 0;
		callback_msg = apmConnection.getApm_operate_type_cd_nm();

		logger.debug("ApmConnection : " + apmConnection);
		
		try {
			
			resultCount = apmConnectionService.deleteApm(apmConnection);
			resultCheck = (resultCount > 0) ? true : false;
			resultMsg = "APM운영유형 "+"[ "+ callback_msg + " ]"+" 이(가) 성공적으로 삭제되었습니다.";
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			resultCheck = false;
			resultCount = 0;
			resultMsg = "데이터베이스 오류입니다";
		}
		result.setMessage(resultMsg);
		result.setResultCount(resultCount);
		result.setResult(resultCheck);
		logger.debug("result"+result);

		return result;
	}

}
