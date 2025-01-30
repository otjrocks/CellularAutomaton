package cellsociety.view;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.Simulation;
import java.util.Map;
import java.util.Map.Entry;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Main view for the visualizing the simulation
 *
 * @author Owen Jennings
 */
public class MainView extends Group {
  private final GridView myGridView;

  /**
   * Create a main view
   *
   * @param width:  width of main view
   * @param height: height of main view
   */
  public MainView(int width, int height, int numRows, int numCols) {
    myGridView = new GridView(width, height, numRows, numCols);
    getChildren().add(myGridView);
  }

  public void step(Grid grid, Simulation simulation) {
    Map<Cell, Integer> stateUpdates = grid.getNextStatesForAllCells(simulation.getRules());
    for (Entry<Cell, Integer> entry : stateUpdates.entrySet()) {
      Paint nextColor = entry.getValue() == 1 ? Color.GREEN : Color.LIGHTGRAY;  // TODO: hardcoded for now, will update after adding new simulation tyoes
      myGridView.setColor(entry.getKey().getRow(), entry.getKey().getCol(), nextColor);
    }
  }


}
