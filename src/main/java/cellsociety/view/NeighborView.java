package cellsociety.view;

import static cellsociety.config.MainConfig.getMessage;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;
import static cellsociety.view.config.NeighborConfig.getAvailableNeighborTypes;

import cellsociety.controller.MainController;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Simulation;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.IntegerField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NeighborView extends VBox {
  private boolean isEditing;
  private final AlertField myAlertField;
  private IntegerField neighborLayerField;
  private int neighborLayer;
  private String neighborType;
  private ComboBox<String> neighborTypeSelector;

  public NeighborView(MainController mainController, boolean isEditing, AlertField alertField) {
    this.setSpacing(ELEMENT_SPACING);
    this.isEditing = isEditing;
    this.myAlertField = alertField;

    initializeView(mainController, isEditing);
  }

  private void initializeView(MainController mainController, boolean isEditing) {
    Simulation simulation = mainController.getSimulation();
    this.neighborLayer = simulation.data().layers();
    this.neighborType = simulation.data().neighborType();
    this.neighborLayerField = new IntegerField();

    createNeighborBox(isEditing, simulation);
  }

  private void createNeighborBox(boolean isEditing, Simulation simulation) {
    createTitle();
    createNeighborDisplay();
    if (isEditing) {
      createUpdateButton();
    }
  }

  private void createUpdateButton() {
    Button updateButton = new Button(getMessage("UPDATE_BUTTON"));
    updateButton.setOnAction(e -> updateNeighborInformation());
    this.getChildren().add(updateButton);
  }

  private void updateNeighborInformation() {
    this.neighborType = neighborTypeSelector.getValue();
    this.neighborLayer = Integer.parseInt(neighborLayerField.getText());
  }


  private void createNeighborDisplay() {
    if (isEditing) {
      createEditableFields();
    } else {
      createBulletPoints();
    }
  }

  private void createEditableFields() {
    neighborTypeSelector = new ComboBox<>(getAvailableNeighborTypes());
    neighborTypeSelector.setValue(neighborType);

    neighborLayerField = new IntegerField();
    neighborLayerField.textProperty().addListener((_, _, newValue) -> {
      try {
        neighborLayer = Integer.parseInt(newValue);
      } catch (NumberFormatException e) {
        myAlertField.flash(getMessage("INVALID_NEIGHBOR_LAYER"), true);
      }
    });

    VBox editableBox = new VBox();
    editableBox.getChildren().addAll(
        new Text(getMessage("NEIGHBOR_TYPE_LABEL")), neighborTypeSelector,
        new Text(getMessage("NEIGHBOR_LAYER_LABEL")), neighborLayerField
    );

    this.getChildren().add(editableBox);
  }

  private void createBulletPoints() {
    createText(String.format("• %s: %s", getMessage("NEIGHBOR_TYPE_TITLE"), neighborType), false);
    createText(String.format("• %s %s", getMessage("NEIGHBOR_LAYER_LABEL"), neighborLayer), false);
  }


  private void createTitle() {
    createText(getMessage("NEIGHBOR_TITLE"), true);
  }

  private void createText(String entry, boolean title) {
    Text newText = new Text(entry);
    if (title) {
      newText.getStyleClass().add("secondary-title");
    }
    this.getChildren().add(newText);
  }


}
