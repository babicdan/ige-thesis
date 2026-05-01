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
    public HexCoordinate rotate(int degrees) {
        degrees = Math.floorMod(degrees, 360);
        if(!ROTATIONS.contains(degrees)) throw new IllegalArgumentException("Unexpected degree value: " + degrees);
        return switch (degrees) {
            case 0 -> this;
            case 60 -> new HexCoordinate(0, 0, !top);
            case 120 -> new HexCoordinate(0, 0, top);
            case 180 -> new HexCoordinate(0, 0, !top);
            case 240 -> new HexCoordinate(0, 0, top);
            case 300 -> new HexCoordinate(0, 0, !top);
            default -> throw new IllegalStateException("Unexpected degree value: " + degrees + ", mismatch in available rotations.");
        };
    }

    @Override
    public HexCoordinate mirror() {
        return new HexCoordinate(-x, -y, !top);
    }

    @Override
    public List<HexCoordinate> neighbours() {
        return NEIGHBOURS;
    }

    @Override
    public int compareTo(HexCoordinate o) {
        int compX = Integer.compare(x, o.x);
        if(compX != 0) return compX;
        int compY = Integer.compare(y, o.y);
        if(compY != 0) return compY;
        return Integer.compare(top?1:0, o.top?1:0);
    }
}
