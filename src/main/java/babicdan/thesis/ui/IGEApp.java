package babicdan.thesis.ui;

import babicdan.thesis.models.coordinate.Coordinate;
import babicdan.thesis.models.coordinate.HexCoordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;
import babicdan.thesis.models.grid.AlgorithmHelper;
import babicdan.thesis.models.grid.GridType;
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

    private static final double ROBOT_SIZE = 0.7;
    private static final double DEFAULT_ZOOM = 30;
    private static final double ZOOM_FACTOR = 1./290;
    private static final double ZOOM_MIN = 6;
    private static final double ZOOM_MAX = 300;

    private GridType inUse = GridType.TRIANGLE;
    private Grid<TriCoordinate> triGrid = AlgorithmHelper.algoTriOne();
    private Grid<HexCoordinate> hexGrid = AlgorithmHelper.hexDemoTwo();
    private Grid<? extends Coordinate<?>> grid = triGrid;

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
    public void start(Stage stage) {
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

        s.widthProperty().addListener((o) -> {
            drawGrid(gridCanvas);
            drawRobots(grid, canvas);
        });

        s.heightProperty().addListener((o) -> {
            drawGrid(gridCanvas);
            drawRobots(grid, canvas);
        });

        cameraPosition = new ScreenCoordinate(-canvas.getWidth()/2, -canvas.getHeight()/2);

        drawTriangleGrid(gridCanvas);
//        drawHexagonalGrid(gridCanvas);
        drawRobots(grid, canvas);

        s.setOnKeyPressed((k) -> {
            switch (k.getCode()) {
                case KeyCode.DIGIT1, KeyCode.NUMPAD1 -> {
                    triGrid = AlgorithmHelper.algoTriOne();
                    grid = triGrid;
                    inUse = GridType.TRIANGLE;
                }
                case KeyCode.DIGIT2, KeyCode.NUMPAD2 -> {
                    triGrid = AlgorithmHelper.algoTriTwo();
                    grid = triGrid;
                    inUse = GridType.TRIANGLE;
                }
                case KeyCode.DIGIT3, KeyCode.NUMPAD3 -> {
                    triGrid = AlgorithmHelper.algoTriThree();
                    grid = triGrid;
                    inUse = GridType.TRIANGLE;
                }
                case KeyCode.DIGIT4, KeyCode.NUMPAD4 -> {
                    triGrid = AlgorithmHelper.algoTriThreeAlt();
                    grid = triGrid;
                    inUse = GridType.TRIANGLE;
                }
                case KeyCode.DIGIT5, KeyCode.NUMPAD5 -> {
                    hexGrid = AlgorithmHelper.algoHexThree();
                    grid = hexGrid;
                    inUse = GridType.HEXAGON;
                }
                case KeyCode.DIGIT6, KeyCode.NUMPAD6 -> {
                    hexGrid = AlgorithmHelper.hexDemoOne();
                    grid = hexGrid;
                    inUse = GridType.HEXAGON;
                }
                case KeyCode.DIGIT7, KeyCode.NUMPAD7 -> {
                    hexGrid = AlgorithmHelper.hexDemoTwo();
                    grid = hexGrid;
                    inUse = GridType.HEXAGON;
                }
                case KeyCode.R -> grid.reloadGrid();
                case KeyCode.C -> copyRobotsAsTikz();
                case KeyCode.SPACE, KeyCode.RIGHT -> grid.step();
                case KeyCode.LEFT -> grid.undoStep();
            }
            drawGrid(gridCanvas);
            drawRobots(grid, canvas);
        });

        s.setOnMousePressed((e) -> {
            dragStartPosition = cameraPosition.offset(e.getX(), e.getY());
        });

        s.setOnMouseClicked((e) -> {
            if(!e.isStillSincePress()) return;
            if(e.getButton() == MouseButton.SECONDARY) {
                cameraPosition = new ScreenCoordinate(-canvas.getWidth()/2, -canvas.getHeight()/2);
                zoom = DEFAULT_ZOOM;
                drawGrid(gridCanvas);
            }
            else
                grid.step();
            drawRobots(grid, canvas);
        });

        s.setOnMouseDragged((e) -> {
            cameraPosition = dragStartPosition.offset(-e.getX(), -e.getY());
            drawGrid(gridCanvas);
            drawRobots(grid, canvas);
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
                drawGrid(gridCanvas);
            }
            else if(e.getDeltaY() > 1)
                grid.undoStep();
            else
                grid.step();
            drawRobots(grid, canvas);
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

    private <C extends Coordinate<C>> void drawRobots(Grid<C> grid, Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 80, 30);
        gc.setFill(Color.BLACK);
        gc.fillText("Round " + grid.getRound(), 10, 20);

        for(var v : grid.getVisited()) {
            gc.setFill(Color.LIGHTSLATEGREY);
            var pos = v.getScreenCoordinate().scale(zoom).offset(cameraPosition.scale(-1));
            gc.fillOval(pos.x()-zoom*ROBOT_SIZE/6, pos.y()-zoom*ROBOT_SIZE/6,
                    zoom*ROBOT_SIZE/3, zoom*ROBOT_SIZE/3);
        }

        var robots = grid.getRobots();
        for(var r : robots) {
            gc.setFill(colorMap.getOrDefault(r.robot(), Color.GREY));
            var pos = r.position().getScreenCoordinate().scale(zoom).offset(cameraPosition.scale(-1));
            gc.fillOval(pos.x()-zoom*ROBOT_SIZE/2, pos.y()-zoom*ROBOT_SIZE/2,
                    zoom*ROBOT_SIZE, zoom*ROBOT_SIZE);
        }
    }


    private void drawGrid(Canvas c) {
        switch (inUse) {
            case SQUARE -> {}
            case TRIANGLE -> drawTriangleGrid(c);
            case HEXAGON -> drawHexagonalGrid(c);
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
            var start = new TriCoordinate(i, bottom).getScreenCoordinate()
                    .scale(zoom).offset(cameraPosition.scale(-1));
            var end = new TriCoordinate(i, top).getScreenCoordinate()
                            .scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }

        for (int i = bottomRight.x() + 1; i >= topLeft.x() - top + bottom; i--) {
            var start = new TriCoordinate(i, bottom).getScreenCoordinate()
                            .scale(zoom).offset(cameraPosition.scale(-1));
            var end = new TriCoordinate(i + top - bottom, top).getScreenCoordinate()
                    .scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }

        int left = bottomLeft.x() - 1;
        int right = topRight.x() + 1;
        for (int i = bottomLeft.y() - 1; i <= topLeft.y() + 1; i++) {
            var start = new TriCoordinate(left, i).getScreenCoordinate()
                    .scale(zoom).offset(cameraPosition.scale(-1));
            var end = new TriCoordinate(right, i).getScreenCoordinate()
                    .scale(zoom).offset(cameraPosition.scale(-1));
            gc.strokeLine(start.x(), start.y(), end.x(), end.y());
        }
    }

    private void drawHexagonalGrid(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(zoom/40);

        var zero = new HexCoordinate(0,0,false);

        for(int i = -20; i <= 20; i++) {
            for(int j = -20; j <=20; j++) {
                for(var d : zero.neighbours()) {
                    var fromHex = new HexCoordinate(i, j, false);
                    var toHex = fromHex.add(d);
                    var start = fromHex.getScreenCoordinate().scale(zoom).offset(cameraPosition.scale(-1));
                    var end = toHex.getScreenCoordinate().scale(zoom).offset(cameraPosition.scale(-1));
                    gc.strokeLine(start.x(), start.y(), end.x(), end.y());
                }
            }
        }
    }

    private void copyRobotsAsTikz() {
        String node = "\\node at (%d, %d) {%c};\n";
        String move = "\\draw (%d, %d) -- +(%d, %d);\n";
        StringBuilder result = new StringBuilder();

        if(inUse == GridType.TRIANGLE) {
            for(var r : triGrid.getRobots()) {
                result.append(String.format(node, r.position().x(), r.position().y(), r.robot().color()));
            }
            result.append("\n");
            for(var r : triGrid.getMoves().entrySet()) {
                for(var dir : r.getValue()) {
                    result.append(String.format(move, r.getKey().x(), r.getKey().y(), dir.position().x(), dir.position().y()));
                }
            }
        }
        else if(inUse == GridType.HEXAGON) {
            for(var r : hexGrid.getRobots()) {
                result.append(String.format(node, r.position().getTriCoordinate().x(), r.position().getTriCoordinate().y(), r.robot().color()));
            }
            for(var r : hexGrid.getMoves().entrySet()) {
                for(var dir : r.getValue()) {
                    result.append(String.format(move, r.getKey().getTriCoordinate().x(), r.getKey().getTriCoordinate().y(),
                            dir.position().getTriCoordinate().x()*(dir.position().top()?-1:1),
                            dir.position().getTriCoordinate().y()*(dir.position().top()?-1:1)
                    ));
                }
            }
        }
        else
            return;

        final Clipboard c = Clipboard.getSystemClipboard();
        final ClipboardContent cc = new ClipboardContent();

        cc.putString(result.toString());
        c.setContent(cc);
    }
}
