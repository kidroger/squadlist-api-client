package uk.co.squadlist.web.model;

public class OutingAvailability {

	private Outing outing;
	private AvailabilityOption availabilityOption;
	
	public Outing getOuting() {
		return outing;
	}
	public void setOuting(Outing outing) {
		this.outing = outing;
	}
	public AvailabilityOption getAvailabilityOption() {
		return availabilityOption;
	}
	public void setAvailabilityOption(AvailabilityOption availability) {
		this.availabilityOption = availability;
	}
	
	@Override
	public String toString() {
		return "OutingAvailability [outing=" + outing + ", availabilityOption="
				+ availabilityOption + "]";
	}
	
}