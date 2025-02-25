package cellsociety.view;

import static cellsociety.config.MainConfig.getMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;

import cellsociety.controller.MainController;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.rules.PercolationRules;
import cellsociety.utility.CreateNewSimulation;
import cellsociety.view.components.AlertField;
import java.awt.Button;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import util.DukeApplicationTest;
import util.TestUtils;

class ViewModeViewTest extends DukeApplicationTest {

  private MainController myMainController;
  private MainController myMainControllerSpy;
  private AlertField myAlertField;
  private final TestUtils myTestUtils = new TestUtils();

  @Override
  public void start(Stage stage) {
    CreateNewSimulation createNewSimulation = new CreateNewSimulation();
    myMainController = createNewSimulation.launchNewSimulation();
    myMainController.hideSplashScreen(); // hide splash screen to jump directly to main screen
    myMainControllerSpy = Mockito.spy(myMainController);
    myAlertField = lookup("#sidebarAlertField").query();
  }

  @BeforeEach
  public void setup() {
    reset(myMainControllerSpy);
  }

  @Test
  public void playPauseButton_VerifyInitialTextPlay_Success() {
    myTestUtils.verifyButtonText("#viewModePlayPauseButton", "PLAY_LABEL");
  }

  @Test
  public void playPauseButton_VerifyInfoAlert_Success() throws InterruptedException {
    clickOn("#viewModePlayPauseButton");
    String expected = String.format(getMessage("INFO_PREFIX"), getMessage("ANIMATION_START"));
    myTestUtils.verifyAlertFieldContains(myAlertField, expected);
    Thread.sleep(
        (AlertField.ALERT_DURATION_SECONDS + 1) * 1000); // wait until all alerts have cleared
    clickOn("#viewModePlayPauseButton");
    expected = String.format(getMessage("INFO_PREFIX"), getMessage("ANIMATION_PAUSE"));
    myTestUtils.verifyAlertFieldContains(myAlertField, expected);
  }


  @Test
  public void playPauseButton_VerifyTextSwitchesOnClick_Success() {
    myTestUtils.verifyButtonText("#viewModePlayPauseButton", "PLAY_LABEL");
    clickOn("#viewModePlayPauseButton");
    myTestUtils.verifyButtonText("#viewModePlayPauseButton", "PAUSE_LABEL");
    clickOn("#viewModePlayPauseButton");
    myTestUtils.verifyButtonText("#viewModePlayPauseButton", "PLAY_LABEL");
  }

  @Test
  public void playPauseButton_VerifyPlayPauseButtonStartsAnimation_Success() {
    assertFalse(myMainController.isPlaying());
    clickOn("#viewModePlayPauseButton");
    assertTrue(myMainController.isPlaying());
    clickOn("#viewModePlayPauseButton");
    assertFalse(myMainController.isPlaying());
  }

  @Test
  public void stepButton_VerifyPausesAnimationIfRunning_Success() {
    clickOn("#viewModePlayPauseButton");
    assertTrue(myMainController.isPlaying());
    clickOn("#viewModeStepButton");
    assertFalse(
        myMainController.isPlaying()); // animation should stop if single step button is clicked
  }

  @Test
  public void speedSlider_VerifyDoesNotChangeIfAnimationIsPlaying_Success() {
    Slider slider = lookup("#viewModeSpeedSlider").query();
    assertFalse(myMainController.isPlaying());
    setValue(slider, slider.getValue() - 1);
    assertFalse(myMainController.isPlaying());
    clickOn("#viewModePlayPauseButton");
    assertTrue(myMainController.isPlaying());
    setValue(slider, slider.getValue() + 1);
    assertTrue(myMainController.isPlaying());
  }

  @Test
  public void simulationMetaDataDisplay_VerifyCorrectMetaData_Success() {
    myTestUtils.verifyText("#viewModeSimulationType",
        String.format("%s %s", getMessage("TYPE_LABEL"), "GameOfLife"));
    myTestUtils.verifyText("#viewModeSimulationName",
        String.format("%s %s", getMessage("NAME_LABEL"), "Glider"));
    myTestUtils.verifyText("#viewModeSimulationAuthor",
        String.format("%s %s", getMessage("AUTHOR_LABEL"), "Richard K. Guy"));
    myTestUtils.verifyText("#viewModeSimulationDescription",
        String.format("%s %s", getMessage("DESCRIPTION_LABEL"),
            "A basic configuration that produces a \"glider\" that moves diagonally across the grid"));
  }

}