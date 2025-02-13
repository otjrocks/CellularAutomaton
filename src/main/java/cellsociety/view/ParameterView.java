package cellsociety.view;

import static cellsociety.config.MainConfig.getMessages;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.model.simulation.Simulation;
import java.util.Map;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A view to display the parameters of a simulation
 *
 * @author Owen Jennings
 */
public class ParameterView extends VBox {

  /**
   * Create a new parameter view using the current parameter values of the provided simulation
   *
   * @param simulation: The simulation that is currently running
   */
  public ParameterView(Simulation simulation) {
    this.setSpacing(ELEMENT_SPACING);
    if (!simulation.rules().getParameters().isEmpty()) {
      this.getStyleClass().add("info-box");
      createTitle();
      createParameters(simulation);
    }
  }

  private void createTitle() {
    createText(getMessages().getString("PARAMETER_TITLE"), true);
  }

  private void createParameters(Simulation simulation) {
    Map<String, Double> parameters = simulation.rules().getParameters();
    for (Map.Entry<String, Double> entry : parameters.entrySet()) {
      createText(String.format("• %s%s %s", entry.getKey(), ": ", entry.getValue()), false);
    }
  }

  private void createText(String entry, boolean title) {
    Text parameter = new Text(entry);
    if (title) {
      parameter.getStyleClass().add("secondary-title");
    }
    this.getChildren().add(parameter);
  }

}
