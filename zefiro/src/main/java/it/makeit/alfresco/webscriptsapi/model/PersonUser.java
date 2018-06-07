package it.makeit.alfresco.webscriptsapi.model;

public class PersonUser {
	private String userName;
	private String lastName;
	private String firstName;

	public String getUserName() {
		return userName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
