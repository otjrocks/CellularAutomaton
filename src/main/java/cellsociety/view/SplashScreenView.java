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
import cellsociety.model.xml.GridException;
import cellsociety.model.xml.InvalidStateException;
import cellsociety.utility.FileUtility;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.SelectorField;
import java.io.IOException;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * A view that handles the splash screen that is shown when the program first loads
 *
 * @author Owen Jennings
 * @author Justin Aronwald
 */
public class SplashScreenView extends VBox {

  private final AlertField myAlertField;
  private SelectorField myLanguageField;
  private CreateDefaultSimView myCreateDefaultSimView;
  private final MainController myMainController;
  private final VBox myContentBox;
  private final SidebarView mySidebarView;

  /**
   * Create a splash screen  view
   *
   * @param mainController the main controller of this view
   * @param alertField     the alert field to display messages
   * @param sidebar        an instance of the sidebar view that holds a lot of simulation metadata
   */
  public SplashScreenView(AlertField alertField, SidebarView sidebar,
      MainController mainController) {
    super();
    this.myAlertField = alertField;
    myAlertField.setId("splashAlertField");
    this.mySidebarView = sidebar;
    this.myMainController = mainController;
    this.myContentBox = new VBox();
    initialize();
  }

  private void initialize() {
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
    myCreateDefaultSimView.setMaxWidth((double) WIDTH / 2);
  }

  private void initializeSplashScreen() {
    this.myCreateDefaultSimView = new CreateDefaultSimView(myMainController, myAlertField) {
      @Override
      public void handleAdditionalButtonActions() {
        myMainController.hideSplashScreen();
      }
    };
    Text title = new Text(getMessage("SPLASH_HEADER"));
    title.setId("splashScreenTitle");
    title.getStyleClass().add("main-title");
    Text description = new Text(getMessage("SPLASH_DESCRIPTION"));
    description.setId("splashDescription");
    description.getStyleClass().add("secondary-title");
    Text instructions = new Text(getMessage("SPLASH_INSTRUCTIONS"));
    instructions.setId("splashInstructions");
    SelectorField myThemeSelector = mySidebarView.createThemeSelector();
    myThemeSelector.setMaxWidth((double) WIDTH / 2);
    myThemeSelector.setAlignment(Pos.CENTER);
    myContentBox.getChildren().clear();
    myContentBox.getChildren()
        .addAll(title, description, instructions, myCreateDefaultSimView, myThemeSelector);

    createLanguageDropdown();
    createFileChooserButton();
    myContentBox.getChildren().add(myAlertField); // add alert field to end of view
  }

  private void createLanguageDropdown() {
    List<String> languages = MainConfig.fetchLanguages();

    if (languages.isEmpty()) {
      myAlertField.flash(getMessage("NO_LANGUAGES_FOUND"), false);
    }

    String defaultLanguage = languages.contains("English") ? "English" : languages.getFirst();
    myLanguageField = new SelectorField(languages,
        PreferencesController.getPreference("language", defaultLanguage),
        "languageDropdown", getMessage("CHANGE_LANGUAGE"), e -> handleLanguageDropdownAction());
    myLanguageField.setAlignment(Pos.CENTER);
    myContentBox.getChildren().addAll(myLanguageField);
  }

  private void handleLanguageDropdownAction() {
    MainConfig.setLanguage(myLanguageField.getValue());
    this.getChildren().clear();
    initialize();
  }

  private void createFileChooserButton() {
    Button myChooseFileButton = new Button(getMessage("CHOOSE_FILE_BUTTON"));
    Text chooseFileText = new Text(getMessage("LOAD_BUTTON_TEXT"));
    myChooseFileButton.setId("splashChooseFileButton");

    myChooseFileButton.setOnAction(e -> handleChooseFileAction());
    myContentBox.getChildren().addAll(chooseFileText, myChooseFileButton);
  }

  private void handleChooseFileAction() {
    try {
      myMainController.handleNewSimulationFromFile();
    } catch (IllegalArgumentException | GridException | InvalidStateException | IOException |
             ParserConfigurationException | SAXException e) {
      myAlertField.flash(e.getMessage(), true);
      myAlertField.flash(getMessage("LOAD_ERROR"), true);
      return;
    }
    myMainController.hideSplashScreen();
  }
}
