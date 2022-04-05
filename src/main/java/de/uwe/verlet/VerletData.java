package de.uwe.verlet;

import java.util.List;

public class VerletData {

    public List<Point> points;

    public VerletData(){

    }

    public VerletData(List<Point> points, List<Stick> sticks) {
        this.points = points;

    }

}
