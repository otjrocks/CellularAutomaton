# Refactoring Lab Discussion
### Owen Jennings (otj4), Justin Aronwald (jga18), Troy Ludwig (tdl21)
### Cell Society Group #4


## Design Principles

* Open/Close
  *   The definition of Open/Close is that software is open for extension and closed for modification. Our interpretation of this is that APIs should always have methods that can't be changed so that future programs will be guaranteed the functionality. However, if new functionality is wanted to be made, an API should be extended so that they can implement that new functionality. Essentially, you can add on to the base, but you can't change the base.
* Liskov Substitution
  * The LSP states that an OOP, if we substitute a superclass object referenced with an object of any of its subclasses, the program shouldn't break. Our interpretation of this is that because the subclasses must inherit all the methods and features of the superclass, if you were to switch the superclass with the subclass, it would function properly. 


### Current Abstractions

#### Abstraction #1: Cell Abstraction

* Current embodiment of principles
  * It adheres to the open/close principle because we haven't modified any of the original implementation of the cell class, but we have extended upon in it in a new cell class.
  * In regard to LSP, all subclasses are valid implementations of the Cell superclass. Any operation we do on a specific instance of a cell can be done an instance of the abstracted cell.

* Improved embodiment of principles
  * In the future, if we need to add more Cells, we need to ensure that our Cell class is used as the default, and extend the Cell object, rather than just adding new functionality to the original class.


#### Abstraction #2: SimulationRules Abstraction
* Current embodiment of principles
    * It adheres to the open/close principle because we haven't modified any of the original implementation of the SimulationRules class, but we have extended upon in it in new SimulationRules class.
    * In regard to LSP, all subclasses are valid implementations of the SimulationRules superclass. Any operation we do on a specific instance of a cell can be done an instance of the abstracted cell.

  * Improved embodiment of principles 
    * In the future, if we need to add more SimulationRules, we need to ensure that our Cell class is used as the default, and extend the Cell object, rather than just adding new functionality to the original class.


#### Abstraction #3: SidebarView
* Current embodiment of principles
  * SidebarView extends Vbox and adheres to the Open/Close principle because it's easily extendable by calling getChildren.add(). However, the core components of it are set in stone

* Improved embodiment of principles
  * Potentially have the Sidebar furthered abstracted into its components such as the information display, alert display, metadata display, etc

### New Abstractions

#### Abstraction #1: New Cell Type
* Description
  * We need new cell types for the falling sand simulation. This will be needed to support gravity.

* How it supports making it easier to implement new features
  * Rather than modifying the original Cell, we are just adding to it. We don't need to modify anything for the new use case.


#### Abstraction #2: New Simulation Rules
* Description
  * Similarly, we need new SimulationRules classes for the falling sand simulation, foraging ants, etc.

* How it supports making it easier to implement new features
  * Likewise, rather than modifying the original SimulationRules class, we are just adding new simulationRules to its subclasses. We don't need to modify anything for the new use cases.



#### Abstraction #3: Theme Selection
* Description
  * For theme selection, we could have a default abstract class that defines the general look. Then we can implement specific themes that extend the default theme. 

* How it supports making it easier to implement new features
  * Rather than just defining multiple separate classes (or if statements) that repeat the same code, we can just define one centralized class that has subclasses to inherit and can control the main theme.



