package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatchCellTest {
  private PatchCell patchCell;

  @BeforeEach
  void setUp() {
    patchCell = new PatchCell(1, 10, 3, 2, new Point2D.Double(2, 2));
  }


  @Test
  void constructor_testPatchCellThrowsExceptionForNegativeValues_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new PatchCell(1, -1, 1, 1, new Point2D.Double(0, 0)),
        "Should throw exception for negative sugar");

    assertThrows(IllegalArgumentException.class, () -> new PatchCell(1, 1, -1, 1, new Point2D.Double(0, 0)),
        "Should throw exception for negative sugarGrowBackInterval");

    assertThrows(IllegalArgumentException.class, () -> new PatchCell(1, 1, 1, -1, new Point2D.Double(0, 0)),
        "Should throw exception for negative sugarGrowBackRate");
  }

  @Test
  void getSugar_normalSugar_returnsSugar() {
    assertEquals(10, patchCell.getSugar());
  }

  @Test
  void setSugar_newSugarBeingSet_checkNewSugar() {
    patchCell.setSugar(100);
    assertEquals(100, patchCell.getSugar());
  }

  @Test
  void regenerateSugar_newSugarBeingSet_checkNewSugar() {
    patchCell.regenerateSugar();
    patchCell.regenerateSugar();
    patchCell.regenerateSugar();

    assertEquals(12, patchCell.getSugar());
  }

  @Test
  void regenerateSugar_sugarNotChanging_getOldSugar() {
    patchCell.regenerateSugar();
    patchCell.regenerateSugar();

    assertEquals(10, patchCell.getSugar());
  }

}