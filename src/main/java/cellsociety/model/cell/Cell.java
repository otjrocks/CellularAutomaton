package cellsociety.model.cell;

import java.awt.geom.Point2D;

/**
 * An abstract Cell model that allows for a state stored in an integer value
 *
 * @author Owen Jennings
 */
public abstract class Cell {

  private int myState;
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
    myLocation = location;
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
   * Modify the state of the current cell.
   *
   * @param newState: integer representation of the state you want the cell to have.
   */
  public void setState(int newState) {
    myState = newState;
  }

  /**
   * Get the location of a cell
   *
   * @return The Point2D representation of a Cell's location
   */
  public Point2D getLocation() {
    return myLocation;
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
