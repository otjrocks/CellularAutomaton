package cellsociety.view;

import static cellsociety.config.MainConfig.MAX_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MAX_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.MESSAGES;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.config.SimulationConfig;
import cellsociety.controller.MainController;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.components.DoubleField;
import cellsociety.view.components.IntegerField;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public abstract class CreateDefaultSimView extends VBox {

  private final MainController mainController;
  private ComboBox<String> simulationSelector;
  private final Map<String, DoubleField> myParameterTextFields = new HashMap<>();
  private final VBox parametersControlBox = new VBox();
  private TextField myNameField;
  private TextField myAuthorField;
  private TextField myDescriptionField;
  private int myNumRows;
  private int myNumCols;
  private IntegerField rowField;
  private IntegerField colField;


  private static final int DEFAULT_NUM_CELLS = 25;

  public CreateDefaultSimView(MainController mainController) {
    this.mainController = mainController;
    this.setSpacing(ELEMENT_SPACING * 2);
    this.setAlignment(Pos.CENTER_LEFT);
    this.myNumRows = mainController.getGridRows();
    this.myNumCols = mainController.getGridCols();
  }

  /**
   * Abstracted method for error showing
   * @param message - the error message
   */
  protected abstract void flashErrorMessage(String message);

  /**
   * Handles the simulation selection process
   */
  protected void createSimulationTypeControl() {
    Text createSimButtonText = new Text(MESSAGES.getString("NEW_SIM_BUTTON_TEXT"));

    ObservableList<String> options =
        FXCollections.observableArrayList(SimulationConfig.simulations);
    simulationSelector = new ComboBox<>(options);
    simulationSelector.setValue(options.getFirst());
    simulationSelector.valueProperty()
        .addListener((ov, t, t1) -> {
          addAllParameters(simulationSelector.getValue());
        });
    HBox container = new HBox();
    container.setAlignment(Pos.CENTER_LEFT);
    container.setSpacing(5);
    Text simulationTypeLabel = new Text(MESSAGES.getString("SIMULATION_TYPE_LABEL"));
    container.getChildren().addAll(simulationTypeLabel, simulationSelector);
    this.getChildren().addAll(createSimButtonText, container);
  }

  private void addAllParameters(String simulationName) {
    parametersControlBox.getChildren().clear();
    myParameterTextFields.clear();
    SimulationConfig.getParameters(simulationName);
    if (!SimulationConfig.getParameters(simulationName).isEmpty()) {
      Text parametersTitle = new Text(MESSAGES.getString("CUSTOMIZE_PARAMETERS_TITLE"));
      parametersTitle.getStyleClass().add("secondary-title");
      parametersControlBox.getChildren().add(parametersTitle);
    }
    for (String parameter : SimulationConfig.getParameters(simulationName)) {
      DoubleField newParameterField = createDoubleField(parameter, parametersControlBox);
      myParameterTextFields.put(parameter, newParameterField);
    }
  }

  private DoubleField createDoubleField(String label, VBox target) {
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    DoubleField doubleTextField = new DoubleField();
    doubleTextField.setText("0");
    Text textFieldLabel = new Text(label);
    box.getChildren().addAll(textFieldLabel, doubleTextField);
    target.getChildren().add(box);
    return doubleTextField;
  }

  /**
   * Handles the row input for the simulation
   */
  protected void createRowControl() {
    rowField = new IntegerField();
    rowField.setText(String.valueOf(DEFAULT_NUM_CELLS));
    rowField.textProperty()
        .addListener((obs, oldVal, newVal) -> myNumRows = parseIntegerField(rowField, 0));

    HBox rowBox = new HBox(new Text(MESSAGES.getString("NUMBER_ROWS")), rowField);
    rowBox.setAlignment(Pos.CENTER_LEFT);
    rowBox.setSpacing(5);
    this.getChildren().add(rowBox);
  }

  /**
   * Handles the col input for the simulation
   */
  protected void createColControl() {
    colField = new IntegerField();
    colField.setText(Integer.toString(DEFAULT_NUM_CELLS));
    colField.textProperty()
        .addListener((obs, oldVal, newVal) -> myNumCols = parseIntegerField(colField, 0));

    HBox colBox = new HBox(new Text(MESSAGES.getString("NUMBER_COLUMNS")), colField);
    colBox.setAlignment(Pos.CENTER_LEFT);
    colBox.setSpacing(5);
    this.getChildren().add(colBox);
  }

  private int parseIntegerField(IntegerField field, int defaultValue) {
    try {
      return Integer.parseInt(field.getText());
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * Handles the text metadata for the simulation
   */
  protected void createSimulationMetaDataTextFields() {
    myNameField = createTextField(MESSAGES.getString("NAME_LABEL"),
        MESSAGES.getString("DEFAULT_NAME"), this);
    myAuthorField = createTextField(MESSAGES.getString("AUTHOR_LABEL"),
        MESSAGES.getString("DEFAULT_AUTHOR"), this);
    myDescriptionField = createTextField(MESSAGES.getString("DESCRIPTION_LABEL"),
        MESSAGES.getString("DEFAULT_DESCRIPTION"), this);
  }

  private TextField createTextField(String label, String defaultValue, VBox target) {
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    TextField textField = new TextField();
    textField.setPromptText(defaultValue);
    Text textFieldLabel = new Text(label);
    box.getChildren().addAll(textFieldLabel, textField);
    target.getChildren().add(box);
    return textField;
  }

  /**
   * Handles the initialization of the different parameter inputs
   * @param parametersControlBox - the parameter inputs
   */
  protected void initializeParametersControl(VBox parametersControlBox) {
    parametersControlBox.setAlignment(Pos.CENTER_LEFT);
    parametersControlBox.setSpacing(5);
    addAllParameters(simulationSelector.getValue());
    this.getChildren().add(parametersControlBox);
  }

  /**
   *
   * @return - the validated and created parameters for the current simulation
   */
  protected Map<String, Double> createAndValidateParameters() {
    Map<String, Double> parameters = new HashMap<>();
    boolean validParameters = true;
    for (String parameter : myParameterTextFields.keySet()) {
      double value;
      try {
        value = Double.parseDouble(myParameterTextFields.get(parameter).getText());
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid parameter: " + parameter + " with value: " + myParameterTextFields.get(parameter).getText());      }
      parameters.put(parameter, value);
    }
    if (!validParameters) {
      return null;
    }
    return parameters;
  }

  SimulationMetaData createMetaData() {
    return new SimulationMetaData(
        simulationSelector.getValue(),
        myNameField.getText(),
        myAuthorField.getText(),
        myDescriptionField.getText());
  }

  /**
   *
   * @return - the number of grid rows from the input
   */
  protected int getRowCount() {
    return parseIntegerField(rowField, DEFAULT_NUM_CELLS);
  }

  /**
   *
   * @return - the number of grid cols from the input
   */
  protected int getColCount() {
    return parseIntegerField(colField, DEFAULT_NUM_CELLS);
  }

  /**
   *
   * @return - the simulation selected
   */
  protected String getSelectedSimulation() {
    return simulationSelector.getValue();
  }

  /**
   * Resets the fields back to nothing
   */
  protected void resetFields() {
    rowField.setText(Integer.toString(DEFAULT_NUM_CELLS));
    colField.setText(Integer.toString(DEFAULT_NUM_CELLS));
    myNameField.clear();
    myAuthorField.clear();
    myDescriptionField.clear();
  }

  /**
   *
   * @return - whether the input fields were valid inputs
   */
  protected boolean runValidationTests() {
    if (!validateRows(myNumRows) || !validateCols(myNumCols)) {
      return true;
    }
    return checkInvalidText(simulationSelector.getValue()) ||
        checkInvalidText(myNameField.getText()) ||
        checkInvalidText(myAuthorField.getText()) ||
        checkInvalidText(myDescriptionField.getText());
  }


  private boolean checkInvalidText(String text) {
    if (text.isEmpty()) {
      flashErrorMessage(MESSAGES.getString("EMPTY_FIELD"));
      return true;
    }
    return false;
  }

  private boolean validateRows(int numRows) {
    boolean valid = numRows >= MIN_GRID_NUM_ROWS && numRows <= MAX_GRID_NUM_ROWS;
    if (!valid) {
      flashErrorMessage(String.format(
          MESSAGES.getString("INVALID_ROWS"), MIN_GRID_NUM_ROWS, MAX_GRID_NUM_ROWS));
      return false;
    }
    return true;
  }

  private boolean validateCols(int numCols) {
    boolean valid = numCols >= MIN_GRID_NUM_COLS && numCols <= MAX_GRID_NUM_COLS;
    if (!valid) {
      flashErrorMessage(String.format(
          MESSAGES.getString("INVALID_COLS"), MIN_GRID_NUM_COLS, MAX_GRID_NUM_COLS));
      return false;
    }
    return true;
  }

  /**
   * Begin the process for creating a simulation by making and validating the parameters then creating solution
   */
  protected void createNewSimulation() {
    SimulationMetaData metaData = createMetaData();

    Map<String, Double> parameters = createAndValidateParameters();
    attemptCreatingNewSimulation(metaData, parameters);
  }

  /**
   *
   * @param metaData - the metadata of the Simulation attempting to be created
   * @param parameters - the parameters of the Simulation
   */
  protected void attemptCreatingNewSimulation(SimulationMetaData metaData,
      Map<String, Double> parameters) {
    try {
      mainController.createNewSimulation(getRowCount(), getColCount(), getSelectedSimulation(),
          metaData, parameters);
      flashErrorMessage(String.format(MESSAGES.getString("NEW_SIMULATION_CREATED")));
    } catch (IllegalArgumentException e) {
      flashErrorMessage(String.format(MESSAGES.getString("ERROR_CREATING_SIMULATION")));
      flashErrorMessage(String.format((e.getMessage())));
    } catch (Exception e) {
      flashErrorMessage(String.format(MESSAGES.getString("ERROR_CREATING_SIMULATION")));
      if (VERBOSE_ERROR_MESSAGES) {
        flashErrorMessage(String.format((e.getMessage())));
      }
    }
  }



}


