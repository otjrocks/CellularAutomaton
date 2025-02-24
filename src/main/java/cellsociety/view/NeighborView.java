package cellsociety.view;

import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.config.MainConfig.getMessage;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;
import static cellsociety.view.config.NeighborConfig.getAvailableNeighborTypes;

import cellsociety.config.SimulationConfig;
import cellsociety.controller.MainController;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.IntegerField;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NeighborView extends VBox {
  private final MainController mainController;
  private boolean isEditing;
  private final AlertField myAlertField;
  private IntegerField neighborLayerField;
  private int neighborLayer;
  private String neighborType;
  private ComboBox<String> neighborTypeSelector;

  public NeighborView(MainController mainController, boolean isEditing) {
    this.setSpacing(ELEMENT_SPACING);
    this.mainController = mainController;
    this.isEditing = isEditing;
    this.myAlertField = new AlertField();

    initializeView(mainController, isEditing);
  }

  private void initializeView(MainController mainController, boolean isEditing) {
    Simulation simulation = mainController.getSimulation();
    this.neighborLayer = simulation.data().layers();
    this.neighborType = simulation.data().neighborType();
    this.neighborLayerField = new IntegerField();

    createNeighborBox(isEditing);
  }

  private void createNeighborBox(boolean isEditing) {
    this.getStyleClass().add("info-box");
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
    if (neighborType.equals(neighborTypeSelector.getValue()) &&
        neighborLayer == Integer.parseInt(neighborLayerField.getText())) {
      return;
    }

    this.neighborType = neighborTypeSelector.getValue();
    try {
      this.neighborLayer = Integer.parseInt(neighborLayerField.getText());
    } catch (NumberFormatException e) {
      myAlertField.flash(getMessage("INVALID_NEIGHBOR_LAYER"), true);
      return;
    }


    Simulation currentSimulation = mainController.getSimulation();
    Simulation newSimulation = getNewSimulation(currentSimulation);

    if (newSimulation == null) {
      return;
    }
    mainController.updateSimulation(newSimulation);
  }


  private Simulation getNewSimulation(Simulation currentSimulation) {
    Simulation newSimulation;
    SimulationMetaData newMetaData = new SimulationMetaData(
        currentSimulation.data().type(),
        currentSimulation.data().name(),
        currentSimulation.data().author(),
        currentSimulation.data().description(),
        neighborType,
        neighborLayer);

    try {
      newSimulation = SimulationConfig.getNewSimulation(currentSimulation.data().type(),
          newMetaData, currentSimulation.rules().getParameters());
    } catch (InvocationTargetException e) {
      myAlertField.flash(e.getCause().getMessage(), true);
      return null;
    } catch (ClassNotFoundException | NoSuchMethodException |
             InstantiationException | IllegalAccessException | InvalidParameterException e) {
      if (VERBOSE_ERROR_MESSAGES) {
        myAlertField.flash(e.getMessage(), true);
      }
      throw new RuntimeException(e);
    }
    return newSimulation;
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
    neighborLayerField.setText(Integer.toString(neighborLayer));
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
