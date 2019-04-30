package stranded.util;

import java.util.ArrayList;

public class PerformanceMonitor {

    private static ArrayList<Task> tasks = new ArrayList<Task>();
    private static int currentFPS;
    private static double lastPrint = -1;
    private static int cycleCount;

    public static void startTask(String name) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).name.equals(name)) {
                tasks.get(i).lastStartTime = ToolBox.getTimeSeconds();
                return;
            }
        }
        Task task = new Task(name);
        tasks.add(task);
        task.lastStartTime = ToolBox.getTimeSeconds();
    }

    public static void endTask(String name) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).name.equals(name)) {
                tasks.get(i).timeSpent += (ToolBox.getTimeSeconds() - tasks.get(i).lastStartTime);
            }
        }
    }

    public static void endCycle() {
        if (lastPrint == -1) {
            lastPrint = ToolBox.getTimeSeconds();
        }
        double time = ToolBox.getTimeSeconds();
        cycleCount++;

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).timeSpent == 0.0) {
                tasks.remove(i);
            }
        }

        if (time >= lastPrint + 1) {
            currentFPS = cycleCount;
            printInfo();
            for (int i = 0; i < tasks.size(); i++) {
                tasks.get(i).timeSpent = 0.0;
            }
            cycleCount = 0;
            lastPrint += 1;
        }
    }

    private static void printInfo() {
        String print = "fps: " + currentFPS + "\t";
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                print += "\t";
            }
            print += tasks.get(i);
        }
        System.out.println(print);
    }
}
