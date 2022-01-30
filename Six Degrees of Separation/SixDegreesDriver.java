//I have neither given nor received unauthorised aid on this assignment. - Asif-Ul Islam.
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class SixDegreesDriver {

	static ArrayList<Person> peeps;
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		

		String fn = "friends.txt";
		String outfile = "output.txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
		
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("** Six Degree of Separation **");
		System.out.println("File Reading Complete.");
		
		boolean first = true;
		while(first) {
			peeps = new ArrayList<Person>();
			readFile(fn);
			System.out.println("Enter the name of the first person:");
			Person personA = new Person((scan.nextLine())); //this is used for the name when input name is not in list
			Person realPeepA = getPerson(personA.getName());
			if(peeps.contains(realPeepA)) {
				boolean second = true;
				while(second) {
					System.out.println("Enter the name of the second person:");
					Person personB = new Person((scan.nextLine())); //used for name when input name is not in list.
					Person realPeepB = getPerson(personB.getName());
					if(peeps.contains(realPeepB)) {
						
						ArrayList<String> mainList = new ArrayList<String>();
						mainList = Search(realPeepA, realPeepB);
						
						if(mainList.size() <= 1) {
							System.out.println("The two people entered are not connected.");
							second = false; //get out of second while loop
						}
						else {
							System.out.println("Relation: ");
							for(int i = mainList.size()-1; i >= 0; i--) {
								System.out.print(mainList.get(i)+" ");
								out.write(mainList.get(i)+" ");
							}
							out.write("\n");
							System.out.println();
							int degrees = mainList.size()-1;
							System.out.println("Degrees of Separation: "+degrees);
							out.write("Degrees of Separation: "+degrees+"\n");
							second = false;
							
						}
							
						
						
					}
					else
						System.out.println("Error: "+personB.getName()+" is not in the list");
					
				}
				System.out.println("Want to try another query? (y/n)");
				String answer = scan.nextLine();
				if(answer.equals("n"))
					first = false;	//get out of first while loop		
			}
			else
				System.out.println("Error: "+personA.getName()+" is not in the list");
		}
		System.out.println("Computing Average Degree of Separation...");
		System.out.println("Average Degree of Separation: "+avgSep());
		out.write("Average Degree of Separation of entire group: "+avgSep());
		

		
		
		
	scan.close();	
	out.close();	
		

	}

	
	  public static void readFile(String file) throws IOException{
		  Scanner fr = new Scanner(new File(file));
	  
		  while(fr.hasNextLine()) {
	  
			  String line = fr.nextLine();
			  String[] names = line.split("\t");
	  
	  //use first name to create Person 
			  Person person = new Person(names[0]);
	  
	  //use the rest of names for the friendsList 
			  for(int i=1; i<names.length; i++) 
				  person.getFriendsList().add(names[i]);
		  
			  peeps.add(person);
			  
		  
	  }
	   
	  }
	 
	   

	public static ArrayList<String> Search(Person A, Person B) {
		A.setExplored(false);
		B.setExplored(false);
		A.setPred(null);
		B.setPred(null);

		boolean found = false;
		ArrayList<Person> ExploreList = new ArrayList<Person>();
		

		ExploreList.add(A);
		A.setExplored(true);
		while (ExploreList.size() != 0 && !found) {
			Person X = ExploreList.remove(0);
			if (X.getName().equals(B.getName())) {
				found = true;
			}

			else {

				for(Person p: peeps) {
					if(p.getName().equals(X.getName())) {
						ArrayList<String> friends = p.getFriendsList();
						for(String f: friends) {
							Person y = getPerson(f);
							if(y.getExplored()==false) {
								ExploreList.add(y);
								y.setExplored(true);
								y.setPred(X.getName());
								
								
							}
						}
						
					}
				}

			}
			

		}
		ArrayList<String> chainList = new ArrayList<String>();
		
		String current = B.getName();
		while(current != null) {
			chainList.add(current);
			current = getPerson(current).getPred();
		}
		
		return chainList;
		
		
		
		
		

	}

	public static Person getPerson(String name) 
	{
		
		for(int i = 0; i < peeps.size(); i++) {
			if(peeps.get(i).getName().equals(name)) 
				return peeps.get(i);			
		
		}
		return null;
		

	}
	
	public static double avgSep() throws IOException
	{
		
		double total = 0;
		String fn = "friends.txt";
		peeps = new ArrayList <Person>();
		readFile(fn);
		int size = peeps.size();
		int[][] connectArray = new int[size][size];
		ArrayList<String> connectList = new ArrayList<String>();
		for(int i = 0; i<size; i++) {
			for(int j = 0; j<size; j++) {
				peeps = new ArrayList <Person>();
				readFile(fn);
				connectList = Search(peeps.get(i), peeps.get(j));
				if(connectList.size() <= 1){
					connectArray[i][j] = size+1;
				}
				else
					connectArray[i][j] = connectList.size()-1;
			}
			
		}
		for(int i = 0; i< size; i++) {
			for(int j = 0; j<size; j++) {
				total += connectArray[i][j];
			}
		}
		
		total -= size*(size+1); //Removing all the pairs with same name
		double average = total/(size*(size-1));
		
		return average;
	}
	
	
	
	
	
	
	

}
