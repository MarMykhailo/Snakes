package com.snakes.snakes.Models;

import java.util.Map;

public interface GameObject {
    void update();
    Map<String, Object> draw();
    boolean intersects(Point cell);
}

