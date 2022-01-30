I have neither given nor received unauthorized aid on this assignment. - Asif-Ul Islam

SpellCheck.java 

main method - Reads in a dictionary of words and then asks the user to input a file to read. Gives the user the option to print, enter a new file or quit
	      the program. 

readDict - Reads in a file of words which is supposed to be the dictionary to check whether words in the file provided by user has misspelled words.

readFile - This methods reads the user provided file of text and then puts the words into a ArrayList of word objects 

misTable - Adds wrongly spelled words into a Array of ArrayList of word objects called misWord.

misTableContains - Checks if a Word object is in the misWord.

misTableGet - Gets a Word from the misWord.

suggest - Gives the user suggestions for correct words for misspelled words in the user given file. This is done by inserting letters of the alphabet
	  between every letter in the word and also by replacing every letter in the word with a letter in the alphabet and then the method checks if the 
	  suggested word is in the dictionary to finally suggest it to the user.

prompt - Provides the user with a prompt after printing a word in the console if the user wants to ignore all the same words or replace all the words or 
	 go to the new word or just quit.

checker - Main spell checking method.


SpellSorter.java 

It has all the same methods as SpellCheck.java and some extra methods

swap - Swaps two elements in a String array

Quicksort - Sorts an array of Strings using quicksort

incorrectWords - Prints all the incorrect words in a file in sorted order to an output file. 

  

 