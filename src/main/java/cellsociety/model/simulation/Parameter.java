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

  public String getString() throws ClassCastException {
    return (String) myValue;
  }

  public Double getDouble() throws ClassCastException {
    if (myValue instanceof Double) {
      return (Double) myValue;
    }
    return Double.parseDouble((String) myValue);
  }

  public Integer getInteger() throws ClassCastException {
    if (myValue instanceof Integer) {
      return (Integer) myValue;
    }
    return getDouble().intValue();
  }

  @Override
  public String toString() {
    return myValue.toString();
  }
}
