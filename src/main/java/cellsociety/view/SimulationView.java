package cellsociety.view;

import java.util.List;

import cellsociety.controller.MainController;
import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.simulation.Simulation;
import cellsociety.view.config.StateDisplayConfig;
import cellsociety.view.config.StateInfo;
import cellsociety.view.grid.GridView;
import cellsociety.view.grid.GridViewFactory;
import cellsociety.view.grid.GridViewFactory.CellShapeType;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Simulation view for the visualizing the simulation
 *
 * @author Owen Jennings
 */
public class SimulationView extends Group {

  private GridView myGridView;
  private Group gridContainer;
  private ScrollPane scrollPane;

  private final Simulation mySimulation;
  private final int myWidth;
  private final int myHeight;
  private final int myNumRows;
  private final int myNumColumns;
  private final MainController myMainController;
  private boolean myGridLinesEnabled = true;


  private double zoomFactor = 1.0;
  private static final double zoomIncrement = 0.08;
  private static final double minZoom = 0.5;
  private static final double maxZoom = 3.5;

  /**
   * Create a simulation view
   *
   * @param width         width of simulation view
   * @param height        height of simulation view
   * @param grid          initial grid of the simulation view
   * @param cellShapeType the type of the cell shape to create a grid with
   */
  public SimulationView(int width, int height, Grid grid, CellShapeType cellShapeType,
      MainController mainController) {
    super();
    myWidth = width;
    myHeight = height;
    myNumRows = mainController.getGridRows();
    myNumColumns = mainController.getGridCols();
    myMainController = mainController;
    mySimulation = mainController.getSimulation();
    initializeGrid(width, height, myNumRows, myNumColumns, grid, cellShapeType, mainController);

    this.setOnScroll(this::handleZoom);
  }

  private void initializeGrid(int width, int height, int numRows, int numCols, Grid grid,
      CellShapeType cellShapeType, MainController mainController) {
    myGridView = GridViewFactory.createCellView(cellShapeType, width, height, numRows, numCols,
        mainController);
    myGridView.setId("gridView");
    initializeInitialGridStates(numRows, numCols, grid);

    gridContainer = new Group(myGridView);
    gridContainer.setId("gridContainer");
    gridContainer.setTranslateX((double) (width - myGridView.getWidth()) / 2);
    gridContainer.setTranslateY((double) (height - myGridView.getHeight()) / 2);

    //ScrollPane keeps the gridcontainer in the bounds
    scrollPane = new ScrollPane(gridContainer);
    scrollPane.setPrefSize(width, height);
    scrollPane.setPannable(true);

    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    getChildren().add(scrollPane);

    myGridView.updateGridLinesColor();
  }

  /**
   * Perform a single step of the animation
   *
   * @param grid       Grid of the simulation
   * @param simulation Simulation that contains rules for updating
   */
  public void step(Grid grid, Simulation simulation) {
    List<CellUpdate> stateUpdates = grid.updateGrid(simulation);
    updateGridViewFromCellUpdateList(stateUpdates);
  }

  /**
   * Set the color of a cell in the grid
   *
   * @param row   The row of the cell you want to update
   * @param col   The column of the cell you want to update
   * @param color The new color you want to set
   */
  public void setColor(int row, int col, Paint color) {
    myGridView.setColor(row, col, color);
  }

  /**
   * Handle whether grid lines should be shown or not
   *
   * @param selected Whether to show grid lines
   */
  public void setGridLines(boolean selected) {
    myGridLinesEnabled = selected;
    myGridView.setGridLines(selected);
  }

  /**
   * Reset the grid line colors on theme change
   */
  public void updateGridLinesColor() {
    myGridView.updateGridLinesColor();
  }

  /**
   * Update the grid to use a new CellShapeType shape
   *
   * @param currentGridState A list of cell updates representing the current grid state.
   * @param value            The new CellShapeType value.
   */
  public void updateGridShape(List<CellUpdate> currentGridState, CellShapeType value) {
    this.getChildren().remove(gridContainer);
    myGridView = GridViewFactory.createCellView(value, myWidth,
        myHeight, myNumRows, myNumColumns, myMainController);
    updateGridViewFromCellUpdateList(currentGridState);
    myGridView.updateGridLinesColor();
    myGridView.setGridLines(myGridLinesEnabled);

    gridContainer = new Group(myGridView);
    getChildren().add(gridContainer);
  }

  private void initializeInitialGridStates(int numRows, int numCols, Grid grid) {
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        Cell nextCell = grid.getCell(row, col);
        int nextState = nextCell.getState();
        Color nextColor = StateDisplayConfig.getStateInfo(mySimulation, nextState).color();
        double nextOpacity = nextCell.getOpacity();
        myGridView.setColor(row, col, nextColor);
        myGridView.setOpacity(row, col, nextOpacity);
        myGridView.addCellTooltip(row, col, grid, mySimulation.data().type());
      }
    }
  }

  private void updateGridViewFromCellUpdateList(List<CellUpdate> stateUpdates) {
    for (CellUpdate stateUpdate : stateUpdates) {
      int nextState = stateUpdate.getState();
      StateInfo nextStateInfo = StateDisplayConfig.getStateInfo(mySimulation, nextState);
      Paint nextColor = nextStateInfo.color();
      double nextOpacity = stateUpdate.getNextCell().getOpacity();
      myGridView.setColor(stateUpdate.getRow(), stateUpdate.getCol(), nextColor);
      myGridView.setOpacity(stateUpdate.getRow(), stateUpdate.getCol(), nextOpacity);
    }
  }

  /**
   * Handles zooming behavior when the user scrolls
   *
   * @param event Scroll event that triggers zoom in or out
   */
  private void handleZoom(ScrollEvent event) {
    event.consume(); // Prevent ScrollPane from also scrolling

    double oldZoom = zoomFactor;

    if (event.getDeltaY() > 0) {
      zoomFactor = Math.min(maxZoom, zoomFactor + zoomIncrement);
    } else {
      zoomFactor = Math.max(minZoom, zoomFactor - zoomIncrement);
    }

    applyZoom(event.getSceneX(), event.getSceneY(), oldZoom);
  }

  //Had ChatGPT debug why the offset wasn't working

  /**
   * Applies the zoom effect to the GridView contents while keeping its actual size constant.
   */
  private void applyZoom(double zoomCenterX, double zoomCenterY, double oldZoom) {
    double scaleChange = zoomFactor / oldZoom;

    double dx = (zoomCenterX - scrollPane.getWidth() / 2) * (1 - scaleChange);
    double dy = (zoomCenterY - scrollPane.getHeight() / 2) * (1 - scaleChange);

    gridContainer.setScaleX(zoomFactor);
    gridContainer.setScaleY(zoomFactor);
    gridContainer.setTranslateX(gridContainer.getTranslateX() + dx);
    gridContainer.setTranslateY(gridContainer.getTranslateY() + dy);

    scrollPane.setVvalue(0.5);
    scrollPane.setHvalue(0.5);
  }

  /**
   * Method to reset the Grid view to be back to normal pr-zoom
   *
   */
  public void resetZoom() {
    gridContainer.setScaleX(1.0);
    gridContainer.setScaleY(1.0);

    gridContainer.setTranslateX((double) (myWidth - myGridView.getWidth()) / 2);
    gridContainer.setTranslateY((double) (myHeight - myGridView.getHeight()) / 2);

    scrollPane.setHvalue(0.5);
    scrollPane.setVvalue(0.5);
  }
}
