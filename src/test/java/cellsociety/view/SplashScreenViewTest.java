package cellsociety.view;

import cellsociety.controller.MainController;
import cellsociety.utility.CreateNewSimulation;
import cellsociety.view.config.ThemeConfig;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static cellsociety.config.MainConfig.getMessage;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SplashScreenViewTest extends ApplicationTest {

  // I used ChatGPT for assistance in overriding the start method to get the view to show in TestFX and in refactoring my code into helper methods
  private MainController myMainController;

  @Override
  public void start(Stage stage) {
    CreateNewSimulation createNewSimulation = new CreateNewSimulation(stage);
    myMainController = Mockito.spy(createNewSimulation.launchNewSimulation());
    SplashScreenView mySplashScreenView = myMainController.getSplashScreen();
    stage.setScene(mySplashScreenView.getScene());
    stage.show();
  }

  @BeforeEach
  public void setup() {
    reset(myMainController);
  }

  @Test
  void languageDropdown_TestSpanishChange_ExpectSpanishText() {
    verifyLanguageChange("Spanish");
  }

  @Test
  void languageDropdown_TestEnglishChange_ExpectEnglishText() {
    verifyLanguageChange("English");
  }

  @Test
  void languageDropdown_TestPigLatinChange_ExpectPigLatinText() {
    verifyLanguageChange("PigLatin");
  }

  /**
   * Helper method to test language changes and verify UI text updates
   *
   * @param language the language to select from the dropdown
   */
  private void verifyLanguageChange(String language) {
    clickOn("#languageDropdown");
    clickOn(language);

    // Verify text elements and buttons change on language change
    verifyText("#splashScreenTitle", "SPLASH_HEADER");
    verifyText("#splashDescription", "SPLASH_DESCRIPTION");
    verifyText("#splashInstructions", "SPLASH_INSTRUCTIONS");
    verifyButtonText("#splashChooseFileButton", "CHOOSE_FILE_BUTTON");
    verifyButtonText("#createSimulationButton", "CREATE_NEW_GRID_HEADER");
  }

  /**
   * Helper method to verify if a specific Text node matches expected text
   *
   * @param fxId       the FX ID of the Text node
   * @param messageKey the message key from config
   */
  private void verifyText(String fxId, String messageKey) {
    Text textNode = lookup(fxId).queryAs(Text.class);
    assertEquals(getMessage(messageKey), textNode.getText());
  }

  /**
   * Helper method to verify if a specific Button node matches expected text
   *
   * @param fxId       the FX ID of the Button
   * @param messageKey the message key from config
   */
  private void verifyButtonText(String fxId, String messageKey) {
    Button button = lookup(fxId).queryAs(Button.class);
    assertEquals(getMessage(messageKey), button.getText());
  }
}
