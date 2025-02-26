package cellsociety.view;

import static cellsociety.config.MainConfig.getCellColors;
import static org.mockito.Mockito.reset;

import cellsociety.controller.MainController;
import cellsociety.utility.CreateNewSimulation;
import cellsociety.view.cell.CellView;
import cellsociety.view.grid.GridView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.DukeApplicationTest;

class GridViewTest extends DukeApplicationTest {

  private MainController myMainController;
  private MainController myMainControllerSpy;
  private GridView myGridView;
  private static final Color deadColor = Color.web(getCellColors().getString("GAMEOFLIFE_COLOR_0"));
  private static final Color aliveColor = Color.web(
      getCellColors().getString("GAMEOFLIFE_COLOR_1"));

  @Override
  public void start(Stage stage) {
    CreateNewSimulation createNewSimulation = new CreateNewSimulation();
    myMainController = createNewSimulation.launchNewSimulation();
    myMainController.hideSplashScreen(); // hide splash screen to jump directly to main screen
    myMainControllerSpy = Mockito.spy(myMainController);
    myGridView = lookup("#gridView").query();
  }

  @BeforeEach
  public void setup() {
    reset(myMainControllerSpy);
  }

  @Test
  public void gridView_EnsureCorrectDefaultSimulationSetup_Success() throws InterruptedException {
    // default simulation is a Game of Life simulation
    // ensure that the default simulation has cells colored correctly for a glider
    // use Color.web() to convert color string constant to hex value
    // ensure glider is set up correctly
    assertCellColorEquals(myGridView.getCell(0, 0), deadColor);
    assertCellColorEquals(myGridView.getCell(0, 1), aliveColor);
    assertCellColorEquals(myGridView.getCell(0, 2), deadColor);
    assertCellColorEquals(myGridView.getCell(1, 0), deadColor);
    assertCellColorEquals(myGridView.getCell(1, 1), deadColor);
    assertCellColorEquals(myGridView.getCell(1, 2), aliveColor);
    assertCellColorEquals(myGridView.getCell(2, 0), aliveColor);
    assertCellColorEquals(myGridView.getCell(2, 1), aliveColor);
    assertCellColorEquals(myGridView.getCell(2, 2), aliveColor);
  }

  @Test
  public void gridView_CheckCellClickStatusChange_FailureNotInEditMode() {
    assertCellColorEquals(myGridView.getCell(0, 0), deadColor);
    clickOn(myGridView.getCell(0, 0));
    assertCellColorEquals(myGridView.getCell(0, 0), deadColor);
  }

  @Test
  public void gridView_CheckCellClickStatusChange_Success() {
    clickOn("#sidebarModeButton");
    assertCellColorEquals(myGridView.getCell(0, 0), deadColor);
    clickOn(myGridView.getCell(0, 0));
    // state should switch to alive after click to update state
    assertCellColorEquals(myGridView.getCell(0, 0), aliveColor);
  }

  @Test
  public void gridView_CheckCellClickCyclesThroughAllStates_Success() {
    clickOn("#sidebarModeButton");
    assertCellColorEquals(myGridView.getCell(1, 1), deadColor);
    clickOn(myGridView.getCell(1, 1));
    // state should switch to alive after click to update state
    assertCellColorEquals(myGridView.getCell(1, 1), aliveColor);
    clickOn(myGridView.getCell(1, 1));
    // state should cycle back to original state since all states (2) have been visited for game of life
    assertCellColorEquals(myGridView.getCell(1, 1), deadColor);
    clickOn(myGridView.getCell(1, 1));
    assertCellColorEquals(myGridView.getCell(1, 1), aliveColor);
  }

  private void assertCellColorEquals(CellView cellView, Color expected) {
    Assertions.assertEquals(expected, cellView.getFill());
  }


}