package cellsociety.model.cell;

import java.awt.geom.Point2D;

public class ForagingAntsCell extends Cell {

    private final double homePheromone;
    private final double foodPheromone;
    private int health;
    private final boolean hasFood;

    public ForagingAntsCell(int state, Point2D location) {
        super(state, location);
        this.homePheromone = 0.0;
        this.foodPheromone = 0.0;
        this.health = 10;
        this.hasFood = false;
    }

    public ForagingAntsCell(int state, Point2D location, double homePheromone, double foodPheromone, int health, boolean hasFood) {
        super(state, location);
        this.homePheromone = homePheromone;
        this.foodPheromone = foodPheromone;
        this.health = health;
        this.hasFood = hasFood;
    }

    public boolean getHasFood(){
        return hasFood;
    }

    public double getHomePheromone() {
        return homePheromone;
    }

    public double getFoodPheromone() {
        return foodPheromone;
    }

    public int getHealth() {
        return health;
    }

    public void reduceHealth(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

}
