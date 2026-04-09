package models.coordinate;

import java.util.List;

public record SquareCoords(int x, int y) implements Coordinate<SquareCoords> {
    private static final List<Integer> ROTATIONS = List.of(0, 90, 180, 270);

    @Override
    public SquareCoords add(SquareCoords other) {
        return new SquareCoords(x + other.x, y + other.y);
    }

    @Override
    public SquareCoords subtract(SquareCoords other) {
        return new SquareCoords(x - other.x, y - other.y);
    }

    @Override
    public SquareCoords rotate(int degrees) {
        degrees = Math.floorMod(degrees, 360);
        if(!ROTATIONS.contains(degrees)) throw new IllegalArgumentException("Unexpected degree value: " + degrees);
        return switch (degrees) {
            case 0 -> this;
            case 90 -> new SquareCoords(-y, x);
            case 180 -> new SquareCoords(-x, -y);
            case 270 -> new SquareCoords(y, -x);
            default -> throw new IllegalStateException("Unexpected degree value: " + degrees + ", mismatch in available rotations.");
        };
    }

    @Override
    public SquareCoords mirror() {
        return new SquareCoords(-x, y);
    }

    @Override
    public List<Integer> getRotations() {
        return ROTATIONS;
    }

    @Override
    public int compareTo(SquareCoords o) {
        int compX = Integer.compare(x, o.x);
        return compX == 0 ? Integer.compare(y, o.y) : compX;
    }
}