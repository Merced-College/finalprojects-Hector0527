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
import java.io.FileWriter;
import java.io.IOException;
/**
 * FIX FOR COMPILATION ERRORS
 * This file contains all three classes needed for the Calorie Tracker app
 * in a single file to avoid import issues.
 */

// The main class for the application
public class CalorieTracker {
    // Using HashMap to store food items and their calorie values
    private static HashMap<String, Integer> foodDatabase = new HashMap<>();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize data structures
        initializeFoodDatabase();
        
        // Welcome message
        System.out.println("===================================");
        System.out.println("CALORIE TRACKER & MEAL PLANNER");
        System.out.println("===================================");
        /**
 * Data values for calculating calories were assisted by OpenAI's ChatGPT (May 2025).
 */
        // Get user input in imperial units
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
        scanner.nextLine(); // clear newline
        System.out.print("Enter your gender (male/female): ");
        String gender = scanner.nextLine();
        System.out.print("Enter your activity level (sedentary/light/moderate/active/very active): ");
        String activityLevel = scanner.nextLine();
        System.out.print("Enter your goal (lose/maintain/gain): ");
        String goal = scanner.nextLine();
        
        // Create user and calculate calories
        User user = new User(name, age, weightLbs, heightFt, heightIn, gender, goal, activityLevel);
        int dailyCalories = user.calculateCalories();
        
        System.out.println("\nHello, " + user.getName() + "!");
        System.out.println("Your daily calorie goal is: " + dailyCalories + " calories.");
        
        // Menu system
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
            scanner.nextLine(); // clear newline
            
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
        
