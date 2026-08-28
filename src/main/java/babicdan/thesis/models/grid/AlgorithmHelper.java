package babicdan.thesis.models.grid;

import babicdan.thesis.models.Robot;
import babicdan.thesis.models.coordinate.HexCoordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;
import babicdan.thesis.models.ruleset.RobotPosition;
import babicdan.thesis.models.ruleset.RobotView;

import java.util.*;

public class AlgorithmHelper {
    static public class Hexes {
        public final static HexCoordinate IDLE = new HexCoordinate(0,0,false);
        public final static HexCoordinate UP = new HexCoordinate(0,0,true);
        public final static HexCoordinate UP_ODD = new HexCoordinate(0,-1,true);
        public final static HexCoordinate DOWN = new HexCoordinate(0,-1,true);
        public final static HexCoordinate DOWN_ODD = new HexCoordinate(0,0,true);
        public final static HexCoordinate LEFT = new HexCoordinate(-1,0,true);
        public final static HexCoordinate RIGHT = new HexCoordinate(-1,0,true);

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
                new RobotPosition<>(new TriCoordinate(1, 0), new Robot('F')));

        grid.addRule(grid.getView(shipMiddle.add(new TriCoordinate(1, 0))),
                new RobotPosition<>(new TriCoordinate(0, 1), new Robot('F')));

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

    public static Grid<HexCoordinate> algoHexOne() {
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

        grid.addRule(grid.getView(front), new RobotPosition<>(Hexes.UP_ODD, new Robot('L')));
        grid.addRule(grid.getView(back), new RobotPosition<>(Hexes.UP_ODD, new Robot('F')));

        grid.addRobot(beacon1, new Robot('B'));

        grid.addRule(grid.getView(front), new RobotPosition<>(Hexes.UP_ODD, new Robot('B')));
        grid.addRule(grid.getView(back), new RobotPosition<>(Hexes.UP_ODD, new Robot('F')));
        grid.addRule(grid.getView(beacon1), new RobotPosition<>(Hexes.DOWN_ODD, new Robot('L')));

        grid.addRobot(beacon2, new Robot('B'));
        grid.addRobot(beacon3, new Robot('F'));

        grid.addRule(grid.getView(back), new RobotPosition<>(Hexes.UP_ODD, new Robot('F')));
        grid.addRule(grid.getView(beacon1), new RobotPosition<>(Hexes.DOWN_ODD, new Robot('L')));
        grid.addRule(grid.getView(beacon3), new RobotPosition<>(Hexes.IDLE, new Robot('B')));

        grid.saveGrid();

        grid.step();    // 1

        grid.addRule(grid.getView(front.add(Hexes.UP_ODD)), new RobotPosition<>(Hexes.UP, new Robot('B')));
        grid.addRule(grid.getView(back.add(Hexes.UP_ODD)), new RobotPosition<>(Hexes.LEFT, new Robot('F')));
        grid.addRule(grid.getView(beacon1.add(Hexes.DOWN_ODD)), new RobotPosition<>(Hexes.LEFT, new Robot('L')));


        grid.step();    // 2
        grid.step();    // 3
        grid.step();    // 4

        // leader seeing a beacon from the distance
        grid.addRule(grid.getView(new HexCoordinate(0,-2,true)), new RobotPosition<>(Hexes.RIGHT, new Robot('L')));


        grid.reloadGrid();

        return grid;
    }

