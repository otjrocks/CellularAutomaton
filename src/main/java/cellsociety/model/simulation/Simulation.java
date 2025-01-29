package cellsociety.model.simulation;

public abstract class Simulation {
  private SimulationRules myRules;
  private SimulationData myData;

  public Simulation(SimulationRules rules, SimulationData data) {
    myRules = rules;
    myData = data;
  }

  public SimulationRules getRules() {
    return myRules;
  }
  public SimulationData getData() {
    return myData;
  }
  public abstract void getNextSimulationState();
}
