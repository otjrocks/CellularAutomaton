package cellsociety.view.config;

import javafx.scene.paint.Color;

/**
 * A record to hold information about a state
 *
 * @param value       The integer value representation of this state
 * @param displayName The display name of a state
 * @param color       the color of a state
 */
public record StateInfo(int value, String displayName, Color color) {

}