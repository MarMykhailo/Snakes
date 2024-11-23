package com.snakes.snakes.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

import org.springframework.stereotype.Service;

import com.snakes.snakes.Models.Player;
import com.snakes.snakes.Models.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class RoomService {
    private List<Room> rooms = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    public boolean createRoom(String name, String type) {
        Room newRoom = new Room();
        newRoom.name = name;
        //newRoom.Type(type);
        rooms.add(newRoom);
        return true;
    }

    public boolean deleteRoom(Long id) {
        return rooms.removeIf(room -> room.id.equals(id));
    }

    public boolean joinPlayer(String name, Long roomId) {
        for (Room room : rooms) {
            if (room.id.equals(roomId)) {
                Player player = players.stream()
                                       .filter(p -> p.name.equals(name))
                                       .findFirst()
                                       .orElse(null);
                if (player != null) {
                    System.out.println("Player isn't null");
                    return room.addPlayer(player);
                } else {
                    System.out.println("Player is null");
                    Player newPlayer = new Player(name);
                    players.add(newPlayer);
                    return room.addPlayer(newPlayer);
                }
            }
        }
        return false;
    }

    public boolean leaveRoom(String nickname, Long roomId) {
        for (Room room : rooms) {
            if (room.id.equals(roomId)) {
                Player player = players.stream()
                                       .filter(p -> p.name.equals(nickname))
                                       .findFirst()
                                       .orElse(null);
                if (player != null) {
                    return room.removePlayer(player);
                }
            }
        }
        return false;
    }

    @GetMapping
    public ResponseEntity<List<Room>> getRooms() {
        return ResponseEntity.ok(rooms);
    }

    public void handleMassage(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        String messageType = (String) messageData.get("action");
        switch (messageType) {
            case "attribution":
                handleAttribution(session, messageData);
                break;
            default:
                Map<String, Object> errorMessage = new HashMap<>();
                errorMessage.put("type", "error");
                errorMessage.put("message", "Unknown message type: " + messageType);
                sendJsonMessage(session, errorMessage);
        }
    }

    private void handleAttribution(WebSocketSession session, Map<String, Object> messageData) throws Exception {
        String name = (String) messageData.get("name");
        Long roomId = (Long) messageData.get("roomId");
        joinPlayer(name, roomId);
    }

    private void sendJsonMessage(WebSocketSession session, Map<String, Object> message) throws Exception {
        session.sendMessage(new TextMessage(message.toString()));
    }
}
