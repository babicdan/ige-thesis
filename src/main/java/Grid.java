import java.util.Optional;

public interface Grid<C extends Coordinate> {
    Optional<Robot> get(C pos);
}
