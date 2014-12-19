package uk.co.squadlist.web.model;

public class AvailabilityOption {
	
	private String id, label, colour;
	
	public AvailabilityOption() {
	}
	
	public AvailabilityOption(String label) {
		this.label = label;
	}
	
	public AvailabilityOption(String label, String colour) {
		this.label = label;
		this.colour = colour;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
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
		AvailabilityOption other = (AvailabilityOption) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AvailabilityOption [id=" + id + ", label=" + label + ", colour=" + colour + "]";
	}
		
}