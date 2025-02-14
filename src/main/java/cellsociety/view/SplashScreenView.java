package cellsociety.view;

import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.config.MainConfig.getMessages;

import cellsociety.config.MainConfig;
import cellsociety.controller.MainController;
import cellsociety.view.components.AlertField;
import cellsociety.view.config.ThemeConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SplashScreenView extends VBox {

  private final AlertField myAlertField;
  private ComboBox<String> languageDropdown;
  private final MainController mainController;
  private HBox myThemeSelectorBox;
  private final CreateDefaultSimView createDefaultSimView;


  public SplashScreenView(AlertField myAlertField, MainController mainController) {
    this.myAlertField = myAlertField;
    this.mainController = mainController;
    this.createDefaultSimView = new CreateDefaultSimView(mainController, myAlertField) {
      @Override
      public void handleAdditionalButtonActions() {
        mainController.hideSplashScreen();
      }
    };

    initializeSplashScreen();
  }

  private void initializeSplashScreen() {

    Text title = new Text(getMessages().getString("SPLASH_HEADER"));
    Text description = new Text(getMessages().getString("SPLASH_DESCRIPTION"));
    Text instructions = new Text(getMessages().getString("SPLASH_INSTRUCTIONS"));

    createThemeSelector();

    this.getChildren()
        .addAll(title, description, instructions, createDefaultSimView, myThemeSelectorBox,
            myAlertField);

    createLanguageDropdown();
    createFileChooserButton();

  }

  private void createThemeSelector() {
    ObservableList<String> options =
        FXCollections.observableArrayList(ThemeConfig.THEMES);
    ComboBox<String> myThemeSelector = new ComboBox<>(options);
    myThemeSelector.setValue(options.getFirst());
    myThemeSelector.valueProperty()
        .addListener((ov, t, t1) -> {
          mainController.setTheme(myThemeSelector.getValue());
        });
    myThemeSelectorBox = new HBox();
    myThemeSelectorBox.setAlignment(Pos.CENTER_LEFT);
    myThemeSelectorBox.setSpacing(5);
    Text simulationTypeLabel = new Text(getMessages().getString("CHANGE_THEME"));
    myThemeSelectorBox.getChildren().addAll(simulationTypeLabel, myThemeSelector);
  }

  private void createLanguageDropdown() {
    languageDropdown = new ComboBox<>();
    Text changeLanguageText = new Text(getMessages().getString("CHANGE_LANGUAGE"));

    String propertiesFolderPath = "src/main/resources/cellsociety/languages/";
    List<String> languages = fetchLanguages(propertiesFolderPath);

    if (languages.isEmpty()) {
      myAlertField.flash(getMessages().getString("NO_LANGUAGES_FOUND"), false);
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
      MainConfig.setLanguage(language);
    });

    this.getChildren().addAll(changeLanguageText, languageDropdown);
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
    Button myChooseFileButton = new Button(getMessages().getString("CHOOSE_FILE_BUTTON"));
    Text chooseFileText = new Text(getMessages().getString("LOAD_BUTTON_TEXT"));
    myChooseFileButton.setOnAction(event -> {
      try {
        mainController.handleNewSimulationFromFile();
        mainController.hideSplashScreen();
      } catch (IllegalArgumentException e) {
        myAlertField.flash(e.getMessage(), true);
        myAlertField.flash(getMessages().getString("LOAD_ERROR"), true);
      } catch (Exception e) {
        myAlertField.flash(getMessages().getString("LOAD_ERROR"), true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
      }
    });
    this.getChildren().addAll(chooseFileText, myChooseFileButton);
  }


}
