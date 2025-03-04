package cellsociety.view.config;

import cellsociety.utility.FileUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A public class so that the neighbor method choosing is accessible in multiple places.
 *
 * @author Justin Aronwald
 */
public class NeighborConfig {

  //Had a little bit of ChatGPT help with the last few lines of this
  private static final String NEIGHBOR_OPTION_DIRECTORY = "src/main/java/cellsociety/model/simulation/getNeighborOptions/";

  /**
   * Scans the directory to find the various different neighbor types.
   *
   * @return - a list of the string names of the neighbor types
   */
  public static ObservableList<String> getAvailableNeighborTypes() {
    return FXCollections.observableArrayList(
        FileUtility.getFileNamesInDirectory(NEIGHBOR_OPTION_DIRECTORY, "Neighbors.java"));
  }
}
