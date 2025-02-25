package cellsociety.controller;

import cellsociety.view.grid.GridViewFactory.CellShapeType;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.STEP_SPEED;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
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
  private Simulation mySimulation;
  private final SplashScreenView mySplashScreenView;
  private Grid myGrid;
  private CellShapeType myCellShapeType = CellShapeType.RECTANGLE;
  private final VBox myMainViewContainer = new VBox();
  private final Timeline mySimulationAnimation = new Timeline();
  private boolean isEditing = false;
  private boolean gridLinesEnabled = Boolean.parseBoolean(
      PreferencesController.getPreference("gridLines", "true"));

  private final ThemeController myThemeController;

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
  }

  /**
   * For testing, get the sidebar view from main controller
   */
  public SidebarView getSidebarView() {
    return mySidebarView;
  }

  /**
   * For testing, get the splash screen view from main controller
   *
   * @return The splash screen view
   */
  public SplashScreenView getSplashScreen() {
    return mySplashScreenView;
  }

  /**
   * For testing, get the simulation view from main controller
   *
   * @return: a simulation view
   */
  public SimulationView getSimulationView() {
    return mySimulationView;
  }

  /**
   * Hide the splash screen view
   */
  public void hideSplashScreen() {
    myRoot.getChildren().remove(mySidebarView);
    mySidebarView = null; // ensure fresh initialization of sidebar in case of language change
    createOrUpdateSidebar();
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
        .add(new KeyFrame(Duration.seconds(speed), _ -> {
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

  public void createNewSimulation(int rows, int cols, String type, SimulationMetaData metaData,
      Map<String, Parameter<?>> parameters) throws InvalidParameterException {
    myGrid = new Grid(rows, cols);
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
    XMLWriter.saveSimulationToXML(mySimulation, myGrid, myCellShapeType, myStage);
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

  private void initializeGridWithCells()
      throws InvalidParameterException {
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

  private void attemptGettingGridAndSimulationFromXMLHandler(String filePath)
      throws SAXException, ParserConfigurationException, IOException, GridException, InvalidStateException {
    try {
      XMLHandler xmlHandler = new XMLHandler(filePath);
      myGrid = xmlHandler.getGrid();
      myCellShapeType = xmlHandler.getCellShapeType();
      updateSimulation(xmlHandler.getSim());
    } catch (SAXException e) {
      mySidebarView.flashWarning(getMessage("ERROR_FORMAT"));
      throw e;
    } catch (ParserConfigurationException e) {
      mySidebarView.flashWarning(getMessage("ERROR_PARSER"));
      throw e;
    } catch (IOException e) {
      mySidebarView.flashWarning(getMessage("ERROR_IO"));
      throw e;
    } catch (NumberFormatException e) {
      mySidebarView.flashWarning(getMessage("ERROR_NUMBER"));
      throw e;
    } catch (NullPointerException e) {
      mySidebarView.flashWarning(getMessage("ERROR_MISSING"));
      throw e;
    } catch (GridException e) {
      mySidebarView.flashWarning(getMessage("ERROR_GRID"));
      throw e;
    } catch (InvalidStateException e) {
      mySidebarView.flashWarning(getMessage("ERROR_STATE"));
      throw e;
    } catch (Exception e) {
      mySidebarView.flashWarning(getMessage("ERROR_GENERAL"));
      if (VERBOSE_ERROR_MESSAGES) {
        mySidebarView.flashWarning(e.getMessage());
      }
      throw e;
    }
  }

  private void createOrUpdateSidebar() {
    if (mySidebarView == null) {
      initializeSidebar();
    } else {
      mySidebarView.update();
    }
  }

  private void createNewMainViewAndUpdateViewContainer() {
    myMainViewContainer.getChildren().clear();
    mySimulationView = new SimulationView(GRID_WIDTH, GRID_HEIGHT, myGrid.getRows(),
        myGrid.getCols(),
        myGrid, myCellShapeType, mySimulation, this);
    mySimulationView.setGridLines(gridLinesEnabled);
    myMainViewContainer.getChildren().add(mySimulationView);
  }

  private void initializeSimulationAnimation() {
    mySimulationAnimation.setCycleCount(Timeline.INDEFINITE);
    mySimulationAnimation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(STEP_SPEED), _ -> {
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
    try {
      updateSimulationFromFile(FileChooserConfig.DEFAULT_SIMULATION_PATH);
    } catch (Exception _) {
    } // can ignore thrown exception since we already handled them earlier in the chain
  }
}
