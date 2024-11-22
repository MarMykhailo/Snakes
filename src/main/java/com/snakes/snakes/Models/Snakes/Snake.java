package com.snakes.snakes.Models.Snakes;

import java.util.List;

import com.snakes.snakes.Models.GameObject;
import com.snakes.snakes.Models.Point;

public class Snake implements GameObject {
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

    @Override
    public void update()
    {
        if(body.size()< 2) return;
        Point newHead = body.get(0).add(direction);
        body.add(0, newHead);
        body.remove(body.size() - 1);
    }

    @Override
    public java.util.Map<String, Object> draw()
    {
        java.util.Map<String, Object> json = new java.util.HashMap<>();
        List<int[]> coordinates = new java.util.ArrayList<>();
        for (Point point : body) {
            coordinates.add(new int[]{point.x, point.y});
        }
        json.put("x", coordinates.stream().map(coord -> coord[0]).toArray());
        json.put("y", coordinates.stream().map(coord -> coord[1]).toArray());
        json.put("color", "green");
        return json;
    }

    @Override
    public boolean intersects(Point cell)
    {
        return body.stream().anyMatch(point -> point.equals(cell));
    }
    
    public void grow()
    {
        Point tail = body.get(body.size() - 1);
        body.add(tail);
    }
}