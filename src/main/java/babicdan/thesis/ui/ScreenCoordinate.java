package babicdan.thesis.ui;

import babicdan.thesis.models.coordinate.HexCoordinate;
import babicdan.thesis.models.coordinate.SquareCoordinate;
import babicdan.thesis.models.coordinate.TriCoordinate;

public record ScreenCoordinate(double x, double y) {
    public ScreenCoordinate(TriCoordinate c) {
        this(c.x() - c.y() * Math.sin(Math.PI/6), - c.y() * Math.cos(Math.PI/6));
    }

    public ScreenCoordinate(SquareCoordinate c) {
        this(c.x(), -c.y());
    }

    public ScreenCoordinate(HexCoordinate c) {
        this(new TriCoordinate(
                2*c.x() + c.y() + (c.top()?1:0), c.x() + 2*c.y() + (c.top()?1:0)
        ));
    }

    public ScreenCoordinate scale(double factor) {
        return new ScreenCoordinate(factor * x, factor * y);
    }

    public ScreenCoordinate offset(double dx, double dy) {
        return new ScreenCoordinate(x + dx, y + dy);
    }

    public ScreenCoordinate offset(ScreenCoordinate d) {
        return new ScreenCoordinate(x + d.x, y + d.y);
    }

    public TriCoordinate getTriCoordinate() {
        double b = - y / Math.cos(Math.PI/6);
        double a = x + b * Math.sin(Math.PI/6);
        return new TriCoordinate((int) Math.round(a), (int) Math.round(b));
    }

    public SquareCoordinate getSquareCoordinate() {
        return new SquareCoordinate((int) Math.round(x), (int) Math.round(-y));
    }
}
