package cellsociety.model.simulation;

import javafx.scene.paint.Color;

/**
 * A record to hold information about a state
 * @param displayName: The display name of a state
 * @param color: the color of a state
 */
public record StateInfo(String displayName, Color color) {

}