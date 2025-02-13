package cellsociety.controller;

import cellsociety.view.SplashScreenView;
import cellsociety.view.components.AlertField;
import cellsociety.view.config.StateDisplayConfig;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import static cellsociety.config.MainConfig.STEP_SPEED;
import cellsociety.config.SimulationConfig;
import cellsociety.model.Grid;
import cellsociety.model.XMLHandlers.GridException;
import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.XMLHandlers.XMLWriter;
import cellsociety.model.cell.Cell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.SidebarView;
import cellsociety.view.SimulationView;
import cellsociety.view.config.FileChooserConfig;
import cellsociety.view.config.StateDisplayConfig;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.text.View;

/**
 * A class to handle the main interactions between the model and view classes
 *
 * @author Owen Jennings
 */
public class MainController {

  private final Group myRoot;
  private final Stage myStage;
  private final ViewController myViewController;
  private SimulationView mySimulationView;
  private Simulation mySimulation;
  private final SplashScreenView mySplashScreenView;
  private Grid myGrid;
  Timeline mySimulationAnimation = new Timeline();
  private boolean isEditing = false;

  private final ThemeController myThemeController;

  /**
   * Initialize the MainController
   *
   * @param root: the main root group of the program
   */
  public MainController(Stage stage, Group root, ViewController viewController) {
    this.myViewController = viewController;
    myThemeController = new ThemeController(stage);
    myStage = stage;
    myRoot = root;
    mySplashScreenView = new SplashScreenView(new AlertField(), this);
    root.getChildren().add(mySplashScreenView);
    initializeSimulationAnimation();
    myRoot.getChildren().remove(mySidebarView);
  }

  /**
   * Hide the splash screen view
   */
  public void hideSplashScreen() {
    myRoot.getChildren().remove(mySplashScreenView);
    myRoot.getChildren().add(myMainViewContainer);
    myRoot.getChildren().add(mySidebarView);
  }

  /**
   * Set the theme to the themeName provided if it exists, otherwise fallback to default theme
   *
   * @param themeName: Name of theme you which to set
   */
  public void setTheme(String themeName) {
    myThemeController.setTheme(themeName);
  }

  public void setSimulationView(SimulationView simulationView) {
    this.mySimulationView = simulationView;
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
   * Returns the current Grid object
   *
   * @return - the active grid object
   */
  public Grid getGrid() {
    return myGrid;
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
   * Get whether the grid animation is currently playing
   *
   * @return true if the animation is playing, false otherwise
   */
  public boolean isPlaying() {
    return mySimulationAnimation.getStatus() == Status.RUNNING;
  }

  public void createNewSimulation(int rows, int cols, String type, SimulationMetaData metaData,
      Map<String, String> parameters) {
    myGrid = new Grid(rows, cols);
    mySimulation = SimulationConfig.getNewSimulation(type, metaData, parameters);
    initializeGridWithCells();
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
      myGrid.setState(row, column, nextState, mySimulation);
      // create new cell instead of just updating cell status, to ensure that new cell has all other information reset for custom cell types
      myGrid.updateCell(
          SimulationConfig.getNewCell(row, column, nextState, mySimulation.data().type()));
      myViewController.updateColorForEditMode(row, column, StateDisplayConfig.getStateInfo(mySimulation, nextState).color());
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
      myViewController.updateSimulationFromFile(filePath);
    }
  }

  public void handleSavingToFile() {
    XMLWriter.saveSimulationToXML(mySimulation, myGrid, myStage);
  }

  private void updateSimulationFromFile(String filePath) {
    try{
      XMLHandler xmlHandler = new XMLHandler(filePath);

      mySimulation = xmlHandler.getSim();
      myGrid = xmlHandler.getGrid();
      createNewMainViewAndUpdateViewContainer();
      createOrUpdateSidebar();
      } catch (SAXException e) {
        mySidebarView.flashWarning("Malformed XML file. Please check the formatting.");
      } catch (ParserConfigurationException e) {
        mySidebarView.flashWarning("XML parser configuration issue.");
      } catch (IOException e) {
        mySidebarView.flashWarning("Unable to read the file. Check permissions and file path.");
      } catch (NumberFormatException e) {
        mySidebarView.flashWarning("Incorrect data format found in XML. Expected numerical values.");
      } catch (NullPointerException e) {
        mySidebarView.flashWarning("Missing required data field. Please add required fields.");
      } catch (GridException e) {
        mySidebarView.flashWarning("Grid values out of bounds. Please adjust initialization configuration.");
      } catch (Exception e) {
        mySidebarView.flashWarning("Unexpected issue while parsing the XML file.");
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
        myGrid, mySimulation, this);
    mySimulationView.setGridLines(gridLinesEnabled);
    myMainViewContainer.getChildren().add(mySimulationView);
  }
>>>>>>> src/main/java/cellsociety/controller/MainController.java

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
    myViewController.updateSimulationView(myGrid, mySimulation);
  }

  private void createMainContainerAndView() {
    myMainViewContainer.setPrefWidth(GRID_WIDTH + 2 * MARGIN);
    myMainViewContainer.setPrefHeight(GRID_HEIGHT + 2 * MARGIN);
    myMainViewContainer.setAlignment(Pos.CENTER);
    updateSimulationFromFile(FileChooserConfig.DEFAULT_SIMULATION_PATH);
  }

  public void initializeSidebar() {
    mySidebarView = new SidebarView(SIDEBAR_WIDTH,
        GRID_HEIGHT - (2 * MARGIN), this);
    mySidebarView.setLayoutX(GRID_WIDTH + 1.5 * MARGIN);
    mySidebarView.setLayoutY(MARGIN);
    myRoot.getChildren().add(mySidebarView);
  }

  public void clearSidebar(MainController controller) {
    mySidebarView.clearSidebar();
  }

  /**
   * Handle whether grid lines should be shown or not
   *
   * @param selected: Whether to show grid lines
   */
  public void setGridLines(boolean selected) {
    gridLinesEnabled = selected;
    mySimulationView.setGridLines(selected);
>>>>>>> src/main/java/cellsociety/controller/MainController.java
  }
}
