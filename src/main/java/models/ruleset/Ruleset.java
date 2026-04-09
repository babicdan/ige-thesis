package models.ruleset;

import models.coordinate.Coordinate;
import models.ruleset.view.RobotView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Ruleset<C extends Coordinate<C>> {
    private final Map<RobotView<C>, RobotMove<C>> rules = new HashMap<>();

    public void addRule(RobotView<C> view, RobotMove<C> move) {
        RobotView<C> normal = view.normalize().getFirst();
        rules.put(normal, normal.transformMove(move));
    }

    public List<RobotMove<C>> getMoves(RobotView<C> view) {
        List<RobotView<C>> normalViews = view.normalize();
        if(!rules.containsKey(normalViews.getFirst())) return List.of();

        RobotMove<C> normalMove = rules.get(normalViews.getFirst());
        HashSet<RobotMove<C>> moves = new HashSet<>();

        for(var v : normalViews) {
            moves.add(v.inverseTransformMove(normalMove));
        }

        return moves.stream().toList();
    }
}
