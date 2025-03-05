package cellsociety.model.cell;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

class DarwinCellTest {

    public DarwinCell instantiateTestCell(){
        List<String> testInstructions = new ArrayList<>();
        testInstructions.add("MOVE 1");
        DarwinCell testCell = new DarwinCell(new DarwinCellRecord(5, new Double(0,0), 90, 0, 0, testInstructions, false, 0));
        return testCell;
    }

    @Test
    void getInstruction_predefinedCell_returnsCorrectInstruction() {
        DarwinCell cell = instantiateTestCell();
        assertEquals("MOVE 1", cell.getInstruction());
    }

    @Test
    void getInstructionAndGetNextInstruction_predefinedCell_returnsCorrectInstructions() {
        DarwinCell cell = instantiateTestCell();
        assertEquals("MOVE 1", cell.getInstruction());
        cell.setInstructions("MOVE 2");
        cell.nextInstruction();
        assertEquals("MOVE 2", cell.getInstruction());
    }

    @Test
    void getInfectionCountdown_predefinedCell_returnsCorrectCountdownValue() {
        DarwinCell cell = instantiateTestCell();
        assertEquals(0, cell.getInfectionCountdown());
    }

    @Test
    void getOrientation_predefinedCell_returnsCorrectOrientation() {
        DarwinCell cell = instantiateTestCell();
        assertEquals(90, cell.getOrientation());
    }

    @Test
    void getPreviousState_predefinedCell_returnsCorrectPrevState() {
        DarwinCell cell = instantiateTestCell();
        assertEquals(0, cell.getPrevState());
    }

    @Test
    void getInfected_predefinedCell_returnsCorrectInfectionState() {
        DarwinCell cell = instantiateTestCell();
        assertFalse(cell.getInfected());
    }
}