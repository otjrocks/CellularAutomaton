package cellsociety.view;

import cellsociety.controller.MainController;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.model.simulation.rules.PercolationRules;
import cellsociety.model.simulation.rules.RockPaperScissorsRules;
import cellsociety.utility.CreateNewSimulation;
import cellsociety.view.components.AlertField;
import cellsociety.view.components.IntegerField;
import cellsociety.view.components.SelectorField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import util.DukeApplicationTest;

import static cellsociety.config.MainConfig.MAX_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MAX_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_COLS;
import static cellsociety.config.MainConfig.MIN_GRID_NUM_ROWS;
import static cellsociety.config.MainConfig.getMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

@ExtendWith(MockitoExtension.class)
public class CreateDefaultSimViewTest extends DukeApplicationTest {

  // I used ChatGPT for assistance in overriding the start method to get the view to show in TestFX and in refactoring my code into helper methods
  private MainController myMainController;
  private MainController myMainControllerSpy;
  private AlertField myAlertField;

  @Override
  public void start(Stage stage) {
    CreateNewSimulation createNewSimulation = new CreateNewSimulation();
    myMainController = createNewSimulation.launchNewSimulation();
    myMainControllerSpy = Mockito.spy(myMainController);
    myAlertField = lookup("#splashAlertField").query();
    // use the myMainControllerSpy to allow testFX to view and write to UI elements
    // myMainController is in charge of the actual logic of the program and is updated throughout
    // The values of the spy may be stale since it is not tracking/updating on all changes made to the main controller object
  }

  @BeforeEach
  public void setup() {
    reset(myMainControllerSpy);
  }

  @Test
  public void createSimulationForm_CreateGameOfLife_Success() {
    createSimulationForm("GameOfLife", "Game Of Life Example", "John Doe",
        "This is a test description for creating a simulation from UI.", 12, 10);


    clickOn("#createSimulationNeighborTypeSelector");
    clickOn("Moore");
    clickOn("#createSimulationNeighborTypeSelector");
    clickOn("VonNeumann");



    TextField neighborLayerField = lookup("#createSimulationNeighborLayerTextField").query();
    writeInputTo(neighborLayerField, "3");

    clickCreateSimulationButton();


    assertInstanceOf(GameOfLifeRules.class, myMainController.getSimulation().rules());
    assertSimulationMetaData("Game Of Life Example", "John Doe",
        "This is a test description for creating a simulation from UI.", 12, 10);
  }

  @Test
  public void createSimulationForm_CreatePercolation_Success() {
    createSimulationForm("Percolation", "Percolation Example", "Owen Jennings",
        "This is a Percolation", 30, 30);

    clickOn("#createSimulationNeighborTypeSelector");
    clickOn("Moore");
    clickOn("#createSimulationNeighborTypeSelector");
    clickOn("VonNeumann");


    TextField neighborLayerField = lookup("#createSimulationNeighborLayerTextField").query();
    writeInputTo(neighborLayerField, "2");

    clickCreateSimulationButton();



    assertInstanceOf(PercolationRules.class, myMainController.getSimulation().rules());
    assertSimulationMetaData("Percolation Example", "Owen Jennings", "This is a Percolation", 30,
        30);
  }

  @Test
  public void createSimulationForm_CreatePercolation_FailureMessageTooManyRows() {
    createSimulationForm("Percolation", "Percolation Example", "Owen Jennings",
        "This is a Percolation", MAX_GRID_NUM_ROWS + 1, 10);
    clickCreateSimulationButton();

    assertErrorMessage(
        String.format(getMessage("INVALID_ROWS"), MIN_GRID_NUM_ROWS, MAX_GRID_NUM_ROWS));
  }

  @Test
  public void createSimulationForm_CreatePercolation_FailureMessageTooFewRows() {
    createSimulationForm("Percolation", "Percolation Example", "Owen Jennings",
        "This is a Percolation", MIN_GRID_NUM_ROWS - 1, 10);
    clickCreateSimulationButton();

    assertErrorMessage(
        String.format(getMessage("INVALID_ROWS"), MIN_GRID_NUM_ROWS, MAX_GRID_NUM_ROWS));
  }

  @Test
  public void createSimulationForm_CreatePercolation_FailureMessageTooManyColumns() {
    createSimulationForm("Percolation", "Percolation Example", "Owen Jennings",
        "This is a Percolation", 10, MAX_GRID_NUM_COLS + 1);
    clickCreateSimulationButton();

    assertErrorMessage(
        String.format(getMessage("INVALID_COLS"), MIN_GRID_NUM_COLS, MAX_GRID_NUM_COLS));
  }

  @Test
  public void createSimulationForm_CreatePercolation_FailureMessageTooFewColumns() {
    createSimulationForm("Percolation", "Percolation Example", "Owen Jennings",
        "This is a Percolation", 10, MIN_GRID_NUM_COLS - 1);
    clickCreateSimulationButton();

    assertErrorMessage(
        String.format(getMessage("INVALID_COLS"), MIN_GRID_NUM_COLS, MAX_GRID_NUM_COLS));
  }

  @Test
  public void createSimulationForm_TestMissingName_FailureMessageMissingField() {
    createSimulationForm("GameOfLife", "", "Owen Jennings", "Test", 30, 30);
    clickCreateSimulationButton();

    assertErrorMessage(getMessage("EMPTY_FIELD"));
  }

  @Test
  public void createSimulationForm_TestMissingAuthor_FailureMessageMissingField() {
    createSimulationForm("GameOfLife", "Test Name", "", "Test", 30, 30);
    clickCreateSimulationButton();

    assertErrorMessage(getMessage("EMPTY_FIELD"));
  }

