package com.ws.entity;

import java.util.List;

import com.google.gson.Gson;

public class Message {

	private String notice;
	
	private String content;
	
	private List<String> usernameList;

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		Message.gson = gson;
	}

	public List<String> getUsernameList() {
		return usernameList;
	}

	public void setUsernameList(List<String> usernameList) {
		this.usernameList = usernameList;
	}

	
	
	@Override
	public String toString() {
		return "Message [notice=" + notice + ", content=" + content + ", usernameList=" + usernameList + "]";
	}



	private static Gson gson = new Gson();
	
	/**
	 * 将Message 转换为json
	 * @return
	 */
	public String toJson(){
		return gson.toJson(this);
	}
	
}
