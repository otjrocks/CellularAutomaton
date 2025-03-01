package cellsociety.view.config;

import cellsociety.controller.PreferencesController;
import cellsociety.utility.FileUtility;
import java.util.List;

/**
 * A class to handle to initialization and changing of the program's theme
 *
 * @author Owen Jennings
 */
public class ThemeConfig {

  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.";
  public static final String DEFAULT_RESOURCE_FOLDER =
      "/%s".formatted(DEFAULT_RESOURCE_PACKAGE.replace(".", "/"));
  public static final String COMMON_STYLES_PATH = "%sstyles.css".formatted(DEFAULT_RESOURCE_FOLDER);
  public static final String DEFAULT_FONT_PATH = "%sfonts.default.ttf".formatted(
      DEFAULT_RESOURCE_FOLDER);
  public static final String BOLD_FONT_PATH = "%sfonts.bold.ttf".formatted(DEFAULT_RESOURCE_FOLDER);
  public static final String THEMES_RESOURCES_FOLDER = "%sthemes/".formatted(
      DEFAULT_RESOURCE_FOLDER);
  public static final String THEMES_RELATIVE_PATH = "src/main/resources/cellsociety/themes/";
  public static final List<String> THEMES = FileUtility.getFileNamesInDirectory(
      THEMES_RELATIVE_PATH, ".css");

  public static final String DEFAULT_THEME = "Light";

  public static final String THEME_KEY = "theme";
  private static String myThemePath =
      "%s%s.css".formatted(THEMES_RESOURCES_FOLDER,
          PreferencesController.getPreference(THEME_KEY, DEFAULT_THEME));
  private static String myCurrentTheme = PreferencesController.getPreference(THEME_KEY,
      DEFAULT_THEME);

  /**
   * Get the path of the current theme's css file
   *
   * @return the path of the current theme's css file
   */
  public static String getThemePath() {
    return myThemePath;
  }

  /**
   * Get the string of the current theme
   *
   * @return String representing the current theme name
   */
  public static String getCurrentTheme() {
    return myCurrentTheme;
  }

  /**
   * Set the theme to the provided theme name. Requires that a file named "themeName.css" exists in
   * the themes folder
   *
   * @param themeName: Name of the theme you want to set
   */
  public static void setTheme(String themeName) {
    myThemePath = "%s%s.css".formatted(THEMES_RESOURCES_FOLDER, themeName);
    PreferencesController.setPreference(THEME_KEY, themeName);
    myCurrentTheme = themeName;
  }
}
