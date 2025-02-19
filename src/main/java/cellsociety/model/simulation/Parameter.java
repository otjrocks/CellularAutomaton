package cellsociety.model.simulation;

import static cellsociety.config.MainConfig.getMessage;

/**
 * A class to handle the creation and storage of parameter values
 *
 * @param <T>: The type of the parameter's value
 * @author Owen Jennings
 */
public class Parameter<T> {

  private final T myValue;

  /**
   * Create a new parameter of type T
   *
   * @param value The value of the parameter. This value will be of type T
   */
  public Parameter(T value) {
    myValue = value;
  }

  /**
   * Get the string representation of a parameter if current value can be cast to String
   *
   * @return String representation of current parameter value
   * @throws InvalidParameterException If the current parameter value cannot be cast to a String.
   */
  public String getString() throws InvalidParameterException {
    try {
      return (String) myValue;
    } catch (Exception e) {
      throw new InvalidParameterException(
          String.format(getMessage("INVALID_PARAMETER"),
              myValue));
    }
  }

  /**
   * The Double representation of the current parameter value
   *
   * @return The Double representation of the value
   * @throws InvalidParameterException If the current parameter cannot be cast or parsed as a
   *                                   Double.
   */
  public Double getDouble() throws InvalidParameterException {
    try {
      if (myValue instanceof Double) {
        return (Double) myValue;
      }
      return Double.parseDouble((String) myValue);
    } catch (Exception e) {
      throw new InvalidParameterException(
          String.format(getMessage("INVALID_PARAMETER"),
              myValue));
    }
  }

  /**
   * The Integer representation of parameter
   *
   * @return An Integer value
   * @throws InvalidParameterException If the current parameter cannot be cast or converted to an
   *                                   integer value.
   */
  public Integer getInteger() throws InvalidParameterException {
    try {
      if (myValue instanceof Integer) {
        return (Integer) myValue;
      }
      return getDouble().intValue();
    } catch (Exception e) {
      throw new InvalidParameterException(
          String.format(getMessage("INVALID_PARAMETER"),
              myValue));
    }
  }

  /**
   * The parameter toString should print the .toString() of the parameters value.
   *
   * @return
   */
  @Override
  public String toString() {
    return myValue.toString();
  }
}
