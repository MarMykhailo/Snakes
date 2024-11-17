package com.snakes.snakes.Models;

public class Player {
    public String name;
    public Snake snake;
    public int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.snake = new Snake();
        snake.body.add(new Point(8, 9));
        snake.body.add(new Point(9, 9));
    }

}
