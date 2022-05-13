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

import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.PartitionTableManagement;
import omc.spop.model.Result;
import omc.spop.service.PartitionManagementService;

/***********************************************************
 * 2018.09.07 	임호경	 최초작성
 *******************************/

@Controller
public class PartitionManagementController extends InterfaceController {

	
	private static final Logger logger = LoggerFactory.getLogger(PartitionManagementController.class);

	@Autowired
	private PartitionManagementService partitionManagementService;
	
	@RequestMapping(value = "/PartitionMng/PartitionTable", method = RequestMethod.GET)
	public String PartitionTable(@ModelAttribute("partitionTableManagement") PartitionTableManagement partitionTableManagement, Model model) {

		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		model.addAttribute("user_id", user_id);
		model.addAttribute("menu_id", partitionTableManagement.getMenu_id() );
		model.addAttribute("menu_nm", partitionTableManagement.getMenu_nm() );

		return "partitionMng/partitionTableManagement";
	}

	
	
	/*검색시 파티션 조회*/
	@RequestMapping(value = "/getPartitionList", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getPartitionList(@ModelAttribute("partitionTableManagement") PartitionTableManagement partitionTableManagement, HttpServletRequest res,
			Model model) {
		List<PartitionTableManagement> resultList = new ArrayList<PartitionTableManagement>();

		try {
			resultList = partitionManagementService.getPartitionList(partitionTableManagement);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();
	}
	
	/*셀렉트시 selectTableName 조회*/        
	@RequestMapping(value = "/selectTableName", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectTableName(@ModelAttribute("partitionTableManagement") PartitionTableManagement partitionTableManagement, HttpServletRequest res, Model model) {
		
		List<PartitionTableManagement> tableNameList = new ArrayList<PartitionTableManagement>();
		try {
			tableNameList = partitionManagementService.selectTableName(partitionTableManagement);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
	
	/*셀렉트시 콤보박스 각각 조회*/        
//	@RequestMapping(value = "/selectPartitionWorkType", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@RequestMapping(value = "/selectType", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String selectType(@ModelAttribute("partitionTableManagement") PartitionTableManagement partitionTableManagement, HttpServletRequest res, Model model) {
		
		logger.debug("PartitionTableManagement : " + partitionTableManagement);
		List<PartitionTableManagement> resultList = new ArrayList<PartitionTableManagement>();
		try {
			resultList = partitionManagementService.selectType(partitionTableManagement);
			logger.debug("resultList : " + resultList);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(resultList).toJSONObject().get("rows").toString();
	}
	
	
	
	
	/*파티션테이블 삭제*/
	@RequestMapping(value = "/deletePartitionTable", method = RequestMethod.POST)
	@ResponseBody
	public Result deletePartitionTable(@ModelAttribute("partitionTableManagement") PartitionTableManagement partitionTableManagement, HttpServletRequest res, Model model)
			throws Exception {

		Result result = new Result(); String resultMsg = null; boolean resultCheck = false; int resultCount = 0;

		
		
		try {
			resultCount = partitionManagementService.deletePartitionTable(partitionTableManagement);
			resultCheck = (resultCount > 0) ? true : false;
			resultMsg = "[ "+ partitionTableManagement.getTable_name() + " ]"+" 이(가) 성공적으로 삭제되었습니다.";
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			
			resultCheck = false;
			resultCount = 0;
			result.setMessage("데이터베이스 오류입니다" + ex.getMessage());
		}
		
		
		result.setMessage(resultMsg); result.setResultCount(resultCount); result.setResult(resultCheck);

		return result;
	}
	
	/*파티션 저장 및 수정*/
	@RequestMapping(value = "/savePartitionTable", method = RequestMethod.POST)
	@ResponseBody
	public Result savePartitonTable(@ModelAttribute("partitionTableManagement") PartitionTableManagement partitionTableManagement, HttpServletRequest res, Model model)
			throws Exception {

		String crud_flag = StringUtils.defaultString(partitionTableManagement.getCrud_flag());
		Result result = new Result(); String resultMsg = null;	boolean resultCheck = false; int resultCount = 0;

		
		try {
			
				if(crud_flag.equals("U")){
					
					resultCount = partitionManagementService.updatePartitionTable(partitionTableManagement);
					resultCheck = (resultCount > 0) ? true : false;
					resultMsg = "수정 되었습니다.";
						
						
				}else{
					
					int check = 0; 
					check = partitionManagementService.checkTableName(partitionTableManagement);  
					boolean checkPk = (check > 0) ? true : false; //중복여부 
					
					logger.debug("partitonVO: " + partitionTableManagement);
					
					if(checkPk){  
						result.setMessage("[ "+partitionTableManagement.getTable_name()+" ] 은(는) 이미 추가되어있습니다.");
						result.setResult(false);
						return result;
						
					}else{
						resultCount = partitionManagementService.insertPartitionTable(partitionTableManagement);
						resultCheck = (resultCount > 0) ? true : false;
						resultMsg = "저장 되었습니다.";
					}
					
				}
			
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			resultCheck = false;
			resultCount = 0;
			result.setMessage("데이터베이스 오류입니다");
		}
		result.setMessage(resultMsg);
		result.setResultCount(resultCount);
		result.setResult(resultCheck);

		return result;
	}
	
	
}
