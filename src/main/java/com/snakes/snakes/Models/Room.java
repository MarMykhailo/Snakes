package com.snakes.snakes.Models;

import java.util.ArrayList;
import java.util.List;

public class Room {
    public int roomId;
    public int maxPlayers;
    public String roomName;
    public List<Player> players;
    public boolean isStarted;
    public boolean isRunning;

    public Room() {
        this.roomId = 1;
        this.maxPlayers = 2;
        this.roomName = "Room";
        this.isStarted = false;
        this.isRunning = false;
        this.players = new ArrayList<>();
    }
}
