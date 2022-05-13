package omc.spop.dao;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.Board;
import omc.spop.model.BoardComment;
import omc.spop.model.BoardManagement;

/***********************************************************
 * 2018.02.23	이원식	최초작성
 **********************************************************/

public interface BoardDao {	
	public BoardManagement getBoardManagement(BoardManagement boardManagement);

	public int getBoardListCount(Board board);
	
	public List<Board> boardList(Board board);
	
	public void updateBoardHitCnt(Board board);
	
	public Board getBoard(Board board);
	
	public String getMaxBoardNo(Board board);
	
	public int insertBoard(Board board);
	
	public int updateBoard(Board board);
	
	public int deleteBoard(Board board);
	
	public int deleteChildBoard(Board board);
	
	public void updateReplyLevel(Board board);
	
	public List<BoardComment> boardCommentList(BoardComment boardComment);
	
	public String getBoardCommentSeq(BoardComment boardComment);
	
	public void insertBoardComment(BoardComment boardComment);
	
	public void deleteBoardComment(BoardComment boardComment);

	public List<Board> readNoticeFiles(Board board);

	public int insertNoticeAttachFiles(Board result);

	public List<Board> getTempBoard(Board board);

	public List<LinkedHashMap<String, Object>> boardListByExcelDown(Board board);

	public int updateNoticeAttachFiles(Board result);

}