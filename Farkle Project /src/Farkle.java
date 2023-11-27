import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
//import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Farkle implements ActionListener {

    private JFrame frame = new JFrame();
    private Container diceContainer = new Container();
    private JButton[] diceButtons = new JButton[6];
    private ImageIcon[] imageIcons = new ImageIcon[6];
    private int[] buttonState = new int[6];
    private int[] dieValue = new int[6];
    private final int HOT_DIE = 0;
    private final int SCORE_DIE = 1;
    private final int LOCKED_DIE = 2;
    private Border selectedBorder = BorderFactory.createLineBorder(Color.RED, 4);
    private List<Integer> selectedDiceIndices = new ArrayList<>();

    private Container buttonContainer = new Container();
    private JButton rollButton = new JButton("Roll");
    private JButton scoreButton = new JButton("Score");
    private JButton stopButton = new JButton("Stop");

    // Add a new button for the hint
    private JButton hintButton = new JButton("Hint");

    private Container labelContainer = new Container();
    private JLabel currentRoundLBL = new JLabel("Current Round: 0");
    private JLabel currentPlayerLBL = new JLabel("Current Player: ");
    private JLabel nextPlayerLBL = new JLabel("Next Player: ");

    private Map<String, Integer> gamesWonTracker = new HashMap<>();

    // Player-specific labels
    private int playerType; // 0 for multiplayer, 1 for vs PC
    private List<JLabel> playerLabels = new ArrayList<>();

    private List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private int currentScore = 0;

    public Farkle() {
        // Add an option for player type
        int numPlayers = 0;
        Object[] playerTypeOptions = { "Multiplayer", "Vs PC" };
        playerType = JOptionPane.showOptionDialog(frame, "Choose Player Type:", "Player Type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, playerTypeOptions,
                playerTypeOptions[0]);

        // type 0 for multiplayer
        if (playerType == 0) {
            numPlayers = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of players:"));
            for (int i = 0; i < numPlayers; i++) {
                String playerName = JOptionPane.showInputDialog("Enter player " + (i + 1) + "'s name:");
                players.add(new Player(playerName));
            }
        } else if (playerType == 1) {
            numPlayers = 2; // Vs PC, so two players (human and computer)
            String playerName = JOptionPane.showInputDialog("Enter your name:");
            players.add(new Player(playerName));
            players.add(new Player("Computer"));

            // Initialize games won tracker for each player
            for (Player player : players) {
                gamesWonTracker.put(player.getName(), 0);
            }
        }

        frame.setSize(600, 600);
        imageIcons[0] = new ImageIcon("./img/one.png");
        imageIcons[1] = new ImageIcon("./img/two.png");
        imageIcons[2] = new ImageIcon("./img/three.png");
        imageIcons[3] = new ImageIcon("./img/four.png");
        imageIcons[4] = new ImageIcon("./img/five.png");
        imageIcons[5] = new ImageIcon("./img/six.png");
        diceContainer.setLayout(new GridLayout(2, 3));

        for (int a = 0; a < diceButtons.length; a++) {
            diceButtons[a] = new JButton();
            diceButtons[a].setIcon(imageIcons[a]);
            diceButtons[a].setEnabled(false);
            diceButtons[a].addActionListener(this);
            diceContainer.add(diceButtons[a]);
        }

        buttonContainer.setLayout(new GridLayout(1, 4));
        buttonContainer.add(rollButton);
        rollButton.addActionListener(this);
        buttonContainer.add(scoreButton);
        scoreButton.addActionListener(this);
        scoreButton.setEnabled(false);
        buttonContainer.add(stopButton);
        stopButton.addActionListener(this);

        // Add a new button for the hint
        hintButton.addActionListener(this);
        buttonContainer.add(hintButton);

        labelContainer.setLayout(new GridLayout(3 + numPlayers, 1));
        labelContainer.add(currentRoundLBL);
        labelContainer.add(currentPlayerLBL);
        labelContainer.add(nextPlayerLBL);

        // Initialize player-specific labels, excluding the computer
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            JLabel playerLabel = new JLabel(
                    player.getName() + ": Current Score = 0, Total Score = 0, Current Round = 1");
            playerLabels.add(playerLabel);
            labelContainer.add(playerLabel);
        }
        frame.setLayout(new BorderLayout());
        frame.add(diceContainer, BorderLayout.CENTER);
        frame.add(buttonContainer, BorderLayout.NORTH);
        frame.add(labelContainer, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        updatePlayerLabels();

    }

    public static void main(String[] args) {
        new Farkle();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Determine the game mode and player type
        boolean isVsComputer = (playerType == 1);
        boolean isComputerPlayer = (currentPlayerIndex == 1);

        if (isVsComputer && isComputerPlayer) {
            // Vs PC logic for computer's turn
            performVSComputerActions();
        } else {
            // Multiplayer logic for human player's turn
            System.out.println("Reached actionPerformed - before if conditions");

            if (e.getSource().equals(rollButton)) {
                clearDiceBorders();
                for (int a = 0; a < diceButtons.length; a++) {
                    if (buttonState[a] == HOT_DIE) {
                        int choice = (int) (Math.random() * 6);
                        dieValue[a] = choice;
                        diceButtons[a].setIcon(imageIcons[choice]);
                        diceButtons[a].setEnabled(true);
                    }
                }
                // Set the state for buttons after rolling
                rollButton.setEnabled(false);
                scoreButton.setEnabled(true);
                stopButton.setEnabled(false);
            } else if (e.getSource().equals(scoreButton)) {
                System.out.println("Reached actionPerformed - inside scoreButton condition");

                int[] valueCount = new int[7];
                for (int a = 0; a < diceButtons.length; a++) {
                    if (buttonState[a] == SCORE_DIE) {
                        valueCount[dieValue[a] + 1]++;
                    }
                }
                // Check for FARKLE condition
                if (valueCount[1] == 0 && valueCount[2] == 0 && valueCount[3] == 0 && valueCount[4] == 0
                        && valueCount[5] == 0 && valueCount[6] == 0) {
                    handleFarkle();
                } else {
                    // Continue with the multiplayer logic
                    performMultiplayerActions(valueCount);

                }

            } else if (e.getSource().equals(stopButton)) {

                System.out.println("Reached actionPerformed - inside stopButton condition");

                // Stop button
                players.get(currentPlayerIndex).transferCurrentScore();
                players.get(currentPlayerIndex).resetCurrentScore();
                players.get(currentPlayerIndex).incrementCurrentRound();

                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                updatePlayerLabels();
                resetDice();
                players.get(currentPlayerIndex).addToTotalScore();

                // Check for winning condition
                checkForWinner();
                System.out.println("Reached actionPerformed - inside stopButton end condition");

            } else if (e.getSource().equals(hintButton)) {
                // Display the hint based on the remaining dice
                int remainingDice = (int) Arrays.stream(buttonState).filter(state -> state == HOT_DIE).count();
                String hint = HintCalculator.getHint(remainingDice);
                JOptionPane.showMessageDialog(frame, hint);
            } else {
                // Handling dice button clicks
                for (int a = 0; a < diceButtons.length; a++) {
                    if (e.getSource().equals(diceButtons[a])) {
                        if (buttonState[a] == HOT_DIE) {
                            selectedDiceIndices.add(a);
                            diceButtons[a].setBorder(selectedBorder);
                            buttonState[a] = SCORE_DIE;
                        } else {
                            selectedDiceIndices.remove(Integer.valueOf(a));
                            diceButtons[a].setBorder(null);
                            buttonState[a] = HOT_DIE;
                        }
                    }
                }
            }
            System.out.println("Reached actionPerformed - outside stopButton condition");

            // Check for winning condition here as well
            if (players.get(currentPlayerIndex).getTotalScore() >= 1000) {
                String winnerName = players.get(currentPlayerIndex).getName();
                displayWinnerMessage(winnerName);

                // Ask if players want to continue
                int option = JOptionPane.showConfirmDialog(frame,
                        "Do you want to continue playing with the same players?",
                        "Game Over", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    // Reset the game and continue
                    resetGame();
                } else {
                    // Exit the game
                    System.exit(0);
                }
            }
        }
    }

    // Player class
    private class Player {
        private String name;
        private int currentScore;
        private int totalScore;
        private int currentRound;

        public Player(String name) {
            this.name = name;
            this.currentScore = 0;
            this.totalScore = 0;
            this.currentRound = 1;
        }

        public String getName() {
            return name;
        }

        public int getCurrentScore() {
            return currentScore;
        }

        public void addToCurrentScore(int score) {
            this.currentScore += score;
        }

        public void transferCurrentScore() {
            totalScore += currentScore;
        }

        public void resetCurrentScore() {
            currentScore = 0;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void addToTotalScore() {
            totalScore += currentScore;
        }

        public int getCurrentRound() {
            return currentRound;
        }

        public void incrementCurrentRound() {
            this.currentRound++;
        }

        public void resetTotalScore() {
            this.totalScore = 0;
        }

        public void resetCurrentRound() {
            this.currentRound = 1;
        }

    }

    void resetDice() {
        for (int a = 0; a < diceButtons.length; a++) {
            diceButtons[a].setEnabled(false);
            buttonState[a] = HOT_DIE;
            diceButtons[a].setBorder(null); // Clear borders when resetting dice
            diceButtons[a].setBackground(Color.LIGHT_GRAY);
        }
        rollButton.setEnabled(true);
        scoreButton.setEnabled(false);
        stopButton.setEnabled(false);

        selectedDiceIndices.clear(); // Clear selected dice indices when resetting dice
    }

    private void clearDiceBorders() {
        for (JButton button : diceButtons) {
            button.setBorder(null);
        }
    }

    private void displayWinnerMessage(String winnerName) {
        // Update games won tracker
        int gamesWon = gamesWonTracker.getOrDefault(winnerName, 0);
        gamesWonTracker.put(winnerName, gamesWon + 1);

        String message = winnerName + " Wins!";
        Object[] options = { "Play Again", "Exit" };
        int choice = JOptionPane.showOptionDialog(frame, message, "Game Over", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            // Continue the game
            resetGame();
        } else {
            // Exit the game
            System.exit(0);
        }
    }

    private void displayHotDieMessage() {
        String message = "You get to roll again!";
        Object[] options = { "Nice!" };
        int ok = JOptionPane.showOptionDialog(frame, message, "You got an Hot Die!", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (ok == JOptionPane.OK_OPTION) {
        }
    }

    private void updatePlayerLabels() {
        Player currentPlayer = players.get(currentPlayerIndex);
        Player nextPlayer = players.get((currentPlayerIndex + 1) % players.size());

        currentPlayerLBL.setText("Current Player: " + currentPlayer.getName());
        nextPlayerLBL.setText("Next Player: " + nextPlayer.getName());
        currentRoundLBL.setText("Current Round: " + currentPlayer.getCurrentRound());

        // Update player-specific labels
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            JLabel playerLabel = playerLabels.get(i);
            playerLabel.setText(player.getName() + ": Current Score = " + player.getCurrentScore() + ", Total Score = "
                    + player.getTotalScore() + ", Current Round = " + player.getCurrentRound() + ", Games Won = "
                    + gamesWonTracker.getOrDefault(player.getName(), 0));

            // Check for winning condition
            if (player.getTotalScore() >= 1000) {
                displayWinnerMessage(player.getName());
            }
        }
    }

    private void performVSComputerActions() {
        if (playerType == 1 && currentPlayerIndex == 1) {

            // Simulate the computer's actions
            simulateComputerActions();

            // After the computer's turn, enable buttons and update labels
            rollButton.setEnabled(true);
            scoreButton.setEnabled(true);
            stopButton.setEnabled(true);
            updatePlayerLabels();
            // Check for winning condition
            checkForWinner();
        }
    }

    private void simulateComputerActions() {
        int maxRolls = 3; // Maximum number of rolls for the computer
        int rolls = 0;
        int totalComputerScore = 0; // Track total computer score

        while (rolls < maxRolls) {

            int[] computerValueCount = new int[7];
            for (int a = 0; a < diceButtons.length; a++) {
                if (buttonState[a] == SCORE_DIE) {
                    computerValueCount[dieValue[a] + 1]++;
                }
            }

            // Simulate computer rolling
            for (int a = 0; a < diceButtons.length; a++) {
                if (buttonState[a] == HOT_DIE) {
                    int choice = (int) (Math.random() * 6);
                    dieValue[a] = choice;
                    diceButtons[a].setIcon(imageIcons[choice]);
                    diceButtons[a].setEnabled(true);
                }
            }

            // Check for FARKLE condition
            if (computerValueCount[1] == 0 && computerValueCount[2] == 0 && computerValueCount[3] == 0
                    && computerValueCount[4] == 0 && computerValueCount[5] == 0 && computerValueCount[6] == 0) {
                // Display FARKLED message
                JOptionPane.showMessageDialog(frame, " FARKLED! Turn forfeited.");
                break;
            }

            // Check for valid scoring combinations
            boolean validCombination = checkForValidCombination(computerValueCount);

            if (validCombination) {
                // Track the total computer score
                totalComputerScore += chooseAndScoreCombination(computerValueCount);

                // Check for conditions to stop rolling
                if (currentScore >= 250 || totalComputerScore >= 250 || rolls == maxRolls - 1) {

                    scoreButton.doClick();
                    // Simulate clicking the "Stop" button
                    stopButton.doClick();
                    break;
                }

                rolls++;
            } else {
                // Display FARKLED message
                JOptionPane.showMessageDialog(frame, "Computer got FARKLED! Turn forfeited.");
                // Simulate clicking the "stop" button (to end the turn with no points)
                stopButton.doClick();
                break;
            }
        }

        // Bank the computer's score
        players.get(currentPlayerIndex).addToCurrentScore(totalComputerScore);
        updatePlayerLabels();

        // Move to the next player for VS PC games
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        updatePlayerLabels(); // Update labels for the new current player
    }

    private boolean checkForValidCombination(int[] valueCount) {
        // Check for valid scoring combinations
        return (valueCount[1] > 0 || valueCount[5] > 0
                || valueCount[2] >= 3 || valueCount[3] >= 3 || valueCount[4] >= 3 || valueCount[6] >= 3
                || (valueCount[1] == 2 && valueCount[2] == 2 && valueCount[3] == 2 && valueCount[4] == 2
                        && valueCount[5] == 2 && valueCount[6] == 2)
                || (valueCount[1] == 1 && valueCount[2] == 1 && valueCount[3] == 1 && valueCount[4] == 1
                        && valueCount[5] == 1 && valueCount[6] == 1));
    }

    private int chooseAndScoreCombination(int[] computerValueCount) {
        // Loop through values 1 to 6
        for (int value = 1; value <= 6; value++) {
            if (computerValueCount[value] >= 3) {
                // Simulate choosing the combination
                String message = "Computer chose combination for value " + value;
                JOptionPane.showMessageDialog(frame, message);

                // Simulate clicking the "Score" button
                scoreButton.doClick();

                // Simulate choosing and scoring a combination
                int roundScore = computeComputerScore(computerValueCount);
                players.get(currentPlayerIndex).addToCurrentScore(roundScore);
                updatePlayerLabels();

                // Check for winning condition
                if (players.get(currentPlayerIndex).getTotalScore() >= 10000) {
                    displayWinnerMessage(players.get(currentPlayerIndex).getName());
                    return roundScore; // Return the score obtained
                }

                // Check for hot die condition
                int lockedCount = 0;
                for (int a = 0; a < diceButtons.length; a++) {
                    if (buttonState[a] == LOCKED_DIE) {
                        lockedCount++;
                    }
                }

                // Display hot die message and reset dice
                if (lockedCount == 6) {
                    for (int a = 0; a < diceButtons.length; a++) {
                        buttonState[a] = HOT_DIE;
                        diceButtons[a].setBackground(Color.LIGHT_GRAY);
                    }
                    displayHotDieMessage();
                }

                return roundScore; // Return the score obtained
            }
        }

        // If no valid combination is found, simulate a Farkle
        String farkleMessage = "Computer rolled a Farkle!";
        JOptionPane.showMessageDialog(frame, farkleMessage);

        // Simulate clicking the "Stop" button (to end the turn with no points)
        stopButton.doClick();

        return 0; // Return 0 as the score (or another appropriate value)

    }

    public int computeComputerScore(int[] computerValueCount) {
        // Compute the computer's score based on the valid combinations
        int computerroundScore = 0;

        if (computerValueCount[1] >= 3) {
            computerroundScore += (computerValueCount[1] - 2) * 1000;
        }
        if (computerValueCount[2] >= 3) {
            computerroundScore += (computerValueCount[2] - 2) * 200;
        }
        if (computerValueCount[3] >= 3) {
            computerroundScore += (computerValueCount[3] - 2) * 300;
        }
        if (computerValueCount[4] >= 3) {
            computerroundScore += (computerValueCount[4] - 2) * 400;
        }
        if (computerValueCount[5] >= 3) {
            computerroundScore += (computerValueCount[5] - 2) * 500;
        }
        if (computerValueCount[6] >= 3) {
            computerroundScore += (computerValueCount[6] - 2) * 600;
        }
        if (computerValueCount[1] < 3) {
            computerroundScore += computerValueCount[1] * 100;
        }
        if (computerValueCount[5] < 3) {
            computerroundScore += computerValueCount[5] * 50;
        }

        return computerroundScore;
    }

    private void handleFarkle() {
        players.get(currentPlayerIndex).resetCurrentScore(); // Reset current score
        players.get(currentPlayerIndex).incrementCurrentRound();
        resetDice();
        updatePlayerLabels();

        // Move to the next player for VS PC games
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        updatePlayerLabels(); // Update labels for the new current player

        // Display FARKLED message
        JOptionPane.showMessageDialog(frame, "FARKLED! Turn forfeited.");

        // Check for winning condition
        if (players.get(currentPlayerIndex).getTotalScore() >= 10000) {
            displayWinnerMessage(players.get(currentPlayerIndex).getName());
        }
    }

    private void performMultiplayerActions(int[] valueCount) {
        System.out.println("Reached performMultiplayerActions - begginning");

        // Multiplayer logic for human player's turn
        boolean validCombination = false;
        System.out.println("checking for valid scoring combinations performMultiplayerActions");
        // Check for valid scoring combinations
        if (valueCount[1] > 0 || valueCount[5] > 0) {
            validCombination = true; // At least one 1 or 5 is selected
        } else if (valueCount[2] >= 3 || valueCount[3] >= 3 || valueCount[4] >= 3 || valueCount[6] >= 3) {
            validCombination = true; // Three of a kind
        } else if (valueCount[1] == 3 || valueCount[2] == 2 && valueCount[3] == 2 && valueCount[4] == 2
                && valueCount[5] == 2 && valueCount[6] == 2) {
            validCombination = true; // Three pairs
        } else if (valueCount[1] == 1 && valueCount[2] == 1 && valueCount[3] == 1 && valueCount[4] == 1
                && valueCount[5] == 1 && valueCount[6] == 1) {
            validCombination = true; // Six-dice straight
        }
        System.out.println("Reached if validCombination in performMultiplayerActions");
        if (validCombination) {
            int roundScore = 0;
            if (valueCount[1] >= 3) {
                roundScore += (valueCount[1] - 2) * 1000;
            }
            if (valueCount[2] >= 3) {
                roundScore += (valueCount[2] - 2) * 200;
            }
            if (valueCount[3] >= 3) {
                roundScore += (valueCount[3] - 2) * 300;
            }
            if (valueCount[4] >= 3) {
                roundScore += (valueCount[4] - 2) * 400;
            }
            if (valueCount[5] >= 3) {
                roundScore += (valueCount[5] - 2) * 500;
            }
            if (valueCount[6] >= 3) {
                roundScore += (valueCount[6] - 2) * 600;
            }
            if (valueCount[1] < 3) {
                roundScore += valueCount[1] * 100;
            }
            if (valueCount[5] < 3) {
                roundScore += valueCount[5] * 50;
            }
            System.out.println("Reached adding score in performMultiplayerActions");
            players.get(currentPlayerIndex).addToCurrentScore(roundScore);
            updatePlayerLabels();
            for (int a = 0; a < diceButtons.length; a++) {
                if (buttonState[a] == SCORE_DIE) {
                    buttonState[a] = LOCKED_DIE;
                    diceButtons[a].setBackground(Color.BLUE);
                }
                diceButtons[a].setEnabled(false);
            }
            int lockedCount = 0;
            for (int a = 0; a < diceButtons.length; a++) {
                if (buttonState[a] == LOCKED_DIE) {
                    lockedCount++;
                }
            }
            if (lockedCount == 6) {
                for (int a = 0; a < diceButtons.length; a++) {
                    buttonState[a] = HOT_DIE;
                    diceButtons[a].setBackground(Color.LIGHT_GRAY);
                }
                displayHotDieMessage();
            }
            rollButton.setEnabled(true);
            scoreButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Invalid Scoring Combination! Please select valid scoring dice.");
        }
        System.out.println("Reached end of performMultiplayerActions");
    }

    private void checkForWinner() {
        // Check for winning condition
        System.out.println("Checking for winner...");

        if (players.get(currentPlayerIndex).getTotalScore() >= 1000) {
            String winnerName = players.get(currentPlayerIndex).getName();
            displayWinnerMessage(winnerName);
            System.out.println("passed winnerName in checkForWinner");

            // Ask if players want to continue
            int option = JOptionPane.showConfirmDialog(frame,
                    "Do you want to continue playing with the same players?",
                    "Game Over", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Reset the game and continue
                resetGame();
            } else {
                // Exit the game
                System.exit(0);
            }
        }
        System.out.println("Winner check complete.");

    }

    private void resetGame() {
        // Reset game state for each player
        for (Player player : players) {
            player.resetCurrentScore();
            player.resetTotalScore();
            player.resetCurrentRound();
        }

        // Reset game-related variables
        currentPlayerIndex = 0;
        currentScore = 0;

        // Clear game tracker
        selectedDiceIndices.clear();

        // Reset dice
        resetDice();

        // Update player labels
        updatePlayerLabels();
    }

}