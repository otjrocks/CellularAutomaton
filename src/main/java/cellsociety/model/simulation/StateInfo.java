package cellsociety.model.simulation;

import javafx.scene.paint.Color;

public class StateInfo {
  private final String displayName;
  private final Color color;

  public StateInfo(String displayName, Color color) {
    this.displayName = displayName;
    this.color = color;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Color getColor() {
    return color;
  }
}