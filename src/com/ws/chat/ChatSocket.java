package com.ws.chat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.gson.utils.GsonUtils;
import com.ws.entity.Message;
import com.ws.entity.SendMsg;

@ServerEndpoint("/chat/chatSocket")
public class ChatSocket {
	
	/* 用户名 */
	private String username;
	/* WebSocket的session */
	private static List<Session> sessionList = new ArrayList<Session>();
	/* 登录的用户名列表 */
	private static List<String> usernameList = new ArrayList<String>();
	/* 用于存放用户名和该用户相对应的session */
	private static Map<String, Session> map = new HashMap<String, Session>();
	
	public ChatSocket() {
		System.out.println("ChatSocket constructor......");
	}
	
	/**
	 * 
	 * @param session
	 * 			当前的WebSocket的session对象，不是servlet的session。
	 * @throws UnsupportedEncodingException 
	 */
	@OnOpen
	public void open(Session session) throws UnsupportedEncodingException{
		System.out.println("WebSocket Opened......");
	
		String queryStr = session.getQueryString();
		//解码
		String decodeStr = URLDecoder.decode(queryStr, "utf-8");
		
		username = decodeStr.split("=")[1];
		
		System.out.println("获取用户名：" + username);
		
		//每次有人登录就将该用户名添加到用户列表中
		this.usernameList.add(username);
		this.sessionList.add(session);
		this.map.put(this.username, session);
		
		String notice = "欢迎 " + this.username + " 进入聊天室！";
		
		Message msg = new Message();
		msg.setNotice(notice);
		msg.setUsernameList(usernameList);
		
		//调用Message 实体类中的toJson方法，将Message对象转换为json字符串
		this.broadcast(this.sessionList, msg.toJson());
	}

	
	
	@OnMessage
	public void message(Session session, String sendStr) throws IOException{
		//获取当前的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		//将从前端接收到的json字符串转换成SendMsg对象
		SendMsg sendMsg = GsonUtils.gson.fromJson(sendStr, SendMsg.class);
		//获取内容并拼接返回字符串
		String responseStr = "";
		
		if(session.isOpen()){
			//依据type的值，判断是否私聊，1位私聊，2为广播
			if(sendMsg.getType() == 1){
				//根据接收到的信息，根据username找到相对应的session对象，实现私聊。
				String targetUser = sendMsg.getTargetUser();
				Session targetSession = this.map.get(targetUser);
				
				responseStr = username + " " + sdf.format(c.getTime()) + "<br><font color=red>" + sendMsg.getContent() + "</font>";
				
				Message msg = new Message();
				msg.setContent(responseStr);
				msg.setUsernameList(usernameList);
				//向指定session返回内容
				targetSession.getBasicRemote().sendText(msg.toJson());
				session.getBasicRemote().sendText(msg.toJson());
			}else{
				responseStr = username + " " + sdf.format(c.getTime()) + "<br>" + sendMsg.getContent();
				Message msg = new Message();
				msg.setContent(responseStr);
				msg.setUsernameList(usernameList);
				
				//调用Message 实体类中的toJson方法，将Message对象转换为json字符串
				this.broadcast(sessionList, msg.toJson());
			}
		}
	}
	
	
	@OnClose
	public void close(Session session){
		this.sessionList.remove(session);
		this.usernameList.remove(this.username);
		
		String notice = "通知：" + this.username + " 已离开当前聊天室！";
		Message msg = new Message();
		msg.setNotice(notice);
		msg.setUsernameList(usernameList);
		
		//调用Message 实体类中的toJson方法，将Message对象转换为json字符串
		this.broadcast(sessionList, msg.toJson());
	}
	
	
	/**
	 * 向浏览器端返回数据
	 * @param sessionList
	 * @param msg
	 * @param last
	 */
	public void broadcast(List<Session> sessionList, String msg) {
		
		for(Iterator<Session> iterator = sessionList.iterator(); iterator.hasNext();){
			Session session = iterator.next();
			try {
				if(session.isOpen()){
					System.err.println(session.getId() + "：" + msg);
					session.getBasicRemote().sendText(msg);
				}
			} catch (IOException e) {
				try{
					session.close();
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
