package babicdan.thesis.models;

public record Robot(char color) implements Comparable<Robot> {
    @Override
    public int compareTo(Robot o) {
        return Integer.compare(color, o.color);
    }
}
