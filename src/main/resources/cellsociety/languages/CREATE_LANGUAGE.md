# Guide: Create A New Language

* To create a new language, start by creating a file named `LANGUAGE.properties` where `LANGUAGE` is
  what you want your language to called in the language selector.
* After creating the new language file, open the `English.properties` file to find all the required
  key-value pairs for the language strings.
* Modify the key-value pairs from the English file as necessary for your language.
* Save the file and run program as normal to see your new language option.
* **Note**: If you forget or omit certain strings from your language file, the program will display
  a missing key message on the front end, but will not crash. This is to prevent missing keys in the
  language file from breaking the program. If you encounter this issue, make sure you have all the
  required language key-value pairs and rerun the program.

* For various simulation rules, the simulation state name is stored in the language file. To modify
  or add a display name for a specific state in the program use the format `SIMULATIONNAME_NAME_k`,
  where SIMULATIONNAME is the uppercase transformation of the name of your rules class (minus the
  Rules.java ending), and k is the integer representation of the state that you are modifying the
  name for.