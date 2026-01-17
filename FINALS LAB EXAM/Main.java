
import controller.TaskManager;
import ui.MainWindow;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        MainWindow window = new MainWindow(taskManager);

        window.loadTask();

        System.out.println("DEBUGGING PURPOSES");
    }
}
