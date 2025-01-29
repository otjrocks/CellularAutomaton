package cellsociety.model;

import cellsociety.model.cell.Cell;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that handles working with cells within a simulation grid.
 */
public class Grid {

  private final int myWidth;
  private final int myHeight;
  private final Map<Point2D, Cell> myCells;

  /**
   * Initialize a data structure to store a grid with the defined width and height
   *
   * @param width:  Width of a grid represented as the number of cells spanning the width (int)
   * @param height: Height of a grid represented as the number of cells spanning the height (int)
   */
  public Grid(int width, int height) {
    myWidth = width;
    myHeight = height;
    myCells = new HashMap<>();
  }

  /**
   * Add a cell to the grid
   *
   * @param cell: The cell you which to add
   * @return true if the cell is successfully added. false if the cell cannot be added because the
   * cells location is improperly formatted or because there is already a cell in the specified
   * location
   */
  public boolean addCell(Cell cell) {
    if (myCells.containsKey(
        cell.getLocation())) {  // cannot add a cell if the specified location already holds a seperate cell
      return false;
    }
    return attemptAddCell(cell);
  }

  /**
   * Remove the cell at a specified location
   *
   * @param location: The Point2D location where you wish to remove a cell
   * @return true if the removal finished correct, false if the location specified cannot be found
   */
  public boolean removeCell(Point2D location) {
    if (!myCells.containsKey(location)) {
      return false;
    }
    myCells.remove(location);
    return true;
  }

  private boolean attemptAddCell(Cell cell) {
    // attempts to add cell to grid. Fails and returns false if cell provided does not have a properly formatted location or does not fit within the grid's width and height
    if (cell.getX() < 0 || cell.getX() >= myWidth || cell.getY() < 0 || cell.getY() >= myHeight) {
      return false;
    }
    myCells.put(cell.getLocation(), cell);
    return true;
  }
}
