package cellsociety.view;

import java.util.HashMap;
import java.util.Map;

import static cellsociety.config.MainConfig.MAX_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MAX_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.MESSAGES;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.Main;
import cellsociety.config.MainConfig;
import cellsociety.config.SimulationConfig;
import cellsociety.controller.MainController;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.DoubleField;
import cellsociety.view.components.IntegerField;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A view to handle the setting of new coordinates for the simulation grid
 *
 * @author Owen Jennings
 */
public class EditModeView extends CreateDefaultSimView {

  private final MainController myMainController;
  private final AlertField myAlertField;
  private final VBox myHeaderBox = new VBox();
  private StateInfoView myStateInfoView;
  private final Map<String, DoubleField> myParameterTextFields = new HashMap<>();

  /**
   * Create a edit mode view
   *
   * @param mainController: the main controller of this view
   * @param alertField:     the alert field to display messages
   */
  public EditModeView(MainController mainController,
      AlertField alertField) {
    super(mainController);
    this.myMainController = mainController;
    this.myAlertField = alertField;
    initialize();
  }

  /**
   * Update the state info display for the edit more view
   */

  public void updateStateInfo() {
    myHeaderBox.getChildren()
        .removeFirst(); // remove the current state info box before creating a new one
    myStateInfoView = new StateInfoView(myMainController.getSimulation());
    myHeaderBox.getChildren()
        .addFirst(myStateInfoView); // add new current state info box to beginning of headerbox
  }

  private void initialize() {
    createHeader();
    createSimulationTypeControl();
    createRowControl();
    createColControl();
    createSimulationMetaDataTextFields();
    VBox parametersControlBox = new VBox();
    initializeParametersControl(parametersControlBox);
    createUpdateButton();
  }

  private void createHeader() {
    Text title = new Text(MESSAGES.getString("CREATE_NEW_GRID_HEADER"));
    title.getStyleClass().add("secondary-title");
    Text instructions = new Text(MESSAGES.getString("EDIT_VIEW_INSTRUCTIONS"));
    instructions.setWrappingWidth(SIDEBAR_WIDTH);
    myHeaderBox.setSpacing(ELEMENT_SPACING * 3);
    this.getChildren().add(myHeaderBox);
    myStateInfoView = new StateInfoView(myMainController.getSimulation());
    myHeaderBox.getChildren().addAll(myStateInfoView, instructions, title);
  }

  private void createUpdateButton() {
    Button updateButton = new Button(MESSAGES.getString("CREATE_NEW_GRID_HEADER"));
    updateButton.setOnAction(event -> {
      if (runValidationTests()) return;

      createNewSimulation();
      resetFields();
      updateStateInfo();

    });
    this.getChildren().add(updateButton);
  }


  protected void flashErrorMessage(String message) {
    if (myAlertField != null) {
      myAlertField.flash(message, true);
    }
  }


}
