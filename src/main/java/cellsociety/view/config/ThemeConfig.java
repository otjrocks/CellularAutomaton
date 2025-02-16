package cellsociety.view.config;

import cellsociety.controller.PreferencesController;

public class ThemeConfig {

  /**
   * A list of all themes available
   */
  public static final String[] THEMES = new String[]{
      "Light",
      "Dark",
      "Neon",
      "Green",
      "Night"
  };

  public static final String DEFAULT_RESOURCE_PACKAGE = "cellsociety.";
  public static final String DEFAULT_RESOURCE_FOLDER =
      "/" + DEFAULT_RESOURCE_PACKAGE.replace(".", "/");
  public static final String COMMON_STYLES_PATH = DEFAULT_RESOURCE_FOLDER + "styles.css";
  public static final String DEFAULT_FONT_PATH = DEFAULT_RESOURCE_FOLDER + "fonts.default.ttf";
  public static final String BOLD_FONT_PATH = DEFAULT_RESOURCE_FOLDER + "fonts.bold.ttf";
  public static final String THEMES_RESOURCES_FOLDER = DEFAULT_RESOURCE_FOLDER + "themes/";

  private static String myThemePath =
      THEMES_RESOURCES_FOLDER + PreferencesController.getPreference("theme", "Light") + ".css";
  private static String myCurrentTheme = THEMES[0];

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
    myThemePath = THEMES_RESOURCES_FOLDER + themeName + ".css";
    PreferencesController.setPreference("theme", themeName);
    myCurrentTheme = themeName;
  }
}