    public static Grid<HexCoordinate> hexDemoOne() {
        List<HexCoordinate> n = new HexCoordinate(0, 0, false).neighbours();
        HashSet<HexCoordinate> nset = new HashSet<>();

        for(var i : n) {
            for(var j : n) {
                for(var k : n) {
                    nset.add(i.add(j).add(k));
                }
            }
        }


        Grid<HexCoordinate> grid = new Grid<>(new ArrayList<>(nset));

        var front = new HexCoordinate(0,0,false);
        var middle = new HexCoordinate(0,-1,false);
        var back = new HexCoordinate(0,-2,true);
        var back_alt = new HexCoordinate(-1,-1,true);

        var beacon1 = new HexCoordinate(1,1,false);
        var beacon2 = new HexCoordinate(-2,3,true);
        var beacon3 = new HexCoordinate(-4,3,false);
        var beacon4 = new HexCoordinate(-4,0,true);
        var beacon5 = new HexCoordinate(0,-3,false);
        var beacon6 = new HexCoordinate(2,-3,true);

        grid.addRobot(front, new Robot('R'));
        grid.addRobot(middle, new Robot('R'));
        grid.addRobot(back, new Robot('R'));

        grid.addRobot(beacon1, new Robot('R'));
        grid.addRobot(beacon2, new Robot('R'));
        grid.addRobot(beacon3, new Robot('R'));
        grid.addRobot(beacon4, new Robot('R'));
        grid.addRobot(beacon6, new Robot('R'));

        grid.addRule(grid.getView(front), new RobotPosition<>(Hexes.UP, new Robot('R')));
        grid.addRule(grid.getView(middle), new RobotPosition<>(Hexes.UP, new Robot('R')));
        grid.addRule(grid.getView(back), new RobotPosition<>(Hexes.UP_ODD, new Robot('R')));

        grid.removeRobot(back);
        grid.addRobot(back_alt, new Robot('R'));
        grid.addRobot(beacon5, new Robot('R'));
        grid.addRule(grid.getView(front), new RobotPosition<>(Hexes.UP, new Robot('R')));
        grid.addRule(grid.getView(middle), new RobotPosition<>(Hexes.UP, new Robot('R')));
        grid.addRule(grid.getView(back_alt), new RobotPosition<>(Hexes.RIGHT, new Robot('R')));



        grid.saveGrid();

        grid.step();

        grid.addRule(grid.getView(beacon1), new RobotPosition<>(Hexes.UP));
        grid.addRule(grid.getView(front = front.add(Hexes.UP)), new RobotPosition<>(Hexes.UP_ODD));

        grid.step();

        grid.addRule(grid.getView(beacon1.add(Hexes.UP)), new RobotPosition<>(Hexes.RIGHT));
        grid.addRule(grid.getView(front.add(Hexes.UP_ODD)), new RobotPosition<>(Hexes.LEFT));

//        grid.step();

//        grid.addRule(grid.getView(front.add(Hexes.LEFT)), new RobotPosition<>(Hexes.UP_ODD));
//        grid.addRule(grid.getView(middle.add(Hexes.UP).add(Hexes.UP_ODD).add(Hexes.UP)), new RobotPosition<>(Hexes.UP_ODD));
//        grid.addRule(grid.getView(back.add(Hexes.UP_ODD).add(Hexes.UP).add(Hexes.UP_ODD)), new RobotPosition<>(Hexes.UP));


        grid.reloadGrid();

        return grid;
    }

    public static Grid<HexCoordinate> hexMovingGroup() {
        List<HexCoordinate> n = new HexCoordinate(0, 0, false).neighbours();
        Grid<HexCoordinate> grid = new Grid<>(n);

        var frontL = new HexCoordinate(-1,1, false);
        var frontR = new HexCoordinate(0,0,true);
        var middleL = new HexCoordinate(-1,0,true);
        var middleR = new HexCoordinate(0,0,false);
        var backL = new HexCoordinate(-1, 0,false);
        var backR = new HexCoordinate(0,-1,true);

        for(var r : List.of(frontL, frontR))
            grid.addRobot(r, new Robot('L'));

        for(var r : List.of(middleL, middleR, backL, backR))
            grid.addRobot(r, new Robot('F'));

        grid.saveGrid();

        // round 1

        // ambiguous
        grid.addRule(new RobotView<>(Map.of(
                Hexes.IDLE, new Robot('L'),
                Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.UP, new Robot('S'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.DOWN, new Robot('F'),
                        Hexes.LEFT, new Robot('F'),
                        Hexes.UP, new Robot('L')
                )), new RobotPosition<>(Hexes.UP, new Robot('S'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.UP, new Robot('F')
                )), new RobotPosition<>(Hexes.UP, new Robot('F'))
        );

        // round 2

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.UP, new Robot('S'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('S'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.LEFT, new Robot('F'),
                        Hexes.UP, new Robot('S')
                )), new RobotPosition<>(Hexes.UP, new Robot('L'))
        );

        // round 3

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.LEFT, new Robot('S'),
                        Hexes.DOWN, new Robot('L')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('L'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.LEFT, new Robot('S'),
                        Hexes.UP, new Robot('S')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('F'))
        );

        // round 4

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.LEFT, new Robot('L'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.UP, new Robot('L'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.UP, new Robot('L'),
                        Hexes.LEFT, new Robot('S')
                )), new RobotPosition<>(Hexes.UP, new Robot('F'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.LEFT, new Robot('F')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('F'))
        );

