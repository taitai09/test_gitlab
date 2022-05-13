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
import omc.spop.model.SendNote;
import omc.spop.model.Users;
import omc.spop.service.MessageService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
@RequestMapping(value = "/Message")
public class MessageController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageService messageService;
	
	/* 전체 쪽지 리스트  */
	@RequestMapping(value="/List", method=RequestMethod.GET)
	public String Message(@ModelAttribute("sendNote") SendNote sendNote, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextMonth("M", "yyyy-MM-dd", nowDate, "1");

		model.addAttribute("nowDate", nowDate );
		model.addAttribute("startDate", startDate );
		model.addAttribute("menu_id", sendNote.getMenu_id() );
		
		return "message/list";
	}	
	
	/* 전체 받은 쪽지 리스트  */
	@RequestMapping(value="/RecvList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String RecvList(@ModelAttribute("sendNote") SendNote sendNote,  Model model) {
		List<SendNote> resultList = new ArrayList<SendNote>();

		try{
			resultList = messageService.recvMessageList(sendNote);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 전체 보낸 쪽지 리스트  */
	@RequestMapping(value="/SendList", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String SendList(@ModelAttribute("sendNote") SendNote sendNote,  Model model) {
		List<SendNote> resultList = new ArrayList<SendNote>();

		try{
			resultList = messageService.sendMessageList(sendNote);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}	

	/* 쪽지 읽기  */
	@RequestMapping(value="/MessageView", method=RequestMethod.POST)
	@ResponseBody
	public Result MessageViewAction(@ModelAttribute("sendNote") SendNote sendNote,  Model model) {
		Result result = new Result();
		SendNote temp = new SendNote();
		try{
			temp = messageService.getMessage(sendNote);
			result.setResult(temp != null ? true : false);
			result.setObject(temp);
			result.setMessage("쪽지 읽기에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 쪽지 삭제  */
	@RequestMapping(value="/DeleteMessage", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteMessageAction(@ModelAttribute("sendNote") SendNote sendNote,  Model model) {
		Result result = new Result();
		int rowCnt = 0;
		try{
			rowCnt = messageService.deleteMessage(sendNote);
			result.setResult(rowCnt > 0 ? true : false);
			result.setMessage("쪽지 삭제에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 쪽지 받을 사용자 리스트  */
	@RequestMapping(value="/RecvMessageUser", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String RecvMessageUser(@ModelAttribute("users") Users users,  Model model) {
		List<Users> resultList = new ArrayList<Users>();

		try{
			resultList = messageService.recvMessageUserList(users);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 쪽지 보내기  */
	@RequestMapping(value="/SendMessage", method=RequestMethod.POST)
	@ResponseBody
	public Result SendMessageAction(@ModelAttribute("sendNote") SendNote sendNote,  Model model) {
		Result result = new Result();
		int insCnt = 0;
		try{
			insCnt = messageService.sendMessage(sendNote);
			result.setResult(insCnt > 0 ? true : false);
			result.setMessage("쪽지 보내기에 실패하였습니다.");
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
}