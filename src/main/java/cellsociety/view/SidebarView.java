package cellsociety.view;

import static cellsociety.config.MainConfig.MESSAGES;

import cellsociety.controller.MainController;
import cellsociety.view.components.AlertField;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A class to handle all the sidebar view elements of the app
 *
 * @author Owen Jennings
 */
public class SidebarView extends VBox {

  public static final double ELEMENT_SPACING = 5;

  private final MainController myMainController;
  private boolean isEditing = false;
  private Button myModeButton;
  private ComboBox<String> languageDropdown;
  private Text changeLanguageText;
  private AlertField myAlertField;
  private final EditModeView myEditModeView;
  private final ViewModeView myViewModeView;


  /**
   * Create a sidebar view with a preferred size of width x height
   *
   * @param width:  preferred width of sidebar box
   * @param height: preferred height of sidebar box
   */
  public SidebarView(int width, int height, MainController controller) {
    this.setPrefSize(width, height);
    this.setAlignment(Pos.TOP_LEFT);
    this.setSpacing(ELEMENT_SPACING);
    myMainController = controller;
    initializeAlertField();
    createChangeModeButton();
    createLanguageDropdown();
    myViewModeView = new ViewModeView(myMainController, myAlertField);
    myEditModeView = new EditModeView(myMainController, myAlertField);
    this.getChildren().addAll(myModeButton, myViewModeView, myAlertField, changeLanguageText, languageDropdown);
  }

  public void update() {
    myViewModeView.update();
  }

  private void initializeAlertField() {
    myAlertField = new AlertField();
    VBox.setVgrow(myAlertField, Priority.ALWAYS);
    myAlertField.setAlignment(Pos.BOTTOM_LEFT);
  }


  private void disableEditView() {
    this.getChildren().clear();
    myViewModeView.update();
    this.getChildren().addAll(myModeButton, myViewModeView, myAlertField, changeLanguageText, languageDropdown);
  }


  private void enableEditView() {
    myEditModeView.updateStateInfo();
    this.getChildren().clear();
    this.getChildren().addAll(myModeButton, myEditModeView, myAlertField, changeLanguageText, languageDropdown);
  }

  private void createChangeModeButton() {
    myModeButton = new Button(MESSAGES.getString("EDIT_MODE"));
    myModeButton.setOnMouseClicked(event -> {
      isEditing = !isEditing;
      if (isEditing) {
        enableEditView();
        myMainController.stopAnimation();
        myModeButton.setText(MESSAGES.getString("VIEW_MODE"));
        myMainController.setEditing(true);
        myAlertField.flash(MESSAGES.getString("EDIT_MODE_ENABLED"), false);
      } else {
        disableEditView();
        myModeButton.setText(MESSAGES.getString("EDIT_MODE"));
        myMainController.setEditing(false);
        myAlertField.flash(MESSAGES.getString("EDIT_MODE_DISABLED"), false);
      }
    });
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

}
