package cellsociety.view;

import static cellsociety.config.MainConfig.getMessage;
import cellsociety.model.simulation.Simulation;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.view.cell.CellView;
import cellsociety.view.cell.RectangleCellView;
import cellsociety.view.config.StateDisplayConfig;
import cellsociety.view.config.StateInfo;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.w3c.dom.css.Rect;

/**
 * A view to handle the current simulation's state information panel, which is displayed on the
 * sidebar. The view is a list that shows the states that correspond to a given colored cell.
 *
 * @author Owen Jennings
 */
public class StateInfoView extends VBox {

  /**
   * Create a new state info view for the current simulation
   *
   * @param simulation: The simulation that is currently running
   */
  public StateInfoView(Simulation simulation) {
    super();
    this.setSpacing(ELEMENT_SPACING);
    this.getStyleClass().add("info-box");
    createTitle();
    for (int i = 0; i < simulation.rules().getNumberStates(); i++) {
      createListItemFromStateInfo(StateDisplayConfig.getStateInfo(simulation, i));
    }
  }

  private void createTitle() {
    Text title = new Text(getMessage("STATE_INFO_TITLE"));
    title.getStyleClass().add("secondary-title");
    this.getChildren().add(title);
  }

  private void createListItemFromStateInfo(StateInfo stateInfo) {
    HBox box = new HBox();
    box.setAlignment(Pos.CENTER_LEFT);
    box.setSpacing(10);
    CellView dummyCell = new RectangleCellView(0, 0, 20, 20);
    dummyCell.setFill(stateInfo.color());
    Text stateInfoText = new Text(stateInfo.displayName());
    box.getChildren().add(dummyCell);
    box.getChildren().add(stateInfoText);
    this.getChildren().add(box);
  }


}
