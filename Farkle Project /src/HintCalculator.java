public class HintCalculator {

    // Method to calculate the probability of getting a Farkle
    public static double calculateFarkleProbability(int remainingDice) {
        // Assuming a six-sided die
        double possibleOutcomes = 6.0;

        // Probability of not getting a scoring combination on one die
        double probabilityOfNotScoring = 5.0 / possibleOutcomes;

        // Probability of not getting a scoring combination on all remaining dice
        double probabilityOfNotFarkling = Math.pow(probabilityOfNotScoring, remainingDice);

        // Probability of getting a Farkle
        double probabilityOfFarkle = 1.0 - probabilityOfNotFarkling;

        return probabilityOfFarkle;
    }

    // Method to provide a hint based on the calculated probability
    public static String getHint(int remainingDice) {
        double probability = calculateFarkleProbability(remainingDice);

        if (probability == 0.0) {
            return "You can't get a Farkle!";
        } else if (probability < 0.2) {
            return "Be careful, there's a high chance of getting a Farkle on your next roll!";
        } else {
            return "You have a decent chance of avoiding a Farkle on your next roll!";
        }
    }
}
