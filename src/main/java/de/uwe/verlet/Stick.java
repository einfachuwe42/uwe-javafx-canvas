package de.uwe.verlet;

import de.uwe.renderer.VerletRenderer;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

public class Stick {

    public Point p1, p2;
    public final double length;
    public double currentLength;

    public Stick(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        this.length = Math.sqrt(dx*dx+dy*dy);
    }

    public void update(double width, double height) {

        if(p1.pinned && p2.pinned)
            return;

        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double distance = Math.sqrt(dx*dx+dy*dy);

        currentLength = distance;

        if(distance < 1){
            distance = 1;
        }
        if(distance > length*1.1){
            distance = length*1.1;
        }

        double diff = length - distance;

        double percent = 0;

        if(p1.pinned || p2.pinned){
            percent = (diff / length);
        }else{
            percent = (diff / length)/2;
        }

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
        if(VerletRenderer.info) {
            double dx = p2.x - p1.x;
            double dy = p2.y - p1.y;
            gc.fillText("" + currentLength, p1.x + dx / 2, p1.y + dy / 2 + 15);
        }
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
