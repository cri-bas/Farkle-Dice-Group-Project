import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

class Game {
    private List<Player> players;
    private List<Dice> dice;
    private Random random;
    private Scanner scanner;
    private Scorer scorer;

    public Game(List<Player> players, List<Dice> dice, Random random, Scanner scanner) {
        this.players = players;
        this.dice = dice;
        this.random = random;
        this.scanner = scanner;
        this.scorer = new Scorer();
    }

    public void start() {
        while (!isGameFinished()) {
            for (Player currentPlayer : players) {
                System.out.println(currentPlayer.getName() + "'s turn.");

                List<Integer> rollResult = new ArrayList<>();
                int turnScore = 0;

                boolean continueRolling = true;

                while (continueRolling) {
                    rollResult.clear();
                    for (Dice die : dice) {
                        if (!die.isSelected()) {
                            rollResult.add(die.roll());
                        } else {
                            // If the die is selected, keep its value without rolling.
                            rollResult.add(die.getFaceValue());
                        }
                    }

                    System.out.println("Rolled: " + rollResult);
                    System.out.println("Current score: " + turnScore);

                    List<Integer> selectedDice = selectScoringDice(rollResult);
                    int selectedScore = scorer.calculateScore(selectedDice);

                    turnScore += selectedScore;

                    // Mark selected dice as "selected."
                    for (Dice die : dice) {
                        if (selectedDice.contains(die.getFaceValue())) {
                            die.setSelected(true);
                        }
                    }

                    // Prompt the player to decide whether to continue rolling.
                    if (rollResult.isEmpty()) {
                        System.out.println("All dice have been scored. You must roll again.");
                        break;
                    }

                    continueRolling = playerDecidesToContinue();
                }

                // Update the player's total score.
                currentPlayer.addToScore(turnScore);
                System.out.println(currentPlayer.getName() + "'s total score: " + currentPlayer.getScore());

                // Reset selected state of dice for the next player's turn.
                for (Dice die : dice) {
                    die.setSelected(false);
                }
            }
        }

        Player winner = determineWinner();
        System.out.println("Game over! " + winner.getName() + " wins with a score of " + winner.getScore());
    }

    private List<Integer> selectScoringDice(List<Integer> rollResult) {
        List<Integer> selectedDice = new ArrayList<>();

        // Display the roll result and ask the player to select scoring dice.
        System.out.println("Select the dice to score by entering their indexes (starting from 1).");
        System.out.println("Example: 1 3 5 (to select the first, third, and fifth dice)");

        String input = scanner.nextLine();
        String[] selectedIndexes = input.split(" ");

        for (String index : selectedIndexes) {
            int selectedIndex = Integer.parseInt(index);
            if (selectedIndex >= 1 && selectedIndex <= rollResult.size()) {
                int selectedDie = rollResult.get(selectedIndex - 1);

                // Check if the selected die is valid for scoring (e.g., 1 or 5).
                if (isValidScoringDie(selectedDie)) {
                    selectedDice.add(selectedDie);
                }
            }
        }

        return selectedDice;
    }

    private boolean isValidScoringDie(int dieValue) {
        return dieValue == 1 || dieValue == 5;
    }

    private boolean playerDecidesToContinue() {
        System.out.print("Do you want to continue rolling (y/n)? ");
        String decision = scanner.nextLine().toLowerCase();
        return decision.equals("y");
    }

    private boolean isGameFinished() {
        // Implement your winning condition here.
        return false;
    }

    private Player determineWinner() {
        Player winner = players.get(0);
        for (Player player : players) {
            if (player.getScore() > winner.getScore()) {
                winner = player;
            }
        }
        return winner;
    }
}
