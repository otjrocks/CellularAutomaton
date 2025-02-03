package cellsociety.model.cell;

import java.awt.geom.Point2D;

/**
 * A cell representation for the WaTor simulation
 *
 * @author Owen Jennings
 */
public class WaTorCell extends Cell {

  private int health;
  private int reproductionEnergy;

  /**
   * Create a WaTor cell
   *
   * @param state    :    The initial state of a cell, represented as an int
   * @param location : The location in the simulation grid represented by a Point2D, where the (x:
   *                 row, y: col) value is the relative location of the cell compared to other cells
   *                 in the grid.
   * @param health:  initial health of the cell
   */
  public WaTorCell(int state, Point2D location, int health) throws IllegalArgumentException {
    super(state, location);
    this.health = health;
    reproductionEnergy = 0;
  }

  /**
   * Get the health of the cell
   *
   * @return the health of the cell
   */
  public int getHealth() {
    return health;
  }

  /**
   * Add to a cells health
   * @param amount: the amount you wish to add
   */
  public void addHealth(int amount) {
    reproductionEnergy += amount;
  }

  /**
   * Get the reproductive energy of the cell
   *
   * @return the reproductive energy of the cell
   */
  public int getReproductionEnergy() {
    return reproductionEnergy;
  }

  /**
   * Sets reproductive energy back to 0.
   */
  public void resetReproductionEnergy() {
    reproductionEnergy = 0;
  }

  @Override
  public void setState(int newState) {
    super.setState(newState);
    health--;
    reproductionEnergy++;
  }
}
