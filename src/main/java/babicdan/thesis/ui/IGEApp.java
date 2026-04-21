package babicdan.thesis.ui;

import babicdan.thesis.models.coordinate.ScreenCoordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import babicdan.thesis.models.Robot;
import babicdan.thesis.models.grid.Grid;
import babicdan.thesis.models.ruleset.RobotPosition;

import java.util.*;


public class IGEApp extends Application {
    private Stage stage;
    private Grid<TriCoordinate> grid;
    private final Map<Robot, Color> colorMap = new HashMap<>(Map.of(
            new Robot(0), Color.BLACK,
            new Robot(1), Color.BLUE
    ));
    private ScreenCoordinate cameraPosition = new ScreenCoordinate(0, 0);
    private ScreenCoordinate dragStartPosition = new ScreenCoordinate(0, 0);
    private double zoom = 30;

    @Override
    public void start(Stage stage) throws InterruptedException {
        this.stage = stage;

        Pane base = new Pane();
        Scene s = new Scene(base, 1200, 900, Color.WHITE);

        stage.setTitle("IGE Problem Visualisation");
        stage.setScene(s);
        stage.show();

        final Canvas canvas = new Canvas(s.getWidth(), s.getHeight());
        canvas.widthProperty().bind(s.widthProperty());
        canvas.heightProperty().bind(s.heightProperty());
        base.getChildren().add(canvas);

        List<TriCoordinate> n = List.of(
                new TriCoordinate(0, 0),
                new TriCoordinate(1, 0),
                new TriCoordinate(1, 1),
                new TriCoordinate(0, 1),
                new TriCoordinate(-1, 0),
                new TriCoordinate(-1, -1),
                new TriCoordinate(0, -1)
        );

        HashSet<TriCoordinate> nset = new HashSet<>();

        for(var i : n) {
            for(var j : n) {
                nset.add(i.add(j));
            }
        }

        grid = new Grid<>(new ArrayList<>(nset));

        var shipFront = new TriCoordinate(0, 0);
        var shipSideLeft = new TriCoordinate(0, 1);
        var shipSideRight = new TriCoordinate(-1, -1);
        var shipBackLeft = new TriCoordinate(-1, 1);
        var shipBackRight = new TriCoordinate(-2, -1);

        var beacon1 = new TriCoordinate(0, -2);
        var beacon2 = new TriCoordinate(2, 4);
        var beacon3 = new TriCoordinate(-4, -1);


        for(var r : List.of(shipFront, shipSideLeft, shipSideRight, shipBackLeft, shipBackRight)) {
            grid.addRobot(r, new Robot(0));
        }
        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(1, 0)));


        for(var r : List.of(beacon1, beacon2, beacon3)) {
            grid.addRobot(r, new Robot(0));
        }

        for(var r: List.of(shipFront, shipSideLeft, shipBackLeft, shipBackRight)) {
            grid.addRule(grid.getView(r), new RobotPosition<>(new TriCoordinate(1, 0)));
        }

        grid.addRule(grid.getView(shipSideRight), new RobotPosition<>(new TriCoordinate(0, 1)));

        grid.addRule(grid.getView(beacon1), new RobotPosition<>(new TriCoordinate(1, 0)));
//        grid.addRule(grid.getView(beacon2), new RobotPosition<>(new TriCoordinate(1, 0)));
        grid.addRule(grid.getView(beacon3), new RobotPosition<>(new TriCoordinate(-1, 0)));

        grid.saveGrid();

        cameraPosition = new ScreenCoordinate(-canvas.getWidth()/2, -canvas.getHeight()/2);

        draw(canvas);


        canvas.setOnMousePressed((e) -> {
            dragStartPosition = cameraPosition.offset(e.getX(), e.getY());
        });

        canvas.setOnMouseClicked((e) -> {
            if(!e.isStillSincePress()) return;
            if(e.getButton() == MouseButton.SECONDARY)
                grid.reloadGrid();
            else if(e.getButton() == MouseButton.MIDDLE) {
                cameraPosition = new ScreenCoordinate(-canvas.getWidth()/2, -canvas.getHeight()/2);
                zoom = 30;
            }
            else
                grid.step();
            draw(canvas);
        });

        canvas.setOnMouseDragged((e) -> {
            cameraPosition = dragStartPosition.offset(-e.getX(), -e.getY());
            draw(canvas);
        });

        canvas.setOnScroll((e) -> {
            if(e.isControlDown()) {
                double newZoom = -100/(e.getDeltaY() / 100 - 100/zoom);
                newZoom = Math.clamp(newZoom, 6, 250);
                ScreenCoordinate pointStart = cameraPosition.offset(e.getX(), e.getY());
                ScreenCoordinate pointEnd = pointStart.scale(newZoom/zoom);
                cameraPosition = pointEnd.offset(-e.getX(), -e.getY());
                zoom = newZoom;
                dragStartPosition = cameraPosition.offset(e.getX(), e.getY());
            }
            else
                grid.step();
            draw(canvas);
        });

