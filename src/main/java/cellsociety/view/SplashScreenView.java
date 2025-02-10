package cellsociety.view;

import static cellsociety.config.MainConfig.MESSAGES;

import cellsociety.view.components.AlertField;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SplashScreenView extends VBox {
  private final Stage splashScreenStage;
  public static final double ELEMENT_SPACING = 5;

  private final AlertField myAlertField;
  public static final int WIDTH = 700;
  public static final int HEIGHT = 800;


  public SplashScreenView(AlertField myAlertField, Stage stage) {
    this.myAlertField = myAlertField;
    this.setSpacing(ELEMENT_SPACING * 2);
    this.splashScreenStage = stage;
    initializeSplashScreen();
  }

  private void initializeSplashScreen() {

    Text title = new Text(MESSAGES.getString("CREATE_SPLASH_HEADER"));

    Scene splashScreenScene = new Scene(this, WIDTH, HEIGHT);
    splashScreenStage.setScene(splashScreenScene);
  }


  public void show() {
    splashScreenStage.show();
  }

}
