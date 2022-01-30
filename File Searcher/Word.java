


import java.util.ArrayList;

public class Word implements Comparable<Word> {

	private String word;
	private ArrayList<String> fileList = new ArrayList<String>();
	
	
	public Word(String word) //constructor
	{
		this.word = word;
		
	}
	
	public void setWord(String w) //make string into a word
	{
		this.word = w;
	}
	
	public void setFiles(ArrayList<String> files) //enter an arraylist of files
	{
		this.fileList = files;
	}
	
	public String getWord() //get the word from Word object
	{
		return this.word;
		
	}
	
	public ArrayList<String> getFiles() //get the array list of files 
	{
		return this.fileList;
	}
	
	public void add(String f) //add a new filename
	{
		this.fileList.add(f);
		
	}
	
	public String toString() // convert elements for BST to strings
	{
		return "files containing "+this.word+": "+this.fileList;
	}
	
	public int compareTo(Word other) //compare words in alphabetical order to use as key in BST
	{
		return this.word.compareTo(other.word);
	}
	
}

