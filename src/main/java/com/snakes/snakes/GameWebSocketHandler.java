package com.snakes.snakes;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Map;

public class GameWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        // Відправляємо підтвердження підключення
        Map<String, Object> connectMessage = new HashMap<>();
        connectMessage.put("type", "connected");
        connectMessage.put("message", "Successfully connected to game server");
        connectMessage.put("sessionId", session.getId());
        sendJsonMessage(session, connectMessage);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // Парсимо вхідне повідомлення як JSON
            Map<String, Object> messageData = objectMapper.readValue(message.getPayload(), Map.class);
            String messageType = (String) messageData.get("type");

            // Обробляємо різні типи повідомлень
            System.out.println("Received message: " + message.getPayload());
            switch (messageType) {
                case "connect":
                    handleConnect(session, messageData);
                    break;
                case "move":
                    handleMove(session, messageData);
                    break;
                default:
                    Map<String, Object> errorMessage = new HashMap<>();
                    errorMessage.put("type", "error");
                    errorMessage.put("message", "Unknown message type: " + messageType);
                    sendJsonMessage(session, errorMessage);
            }
        } catch (Exception e) {
            Map<String, Object> errorMessage = new HashMap<>();
            errorMessage.put("type", "error");
            errorMessage.put("message", "Error processing message: " + e.getMessage());
            sendJsonMessage(session, errorMessage);
        }
    }

    private void handleConnect(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        Map<String, Object> welcomeMessage = new HashMap<>();
        welcomeMessage.put("type", "welcome");
        welcomeMessage.put("message", "Welcome to the game!");
        welcomeMessage.put("timestamp", System.currentTimeMillis());
        sendJsonMessage(session, welcomeMessage);
    }

    private void handleMove(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        String direction = (String) messageData.get("direction");
        // Тут буде ваша логіка обробки руху
        Map<String, Object> moveConfirmedMessage = new HashMap<>();
        moveConfirmedMessage.put("type", "moveConfirmed");
        moveConfirmedMessage.put("direction", direction);
        moveConfirmedMessage.put("timestamp", System.currentTimeMillis());
        sendJsonMessage(session, moveConfirmedMessage);
    }

    private void sendJsonMessage(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        String jsonMessage = objectMapper.writeValueAsString(messageData);
        session.sendMessage(new TextMessage(jsonMessage));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        System.out.println("WebSocket connection closed: " + session.getId());
    }
}
