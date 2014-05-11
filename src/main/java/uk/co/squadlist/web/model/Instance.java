package uk.co.squadlist.web.model;

import java.util.Date;

public class Instance {
	
	private String id;
	private String name;
	private Date lastUsed;
	private String timeZone;
	
	public Instance() {
	}
	
	public Instance(String id, String name, Date lastUsed, String timeZone) {
		this.id = id;
		this.name = name;
		this.lastUsed = lastUsed;
		this.timeZone = timeZone;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Date getLastUsed() {
		return lastUsed;
	}
	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	@Override
	public String toString() {
		return "Instance [id=" + id + ", lastUsed=" + lastUsed + ", name="
				+ name + ", timeZone=" + timeZone + "]";
	}
	
}