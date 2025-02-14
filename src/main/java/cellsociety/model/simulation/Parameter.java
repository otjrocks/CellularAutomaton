package cellsociety.model.simulation;

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

  public String getString() throws InvalidParameterException {
    try {
      return (String) myValue;
    } catch (Exception e) {
      throw new InvalidParameterException("Invalid parameter type: " + myValue);
    }
  }

  public Double getDouble() throws InvalidParameterException {
    try {
      if (myValue instanceof Double) {
        return (Double) myValue;
      }
      return Double.parseDouble((String) myValue);
    } catch (Exception e) {
      throw new InvalidParameterException("Invalid parameter type: " + myValue);
    }
  }

  public Integer getInteger() throws InvalidParameterException {
    try {
      if (myValue instanceof Integer) {
        return (Integer) myValue;
      }
      return getDouble().intValue();
    } catch (Exception e) {
      throw new InvalidParameterException("Invalid parameter type: " + myValue);
    }
  }

  @Override
  public String toString() {
    return myValue.toString();
  }
}
