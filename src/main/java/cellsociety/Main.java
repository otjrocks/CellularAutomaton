package cellsociety;

import static cellsociety.config.MainConfig.HEIGHT;
import static cellsociety.config.MainConfig.TITLE;
import static cellsociety.config.MainConfig.WIDTH;
import cellsociety.controller.MainController;
import cellsociety.utility.CreateNewSimulation;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 * Main file for application
 */
public class Main extends Application {

  private final Group root = new Group();

  /**
   * Initialize what will be displayed.
   */
  @Override
  public void start(Stage stage) {
    CreateNewSimulation simulationManager = new CreateNewSimulation();
    simulationManager.launchNewSimulation();
  }
}
