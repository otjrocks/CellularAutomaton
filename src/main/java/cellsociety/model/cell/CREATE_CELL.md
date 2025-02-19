# Guide: Create A New Cell

* By default, simulations use the DefaultCell, which is an extension of the Cell class.
* If you need a custom cell type for your simulation, you can create one in this directory.
* To start, create a file named `ExampleCell.java` where `Example` is the name of simulation rules
  that corresponds to the cell you are creating.
* Next edit the code to ensure that you are extending the Cell class and implement any required
  methods.
* Automatically, your cell will be used with your simulation if the name matches the name of your
  rules class. 