# Design Document for Cell Society Project

### Team Number 4 

### Names: Owen Jennings (otj4), Justin Aronwald (jga18), Troy Ludwig (tdl21)

## Team Roles and Responsibilities

At the beginning of each week, we spent an hour planning. As part of this team planning, we evenly divided the Gitlab issues based on interest and assigned Gitlab issues to our profiles. 

* Team Member \#1 \- Owen Jennings  
  * Throughout the project, I ensured that our design requirements were met and the work was divided evenly. I made sure that I always completed my assigned Gitlab tasks on time and with consideration for the design to minimize the amount of refactoring required in future steps. I spent most of my time refactoring code, such as clearing all the code warnings in the GitLab pipeline so that it passed before we submitted our assignment. I also had a large role in our team’s planning and design of the project. I believe I was approachable and someone who my teammates could come to with questions. Specifically, I focused on the Cell and Grid on the model side and a majority of the initial view components, such as SimulationView, SidebarView, EditModeView, ViewModeView, etc.  
      
* Team Member \#2 \- Justin Aronwald  
  * I think my role was heavily dominated in creating features and simulations. I focused less on creating UI and making sure the data files worked, as I was almost solely focused on making sure all the simulations ran well. In the end, my work was 75% backend-dominated, compared to the other features. That being said, I did get to implement some features relating to UI and others. For example, for the first requirement, I implemented the 6 simulations, including the rules and abstractions and such. Then, for the next requirements, I completed 2 more simulations, created property files for Spanish and PigLatin, and created a Splash Screen, among other smaller features. Finally, I created the Darwin simulation with heavy abstraction, refactored the neighbor logic, and did bug fixes for other features like Zoom.

* Team Member \#3 \- Troy Ludwig  
  * For the first set of requirements, I was responsible for XML handling (reading from and writing to). For the second set of requirements, I was responsible for XML error handling, Foraging Ants and General Game of Life implementations, and the random configuration generation files. For the final set of requirements, I was responsible for all added view elements (histogram, half of zoom and simulation iterations) and worked with Justin to implement the Darwin simulation. I took on various miscellaneous jobs as my role was not centralized in either the front end or back end.

## Design Goals

* Intuitive Simulation Addition  
  * Our main design goal was to be able to add new simulations with relative ease. Because of the nature of cell society, being able to add new simulations quickly was a huge focus, so we abstracted our code heavily so that users could efficiently create novel simulations. This can be seen with our highly abstracted architecture, which allows users to define a simulation based on previously defined rules and metadata. Then, with modularized and reusable neighborhood/parameter components, everything is streamlined to integrate the new rules. Everything was encapsulated to separate the fundamental simulation mechanics from the actual implementation, so the definition was independent and logical.

 

* Comprehensive but Understandable User Interface  
  * Another goal we focused on was creating an intuitive user interface so that those unfamiliar with the project would be able to create and run new simulations with relative ease. On top of properly communicating features, we wanted the elements presented to users to be easily viewable and stylish with use of themes and customizable view properties. We also added Edit and View mode, so that the user would not inadvertently update the simulation state when they are trying to just view the simulation.

 

* Easily Modifiable Configuration  
  * We created a form element that allowed the user to create a default simulation with a specified neighbor policy, edge policy, simulation type, various metadata fields, grid size, and custom parameter values. Additionally, you could click on a cell and cycle through all the available states to update them in real-time. This allowed us to create new simulations without editing any part of XML files. By allowing the user to modify the simulation configuration and set it up without needing to know XML, we greatly improved the experience for creating and saving new simulation XML files.

 

#### How were Specific Features Made Easy to Add

* Feature \#1 \- Neighbor and Edge Policy Configuration  
  * Creating new neighbor and edge policies were intended to be very easy to add due to the necessity of each for each simulation. Every configuration must include some sort of neighbor and edge policy, so we wanted an explicit, but modularized way of defining this behavior. Both of these were created by implementing neighbors and edge packages that defined an according class and interface to define preset behaviors. Then, within the package, one could identify a certain coordinate system for the neighbors (getDirections) and an edge strategy (adjustCoordinate) for the edge policy. This is fairly easy to add because the design allows for just a new file to be created for both, such that you can define any sort of direction or edge policy. With the addition of just one file and one new method each, the code will automatically create the new policy for both types of configuration.  
      
