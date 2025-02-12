package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CellTest {

  Cell cellOne;
  Cell cellTwo;

  @BeforeEach
  void setUp() {
    cellOne = new Cell(5, new Double(0, 1)) {
    };
  }

  @Test
  void testConstructor() {
    // Check error handling for constructor of cell with non-integer location values or negative location values
    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new Cell(0, new Double(-1, 0)) {
        });
    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new Cell(0, new Double(0, -1)) {
        });
    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new Cell(0, new Double(1.1, 1)) {
        });
    assertThrows(IllegalArgumentException.class, () ->
        cellTwo = new Cell(0, new Double(1, 5.5)) {
        });
  }

  @Test
  void cannotAddNegativeState() {
    assertThrows(IllegalArgumentException.class, () -> new Cell(-1,  new Double(0, 0)){});
  }

  @Test
  void cannotAddNullLocation() {
    assertThrows(IllegalArgumentException.class, () -> new Cell(0, null){});
  }

  @Test
  void getState() {
    assertEquals(5, cellOne.getState());
  }

  @Test
  void getLocation() {
    assertEquals(new Double(0, 1), cellOne.getLocation());
  }

  @Test
  void getRow() {
    assertEquals(0, cellOne.getRow());
  }

  @Test
  void getCol() {
    assertEquals(1, cellOne.getCol());
  }
}