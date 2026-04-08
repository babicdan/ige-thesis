package models.grid;

import models.Robot;
import models.coordinate.SquareCoords;
import models.view.SquareRobotView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SquareGrid implements Grid<SquareCoords> {
    private final Map<SquareCoords, Robot> grid = new HashMap<>();


    @Override
    public Optional<Robot> get(SquareCoords pos) {
        return Optional.ofNullable(grid.get(pos));
    }

    @Override
    public void addRobot(SquareCoords pos, Robot r) {
        grid.put(pos, r);
    }

    @Override
    public void step() {
        for(var pair : grid.entrySet()) {
            SquareRobotView view = new SquareRobotView(pair.getKey(), this::get);
            var test = view.normalize();
            IO.println(test.toString());
        }
    }

    @Override
    public String toString() {
        return grid.toString();
    }
}