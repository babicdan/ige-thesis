package models;

public record Robot(int color) implements Comparable<Robot> {
    @Override
    public int compareTo(Robot o) {
        return Integer.compare(color, o.color);
    }
}
