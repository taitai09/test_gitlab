package omc.spop.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import omc.spop.model.Board;
import omc.spop.model.BoardComment;
import omc.spop.model.BoardManagement;
import omc.spop.model.PerfGuide;

/***********************************************************
 * 2017.11.13	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업 
 **********************************************************/

public interface BoardMngService {
	/** 게시판 설정관리 정보 조회 */
	BoardManagement getBoardManagement(BoardManagement boardManagement) throws Exception;	

	/** 게시판 리스트 */
	List<Board> boardList(Board board) throws Exception;
	
	/** 게시판 정보 조회 */
	Board getBoard(Board board) throws Exception;

	/** 게시판 - 게시물 INSERT */
	void insertBoard(MultipartFile file, Board board) throws Exception;
	
	/** 게시판 - 게시물 UPDATE */
	void updateBoard(MultipartFile file, Board board) throws Exception;
	
	/** 게시판 - 게시물 DELETE */
	void deleteBoard(Board board) throws Exception;
	
	/** 게시판 - 게시물 답글 등록 */
	void replyBoard(MultipartFile file, Board board) throws Exception;

	/** 게시판 댓글 정보 조회 */
	List<BoardComment> boardCommentList(BoardComment boardComment) throws Exception;
	
	/** 게시판 댓글 저장 */
	void saveBoardComment(BoardComment boardComment) throws Exception;
	
	/** 게시판 댓글 삭제 */
	void deleteBoardComment(BoardComment boardComment) throws Exception;
	
	/** 새로시작 **/
	/** 공지사항 리스트 조회 */
	List<Board> noticeList(Board board);

	/** 공지사항 조회수 */
	int updateHitCnt(Board board);

	/** 공지사항 조회 */
	Board readNoticeContents(Board board);

	/** 공지사항 파일 조회 */
	List<Board> readNoticeFiles(Board board);

	/** 공지사항 등록 
	 * @throws Exception */
	int insertNotice(MultipartHttpServletRequest multipartRequest, Board board) throws Exception;
	
	/** 공지사항 수정 
	 * @throws Exception */
	int updateNotice(MultipartHttpServletRequest multipartRequest, Board board) throws Exception;

	/** 공지사항 삭제 */
	int deleteNotice(Board board);

	/** 공지사항 첨부파일 삭제 */
	int deleteAttachFile(Board board);


}
