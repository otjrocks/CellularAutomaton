package cellsociety.view.config;

import cellsociety.config.MainConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.scene.paint.Color;

import static cellsociety.config.MainConfig.getCellColors;
import static cellsociety.config.MainConfig.getMessage;

import cellsociety.model.simulation.Simulation;

/**
 * A config file to determine how to display a given state in the front end. This centralizes state
 * display configurations for different simulations.
 *
 * @author Owen Jennings
 */
public class StateDisplayConfig {

  private static final Random RANDOM = new Random();
  private static final Map<String, Color> RANDOM_COLORS_MAP = new HashMap<>();
  private static final Map<StateCacheKey, StateInfo> STATE_INFO_CACHE = new HashMap<>();

  /**
   * Get the state information for front-end display. Uses caching to avoid redundant computations.
   *
   * @param simulation The simulation you are running.
   * @param state      The state you are querying for.
   * @return The state information of the provided state and simulation.
   */
  public static StateInfo getStateInfo(Simulation simulation, int state) {
    StateCacheKey cacheKey = new StateCacheKey(simulation, state);

    // ChatGPT assisted in creating a cache to store values instead of looking up everytime to improve efficiency
    return STATE_INFO_CACHE.computeIfAbsent(cacheKey, key -> {
      String simulationType = simulation.data().type().toUpperCase();
      return getStateInfoFromSimulationTypeString(state, simulationType);
    });
  }

  public static StateInfo getStateInfo(Simulation simulation, int state, String language) {
    String currentLanguage = MainConfig.getLanguage();
    MainConfig.setLanguage(language);
    StateCacheKey cacheKey = new StateCacheKey(simulation, state);

    // ChatGPT assisted in creating a cache to store values instead of looking up everytime to improve efficiency
    StateInfo result = STATE_INFO_CACHE.computeIfAbsent(cacheKey, key -> {
      String simulationType = simulation.data().type().toUpperCase();
      return getStateInfoFromSimulationTypeString(state, simulationType);
    });
    MainConfig.setLanguage(currentLanguage); // reset to original language
    return result;
  }

  private static StateInfo getStateInfoFromSimulationTypeString(int state, String simulationType) {
    String nameKey = "%s_NAME_%d".formatted(simulationType, state);
    String colorKey = "%s_COLOR_%d".formatted(simulationType, state);

    String stateName = getStateName(nameKey, state);
    Color stateColor = getStateColor(colorKey, simulationType, state);

    return new StateInfo(state, stateName, stateColor);
  }

  private static String getStateName(String key, int state) {
    String stateName = getMessage(key);
    if (stateName.equals(getMessage("MISSING_KEY")) || stateName.equals("UNKNOWN")) {
      return String.format(getMessage("STATE"), state);
    }
    return stateName;
  }

  private static Color getStateColor(String key, String simulationType, int state) {
    String stateKey = "%s_%d".formatted(simulationType, state);

    if (RANDOM_COLORS_MAP.containsKey(stateKey)) {
      return RANDOM_COLORS_MAP.get(stateKey);
    }
    return attemptGettingColorFromPropertyFileOrReturnDefaultColor(key, stateKey);
  }

  private static Color attemptGettingColorFromPropertyFileOrReturnDefaultColor(String key,
      String stateKey) {
    try {
      Color color = Color.valueOf(getCellColors().getString(key).toUpperCase());
      RANDOM_COLORS_MAP.put(stateKey, color);
      return color;
    } catch (Exception e) {
      Color randomColor = getRandomColor();
      RANDOM_COLORS_MAP.put(stateKey, randomColor);
      return randomColor;
    }
  }

  private static Color getRandomColor() {
    return Color.rgb(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
  }

  /**
   * A key for caching state information, using a record for simplicity.
   */
  private record StateCacheKey(Simulation simulation, int state) {

  }
}
