package cellsociety.view;

import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.config.MainConfig.getMessages;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.config.SimulationConfig;
import cellsociety.controller.MainController;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.DoubleField;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A view to display and edit the parameters of a simulation
 *
 * @author Owen Jennings
 */
public class ParameterView extends VBox {

  private final MainController myMainController;
  private final boolean isEditing;
  private final Map<String, DoubleField> parameterFields = new HashMap<>();
  private final AlertField myAlertField;

  /**
   * Create a new parameter view using the current parameter values of the provided simulation
   *
   * @param mainController The main controller of the program
   * @param isEditing      Whether the user should be able to edit parameters or not
   */
  public ParameterView(MainController mainController, boolean isEditing) {
    this.setSpacing(ELEMENT_SPACING);
    myMainController = mainController;
    this.isEditing = isEditing;
    myAlertField = new AlertField();
    initializeView(mainController, isEditing);
  }

  private void initializeView(MainController mainController, boolean isEditing) {
    Simulation simulation = mainController.getSimulation();
    if (!simulation.rules().getParameters().isEmpty()) {
      this.getStyleClass().add("info-box");
      createTitle();
      createParameters(simulation);
      if (isEditing) {
        createUpdateButton();
      }
    }
    this.getChildren().add(myAlertField);
  }

  private void createTitle() {
    createText(getMessages().getString("PARAMETER_TITLE"), true);
  }

  private void createParameters(Simulation simulation) {
    Map<String, Parameter<?>> parameters = simulation.rules().getParameters();
    for (Entry<String, Parameter<?>> entry : parameters.entrySet()) {
      String key = entry.getKey();
      Parameter<?> param = entry.getValue();
      if (isEditing) {
        createEditableParameter(param, key);
      } else {
        createParameterBulletPoint(key, param);
      }
    }
  }

  private void createEditableParameter(Parameter<?> param, String key) {
    DoubleField field = new DoubleField();
    try {
      field.setText(param.getString());
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
    parameterFields.put(key, field);
    createParameterBulletPoint(key, param);
    this.getChildren().add(field);
  }

  private void createParameterBulletPoint(String key, Parameter<?> param) {
    try {
      createText(String.format("• %s: %s", key, param.getString()), false);
    } catch (InvalidParameterException e) {
      throw new RuntimeException(e);
    }
  }

  private void createUpdateButton() {
    Button updateButton = new Button(getMessages().getString("UPDATE_BUTTON"));
    updateButton.setOnAction(e -> updateParameters());
    this.getChildren().add(updateButton);
  }

  private void updateParameters() {
    Map<String, Parameter<?>> newParameters = new HashMap<>();
    setNewParametersMap(newParameters);
    attemptCreatingNewSimulation(newParameters);
  }

  private void attemptCreatingNewSimulation(Map<String, Parameter<?>> newParameters) {
    Simulation currentSimulation = myMainController.getSimulation();
    Simulation newSimulation;
    try {
      newSimulation = SimulationConfig.getNewSimulation(currentSimulation.data().type(),
          currentSimulation.data(), newParameters);
    } catch (InvocationTargetException e) {
      myAlertField.flash(e.getCause().getMessage(), true);
      return;
    } catch (ClassNotFoundException | NoSuchMethodException |
             InstantiationException | IllegalAccessException | InvalidParameterException e) {
      if (VERBOSE_ERROR_MESSAGES) {
        myAlertField.flash(e.getMessage(), true);
      }
      throw new RuntimeException(e);
    }
    myMainController.updateSimulation(newSimulation);
  }

  private void setNewParametersMap(Map<String, Parameter<?>> newParameters) {
    for (Entry<String, DoubleField> entry : parameterFields.entrySet()) {
      String key = entry.getKey();
      DoubleField field = entry.getValue();
      try {
        String newValue = field.getText();
        newParameters.put(key, new Parameter<>(newValue));
      } catch (NumberFormatException ignored) {
        System.out.println("Unable to add parameter: " + key);
      }
    }
  }

  private void createText(String entry, boolean title) {
    Text parameter = new Text(entry);
    if (title) {
      parameter.getStyleClass().add("secondary-title");
    }
    this.getChildren().add(parameter);
  }
}
