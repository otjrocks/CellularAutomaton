package cellsociety.model.edge;

/**
 * A factory to create an edge strategy object
 *
 * @author Owen Jennings
 */
public class EdgeStrategyFactory {

  /**
   * This enum stores all the supported edge strategies
   */
  public enum EdgeStrategyType {
    FIXED("Fixed"),
    TOROIDAL("Toroidal"),
    MIRROR("Mirror");

    private final String displayName;

    EdgeStrategyType(String displayName) {
      this.displayName = displayName;
    }

    // Override toString() to return display name
    @Override
    public String toString() {
      return displayName;
    }
  }

  /**
   * Create an edge strategy of the provided type
   *
   * @param edgeStrategyType The edge strategy type you want to create
   * @return An EdgeStrategy object for the specified type
   */
  public static EdgeStrategy createEdgeStrategy(EdgeStrategyType edgeStrategyType) {
    return switch (edgeStrategyType) {
      case FIXED -> new FixedEdgeStrategy();
      case TOROIDAL -> new ToroidalEdgeStrategy();
      case MIRROR -> new MirrorEdgeStrategy();
    };
  }


}
