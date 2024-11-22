package com.snakes.snakes.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.snakes.snakes.Services.RoomService;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/")
    public String menu() {
        return "menu.html";
    }

    @GetMapping("/create-room")
    public ResponseEntity<String> createRoom() {
        boolean isCreated = roomService.createRoom();
        if (isCreated) {
            return ResponseEntity.ok("Room created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create room");
        }
    }

    @GetMapping("/delete-room/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable("id") Long id) {
        boolean isDeleted = roomService.deleteRoom(id);
        if (isDeleted) {
            return ResponseEntity.ok("Room deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete room");
        }
    }

    @GetMapping("/join-room/{playerId}/{id}")
    public ResponseEntity<String> joinRoom(@PathVariable("playerId") Long playerId, @PathVariable("id") Long id) {
        boolean isJoined = roomService.joinRoom(playerId, id);
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
