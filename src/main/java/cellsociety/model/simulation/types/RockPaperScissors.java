package cellsociety.model.simulation.types;

import static cellsociety.config.MainConfig.MESSAGES;

import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.model.simulation.SimulationRules;
import cellsociety.model.simulation.StateInfo;
import javafx.scene.paint.Color;

public class RockPaperScissors extends Simulation {
  public RockPaperScissors(SimulationRules rules, SimulationMetaData data) {
    super(rules, data);
  }

  @Override
  protected void initializeStateMap() {
    stateMap.put(0, new StateInfo(MESSAGES.getString("EMPTY"), Color.WHITE));
    stateMap.put(1, new StateInfo(MESSAGES.getString("ONE"), Color.RED));
    stateMap.put(2, new StateInfo(MESSAGES.getString("TWO"), Color.ORANGE));
    stateMap.put(3, new StateInfo(MESSAGES.getString("THREE"), Color.YELLOW));
    stateMap.put(4, new StateInfo(MESSAGES.getString("FOUR"), Color.GREEN));
    stateMap.put(5, new StateInfo(MESSAGES.getString("FIVE"), Color.BLUE));
    stateMap.put(6, new StateInfo(MESSAGES.getString("SIX"), Color.PURPLE));
    stateMap.put(7, new StateInfo(MESSAGES.getString("SEVEN"), Color.MAGENTA));
    stateMap.put(8, new StateInfo(MESSAGES.getString("EIGHT"), Color.MAROON));
    stateMap.put(9, new StateInfo(MESSAGES.getString("NINE"), Color.ALICEBLUE));
    stateMap.put(10, new StateInfo(MESSAGES.getString("TEN"), Color.CYAN));
    stateMap.put(11, new StateInfo(MESSAGES.getString("ELEVEN"), Color.DARKGRAY));
    stateMap.put(12, new StateInfo(MESSAGES.getString("TWELVE"), Color.PINK));
    stateMap.put(13, new StateInfo(MESSAGES.getString("THIRTEEN"), Color.FUCHSIA));
    stateMap.put(14, new StateInfo(MESSAGES.getString("FOURTEEN"), Color.INDIGO));
    stateMap.put(15, new StateInfo(MESSAGES.getString("FIFTEEN"), Color.SIENNA));
    stateMap.put(16, new StateInfo(MESSAGES.getString("SIXTEEN"), Color.CRIMSON));
    stateMap.put(17, new StateInfo(MESSAGES.getString("SEVENTEEN"), Color.MINTCREAM));
    stateMap.put(18, new StateInfo(MESSAGES.getString("EIGHTEEN"), Color.LIGHTGREEN));
    stateMap.put(19, new StateInfo(MESSAGES.getString("NINETEEN"), Color.DARKGOLDENROD));
    stateMap.put(20, new StateInfo(MESSAGES.getString("TWENTY"), Color.LIGHTGOLDENRODYELLOW));
  }
}
