/* Hector Valenzuela
 * 05.16.25
 * FIT TRACK
 * CalorieTracker - A terminal-based application for tracking calories and meal planning
 * 
 * This class contains the main program flow and interface for the application.
 * It implements several key features:
 * - User profile creation and calorie target calculation
 * - Meal logging using a queue data structure
 * - Food database management with HashMap
 * - Meal planning with ArrayList
 * - Data persistence for user profile and food logs
 * 
 */

import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class CalorieTracker {
    private static HashMap<String, Integer> foodDatabase = new HashMap<>();
    private static HashMap<String, ArrayList<String>> mealSuggestions = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeFoodDatabase();
        initializeMealSuggestions();

        System.out.println("===================================");
        System.out.println("CALORIE TRACKER & MEAL PLANNER");
        System.out.println("===================================");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        System.out.print("Enter your weight (lbs): ");
        double weightLbs = scanner.nextDouble();
        System.out.print("Enter your height - feet: ");
        int heightFt = scanner.nextInt();
        System.out.print("Enter your height - inches: ");
        int heightIn = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter your gender (male/female): ");
        String gender = scanner.nextLine();
        System.out.print("Enter your activity level (sedentary/light/moderate/active/very active): ");
        String activityLevel = scanner.nextLine();
        System.out.print("Enter your goal (lose/maintain/gain): ");
        String goal = scanner.nextLine();

        User user = new User(name, age, weightLbs, heightFt, heightIn, gender, goal, activityLevel);
        int dailyCalories = user.calculateCalories();

        System.out.println("\nHello, " + user.getName() + "!");
        System.out.println("Your daily calorie goal is: " + dailyCalories + " calories.");

        boolean running = true;
        while (running) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Log a meal");
            System.out.println("2. View today's food log");
            System.out.println("3. Generate meal plan");
            System.out.println("4. Add food to database");
            System.out.println("5. View food database");
            System.out.println("6. Save user data");
            System.out.println("7. Load user data");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    logMeal(scanner, user);
                    break;
                case 2:
                    displayFoodLog(user);
                    break;
                case 3:
                    generateMealPlan(user, scanner);
                    break;
                case 4:
                    addFoodToDatabase(scanner);
                    break;
                case 5:
                    viewFoodDatabase();
                    break;
                case 6:
                    saveUserData(user);
                    break;
                case 7:
                    loadUserData(scanner);
                    break;
                case 8:
                    running = false;
                    System.out.println("Thank you for using the Calorie Tracker!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void initializeFoodDatabase() {
        foodDatabase.put("apple", 95);
        foodDatabase.put("banana", 105);
        foodDatabase.put("orange", 45);
        foodDatabase.put("chicken breast", 165);
        foodDatabase.put("salmon", 206);
        foodDatabase.put("rice", 206);
        foodDatabase.put("pasta", 200);
        foodDatabase.put("bread", 80);
        foodDatabase.put("egg", 78);
        foodDatabase.put("milk", 122);
    }

    private static void initializeMealSuggestions() {
        mealSuggestions.put("breakfast", new ArrayList<>(List.of("oatmeal", "eggs", "toast", "banana", "milk")));
        mealSuggestions.put("lunch", new ArrayList<>(List.of("chicken breast", "rice", "vegetables", "salad", "apple")));
        mealSuggestions.put("dinner", new ArrayList<>(List.of("salmon", "pasta", "broccoli", "bread", "orange")));
    }

    private static void logMeal(Scanner scanner, User user) {
        System.out.println("\n==== LOG A MEAL ====");
        System.out.print("Enter meal type (breakfast/lunch/dinner/snack): ");
        String mealType = scanner.nextLine().toLowerCase();

        if (mealSuggestions.containsKey(mealType)) {
            System.out.println("Suggested options for " + mealType + ": " + String.join(", ", mealSuggestions.get(mealType)));
        }

        boolean addingFood = true;
        while (addingFood) {
            System.out.print("Enter food item (or 'done' to finish): ");
            String foodItem = scanner.nextLine().toLowerCase();

            if (foodItem.equals("done")) {
                addingFood = false;
                continue;
            }

            if (foodDatabase.containsKey(foodItem)) {
                System.out.print("Enter serving size (default = 1): ");
                double servingSize = scanner.nextDouble();
                scanner.nextLine();

                int calories = (int)(foodDatabase.get(foodItem) * servingSize);
                FoodItem food = new FoodItem(foodItem, calories, mealType);
                user.addFoodItem(food);
                System.out.println(foodItem + " added: " + calories + " calories");
            } else {
                System.out.println("Food not found in database. Would you like to add it? (yes/no)");
                String response = scanner.nextLine();

                if (response.equalsIgnoreCase("yes")) {
                    System.out.print("Enter calories per serving: ");
                    int calories = scanner.nextInt();
                    scanner.nextLine();

                    foodDatabase.put(foodItem, calories);
                    System.out.print("Enter serving size for this meal: ");
                    double servingSize = scanner.nextDouble();
                    scanner.nextLine();

                    int totalCalories = (int)(calories * servingSize);
                    FoodItem food = new FoodItem(foodItem, totalCalories, mealType);
                    user.addFoodItem(food);
                    System.out.println(foodItem + " added: " + totalCalories + " calories");
                }
            }
        }
    }

    // Additional methods remain unchanged...
} 
