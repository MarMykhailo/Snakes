package com.snakes.snakes.Models;

import java.util.ArrayList;
import java.util.List;

public class Room {
    public int roomId;
    public int maxPlayers;
    public String roomName;
    public List<Player> players;
    public List<GameObject> objects;
    public boolean isStarted;
    public boolean isRunning;

    public Room() {
        this.roomId = 1;
        this.maxPlayers = 4;
        this.roomName = "Room";
        this.isStarted = false;
        this.isRunning = false;
        this.players = new ArrayList<>();
        this.objects = new ArrayList<>();

        objects.add(new Wall(new Point(3, 3)));
        objects.add(new Wall(new Point(4, 1)));
        objects.add(new Wall(new Point(0, 4)));
        objects.add(new Wall(new Point(10, 3)));

        objects.add(new Apple(new Point(5, 5)));
    }
}
