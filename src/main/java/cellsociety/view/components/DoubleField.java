package cellsociety.view.components;


import javafx.scene.control.TextField;

/**
 * A Text field that only accepts double values Source:
 * https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
 *
 * @author Owen Jennings
 */
public class DoubleField extends TextField {

  @Override
  public void replaceText(int start, int end, String text) {
    if (validate(text)) {
      super.replaceText(start, end, text);
    }
  }

  @Override
  public void replaceSelection(String text) {
    if (validate(text)) {
      super.replaceSelection(text);
    }
  }

  private boolean validate(String text) {
    return text.isEmpty() || text.matches("\\d*\\.?\\d*");
  }
}