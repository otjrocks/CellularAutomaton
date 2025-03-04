package cellsociety.model.simulation;

/**
 * An exception to throw whenever an issue occurs creating a simulation from a file or trying to get
 * the simulation rules/data from a file. This extends runtime exception.
 *
 * @author Owen Jennings
 */
public class SimulationCreationException extends RuntimeException {

  /**
   * The default constructor for this exception.
   *
   * @param e The exception being thrown
   */
  public SimulationCreationException(Exception e) {
    super(e);
  }

  /**
   * An exception with a message provided.
   *
   * @param message The message to include
   * @param e       The exception being thrown
   */
  public SimulationCreationException(String message, Exception e) {
    super(message, e);
  }
}
