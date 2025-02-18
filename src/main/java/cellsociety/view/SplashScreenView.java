package cellsociety.view;

import static cellsociety.config.MainConfig.HEIGHT;
import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.config.MainConfig.WIDTH;
import static cellsociety.config.MainConfig.getMessage;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.config.MainConfig;
import cellsociety.controller.MainController;
import cellsociety.controller.PreferencesController;
import cellsociety.view.components.AlertField;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SplashScreenView extends VBox {

  private static final String LANGUAGES_PATH = "src/main/resources/cellsociety/languages/";

  private final AlertField myAlertField;
  private ComboBox<String> languageDropdown;
  private final MainController myMainController;
  private final CreateDefaultSimView createDefaultSimView;
  private final VBox myContentBox;
  private final SidebarView mySidebarView;

  public SplashScreenView(AlertField alertField, SidebarView sidebar,
      MainController mainController) {
    this.myAlertField = alertField;
    this.mySidebarView = sidebar;
    this.myMainController = mainController;
    this.createDefaultSimView = new CreateDefaultSimView(mainController, myAlertField) {
      @Override
      public void handleAdditionalButtonActions() {
        mainController.hideSplashScreen();
      }
    };
    this.myContentBox = new VBox();
    initializeSplashScreen();
    handleBoxSizingAndAlignment();
  }

  private void handleBoxSizingAndAlignment() {
    myContentBox.setSpacing(ELEMENT_SPACING);
    myContentBox.setAlignment(Pos.CENTER);
    myContentBox.setPrefWidth((double) WIDTH / 2);
    myAlertField.setAlignment(Pos.CENTER);
    this.getChildren().add(myContentBox);
    this.setPrefWidth(WIDTH);
    this.setPrefHeight(HEIGHT - (MARGIN * 4));
    this.setAlignment(Pos.CENTER);
    createDefaultSimView.setMaxWidth((double) WIDTH / 2);
  }

  private void initializeSplashScreen() {
    Text title = new Text(getMessage("SPLASH_HEADER"));
    title.getStyleClass().add("main-title");
    Text description = new Text(getMessage("SPLASH_DESCRIPTION"));
    description.getStyleClass().add("secondary-title");
    Text instructions = new Text(getMessage("SPLASH_INSTRUCTIONS"));
    HBox myThemeSelectorBox = mySidebarView.createThemeSelector();
    myThemeSelectorBox.setMaxWidth((double) WIDTH / 2);
    myContentBox.getChildren()
        .addAll(title, description, instructions, createDefaultSimView, myThemeSelectorBox);

    createLanguageDropdown();
    createFileChooserButton();
    myContentBox.getChildren().add(myAlertField); // add alert field to end of view
  }

  private void createLanguageDropdown() {
    languageDropdown = new ComboBox<>();
    Text changeLanguageText = new Text(getMessage("CHANGE_LANGUAGE"));

    List<String> languages = fetchLanguages();

    if (languages.isEmpty()) {
      myAlertField.flash(getMessage("NO_LANGUAGES_FOUND"), false);
    }

    languageDropdown.getItems().addAll(languages);
    String defaultLanguage = languages.contains("English") ? "English" : languages.getFirst();
    languageDropdown.setValue(
        PreferencesController.getPreference("language", defaultLanguage));
    languageDropdown.setOnAction(event -> MainConfig.setLanguage(languageDropdown.getValue()));

    myContentBox.getChildren().addAll(changeLanguageText, languageDropdown);
  }

  private List<String> fetchLanguages() {
    List<String> languages = new ArrayList<>();
    File directory = new File(LANGUAGES_PATH);
    File[] files = directory.listFiles((dir, name) -> name.endsWith(".properties"));

    if (files != null) {
      for (File file : files) {
        languages.add(file.getName().replace(".properties", ""));
      }
    }
    return languages;
  }

  private void createFileChooserButton() {
    Button myChooseFileButton = new Button(getMessage("CHOOSE_FILE_BUTTON"));
    Text chooseFileText = new Text(getMessage("LOAD_BUTTON_TEXT"));

    myChooseFileButton.setOnAction(event -> {
      try {
        myMainController.handleNewSimulationFromFile();
        myMainController.hideSplashScreen();
      } catch (IllegalArgumentException e) {
        myAlertField.flash(e.getMessage(), true);
        myAlertField.flash(getMessage("LOAD_ERROR"), true);
      } catch (Exception e) {
        myAlertField.flash(getMessage("LOAD_ERROR"), true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
      }
    });
    myContentBox.getChildren().addAll(chooseFileText, myChooseFileButton);
  }
}
