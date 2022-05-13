package omc.spop.service;

import java.util.List;

import omc.spop.model.SendNote;
import omc.spop.model.Users;

/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/

public interface MessageService {
	/** 전체 받은 쪽지 리스트 */
	List<SendNote> recvMessageList(SendNote sendNote) throws Exception;
	
	/** 전체 보낸 쪽지 리스트 */
	List<SendNote> sendMessageList(SendNote sendNote) throws Exception;	
	
	/** 쪽지 읽기 */
	SendNote getMessage(SendNote sendNote) throws Exception;
	
	/** 쪽지 삭제 */
	int deleteMessage(SendNote sendNote) throws Exception;	
	
	/** 쪽지 받을 사용자 리스트 */
	List<Users> recvMessageUserList(Users users) throws Exception;
	
	/** 쪽지 보내기 */
	int sendMessage(SendNote sendNote) throws Exception;
}
