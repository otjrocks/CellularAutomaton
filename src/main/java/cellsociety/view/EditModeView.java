package cellsociety.view;

import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.getMessage;

import cellsociety.controller.MainController;

import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.view.components.AlertField;
import cellsociety.view.components.SelectorField;
import cellsociety.view.grid.GridViewFactory.CellShapeType;
import java.util.ArrayList;
import java.util.List;
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
  private NeighborView myNeighborView;
  private SelectorField myShapeSelector;

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
    myHeaderBox.getChildren().clear();
    this.getChildren().remove(myHeaderBox);
    createHeader();
  }

  private void createHeader() {
    Text title = new Text(getMessage("CREATE_NEW_GRID_HEADER"));
    title.getStyleClass().add("secondary-title");
    Text instructions = new Text(getMessage("EDIT_VIEW_INSTRUCTIONS"));
    instructions.setWrappingWidth(SIDEBAR_WIDTH);
    myHeaderBox.setSpacing(ELEMENT_SPACING * 3);
    createHeaderElements();
    myHeaderBox.getChildren()
        .addAll(myStateInfoView, myParameterView, myNeighborView, myShapeSelector, instructions,
            title);
    this.getChildren().addFirst(myHeaderBox);
  }

  private void createHeaderElements() {
    myStateInfoView = new StateInfoView(myMainController.getSimulation());
    myParameterView = new ParameterView(myMainController, true);
    myNeighborView = new NeighborView(myMainController, true);
    myShapeSelector = createShapeSelector();
  }

  private SelectorField createShapeSelector() {
    List<CellShapeType> cellShapes = List.of(CellShapeType.values());
    List<String> displayNames = new ArrayList<>();
    for (CellShapeType cellShape : cellShapes) {
      displayNames.add(cellShape.toString());
    }
    return new SelectorField(displayNames, displayNames.getFirst(),
        "editModeShapeSelector",
        getMessage("SHAPE_SELECTOR"), _ -> updateGridShape());
  }

  private void updateGridShape() {
    String selectedValue = myShapeSelector.getValue();
    myMainController.updateGridShape(CellShapeType.valueOf(selectedValue.toUpperCase()));
  }
}
