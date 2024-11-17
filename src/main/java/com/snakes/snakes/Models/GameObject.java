package com.snakes.snakes.Models;

    public interface GameObject {
        void update();
        void draw();
        boolean intersects(Point cell);
    }

