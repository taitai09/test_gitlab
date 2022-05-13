package omc.spop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import omc.spop.model.BoardManagement;
import omc.spop.model.Result;
import omc.spop.service.BoardSettingService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업 (오픈메이드 관리자용)
 **********************************************************/

@Controller
@RequestMapping(value = "/Config")//게시판 관리 기본
public class BoardSettingController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardSettingController.class);
	
	@Autowired
	private BoardSettingService boardSettingService;

	/* 환경설정 - 게시판 설정관리 */
	@RequestMapping(value="/BoardSetting", method=RequestMethod.GET)
	public String MngSetting(@ModelAttribute("boardManagement") BoardManagement boardManagement,Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		
		model.addAttribute("nowDate", nowDate );
		model.addAttribute("menu_id", boardManagement.getMenu_id() );
		model.addAttribute("menu_nm", boardManagement.getMenu_nm() );
			
		return "config/boardMng/boardSetting";
	}

	/* 시스템관리 - 게시판관리 - 설정관리 action */
	@RequestMapping(value="/BoardSetting", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String MngSettingAction(@ModelAttribute("boardManagement") BoardManagement boardManagement,  Model model) {
		List<BoardManagement> resultList = new ArrayList<BoardManagement>();

		try{
			resultList = boardSettingService.boardSettingList(boardManagement);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

		return success(resultList).toJSONObject().toString();	
	}
	
	/* 시스템관리 - 게시판관리 - 설정관리 Save */
	@RequestMapping(value="/BoardSetting/Save", method=RequestMethod.POST)
	@ResponseBody
	public Result BoardSettingSaveAction(@ModelAttribute("boardManagement") BoardManagement boardManagement,  Model model) {
		Result result = new Result();
		
		try{
			boardSettingService.saveBoardSetting(boardManagement);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	/* 시스템관리 - 게시판관리 - 설정관리 Delete */
	@RequestMapping(value="/BoardSetting/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result BoardSettingDeleteAction(@ModelAttribute("boardManagement") BoardManagement boardManagement,  Model model) {
		Result result = new Result();
		
		try{
			boardSettingService.deleteBoardSetting(boardManagement);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;	
	}
	
	
	/* 시스템관리 - Select board_type_cd */	
	@RequestMapping(value = "/getBoardTypeCd", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getBoardTypeCd(@ModelAttribute("boardManagement") BoardManagement boardManagement, HttpServletRequest res, Model model) {
		
		List<BoardManagement> tableNameList = new ArrayList<BoardManagement>();
		try {
			tableNameList = boardSettingService.getBoardTypeCd(boardManagement);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingClass().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}
		return success(tableNameList).toJSONObject().get("rows").toString();
	}
	
}