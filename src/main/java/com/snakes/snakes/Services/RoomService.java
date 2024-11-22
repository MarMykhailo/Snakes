package com.snakes.snakes.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

import org.springframework.stereotype.Service;

import com.snakes.snakes.Models.Player;
import com.snakes.snakes.Models.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class RoomService {
    private List<Room> rooms = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    public boolean createRoom(String name, String type) {
		Room newRoom = new Room();
        newRoom.name = name;
        //newRoom.type = type;
        rooms.add(newRoom);
        return true;
	}

    public boolean deleteRoom(Long id) {
        return rooms.removeIf(room -> room.id == id);
    }

    public boolean joinPlayer(String name , Long roomId) {
        for (Room room : rooms) {
            if (room.id == roomId) {
                Player player = players.stream()
                                       .filter(p -> p.name.equals(name))
                                       .findFirst()
                                       .orElse(null);
                if (player != null) {
                    System.out.println("Player isn't null");
                    return room.addPlayer(player);
                }else{
                    System.out.println("Player is null");
                    Player newPlayer = new Player(name);
                    return room.addPlayer(newPlayer);
                }
            }
        }
        return false;
    }

    public boolean leaveRoom(String nickname, Long roomId) {

        for (Room room : rooms) {
            if (room.id == roomId) {
                Player player = players.stream()
                                       .filter(p -> p.name == nickname)
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
        List<Room> roomDetailsList = new ArrayList<>();
        for (Room room : rooms) {
            roomDetailsList.add(room);
        }
        return ResponseEntity.ok(roomDetailsList);
    }
}
