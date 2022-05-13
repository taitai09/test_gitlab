package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.dao.DBMngDao;
import omc.spop.model.NotUseHint;
import omc.spop.model.UiExceptDbUser;
import omc.spop.service.DBMngService;

/***********************************************************
 * 2018.08.23 	임호경	 최초작성
 **********************************************************/

@Service("dbMngService")
public class DBMngServiceImpl implements DBMngService {
	@Autowired
	private DBMngDao dbMngDao;

	@Override
	public List<NotUseHint> getHintList(NotUseHint notUseHint) {
		return dbMngDao.getHintList(notUseHint);
	}

	@Override
	public int deleteHint(NotUseHint notUseHint) {
		return dbMngDao.deleteHint(notUseHint);
	}

	@Override
	public int updateHint(NotUseHint notUseHint) {
		return dbMngDao.updateHint(notUseHint);
	}

	@Override
	public int insertHint(NotUseHint notUseHint) {
		return dbMngDao.insertHint(notUseHint);
	}

	@Override
	public int checkHint(NotUseHint notUseHint) {
		return dbMngDao.checkHint(notUseHint);
	}

	@Override
	public String getDbAbbrNm(NotUseHint notUseHint) {
		return dbMngDao.getDbAbbrNm(notUseHint);
	}

	@Override
	public List<UiExceptDbUser> getUsernameList(UiExceptDbUser uiExceptDbUser) {
		return dbMngDao.getUsernameList(uiExceptDbUser);
	}


	@Override
	public int deleteUsername(UiExceptDbUser uiExceptDbUser) {
		return dbMngDao.deleteUsername(uiExceptDbUser);
	}

	@Override
	public int updateUsername(UiExceptDbUser uiExceptDbUser) {
		return dbMngDao.updateUsername(uiExceptDbUser);
	}

	@Override
	public int insertUsername(UiExceptDbUser uiExceptDbUser) {
		return dbMngDao.insertUsername(uiExceptDbUser);
	}

	@Override
	public int checkUsername(UiExceptDbUser uiExceptDbUser) {
		return dbMngDao.checkUsername(uiExceptDbUser);
	}

}
