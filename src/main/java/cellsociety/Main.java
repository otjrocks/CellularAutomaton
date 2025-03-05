package cellsociety;

import cellsociety.utility.CreateNewSimulation;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main file for application.
 *
 * @author Owen Jennings
 * @author Justin Aronwald
 */
public class Main extends Application {

  /**
   * Initialize what will be displayed.
   */
  @Override
  public void start(Stage stage) {
    CreateNewSimulation simulationManager = new CreateNewSimulation();
    simulationManager.launchNewSimulation();
  }

}
