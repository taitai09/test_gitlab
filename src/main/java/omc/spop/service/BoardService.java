package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import omc.spop.model.Board;
import omc.spop.model.BoardComment;
import omc.spop.model.BoardManagement;

/***********************************************************
 * 2018.02.23	이원식	최초작성
 **********************************************************/

public interface BoardService {
	/** 게시판 설정관리 정보 조회 */
	BoardManagement getBoardManagement(BoardManagement boardManagement) throws Exception;	
	
	/** 게시판 리스트 */
	List<Board> boardList(Board board) throws Exception;

	/** 게시판 조회수 증가 */
	void updateBoardHitCnt(Board board) throws Exception;
	
	/** 게시판 정보 조회 */
	Board getBoard(Board board) throws Exception;

	/** 게시판 - 게시물 INSERT */
	int insertBoard(List<MultipartFile> fileList, Board board) throws Exception;
	
	/** 게시판 - 게시물 UPDATE */
	int updateBoard(List<MultipartFile> fileList, Board board) throws Exception;
	
	/** 게시판 - 게시물 DELETE */
	int deleteBoard(Board board) throws Exception;
	
	/** 게시판 - 게시물 답글 등록 */
	int replyBoard(List<MultipartFile> fileList, Board board) throws Exception;

	/** 게시판 댓글 정보 조회 */
	List<BoardComment> boardCommentList(BoardComment boardComment) throws Exception;
	
	/** 게시판 댓글 저장 */
	void saveBoardComment(BoardComment boardComment) throws Exception;
	
	/** 게시판 댓글 삭제 */
	void deleteBoardComment(BoardComment boardComment) throws Exception;

	/** 게시판 파일읽기*/
	List<Board> readNoticeFiles(Board board);

	List<LinkedHashMap<String, Object>> boardListByExcelDown(Board board);
}
