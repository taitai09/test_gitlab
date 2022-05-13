package omc.spop.service;

import java.util.List;

import omc.spop.model.NotUseHint;
import omc.spop.model.UiExceptDbUser;

/***********************************************************
 * 2018.09.03	임호경	최초작성
 **********************************************************/

public interface DBMngService {

	List<NotUseHint> getHintList(NotUseHint notUseHint);

	int deleteHint(NotUseHint notUseHint);

	int updateHint(NotUseHint notUseHint);

	int insertHint(NotUseHint notUseHint);

	int checkHint(NotUseHint notUseHint);

	String getDbAbbrNm(NotUseHint notUseHint);

	List<UiExceptDbUser> getUsernameList(UiExceptDbUser uiExceptDbUser);

	int deleteUsername(UiExceptDbUser uiExceptDbUser);

	int updateUsername(UiExceptDbUser uiExceptDbUser);

	int insertUsername(UiExceptDbUser uiExceptDbUser);

	int checkUsername(UiExceptDbUser uiExceptDbUser);

  
}
 