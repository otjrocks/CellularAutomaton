package cellsociety.view;

import cellsociety.controller.MainController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A view to handle the setting of new coordinates for the simulation grid
 *
 * @author Owen Jennings
 */
public class SetCoordinatesView extends VBox {

  private int myNumRows;
  private int myNumCols;
  private Text rowText;
  private Text colText;
  private final MainController myMainController;

  public SetCoordinatesView(int rows, int cols, MainController mainController) {
    this.setSpacing(5);
    this.setAlignment(Pos.CENTER_LEFT);
    myNumRows = rows;
    myNumCols = cols;
    myMainController = mainController;
    initialize();
  }

  private void initialize() {
    createTitle();
    createRowControl();
    createColControl();
    createCreateButton();
  }

  private void createTitle() {
    Text title = new Text("Create New Grid:");
    title.setFont(new Font("Arial", 20));
    this.getChildren().add(title);

  }

  private void createCreateButton() {
    Button createButton = new Button("Create");
    createButton.setOnMouseClicked(event -> {
      myMainController.updateGridCoordinates(myNumRows, myNumCols);
    });
    this.getChildren().add(createButton);
  }

  private void createColControl() {
    HBox container = new HBox();
    container.setAlignment(Pos.CENTER_LEFT);
    container.setSpacing(5);
    Text label = new Text("Number of Columns:");
    Button decrementButton = new Button("-");
    decrementButton.setOnMouseClicked(event -> setCols(myNumCols - 1));
    colText = new Text(Integer.toString(myNumCols));
    Button incrementButton = new Button("+");
    incrementButton.setOnMouseClicked(event -> setCols(myNumCols + 1));
    container.getChildren().addAll(label, decrementButton, colText, incrementButton);
    this.getChildren().add(container);
  }

  private void createRowControl() {
    HBox container = new HBox();
    container.setAlignment(Pos.CENTER_LEFT);
    container.setSpacing(5);
    Text label = new Text("Number of Rows:");
    Button decrementButton = new Button("-");
    decrementButton.setOnMouseClicked(event -> setRows(myNumRows - 1));
    rowText = new Text(Integer.toString(myNumRows));
    Button incrementButton = new Button("+");
    incrementButton.setOnMouseClicked(event -> setRows(myNumRows + 1));
    container.getChildren().addAll(label, decrementButton, rowText, incrementButton);
    this.getChildren().add(container);
  }

  private void setRows(int amount) {
    myNumRows = Math.max(amount, 0);
    rowText.setText(Integer.toString(myNumRows));
  }

  private void setCols(int amount) {
    myNumCols = Math.max(amount, 0);
    colText.setText(Integer.toString(myNumCols));
  }

}
