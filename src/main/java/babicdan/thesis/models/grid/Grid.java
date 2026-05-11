package babicdan.thesis.models.grid;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.Coordinate;
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

    protected List<Map<C, Robot>> roundSequence = new ArrayList<>();
    protected Set<C> visited = new HashSet<>();


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
        visited.add(pos);
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
        roundSequence.add(newGrid);

        for(var pair : grid.entrySet()) {
            visited.add(pair.getKey());
        }

        return true;
    }

    public void undoStep() {
        if(roundSequence.size() <= 1) return;
        round--;
        roundSequence.removeLast();
        grid = roundSequence.getLast();
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
        roundSequence.clear();
        roundSequence.add(grid);
    }

    public void reloadGrid() {
        grid = new HashMap<>(savedGrid);
        round = savedRound;
        roundSequence.clear();
        roundSequence.add(grid);

        visited.clear();
        for(var pair : grid.entrySet()) {
            visited.add(pair.getKey());
        }
    }

    public int getRound() {
        return round;
    }

    public Set<C> getVisited() {
        return visited;
    }

    @Override
    public String toString() {
        return "Grid" + grid;
    }
}
