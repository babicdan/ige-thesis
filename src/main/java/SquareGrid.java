import java.util.Map;
import java.util.Optional;

public class SquareGrid implements Grid<CartesianCoordinates> {
    private Map<CartesianCoordinates, Robot> grid;
    @Override
    public Optional<Robot> get(CartesianCoordinates pos) {
        return Optional.empty();
    }
}