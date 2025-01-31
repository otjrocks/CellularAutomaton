package cellsociety.view;

import cellsociety.model.cell.CellStateUpdate;
import cellsociety.model.Grid;
import cellsociety.model.simulation.Simulation;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Simulation view for the visualizing the simulation
 *
 * @author Owen Jennings
 */
public class SimulationView extends Group {
  private final GridView myGridView;

  /**
   * Create a simulation view
   *
   * @param width:  width of simulation view
   * @param height: height of simulation view
   */
  public SimulationView(int width, int height, int numRows, int numCols) {
    myGridView = new GridView(width, height, numRows, numCols);
    getChildren().add(myGridView);
  }

  public void step(Grid grid, Simulation simulation) {
    List<CellStateUpdate> stateUpdates = grid.getNextStatesForAllCells(simulation.getRules());
    for (CellStateUpdate stateUpdate : stateUpdates) {
      Paint nextColor = stateUpdate.getState() == 1 ? Color.GREEN : Color.LIGHTGRAY;  // TODO: hardcoded for now, will update after adding new simulation tyoes
      myGridView.setColor(stateUpdate.getRow(), stateUpdate.getCol(), nextColor);
    }
  }


}
