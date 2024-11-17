package com.snakes.snakes.Models;

import java.util.List;

public class Snake {
    public List<Point> body;
    public Point direction;

    public Snake() {
        body = new java.util.ArrayList<>();
        body.add(new Point(9, 9));
        direction = new Point(1, 0);
    }

    public void changeDirection(String newDirection)
    {
        switch (newDirection) {
            case "up":
                direction = new Point(0, -1);
                break;
            case "down":
                direction = new Point(0, 1);
                break;
            case "left":
                direction = new Point(-1, 0);
                break;
            case "right":
                direction = new Point(1, 0);
                break;
            default:
                break;
        }
    }

    public void update()
    {
        Point newHead = body.get(0).add(direction);
        body.add(0, newHead);
        body.remove(body.size() - 1);
    }

    public void grow()
    {
        Point tail = body.get(body.size() - 1);
        body.add(tail);
    }
}