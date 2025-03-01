package cellsociety.model.cell;

import java.awt.geom.Point2D;

/**
 * A cell representation for the WaTor simulation. The cell is immutable to prevent any unintended
 * changes in the grid
 *
 * @author Owen Jennings
 */
public class WaTorWorldCell extends Cell {

  public static final int DEFAULT_HEALTH = 5;
  private final int health;
  private final double reproductionEnergy;

  /**
   * Create a WaTor cell with the default health amount. The cell is immutable to prevent unintended
   * changes to the grid
   */
  public WaTorWorldCell(int state, Point2D location) {
    super(state, location);
    this.health = DEFAULT_HEALTH;
    this.reproductionEnergy = 0; // by default cell has no reproductive energy
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
  public WaTorWorldCell(int state, Point2D location, int health, double reproductionEnergy) {
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
   * Get the reproductive energy of the cell
   *
   * @return the reproductive energy of the cell
   */
  public double getReproductionEnergy() {
    return reproductionEnergy;
  }
}
