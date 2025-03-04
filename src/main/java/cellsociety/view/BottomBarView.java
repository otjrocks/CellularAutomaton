package cellsociety.view;

import java.util.HashMap;
import java.util.Map;

import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.MARGIN;
import static cellsociety.config.MainConfig.getCellColors;
import static cellsociety.config.MainConfig.getMessage;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.config.SimulationConfig;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/**
 * A class to handle all the BottomBar view elements of the app
 *
 * @author Troy Ludwig
 */
public class BottomBarView extends VBox {

  private static final int MAX_HISTORY_SIZE = 300;
  private final Text myIterationText = new Text();
  private LineChart<Number, Number> stateChangeChart;
  private final Map<String, Series<Number, Number>> stateChangeSeriesMap = new HashMap<>();
  private final NumberAxis xAxis = new NumberAxis();
  private final NumberAxis yAxis = new NumberAxis();
  private int stepCount = 0;
  private final String myIterationCountLabel;
  private volatile int latestCount = 0;


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
    myIterationCountLabel = getMessage("ITERATOR_TEXT");
    myIterationText.setLayoutX(width - 2 * MARGIN);
    myIterationText.setLayoutY(height - 2 * MARGIN);
    // I asked ChatGPT for assistance in scheduling the text UI updates to improve efficiency.
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(
        () -> Platform.runLater(() -> myIterationText.setText(myIterationCountLabel + latestCount)),
        0, 200, TimeUnit.MILLISECONDS);
    this.getChildren().add(myIterationText);
    setupStateChangeChart();
    update();
  }

  /**
   * update the bottom bar view, by resetting the iteration count and clearing the state change
   * chart
   */
  public void update() {
    stateChangeChart.getData().clear();
    stateChangeSeriesMap.clear();
    updateIterationCounter(0);
    stepCount = 0;
    xAxis.setLowerBound(0);
    xAxis.setUpperBound(MAX_HISTORY_SIZE);
  }

  /**
   * update the iteration counter displayed on the bottom bar
   *
   * @param count The new count you want to display
   */

  public void updateIterationCounter(int count) {
    latestCount = count;
  }

  private void setupStateChangeChart() {
    setupXAxis();
    setupYAxis();
    setupChart();
    getChildren().add(stateChangeChart);
  }

  private void setupChart() {
    stateChangeChart = new LineChart<>(xAxis, yAxis);
    stateChangeChart.setCreateSymbols(false);
    stateChangeChart.setAnimated(true);
    stateChangeChart.setTitle(getMessage("STATECHANGE_CHART"));
    stateChangeChart.setMinWidth(GRID_WIDTH);
    stateChangeChart.setMaxWidth(GRID_WIDTH);
    stateChangeChart.getStyleClass().add("state-change");
    stateChangeChart.setLegendVisible(false);
    stateChangeChart.setMinHeight(GRID_HEIGHT);
    stateChangeChart.setMaxHeight(GRID_HEIGHT);
  }

  private void setupYAxis() {
    yAxis.setLabel(getMessage("STATECHANGE_CHART_Y"));
    yAxis.setAutoRanging(false);
    yAxis.setLowerBound(0);
  }

  private void setupXAxis() {
    xAxis.setLabel(getMessage("STATECHANGE_CHART_X"));
    xAxis.setAutoRanging(false);
    xAxis.setMinorTickVisible(false);
    xAxis.setTickMarkVisible(false);
    xAxis.setTickLabelsVisible(false);
  }

  /**
   * Update the state change chart
   *
   * @param stateCounts The count of all states from the current simulation state
   * @param simType     The simulation type string
   */
  public void updateStateChangeChart(Map<String, Integer> stateCounts, String simType) {
    Platform.runLater(() -> {
      int totalStates = sumMapValues(stateCounts);
      yAxis.setUpperBound(totalStates);
      yAxis.setTickUnit((double) totalStates / 5);

      for (Entry<String, Integer> entry : stateCounts.entrySet()) {
        String stateName = entry.getKey();
        int newValue = entry.getValue();
        addStateNameToStateChangeMap(stateName);

        Series<Number, Number> series = stateChangeSeriesMap.get(stateName);
        series.getData().add(new Data<>(stepCount, newValue));

        // Ensure the series maintains a fixed history size
        if (series.getData().size() > MAX_HISTORY_SIZE) {
          series.getData().removeFirst(); // Remove the oldest data point
        }
        xAxis.setUpperBound(stepCount);
        if (stepCount > MAX_HISTORY_SIZE) {
          xAxis.setLowerBound(stepCount - MAX_HISTORY_SIZE + 1);
        }
      }

      updateXYChart(simType);
      stepCount++;
    });
  }


  private void updateXYChart(String simType) {
    for (Series<Number, Number> series : stateChangeChart.getData()) {
      String colorString = getColorStringForState(simType, series);
      series.getNode().setStyle("-fx-stroke: " + colorString + ";");
    }
  }

  private static String getColorStringForState(String simType, Series<Number, Number> series) {
    return getCellColors().getString(
        (simType + "_COLOR_" + SimulationConfig.returnStateValueBasedOnName(simType,
            series.getName().replaceAll("\\s+", ""))).toUpperCase()).toLowerCase();
  }

  private void addStateNameToStateChangeMap(String stateName) {
    if (!stateChangeSeriesMap.containsKey(stateName)) {
      Series<Number, Number> newSeries = new Series<>();
      newSeries.setName(stateName);
      stateChangeSeriesMap.put(stateName, newSeries);
      stateChangeChart.getData().add(newSeries);
    }
  }

  private int sumMapValues(Map<String, Integer> map) {
    int total = 0;
    for (String key : map.keySet()) {
      total += map.get(key);
    }
    return total;
  }

}
