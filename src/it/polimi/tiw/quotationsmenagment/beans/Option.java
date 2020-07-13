package it.polimi.tiw.quotationsmenagment.beans;

public class Option {
	private int ID;
	private String type;
	private String name;
	
	public Option() {
		//set attributes with setters
	}
	
	public Option(String type, String name) {
		this.setType(type);
		this.setName(name);
	}

	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	public String toString() {
		return "Option: [ID: " + this.ID + ", name: " + this.name + ", type: " + this.type + "]";
	}
}
