package cellsociety.view;

import static cellsociety.config.MainConfig.getMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;

import cellsociety.controller.MainController;
import cellsociety.utility.CreateNewSimulation;
import cellsociety.view.components.AlertField;
import cellsociety.view.config.ThemeConfig;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.DukeApplicationTest;
import util.TestUtils;

class SidebarViewTest extends DukeApplicationTest {

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
  public void modeButton_EnsureButtonTextIsEditInitially_Success() {
    myTestUtils.verifyButtonText("#sidebarModeButton", "EDIT_MODE");
  }

  @Test
  public void modeButton_EnsureButtonTextSwitchesOnClick_Success() {
    // Initially mode is View so button say switch to edit mode
    myTestUtils.verifyButtonText("#sidebarModeButton", "EDIT_MODE");
    // ensure click on button alerts switch to edit mode and button text changes
    clickOn("#sidebarModeButton");
    String expectedAlert = String.format(getMessage("INFO_PREFIX"),
        getMessage("EDIT_MODE_ENABLED"));
    assertTrue(myAlertField.getMessages().contains(expectedAlert));
    myTestUtils.verifyButtonText("#sidebarModeButton", "VIEW_MODE");
    // ensure switch back to view mode displays info alert and button text changes back to edit mode
    clickOn("#sidebarModeButton");
    expectedAlert = String.format(getMessage("INFO_PREFIX"),
        getMessage("EDIT_MODE_DISABLED"));
    assertTrue(myAlertField.getMessages().contains(expectedAlert));
    myTestUtils.verifyButtonText("#sidebarModeButton", "EDIT_MODE");
  }

  @Test
  public void themeSelector_EnsureInitialThemeIsSelected_Success() {
    ComboBox<String> themeSelector = lookup("#themeDropdown").queryComboBox();
    // ensure current selected theme is current theme
    assertEquals(themeSelector.getValue(), ThemeConfig.getCurrentTheme());
  }

  @Test
  public void themeSelector_ThemeUpdate_Success() {
    ComboBox<String> themeSelector = lookup("#themeDropdown").queryComboBox();
    // ensure current selected theme is current theme
    assertEquals(themeSelector.getValue(), ThemeConfig.getCurrentTheme());

    clickOn("#themeDropdown");
    clickOn(themeSelector.getItems().getFirst());
    assertEquals(ThemeConfig.getCurrentTheme(), themeSelector.getItems().getFirst());
  }

  @Test
  public void resetButton_ResetsZoom_Success() {
    //ensures the button is clickable
    clickOn("#resetZoomButton");
  }

}