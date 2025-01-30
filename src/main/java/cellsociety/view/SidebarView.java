package cellsociety.view;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationData;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A class to handle all the sidebar view elements of the app
 *
 * @author Owen Jennings
 */
public class SidebarView extends VBox {

  public static final double ELEMENT_SPACING = 5;

  private int myWidth;
  private Simulation mySimulation;

  /**
   * Create a sidebar view with a preferred size of width x height
   *
   * @param width:  preferred width of sidebar box
   * @param height: preferred height of sidebar box
   */
  public SidebarView(int width, int height, Simulation simulation) {
    this.setPrefSize(width, height);
    this.setAlignment(Pos.TOP_LEFT);
    this.setSpacing(ELEMENT_SPACING);
    myWidth = width;
    mySimulation = simulation;
    initializeSidebar();
  }

  private void initializeSidebar() {
    initializeStaticContent();
    initializeSimulationDataDisplay();
  }

  private void initializeSimulationDataDisplay() {
    SimulationData simulationData = mySimulation.getData();
    addTextToSidebar("Name: " + simulationData.getName(), 14, TextAlignment.LEFT);
    addTextToSidebar("Type: " + simulationData.getType(), 14, TextAlignment.LEFT);
    addTextToSidebar("Author: " + simulationData.getAuthor(), 14, TextAlignment.LEFT);
    addTextToSidebar("Current Simulation Information: " + simulationData.getDescription(), 14,
        TextAlignment.LEFT);
  }

  private void initializeStaticContent() {
    addTextToSidebar("Cellular Automaton", 20, TextAlignment.CENTER);
    addTextToSidebar("Current Simulation Information: ", 18, TextAlignment.LEFT);
  }

  private void addTextToSidebar(String message, double size, TextAlignment align) {
    Text title = createText(message, size, align);
    this.getChildren().add(title);
  }

  private Text createText(String message, double size, TextAlignment align) {
    Text text = new Text(message);
    text.setFont(new Font("Arial", size));
    text.setFill(Color.BLACK);
    text.setTextAlignment(align);
    text.setWrappingWidth(myWidth);
    return text;
  }
}
