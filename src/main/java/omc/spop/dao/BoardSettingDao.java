package omc.spop.dao;

import java.util.List;

import omc.spop.model.BoardManagement;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업(오픈메이드 관리자용)
 **********************************************************/

public interface BoardSettingDao {	
	public List<BoardManagement> boardSettingList(BoardManagement boardManagement);

	public String getMaxBoardMgmtNo(BoardManagement boardManagement);
	
	public void insertBoardSetting(BoardManagement boardManagement);
	
	public void updateBoardSetting(BoardManagement boardManagement);

	public void deleteBoardSetting(BoardManagement boardManagement);

	public List<BoardManagement> getBoardTypeCd(BoardManagement boardManagement);
}