* Feature \#2 \- Add new themes and language  
  * We created a utility class called FileUtility with a method called getFileNamesInDirectory(String directoryPath, String ending), which allowed us to get all the file names in a given directory with a provided ending (i.e. .properties). Using this method, we could find all the theme files (ending in .css) and all language files (ending in .properties) located in a specific resource path. This made it easy to add new themes and languages.   
      
* Feature \#3 \- Creating a new grid and cell shape type in the UI  
  * We utilized a GridViewFactory to generate grids with cells of various shapes. This was done by defining several abstraction gridView objects that defined layouts for rectangle, hexagon, and triangle tilings by adding the equivalent abstracted cellView classes. To a new grid/cell shape, one would simply have to define their shape within GridViewFactory and provide implementations for the shapeCellView and how they would be aligned in the shapeGridView. The differences in implementation are heavily dependent on the type of shape. Some shapes will have simple tiling configurations (like Rectangle) while others need more complicated rules (like Hexagon) but the process of adding the shape is simple.

 

## High-level Design

#### Core Classes and Abstractions, their Responsibilities and Collaborators

* Class \#1 \- SimulationRules  
  * At the heart of our project is the SimuationRules class, which is an abstract class that defines the parameters, neighbor configuration, and rules of a simulation. It’s the foundation for defining any sort of simulation behavior. Its responsibilities include defining the core structure for simulation rules, managing parameters for the different simulations, and handling neighbor configuration. It also has a large responsibility in defining how states evolve, by creating an overridable method for getting the next state (getNextState) for one cell or getting the next state for all cells (getNextStateForAllCells). This is done so that simulations can choose whether they want to do one sweep of the cell, handling each, one by one, or doing a simultaneous update on each cell. The only abstracted method is a getNumberStates method which returns the total number of states for a simulation rule.   
  * Simulation Rule collaborates with the Grid class to gain access to all the cells, the Cell class to handle updates to an individual Cell, the CellUpdate class to handle updating a new Cell, the GetNeighbors Class to define how a cell’s neighbors are determined, and the Parameter class to store the parameterized values for various simulations. It also interacts with error throwing classes like InvalidParameterException to handle issues with the parameters.   
  * SimulationRules is created in a factory-like method getRules in the SimulationConfig file, which works by using reflection to create a new instance of the SimulationRules class, depending on the simulationName, parameters, and GetNeighbors provided.

 

* Class \#2 \- Grid  
  * The grid abstraction allows the model classes to interact with the grid without knowing how it is implemented. Internally, we used private Map\<Point2D, Cell\> myCells to store our grid, however, none of our public methods revealed how the grid was stored, so this implementation could be swapped out with a 2D list or other data structure without impacting how other classes used the grid. We included a getCell and addCell method to allow the user to easily get or set the cell located at a specific row or column in the grid. The grid also contains an edge strategy, so whenever getCell is called with a specific coordinate, the edge strategy’s method adjustCoordinate() takes the requested coordinate and adjusts it to a new coordinate to find the cell based on the current edge policy. The method updateGrid(Simulation simulation) is used to do one step in the simulation and update the grid state accordingly. The updateGrid first gets a list of CellUpdates from calling the simulation rules getNextStatesForAllCells(), which is all the cells that need to be updated in this iteration of the simulation. Using the CellUpdate objects, the grid calls a private method updateCell which replaces the current cell in the data structure at the provided point with the new cell provided in the CellUpdate object. The update grid returns the list of cell updates after the update is completed so that the view can also update accordingly. The grid contains a collection of Cell objects and is responsible for maintaining the collection of cells as the program is running. The updateGrid() method allows for the model and view to interact; when updateGrid is called the Grid model is updated and a list of cell updates is provided to the view to allow it to update all the visual aspects of the grid.

 

