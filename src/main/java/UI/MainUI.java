package UI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Task;
import model.Category;
import model.Priority;
import logic.TaskManager;
import logic.TaskPersistence;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

public class MainUI extends Application {
    private ListView<Task> taskViewList;
    private TaskManager taskManager;
    private ComboBox<String> boxFilter;
    private ComboBox<Priority> boxPriorityFilter;
    private ComboBox<Category> boxCategoryFilter;
    private ComboBox<String> boxSort;

    @Override
    public void start(Stage primaryStage) throws IOException {
        taskManager = new TaskManager();
        taskViewList = new ListView<>();
        List<Task> LoadedTasks = TaskPersistence.load();

        if(LoadedTasks != null) {
            System.out.println(taskManager.setTasks(LoadedTasks));
        }
        else{
            System.out.println("No previous tasks was found");
        }

        //updateTaskViewList();

        Button addTaskButton = new Button("Add Task");
        Button markTaskDoneButton = new Button("Mark Task Done");
        Button deleteTaskButton = new Button("Delete Task");
        Button saveButton = new Button("Save");
        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");


        markTaskDoneButton.setOnAction(event -> {markSelectedTaskCompleted();});
        deleteTaskButton.setOnAction(event -> {
            try {
                deleteSelectedTask();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        addTaskButton.setOnAction(e -> {
            Task task = showAddTaskDialog();
            if (task != null) {
                taskManager.addTask(task);
                updateTaskViewList();
            }
        });
        saveButton.setOnAction(e -> {
            try {
                TaskPersistence.save(taskManager.getAllTasks());
                showInfo("Saved successfully.");
            } catch (IOException ex) {
                showError("Failed to save tasks.");
            }
        });
        undoButton.setOnAction(e -> {
            if (taskManager.undo()) {
                updateTaskViewList();
            } else {
                showInfo("Nothing to undo.");
            }
        });
        redoButton.setOnAction(e -> {
            if (taskManager.redo()) {
                updateTaskViewList();
            } else {
                showInfo("Nothing to redo.");
            }
        });

        boxFilter = new ComboBox<>();
        boxFilter.getItems().addAll("All", "Completed", "Pending", "By Priority", "By Category");
        boxFilter.setValue("All");

        boxPriorityFilter = new ComboBox<>();
        boxPriorityFilter.getItems().addAll(Priority.values());
        boxPriorityFilter.setPromptText("Select Priority");
        boxPriorityFilter.setVisible(false);

        boxCategoryFilter = new ComboBox<>();
        boxCategoryFilter.getItems().addAll(Category.values());
        boxCategoryFilter.setPromptText("Select Category");
        boxCategoryFilter.setVisible(false);

        boxSort = new ComboBox<>();
        boxSort.getItems().addAll("None", "By Due Date", "By Completion + Due Date");
        boxSort.setValue("None");

        boxPriorityFilter.setOnAction(e -> updateTaskViewList());
        boxCategoryFilter.setOnAction(e -> updateTaskViewList());
        boxSort.setOnAction(e -> updateTaskViewList());

        boxFilter.setOnAction(e -> {
            String value = boxFilter.getValue();
            boxPriorityFilter.setVisible("By Priority".equals(value));
            boxCategoryFilter.setVisible("By Category".equals(value));
            updateTaskViewList();
        });

        HBox boxForButtons = new HBox (10, addTaskButton, deleteTaskButton, markTaskDoneButton, undoButton, redoButton, saveButton);
        boxForButtons.setStyle("-fx-padding: 10px; -fx-allignment: center;");

        HBox topBar = new HBox(10, boxFilter, boxPriorityFilter, boxCategoryFilter, boxSort);
        topBar.setPadding(new Insets(10));

        VBox boxMain = new VBox(10,topBar, taskViewList, boxForButtons);

        boxMain.setStyle("-fx-padding: 20px;");

        updateTaskViewList();

        Scene scene = new Scene(boxMain);
        primaryStage.setTitle("To-Do List");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void updateTaskViewList(){
        //taskViewList.getItems().setAll(taskManager.getAllTasks());
        List<Task> tasks = taskManager.getAllTasks();

        switch(boxFilter.getValue()){
            case "Completed" : tasks = taskManager.getCompletedTaskList(); break;
            case "Pending" : tasks = taskManager.getPendingTaskList(); break;
            case "By Priority" : {
                Priority p = boxPriorityFilter.getValue();
                if (p != null) tasks = taskManager.getTaskListByPriority(p);
                break;
            }
            case "By Category" : {
                Category c = boxCategoryFilter.getValue();
                if (c != null) tasks = taskManager.getTaskListByCategory(c);
                break;
            }
        }

        switch(boxSort.getValue()){
            case "By Due Date" : tasks = taskManager.sortByDueDate(); break;
            case "By Completion + Due Date" : tasks = taskManager.sortByComplitionAndDueDate(); break;
        }

        taskViewList.setItems(FXCollections.observableArrayList(tasks));
    }

    private void markSelectedTaskCompleted(){
        Task selectedTask = taskViewList.getSelectionModel().getSelectedItem();
        if(selectedTask != null && !selectedTask.getTaskCompleted()){
            selectedTask.setTaskCompleted(true);
            updateTaskViewList();
        }
    }

    private void deleteSelectedTask() throws IOException {
        Task selectedTask = taskViewList.getSelectionModel().getSelectedItem();
        if(selectedTask != null){
            taskManager.removeTask(taskManager.getAllTasks().indexOf(selectedTask));
            updateTaskViewList();
        }
    }

    private Task showAddTaskDialog(){
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle("Add New Task");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField taskNameField = new TextField();
        taskNameField.setPromptText("Task name");

        DatePicker dueDatePicker = new DatePicker();

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(Category.values());
        categoryBox.setPromptText("Task category");

        ComboBox<Priority> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(Priority.values());
        priorityBox.setPromptText("Task priority");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Task name:"), 0, 0);
        grid.add(taskNameField, 1, 0);
        grid.add(new Label("Due date:"), 0, 1);
        grid.add(dueDatePicker, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryBox, 1, 2);
        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String taskName = taskNameField.getText();
                LocalDate dueDate = dueDatePicker.getValue();
                Priority priority = priorityBox.getValue();
                Category category = categoryBox.getValue();

                if (taskName != null && dueDate != null && priority != null && category != null) {
                    return new Task(taskName, dueDate, category, priority);
                }
            }
            return null;
        });

        Optional<Task> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void showInfo(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,  message,  ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR,  message,  ButtonType.OK);
        alert.setHeaderText("ERROR");
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {

        System.out.println("Application stops");
        super.stop();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
