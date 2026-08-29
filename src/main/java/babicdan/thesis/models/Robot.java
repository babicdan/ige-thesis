package babicdan.thesis.models;

public record Robot(char color) implements Comparable<Robot> {
    public Robot() {
        this('R');
    }

    @Override
    public int compareTo(Robot o) {
        return Integer.compare(color, o.color);
    }
}
