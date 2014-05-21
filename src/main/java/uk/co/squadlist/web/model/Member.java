package uk.co.squadlist.web.model;

import java.util.Date;
import java.util.List;

public class Member {
	
	private String id, username, role, firstName, lastName, gender, emailAddress, contactNumber, emergencyContactName, emergencyContactNumber, rowingPoints, scullingPoints, sweepOarSide, registrationNumber, password;
	private Integer weight;
	private List<Squad> squads;
	private Date dateOfBirth;
	private Boolean admin;
	
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
	public void setWeight(Integer weight) {
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

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}
	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}

	public String getSweepOarSide() {
		return sweepOarSide;
	}
	public void setSweepOarSide(String sweepOarSide) {
		this.sweepOarSide = sweepOarSide;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	@Override
	public String toString() {
		return "Member [admin=" + admin + ", contactNumber=" + contactNumber
				+ ", dateOfBirth=" + dateOfBirth + ", emailAddress="
				+ emailAddress + ", emergencyContactName="
				+ emergencyContactName + ", emergencyContactNumber="
				+ emergencyContactNumber + ", firstName=" + firstName
				+ ", gender=" + gender + ", id=" + id + ", lastName="
				+ lastName + ", password=" + password + ", registrationNumber="
				+ registrationNumber + ", role=" + role + ", rowingPoints="
				+ rowingPoints + ", scullingPoints=" + scullingPoints
				+ ", squads=" + squads + ", sweepOarSide=" + sweepOarSide
				+ ", username=" + username + ", weight=" + weight + "]";
	}
	
}