        scanner.close(); // clean resource management
    }
    
    /**
 * Initialize the food database with common food items and average calorie values.
 * Data values and food selection were assisted by OpenAI's ChatGPT (May 2025).
 */
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
    
    /**
     * Method to log a meal for the user
     * Uses a queue-like structure to add food chronologically
     * Time Complexity: O(1) - Adding to LinkedList is constant time
     */
    private static void logMeal(Scanner scanner, User user) {
        System.out.println("\n==== LOG A MEAL ====");
        System.out.print("Enter meal type (breakfast/lunch/dinner/snack): ");
        String mealType = scanner.nextLine();
        
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
                scanner.nextLine(); // clear newline
                
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
                    scanner.nextLine(); // clear newline
                    
                    foodDatabase.put(foodItem, calories);
                    System.out.print("Enter serving size for this meal: ");
                    double servingSize = scanner.nextDouble();
                    scanner.nextLine(); // clear newline
                    
                    int totalCalories = (int)(calories * servingSize);
                    FoodItem food = new FoodItem(foodItem, totalCalories, mealType);
                    user.addFoodItem(food);
                    System.out.println(foodItem + " added: " + totalCalories + " calories");
                }
            }
        }
    }
    
    /**
     * Display the user's food log for the day
     * Uses recursive method to calculate total calories
     * Time Complexity: O(n) where n is the number of food items
     */
    private static void displayFoodLog(User user) {
        LinkedList<FoodItem> foodLog = user.getFoodLog();
        
        if (foodLog.isEmpty()) {
            System.out.println("\nNo meals logged yet today.");
            return;
        }
        
        System.out.println("\n==== TODAY'S FOOD LOG ====");
        System.out.println("Food Item\t\tCalories\tMeal Type");
        System.out.println("--------------------------------------------");
        
        for (FoodItem item : foodLog) {
            System.out.println(item.getName() + "\t\t" + item.getCalories() + "\t\t" + item.getMealType());
        }
        
        int totalCalories = calculateTotalCalories(foodLog, 0, 0);
        int remainingCalories = user.calculateCalories() - totalCalories;
        
        System.out.println("\nTotal calories consumed: " + totalCalories);
        System.out.println("Calorie target: " + user.calculateCalories());
        System.out.println("Remaining calories: " + remainingCalories);
    }
    
    /**
     * Recursive method to calculate total calories from food log
     * Time Complexity: O(n) where n is the number of food items
     */
    private static int calculateTotalCalories(LinkedList<FoodItem> foodLog, int index, int total) {
        // Base case
        if (index >= foodLog.size()) {
            return total;
        }
        
        // Recursive step
        total += foodLog.get(index).getCalories();
        return calculateTotalCalories(foodLog, index + 1, total);
    }
    
    /**
     * Generate a simple meal plan based on the user's calorie needs
     * Uses array to store meal suggestions
     * Time Complexity: O(1) - Fixed operations
     */
    private static void generateMealPlan(User user, Scanner scanner) {
        System.out.println("\n==== MEAL PLAN GENERATOR ====");
        int totalCalories = user.calculateCalories();
        
        // Define percentages for each meal
        double[] mealPercentages = {0.25, 0.35, 0.30, 0.10}; // breakfast, lunch, dinner, snack
        String[] mealNames = {"Breakfast", "Lunch", "Dinner", "Snack"};
        
        System.out.println("Daily Calorie Target: " + totalCalories);
        System.out.println("\nRecommended Calorie Distribution:");
        
        for (int i = 0; i < mealPercentages.length; i++) {
            int mealCalories = (int)(totalCalories * mealPercentages[i]);
            System.out.println(mealNames[i] + ": " + mealCalories + " calories");
            
            // Suggest foods for each meal
            suggestFoodsForMeal(mealCalories, mealNames[i]);
        }
    }
    
    /**
     * Helper method to suggest foods for a specific meal
     * Time Complexity: O(n) where n is the size of the food database
     */
    private static void suggestFoodsForMeal(int targetCalories, String mealType) {
        ArrayList<String> suggestions = new ArrayList<>();
        int currentCalories = 0;
        
        // Simple greedy algorithm to suggest foods 
        // This is not optimal but provides reasonable suggestions
        for (String food : foodDatabase.keySet()) {
            int foodCalories = foodDatabase.get(food);
            if (currentCalories + foodCalories <= targetCalories) {
                suggestions.add(food + " (" + foodCalories + " cal)");
                currentCalories += foodCalories;
                
                // Stop once we reach 85% of target to avoid exceeding
                if (currentCalories >= targetCalories * 0.85) {
                    break;
                }
            }
        }
        
        System.out.println("  Suggested foods: " + String.join(", ", suggestions));
    }
    
    /**
     * Add a new food item to the database
     * Time Complexity: O(1) - HashMap insertion
     */
    private static void addFoodToDatabase(Scanner scanner) {
        System.out.println("\n==== ADD FOOD TO DATABASE ====");
        System.out.print("Enter food name: ");
        String foodName = scanner.nextLine().toLowerCase();
        
        if (foodDatabase.containsKey(foodName)) {
            System.out.println("Food already exists in database with " + foodDatabase.get(foodName) + " calories.");
            System.out.print("Would you like to update it? (yes/no): ");
            String response = scanner.nextLine();
            
            if (!response.equalsIgnoreCase("yes")) {
                return;
            }
        }
        
        System.out.print("Enter calories per serving: ");
        int calories = scanner.nextInt();
        scanner.nextLine(); // clear newline
        
        foodDatabase.put(foodName, calories);
        System.out.println(foodName + " added to database with " + calories + " calories per serving.");
    }
    
    /**
     * View all foods in the database
     * Time Complexity: O(n) where n is the size of the food database
     */
    private static void viewFoodDatabase() {
        System.out.println("\n==== FOOD DATABASE ====");
        System.out.println("Food\t\tCalories (per serving)");
        System.out.println("-----------------------------");
        
        for (String food : foodDatabase.keySet()) {
            System.out.println(food + "\t\t" + foodDatabase.get(food));
        }
    }
    
    /**
     * Save user data to a file
     * Time Complexity: O(n) where n is the size of the food log
     */
    private static void saveUserData(User user) {
        try {
            FileWriter writer = new FileWriter("user_data.txt");
            writer.write("Name: " + user.getName() + "\n");
            writer.write("Age: " + user.getAge() + "\n");
            writer.write("Weight (kg): " + user.getWeightKg() + "\n");
            writer.write("Height (cm): " + user.getHeightCm() + "\n");
            writer.write("Gender: " + user.getGender() + "\n");
            writer.write("Goal: " + user.getGoal() + "\n");
            writer.write("Activity Level: " + user.getActivityLevel() + "\n");
            
            // Save food log
            writer.write("===FOOD LOG===\n");
            for (FoodItem item : user.getFoodLog()) {
                writer.write(item.getName() + "," + item.getCalories() + "," + item.getMealType() + "\n");
            }
            
            writer.close();
            System.out.println("User data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }
    
    /**
     * Load user data from a file
     * Time Complexity: O(n) where n is the size of the file
     */
    private static void loadUserData(Scanner scanner) {
        System.out.println("Note: This is a simplified implementation. In a real app, this would load the user profile.");
        System.out.println("For now, you'll need to re-enter your information when you restart the program.");
    }
}

/**
 * User class for the Calorie Tracker App
 * Stores user information and handles calorie calculations
 */
class User {
    private String name;
    private int age;
    private double weightKg;   // converted from lbs
    private double heightCm;   // converted from ft + in
    private String gender;
    private String goal;
    private String activityLevel;
    private LinkedList<FoodItem> foodLog;  // Using LinkedList as a queue-like structure
    
    /**
     * Constructor: converts imperial to metric and initializes food log
     * Time Complexity: O(1) - Fixed operations
     */
    public User(String name, int age, double weightLbs, int heightFt, int heightIn, 
                String gender, String goal, String activityLevel) {
        this.name = name;
        this.age = age;
        this.weightKg = weightLbs * 0.453592; // lbs to kg
        this.heightCm = (heightFt * 12 + heightIn) * 2.54; // ft+in to cm
        this.gender = gender;
        this.goal = goal;
        this.activityLevel = activityLevel;
        this.foodLog = new LinkedList<>();
    }
    
    /**
     * Method to calculate daily calorie needs
     * Implements Mifflin-St Jeor formula for BMR calculation
     * Adjusts based on activity level and weight goal
     * Time Complexity: O(1) - Fixed operations
     */
    public int calculateCalories() {
        // Calculate BMR (Basal Metabolic Rate)
        int bmr;
        if (gender.equalsIgnoreCase("male")) {
            bmr = (int)(10 * weightKg + 6.25 * heightCm - 5 * age + 5);
        } else {
            bmr = (int)(10 * weightKg + 6.25 * heightCm - 5 * age - 161);
        }
        
        // Apply activity factor
        double activityFactor = getActivityFactor();
        int tdee = (int)(bmr * activityFactor); // TDEE = Total Daily Energy Expenditure
        
        // Adjust for goal
        if (goal.equalsIgnoreCase("lose")) {
            return (int)(tdee * 0.8); // 20% deficit for weight loss
        } else if (goal.equalsIgnoreCase("gain")) {
            return (int)(tdee * 1.15); // 15% surplus for weight gain
        } else {
            return tdee; // maintain weight
        }
    }
    
    /**
     * Helper method to determine activity factor
     * Time Complexity: O(1) - Fixed operations
     */
    private double getActivityFactor() {
        switch (activityLevel.toLowerCase()) {
            case "sedentary":
                return 1.2; // Little or no exercise
            case "light":
                return 1.375; // Light exercise 1-3 days/week
            case "moderate":
                return 1.55; // Moderate exercise 3-5 days/week
            case "active":
                return 1.725; // Active exercise 6-7 days/week
            case "very active":
                return 1.9; // Very intense exercise daily
            default:
                return 1.2; // Default to sedentary if input is invalid
        }
    }
    
    /**
     * Add a food item to the user's food log
     * Time Complexity: O(1) - LinkedList add operation
     */
    public void addFoodItem(FoodItem item) {
        foodLog.add(item);
    }
    
    /**
     * Get the user's food log
     * Time Complexity: O(1) - Direct reference return
     */
    public LinkedList<FoodItem> getFoodLog() {
        return foodLog;
    }
    
    /**
     * Clear the user's food log
     * Time Complexity: O(1) - LinkedList clear operation
     */
    public void clearFoodLog() {
        foodLog.clear();
    }
    
    // Getters for user properties
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public double getWeightKg() {
        return weightKg;
    }
    
    public double getHeightCm() {
        return heightCm;
    }
    
    public String getGender() {
        return gender;
    }
    
    public String getGoal() {
        return goal;
    }
    
    public String getActivityLevel() {
        return activityLevel;
    }
}

/**
 * FoodItem class for the Calorie Tracker App
 * Represents a single food item with its nutritional information
 */
class FoodItem {
    private String name;
    private int calories;
    private String mealType;
    
    /**
     * Constructor for creating a new food item
     * Time Complexity: O(1) - Fixed operations
     */
    public FoodItem(String name, int calories, String mealType) {
        this.name = name;
        this.calories = calories;
        this.mealType = mealType;
    }
    
    // Getters for food item properties
    public String getName() {
        return name;
    }
    
    public int getCalories() {
        return calories;
    }
    
    public String getMealType() {
        return mealType;
    }
    
    @Override
    public String toString() {
        return name + " (" + calories + " calories)";
    }
}