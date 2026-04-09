import models.Robot;
import models.coordinate.SquareCoords;
import models.grid.SquareGrid;
import models.ruleset.RobotMove;

void main(String[] args) {
    SquareGrid grid = new SquareGrid();
    var left = new SquareCoords(0, 0);
    var right = new SquareCoords(1, 0);

    grid.addRobot(left, new Robot(0));
    grid.addRobot(right, new Robot(1));

    grid.addRule(grid.getView(left), new RobotMove<>(right, new Robot(0)));
    grid.addRule(grid.getView(right), new RobotMove<>(right, new Robot(1)));

    for(int i = 0; i < 20; i ++) {
        IO.println(grid.toString());
        grid.step();
    }
}