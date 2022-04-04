package de.uwe.renderer;

import de.uwe.Renderer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BestRenderer implements Renderer {

    private final List<Item> items = new ArrayList<>();

    public BestRenderer(){
        final Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            final double x = 600 * rand.nextDouble();
            final double y = 400 * rand.nextDouble();
            items.add(new Item(x, y));
        }
    }

    @Override
    public void render(GraphicsContext gc, double width, double height, double mouseX, double mouseY, long now) {
        gc.setFill(Color.ORANGERED);
        for (Item item : items) {
            item.render(gc);
        }
    }

    private static class Item {
        public double size;
        public double x, y;
        public double time = 0;

        public Item(double x, double y) {
            this.x = x;
            this.y = y;
            this.time = Math.PI * new Random().nextDouble();
        }

        public void render(GraphicsContext gc) {
            time += 0.0166;
            size = 30 * Math.abs(Math.sin(Math.PI*time));
            gc.fillOval(x-size/2, y-size/2, size, size);
        }
    }
}
