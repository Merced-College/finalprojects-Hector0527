public class User {
    String name;
    int age;
    double weightKg;   // converted from lbs
    double heightCm;   // converted from ft + in
    String gender;
    String goal;

    // Constructor: converts imperial to metric
    public User(String name, int age, double weightLbs, int heightFt, int heightIn, String gender, String goal) {
        this.name = name;
        this.age = age;
        this.weightKg = weightLbs * 0.453592; // lbs to kg
        this.heightCm = (heightFt * 12 + heightIn) * 2.54; // ft+in to cm
        this.gender = gender;
        this.goal = goal;
    }

    /**
     * Method and logic written with assistance from ChatGPT (OpenAI)
     * Prompted and adapted by Hector Valenzuela
     * Description: Implements Mifflin-St Jeor formula for calorie calculation
     */
    public int calculateCalories() {
        if (gender.equalsIgnoreCase("male")) {
            return (int)(10 * weightKg + 6.25 * heightCm - 5 * age + 5);
        } else {
            return (int)(10 * weightKg + 6.25 * heightCm - 5 * age - 161);
        }
    }
}
