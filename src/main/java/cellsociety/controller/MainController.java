package cellsociety.controller;

import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.STEP_SPEED;
import static cellsociety.config.MainConfig.WIDTH;

import cellsociety.config.FileChooserConfig;
import cellsociety.model.Grid;
import cellsociety.model.XMLHandlers.XMLDefiner;
import cellsociety.model.XMLHandlers.XMLHandler;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationData;
import cellsociety.model.simulation.types.GameOfLife;
import cellsociety.model.simulation.rules.GameOfLifeRules;
import cellsociety.view.SidebarView;
import cellsociety.view.SimulationView;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

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

  /**
   * Initialize the MainController
   *
   * @param root: the main root group of the program
   */
  public MainController(Stage stage, Group root) {
    myStage = stage;
    myRoot = root;
    createMainContainerAndView();
    initializeSidebar(this);
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

  public void handleNewSimulationFromFile() {
    stopAnimation(); // stop animation if it is currently running
    String filePath = FileChooserConfig.FILE_CHOOSER.showOpenDialog(myStage).getAbsolutePath();
    XMLHandler xmlHandler = null;
    try {
      xmlHandler = XMLDefiner.createHandler(filePath);
    } catch (SAXException | ParserConfigurationException | IOException e) {
      throw new RuntimeException(e);
    }
    
    mySimulation = xmlHandler.getSim();
    myGrid = xmlHandler.getGrid();
    createNewMainViewAndUpdateViewContainer();
    mySidebarView.updateSidebar();
  }

  private void createNewMainViewAndUpdateViewContainer() {
    myMainViewContainer.getChildren().remove(mySimulationView);
    mySimulationView = new SimulationView(GRID_WIDTH, GRID_HEIGHT, myGrid.getRows(), myGrid.getCols(),
        myGrid, mySimulation);
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
    myGrid.updateGrid(mySimulation);
  }

  private void createMainContainerAndView() {
    myMainViewContainer.setPrefWidth(GRID_WIDTH + 2 * MARGIN);
    myMainViewContainer.setPrefHeight(GRID_HEIGHT + 2 * MARGIN);
    myMainViewContainer.setAlignment(Pos.CENTER);

    int numRows = 50, numCols = 50;
    createInitialSimulation(numRows, numCols); // sample game for now, not reading from file
    mySimulationView = new SimulationView(GRID_WIDTH, GRID_HEIGHT, numRows, numCols, myGrid,
        mySimulation);
    myMainViewContainer.getChildren().add(mySimulationView);
    myRoot.getChildren().add(myMainViewContainer);
  }

  private void initializeSidebar(MainController controller) {
    mySidebarView = new SidebarView(WIDTH - GRID_WIDTH - (3 * MARGIN),
        GRID_HEIGHT - (2 * MARGIN), controller);
    mySidebarView.setLayoutX(GRID_WIDTH + 2 * MARGIN);
    mySidebarView.setLayoutY(MARGIN);
    myRoot.getChildren().add(mySidebarView);
  }


  private void createInitialSimulation(int numRows, int numCols) {
    myGrid = new Grid(numRows, numCols);

    // Add a glider pattern: Asked ChatGPT for helping make the glider for the simulation
    myGrid.addCell(new DefaultCell(1, new Point2D.Double(1, 0)));
    myGrid.addCell(new DefaultCell(1, new Point2D.Double(2, 1)));
    myGrid.addCell(new DefaultCell(1, new Point2D.Double(0, 2)));
    myGrid.addCell(new DefaultCell(1, new Point2D.Double(1, 2)));
    myGrid.addCell(new DefaultCell(1, new Point2D.Double(2, 2)));
    // Initialize every point in the grid to a DefaultCell with state 0
    for (int x = 0; x < numRows; x++) {
      for (int y = 0; y < numCols; y++) {
        myGrid.addCell(new DefaultCell(0, new Point2D.Double(x, y)));
      }
    }

    mySimulation = new GameOfLife(
        new GameOfLifeRules(),
        new SimulationData("Game of Life", "Glider", "Richard K. Guy",
            "A basic configuration that produces a \"glider\" that moves diagonally across the grid using the Game of Life simulation conditions.",
            new ArrayList<>())
    );
  }
}
