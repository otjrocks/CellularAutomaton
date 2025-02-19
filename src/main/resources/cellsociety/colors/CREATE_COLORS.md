# Guide: Create Custom State Colors

* By default, cell states are designed random color values when they are first created.
* However, using the `CellColors.properties` file, you can assign specific colors as the default
  color for a given simulations states.\
* To set the color for a given state folow the format `SIMULATIONNAME_COLOR_K` where SIMULATIONNAME
  is the uppercase transformation of your simulation name as it is defined in the rules class (The
  file name minus the ending Rules.java). The K value is the integer value for the state that you
  want to set the color to
* For example, if you have a simulation rules file GameOfLifeRules and you want to set the color for
  the 0th state to WHITE, then you should add to the properties file the key-value pair
  `GAMEOFLIFE_COLOR_0=WHITE`
* **Important**: The color field must be one of the static colors used by JavaFX. For a full list of
  potential colors,
  view: [JavaFX Color documentation](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/paint/Color.html)
