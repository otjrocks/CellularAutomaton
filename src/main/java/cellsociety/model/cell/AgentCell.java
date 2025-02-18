package cellsociety.model.cell;

import java.awt.geom.Point2D;

public class AgentCell extends Cell {
  private final int vision;
  private final int metabolism;
  private int sugar;

  /**
   * The default constructor for a Cell
   *
   * @param state    :    The initial state of a cell, represented as an int
   * @param location : The location in the simulation grid represented by a Point2D, where the (x:
   *                 row, y: col) value is the relative location of the cell compared to other cells
   *                 in the grid.
   */
  public AgentCell(int state, Point2D location, int vision, int metabolism, int sugar) throws IllegalArgumentException {
    super(state, location);
    if (vision < 0 || metabolism < 0 || sugar < 0) {
      throw new IllegalArgumentException("Attributes must be non-negative.");
    }
    this.vision = vision;
    this.metabolism = metabolism;
    this.sugar = sugar;
  }

  /**
   * getter for the vision
   * @return - the number of elements it searches for in the 4 directions
   */
  public int getVision() {
    return vision;
  }

  /**
   * getter for the metabolism
   * @return - an integer representation of sugar consumed per round
   */
  public int getMetabolism() {
    return metabolism;
  }

  /**
   * a getter for the sugar
   * @return - an integer representation of the agent's current amount of sugar
   */
  public int getSugar() {
    return sugar;
  }

}
