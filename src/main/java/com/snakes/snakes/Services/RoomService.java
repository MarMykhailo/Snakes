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
    public List<Room> rooms = new ArrayList<>();
    public List<Player> players = new ArrayList<>();

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

    public boolean joinPlayer(WebSocketSession session, String name, Long roomId) {
        Player player = players.stream()
                               .filter(p -> p.name.equals(name))
                               .findFirst()
                               .orElse(null);
        if(player != null) {
            player.session = session;
            Room room = rooms.stream()
                             .filter(r -> r.id.equals(roomId))
                             .findFirst()
                             .orElse(null);
            if(room != null) {
                System.out.println("Added player");
                //первірка на унікальність (чи моє таке імя)
                boolean isUnique = room.players.stream()
                                               .noneMatch(p -> p.name.equals(name));
                if (!isUnique) {
                    System.out.println("Player name is not unique in the room");
                    return true;
                }
                return room.addPlayer(player);
            }
            System.out.println("Room is null");
        } else {
            System.out.println("Player is null");
            Player newPlayer = new Player(name);
            players.add(newPlayer);
            newPlayer.session = session;
            Room room = rooms.stream()
                             .filter(r -> r.id.equals(roomId))
                             .findFirst()
                             .orElse(null);
            if(room != null) {
                System.out.println("Added player");
                return room.addPlayer(newPlayer);
            }
            return true;
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
        System.out.println("Received message (Room): " + messageData);
        switch (messageType) {
            case "attribution":
                handleAttribution(session, messageData);
                System.out.println("Attribution");
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
        String roomIds = (String) messageData.get("roomId");

        long roomId = Long.parseLong(roomIds);
        joinPlayer(session, name, roomId);
        //виводжу кімнати та равців що в них
        rooms.forEach(room -> {
            System.out.println("Room ID: " + room.id);
            room.players.forEach(player -> {
                System.out.println("Player: " + player.name);
            });
        });
        System.out.println("end print");
    }

    private void sendJsonMessage(WebSocketSession session, Map<String, Object> message) throws Exception {
        session.sendMessage(new TextMessage(message.toString()));
    }
}
