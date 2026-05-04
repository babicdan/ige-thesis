package babicdan.thesis.models.coordinate;

import babicdan.thesis.ui.ScreenCoordinate;

import java.util.List;
import java.util.stream.Collectors;

public record SquareCoordinate(int x, int y) implements Coordinate<SquareCoordinate> {
    private static final List<Integer> ROTATIONS = List.of(0, 90, 180, 270);
    private static final List<SquareCoordinate> NEIGHBOURS = List.of(
            new SquareCoordinate(0, 0),
            new SquareCoordinate(1, 0),
            new SquareCoordinate(0, 1),
            new SquareCoordinate(-1, 0),
            new SquareCoordinate(0, -1)
    );

    @Override
    public SquareCoordinate add(SquareCoordinate other) {
        return new SquareCoordinate(x + other.x, y + other.y);
    }

    public SquareCoordinate subtract(SquareCoordinate other) {
        return new SquareCoordinate(x - other.x, y - other.y);
    }

    @Override
    public SquareCoordinate rotate(int degrees) {
        degrees = Math.floorMod(degrees, 360);
        if(!ROTATIONS.contains(degrees)) throw new IllegalArgumentException("Unexpected degree value: " + degrees);
        return switch (degrees) {
            case 0 -> this;
            case 90 -> new SquareCoordinate(-y, x);
            case 180 -> new SquareCoordinate(-x, -y);
            case 270 -> new SquareCoordinate(y, -x);
            default -> throw new IllegalStateException("Unexpected degree value: " + degrees + ", mismatch in available rotations.");
        };
    }

    @Override
    public SquareCoordinate mirror() {
        return new SquareCoordinate(-x, y);
    }

    @Override
    public List<SquareCoordinate> neighbours() {
        return NEIGHBOURS.stream().map(this::add).collect(Collectors.toList());
    }

    @Override
    public ScreenCoordinate getScreenCoordinate() {
        return new ScreenCoordinate(x, -y);
    }

    @Override
    public List<Integer> getRotations() {
        return ROTATIONS;
    }

    @Override
    public int compareTo(SquareCoordinate o) {
        int compX = Integer.compare(x, o.x);
        return compX == 0 ? Integer.compare(y, o.y) : compX;
    }

    @Override
    public String toString() {
        return '[' +
                "x=" + x +
                ", y=" + y +
                ']';
    }
}