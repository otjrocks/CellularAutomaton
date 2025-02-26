package cellsociety.view;

import static cellsociety.config.MainConfig.GRID_WIDTH;
import static cellsociety.config.MainConfig.getMessage;
import cellsociety.controller.MainController;
import javafx.geometry.Pos;
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
    private Text myIterationText = createText(getMessage("ITERATOR_TEXT") + "0");
    
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
    update();
  }

   public void update() {
    updateIterationCounter(0);
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

}