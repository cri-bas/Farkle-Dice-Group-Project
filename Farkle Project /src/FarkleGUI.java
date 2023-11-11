import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FarkleGUI extends JFrame {
    private List<JButton> diceButtons;
    private JTextArea outputTextArea;
    private JButton rollButton;
    private JButton endTurnButton;

    private List<Integer> rollResult;
    private List<Integer> selectedDice;
    private int currentScore;

    public FarkleGUI() {
        // Set up the frame
        setTitle("Farkle Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        diceButtons = new ArrayList<>();
        outputTextArea = new JTextArea();
        rollButton = new JButton("Roll");
        endTurnButton = new JButton("End Turn");

        // Layout components
        setLayout(new BorderLayout());
        add(outputTextArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rollButton);
        buttonPanel.add(endTurnButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initialize game state
        rollResult = new ArrayList<>();
        selectedDice = new ArrayList<>();
        currentScore = 0;

        // Set up action listeners
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rollDice();
            }
        });

        endTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endTurn();
            }
        });

        // Initialize the GUI
        updateGUI();
    }

    private void rollDice() {
        // Simulate rolling six dice
        rollResult.clear();
        for (int i = 0; i < 6; i++) {
            rollResult.add((int) (Math.random() * 6) + 1);
        }

        // Enable all dice buttons
        for (JButton button : diceButtons) {
            button.setEnabled(true);
        }

        // Update the GUI
        updateGUI();
    }

    private void endTurn() {
        // Process the selected dice and update the score
        int turnScore = calculateScore(selectedDice);
        currentScore += turnScore;

        // Check for Farkle
        if (turnScore == 0) {
            outputTextArea.append("\nFarkle! No scoring combinations. Turn score reset to 0.");
            currentScore = 0;
        }

        // Clear the selected dice for the next turn
        selectedDice.clear();

        // Roll the dice for the next turn
        rollDice();
    }

    private int calculateScore(List<Integer> dice) {
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

    private boolean isStraight(List<Integer> dice) {
        return dice.equals(List.of(1, 2, 3, 4, 5, 6));
    }

    private int calculateSingleDiceScore(List<Integer> dice, int value) {
        int count = Collections.frequency(dice, value);
        int score = 0;
        if (value == 1) {
            score += count * 100; // A 1 earns 100 points each.
        } else if (value == 5) {
            score += count * 50; // A 5 earns 50 points each.
        }
        return score;
    }

    private int calculateThreeOfAKindScore(List<Integer> dice) {
        for (int dieValue = 1; dieValue <= 6; dieValue++) {
            int count = Collections.frequency(dice, dieValue);
            if (count >= 3) {
                return dieValue * 100; // Three of a kind earns face value times 100.
            }
        }
        return 0;
    }

    private int calculateThreePairsScore(List<Integer> dice) {
        if (dice.size() == 6 && dice.get(0) == dice.get(1) && dice.get(2) == dice.get(3)
                && dice.get(4) == dice.get(5)) {
            return 750; // Three pairs are worth 750 points.
        }
        return 0;
    }

    private void updateGUI() {
        // Update the text area to display the current game state
        outputTextArea.setText(
                "Roll Result: " + rollResult + "\nSelected Dice: " + selectedDice + "\nCurrent Score: " + currentScore);

        // Clear previous dice buttons
        for (JButton button : diceButtons) {
            remove(button);
        }
        diceButtons.clear();

        // Create buttons for each die in the roll result
        for (int i = 0; i < rollResult.size(); i++) {
            int dieValue = rollResult.get(i);
            JButton button = new JButton(Integer.toString(dieValue));

            // Set up action listener for dice buttons
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectDie(dieValue);
                }
            });

            // Add the button to the list and the frame
            diceButtons.add(button);
            add(button);
        }

        // Repaint the frame to reflect changes
        revalidate();
        repaint();
    }

    private void selectDie(int dieValue) {
        // Add the selected die to the list
        selectedDice.add(dieValue);

        // Disable the selected die button
        for (JButton button : diceButtons) {
            if (button.getText().equals(Integer.toString(dieValue))) {
                button.setEnabled(false);
            }
        }

        // Update the GUI
        updateGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FarkleGUI().setVisible(true);
            }
        });
    }
}
