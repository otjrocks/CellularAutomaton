package cellsociety.model.cell;

import java.awt.geom.Point2D;

import cellsociety.model.simulation.rules.ForagingAntsRules.State;

/**
 * The cell type used for Foraging ants simulation.
 *
 * @author Troy Ludwig
 * @author Owen Jennings
 */
public class ForagingAntsCell extends Cell {

  private int myHealth;
  private final double myHomePheromone;
  private final double myFoodPheromone;
  private final boolean myHasFood;
  private final double myMaxPher;

  /**
   * The default constructor for a foraging ant cell.
   *
   * @param state    The initial state for the cell
   * @param location THe initial location for the cell
   */
  public ForagingAntsCell(int state, Point2D location) {
    super(state, location);
    this.myHomePheromone = 0.0;
    this.myFoodPheromone = 0.0;
    this.myHealth = 300;
    this.myHasFood = false;
    this.myMaxPher = 100;
  }

  /**
   * A constructor for foraging ant cell with more initial configuration.
   *
   * @param state         The initial state
   * @param location      The initial location
   * @param homePheromone The initial home pheromone
   * @param foodPheromone The initial food pheromone
   * @param health        The initial health
   * @param hasFood       If the cell initially has health
   * @param max_pher      The max pheromone amount for this cell
   */
  public ForagingAntsCell(int state, Point2D location, double homePheromone, double foodPheromone,
      int health, boolean hasFood, double maxPher) {
    super(state, location);
    this.myHomePheromone = homePheromone;
    this.myFoodPheromone = foodPheromone;
    this.myHealth = health;
    this.myHasFood = hasFood;
    this.myMaxPher = maxPher;
  }

  /**
   * Check if cell has food.
   *
   * @return true if the cell has food, false otherwise
   */
  public boolean getHasFood() {
    return myHasFood;
  }

  /**
   * Get the home pheromone for the cell.
   *
   * @return A double representing the home pheromone
   */
  public double getHomePheromone() {
    return myHomePheromone;
  }

  /**
   * Get the food pheromone for the cell.
   *
   * @return The food pheromone
   */
  public double getFoodPheromone() {
    return myFoodPheromone;
  }

  /**
   * The health of the cell.
   *
   * @return An int representing the cell health
   */
  public int getHealth() {
    return myHealth;
  }

  /**
   * Reduce the health of the cell by a determined amount or 0 if less than 0.
   *
   * @param amount The amount to reduce health by
   */
  public void reduceHealth(int amount) {
    this.myHealth -= amount;
    if (this.myHealth < 0) {
      this.myHealth = 0;
    }
  }

  @Override
  public double getOpacity() {
    if (getState() != State.EMPTY.getValue()) {
      return 1.0;
    }
    return (myHomePheromone + myFoodPheromone) / (2 * myMaxPher);
  }

}
