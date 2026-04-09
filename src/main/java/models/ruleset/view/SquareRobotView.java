package models.ruleset.view;

import models.Robot;
import models.coordinate.SquareCoords;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SquareRobotView extends RobotView<SquareCoords> {
    private static final List<SquareCoords> NEIGHBOURS = new ArrayList<>(List.of(
            new SquareCoords(0, 0),
            new SquareCoords(1, 0),
            new SquareCoords(0, 1),
            new SquareCoords(-1, 0),
            new SquareCoords(0, -1)
    ));

    public SquareRobotView(SquareCoords pos, Function<SquareCoords, Optional<Robot>> grid) {
        super(pos, grid, NEIGHBOURS);
    }
}