//        canvas.setOnMouseMoved((e) -> {
//            var closestTri = cameraPosition.offset(e.getX(), e.getY()).scale(1/zoom).getTriCoordinate();
//            GraphicsContext gc = canvas.getGraphicsContext2D();
//            gc.setFill(Color.RED);
//            var pos = new ScreenCoordinate(closestTri).scale(zoom).offset(cameraPosition.scale(-1));
//            gc.fillOval(pos.x()-zoom*0.1, pos.y()-zoom*0.1, zoom*0.2, zoom*0.2);
//        });

//        canvas.setOnMouseMoved((e) -> {
//            GraphicsContext gc = canvas.getGraphicsContext2D();
//            var closest = cameraPosition.offset(e.getX(), e.getY()).scale(1/zoom).getTriCoordinate();
//            int v = closest.x() + closest.y();
//            gc.setFill(switch (Math.floorMod(v,4)) {
//                case 0 -> Color.RED;
//                case 1 -> Color.ORANGE;
//                case 2 -> Color.YELLOW;
//                case 3 -> Color.CORAL;
//                default -> Color.GRAY;
//            });
//            var pos = new ScreenCoordinate(closest).scale(zoom).offset(cameraPosition.scale(-1));
//            gc.fillOval(e.getX()-zoom*0.05, e.getY()-zoom*0.05, zoom*0.1, zoom*0.1);
//        });
    }

    private void draw(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        var robots = grid.getRobots();
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        drawTriangleGrid(c);
        for(var r : robots) {
            gc.setFill(colorMap.getOrDefault(r.color(), Color.GREY));
            var pos = new ScreenCoordinate(r.position()).scale(zoom).offset(cameraPosition.scale(-1));
            gc.fillOval(pos.x()-zoom*0.4, pos.y()-zoom*0.4, zoom*0.8, zoom*0.8);
        }
    }

    private void drawTriangleGrid(Canvas c) {
        var topLeft = cameraPosition.scale(1/zoom).getTriCoordinate();
        var topRight = cameraPosition.offset(c.getWidth(), 0).scale(1/zoom).getTriCoordinate();
        var bottomLeft = cameraPosition.offset(0, c.getHeight()).scale(1/zoom).getTriCoordinate();
        var bottomRight = cameraPosition.offset(c.getWidth(), c.getHeight()).scale(1/zoom).getTriCoordinate();

        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(zoom/40);

        for(int i = bottomLeft.x()-1; i <= topRight.x()+1; i++) {
            var start = new ScreenCoordinate(new TriCoordinate(i, bottomLeft.y()-1)).scale(zoom).offset(cameraPosition.scale(-1));
            var end = new ScreenCoordinate(new TriCoordinate(i, topLeft.y()+1)).scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }

        for(int i = bottomRight.x()+1; i >= topLeft.x()-topLeft.y()+bottomLeft.y()-1; i--) {
            var start = new ScreenCoordinate(new TriCoordinate(i, bottomLeft.y()-1)).scale(zoom).offset(cameraPosition.scale(-1));
            var end = new ScreenCoordinate(new TriCoordinate(i+topLeft.y()-bottomLeft.y()+2, topLeft.y()+1)).scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }


        for(int i = bottomLeft.y()-1; i <= topLeft.y()+1; i++) {
            var start = new ScreenCoordinate(new TriCoordinate(bottomLeft.x()-1, i)).scale(zoom).offset(cameraPosition.scale(-1));
            var end = new ScreenCoordinate(new TriCoordinate(topRight.x()+1, i)).scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }

    }
}