* Class \#3 \- Cell  
  * The cell class is the basic block that composes all simulations. At its core, a Cell object has a state for representing its purpose within a simulation, a Point2D object to represent a location in the grid, and an int representing opacity for style purposes. This class has been extended with several subclasses to meet the needs of various simulation types. Currently, we have five cell abstractions (DefaultCell, WaTorCell, ForagingAntsCell, SugarscapeCell, and DarwinCell) that contain a variety of different methods and instance variables.   
  * The Cell class collaborates mainly with other Cells, the Grid and GridUtility classes, and SimulationConfig. When generating a new grid, the cell type will be determined by SimulationConfig (using the getNewCell() method) based on the current simulation for which the grid is being created. Cell state can also be defined by input from a .xml file or by updating it in the edit mode. Cell changes within a simulation occur based on relation to other Cell neighbors and are updated through the Grid class (using updateCell and attemptAddCell in tandem.)

 

* Class \#4 \- MainController  
  * The responsibility of the MainController is to handle the interactions between the model and view and any of the complicated update logic occurring between multiple view components. The MainController was responsible for storing and updating the current window’s simulation rules and metadata, the grid model, and updating the view’s various components such as the sidebar and simulation view whenever the animation was running. The MainController also handled the transition between the splash screen view and the main view of the program. Whenever a file was saved or opened, the main controller was in charge of updating the necessary UI components and models to reflect the new configuration file. In summary, the MainController handled any updates that impacted multiple view components or any major interactions between the view and model.

 

## Assumptions or Simplifications

For a list of all of our assumptions, please visit our README document.

* Decision \#1 \- The state of the cell within a simulation can always be represented by an integer value.   
  * One cell is only ever able to represent one state at a time.  
  * The simulation can only contain a maximum of Integer.MAX\_VALUE states in total. 

 

* Decision \#2 \- Grid and simulation configuration updates should only be allowed when the user is in edit mode  
  * To prevent the user from inadvertently updating the cell states in the grid, neighbor configuration, grid shape, edge type, or simulation rules while trying to view the simulation, we created an edit and view mode. In view mode, the user is not allowed to update the configuration until the switch to edit mode. This is to prevent the user from accidentally updating the configuration when they are just trying to view it. Our design allows for dynamic updates while the simulation animation is running, but we decided to disable this feature unless the user is in edit mode to prevent any confusion from the user.

 

* Decision \#3 \- RockPaperScissors  
  * One large assumption we have with the RockPaperScissors simulation is in the way the simulation works. Rather than have the current cell lose/beat only the next or previous state, we assume that for any x number of states, the current state will lose to the x/2 states before it, and beat the x/2 states ahead of it. Despite the changes, we still maintain a circular route of winners/losers. Furthermore, to determine the next state of a cell, if more than one neighbor is above the winning threshold, we will update the cell with the furthest winning cell. This makes sense so that the “best” possible winner is selected. The overall assumption was completed because with a large number of states, the likelihood of having neighbors of state x \+ 1 or x \- 1 is very low, so we wanted a more realistic application. 

* Decision \#4 \- Darwin Assumptions  
  * We assumed a few aspects of the Darwin simulation. For one, we assume that for each move instruction, each pixel represents one block in our simulation. Thus, a MOVE 4 will move a species 4 blocks, rather than 4 pixels. Another assumption is that degrees will get rounded to the nearest 45 degrees. So, if somebody puts something like 30 degrees, we will round that and handle the orientation depending on the neighbor configuration. For example, for 30 degrees and a 0-degree initial orientation, a right turn for Moore rounds to 45 degrees, while for Von Neumann, it rounds to 0 degrees.

 

## Changes from the Original Plan

