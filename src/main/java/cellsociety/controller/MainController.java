package cellsociety.controller;

import cellsociety.view.config.StateDisplayConfig;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cellsociety.view.config.FileChooserConfig;

import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.STEP_SPEED;

import cellsociety.config.SimulationConfig;
import cellsociety.model.Grid;
import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.XMLHandlers.XMLWriter;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.SidebarView;
import cellsociety.view.SimulationView;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A class to handle the main interactions between the model and view classes
 *
 * @author Owen Jennings
 */
public class MainController {

  private final Group myRoot;
  private final Stage myStage;
  private SimulationView mySimulationView;
  private SidebarView mySidebarView;
  private Simulation mySimulation;
  private Grid myGrid;
  VBox myMainViewContainer = new VBox();
  Timeline mySimulationAnimation = new Timeline();
  private boolean isEditing = false;

  /**
   * Initialize the MainController
   *
   * @param root: the main root group of the program
   */
  public MainController(Stage stage, Group root) {
    myStage = stage;
    myRoot = root;
    createMainContainerAndView();
    initializeSimulationAnimation();
  }

  /**
   * Start the simulation animation if it is not already running
   */
  public void startAnimation() {
    if (mySimulationAnimation.getStatus() != Status.RUNNING) {
      mySimulationAnimation.play();
    }
  }

  /**
   * Stop the simulation animation if it is currently running
   */
  public void stopAnimation() {
    if (mySimulationAnimation.getStatus() == Status.RUNNING) {
      mySimulationAnimation.stop();
    }
  }

  /**
   * Returns the current simulation object
   *
   * @return The currently running simulation object
   */
  public Simulation getSimulation() {
    return mySimulation;
  }

  /**
   * Runs a single step animation on the simulation view. If the simulation animation is currently
   * running, stop the animation and conduct a single step
   */
  public void handleSingleStep() {
    stopAnimation();
    step();
  }

  /**
   * Update the animation to have a new speed
   *
   * @param speed: the new speed of the animation
   * @param start: a boolean to determine if the animation should start with the new speed or remain
   *               stopped
   */
  public void updateAnimationSpeed(double speed, boolean start) {
    mySimulationAnimation.stop();
    mySimulationAnimation.getKeyFrames().clear();
    mySimulationAnimation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(speed), e -> {
          try {
            step();
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }));
    if (!isEditing && start) {
      mySimulationAnimation.play();
    }
  }

  /**
   * Set whether the user is editing the simulation
   *
   * @param editing: boolean indicating if user is editing the view
   */
  public void setEditing(boolean editing) {
    isEditing = editing;
  }

  /**
   * Get rows in the grid
   *
   * @return int number of rows
   */
  public int getGridRows() {
    return myGrid.getRows();
  }

  /**
   * Get columns in the grid
   *
   * @return int number of columns
   */
  public int getGridCols() {
    return myGrid.getCols();
  }

  /**
   * Get whether the grid animation is currently playing
   *
   * @return true if the animation is playing, false otherwise
   */
  public boolean isPlaying() {
    return mySimulationAnimation.getStatus() == Status.RUNNING;
  }

  public void createNewSimulation(int rows, int cols, String type, SimulationMetaData metaData,
      Map<String, Double> parameters) {
    myGrid = new Grid(rows, cols);
    mySimulation = SimulationConfig.getNewSimulation(type, metaData, parameters);
    initializeGridWithCells();
    createNewMainViewAndUpdateViewContainer();
    createOrUpdateSidebar();
  }

  private void initializeGridWithCells() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getCols(); j++) {
        int initialState = 0;
        String simulationType = mySimulation.data().type();
        myGrid.addCell(SimulationConfig.getNewCell(i, j, initialState, simulationType));
      }
    }
  }

  /**
   * Change/Increment the cell state to the next possible state. Used to edit the simulation's grid
   * from user input. The cell state will only be changed if the user is currently in editing mode
   *
   * @param row:    row of cell you which to change state of
   * @param column: column of cell you which to change state of
   */
  public void changeCellState(int row, int column) {
    if (isEditing) {
      Cell cell = myGrid.getCell(row, column);
      int nextState = getNextAvailableState(cell);

      cell.setState(nextState);
      // create new cell instead of just updating cell status, to ensure that new cell has all other information reset for custom cell types
      myGrid.updateCell(
          SimulationConfig.getNewCell(row, column, nextState, mySimulation.data().type()));
      mySimulationView.setColor(row, column,
          StateDisplayConfig.getStateInfo(mySimulation, nextState).color());
    }
  }

  // From the states list from the simulation get the next available state from a sorted order
  private int getNextAvailableState(Cell cell) {
   int numStates = mySimulation.rules().getNumberStates();
   int currentState = cell.getState();
   return (currentState + 1) % numStates;
  }

  /**
   * Handle the loading and creation of a new simulation from a file chooser
   */
  public void handleNewSimulationFromFile() {
    stopAnimation(); // stop animation if it is currently running
    File file = FileChooserConfig.FILE_CHOOSER.showOpenDialog(myStage);
    if (file != null) { // only update simulation if a file was selected
      String filePath = file.getAbsolutePath();
      updateSimulationFromFile(filePath);
    }
  }

  public void handleSavingToFile() {
    XMLWriter.saveSimulationToXML(mySimulation, myGrid, myStage);
  }

  private void updateSimulationFromFile(String filePath) {
    XMLHandler xmlHandler = new XMLHandler(filePath);

    mySimulation = xmlHandler.getSim();
    myGrid = xmlHandler.getGrid();
    createNewMainViewAndUpdateViewContainer();
    createOrUpdateSidebar();
  }

  private void createOrUpdateSidebar() {
    if (mySidebarView == null) {
      initializeSidebar(this);
    } else {
      mySidebarView.update();
    }
  }

  private void createNewMainViewAndUpdateViewContainer() {
    myMainViewContainer.getChildren().clear();
    mySimulationView = new SimulationView(GRID_WIDTH, GRID_HEIGHT, myGrid.getRows(),
        myGrid.getCols(),
        myGrid, mySimulation, this);
    myMainViewContainer.getChildren().add(mySimulationView);
  }

  private void initializeSimulationAnimation() {
    mySimulationAnimation.setCycleCount(Timeline.INDEFINITE);
    mySimulationAnimation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(STEP_SPEED), e -> {
          try {
            step();  // step function
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }));
  }

  private void step() {
    mySimulationView.step(myGrid, mySimulation);
  }

  private void createMainContainerAndView() {
    myMainViewContainer.setPrefWidth(GRID_WIDTH + 2 * MARGIN);
    myMainViewContainer.setPrefHeight(GRID_HEIGHT + 2 * MARGIN);
    myMainViewContainer.setAlignment(Pos.CENTER);
    updateSimulationFromFile(FileChooserConfig.DEFAULT_SIMULATION_PATH);
    myRoot.getChildren().add(myMainViewContainer);
  }

  private void initializeSidebar(MainController controller) {
    mySidebarView = new SidebarView(SIDEBAR_WIDTH,
        GRID_HEIGHT - (2 * MARGIN), controller);
    mySidebarView.setLayoutX(GRID_WIDTH + 1.5 * MARGIN);
    mySidebarView.setLayoutY(MARGIN);
    myRoot.getChildren().add(mySidebarView);
  }

}
