package cellsociety.model.cell;

import java.awt.geom.Point2D;

/**
 * A default implementation of a cell.
 *
 * @author Owen Jennings
 */
public class DefaultCell extends Cell {

  /**
   * The default constructor for a Cell.
   *
   * @param state    : The initial state of a cell, represented as an int
   * @param location : The location in the simulation grid represented by a Point2D, where the (x,y)
   *                 value is the relative location of the cell compared to other cells in the
   *                 grid.
   */
  public DefaultCell(int state, Point2D location) {
    super(state, location);
  }
}
