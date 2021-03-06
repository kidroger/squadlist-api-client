package uk.co.squadlist.web.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class Member {
	
	private String id, username, facebookId, role, firstName, lastName, knownAs, 
		gender, emailAddress, contactNumber, emergencyContactName, emergencyContactNumber, 
		rowingPoints, sculling, scullingPoints, sweepOarSide, registrationNumber, password;
	private Integer weight;
	private List<Squad> squads;
	private Date dateOfBirth, availabilityLastUpdated, lastLoggedIn;
	private Boolean admin;
	private String profileImage;
	private boolean inactive;
	
	private Map<String, String> address;
	
	public Member() {
		this.address = Maps.newHashMap();
	}
	
	public Member(String firstName, String lastName, List<Squad> squads, String emailAddress, String password, Date dateOfBirth) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.dateOfBirth = dateOfBirth;		
		this.squads = squads;
		this.address = Maps.newHashMap();
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
	public String getFacebookId() {
		return facebookId;
	}
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	public String getKnownAs() {
		return knownAs;
	}
	public void setKnownAs(String knownAs) {
		this.knownAs = knownAs;
	}
	
	public String getSculling() {
		return sculling;
	}
	public void setSculling(String sculling) {
		this.sculling = sculling;
	}
	
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	
	public Date getAvailabilityLastUpdated() {
		return availabilityLastUpdated;
	}
	public void setAvailabilityLastUpdated(Date availabilityLastUpdated) {
		this.availabilityLastUpdated = availabilityLastUpdated;
	}
	
	public String getDisplayName() {
		if (!Strings.isNullOrEmpty(firstName) && !Strings.isNullOrEmpty(lastName)) {
			return firstName + " " + lastName;			
		}
		return username;
	}
	
	public boolean getInactive() {
		return inactive;
	}
	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}
	
	public Map<String, String> getAddress() {
		return address;
	}
	public void setAddress(Map<String, String> address) {
		this.address = address;
	}

	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}
	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Member{" +
						"id='" + id + '\'' +
						", username='" + username + '\'' +
						", facebookId='" + facebookId + '\'' +
						", role='" + role + '\'' +
						", firstName='" + firstName + '\'' +
						", lastName='" + lastName + '\'' +
						", knownAs='" + knownAs + '\'' +
						", gender='" + gender + '\'' +
						", emailAddress='" + emailAddress + '\'' +
						", contactNumber='" + contactNumber + '\'' +
						", emergencyContactName='" + emergencyContactName + '\'' +
						", emergencyContactNumber='" + emergencyContactNumber + '\'' +
						", rowingPoints='" + rowingPoints + '\'' +
						", sculling='" + sculling + '\'' +
						", scullingPoints='" + scullingPoints + '\'' +
						", sweepOarSide='" + sweepOarSide + '\'' +
						", registrationNumber='" + registrationNumber + '\'' +
						", password='" + password + '\'' +
						", weight=" + weight +
						", squads=" + squads +
						", dateOfBirth=" + dateOfBirth +
						", availabilityLastUpdated=" + availabilityLastUpdated +
						", lastLoggedIn=" + lastLoggedIn +
						", admin=" + admin +
						", profileImage='" + profileImage + '\'' +
						", inactive=" + inactive +
						", address=" + address +
						'}';
	}

}