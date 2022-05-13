package omc.spop.service;

import java.util.List;

import omc.spop.model.BoardManagement;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업 (오픈메이드 관리자용) 
 **********************************************************/

public interface BoardSettingService {
	/** 게시판 설정관리 리스트 */
	List<BoardManagement> boardSettingList(BoardManagement boardManagement) throws Exception;

	/** 게시판 설정관리 저장 */
	void saveBoardSetting(BoardManagement boardManagement) throws Exception;

	/** 게시판 설정관리 저장 */
	void deleteBoardSetting(BoardManagement boardManagement);

	List<BoardManagement> getBoardTypeCd(BoardManagement boardManagement);
}
