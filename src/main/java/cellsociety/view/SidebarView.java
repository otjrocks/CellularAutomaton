package cellsociety.view;

import static cellsociety.config.MainConfig.STEP_SPEED;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;

import cellsociety.controller.MainController;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.components.AlertField;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
  private Button mySaveButton;
  private Slider mySpeedSlider;
  private final VBox myMetaData = new VBox();
  private final HBox myButtons = new HBox();
  private CreateNewSimulationView myCreateNewSimulationView;
  private AlertField myAlertField;

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
    createChangeModeButton();
    this.getChildren().add(myModeButton);
    this.getChildren().add(myMetaData);
    myMetaData.setSpacing(ELEMENT_SPACING);
    myWidth = width;
    myMainController = controller;
    initializeSidebar();
  }

  /**
   * Update the non-static content of the sidebar
   */
  public void updateSidebar() {
    updateSidebarContent();
  }

  private AlertField initializeAlertField() {
    final AlertField myAlertField;
    myAlertField = new AlertField();
    this.getChildren().add(myAlertField);
    VBox.setVgrow(myAlertField, Priority.ALWAYS);
    myAlertField.setAlignment(Pos.BOTTOM_LEFT);
    return myAlertField;
  }

  private void initializeSidebar() {
    updateSidebar();
    initializeSpeedSlider();
    this.getChildren().add(createAllButtons());
    myAlertField = initializeAlertField();
    myCreateNewSimulationView = new CreateNewSimulationView(
        myMainController.getGridRows(), myMainController.getGridCols(), myMainController,
        myAlertField);
  }

  private void initializeSpeedSlider() {
    double initialSliderValue = (1 / STEP_SPEED);
    mySpeedSlider = new Slider(initialSliderValue / SPEED_SLIDER_DELTA,
        initialSliderValue * SPEED_SLIDER_DELTA, initialSliderValue);
    mySpeedSlider.valueProperty().addListener(
        (observable, oldValue, newValue) -> myMainController.updateAnimationSpeed(
            1 / (Double) newValue, isPlaying));
    this.getChildren().add(mySpeedSlider);
  }

  private void updateSidebarContent() {
    myMetaData.getChildren().clear();
    createChangeModeButton();
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
    createSaveFileButton();
    myButtons.setAlignment(Pos.CENTER_LEFT);
    myButtons.setSpacing(ELEMENT_SPACING);
    myButtons.getChildren()
        .addAll(myPlayPauseButton, myStepButton, myChooseFileButton, mySaveButton);
    return myButtons;
  }

  private void disableEditView() {
    this.getChildren().clear();
    this.getChildren().addAll(myModeButton, myMetaData, mySpeedSlider, myButtons, myAlertField);
  }


  private void enableEditView() {
    this.getChildren().clear();
    this.getChildren().addAll(myModeButton, myCreateNewSimulationView, myAlertField);
    moveAlertFieldToBottom();
  }

  private void moveAlertFieldToBottom() {
    this.getChildren().remove(myAlertField);
    this.getChildren().add(myAlertField);
  }

  private void createChangeModeButton() {
    myModeButton = new Button("Edit Mode");
    myModeButton.setOnMouseClicked(event -> {
      isEditing = !isEditing;
      if (isEditing) {
        enableEditView();
        stopAnimationPlayIfRunning();
        myModeButton.setText("View Mode");
        myMainController.setEditing(true);
        myAlertField.flash("Editing mode enabled!", false);
      } else {
        disableEditView();
        myModeButton.setText("Edit Mode");
        myMainController.setEditing(false);
        myAlertField.flash("Editing mode disabled!", false);
      }
    });
  }

  private void createSimulationMetaDataDisplay() {
    SimulationMetaData simulationData = myMainController.getSimulation().getData();
    Text name = createText("Name: " + simulationData.name());
    Text type = createText("Type: " + simulationData.type());
    Text author = createText("Author: " + simulationData.author());
    Text description = createText("Description: " + simulationData.description()
    );
    myMetaData.getChildren().addAll(name, type, author, description);
  }

  private void createFileChooserButton() {
    myChooseFileButton = new Button("Choose File");
    myChooseFileButton.setOnAction(event -> {
      try {
        myMainController.handleNewSimulationFromFile();
      } catch (Exception e) {
        myAlertField.flash("Could not load simulation from file!", true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
      }
      stopAnimationPlayIfRunning();
    });
    this.getChildren().add(myChooseFileButton);
  }

  private void createSaveFileButton() {
    mySaveButton = new Button("Save to XML");
    mySaveButton.setOnMouseClicked(event -> {
      stopAnimationPlayIfRunning();
      try {
        myMainController.handleSavingToFile();
        myAlertField.flash("File successfully saved!", false);
      } catch (Exception e) {
        myAlertField.flash("Could not save to file!", true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
      }
    });
  }

  private void createStepButton() {
    myStepButton = new Button("Single Step");
    myStepButton.setOnAction(event -> {
      myMainController.handleSingleStep();
      stopAnimationPlayIfRunning();
    });
    this.getChildren().add(myStepButton);
  }

  private void stopAnimationPlayIfRunning() {
    if (isPlaying) {
      stopAnimation();
    }
  }

  private void stopAnimation() {
    myMainController.stopAnimation();
    myPlayPauseButton.setText("Play");
    isPlaying = false;
    myAlertField.flash("Animation successfully paused!", false);
  }

  private void createPlayPauseButton() {
    myPlayPauseButton = new Button("Play");
    myPlayPauseButton.setOnAction(event -> {
      isPlaying = !isPlaying;
      if (isPlaying) {
        myPlayPauseButton.setText("Pause");
        myMainController.startAnimation();
        myAlertField.flash("Animation successfully started!", false);
      } else {
        stopAnimation();
      }
    });
    this.getChildren().addAll(myPlayPauseButton);
  }

  private void initializeStaticContent() {
    Text title = createText("Cellular Automaton");
    Text infoTitle = createText("Current Simulation Information: ");
    myMetaData.getChildren().addAll(title, infoTitle);
  }

  private Text createText(String message) {
    Text text = new Text(message);
    text.setFill(Color.BLACK);
    text.setTextAlignment(TextAlignment.LEFT);
    text.setWrappingWidth(myWidth);
    return text;
  }

}
