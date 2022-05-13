package omc.spop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.MessageDao;
import omc.spop.model.RecvNote;
import omc.spop.model.SendNote;
import omc.spop.model.Users;
import omc.spop.service.MessageService;
import omc.spop.utils.DateUtil;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.03.27	이원식	OPENPOP V2 최초작업
 **********************************************************/

@Service("MessageService")
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageDao messageDao;

	@Override
	public List<SendNote> recvMessageList(SendNote sendNote) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sendNote.setRecv_user_id(user_id);
		
		return messageDao.recvMessageList(sendNote);
	}
	
	@Override
	public List<SendNote> sendMessageList(SendNote sendNote) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		sendNote.setSend_user_id(user_id);
		
		return messageDao.sendMessageList(sendNote);
	}	
	
	@Override
	public SendNote getMessage(SendNote sendNote) throws Exception {
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		SendNote temp = new SendNote();
		RecvNote recvNote = new RecvNote();

		// 1. RECV_NOTE READ_YN(읽음) UPDATE
		recvNote.setSend_dt(sendNote.getSend_dt());
		recvNote.setSend_user_id(sendNote.getSend_user_id());
		recvNote.setRecv_user_id(sendNote.getRecv_user_id());
		
		if(sendNote.getSend_yn().equals("N")){		
			recvNote.setRead_yn("Y");
		}else{
			recvNote.setRead_yn("N");
		}
		
		messageDao.updateRecvNote(recvNote);

		sendNote.setCheck_user_id(user_id);
		
		// 2. SELECT SEND_NOTE;
		temp = messageDao.getMessage(sendNote);
		
		return temp;
	}	
	
	@Override
	public int deleteMessage(SendNote sendNote) throws Exception {
		int rowCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		RecvNote recvNote = new RecvNote();
		
		if(sendNote.getSend_yn().equals("Y")){ // 보낸 쪽지 삭제.
			rowCnt = messageDao.updateSendNote(sendNote);
		}else{ // 받은 쪽지 삭제.
			recvNote.setSend_user_id(sendNote.getSend_user_id());
			recvNote.setSend_dt(sendNote.getSend_dt());
			recvNote.setRecv_user_id(user_id);
			recvNote.setDel_yn("Y");
			
			rowCnt = messageDao.updateRecvNote(recvNote);
		}
		
		
		return rowCnt;
	}
	
	@Override
	public List<Users> recvMessageUserList(Users users) throws Exception {
		return messageDao.recvMessageUserList(users);
	}	
	
	@Override
	public int sendMessage(SendNote sendNote) throws Exception {
		int rowCnt = 0;
		String user_id = SessionManager.getLoginSession().getUsers().getUser_id();
		String sendDt = DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss");
		RecvNote recvNote;
		
		// 1. SEND_NOTE INSERT
		sendNote.setSend_user_id(user_id);
		sendNote.setSend_dt(sendDt);
		messageDao.insertSendNote(sendNote);
		
		// 2. RECF_NOTE INSERT
		String[] recvUserArry = StringUtil.split(sendNote.getRecvUserArry(), ";");
		
		if(recvUserArry.length > 0){
			for (int i = 0; i < recvUserArry.length; i++) {
				recvNote = new RecvNote();
				recvNote.setSend_user_id(user_id);
				recvNote.setSend_dt(sendDt);
				recvNote.setRecv_user_id(recvUserArry[i]);
				
				rowCnt = messageDao.insertRecvNote(recvNote);
			}
		}
		
		return rowCnt;
	}
}