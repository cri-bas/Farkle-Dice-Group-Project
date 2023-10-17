import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class FarkleGame {
    public static void main(String[] args) {
        // create scanner obj
        Scanner scanner = new Scanner(System.in);

        // random
        Random random = new Random();

        // list of players
        List<Player> players = new ArrayList<>();

        // list of dices
        List<Dice> dice = new ArrayList<>();

        // initialize 2 players and dice, you can add more players here
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));

        // 6 dice
        dice.add(new Dice(6));
        dice.add(new Dice(6));
        dice.add(new Dice(6));
        dice.add(new Dice(6));
        dice.add(new Dice(6));
        dice.add(new Dice(6));

        Game game = new Game(players, dice, random, scanner);
        game.start();
    }
}
