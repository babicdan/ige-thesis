package babicdan.thesis.models.grid;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.HexCoordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;
import babicdan.thesis.models.ruleset.RobotPosition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AlgorithmHelper {
    static public class HexDirections {
        public final static HexCoordinate idle = new HexCoordinate(0,0,false);
        public final static HexCoordinate up = new HexCoordinate(0,0,true);
        public final static HexCoordinate upOdd = new HexCoordinate(0,-1,true);
        public final static HexCoordinate down = new HexCoordinate(0,-1,true);
        public final static HexCoordinate downOdd = new HexCoordinate(0,0,true);
        public final static HexCoordinate left = new HexCoordinate(-1,0,true);
        public final static HexCoordinate right = new HexCoordinate(-1,0,true);

    }

    public static Grid<TriCoordinate> algoTriOne() {

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


        grid.removeRobot(beacon3);
        grid.addRobot(beacon3.add(new TriCoordinate(-1, 0)), new Robot('R'));

        grid.saveGrid();
        grid.reloadGrid();
        return grid;
    }

    public static Grid<TriCoordinate> algoTriTwo() {
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

    public static Grid<TriCoordinate> algoTriThreeAlt() {
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

    public static Grid<TriCoordinate> algoTriThree() {
        Grid<TriCoordinate> grid = new Grid<>(new TriCoordinate(0, 0).neighbours());

        var shipFront = new TriCoordinate(0, 0);
        var shipBack = new TriCoordinate(-1, 0);

        var beacon1 = new TriCoordinate(-1, -1);
        var beacon2 = new TriCoordinate(1, 2);
        var beacon3 = new TriCoordinate(-3, 0);

        grid.addRobot(shipFront, new Robot('L'));
        grid.addRobot(shipBack, new Robot('F'));

        grid.saveGrid();

        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('L')));
        grid.addRule(grid.getView(shipBack), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('F')));

        grid.addRobot(beacon1.add(new TriCoordinate(1, 0)), new Robot('B'));


        grid.addRule(grid.getView(shipFront), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('L')));

        grid.step();

        grid.addRule(grid.getView(shipFront.add(new TriCoordinate(1, 0))),
                new RobotPosition<>(new TriCoordinate(0, 1), new Robot('L')));
        grid.addRule(grid.getView(shipBack.add(new TriCoordinate(1, 0))),
                new RobotPosition<>(new TriCoordinate(1, 0), new Robot('F')));

        grid.addRule(grid.getView(beacon1.add(new TriCoordinate(1, 0))), new RobotPosition<>(new TriCoordinate(1, 0), new Robot('B')));

        grid.step();

        grid.addRule(grid.getView(shipBack.add(new TriCoordinate(2, 0))),
                new RobotPosition<>(new TriCoordinate(0, 1), new Robot('F')));

        grid.addRule(grid.getView(beacon1.add(new TriCoordinate(2, 0))),
                new RobotPosition<>(new TriCoordinate(0, -1), new Robot('B')));

        grid.reloadGrid();

        grid.addRobot(beacon1, new Robot('B'));
        grid.addRobot(beacon2, new Robot('B'));
        grid.addRobot(beacon3, new Robot('B'));

        grid.saveGrid();

        return grid;
    }

    public static Grid<HexCoordinate> algoHexThree() {
        List<HexCoordinate> n = new HexCoordinate(0, 0, false).neighbours();
        HashSet<HexCoordinate> nset = new HashSet<>();

        for(var i : n) {
            for(var j : n) {
                nset.add(i.add(j));
            }
        }

        Grid<HexCoordinate> grid = new Grid<>(new ArrayList<>(nset));

        var front = new HexCoordinate(0,0,true);
//        var middle = new HexCoordinate(0,-1,true);
        var back = new HexCoordinate(0,-1,true);

        var beacon1 = new HexCoordinate(-1,0,true);
        var beacon2 = new HexCoordinate(-1,-1,true);
        var beacon3 = new HexCoordinate(1,-2,true);

        grid.addRobot(front, new Robot('L'));
        grid.addRobot(back, new Robot('F'));

        grid.addRule(grid.getView(front), new RobotPosition<>(HexDirections.upOdd, new Robot('L')));
        grid.addRule(grid.getView(back), new RobotPosition<>(HexDirections.upOdd, new Robot('F')));

        grid.addRobot(beacon1, new Robot('B'));

        grid.addRule(grid.getView(front), new RobotPosition<>(HexDirections.upOdd, new Robot('B')));
        grid.addRule(grid.getView(back), new RobotPosition<>(HexDirections.upOdd, new Robot('F')));
        grid.addRule(grid.getView(beacon1), new RobotPosition<>(HexDirections.downOdd, new Robot('L')));

        grid.addRobot(beacon2, new Robot('B'));
        grid.addRobot(beacon3, new Robot('F'));

        grid.addRule(grid.getView(back), new RobotPosition<>(HexDirections.upOdd, new Robot('F')));
        grid.addRule(grid.getView(beacon1), new RobotPosition<>(HexDirections.downOdd, new Robot('L')));
        grid.addRule(grid.getView(beacon3), new RobotPosition<>(HexDirections.idle, new Robot('B')));

        grid.saveGrid();

        grid.step();    // 1

        grid.addRule(grid.getView(front.add(HexDirections.upOdd)), new RobotPosition<>(HexDirections.up, new Robot('B')));
        grid.addRule(grid.getView(back.add(HexDirections.upOdd)), new RobotPosition<>(HexDirections.left, new Robot('F')));
        grid.addRule(grid.getView(beacon1.add(HexDirections.downOdd)), new RobotPosition<>(HexDirections.left, new Robot('L')));


        grid.step();    // 2
        grid.step();    // 3
        grid.step();    // 4

        // leader seeing a beacon from the distance
        grid.addRule(grid.getView(new HexCoordinate(0,-2,true)), new RobotPosition<>(HexDirections.right, new Robot('L')));


        grid.reloadGrid();

        return grid;
    }

    public static Grid<HexCoordinate> hexDemoOne() {
        List<HexCoordinate> n = new HexCoordinate(0, 0, false).neighbours();
        HashSet<HexCoordinate> nset = new HashSet<>();

        for(var i : n) {
            for(var j : n) {
                nset.add(i.add(j));
            }
        }

        Grid<HexCoordinate> grid = new Grid<>(new ArrayList<>(nset));

        var front = new HexCoordinate(0,0,false);
        var middle = new HexCoordinate(0,-1,false);
        var back = new HexCoordinate(0,-2,true);

        grid.addRobot(front, new Robot('R'));
        grid.addRobot(middle, new Robot('R'));
        grid.addRobot(back, new Robot('R'));

        grid.addRule(grid.getView(front), new RobotPosition<>(new HexCoordinate(0, 0, true), new Robot('R')));
        grid.addRule(grid.getView(middle), new RobotPosition<>(new HexCoordinate(0, 0, true), new Robot('R')));
        grid.addRule(grid.getView(back), new RobotPosition<>(new HexCoordinate(0, -1, true), new Robot('R')));

        grid.saveGrid();

        return grid;
    }

    public static Grid<HexCoordinate> hexDemoTwo() {
        List<HexCoordinate> n = new HexCoordinate(0, 0, false).neighbours();
        HashSet<HexCoordinate> nset = new HashSet<>();

        for(var i : n) {
            for(var j : n) {
                nset.add(i.add(j));
            }
        }

        Grid<HexCoordinate> grid = new Grid<>(new ArrayList<>(nset));

        var front = new HexCoordinate(0,0,false);
        var middle = new HexCoordinate(0,-1,true);
        var back = new HexCoordinate(0,-1,false);

        var beacon = new HexCoordinate(-1,1,true);


        grid.addRobot(front, new Robot('L'));
        grid.addRobot(middle, new Robot('F'));
        grid.addRobot(back, new Robot('F'));
        grid.addRobot(beacon, new Robot('L'));

        grid.addRule(grid.getView(front), new RobotPosition<>(new HexCoordinate(0, 0, true), new Robot('L')));
        grid.addRule(grid.getView(middle), new RobotPosition<>(new HexCoordinate(0, -1, true), new Robot('F')));
        grid.addRule(grid.getView(back), new RobotPosition<>(new HexCoordinate(0, 0, true), new Robot('F')));

        grid.saveGrid();

        grid.step();    // 1

        grid.addRule(grid.getView(front.add(new HexCoordinate(0, 0, true))),
                new RobotPosition<>(new HexCoordinate(0, -1, true), new Robot('L')));

        grid.step();    // 2

        grid.addRule(grid.getView(middle.add(new HexCoordinate(0, -1, false))),
                new RobotPosition<>(new HexCoordinate(0, -1, true), new Robot('F')));

        grid.addRule(grid.getView(beacon), new RobotPosition<>(new HexCoordinate(0, 0, true), new Robot('L')));

        grid.addRule(grid.getView(front.add(new HexCoordinate(0, 1, false))),
                new RobotPosition<>(new HexCoordinate(0, 0, true), new Robot('L')));

        grid.step();    // 3

        grid.addRule(grid.getView(middle.add(new HexCoordinate(0, -2, true))),
                new RobotPosition<>(new HexCoordinate(-1, 0, true), new Robot('F')));

        grid.reloadGrid();
        return grid;
    }

}
