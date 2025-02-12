package cellsociety.view;

import static cellsociety.config.MainConfig.MESSAGES;

import cellsociety.controller.MainController;
import cellsociety.view.components.AlertField;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    createShowGridLinesCheckbox();
    myViewModeView = new ViewModeView(myMainController, myAlertField);
    myEditModeView = new EditModeView(myMainController, myAlertField);
    this.getChildren().addAll(myModeButton, myGridLinesCheckboxField, myViewModeView, myAlertField);
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
    this.getChildren().addAll(myModeButton, myGridLinesCheckboxField, myViewModeView, myAlertField);
  }


  private void enableEditView() {
    myEditModeView.updateStateInfo();
    this.getChildren().clear();
    this.getChildren().addAll(myModeButton, myGridLinesCheckboxField, myEditModeView, myAlertField);
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

  private void createShowGridLinesCheckbox() {
    myGridLinesCheckboxField.setSpacing(ELEMENT_SPACING);
    CheckBox gridLinesCheckbox = new CheckBox();
    gridLinesCheckbox.setSelected(true);
    gridLinesCheckbox.setOnMouseClicked(event -> {
      myMainController.setGridLines(gridLinesCheckbox.isSelected());
    });
    Text title = new Text(MESSAGES.getString("GRID_LINES_LABEL"));
    myGridLinesCheckboxField.getChildren().addAll(gridLinesCheckbox, title);
  }

  /**
   * removes all elements of the sidebar
   */
  public void clearSidebar() {
    this.getChildren().clear();
  }

}