        grid.reloadGrid();
        return grid;
    }

    public static Grid<HexCoordinate> algoHexTwo() {
        // visibility range = 1
        List<HexCoordinate> n = new HexCoordinate(0, 0, false).neighbours();
        Grid<HexCoordinate> grid = new Grid<>(n);

//        var frontL = new HexCoordinate(-1,1, false);
//        var frontR = new HexCoordinate(0,0,true);
//        var middleL = new HexCoordinate(-1,0,true);
//        var middleR = new HexCoordinate(0,0,false);
//        var backL = new HexCoordinate(-1, 0,false);
//        var backR = new HexCoordinate(0,-1,true);

        var frontL = new HexCoordinate(-1,0,true);
        var frontR = new HexCoordinate(0,0,false);
        var middleL = new HexCoordinate(-1, 0,false);
        var middleR = new HexCoordinate(0,-1,true);
        var backL = new HexCoordinate(-2, 0,true);
        var backR = new HexCoordinate(1,-1,false);

        var beacon1 = new HexCoordinate(-1,1, true);
        var beacon2 = new HexCoordinate(-3,3, false);
        var beacon3 = new HexCoordinate(-5,2, true);
        var beacon4 = new HexCoordinate(-4,0, false);
        var beacon5 = new HexCoordinate(-2,-2, true);
        var beacon6 = new HexCoordinate(1,-2,true);

        for(var r : List.of(frontL, frontR))
            grid.addRobot(r, new Robot('L'));

        for(var r : List.of(middleL, middleR))
            grid.addRobot(r, new Robot('F'));

        for(var r : List.of(backL, beacon6))
            grid.addRobot(r, new Robot('S'));

        for(var r : List.of(beacon1, beacon2, beacon3, beacon4, beacon5, backR))
            grid.addRobot(r, new Robot('B'));;

        // round 1

        // ambiguous
        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.UP, new Robot('S'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.DOWN, new Robot('F'),
                        Hexes.LEFT, new Robot('F'),
                        Hexes.UP, new Robot('L')
                )), new RobotPosition<>(Hexes.UP, new Robot('S'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.UP, new Robot('F')
                )), new RobotPosition<>(Hexes.UP, new Robot('F'))
        );

        // round 2

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.UP, new Robot('S'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('S'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.LEFT, new Robot('F'),
                        Hexes.UP, new Robot('S')
                )), new RobotPosition<>(Hexes.UP, new Robot('L'))
        );


        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.LEFT, new Robot('S'),
                        Hexes.UP, new Robot('S')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('F')));

        // round 3

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.LEFT, new Robot('S'),
                        Hexes.DOWN, new Robot('L')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('L'))
        );

        // round 4

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.LEFT, new Robot('L'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.UP, new Robot('L'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.UP, new Robot('L'),
                        Hexes.LEFT, new Robot('S')
                )), new RobotPosition<>(Hexes.UP, new Robot('F'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.LEFT, new Robot('F')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('F'))
        );




        // BEACON RULES

        // beacon6 exceptions

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.UP_ODD, new Robot('B')
                )), new RobotPosition<>(Hexes.UP_ODD, new Robot('B'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('B'),
                        Hexes.LEFT, new Robot('F'),
                        Hexes.DOWN, new Robot('S')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('F'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.UP_ODD, new Robot('L'),
                        Hexes.RIGHT, new Robot('B')
                )), new RobotPosition<>(Hexes.UP_ODD, new Robot('F'))
        );




        // round 1, first spotting a beacon
        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.DOWN, new Robot('F'),
                        Hexes.UP, new Robot('B')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('L'))
        );

        // round 2, inner side movement

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.RIGHT, new Robot('S')
                )), new RobotPosition<>(Hexes.UP_ODD, new Robot('F'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.LEFT, new Robot('L'),
                        Hexes.UP, new Robot('B'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('F'))
        );

        // round 3, inner side moves back, outer side spots beacon

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.RIGHT, new Robot('L'),
                        Hexes.UP_ODD, new Robot('F')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('B'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.LEFT, new Robot('B'),
                        Hexes.DOWN, new Robot('L')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('B'))
        );

        // round 4

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('B'),
                        Hexes.RIGHT, new Robot('B'),
                        Hexes.DOWN_ODD, new Robot('B')
                )), new RobotPosition<>(Hexes.UP_ODD, new Robot('B'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('B'),
                        Hexes.LEFT, new Robot('B'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('F'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.UP_ODD, new Robot('B'),
                        Hexes.RIGHT, new Robot('S')
                )), new RobotPosition<>(Hexes.UP_ODD, new Robot('S'))
        );


        // left front/back

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.UP_ODD, new Robot('B')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('F'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.RIGHT, new Robot('B')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('L'))
        );

        // left center
        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('B'),
                        Hexes.LEFT, new Robot('F'),
                        Hexes.UP, new Robot('B'),
                        Hexes.DOWN, new Robot('L')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('L'))
        );

        // round 5

        // left center
        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L'),
                        Hexes.LEFT, new Robot('L'),
                        Hexes.UP, new Robot('F'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('F'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('B'),
                        Hexes.DOWN, new Robot('F')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('L'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('S'),
                        Hexes.DOWN, new Robot('F'),
                        Hexes.LEFT, new Robot('F')
                )), new RobotPosition<>(Hexes.UP, new Robot('B'))
        );

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.UP_ODD, new Robot('S')
                )), new RobotPosition<>(Hexes.UP_ODD, new Robot('F'))
        );

        // round 6

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('F'),
                        Hexes.LEFT, new Robot('F'),
                        Hexes.UP, new Robot('B')
                )), new RobotPosition<>(Hexes.LEFT, new Robot('F'))
        );

        // round 7

        grid.addRule(new RobotView<>(Map.of(
                        Hexes.IDLE, new Robot('L')
                )), new RobotPosition<>(Hexes.IDLE, new Robot('B'))
        );


        grid.saveGrid();

        return grid;
    }

}
