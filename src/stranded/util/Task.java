package stranded.util;

public class Task {

    public final String name;
    public double timeSpent;
    public double lastStartTime;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(name + " time: %.4f", timeSpent);
    }
}