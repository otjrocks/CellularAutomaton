package cellsociety.config;

public class LanguageConfig {
  private final String language;

  public LanguageConfig(String language) {
    this.language = language;
  }

  /**
   *
   * @return - the configured language of the app
   */
  public String getLanguage() {
    return language;
  }

}
