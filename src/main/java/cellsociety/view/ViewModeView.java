package cellsociety.view;

import static cellsociety.config.MainConfig.INITIAL_STEP_SPEED;
import static cellsociety.config.MainConfig.SIDEBAR_WIDTH;
import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;
import static cellsociety.config.MainConfig.getMessage;
import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import cellsociety.controller.MainController;
import cellsociety.controller.PreferencesController;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.utility.CreateNewSimulation;
import cellsociety.view.components.AlertField;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A view to handle all the UI components that are shown when view mode is enabled
 *
 * @author Owen Jennings
 */
public class ViewModeView extends VBox {

  public static final double SPEED_SLIDER_DELTA = 10;
  public static final String COMBINE_TWO_STRINGS = "%s %s";

  private final MainController myMainController;
  private final AlertField myAlertField;
  private Button myPlayPauseButton;
  private Button myChooseFileButton;
  private Button mySaveButton;
  private Button myStepButton;
  private Button newSimulationButton;
  private final VBox mySpeedSliderBox = new VBox();
  private FlowPane myControlButtons = new FlowPane();

  /**
   * Initialize the ViewModeView.
   *
   * @param mainController: the main controller of this view
   * @param alertField:     the alert field to display alerts to the user
   */
  public ViewModeView(MainController mainController, AlertField alertField) {
    super();
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
    StateInfoView myStateInfoView = new StateInfoView(myMainController.getSimulation());
    ParameterView myParameterView = new ParameterView(myMainController, false);
    NeighborView myNeighborView = new NeighborView(myMainController, false);
    setPlayPauseButtonText();
    this.getChildren().addAll(myControlButtons, mySpeedSliderBox);
    createSimulationMetaDataDisplay();
    this.getChildren().addAll(myStateInfoView, myParameterView, myNeighborView);
  }


  private void initializeSpeedSlider() {
    double initialSliderValue = 1 / INITIAL_STEP_SPEED;
    double currentSliderValue = Double.parseDouble(
        PreferencesController.getPreference("animationSpeed", String.valueOf(initialSliderValue)));
    Text sliderLabel = new Text(getMessage("SLIDER_LABEL"));
    Slider speedSlider = new Slider(initialSliderValue / SPEED_SLIDER_DELTA,
        initialSliderValue * SPEED_SLIDER_DELTA, currentSliderValue);
    speedSlider.setId("viewModeSpeedSlider");
    speedSlider.valueProperty().addListener(
        (observable, oldVal, newValue) -> handleSpeedSliderUpdate(newValue));
    mySpeedSliderBox.getChildren().addAll(sliderLabel, speedSlider);
  }

  private void handleSpeedSliderUpdate(Number newValue) {
    myMainController.updateAnimationSpeed(
        1 / (Double) newValue, myMainController.isPlaying());
    PreferencesController.setPreference("animationSpeed", String.valueOf(newValue));
  }

  private FlowPane createAllButtons() {
    createPlayPauseButton();
    createStepButton();
    createFileChooserButton();
    createSaveFileButton();
    createNewSimulationButton();
    myControlButtons.setAlignment(Pos.CENTER_LEFT);
    myControlButtons.setHgap(ELEMENT_SPACING);
    myControlButtons.setVgap(ELEMENT_SPACING);
    myControlButtons.getStyleClass().add("control-buttons");
    myControlButtons.getChildren()
        .addAll(myPlayPauseButton, myStepButton, myChooseFileButton, mySaveButton,
            newSimulationButton);
    return myControlButtons;
  }

  private void createSimulationMetaDataDisplay() {
    SimulationMetaData simulationData = myMainController.getSimulation().data();
    Text infoText = new Text(getMessage("INFO_DISPLAY_TITLE"));
    infoText.getStyleClass().add("secondary-title");
    Text name = createText(
        String.format(COMBINE_TWO_STRINGS, getMessage("NAME_LABEL"), simulationData.name()));
    name.setId("viewModeSimulationName");
    Text type = createText(
        String.format(COMBINE_TWO_STRINGS, getMessage("TYPE_LABEL"), simulationData.type()));
    type.setId("viewModeSimulationType");
    Text author = createText(
        String.format(COMBINE_TWO_STRINGS, getMessage("AUTHOR_LABEL"), simulationData.author()));
    author.setId("viewModeSimulationAuthor");
    Text description = createText(
        String.format(COMBINE_TWO_STRINGS, getMessage("DESCRIPTION_LABEL"),
            simulationData.description())
    );
    description.setId("viewModeSimulationDescription");
    Text edgeType = createText(
        String.format(COMBINE_TWO_STRINGS, getMessage("EDGE_TYPE_LABEL"),
            myMainController.getEdgeStrategyType().toString())
    );
    edgeType.setId("viewModeEdgeTypeDisplay");
    VBox metaDataBox = new VBox();
    metaDataBox.setSpacing(ELEMENT_SPACING);
    metaDataBox.getChildren().addAll(infoText, name, type, author, edgeType, description);
    metaDataBox.getStyleClass().add("metadata-container");
    this.getChildren().add(metaDataBox);
  }

