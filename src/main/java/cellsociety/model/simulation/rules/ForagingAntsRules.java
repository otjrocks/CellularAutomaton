package cellsociety.model.simulation.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cellsociety.model.Grid;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.cell.ForagingAntsCell;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;

public class ForagingAntsRules extends SimulationRules {

    private final Random random = new Random();
    private final int myPheromoneDecayRate;
    private final int myAntReproductionTime;
    private final int myMaxPheromoneAmount;

    private int myReproductionTimer = 0;
    private final List<Cell> nestCells = new ArrayList<>();
    private final List<Cell> nestCellNeighbors = new ArrayList<>();

        public ForagingAntsRules(Map<String, Parameter<?>> parameters) throws InvalidParameterException{
        super(parameters);
        if (parameters == null || parameters.isEmpty()) {
        setParameters(setDefaultParameters());
        }
        checkMissingParameterAndThrowException("pheromoneDecayRate");
        checkMissingParameterAndThrowException("antReproductionTime");
        checkMissingParameterAndThrowException("maxPheromoneAmount");
        myPheromoneDecayRate = getParameters().get("pheromoneDecayRate").getInteger();
        myAntReproductionTime = getParameters().get("antReproductionTime").getInteger();
        myMaxPheromoneAmount = getParameters().get("maxPheromoneAmount").getInteger();
        validateParameterRange();
    }

    private void validateParameterRange() throws InvalidParameterException {
        if (myPheromoneDecayRate < 0) {
        throwInvalidParameterException("pheromoneDecayRate");
        }
        if (myAntReproductionTime < 0) {
        throwInvalidParameterException("antReproductionTime");
        }
        if (myMaxPheromoneAmount < 0) {
        throwInvalidParameterException("maxPheromoneAmount");
        }
    }

    private Map<String, Parameter<?>> setDefaultParameters() {
        Map<String, Parameter<?>> parameters = new HashMap<>();

        parameters.put("pheromoneDecayRate", new Parameter<>(3));
        parameters.put("antReproductionTime", new Parameter<>(4));
        parameters.put("maxPheromoneAmount", new Parameter<>(1));

        return parameters;
    }

    public enum State {
        EMPTY(0),
        ANT(1),
        FOOD(2),
        NEST(3);

        private final int value;

        State(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static void fromValue(int value) {
            for (State state : values()) {
                if (state.value == value) {
                    return;
                }
            }
            throw new IllegalArgumentException("Invalid AntState value: " + value);
        }
    }

    public List<Cell> getNeighbors(Cell cell, Grid grid) {
        return super.getNeighbors(cell, grid, false);
    }

    @Override
    public int getNextState(Cell cell, Grid grid) {
        return -1; // Not used
    }

    @Override
    public int getNumberStates() {
        return 4;
    }

    @Override
    public List<CellUpdate> getNextStatesForAllCells(Grid grid) {
        List<CellUpdate> nextStates = new ArrayList<>();
        List<Cell> antCells = new ArrayList<>();
        myReproductionTimer++;

        Iterator<Cell> cellIterator = grid.getCellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getState() == State.ANT.getValue()) {
                antCells.add(cell);
            }
            if (cell.getState() == State.NEST.getValue()) {
                nestCells.add(cell);
            }
        }

        for (Cell ant : antCells) {
            processAntMovement(ant, grid, nextStates);
        }

        if(myReproductionTimer >= myAntReproductionTime){
            for (Cell nestCell: nestCells){
                for(Cell neighbor: getNeighborsByState(nestCell, grid, State.EMPTY.getValue())){
                    nestCellNeighbors.add(neighbor);
                }
            }
            ForagingAntsCell newAnt = (ForagingAntsCell)nestCellNeighbors.get(random.nextInt(nestCellNeighbors.size()));
            nextStates.add(new CellUpdate(newAnt.getLocation(), new ForagingAntsCell(State.ANT.getValue(), newAnt.getLocation(), newAnt.getHomePheromone(), newAnt.getFoodPheromone(), 10, false)));
            myReproductionTimer = 0;
        }

