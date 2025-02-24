package cellsociety.view.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A public class so that the neighbor method choosing is accessible in multiple places
 * @Author Justin Aronwald
 */
public class NeighborConfig {
  //Had a little bit of ChatGPT help with the last few lines of this

  /**
   * scans the directory to find the various different neighbor types
   * @return - a list of the string names of the neighbor types
   */
  public static ObservableList<String> getAvailableNeighborTypes() {
    File directory = new File("src/main/java/cellsociety/model/simulation/getNeighborOptions/");
    List<String> neighborTypes = new ArrayList<>();

    if (directory.exists() && directory.isDirectory()) {
      File[] files = directory.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.getName().endsWith("Neighbors.java")) {
            String typeName = file.getName().replace("Neighbors.java", "");
            neighborTypes.add(typeName);
          }
        }
      }
    }
    return FXCollections.observableArrayList(neighborTypes);
  }
}
