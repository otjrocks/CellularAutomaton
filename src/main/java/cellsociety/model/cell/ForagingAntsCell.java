package cellsociety.model.cell;

import java.awt.geom.Point2D;

public class ForagingAntsCell extends Cell {

    private double homePheromone;
    private double foodPheromone;
    private int health;
    private int reproductionTime;

    public ForagingAntsCell(int state, Point2D location) {
        super(state, location);
        this.homePheromone = 0.0;
        this.foodPheromone = 0.0;
        this.health = 10;
        this.reproductionTime = 0;
    }

    public ForagingAntsCell(int state, Point2D location, double homePheromone, double foodPheromone, int health, int reproductionTime) {
        super(state, location);
        this.homePheromone = homePheromone;
        this.foodPheromone = foodPheromone;
        this.health = health;
        this.reproductionTime = reproductionTime;
    }

    public double getHomePheromone() {
        return homePheromone;
    }

    public void setHomePheromone(double homePheromone) {
        this.homePheromone = homePheromone;
    }

    public double getFoodPheromone() {
        return foodPheromone;
    }

    public void setFoodPheromone(double foodPheromone) {
        this.foodPheromone = foodPheromone;
    }

    public int getHealth() {
        return health;
    }

    public int getReproductionTime() {
        return reproductionTime;
    }

    public void reduceHealth(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public void resetReproductionTime() {
        this.reproductionTime = 0;
    }

    public void incrementReproductionTime() {
        this.reproductionTime++;
    }

    public void dropHomePheromone(double pheromoneAmount) {
        this.homePheromone += pheromoneAmount;
    }

    public void dropFoodPheromone(double pheromoneAmount) {
        this.foodPheromone += pheromoneAmount;
    }

    public void decayPheromones(double decayRate) {
        this.homePheromone *= (1 - decayRate);
        this.foodPheromone *= (1 - decayRate);
    }

}
