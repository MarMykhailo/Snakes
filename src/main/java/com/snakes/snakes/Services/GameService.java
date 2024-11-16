package com.snakes.snakes.Services;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.snakes.snakes.Models.Snake;

public class GameService {
    
    //Player player;
    //Room room;
    Snake snake;
    public GameService() {
    }

    public void handleMassage(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        //визанчення типу повідомлення
        //зміна стану певної гри 
        //зміна напрямку руху певного гравця що належить певній кімнаті

        
    }

    public void sendJsonMessage(WebSocketSession session, Map<String, Object> messageData) {
       
    }

    public void changeGameState(WebSocketSession session, Map<String, Object> message) {
        
    }

    public void changeDirection(WebSocketSession session, Map<String, Object> message) {
        
    }
}
