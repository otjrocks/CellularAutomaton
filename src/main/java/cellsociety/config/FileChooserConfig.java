package cellsociety.config;

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
  public static final String DATA_FILE_FOLDER = System.getProperty("user.dir") + "/data";
  // NOTE: make ONE chooser since generally accepted behavior is that it remembers where user left it last
  public static final FileChooser FILE_CHOOSER = makeChooser();

  // set some sensible defaults when the FileChooser is created
  private static FileChooser makeChooser() {
    FileChooser result = new FileChooser();
    result.setTitle("Open Data File");
    // pick a reasonable place to start searching for files
    result.setInitialDirectory(new File(DATA_FILE_FOLDER));
    result.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Data Files",
        FileChooserConfig.DATA_FILE_EXTENSION));
    return result;
  }

}
