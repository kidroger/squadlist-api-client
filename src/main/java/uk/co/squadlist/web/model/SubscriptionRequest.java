package uk.co.squadlist.web.model;

import java.util.Date;

public class SubscriptionRequest {
	
	private String id, club, location, firstName, lastName, email, reason;	
	private Date received;
	private String status, notes, accessDetails;

	public SubscriptionRequest() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClub() {
		return club;
	}

	public void setClub(String club) {
		this.club = club;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getReceived() {
		return received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getAccessDetails() {
		return accessDetails;
	}

	public void setAccessDetails(String accessDetails) {
		this.accessDetails = accessDetails;
	}

	@Override
	public String toString() {
		return "SubscriptionRequest [id=" + id + ", club=" + club
				+ ", location=" + location + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", reason="
				+ reason + ", received=" + received + ", status=" + status
				+ ", notes=" + notes + ", accessDetails=" + accessDetails + "]";
	}
	
}