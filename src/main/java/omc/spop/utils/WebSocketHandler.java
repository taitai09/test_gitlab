package omc.spop.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import omc.spop.dao.CommonDao;
import omc.spop.model.Users;
import omc.spop.model.WebSocket;

public class WebSocketHandler extends TextWebSocketHandler {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private CommonDao commonDao;
	
	/**
     * 서버에 연결한 사용자들을 저장하는 리스트
     */
    private List<WebSocketSession> connectedUsers;
    
    private List<WebSocket> loginUsers;
    
    public WebSocketHandler() {
        connectedUsers = new ArrayList<WebSocketSession>();
        loginUsers = new ArrayList<WebSocket>();
    }

    /**
     * 접속과 관련된 Event Method
     * 
     * @param WebSocketSession 접속한 사용자
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	Users users = new Users();
    	connectedUsers.add(session); 
        
    	logger.info(session.getPrincipal().getName() + " [" +session.getId() + "]님이 접속했습니다.");
    	
		commonDao = sqlSession.getMapper(CommonDao.class);
		users = commonDao.login(session.getPrincipal().getName());
		
		WebSocket webSocket = new WebSocket();
		webSocket.setSession_id(session.getId());
		webSocket.setUser_id(users.getUser_id());
		webSocket.setAuth_cd(users.getAuth_cd());
		webSocket.setWrkjob_cd(users.getWrkjob_cd());
		
		loginUsers.add(webSocket);
    }
    
    /**
     * 두 가지 이벤트를 처리
     * 
     * 1. Send : 클라이언트가 서버에게 메시지를 보냄
     * 2. Emit : 서버에 연결되어 있는 클라이언트에게 메시지를 보냄
     * 
     * @param WebSocketSession 메시지를 보낸 클라이언트
     * @param TextMessage 메시지의 내용
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	WebSocket webSocket = WebSocket.converWebSocket(message.getPayload());

        for (WebSocketSession webSocketSession : connectedUsers) {
            if (webSocket.getSend_type().equals("ALL")) { // 전체 메시지 전송
            	webSocketSession.sendMessage(new TextMessage(webSocket.toJSONObject().toJSONString()));
            }
        }

        if (webSocket.getSend_type().equals("USER")) { // 특정 사용자에게 메시지 전송
        	for(int i = 0 ; i < loginUsers.size() ; i++){
        		if (loginUsers.get(i).getUser_id().equals(webSocket.getSend_gubun())) {
        			connectedUsers.get(i).sendMessage(new TextMessage(webSocket.toJSONObject().toJSONString()));
                    break;
                }
        	}
        } else if (webSocket.getSend_type().equals("AUTH")) { // 특정 권한 전체에게 메시지 전송
        	for(int i = 0 ; i < loginUsers.size() ; i++){
        		if (loginUsers.get(i).getAuth_cd().equals(webSocket.getSend_gubun())) {
        			connectedUsers.get(i).sendMessage(new TextMessage(webSocket.toJSONObject().toJSONString()));
                    continue;
                }
        	}            	
        } else if (webSocket.getSend_type().equals("WRKJOB")) { // 특정 업무 전체에게 메시지 전송
        	for(int i = 0 ; i < loginUsers.size() ; i++){
        		if (loginUsers.get(i).getWrkjob_cd().equals(webSocket.getSend_gubun())) {
        			connectedUsers.get(i).sendMessage(new TextMessage(webSocket.toJSONObject().toJSONString()));
                    continue;
                }
        	}
        }
    }
    
    /**
     * 클라이언트가 서버와 연결을 끊었을때 실행되는 메소드
     * 
     * @param WebSocketSession 연결을 끊은 클라이언트
     * @param CloseStatus 연결 상태(확인 필요함)
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        connectedUsers.remove(session);

        for(int i = 0 ; i < loginUsers.size() ; i++){
        	if(session.getId().equals(loginUsers.get(i).getSession_id())){
        		loginUsers.remove(i);
        	}
        }
        
        super.afterConnectionClosed(session, status);
	}
}
