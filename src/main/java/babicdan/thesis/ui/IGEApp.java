package babicdan.thesis.ui;

import babicdan.thesis.models.coordinate.ScreenCoordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    private ScreenCoordinate offset = new ScreenCoordinate(0, 0);
    private ScreenCoordinate previous = new ScreenCoordinate(0, 0);

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
        grid.addRule(grid.getView(beacon3), new RobotPosition<>(new TriCoordinate(-1, 0)));

        grid.saveGrid();

        draw(canvas);


        canvas.setOnMousePressed((e) -> {
            previous = new ScreenCoordinate(e.getX() - offset.x(), e.getY() - offset.y());
        });

        canvas.setOnMouseClicked((e) -> {
            if(!e.isStillSincePress()) return;
            if(e.getButton() == MouseButton.SECONDARY)
                grid.reloadGrid();
            else
                grid.step();
            draw(canvas);
        });

        canvas.setOnMouseDragged((e) -> {
            offset = new ScreenCoordinate(e.getX() - previous.x(), e.getY() - previous.y());
            draw(canvas);
        });

        canvas.setOnScroll((e) -> {
            grid.step();
            draw(canvas);
        });
    }

    private void draw(Canvas c) {
        GraphicsContext gc = c.getGraphicsContext2D();
        var robots = grid.getRobots();
        gc.clearRect(0, 0, c.getWidth(), c.getHeight());
        for(var r : robots) {
            gc.setFill(colorMap.getOrDefault(r.color(), Color.GREY));
            var pos = r.position().getScreenCoordinate().scale(20).offset(c.getWidth()/2 + offset.x(), c.getHeight()/2 + offset.y());
            gc.fillOval(pos.x(), pos.y(), 16, 16);
        }
    }
}
