package ui;

import controller.TaskManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.Task;

public class MainWindow extends JFrame {

    private JTable taskTable;
    private DefaultTableModel tableModel;
    private TaskManager taskManager;

    private FormWindow formWindow;

    public MainWindow(TaskManager taskManager) {
        this.taskManager = taskManager;

        setTitle("TO-DO-LIST");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton addTaskButton = new JButton("ADD TASK");
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(addTaskButton);
        add(topPanel, BorderLayout.NORTH);

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (formWindow == null || !formWindow.isDisplayable()) {
                    formWindow = new FormWindow(taskManager, MainWindow.this);
                    formWindow.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            formWindow = null;
                        }

                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            formWindow = null;
                        }
                    });
                    formWindow.setVisible(true);
                } else {
                    formWindow.toFront();
                    formWindow.requestFocus();
                }
            }
        });

        String[] columns = {
            "Task ID (Double click to delete)",
            "Task Name",
            "Task Description (Double click to view)",
            "Status             v"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        taskTable = new JTable(tableModel);

        taskTable.getTableHeader().setReorderingAllowed(false);
        taskTable.getTableHeader().setResizingAllowed(false);

        String[] statusOpts = {"NOT STARTED", "ONGOING", "DONE"};
        TableColumn statusColumn = taskTable.getColumnModel().getColumn(3);
        statusColumn.setCellEditor(new DefaultCellEditor(new JComboBox<>(statusOpts)));

        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 3 && row >= 0) {
                Task task = taskManager.getTasks().get(row);
                String newStatus = (String) tableModel.getValueAt(row, column);
                task.setStatus(newStatus);
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);

        taskTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int row = taskTable.rowAtPoint(e.getPoint());
                    int col = taskTable.columnAtPoint(e.getPoint());

                    if (row >= 0 && col == 2) {
                        Task selectedTask = taskManager.getTasks().get(row);

                        JTextArea textArea = new JTextArea(selectedTask.getTaskDescription());
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
                        textArea.setColumns(40);
                        textArea.setRows(10);

                        JScrollPane scrollPane = new JScrollPane(textArea);

                        String[] options = {"SAVE", "CANCEL"};

                        int result = JOptionPane.showOptionDialog(
                                MainWindow.this,
                                scrollPane,
                                "EDITABLE - " + selectedTask.getTaskName(),
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                options,
                                options[0]
                        );

                        if (result == JOptionPane.YES_NO_OPTION) {
                            selectedTask.setTaskDescription(textArea.getText());
                            loadTask();
                        }

                    }
                    if (row >= 0 && col == 0) {
                        taskManager.removeTask(row);
                        taskManager.updateTaskId();
                        loadTask();
                    }
                }
            }
        });

    }

    public void loadTask() {
        tableModel.setRowCount(0);

        for (Task task : taskManager.getTasks()) {
            Object[] row = {
                String.format("%05d", task.getTaskId()),
                task.getTaskName(),
                task.getTaskDescription(),
                task.getStatus()
            };
            tableModel.addRow(row);
        }
    }

}
