package babicdan.thesis.models.coordinate;

public record ScreenCoordinate(double x, double y) {
    public ScreenCoordinate scale(double factor) {
        return new ScreenCoordinate(factor * x, factor * y);
    }

    public ScreenCoordinate offset(double dx, double dy) {
        return new ScreenCoordinate(x + dx, y + dy);
    }
}
