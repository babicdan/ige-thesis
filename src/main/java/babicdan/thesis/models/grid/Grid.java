package babicdan.thesis.models.grid;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.Coordinate;
import babicdan.thesis.models.ruleset.RobotPosition;
import babicdan.thesis.models.ruleset.Ruleset;
import babicdan.thesis.models.ruleset.RobotView;

import java.util.*;
import java.util.stream.Collectors;

public class Grid<C extends Coordinate<C>> {
    protected Map<C, Robot> grid = new HashMap<>();
    protected Map<C, Robot> savedGrid = new HashMap<>();
    protected final Ruleset<C> ruleset = new Ruleset<>();
    protected List<C> inView;

    public Grid(List<C> inView) {
        this.inView = inView;
    }

    public Optional<Robot> get(C pos) {
        return Optional.ofNullable(grid.get(pos));
    }

    public List<RobotPosition<C>> getRobots() {
        return grid.entrySet().stream().map((e) -> new RobotPosition<>(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public RobotView<C> getView(C pos) {
        return new RobotView<>(pos, this::get, inView);
    }

    public void addRobot(C pos, Robot r) {
        grid.put(pos, r);
    }

    public Ruleset<C> getRuleset() {
        return ruleset;
    }

    public void addRule(RobotView<C> view, RobotPosition<C> move) {
        ruleset.addRule(view, move);
    }

    public boolean step() {
        Map<C, Robot> newGrid = new HashMap<>();
        for(var pair : grid.entrySet()) {
            ArrayList<RobotPosition<C>> moves = new ArrayList<>(ruleset.getMoves(new RobotView<>(pair.getKey(), this::get, inView)));
            if(moves.isEmpty()) {
                if (newGrid.put(pair.getKey(), pair.getValue()) != null)
                    return false;
            }
            else {
                Collections.shuffle(moves);
                var move = moves.getFirst();
                if(newGrid.put(pair.getKey().add(move.position()), move.color()) != null)
                    return false;
                // TODO: Collision detection (edge)
            }
        }
        grid = newGrid;
        return true;
    }

    public void saveGrid() {
        savedGrid = new HashMap<>(grid);
    }

    public void reloadGrid() {
        grid = new HashMap<>(savedGrid);
    }

    @Override
    public String toString() {
        return "Grid" + grid;
    }
}
