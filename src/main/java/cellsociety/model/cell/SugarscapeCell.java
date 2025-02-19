package cellsociety.model.cell;

import cellsociety.model.simulation.rules.SugarscapeRules;
import java.awt.geom.Point2D;

public class SugarscapeCell extends Cell {
  private int sugar;
  private int sugarGrowBackRate;
  private int sugarGrowBackInterval;
  private int intervalsSinceLastGrowBack;

  private int vision;
  private int metabolism;
  private int agentSugar;
  private final int DEFAULT_VALUE = 2;



  public SugarscapeCell(int state, Point2D location) {
    super(state, location);
    sugar = DEFAULT_VALUE*3;
    sugarGrowBackRate = DEFAULT_VALUE;
    sugarGrowBackInterval = DEFAULT_VALUE;
    intervalsSinceLastGrowBack = DEFAULT_VALUE;
    vision = DEFAULT_VALUE;
    metabolism = DEFAULT_VALUE;
    agentSugar = DEFAULT_VALUE;

  }

  /**
   * Creates a Sugarscape Cell that can act as a Patch or an Agent.
   *
   * @param state - The type of cell (EMPTY, PATCH, or AGENT)
   * @param location - The cell's location in the grid
   * @param sugar - Initial sugar level
   * @param sugarGrowBackInterval - time required for sugar to grow back (only for patches)
   * @param sugarGrowBackRate - amount of sugar restored after the interval (only for patches)
   * @param vision - how far in each 4 directions the agent should look (only for agents)
   * @param metabolism - sugar consumption per turn (only for agents)
   */
  public SugarscapeCell(int state, Point2D location, int sugar, int sugarGrowBackInterval,
      int sugarGrowBackRate, int vision, int metabolism) {
    super(state, location);

    if (sugar < 0 || sugarGrowBackRate < 0 || sugarGrowBackInterval < 1 || vision < 0 || metabolism < 0) {
      throw new IllegalArgumentException("All ints must be non-negative.");
    }

    this.sugar = sugar;
    this.sugarGrowBackInterval = sugarGrowBackInterval;
    this.sugarGrowBackRate = sugarGrowBackRate;
    this.intervalsSinceLastGrowBack = 0;

    this.vision = vision;
    this.metabolism = metabolism;
  }


  /**
   * check if a cell is an agent
   * @return - boolean on if the cell is an agent
   */
  public boolean isAgent() {
    return getState() == SugarscapeRules.State.AGENTS.getValue();
  }

  /**
   * check if a cell is a patch
   * @return - boolean on if the cell is a patch cell
   */
  public boolean isPatch() {
    return getState() == SugarscapeRules.State.PATCHES.getValue();
  }


  /**
   * getter for the vision
   * @return - the number of elements it searches for in the 4 directions for the agent
   */
  public int getVision() {
    return vision;
  }

  /**
   * getter for the metabolism
   * @return - an integer representation of sugar consumed per round for the agent
   */
  public int getMetabolism() {
    return metabolism;
  }

  /**
   * a getter for the sugar
   * @return - an integer representation of the agent/patch's current amount of sugar
   */
  public int getSugar() {
    return sugar;
  }


  /**
   * setter for the sugar
   * @param sugar - the current sugar contained in the cell
   */
  public void setSugar(int sugar) {
    if (sugar < 0) {
      throw new IllegalArgumentException("All ints must be non-negative.");
    }
    this.sugar = sugar;
  }

  /**
   * getter for the sugar grow back rate
   * @return - the int value of how much to increment the sugar by
   */
  public int getSugarGrowBackRate() {
    return sugarGrowBackRate;
  }

  /**
   * getter for the sugar grow back interval
   * @return - the int representation of the time it takes before sugar can be replenished.
   */
  public int getSugarGrowBackInterval() {
    return sugarGrowBackInterval;
  }

  /**
   * setter to increment the current sugar by the grow back rate of sugar and reset the intervals back to 0
   */
  public void regenerateSugar() {
    if (!isPatch()) {
      return;
    }

    intervalsSinceLastGrowBack++;
    if (intervalsSinceLastGrowBack >= sugarGrowBackInterval) {
      sugar += sugarGrowBackRate;
      intervalsSinceLastGrowBack = 0;
    }
  }

  /**
   * sets the parameters of each cell based on the provided parmaters
   *
   * @param sugar - sugar value of the cell
   * @param sugarGrowBackRate - increment that the sugar gets on a certain interval
   * @param sugarGrowBackInterval - the interval needed to increment sugar
   * @param vision - how far an agent cell can search in each direction
   * @param metabolism - how much sugar is consumed per round
   */
  public void setParameters(int sugar, int sugarGrowBackRate, int sugarGrowBackInterval, int vision, int metabolism) {
    this.sugar = sugar;
    this.sugarGrowBackRate = sugarGrowBackRate;
    this.sugarGrowBackInterval = sugarGrowBackInterval;
    this.vision = vision;
    this.metabolism = metabolism;
  }

}
