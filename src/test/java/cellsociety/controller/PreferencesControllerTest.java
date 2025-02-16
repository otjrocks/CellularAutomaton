package cellsociety.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.prefs.Preferences;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PreferencesControllerTest {

  private static final String TEST_KEY = "testKey";
  private static final String TEST_VALUE = "testValue";
  private static final String DEFAULT_VALUE = "defaultValue";
  private static final Preferences preferences = Preferences.userNodeForPackage(
      PreferencesController.class);

  @BeforeEach
  void setUp() {
    preferences.remove(TEST_KEY); // Ensure an empty preferences before each test
  }

  @Test
  void setPreference_newKey_valueSaved() {
    PreferencesController.setPreference(TEST_KEY, TEST_VALUE);
    assertEquals(TEST_VALUE, preferences.get(TEST_KEY, DEFAULT_VALUE));
  }

  @Test
  void getPreference_existingKey_returnsStoredValue() {
    preferences.put(TEST_KEY, TEST_VALUE);
    assertEquals(TEST_VALUE, PreferencesController.getPreference(TEST_KEY, DEFAULT_VALUE));
  }

  @Test
  void getPreference_nonExistingKey_returnsDefaultValue() {
    assertEquals(DEFAULT_VALUE, PreferencesController.getPreference(TEST_KEY, DEFAULT_VALUE));
  }

  @Test
  void getAndSetPreferences_SameKey_returnsCorrectValue() {
    // test to make sure preference changes are automatically flushed to file
    PreferencesController.setPreference(TEST_KEY, TEST_VALUE);
    assertEquals(TEST_VALUE, PreferencesController.getPreference(TEST_KEY, DEFAULT_VALUE));
  }
}
