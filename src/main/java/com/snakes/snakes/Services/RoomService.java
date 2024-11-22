package com.snakes.snakes.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.snakes.snakes.Models.Room;

@Service
public class RoomService {
    List<Room> rooms = new ArrayList<>();

    public boolean leaveRoom(Long playerId, Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'leaveRoom'");
    }

    public Object getRooms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRooms'");
    }

    public boolean joinRoom(Long playerId, Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'joinRoom'");
    }

    public boolean deleteRoom(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRoom'");
    }

    public boolean createRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createRoom'");
    }
    
}
