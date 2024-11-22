package com.snakes.snakes.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import com.snakes.snakes.Models.Point;
import com.snakes.snakes.Models.Snakes.Apple;
import com.snakes.snakes.Models.Snakes.Wall;
import com.snakes.snakes.Models.GameObject;
import com.snakes.snakes.Models.Player;
import com.snakes.snakes.Models.Room;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class GameService {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public List<Room> rooms;

    public GameService() {
        rooms = new ArrayList<>();
        Room room = new Room();
        rooms.add(room);
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

    public void handleMassage(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        String messageType = (String) messageData.get("action");
        switch (messageType) {
            case "changeState":
                changeState(session, messageData);
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
        rooms.get(0).isStarted = true;
        rooms.get(0).isRunning = true;
        while (rooms.get(0).isStarted) {
            if (rooms.get(0).isRunning)
                update();
                draw(session);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseGame(WebSocketSession session) {
        rooms.get(0).isRunning = false;
    }

    public void resumeGame(WebSocketSession session) {
        rooms.get(0).isRunning = true;
    }

    //це має бути для змійок або для кімнати?
    public void changeDirection(WebSocketSession session, Map<String, Object> messageData) {
        String direction = (String) messageData.get("direction");
        Player player = rooms.get(0).players.stream()
            .filter(p -> p.session.equals(session))
            .findFirst()
            .orElse(null);
        if (player != null) {
            player.snake.changeDirection(direction);
        }
    }

    public void update() {
        // оновлення позиції змійки
        for (Player player : rooms.get(0).players) {
            player.snake.update();
            // перевірка на зіткнення
            // беру позицію голови змійки і перевіряю чи вона зіткнулася з чимось
            // перевірка зіткнення з самою собою - смерть
            if(player.snake.body.size() == 0) {
                continue;
            }
            Point head = player.snake.body.get(0);

            // Check collision with itself
            for (int i = 1; i < player.snake.body.size(); i++) {
                if (head.equals(player.snake.body.get(i))) {
                    player.snake.body.clear();
                    break;
                }
            }

            // Check collision with other players' snakes
            for (Player otherPlayer : rooms.get(0).players) {
                if (otherPlayer != player) {
                    for (Point point : otherPlayer.snake.body) {
                        if (head.equals(point)) {
                            player.snake.body.clear();
                            break;
                        }
                    }
                }
            }

            // перевірка зіткнень з обєктами на карті
            for (GameObject object : rooms.get(0).objects) {
                if (object.intersects(head)) {
                    if(object instanceof Apple) {
                        // Remove the apple
                        rooms.get(0).objects.remove(object);
                        player.snake.grow();
                    }
                    else if(object instanceof Wall) {
                        player.snake.body.clear();
                    }
                    /*else if(objects instanceof Bomb) {
                        player.snake.body.clear();
                    }
                    else if(objects instanceof Portal) {
                        player.snake.body.clear();
                    }*/
                    break;
                }
            }
        }
    }

    public void draw(WebSocketSession session) {
        try {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("type", "update");

            List<Map<String, Object>> cells = new ArrayList<>();

            cells.addAll(rooms.get(0).players.stream()
                .flatMap(player -> player.snake.body.stream()
                    .map(cell -> {
                        Map<String, Object> cellData = new HashMap<>();
                        cellData.put("x", cell.x);
                        cellData.put("y", cell.y);
                        cellData.put("color", "green");
                        return cellData;
                    }))
                .collect(Collectors.toList()));

            cells.addAll(rooms.get(0).objects.stream()
                .map(GameObject::draw)
                .collect(Collectors.toList()));

            messageData.put("cells", cells);

            for (Player player : rooms.get(0).players) {
                sendJsonMessage(player.session, messageData);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(WebSocketSession session) {
        Player player = new Player("Player", new Point((int) (Math.random() * 10) + 1, (int) (Math.random() * 10) + 1));
        player.session = session;
        rooms.get(0).players.add(player);
    }

    public void removePlayer(WebSocketSession session) {
        rooms.get(0).players.removeIf(player -> player.session.equals(session));
    }

    public boolean createRoom() {
        Room newRoom = new Room();
        rooms.add(newRoom);
        return newRoom != null;
    }
}
