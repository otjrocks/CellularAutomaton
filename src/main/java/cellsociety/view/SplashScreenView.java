package cellsociety.view;

import static cellsociety.config.MainConfig.MESSAGES;

import cellsociety.view.components.AlertField;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SplashScreenView extends VBox {
  private final Stage splashScreenStage;
  public static final double ELEMENT_SPACING = 5;

  private final AlertField myAlertField;
  public static final int WIDTH = 700;
  public static final int HEIGHT = 800;

  private ComboBox<String> languageDropdown;
  private Text changeLanguageText;


  public SplashScreenView(AlertField myAlertField, Stage stage) {
    this.myAlertField = myAlertField;
    this.setSpacing(ELEMENT_SPACING * 2);
    this.splashScreenStage = stage;
    initializeSplashScreen(stage);
  }

  private void initializeSplashScreen(Stage stage) {

    Text title = new Text(MESSAGES.getString("SPLASH_HEADER"));
    Text description = new Text(MESSAGES.getString("SPLASH_DESCRIPTION"));
    Text instructions = new Text(MESSAGES.getString("SPLASH_INSTRUCTIONS"));
    //Text blankButton = new Text(MESSAGES.getString("SPLASH_CREATE_BLANK_BUTTON"));
    //Text loadButton = new Text(MESSAGES.getString("SPLASH_LOAD_BUTTON"));

    Text changeLanguageButton = new Text(MESSAGES.getString("CHANGE_LANGUAGE"));
    Text changeThemeButton = new Text(MESSAGES.getString("CHANGE_THEME"));

    createLanguageDropdown();



    this.getChildren().addAll(title, description, changeLanguageText, languageDropdown);



    Scene splashScreenScene = new Scene(this, WIDTH, HEIGHT);
    splashScreenStage.setScene(splashScreenScene);
  }

  private void createLanguageDropdown() {
    languageDropdown = new ComboBox<>();
    changeLanguageText = new Text(MESSAGES.getString("CHANGE_LANGUAGE"));

    String propertiesFolderPath = "src/main/resources/cellsociety/languages/";
    List<String> languages = fetchLanguages(propertiesFolderPath);

    if (languages.isEmpty()) {
      myAlertField.flash(MESSAGES.getString("NO_LANGUAGES_FOUND"), false);
    }

    for (String language : languages) {
      languageDropdown.getItems().add(language);
    }
    if (languages.contains("English")) {
      languageDropdown.setValue("English");
    } else {
      languageDropdown.setValue(languages.getFirst());
    }

    languageDropdown.setOnAction(event -> {
      String language = languageDropdown.getValue();
      System.out.println("Selected language: " + language);
    });
  }

  private List<String> fetchLanguages(String propertiesFolderPath) {
    List<String> languages = new ArrayList<>();
    File directory = new File(propertiesFolderPath);
    // this next line was from an LLM
    File[] files = directory.listFiles((dir, name) -> name.endsWith(".properties"));

    if (files != null) {
      for (File file : files) {
        String fileNameWithoutExtension = file.getName().replace(".properties", "");
        languages.add(fileNameWithoutExtension);
      }
    }

    return languages;
  }


  public void show() {
    splashScreenStage.show();
  }

}
