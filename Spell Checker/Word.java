
public class Word {

	private boolean ignore;
	private String replacement;
	private boolean replace;
	private String word;
	private int lineNum;
	
	public Word(String word){
		this.lineNum = 0;
		this.word = word;
		this.ignore = false;
		this.replacement = word;
		this.replace = false;
		
	}
	
	public void setIgnore(boolean choice) {
		this.ignore = choice;
	}
	
	public void setReplacement(String word) {
		this.replacement = word;
	}
	
	public void setReplace(boolean choice) {
		this.replace = choice;
	}
	
	public void setlineNum(int num) {
		this.lineNum = num;
	}
	
	public boolean getIgnore() {
		return this.ignore;
	}
	
	public String getReplacement() {
		return this.replacement;
	}
	
	public boolean getReplace() {
		return this.replace;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public int getlineNum() {
		return this.lineNum;
	}
}
