<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type="text/javascript">
	var ws;//一个ws对象就是一个通信管道。
	var target = 'ws://localhost:8080/WebSocket/echo/echoSocket';	//项目名+映射服务路径
	function testOpen(){
		if("WebSocket" in window){
			ws = new WebSocket(target);
		}else if("MozWebSocket" in window){	//火狐浏览器的WebSocket
			ws = new MozWebSocket(target);
		}else{
			alert("WebSocket is not supported by this browser !")
			return;
		}
	}
	function testSend(){
		if(ws != null){
			var msg = document.getElementById("msg").value;
			ws.send(msg);
			document.getElementById("msg").value = "";
		}else{
			alert("WebSocket connection not established，please connect。。。");
		}

		//相当于callback事件
		ws.onmessage = function(data){
			var msgDiv = document.getElementById("msgDiv");
			msgDiv.innerHTML += event.data + "\n";
			console.info(data);
		}
	}
</script>
</head>
<body>
	<p>webSocket连接步骤:</p>
	<code>
		1、开启连接，建立管道。<br>
		2、客户端给服务端发送数据。<br>
		3、服务端接收到数据。<br>
		4、服务端给客户端发送数据。<br>
		5、客户端接收数据。<br>
		6、监听三类基本事件：onopen、onmessage、onclose。
	</code>
	<p>实现WebSocket要点：</p>
	<code>
		1、实现ServerApplicationConfig 接口的方法。<br>
		2、使用getAnnotatedEndpointClasses 方法返回指定的类。<br>
		3、在指定的类上面要使用@ServerEndPoint 注解<br>
	</code>
	<br>
	<b>注意：jdk版本1.7+，tomcat版本7.0+，依赖于websocket-api.jar和tomcat-websocket.jar，jar包位置在tomcat/lib下。</b>
	<br>
	<hr>
	<ol>
		<li>
			<p>Echo Demo：</p>
			<button onclick="testOpen()">1、Test open</button>
			<input type="text" id="msg"><button onclick="testSend()">2、Test Send</button>
			<br>
			<textarea id="msgDiv" rows="10" cols="46" ></textarea>
			<br/><br/>
			<hr>
			<br/>
		</li>
		<li>
			<p>Chat Demo：需要打开多个页面请求，实现私聊或者广播效果。</p>
			<form action="LoginServlet" method="get">
				username：<input name="username">
				<button type="submit">登录</button>
			</form>
		</li>
	</ol>
	<hr>
	<h4>和dwr原理差不多：</h4>
</body>
</html>