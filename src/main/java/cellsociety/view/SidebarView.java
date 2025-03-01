package cellsociety.view;

import static cellsociety.config.MainConfig.getMessage;

import cellsociety.controller.MainController;
import cellsociety.controller.PreferencesController;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.SelectorField;
import cellsociety.view.config.ThemeConfig;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
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
  private SelectorField myThemeSelector;
  private final HBox myGridLinesCheckboxField = new HBox();
  private AlertField myAlertField;
  private final EditModeView myEditModeView;
  private final ViewModeView myViewModeView;
  private final FlowPane myControlsBox = new FlowPane();


  /**
   * Create a sidebar view with a preferred size of width x height
   *
   * @param width:  preferred width of sidebar box
   * @param height: preferred height of sidebar box
   */
  public SidebarView(int width, int height, MainController controller) {
    super();
    this.setPrefSize(width, height);
    this.setAlignment(Pos.TOP_LEFT);
    this.setSpacing(ELEMENT_SPACING);
    this.getStyleClass().add("sidebar");
    myMainController = controller;
    initializeAlertField();
    createChangeModeButton();
    createThemeSelector();
    createShowGridLinesCheckboxField();
    myViewModeView = new ViewModeView(myMainController, myAlertField);
    myEditModeView = new EditModeView(myMainController, myAlertField);
    addControlsToBox();
    addAllComponentsToSidebar();
  }

  /**
   * Create the theme selector element, which can be accessed across multiple views
   *
   * @return A SelectorField containing the theme selector and its label
   */
  public SelectorField createThemeSelector() {
    myThemeSelector = new SelectorField(ThemeConfig.THEMES, ThemeConfig.getCurrentTheme(), "themeDropdown", getMessage("CHANGE_THEME"), e -> myMainController.setTheme(myThemeSelector.getValue()));
    myThemeSelector.setAlignment(Pos.CENTER_LEFT);
    this.getChildren().add(myThemeSelector);
    return myThemeSelector;
  }

  /**
   * Flash a warning on the sidebar view's alert field.
   *
   * @param message The message you want to display with the warning
   */
  public void flashWarning(String message) {
    myAlertField.flash(message, true);
  }

  private void addAllComponentsToSidebar() {
    initializeTitle();
    this.getChildren()
        .addAll(myControlsBox, myViewModeView, myAlertField);
  }

  private void addControlsToBox() {
    myControlsBox.getChildren().clear();
    myControlsBox.setAlignment(Pos.CENTER_LEFT);
    myControlsBox.setHgap(ELEMENT_SPACING);
    myControlsBox.setVgap(ELEMENT_SPACING);
    myControlsBox.getChildren().addAll(myModeButton, myThemeSelector, myGridLinesCheckboxField);
  }

  private void addAllEditModeComponents() {
    this.getChildren().clear();
    initializeTitle();
    this.getChildren()
        .addAll(myControlsBox, myEditModeView, myAlertField);
  }

  private void initializeTitle() {
    Text title = new Text(getMessage("TITLE"));
    title.getStyleClass().add("main-title");
    this.getChildren().add(title);
  }

  public void update() {
    myViewModeView.update();
    myEditModeView.updateDisplay();
  }

  private void initializeAlertField() {
    myAlertField = new AlertField();
    myAlertField.setId("sidebarAlertField");
    VBox.setVgrow(myAlertField, Priority.ALWAYS);
    myAlertField.setAlignment(Pos.BOTTOM_LEFT);
  }


  private void disableEditView() {
    this.getChildren().clear();
    myViewModeView.update();
    addAllComponentsToSidebar();
  }


  private void enableEditView() {
    myEditModeView.updateDisplay();
    addAllEditModeComponents();
  }

  private void createChangeModeButton() {
    myModeButton = new Button(getMessage("EDIT_MODE"));
    myModeButton.setId("sidebarModeButton");
    myModeButton.setOnMouseClicked(e -> handleChangeModeButtonClick());
  }

  private void handleChangeModeButtonClick() {
    isEditing = !isEditing;
    if (isEditing) {
      handleButtonActionEnableEditing();
    } else {
      handleButtonActionDisableEditing();
    }
  }

  private void handleButtonActionDisableEditing() {
    disableEditView();
    myModeButton.setText(getMessage("EDIT_MODE"));
    myMainController.setEditing(false);
    myAlertField.flash(getMessage("EDIT_MODE_DISABLED"), false);
  }

  private void handleButtonActionEnableEditing() {
    enableEditView();
    myMainController.stopAnimation();
    myModeButton.setText(getMessage("VIEW_MODE"));
    myMainController.setEditing(true);
    myAlertField.flash(getMessage("EDIT_MODE_ENABLED"), false);
  }

  private void createShowGridLinesCheckboxField() {
    myGridLinesCheckboxField.setSpacing(ELEMENT_SPACING);
    myGridLinesCheckboxField.setAlignment(Pos.CENTER_LEFT);
    CheckBox gridLinesCheckbox = createGridLinesCheckbox();
    Text title = new Text(getMessage("GRID_LINES_LABEL"));
    myGridLinesCheckboxField.getChildren().addAll(gridLinesCheckbox, title);
  }

  private CheckBox createGridLinesCheckbox() {
    CheckBox gridLinesCheckbox = new CheckBox();
    gridLinesCheckbox.setSelected(
        Boolean.parseBoolean(PreferencesController.getPreference("gridLines", "true")));
    gridLinesCheckbox.setOnAction(
        e -> myMainController.setGridLines(gridLinesCheckbox.isSelected()));
    return gridLinesCheckbox;
  }

}
