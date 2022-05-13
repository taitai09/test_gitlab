package omc.spop.dao;

import java.util.List;

import omc.spop.model.RecvNote;
import omc.spop.model.SendNote;
import omc.spop.model.Users;

/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface MessageDao {
	public List<SendNote> recvMessageList(SendNote sendNote);
	
	public List<SendNote> sendMessageList(SendNote sendNote);
	
	public int updateSendNote(SendNote sendNote);

	public int updateRecvNote(RecvNote recvNote);
	
	public SendNote getMessage(SendNote sendNote);
	
	public List<Users> recvMessageUserList(Users users);
	
	public int insertSendNote(SendNote sendNote);
	
	public int insertRecvNote(RecvNote recvNote);	
}
