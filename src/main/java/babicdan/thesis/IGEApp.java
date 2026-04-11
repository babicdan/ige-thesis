package babicdan.thesis;

import babicdan.thesis.models.coordinate.ScreenCoordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import babicdan.thesis.models.Robot;
import babicdan.thesis.models.grid.Grid;
import babicdan.thesis.models.ruleset.RobotPosition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IGEApp extends Application {
    private Stage stage;
    private Grid<TriCoordinate> grid;
    private final Map<Robot, Color> colorMap = new HashMap<>(Map.of(
            new Robot(0), Color.GREEN,
            new Robot(1), Color.BLUE
    ));
    private ScreenCoordinate offset = new ScreenCoordinate(0, 0);
    private ScreenCoordinate previous = new ScreenCoordinate(0, 0);

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        Pane base = new Pane();
        Scene s = new Scene(base, 600, 400, Color.WHITE);

        stage.setTitle("IGE Visualisation");
        stage.setScene(s);
        stage.show();

        final Canvas canvas = new Canvas(s.getWidth(), s.getHeight());
        canvas.widthProperty().bind(s.widthProperty());
        canvas.heightProperty().bind(s.heightProperty());

        base.getChildren().add(canvas);

        grid = new Grid<>(List.of(
                new TriCoordinate(0, 0),
                new TriCoordinate(1, 0),
                new TriCoordinate(1, 1),
                new TriCoordinate(0, 1),
                new TriCoordinate(-1, 0),
                new TriCoordinate(-1, -1),
                new TriCoordinate(0, -1)
        ));

        var left = new TriCoordinate(0, 0);
        var right = new TriCoordinate(0, -1);

        grid.addRobot(left, new Robot(0));
        grid.addRobot(right, new Robot(1));

        grid.addRule(grid.getView(left), new RobotPosition<>(right, new Robot(0)));
        grid.addRule(grid.getView(right), new RobotPosition<>(right, new Robot(1)));

        draw(canvas);


        canvas.setOnMousePressed((e) -> {
            previous = new ScreenCoordinate(e.getX() - offset.x(), e.getY() - offset.y());
        });

        canvas.setOnMouseClicked((e) -> {
            if(!e.isStillSincePress()) return;
            grid.step();
            draw(canvas);
        });

        canvas.setOnMouseDragged((e) -> {
            offset = new ScreenCoordinate(e.getX() - previous.x(), e.getY() - previous.y());
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
