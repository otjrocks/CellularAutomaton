package cellsociety.view;

import static cellsociety.config.MainConfig.getMessages;

import cellsociety.controller.MainController;
import cellsociety.view.components.AlertField;
import cellsociety.view.config.ThemeConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A class to handle all the sidebar view elements of the app
 *
 * @author Owen Jennings
 */
public class SidebarView extends VBox {

  public static final double ELEMENT_SPACING = 10;

  private final MainController myMainController;
  private boolean isEditing = false;
  private Button myModeButton;
  private HBox myThemeSelectorBox;
  private final HBox myGridLinesCheckboxField = new HBox();
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
    createThemeSelector();
    createShowGridLinesCheckbox();
    myViewModeView = new ViewModeView(myMainController, myAlertField);
    myEditModeView = new EditModeView(myMainController, myAlertField);
    addAllComponentsToSidebar();
  }

  private void addAllComponentsToSidebar() {
    this.getChildren()
        .addAll(myModeButton, myThemeSelectorBox, myGridLinesCheckboxField, myViewModeView,
            myAlertField);
  }

  private void addAllEditModeComponents() {
    this.getChildren()
        .addAll(myModeButton, myThemeSelectorBox, myGridLinesCheckboxField, myEditModeView,
            myAlertField);
  }

  /**
   * Create the theme selector element, which can be accessed across multiple views
   *
   * @return A HBox containing the theme selector and its label
   */
  public HBox createThemeSelector() {
    ObservableList<String> options =
        FXCollections.observableArrayList(ThemeConfig.THEMES);
    ComboBox<String> myThemeSelector = new ComboBox<>(options);
    myThemeSelector.setValue(ThemeConfig.getCurrentTheme());
    myThemeSelector.valueProperty()
        .addListener((ov, t, t1) -> {
          myMainController.setTheme(myThemeSelector.getValue());
          myThemeSelector.setValue(myThemeSelector.getValue());
        });
    myThemeSelectorBox = new HBox();
    myThemeSelectorBox.setAlignment(Pos.CENTER_LEFT);
    myThemeSelectorBox.setSpacing(5);
    Text simulationTypeLabel = new Text(getMessages().getString("CHANGE_THEME"));
    myThemeSelectorBox.getChildren().addAll(simulationTypeLabel, myThemeSelector);
    return myThemeSelectorBox;
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
    addAllComponentsToSidebar();
  }


  private void enableEditView() {
    myEditModeView.updateStateInfo();
    this.getChildren().clear();
    addAllEditModeComponents();
  }

  private void createChangeModeButton() {
    myModeButton = new Button(getMessages().getString("EDIT_MODE"));
    myModeButton.setOnMouseClicked(event -> {
      isEditing = !isEditing;
      if (isEditing) {
        enableEditView();
        myMainController.stopAnimation();
        myModeButton.setText(getMessages().getString("VIEW_MODE"));
        myMainController.setEditing(true);
        myAlertField.flash(getMessages().getString("EDIT_MODE_ENABLED"), false);
      } else {
        disableEditView();
        myModeButton.setText(getMessages().getString("EDIT_MODE"));
        myMainController.setEditing(false);
        myAlertField.flash(getMessages().getString("EDIT_MODE_DISABLED"), false);
      }
    });
  }

  private void createShowGridLinesCheckbox() {
    myGridLinesCheckboxField.setSpacing(ELEMENT_SPACING);
    CheckBox gridLinesCheckbox = new CheckBox();
    gridLinesCheckbox.setSelected(true);
    gridLinesCheckbox.setOnAction(
        event -> myMainController.setGridLines(gridLinesCheckbox.isSelected()));
    Text title = new Text(getMessages().getString("GRID_LINES_LABEL"));
    myGridLinesCheckboxField.getChildren().addAll(gridLinesCheckbox, title);
  }

  public void flashWarning(String message) {
    myAlertField.flash(message, true);
  }

}
