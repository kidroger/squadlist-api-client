package uk.co.squadlist.web.model;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.Lists;

public class Member {
	
	private String id, firstName, lastName, gender, dateOfBirth, emailAddress, contactNumber, rowingPoints, scullingPoints, registrationNumber, password;
	private int weight;
	private List<Squad> squads;
	
	public Member() {
	}
	
	public Member(String firstName, String lastName, Squad squad, String emailAddress, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.squads = Lists.newArrayList();
		if (squad != null) {
			squads.add(squad);
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
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
	public int getWeight() {
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
	
	@Deprecated	// TODO should be a RESTful POST
	public List<NameValuePair> toNameValuePairs() {
		final List<NameValuePair> nameValuePairs = Lists.newArrayList();
		nameValuePairs.add(new BasicNameValuePair("firstName", this.getFirstName()));
		nameValuePairs.add(new BasicNameValuePair("lastName", this.getLastName()));
		nameValuePairs.add(new BasicNameValuePair("emailAddress", this.getEmailAddress()));
		nameValuePairs.add(new BasicNameValuePair("contactNumber", this.getContactNumber()));
		nameValuePairs.add(new BasicNameValuePair("weight", Integer.toString(this.getWeight())));
		nameValuePairs.add(new BasicNameValuePair("registrationNumber", this.getRegistrationNumber()));
		nameValuePairs.add(new BasicNameValuePair("rowingPoints", this.getRowingPoints()));
		nameValuePairs.add(new BasicNameValuePair("scullingPoints", this.getScullingPoints()));
		nameValuePairs.add(new BasicNameValuePair("scullingPoints", this.getScullingPoints()));

		return nameValuePairs;
	}
	
	@Override
	public String toString() {
		return "Member [contactNumber=" + contactNumber + ", dateOfBirth="
				+ dateOfBirth + ", emailAddress=" + emailAddress
				+ ", firstName=" + firstName + ", gender=" + gender + ", id="
				+ id + ", lastName=" + lastName + ", registrationNumber="
				+ registrationNumber + ", rowingPoints=" + rowingPoints
				+ ", scullingPoints=" + scullingPoints + ", squads=" + squads
				+ ", weight=" + weight + "]";
	}
	
}
