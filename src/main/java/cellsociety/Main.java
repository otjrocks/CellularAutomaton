package cellsociety;

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
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main file for application
 */
public class Main extends Application {

  public static final String TITLE = "Cell Society";
  public static final int WIDTH = 1400;
  public static final int HEIGHT = 800;
  public static final Paint BACKGROUND_COLOR = Color.LIGHTGRAY;
  public static final int MARGIN = 20;
  public static final int GRID_WIDTH = (2 * WIDTH) / 3;
  public static final int GRID_HEIGHT = HEIGHT - (2 * MARGIN);
  public static final double STEP_SPEED = 0.05;

  private final Group root = new Group();
  private SimulationView myMainView;
  private Simulation mySimulation;
  private Grid myGrid;

  /**
   * Initialize what will be displayed.
   */
  @Override
  public void start(Stage stage) {
    initializeStage(stage);
    initializeStepAnimation();
    initializeSidebar(mySimulation);
  }

  private void initializeSidebar(Simulation simulation) {
    SidebarView sidebar = new SidebarView(WIDTH - GRID_WIDTH - (3 * MARGIN), GRID_HEIGHT - (2 * MARGIN), simulation);
    sidebar.setLayoutX(GRID_WIDTH + 2 * MARGIN);
    sidebar.setLayoutY(MARGIN);
    root.getChildren().add(sidebar);
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

  private void initializeStage(Stage stage) {
    ScrollPane scrollPane = createMainScrollPane();
    createMainContainerAndView();

    Scene scene = new Scene(scrollPane, WIDTH, HEIGHT, BACKGROUND_COLOR);
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();
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
    root.getChildren().add(mainContainer);
  }

  private ScrollPane createMainScrollPane() {
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);
    scrollPane.setContent(root);
    return scrollPane;
  }

  public void step() {
    myMainView.step(myGrid, mySimulation);
    myGrid.updateGrid(mySimulation);
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
        new SimulationData("GOL", "GOL", "Author", "description", new ArrayList<>())
    );
  }
}
