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
import java.util.List;

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

    // Player-specific labels
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

    private int playerType; // 0 for multiplayer, 1 for vs PC

    public static void main(String[] args) {
        new Farkle();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
            if (playerType == 1 && currentPlayerIndex == 1) {
                // Vs PC logic for computer's turn
                simulateComputerTurn();
            } else {
                int[] valueCount = new int[7];
                for (int a = 0; a < diceButtons.length; a++) {
                    if (buttonState[a] == SCORE_DIE) {
                        valueCount[dieValue[a] + 1]++;
                    }
                }

                // Check for FARKLE condition
                if (valueCount[1] == 0 && valueCount[2] == 0 && valueCount[3] == 0 && valueCount[4] == 0
                        && valueCount[5] == 0 && valueCount[6] == 0) {
                    Object[] options = { "Yes", "No" };
                    int dialogChoice = JOptionPane.showOptionDialog(frame, "Forfeit your Score & Turn?", "FARKLED!",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (dialogChoice == JOptionPane.YES_OPTION) {
                        players.get(currentPlayerIndex).resetCurrentScore(); // Reset current score
                        players.get(currentPlayerIndex).incrementCurrentRound();
                        resetDice();
                        currentScore = 0;
                        updatePlayerLabels();

                        // Move to the next player for multiplayer games
                        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                        updatePlayerLabels(); // Update labels for the new current player

                        // Display FARKLED message
                        JOptionPane.showMessageDialog(frame, "FARKLED! Turn forfeited.");

                        // Check for winning condition
                        if (players.get(currentPlayerIndex).getTotalScore() >= 10000) {
                            displayWinnerMessage(players.get(currentPlayerIndex).getName());
                        }
                    }
                } else {
                    boolean validCombination = false;

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
                }

            }
        } else if (e.getSource().equals(stopButton)) {
            // Stop button
            players.get(currentPlayerIndex).transferCurrentScore();
            players.get(currentPlayerIndex).resetCurrentScore(); // this line reset current score
            players.get(currentPlayerIndex).incrementCurrentRound();
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // Switch to the next player
            updatePlayerLabels();
            resetDice();
            players.get(currentPlayerIndex).addToTotalScore();
        } else if (e.getSource().equals(hintButton)) {
            // Display the hint based on the remaining dice
            int remainingDice = (int) Arrays.stream(buttonState).filter(state -> state == HOT_DIE).count();
            String hint = HintCalculator.getHint(remainingDice);
            JOptionPane.showMessageDialog(frame, hint);
        } else {
            for (int a = 0; a < diceButtons.length; a++) {
                if (e.getSource().equals(diceButtons[a])) {
                    if (buttonState[a] == HOT_DIE) {
                        selectedDiceIndices.add(a); // Add the selected dice index
                        diceButtons[a].setBorder(selectedBorder); // Set the border for selected dice
                        buttonState[a] = SCORE_DIE;
                    } else {
                        selectedDiceIndices.remove(Integer.valueOf(a)); // Remove the unselected dice index
                        diceButtons[a].setBorder(null); // Remove the border for unselected dice
                        buttonState[a] = HOT_DIE;
                    }
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
        String message = winnerName + " Wins!";
        Object[] options = { "Exit" };
        int choice = JOptionPane.showOptionDialog(frame, message, "Game Over", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == JOptionPane.OK_OPTION) {
            System.exit(0); // Terminate the game
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
                    + player.getTotalScore() + ", Current Round = " + player.getCurrentRound());

            // Check for winning condition
            if (player.getTotalScore() >= 1000) {
                displayWinnerMessage(player.getName());
            }
        }
    }

    private void simulateComputerTurn() {
        // Add logic to simulate the computer's turn
        // You can use the existing logic for rolling, scoring, etc.
        // Adjust the logic based on how you want the computer to play
        // For simplicity, you might want to add a delay to make it seem like the
        // computer is "thinking"
        // Update player labels after the computer's turn

        rollButton.setEnabled(false); // Disable roll button during computer's turn
        scoreButton.setEnabled(false);
        stopButton.setEnabled(false);

        // Simulate computer's "thinking" with a delay
        try {
            Thread.sleep(1000); // Adjust the delay time as needed
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        // Simulate the computer's actions (you might need to customize this based on
        // your game)
        simulateComputerActions();

        // After the computer's turn, enable buttons and update labels
        rollButton.setEnabled(true);
        scoreButton.setEnabled(true);
        stopButton.setEnabled(true);
        updatePlayerLabels();
    }

    private void simulateComputerActions() {
        // Add logic to simulate the computer's actions (e.g., rolling, scoring)
        // Adjust the logic based on how you want the computer to play
        // This could involve calling methods from the existing game logic

        // For example:
        // computerRoll();
        // computerScore();
    }

    // ... existing code
}
