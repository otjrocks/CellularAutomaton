package cellsociety.model.simulation.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cellsociety.model.Grid;
import cellsociety.model.cell.AntCell;
import cellsociety.model.cell.Cell;
import cellsociety.model.cell.CellUpdate;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.SimulationRules;

public class ForagingAntsRules extends SimulationRules {

    private final Random random = new Random();
    private final int myPheromoneDecayRate;
    private final int myAntReproductionTime;
    private final int myMaxPheromoneAmount;

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

        Iterator<Cell> cellIterator = grid.getCellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getState() == State.ANT.getValue()) {
                antCells.add(cell);
            }
        }

        for (Cell ant : antCells) {
            processAntMovement(ant, grid, nextStates);
        }

        return nextStates;
    }

    private void processAntMovement(Cell cell, Grid grid, List<CellUpdate> nextStates) {
        AntCell ant = (AntCell) cell;
        ant.reduceHealth(1);
        List<Cell> emptyNeighbors = getNeighborsByState(ant, grid, State.EMPTY.getValue());
        List<Cell> foodNeighbors = getNeighborsByState(ant, grid, State.FOOD.getValue());

        
        if (!foodNeighbors.isEmpty()) {
            Cell food = foodNeighbors.get(random.nextInt(foodNeighbors.size()));
            if (!food.getLocation().equals(ant.getLocation())) {
                dropFoodPheromone(ant, grid);
                nextStates.add(new CellUpdate(food.getLocation(), new AntCell(State.ANT.getValue(), food.getLocation())));
            }
        }

        if (!emptyNeighbors.isEmpty()) {
            Cell newLocation = emptyNeighbors.get(random.nextInt(emptyNeighbors.size()));
            dropHomePheromone(ant, grid);
            nextStates.add(new CellUpdate(newLocation.getLocation(), new AntCell(State.ANT.getValue(), newLocation.getLocation())));
        }

        if (shouldReproduce(ant)) {
            reproduceAnt(ant, nextStates);
        }
        else{
            ant.incrementReproductionTime();
        }

    }

    private void dropHomePheromone(AntCell antCell, Grid grid) {
        if (antCell.getState() == State.NEST.getValue()) {
            antCell.setHomePheromone(myMaxPheromoneAmount);
        } else {
            List<Cell> neighbors = getNeighbors(antCell, grid);
            double maxNeighborHomePheromone = 0;

            for (Cell neighbor : neighbors) {
                if (neighbor instanceof AntCell) {
                    AntCell antNeighbor = (AntCell) neighbor;
                    maxNeighborHomePheromone = Math.max(maxNeighborHomePheromone, antNeighbor.getHomePheromone());
                }
            }

            double desiredPheromone = maxNeighborHomePheromone - 2;
            double pheromoneDifference = desiredPheromone - antCell.getHomePheromone();

            if (pheromoneDifference > 0) {
                antCell.setHomePheromone(antCell.getHomePheromone() + pheromoneDifference);
            }
        }
    }

    private void dropFoodPheromone(AntCell antCell, Grid grid) {
        if (antCell.getState() == State.FOOD.getValue()) {
            antCell.setFoodPheromone(myMaxPheromoneAmount);
        } else {
            List<Cell> neighbors = getNeighbors(antCell, grid);
            double maxNeighborFoodPheromone = 0;

            for (Cell neighbor : neighbors) {
                if (neighbor instanceof AntCell) {
                    AntCell antNeighbor = (AntCell) neighbor;
                    maxNeighborFoodPheromone = Math.max(maxNeighborFoodPheromone, antNeighbor.getFoodPheromone());
                }
            }

            double desiredPheromone = maxNeighborFoodPheromone - 2;
            double pheromoneDifference = desiredPheromone - antCell.getFoodPheromone();

            if (pheromoneDifference > 0) {
                antCell.setFoodPheromone(antCell.getFoodPheromone() + pheromoneDifference);
            }
        }
    }

    private boolean shouldReproduce(AntCell ant) {
        boolean shouldReproduce =
            ant.getReproductionTime() >= myAntReproductionTime;
        return shouldReproduce;
    }

    private void reproduceAnt(AntCell ant, List<CellUpdate> nextStates) {
        Cell offspringAnt = new AntCell(State.ANT.getValue(), ant.getLocation());
        nextStates.add(new CellUpdate(ant.getLocation(), offspringAnt));
        ant.resetReproductionTime();
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
}
