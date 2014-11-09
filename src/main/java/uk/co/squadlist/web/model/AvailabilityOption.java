package uk.co.squadlist.web.model;

public class AvailabilityOption {
	
	private String label, colour;
	
	public AvailabilityOption() {
	}
	
	public AvailabilityOption(String label) {
		this.label = label;
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
		return "AvailabilityOption [colour=" + colour + ", label=" + label + "]";
	}
	
}
