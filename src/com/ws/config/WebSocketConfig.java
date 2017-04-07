package com.ws.config;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

public class WebSocketConfig implements ServerApplicationConfig {

	/**
	 * 注解方式的启动
	 */
	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		System.out.println("总共有" + scanned.size()+"个ServerEndpoint");
		
		//过滤掉所有其他，以避免运行时出现问题; 如果使用所有带注释的端点，请删除此类。
		Set<Class<?>> result = new HashSet<>();
		for (Class<?> clazz : scanned) {  
            if (clazz.getPackage().getName().startsWith("com.ws")) {  
                result.add(clazz);  
            }  
        }  
		//返回,该语句起到过滤的作用，返回指定的
		return result;
	}

	/**
	 * 接口方式的启动
	 */
	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> scanned) {

        return null;  
	}

}
