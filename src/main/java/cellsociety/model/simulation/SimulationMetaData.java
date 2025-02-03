package cellsociety.model.simulation;

/**
 * A record to store metadata about a simulation
 *
 * @param type:        The simulation type
 * @param name:        The simulation example's name
 * @param author:      The author of the simulation
 * @param description: The description of the simulation
 */
public record SimulationMetaData(String type, String name, String author, String description) {

}
