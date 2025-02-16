# cell society

## TEAM NUMBER

## NAMES

This project implements a cellular automata simulator.

### Timeline

* Start Date:

* Finish Date:

* Hours Spent:

### Attributions

* Resources used for learning (including AI assistance)

* Resources used directly (including AI assistance)

- https://github.com/antoniopelusi/JavaFX-Dark-Theme
- For understanding falling sand rules: https://www.youtube.com/watch?v=5Ka3tbbT-9E

### Running the Program

* Main class:

* Data files needed:

* Interesting data files:

* Key/Mouse inputs:

### Notes/Assumptions

* Assumptions or Simplifications:
    * The state of a simulation is stored as an integer value.
        * The default state for a simulation is assumed to be 0 and simulation states are a positive
          integer value.
        * The number of states method lets the front end know how many states to expect. For
          example, if the number of states is 3, then the valid states should be 0 (default), 1, 2.
    * We assumed the typical usage of Percolation such that water flows from the top and only
      considers the adjacent neighbors (not the diagonals).
    * In WaTor World, we assumed that the sharks will be processed first, so if a shark moves to a
      cell before a fish moves, the fish will be removed and will not have the opportunity to move.
      Thus, we process sharks first, then as long as the fish cells are untouched, we move those to
      new cells.
    * Reproduction of sharks and fish occurs one iteration after there reproduction time has passed.
      For example, if the reproductive time of a shark is set to 2, then it will move around for its
      first 2 iterations of life and then reproduce on its third iteration/step of life.
    * For RockPaperScissors, we assumed that for any x number of states, the current state will lose
      to the n/2 numbers before it, and beat the n/2 winners ahead of it (in the circular route).
      Furthermore, to determine the next state of a cell, if more than 1 cell are above the
      threshold, we will update the cell with the furthest winning cell. By this, we mean that in
      the n/2 cells that beat the current state, we will take the furthest cell from the current
      state.

    * We assumed the typical usage of Percolation such that water flows from the top and only
      considers the adjacent neighbors (not the diagonals).
    * In WaTor World, we assumed that the sharks will be processed first, so if a shark moves to a
      cell before a fish moves, the fish will be removed and will not have the opportunity to move.
      Thus, we process sharks first, then as long as the fish cells are untouched, we move those to
      new cells.
    * We assumed that the grid state should only be modifiable when the user is in our "edit mode,"
      so that users do not unintentionally modify the simulation. However, only a single if
      statement would need to be changed to allow grid state updating while the simulation is
      running.
    * In Falling Sand, if a sand cell is able to moved directly down, it will automatically move
      down. If it can only move to its left or right diagonal, it will randomly choose one of the
      diagonals if both of them are empty. If only one diagonal is empty and it cannot fall directly
      down, it will move to the only open diagonal.

* Known Bugs:

* Features implemented:

* Features unimplemented:

* Noteworthy Features:

### Assignment Impressions


