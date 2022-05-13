package omc.spop.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import omc.spop.base.SessionManager;
import omc.spop.dao.BoardDao;
import omc.spop.dao.BoardMngDao;
import omc.spop.model.Board;
import omc.spop.model.BoardComment;
import omc.spop.model.BoardManagement;
import omc.spop.service.BoardService;
import omc.spop.utils.FileMngUtil;

/***********************************************************
 * 2018.02.23	이원식	최초작성
 **********************************************************/

@Service("BoardService")
public class BoardServiceImpl implements BoardService {
	
	private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
	
	@Autowired
	private BoardDao boardDao;

	@Autowired
	private BoardMngDao boardMngDao;
	
	@Override
	public BoardManagement getBoardManagement(BoardManagement boardManagement) throws Exception {
		return boardDao.getBoardManagement(boardManagement);
	}	

	@Override
	public void updateBoardHitCnt(Board board) throws Exception {
		boardDao.updateBoardHitCnt(board);
	}

	@Override
	public Board getBoard(Board board) throws Exception {
		return boardDao.getBoard(board);
	}

	@Override
	public int insertBoard(List<MultipartFile> fileList, Board board) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		Board result = new Board();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String boardNo = "";
		int check = 0;

		// 2. BOARD MAX BOARD_NO 조회
		boardNo = boardDao.getMaxBoardNo(board);
		
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
		result.setUpd_id(user_id);
		
		// 3. BOARD INSERT
		check = boardDao.insertBoard(result);
   
		if(check > 0){
			for (MultipartFile file : fileList) {
				if(!file.isEmpty() && check > 0){
					// 1. 파일 업로드 처리
					try {
						result = fileMng.uploadBoard(file, board);	
					}catch (Exception ex) {
						logger.error("File Upload error => " + ex.getMessage());
						ex.printStackTrace();
						throw ex;
					}
					check = boardDao.insertNoticeAttachFiles(result);
				}
			}
		}
		return check;
	}
	
	@Override
	public int updateBoard(List<MultipartFile> fileList, Board board) throws Exception {
		
		FileMngUtil fileMng = new FileMngUtil();
		Board result = new Board();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		int check = 0;
		
		result.setBoard_mgmt_no(board.getBoard_mgmt_no());
		result.setBoard_no(board.getBoard_no());
		result.setTop_notice_yn(board.getTop_notice_yn());
		result.setTitle(board.getTitle());
		result.setBoard_contents(board.getBoard_contents());
		result.setUpd_id(board.getUpd_id());
		
		// 2. BOARD UPDATE
		check = boardDao.updateBoard(result);
		
		if(check > 0){
			for (MultipartFile file : fileList) {
				if(!file.isEmpty() && check > 0){
					// 1. 파일 업로드 처리
					try {
						result = fileMng.uploadBoard(file, board);
					}catch (Exception ex) {
						logger.error("File Upload error => " + ex.getMessage());
						ex.printStackTrace();
						throw ex;
					}
					result.setBoard_no(board.getBoard_no());
					check = boardDao.updateNoticeAttachFiles(result);
				}
			}
		}
		return check;
	}
	
	@Override
	public int deleteBoard(Board board) throws Exception {
		List<BoardComment> boardCommentList = new ArrayList<BoardComment>();
		BoardComment boardComment = new BoardComment();
		int count = 0;
		
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		board.setUpd_id(user_id);
		
		try {
			// 자신 글 삭제
			count = boardDao.deleteBoard(board);
			
			// 자식글 삭제
			// boardDao.deleteChildBoard(board);
			
			// 댓글 삭제
			boardComment.setBoard_mgmt_no( board.getBoard_mgmt_no() );
			boardComment.setBoard_no( board.getBoard_no() );
			boardComment.setUpd_id( board.getUpd_id() );
			boardCommentList = boardDao.boardCommentList(boardComment);
			
			if ( boardCommentList.size() > 0 ) {
				for ( BoardComment boardCom : boardCommentList ) {
					boardComment.setComment_seq( boardCom.getComment_seq() );
					boardDao.deleteBoardComment(boardComment);
				}
			}
		} catch (Exception ex) {
			String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
			logger.error(methodName + " 예외발생 ==> " + ex.getMessage());
			ex.printStackTrace();
			throw ex;
		}
		
		return count;
	}
	
	@Override
	public int replyBoard(List<MultipartFile> fileList, Board board) throws Exception {
		FileMngUtil fileMng = new FileMngUtil();
		Board result = new Board();
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String boardNo = "";
		int check = 0;
		
		// 2. BOARD MAX BOARD_NO 조회
		boardNo = boardDao.getMaxBoardNo(board);
		
		// 3. 답글 LEVEL UPDATE
		boardDao.updateReplyLevel(board);
		
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
		check = boardDao.insertBoard(result);
		
		if(check > 0){
			for (MultipartFile file : fileList) {
				if(!file.isEmpty()){
					// 1. 파일 업로드 처리
					try {
						result = fileMng.uploadBoard(file, board);	
					}catch (Exception ex) {
						logger.error("File Upload error => " + ex.getMessage());
						ex.printStackTrace();
						throw ex;
					}
					check = boardDao.insertNoticeAttachFiles(result);
				}
			}
		}
		return check;
	}
	
	@Override
	public List<BoardComment> boardCommentList(BoardComment boardComment) throws Exception {
		return boardDao.boardCommentList(boardComment);
	}
	
	@Override
	public void saveBoardComment(BoardComment boardComment) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String commentSeq = "";
		// 1. MAX COMMENT SEQ 조회
		commentSeq = boardDao.getBoardCommentSeq(boardComment);
		
		boardComment.setComment_seq(commentSeq);
		boardComment.setReg_id(user_id);
		
		// 2. BOARD_COMMENT INSERT
		boardDao.insertBoardComment(boardComment);
	}
	
	@Override
	public void deleteBoardComment(BoardComment boardComment) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		boardComment.setUpd_id(user_id);
		
		// 1. BOARD_COMMENT DELETE
		boardDao.deleteBoardComment(boardComment);
	}

	@Override
	public List<Board> readNoticeFiles(Board board) {
		return boardDao.readNoticeFiles(board);
	}

	@Override
	public List<Board> boardList(Board board) throws Exception {
		board.setTotalCount(boardDao.getBoardListCount(board)); //리스트의 전체 행수를 PageUtil 에 세팅해줌.
		List<Board> resultList = null;
		List<Board> temp = null;
//		boardDao.getFileExtNmList(board);
		resultList = boardDao.boardList(board);
		for(int i = 0; i < resultList.size(); i++){
			board.setBoard_no(resultList.get(i).getBoard_no());
			temp = boardDao.readNoticeFiles(board);
			resultList.get(i).setTemp(temp);
		}
		
		return resultList;
	}
	
	@Override
	public List<LinkedHashMap<String, Object>> boardListByExcelDown(Board board) {
		List<LinkedHashMap<String, Object>> resultList = null;
//		List<Board> temp = null;
		resultList = boardDao.boardListByExcelDown(board);
		/*for(int i = 0; i < resultList.size(); i++){
			System.out.println(resultList.toString());
			board.setBoard_no(String.valueOf(resultList.get(i).get("BOARD_NO")));
			temp = boardDao.readNoticeFiles(board);
			System.out.println(temp.toString());
//			resultList.get(i).setTemp(temp);
			resultList.get(i).put("temp", temp);
		}*/

		return resultList;
	}
}