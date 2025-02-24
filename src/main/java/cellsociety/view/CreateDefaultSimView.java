package cellsociety.view;

import cellsociety.model.simulation.InvalidParameterException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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

  private final MainController myMainController;
  private ComboBox<String> mySimulationSelector;
  private final Map<String, TextField> myParameterTextFields = new HashMap<>();
  private final VBox myParametersControlBox = new VBox();
  private TextField myNameField;
  private TextField myAuthorField;
  private TextField myDescriptionField;
  private ComboBox<String> myNeighborTypeSelector;
  private IntegerField myNeighborLayerField;
  private int myNeighborLayer;
  private int myNumRows;
  private int myNumCols;
  private IntegerField myRowField;
  private IntegerField myColField;
  private final AlertField myAlertField;



  private static final int DEFAULT_NUM_CELLS = 25;

  /**
   * This constructor creates the default sim view to display in splashscreen and the sim
   *
   * @param mainController - an instance of the main controller
   * @param alertField     - the field that flashes errors
   */
  public CreateDefaultSimView(MainController mainController, AlertField alertField) {
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
   * Handles the simulation selection process
   */
  private void createSimulationTypeControl() {
    Text createSimButtonText = new Text(getMessage("NEW_SIM_BUTTON_TEXT"));
    ObservableList<String> options =
        FXCollections.observableArrayList(SimulationConfig.SIMULATIONS);
    mySimulationSelector = new ComboBox<>(options);
    mySimulationSelector.setId("createSimulationSelector");
    mySimulationSelector.setValue(options.getFirst());
    mySimulationSelector.valueProperty()
        .addListener((_, _, _) -> addAllParameters(mySimulationSelector.getValue()));
    HBox container = new HBox();
    container.setAlignment(Pos.CENTER_LEFT);
    container.setSpacing(5);
    Text simulationTypeLabel = new Text(getMessage("SIMULATION_TYPE_LABEL"));
    container.getChildren().addAll(simulationTypeLabel, mySimulationSelector);
    this.getChildren().addAll(createSimButtonText, container);
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
   * Handles the row input for the simulation
   */
  private void createRowControl() {
    myRowField = new IntegerField();
    myRowField.setId("createSimulationRowField");
    myRowField.setText(String.valueOf(DEFAULT_NUM_CELLS));
    myRowField.textProperty()
        .addListener((_, _, _) -> myNumRows = parseIntegerField(myRowField, 0));

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
        .addListener((_, _, _) -> myNumCols = parseIntegerField(myColField, 0));

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
    myNameField = createTextField(getMessage("NAME_LABEL"), getMessage("DEFAULT_NAME"), this);
    myNameField.setId("createSimulationNameTextField");

    myAuthorField = createTextField(getMessage("AUTHOR_LABEL"), getMessage("DEFAULT_AUTHOR"), this);
    myAuthorField.setId("createSimulationAuthorTextField");

    myDescriptionField = createTextField(getMessage("DESCRIPTION_LABEL"), getMessage("DEFAULT_DESCRIPTION"), this);
    myDescriptionField.setId("createSimulationDescriptionTextField");

    createNeighborTypeSelector(this);
    createNeighborLayerField(this);
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

  private void createNeighborTypeSelector(VBox target) {
    myNeighborTypeSelector = new ComboBox<>(getAvailableNeighborTypes());
    myNeighborTypeSelector.setId("createSimulationNeighborTypeSelector");

    if (myNeighborTypeSelector.getValue() != null) {
    myNeighborTypeSelector.setValue(myNeighborTypeSelector.getItems().getFirst());
    }

    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    Text label = new Text(getMessage("NEIGHBOR_TYPE_LABEL"));

    box.getChildren().addAll(label, myNeighborTypeSelector);
    target.getChildren().add(box);
  }

  private void createNeighborLayerField(VBox target) {
    myNeighborLayerField = new IntegerField();
    myNeighborLayerField.setId("createSimulationNeighborLayerTextField");

    myNeighborLayerField.textProperty().addListener((_, _, _) -> myNeighborLayer = parseIntegerField(myNeighborLayerField, 1));
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    Text label = new Text(getMessage("NEIGHBOR_LAYER_LABEL"));

    box.getChildren().addAll(label, myNeighborLayerField);
    target.getChildren().add(box);
  }

  //Had a little bit of ChatGPT help with the last few lines of this

  /**
   * scans the directory to find the various different neighbor types
   * @return - a list of the string names of the neighbor types
   */
  private static ObservableList<String> getAvailableNeighborTypes() {
    File directory = new File("src/main/java/cellsociety/model/simulation/getNeighborOptions/");
    List<String> neighborTypes = new ArrayList<>();

    if (directory.exists() && directory.isDirectory()) {
      File[] files = directory.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.getName().endsWith("Neighbors.java")) {
            String typeName = file.getName().replace("Neighbors.java", "");
            neighborTypes.add(typeName);
          }
        }
      }
    }
    return FXCollections.observableArrayList(neighborTypes);
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

  /**
   * @return - the number of grid rows from the input
   */
  private int getRowCount() {
    return parseIntegerField(myRowField, DEFAULT_NUM_CELLS);
  }

  /**
   * @return - the number of grid cols from the input
   */
  private int getColCount() {
    return parseIntegerField(myColField, DEFAULT_NUM_CELLS);
  }

  /**
   * @return - the simulation selected
   */
  private String getSelectedSimulation() {
    return mySimulationSelector.getValue();
  }

  /**
   * @return - whether the input fields were valid inputs
   */
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
      String className = String.format("cellsociety.model.simulation.getNeighborOptions.%s%s", myNeighborType,
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
  private void createNewSimulation() throws IllegalArgumentException, InvalidParameterException {
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
      Map<String, Parameter<?>> parameters) throws InvalidParameterException {
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
    createSimButton.setOnAction(_ -> handleCreateNewSimulationAction());
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
  protected void handleAdditionalButtonActions() throws IllegalArgumentException {
  }

}


