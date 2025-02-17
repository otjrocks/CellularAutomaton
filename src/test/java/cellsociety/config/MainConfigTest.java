package cellsociety.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MainConfigTest {

  @Test
  void getMessage_Title_ExpectedString() {
    MainConfig.setLanguage("English");
    assertEquals("Cell Society", MainConfig.getMessage("TITLE"));
  }

  @Test
  void getMessage_InvalidKey_UnknownReturned() {
    MainConfig.setLanguage("English");
    assertEquals("Unknown", MainConfig.getMessage("NOT_A_VALID_KEY"));
    MainConfig.setLanguage("Spanish");
    assertEquals("Desconocido", MainConfig.getMessage("NOT_A_VALID_KEY"));
  }

  @Test
  void setLanguage_Spanish_TitleUpdated() {
    MainConfig.setLanguage("Spanish");
    assertEquals("Sociedad de Células", MainConfig.getMessage("TITLE"));
  }

  @Test
  void setLanguage_InvalidLanguage_ExceptionThrown() {
    assertThrows(IllegalArgumentException.class, () -> MainConfig.setLanguage("NotALanguage"));
  }

  @Test
  void getCellColors_GameOfLife_DefaultWhite() {
    assertEquals("WHITE", MainConfig.getCellColors().getString("GAMEOFLIFE_COLOR_0"));
  }
}
