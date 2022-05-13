package omc.spop.dao;

import java.util.List;

import omc.spop.model.Board;
import omc.spop.model.BoardComment;
import omc.spop.model.BoardManagement;

/***********************************************************
 * 2017.11.13	이원식	최초작성
 * 2018.03.08	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface BoardMngDao {	
	public List<BoardManagement> mngSettingList(BoardManagement boardManagement);
	
	public BoardManagement getBoardManagement(BoardManagement boardManagement);
	
	public String getMaxBoardMgmtNo(BoardManagement boardManagement);
	
	public void insertMngSetting(BoardManagement boardManagement);
	
	public void updateMngSetting(BoardManagement boardManagement);
	
	public int getBoardListCount(Board board);
	
	public List<Board> boardList(Board board);
	
	public Board getBoard(Board board);
	
	public String getMaxBoardNo(Board board);
	
	public void insertBoard(Board board);
	
	public void updateBoard(Board board);
	
	public void deleteBoard(Board board);
	
	public void deleteChildBoard(Board board);
	
	public void updateReplyLevel(Board board);
	
	public List<BoardComment> boardCommentList(BoardComment boardComment);
	
	public String getBoardCommentSeq(BoardComment boardComment);
	
	public void insertBoardComment(BoardComment boardComment);
	
	public void deleteBoardComment(BoardComment boardComment);

	public List<Board> noticeList(Board board);

	public int updateHitCnt(Board board);

	public Board readNoticeContents(Board board);

	public List<Board> readNoticeFiles(Board board);

	public String getMaxPerfGuideNo(Board board);

	public int insertNotice(Board board);

	public String getMaxNoticeFileSeq(Board board);

	public int insertNoticeAttachFiles(Board board);

	public int updateNotice(Board board);

	public int deleteNotice(Board board);

	public int deleteAttachFile(Board board);
}