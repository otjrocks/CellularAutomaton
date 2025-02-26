package cellsociety.utility;

import cellsociety.controller.MainController;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import static cellsociety.config.MainConfig.HEIGHT;
import static cellsociety.config.MainConfig.TITLE;
import static cellsociety.config.MainConfig.WIDTH;

public class CreateNewSimulation {

  private final Stage myStage;
  private final Group myRoot;

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
//    scrollPane.addEventFilter(MouseEvent.ANY, Event::consume);
    scrollPane.setContent(myRoot);
    return scrollPane;
  }

}
