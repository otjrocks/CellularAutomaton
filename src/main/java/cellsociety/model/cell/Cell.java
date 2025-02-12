package cellsociety.model.cell;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * An abstract Cell model that allows for a state stored in an integer value The Cell class is
 * immutable, so that attributes cannot be improperly updated.
 *
 * @author Owen Jennings
 */
public abstract class Cell {

  private final int myState;
  private final Point2D myLocation;

  /**
   * The default constructor for a Cell
   *
   * @param state:    The initial state of a cell, represented as an int
   * @param location: The location in the simulation grid represented by a Point2D, where the (x:
   *                  row, y: col) value is the relative location of the cell compared to other
   *                  cells in the grid.
   */
  public Cell(int state, Point2D location) throws IllegalArgumentException {
    if (state < 0) {
      throw new IllegalArgumentException("Invalid state, state must be a positive integer");
    }
    myState = state;
    if (location == null ||
        location.getX() < 0 ||
        location.getY() < 0 ||
        location.getX() % 1 != 0 ||
        location.getY() % 1 != 0) { // Throw exception if
      throw new IllegalArgumentException(
          "Invalid cell location, location must be represented as a positive integer");
    }
    // create new Point for location to ensure point is immutable in the future
    myLocation = new Double(location.getX(), location.getY());
  }

  /**
   * Get the current state of a cell
   *
   * @return integer representation of a cell's state
   */
  public int getState() {
    return myState;
  }

  /**
   * Get the location of a cell.
   *
   * @return The Point2D representation of a Cell's location
   */
  public Point2D getLocation() {
    return (Point2D) myLocation.clone();  // provide user with clone of location to prevent mutation
  }

  /**
   * Get row coordinate of cell
   *
   * @return int representing row location
   */
  public int getRow() {
    return (int) myLocation.getX();
  }

  /**
   * Get col coordinate of cell
   *
   * @return int representing col location
   */
  public int getCol() {
    return (int) myLocation.getY();
  }

}
