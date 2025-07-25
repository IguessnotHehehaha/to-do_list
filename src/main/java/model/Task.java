package model;

import java.time.*;

public class Task {
    private String TaskName;
    private LocalDate TaskDueDate;
    private Category TaskCategory;
    private Priority TaskPriority;
    private Boolean TaskCompleted;

    public Task (String TaskName, LocalDate TaskDueDate, Category TaskCategory, Priority TaskPriority, Boolean taskCompleted) {
        this.TaskName = TaskName;
        this.TaskDueDate = TaskDueDate;
        this.TaskCategory = TaskCategory;
        this.TaskPriority = TaskPriority;
        this.TaskCompleted = taskCompleted;
    }
    public Task (String TaskName, LocalDate TaskDueDate, Category TaskCategory, Priority TaskPriority) {
        this.TaskName = TaskName;
        this.TaskDueDate = TaskDueDate;
        this.TaskCategory = TaskCategory;
        this.TaskPriority = TaskPriority;
        this.TaskCompleted = Boolean.FALSE;
    }
    public Task  (String TaskName, LocalDate TaskDueDate, Category TaskCategory) {
        this.TaskName = TaskName;
        this.TaskDueDate = TaskDueDate;
        this.TaskCategory = TaskCategory;
        this.TaskPriority = Priority.LOW;
        this.TaskCompleted = Boolean.FALSE;
    }
    public Task  (String TaskName, LocalDate TaskDueDate) {
        this.TaskName = TaskName;
        this.TaskDueDate = TaskDueDate;
    }
    public Task (Task other){
        this.TaskName = other.TaskName;
        this.TaskDueDate = other.TaskDueDate;
        this.TaskCategory = other.TaskCategory;
        this.TaskPriority = other.TaskPriority;
        this.TaskCompleted = other.TaskCompleted;
    }

    public String getTaskName() {
        return TaskName;
    }
    public LocalDate getTaskDueDate() {
        return TaskDueDate;
    }
    public Category getTaskCategory() {
        return TaskCategory;
    }
    public Priority getTaskPriority() {
        return TaskPriority;
    }
    public Boolean getTaskCompleted() {
        return TaskCompleted;
    }

    public void setTaskDueDate(LocalDate taskDueDate) {
        this.TaskDueDate = taskDueDate;
    }
    public void setTaskCategory(Category taskCategory) {
        this.TaskCategory = taskCategory;
    }
    public void setTaskPriority(Priority priority) {
        this.TaskPriority = priority;
    }
    public void  setTaskCompleted(Boolean taskCompleted) {
        this.TaskCompleted = taskCompleted;
    }
    public void setTaskName (String taskName) {
        this.TaskName = taskName;
    }

    @Override
    public String toString() {
        return (this.TaskCompleted ? "[x]" : "[ ]") +  this.TaskName + "| due " +  this.TaskDueDate + " | category " +  this.TaskCategory + "| priority " +  this.TaskPriority;
    }

}
