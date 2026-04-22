package babicdan.thesis.models.grid;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.TriCoordinate;
import babicdan.thesis.models.ruleset.RobotPosition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AlgorithmHelper {
    public static Grid<TriCoordinate> algorithmTriOne() {

        List<TriCoordinate> n = new TriCoordinate(0, 0).neighbours();

        HashSet<TriCoordinate> nset = new HashSet<>();

        for(var i : n) {
            for(var j : n) {
                nset.add(i.add(j));
            }
        }

        Grid<TriCoordinate> grid = new Grid<>(new ArrayList<>(nset));

        var shipFront = new TriCoordinate(0, 0);
        var shipSideLeft = new TriCoordinate(0, 1);
        var shipSideRight = new TriCoordinate(-1, -1);
        var shipBackLeft = new TriCoordinate(-1, 1);
        var shipBackRight = new TriCoordinate(-2, -1);

        var beacon1 = new TriCoordinate(0, -2);
        var beacon2 = new TriCoordinate(2, 4);
        var beacon3 = new TriCoordinate(-4, -1);


        for(var r : List.of(shipFront, shipSideLeft, shipSideRight, shipBackLeft, shipBackRight)) {
            grid.addRobot(r, new Robot(0));
        }
        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(1, 0)));


        for(var r : List.of(beacon1, beacon2, beacon3)) {
            grid.addRobot(r, new Robot(0));
        }

        for(var r: List.of(shipFront, shipSideLeft, shipBackLeft, shipBackRight)) {
            grid.addRule(grid.getView(r), new RobotPosition<>(new TriCoordinate(1, 0)));
        }

        grid.addRule(grid.getView(shipSideRight), new RobotPosition<>(new TriCoordinate(0, 1)));

        grid.addRule(grid.getView(beacon1), new RobotPosition<>(new TriCoordinate(1, 0)));
//        grid.addRule(grid.getView(beacon2), new RobotPosition<>(new TriCoordinate(1, 0)));
        grid.addRule(grid.getView(beacon3), new RobotPosition<>(new TriCoordinate(-1, 0)));

        grid.saveGrid();

        return grid;
    }

    public static Grid<TriCoordinate> algorithmTriTwo() {
        return null;
    }

    public static Grid<TriCoordinate> algorithmTriThree() {
        return null;
    }
}
