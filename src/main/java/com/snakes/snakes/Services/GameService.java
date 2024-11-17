package com.snakes.snakes.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import com.snakes.snakes.Models.Point;
import com.snakes.snakes.Models.Snake;
import com.snakes.snakes.Models.Player;
import com.snakes.snakes.Models.Room;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameService {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    Player player;
    Room room;

    public GameService() {
        this.player = new Player("Player");
        this.room = new Room();
        room.players.add(player);
    }

    public void handleMassage(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        String messageType = (String) messageData.get("action");
        switch (messageType) {
            case "changeState":
                changeState(session, messageData);
                break;
            case "changeDirection":
                System.out.println("changeDirection");
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
        executorService.submit(() -> {
            try {
                String jsonMessage = new ObjectMapper().writeValueAsString(messageData);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void changeState(WebSocketSession session, Map<String, Object> messageData) {
        String state = (String) messageData.get("state");
        switch (state) {
            case "start":
                new Thread(() -> startGame(session)).start();
                break;
            case "pause":
                new Thread(() -> pauseGame(session)).start();
                break;
            case "resume":
                new Thread(() -> resumeGame(session)).start();
                break;
            default:
                Map<String, Object> errorMessage = new HashMap<>();
                errorMessage.put("type", "error");
                errorMessage.put("message", "Unknown state: " + state);
                sendJsonMessage(session, errorMessage);
        }
    }

    public void startGame(WebSocketSession session) {
        room.isStarted = true;
        room.isRunning = true;
        // початок гри //стан кімнати
        while (room.isStarted) {
            if (room.isRunning)
                update();
                draw(session);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseGame(WebSocketSession session) {
        room.isRunning = false;
    }

    public void resumeGame(WebSocketSession session) {
        room.isRunning = true;
    }

    public void changeDirection(WebSocketSession session, Map<String, Object> messageData) {
        String direction = (String) messageData.get("direction");
        room.players.get(0).snake.changeDirection(direction);
        // update();
        // draw(session);
    }

    public void update() {
        // оновлення позиції змійки
        room.players.get(0).snake.update();
    }

    public void draw(WebSocketSession session){
        // відправка нової позиції змійки
        try {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("type", "update");
            messageData.put("cells",  room.players.get(0).snake.body.stream().map(cell -> {
                Map<String, Object> cellData = new HashMap<>();
                cellData.put("x", cell.x);
                cellData.put("y", cell.y);
                cellData.put("color", "green");
                return cellData;
            }).collect(Collectors.toList()));
            sendJsonMessage(session, messageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
