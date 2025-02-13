package cellsociety.view;

import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.getMessages;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.controller.MainController;
import cellsociety.controller.ViewController;
import cellsociety.view.components.AlertField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A view to handle the setting of new coordinates for the simulation grid
 *
 * @author Owen Jennings
 */
public class EditModeView extends CreateDefaultSimView {

  private final ViewController myViewController;
  private final AlertField myAlertField;
  private final VBox myHeaderBox = new VBox();
  private StateInfoView myStateInfoView;

  /**
   * Create a edit mode view
   *
   * @param viewController: the main controller of this view
   * @param alertField:     the alert field to display messages
   */
  public EditModeView(ViewController viewController,
      AlertField alertField) {
    super(viewController);
    this.myViewController = viewController;
    this.myAlertField = alertField;
    initialize();
  }

  /**
   * Update the state info display for the edit more view
   */

  public void updateStateInfo() {
    myHeaderBox.getChildren()
        .removeFirst(); // remove the current state info box before creating a new one
    myStateInfoView = new StateInfoView(myViewController.getSimulation());
    myHeaderBox.getChildren()
        .addFirst(myStateInfoView); // add new current state info box to beginning of headerbox
  }

  private void initialize() {
    createHeader();
    createSimulationTypeControl();
    createRowControl();
    createColControl();
    createSimulationMetaDataTextFields();
    initializeParametersControl();
    createUpdateButton();
  }

  private void createHeader() {
    Text title = new Text(getMessages().getString("CREATE_NEW_GRID_HEADER"));
    title.getStyleClass().add("secondary-title");
    Text instructions = new Text(getMessages().getString("EDIT_VIEW_INSTRUCTIONS"));
    instructions.setWrappingWidth(SIDEBAR_WIDTH);
    myHeaderBox.setSpacing(ELEMENT_SPACING * 3);
    this.getChildren().add(myHeaderBox);
    myStateInfoView = new StateInfoView(myViewController.getSimulation());
    myHeaderBox.getChildren().addAll(myStateInfoView, instructions, title);
  }

  private void createUpdateButton() {
    Button updateButton = new Button(getMessages().getString("CREATE_NEW_GRID_HEADER"));
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
