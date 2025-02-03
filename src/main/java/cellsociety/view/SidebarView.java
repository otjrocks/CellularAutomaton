package cellsociety.view;

import cellsociety.controller.MainController;
import cellsociety.model.simulation.SimulationMetaData;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

  private final int myWidth;
  private final MainController myMainController;
  private boolean isPlaying = false;
  private boolean isEditing = false;
  private Button playPauseButton;
  private Button stepButton;
  private Button chooseFileButton;

  /**
   * Create a sidebar view with a preferred size of width x height
   *
   * @param width:  preferred width of sidebar box
   * @param height: preferred height of sidebar box
   */
  public SidebarView(int width, int height, MainController controller) {
    this.setPrefSize(width, height);
    this.setAlignment(Pos.TOP_LEFT);
    this.setSpacing(ELEMENT_SPACING);
    myWidth = width;
    myMainController = controller;
    initializeSidebar();
  }

  public void updateSidebar() {
    this.getChildren().clear();
    initializeStaticContent();
    initializeSimulationDataDisplay();
  }

  private void initializeSidebar() {
    initializeStaticContent();
    initializeSimulationDataDisplay();
  }

  private void initializeSimulationDataDisplay() {
    createSimulationMetaDataDisplay();
    createPlayPauseButton();
    createStepButton();
    createFileChooserButton();
    StateInfoView stateInfoView = new StateInfoView(myMainController.getSimulation());
    ParameterView parameterView = new ParameterView(myMainController.getSimulation());
    this.getChildren().addAll(stateInfoView, parameterView);
    Button modeButton = new Button("Edit Mode");
    modeButton.setOnMouseClicked(event -> {
      isEditing = !isEditing;
      if (isEditing) {
        stopAnimation();
        setDisableAllButtonsExceptModeButton(true);
        modeButton.setText("View Mode");
        myMainController.setEditing(true);
      } else {
        setDisableAllButtonsExceptModeButton(false);
        modeButton.setText("Edit Mode");
        myMainController.setEditing(false);
      }
    });
    this.getChildren().add(modeButton);
  }

  private void stopAnimation() {
    myMainController.stopAnimation();
    playPauseButton.setText("Play");
    isPlaying = false;
  }

  private void setDisableAllButtonsExceptModeButton(boolean disable) {
    playPauseButton.setDisable(disable);
    stepButton.setDisable(disable);
    chooseFileButton.setDisable(disable);
  }

  private void createSimulationMetaDataDisplay() {
    SimulationMetaData simulationData = myMainController.getSimulation().getData();
    addTextToSidebar("Name: " + simulationData.name(), 14, TextAlignment.LEFT);
    addTextToSidebar("Type: " + simulationData.type(), 14, TextAlignment.LEFT);
    addTextToSidebar("Author: " + simulationData.author(), 14, TextAlignment.LEFT);
    addTextToSidebar("Description: " + simulationData.description(), 14,
        TextAlignment.LEFT);
  }

  private void createFileChooserButton() {
    chooseFileButton = new Button("Choose File");
    chooseFileButton.setOnAction(event -> {
      myMainController.handleNewSimulationFromFile();
      stopAnimationPlayIfRunning(playPauseButton);
    });
    this.getChildren().add(chooseFileButton);
  }

  private void createStepButton() {
    stepButton = new Button("Single Step");
    stepButton.setOnAction(event -> {
      myMainController.handleSingleStep();
      stopAnimationPlayIfRunning(playPauseButton);
    });
    this.getChildren().add(stepButton);
  }

  private void stopAnimationPlayIfRunning(Button playPauseButton) {
    if (isPlaying) {
      playPauseButton.setText("Play");
      isPlaying = false;
    }
  }

  private void createPlayPauseButton() {
    playPauseButton = new Button("Play");
    playPauseButton.setOnAction(event -> {
      isPlaying = !isPlaying;
      if (isPlaying) {
        playPauseButton.setText("Pause");
        myMainController.startAnimation();
      } else {
        playPauseButton.setText("Play");
        myMainController.stopAnimation();
      }
    });
    this.getChildren().addAll(playPauseButton);
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
