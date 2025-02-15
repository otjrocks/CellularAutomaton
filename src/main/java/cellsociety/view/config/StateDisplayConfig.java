package cellsociety.view.config;

import static cellsociety.config.MainConfig.getCellColors;
import static cellsociety.config.MainConfig.getMessages;

import cellsociety.model.simulation.Simulation;
import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A config file to determine how to display a given state in the front end. This centralizes state
 * display configurations for different simulations.
 *
 * @author Owen Jennings
 */
// I used ChatGPT to assist with refactoring this code
public class StateDisplayConfig {

  private static final Random RANDOM = new Random();
  private static final Map<String, Color> RANDOM_COLORS_MAP = new HashMap<>();
  // store the random colors previously used for undefined states so that all cells
  // of the same state are assigned the same random color everytime they are generated.

  /**
   * Get the state information for front-end display. If no custom color or name is defined, use a
   * default name and a randomly generated color that persists.
   *
   * @param simulation The simulation you are running.
   * @param state      The state you are querying for.
   * @return The state information of the provided state and simulation.
   */
  public static StateInfo getStateInfo(Simulation simulation, int state) {
    String simulationType = simulation.data().type().toUpperCase();
    return getStateInfoFromSimulationTypeString(state, simulationType);
  }

  private static StateInfo getStateInfoFromSimulationTypeString(int state, String simulationType) {
    String nameKey = simulationType + "_NAME_" + state;
    String colorKey = simulationType + "_COLOR_" + state;

    String stateName = getStateName(nameKey, state);
    Color stateColor = getStateColor(colorKey, simulationType, state);

    return new StateInfo(stateName, stateColor);
  }

  /**
   * Retrieves the state name from the messages configuration or defaults to "STATE {state}".
   */
  private static String getStateName(String key, int state) {
    try {
      return getMessages().getString(key);
    } catch (Exception e) {
      return String.format(getMessages().getString("STATE"), state); // "State k" as default name
    }
  }

  /**
   * Retrieves the state color using reflection, falling back to a persistent randomly generated
   * color if not found.
   */
  private static Color getStateColor(String key, String simulationType, int state) {
    String stateKey = simulationType + "_" + state;

    // If a random color was already assigned, return it
    if (RANDOM_COLORS_MAP.containsKey(stateKey)) {
      return RANDOM_COLORS_MAP.get(stateKey);
    }

    try {
      Field field = Color.class.getField(getCellColors().getString(key).toUpperCase());
      Color color = (Color) field.get(null);
      RANDOM_COLORS_MAP.put(stateKey, color); // Store the found color
      return color;
    } catch (Exception e) {
      Color randomColor = getRandomColor();
      RANDOM_COLORS_MAP.put(stateKey, randomColor); // Store the random color
      return randomColor;
    }
  }

  /**
   * Generates a random color if a predefined one is not found.
   */
  private static Color getRandomColor() {
    return Color.rgb(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
  }
}
