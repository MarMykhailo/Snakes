package com.snakes.snakes.Models.Snakes;

import java.util.HashMap;
import java.util.Map;

import com.snakes.snakes.Models.GameObject;
import com.snakes.snakes.Models.Point;

public class Apple implements GameObject {
    public Point position;

    public Apple(Point position) {
        this.position = position;
    }

    @Override
    public void update() {
    }

    @Override
    public Map<String, Object> draw() {
        Map<String, Object> json = new HashMap<>();
        json.put("x", position.x);
        json.put("y", position.y);
        json.put("color", "red");
        return json;
    }

    @Override
    public boolean intersects(Point cell) {
        return position.equals(cell);
    }
}