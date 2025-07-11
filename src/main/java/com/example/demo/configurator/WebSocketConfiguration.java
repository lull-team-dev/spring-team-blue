package com.example.demo.configurator;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	/**
	 * STOMPを使ったWebSocketのやり取りを開始するエンドポイントを設定。
	 * ここではURL"/chat"を設定し、SockJSを使ってクライアント処理される前提で設定を行う。
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chat").withSockJS();
	}

	/**
	 * 宛先ごとの処理を定義する。
	 * ここでは
	 * URL"/receive"から始まるURLをクライアント側で受信する。
	 * URL"/send"から始まるURLをコントローラ側で処理する。（その際、"/send"はURLから除去される）
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/send")
				.enableSimpleBroker("/receive");
	}

}
