package omc.spop.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import omc.spop.base.SessionManager;
import omc.spop.dao.BoardMngDao;
import omc.spop.model.Board;
import omc.spop.model.BoardComment;
import omc.spop.model.BoardManagement;
import omc.spop.model.PerfGuide;
import omc.spop.model.PerfGuideAttachFile;
import omc.spop.service.BoardMngService;
import omc.spop.utils.FileMngUtil;

/***********************************************************
 * 2017.11.10	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업 
 **********************************************************/

@Service("BoardMngService")
public class BoardMngServiceImpl implements BoardMngService {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardMngServiceImpl.class);
	
	@Autowired
	private BoardMngDao boardMngDao;

	@Override
	public BoardManagement getBoardManagement(BoardManagement boardManagement) throws Exception {
		return boardMngDao.getBoardManagement(boardManagement);
	}	

	@Override
	public List<Board> boardList(Board board) throws Exception {
		board.setTotalCount(boardMngDao.getBoardListCount(board));
		return boardMngDao.boardList(board);
	}
	
	@Override
	public Board getBoard(Board board) throws Exception {
		return boardMngDao.getBoard(board);
	}

	@Override
	public void insertBoard(MultipartFile file, Board board) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		Board result = new Board();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String boardNo = "";

		if(!file.isEmpty()){
			// 1. 파일 업로드 처리
			try {
				result = fileMng.uploadBoard(file, board);			
			}catch (Exception ex) {
				logger.error("File Upload error => " + ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}
		}else{
			result.setFile_nm("");
			result.setOrg_file_nm("");
			result.setFile_size("");
			result.setFile_ext_nm("");
		}

		// 2. BOARD MAX BOARD_NO 조회
		boardNo = boardMngDao.getMaxBoardNo(board);
		
		result.setBoard_mgmt_no(board.getBoard_mgmt_no());
		result.setBoard_no(boardNo);
		result.setTop_notice_yn(board.getTop_notice_yn());
		result.setTitle(board.getTitle());
		result.setBoard_contents(board.getBoard_contents());
		result.setRef_board_no(boardNo);
		result.setTop_board_no(boardNo);
		result.setDepth("0");
		result.setLvl("0");
		result.setHit_cnt("0");
		result.setReg_id(user_id);
		
		// 3. BOARD INSERT
		boardMngDao.insertBoard(result);
	}
	
	@Override
	public void updateBoard(MultipartFile file, Board board) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		Board result = new Board();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();

		if(!file.isEmpty()){
			// 1. 파일 업로드 처리
			try {
				result = fileMng.uploadBoard(file, board);			
			}catch (Exception ex) {
				logger.error("File Upload error => " + ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}
		}else{
			result.setFile_nm(board.getFile_nm());
			result.setOrg_file_nm(board.getOrg_file_nm());
			result.setFile_size(board.getFile_size());
			result.setFile_ext_nm(board.getFile_ext_nm());
		}

		result.setBoard_mgmt_no(board.getBoard_mgmt_no());
		result.setBoard_no(board.getBoard_no());
		result.setTop_notice_yn(board.getTop_notice_yn());
		result.setTitle(board.getTitle());
		result.setBoard_contents(board.getBoard_contents());
		result.setUpd_id(user_id);
		
		// 2. BOARD UPDATE
		boardMngDao.updateBoard(result);
	}
	
	@Override
	public void deleteBoard(Board board) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		board.setUpd_id(user_id);
		
		// 자신 글 삭제
		boardMngDao.deleteBoard(board);
		
		// 자식글 삭제
		boardMngDao.deleteChildBoard(board);
	}
	
	@Override
	public void replyBoard(MultipartFile file, Board board) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		Board result = new Board();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String boardNo = "";

		if(!file.isEmpty()){
			// 1. 파일 업로드 처리
			try {
				result = fileMng.uploadBoard(file, board);			
			}catch (Exception ex) {
				logger.error("File Upload error => " + ex.getMessage());
				ex.printStackTrace();
				throw ex;
			}
		}else{
			result.setFile_nm("");
			result.setOrg_file_nm("");
			result.setFile_size("");
			result.setFile_ext_nm("");
		}

		// 2. BOARD MAX BOARD_NO 조회
		boardNo = boardMngDao.getMaxBoardNo(board);
		
		// 3. 답글 LEVEL UPDATE
		boardMngDao.updateReplyLevel(board);
		
		int intDepth = Integer.parseInt(board.getDepth()) + 1;
		int intLvl = Integer.parseInt(board.getLvl()) + 1;
		
		result.setBoard_mgmt_no(board.getBoard_mgmt_no());
		result.setBoard_no(boardNo);
		result.setTop_notice_yn(board.getTop_notice_yn());
		result.setTitle(board.getTitle());
		result.setBoard_contents(board.getBoard_contents());
		result.setRef_board_no(board.getRef_board_no());
		result.setTop_board_no(board.getTop_board_no());
		result.setDepth(String.valueOf(intDepth));
		result.setLvl(String.valueOf(intLvl));
		result.setHit_cnt("0");
		result.setReg_id(user_id);
		
		// 3. BOARD INSERT
		boardMngDao.insertBoard(result);
	}
	
	@Override
	public List<BoardComment> boardCommentList(BoardComment boardComment) throws Exception {
		return boardMngDao.boardCommentList(boardComment);
	}
	
	@Override
	public void saveBoardComment(BoardComment boardComment) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String commentSeq = "";
		// 1. MAX COMMENT SEQ 조회
		commentSeq = boardMngDao.getBoardCommentSeq(boardComment);
		
		boardComment.setComment_seq(commentSeq);
		boardComment.setReg_id(user_id);
		
		// 2. BOARD_COMMENT INSERT
		boardMngDao.insertBoardComment(boardComment);
	}
	
	@Override
	public void deleteBoardComment(BoardComment boardComment) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		boardComment.setUpd_id(user_id);
		
		// 1. BOARD_COMMENT DELETE
		boardMngDao.deleteBoardComment(boardComment);
	}

	
	
	/**새로시작**/
	@Override
	public List<Board> noticeList(Board board) {
		return boardMngDao.noticeList(board);
	}

	@Override
	public int updateHitCnt(Board board) {
		return boardMngDao.updateHitCnt(board);
	}

	@Override
	public Board readNoticeContents(Board board) {
		return boardMngDao.readNoticeContents(board);
	}

	@Override
	public List<Board> readNoticeFiles(Board board) {
		return boardMngDao.readNoticeFiles(board);
	}

	@Override
	public int insertNotice(MultipartHttpServletRequest multipartRequest, Board board) throws Exception {
		int check = 0;
		FileMngUtil fileMng = new FileMngUtil();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String fileSeq = "";
		String board_mgmt_no = "1"; // 공지사항

		// 1-1. PERF_GUIDE MAX GUIDE_NO 조회		
//		board_no = boardMngDao.getMaxPerfGuideNo(board);
		
		board.setBoard_mgmt_no(board_mgmt_no);
		board.setReg_id(user_id);
		// 1-2. PERF_GUIDE INSERT
		check = boardMngDao.insertNotice(board);
		
		if(check > 0){
		List<MultipartFile> fileList = multipartRequest.getFiles("uploadFile");
	        for (MultipartFile file : fileList) {
				if(!file.isEmpty() && check > 0){
					// 2. 파일 업로드 정보 조회
					try {
						board = fileMng.uploadBoard(file, board);
						board.setBoard_mgmt_no(board_mgmt_no);
					}catch (Exception ex) {
						logger.error("error => " + ex.getMessage());
						ex.printStackTrace();
						throw new Exception("error => " + ex.getMessage());
					}
					//2-1. 파일 존재시 PERF_GUIDE_ATTACH_FILE MAX FILE_SEQ 조회
//					fileSeq = boardMngDao.getMaxNoticeFileSeq(board);
							
					//2-2. 파일 존재시 PERF_GUIDE_ATTACH_FILE INSERT
//					board.setFile_seq(fileSeq);
					check = boardMngDao.insertNoticeAttachFiles(board);
				}
	        }
		}
		return check ; 
	}

	@Override
	public int updateNotice(MultipartHttpServletRequest multipartRequest, Board board) throws Exception {
		int check = 0;
		FileMngUtil fileMng = new FileMngUtil();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String fileSeq = "";
		String board_mgmt_no = "1"; // 공지사항
		// 1-1. PERF_GUIDE MAX GUIDE_NO 조회		
//		board_no = boardMngDao.getMaxPerfGuideNo(board);
		
		board.setBoard_mgmt_no(board_mgmt_no);
		board.setUpd_id(user_id);
		// 1-2. PERF_GUIDE INSERT
		check = boardMngDao.updateNotice(board);
		
		if(check > 0){
		List<MultipartFile> fileList = multipartRequest.getFiles("uploadFile");
	        for (MultipartFile file : fileList) {
				if(!file.isEmpty() && check > 0){
					// 2. 파일 업로드 정보 조회
					try {
						board = fileMng.uploadBoard(file, board);
						board.setBoard_mgmt_no(board_mgmt_no);
					}catch (Exception ex) {
						logger.error("error => " + ex.getMessage());
						ex.printStackTrace();
						throw new Exception("error => " + ex.getMessage());
					}
					//2-1. 파일 존재시 PERF_GUIDE_ATTACH_FILE MAX FILE_SEQ 조회
//					fileSeq = boardMngDao.getMaxNoticeFileSeq(board);
							
					//2-2. 파일 존재시 PERF_GUIDE_ATTACH_FILE INSERT
//					board.setFile_seq(fileSeq);
					check = boardMngDao.insertNoticeAttachFiles(board);
				}
	        }
		}
		return check ; 
	}

	@Override
	public int deleteNotice(Board board) {
		int check = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		board.setUpd_id(user_id);
		check = boardMngDao.deleteNotice(board);
		
		return check;
	}

	@Override
	public int deleteAttachFile(Board board) {
		return boardMngDao.deleteAttachFile(board);
	}

}