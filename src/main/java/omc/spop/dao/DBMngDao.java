package omc.spop.dao;

import java.util.List;

import omc.spop.model.NotUseHint;
import omc.spop.model.UiExceptDbUser;

/***********************************************************
 * 2018.09.03	임호경	최초작성
 **********************************************************/

public interface DBMngDao {

	public List<NotUseHint> getHintList(NotUseHint notUseHint);

	public int deleteHint(NotUseHint notUseHint);

	public int updateHint(NotUseHint notUseHint); 
	
	public int insertHint(NotUseHint notUseHint);

	public int checkHint(NotUseHint notUseHint);

	public String getDbAbbrNm(NotUseHint notUseHint);


	public int saveUsername(UiExceptDbUser uiExceptDbUser);

	public int deleteUsername(UiExceptDbUser uiExceptDbUser);

	public List<UiExceptDbUser> getUsernameList(UiExceptDbUser uiExceptDbUser);

	public int insertUsername(UiExceptDbUser uiExceptDbUser);

	public int checkUsername(UiExceptDbUser uiExceptDbUser);

	public int updateUsername(UiExceptDbUser uiExceptDbUser);

}
