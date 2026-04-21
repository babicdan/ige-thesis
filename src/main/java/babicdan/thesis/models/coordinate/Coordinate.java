package babicdan.thesis.models.coordinate;

import java.util.List;

public interface Coordinate<C> extends Comparable<C> {
    default List<Integer> getRotations() {
        return List.of();
    }
    C add(C other);
    C subtract(C other);
    C rotate(int degrees);
    C mirror();
}
