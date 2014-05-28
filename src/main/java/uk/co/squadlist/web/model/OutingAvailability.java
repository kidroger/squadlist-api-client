package uk.co.squadlist.web.model;

public class OutingAvailability {

	private Outing outing;
	private AvailabilityOption availability;
	
	public Outing getOuting() {
		return outing;
	}
	public void setOuting(Outing outing) {
		this.outing = outing;
	}
	public AvailabilityOption getAvailability() {
		return availability;
	}
	public void setAvailability(AvailabilityOption availability) {
		this.availability = availability;
	}
	
	@Override
	public String toString() {
		return "OutingAvailability [outing=" + outing + ", availability=" + availability + "]";
	}

}
