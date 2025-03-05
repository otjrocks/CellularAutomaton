package cellsociety.model.cell;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import cellsociety.config.SimulationConfig;

/**
 * The cell type used for Darwin simulation.
 *
 * @author Justin Aronwald
 */
public class DarwinCell extends Cell {
  private static final int stateValChecker = 10;

  private final int prevSpecies;
  private int orientation;
  private int curInstructionIndex;
  private int infectionCountdown;
  private final boolean isInfected;
  private final List<String> instructions;

  /**
   * The default constructor for a Cell.
   *
   * @param state    The initial state of a cell, represented as an int. In this case, it represents
   *                 a species
   * @param location The location in the simulation grid represented by a Point2D, where the (x:
   *                 row, y: col) value is the relative location of the cell compared to other cells
   *                 in the grid.
   */
  public DarwinCell(int state, Point2D location) {
    super(state, location);
    this.orientation = 0;
    this.curInstructionIndex = 0;
    this.infectionCountdown = 0;
    if (state < stateValChecker) {
      this.instructions = SimulationConfig.assignInstructionsFromState(state);
    } else {
      this.instructions = new ArrayList<>();
    }
    this.isInfected = false;
    this.prevSpecies = 0;
  }

  /**
   * Create a Darwin Cell.
   *
   * @param record The darwin cell record for the cell
   */
  public DarwinCell(DarwinCellRecord record) {
    super(record.state(), record.location());
    this.orientation = record.orientation();
    this.instructions = new ArrayList<>(record.instructions());
    this.curInstructionIndex = record.currentInstIndex();
    this.prevSpecies = record.previousSpecies();
    this.infectionCountdown = record.infectionCountdown();
    this.isInfected = record.infected();
  }

  /**
   * Get the current instruction of the Species.
   *
   * @return - the current instruction that the species will execute
   */
  public String getInstruction() {
    if (curInstructionIndex < 0 || curInstructionIndex >= instructions.size()) {
      return "";
    }
    return instructions.get(curInstructionIndex);
  }

  /**
   * Handles incrementing of instructions.
   */
  // had help from ChatGPT to get the logic of looping instructions
  public void nextInstruction() {
    if (!instructions.isEmpty()) {
      curInstructionIndex = (curInstructionIndex + 1) % instructions.size();
    }
  }

  /**
   * Getter for the state of the previous species.
   *
   * @return - an int representation of the previous species' state
   */
  public int getPrevState() {
    return prevSpecies;
  }


  /**
   * Rotates the current orientation to the left by a rounded degrees.
   *
   * @param degrees  - the degrees to which the orientation will rotate
   * @param stepSize - the number of directions to look in -- tells the neighbor configuration
   */
  public void turnLeft(int degrees, int stepSize) {
    orientation = (orientation - degrees) % 360;
    if (orientation < 0) {
      orientation += 360;
    }
    orientation = roundOrientation(orientation, stepSize);
  }

  /**
   * Rotates the current orientation to the right by a rounded degrees.
   *
   * @param degrees  - the degrees to which the orientation will rotate
   * @param stepSize - the number of directions to look in -- tells the neighbor configuration
   */

  public void turnRight(int degrees, int stepSize) {
    orientation = (orientation + degrees) % 360;
    orientation = roundOrientation(orientation, stepSize);
  }

  //Had ChatGPT help with math logic
  private int roundOrientation(int orientation, int stepSize) {
    int[] mooreAngles = {0, 45, 90, 135, 180, 225, 270, 315};
    int[] vonNeumannAngles = {0, 90, 180, 270};

    int[] angles = (stepSize == 8) ? mooreAngles : vonNeumannAngles;

    int closestAngle = angles[0];
    int minDiff = Math.abs(orientation - closestAngle);

    for (int angle : angles) {
      int diff = Math.abs(orientation - angle);
      if (diff < minDiff) {
        minDiff = diff;
        closestAngle = angle;
      }
    }

    return closestAngle;
  }

  /**
   * Decreases the Infection Countdown.
   */
  public void handleInfectionDecrease() {
    if (infectionCountdown > 0) {
      infectionCountdown--;
    }
  }

  //had help from ChatGPT to generate the radian logic

  /**
   * Determines the direction that the cell faces.
   *
   * @return - a unit vector in the direction the cell faces
   */
  public Point2D getFrontDirection() {
    double radians = Math.toRadians(orientation);
    double deltaX = Math.cos(radians);
    double deltaY = -Math.sin(radians);

    return new Point2D.Double(deltaX, deltaY);
  }

  /**
   * Sets the current instruction index to a new index.
   *
   * @param instructionIndex - the new index to be set to
   */
  public void setCurInstructionIndex(int instructionIndex) {
    this.curInstructionIndex = instructionIndex;
  }


  /**
   * Gets the instruction list.
   *
   * @return - the list of instructions for the species
   */
  public List<String> getAllInstructions() {
    return instructions;
  }

  /**
   * Getter for the orientation.
   *
   * @return - the angle which the species is turned
   */
  public int getOrientation() {
    return orientation;
  }

  /**
   * Getter for the infection countdown.
   *
   * @return - the number of steps left before a potential infection reverse
   */
  public int getInfectionCountdown() {
    return infectionCountdown;
  }

  /**
   * Setter for an instruction (used mainly for tests).
   *
   * @param instruction - an instruction for a species
   */
  public void setInstructions(String instruction) {
    this.instructions.add(instruction);
  }

  /**
   * Getter for the instruction index.
   *
   * @return - the index of the current instruction to run
   */
  public int getCurInstructionIndex() {
    return curInstructionIndex;
  }

  /**
   * Getter for the infection state of the cell.
   *
   * @return - whether the cell is infected
   */
  public boolean getInfected() {
    return isInfected;
  }

}
