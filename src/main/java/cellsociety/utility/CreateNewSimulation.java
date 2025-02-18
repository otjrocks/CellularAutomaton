package cellsociety.utility;

import cellsociety.controller.MainController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
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

  public void launchNewSimulation() {
    initializeStage(myStage);
    myStage.setTitle(TITLE);

    new MainController(myStage, myRoot);
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
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setContent(myRoot);
    return scrollPane;
  }

}