  @Test
  public void createSimulationForm_TestMissingDescription_FailureMessageMissingField() {
    createSimulationForm("GameOfLife", "Test Name", "Owen Jennings", "", 30, 30);
    clickCreateSimulationButton();

    assertErrorMessage(getMessage("EMPTY_FIELD"));
  }

  @Test
  public void createSimulationForm_TestRockPaperScissorsParameter_FailureMessageInvalidParameterNotANumber() {
    createSimulationForm("RockPaperScissors", "Test Name", "Owen Jennings",
        "Rock Paper Scissors Simulation", 30, 30);

    TextField minThresholdParameter = lookup("#createSimulationParameter_minThreshold").query();
    writeInputTo(minThresholdParameter, "Apple");

    clickCreateSimulationButton();

    assertErrorMessage(String.format(getMessage("INVALID_PARAMETER"),
        getMessage("INVALID_PARAMETER_TYPE")));
  }

  @Test
  public void createSimulationForm_TestRockPaperScissorsNeighborSearchRadius_FailureMessageInvalidNeighborLayer() {
    createSimulationForm("RockPaperScissors", "Test Name", "Justin Aronwald",
        "Rock Paper Scissors Simulation", 30, 30);

    TextField neighborLayerField = lookup("#createSimulationNeighborLayerTextField").query();
    writeInputTo(neighborLayerField, "0");

    clickCreateSimulationButton();

    assertErrorMessage(String.format(getMessage("INVALID_NEIGHBOR_LAYER")));
  }

  @Test
  public void createSimulationForm_TestRockPaperScissorsParameter_FailureMessageInvalidParameterWrongRange() {
    createSimulationForm("RockPaperScissors", "Test Name", "Owen Jennings",
        "Rock Paper Scissors Simulation", 30, 30);

    TextField minThresholdParameter = lookup("#createSimulationParameter_minThreshold").query();
    writeInputTo(minThresholdParameter, "5");

    TextField numStates = lookup("#createSimulationParameter_numStates").query();
    writeInputTo(numStates, "5");

    clickCreateSimulationButton();

    assertErrorMessage(String.format(getMessage("INVALID_PARAMETER"), "minThreshold"));
  }

  @Test
  public void createSimulationForm_TestRockPaperScissorsParameter_FailureMessageInvalidParameterNegativeStates() {
    createSimulationForm("RockPaperScissors", "Test Name", "Owen Jennings",
        "Rock Paper Scissors Simulation", 30, 30);

    TextField minThresholdParameter = lookup("#createSimulationParameter_minThreshold").query();
    writeInputTo(minThresholdParameter, "0.5");

    TextField numStates = lookup("#createSimulationParameter_numStates").query();
    writeInputTo(numStates, "-1.5");

    clickCreateSimulationButton();
    assertErrorMessage(String.format(getMessage("INVALID_PARAMETER"), "numStates"));
  }

  @Test
  public void createSimulationForm_TestRockPaperScissorsWithParameters_Success() {
    createSimulationForm("RockPaperScissors", "Rock Paper", "Owen Jennings",
        "Rock Paper Scissors Simulation", 30, 30);

    TextField minThresholdParameter = lookup("#createSimulationParameter_minThreshold").query();
    writeInputTo(minThresholdParameter, "0.5");

    TextField numStates = lookup("#createSimulationParameter_numStates").query();
    writeInputTo(numStates, "2");

    clickCreateSimulationButton();

    // Assert that simulation was created successfully and main controller was updated
    assertInstanceOf(RockPaperScissorsRules.class, myMainController.getSimulation().rules());
    assertSimulationMetaData("Rock Paper", "Owen Jennings", "Rock Paper Scissors Simulation", 30,
        30);
  }

  private void createSimulationForm(String simulationType, String name, String author,
      String description, int rows, int cols) {
    clickOn("#createSimulationSelector");
    clickOn(simulationType);

    fillSimulationForm(name, author, description, rows, cols);
  }

  private void assertErrorMessage(String errorMessage) {
    String expectedError = String.format(getMessage("WARNING_PREFIX"), errorMessage);
    assertTrue(myAlertField.getMessages().contains(expectedError));
  }

  private void fillSimulationForm(String name, String author, String description, int rows,
      int cols) {
    IntegerField rowField = lookup("#createSimulationRowField").query();
    writeInputTo(rowField, String.valueOf(rows));

    IntegerField colField = lookup("#createSimulationColField").query();
    writeInputTo(colField, String.valueOf(cols));

    TextField nameField = lookup("#createSimulationNameTextField").query();
    writeInputTo(nameField, name);

    TextField authorField = lookup("#createSimulationAuthorTextField").query();
    writeInputTo(authorField, author);

    TextField descriptionField = lookup("#createSimulationDescriptionTextField").query();
    writeInputTo(descriptionField, description);
  }

  private void clickCreateSimulationButton() {
    Button createSimulationButton = lookup("#createSimulationButton").query();
    clickOn(createSimulationButton);
    waitForFxEvents();  // Ensure that all UI events and updates are processed before assertions
  }

  private void assertSimulationMetaData(String name, String author, String description, int rows,
      int cols) {
    SimulationMetaData currentMetaData = myMainController.getSimulation().data();
    assertEquals(name, currentMetaData.name());
    assertEquals(author, currentMetaData.author());
    assertEquals(description, currentMetaData.description());
    assertEquals(rows, myMainController.getGridRows());
    assertEquals(cols, myMainController.getGridCols());
  }

}
