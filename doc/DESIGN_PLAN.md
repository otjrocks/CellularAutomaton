# Cell Society Design Plan
### Team Number: 4
### By: Owen Jennings (otj4), Justin Aronwald (jga18), Troy Ludwig (tdl21)


#### Examples

Here is a graphical look at my design:

![This is cool, too bad you can't see it](images/online-shopping-uml-example.png "An initial UI")

made from [a tool that generates UML from existing code](http://staruml.io/).


Here is our amazing UI:

![This is cool, too bad you can't see it](images/29-sketched-ui-wireframe.jpg "An alternate design")

taken from [Brilliant Examples of Sketched UI Wireframes and Mock-Ups](https://onextrapixel.com/40-brilliant-examples-of-sketched-ui-wireframes-and-mock-ups/).



## Overview
Through this project, our team hopes to create an interactive Cellular Automata simulations. Our program will allow for multiple different simulation types configured by formatted XML files. The program will begin by prompting the user to select a simulation file to load, which runs the simulation on load. The application will show a grid view that animates (steps through) each predetermined time step. 
Our main goals include a functional simulation created by proper encapsulated code and abstraction. We hope to build on the skills developed in class and work on effectively modularizing the code. We want the design to be flexible in the sense that new simulations added are easily loaded and new user features are easily integrable.    

At a high level, we know that we are going to include a hierarchy for the grid. The cell class will be abstracted to handle the various cell cases. We are going to ensure proper separation between the UI elements and the translation of files into the grid-like system. Broadly, each cell is going to have methods for updating its state and relations to its neighbors.

In terms of high level flow, users are going to input an XML file with the grid coordination. Then, each cell's status will be determined by a set of rules predefined for that specific simulation. Then, each cell will be processed and the grid will be updated to accommodate each of the cell changes. Finally, the UI will be updated to display the changes to the user.
## User Interface


## Configuration File Format


## Design Details


## Use Cases


## Design Considerations


## Team Responsibilities

 * Team Member #1

 * Team Member #2

 * Team Member #3
