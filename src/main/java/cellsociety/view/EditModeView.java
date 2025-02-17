package cellsociety.view;

import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.getMessage;
import cellsociety.controller.MainController;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;
import cellsociety.view.components.AlertField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A view to handle the setting of new coordinates for the simulation grid
 *
 * @author Owen Jennings
 */
public class EditModeView extends VBox {

  private final MainController myMainController;
  private final AlertField myAlertField;
  private final VBox myHeaderBox = new VBox();
  private StateInfoView myStateInfoView;
  private ParameterView myParameterView;

  /**
   * Create a edit mode view
   *
   * @param mainController: the main controller of this view
   * @param alertField:     the alert field to display messages
   */
  public EditModeView(MainController mainController,
      AlertField alertField) {
    this.myMainController = mainController;
    this.myAlertField = alertField;
    createHeader();
    CreateDefaultSimView createDefaultSimView = new CreateDefaultSimView(mainController,
        myAlertField) {
      @Override
      protected void handleAdditionalButtonActions() throws IllegalArgumentException {
        super.handleAdditionalButtonActions();
        updateDisplay(); // update state info when new simulation is created.
      }
    };
    this.getChildren().addAll(createDefaultSimView);
  }

  /**
   * Update the edit display whenever the simulation potential has changed
   */
  public void updateDisplay() {
    myHeaderBox.getChildren().remove(myStateInfoView);
    myHeaderBox.getChildren().remove(myParameterView);
    myStateInfoView = new StateInfoView(myMainController.getSimulation());
    myParameterView = new ParameterView(myMainController, true);
    myHeaderBox.getChildren().addFirst(myParameterView);
    myHeaderBox.getChildren().addFirst(myStateInfoView);
  }

  private void createHeader() {
    Text title = new Text(getMessage("CREATE_NEW_GRID_HEADER"));
    title.getStyleClass().add("secondary-title");
    Text instructions = new Text(getMessage("EDIT_VIEW_INSTRUCTIONS"));
    instructions.setWrappingWidth(SIDEBAR_WIDTH);
    myHeaderBox.setSpacing(ELEMENT_SPACING * 3);
    this.getChildren().add(myHeaderBox);
    myStateInfoView = new StateInfoView(myMainController.getSimulation());
    myParameterView = new ParameterView(myMainController, true);
    myHeaderBox.getChildren().addAll(myStateInfoView, myParameterView, instructions, title);
  }
}
