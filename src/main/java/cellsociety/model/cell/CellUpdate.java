package cellsociety.model.cell;

import java.awt.geom.Point2D;

/**
 * A wrapper method to pass update information from one class to another. The location indicates
 * where in the grid the update is occurring. Cell contains all the state information for the update
 * to the location.
 *
 * @author Owen Jennings
 */
public class CellUpdate {

  private final Point2D myLocation;
  private final Cell myNextCell;


  /**
   * Initialize a Cell State Update Object.
   *
   * @param location location you which to update
   * @param nextCell new cell to update at location
   */
  public CellUpdate(Point2D location, Cell nextCell) {
    this.myLocation = location;
    this.myNextCell = nextCell;
  }


  /**
   * Get row of cell to update.
   *
   * @return row of cell to update
   */
  public int getRow() {
    return (int) myLocation.getX();
  }

  /**
   * Get column of cell to update.
   *
   * @return column of cell to update
   */
  public int getCol() {
    return (int) myLocation.getY();
  }

  /**
   * Get state to update to.
   *
   * @return integer for state to update
   */
  public int getState() {
    return myNextCell.getState();
  }

  /**
   * Get cell for update.
   *
   * @return the cell with all updated information
   */
  public Cell getNextCell() {
    return myNextCell;
  }
}
