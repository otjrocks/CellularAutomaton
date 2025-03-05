package cellsociety.model.xml;

/**
 * An exception for Invalid state provided for a simulation.
 *
 * @author Troy Ludwig
 */
public class InvalidStateException extends Exception {

  /**
   * Create an exception with an error message.
   *
   * @param s The message you want to provide
   */
  public InvalidStateException(String s) {
    super(s);
  }
}