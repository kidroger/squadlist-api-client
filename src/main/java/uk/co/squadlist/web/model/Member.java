package uk.co.squadlist.web.model;

import java.util.Date;
import java.util.List;

public class Member {
	
	private String id, username, firstName, lastName, gender, emailAddress, contactNumber, rowingPoints, scullingPoints, registrationNumber, password;
	private Integer weight;
	private List<Squad> squads;
	private Date dateOfBirth;
	
	public Member() {
	}
	
	public Member(String firstName, String lastName, List<Squad> squads, String emailAddress, String password, Date dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.dateOfBirth = dateOfBirth;		
		this.squads = squads;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getRowingPoints() {
		return rowingPoints;
	}
	public void setRowingPoints(String rowingPoints) {
		this.rowingPoints = rowingPoints;
	}
	public String getScullingPoints() {
		return scullingPoints;
	}
	public void setScullingPoints(String scullingPoints) {
		this.scullingPoints = scullingPoints;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public List<Squad> getSquads() {
		return squads;
	}
	public void setSquads(List<Squad> squads) {
		this.squads = squads;
	}
	public String getPassword() {
		return password;
	}	
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Member [contactNumber=" + contactNumber + ", dateOfBirth="
				+ dateOfBirth + ", emailAddress=" + emailAddress
				+ ", firstName=" + firstName + ", gender=" + gender + ", id="
				+ id + ", lastName=" + lastName + ", password=" + password
				+ ", registrationNumber=" + registrationNumber
				+ ", rowingPoints=" + rowingPoints + ", scullingPoints="
				+ scullingPoints + ", squads=" + squads + ", username="
				+ username + ", weight=" + weight + "]";
	}
	
}
