package cellsociety.view;

import static cellsociety.config.MainConfig.INITIAL_STEP_SPEED;
import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.STEP_SPEED;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.config.MainConfig.getMessages;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.controller.MainController;
import cellsociety.controller.PreferencesController;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.components.AlertField;
import java.util.concurrent.Flow;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
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
  private final VBox mySpeedSliderBox = new VBox();
  private FlowPane myControlButtons = new FlowPane();

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
    ParameterView myParameterView = new ParameterView(myMainController, false);
    setPlayPauseButtonText();
    this.getChildren().addAll(myStateInfoView, myParameterView, myControlButtons, mySpeedSliderBox);
  }


  private void initializeSpeedSlider() {
    double initialSliderValue = 1 / INITIAL_STEP_SPEED;
    double currentSliderValue = Double.parseDouble(
        PreferencesController.getPreference("animationSpeed", String.valueOf(initialSliderValue)));
    Text sliderLabel = new Text(getMessages().getString("SLIDER_LABEL"));
    Slider speedSlider = new Slider(initialSliderValue / SPEED_SLIDER_DELTA,
        initialSliderValue * SPEED_SLIDER_DELTA, currentSliderValue);
    speedSlider.valueProperty().addListener(
        (observable, oldValue, newValue) -> {
          myMainController.updateAnimationSpeed(
              1 / (Double) newValue, myMainController.isPlaying());
          PreferencesController.setPreference("animationSpeed", String.valueOf(newValue));
        });
    mySpeedSliderBox.getChildren().addAll(sliderLabel, speedSlider);
  }

  private FlowPane createAllButtons() {
    createPlayPauseButton();
    createStepButton();
    createFileChooserButton();
    createSaveFileButton();
    myControlButtons.setAlignment(Pos.CENTER_LEFT);
    myControlButtons.setAlignment(Pos.CENTER);
    myControlButtons.setHgap(ELEMENT_SPACING);
    myControlButtons.setVgap(ELEMENT_SPACING);
    myControlButtons.getStyleClass().add("control-buttons");
    myControlButtons.getChildren()
        .addAll(myPlayPauseButton, myStepButton, myChooseFileButton, mySaveButton);
    return myControlButtons;
  }

  private void createSimulationMetaDataDisplay() {
    SimulationMetaData simulationData = myMainController.getSimulation().data();
    Text infoText = new Text(getMessages().getString("INFO_DISPLAY_TITLE"));
    infoText.getStyleClass().add("secondary-title");
    Text name = createText(
        String.format("%s %s", getMessages().getString("NAME_LABEL"), simulationData.name()));
    Text type = createText(
        String.format("%s %s", getMessages().getString("TYPE_LABEL"), simulationData.type()));
    Text author = createText(
        String.format("%s %s", getMessages().getString("AUTHOR_LABEL"), simulationData.author()));
    Text description = createText(
        String.format("%s %s", getMessages().getString("DESCRIPTION_LABEL"),
            simulationData.description())
    );
    VBox metaDataBox = new VBox();
    metaDataBox.setSpacing(ELEMENT_SPACING);
    metaDataBox.getChildren().addAll(infoText, name, type, author, description);
    metaDataBox.getStyleClass().add("metadata-container");
    this.getChildren().add(metaDataBox);
  }

  private void createFileChooserButton() {
    myChooseFileButton = new Button(getMessages().getString("CHOOSE_FILE_BUTTON"));
    myChooseFileButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
    myChooseFileButton.setOnAction(event -> {
      try {
        stopAnimationPlayIfRunning();
        myMainController.handleNewSimulationFromFile();
      } catch (IllegalArgumentException e) {
        myAlertField.flash(e.getMessage(), true);
        myAlertField.flash(getMessages().getString("LOAD_ERROR"), true);
      } catch (Exception e) {
        myAlertField.flash(getMessages().getString("LOAD_ERROR"), true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
      }
      stopAnimationPlayIfRunning();
    });
    this.getChildren().add(myChooseFileButton);
  }

  private void createSaveFileButton() {
    mySaveButton = new Button(getMessages().getString("SAVE_TO_XML"));
    mySaveButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
    mySaveButton.setOnMouseClicked(event -> {
      stopAnimationPlayIfRunning();
      try {
        myMainController.handleSavingToFile();
        myAlertField.flash(getMessages().getString("FILE_SAVE_SUCCESS"), false);
      } catch (Exception e) {
        myAlertField.flash(getMessages().getString("FILE_SAVE_FAIL"), true);
        if (VERBOSE_ERROR_MESSAGES) {
          myAlertField.flash(e.getMessage(), true);
        }
      }
    });
  }

  private void createStepButton() {
    myStepButton = new Button(getMessages().getString("STEP_LABEL"));
    myStepButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
    myStepButton.setOnAction(event -> {
      stopAnimationPlayIfRunning();
      myMainController.handleSingleStep();
    });
  }

  private void createPlayPauseButton() {
    myPlayPauseButton = new Button(getMessages().getString("PLAY_LABEL"));
    myPlayPauseButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
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
    myAlertField.flash(getMessages().getString("ANIMATION_START"), false);
  }

  private void stopAnimation() {
    myMainController.stopAnimation();
    setPlayPauseButtonText();
    myAlertField.flash(getMessages().getString("ANIMATION_PAUSE"), false);
  }

  private void stopAnimationPlayIfRunning() {
    if (myMainController.isPlaying()) {
      stopAnimation();
    }
  }

  private void setPlayPauseButtonText() {
    if (myMainController.isPlaying()) {
      myPlayPauseButton.setText(getMessages().getString("PAUSE_LABEL"));
    } else {
      myPlayPauseButton.setText(getMessages().getString("PLAY_LABEL"));
    }
  }

  private void initializeStaticContent() {
    Text title = createText(getMessages().getString("TITLE"));
    title.getStyleClass().add("main-title");
    this.getChildren().addAll(title);
  }

  private Text createText(String message) {
    Text text = new Text(message);
    text.setFill(Color.BLACK);
    text.setTextAlignment(TextAlignment.LEFT);
    text.setWrappingWidth(SIDEBAR_WIDTH - (ELEMENT_SPACING * 6));
    return text;
  }

}
