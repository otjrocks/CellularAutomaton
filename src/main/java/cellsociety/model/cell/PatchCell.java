package cellsociety.model.cell;

import java.awt.geom.Point2D;

public class PatchCell  extends Cell {
  private int sugar;
  private final int sugarGrowBackRate;
  private final int sugarGrowBackInterval;
  private int intervalsSinceLastGrowBack;

  /**
   * The default constructor for a Cell
   *
   * @param state    :    The initial state of a cell, represented as an int
   * @param location : The location in the simulation grid represented by a Point2D, where the (x:
   *                 row, y: col) value is the relative location of the cell compared to other cells
   *                 in the grid.
   */
  public PatchCell(int state, int sugar, int sugarGrowBackInterval, int sugarGrowBackRate, Point2D location) throws IllegalArgumentException {
    super(state, location);
    if (sugar < 0 || sugarGrowBackRate < 0 || sugarGrowBackInterval < 1) {
      throw new IllegalArgumentException("Sugar values and interval must be non-negative, interval must be at least 1.");
    }
    this.sugar = sugar;
    this.sugarGrowBackRate = sugarGrowBackRate;
    this.sugarGrowBackInterval = sugarGrowBackInterval;
    this.intervalsSinceLastGrowBack = 0;
  }

  /**
   * getter for the current sugar level
   * @return - an integer representation of the current level of sugar for the patch
   */
  public int getSugar() {
    return sugar;
  }

  /**
   * setter for the sugar
   * @param sugar - the current sugar contained in the cell
   */
  public void setSugar(int sugar) {
    this.sugar = sugar;
  }

  /**
   * getter for the sugar grow back rate
   * @return - the int value of how much to increment the sugar by
   */
  public int getSugarGrowBackRate() {
    return sugarGrowBackRate;
  }

  /**
   * getter for the sugar grow back interval
   * @return - the int representation of the time it takes before sugar can be replenished.
   */
  public int getSugarGrowBackInterval() {
    return sugarGrowBackInterval;
  }

  /**
   * setter to increment the current sugar by the grow back rate of sugar and reset the intervals back to 0
   */
  public void regenerateSugar() {
    intervalsSinceLastGrowBack++;
    if (intervalsSinceLastGrowBack >= sugarGrowBackInterval) {
      sugar += sugarGrowBackRate;
      intervalsSinceLastGrowBack = 0;
    }
  }
}