* Change \#1 \- View Classes  
  * The view classes are where we deviated the most from our original design. Initially, we planned to have an abstract View class that would extend JavaFX’s Group object, which would be used for all our View components. We additionally included in the design that we would split our view into 3 components: CellView, GridView, and SimulationView.   
  * In our final design, we never created an abstract View class, because we decided that extending Group, VBox, and HBox directly for our view components instead of an abstract View class sufficed.   
  * We made CellView and GridView abstract classes which allowed for multiple cell/grid shape types. For example, our HexagonGridView consisted of a 2D list of HexagonCellViews properly initialized to the correct size and location so that the hexagons would align properly. We created a GridViewFactory, which used the Factory design pattern to easily create a new grid view of a specified shape type, size, and number of rows and columns.  
  * We retained the SimulationView class but in our final design, this class’s sole responsibility was dynamically updating the grid view appearance and state based on the animation and user input. We decided to move the functionality of the buttons and various other displays initially planned as part of the SimulationView to other classes to ensure that each class had a single responsibility.   
  * We created a SidebarView, EditModeView, ViewModeView, and various other view components not included in our initial design to account for new features and to separate the parts of the screen into logically separate components.  
  * Our initial view design did not include a controller to handle the updating of our view components. We later added a MainController, PreferencesController, and ThemeController to handle the updating of our view classes and the interactions between our view and model classes.

 

* Change \#2 \- Changes to SimulationData and Simulation classes to record  
  * We had initially designed the SimulationData class to hold various pieces of information related to the simulation and return them using get methods as requested by the Simulation class. We moved this functionality into a record called SimulationMetaData that holds the same information in a more easily accessible manner.  
  * We also moved from the idea of an abstracted Simulation class for all simulations to a Simulation record holding the SimulationRules and SimulationMetaData. This made it so that almost all “decision-making” functionality within the simulation was located in SimulationRules.

 

* Change \#3 \- SimulationRules updates  
  * Originally, SimulationRules was an abstract class that was implemented by a Simulation Class. We removed the Simulation class due to repetitiveness and made the SimulationRules more representative of a global simulation class. This reduced the number of classes we had to write by a large quantity.   
  * SimulationRules was originally defined with a getNeighbors() and getNextState() class. We eliminated the getNeighbors method by refactoring that into its own GetNeighbors abstract class. This was done to make creating neighbor configurations far easier, and so the method was replaced by a class.   
  * getNextState, although it remains, was modified as we had to add a getNextStateForAllCells as well as remove it as an abstracted method. Due to some simulations requiring one pass through all cells simultaneously, versus only needing to get the state for one cell at a time, we had to split the logic up into two methods \- getNextState and getNextStateForAllCells. This was done by creating a default implementation that could be overridden.

 

* Change \#4 \- Splitting of XMLHandler and XMLWriter into multiple classes  
  * Originally, we had put all XML functionality into a single class called XMLHandler. Because of the base differences between reading and writing to .xml files, we decided splitting this functionality between two classes would be best for user comprehension and isolation principles.   
  * The main differences between the two are the format from which the data is being parsed and the use of Document objects vs. Transformer objects for the data translation.

 

## How to Add New Features

* Feature \#1 \- How to add a new simulation rules  
  * To add a new Simulation Rules file, the steps are the following:  
