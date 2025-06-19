package com.uam.agendave.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Broker simple para mensajes salientes
        config.setApplicationDestinationPrefixes("/app"); // Prefijo para mensajes entrantes
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket puro (para brokerURL) → endpoint separado
        registry
                .addEndpoint("/ws-pure")
                .setAllowedOrigins("https://registifyfront.fladadrome.xyz", "https://registify.fladadrome.xyz")


        // SockJS (para fallback) → endpoint bajo /api/ws
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }

}
