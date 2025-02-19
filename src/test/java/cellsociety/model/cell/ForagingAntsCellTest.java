package cellsociety.model.cell;

import java.awt.geom.Point2D.Double;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ForagingAntsCellTest {

  Cell cellOne;
  Cell cellTwo;

  @BeforeEach
  void setUp() {
    cellOne = new ForagingAntsCell(5, new Double(0, 1)) {
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
        cellTwo = new ForagingAntsCell(0, new Double(0, -1), 1.0, 1.0, 10, false) {
        });

    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new ForagingAntsCell(0, new Double(5.5, 0) ,1.0, 1.0, 10, false) {
        });
  }

}