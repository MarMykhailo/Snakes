package com.snakes.snakes.Models;

public class Point {
    public Integer x;
    public Integer y;

    public Point(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    public boolean equals(Point other) {
        return this.x.equals(other.x) && this.y.equals(other.y);
    }
}
