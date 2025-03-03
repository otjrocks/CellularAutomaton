package cellsociety.model.edge;

import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.edge.EdgeStrategyFactory.EdgeStrategyType;
import org.junit.Test;

public class EdgeStrategyFactoryTest {

  @Test
  public void fixedEdge_checkFixedGridReturned_Success() {
    assertInstanceOf(FixedEdgeStrategy.class, EdgeStrategyFactory.createEdgeStrategy(
        EdgeStrategyType.FIXED));
  }

  @Test
  public void toroidalEdge_checkToroidalGridReturned_Success() {
    assertInstanceOf(ToroidalEdgeStrategy.class, EdgeStrategyFactory.createEdgeStrategy(
        EdgeStrategyType.TOROIDAL));
  }

  @Test
  public void mirrorEdge_checkMirrorGridReturned_Success() {
    assertInstanceOf(MirrorEdgeStrategy.class, EdgeStrategyFactory.createEdgeStrategy(
        EdgeStrategyType.MIRROR));
  }

}