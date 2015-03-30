package uk.co.squadlist.web.model;


public class Instance {

	private String id;
	private String name;
	private String timeZone;
	private boolean availabilityVisible;
	private Boolean beta;
	private String memberOrdering;

	public Instance() {
	}

	public Instance(String id, String name, String timeZone, boolean availabilityVisible) {
		this.id = id;
		this.name = name;
		this.timeZone = timeZone;
		this.availabilityVisible = availabilityVisible;
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

	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isAvailabilityVisible() {
		return availabilityVisible;
	}
	public void setAvailabilityVisible(boolean availabilityVisible) {
		this.availabilityVisible = availabilityVisible;
	}

	public Boolean getBeta() {
		return beta;
	}
	public void setBeta(Boolean beta) {
		this.beta = beta;
	}

	public String getMemberOrdering() {
		return memberOrdering;
	}
	public void setMemberOrdering(String memberOrdering) {
		this.memberOrdering = memberOrdering;
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
		Instance other = (Instance) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Instance [availabilityVisible=" + availabilityVisible
				+ ", beta=" + beta + ", id=" + id + ", memberOrdering="
				+ memberOrdering + ", name=" + name + ", timeZone=" + timeZone
				+ "]";
	}

}