package cellsociety.model.cell;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * A record storing the information required for a darwin cell.
 *
 * @param state              The state int value
 * @param location           The location of the cell
 * @param orientation        The orientation of the cell
 * @param infectionCountdown The infection countdown
 * @param currentInstIndex   The current instance index
 * @param instructions       The current instructions list
 * @param infected           Whether the cell is infected
 * @param previousSpecies    The previous species of the cell stored as an int
 */
public record DarwinCellRecord(int state, Point2D location, int orientation,
                               int infectionCountdown, int currentInstIndex,
                               List<String> instructions, boolean infected,
                               int previousSpecies) {

}