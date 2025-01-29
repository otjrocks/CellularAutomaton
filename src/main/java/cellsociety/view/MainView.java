package cellsociety.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class MainView extends Group {
  private static final int myGridWidth = 300;
  private static final int myGridHeight = 300;
  private final GridView myGridView;

  public MainView(int width, int height) {
    myGridView = new GridView(width, height, 20, 20);
    getChildren().add(myGridView);
    myGridView.setColor(0, 0, Color.BLUE);
    myGridView.setColor(1, 0, Color.RED);
    myGridView.setColor(0, 1, Color.ORANGE);
  }


}
