package de.uwe.verlet;

import java.util.List;

public class VerletData {

    public List<Point> points;
    public List<Stick> sticks;

    public VerletData(){

    }

    public VerletData(List<Point> points, List<Stick> sticks) {
        this.points = points;
        this.sticks = sticks;
    }

    @Override
    public String toString() {
        return "VerletData{" +
                "points=" + points +
                ", sticks=" + sticks +
                '}';
    }
}
