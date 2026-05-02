package babicdan.thesis.ui;

import babicdan.thesis.models.coordinate.HexCoordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;
import babicdan.thesis.models.grid.AlgorithmHelper;
import babicdan.thesis.models.ruleset.RobotPosition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import babicdan.thesis.models.Robot;
import babicdan.thesis.models.grid.Grid;

import java.util.*;


public class IGEApp extends Application {
    private Stage stage;

    private static final double ROBOT_SIZE = 0.8;
    private static final double DEFAULT_ZOOM = 30;
    private static final double ZOOM_FACTOR = 1./290;
    private static final double ZOOM_MIN = 6;
    private static final double ZOOM_MAX = 300;

    private Grid<HexCoordinate> grid;

    private final Map<Robot, Color> colorMap = new HashMap<>(Map.of(
            new Robot('R'), Color.BLACK,
            new Robot('L'), Color.DODGERBLUE,
            new Robot('F'), Color.LAWNGREEN,
            new Robot('B'), Color.CRIMSON
    ));
    private ScreenCoordinate cameraPosition = new ScreenCoordinate(0, 0);
    private ScreenCoordinate dragStartPosition = new ScreenCoordinate(0, 0);
    private double zoom = DEFAULT_ZOOM;

    @Override
    public void start(Stage stage) throws InterruptedException {
        this.stage = stage;

        Pane pane = new Pane();
        Scene s = new Scene(pane, 1200, 900, Color.WHITE);

        stage.setTitle("IGE Problem Visualisation");
        stage.setScene(s);
        stage.show();

        final Canvas gridCanvas = new Canvas(s.getWidth(), s.getHeight());
        final Canvas canvas = new Canvas(s.getWidth(), s.getHeight());
        for(var c : List.of(gridCanvas, canvas)) {
            c.widthProperty().bind(s.widthProperty());
            c.heightProperty().bind(s.heightProperty());
            pane.getChildren().add(c);
        }

        cameraPosition = new ScreenCoordinate(-canvas.getWidth()/2, -canvas.getHeight()/2);

//        grid = AlgorithmHelper.algorithmTriOne();
        var front = new HexCoordinate(0,0,false);
        var back = new HexCoordinate(-1,0,true);
        grid = new Grid<>(front.neighbours());


        grid.addRobot(front, new Robot('L'));
        grid.addRobot(back, new Robot('F'));
        grid.addRule(grid.getView(front), new RobotPosition<>(new HexCoordinate(0, 0, true), new Robot('L')));
        grid.addRule(grid.getView(back), new RobotPosition<>(new HexCoordinate(-1, 0, true), new Robot('F')));


        grid.saveGrid();

        drawHexagonalGrid(gridCanvas);
        draw(canvas);

        s.setOnKeyPressed((k) -> {
            switch (k.getCode()) {
//                case KeyCode.DIGIT1, KeyCode.NUMPAD1 -> grid = AlgorithmHelper.algorithmTriOne();
//                case KeyCode.DIGIT2, KeyCode.NUMPAD2 -> grid = AlgorithmHelper.algorithmTriTwo();
//                case KeyCode.DIGIT3, KeyCode.NUMPAD3 -> grid = AlgorithmHelper.algorithmTriThree();
//                case KeyCode.DIGIT5, KeyCode.NUMPAD5 -> grid = AlgorithmHelper.algorithmTriThreeAlt();
                case KeyCode.R -> grid.reloadGrid();
                case KeyCode.C -> copyRobotsAsTikz();
            }
            draw(canvas);
        });

        s.setOnMousePressed((e) -> {
            dragStartPosition = cameraPosition.offset(e.getX(), e.getY());
        });

        s.setOnMouseClicked((e) -> {
            if(!e.isStillSincePress()) return;
            if(e.getButton() == MouseButton.SECONDARY) {
                cameraPosition = new ScreenCoordinate(-canvas.getWidth()/2, -canvas.getHeight()/2);
                zoom = DEFAULT_ZOOM;
                drawHexagonalGrid(gridCanvas);
            }
            else
                grid.step();
            draw(canvas);
        });

        s.setOnMouseDragged((e) -> {
            cameraPosition = dragStartPosition.offset(-e.getX(), -e.getY());
            drawHexagonalGrid(gridCanvas);
            draw(canvas);
        });

        s.setOnScroll((e) -> {
            if(e.isControlDown()) {
                double newZoom = Math.exp(Math.log(zoom) + e.getDeltaY() * ZOOM_FACTOR);
                newZoom = Math.clamp(newZoom, ZOOM_MIN, ZOOM_MAX);
                ScreenCoordinate pointStart = cameraPosition.offset(e.getX(), e.getY());
                ScreenCoordinate pointEnd = pointStart.scale(newZoom/zoom);
                cameraPosition = pointEnd.offset(-e.getX(), -e.getY());
                zoom = newZoom;
                dragStartPosition = cameraPosition.offset(e.getX(), e.getY());
                drawHexagonalGrid(gridCanvas);
            }
            else
                grid.step();
            draw(canvas);
        });

//        s.setOnMouseMoved((e) -> {
//            var closestTri = cameraPosition.offset(e.getX(), e.getY()).scale(1/zoom).getTriCoordinate();
//            GraphicsContext gc = canvas.getGraphicsContext2D();
//            gc.setFill(Color.WHITE);
//            gc.fillRect(0, 0, 200, 30);
//            gc.setFill(Color.BLACK);
//            gc.fillText(closestTri.toString(), 10, 20);
//            var pos = new ScreenCoordinate(closestTri).scale(zoom).offset(cameraPosition.scale(-1));
//            gc.setFill(Color.MEDIUMVIOLETRED);
//            gc.fillOval(pos.x()-zoom*0.1, pos.y()-zoom*0.1, zoom*0.2, zoom*0.2);
//        });

//        s.setOnMouseMoved((e) -> {
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
//        drawTriangleGrid(c);
        drawHexagonalGrid(c);
        for(var r : robots) {
            gc.setFill(colorMap.getOrDefault(r.robot(), Color.GREY));
            var pos = new ScreenCoordinate(r.position()).scale(zoom).offset(cameraPosition.scale(-1));
            gc.fillOval(pos.x()-zoom*ROBOT_SIZE/2, pos.y()-zoom*ROBOT_SIZE/2,
                    zoom*ROBOT_SIZE, zoom*ROBOT_SIZE);
        }
    }

