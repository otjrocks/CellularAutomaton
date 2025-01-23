# Rock Paper Scissors Lab Discussion
#### Owen Jennings (otj4), Justin Aronwald (jga18), Troy Ludwig (tdl21)


### High Level Design Goals

The game should have the following structure:
- Player class
- GameSettings (config/class)
- Game class
- Abstract Move class
  - Specific moves implementations such as Rock, Paper, Scissors

The game class should handle all the common logic for a round and create the players and moves based on the game settings. The game class will handle win and loss scenarios.
The move class should have some abstract method called beats() which returns a boolean for whether it beats another move.
There should also be getter methods to get information about a move such as its description and name.
The Player have a method to get user input for their next move. It have a number of lives/scores if the game has multiple rounds.
We will also need a static class to handle file reading and translation to create new move objects.

### CRC Card Classes

This class's purpose or value is to represent a customer's order:

| Player                          |         |
|---------------------------------|---------|
| Move getMove()                  | Scanner |
| int getScore()                  | Move    |
| int getLives()                  |         |
| void setLives(int lives)        |         |
| void incrementScore(int amount) |         |


| Game                                                                                                |        |
|-----------------------------------------------------------------------------------------------------|--------|
| void play()                                                                                         | GameSettings |
| private void initializePlayers(int num)                                                             |        |
| public void setPossibleMoves(List<Move> moves)                                                      |        |
| private List<Player> getWinners()                                                                   | Player |
| private void printMoveOptions() -> print who is currently playing and what their possible moves are | Moves  |


| Move (abstract)                                                     |              |
|---------------------------------------------------------------------|--------------|
| abstract boolean beats(Move m) -> return if move beats another move |              |
| String getMoveName()                                                |              |
| String getMoveDescription()                                         |              |



### Use Cases

* A new game is started with five players, their scores are reset to 0.
 ```java
Game game = new Game();
game.initializePlayers(5);
game.play();
 ```

* A player chooses his RPS "weapon" with which he wants to play for this round.
 ```java
Game game = new Game();
game.printMoveOptions();
Move move = currentPlayer.getMove();

 ```

* Given three players' choices, one player wins the round, and their scores are updated.

 ```java
import java.util.ArrayList;

public void play() {
  Move prevMove = null;
  for (Player currentPlayer : gamePlayers) {
    Move move = currentPlayer.getMove();
    if (move.beats(prevMove)) {
      currentPlayer.updateScore();
    }
    prevMove = move;
  }
}
 ```

* A new choice is added to an existing game and its relationship to all the other choices is updated.

Create a new move object for this move. Implement its comparator/beats method to handle whether it is "stronger", "tie", or "weaker" than all the other moves.
Practically, this could be implemented using an integer value for strength, to represent if it is stronger or weaker or time to another move.

* A new game is added to the system, with its own relationships for its all its "weapons".
 ```java
Game game = new Game();
MoveFileReader reader = new MoveFileReader();
List<Move> possibleMoves = reader.createMoves(File f);
game.setMoves(List<Move> moves);
 ```
