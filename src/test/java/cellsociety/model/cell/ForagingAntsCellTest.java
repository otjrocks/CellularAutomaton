package cellsociety.model.cell;

import java.awt.geom.Point2D.Double;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ForagingAntsCellTest {

  ForagingAntsCell cellOne;
  ForagingAntsCell cellTwo;

  @BeforeEach
  void setUp() {
    cellOne = new ForagingAntsCell(5, new Double(0, 1), 10.0, 25.0, 500, true, 10) {
    };
  }

  @Test
  void ForagingAntsConstructor_InvalidInitialization_throwsException(){
    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new ForagingAntsCell(0, new Double(-1, 0)) {
        });

    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new ForagingAntsCell(0, new Double(5.5, 0)) {
        });

    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new ForagingAntsCell(0, new Double(0, -1), 1.0, 1.0, 10, false, 10) {
        });

    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new ForagingAntsCell(0, new Double(5.5, 0) ,1.0, 1.0, 10, false, 10) {
        });
  }

  @Test
  void antCellConstructor_defaultConstructor_returnsDeafultValues(){
    cellTwo = new ForagingAntsCell(0, new Double(0, 0));
    assertEquals(cellTwo.getHomePheromone(), 0);
    assertEquals(cellTwo.getFoodPheromone(), 0);
    assertEquals(cellTwo.getHealth(), 300);
    assertEquals(cellTwo.getHasFood(), false);
  }

  @Test
  void getHasFood_antHasFood_returnsTrue(){
    assertEquals(cellOne.getHasFood(), true);
  }

  @Test
  void getHomePheromone_hasPheromoneValue_returnsPheromoneValue(){
    assertEquals(cellOne.getHomePheromone(), 10.0);
  }

  @Test
  void getFoodPheromone_hasPheromoneValue_returnsPheromoneValue(){
    assertEquals(cellOne.getFoodPheromone(), 25.0);
  }

  @Test
  void getHealth_hasHealthValue_returnsHealthValue(){
    assertEquals(cellOne.getHealth(), 500);
  }

  @Test
  void reduceHealth_reduceBy50_returnsReducedHealthValue(){
    cellOne.reduceHealth(50);
    assertEquals(cellOne.getHealth(), 450);
  }

  @Test
  void reduceHealth_reduceToNegative_returnsReducedHealthValue(){
    cellOne.reduceHealth(1000);
    assertEquals(cellOne.getHealth(), 0);
  }

}