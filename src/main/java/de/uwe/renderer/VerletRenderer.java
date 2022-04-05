package de.uwe.renderer;

import de.uwe.PersistUtils;
import de.uwe.Renderer;
import de.uwe.SettingsStage;
import de.uwe.verlet.Point;
import de.uwe.verlet.Stick;
import de.uwe.verlet.VerletData;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.IntFunction;

@ApplicationScoped
public class VerletRenderer implements Renderer {

    private final List<Point> points = new ArrayList<>();
    private final List<Stick> sticks = new ArrayList<>();
    private final List<Point> selectedPoints = new ArrayList<>();

    private boolean pause = false;
    private boolean info = false;

    @Inject
    public SettingsStage settingsStage;

    public void restart(){

        clear();

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

    private final List<Double[]> values = new ArrayList<>();

    @Override
    public void render(GraphicsContext gc, double width, double height, double mouseX, double mouseY, long now) {


        if(info) {

            gc.setFill(Color.YELLOW);
            gc.fillText("points: " + points.size(), 50, 50);
            gc.fillText("sticks: " + sticks.size(), 50, 75);
            gc.fillText("lastId: " + Point.lastId, 50, 100);

        }

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

        //polyline
        gc.setStroke(Color.DEEPPINK);
        final int size = values.size();
        final double[] pointX = values.stream().mapToDouble(v -> v[0]).toArray();
        final double[] pointY = values.stream().mapToDouble(v -> v[1]).toArray();
        gc.strokePolyline(pointX, pointY, size);

        //sticks
        gc.setStroke(Color.YELLOWGREEN);
        for (Stick stick : sticks) {
            stick.render(gc);
        }

        //points
        gc.setFill(Color.ORANGERED);
        for (Point point : points) {
            point.render(gc, selectedPoints.contains(point));
            if(point.marked){
                values.add(new Double[] { point.x, point.y });
            }
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

    private List<Stick> getConnectedSticks(Point point) {
        return sticks.stream().filter(s -> s.p1.equals(point) || s.p2.equals(point)).toList();
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
                    case P -> pause = !pause;
                    case A -> points.add(Point.create(mouseX, mouseY, mouseX, mouseY));
                    case Y -> addPointWithConstrain(mouseX, mouseY);
                    case X -> addPointWithConstrainFixLength(mouseX, mouseY);
                    case D -> createConstrain();
                    case S -> selectPoint(mouseX, mouseY);
                    case E -> clearSelectedPoints();
                    case Q -> save();
                    case W -> load();
                    case C -> clear();
                    case G -> switchGravity();
                    case I -> info = !info;
                    case F -> removePoint();
                    case V -> settingsStage.show();
                    case M -> markPoint();
                    case L -> values.clear();


                }
            }
        }
    }

    private void markPoint() {

        if(selectedPoints.size() == 1) {
            final Point selectedPoint = selectedPoints.get(0);
            for (Point point : points) {
                if(point.equals(selectedPoint)){
                    point.marked = !point.marked;
                }else{
                    point.marked = false;
                }
            }
            selectedPoints.clear();
        }

    }

    private void clearSelectedPoints() {
        selectedPoints.clear();
    }

    private void removePoint() {

        if(!points.isEmpty() && selectedPoints.size() == 1){
            final Point selectedPoint = selectedPoints.get(0);
            final List<Stick> connectedSticks = getConnectedSticks(selectedPoint);

            if(connectedSticks.size() == 2){

                Stick stick1 = connectedSticks.get(0);
                Stick stick2 = connectedSticks.get(1);

                Point p1 = stick1.p1.equals(selectedPoint) ? stick1.p2 : stick1.p1;
                Point p2 = stick2.p1.equals(selectedPoint) ? stick2.p2 : stick2.p1;

                Stick newStick = new Stick(p1, p2);

                sticks.removeAll(connectedSticks);
                points.remove(selectedPoint);
                sticks.add(newStick);
                selectedPoints.clear();

            }
        }
    }


    private void addPointWithConstrainFixLength(double mouseX, double mouseY) {

        final double LENGTH = 20;

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
        values.clear();
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
            if(point.id > Point.lastId){
                Point.lastId = point.id;
            }
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
