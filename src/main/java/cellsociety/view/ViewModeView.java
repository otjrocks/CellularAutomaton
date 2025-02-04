package cellsociety.view;

import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.STEP_SPEED;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.controller.MainController;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.components.AlertField;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A view to handle all the UI components that are shown when view mode is enabled
 *
 * @author Owen Jennings
 */
public class ViewModeView extends VBox {

  public static final double SPEED_SLIDER_DELTA = 10;

  private final MainController myMainController;
  private final AlertField myAlertField;
  private Button myPlayPauseButton;
  private Button myChooseFileButton;
  private Button mySaveButton;
  private Button myStepButton;
  private Slider mySpeedSlider;
  private HBox myControlButtons = new HBox();

  /**
   * Initialize the ViewModeView.
   *
   * @param mainController: the main controller of this view
   * @param alertField:     the alert field to display alerts to the user
   */
  public ViewModeView(MainController mainController, AlertField alertField) {
    this.setSpacing(ELEMENT_SPACING);
    myMainController = mainController;
    myAlertField = alertField;
    myControlButtons = createAllButtons();
    initializeSpeedSlider();
    update();
  }

  /**
   * Update the non-static content of the sidebar
   */
  public void update() {
    this.getChildren().clear();
    initializeStaticContent();
    createSimulationMetaDataDisplay();
    StateInfoView myStateInfoView = new StateInfoView(myMainController.getSimulation());
    ParameterView myParameterView = new ParameterView(myMainController.getSimulation());
    setPlayPauseButtonText();
    this.getChildren().addAll(myStateInfoView, myParameterView, mySpeedSlider, myControlButtons);
  }


  private void initializeSpeedSlider() {
    double initialSliderValue = (1 / STEP_SPEED);
    mySpeedSlider = new Slider(initialSliderValue / SPEED_SLIDER_DELTA,
        initialSliderValue * SPEED_SLIDER_DELTA, initialSliderValue);
    mySpeedSlider.valueProperty().addListener(
        (observable, oldValue, newValue) -> myMainController.updateAnimationSpeed(
            1 / (Double) newValue, myMainController.isPlaying()));
  }

  private HBox createAllButtons() {
    createPlayPauseButton();
    createStepButton();
    createFileChooserButton();
    createSaveFileButton();
    myControlButtons.setAlignment(Pos.CENTER_LEFT);
    myControlButtons.setSpacing(ELEMENT_SPACING);
    myControlButtons.getChildren()
        .addAll(myPlayPauseButton, myStepButton, myChooseFileButton, mySaveButton);
    return myControlButtons;
  }

  private void createSimulationMetaDataDisplay() {
    SimulationMetaData simulationData = myMainController.getSimulation().getData();
    Text name = createText("Name: " + simulationData.name());
    Text type = createText("Type: " + simulationData.type());
    Text author = createText("Author: " + simulationData.author());
    Text description = createText("Description: " + simulationData.description()
    );
    this.getChildren().addAll(name, type, author, description);
  }

  private void createFileChooserButton() {
    myChooseFileButton = new Button("Choose File");
    myChooseFileButton.setOnAction(event -> {
      try {
        stopAnimationPlayIfRunning();
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
      stopAnimationPlayIfRunning();
      myMainController.handleSingleStep();
    });
  }

  private void createPlayPauseButton() {
    myPlayPauseButton = new Button("Play");
    myPlayPauseButton.setOnAction(event -> {
      if (myMainController.isPlaying()) {
        stopAnimation();
      } else {
        startAnimation();
      }
    });
    this.getChildren().addAll(myPlayPauseButton);
  }

  private void startAnimation() {
    myMainController.startAnimation();
    setPlayPauseButtonText();
    myAlertField.flash("Animation successfully started!", false);
  }

  private void stopAnimation() {
    myMainController.stopAnimation();
    setPlayPauseButtonText();
    myAlertField.flash("Animation successfully paused!", false);
  }

  private void stopAnimationPlayIfRunning() {
    if (myMainController.isPlaying()) {
      stopAnimation();
    }
  }

  private void setPlayPauseButtonText() {
    if (myMainController.isPlaying()) {
      myPlayPauseButton.setText("Pause");
    } else {
      myPlayPauseButton.setText("Play");
    }
  }

  private void initializeStaticContent() {
    Text title = createText("Cellular Automaton");
    Text infoTitle = createText("Current Simulation Information: ");
    this.getChildren().addAll(title, infoTitle);
  }

  private Text createText(String message) {
    Text text = new Text(message);
    text.setFill(Color.BLACK);
    text.setTextAlignment(TextAlignment.LEFT);
    text.setWrappingWidth(SIDEBAR_WIDTH);
    return text;
  }
}
