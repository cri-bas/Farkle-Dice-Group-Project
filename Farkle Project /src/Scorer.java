import java.util.Collections;
import java.util.List;

public class Scorer {
    public static int calculateScore(List<Integer> dice) {
        int score = 0;
        Collections.sort(dice); // Sort the dice for easier scoring checks.

        if (isStraight(dice)) {
            score += 1000; // A straight earns 1,000 points.
        } else {
            score += calculateSingleDiceScore(dice, 1); // Add points for 1s.
            score += calculateSingleDiceScore(dice, 5); // Add points for 5s.
            score += calculateThreeOfAKindScore(dice); // Add points for three of a kind.
            score += calculateThreePairsScore(dice); // Add points for three pairs.
        }

        // Check for special case: Three 1s.
        if (dice.contains(1) && Collections.frequency(dice, 1) >= 3) {
            score += 1000;
        }

        // Check for Farkle.
        if (score == 0) {
            score = -1; // Indicate a Farkle.
        }

        return score;
    }

    private static boolean isStraight(List<Integer> dice) {
        return dice.equals(List.of(1, 2, 3, 4, 5, 6));
    }

    private static int calculateSingleDiceScore(List<Integer> dice, int value) {
        int count = Collections.frequency(dice, value);
        int score = 0;
        if (value == 1) {
            score += count * 100; // A 1 earns 100 points each.
        } else if (value == 5) {
            score += count * 50; // A 5 earns 50 points each.
        }
        return score;
    }

    private static int calculateThreeOfAKindScore(List<Integer> dice) {
        for (int dieValue = 1; dieValue <= 6; dieValue++) {
            int count = Collections.frequency(dice, dieValue);
            if (count >= 3) {
                return dieValue * 100; // Three of a kind earns face value times 100.
            }
        }
        return 0;
    }

    private static int calculateThreePairsScore(List<Integer> dice) {
        if (dice.size() == 6 && dice.get(0) == dice.get(1) && dice.get(2) == dice.get(3)
                && dice.get(4) == dice.get(5)) {
            return 750; // Three pairs are worth 750 points.
        }
        return 0;
    }
}
