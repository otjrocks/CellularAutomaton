package cellsociety.view.config;

import cellsociety.model.simulation.Simulation;
import javafx.scene.paint.Color;

import static cellsociety.config.MainConfig.MESSAGES;

/**
 * A config file to determine how to display a given state in the front end This centralizes state
 * display configurations for different simulations.
 *
 * @author Owen Jennings
 */
public class StateDisplayConfig {

  private static final Color DEFAULT_COLOR = Color.LIGHTBLUE;

  // I asked ChatGPT for assistance with refactoring our existing color config to this file

  /**
   * Get the state information for front-end display. If no custom color or name is defined use a
   * default name and the default color
   *
   * @param simulation The simulation you are running
   * @param state      the state you are querying for
   * @return the state information of the provided state and simulation
   */
  public static StateInfo getStateInfo(Simulation simulation, int state) {
    String simulationType = simulation.data().type();
    return switch (simulationType) {
      case "GameOfLife" -> gameOfLifeStateInfo(state);
      case "Percolation" -> percolationStateInfo(state);
      case "Segregation" -> segregationStateInfo(state);
      case "SpreadingOfFire" -> spreadingOfFireStateInfo(state);
      case "WaTorWorld" -> waTorWorldStateInfo(state);
      case "RockPaperScissors" -> rockPaperScissorsStateInfo(state);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo gameOfLifeStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(MESSAGES.getString("DEAD"), Color.WHITE);
      case 1 -> new StateInfo(MESSAGES.getString("ALIVE"), Color.BLACK);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo percolationStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(MESSAGES.getString("BLOCKED"), Color.BLACK);
      case 1 -> new StateInfo(MESSAGES.getString("OPEN"), Color.WHITE);
      case 2 -> new StateInfo(MESSAGES.getString("FILLED"), Color.BLUE);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo segregationStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(MESSAGES.getString("EMPTY"), Color.WHITE);
      case 1 -> new StateInfo(MESSAGES.getString("GROUP_ONE"), Color.RED);
      case 2 -> new StateInfo(MESSAGES.getString("GROUP_TWO"), Color.BLUE);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo spreadingOfFireStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(MESSAGES.getString("EMPTY"), Color.BLACK);
      case 1 -> new StateInfo(MESSAGES.getString("TREE"), Color.GREEN);
      case 2 -> new StateInfo(MESSAGES.getString("BURNING"), Color.RED);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo waTorWorldStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(MESSAGES.getString("EMPTY"), Color.BLACK);
      case 1 -> new StateInfo(MESSAGES.getString("FISH"), Color.GREEN);
      case 2 -> new StateInfo(MESSAGES.getString("SHARK"), Color.BLUE);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo rockPaperScissorsStateInfo(int state) {
    Color[] colors = {Color.GRAY, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
        Color.PURPLE,
        Color.MAGENTA, Color.MAROON, Color.ALICEBLUE, Color.CYAN, Color.DARKGRAY, Color.PINK,
        Color.FUCHSIA, Color.INDIGO, Color.SIENNA, Color.CRIMSON, Color.MINTCREAM,
        Color.LIGHTGREEN, Color.DARKGOLDENROD, Color.LIGHTGOLDENRODYELLOW};
    if (state >= 0 && state < colors.length) {
      return new StateInfo(MESSAGES.getString(String.valueOf(state)), colors[state]);
    }
    return getDefaultStateInfo(state);
  }

  private static StateInfo getDefaultStateInfo(int state) {
    return new StateInfo(String.format(MESSAGES.getString("STATE"), state), DEFAULT_COLOR);
  }
}
