package de.uwe.verlet;

import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public class Stick {

    public Point p1, p2;
    public double length;

    public Stick(){

    }

    public Stick(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        this.length = Math.sqrt(dx*dx+dy*dy);
    }

    public void update(double width, double height) {

        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double distance = Math.sqrt(dx*dx+dy*dy);

        double diff = length - distance;
        double percent = (diff / length)/2;

        double offsetX = dx * percent;
        double offsetY = dy * percent;

        if(!p1.pinned) {
            p1.x -= offsetX;
            p1.y -= offsetY;
        }
        if(!p2.pinned) {
            p2.x += offsetX;
            p2.y += offsetY;
        }
    }

    public void render(GraphicsContext gc) {
        gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stick stick = (Stick) o;
        return Objects.equals(p1, stick.p1) && Objects.equals(p2, stick.p2) || Objects.equals(p2, stick.p1) && Objects.equals(p1, stick.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }
}