  private void createFileChooserButton() {
    myChooseFileButton = new Button(getMessage("CHOOSE_FILE_BUTTON"));
    myChooseFileButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
    myChooseFileButton.setOnAction(event -> {
      handleFileChooserAction();
    });
    this.getChildren().add(myChooseFileButton);
  }

  private void handleFileChooserAction() {
    try {
      stopAnimationPlayIfRunning();
      myMainController.handleNewSimulationFromFile();
    } catch (IllegalArgumentException e) {
      myAlertField.flash(e.getMessage(), true);
      myAlertField.flash(getMessage("LOAD_ERROR"), true);
    } catch (Exception e) {
      myAlertField.flash(getMessage("LOAD_ERROR"), true);
      if (VERBOSE_ERROR_MESSAGES) {
        myAlertField.flash(e.getMessage(), true);
      }
    }
    stopAnimationPlayIfRunning();
  }

  private void createSaveFileButton() {
    mySaveButton = new Button(getMessage("SAVE_TO_XML"));
    mySaveButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
    mySaveButton.setOnMouseClicked(e -> {
      handleFileSaveAction();
    });
  }

  private void handleFileSaveAction() {
    stopAnimationPlayIfRunning();
    try {
      myMainController.handleSavingToFile();
      myAlertField.flash(getMessage("FILE_SAVE_SUCCESS"), false);
    } catch (IllegalArgumentException e) {
      myAlertField.flash(getMessage("FILE_SAVE_FAIL"), true);
      if (VERBOSE_ERROR_MESSAGES) {
        myAlertField.flash(e.getMessage(), true);
      }
    }
  }

  private void createStepButton() {
    myStepButton = new Button(getMessage("STEP_LABEL"));
    myStepButton.setId("viewModeStepButton");
    myStepButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
    myStepButton.setOnAction(event -> {
      stopAnimationPlayIfRunning();
      myMainController.handleSingleStep();
    });
  }

  private void createNewSimulationButton() {
    newSimulationButton = new Button(getMessage("NEW_SIMULATION_LABEL"));
    newSimulationButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
    newSimulationButton.setOnAction(event -> {
      CreateNewSimulation simulationManager = new CreateNewSimulation();
      simulationManager.launchNewSimulation();
    });
  }

  private void createPlayPauseButton() {
    myPlayPauseButton = new Button(getMessage("PLAY_LABEL"));
    myPlayPauseButton.setId("viewModePlayPauseButton");
    myPlayPauseButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
    myPlayPauseButton.setOnAction(e -> {
      handlePlayPauseButtonAction();
    });
    this.getChildren().addAll(myPlayPauseButton);
  }

  private void handlePlayPauseButtonAction() {
    if (myMainController.isPlaying()) {
      stopAnimation();
    } else {
      startAnimation();
    }
  }

  private void startAnimation() {
    myMainController.startAnimation();
    setPlayPauseButtonText();
    myAlertField.flash(getMessage("ANIMATION_START"), false);
  }

  private void stopAnimation() {
    myMainController.stopAnimation();
    setPlayPauseButtonText();
    myAlertField.flash(getMessage("ANIMATION_PAUSE"), false);
  }

  private void stopAnimationPlayIfRunning() {
    if (myMainController.isPlaying()) {
      stopAnimation();
    }
  }

  private void setPlayPauseButtonText() {
    if (myMainController.isPlaying()) {
      myPlayPauseButton.setText(getMessage("PAUSE_LABEL"));
    } else {
      myPlayPauseButton.setText(getMessage("PLAY_LABEL"));
    }
  }

  private static Text createText(String message) {
    Text text = new Text(message);
    text.setTextAlignment(TextAlignment.LEFT);
    text.setWrappingWidth(SIDEBAR_WIDTH - (ELEMENT_SPACING * 6));
    return text;
  }

}
