package it.polimi.tiw.quotationsmenagment.beans;

public class User {
	private String username;
	private String email;
	private int ID;
	private boolean isClient;
	
	public User() {
		
	}
	
	public User(String username) {
		this.username = username;
	}
	
	public User(String username, int ID) {
		this.username = username;
		this.ID = ID;
	}
	
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isClient() {
		return isClient;
	}
	
	public boolean isEmployee() {
		return !isClient;
	}

	public void setIsClient(boolean isClient) {
		this.isClient = isClient;
	}
	
	public String toString() {
		return "USER: [ID: " + this.ID + ", username: " + this.username +", isClient: " + this.isClient + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
