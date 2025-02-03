package cellsociety.view;

import cellsociety.model.simulation.Simulation;
import java.util.Map;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    this.setSpacing(10);
    if (!simulation.getRules().getParameters().isEmpty()) {
      createTitle();
      createParameters(simulation);
    }
  }

  private void createTitle() {
    createText("Parameters:", 20);
  }

  private void createParameters(Simulation simulation) {
    Map<String, Double> parameters = simulation.getRules().getParameters();
    for (Map.Entry<String, Double> entry : parameters.entrySet()) {
      createText("- " + entry.getKey() + ": " + entry.getValue(), 14);
    }
  }

  private void createText(String entry, int size) {
    Text parameter = new Text(entry);
    parameter.setFont(new Font("Arial", size));
    this.getChildren().add(parameter);
  }
}
