import java.io.*;
import java.util.*;

class ExpenseTracker {
    private static final String DATA_FILE = "expense_data.txt";
    private Map<String, List<Expense>> expenseMap;
    private Scanner scanner;

    public ExpenseTracker() {
        expenseMap = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public void createAccount(String username) {
        expenseMap.put(username, new ArrayList<>());
    }

    public void inputExpense(String username) {
        System.out.println("Enter date (MM/DD/YYYY): ");
        String date = scanner.next();
        System.out.println("Enter category: ");
        String category = scanner.next();
        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();

        List<Expense> expenses = expenseMap.get(username);
        expenses.add(new Expense(date, category, amount));
    }

    public void viewExpenses(String username) {
        List<Expense> expenses = expenseMap.get(username);
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.println("Date\t\tCategory\tAmount");
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    public void calculateTotalExpenses(String username) {
        Map<String, Double> categoryTotals = new HashMap<>();
        List<Expense> expenses = expenseMap.get(username);
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        }
        System.out.println("Total Expenses per Category:");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
        }
    }

    public void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, List<Expense>> entry : expenseMap.entrySet()) {
                String username = entry.getKey();
                List<Expense> expenses = entry.getValue();
                for (Expense expense : expenses) {
                    writer.println(username + "," + expense.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    public void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String[] expenseParts = Arrays.copyOfRange(parts, 1, parts.length);
                List<Expense> expenses = expenseMap.getOrDefault(username, new ArrayList<>());
                Expense expense = Expense.fromString(String.join(",", expenseParts));
                expenses.add(expense);
                expenseMap.put(username, expenses);
            }
        } catch (IOException e) {
            System.err.println("Error loading data from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        tracker.loadDataFromFile();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Create Account\n2. Input Expense\n3. View Expenses\n4. Calculate Total Expenses\n5. Save Data\n6. Exit");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter username: ");
                    String username = scanner.next();
                    tracker.createAccount(username);
                    break;
                case 2:
                    System.out.println("Enter username: ");
                    username = scanner.next();
                    tracker.inputExpense(username);
                    break;
                case 3:
                    System.out.println("Enter username: ");
                    username = scanner.next();
                    tracker.viewExpenses(username);
                    break;
                case 4:
                    System.out.println("Enter username: ");
                    username = scanner.next();
                    tracker.calculateTotalExpenses(username);
                    break;
                case 5:
                    tracker.saveDataToFile();
                    break;
                case 6:
                    tracker.saveDataToFile();
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

class Expense {
    private String date;
    private String category;
    private double amount;

    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return date + "\t" + category + "\t\t$" + amount;
    }

    public static Expense fromString(String data) {
        String[] parts = data.split(",");
        String date = parts[0];
        String category = parts[1];
        double amount = Double.parseDouble(parts[2]);
        return new Expense(date, category, amount);
    }
}

