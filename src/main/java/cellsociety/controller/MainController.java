package cellsociety.controller;

import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.STEP_SPEED;
import static cellsociety.config.MainConfig.WIDTH;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationData;
import cellsociety.model.simulation.types.GameOfLife;
import cellsociety.model.simulation.types.GameOfLifeRules;
import cellsociety.view.SidebarView;
import cellsociety.view.SimulationView;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * A class to handle the main interactions between the model and view classes
 * 
 * @author Owen Jennings
 */
public class MainController {

  private final Group myRoot;
  private SimulationView myMainView;
  private Simulation mySimulation;
  private Grid myGrid;
  private boolean isPlaying = false;
  private double simulationSpeedMultiplier;

  /**
   * Initialize the MainController
   * @param root: the main root group of the program
   */
  public MainController(Group root) {
    myRoot = root;
    simulationSpeedMultiplier = 1;
    createMainContainerAndView();
    initializeSidebar(this);
    initializeStepAnimation();
  }

  /**
   * Update the isPlaying parameter which determines whether simulation should be animated
   * @param isPlaying: the new parameter for isPlaying
   */
  public void setIsPlaying(boolean isPlaying) {
    this.isPlaying = isPlaying;
  }

  /**
   * Returns the current simulation object
   * @return The currently running simulation object
   */
  public Simulation getSimulation() {
    return mySimulation;
  }

  private void initializeStepAnimation() {
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(STEP_SPEED), e -> {
          try {
            step();  // step function
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }));
    animation.play();
  }

  public void step() {
    if (isPlaying) {
      myMainView.step(myGrid, mySimulation);
      myGrid.updateGrid(mySimulation);
    }
  }

  private void createMainContainerAndView() {
    VBox mainContainer = new VBox();
    mainContainer.setPrefWidth(GRID_WIDTH + 2 * MARGIN);
    mainContainer.setPrefHeight(GRID_HEIGHT + 2 * MARGIN);
    mainContainer.setAlignment(Pos.CENTER);

    int numRows = 50, numCols = 50;
    createGOFModel(numRows, numCols); // sample game for now, not reading from file
    myMainView = new SimulationView(GRID_WIDTH, GRID_HEIGHT, numRows, numCols);
    mainContainer.getChildren().add(myMainView);
    myRoot.getChildren().add(mainContainer);
  }
  
  private void initializeSidebar(MainController controller) {
    SidebarView sidebar = new SidebarView(WIDTH - GRID_WIDTH - (3 * MARGIN),
        GRID_HEIGHT - (2 * MARGIN), controller);
    sidebar.setLayoutX(GRID_WIDTH + 2 * MARGIN);
    sidebar.setLayoutY(MARGIN);
    myRoot.getChildren().add(sidebar);
  }


  private void createGOFModel(int numRows, int numCols) {
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
        new SimulationData("Game of Life", "Glider", "Richard K. Guy", "A basic configuration that produces a \"glider\" that moves diagonally across the grid using the Game of Life simulation conditions.", new ArrayList<>())
    );
  }
}
