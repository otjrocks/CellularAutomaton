package cellsociety;

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

  public String getType() {
    return myType;
  }

  public String getName() {
    return myName;
  }

  public String getAuthor() {
    return myAuthor;
  }

  public String getDescription() {
    return myDescription;
  }

  public ArrayList<Color> getColors() {
    return myColors;
  }

  public void setType(String type) {
    myType = type;
  }

  public void setName(String name) {
    myName = name;
  }

  public void setAuthor(String author) {
    myAuthor = author;
  }

  public void setDescription(String description) {
    myDescription = description;
  }

  public void setColors(ArrayList<Color> colors) {
    myColors = colors;
  }


}
