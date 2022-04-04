package de.uwe;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;

public interface Renderer {

    void render(GraphicsContext gc, double width, double height, double mouseX, double mouseY, long now);
    default EventHandler<InputEvent> getEventHandler() { return null; };
}
