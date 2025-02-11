package cellsociety.view;

import static cellsociety.config.MainConfig.MAX_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MAX_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.MESSAGES;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;

import cellsociety.config.SimulationConfig;
import cellsociety.controller.MainController;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.DoubleField;
import cellsociety.view.components.IntegerField;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SplashScreenView extends VBox {
  private final Stage splashScreenStage;
  public static final double ELEMENT_SPACING = 5;

  private final AlertField myAlertField;
  public static final int WIDTH = 700;
  public static final int HEIGHT = 800;

  private ComboBox<String> languageDropdown;
  private Text changeLanguageText;
  private Button myChooseFileButton;
  private Runnable onStart;
  private MainController mainController;

  private ComboBox<String> simulationSelector;
  private final Map<String, DoubleField> myParameterTextFields = new HashMap<>();
  private VBox parametersControlBox;
  private TextField myNameField;
  private TextField myAuthorField;
  private TextField myDescriptionField;
  private int myNumRows;
  private int myNumCols;
  private IntegerField rowField;
  private IntegerField colField;

  public static final int DEFAULT_NUM_CELLS = 25;





  public SplashScreenView(AlertField myAlertField, Stage stage, MainController mainController,
      Runnable onStart) {
    this.myAlertField = myAlertField;
    this.setSpacing(ELEMENT_SPACING * 2);
    this.splashScreenStage = stage;
    this.onStart = onStart;
    this.mainController = mainController;

    initializeSplashScreen(onStart);
  }

  private void initializeSplashScreen(Runnable onStart) {

    Text title = new Text(MESSAGES.getString("SPLASH_HEADER"));
    Text description = new Text(MESSAGES.getString("SPLASH_DESCRIPTION"));
    Text instructions = new Text(MESSAGES.getString("SPLASH_INSTRUCTIONS"));
    //Text blankButton = new Text(MESSAGES.getString("SPLASH_CREATE_BLANK_BUTTON"));
    Text changeLanguageButton = new Text(MESSAGES.getString("CHANGE_LANGUAGE"));
    Text changeThemeButton = new Text(MESSAGES.getString("CHANGE_THEME"));

    createLanguageDropdown();
    Text loadButton = new Text(MESSAGES.getString("SPLASH_LOAD_BUTTON"));
    this.getChildren().addAll(title, description, instructions, changeLanguageText, languageDropdown, loadButton);
    createFileChooserButton();

    createSimulationTypeControl();
    createRowControl();
    createColControl();
    createSimulationMetaDataTextFields();
    initializeParametersControl();
    createUpdateButton();


    Scene splashScreenScene = new Scene(this, WIDTH, HEIGHT);
    splashScreenStage.setScene(splashScreenScene);
  }

  private void createLanguageDropdown() {
    languageDropdown = new ComboBox<>();
    changeLanguageText = new Text(MESSAGES.getString("CHANGE_LANGUAGE"));

    String propertiesFolderPath = "src/main/resources/cellsociety/languages/";
    List<String> languages = fetchLanguages(propertiesFolderPath);

    if (languages.isEmpty()) {
      myAlertField.flash(MESSAGES.getString("NO_LANGUAGES_FOUND"), false);
    }

    for (String language : languages) {
      languageDropdown.getItems().add(language);
    }
    if (languages.contains("English")) {
      languageDropdown.setValue("English");
    } else {
      languageDropdown.setValue(languages.getFirst());
    }

    languageDropdown.setOnAction(event -> {
      String language = languageDropdown.getValue();
      System.out.println("Selected language: " + language);
    });
  }

  private List<String> fetchLanguages(String propertiesFolderPath) {
    List<String> languages = new ArrayList<>();
    File directory = new File(propertiesFolderPath);
    // this next line was from an LLM
    File[] files = directory.listFiles((dir, name) -> name.endsWith(".properties"));

    if (files != null) {
      for (File file : files) {
        String fileNameWithoutExtension = file.getName().replace(".properties", "");
        languages.add(fileNameWithoutExtension);
      }
    }

    return languages;
  }


  private void createFileChooserButton() {
    myChooseFileButton = new Button(MESSAGES.getString("CHOOSE_FILE_BUTTON"));
    myChooseFileButton.setOnAction(event -> {
      try {
        mainController.handleNewSimulationFromFile();
        splashScreenStage.close();
        onStart.run();
      } catch (IllegalArgumentException e) {
        myAlertField.flash(e.getMessage(), true);
        myAlertField.flash(MESSAGES.getString("LOAD_ERROR"), true);
      } catch (Exception e) {
        myAlertField.flash(MESSAGES.getString("LOAD_ERROR"), true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
      }
    });
    this.getChildren().add(myChooseFileButton);
  }

  private void createSimulationTypeControl() {
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
    this.getChildren().add(container);
  }

  private void createSimulationMetaDataTextFields() {
    myNameField = createTextField(MESSAGES.getString("NAME_LABEL"),
        MESSAGES.getString("DEFAULT_NAME"), this);
    myAuthorField = createTextField(MESSAGES.getString("AUTHOR_LABEL"),
        MESSAGES.getString("DEFAULT_AUTHOR"), this);
    myDescriptionField = createTextField(MESSAGES.getString("DESCRIPTION_LABEL"),
        MESSAGES.getString("DEFAULT_DESCRIPTION"), this);
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


  private void createUpdateButton() {
    Button updateButton = new Button(MESSAGES.getString("CREATE_NEW_GRID_HEADER"));
    updateButton.setOnMouseClicked(event -> {
      handleUpdateButton();
    });
    this.getChildren().add(updateButton);
  }

  private void handleUpdateButton() {
    myNumRows = parseIntegerField(rowField, myNumRows);
    myNumCols = parseIntegerField(colField, myNumCols);

    if (runValidationTests()) {
      return;
    }

    splashScreenStage.close();
    onStart.run();
    createNewSimulation();
  }

  private boolean runValidationTests() {
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
      myAlertField.flash(MESSAGES.getString("EMPTY_FIELD"), true);
      return true;
    }
    return false;
  }

  private boolean validateRows(int numRows) {
    boolean valid = numRows >= MIN_GRID_NUM_ROWS && numRows <= MAX_GRID_NUM_ROWS;
    if (!valid) {
      myAlertField.flash(
          String.format(MESSAGES.getString("INVALID_ROWS"), MIN_GRID_NUM_ROWS, MAX_GRID_NUM_ROWS),
          true);
    }
    return valid;
  }

  private boolean validateCols(int numCols) {
    boolean valid = numCols >= MIN_GRID_NUM_COLS && numCols <= MAX_GRID_NUM_COLS;
    if (!valid) {
      myAlertField.flash(
          String.format(MESSAGES.getString("INVALID_COLS"), MIN_GRID_NUM_COLS, MAX_GRID_NUM_COLS),
          true);
    }
    return valid;
  }

  private void createNewSimulation() {
    SimulationMetaData metaData = createMetaData();

    Map<String, Double> parameters = createAndValidateParameters();
    attemptCreatingNewSimulation(metaData, parameters);
  }

  private SimulationMetaData createMetaData() {
    SimulationMetaData metaData = new SimulationMetaData(
        simulationSelector.getValue(),
        myNameField.getText(),
        myAuthorField.getText(),
        myDescriptionField.getText());
    return metaData;
  }

  private Map<String, Double> createAndValidateParameters() {
    Map<String, Double> parameters = new HashMap<>();
    boolean validParameters = true;
    for (String parameter : myParameterTextFields.keySet()) {
      double value;
      try {
        value = Double.parseDouble(myParameterTextFields.get(parameter).getText());
      } catch (Exception e) {
        myAlertField.flash(MESSAGES.getString("INVALID_PARAMETERS"), true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
        validParameters = false;
        break;
      }
      parameters.put(parameter, value);
    }
    if (!validParameters) {
      return null;
    }
    return parameters;
  }

  private void attemptCreatingNewSimulation(SimulationMetaData metaData,
      Map<String, Double> parameters) {
    try {
      mainController.createNewSimulation(myNumRows, myNumCols, simulationSelector.getValue(),
          metaData, parameters);
      myAlertField.flash(MESSAGES.getString("NEW_SIMULATION_CREATED"), false);
    } catch (IllegalArgumentException e) {
      myAlertField.flash(MESSAGES.getString("ERROR_CREATING_SIMULATION"), true);
      myAlertField.flash(e.getMessage(), true);
    } catch (Exception e) {
      myAlertField.flash(MESSAGES.getString("ERROR_CREATING_SIMULATION"), true);
      if (VERBOSE_ERROR_MESSAGES) {
        myAlertField.flash(e.getMessage(), true);
      }
    }
  }

  private void createRowControl() {
    rowField = new IntegerField();
    rowField.setText(String.valueOf(DEFAULT_NUM_CELLS));
    rowField.textProperty()
        .addListener((obs, oldVal, newVal) -> myNumRows = parseIntegerField(rowField, 0));

    HBox rowBox = new HBox(new Text(MESSAGES.getString("NUMBER_ROWS")), rowField);
    rowBox.setAlignment(Pos.CENTER_LEFT);
    rowBox.setSpacing(5);
    this.getChildren().add(rowBox);
  }

  private void createColControl() {
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


  public void show() {
    splashScreenStage.show();
  }

}
