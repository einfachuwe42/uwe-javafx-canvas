package de.uwe.renderer;

import de.uwe.PersistUtils;
import de.uwe.Renderer;
import de.uwe.verlet.Point;
import de.uwe.verlet.Stick;
import de.uwe.verlet.VerletData;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class VerletRenderer implements Renderer {

    private final List<Point> points = new ArrayList<>();
    private final List<Stick> sticks = new ArrayList<>();
    private final List<Point> selectedPoints = new ArrayList<>();

    private boolean pause = false;

    public VerletRenderer(){
    }

    public void pause(){
        pause = !pause;
    }

    public void restart(){

        points.clear();
        sticks.clear();

        Point p00 = Point.create(300, 20, 300, 20);
        Point p02 = Point.create(300, 50, 300, 50);
        Point p0 = Point.create(300, 80, 300, 80);
        Point p1 = Point.create(100, 100, 100, 100);
        Point p2 = Point.create(200, 100, 200, 100);
        Point p3 = Point.create(200, 200, 200, 200);
        Point p4 = Point.create(100, 200, 98, 202);

        p00.pinned = true;

        points.add(p00);
        points.add(p02);
        points.add(p0);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        sticks.add(new Stick(p00, p02));
        sticks.add(new Stick(p02, p0));
        sticks.add(new Stick(p0, p1));
        sticks.add(new Stick(p1, p2));
        sticks.add(new Stick(p2, p3));
        sticks.add(new Stick(p3, p4));
        sticks.add(new Stick(p4, p1));
        sticks.add(new Stick(p1, p3));

    }

    @Override
    public void render(GraphicsContext gc, double width, double height, double mouseX, double mouseY, long now) {

        if(!pause){
            for (Point point : points) {
                point.update(width, height);
            }
            for (Stick stick : sticks) {
                stick.update(width, height);
            }
            for (Stick stick : sticks) {
                stick.update(width, height);
            }
            for (Stick stick : sticks) {
                stick.update(width, height);
            }
        }

        gc.setStroke(Color.YELLOWGREEN);
        for (Stick stick : sticks) {
            stick.render(gc);
        }

        gc.setFill(Color.ORANGERED);
        for (Point point : points) {
            point.render(gc, selectedPoints.contains(point));
        }

    }

    public Point getNearestPoint(double x, double y){

        double nearestDistance = 99999;
        Point nearestPoint = null;
        for (Point point : points) {

            point.color = Color.ORANGERED;
            double dx = point.x - x;
            double dy = point.y - y;
            double distance = Math.sqrt(dx*dx+dy*dy);

            if(distance < nearestDistance){
                nearestDistance = distance;
                nearestPoint = point;
            }
        }
        if(nearestPoint != null)
            nearestPoint.color = Color.LIGHTBLUE;
        return nearestPoint;
    }

    @Override
    public EventHandler<InputEvent> getEventHandler() {
        return new Handler();
    }

    public class Handler implements EventHandler<InputEvent> {

        double mouseX, mouseY, mouseDeltaX ,mouseDeltaY;

        Point selectedPoint = null;
        boolean mousePressed = false;
        boolean wasPinned = false;

        @Override
        public void handle(InputEvent event) {

            if(event.getEventType() == MouseEvent.MOUSE_MOVED){
                final MouseEvent e = (MouseEvent) event;
                this.mouseX = e.getX();
                this.mouseY = e.getY();
                selectedPoint = getNearestPoint(e.getX(), e.getY());
            }

            if(event.getEventType() == MouseEvent.MOUSE_DRAGGED){
                final MouseEvent e = (MouseEvent) event;
                double x = e.getX();
                double y = e.getY();
                if(selectedPoint != null) {
                    selectedPoint.x = selectedPoint.x1 = mouseDeltaX + x;
                    selectedPoint.y = selectedPoint.y1 = mouseDeltaY + y;
                }
            }

            if(event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                final MouseEvent e = (MouseEvent) event;
                if(selectedPoint != null) {
                    if(e.getButton().equals(MouseButton.SECONDARY)){
                        selectedPoint.pinned = !selectedPoint.pinned;
                    }
                }
            }

            if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
                final MouseEvent e = (MouseEvent) event;
                mousePressed = true;
                if(selectedPoint != null) {
                    wasPinned = selectedPoint.pinned;
                    selectedPoint.pinned = true;
                    mouseDeltaX = selectedPoint.x - e.getX();
                    mouseDeltaY = selectedPoint.y - e.getY();

                }
            }

            if(event.getEventType() == MouseEvent.MOUSE_RELEASED){
                mousePressed = false;
                if(selectedPoint != null) {
                    selectedPoint.pinned = wasPinned;
                }
            }

            if(event.getEventType() == KeyEvent.KEY_PRESSED){
                final KeyEvent e = (KeyEvent) event;
                switch (e.getCode()) {
                    case R -> restart();
                    case P -> pause();
                    case A -> points.add(Point.create(mouseX, mouseY, mouseX, mouseY));
                    case Y -> addPointWithConstrain(mouseX, mouseY);
                    case S -> selectPoint(mouseX, mouseY);
                    case D -> createConstrain();
                    case Q -> save();
                    case W -> load();
                    case C -> clear();
                    case G -> switchGravity();
                    case X -> addPointWithConstrainFixLength(mouseX, mouseY);

                }
            }
        }
    }

    private void addPointWithConstrainFixLength(double mouseX, double mouseY) {


        final double LENGTH = 50;

        if(!points.isEmpty()){
            final Point lastPoint = points.get(points.size()-1);
            double dx = mouseX - lastPoint.x;
            double dy = mouseY - lastPoint.y;
            double angle = Math.atan2(dy, dx);
            double x = lastPoint.x + LENGTH * Math.cos(angle);
            double y = lastPoint.y + LENGTH * Math.sin(angle);
            final Point newPoint = Point.create(x, y, x, y);
            final Stick newStick = new Stick(lastPoint, newPoint);
            sticks.add(newStick);
            points.add(newPoint);
        }


    }

    private void addPointWithConstrain(double mouseX, double mouseY) {

        final Point newPoint = Point.create(mouseX, mouseY, mouseX, mouseY);

        if(!points.isEmpty()){
            final Point lastPoint = points.get(points.size()-1);
            final Stick newStick = new Stick(lastPoint, newPoint);
            sticks.add(newStick);
        }
        points.add(newPoint);


    }

    private void switchGravity() {
        for (Point point : points) {
            point.gravity *= -1;
        }
    }

    private void clear() {
        points.clear();
        sticks.clear();
    }

    public void selectPoint(double mouseX, double mouseY){

        final Point nearestPoint = getNearestPoint(mouseX, mouseY);
        if(selectedPoints.contains(nearestPoint))
            return;

        if(selectedPoints.size() < 2){
            selectedPoints.add(nearestPoint);
        }else{
            selectedPoints.clear();
            selectedPoints.add(nearestPoint);
        }

    }

    private void createConstrain() {

        if(selectedPoints.size() == 2){

            final Point p1 = selectedPoints.get(0);
            final Point p2 = selectedPoints.get(1);
            final Stick test = new Stick(p1, p2);
            final Stick stick = sticks.stream().filter(s -> s.equals(test)).findFirst().orElse(null);
            if(stick == null){
                sticks.add(test);
            }else{
                sticks.remove(test);
            }
            selectedPoints.clear();

        }else if(selectedPoints.size() == 1) {

            final Point selectedPoint = selectedPoints.get(0);
            final List<Stick> sticksToRemove = sticks.stream().filter(s -> s.p1.equals(selectedPoint) || s.p2.equals(selectedPoint)).toList();
            points.remove(selectedPoint);
            sticks.removeAll(sticksToRemove);
            selectedPoints.clear();
        }

    }


    /**
     *
     *
     *
     */
    private void save() {

        final VerletData data = new VerletData(points, sticks);
        PersistUtils.saveJsonObject(data, VerletData.class);

        final List<String> stickLines = new ArrayList<>();
        for (Stick stick : sticks) {
            final int id1 = stick.p1.id;
            final int id2 = stick.p2.id;
            final String line = id1+" "+id2;
            stickLines.add(line);
        }

        PersistUtils.writeSticksToFile("verletData.txt", stickLines);

    }

    private void load() {
        clear();
        final VerletData data = PersistUtils.loadJsonObject(VerletData.class);
        for (Point point : data.points) {
            points.add(new Point(point));
        }

        final List<String> stickLines = PersistUtils.readSticksFromFile("verletData.txt");

        for (String line : stickLines) {
            String[] ids = line.split(" ");
            int id1 = Integer.parseInt(ids[0]);
            int id2 = Integer.parseInt(ids[1]);
            final Point p1 = points.stream().filter(p -> p.id == id1).findFirst().orElseThrow();
            final Point p2 = points.stream().filter(p -> p.id == id2).findFirst().orElseThrow();
            sticks.add(new Stick(p1, p2));
        }
    }

}
