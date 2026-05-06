package babicdan.thesis.models.grid;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.Coordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;
import babicdan.thesis.models.ruleset.RobotPosition;
import babicdan.thesis.models.ruleset.Ruleset;
import babicdan.thesis.models.ruleset.RobotView;

import java.util.*;
import java.util.stream.Collectors;

public class Grid<C extends Coordinate<C>> {
    private record Edge<C extends Coordinate<C>>(C v, C w) {
        public Edge(C v, C w) {
            if(v.compareTo(w) < 0) {
                this.v = v;
                this.w = w;
            }
            else {
                this.v = w;
                this.w = v;
            }
        }
    }

    protected Map<C, Robot> grid = new HashMap<>();
    protected int round = 0;

    protected List<C> robotView;
    protected final Ruleset<C> ruleset = new Ruleset<>();
    protected Map<C, Robot> savedGrid = new HashMap<>();
    protected int savedRound = 0;

    public Grid(List<C> robotView) {
        this.robotView = robotView;
    }

    public Optional<Robot> get(C pos) {
        return Optional.ofNullable(grid.get(pos));
    }

    public List<RobotPosition<C>> getRobots() {
        return grid.entrySet().stream().map((e) -> new RobotPosition<>(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public RobotView<C> getView(C pos) {
        return new RobotView<>(pos, this::get, robotView);
    }

    public void addRobot(C pos, Robot r) {
        grid.put(pos, r);
    }

    public boolean removeRobot(C pos) {
        return grid.remove(pos) != null;
    }

    public Ruleset<C> getRuleset() {
        return ruleset;
    }

    public void addRule(RobotView<C> view, RobotPosition<C> move) {
        ruleset.addRule(view, move);
    }

    public boolean step() {
        Map<C, Robot> newGrid = new HashMap<>();
        Set<Edge<C>> usedEdges = new HashSet<>();
        for(var pair : grid.entrySet()) {
            ArrayList<RobotPosition<C>> moves = new ArrayList<>(ruleset.getMoves(new RobotView<>(pair.getKey(), this::get, robotView)));
            if(moves.isEmpty()) {
                if (newGrid.put(pair.getKey(), pair.getValue()) != null)
                    return false;
            }
            else {
                Collections.shuffle(moves);
                var move = moves.getFirst();

                // New map insertion, vertex collision detection
                if(newGrid.put(pair.getKey().add(move.position()), move.robot()) != null)
                    return false;

                // Edge collision detection
                if(!usedEdges.add(
                        new Edge<>(
                                pair.getKey(), pair.getKey().add(move.position())
                        )
                ))
                    return false;
            }
        }
        round++;
        grid = newGrid;
        return true;
    }

    public HashMap<C, List<RobotPosition<C>>> getMoves() {
        HashMap<C, List<RobotPosition<C>>> result = new HashMap<>();
        for(var pair : grid.entrySet()) {
            var moves = ruleset.getMoves(new RobotView<>(pair.getKey(), this::get, robotView));
            result.put(pair.getKey(), moves);
        }
        return result;
    }

    public void saveGrid() {
        savedGrid = new HashMap<>(grid);
        savedRound = round;
    }

    public void reloadGrid() {
        grid = new HashMap<>(savedGrid);
        round = savedRound;
    }

    public int getRound() {
        return round;
    }

    @Override
    public String toString() {
        return "Grid" + grid;
    }
}
