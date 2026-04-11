package babicdan.thesis.models.coordinate;

import java.util.List;

public record TriCoordinate(int x, int y) implements Coordinate<TriCoordinate> {
    private static final List<Integer> ROTATIONS = List.of(0, 60, 120, 180, 240, 300);
    @Override
    public List<Integer> getRotations() {
        return ROTATIONS;
    }

    @Override
    public TriCoordinate add(TriCoordinate other) {
        return new TriCoordinate(x + other.x, y + other.y);
    }

    @Override
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
    public ScreenCoordinate getScreenCoordinate() {
        return new ScreenCoordinate(x - y * Math.sin(Math.PI/6), - y * Math.cos(Math.PI/6));
    }

    @Override
    public int compareTo(TriCoordinate o) {
        int compX = Integer.compare(x, o.x);
        return compX == 0 ? Integer.compare(y, o.y) : compX;
    }
}
