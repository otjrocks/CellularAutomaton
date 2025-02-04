package cellsociety.view;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A view to handle the setting of new coordinates for the simulation grid
 *
 * @author Owen Jennings
 */
public class CreateNewSimulationView extends VBox {

  private final MainController myMainController;
  private int myNumRows;
  private int myNumCols;
  private IntegerField rowField;
  private IntegerField colField;
  private ComboBox<String> simulationSelector;
  private TextField myNameField;
  private TextField myAuthorField;
  private TextField myDescriptionField;
  private VBox parametersControlBox;
  private final Map<String, DoubleField> myParameterTextFields = new HashMap<>();

  public CreateNewSimulationView(int rows, int cols, MainController mainController) {
    this.setSpacing(5);
    this.setAlignment(Pos.CENTER_LEFT);
    myNumRows = rows;
    myNumCols = cols;
    myMainController = mainController;
    initialize();
  }

  private void initialize() {
    createTitle();
    createSimulationTypeControl();
    createRowControl();
    createColControl();
    createSimulationMetaDataTextFields();
    initializeParametersControl();
    createUpdateButton();
  }

  private void initializeParametersControl() {
    parametersControlBox = new VBox();
    parametersControlBox.setAlignment(Pos.CENTER_LEFT);
    parametersControlBox.setSpacing(5);
    addAllParameters(simulationSelector.getValue());
    this.getChildren().add(parametersControlBox);
  }

  private void addAllParameters(String simulationName) {
    parametersControlBox.getChildren().clear();
    myParameterTextFields.clear();
    SimulationConfig.getParameters(simulationName);
    if (!SimulationConfig.getParameters(simulationName).isEmpty()) {
      Text parametersTitle = new Text("Customize Parameters: ");
      parametersTitle.setFont(new Font(20));
      parametersControlBox.getChildren().add(parametersTitle);
    }
    for (String parameter : SimulationConfig.getParameters(simulationName)) {
      DoubleField newParameterField = createDoubleField(parameter, "0", parametersControlBox);
      myParameterTextFields.put(parameter, newParameterField);
    }
  }

  private void createSimulationMetaDataTextFields() {
    myNameField = createTextField("Name: ", "A Glider", this);
    myAuthorField = createTextField("Author: ", "John Doe", this);
    myDescriptionField = createTextField("Description: ", "A simple glider", this);
  }

  private DoubleField createDoubleField(String label, String defaultValue, VBox target) {
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    DoubleField doubleTextField = new DoubleField();
    doubleTextField.setText(defaultValue);
    Text textFieldLabel = new Text(label);
    box.getChildren().addAll(textFieldLabel, doubleTextField);
    target.getChildren().add(box);
    return doubleTextField;
  }

  private TextField createTextField(String label, String defaultValue, VBox target) {
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(5);
    TextField textField = new TextField(defaultValue);
    Text textFieldLabel = new Text(label);
    box.getChildren().addAll(textFieldLabel, textField);
    target.getChildren().add(box);
    return textField;
  }

  private void createSimulationTypeControl() {
    ObservableList<String> options =
        FXCollections.observableArrayList(SimulationConfig.simulations);
    simulationSelector = new ComboBox<>(options);
    simulationSelector.setValue(options.getFirst());
    simulationSelector.valueProperty()
        .addListener((ov, t, t1) -> addAllParameters(simulationSelector.getValue()));
    this.getChildren().add(simulationSelector);
  }

  private void createTitle() {
    Text title = new Text("Create New Grid:");
    title.setFont(new Font("Arial", 20));
    this.getChildren().add(title);
  }

  private void createUpdateButton() {
    javafx.scene.control.Button updateButton = new javafx.scene.control.Button("Create New Grid");
    updateButton.setOnMouseClicked(event -> {
      myNumRows = parseIntegerField(rowField, myNumRows);
      myNumCols = parseIntegerField(colField, myNumCols);

      SimulationMetaData metaData = new SimulationMetaData(
          simulationSelector.getValue(),
          myNameField.getText(),
          myAuthorField.getText(),
          myDescriptionField.getText());

      Map<String, Double> parameters = new HashMap<>();
      for (String parameter : myParameterTextFields.keySet()) {
        parameters.put(parameter,
            Double.parseDouble(myParameterTextFields.get(parameter).getText()));
      }

      myMainController.createNewSimulation(myNumRows, myNumCols, simulationSelector.getValue(),
          metaData, parameters);
    });
    this.getChildren().add(updateButton);
  }

  private void createRowControl() {
    rowField = new IntegerField();
    rowField.setText(Integer.toString(myNumRows));
    rowField.textProperty()
        .addListener((obs, oldVal, newVal) -> myNumRows = parseIntegerField(rowField, myNumRows));

    HBox rowBox = new HBox(new Text("Number of Rows:"), rowField);
    rowBox.setAlignment(Pos.CENTER_LEFT);
    rowBox.setSpacing(5);
    this.getChildren().add(rowBox);
  }

  private void createColControl() {
    colField = new IntegerField();
    colField.setText(Integer.toString(myNumCols));
    colField.textProperty()
        .addListener((obs, oldVal, newVal) -> myNumCols = parseIntegerField(colField, myNumCols));

    HBox colBox = new HBox(new Text("Number of Columns:"), colField);
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
}
