package omc.spop.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import omc.spop.base.Config;
import omc.spop.base.InterfaceController;
import omc.spop.base.SessionManager;
import omc.spop.model.Board;
import omc.spop.model.BoardComment;
import omc.spop.model.BoardManagement;
import omc.spop.model.DownLoadFile;
import omc.spop.model.Result;
import omc.spop.service.BoardMngService;
import omc.spop.service.CommonService;
import omc.spop.utils.DateUtil;

/***********************************************************
 * 2017.11.13	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Controller
public class BoardMngController extends InterfaceController {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardMngController.class);
	
	@Autowired
	private BoardMngService boardMngService;

	@Autowired
	private CommonService commonService;

	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	

	/* 게시판관리 - 게시물 관리 */
	@RequestMapping(value="/BoardMng/List")
	public String List(@ModelAttribute("board") Board board, Model model) {
		List<Board> resultList = new ArrayList<Board>();
		BoardManagement boardManagement = new BoardManagement();

		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardMngService.getBoardManagement(boardManagement);
			
			resultList = boardMngService.boardList(board);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("resultList", resultList );
		model.addAttribute("menu_id", board.getMenu_id() );
		model.addAttribute("menu_nm", board.getMenu_nm() );
		
		return "boardMng/list";
	}
	
	/* 게시판관리 - 게시물 등록 */
	@RequestMapping(value="/BoardMng/Insert")
	public String Insert(@ModelAttribute("board") Board board, Model model) {
		BoardManagement boardManagement = new BoardManagement();

		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardMngService.getBoardManagement(boardManagement);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("menu_id", board.getMenu_id());
		model.addAttribute("menu_nm", board.getMenu_nm());

		return "boardMng/insert";
	}	
	
	/* 게시판관리 - 게시물 등록 Action */
	@RequestMapping(value="/BoardMng/InsertAction", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result InsertAction(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("board") Board board, Model model) {
		Result result = new Result();
		if(file.getSize() > maxUploadSize){
	    	result.setResult(false);
	    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
	    }else{
			try {				
				boardMngService.insertBoard(file, board);
				result.setResult(true);
			}catch (Exception ex) {
				ex.printStackTrace();
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}		
			
			
	    }
		
		return result;		
	}	
	
	/* 게시판관리 - 게시물 보기 */
	@RequestMapping(value="/BoardMng/View", method=RequestMethod.POST)
	public String View(@ModelAttribute("board") Board board, Model model) {
		Board result = new Board();
		BoardManagement boardManagement = new BoardManagement();
		
		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardMngService.getBoardManagement(boardManagement);

			result = boardMngService.getBoard(board);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("result", result );
		model.addAttribute("menu_id", result.getMenu_id());
		model.addAttribute("menu_nm", result.getMenu_nm());

		return "boardMng/view";
	}
	
	/* 게시판관리 - 게시물 수정 */
	@RequestMapping(value="/BoardMng/Update", method=RequestMethod.POST)
	public String Update(@ModelAttribute("board") Board board, Model model) {
		Board result = new Board();
		BoardManagement boardManagement = new BoardManagement();
		
		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardMngService.getBoardManagement(boardManagement);

			result = boardMngService.getBoard(board);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("result", result );
		model.addAttribute("menu_id", result.getMenu_id());
		model.addAttribute("menu_nm", result.getMenu_nm());

		return "boardMng/update";
	}
	
	/* 게시판관리 - 게시물 등록 Action */
	@RequestMapping(value="/BoardMng/UpdateAction", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result UpdateAction(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("board") Board board, Model model) {
		Result result = new Result();
		if(file.getSize() > maxUploadSize){
	    	result.setResult(false);
	    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
	    }else{
			try {				
				boardMngService.updateBoard(file, board);
				result.setResult(true);
			}catch (Exception ex) {
				ex.printStackTrace();
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}		
	    }
		
		return result;		
	}	
	
	/* 게시판관리 - 게시물 삭제 */
	@RequestMapping(value="/BoardMng/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteAction(@ModelAttribute("board") Board board,  Model model) {
		Result result = new Result();
		
		try{
			boardMngService.deleteBoard(board);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 게시판관리 - 게시물 답글 등록 */
	@RequestMapping(value="/BoardMng/Reply", method=RequestMethod.POST)
	public String Reply(@ModelAttribute("board") Board board, Model model) {
		Board result = new Board();
		BoardManagement boardManagement = new BoardManagement();
		
		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardMngService.getBoardManagement(boardManagement);

			result = boardMngService.getBoard(board);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("result", result );
		model.addAttribute("menu_id", result.getMenu_id());
		model.addAttribute("menu_nm", result.getMenu_nm());

		return "boardMng/reply";
	}
	
	/* 게시판관리 - 게시물 답글등록 Action */
	@RequestMapping(value="/BoardMng/ReplyAction", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result ReplyAction(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("board") Board board, Model model) {
		Result result = new Result();
		if(file.getSize() > maxUploadSize){
	    	result.setResult(false);
	    	result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
	    }else{
			try {				
				boardMngService.replyBoard(file, board);
				result.setResult(true);
			}catch (Exception ex) {
				ex.printStackTrace();
				result.setResult(false);
				result.setMessage(ex.getMessage());
			}		
	    }
		
		return result;		
	}	
	
	/* 게시판관리 - 댓글 리스트 조회 */
	@RequestMapping(value="/BoardMng/CommentList", method=RequestMethod.POST)
	@ResponseBody
	public Result CommentListAction(@ModelAttribute("boardComment") BoardComment boardComment,  Model model) {
		Result result = new Result();
		List<BoardComment> boardCommentList = new ArrayList<BoardComment>();
		
		try{
			boardCommentList = boardMngService.boardCommentList(boardComment);
			result.setResult(boardCommentList.size() > 0 ? true : false);
			result.setObject(boardCommentList); 
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}
	
	/* 게시판관리 - 댓글 저장 */
	@RequestMapping(value="/BoardMng/SaveComment", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveCommentAction(@ModelAttribute("boardComment") BoardComment boardComment,  Model model) {
		Result result = new Result();

		try{
			boardMngService.saveBoardComment(boardComment);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	/* 게시판관리 - 댓글 삭제 */
	@RequestMapping(value="/BoardMng/DeleteComment", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteCommentAction(@ModelAttribute("boardComment") BoardComment boardComment,  Model model) {
		Result result = new Result();

		try{
			boardMngService.deleteBoardComment(boardComment);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}

		return result;	
	}	
	
	@RequestMapping(value = "/BoardMng/downFile")
	@ResponseBody
    public ModelAndView fileDown(@ModelAttribute("board") Board board, HttpServletRequest request, HttpServletResponse response) throws Exception{
		DownLoadFile download = new DownLoadFile();

		String boardRoot = Config.getString("upload.board.dir");
		String filePath = boardRoot;

		download.setFile_path(filePath);
		download.setFile_nm(board.getFile_nm());		
		download.setOrg_file_nm(board.getOrg_file_nm());

		File downloadFile = new File(filePath + board.getFile_nm());
        
        if(!downloadFile.canRead()){
            throw new Exception("File can't read(파일을 찾을 수 없습니다)");
        }

        return new ModelAndView("newFileDownload", "downloadFile", download);
    }
	
	
	/*새로시작*/
	
	/* 공지사항 */
	@RequestMapping(value="/BoardMng/Notice")
	public String NoticeList(@ModelAttribute("board") Board board, Model model) {
		String nowDate = DateUtil.getNowDate("yyyy-MM-dd");
		String startDate = DateUtil.getNextDay("M", "yyyy-MM-dd", nowDate, "365");
		
		if(board.getStrStartDt() == null || board.getStrStartDt().equals("")){
			board.setStrStartDt(startDate);
		}
		
		if(board.getStrEndDt() == null || board.getStrEndDt().equals("")){
			board.setStrEndDt(nowDate);
		}		
		
		model.addAttribute("menu_id", board.getMenu_id());
		model.addAttribute("menu_nm", board.getMenu_nm());
//		model.addAttribute("searchBtnClickCount", board.getSearchBtnClickCount());
		model.addAttribute("call_from_parent", board.getCall_from_parent());

		return "board/noticeList";
	}
	
	/* 공지사항 검색 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/BoardMng/Notice/list", method=RequestMethod.POST, produces="application/text; charset=utf8")
	@ResponseBody
	public String NoticeListAction(@ModelAttribute("board") Board board, Model model) {
		List<Board> resultList = new ArrayList<Board>();

		int dataCount4NextBtn = 0;
		try{
			resultList = boardMngService.noticeList(board);
			if (resultList != null && resultList.size() > board.getPagePerCount()) {
				dataCount4NextBtn = resultList.size();
				resultList.remove(board.getPagePerCount());
			}
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			return getErrorJsonString(ex);
		}

//		return success(resultList).toJSONObject().toString();	
		JSONObject jobj = success(resultList).toJSONObject();
		jobj.put("dataCount4NextBtn", dataCount4NextBtn);
		return jobj.toString();
	}
	
	/*공지사항 업데이트 보기 */
	@RequestMapping(value = "/BoardMng/Notice/update")
	public String NoticeUpdate(@ModelAttribute("board") Board board, Model model) {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		model.addAttribute("auth_cd", auth_cd);
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		model.addAttribute("user_id", user_id);
		String reg_id = board.getReg_id();
		model.addAttribute("reg_id", reg_id);

		try{
			int hitCnt = boardMngService.updateHitCnt(board);//조회수 저장		
			String board_no = StringUtils.defaultString(board.getBoard_no());
			board.setBoard_no(board_no);
			model.addAttribute("noticeContents", boardMngService.readNoticeContents(board));
			model.addAttribute("noticeFiles", boardMngService.readNoticeFiles(board));
			model.addAttribute("hitCnt", hitCnt);
			model.addAttribute("menu_id", board.getMenu_id());
			model.addAttribute("menu_nm", board.getMenu_nm());
			
			logger.debug("테스트 ::::"+board.toString());
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		return "board/noticeUpdate";
	}
	
	/* 공지사항 업데이트 저장  */
	@RequestMapping(value="/BoardMng/Notice/update", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result NoticeUpdateAction(MultipartHttpServletRequest multipartRequest, @ModelAttribute("board") Board board, Model model) throws IOException {
		List<MultipartFile> fileList = multipartRequest.getFiles("uploadFile");
		Result result = new Result();
		String file_name = "";
		try {
			
		for (MultipartFile file : fileList) {
			logger.debug("########### 업로드하려는 파일의 이름 : "+ file.getOriginalFilename()+" ###########");
        	
        	if(file.getOriginalFilename().indexOf(".") != -1){
        		file_name = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        		List<String> noneFileList = new ArrayList<String>();
        		noneFileList = commonService.getNoneFileList();
        		if(noneFileList.contains(file_name)){
        			result.setResult(false);
        			result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
        			return result;
        		}
        	}else{
        		result.setResult(false);
        		result.setMessage("업로드하려는 확장자파일의 이름이 정확하지 않습니다.");
        		return result;
        	}
			if(file.getSize() > maxUploadSize){
				String originalFileName = file.getOriginalFilename();
				int fileLength = file.getBytes().length/1024/1024;
				result.setResult(false);
//        		result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
				result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>10메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
				return result;		
			}
		}
			int check = boardMngService.updateNotice(multipartRequest, board);
			result.setResult(true);
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}		
		
		return result;		
	}
	
	/* 공지사항 등록 */
	@RequestMapping(value = "/BoardMng/Notice/insert", method=RequestMethod.GET)
	public String NoticeInsert(@ModelAttribute("board") Board board, Model model) {
		model.addAttribute("menu_id", board.getMenu_id());
		model.addAttribute("menu_nm", board.getMenu_nm());
		
		return "board/noticeInsert";
	}	
	
	
	/* 공지사항 등록 저장  */
	@RequestMapping(value="/BoardMng/Notice/insert", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result NoticeInsertAction(MultipartHttpServletRequest multipartRequest, @ModelAttribute("board") Board board, Model model) throws IOException {
		List<MultipartFile> fileList = multipartRequest.getFiles("uploadFile");
		Result result = new Result();
		String file_name = "";
		
		try {				
			for (MultipartFile file : fileList) {
	        	logger.debug("########### 업로드하려는 파일의 이름 : "+ file.getOriginalFilename()+" ###########");
	        	
	        	if(file.getOriginalFilename().indexOf(".") != -1){
	        		file_name = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
	        		List<String> noneFileList = new ArrayList<String>();
	        		noneFileList = commonService.getNoneFileList();
	        		if(noneFileList.contains(file_name)){
	        			result.setResult(false);
	        			result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
	        			return result;
	        		}
	        	}else{
	        		result.setResult(false);
	        		result.setMessage("업로드하려는 확장자파일의 이름이 정확하지 않습니다.");
	        		return result;
	        	}
        	if(file.getSize() > maxUploadSize){
        		String originalFileName = file.getOriginalFilename();
				int fileLength = file.getBytes().length/1024/1024;
        		result.setResult(false);
//        		result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
        		result.setMessage(originalFileName+" 파일 용량이 너무 큽니다.<br/>10메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB");
        		return result;		
        	}
		}
        	int check = boardMngService.insertNotice(multipartRequest, board);
        	result.setResult(true);
        }catch (Exception ex) {
        	ex.printStackTrace();
        	result.setResult(false);
        	result.setMessage(ex.getMessage());
        }		
		
		return result;		
	}
	
	/* 사례/가이드 DELTE */
	@RequestMapping(value="/BoardMng/Notice/delete", method=RequestMethod.POST)
	@ResponseBody
	public Result NoticeDeleteAction(@ModelAttribute("board") Board board, Model model) {
		Result result = new Result();
		try {				
			int check = boardMngService.deleteNotice(board);
			result.setResult(check > 0 ? true : false);
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;		
	}
	
	@RequestMapping(value = "/BoardMng/Notice/deleteAttachFile")
	@ResponseBody
	public Result DeleteAttachFile(@ModelAttribute("board") Board board, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Result result = new Result();
		String filePath = Config.getString("upload.board.dir");
		String[] file_nm = StringUtils.defaultString(board.getFile_nm()).split(",");
		String[] file_seq = StringUtils.defaultString(board.getFile_seq()).split(",");
		String[] org_file_nm = StringUtils.defaultString(board.getOrg_file_nm()).split(",");
		//board.setBoard_mgmt_no("1"); //공지사항
		if ( file_nm.length != 0 ) {
			for (int fileIdx = 0; fileIdx < file_nm.length; fileIdx++) {
				if ( file_nm[fileIdx].equals("") ) {
					logger.debug("file_nm is '"+file_nm[fileIdx]+"'");
					result.setResult(false);
					result.setMessage("파일이 존재하지 않습니다.");
					return result;
				}
				
				File attachfile = new File(filePath + file_nm[fileIdx].replaceAll("[*]",",").replaceAll("[:]"," "));
				
				if(!attachfile.canRead()){
					result.setResult(false);
					result.setMessage("[ "+org_file_nm[fileIdx].replaceAll("[*]",",").replaceAll("[:]"," ")+" ]<br>파일을 찾을 수 없습니다.");
					return result;
				}
				boolean deleteResult = attachfile.delete();
				logger.debug("deleteResult:"+deleteResult);
			}
			
			for (String strSeq : file_seq) {
				
				board.setFile_seq(strSeq);
				try {
					int check = boardMngService.deleteAttachFile(board);
					logger.debug("deleteResultCount:"+check);
					result.setResult(true);
					result.setMessage("파일을 삭제하였습니다.");
				} catch (Exception e) {
					e.printStackTrace();
					result.setResult(true);
					result.setMessage("파일 삭제에 실패하였습니다.");
					return result;
				}
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/BoardMng/Notice/DownBoardFile", method = RequestMethod.POST)
	public ModelAndView downloadFile(@ModelAttribute("board") Board board, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DownLoadFile download = new DownLoadFile();
		//String rootPath = request.getSession().getServletContext().getRealPath("/");
		//String filePath = rootPath + guideRoot;
		
		String filePath = Config.getString("upload.board.dir");
		
		String file_nm = StringUtils.defaultString(board.getFile_nm());
		String org_file_nm = StringUtils.defaultString(board.getOrg_file_nm());
		
		logger.debug("file_nm is '"+file_nm+"' and org_file_nm is '"+org_file_nm+"'");
		if(file_nm.equals("") || org_file_nm.equals("")){
			String errorMessage = "파일이 존재하지 않습니다.";
			logger.debug(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			
			return new ModelAndView("list", "msg", errorMessage);
		}
		
		download.setFile_path(filePath);
		download.setFile_nm(file_nm);
		download.setOrg_file_nm(org_file_nm);

		File downloadFile = new File(filePath + file_nm);
		
		if(!downloadFile.canRead()){
			String errorMessage = "[ "+org_file_nm+" ]<br>파일을 찾을 수 없습니다";
			logger.debug(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			
			Result result = new Result();
			result.setResult(false);
			result.setMessage("실패");
			return new ModelAndView("list", "msg", errorMessage);
		}
		
		return new ModelAndView("newFileDownload", "downloadFile", download);
	}
	
/*	@RequestMapping(value = "/BoardMng/Notice/DownBoardFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute("board") Board board, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		DownLoadFile download = new DownLoadFile();
		//String rootPath = request.getSession().getServletContext().getRealPath("/");
		//String filePath = rootPath + guideRoot;
		
		String filePath = Config.getString("upload.board.dir");
		request.setAttribute("DownloadDir", "upload.board.dir");
		
		String file_nm = StringUtils.defaultString(board.getFile_nm());
		String org_file_nm = StringUtils.defaultString(board.getOrg_file_nm());
		
		logger.debug("file_nm is '"+file_nm+"' and org_file_nm is '"+org_file_nm+"'");
		if(file_nm.equals("") || org_file_nm.equals("")){
			String errorMessage = "파일이 존재하지 않습니다.";
			logger.debug(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return; 			
		}
		
		download.setFile_path(filePath);
		download.setFile_nm(file_nm);		
		download.setOrg_file_nm(org_file_nm);
		
		File downloadFile = new File(filePath + file_nm);
		
		if(!downloadFile.canRead()){
			String errorMessage = "File can't read(파일을 찾을 수 없습니다)";
			logger.debug(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;            
		}
		
//        PerfGuideUse perfGuideUse = new PerfGuideUse();
//        perfGuideUse.setGuide_no(board.getBoard_no());
//        perfGuideUse.setUse_seq(board.getUse_seq());
		
				try {	뭔지 모르겠음			
			precedentSboardMngServiceervice.updatePerfGuideUse(perfGuideUse);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
//        return new ModelAndView("fileDownload", "downloadFile", download);
		
		if(!downloadFile.exists()){
			String errorMessage = "Sorry. The file you are looking for does not exist";
			logger.debug(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}
		
		String mimeType = URLConnection.guessContentTypeFromName(downloadFile.getName());
		logger.debug("mimeType1 :" + mimeType);
		if (mimeType == null) {
			logger.debug("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}
		
		logger.debug("mimetype2 : " + mimeType);
		String org_file_nm_enc = java.net.URLEncoder.encode(download.getOrg_file_nm(), "utf-8");
		logger.debug("org_file_nm_enc : " + org_file_nm_enc);
		
		response.setContentType(mimeType);
		
		 "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]
//        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + org_file_nm_enc +"\""));
		response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(download.getOrg_file_nm(), "utf-8") + "\";");
		
		
		 "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting
		//response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		
		response.setContentLength((int)downloadFile.length());
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(downloadFile));
		
		//Copy bytes from source to destination(outputstream in this example), closes both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}	
*/	
}