package com.snakes.snakes.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import com.snakes.snakes.Models.Point;
import com.snakes.snakes.Models.Snake;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameService {
    
    //Player player;
    //Room room;
    Snake snake;
    public GameService() {
        snake = new Snake();
        System.out.println("Game service created");
        snake.body.add(new Point(9, 9));
    }

    public void handleMassage(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        String messageType = (String) messageData.get("action");
        switch (messageType) {
            case "changeGameState":
                changeGameState(session, messageData);
                break;
            case "changeDirection":
                changeDirection(session, messageData);
                break;
            default:
                Map<String, Object> errorMessage = new HashMap<>();
                errorMessage.put("type", "error");
                errorMessage.put("message", "Unknown message type: " + messageType);
                sendJsonMessage(session, errorMessage);
        }
    }

    public void sendJsonMessage(WebSocketSession session, Map<String, Object> messageData) {
       
    }

    public void changeGameState(WebSocketSession session, Map<String, Object> messageData) {
        
    }

    public void changeDirection(WebSocketSession session, Map<String, Object> messageData) {
        String direction = (String) messageData.get("direction");
        snake.changeDirection(direction);
        update();
        draw(session);
    }


    public void update() {
        // оновлення позиції змійки
        snake.update();
    }

    public void draw(WebSocketSession session){
        // відправка нової позиції змійки
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("type", "update");
            messageData.put("cells", snake.body.stream().map(cell -> {
                Map<String, Object> cellData = new HashMap<>();
                cellData.put("x", cell.x);
                cellData.put("y", cell.y);
                cellData.put("color", "green");
                return cellData;
            }).collect(Collectors.toList()));
            String jsonMessage = objectMapper.writeValueAsString(messageData);
            System.out.println("Sending message: " + jsonMessage);
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
