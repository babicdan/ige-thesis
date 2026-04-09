package models.ruleset.view;


import models.Robot;
import models.coordinate.Coordinate;
import models.ruleset.RobotMove;

import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class RobotView<C extends Coordinate<C>> implements Comparable<RobotView<C>> {
    protected SortedMap<C, Robot> view = new TreeMap<>();
    protected Supplier<List<Integer>> getRotations = (List::of);
    protected int rotation = 0;
    protected boolean mirrored = false;

    private RobotView(SortedMap<C, Robot> view, int rotation, boolean mirrored) {
        this.view = view;
        this.rotation = rotation;
        this.mirrored = mirrored;
    }

    public RobotView(C pos, Function<C, Optional<Robot>> grid, List<C> neighbours) {
        getRotations = pos::getRotations;
        for(var n : neighbours) {
            Optional<Robot> r = grid.apply(pos.add(n));
            r.ifPresent(robot -> view.put(n, robot));
        }
    }

    public RobotView<C> rotate(int degrees) {
        SortedMap<C, Robot> resView = new TreeMap<>();
        for(var pair : view.entrySet()) {
            resView.put(pair.getKey().rotate(degrees), pair.getValue());
        }
        return new RobotView<>(resView, rotation + degrees, mirrored);
    }

    public RobotView<C> mirror() {
        SortedMap<C, Robot> resView = new TreeMap<>();
        for(var pair : view.entrySet()) {
            resView.put(pair.getKey().mirror(), pair.getValue());
        }
        return new RobotView<>(resView, rotation, !mirrored);
    }

    public List<RobotView<C>> normalize() {
        ArrayList<RobotView<C>> min = new ArrayList<>();
        for(int d : getRotations.get()) {
            for(boolean b : List.of(true, false)) {
                var alt = this.rotate(d);
                if(b) alt = alt.mirror();
                int comp = 0;
                if(!min.isEmpty())
                    comp = alt.compareTo(min.getFirst());
                if(comp < 0)
                    min.clear();
                if(comp <= 0)
                    min.add(alt);
            }
        }
        return min;
    }

    public RobotMove<C> transformMove(RobotMove<C> move) {
        if(mirrored)
            return move.rotate(this.rotation).mirror();
        else
            return move.rotate(rotation);
    }

    public RobotMove<C> inverseTransformMove(RobotMove<C> move) {
        if(mirrored)
            return move.rotate(-this.rotation).mirror();
        else
            return move.rotate(-rotation);
    }

    @Override
    public int compareTo(RobotView<C> o) {
        var i1 = view.entrySet().iterator();
        var i2 = o.view.entrySet().iterator();

        while(i1.hasNext() && i2.hasNext()) {
            var pair1 = i1.next();
            var pair2 = i2.next();
            int keyCompare = pair1.getKey().compareTo(pair2.getKey());
            if(keyCompare != 0) return keyCompare;
            int valueCompare = pair1.getValue().compareTo(pair2.getValue());
            if(valueCompare != 0) return valueCompare;
        }

        if(i1.hasNext()) return -1;
        if(i2.hasNext()) return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RobotView<?> robotView = (RobotView<?>) o;
        return Objects.equals(view, robotView.view);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(view);
    }

    @Override
    public String toString() {
        return "RobotView{" +
                "view=" + view + '\n' +
                ", rotation=" + rotation +
                ", mirrored=" + mirrored +
                '}';
    }
}
