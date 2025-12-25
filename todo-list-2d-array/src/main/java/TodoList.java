import java.io.*;
import java.util.Scanner;

public class TodoList {
    private String[][] tasks; // array 2D për detyrat
    private int taskCount;
    private final int MAX_TASKS = 50;
    private Scanner scanner;

    public TodoList() {
        tasks = new String[MAX_TASKS][9]; // Name, Description, Status, Priority, Category, DueDate, EstTime, Tags, AssignedTo
        taskCount = 0;
        scanner = new Scanner(System.in);
        loadFromFile(); // ngarko nga file në fillim
    }

    // 1. Shto detyre te re
    public void addTask() {
        if (taskCount >= MAX_TASKS) {
            System.out.println("List is full!");
            return;
        }
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Description: ");
            String desc = scanner.nextLine();

            System.out.print("Priority (1-5): ");
            String priority = scanner.nextLine();

            System.out.print("Category: ");
            String category = scanner.nextLine();

            System.out.print("Due date (dd/mm/yyyy): ");
            String dueDate = scanner.nextLine();

            System.out.print("Estimated time (hours): ");
            String estTime = scanner.nextLine();

            System.out.print("Tags (comma separated): ");
            String tags = scanner.nextLine();

            System.out.print("Assigned to: ");
            String assignedTo = scanner.nextLine();

            tasks[taskCount++] = new String[]{name, desc, "Pending", priority, category, dueDate, estTime, tags, assignedTo};
            System.out.println("✓ Task added!");
        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    // 2. Shfaq te gjitha detyrat
    public void showAllTasks() {
        if (taskCount == 0) {
            System.out.println("No tasks!");
            return;
        }
        System.out.printf("%-3s %-15s %-15s %-10s %-8s %-10s %-8s %-15s %-10s\n",
                "ID", "Name", "Description", "Status", "Pri", "Category", "Due", "Tags", "Assigned");
        System.out.println("=".repeat(110));
        for (int i = 0; i < taskCount; i++) {
            System.out.printf("%-3d %-15s %-15s %-10s %-8s %-10s %-8s %-15s %-10s\n",
                    i + 1, tasks[i][0], tasks[i][1], tasks[i][2], tasks[i][3],
                    tasks[i][4], tasks[i][5], tasks[i][7], tasks[i][8]);
        }
    }

    // 3. Ndrysho statusin
    public void changeStatus() {
        showAllTasks();
        if (taskCount == 0) return;
        try {
            System.out.print("Choose task ID: ");
            int id = Integer.parseInt(scanner.nextLine()) - 1;
            if (id < 0 || id >= taskCount) {
                System.out.println("Invalid ID!");
                return;
            }
            tasks[id][2] = tasks[id][2].equals("Pending") ? "Done" : "Pending";
            System.out.println("✓ Status changed!");
        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    // 4. Filtrimi sipas kategori, prioritet ose tags
    public void filterTasks() {
        if (taskCount == 0) {
            System.out.println("No tasks!");
            return;
        }
        System.out.println("Filter by: 1.Category 2.Priority 3.Tags");
        System.out.print("Choose: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter value: ");
            String value = scanner.nextLine();
            boolean found = false;
            for (int i = 0; i < taskCount; i++) {
                if ((choice == 1 && tasks[i][4].equalsIgnoreCase(value)) ||
                        (choice == 2 && tasks[i][3].equals(value)) ||
                        (choice == 3 && tasks[i][7].contains(value))) {
                    System.out.println((i + 1) + ". " + tasks[i][0] + " | " + tasks[i][2] + " | " + tasks[i][5]);
                    found = true;
                }
            }
            if (!found) System.out.println("No matching tasks!");
        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    // 5. Sortimi sipas dates, prioritetit ose koha e parashikuar
    public void sortTasks() {
        if (taskCount == 0) {
            System.out.println("No tasks!");
            return;
        }
        System.out.println("Sort by: 1.DueDate 2.Priority 3.EstTime");
        System.out.print("Choose: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            for (int i = 0; i < taskCount - 1; i++) {
                for (int j = 0; j < taskCount - i - 1; j++) {
                    boolean swap = false;
                    switch (choice) {
                        case 1 -> swap = tasks[j][5].compareTo(tasks[j + 1][5]) > 0;
                        case 2 -> swap = Integer.parseInt(tasks[j][3]) > Integer.parseInt(tasks[j + 1][3]);
                        case 3 -> swap = Double.parseDouble(tasks[j][6]) > Double.parseDouble(tasks[j + 1][6]);
                    }
                    if (swap) {
                        String[] temp = tasks[j];
                        tasks[j] = tasks[j + 1];
                        tasks[j + 1] = temp;
                    }
                }
            }
            System.out.println("✓ Tasks sorted!");
        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    // 6. Editimi i detyres
    public void editTask() {
        showAllTasks();
        if (taskCount == 0) return;
        try {
            System.out.print("Choose task ID: ");
            int id = Integer.parseInt(scanner.nextLine()) - 1;
            if (id < 0 || id >= taskCount) {
                System.out.println("Invalid ID!");
                return;
            }
            System.out.println("Edit: 1.Name 2.Description 3.Category 4.DueDate");
            System.out.print("Choose field: ");
            int field = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new value: ");
            String value = scanner.nextLine();
            switch (field) {
                case 1 -> tasks[id][0] = value;
                case 2 -> tasks[id][1] = value;
                case 3 -> tasks[id][4] = value;
                case 4 -> tasks[id][5] = value;
                default -> System.out.println("Invalid choice!");
            }
            System.out.println("✓ Task updated!");
        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    // 7. Fshirja e detyres
    public void deleteTask() {
        showAllTasks();
        if (taskCount == 0) return;
        try {
            System.out.print("Choose task ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine()) - 1;
            if (id < 0 || id >= taskCount) {
                System.out.println("Invalid ID!");
                return;
            }
            for (int i = id; i < taskCount - 1; i++) {
                tasks[i] = tasks[i + 1];
            }
            taskCount--;
            System.out.println("✓ Task deleted!");
        } catch (Exception e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }

    // 8. Ruajtja dhe leximi nga file
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (int i = 0; i < taskCount; i++) {
                writer.write(String.join(",", tasks[i]));
                writer.newLine();
            }
            System.out.println("✓ Tasks saved!");
        } catch (Exception e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            taskCount = 0;
            while ((line = reader.readLine()) != null && taskCount < MAX_TASKS) {
                String[] parts = line.split(",");
                if (parts.length == 9) {
                    tasks[taskCount++] = parts;
                }
            }
        } catch (FileNotFoundException e) {
            // nuk ka file ne fillim, normale
        } catch (Exception e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    // 9. Statistikat
    public void showStatistics() {
        int pending = 0, done = 0;
        for (int i = 0; i < taskCount; i++) {
            if (tasks[i][2].equals("Pending")) pending++;
            else done++;
        }
        System.out.println("Total: " + taskCount + ", Pending: " + pending + ", Done: " + done);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n1.Add 2.Show 3.ChangeStatus 4.Filter 5.Sort 6.Edit 7.Delete 8.Save 9.Stats 0.Exit");
            System.out.print("Choose: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addTask();
                    case 2 -> showAllTasks();
                    case 3 -> changeStatus();
                    case 4 -> filterTasks();
                    case 5 -> sortTasks();
                    case 6 -> editTask();
                    case 7 -> deleteTask();
                    case 8 -> saveToFile();
                    case 9 -> showStatistics();
                    case 0 -> {
                        saveToFile();
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Enter a number.");
            }
        }
    }

    public static void main(String[] args) {
        TodoList todo = new TodoList();
        todo.showMenu();
    }
}

