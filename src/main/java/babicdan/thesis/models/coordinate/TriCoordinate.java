package babicdan.thesis.models.coordinate;

import java.util.List;
import java.util.stream.Collectors;

public record TriCoordinate(int x, int y) implements Coordinate<TriCoordinate> {
    private static final List<Integer> ROTATIONS = List.of(0, 60, 120, 180, 240, 300);
    private static final List<TriCoordinate> NEIGHBOURS = List.of(
            new TriCoordinate(0, 0),
            new TriCoordinate(1, 0),
            new TriCoordinate(1, 1),
            new TriCoordinate(0, 1),
            new TriCoordinate(-1, 0),
            new TriCoordinate(-1, -1),
            new TriCoordinate(0, -1)
    );
    @Override
    public List<Integer> getRotations() {
        return ROTATIONS;
    }

    @Override
    public TriCoordinate add(TriCoordinate other) {
        return new TriCoordinate(x + other.x, y + other.y);
    }

    public TriCoordinate subtract(TriCoordinate other) {
        return new TriCoordinate(x - other.x, y - other.y);
    }

    @Override
    public TriCoordinate rotate(int degrees) {
        degrees = Math.floorMod(degrees, 360);
        if(!ROTATIONS.contains(degrees)) throw new IllegalArgumentException("Unexpected degree value: " + degrees);
        return switch (degrees) {
            case 0 -> this;
            case 60 -> new TriCoordinate(x - y, x);
            case 120 -> new TriCoordinate(-y, x - y);
            case 180 -> new TriCoordinate(-x, -y);
            case 240 -> new TriCoordinate(-x + y, -x);
            case 300 -> new TriCoordinate(y, -x + y);
            default -> throw new IllegalStateException("Unexpected degree value: " + degrees + ", mismatch in available rotations.");
        };
    }

    @Override
    public TriCoordinate mirror() {
        return new TriCoordinate(y, x);
    }

    @Override
    public List<TriCoordinate> neighbours() {
        return NEIGHBOURS.stream().map(this::add).collect(Collectors.toList());
    }

    @Override
    public int compareTo(TriCoordinate o) {
        int compX = Integer.compare(x, o.x);
        return compX == 0 ? Integer.compare(y, o.y) : compX;
    }
}
