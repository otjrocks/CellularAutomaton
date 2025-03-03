package cellsociety.controller;

import cellsociety.model.cell.CellUpdate;
import cellsociety.model.edge.EdgeStrategyFactory;
import cellsociety.model.edge.EdgeStrategyFactory.EdgeStrategyType;
import cellsociety.view.grid.GridViewFactory.CellShapeType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import static cellsociety.config.MainConfig.DEFAULT_CELL_SHAPE;
import static cellsociety.config.MainConfig.DEFAULT_EDGE_STRATEGY;
import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.LOGGER;
import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.STEP_SPEED;
import static cellsociety.config.MainConfig.getMessage;

import cellsociety.config.SimulationConfig;
import cellsociety.model.Grid;
import cellsociety.model.xml.GridException;
import cellsociety.model.xml.InvalidStateException;
import cellsociety.model.xml.XMLHandler;
import cellsociety.model.xml.XMLWriter;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.BottomBarView;
import cellsociety.view.SidebarView;
import cellsociety.view.SimulationView;
import cellsociety.view.SplashScreenView;
import cellsociety.view.components.AlertField;
import cellsociety.view.config.FileChooserConfig;
import cellsociety.view.config.StateDisplayConfig;
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
  private BottomBarView myBottomBarView;
  private Simulation mySimulation;
  private final SplashScreenView mySplashScreenView;
  private Grid myGrid;
  private CellShapeType myCellShapeType = DEFAULT_CELL_SHAPE;
  private EdgeStrategyType myEdgeStrategyType = DEFAULT_EDGE_STRATEGY;
  private final VBox myMainViewContainer = new VBox();
  private final Timeline mySimulationAnimation = new Timeline();
  private boolean isEditing = false;
  private boolean gridLinesEnabled = Boolean.parseBoolean(
      PreferencesController.getPreference("gridLines", "true"));

  private final ThemeController myThemeController;
  private int myIterationCount;

  /**
   * Initialize the MainController
   *
   * @param root: the main root group of the program
   */
  public MainController(Stage stage, Group root) {
    myThemeController = new ThemeController(stage);
    myStage = stage;
    myRoot = root;
    createMainContainerAndView();
    initializeSimulationAnimation();
    mySplashScreenView = new SplashScreenView(new AlertField(), mySidebarView, this);
    root.getChildren().add(mySplashScreenView);
    myRoot.getChildren().remove(mySidebarView);
    myRoot.getChildren().remove(myBottomBarView);
  }

  /**
   * Hide the splash screen view
   */
  public void hideSplashScreen() {
    myRoot.getChildren().remove(mySidebarView);
    myRoot.getChildren().remove(myBottomBarView);
    mySidebarView = null; // ensure fresh initialization of sidebar in case of language change
    myBottomBarView = null;
    createOrUpdateSidebar();
    createOrUpdateBottomBar();
    myRoot.getChildren().remove(mySplashScreenView);
    myRoot.getChildren().add(myMainViewContainer);
  }

  /**
   * Set the theme to the themeName provided if it exists, otherwise fallback to default theme
   *
   * @param themeName: Name of theme you which to set
   */
  public void setTheme(String themeName) {
    myThemeController.setTheme(themeName);
    mySimulationView.updateGridLinesColor();
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
    PreferencesController.setPreference("animationSpeed", String.valueOf(speed));
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
   * Get whether the grid animation is currently playing
   *
   * @return true if the animation is playing, false otherwise
   */
  public boolean isPlaying() {
    return mySimulationAnimation.getStatus() == Status.RUNNING;
  }

  /**
   * Create a new simulation with the provided information
   *
   * @param rows       The number of rows for the grid
   * @param cols       the number of columns for the grid
   * @param type       the type of the simulation as a string
   * @param metaData   The metadata for your simulation
   * @param parameters The parameters map for your simulation
   */
  public void createNewSimulation(int rows, int cols, String type, SimulationMetaData metaData,
      Map<String, Parameter<?>> parameters) {
    myGrid = new Grid(rows, cols, EdgeStrategyFactory.createEdgeStrategy(DEFAULT_EDGE_STRATEGY));
    try {
      mySimulation = SimulationConfig.getNewSimulation(type, metaData, parameters);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(
          e.getCause()
              .getMessage());  // provide exception message thrown by class, not the reflection api
    } catch (ClassNotFoundException | NoSuchMethodException |
             InstantiationException | IllegalAccessException | InvalidParameterException e) {
      throw new RuntimeException(e);
    }
    initializeGridWithCells();
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
    if (isEditing) {
      Cell cell = myGrid.getCell(row, column);
      int nextState = getNextAvailableState(cell);
      myGrid.setState(row, column, nextState, mySimulation);
      // create new cell instead of just updating cell status, to ensure that new cell has all other information reset for custom cell types
      myGrid.updateCell(
          SimulationConfig.getNewCell(row, column, nextState, mySimulation.data().type()));
      mySimulationView.setColor(row, column,
          StateDisplayConfig.getStateInfo(mySimulation, nextState).color());
    }
  }

  /**
   * Handle the loading and creation of a new simulation from a file chooser
   */
  public void handleNewSimulationFromFile()
      throws GridException, InvalidStateException, IOException, ParserConfigurationException, SAXException {
    stopAnimation(); // stop animation if it is currently running
    File file = FileChooserConfig.FILE_CHOOSER.showOpenDialog(myStage);
    if (file == null) { // only update simulation if a file was selected
      throw new IllegalArgumentException(getMessage("NO_FILE"));
    }
    String filePath = file.getAbsolutePath();
    updateSimulationFromFile(filePath);
  }

  /**
   * A method to handle the saving of the current state of the program to an XML file using the
   * XMLWriter
   */
  public void handleSavingToFile() {
    XMLWriter.saveSimulationToXML(mySimulation, myGrid, myCellShapeType, myEdgeStrategyType,
        myStage);
  }

  /**
   * Update the main simulation of the program
   *
   * @param simulation: The simulation you wish to switch to
   */
  public void updateSimulation(Simulation simulation) {
    mySimulation = simulation;
    createNewMainViewAndUpdateViewContainer();
    createOrUpdateSidebar();
    createOrUpdateBottomBar();
  }

  /**
   * Initialize the sidebar of the program
   */
  public void initializeSidebar() {
    mySidebarView = new SidebarView(SIDEBAR_WIDTH,
        GRID_HEIGHT - (2 * MARGIN), this);
    mySidebarView.setLayoutX(GRID_WIDTH + 1.5 * MARGIN);
    mySidebarView.setLayoutY(MARGIN);
    myRoot.getChildren().add(mySidebarView);
  }

  /**
   * Initialize the bottom bar of the program
   */
  public void initializeBottomBar() {
    myBottomBarView = new BottomBarView(GRID_WIDTH,
        GRID_HEIGHT * 2);
    myBottomBarView.setLayoutX(MARGIN);
    myBottomBarView.setLayoutY(GRID_HEIGHT + 1.5 * MARGIN);
    myBottomBarView.setMaxWidth(GRID_WIDTH - 2 * MARGIN);
    myRoot.getChildren().add(myBottomBarView);
  }

  /**
   * Handle whether grid lines should be shown or not
   *
   * @param selected: Whether to show grid lines
   */
  public void setGridLines(boolean selected) {
    PreferencesController.setPreference("gridLines", String.valueOf(selected));
    gridLinesEnabled = selected;
    mySimulationView.setGridLines(selected);
  }

  /**
   * Get current grid number of rows
   *
   * @return The current grid's number of rows
   */
  public int getGridRows() {
    return myGrid.getRows();
  }

  /**
   * Get current grid number of columns
   *
   * @return The current grid's number of columns
   */
  public int getGridCols() {
    return myGrid.getCols();
  }

  /**
   * Update the grid to use the new shape specified by the new value Calls updateGridShape from
   * simulation view class providing the current cell states in the grid along with the cell shape
   * type to transition to
   *
   * @param value The CellShapeType to update to
   */
  public void updateGridShape(CellShapeType value) {
    List<CellUpdate> currentStates = new ArrayList<>();
    for (Iterator<Cell> it = myGrid.getCellIterator(); it.hasNext(); ) {
      Cell cell = it.next();
      currentStates.add(new CellUpdate(cell.getLocation(), cell));
    }
    mySimulationView.updateGridShape(currentStates, value);
    myCellShapeType = value;
  }

  /**
   * Compute the count of each state present in the current simulation grid
   *
   * @return A map where the string represents the state's name and an int value for the count
   */
  public Map<String, Integer> computeStateCounts() {
    Map<String, Integer> stateCounts = new HashMap<>();
    for (int row = 0; row < myGrid.getRows(); row++) {
      for (int col = 0; col < myGrid.getCols(); col++) {
        int state = myGrid.getCell(row, col).getState();
        stateCounts.put(getMessage((mySimulation.data().type() + "_NAME_" + state).toUpperCase()),
            stateCounts.getOrDefault(
                getMessage((mySimulation.data().type() + "_NAME_" + state).toUpperCase()), 0) + 1);
      }
    }
    return stateCounts;
  }

  /**
   * Get the current edge strategy type of this main controller
   *
   * @return The edge strategy type for the current simulation
   */
  public EdgeStrategyType getEdgeStrategyType() {
    return myEdgeStrategyType;
  }

  private void initializeGridWithCells() {
    for (int i = 0; i < myGrid.getRows(); i++) {
      for (int j = 0; j < myGrid.getCols(); j++) {
        int initialState = 0;
        String simulationType = mySimulation.data().type();
        Cell cell = SimulationConfig.getNewCell(i, j, initialState, simulationType);
        myGrid.addCell(cell);
      }
    }
  }

  // From the states list from the simulation get the next available state from a sorted order
  private int getNextAvailableState(Cell cell) {
    int numStates = mySimulation.rules().getNumberStates();
    int currentState = cell.getState();
    return (currentState + 1) % numStates;
  }

  private void updateSimulationFromFile(String filePath)
      throws SAXException, InvalidStateException, GridException, IOException, ParserConfigurationException {
    attemptGettingGridAndSimulationFromXMLHandler(filePath);
  }

  // I used ChatGPT to assist in refactoring this method
  private void attemptGettingGridAndSimulationFromXMLHandler(String filePath)
      throws SAXException, ParserConfigurationException, IOException, GridException, InvalidStateException {
    try {
      attemptUpdateFromFilePath(filePath);
    } catch (Exception e) {
      handleGridAndSimulationCreationExceptions(e);
      throw e;
    }
  }

  private void handleGridAndSimulationCreationExceptions(Exception e) {
    String errorMessageKey = getErrorMessageKey(e);
    mySidebarView.flashWarning(getMessage(errorMessageKey));
    LOGGER.warn(getMessage(errorMessageKey), e);
  }

  // I used ChatGPT to help in refactoring the getErrorMessageKey method to improve cyclomatic complexity
  private static final Map<Class<? extends Exception>, String> ERROR_MESSAGE_MAP = new HashMap<>();

  static {
    ERROR_MESSAGE_MAP.put(SAXException.class, "ERROR_FORMAT");
    ERROR_MESSAGE_MAP.put(ParserConfigurationException.class, "ERROR_PARSER");
    ERROR_MESSAGE_MAP.put(IOException.class, "ERROR_IO");
    ERROR_MESSAGE_MAP.put(NumberFormatException.class, "ERROR_NUMBER");
    ERROR_MESSAGE_MAP.put(NullPointerException.class, "ERROR_MISSING");
    ERROR_MESSAGE_MAP.put(GridException.class, "ERROR_GRID");
    ERROR_MESSAGE_MAP.put(InvalidStateException.class, "ERROR_STATE");
  }

  private static String getErrorMessageKey(Exception e) {
    return ERROR_MESSAGE_MAP.getOrDefault(e.getClass(), "ERROR_GENERAL");
  }


  private void attemptUpdateFromFilePath(String filePath)
      throws SAXException, IOException, ParserConfigurationException, GridException, InvalidStateException {
    myIterationCount = 0;
    XMLHandler xmlHandler = new XMLHandler(filePath);
    myGrid = xmlHandler.getGrid();
    myCellShapeType = xmlHandler.getCellShapeType();
    myEdgeStrategyType = xmlHandler.getEdgeStrategyType();
    updateSimulation(xmlHandler.getSim());
  }

  private void createOrUpdateSidebar() {
    if (mySidebarView == null) {
      initializeSidebar();
    } else {
      mySidebarView.update();
    }
  }

  private void createOrUpdateBottomBar() {
    if (myBottomBarView == null) {
      initializeBottomBar();
    } else {
      myBottomBarView.update();
    }
  }

  private void createNewMainViewAndUpdateViewContainer() {
    myMainViewContainer.getChildren().clear();
    mySimulationView = new SimulationView(GRID_WIDTH, GRID_HEIGHT,
        myGrid, myCellShapeType, this);
    mySimulationView.setGridLines(gridLinesEnabled);
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
    myIterationCount++;
    myBottomBarView.updateIterationCounter(myIterationCount);
    mySimulationView.step(myGrid, mySimulation);
    Map<String, Integer> stateCounts = computeStateCounts();
    myBottomBarView.updateStateChangeChart(stateCounts, mySimulation.data().type());
  }

  private void createMainContainerAndView() {
    myMainViewContainer.setPrefWidth(GRID_WIDTH + 2 * MARGIN);
    myMainViewContainer.setPrefHeight(GRID_HEIGHT + 2 * MARGIN);
    myMainViewContainer.setAlignment(Pos.CENTER);
    try {
      updateSimulationFromFile(FileChooserConfig.DEFAULT_SIMULATION_PATH);
    } catch (Exception e) {
      LOGGER.warn("Error loading the default simulation file: {}", e.getMessage());
    } // can ignore thrown exception since we already handled them earlier in the chain
  }

  /**
   * Update the grid edge type for the grid controlled by this class
   *
   * @param edgeStrategyType The new edge strategy you want to use
   */
  public void updateGridEdgeType(EdgeStrategyType edgeStrategyType) {
    myEdgeStrategyType = edgeStrategyType;
    myGrid.setEdgeStrategy(EdgeStrategyFactory.createEdgeStrategy(edgeStrategyType));
  }
}
