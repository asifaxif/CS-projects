
import java.util.ArrayList;
public class Person {

	private String name;
	private boolean explored;
	private String pred;
	private ArrayList<String> friendsList = new ArrayList<String>();
	
	public Person(String person)
	{
		this.name = person;
		this.pred = null;
		this.explored = false;
		
		
	}
	
	public void setName(String n)
	{
		this.name = n;
	}
	
	public void setExplored(boolean valid)
	{
		this.explored = valid;
	}
	
	public void setPred(String p)
	{
		this.pred = p;
	}
	
	public void setFriendsList(String n)
	{
		this.friendsList.add(n);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public boolean getExplored()
	{
		return this.explored;
	}
	
	public String getPred()
	{
		return this.pred;
	}
	
	public ArrayList<String> getFriendsList()
	{
		return this.friendsList;
	}
	
	
	
}
