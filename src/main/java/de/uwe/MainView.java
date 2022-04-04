package de.uwe;

import de.uwe.renderer.BestRenderer;
import de.uwe.renderer.VerletRenderer;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.Objects;

@ApplicationScoped
public class MainView {

    public static String darkStyle = Objects.requireNonNull(MainView.class.getClassLoader().getResource("css/modena_dark.css")).toExternalForm();
    public static String styleCss = Objects.requireNonNull(MainView.class.getClassLoader().getResource("css/style.css")).toExternalForm();

    public static double WIDTH = 600;
	public static double HEIGHT = 400;

    private final Canvas canvas = new Canvas();
    private Renderer renderer = null;
    private double mouseX = 0;
    private double mouseY = 0;


    public void start(@Observes Stage stage) {

        final Pane root = new Pane(canvas);
        root.setPrefWidth(WIDTH);
        root.setPrefHeight(HEIGHT);

        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {

            mouseX = e.getX();
            mouseY = e.getY();

        });

        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(darkStyle, styleCss);

        stage.setScene(scene);
        stage.setTitle("Uwe-javafx-canvas");
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();

        renderer = new VerletRenderer();
        canvas.addEventHandler(InputEvent.ANY, renderer.getEventHandler());
        canvas.requestFocus();

        final AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                final GraphicsContext gc = canvas.getGraphicsContext2D();
                final double width = canvas.getWidth();
                final double height = canvas.getHeight();
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, width, height);
                renderer.render(gc, width, height, mouseX, mouseY, now);
            }
        };

        timer.start();
    }


}
