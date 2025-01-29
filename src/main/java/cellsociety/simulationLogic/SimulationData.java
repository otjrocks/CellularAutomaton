package cellsociety.simulationLogic;

import java.util.ArrayList;
import javafx.scene.paint.Color;

public class SimulationData {
  private String myType;
  private String myName;
  private String myAuthor;
  private String myDescription;
  private ArrayList<Color> myColors;

  public SimulationData(String type, String name, String author, String description, ArrayList<Color> colors) {
    myType = type;
    myName = name;
    myAuthor = author;
    myDescription = description;
    myColors = colors;
  }

  /**
   *
   * @return the type of the simulation
   */
  public String getType() {
    return myType;
  }

  /**
   *
   * @return the name of the simulation
   */
  public String getName() {
    return myName;
  }

  /**
   *
   * @return the author of the simulation
   */
  public String getAuthor() {
    return myAuthor;
  }

  /**
   *
   * @return the description of the simulation
   */
  public String getDescription() {
    return myDescription;
  }

  /**
   *
   * @return the colors of the simulation
   */
  public ArrayList<Color> getColors() {
    return myColors;
  }

  /**
   *
   * @param type - the new type of simulation
   */
  public void setType(String type) {
    myType = type;
  }

  /**
   *
   * @param name - the new name of the simulation
   */
  public void setName(String name) {
    myName = name;
  }

  /**
   *
   * @param author - the new author of the simulation
   */
  public void setAuthor(String author) {
    myAuthor = author;
  }

  /**
   *
   * @param description - the new description of the simulation
   */
  public void setDescription(String description) {
    myDescription = description;
  }

  /**
   *
   * @param colors - the new list of colors of the simulation
   */
  public void setColors(ArrayList<Color> colors) {
    myColors = colors;
  }


}
