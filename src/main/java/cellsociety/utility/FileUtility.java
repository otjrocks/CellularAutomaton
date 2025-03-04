package cellsociety.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class containing utility methods pertaining to files.
 *
 * @author Owen Jennings
 */
public class FileUtility {

  /**
   * Get a list of all the files in the provided directory that end with a specific string or file
   * extension
   *
   * @param directoryPath The directory you are querying
   * @param ending The ending of the files you which to query for
   * @return: A list of all the file names in the directory provided that end with the provided ending
   */
  public static List<String> getFileNamesInDirectory(String directoryPath, String ending) {
    List<String> fileNames = new ArrayList<>();
    File directory = new File(directoryPath);
    File[] files = directory.listFiles((file, name) -> name.endsWith(ending));

    if (files != null) {
      for (File file : files) {
        fileNames.add(file.getName().replace(ending, ""));
      }
    }
    Collections.sort(fileNames);
    return fileNames;
  }

}
