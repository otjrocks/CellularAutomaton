package cellsociety.model.cell;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SugarscapeCellTest {
  private SugarscapeCell sugarscapePatchCell;
  private SugarscapeCell sugarscapeAgentCell;

  @BeforeEach
  void setUp() {
    sugarscapePatchCell = new SugarscapeCell(1, new Double(1, 1), 10, 3, 2, 0, 0);
    sugarscapeAgentCell = new SugarscapeCell(2, new Double(1, 1), 10, 1, 0, 5, 1);
  }

  @Test
  void constructor_testSugerscapeCellThrowsExceptionForNegativeValues_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new SugarscapeCell(1, new Point2D.Double(0, 0), -1, 1, 1, 1, 1),
        "Should throw exception for negative vision");

    assertThrows(IllegalArgumentException.class, () -> new SugarscapeCell(1, new Point2D.Double(0, 0), 1, 0, 1, 1 ,1),
        "Should throw exception for non-positive grow back interval");

    assertThrows(IllegalArgumentException.class, () -> new SugarscapeCell(1, new Point2D.Double(0, 0), 1, 1, -1, 1, 1),
        "Should throw exception for negative grow back rate");

    assertThrows(IllegalArgumentException.class, () -> new SugarscapeCell(1, new Point2D.Double(0, 0), 1, 1, 1, -1, 1),
        "Should throw exception for negative vision");

    assertThrows(IllegalArgumentException.class, () -> new SugarscapeCell(1, new Point2D.Double(0, 0), 1, 1, 1, 1, -1),
        "Should throw exception for negative metabolism");
  }

  @Test
  void isPatch_testPatchCell_returnsTrue() {
    assertTrue(sugarscapePatchCell.isPatch());
    assertFalse(sugarscapePatchCell.isAgent());
  }

  @Test
  void isAgent_testAgentCell_returnsTrue() {
    assertTrue(sugarscapeAgentCell.isAgent());
    assertFalse(sugarscapePatchCell.isAgent());
  }

  @Test
  void getVision_normalVision_returnsVision() {
    assertEquals(5, sugarscapeAgentCell.getVision());
  }

  @Test
  void getMetabolism_normalMetabolism_returnsMetabolism() {
    assertEquals(1, sugarscapeAgentCell.getMetabolism());
  }

  @Test
  void getSugar_normalSugar_returnsSugar() {
    assertEquals(10, sugarscapePatchCell.getSugar());
  }

  @Test
  void setSugar_newSugarBeingSet_checkNewSugar() {
    sugarscapePatchCell.setSugar(100);
    assertEquals(100, sugarscapePatchCell.getSugar());
  }

  @Test
  void regenerateSugar_newSugarBeingSet_checkNewSugar() {
    sugarscapePatchCell.regenerateSugar();
    sugarscapePatchCell.regenerateSugar();
    sugarscapePatchCell.regenerateSugar();

    assertEquals(12, sugarscapePatchCell.getSugar());
  }

  @Test
  void regenerateSugar_sugarNotChanging_getOldSugar() {
    sugarscapePatchCell.regenerateSugar();
    sugarscapePatchCell.regenerateSugar();

    assertEquals(10, sugarscapePatchCell.getSugar());
  }

  @Test
  void getSugarGrowBackInterval_normalSugarGrowBack_returnInterval(){
    assertEquals(3, sugarscapePatchCell.getSugarGrowBackInterval());
  }

  @Test
  void getSugarGrowBackRate_normalSugarGrowBack_returnInterval(){
    assertEquals(2, sugarscapePatchCell.getSugarGrowBackRate());
  }


}