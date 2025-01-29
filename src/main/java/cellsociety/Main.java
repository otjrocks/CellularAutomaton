package cellsociety;

import cellsociety.model.Grid;
import cellsociety.model.cell.DefaultCell;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationData;
import cellsociety.model.simulation.types.GameOfLife;
import cellsociety.model.simulation.types.GameOfLifeRules;
import cellsociety.view.MainView;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * Main file for application
 */
public class Main extends Application {

  public static final String TITLE = "Cell Society";
  public static final int WIDTH = 1200;
  public static final int HEIGHT = 800;
  public static final int GRID_WIDTH = 200;
  public static final int GRID_HEIGHT = 200;
  public static final Paint BACKGROUND_COLOR = Color.LIGHTGRAY;
  public static final int MARGIN = 10;

  private final Group root = new Group();

  /**
   * Initialize what will be displayed.
   */
  @Override
  public void start(Stage stage) {
    Grid grid = new Grid(10, 10);
    grid.addCell(new DefaultCell(1, new Point2D.Double(0,0)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(0, 1)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 1)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(1, 0)));
    grid.addCell(new DefaultCell(1, new Point2D.Double(2, 0)));
    GameOfLife simulation = new GameOfLife(new GameOfLifeRules(), new SimulationData("GOL", "GOL", "Author", "description", new ArrayList<>()));
    grid.printGrid();
    grid.updateGrid(simulation);
    System.out.println("\n\n");
    grid.printGrid();
//    Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND_COLOR);
//    stage.setScene(scene);
//    stage.setTitle(TITLE);
//    stage.show();
//
//    MainView mainView = new MainView(GRID_WIDTH, GRID_HEIGHT);
//    mainView.setLayoutY((double) (HEIGHT - GRID_HEIGHT) / 2);
//    mainView.setLayoutX(MARGIN);
//    root.getChildren().add(mainView);
  }

}
