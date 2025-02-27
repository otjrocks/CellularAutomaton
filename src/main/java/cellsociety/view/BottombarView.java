package cellsociety.view;

import java.util.HashMap;
import java.util.Map;

import static cellsociety.config.MainConfig.GRID_HEIGHT;
import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.getCellColors;
import static cellsociety.config.MainConfig.getMessage;
import cellsociety.config.SimulationConfig;
import cellsociety.controller.MainController;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 * A class to handle all the bottombar view elements of the app
 *
 */
public class BottombarView extends VBox {
    public static final double ELEMENT_SPACING = 10;

    private final MainController myMainController;
    private final Text myIterationText = createText(getMessage("ITERATOR_TEXT") + "0");
    private BarChart<String, Number> stateHistogram;
    private LineChart<Number, Number> stateChangeChart;
    private final Map<String, XYChart.Series<Number, Number>> stateChangeSeriesMap = new HashMap<>();
    private int stepCount = 0;
    
    /**
   * Create a bottombar view with a preferred size of width x height
   *
   * @param width:  preferred width of bottombar box
   * @param height: preferred height of bottombar box
   */
    
    public BottombarView(int width, int height, MainController controller) {
      this.setPrefSize(width, height);
      this.setAlignment(Pos.TOP_LEFT);
      this.setSpacing(ELEMENT_SPACING);
      this.getStyleClass().add("bottombar");
      myMainController = controller;
      this.getChildren().add(myIterationText);
      setupHistogram();
      setupStateChangeChart();
      update();
    }

   public void update() {
    updateIterationCounter(0);
    stateHistogram.getData().clear();
    stateChangeChart.getData().clear();
   }

    private Text createText(String message) {
      Text text = new Text(message);
      text.setTextAlignment(TextAlignment.LEFT);
      text.setWrappingWidth(GRID_WIDTH - (ELEMENT_SPACING * 6));
      return text;
    }

    public void updateIterationCounter(int count) {
      myIterationText.setText(getMessage("ITERATOR_TEXT") + count);
    }

    private void setupHistogram() {
      CategoryAxis xAxis = new CategoryAxis();
      NumberAxis yAxis = new NumberAxis();
      xAxis.setLabel("Cell State");
      yAxis.setLabel("Count");
      yAxis.setAutoRanging(true); 

      stateHistogram = new BarChart<>(xAxis, yAxis);
      stateHistogram.setTitle("Cell State Distribution");
      stateHistogram.setMinWidth(GRID_WIDTH);
      stateHistogram.setMaxWidth(GRID_WIDTH);
      stateHistogram.setMaxHeight(GRID_HEIGHT/2);
      
      XYChart.Series<String, Number> histogramSeries = new XYChart.Series<>();
      stateHistogram.getData().add(histogramSeries);

      getChildren().add(stateHistogram);
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

    public void updateHistogram(Map<String, Integer> stateCounts, String simType) {
      XYChart.Series<String, Number> newSeries = new XYChart.Series<>();

      int maxValue = stateCounts.values().stream().max(Integer::compareTo).orElse(100);
      stateHistogram.getYAxis().setAutoRanging(true);
      ((NumberAxis) stateHistogram.getYAxis()).setUpperBound(maxValue + 10);
      ((NumberAxis) stateHistogram.getYAxis()).setTickUnit(Math.max(1, maxValue / 10));

      for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
          newSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
      }

      stateHistogram.getData().clear();
      stateHistogram.getData().add(newSeries);

      Platform.runLater(() -> {
          for (XYChart.Data<String, Number> data : newSeries.getData()) {
              Node barNode = data.getNode();
              if (barNode != null) {
                  String colorString = getCellColors().getString((simType + "_COLOR_" + SimulationConfig.returnStateValueBasedOnName(simType, data.getXValue().replaceAll("\\s+", ""))).toUpperCase()).toLowerCase();
                  barNode.setStyle("-fx-bar-fill: " + colorString + ";");
              }
          }
      });
    }

    public void updateStateChangeChart(Map<String, Integer> stateCounts) {
      for (Map.Entry<String, Integer> entry : stateCounts.entrySet()) {
          String stateName = entry.getKey();
          int newValue = entry.getValue();

          if (!stateChangeSeriesMap.containsKey(stateName)) {
              XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
              newSeries.setName(stateName);
              stateChangeSeriesMap.put(stateName, newSeries);
              stateChangeChart.getData().add(newSeries);
          }

          stateChangeSeriesMap.get(stateName).getData().add(new XYChart.Data<>(stepCount, newValue));
      }

      stepCount++;
    }

}