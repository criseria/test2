package org.jay.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.log4j.Log4j;

@Log4j
public class ChattingHandler extends TextWebSocketHandler{
	
	private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

	// 채팅을 위해 해당 페이지에 들어오면 클라이언트가 연결된 후 해당 클라이언트의 세션을 sessionList에 add
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		log.info("#ChattingHandler, afterConnectionEstablished");
		sessionList.add(session);
		
		log.info(session.getPrincipal().getName() + "님이 입장하셨습니다.");
	}

	// 웹 소켓 서버로 메세지를 전송했을 때 이 메소드 호출. 현재 서버에 접속한 session 모두에게 메세지를 전달해야 하므로 for문 돌며 메세지 전송
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		log.info("#ChattingHandler, handleTextMessage");
		log.info(session.getId() + ": " + message);
		
		for(WebSocketSession s : sessionList) {
			s.sendMessage(new TextMessage(session.getPrincipal().getName() + ":" + message.getPayload()));
		}
	}

	// 클라이언트 연결이 끊어진 경우 remove로 해당 세션 제거
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		log.info("#ChattingHandler, afterConnectionClosed");
		
		sessionList.remove(session);
		
		log.info(session.getPrincipal().getName() + "님이 퇴장하셨습니다.");
	}
}
