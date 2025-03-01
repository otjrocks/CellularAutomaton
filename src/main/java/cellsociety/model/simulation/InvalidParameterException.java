package cellsociety.model.simulation;

import static cellsociety.config.MainConfig.LOGGER;

/**
 * An exception to throw whenever a simulation is created with an invalid parameter
 *
 * @author Owen Jennings
 */
public class InvalidParameterException extends Exception {

  /**
   * The default constructor for this exception
   *
   * @param message The message to throw with this exception
   */
  public InvalidParameterException(String message) {
    super(message);
    LOGGER.warn("InvalidParameterException: {}", message);
  }
}