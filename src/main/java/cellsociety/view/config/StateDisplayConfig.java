package cellsociety.view.config;

import static cellsociety.config.MainConfig.getMessages;
import cellsociety.model.simulation.Simulation;
import javafx.scene.paint.Color;


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
      case "ForagingAnts" -> ForagingAntsStateInfo(state);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo gameOfLifeStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(getMessages().getString("DEAD"), Color.WHITE);
      case 1 -> new StateInfo(getMessages().getString("ALIVE"), Color.BLACK);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo percolationStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(getMessages().getString("BLOCKED"), Color.BLACK);
      case 1 -> new StateInfo(getMessages().getString("OPEN"), Color.WHITE);
      case 2 -> new StateInfo(getMessages().getString("FILLED"), Color.BLUE);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo segregationStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(getMessages().getString("EMPTY"), Color.WHITE);
      case 1 -> new StateInfo(getMessages().getString("GROUP_ONE"), Color.RED);
      case 2 -> new StateInfo(getMessages().getString("GROUP_TWO"), Color.BLUE);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo spreadingOfFireStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(getMessages().getString("EMPTY"), Color.BLACK);
      case 1 -> new StateInfo(getMessages().getString("TREE"), Color.GREEN);
      case 2 -> new StateInfo(getMessages().getString("BURNING"), Color.RED);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo waTorWorldStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(getMessages().getString("EMPTY"), Color.BLACK);
      case 1 -> new StateInfo(getMessages().getString("FISH"), Color.GREEN);
      case 2 -> new StateInfo(getMessages().getString("SHARK"), Color.BLUE);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo ForagingAntsStateInfo(int state) {
    return switch (state) {
      case 0 -> new StateInfo(getMessages().getString("EMPTY"), Color.BLACK);
      case 1 -> new StateInfo(getMessages().getString("ANT"), Color.RED);
      case 2 -> new StateInfo(getMessages().getString("FOOD"), Color.BLUE);
      case 3 -> new StateInfo(getMessages().getString("NEST"), Color.PURPLE);
      default -> getDefaultStateInfo(state);
    };
  }

  private static StateInfo rockPaperScissorsStateInfo(int state) {
    Color[] colors = {
        Color.GRAY, Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
        Color.PURPLE, Color.MAGENTA, Color.MAROON, Color.ALICEBLUE, Color.CYAN, Color.DARKGRAY,
        Color.PINK, Color.FUCHSIA, Color.INDIGO, Color.SIENNA, Color.CRIMSON, Color.MINTCREAM,
        Color.LIGHTGREEN, Color.DARKGOLDENROD, Color.LIGHTGOLDENRODYELLOW
    };

    String[] states = {
        "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN",
        "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN",
        "NINETEEN", "TWENTY"
    };

    if (state >= 0 && state < states.length) {
      return new StateInfo(getMessages().getString(states[state]), colors[state]);
    }
    return getDefaultStateInfo(state);
  }


  private static StateInfo getDefaultStateInfo(int state) {
    return new StateInfo(String.format(getMessages().getString("STATE"), state), DEFAULT_COLOR);
  }
}
