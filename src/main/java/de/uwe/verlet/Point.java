package de.uwe.verlet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Point {

    public static int lastId = 0;

    public int id;
    public double x, y;
    public double x1, y1;
    public double bounce = 1;
    public double gravity = 0.05;
    public boolean pinned = false;
    public boolean marked = false;
    public transient Color color = Color.ORANGERED;

    public Point(){

    }

    public Point(double x, double y, double x1, double y1) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }

    public Point(Point point){
        this.id = point.id;
        this.x = point.x;
        this.y = point.y;
        this.x1 = point.x;
        this.y1 = point.y;
        bounce = point.bounce;
        gravity = point.gravity;
        pinned = point.pinned;
        color = point.color;
    }

    public static Point create(double x, double y, double x1, double y1){
        final Point point = new Point(x, y, x1, y1);
        point.id = ++lastId;
        return point;
    }

    public void update(double width, double height) {

        if(!pinned){
            double vx = x - x1;
            double vy = y - y1;

            x1 = x;
            y1 = y;
            x += vx;
            y += vy;
            y += gravity;

            if(y > height){
                y = height;
                y1 = y + vy * bounce;
            }else if(y < 0){
                y = 0;
                y1 = y + vy * bounce;
            }

            if(x > width){
                x = width;
                x1 = x + vx * bounce;
            }else if(x < 0){
                x = 0;
                x1 = x + vx * bounce;
            }
        }

    }

    public void render(GraphicsContext gc, boolean selected) {
        if(selected){
            gc.setFill(Color.YELLOW);
            gc.fillOval(x-4, y-4 ,8, 8);
        }
        gc.setFill(color);
        gc.fillOval(x-3, y-3 ,6, 6);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return id == point.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
