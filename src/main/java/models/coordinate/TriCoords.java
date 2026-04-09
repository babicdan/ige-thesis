package models.coordinate;

import java.util.List;

public record TriCoords(int x, int y) implements Coordinate<TriCoords> {
    private static final List<Integer> ROTATIONS = List.of(0, 60, 120, 180, 240, 300);
    @Override
    public List<Integer> getRotations() {
        return ROTATIONS;
    }

    @Override
    public TriCoords add(TriCoords other) {
        return new TriCoords(x + other.x, y + other.y);
    }

    @Override
    public TriCoords subtract(TriCoords other) {
        return new TriCoords(x - other.x, y - other.y);
    }

    @Override
    public TriCoords rotate(int degrees) {
        degrees = Math.floorMod(degrees, 360);
        if(!ROTATIONS.contains(degrees)) throw new IllegalArgumentException("Unexpected degree value: " + degrees);
        return switch (degrees) {
            case 0 -> this;
            case 60 -> new TriCoords(x - y, x);
            case 120 -> new TriCoords(-y, x - y);
            case 180 -> new TriCoords(-x, -y);
            case 240 -> new TriCoords(-x + y, -x);
            case 300 -> new TriCoords(y, -x + y);
            default -> throw new IllegalStateException("Unexpected degree value: " + degrees + ", mismatch in available rotations.");
        };
    }

    @Override
    public TriCoords mirror() {
        return new TriCoords(y, x);
    }

    @Override
    public int compareTo(TriCoords o) {
        int compX = Integer.compare(x, o.x);
        return compX == 0 ? Integer.compare(y, o.y) : compX;
    }
}
