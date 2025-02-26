package cellsociety.view.components;

import static cellsociety.view.SidebarView.ELEMENT_SPACING;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * A field that is used to select and item from a combo box and do some action with it
 *
 * @author Owen Jennings
 */
public class SelectorField extends HBox {

  private final ComboBox<String> myComboBox;

  /**
   * Create a Selector field.
   *
   * @param options:      A list of strings representing the possible options
   * @param defaultValue: The default value of the selector
   * @param id:           The id of the combobox used in this field
   * @param labelText:    The label text for the selector
   * @param action:       The action to run on change in the selector's value
   */
  public SelectorField(List<String> options, String defaultValue, String id, String labelText,
      EventHandler<ActionEvent> action) {
    Text myLabel = new Text(labelText);
    myComboBox = new ComboBox<>();
    myComboBox.getItems().addAll(options);
    myComboBox.setValue(defaultValue);
    myComboBox.setOnAction(action);
    myComboBox.setId(id);
    myComboBox.getStyleClass().add("combo-box");
    this.getChildren().addAll(myLabel, myComboBox);
    this.setSpacing(ELEMENT_SPACING);
    this.setAlignment(Pos.CENTER_LEFT);
  }

  /**
   * Get the current value of the selector field
   *
   * @return The current value of the selector/combobox
   */
  public String getValue() {
    return myComboBox.getValue();
  }

}
