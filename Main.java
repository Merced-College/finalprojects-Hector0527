import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        // Get user input in imperial units
        System.out.print("Enter your name: ");
        String name = scnr.nextLine();
        System.out.print("Enter your age: ");
        int age = scnr.nextInt();
        System.out.print("Enter your weight (lbs): ");
        double weightLbs = scnr.nextDouble();
        System.out.print("Enter your height - feet: ");
        int heightFt = scnr.nextInt();
        System.out.print("Enter your height - inches: ");
        int heightIn = scnr.nextInt();
        scnr.nextLine(); // clear newline
        System.out.print("Enter your gender (male/female): ");
        String gender = scnr.nextLine();
        System.out.print("Enter your goal (lose/maintain/gain): ");
        String goal = scnr.nextLine();

        // Create user and calculate calories
        User user = new User(name, age, weightLbs, heightFt, heightIn, gender, goal);
        int dailyCalories = user.calculateCalories();

        System.out.println("\nHello, " + user.name + "!");
        System.out.println("Your daily calorie goal is: " + dailyCalories + " calories.");

        scnr.close(); // clean resource management
    }
}