1. In cellsociety.model.simulation.rules, define a new rules class which extends SimulationRules within the rules package. It should have the naming convention “simulationName”Rules.java  
2. Each simulation rule class should implement:  
   1. A constructor that calls the super(parameters, myGetNeighbors  
   2. Either getNextState or getNextStateForAllCells to determine the updates that will occur to each cell   
   3. A getNumberStates method to define the number of possible states in the simulation  
3. If additional parameters are needed, define a cell type with naming convention simulationCell  
   * Due to the reflective nature of our code, all you have to do is create the above file, and it will be automatically updated within the codebase. 

 

* Feature \#2 \- How to add a new theme/language  
  * To add a new theme you only needed to create a new css file in the correct directory called NAME.css where NAME was the display name of the theme. Inside the file, you only need to update 5 color variables to update the appearance of all the elements in the UI. Example:

\`\`\`  
\* {  
  \-fx-primary: \#1D1616;  
  \-fx-secondary: \#EEEEEE;  
  \-fx-light: \#232121;  
  \-fx-accent: \#D95F59;  
  \-fx-warning: \#D84040;  
}  
\`\`\`

* Similarly, for a new language, you only need to create a new properties file in the correct directory named LANGUAGE.properties where LANGUAGE is the display name of the language for the theme selector. Inside the file, you only need to include the key-value pairs for your language. To ensure that you have all the key-value pairs, you can copy and paste the English language file and edit the messages to your new language. The theme/language selector and UI will automatically update when you rerun the program after adding the new theme or language file.  
  * For more information see CREATE\_THEME.md and CREATE\_LANGUAGE.md in the resources folders.

 

* Feature \#3 \- How to create a new XML file  
  * To add a new .xml configuration file, one can:  
    * Write the .xml file manually and move it to the proper directory  
    * Generate a new configuration file through the simulation UI  
  * For writing .xml files manually:  
    * There are several example files for each simulation in marked folders under resources, but every configuration file will include these elements:  
      * Type \- what variety of simulation is being generated?  
      * Title \- what is the simulation called?  
      * Author \- who made this configuration?  
      * Description \- what does this simulation do?  
      * NeighborType \- how do you want a cell to perceive neighbors?  
      * NeighborLayer \- how far away can cells see neighbors?  
      * GridHeight and GridWidth \- how big is the simulation grid?  
      * InitialConfiguration \- what are the starting states of all cells in  the grid?  
      * Parameters \- what additional info does the simulation need to run properly?  
  * For generating through the simulation UI:  
    * You can use the Edit Mode to build the grid however you’d like and use the Save to XML button to do just that  
    * You can pause any currently running simulation and use the Save to XML button to save its current configuration

 

* Feature \#4 \- How to add new neighbor configuration  
  * To add a new GetNeighbor configuration, the steps are the following:   
1. In the cellsociety.model.simulation.neighbors package, create a new file named “NewNeighborType”Neighbors.java which extends getNeighbors  
2. Implement the constructor, which should call super(layers) to initialize the number of layers to search for  
3. Override getDirections(int row, int column)  
   1. Define the pattern of neighbor retrieval that this configuration should use  
         
   * Due to the reflective nature of our code, all you have to do is create the above file, and it will be automatically updated within the codebase.   
     

 

#### Features Not Yet Done

We completed all core features – here are some features we would have liked to implement if given more time:

* Feature \#1 \- Backwards stepping and resetting   
  * To enable resetting to the initial grid, we could easily store in memory or an XML file the grid state when a new simulation is created. When the reset button is clicked we can update the view to use the state stored from the initial grid.  
  * To allow for backward stepping, we could store in memory a history of the previous steps or updates to the simulation. When the back button is hit, we would simply need to update the UI to the most recent value in the history.  
      
* Feature \#2 \- A Mini-Grid view element  
  * The Mini-Grid view has an additional extension feature that we found interesting but less useful than other view extensions like the zoom feature or cell histogram. To implement this, we would initialize what would be essentially another gridview in a vacant corner when zooming to show users their zoomed position relative to the whole grid. We would also need to define some red stroke linked to the zoom boundary to record user location and display it on the “mini map.” This stroke would need to be dynamically scalable similar to the zoom window.  
      
* Feature \#3 \- Infinite grid edge type  
  * This is a feature that we found to be a little more complex, so if given more time, we would have loved to take a stab at an infinite grid edge type. Our existing configuration allows for toroidal and mirroring of a grid within the edge package, so we would need a new edgeStrategy for this. To do so, we would likely need to override the adjustCoordinate policies to return the coordinates when a coordinate is out of bounds, rather than adjusting it. Then, we would need to modify the Grid class to dynamically expand when it is out of bounds, ultimately putting the coordinate back in bounds. We would then need to target the zoom of the grid, so the grid doesn’t just overtake the entire screen, so we would have to add an automatic zoom out to adjust the shape.   
* Feature \#4 \- Help documentation  
  * After witnessing another group present with this feature, we realized how helpful it would be to include this in our code. Although this was an extension feature, we had already implemented hovering tooltips over cells, so we could implement a very similar feature when deciding what simulation to choose from. Otherwise, we would have just created a separate help page to explain various simulations.