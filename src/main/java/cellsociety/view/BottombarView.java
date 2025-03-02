package cellsociety.view;

import java.util.HashMap;
import java.util.Map;

import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.getCellColors;
import static cellsociety.config.MainConfig.getMessage;
import cellsociety.config.SimulationConfig;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 * A class to handle all the BottomBar view elements of the app
 *
 * @author Troy Ludwig
 */
public class BottomBarView extends VBox {

  public static final double ELEMENT_SPACING = 10;
  private final Text myIterationText = createText(getMessage("ITERATOR_TEXT") + "0");
  private LineChart<Number, Number> stateChangeChart;
  private final Map<String, XYChart.Series<Number, Number>> stateChangeSeriesMap = new HashMap<>();
  private int stepCount = 0;

  /**
   * Create a bottom bar view with a preferred size of width x height
   *
   * @param width:  preferred width of bottom bar box
   * @param height: preferred height of bottom bar box
   */
  public BottomBarView(int width, int height) {
    super();
    this.setPrefSize(width, height);
    this.setAlignment(Pos.TOP_LEFT);
    this.setSpacing(ELEMENT_SPACING);
    this.getStyleClass().add("bottombar");
    this.getChildren().add(myIterationText);
    setupStateChangeChart();
    update();
  }

  /**
   * update the bottom bar view, by resetting the iteration count and clearing the state change
   * chart
   */
  public void update() {
    updateIterationCounter(0);
    stateChangeChart.getData().clear();
  }

  private Text createText(String message) {
    Text text = new Text(message);
    text.setTextAlignment(TextAlignment.LEFT);
    text.setWrappingWidth(GRID_WIDTH - (ELEMENT_SPACING * 6));
    return text;
  }

  /**
   * update the iteration counter displayed on the bottom bar
   *
   * @param count The new count you want to display
   */
  public void updateIterationCounter(int count) {
    myIterationText.setText(getMessage("ITERATOR_TEXT") + count);
  }

  private void setupStateChangeChart() {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("Simulation Step");
    yAxis.setLabel("Population");

    stateChangeChart = new LineChart<>(xAxis, yAxis);
    stateChangeChart.setTitle("State Population Change Over Time");
    stateChangeChart.setMinWidth(GRID_WIDTH);
    stateChangeChart.setMaxWidth(GRID_WIDTH);

    stateChangeChart.setMaxHeight(GRID_HEIGHT);

    getChildren().add(stateChangeChart);
  }

  /**
   * Update the state change chart
   *
   * @param stateCounts The count of all states from the current simulation state
   * @param simType     The simulation type string
   */
  public void updateStateChangeChart(Map<String, Integer> stateCounts, String simType) {
    for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
      String stateName = entry.getKey();
      int newValue = entry.getValue();
      addStateNameToStateChangeMap(stateName);
      stateChangeSeriesMap.get(stateName).getData().add(new XYChart.Data<>(stepCount, newValue));
    }

    updateXYChart(simType);

    stepCount++;
  }

  private void updateXYChart(String simType) {
    Platform.runLater(() -> {
      for (XYChart.Series<Number, Number> series : stateChangeChart.getData()) {
        String colorString = getColorStringForState(simType, series);
        series.getNode().setStyle("-fx-stroke: " + colorString + ";");
        updateLegendNodeColor(series, colorString);
      }
    });
  }

  private static String getColorStringForState(String simType, Series<Number, Number> series) {
    return getCellColors().getString(
        (simType + "_COLOR_" + SimulationConfig.returnStateValueBasedOnName(simType,
            series.getName().replaceAll("\\s+", ""))).toUpperCase()).toLowerCase();
  }

  private void updateLegendNodeColor(Series<Number, Number> series, String colorString) {
    for (Node legendNode : stateChangeChart.lookupAll(".chart-legend-item-symbol")) {
      if (legendNode != null && series.getName()
          .equals(legendNode.getAccessibleText())) {
        legendNode.setStyle("-fx-background-color: " + colorString + ", white;");
      }
    }
  }

  private void addStateNameToStateChangeMap(String stateName) {
    if (!stateChangeSeriesMap.containsKey(stateName)) {
      XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
      newSeries.setName(stateName);
      stateChangeSeriesMap.put(stateName, newSeries);
      stateChangeChart.getData().add(newSeries);
    }
  }

}