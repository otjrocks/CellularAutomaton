# Guide: Create A New Simulation Rule

* Create a new java file in this directory that ends with Rules.java. For example, if you wish to
  create a rule for a simulation called Example then you should create the file `ExampleRules.java`.
    * **Important**: You class name must follow this format, as our program uses reflection to
      obtain all the possible simulation rules in this package.
* Make sure that your new simulation rules class extends on SimulationRules and implements all the
  required abstract methods.
    * In the constructor, make sure to check for all the required parameters and validate the
      parameters or throw an exception if the parameters are malformed.
* For state updates overwrite one or both of the following methods:
    * `public int getNextState(Cell cell, Grid grid)`
    * `public List<CellUpdate> getNextStatesForAllCells(Grid grid)`
    * **Reference** the JavaDoc comments for these methods for more information on implementing them
* If your simulation requires parameters, implement the method
  `public static List<String> getRequiredParameters()`, so that the frontend and various parts of
  the program can know and reference the name of all your required parameters.
* By default, your simulation will use the DefaultCell type. If you need a specific cell type,
  create it with the `CREATE_CELL.md` guide.
    * **Important**: The cell name must match the simulation name to be created properly. For
      example, if you have a simulation rule called `ExampleRules.java`, then its corresponding cell
      type should be named `ExampleCell.java`