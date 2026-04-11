package babicdan.thesis.models.ruleset;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.Coordinate;

public record RobotPosition<C extends Coordinate<C>>(C position, Robot color) {
    public RobotPosition(C position) {
        this(position, new Robot(0));
    }

    public RobotPosition<C> rotate(int degrees) {
        return new RobotPosition<>(position.rotate(degrees), color);
    }

    public RobotPosition<C> mirror() {
        return new RobotPosition<>(position.mirror(), color);
    }
}
