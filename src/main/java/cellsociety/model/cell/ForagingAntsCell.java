package cellsociety.model.cell;

import java.awt.geom.Point2D;

import cellsociety.model.simulation.rules.ForagingAntsRules.State;

public class ForagingAntsCell extends Cell {

    private int myHealth;
    private final double myHomePheromone;
    private final double myFoodPheromone;
    private final boolean myHasFood;
    private final double myMaxPher;

    public ForagingAntsCell(int state, Point2D location) {
        super(state, location);
        this.myHomePheromone = 0.0;
        this.myFoodPheromone = 0.0;
        this.myHealth = 300;
        this.myHasFood = false;
        this.myMaxPher = 100;
    }

    public ForagingAntsCell(int state, Point2D location, double homePheromone, double foodPheromone, int health, boolean hasFood, double max_pher) {
        super(state, location);
        this.myHomePheromone = homePheromone;
        this.myFoodPheromone = foodPheromone;
        this.myHealth = health;
        this.myHasFood = hasFood;
        this.myMaxPher = max_pher;
    }

    public boolean getHasFood(){
        return myHasFood;
    }

    public double getHomePheromone() {
        return myHomePheromone;
    }

    public double getFoodPheromone() {
        return myFoodPheromone;
    }

    public int getHealth() {
        return myHealth;
    }

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
        return (myHomePheromone + myFoodPheromone)/(2*myMaxPher);
    }

}
