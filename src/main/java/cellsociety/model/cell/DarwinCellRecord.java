package cellsociety.model.cell;

import java.awt.geom.Point2D;
import java.util.List;

public record DarwinCellRecord(int state, Point2D location, int orientation, 
      int infectionCountdown, int currentInstIndex,
      List<String> instructions, boolean infected, 
      int previousSpecies){

}