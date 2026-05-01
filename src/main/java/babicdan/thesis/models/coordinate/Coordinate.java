package babicdan.thesis.models.coordinate;

import java.util.List;

public interface Coordinate<C extends Coordinate<C>> extends Comparable<C> {
    List<Integer> getRotations();
    C add(C other);
    C rotate(int degrees);
    C mirror();
    List<C> neighbours();
}
