package controller;

import java.util.ArrayList;
import model.Task;

public class TaskManager {

    private final ArrayList<Task> tasks;
    private int nextTaskId;

    public TaskManager() {
        tasks = new ArrayList<>();
        nextTaskId = 1;
    }

    public void updateTaskId() {
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setTaskId(i + 1);
        }
        nextTaskId = tasks.size() + 1;
    }

    public void addTask(Task task) {
        task.setTaskId(nextTaskId);
        tasks.add(task);
        updateTaskId();

    }

    public void updateTask(int i, Task task) {
        if (i >= 0 && i < tasks.size()) {
            tasks.set(i, task);
        }
    }

    public void removeTask(int i) {
        if (i >= 0 && i < tasks.size()) {
            tasks.remove(i);
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public int getNextTaskId() {
        return nextTaskId;
    }

}
