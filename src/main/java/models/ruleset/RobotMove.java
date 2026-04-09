package models.ruleset;

import models.Robot;
import models.coordinate.Coordinate;

public record RobotMove<C extends Coordinate<C>>(C direction, Robot newColor) {
    public RobotMove<C> rotate(int degrees) {
        return new RobotMove<>(direction.rotate(degrees), newColor);
    }

    public RobotMove<C> mirror() {
        return new RobotMove<>(direction.mirror(), newColor);
    }
}
