package cellsociety.controller;

import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;

import cellsociety.model.Grid;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.SidebarView;
import cellsociety.view.SimulationView;
import cellsociety.view.config.FileChooserConfig;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewController {
  private SidebarView mySidebarView;
  private final Group myRoot;
  private boolean isEditing = false;
  private boolean gridLinesEnabled = true;
  private SimulationView mySimulationView;


  VBox myMainViewContainer = new VBox();
  private final MainController myMainController;

  public ViewController(Stage stage, Group root) {
    myRoot = root;
    this.myMainController = new MainController(stage, root, this);

    createMainContainerAndView();
    createOrUpdateSidebar();

  }


  private void createMainContainerAndView() {
    myMainViewContainer.setPrefWidth(GRID_WIDTH + 2 * MARGIN);
    myMainViewContainer.setPrefHeight(GRID_HEIGHT + 2 * MARGIN);
    myMainViewContainer.setAlignment(Pos.CENTER);

    updateSimulationFromFile(FileChooserConfig.DEFAULT_SIMULATION_PATH);
    myRoot.getChildren().add(myMainViewContainer);
  }

  private void createNewMainViewAndUpdateViewContainer() {
    myMainViewContainer.getChildren().clear();
    mySimulationView = new SimulationView(
        GRID_WIDTH,
        GRID_HEIGHT,
        myMainController.getGridRows(),
        myMainController.getGridCols(),
        myMainController.getGrid(),
        myMainController.getSimulation(),
        this
    );
    mySimulationView.setGridLines(gridLinesEnabled);
    myMainViewContainer.getChildren().add(mySimulationView);
  }

  protected void updateSimulationFromFile(String filePath) {
    myMainController.updateSimulationFromFile(filePath);
    createNewMainViewAndUpdateViewContainer();
    myMainController.setSimulationView(mySimulationView);
    createOrUpdateSidebar();


  }

  private void createOrUpdateSidebar() {
    if (mySidebarView == null) {
      initializeSidebar();
    } else {
      mySidebarView.update();
    }
  }

  public Simulation getSimulation() {
    return myMainController.getSimulation();
  }

  /**
   * Update the animation to have a new speed
   *
   * @param speed: the new speed of the animation
   * @param start: a boolean to determine if the animation should start with the new speed or remain
   *               stopped
   */
  public void updateAnimationSpeed(double speed, boolean start) {
    myMainController.updateAnimationSpeed(speed, start);
  }

  /**
   * Get whether the grid animation is currently playing
   *
   * @return true if the animation is playing, false otherwise
   */
  public boolean isPlaying() {
    return myMainController.isPlaying();
  }


  public void initializeSidebar() {
    mySidebarView = new SidebarView(SIDEBAR_WIDTH,
        GRID_HEIGHT - (2 * MARGIN), this);
    mySidebarView.setLayoutX(GRID_WIDTH + 1.5 * MARGIN);
    mySidebarView.setLayoutY(MARGIN);
    myRoot.getChildren().add(mySidebarView);
  }

  public void clearSidebar() {
    if (mySidebarView != null) {
      mySidebarView.clearSidebar();
    }
  }

  /**
   * Handle the loading and creation of a new simulation from a file chooser
   */
  public void handleNewSimulationFromFile() {
    myMainController.handleNewSimulationFromFile();
  }

  public void handleSavingToFile() {
    myMainController.handleSavingToFile();
  }

  /**
   * Runs a single step animation on the simulation view. If the simulation animation is currently
   * running, stop the animation and conduct a single step
   */
  public void handleSingleStep() {
    myMainController.handleSingleStep();
  }


  /**
   * Start the simulation animation if it is not already running
   */
  public void startAnimation() {
    myMainController.startAnimation();
  }

  /**
   * Stop the simulation animation if it is currently running
   */
  public void stopAnimation() {
    myMainController.stopAnimation();
  }
  /**
   * Handle whether grid lines should be shown or not
   * @param selected: Whether to show grid lines
   */
  public void setGridLines(boolean selected) {
    gridLinesEnabled = selected;
    mySimulationView.setGridLines(selected);
  }

  public void createNewSimulation(int rows, int cols, String type, SimulationMetaData metaData,
      Map<String, String> parameters) {
    myMainController.createNewSimulation(rows, cols, type, metaData, parameters);
    createNewMainViewAndUpdateViewContainer();
  }


  /**
   * Change/Increment the cell state to the next possible state. Used to edit the simulation's grid
   * from user input. The cell state will only be changed if the user is currently in editing mode
   *
   * @param row:    row of cell you which to change state of
   * @param column: column of cell you which to change state of
   */
  public void changeCellState(int row, int column) {
    myMainController.changeCellState(row, column);
  }

  /**
   * Set whether the user is editing the simulation
   *
   * @param editing: boolean indicating if user is editing the view
   */
  public void setEditing(boolean editing) {
    myMainController.setEditing(editing);
  }

  /**
   * Get columns in the grid
   *
   * @return int number of columns
   */
  public int getGridCols() {
    return myMainController.getGridCols();
  }

  /**
   * Get rows in the grid
   *
   * @return int number of rows
   */
  public int getGridRows() {
    return myMainController.getGridRows();
  }

  public void updateSimulationView(Grid grid, Simulation simulation) {
      mySimulationView.step(grid, simulation);
  }

}
