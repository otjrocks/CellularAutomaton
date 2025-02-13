package cellsociety.controller;

import static cellsociety.view.config.ThemeConfig.BOLD_FONT_PATH;
import static cellsociety.view.config.ThemeConfig.DEFAULT_FONT_PATH;

import cellsociety.view.config.ThemeConfig;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * A class to handle setting and updating the theme of the program
 *
 * @author Owen Jennings
 */
public class ThemeController {

  private final Scene myScene;

  /**
   * Initialize a Theme Controller
   *
   * @param stage: The stage you want the theme to be applied to
   */
  public ThemeController(Stage stage) {
    myScene = stage.getScene();
    loadRequiredFonts();
    myScene.getStylesheets().add(ThemeConfig.getThemePath()); // add theme css file
    myScene.getStylesheets().add(ThemeConfig.COMMON_STYLES_PATH); // add common css file
  }

  private void loadRequiredFonts() {
    Font.loadFont(getClass().getResourceAsStream(DEFAULT_FONT_PATH), 14);
    Font.loadFont(getClass().getResourceAsStream(BOLD_FONT_PATH), 24);
  }

  /**
   * Set and update the theme to the theme specified by the name provided
   * Note, the theme must be a valid theme name in the resources folder theme
   *
   * @param themeName: The name of the theme you wish to activate
   */
  public void setTheme(String themeName) {
    ThemeConfig.setTheme(themeName);
    myScene.getStylesheets().clear();
    myScene.getStylesheets().add(ThemeConfig.getThemePath()); // add theme css file
    myScene.getStylesheets().add(ThemeConfig.COMMON_STYLES_PATH); // add common css file
  }
}
