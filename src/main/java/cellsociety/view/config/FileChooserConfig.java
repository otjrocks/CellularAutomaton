package cellsociety.view.config;

import static cellsociety.config.MainConfig.getMessage;

import java.io.File;

import javafx.stage.FileChooser;

/**
 * A public class to allow various classes to access the file chooser for this program, so that it
 * remembers where the user left it at last
 *
 * @author Owen Jennings
 */
public class FileChooserConfig {

  // kind of data files to look for
  public static final String DATA_FILE_EXTENSION = "*.xml";
  // default to start in the data folder to make it easy on the user to find
  public static final String DATA_FILE_FOLDER = "%s/src/main/resources".formatted(
      System.getProperty("user.dir"));
  public static final String DATA_SAVE_FOLDER = "%s/simulations".formatted(DATA_FILE_FOLDER);
  public static final String DEFAULT_SIMULATION_PATH =
      "%s/simulations/default.xml".formatted(DATA_FILE_FOLDER); // default simulation
  // NOTE: make ONE chooser since generally accepted behavior is that it remembers where user left it last
  public static final FileChooser FILE_CHOOSER = makeChooser();

  /**
   * Create a file chooser for saving a xml file with a name.xml format.
   *
   * @param name name of the file you wish to save
   * @return the file chooser you create
   */
  public static FileChooser makeSaveChooser(String name) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(getMessage("SAVE_FILE_TITLE"));
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter(getMessage("XML_FILE_EXTENSION_NAME"),
            FileChooserConfig.DATA_FILE_EXTENSION));
    fileChooser.setInitialDirectory(new File(DATA_SAVE_FOLDER));
    fileChooser.setInitialFileName("%s.xml".formatted(name));
    return fileChooser;
  }


  // set some sensible defaults when the FileChooser is created
  private static FileChooser makeChooser() {
    FileChooser result = new FileChooser();
    result.setTitle(getMessage("OPEN_FILE_TITLE"));
    // pick a reasonable place to start searching for files
    result.setInitialDirectory(new File(DATA_FILE_FOLDER));
    result.getExtensionFilters()
        .setAll(new FileChooser.ExtensionFilter(getMessage("XML_FILE_EXTENSION_NAME"),
            FileChooserConfig.DATA_FILE_EXTENSION));
    return result;
  }

}
