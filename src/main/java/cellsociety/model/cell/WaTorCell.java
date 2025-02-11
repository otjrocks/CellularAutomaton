package cellsociety.model.cell;

import java.awt.geom.Point2D;

/**
 * A cell representation for the WaTor simulation
 *
 * @author Owen Jennings
 */
public class WaTorCell extends Cell {

  public static final int DEFAULT_HEALTH = 5;
  private int health;
  private double reproductionEnergy;

  /**
   * Create a WaTor cell with the default health amount
   */
  public WaTorCell(int state, Point2D location) {
    super(state, location);
    this.health = DEFAULT_HEALTH;
  }

  /**
   * Create a WaTor cell
   *
   * @param state               :    The initial state of a cell, represented as an int
   * @param location            : The location in the simulation grid represented by a Point2D,
   *                            where the (x: row, y: col) value is the relative location of the
   *                            cell compared to other cells in the grid.
   * @param health:             initial health of the cell
   * @param reproductionEnergy: initial reproduction energy of cell
   */
  public WaTorCell(int state, Point2D location, int health, double reproductionEnergy)
      throws IllegalArgumentException {
    super(state, location);
    this.health = health;
    this.reproductionEnergy = reproductionEnergy;
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
   * Resets the health of the cell to the default health
   */
  public void resetHealth() {
    this.health = DEFAULT_HEALTH;
  }

  /**
   * Set health of a cell
   */
  public void setHealth(int health) {
    this.health = health;
  }

  /**
   * Add to a cells health
   *
   * @param amount: the amount you wish to add
   */
  public void addHealth(int amount) {
    health += amount;
  }

  /**
   * Get the reproductive energy of the cell
   *
   * @return the reproductive energy of the cell
   */
  public double getReproductionEnergy() {
    return reproductionEnergy;
  }

  /**
   * Sets reproductive energy back to 0.
   */
  public void resetReproductionEnergy() {
    reproductionEnergy = 0;
  }

  /**
   * Set reproductive energy
   *
   * @param reproductionEnergy: new value for energy
   */
  public void setReproductionEnergy(double reproductionEnergy) {
    this.reproductionEnergy = reproductionEnergy;
  }

  /**
   * Decrease the health of cell
   */
  public void decreaseHealth() {
    health -= 1;
  }

}
