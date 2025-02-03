package cellsociety.view;

import cellsociety.model.cell.CellStateUpdate;
import cellsociety.model.Grid;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.StateInfo;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javax.swing.plaf.nimbus.State;

/**
 * Simulation view for the visualizing the simulation
 *
 * @author Owen Jennings
 */
public class SimulationView extends Group {
  private final GridView myGridView;
  private final Simulation mySimulation;

  /**
   * Create a simulation view
   *
   * @param width:  width of simulation view
   * @param height: height of simulation view
   * @param grid: initial grid of the simulation view
   * @param simulation: initial simulation of the simulation view
   */
  public SimulationView(int width, int height, int numRows, int numCols, Grid grid, Simulation simulation) {
    myGridView = new GridView(width, height, numRows, numCols);
    mySimulation = simulation;
    initializeInitialGridStates(numRows, numCols, grid);
    getChildren().add(myGridView);
  }

  private void initializeInitialGridStates(int numRows, int numCols, Grid grid) {
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        int nextState = grid.getCell(row, col).getState();
        Color nextColor = mySimulation.getStateInfo(nextState).getColor();
        myGridView.setColor(row, col, nextColor);
      }
    }
  }

  public void step(Grid grid, Simulation simulation) {
    List<CellStateUpdate> stateUpdates = simulation.getRules().getNextStatesForAllCells(grid);
    for (CellStateUpdate stateUpdate : stateUpdates) {
      int nextState = stateUpdate.getState();
      StateInfo nextStateInfo = simulation.getStateInfo(nextState);
      Paint nextColor = nextStateInfo.getColor();
      myGridView.setColor(stateUpdate.getRow(), stateUpdate.getCol(), nextColor);
    }
  }


}
