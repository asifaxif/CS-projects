// I have neither given nor received unauthorized aid on this assignment. - Asif-Ul Islam
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SpellSorter {
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Word>[] misWord = new ArrayList[52];
	
	
	public static QuadraticProbingHashTable<String> dictionary = new QuadraticProbingHashTable<String>();
	public static ArrayList<Word> filewords = new ArrayList<Word>();

	public static void main(String[] args) throws IOException { //spell checker function takes a file from user and then goes through misspelled words and gives the option to replace or ignore
		// TODO Auto-generated method stub
		 
		
		dictionary = readDict("Dictionary.txt");
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Reading in Dictionary...");
		System.out.println("Dictionary Read.");
		System.out.println("Please enter a file to spell check>>");
		String inputFile = scan.nextLine();
		readFile(inputFile);
		String outOrder = inputFile.substring(0,inputFile.length()-4)+"_order.txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(outOrder));
		String outCorrect = inputFile.substring(0,inputFile.length()-4)+"_corrected.txt";
		BufferedWriter outC = new BufferedWriter(new FileWriter(outCorrect));
		String outSorted = inputFile.substring(0,inputFile.length()-4)+"_sorted.txt";
		BufferedWriter outS = new BufferedWriter(new FileWriter(outSorted));
		
		boolean main = true;
		
		boolean function = true;
		
		while(function) {
			System.out.println("Print words (p), enter new file (f), or quit (q) ?");
			String answer = scan.nextLine();
			//int lineNum = 0;
			ArrayList<Integer> findLineNum = new ArrayList<Integer>();
			
			if(answer.equals("p")) {
				checker(findLineNum, out, outC, scan, main);
			}
			else if(answer.equals("f")) {
				System.out.println("Please enter a file to spell check>>");
				String newFile = scan.nextLine();
				filewords = new ArrayList<Word>();
				readFile(newFile);
				findLineNum = new ArrayList<Integer>();
				checker(findLineNum, out, outC, scan, main);
				
			}
			
			else {
				System.out.println();
				incorrectWords(inputFile, outS);
				System.out.println("Goodbye!");
				
				function = false;
			}
		}
		out.close();
		outC.close();
		outS.close();
		scan.close();
		
	}
	
	private static void swap(String[] a, int i, int j) { //swaps two elements from a string array
    
    String temp = a[i];
    a[i] = a[j];
    a[j] = temp;
    }
	
	private static void quickSort(String[] a, int start, int end) { //quicksort an array of strings
	
	        
	        int i = start;
	       
	        int j = end;

	        // only examine arrays of more than 3 elements.
	        if (j - i > 2)
	        {
	            
	            String pivot = a[i];
	          
	            while (j > i)
	            {
	                
	                while (a[i].compareTo(pivot) <= 0 && i < end && j > i){
	                    i++;
	                }
	               
	                while (a[j].compareTo(pivot) >= 0 && j > start && j >= i){
	                    j--;
	                }
	                
	                if (j > i)
	                    swap(a, i, j);
	            }
	            
	            swap(a, start, j);
	            
	            quickSort(a, start, j - 1);
	            
	            quickSort(a, j + 1, end);
	        }
	        else { // insertion sort 
	        	int k,m;
	        	String key;
	        	for (k = 1; k < a.length; k++) { //the condition has changed
	        	    key = a[k];
	        	    m = k - 1;
	        	    while (m >= 0) {
	        	      if (key.compareTo(a[m]) > 0) {//here too
	        	        break;
	        	      }
	        	      a[m + 1] = a[m];
	        	      m--;
	        	    }
	        	    a[m + 1] = key;
	        	    //System.out.println(Arrays.toString(inputArray));
	        	  }
	        }
	    }
	
	
	public static String[] incorrectWords(String filename, BufferedWriter outS) throws IOException { //method to print all the misspelled words in sorted order into a file
		
		filewords = new ArrayList<Word>();
		
		readFile(filename);
		
		dictionary = readDict("Dictionary.txt");
		
		ArrayList<String> incwordarr = new ArrayList<String>();
		ArrayList<String> lineNum = new ArrayList<String>();
		
		for(Word w: filewords) {
			if(!dictionary.contains(w.getWord())) {
				if(!incwordarr.contains(w.getWord())) {
					incwordarr.add(w.getWord());
					lineNum.add(w.getWord()+" "+w.getlineNum());
				}
				else {
				String wrd = "";
				wrd = lineNum.get(incwordarr.indexOf(w.getWord()));
				wrd += " "+w.getlineNum();
				lineNum.set(incwordarr.indexOf(w.getWord()), wrd);
			}}
		}
		
		
		String[] incWord = new String[lineNum.size()];
		
		for(int i = 0; i<lineNum.size(); i++) {
			incWord[i] = lineNum.get(i);
		}
	
		
		
		quickSort(incWord, 0, incWord.length-1);
		
		for(int i = 0; i<incWord.length; i++) {
			outS.write(incWord[i]+"\n");
			
		}
		
		//System.out.println(Arrays.toString(incWord));
		return incWord;
	}
	
	
	
	
	
	
	
	
	
	public static QuadraticProbingHashTable<String> readDict(String filename) throws IOException { //read in a text file with words from dictionary to use as guide for rightly spelled words
		QuadraticProbingHashTable<String> dict = new QuadraticProbingHashTable<String>();
		
		File myfile = new File(filename);
		
		Scanner filereader = new Scanner(myfile);
		
		while(filereader.hasNext()) {
			String word = filereader.nextLine();
			dict.insert(word);
		}
		
		filereader.close();
		return dict;
		
		
	}
	
	 public static ArrayList<Word> readFile(String file) throws IOException{ //read file that the user wants to spell check
		  Scanner fr = new Scanner(new File(file));
		  
		  int count = 0;
		  while(fr.hasNextLine()) {
	  
			  String line = fr.nextLine();
			  String[] words = line.split(" ");
			  count++;
	  
			  for(int i=0; i<words.length; i++){
					String finalWord = "";
					for (int j=0; j<words[i].length(); j++){
						if(Character.isLetter(words[i].charAt(j))){ //to check if each character is a letter
							finalWord += words[i].charAt(j); //constructs a string word using only letters
						}
					}
					Word word = new Word(finalWord);
					word.setlineNum(count);
					filewords.add(word);
			  
		  
	  }
	  }
		  
		  return filewords;
		  }
	
	public static ArrayList<Word>[] misTable(Word word){ //method that adds wrongly spelled words into a hash table like structure
		
		//ArrayList<Word> wordAr = new ArrayList<Word>();
		
		for(int i = 0; i<52; i++) {
			misWord[i] = new ArrayList<Word>();
		}
		
		int index;
		
		char ch1 = word.getWord().charAt(0);
		if (Character.isUpperCase(ch1)) {
			int int_ch1 = (int) ch1;
			index = int_ch1-65;
		}
		else {
			int int_ch1 = (int) ch1;
			index = int_ch1-71;
		}
		if(misTableContains(word)==false) {
			misWord[index].add(word);
		}
		
		
		
		return misWord;
		
	}
	
	public static boolean misTableContains(Word word) { //checks if the mistable contains a certain misspelled word

		int index;
		
		char ch1 = word.getWord().charAt(0);
		if (Character.isUpperCase(ch1)) {
			int int_ch1 = (int) ch1;
			index = int_ch1-65;
		}
		else {
			int int_ch1 = (int) ch1;
			index = int_ch1-71;
		}
		
		//System.out.println(index);
		
		boolean contains = false;
		//String wrd = "";
		if(misWord[index] != null) {
			for(int i = 0; i < misWord[index].size(); i++) {
				if(misWord[index].get(i).getWord().equals(word.getWord())) {
					contains = true;
					i = misWord[index].size();
				}
				else {
					contains = false;
					i = misWord[index].size();
				}
			}
			
			
			
			
			
		}
		else {
			contains = false;
			 
		}
		//System.out.println(contains);
		return contains;
		
		
	}
	
	public static Word misTableGet(Word word) { //get a wrongly spelled word from the mistable
		int index;
		
		char ch1 = word.getWord().charAt(0);
		if (Character.isUpperCase(ch1)) {
			int int_ch1 = (int) ch1;
			index = int_ch1-65;
		}
		else {
			int int_ch1 = (int) ch1;
			index = int_ch1-71;
		}
		
		Word wrd = new Word(word.getWord());
		for(int i = 0; i < misWord[index].size(); i++) {
			if(misWord[index].get(i).getWord().equals(word.getWord())) {
				wrd = misWord[index].get(i);
			}
		}
		return wrd;
	}
	
	public static ArrayList<String> suggest(Word word) { //give the user suggestions for fixing wrongly spelled words
		String w = word.getWord();
		char[] charAr = w.toCharArray();
		ArrayList<String> words = new ArrayList<String>();
		for(int i = 0; i <= charAr.length; i++) {
			for(char j = 'a'; j<= 'z'; j++) {
				words.add(w.substring(0,i)+j+w.substring(i,charAr.length)); //insert
				
			}
		}
		
		for(int i = 0; i < charAr.length; i++) {
			for(char j = 'a'; j<= 'z'; j++) {
				if(i == 0) { //replace
					words.add(j+w.substring(i+1,charAr.length));
				}
				else {
					words.add(w.substring(0,i)+j+w.substring(i+1,charAr.length));
				}
			}
		}
		
		
		ArrayList<String> correctWord = new ArrayList<String>();
		for(String wrd: words) {
			//System.out.println(wrd);
			if(dictionary.contains(wrd)) {
				//System.out.println(wrd);
				correctWord.add(wrd);
			}
		}
		
		return correctWord;
	}
	
	public static void Ignore(Word word) {
		
		int index;
		
		char ch1 = word.getWord().charAt(0);
		if (Character.isUpperCase(ch1)) {
			int int_ch1 = (int) ch1;
			index = int_ch1-65;
		}
		else {
			int int_ch1 = (int) ch1;
			index = int_ch1-71;
		}
		
		for(int i = 0; i < misWord[index].size(); i++) {
			if(misWord[index].get(i).getWord().equals(word.getWord())) {
				word.setIgnore(true);
			}
		
	}
	}
	
	public static void Replace(Word word) {
		
		int index;
		
		char ch1 = word.getWord().charAt(0);
		if (Character.isUpperCase(ch1)) {
			int int_ch1 = (int) ch1;
			index = int_ch1-65;
		}
		else {
			int int_ch1 = (int) ch1;
			index = int_ch1-71;
		}
		
		for(int i = 0; i < misWord[index].size(); i++) {
			if(misWord[index].get(i).getWord().equals(word.getWord())) {
				word.setReplace(true);
			}
		
	}
		
	}
	
	public static void Replacement(Word word) {
		
		int index;
		
		char ch1 = word.getWord().charAt(0);
		if (Character.isUpperCase(ch1)) {
			int int_ch1 = (int) ch1;
			index = int_ch1-65;
		}
		else {
			int int_ch1 = (int) ch1;
			index = int_ch1-71;
		}
		
		for(int i = 0; i < misWord[index].size(); i++) {
			if(misWord[index].get(i).getWord().equals(word.getWord())) {
				word.setReplacement(word.getWord());
			}
		
	}
		
	}
	
	public static boolean prompt(Word w, BufferedWriter output, Scanner scan, ArrayList<Integer> findLineNum) throws IOException { //prompt the user if they want to ignore, replace or just get the next word
		//System.out.println("--"+w.getWord()+" "+w.getlineNum());
		//boolean inner = true;
		
		boolean loop = true;
			
		System.out.println("ignore all (i), replace all (r), next (n), or quit (q) ?");
		String answer3 = scan.nextLine();
		
		if(!answer3.equals("q")) {
			
		
		if(answer3.equals("i")) {
			w.setIgnore(true);
			//Ignore(w);
			//System.out.println(w.getWord()+" "+w.getIgnore());
			//output to file;
			Integer lineN = Integer.valueOf(w.getlineNum());
			if(findLineNum.size() > 1 && w.getlineNum() !=  findLineNum.get(findLineNum.lastIndexOf(lineN)-1)) {
				output.write("\n");
				output.write(w.getWord()+" ");
				}
			else {
				output.write(w.getWord()+" ");
				}
			//inner = false;
				
			}
		else if(answer3.equals("r")) {
			ArrayList<String> sajest = suggest(w);
			if(sajest.size() == 0) {
				System.out.println("There are no suggestions for replacement.");
				Integer lineN = Integer.valueOf(w.getlineNum());
				if(findLineNum.size() > 1 && w.getlineNum() !=  findLineNum.get(findLineNum.lastIndexOf(lineN)-1)) {
					output.write("\n");
					output.write(w.getWord()+" ");
					}
				else {
					output.write(w.getWord()+" ");
					}
					
				}
			else {
				String sug = "";
				for(int i =0; i<sajest.size(); i++) {
					sug += "("+i+")"+sajest.get(i)+" ";
					}
					
				System.out.println("Replace with "+sug+", "+"or next (n), or quit (q)?");
				String answer4 = scan.nextLine();
				if(answer4.equals("n")) {
					
				}
				else if(answer4.equals("q")){
					loop = false;
					//inner = false;
				}
				else {
					
				}
				int num = Integer.parseInt(answer4);
				w.setReplace(true);
				//Replace(w);
				//System.out.println(w.getReplace());
				
				w.setReplacement(sajest.get(num));
				//Replacement(w);
				//output file;
				Integer lineN = Integer.valueOf(w.getlineNum());
				if(findLineNum.size() > 1 && w.getlineNum() !=  findLineNum.get(findLineNum.lastIndexOf(lineN)-1)) {
					output.write("\n");
					output.write(w.getReplacement()+" ");
					}
				else {
					output.write(w.getReplacement()+" ");
					}
					//outC.write(w.getReplacement()+" ");
				}
				
			//inner = false;	
			}
		
		else {//next
			//output file;
			Integer lineN = Integer.valueOf(w.getlineNum());
			if(findLineNum.size() > 1 && w.getlineNum() !=  findLineNum.get(findLineNum.lastIndexOf(lineN)-1)) {
				output.write("\n");
				output.write(w.getWord()+" ");
				}
			else {
				output.write(w.getWord()+" ");
				}
			//inner = false;
			}
	  
	  }
		else if(answer3.equals("q")) {
			output.write(w.getWord()+" ");
			loop = false;
			System.out.println("Spell check complete!");
		}
		return loop;
	}
	
	
	
	
	public static void checker(ArrayList<Integer> findLineNum, BufferedWriter out, BufferedWriter outC, Scanner scan, boolean main ) throws IOException { // main spell checker method
		
			
		
		for(Word w: filewords) {
			findLineNum.add(w.getlineNum());
			//System.out.println("--"+w.getWord());
			
			if (main == true) {
				
			if(!dictionary.contains(w.getWord())) {
				//output file;
				out.write(w.getWord()+" "+w.getlineNum());
				out.write("\n");
				//System.out.println("--"+w.getWord()+" "+w.getlineNum());
				
					
				
				if(misTableContains(w) == false) {
					System.out.println("--"+w.getWord()+" "+w.getlineNum());
					misTable(w);
					
					main = prompt(w,outC,scan,findLineNum);	
				}
				else { //word in mistable
					
					Word x = new Word(w.getWord());
					x = misTableGet(w);
					
					if(x.getIgnore() == true) {
						//output file;
						Integer lineN = Integer.valueOf(w.getlineNum());
						if(findLineNum.size() > 1 && w.getlineNum() !=  findLineNum.get(findLineNum.lastIndexOf(lineN)-1)) {
							outC.write("\n");
							outC.write(w.getWord()+" ");
						}
						else {
							outC.write(w.getWord()+" ");
						}
						//inner = false;
						
					}
					else if(x.getReplace() == true) {
						//output file;
						Integer lineN = Integer.valueOf(w.getlineNum());
						if(findLineNum.size() > 1 && w.getlineNum() !=  findLineNum.get(findLineNum.lastIndexOf(lineN)-1)) {
							outC.write("\n");
							outC.write(x.getReplacement()+" ");
						}
						else {
							outC.write(x.getReplacement()+" ");
						}
						//inner = false;
						
					}
					else { // user chose next
						System.out.println("--"+x.getWord()+" "+x.getlineNum());
						main = prompt(x,outC,scan,findLineNum);
					  
						
					  }
					 
				}
				//main = false;
			}
			else { //not misspelled
				
				Integer lineN = Integer.valueOf(w.getlineNum());
				if(findLineNum.size() > 1 && w.getlineNum() !=  findLineNum.get(findLineNum.lastIndexOf(lineN)-1)) {
					outC.write("\n");
					outC.write(w.getWord()+" ");
				}
				else {
					outC.write(w.getWord()+" ");
				}
			}
			//main= false;
		}
		//System.out.println("Spell check complete!");
		//main = false;
		else {
			
			if(!dictionary.contains(w.getWord())) {
				//output file;
				out.write(w.getWord()+" "+w.getlineNum());
				out.write("\n");
			}
			Integer lineN = Integer.valueOf(w.getlineNum());
			if(findLineNum.size() > 1 && w.getlineNum() !=  findLineNum.get(findLineNum.lastIndexOf(lineN)-1)) {
				outC.write("\n");
				outC.write(w.getWord()+" ");
			}
			else {
				outC.write(w.getWord()+" ");
			}
			}
		}
		
	}


}
