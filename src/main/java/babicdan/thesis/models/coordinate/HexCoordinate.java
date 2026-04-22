package babicdan.thesis.models.coordinate;

import java.util.List;

public record HexCoordinate(int x, int y, boolean top) implements Coordinate<HexCoordinate> {
    private static final List<Integer> ROTATIONS = List.of(0, 60, 120, 180, 240, 300);
    private static final List<HexCoordinate> NEIGHBOURS = List.of(
            new HexCoordinate(0, 0, false),
            new HexCoordinate(0, 0, true),
            new HexCoordinate(-1, 0, true),
            new HexCoordinate(0, -1, true)
    );

    public List<Integer> getRotations() {
        return ROTATIONS;
    }

    @Override
    public HexCoordinate add(HexCoordinate o) {
        return new HexCoordinate((top == o.top) ? x - o.x : x + o.x,
                (top == o.top) ? y - o.y : y + o.y,
                top ^ o.top);
    }

    @Override
    public HexCoordinate subtract(HexCoordinate other) {
        return null;
    }

    @Override
    public HexCoordinate rotate(int degrees) {
        return null;
    }

    @Override
    public HexCoordinate mirror() {
        return new HexCoordinate(-x, -y, !top);
    }

    @Override
    public List<HexCoordinate> neighbours() {
        return List.of();
    }

    @Override
    public int compareTo(HexCoordinate o) {
        return 0;
    }
}
