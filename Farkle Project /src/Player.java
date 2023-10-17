/**
 * Player
 */
public class Player {
    private String name;
    private int score;

    // constructor for player class
    public Player(String name) {
        this.name = name;
    }

    // getter methods
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    // int method that returns nothing but adds points to the score int obj
    public void addToScore(int points) {
        score += points;
    }
}