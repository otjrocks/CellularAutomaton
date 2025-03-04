package cellsociety.view;

import cellsociety.view.components.SelectorField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cellsociety.config.MainConfig.LOGGER;
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
import static cellsociety.view.config.NeighborConfig.getAvailableNeighborTypes;

import cellsociety.view.components.AlertField;
import cellsociety.view.components.IntegerField;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/**
 * A class handling the creation of the "create a new default" simulation form.
 *
 * @author Owen Jennings
 * @author Justin Aronwald
 */
public class CreateDefaultSimView extends VBox {

  private final MainController myMainController;
  private SelectorField mySimulationSelector;
  private final Map<String, TextField> myParameterTextFields = new HashMap<>();
  private final VBox myParametersControlBox = new VBox();
  private TextField myNameField;
  private TextField myAuthorField;
  private TextField myDescriptionField;
  private SelectorField myNeighborTypeSelector;
  private IntegerField myNeighborLayerField;
  private int myNeighborLayer;
  private int myNumRows;
  private int myNumCols;
  private IntegerField myRowField;
  private IntegerField myColField;
  private final AlertField myAlertField;


  private static final int DEFAULT_NUM_CELLS = 25;

  /**
   * This constructor creates the default sim view to display in splashscreen and the sim.
   *
   * @param mainController - an instance of the main controller
   * @param alertField     - the field that flashes errors
   */
  public CreateDefaultSimView(MainController mainController, AlertField alertField) {
    super();
    this.myMainController = mainController;
    this.setSpacing(ELEMENT_SPACING);
    this.setAlignment(Pos.CENTER_LEFT);
    this.myNumRows = DEFAULT_NUM_CELLS;
    this.myNumCols = DEFAULT_NUM_CELLS;
    this.myAlertField = alertField;
    this.myNeighborLayer = 1;
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
   * Handles the simulation selection process.
   */
  private void createSimulationTypeControl() {
    mySimulationSelector = new SelectorField(List.of(SimulationConfig.SIMULATIONS),
        SimulationConfig.SIMULATIONS[0], "createSimulationSelector",
        getMessage("SIMULATION_TYPE_LABEL"),
        e -> addAllParameters(mySimulationSelector.getValue()));
    mySimulationSelector.setAlignment(Pos.CENTER_LEFT);
    this.getChildren().add(mySimulationSelector);
  }

  private void addAllParameters(String simulationName) {
    myParametersControlBox.getChildren().clear();
    myParameterTextFields.clear();
    if (!SimulationConfig.getParameters(simulationName).isEmpty()) {
      Text parametersTitle = new Text(getMessage("CUSTOMIZE_PARAMETERS_TITLE"));
      parametersTitle.getStyleClass().add("secondary-title");
      myParametersControlBox.getChildren().add(parametersTitle);
    }
    for (String parameter : SimulationConfig.getParameters(simulationName)) {
      TextField newParameterField = createTextField(parameter, myParametersControlBox);
      newParameterField.setId(String.format("createSimulationParameter_%s", parameter));
      myParameterTextFields.put(parameter, newParameterField);
    }
  }

  private TextField createTextField(String label, VBox target) {
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    TextField textField = new TextField();
    Text textFieldLabel = new Text(label);
    box.getChildren().addAll(textFieldLabel, textField);
    target.getChildren().add(box);
    return textField;
  }

  /**
   * Handles the row input for the simulation.
   */
  private void createRowControl() {
    myRowField = new IntegerField();
    myRowField.setId("createSimulationRowField");
    myRowField.setText(String.valueOf(DEFAULT_NUM_CELLS));
    myRowField.textProperty()
        .addListener(
            (observableValue, oldVal, newVal) -> myNumRows = parseIntegerField(myRowField, 0));

    HBox rowBox = new HBox(new Text(getMessage("NUMBER_ROWS")), myRowField);
    rowBox.setAlignment(Pos.CENTER_LEFT);
    rowBox.setSpacing(5);
    this.getChildren().add(rowBox);
  }

  /**
   * Handles the col input for the simulation
   */
  private void createColControl() {
    myColField = new IntegerField();
    myColField.setId("createSimulationColField");
    myColField.setText(Integer.toString(DEFAULT_NUM_CELLS));
    myColField.textProperty()
        .addListener((observable, oldVal, newVal) -> myNumCols = parseIntegerField(myColField, 0));

    HBox colBox = new HBox(new Text(getMessage("NUMBER_COLUMNS")), myColField);
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
    myNameField = createTextField(getMessage("NAME_LABEL"), getMessage("DEFAULT_NAME"));
    myNameField.setId("createSimulationNameTextField");

    myAuthorField = createTextField(getMessage("AUTHOR_LABEL"), getMessage("DEFAULT_AUTHOR"));
    myAuthorField.setId("createSimulationAuthorTextField");

    myDescriptionField = createTextField(getMessage("DESCRIPTION_LABEL"),
        getMessage("DEFAULT_DESCRIPTION"));
    myDescriptionField.setId("createSimulationDescriptionTextField");

    createNeighborTypeSelector();
    createNeighborLayerField();
  }

  private TextField createTextField(String label, String defaultValue) {
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    TextField textField = new TextField();
    textField.setPromptText(defaultValue);
    Text textFieldLabel = new Text(label);
    box.getChildren().addAll(textFieldLabel, textField);
    this.getChildren().add(box);
    return textField;
  }

  private void createNeighborTypeSelector() {
    myNeighborTypeSelector = new SelectorField(getAvailableNeighborTypes(),
        getAvailableNeighborTypes().getFirst(), "createSimulationNeighborTypeSelector",
        getMessage("NEIGHBOR_TYPE_LABEL"), e -> {
    });
    this.getChildren().add(myNeighborTypeSelector);
  }

  private void createNeighborLayerField() {
    myNeighborLayerField = new IntegerField();
    myNeighborLayerField.setId("createSimulationNeighborLayerTextField");
    myNeighborLayerField.setText("1");
    myNeighborLayer = 1;

    myNeighborLayerField.textProperty()
        .addListener((observable, oldVal, newVal) -> myNeighborLayer = parseIntegerField(
            myNeighborLayerField, 1));
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    Text label = new Text(getMessage("NEIGHBOR_LAYER_LABEL"));

    box.getChildren().addAll(label, myNeighborLayerField);
    this.getChildren().add(box);
  }


  /**
   * Handles the initialization of the different parameter inputs
   */
  private void initializeParametersControl() {
    myParametersControlBox.setAlignment(Pos.CENTER_LEFT);
    myParametersControlBox.setSpacing(5);
    addAllParameters(mySimulationSelector.getValue());
    this.getChildren().add(myParametersControlBox);
  }


  SimulationMetaData createMetaData() {
    return new SimulationMetaData(
        mySimulationSelector.getValue(),
        myNameField.getText(),
        myAuthorField.getText(),
        myDescriptionField.getText(),
        myNeighborTypeSelector.getValue(),
        myNeighborLayer
    );
  }

  private int getRowCount() {
    return parseIntegerField(myRowField, DEFAULT_NUM_CELLS);
  }

  private int getColCount() {
    return parseIntegerField(myColField, DEFAULT_NUM_CELLS);
  }

  private String getSelectedSimulation() {
    return mySimulationSelector.getValue();
  }

  private boolean checkHasInvalidInput() {
    if (!validateRows(myNumRows) || !validateCols(myNumCols)) {
      return true;
    }
    return checkInvalidText(mySimulationSelector.getValue()) ||
        checkInvalidText(myNameField.getText()) ||
        checkInvalidText(myAuthorField.getText()) ||
        checkInvalidText(myDescriptionField.getText()) ||
        checkInvalidNeighborType(myNeighborTypeSelector.getValue()) ||
        checkInvalidLayer(myNeighborLayer);
  }

  private boolean checkInvalidLayer(int myNeighborLayer) {
    if (myNeighborLayer < 1) {
      myAlertField.flash(getMessage("INVALID_NEIGHBOR_LAYER"), true);
      return true;
    }
    return false;
  }

  private boolean checkInvalidNeighborType(String myNeighborType) {
    try {
      String className = String.format("cellsociety.model.simulation.getNeighborOptions.%s%s",
          myNeighborType,
          "Neighbors");
      Class.forName(className);
      return false;
    } catch (ClassNotFoundException e) {
      myAlertField.flash(getMessage("INVALID_NEIGHBOR_TYPE"), true);
      return true;
    }
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
  private void createNewSimulation() {
    SimulationMetaData metaData = createMetaData();

    Map<String, Parameter<?>> parameters = new HashMap<>();
    for (String parameter : myParameterTextFields.keySet()) {
      parameters.put(parameter,
          new Parameter<Object>(myParameterTextFields.get(parameter).getText()));
    }
    attemptCreatingNewSimulation(metaData, parameters);
  }

  private void attemptCreatingNewSimulation(SimulationMetaData metaData,
      Map<String, Parameter<?>> parameters) {
    try {
      myMainController.createNewSimulation(getRowCount(), getColCount(), getSelectedSimulation(),
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
    createSimButton.setId("createSimulationButton");
    createSimButton.setOnAction(e -> handleCreateNewSimulationAction());
    this.getChildren().add(createSimButton);
  }

  private void handleCreateNewSimulationAction() {
    if (checkHasInvalidInput()) {
      return;
    }
    try {
      createNewSimulation();
    } catch (Exception e) {
      return;
    }
    handleAdditionalButtonActions();
  }

  /**
   * Handle any addition button actions that you want to occur when the button is clicked and the
   * simulation is created successfully
   */
  protected void handleAdditionalButtonActions() {
    LOGGER.info("New simulation created from form!");
  }

}


