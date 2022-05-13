package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.BoardSettingDao;
import omc.spop.model.BoardManagement;
import omc.spop.service.BoardSettingService;

/***********************************************************
 * 2018.03.08	이원식	OPENPOP V2 최초작업 (오픈메이드 관리자용) 
 **********************************************************/

@Service("BoardSettingService")
public class BoardSettingServiceImpl implements BoardSettingService {
	@Autowired
	private BoardSettingDao boardSettingDao;

	@Override
	public List<BoardManagement> boardSettingList(BoardManagement boardManagement) throws Exception {
		return boardSettingDao.boardSettingList(boardManagement);
	}

	@Override
	public void saveBoardSetting(BoardManagement boardManagement) throws Exception {
		String boardMgmtNo = "";
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		boardManagement.setUpd_id(user_id);
		boardManagement.setReg_id(user_id);
		
		if(boardManagement.getBoard_mgmt_no() != null && !boardManagement.getBoard_mgmt_no().equals("")){//update
			boardSettingDao.updateBoardSetting(boardManagement);
		}else{//null일경우 insert
//			boardMgmtNo = boardSettingDao.getMaxBoardMgmtNo(boardManagement);
//			boardManagement.setBoard_mgmt_no(boardMgmtNo);
			boardSettingDao.insertBoardSetting(boardManagement);	
		}
	}

	@Override
	public void deleteBoardSetting(BoardManagement boardManagement) {
		 boardSettingDao.deleteBoardSetting(boardManagement);
		
	}

	@Override
	public List<BoardManagement> getBoardTypeCd(BoardManagement boardManagement) {
		return boardSettingDao.getBoardTypeCd(boardManagement);

	}
}