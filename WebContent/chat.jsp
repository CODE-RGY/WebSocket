<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/chat.css" />
<script src="js/jquery-3.1.1.min.js"></script>
<script type="text/javascript">
	$(function() {
		//为发送按钮绑定事件
		$("#sendBtn").click(sendContent);
		/* 进入聊天室就打开WebSocket通道 */
		var ws; //一个ws对象就是一个通信管道。
		//WebSocket：项目名，chat/chatSocket是映射服务路径。
		var target = 'ws://localhost:8080/WebSocket/chat/chatSocket?username=${sessionScope.username}';
		openSocket(); //调用连接管道方法
		function openSocket() {
			if ("WebSocket" in window) {
				ws = new WebSocket(target);
			} else if ("MozWebSocket" in window) { //火狐浏览器的WebSocket
				ws = new MozWebSocket(target);
			} else {
				alert("WebSocket is not supported by this browser !")
				return;
			}
		}
		function sendContent() {
			var sendObj = null;
			var content = $("#content").val();
			var checkedUser = $("#userDiv :checked");
			if (checkedUser.length != 0) {
				var targetUser = checkedUser.val(); //获取选中用户的集合
				sendObj = {
					targetUser : targetUser,
					content : content,
					type : 1
				//如果是1私聊，如果是2就是广播
				}
			} else {
				sendObj = {
					content : content,
					type : 2
				//如果是1私聊，如果是2就是广播
				}
			}
			if (ws != null) {
				var sendStr = JSON.stringify(sendObj);
				ws.send(sendStr); //提交内容
				$("#content").val("");
			} else {
				alert("WebSocket connection not established，please connect。。。");
			}
		}
		ws.onmessage = function(data) {
			eval("var msg = " + event.data);
			$("#userDiv").html("");
			if (undefined != msg.notice) {
				$("#msgDiv").append(msg.notice + "<br><br>");
			}
			if (undefined != msg.usernameList) {
				for (username in msg.usernameList) {
					$("#userDiv").append(
							"<input type='checkbox' value='"+ msg.usernameList[username] +"' />"
									+ msg.usernameList[username] + "<br>");
				}
			}
			if (undefined != msg.content) {
				$("#msgDiv").append(msg.content + "<br><br>");
			}
		}
	})
</script>
</head>
<body>
	<p>Chat Page</p>
	<fieldset>
		<legend>聊天室</legend>
		<div id="msgDiv"></div>
		<div id="userDiv"></div>
		<div id="sendDiv">
			<input type="text" id="content" name="content" />
			<button id="sendBtn">发送</button>
		</div>
	</fieldset>
	<br>
	<br>
	<hr>
	<h4>因为48行，$("#userDiv :checked")只能获取选中的第一个，所以只能给一个人私聊。</h4>
</body>
</html>