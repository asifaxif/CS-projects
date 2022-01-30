

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileSearcher {
	
	static ArrayList<Word> wordList = new ArrayList<Word>();
	static File[] fiList;
	static BinarySearchTree<Word> bst = new BinarySearchTree<Word>();

	public static void main(String[] args) throws IOException{ 
		// TODO Auto-generated method stub
		
		Scanner scan = new Scanner(System.in);
		
		boolean valid = true;
		
		File file = new File(args[0]);
		
		scanFiles(file);
		
		while(valid) {
			System.out.println("Please enter a command (a, s, or q)>>");
			
			String choice = scan.nextLine();
			
			if(choice.equals("a")) {
				bst.printTree();
			}
			else if(choice.equals("s")) {
				System.out.println("Word to find>>");
				
				String wordChoice = scan.nextLine();
				
				Word wchoice = wordExist(wordChoice);
				
				if(wchoice == null) {
					System.out.println(wordChoice+" is not found");
				}
				else {
					System.out.println(bst.find(wchoice));
				}
			}
			else {
				valid = false;
			}
		}
		scan.close();
		

	}
	
	public static Word wordExist(String word) { //converts string to word and returns the word if it exists and null if not
		
		for (Word w: wordList) {
			if (w.getWord().equals(word))
				return w;
		}
		return null;
	}
	
	public static void scanFiles(File folder) throws FileNotFoundException { //looks through folder, directories and sub-directories and adds file name to bst
		
		fiList = folder.listFiles(); //get all files/sub-directories in folder
		
		for (File f: fiList) { //goes through each file in directory folder
			if(!f.isHidden() && f.getName().charAt(0) != '.'){
				if(f.isDirectory()){
					scanFiles(f); // recursive function to check each file
				}
				else {
					String file = f.toString();
					Scanner filereader = new Scanner(new File(file));
					
					while (filereader.hasNextLine()){
						String line = filereader.nextLine();
						String[] words = line.split(" ");
						
						for(int i=0; i<words.length; i++){
							String finalWord = "";
							for (int j=0; j<words[i].length(); j++){
								if(Character.isLetter(words[i].charAt(j))){ //to check if each character is a letter
									finalWord += words[i].charAt(j); //constructs a string word using only letters
								}
							}
							Word w = new Word(finalWord);
							wordList.add(w);
							if (bst.contains(w)){
								bst.find(w).add(f.getName()); //for duplicates get multiple file names for the same word
							}
							else {
								w.add(f.getName()); //if no duplicates exist
								bst.insert(w);
							}
						}
						
					}
				}
			}
			
		}
	}

}
