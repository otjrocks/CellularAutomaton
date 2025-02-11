package cellsociety.view;

import static cellsociety.config.MainConfig.MESSAGES;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;

import cellsociety.controller.MainController;
import cellsociety.view.components.AlertField;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
  private Button myChooseFileButton;
  private Runnable onStart;
  private MainController mainController;


  public SplashScreenView(AlertField myAlertField, Stage stage, MainController mainController,
      Runnable onStart) {
    this.myAlertField = myAlertField;
    this.setSpacing(ELEMENT_SPACING * 2);
    this.splashScreenStage = stage;
    this.onStart = onStart;
    this.mainController = mainController;

    initializeSplashScreen(onStart);
  }

  private void initializeSplashScreen(Runnable onStart) {

    Text title = new Text(MESSAGES.getString("SPLASH_HEADER"));
    Text description = new Text(MESSAGES.getString("SPLASH_DESCRIPTION"));
    Text instructions = new Text(MESSAGES.getString("SPLASH_INSTRUCTIONS"));
    //Text blankButton = new Text(MESSAGES.getString("SPLASH_CREATE_BLANK_BUTTON"));
    //Text loadButton = new Text(MESSAGES.getString("SPLASH_LOAD_BUTTON"));

    Text changeLanguageButton = new Text(MESSAGES.getString("CHANGE_LANGUAGE"));
    Text changeThemeButton = new Text(MESSAGES.getString("CHANGE_THEME"));

    createLanguageDropdown();
    this.getChildren().addAll(title, description, instructions, changeLanguageText, languageDropdown);
    createFileChooserButton();



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


  private void createFileChooserButton() {
    myChooseFileButton = new Button(MESSAGES.getString("CHOOSE_FILE_BUTTON"));
    myChooseFileButton.setOnAction(event -> {
      try {
        mainController.handleNewSimulationFromFile();
        splashScreenStage.close();
        onStart.run();
      } catch (IllegalArgumentException e) {
        myAlertField.flash(e.getMessage(), true);
        myAlertField.flash(MESSAGES.getString("LOAD_ERROR"), true);
      } catch (Exception e) {
        myAlertField.flash(MESSAGES.getString("LOAD_ERROR"), true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
      }
    });
    this.getChildren().add(myChooseFileButton);
  }

  public void show() {
    splashScreenStage.show();
  }

}
