package ui;

import controller.TaskManager;
import java.awt.*;
import javax.swing.*;
import model.Task;

public class FormWindow extends JFrame {

    private TaskManager taskManager;
    private MainWindow mainWindow;

    private JTextField idField;
    private JTextField nameField;
    private JTextArea descField;
    private JTextField statusField;

    public FormWindow(TaskManager taskManager, MainWindow mainWindow) {
        this.taskManager = taskManager;
        this.mainWindow = mainWindow;

        setTitle("TO-DO-LIST FORM");
        setSize(400, 250);
        setLayout(new GridLayout(5, 2, 5, 5));
        setLocationRelativeTo(null);

        add(new JLabel("Task ID: "));
        idField = new JTextField();
        idField.setEditable(false);
        idField.setText(String.format("%05d", taskManager.getNextTaskId()));
        add(idField);

        add(new JLabel("Task Name: "));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Task Description: "));
        descField = new JTextArea(5, 20);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        add(descField);

        add(new JLabel("Status: "));
        statusField = new JTextField();
        String[] statusOptions = {"NOT STARTED", "ONGOING", "DONE"}; 
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        add(statusComboBox);


        JButton addButton = new JButton("Save Task");
        add(addButton);

        addButton.addActionListener(e
                -> {
            try {
                
                String name = nameField.getText();
                String desc = descField.getText();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Task name cannot be empty!\nAt least put an effort into adding a name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (name.length() > 30) {
                    JOptionPane.showMessageDialog(this, "Task name too long! This isn't the description.\nEnter a much shorter name\n(You cannot exceed 30 characters).", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                for (Task t : taskManager.getTasks()) {
                    if (t.getTaskName().equalsIgnoreCase(name)) {
                        JOptionPane.showMessageDialog(this, "Task name already exists!\nShow some creativity will you?", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Task task = new Task();
                task.setTaskName(name);
                task.setTaskDescription(desc);
                task.setStatus((String) statusComboBox.getSelectedItem());

                taskManager.addTask(task);

                mainWindow.loadTask();
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Task ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

}
