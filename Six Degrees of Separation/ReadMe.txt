

Person.java
Encapsulated Person class with private name, explored boolean, predecessor and ArrayList of friends 
There are getter and setter methods for each. 

SixDegreesDriver.java

Main method - consists of two while loops. The outer while loop 'first' is used to get the user input for the first name if the first name is wrong user 
	      is prompted again. The inner while 'first' is called when user provides a name from the list for first name and then similar process is 
	      repeated for second name. After getting two correct names, the Search method finds the chainList or chain of names connected together.
	      If chainList size is one that means that the two people are not connected. 

readFile - Reads the user provided file for names for finding connections.

Search - Searches and finds the chainList or connection between two names. It stores the connected names in an ArrayList which is iterated backwards in the 
	 main method to get the connection from first name to second name. 

getPerson - Converts from string to Person object.

avgSep - Finds the average degree of separation between the whole group. 

