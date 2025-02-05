package cellsociety.model.cell;

import java.awt.geom.Point2D;

/**
 * A wrapper method to pass cell state/information updates from one class to another
 *
 * @author Owen Jennings
 */
public class CellStateUpdate {

  private final Point2D myLocation;
  private final int myNextState;


  /**
   * Initialize a Cell State Update Object
   *
   * @param location    location you which to update
   * @param myNextState new state to update to
   */
  public CellStateUpdate(Point2D location, int myNextState) {
    this.myLocation = location;
    this.myNextState = myNextState;
  }


  /**
   * Get row of cell to update
   *
   * @return row of cell to update
   */
  public int getRow() {
    return (int) myLocation.getX();
  }

  /**
   * Get column of cell to update
   *
   * @return column of cell to update
   */
  public int getCol() {
    return (int) myLocation.getY();
  }

  /**
   * Get state to update to
   *
   * @return integer for state to update
   */
  public int getState() {
    return myNextState;
  }
}
