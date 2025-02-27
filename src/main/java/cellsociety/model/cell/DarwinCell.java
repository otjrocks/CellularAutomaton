package cellsociety.model.cell;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class DarwinCell extends Cell{
  private int prevSpecies;
  private int orientation;
  private int curInstructionIndex;
  private int infectionCountdown;
  private List<String> instructions;

  /**
   * The default constructor for a Cell
   *
   * @param state    :    The initial state of a cell, represented as an int. In this case, it represents a species
   * @param location : The location in the simulation grid represented by a Point2D, where the (x:
   *                 row, y: col) value is the relative location of the cell compared to other cells
   *                 in the grid.
   */
  public DarwinCell(int state, Point2D location) throws IllegalArgumentException {
    super(state, location);
    this.orientation = 0;
    this.curInstructionIndex = 0;
    this.infectionCountdown = 0;
    this.instructions = new ArrayList<>();
  }

  /**
   * Create a Darwin Cell
   *
   * @param state - The initial state of a cell, represented as an int. In this case, it represents a species
   * @param location - The location in the simulation grid represented by a Point2D, where the (x:
   *                 row, y: col) value is the relative location of the cell compared to other cells
   *                 in the grid.
   * @param orientation - the initial direction that the cell faces
   * @param infectionCountdown - the number of steps left before a potential infection revers
   * @param instructions - the list of movement or infection commands
   */
  public DarwinCell(int state, Point2D location, int orientation, int infectionCountdown, List<String> instructions) throws IllegalArgumentException {
    super(state, location);
    this.orientation = orientation;
    this.instructions = new ArrayList<>(instructions);
    this.curInstructionIndex = 0;
    this.prevSpecies = -1;
    this.infectionCountdown = infectionCountdown;
  }

  /**
   * Get the current instruction of the Species
   *
   * @return - the current instruction that the species will execute
   */
  public String getInstruction() {
    if (curInstructionIndex < 0 || curInstructionIndex >= instructions.size() || instructions.isEmpty()) {
      return "";
    }
    return instructions.get(curInstructionIndex);
  }

  /**
   * Handles incrementing of instructions
   *
   */
  // had help from ChatGPT to get the logic of looping instructions
  public void nextInstruction() {
    if (!instructions.isEmpty()) {
      curInstructionIndex = (curInstructionIndex + 1) % instructions.size();
    }
  }

  /**
   * Rotates the current orientation to the left
   *
   * @param degrees - the degree that the current orientation will rotate by
   */
  public void turnLeft(int degrees) {
    orientation = (orientation - degrees) % 360;
  }

  /**
   * Rotates the current orientation to the right
   *
   * @param degrees - the degree that the current orientation will rotate by
   */
  public void turnRight(int degrees) {
    orientation = (orientation + degrees) % 360;
  }

  /**
   * Decreases the Infection Countdown
   *
   */
  public void handleInfectionDecrease() {
    if (infectionCountdown > 0) {
      infectionCountdown--;
    }
  }

}
