package uk.co.squadlist.web.model;

public class AvailabilityOption {
	
	private String id, label, colour;
	
	public AvailabilityOption() {
	}
	
	public AvailabilityOption(String label) {
		this.label = label;
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
	public String toString() {
		return "AvailabilityOption [id=" + id + ", label=" + label
				+ ", colour=" + colour + "]";
	}
		
}