    private void drawTriangleGrid(Canvas c) {
        var topLeft = cameraPosition.scale(1/zoom).getTriCoordinate();
        var topRight = cameraPosition.offset(c.getWidth(), 0).scale(1/zoom).getTriCoordinate();
        var bottomLeft = cameraPosition.offset(0, c.getHeight()).scale(1/zoom).getTriCoordinate();
        var bottomRight = cameraPosition.offset(c.getWidth(), c.getHeight()).scale(1/zoom).getTriCoordinate();

        GraphicsContext gc = c.getGraphicsContext2D();
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(zoom/40);

        int bottom = bottomLeft.y() - 1;
        int top = topLeft.y() + 1;

        for (int i = bottomLeft.x() - 1; i <= topRight.x() + 1; i++) {
            var start = new ScreenCoordinate(
                    new TriCoordinate(i, bottom)).scale(zoom).offset(cameraPosition.scale(-1));
            var end = new ScreenCoordinate(
                    new TriCoordinate(i, top)).scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }

        for (int i = bottomRight.x() + 1; i >= topLeft.x() - top + bottom; i--) {
            var start = new ScreenCoordinate(
                    new TriCoordinate(i, bottom)).scale(zoom).offset(cameraPosition.scale(-1));
            var end = new ScreenCoordinate(
                    new TriCoordinate(i + top - bottom, top)).scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }

        int left = bottomLeft.x() - 1;
        int right = topRight.x() + 1;
        for (int i = bottomLeft.y() - 1; i <= topLeft.y() + 1; i++) {
            var start = new ScreenCoordinate(
                    new TriCoordinate(left, i)).scale(zoom).offset(cameraPosition.scale(-1));
            var end = new ScreenCoordinate(
                    new TriCoordinate(right, i)).scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }
    }

    private void drawHexagonalGrid(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(zoom/40);

        var zero = new HexCoordinate(0,0,false);

        for(int i = -20; i <= 20; i++) {
            for(int j = -20; j <=20; j++) {
                for(var d : zero.neighbours()) {
                    var fromHex = new HexCoordinate(i, j, false);
                    var toHex = fromHex.add(d);
                    var start = new ScreenCoordinate(fromHex).scale(zoom).offset(cameraPosition.scale(-1));
                    var end = new ScreenCoordinate(toHex).scale(zoom).offset(cameraPosition.scale(-1));
                    gc.strokeLine(start.x(), start.y(), end.x(), end.y());
                }
            }
        }
    }

    private void copyRobotsAsTikz() {
        String node = "\\node at (%d, %d) {%c};\n";
        String move = "\\draw (%d, %d) -- +(%d, %d);\n";
        StringBuilder result = new StringBuilder();
        final Clipboard c = Clipboard.getSystemClipboard();
        final ClipboardContent cc = new ClipboardContent();
        for(var r : grid.getRobots()) {
            result.append(String.format(node, r.position().x(), r.position().y(), r.robot().color()));
        }
        result.append("\n");
        for(var r : grid.getMoves().entrySet()) {
            for(var dir : r.getValue()) {
                result.append(String.format(move, r.getKey().x(), r.getKey().y(), dir.position().x(), dir.position().y()));
            }
        }

        cc.putString(result.toString());
        c.setContent(cc);
    }
}
