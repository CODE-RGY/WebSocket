package com.ws.echo;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo/echoSocket")
public class EchoSocket {

	public EchoSocket() {
		System.err.println("==============================================================");
		System.out.println("EchoSocket contructor....");
	}

	//连接管道
	@OnOpen
	public void open(Session session){
		//一个session 代表一个通信会话
		
		System.out.println("Session ID：" + session.getId());
	}
	
	//返回消息
	@OnMessage
	public void message(Session session, String msg, boolean last){
		
		try {
			if(session.isOpen()){
				System.err.println("msg：" + msg);
				session.getBasicRemote().sendText("服务端：" + msg, last );
			}
		} catch (IOException e) {
			try{
				session.close();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	//管道关闭
	@OnClose
	public void close(Session session){
		System.out.println("session ID：" + session.getId() + " closed");
		System.err.println("==============================================================");
	}
	
	
}
