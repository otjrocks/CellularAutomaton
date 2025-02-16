package cellsociety.model.simulation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParameterTest {

  // I used ChatGPT to assist in writing these tests
  @Test
  void getString_ValidString_CorrectValue() throws InvalidParameterException {
    Parameter<String> param = new Parameter<>("testValue");
    assertEquals("testValue", param.getString());
  }

  @Test
  void getString_InvalidType_WantedStringHadInteger_Exception() {
    Parameter<Integer> param = new Parameter<>(42);
    assertThrows(InvalidParameterException.class, param::getString);
  }

  @Test
  void getDouble_ValidDouble_CorrectValue() throws InvalidParameterException {
    Parameter<Double> param = new Parameter<>(42.5);
    assertEquals(42.5, param.getDouble());
  }

  @Test
  void getDouble_StringConvertibleToDouble_CorrectValue() throws InvalidParameterException {
    Parameter<String> param = new Parameter<>("42.5");
    assertEquals(42.5, param.getDouble());
  }

  @Test
  void getDouble_InvalidType_Exception() {
    Parameter<String> param = new Parameter<>("notADouble");
    assertThrows(InvalidParameterException.class, param::getDouble);
  }

  @Test
  void getInteger_ValidInteger_CorrectValue() throws InvalidParameterException {
    Parameter<Integer> param = new Parameter<>(42);
    assertEquals(42, param.getInteger());
  }

  @Test
  void getInteger_StringConvertibleToInteger_CorrectValue() throws InvalidParameterException {
    Parameter<String> param = new Parameter<>("42");
    assertEquals(42, param.getInteger());
  }

  @Test
  void getInteger_InvalidType_Exception() {
    Parameter<String> param = new Parameter<>("notAnInteger");
    assertThrows(InvalidParameterException.class, param::getInteger);
  }

  @Test
  void toString_ValidValue_CorrectString() {
    Parameter<Integer> param = new Parameter<>(123);
    assertEquals("123", param.toString());
  }
}
