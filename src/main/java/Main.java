import models.Robot;
import models.coordinate.SquareCoords;
import models.grid.SquareGrid;

void main(String[] args) {
    SquareGrid grid = new SquareGrid();
    grid.addRobot(new SquareCoords(0, 0), new Robot(0));
    grid.addRobot(new SquareCoords(1, 0), new Robot(1));
    grid.step();
}