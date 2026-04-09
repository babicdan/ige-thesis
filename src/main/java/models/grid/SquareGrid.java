package models.grid;

import models.Robot;
import models.coordinate.SquareCoords;
import models.ruleset.RobotMove;
import models.ruleset.Ruleset;
import models.ruleset.view.RobotView;
import models.ruleset.view.SquareRobotView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SquareGrid implements Grid<SquareCoords> {
    private Map<SquareCoords, Robot> grid = new HashMap<>();
    private final Ruleset<SquareCoords> ruleset = new Ruleset<>();

    @Override
    public Optional<Robot> get(SquareCoords pos) {
        return Optional.ofNullable(grid.get(pos));
    }

    public SquareRobotView getView(SquareCoords pos) {
        return new SquareRobotView(pos, this::get);
    }

    @Override
    public void addRobot(SquareCoords pos, Robot r) {
        grid.put(pos, r);
    }

    @Override
    public void addRule(RobotView<SquareCoords> view, RobotMove<SquareCoords> move) {
        ruleset.addRule(view, move);
    }

    @Override
    public void step() {
        Map<SquareCoords, Robot> newGrid = new HashMap<>();
        for(var pair : grid.entrySet()) {
            List<RobotMove<SquareCoords>> moves = ruleset.getMoves(new SquareRobotView(pair.getKey(), this::get));
            if(moves.isEmpty())
                newGrid.put(pair.getKey(), pair.getValue());
            else {
                var move = moves.getFirst();
                newGrid.put(pair.getKey().add(move.direction()), move.newColor());
                // TODO: Collision detection
            }
        }
        grid = newGrid;
    }

    @Override
    public String toString() {
        return grid.toString();
    }
}