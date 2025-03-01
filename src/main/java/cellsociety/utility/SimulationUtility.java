package cellsociety.utility;

import static cellsociety.config.MainConfig.VERBOSE_ERROR_MESSAGES;

import cellsociety.config.SimulationConfig;
import cellsociety.model.simulation.InvalidParameterException;
import cellsociety.model.simulation.Parameter;
import cellsociety.model.simulation.Simulation;
import cellsociety.model.simulation.SimulationMetaData;
import cellsociety.view.components.AlertField;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * A utility class for operations related to creating and updating simulation objects
 *
 * @author Owen Jennings
 */
public class SimulationUtility {

  /**
   * Generate a new simulation from a current simulation with new information such as updated
   * metadata or new parameter values
   *
   * @param currentSimulation The current simulation you want to update metadata for
   * @param newMetaData       The new metadata you want to use
   * @param newParameters     The new parameters you want to use
   * @param alertField        The alert field to use for any errors or exceptions
   * @return A simulation object
   */
  public static Simulation updateSimulation(Simulation currentSimulation,
      SimulationMetaData newMetaData, Map<String, Parameter<?>> newParameters,
      AlertField alertField) {
    Simulation newSimulation;
    try {
      newSimulation = SimulationConfig.getNewSimulation(currentSimulation.data().type(),
          newMetaData, newParameters);
    } catch (InvocationTargetException e) {
      alertField.flash(e.getCause().getMessage(), true);
      return null;
    } catch (ClassNotFoundException | NoSuchMethodException |
             InstantiationException | IllegalAccessException | InvalidParameterException e) {
      if (VERBOSE_ERROR_MESSAGES) {
        alertField.flash(e.getMessage(), true);
      }
      throw new RuntimeException(e);
    }
    return newSimulation;
  }
}
