# Farkle Dice Group Project

COSC 1337 Programming Fund. II group project for Farkle dice game.
**Overview:**
Farkle is a Java-based implementation of the classic dice game Farkle with an added hint feature. The game supports both multiplayer and a player-versus-computer mode. Players take turns rolling six dice and scoring based on various combinations. The goal is to be the first to reach a total score of 10,000 points.

**Features:**
Dice Rolling: Players can roll six dice to accumulate points based on scoring combinations.

Scoring Combinations: The game recognizes various scoring combinations, including three of a kind, straights, and individual ones and fives.

Multiplayer and Vs PC Modes: The game supports both multiplayer mode (up to N players) and a player versus computer mode.

Hint Feature: Players can use the "Hint" button to get advice on whether the current dice configuration poses a risk of getting a Farkle.
**HOW TO PLAY:**

1. Launch the game.
2. Choose the player type: Multiplayer or Vs PC.
3. If Multiplayer, enter the number of players and their names.
4. If Vs PC, enter your name, and play against the computer.
5. Roll the dice by clicking the "Roll" button.
6. Select dice to score points or continue rolling for better combinations.
7. Click "Score" to lock in points, "Stop" to end the turn, or "Hint" to get advice on the current dice configuration.
   Additional Notes:
   The game tracks players' scores, current rounds, and games won.
   In Vs PC mode, the computer makes strategic decisions during its turn.
   The game offers the option to continue playing with the same players or exit after a game ends.
   Implementation Details
   The game is implemented in Java and features a modular design. The HintCalculator class provides probability-based hints, and the Player class encapsulates player-related functionalities. The game interface utilizes Java Swing for a simple graphical user interface.

**Dependencies**
The game relies on Java Swing for the graphical user interface.

**How to Run**

1. Compile the Java files.
2. Run the compiled Farkle class:
   javac Farkle.java
   java Farkle
3. Follow the on-screen prompts to set up the game and enjoy playing!

**RULES**
The goal is to be the player with 10,000 or more points on the final turn.
• Each player starts their turn by rolling six dice.
• After rolling, the player sets aside specific dice combinations which have a score value.
• The player can either bank the points earned that turn to their total points and pass the dice to the next player or risk the points earned that turn and roll the remaining dice again, hoping to earn more points.
• If the remaining dice rolled do not have a scoring combination, then the player has “FARKLED” and points earned that turn are gone forever.
• If the player has successfully with some luck and strategy used all six dice to score, then the player gets to roll all six dice again for a chance to earn more points. This “hot dice” move can be repeated over and over.
• When the player has either banked their points or Farkled, the dice are passed to the next player.
• The next player rolls all six dice. Play continues until it is your turn again.
• To win at Farkle you must be the player with the highest score above 10,000 points on the final round of play.

**COMBINATIONS**
The following combinations can be scored:

• a single dice showing a 1 or a 5,

• three of a kind, such as, 2 2 2

• three pairs, 2 2, 4 4, 6 6 for example

• a six-dice straight, 1 2 3 4 5 6

You must select at least one scoring die after each roll. After you select the dice you want to keep you can either risk all the points earned this turn and roll the remaining dice (the fewer dice you roll the greater the chance you will Farkle, see Farkle Odds) or bank those hard earned points on your way to 10,000+ points.
When 10,000 or more points are scored, that player goes out. Each player gets one more turn to beat the high score.

Scoring is based on selected dice in each roll. You cannot earn points by combining dice from different rolls. For example, if you roll a 5, 5 (50 points each x 2 for 100 points), and then roll another 5 (50 points), you can’t combine the 5, 5 with the 5, to form three of a kind (500 points).

If none of your dice rolled earn points, you get a Farkle and lose any earned points that round. Warning! Three Farkles in a row and you lose 1,000 points. Ouch!
The 1 and 5 spot dice are super special, as they are the only dice that can be scored outside of a combination (such as three of a kind).

• A 1 earns 100 points

• A 5 earns 50 points

• Three of kind earn the face value times 100, e.g., 2, 2, 2 = 200 points

• Three 1s are special and earn 1,000 points

• Three Pairs is worth 750 points

• A straight earns 1,000 points

**ODDS**

Rolling a Farkle:

6 Dice Left = 2.52% or 1176/46656

5 Dice Left = 7.72% or 600/7776

4 Dice Left = 15.74% or 204/1296

3 Dice Left = 27.78% or 60/216

2 Dice Left = 44.44% or 16/36

1 Dice Left = 66.67% or 4/6

Odds of Scoring:

6 Dice Left = 97.48% or 45480/46656

5 Dice Left = 92.28% or 7176/7776

4 Dice Left = 85.25% or 1092/1296

3 Dice Left = 72.22% or 156/216

2 Dice Left = 55.56% or 20/36

1 Dice Left = 33.34% or 2/6
