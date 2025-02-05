package cellsociety;

import static cellsociety.config.MainConfig.BOLD_FONT_PATH;
import static cellsociety.config.MainConfig.DEFAULT_FONT_PATH;
import static cellsociety.config.MainConfig.HEIGHT;
import static cellsociety.config.MainConfig.STYLESHEET_PATH;
import static cellsociety.config.MainConfig.TITLE;
import static cellsociety.config.MainConfig.WIDTH;

import cellsociety.controller.MainController;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
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
    initializeStage(stage);
  }

  private void initializeStage(Stage stage) {
    ScrollPane scrollPane = createMainScrollPane();

    Scene scene = new Scene(scrollPane, WIDTH, HEIGHT);
    Font.loadFont(getClass().getResourceAsStream(DEFAULT_FONT_PATH), 14);
    Font.loadFont(getClass().getResourceAsStream(BOLD_FONT_PATH), 14);
    scene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());
    new MainController(stage, root); // create main controller and give access to main root
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();
  }

  private ScrollPane createMainScrollPane() {
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setContent(root);
    return scrollPane;
  }
}
