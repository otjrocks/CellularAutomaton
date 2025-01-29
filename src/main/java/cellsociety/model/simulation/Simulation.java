package cellsociety.model.simulation;

public abstract class Simulation {
  private SimulationRules myRules;
  private SimulationData myData;

  public SimulationRules getRules() {
    return myRules;
  }
  public SimulationData getData() {
    return myData;
  }
  abstract void getNextSimulationState();
}
