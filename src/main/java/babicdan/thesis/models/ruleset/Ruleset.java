package babicdan.thesis.models.ruleset;

import babicdan.thesis.models.coordinate.Coordinate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Ruleset<C extends Coordinate<C>> {
    private final Map<RobotView<C>, RobotPosition<C>> rules = new HashMap<>();

    public void addRule(RobotView<C> view, RobotPosition<C> move) {
        RobotView<C> normal = view.normalize().getFirst();
        rules.put(normal, normal.transformMove(move));
    }

    public List<Map.Entry<RobotView<C>, RobotPosition<C>>> getRules() {
        return rules.entrySet().stream().toList();
    }

    public List<RobotPosition<C>> getMoves(RobotView<C> view) {
        List<RobotView<C>> normalViews = view.normalize();
        if(!rules.containsKey(normalViews.getFirst())) return List.of();

        RobotPosition<C> normalMove = rules.get(normalViews.getFirst());
        HashSet<RobotPosition<C>> moves = new HashSet<>();

        for(var v : normalViews) {
            moves.add(v.inverseTransformMove(normalMove));
        }

        return moves.stream().toList();
    }

}
