package cellsociety.view;

import static cellsociety.config.MainConfig.STEP_SPEED;

import cellsociety.config.MainConfig;
import cellsociety.controller.MainController;
import cellsociety.model.simulation.SimulationMetaData;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
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
  public static final double SPEED_SLIDER_DELTA = 10; // how drastic changes from speed slider should be

  private final int myWidth;
  private final MainController myMainController;
  private boolean isPlaying = false;
  private boolean isEditing = false;
  private Button myPlayPauseButton;
  private Button myStepButton;
  private Button myChooseFileButton;
  private Button myModeButton;
  private final VBox myMetaData = new VBox();
  private CreateNewSimulationView myCreateNewSimulationView;

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
    this.getChildren().add(myMetaData);
    myMetaData.setSpacing(ELEMENT_SPACING);
    myWidth = width;
    myMainController = controller;
    initializeSidebar();
  }

  public void updateSidebar() {
    updateSidebarContent();
  }

  private void initializeSidebar() {
    updateSidebar();
    double initialSliderValue = (1 / STEP_SPEED);
    initializeSpeedSlider(initialSliderValue);
    this.getChildren().add(createAllButtons());
    myCreateNewSimulationView = new CreateNewSimulationView(
        myMainController.getGridRows(), myMainController.getGridCols(), myMainController);
  }

  private void initializeSpeedSlider(double initialSliderValue) {
    Slider slider = new Slider(initialSliderValue / SPEED_SLIDER_DELTA,
        initialSliderValue * SPEED_SLIDER_DELTA, initialSliderValue);
    slider.valueProperty().addListener((observable, oldValue, newValue) -> myMainController.updateAnimationSpeed(1 / (Double) newValue, isPlaying));
    this.getChildren().add(slider);
  }

  private void updateSidebarContent() {
    myMetaData.getChildren().clear();
    initializeStaticContent();
    createSimulationMetaDataDisplay();
    StateInfoView myStateInfoView = new StateInfoView(myMainController.getSimulation());
    ParameterView myParameterView = new ParameterView(myMainController.getSimulation());
    myMetaData.getChildren().addAll(myStateInfoView, myParameterView);
  }

  private HBox createAllButtons() {
    createPlayPauseButton();
    createStepButton();
    createFileChooserButton();
    createChangeModeButton();
    HBox buttons = new HBox();
    buttons.setAlignment(Pos.CENTER_LEFT);
    buttons.setSpacing(ELEMENT_SPACING);
    buttons.getChildren().addAll(myPlayPauseButton, myStepButton, myChooseFileButton, myModeButton);
    return buttons;
  }

  private void disableEditView() {
    this.getChildren().remove(myCreateNewSimulationView);
  }

  private void enableEditView() {
    this.getChildren().add(myCreateNewSimulationView);
  }

  private void createChangeModeButton() {
    myModeButton = new Button("Edit Mode");
    myModeButton.setOnMouseClicked(event -> {
      isEditing = !isEditing;
      if (isEditing) {
        enableEditView();
        stopAnimation();
        setDisableAllButtonsExceptModeButton(true);
        myModeButton.setText("View Mode");
        myMainController.setEditing(true);
      } else {
        disableEditView();
        setDisableAllButtonsExceptModeButton(false);
        myModeButton.setText("Edit Mode");
        myMainController.setEditing(false);
      }
    });
    this.getChildren().add(myModeButton);
  }

  private void stopAnimation() {
    myMainController.stopAnimation();
    myPlayPauseButton.setText("Play");
    isPlaying = false;
  }

  private void setDisableAllButtonsExceptModeButton(boolean disable) {
    myPlayPauseButton.setDisable(disable);
    myStepButton.setDisable(disable);
    myChooseFileButton.setDisable(disable);
  }

  private void createSimulationMetaDataDisplay() {
    SimulationMetaData simulationData = myMainController.getSimulation().getData();
    Text name = createText("Name: " + simulationData.name(), 14, TextAlignment.LEFT);
    Text type = createText("Type: " + simulationData.type(), 14, TextAlignment.LEFT);
    Text author = createText("Author: " + simulationData.author(), 14, TextAlignment.LEFT);
    Text description = createText("Description: " + simulationData.description(), 14,
        TextAlignment.LEFT);
    myMetaData.getChildren().addAll(name, type, author, description);
  }

  private void createFileChooserButton() {
    myChooseFileButton = new Button("Choose File");
    myChooseFileButton.setOnAction(event -> {
      myMainController.handleNewSimulationFromFile();
      stopAnimationPlayIfRunning(myPlayPauseButton);
    });
    this.getChildren().add(myChooseFileButton);
  }

  private void createStepButton() {
    myStepButton = new Button("Single Step");
    myStepButton.setOnAction(event -> {
      myMainController.handleSingleStep();
      stopAnimationPlayIfRunning(myPlayPauseButton);
    });
    this.getChildren().add(myStepButton);
  }

  private void stopAnimationPlayIfRunning(Button playPauseButton) {
    if (isPlaying) {
      playPauseButton.setText("Play");
      isPlaying = false;
    }
  }

  private void createPlayPauseButton() {
    myPlayPauseButton = new Button("Play");
    myPlayPauseButton.setOnAction(event -> {
      isPlaying = !isPlaying;
      if (isPlaying) {
        myPlayPauseButton.setText("Pause");
        myMainController.startAnimation();
      } else {
        myPlayPauseButton.setText("Play");
        myMainController.stopAnimation();
      }
    });
    this.getChildren().addAll(myPlayPauseButton);
  }

  private void initializeStaticContent() {
    Text title = createText("Cellular Automaton", 20, TextAlignment.CENTER);
    Text infoTitle = createText("Current Simulation Information: ", 18, TextAlignment.LEFT);
    myMetaData.getChildren().addAll(title, infoTitle);
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
