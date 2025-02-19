package cellsociety.view;

import java.util.HashMap;
import java.util.Map;

import static cellsociety.config.MainConfig.MAX_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MAX_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.getMessage;
import cellsociety.config.SimulationConfig;
import cellsociety.controller.MainController;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationMetaData;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.IntegerField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CreateDefaultSimView extends VBox {

  private final MainController mainController;
  private ComboBox<String> simulationSelector;
  private final Map<String, TextField> myParameterTextFields = new HashMap<>();
  private final VBox parametersControlBox = new VBox();
  private TextField myNameField;
  private TextField myAuthorField;
  private TextField myDescriptionField;
  private int myNumRows;
  private int myNumCols;
  private IntegerField rowField;
  private IntegerField colField;
  private final AlertField myAlertField;


  private static final int DEFAULT_NUM_CELLS = 25;

  public CreateDefaultSimView(MainController mainController, AlertField alertField) {
    this.mainController = mainController;
    this.setSpacing(ELEMENT_SPACING);
    this.setAlignment(Pos.CENTER_LEFT);
    this.myNumRows = DEFAULT_NUM_CELLS;
    this.myNumCols = DEFAULT_NUM_CELLS;
    this.myAlertField = alertField;
    initializeForm();
  }

  private void initializeForm() {
    createSimulationTypeControl();
    createRowControl();
    createColControl();
    createSimulationMetaDataTextFields();
    initializeParametersControl();
    createNewSimulationButton();
  }

  /**
   * Handles the simulation selection process
   */
  private void createSimulationTypeControl() {
    Text createSimButtonText = new Text(getMessage("NEW_SIM_BUTTON_TEXT"));
    ObservableList<String> options =
        FXCollections.observableArrayList(SimulationConfig.SIMULATIONS);
    simulationSelector = new ComboBox<>(options);
    simulationSelector.setValue(options.getFirst());
    simulationSelector.valueProperty()
        .addListener((ov, t, t1) -> {
          addAllParameters(simulationSelector.getValue());
        });
    HBox container = new HBox();
    container.setAlignment(Pos.CENTER_LEFT);
    container.setSpacing(5);
    Text simulationTypeLabel = new Text(getMessage("SIMULATION_TYPE_LABEL"));
    container.getChildren().addAll(simulationTypeLabel, simulationSelector);
    this.getChildren().addAll(createSimButtonText, container);
  }

  private void addAllParameters(String simulationName) {
    parametersControlBox.getChildren().clear();
    myParameterTextFields.clear();
    if (!SimulationConfig.getParameters(simulationName).isEmpty()) {
      Text parametersTitle = new Text(getMessage("CUSTOMIZE_PARAMETERS_TITLE"));
      parametersTitle.getStyleClass().add("secondary-title");
      parametersControlBox.getChildren().add(parametersTitle);
    }
    for (String parameter : SimulationConfig.getParameters(simulationName)) {
      TextField newParameterField = createTextField(parameter, parametersControlBox);
      myParameterTextFields.put(parameter, newParameterField);
    }
  }

  private TextField createTextField(String label, VBox target) {
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    TextField textField = new TextField();
    textField.setText("0");
    Text textFieldLabel = new Text(label);
    box.getChildren().addAll(textFieldLabel, textField);
    target.getChildren().add(box);
    return textField;
  }

  /**
   * Handles the row input for the simulation
   */
  private void createRowControl() {
    rowField = new IntegerField();
    rowField.setText(String.valueOf(DEFAULT_NUM_CELLS));
    rowField.textProperty()
        .addListener((obs, oldVal, newVal) -> myNumRows = parseIntegerField(rowField, 0));

    HBox rowBox = new HBox(new Text(getMessage("NUMBER_ROWS")), rowField);
    rowBox.setAlignment(Pos.CENTER_LEFT);
    rowBox.setSpacing(5);
    this.getChildren().add(rowBox);
  }

  /**
   * Handles the col input for the simulation
   */
  private void createColControl() {
    colField = new IntegerField();
    colField.setText(Integer.toString(DEFAULT_NUM_CELLS));
    colField.textProperty()
        .addListener((obs, oldVal, newVal) -> myNumCols = parseIntegerField(colField, 0));

    HBox colBox = new HBox(new Text(getMessage("NUMBER_COLUMNS")), colField);
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
  private void createSimulationMetaDataTextFields() {
    myNameField = createTextField(getMessage("NAME_LABEL"),
        getMessage("DEFAULT_NAME"), this);
    myAuthorField = createTextField(getMessage("AUTHOR_LABEL"),
        getMessage("DEFAULT_AUTHOR"), this);
    myDescriptionField = createTextField(getMessage("DESCRIPTION_LABEL"),
        getMessage("DEFAULT_DESCRIPTION"), this);
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
   */
  private void initializeParametersControl() {
    parametersControlBox.setAlignment(Pos.CENTER_LEFT);
    parametersControlBox.setSpacing(5);
    addAllParameters(simulationSelector.getValue());
    this.getChildren().add(parametersControlBox);
  }


  SimulationMetaData createMetaData() {
    return new SimulationMetaData(
        simulationSelector.getValue(),
        myNameField.getText(),
        myAuthorField.getText(),
        myDescriptionField.getText());
  }

  /**
   * @return - the number of grid rows from the input
   */
  private int getRowCount() {
    return parseIntegerField(rowField, DEFAULT_NUM_CELLS);
  }

  /**
   * @return - the number of grid cols from the input
   */
  private int getColCount() {
    return parseIntegerField(colField, DEFAULT_NUM_CELLS);
  }

  /**
   * @return - the simulation selected
   */
  private String getSelectedSimulation() {
    return simulationSelector.getValue();
  }

  /**
   * @return - whether the input fields were valid inputs
   */
  private boolean checkHasInvalidInput() {
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
      myAlertField.flash(getMessage("EMPTY_FIELD"), true);
      return true;
    }
    return false;
  }

  private boolean validateRows(int numRows) {
    boolean valid = numRows >= MIN_GRID_NUM_ROWS && numRows <= MAX_GRID_NUM_ROWS;
    if (!valid) {
      myAlertField.flash(String.format(
          getMessage("INVALID_ROWS"), MIN_GRID_NUM_ROWS, MAX_GRID_NUM_ROWS), true);
      return false;
    }
    return true;
  }

  private boolean validateCols(int numCols) {
    boolean valid = numCols >= MIN_GRID_NUM_COLS && numCols <= MAX_GRID_NUM_COLS;
    if (!valid) {
      myAlertField.flash(String.format(
          getMessage("INVALID_COLS"), MIN_GRID_NUM_COLS, MAX_GRID_NUM_COLS), true);
      return false;
    }
    return true;
  }

  /**
   * Begin the process for creating a simulation by making and validating the parameters then
   * creating solution
   */
  private void createNewSimulation() throws IllegalArgumentException {
    SimulationMetaData metaData = createMetaData();

    Map<String, Parameter<?>> parameters = new HashMap<>();
    for (String parameter : myParameterTextFields.keySet()) {
      parameters.put(parameter,
          new Parameter<Object>(myParameterTextFields.get(parameter).getText()));
    }
    attemptCreatingNewSimulation(metaData, parameters);
  }

  /**
   * @param metaData   - the metadata of the Simulation attempting to be created
   * @param parameters - the parameters of the Simulation
   */
  private void attemptCreatingNewSimulation(SimulationMetaData metaData,
      Map<String, Parameter<?>> parameters) {
    try {
      mainController.createNewSimulation(getRowCount(), getColCount(), getSelectedSimulation(),
          metaData, parameters);
      myAlertField.flash(String.format(getMessage("NEW_SIMULATION_CREATED")), false);
    } catch (Exception e) {
      myAlertField.flash(String.format(getMessage("ERROR_CREATING_SIMULATION")), true);
      myAlertField.flash(String.format((e.getMessage())), true);
      throw e;
    }
  }

  private void createNewSimulationButton() {
    Button createSimButton = new Button(getMessage("CREATE_NEW_GRID_HEADER"));

    createSimButton.setOnAction(event -> {
      if (checkHasInvalidInput()) {
        return;
      }
      try {
        createNewSimulation();
      } catch (Exception e) {
        return;
      }
      handleAdditionalButtonActions();
    });
    this.getChildren().add(createSimButton);
  }


  /**
   * Handle any addition button actions that you want to occur when the button is clicked and the
   * simulation is created successfully
   */
  protected void handleAdditionalButtonActions() throws IllegalArgumentException {
  }

}


