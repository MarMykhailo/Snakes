package com.snakes.snakes.Models;

import org.springframework.web.socket.WebSocketSession;

import com.snakes.snakes.Models.Snakes.Snake;

public class Player {
    public Long id;
    public String name;
    public Snake snake;
    public int score;
    public WebSocketSession session;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.snake = new Snake();
        snake.body.add(new Point(8, 9));
        snake.body.add(new Point(9, 9));
    }

    public Player(String name, Point point) {
        this.name = name;
        this.score = 0;
        this.snake = new Snake();
        snake.grow();
        snake.grow();
    }

}
