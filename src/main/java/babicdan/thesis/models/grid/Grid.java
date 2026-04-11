package babicdan.thesis.models.grid;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.Coordinate;
import babicdan.thesis.models.ruleset.RobotPosition;
import babicdan.thesis.models.ruleset.Ruleset;
import babicdan.thesis.models.ruleset.view.RobotView;

import java.util.*;
import java.util.stream.Collectors;

public class Grid<C extends Coordinate<C>> {
    protected Map<C, Robot> grid = new HashMap<>();
    protected final Ruleset<C> ruleset = new Ruleset<>();
    protected List<C> neighbours;

    public Grid(List<C> neighbours) {
        this.neighbours = neighbours;
    }

    public Optional<Robot> get(C pos) {
        return Optional.ofNullable(grid.get(pos));
    }

    public List<RobotPosition<C>> getRobots() {
        return grid.entrySet().stream().map((e) -> new RobotPosition<C>(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public RobotView<C> getView(C pos) {
        return new RobotView<C>(pos, this::get, neighbours);
    }

    public void addRobot(C pos, Robot r) {
        grid.put(pos, r);
    }

    public void addRule(RobotView<C> view, RobotPosition<C> move) {
        ruleset.addRule(view, move);
    }

    public void step() {
        Map<C, Robot> newGrid = new HashMap<>();
        for(var pair : grid.entrySet()) {
            List<RobotPosition<C>> moves = ruleset.getMoves(new RobotView<>(pair.getKey(), this::get, neighbours));
            if(moves.isEmpty())
                newGrid.put(pair.getKey(), pair.getValue());
            else {
                var move = moves.getFirst();
                newGrid.put(pair.getKey().add(move.position()), move.color());
                // TODO: Collision detection (vertex and edge)
            }
        }
        grid = newGrid;
    }

    @Override
    public String toString() {
        return "Grid" + grid;
    }
}
