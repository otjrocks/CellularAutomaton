package cellsociety.utility;

import cellsociety.controller.MainController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import static cellsociety.config.MainConfig.HEIGHT;
import static cellsociety.config.MainConfig.TITLE;
import static cellsociety.config.MainConfig.WIDTH;

/**
 * Create a new simulation window
 *
 * @author Justin Aronwald
 */
public class CreateNewSimulation {

  private final Stage myStage;
  private final Group myRoot;

  /**
   * Create a new simulation without providing a stage or group that it should use
   */
  public CreateNewSimulation() {
    this.myStage = new Stage();
    this.myRoot = new Group();
  }

  /**
   * For JavaFX testing, you must use the stage provided to you
   *
   * @param stage State to create a new simulation for
   */
  public CreateNewSimulation(Stage stage) {
    this.myStage = stage;
    this.myRoot = new Group();
  }

  /**
   * Launch the main controller and initialize the stage for your new simulation window
   *
   * @return The main controller for this new simulation window
   */
  public MainController launchNewSimulation() {
    initializeStage(myStage);
    myStage.setTitle(TITLE);

    return new MainController(myStage, myRoot);
  }

  private void initializeStage(Stage stage) {
    ScrollPane scrollPane = createMainScrollPane();

    Scene scene = new Scene(scrollPane, WIDTH, HEIGHT);
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();
  }

  private ScrollPane createMainScrollPane() {
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPannable(true);
    scrollPane.setContent(myRoot);
    return scrollPane;
  }

}
