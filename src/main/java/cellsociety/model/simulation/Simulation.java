package cellsociety.model.simulation;

import cellsociety.model.simulation.rules.SimulationRules;

/**
 * A record to store information about a simulation.
 *
 * @param rules The rules of the simulation
 * @param data  The metadata of the simulation
 */
public record Simulation(SimulationRules rules, SimulationMetaData data) {

}