        return nextStates;
    }

    private void processAntMovement(Cell cell, Grid grid, List<CellUpdate> nextStates) {
        ForagingAntsCell ant = (ForagingAntsCell) cell;
        ant.reduceHealth(1);
        List<Cell> emptyNeighbors = getNeighborsByState(ant, grid, State.EMPTY.getValue());
        List<Cell> foodNeighbors = getNeighborsByState(ant, grid, State.FOOD.getValue());
        List<Cell> nestNeighbors = getNeighborsByState(ant, grid, State.NEST.getValue());

        if(!ant.getHasFood()){
            if (!foodNeighbors.isEmpty() && !emptyNeighbors.isEmpty()){
                ForagingAntsCell nextLocation = (ForagingAntsCell)highestPheromoneNeighbor(emptyNeighbors, "home");
                nextStates.add(new CellUpdate(nextLocation.getLocation(), new ForagingAntsCell(State.ANT.getValue(), nextLocation.getLocation(), updateHomePheromone(nextLocation, grid), updateFoodPheromone(nextLocation, grid), ant.getHealth(), true)));
                if (!nextLocation.getLocation().equals(ant.getLocation())) {
                    Cell newEmpty = new ForagingAntsCell(State.EMPTY.getValue(), ant.getLocation(), ant.getHomePheromone(), ant.getFoodPheromone(), 0, false);
                    nextStates.add(new CellUpdate(ant.getLocation(), newEmpty));
                }
            }
            else if (!emptyNeighbors.isEmpty()) {
                ForagingAntsCell nextLocation = (ForagingAntsCell)highestPheromoneNeighbor(emptyNeighbors, "food");
                nextStates.add(new CellUpdate(nextLocation.getLocation(), new ForagingAntsCell(State.ANT.getValue(), nextLocation.getLocation(), updateHomePheromone(nextLocation, grid), updateFoodPheromone(nextLocation, grid), ant.getHealth(), ant.getHasFood())));
                if (!nextLocation.getLocation().equals(ant.getLocation())) {
                    Cell newEmpty = new ForagingAntsCell(State.EMPTY.getValue(), ant.getLocation(), ant.getHomePheromone(), ant.getFoodPheromone(), 0, false);
                    nextStates.add(new CellUpdate(ant.getLocation(), newEmpty));
                }
            }
        }
        else{
            if (!nestNeighbors.isEmpty() && !emptyNeighbors.isEmpty()){
                ForagingAntsCell nextLocation = (ForagingAntsCell)highestPheromoneNeighbor(emptyNeighbors, "food");
                nextStates.add(new CellUpdate(nextLocation.getLocation(), new ForagingAntsCell(State.ANT.getValue(), nextLocation.getLocation(), updateHomePheromone(nextLocation, grid), updateFoodPheromone(nextLocation, grid), ant.getHealth(), false)));
                if (!nextLocation.getLocation().equals(ant.getLocation())) {
                    Cell newEmpty = new ForagingAntsCell(State.EMPTY.getValue(), ant.getLocation(), ant.getHomePheromone(), ant.getFoodPheromone(), 0, false);
                    nextStates.add(new CellUpdate(ant.getLocation(), newEmpty));
                }
            }
            else if (!emptyNeighbors.isEmpty()) {
                ForagingAntsCell nextLocation = (ForagingAntsCell)highestPheromoneNeighbor(emptyNeighbors, "home");
                nextStates.add((new CellUpdate(nextLocation.getLocation(), new ForagingAntsCell(State.ANT.getValue(), nextLocation.getLocation(), updateHomePheromone(nextLocation, grid), updateFoodPheromone(nextLocation, grid), ant.getHealth(), ant.getHasFood()))));
                if (!nextLocation.getLocation().equals(ant.getLocation())) {
                    Cell newEmpty = new ForagingAntsCell(State.EMPTY.getValue(), ant.getLocation(), ant.getHomePheromone(), ant.getFoodPheromone(), 0, false);
                    nextStates.add(new CellUpdate(ant.getLocation(), newEmpty));
                }
            }

        }
    }

    private double updateHomePheromone(ForagingAntsCell antCell, Grid grid) {
        
        List<Cell> neighbors = getNeighbors(antCell, grid);
        double maxNeighborHomePheromone = 0;

        for (Cell neighbor : neighbors) {
            if(neighbor.getState() == State.NEST.getValue()){
                return myMaxPheromoneAmount;
            }
            ForagingAntsCell antNeighbor = (ForagingAntsCell) neighbor;
            maxNeighborHomePheromone = Math.max(maxNeighborHomePheromone, antNeighbor.getHomePheromone());
        }
        
        if(maxNeighborHomePheromone == 0){
            return 0;
        }
        double desiredPheromone = maxNeighborHomePheromone - 2;
        double pheromoneDifference = desiredPheromone - antCell.getHomePheromone();
        
        return antCell.getHomePheromone() + pheromoneDifference;
    }

    private double updateFoodPheromone(ForagingAntsCell antCell, Grid grid) {
        
        List<Cell> neighbors = getNeighbors(antCell, grid);
        double maxNeighborFoodPheromone = 0;

        for (Cell neighbor : neighbors) {
            if(neighbor.getState() == State.FOOD.getValue()){
                return myMaxPheromoneAmount;
            }
            ForagingAntsCell antNeighbor = (ForagingAntsCell) neighbor;
            maxNeighborFoodPheromone = Math.max(maxNeighborFoodPheromone, antNeighbor.getFoodPheromone());
        }

        if(maxNeighborFoodPheromone == 0){
            return 0;
        }
        double desiredPheromone = maxNeighborFoodPheromone - 2;
        double pheromoneDifference = desiredPheromone - antCell.getFoodPheromone();
        
        return antCell.getFoodPheromone() + pheromoneDifference;
    }

    private List<Cell> getNeighborsByState(Cell cell, Grid grid, int state) {
        List<Cell> allNeighbors = getNeighbors(cell, grid);
        List<Cell> neighborsByState = new ArrayList<>();

        for (Cell neighbor : allNeighbors) {
            if (neighbor.getState() == state) {
                neighborsByState.add(neighbor);
            }
        }
        return neighborsByState;
    }

    private Cell highestPheromoneNeighbor(List<Cell> emptyNeighbors, String type){
        if(type.equals("home")){
            double maxNeighborHomePheromone = 0;
            Cell highestPheromoneNeighbor = new ForagingAntsCell(State.EMPTY.getValue(), emptyNeighbors.get(0).getLocation());
            for (Cell neighbor : emptyNeighbors) {
                ForagingAntsCell antNeighbor = (ForagingAntsCell) neighbor;
                if(antNeighbor.getHomePheromone() > maxNeighborHomePheromone){
                    maxNeighborHomePheromone = antNeighbor.getHomePheromone();
                    highestPheromoneNeighbor = antNeighbor;
                }
            }
            if (maxNeighborHomePheromone == 0){
                Cell newAnt = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
                return newAnt;
            }
            return highestPheromoneNeighbor;
        }
        else if(type.equals("food")){
            double maxNeighborFoodPheromone = 0;
            Cell highestPheromoneNeighbor = new ForagingAntsCell(State.EMPTY.getValue(), emptyNeighbors.get(0).getLocation());
            for (Cell neighbor : emptyNeighbors) {
                ForagingAntsCell antNeighbor = (ForagingAntsCell) neighbor;
                if(antNeighbor.getFoodPheromone() > maxNeighborFoodPheromone){
                    maxNeighborFoodPheromone = antNeighbor.getFoodPheromone();
                    highestPheromoneNeighbor = antNeighbor;
                }
            }
            if (maxNeighborFoodPheromone == 0){
                Cell newAnt = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
                return newAnt;
            }
            return highestPheromoneNeighbor;
        }
        return emptyNeighbors.get(0);
    }
}
