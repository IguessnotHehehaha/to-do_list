package logic;

import model.Category;
import model.Priority;
import model.Task;

import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        List<Task> LoadedTasks = TaskPersistence.load();
        int idx = -1;
        String trash = "";

        if(LoadedTasks != null) {
            System.out.println(taskManager.setTasks(LoadedTasks));
        }
        else{
            System.out.println("No previous tasks was found");
        }

        while(true){
            System.out.println("\n=== TO-DO LIST MENU ===");
            System.out.println("1. View tasks");
            System.out.println("2. Add task");
            System.out.println("3. Mark task as completed");
            System.out.println("4. Delete task");
            System.out.println("5. Sort by completion + date");
            System.out.println("6. Filter task");
            System.out.println("7. Undo");
            System.out.println("8. Redo");
            System.out.println("9. Save and exit");
            System.out.print("Select an option: ");
            int userChoise = scanner.nextInt();
            trash = scanner.nextLine();
            switch(userChoise) {
                case 1: taskManager.getAllTasks().forEach(System.out::println); break;
                case 2:
                    System.out.println("Title: ");
                    String taskName = scanner.nextLine();

                    System.out.println("DueDate: ");
                    LocalDate dueDate = LocalDate.parse(scanner.nextLine());

                    System.out.println("Category: ");
                    Category category = Category.valueOf(scanner.nextLine().toUpperCase());

                    System.out.println("Priority: ");
                    Priority priority = Priority.valueOf(scanner.nextLine().toUpperCase());
                    System.out.println(taskManager.addTask(new Task(taskName, dueDate, category, priority)));
                    break;
                case 3:
                    System.out.println("Task index to toggle: ");
                    idx = Integer.parseInt(scanner.nextLine());
                    System.out.println(taskManager.toggleTaskComplete(idx));
                    break;
                case 4:
                    System.out.println("Task index to delete");
                    idx = Integer.parseInt(scanner.nextLine());
                    System.out.println(taskManager.removeTask(idx));
                    break;
                case 5:
                    System.out.println(taskManager.sortByComplitionAndDueDate()); break;
                case 6 : {
                    System.out.println("\n-- Filter Options --");
                    System.out.println("1. Completed tasks");
                    System.out.println("2. Pending tasks");
                    System.out.println("3. By category");
                    System.out.println("4. By priority");
                    System.out.println("5. Sort by due date");
                    System.out.print("Choose filter: ");
                    int filterOption = Integer.parseInt(scanner.nextLine());
                    switch (filterOption) {
                        case 1 : taskManager.getCompletedTaskList().forEach(System.out::println); break;
                        case 2 : taskManager.getPendingTaskList().forEach(System.out::println); break;
                        case 3 : {
                            System.out.print("Enter category (WORK, PERSONAL, etc.): ");
                            category = Category.valueOf(scanner.nextLine().toUpperCase());
                            taskManager.getTaskListByCategory(category).forEach(System.out::println);
                            break;
                        }
                        case 4 : {
                            System.out.print("Enter priority (LOW, MEDIUM, HIGH): ");
                            priority = Priority.valueOf(scanner.nextLine().toUpperCase());
                            taskManager.getTaskListByPriority(priority).forEach(System.out::println);
                            break;
                        }
                        case 5 : taskManager.sortByDueDate().forEach(System.out::println); break;
                        default : System.out.println("Invalid filter option."); break;
                    }
                    break;
                }
                case 9: {
                    TaskPersistence.save(taskManager.getAllTasks());
                    System.out.println("Saved. Goodbye!");
                    return;
                }
                case 7: {
                    if(taskManager.undo()){
                        System.out.println("Last action undone successfully");
                    }
                    else{
                        System.out.println("Nothing to undo.");
                    }
                    break;
                }
                case 8: {
                    if(taskManager.redo()){
                        System.out.println("Last action redo successfully");
                    }
                    else{
                        System.out.println("Nothing to redo.");
                    }
                    break;
                }
                default : System.out.println("invalid option."); break;
            }
        }
    }
}
