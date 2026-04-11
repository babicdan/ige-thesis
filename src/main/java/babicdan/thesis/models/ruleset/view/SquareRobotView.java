package babicdan.thesis.models.ruleset.view;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.SquareCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SquareRobotView extends RobotView<SquareCoordinate> {
    public static final List<SquareCoordinate> NEIGHBOURS = new ArrayList<>(List.of(
            new SquareCoordinate(0, 0),
            new SquareCoordinate(1, 0),
            new SquareCoordinate(0, 1),
            new SquareCoordinate(-1, 0),
            new SquareCoordinate(0, -1)
    ));

    public SquareRobotView(SquareCoordinate pos, Function<SquareCoordinate, Optional<Robot>> grid) {
        super(pos, grid, NEIGHBOURS);
    }
}
