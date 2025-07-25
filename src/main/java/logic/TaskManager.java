package logic;

import model.Category;
import model.Priority;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> TaskList = new ArrayList<>();
    private Stack<List<Task>> undoStack = new Stack<>();
    private Stack<List<Task>> redoStack = new Stack<>();

    public String setTasks(List<Task> tasks) {
        // this.Tasklist = tasks;
        TaskList = tasks.stream().map(task -> new Task(
                task.getTaskName(),
                task.getTaskDueDate(),
                task.getTaskCategory(),
                task.getTaskPriority(),
                task.getTaskCompleted()
        )).collect(Collectors.toList());
        return "previous data is ready to use";
    }

    private void saveCurrentTaskListState(){
        List<Task> CurrentState = TaskList.stream().map(Task::new).collect(Collectors.toList());
        undoStack.push(CurrentState);
        redoStack.clear();
    }

    public String addTask(Task task) {
        saveCurrentTaskListState();
        if (TaskList.add(task)) {
            return "task successfully added with index " + TaskList.indexOf(task);
        } else {
            return "task was not added to the list. Try adding another task.";
        }
    }

    public String removeTask(Integer taskId) throws IOException {
        saveCurrentTaskListState();
        if(taskId >=0 &&  taskId < TaskList.size()) {
            TaskList.remove(TaskList.get(taskId));
            //TaskPersistence.save(TaskList);
            return  "task was removed from the list.";
        }
        else{
            return "there is no task with the given ID";
        }
    }

    public String toggleTaskComplete(Integer taskId) {
        saveCurrentTaskListState();
        if(taskId >= 0 && taskId < TaskList.size()) {
            TaskList.get(taskId).setTaskCompleted(true);
            return "task was successfully completed";
        }
        else{
            return "there is no task with the given ID";
        }
    }

    public List<Task> getAllTasks() {
        return TaskList;
    }

    public List<Task> getTaskListByCategory(Category category) {
        List<Task> temporaryTaskList = new ArrayList<>();
        for (Task task : TaskList) {
            if(task.getTaskCategory() == category) {
                temporaryTaskList.add(task);
            }
        }
        return temporaryTaskList;
    }

    public List<Task> getTaskListByPriority(Priority priority) {
        /*List<Task> temporaryTaskList = new ArrayList<>();
        for (Task task : TaskList) {
            if(task.getTaskPriority() == priority) {
                temporaryTaskList.add(task);
            }
        }
        return temporaryTaskList;*/
        return TaskList.stream().filter(task -> task.getTaskPriority() == priority).collect(Collectors.toList());
    }

    public List<Task> getCompletedTaskList() {
        return TaskList.stream().filter(task -> task.getTaskCompleted() == Boolean.TRUE).collect(Collectors.toList());
    }

    public List<Task> getPendingTaskList() {
        return TaskList.stream().filter(task -> task.getTaskCompleted() == Boolean.FALSE).collect(Collectors.toList());
    }

    public List<Task> sortByDueDate() {
        TaskList.sort(Comparator.comparing(Task::getTaskDueDate));
        return this.getAllTasks();
    }

    public List<Task> sortByComplitionAndDueDate() {
        TaskList.sort(Comparator.comparing(Task::getTaskCompleted).thenComparing(Task::getTaskDueDate));
        return this.getAllTasks();
    }

    public boolean undo (){
        if(!undoStack.isEmpty()) {
            redoStack.push(new  ArrayList<>(TaskList));
            TaskList = undoStack.pop();
            return true;
        }
        return false;
    }
    public boolean redo (){
        if(!redoStack.isEmpty()) {
            undoStack.push(new  ArrayList<>(TaskList));
            TaskList = redoStack.pop();
            return true;
        }
        return false;
    }
}
