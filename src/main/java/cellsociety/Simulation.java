package cellsociety;

public abstract class Simulation {
  private SimulationRules myRules;
  private SimulationData myData;

  abstract void getNextState();
}
