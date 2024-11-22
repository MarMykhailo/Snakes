package com.snakes.snakes.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.snakes.snakes.Services.RoomService;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/")
    public String menu() {
        return "menu.html";
    }

    @PostMapping("/create-room")
    public ResponseEntity<String> createRoom(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String type = payload.get("type");
        boolean isCreated = roomService.createRoom(name, type);
        if (isCreated) {
            return ResponseEntity.ok("Room created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create room");
        }
    }

    @DeleteMapping("/delete-room/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") Long id) {
        boolean isDeleted = roomService.deleteRoom(id);
        if (isDeleted) {
            return ResponseEntity.ok("Room deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete room");
        }
    }

    @GetMapping("/join-room/{playerId}/{id}")
    public ResponseEntity<String> addPlayerToRoom(@PathVariable("playerId") Long playerId, @PathVariable("id") Long id) {
        boolean isJoined = roomService.joinPlayer(playerId, id);
        if (isJoined) {
            return ResponseEntity.ok("Room joined successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to join room");
        }
    }

    @GetMapping("/leave-room/{playerId}/{id}")
    public ResponseEntity<String> leaveRoom(@PathVariable("playerId") Long playerId, @PathVariable("id") Long id) {
        boolean isLeft = roomService.leaveRoom(playerId, id);
        if (isLeft) {
            return ResponseEntity.ok("Room left successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to leave room");
        }
    }

    @GetMapping("/rooms")
    public ResponseEntity<Object> getRooms() {
        return ResponseEntity.ok(roomService.getRooms());
    }
}
