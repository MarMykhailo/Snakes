package com.snakes.snakes.Models;

import java.util.ArrayList;
import java.util.List;

import com.snakes.snakes.Models.Snakes.Apple;
import com.snakes.snakes.Models.Snakes.Wall;

public class Room {
    public Long id;
    public String name;
    public boolean isStarted;
    public boolean isRunning;
    public int maxPlayers;
    public List<Player> players;
    public List<GameObject> objects;

    public Room() {
        this.id = 1L;
        this.name = "Room";
        this.isStarted = false;
        this.isRunning = false;
        this.maxPlayers = 4;
        this.players = new ArrayList<>();
        this.objects = new ArrayList<>();

        objects.add(new Wall(new Point(3, 3)));
        objects.add(new Wall(new Point(4, 1)));
        objects.add(new Wall(new Point(0, 4)));
        objects.add(new Wall(new Point(10, 3)));

        objects.add(new Apple(new Point(5, 5)));
    }

    public void start() {
        isStarted = true;
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public boolean addPlayer(Player player) {
        players.add(player);
        return true;
    }

    public boolean removePlayer(Player player) {
        players.remove(player);
        return true;
    }

    public void update() {
        for (Player player : players) {
            player.snake.update();
            Point head = player.snake.body.get(0);

            for (int i = 1; i < player.snake.body.size(); i++) {
                if (head.equals(player.snake.body.get(i))) {
                    player.snake.body.clear();
                    break;
                }
            }

            for (Player otherPlayer : players) {
                if (otherPlayer != player) {
                    for (Point point : otherPlayer.snake.body) {
                        if (head.equals(point)) {
                            player.snake.body.clear();
                            break;
                        }
                    }
                }
            }

            for (GameObject object : objects) {
                if (object.intersects(head)) {
                    if (object instanceof Apple) {
                        objects.remove(object);
                        player.snake.grow();
                    } else if (object instanceof Wall) {
                        player.snake.body.clear();
                    }
                }
            }
        }
    }
}
