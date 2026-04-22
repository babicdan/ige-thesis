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
            grid.addRobot(r, new Robot('R'));
        }
        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(1, 0)));


        for(var r : List.of(beacon1, beacon2, beacon3)) {
            grid.addRobot(r, new Robot('R'));
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
        Grid<TriCoordinate> grid = new Grid<>(new TriCoordinate(0, 0).neighbours());

        var shipFront = new TriCoordinate(0, 0);
        var shipMiddle = new TriCoordinate(-1, 0);
        var shipBack = new TriCoordinate(-2, 0);

        var beacon1 = new TriCoordinate(1, 1);
        var beacon2 = new TriCoordinate(-1, 2);
        var beacon3 = new TriCoordinate(-3, -2);

        grid.addRobot(shipFront, new Robot('L'));
        grid.addRobot(shipMiddle, new Robot('F'));
        grid.addRobot(shipBack, new Robot('F'));

        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('L')));
        grid.addRule(grid.getView(shipMiddle), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('F')));
        grid.addRule(grid.getView(shipBack), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('F')));

        grid.addRobot(beacon1, new Robot('L'));
        grid.addRobot(beacon2, new Robot('L'));
        grid.addRobot(beacon3, new Robot('L'));

        grid.saveGrid();

        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(0, 1), new Robot('L')));

        grid.step();

        grid.addRule(grid.getView(shipFront.add(new TriCoordinate(0, 1))),
                new RobotPosition<>(new TriCoordinate(0, 1), new Robot('L')));


        grid.addRule(grid.getView(shipBack.add(new TriCoordinate(1, 0))),
                new RobotPosition<>(new TriCoordinate(1, 1), new Robot('F')));

        grid.addRule(grid.getView(beacon1), new RobotPosition<>(new TriCoordinate(0, -1), new Robot('L')));

        grid.step();

        grid.addRule(grid.getView(shipBack.add(new TriCoordinate(2, 0))),
                new RobotPosition<>(new TriCoordinate(0, 1), new Robot('F')));


        grid.reloadGrid();
        return grid;
    }

    public static Grid<TriCoordinate> algorithmTriThree() {
        Grid<TriCoordinate> grid = new Grid<>(new TriCoordinate(0, 0).neighbours());

        var shipFront = new TriCoordinate(0, 0);
        var shipBack = new TriCoordinate(-1, 0);

        var beacon1 = new TriCoordinate(1, 1);
        var beacon2 = new TriCoordinate(-1, 2);
        var beacon3 = new TriCoordinate(-2, -1);

        grid.addRobot(shipFront, new Robot('L'));
        grid.addRobot(shipBack, new Robot('F'));

        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('L')));
        grid.addRule(grid.getView(shipBack), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('F')));

        grid.addRobot(beacon1, new Robot('B'));
        grid.addRobot(beacon2, new Robot('B'));
        grid.addRobot(beacon3, new Robot('B'));

        grid.saveGrid();

        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(0, 1), new Robot('L')));
        grid.addRule(grid.getView(shipBack), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('F')));

        grid.addRule(grid.getView(beacon3), new RobotPosition<>(new TriCoordinate(-1, -1), new Robot('B')));

        grid.step();

        grid.addRule(grid.getView(beacon1), new RobotPosition<>(new TriCoordinate(0, -1), new Robot('B')));



        grid.reloadGrid();
        return grid;
    }
}
