package models.grid;

import models.Robot;
import models.coordinate.Coordinate;

import java.util.Optional;

public interface Grid<C extends Coordinate<C>> {
    Optional<Robot> get(C pos);

    void addRobot(C pos, Robot r);

//    void addRule();

    void step();
}
