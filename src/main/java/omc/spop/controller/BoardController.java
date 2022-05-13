package omc.spop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringUtils;
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
import omc.spop.service.BoardService;
import omc.spop.service.CommonService;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.02.23	이원식	최초작성
 * 2021.03.30	이재우	성능개선
 **********************************************************/

@Controller
@RequestMapping(value = "/Board")
public class BoardController extends InterfaceController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private BoardMngService boardMngService;
	
	@Autowired
	private CommonService commonService;

	@Value("#{defaultConfig['maxUploadSize']}")
	private int maxUploadSize;
	
	@Value("#{defaultConfig['maxUploadMegaBytes']}")
	private int maxUploadMegaBytes;
	
	@Value("#{defaultConfig['maxUpload10MBSize']}")
	private int maxUpload10MBSize;
	
	@Value("#{defaultConfig['maxUpload10MegaBytes']}")
	private int maxUpload10MegaBytes;
	
	/* 게시판 - 리스트 */
	@RequestMapping(value="/List")
	public String List(@ModelAttribute("board") Board board, Model model) {
		List<Board> resultList = new ArrayList<Board>();
		BoardManagement boardManagement = new BoardManagement();
		StringBuffer title = new StringBuffer();
		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardService.getBoardManagement(boardManagement);
			resultList = boardService.boardList(board);
		
			for ( Board boardStr : resultList ) {
				// html 특수문자 변환
				title.append( removeSpecialChar( boardStr.getTitle() ) );
				boardStr.setTitle( title.toString() );
				title.setLength(0);
				
				// html 형식 제거.
//				title.append( removeHTML( boardStr.getTitle() ));
//				boardStr.setTitle( title.toString() );
//				title.setLength(0);
			}
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("boardManagement", boardManagement);
		model.addAttribute("resultList", resultList );
		model.addAttribute("menu_id", board.getMenu_id());
		model.addAttribute("menu_nm", board.getMenu_nm());
		model.addAttribute("board", board);
//		model.addAttribute("attachFile", boardService.readNoticeFiles(board));
		
		return "board/list";
	}
	
	/* 게시판 - 게시물 등록 */
	@RequestMapping(value="/Insert", method= {RequestMethod.POST, RequestMethod.GET})
	public String Insert(@ModelAttribute("board") Board board, Model model) {
		BoardManagement boardManagement = new BoardManagement();
		
		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardService.getBoardManagement(boardManagement);
//			boardManagement.setFile_add_yn(null); // 에러코드
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("menu_id", board.getMenu_id());
		model.addAttribute("menu_nm", board.getMenu_nm());
		
		return "board/insert";
	}
	
	/* 게시판 - 게시물 등록 Action */
	@RequestMapping(value="/InsertAction", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result InsertAction(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("board") Board board, Model model) {
		Result result = new Result();
		int check = 0;
		int fileLength = 0;
		List<MultipartFile> fileList = board.getUploadFile();
		String file_name = "";
		
		try {// 파일명이 하나라도 있을경우 실행.
			if ( fileList.size() > 0 && !fileList.get(0).getOriginalFilename().equals("") ) {
				if ( fileList.size() < 4 ) {
					
					// DBA게시판 일 경우 ( Board_mgmt_no = 2 )
					if ( board.getBoard_mgmt_no().equals("2") ) {
						for (MultipartFile attachfile : fileList) {
							
							logger.debug("########### 업로드하려는 파일의 이름 : "+ attachfile.getOriginalFilename()+" ###########");
							if ( attachfile.getSize() >= maxUpload10MBSize ) {
								logger.debug("fileSize ===---> "+ attachfile.getSize() + "maxUpload10MBSize ==>? "+ maxUpload10MBSize);
								
								fileLength = attachfile.getBytes().length/1024/1024;
								
								result.setResult(false);
								result.setMessage( attachfile.getOriginalFilename()+"<br>파일 용량이 너무 큽니다.<br/>현재용량 :"+fileLength+" MB , 최대용량 : 10 MB");
								
								return result;
							}
							
							if ( attachfile.getOriginalFilename().lastIndexOf(".") != -1 ) {
								logger.debug("fileName ===---> "+ attachfile.getOriginalFilename() + " , "+ attachfile.getOriginalFilename().lastIndexOf(".") );
								file_name = attachfile.getOriginalFilename().substring(attachfile.getOriginalFilename().lastIndexOf("."));
								List<String> noneFileList = commonService.getNoneFileList();
								
								if ( noneFileList.contains(file_name) ) {
									result.setResult(false);
									result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
									
									return result;
								}
							}
						}
					} else {
						for(MultipartFile attachFile : fileList){
							logger.debug("########### 공지 업로드하려는 파일의 이름 : "+ attachFile.getOriginalFilename()+" ###########");
							
							if(attachFile.getOriginalFilename().lastIndexOf(".") != -1){
								file_name = attachFile.getOriginalFilename().substring(attachFile.getOriginalFilename().trim().lastIndexOf("."));
								List<String> noneFileList = commonService.getNoneFileList();
								
								if(noneFileList.contains(file_name)){
									result.setResult(false);
									result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
									
									return result;
								}
							}
							
							if(attachFile.getSize() >= maxUploadSize){
								fileLength = attachFile.getBytes().length/1024/1024;
								
								result.setResult(false);
								result.setMessage( attachFile.getOriginalFilename()+"<br>파일 용량이 너무 큽니다.<br/>현재용량 :"+fileLength+" MB , 최대용량 : "+maxUploadMegaBytes+" MB");
								
								return result;
							}
						}
					}
				} else {
					result.setMessage("첨부파일은 최대 3개까지 첨부가능 합니다.");
					result.setResult(false);
					return result;
				}
			}
			
			check = boardService.insertBoard(fileList, board);
			result.setResult(check > 0 ? true : false);
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외 발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		return result;
	}
	
	/* 게시판 - 게시물 보기 */
	@RequestMapping(value="/View", method= {RequestMethod.POST, RequestMethod.GET})
	public String View(@ModelAttribute("board") Board board, Model model) {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		Board subBoard = new Board();
		BoardManagement boardManagement = new BoardManagement();
		
		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardService.getBoardManagement(boardManagement);
			
			/* 조회수 증가 */
			boardService.updateBoardHitCnt(board);
			
			subBoard = boardService.getBoard(board);
			subBoard.setTitle( removeSpecialChar( subBoard.getTitle() ) );
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("user_id", user_id );
		model.addAttribute("auth_cd", auth_cd );
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("result", subBoard );
		model.addAttribute("menu_id", board.getMenu_id());
		model.addAttribute("menu_nm", board.getMenu_nm());
		
		model.addAttribute("attachFile", boardService.readNoticeFiles(board));
		
		return "board/view";
	}
	
	/* 게시판 - 게시물 수정 */
	@RequestMapping(value="/Update", method= {RequestMethod.GET,RequestMethod.POST})
	public String Update(@ModelAttribute("board") Board board, Model model) {
		Board result = new Board();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		BoardManagement boardManagement = new BoardManagement();
		List<Board> fileList = new LinkedList<Board>(); 
		StringBuffer files = new StringBuffer();
		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardService.getBoardManagement(boardManagement);
			fileList = boardService.readNoticeFiles(board);
			result = boardService.getBoard(board);
			
			for ( Board bod : fileList ) {
				files.append(bod.getOrg_file_nm()).append(",");
			}
			
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("user_id", user_id );
		model.addAttribute("auth_cd", auth_cd );
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("result", result );
		model.addAttribute("menu_id", board.getMenu_id());
		model.addAttribute("menu_nm", board.getMenu_nm());
		model.addAttribute("files", files.toString());
		model.addAttribute("attachFile", fileList);
		
		return "board/update";
	}
	
	/* 게시판 - 게시물 수정 Action */
	@RequestMapping(value="/UpdateAction", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result UpdateAction(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("board") Board board, Model model) throws Exception {
		Result result = new Result();
		int check = 0;
		int fileLength = 0;
		List<MultipartFile> fileList = board.getUploadFile();
		List<Board> files = boardService.readNoticeFiles(board);
		String file_name = "";
		
		try {
			if ( fileList.size() > 0 && fileList.get(0).getOriginalFilename().equals("") == false &&
					files.size() < 4 && (fileList.size() + files.size()) < 4 ||
					( (fileList.size() + files.size()) <= 4 && fileList.get(0).getOriginalFilename().equals("") ) == true) {
			
				logger.debug("########### 업로드하려는 파일의 이름 : "+ StringUtils.defaultString(fileList.get(0).getOriginalFilename())+" , size ==>"+
						(fileList.size() + files.size())+" , fileList == "+fileList.size() +", "+files.size()+" ###########");
				
				// DBA게시판 일 경우 ( Board_mgmt_no = 2 )
				if ( board.getBoard_mgmt_no().equals("2") ) {
					// 파일명이 하나라도 있을경우 실행.
					
					for ( MultipartFile attachfile : fileList ) {
						if ( attachfile.getSize() >= maxUpload10MBSize ) {
							logger.debug("fileSize ===---> "+ attachfile.getSize() + "maxUpload10MBSize ==>? "+ maxUpload10MBSize);
							
							fileLength = attachfile.getBytes().length/1024/1024;
							
							result.setResult(false);
							result.setMessage( attachfile.getOriginalFilename()+"<br>파일 용량이 너무 큽니다.<br/>현재용량 :"+fileLength+" MB , 최대용량 : 10 MB");
							
							return result;
						}
						
						if ( attachfile.getOriginalFilename().lastIndexOf(".") != -1 ) {
							logger.debug("fileName ===---> "+ attachfile.getOriginalFilename() + " , "+ attachfile.getOriginalFilename().lastIndexOf(".") );
							file_name = attachfile.getOriginalFilename().substring(attachfile.getOriginalFilename().lastIndexOf("."));
							List<String> noneFileList = commonService.getNoneFileList();
							
							if ( noneFileList.contains(file_name) ) {
								result.setResult(false);
								result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
								
								return result;
							}
						}
					}
				} else {
					for(MultipartFile attachFile : fileList){
						if ( attachFile.getOriginalFilename().lastIndexOf(".") != -1) {
							logger.debug("fileName ===---> "+ attachFile.getOriginalFilename() + " , "+ attachFile.getOriginalFilename().lastIndexOf(".") );
							file_name = attachFile.getOriginalFilename().substring(attachFile.getOriginalFilename().lastIndexOf("."));
							List<String> noneFileList = commonService.getNoneFileList();
							
							if ( noneFileList.contains(file_name) ) {
								result.setResult(false);
								result.setMessage("[ "+ file_name +" ]의 확장자는 업로드할 수 없습니다.");
								
								return result;
							}
						}
						
						if ( attachFile.getSize() >= maxUploadSize ) {
							fileLength = attachFile.getBytes().length/1024/1024;
							
							result.setResult(false);
							result.setMessage(attachFile.getOriginalFilename()+" 파일 용량이 너무 큽니다.<br>현재용량 : "+fileLength+" MB , 최대 용량 : "+maxUploadMegaBytes+" MB");
							throw new Exception(result.getMessage());
						}
					}
				}
				
				check = boardService.updateBoard(fileList, board);
				result.setResult(check > 0 ? true : false);
			} else {
				result.setMessage("첨부파일은 최대 3개까지 첨부가능 합니다.");
				result.setResult(false);
				return result;
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 게시판 - 게시물 삭제 */
	@RequestMapping(value="/Delete", method=RequestMethod.POST)
	@ResponseBody
	public Result Delete(@ModelAttribute("board") Board board,  Model model) {
		Result result = new Result();
		List<Board> fileList = new ArrayList<Board>();
		int count = 0;
		
		String filePath = Config.getString("upload.board.dir");
		
		try {
			count = boardService.deleteBoard(board);
			
			if ( count > 0 ) {
				fileList = boardService.readNoticeFiles(board);
				
				if ( fileList.size() > 0 ) {
					for ( Board file : fileList ) {
						String file_nm = StringUtils.defaultString(file.getFile_nm());
						
						File attachfile = new File(filePath + file_nm);
						
						if ( !file_nm.equals("") && attachfile.canRead() ) {
							boolean deleteResult = attachfile.delete();
							logger.debug("deleteResult =====> "+deleteResult);
						}
						int cnt = 0;
						cnt += boardMngService.deleteAttachFile( file );
						logger.debug("deleteResultCount =====> "+cnt);
					}
				}
			}
			result.setResult(true);
		} catch ( Exception ex ) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 게시판 - 게시물 답글 등록 */
	@RequestMapping(value="/Reply", method=RequestMethod.POST)
	public String Reply(@ModelAttribute("board") Board board, Model model) {
		Board result = new Board();
		BoardManagement boardManagement = new BoardManagement();
		
		try{
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardService.getBoardManagement(boardManagement);
			
			result = boardService.getBoard(board);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		
		model.addAttribute("boardManagement", boardManagement );
		model.addAttribute("result", result );
		model.addAttribute("menu_id", board.getMenu_id());
		model.addAttribute("menu_nm", board.getMenu_nm());
		
		return "board/reply";
	}
	
	/* 게시판 - 게시물 답글등록 Action */
	@RequestMapping(value="/ReplyAction", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	@ResponseBody
	public Result ReplyAction(@RequestParam("uploadFile") MultipartFile file, @ModelAttribute("board") Board board, Model model) {
		Result result = new Result();
		int check = 0;
		List<MultipartFile> fileList = board.getUploadFile();
		
		for(MultipartFile attachFile : fileList){
			if(attachFile.getSize() > maxUploadSize){
				result.setResult(false);
				result.setMessage("파일 용량이 너무 큽니다.\\n"+maxUploadMegaBytes+"메가 이하로 선택해 주세요.");
			}
		}
		
		try {
			check = boardService.replyBoard(fileList, board);
			result.setResult(true);
		}catch (Exception ex) {
			ex.printStackTrace();
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 게시판 - 댓글 리스트 조회 */
	@RequestMapping(value="/CommentList", method=RequestMethod.POST)
	@ResponseBody
	public Result CommentListAction(@ModelAttribute("boardComment") BoardComment boardComment,  Model model) {
		Result result = new Result();
		List<BoardComment> boardCommentList = new ArrayList<BoardComment>();
		
		try{
			boardCommentList = boardService.boardCommentList(boardComment);
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
	
	/* 게시판 - 댓글 저장 */
	@RequestMapping(value="/SaveComment", method=RequestMethod.POST)
	@ResponseBody
	public Result SaveCommentAction(@ModelAttribute("boardComment") BoardComment boardComment,  Model model) {
		Result result = new Result();
		
		try{
			boardService.saveBoardComment(boardComment);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	/* 게시판 - 댓글 삭제 */
	@RequestMapping(value="/DeleteComment", method=RequestMethod.POST)
	@ResponseBody
	public Result DeleteCommentAction(@ModelAttribute("boardComment") BoardComment boardComment,  Model model) {
		Result result = new Result();
		
		try{
			boardService.deleteBoardComment(boardComment);
			result.setResult(true);
		} catch (Exception ex){
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			result.setResult(false);
			result.setMessage(ex.getMessage());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/DownFile")
	@ResponseBody
	public ModelAndView DownFile(@ModelAttribute("board") Board board, HttpServletRequest request, HttpServletResponse response) throws Exception{
		DownLoadFile download = new DownLoadFile();
		
		String boardRoot = Config.getString("upload.board.dir");
		String filePath = boardRoot;
		
		download.setFile_path(filePath);
		download.setFile_nm(board.getFile_nm());
		download.setOrg_file_nm(board.getOrg_file_nm());
		
		File downloadFile = new File(filePath + board.getFile_nm());
		
		if(!downloadFile.canRead()){
			download.setFile_path("");
			download.setFile_nm("");
			download.setOrg_file_nm("첨부파일이_존재하지_않습니다.");
			download.setDefaultText( board.getBoard_no() );
			download.setMenu_id( board.getMenu_id() );
			download.setMenu_nm( board.getMenu_nm() );
			
			ModelAndView mav = new ModelAndView();
			Board result = new Board();
			
			BoardManagement boardManagement = new BoardManagement();
			boardManagement.setBoard_mgmt_no(board.getBoard_mgmt_no());
			boardManagement = boardService.getBoardManagement(boardManagement);

			String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
			String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
			
			logger.debug("searchKey ===== > "+ download.getSearchKey2() );
			if ( "list".equals( board.getSearchKey2() ) ) {
				List<Board> resultList = new ArrayList<Board>();
				StringBuffer title = new StringBuffer();
				try {
					resultList = boardService.boardList(board);
					
					for ( Board boardStr : resultList ) {
						// html 특수문자 변환
						title.append( removeSpecialChar( boardStr.getTitle() ));
						boardStr.setTitle( title.toString() );
						title.setLength(0);
						
						// html 형식 제거.
						title.append( removeHTML( boardStr.getTitle() ));
						boardStr.setTitle( title.toString() );
						title.setLength(0);
					}
					
				} catch (Exception ex){
					String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
					logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				}
				
				mav.addObject("boardManagement", boardManagement);
				mav.addObject("resultList", resultList );
				mav.addObject("menu_id", board.getMenu_id());
				mav.addObject("menu_nm", board.getMenu_nm());
				mav.addObject("board", board);
				
				mav.setViewName("board/list");
			} else if ( "update".equals( board.getSearchKey2() ) ) {
				try {
					result = boardService.getBoard(board);
				} catch (Exception ex){
					String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
					logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				}
				
				mav.addObject("user_id", user_id );
				mav.addObject("auth_cd", auth_cd );
				mav.addObject("boardManagement", boardManagement );
				mav.addObject("result", result );
				mav.addObject("menu_id", board.getMenu_id());
				mav.addObject("menu_nm", board.getMenu_nm());
				mav.addObject("attachFile", boardService.readNoticeFiles(board));
				
				mav.setViewName("board/update");
			} else {
				try {
					/* 조회수 증가 */
//					boardService.updateBoardHitCnt(board);
					
					result = boardService.getBoard(board);
				} catch (Exception ex){
					String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
					logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
				}
				
				mav.addObject("user_id", user_id );
				mav.addObject("auth_cd", auth_cd );
				mav.addObject("boardManagement", boardManagement );
				mav.addObject("result", result );
				mav.addObject("menu_id", board.getMenu_id());
				mav.addObject("menu_nm", board.getMenu_nm());
				
				mav.addObject("attachFile", boardService.readNoticeFiles(board));
				mav.setViewName( "board/view" );
			}
			mav.addObject("defaultText", "[ "+board.getOrg_file_nm()+" ]<br>파일을 찾을 수 없습니다.");
			return mav;
		} else {
			return new ModelAndView("newFileDownload", "downloadFile", download);
		}
	}
	
	/*설정 - 사용자관리 엑셀 다운*/
	@RequestMapping(value = "/Notice/ExcelDown", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView PerformanceCheckMngListExcelDown(HttpServletRequest req, HttpServletResponse res,
			@ModelAttribute("board") Board board, Model model) throws Exception {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		
		try {
			resultList = boardService.boardListByExcelDown(board);
		} catch (Exception ex) {
			String methodName = new Object() {
			}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
		}
		model.addAttribute("fileName", "공지사항");
		model.addAttribute("sheetName", "공지사항");
		model.addAttribute("excelId", "BOARD_NOTICE");
		// return a view which will be resolved by an excel view resolver
		return new ModelAndView("xlsxView", "resultList", resultList);
	}
	/**
	 * 특수문자 제거
	 *
	 * @param strText  특수문자가 포함된 문자열
	 * @return 문자열
	 */
	public String removeSpecialChar(String strText) {
		String returnValue;
		
		returnValue = strText;
		returnValue = returnValue.replaceAll("&lt;","<");
		returnValue = returnValue.replaceAll("&gt;",">");
		returnValue = returnValue.replaceAll("&amp;","&");
		returnValue = returnValue.replaceAll("&quot;","\"");
		returnValue = returnValue.replaceAll("&#035;","#");
		returnValue = returnValue.replaceAll("&nbsp;"," ");
		returnValue = returnValue.replaceAll("&#39;","'");
		returnValue = returnValue.replaceAll("<>","@@");
		returnValue = returnValue.replaceAll("(<span>)|(</span>)","");
		returnValue = returnValue.replaceAll("(<a>)|(</a>)","");
		returnValue = returnValue.replaceAll("(<P>)|(</P>)","");
		returnValue = returnValue.replaceAll("(<br>)|(<br\\/>)|(<br \\/>)","\n");
		returnValue = returnValue.replaceAll("@@","<>");
		returnValue = returnValue.replaceAll("\n","\r\n");
		
		return returnValue;
	}
	/**
	 * HTML 테크 제거후 문자열 리턴
	 *
	 * @param strText  HTML이 포함된 문자열
	 * @return 문자열
	 */
	public String removeHTML(String strText) {
		String returnValue;
		
		returnValue = strText;
//		returnValue = returnValue.replaceAll("\\s"," ");
		returnValue = returnValue.replaceAll("&nbsp;"," ");
		returnValue = returnValue.replaceAll("&#39;","'");
		returnValue = returnValue.replaceAll("<>","@@");
		returnValue = returnValue.replaceAll("(<br>)|(<br\\/>)|(<br \\/>)","\n");
		returnValue = returnValue.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>","");
		returnValue = returnValue.replaceAll("@@","<>");
		returnValue = returnValue.replaceAll("\n","\r\n");
		
		return returnValue;
	}
}
