package Farkle Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Farkle1 implements ActionListener {

    private static final String IMG_PATH = "./DiceImages/";
    private static final int HOT_DIE = 0;
    private static final int SCORE_DIE = 1;
    private static final int LOCKED_DIE = 2;

    private JFrame frame;
    private Container diceContainer;
    private JButton[] diceButtons;
    private ImageIcon[] imageIcons;
    private int[] buttonState;
    private int[] dieValue;

    private Container buttonContainer;
    private JButton rollButton;
    private JButton scoreButton;
    private JButton stopButton;

    private Container labelContainer;
    private JLabel currentScoreLBL;
    private JLabel totalScoreLBL;
    private JLabel currentRoundLBL;

    private int currentScore;
    private int totalScore;
    private int currentRound;

    public Farkle1() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();

        frame.setLayout(new BorderLayout());
        frame.add(diceContainer, BorderLayout.CENTER);
        frame.add(buttonContainer, BorderLayout.NORTH);
        frame.add(labelContainer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void initializeComponents() {
        diceContainer = new Container();
        diceButtons = new JButton[6];
        imageIcons = new ImageIcon[6];
        buttonState = new int[6];
        dieValue = new int[6];

        for (int i = 0; i < diceButtons.length; i++) {
            diceButtons[i] = new JButton();
            diceButtons[i].setIcon(loadImageIcon(i + 1));
            diceButtons[i].setEnabled(false);
            diceButtons[i].addActionListener(this);
            diceContainer.add(diceButtons[i]);
        }

        buttonContainer = new Container();
        buttonContainer.setLayout(new GridLayout(1, 3));

        rollButton = new JButton("Roll");
        scoreButton = new JButton("Score");
        stopButton = new JButton("Stop");

        rollButton.addActionListener(this);
        scoreButton.addActionListener(this);
        stopButton.addActionListener(this);

        buttonContainer.add(rollButton);
        buttonContainer.add(scoreButton);
        buttonContainer.add(stopButton);

        scoreButton.setEnabled(false);
        stopButton.setEnabled(false);

        labelContainer = new Container();
        labelContainer.setLayout(new GridLayout(3, 1));

        currentScoreLBL = new JLabel("Current Score: 0");
        totalScoreLBL = new JLabel("Total Score: 0");
        currentRoundLBL = new JLabel("Current Round: 1");

        labelContainer.add(currentScoreLBL);
        labelContainer.add(totalScoreLBL);
        labelContainer.add(currentRoundLBL);
    }

    private ImageIcon loadImageIcon(int value) {
        return new ImageIcon(IMG_PATH + value + ".png");
    }

    public static void main(String[] args) {
        new Farkle();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Implement actionPerformed logic
    }

    // Other methods as needed
}
