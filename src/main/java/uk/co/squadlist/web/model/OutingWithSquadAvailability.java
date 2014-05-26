package uk.co.squadlist.web.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class OutingWithSquadAvailability {

	private Outing outing;
	
	@JsonProperty("availability")
	private Map<String, AvailabilityOption> availability;

	public OutingWithSquadAvailability() {
	}

	public OutingWithSquadAvailability(Outing outing, Map<String, AvailabilityOption> availability) {
		this.outing = outing;
		this.availability = availability;
	}

	public Outing getOuting() {
		return outing;
	}

	public void setOuting(Outing outing) {
		this.outing = outing;
	}

	public Map<String, AvailabilityOption> getAvailability() {
		return availability;
	}

	public void setAvailability(Map<String, AvailabilityOption> availability) {
		this.availability = availability;
	}

	@Override
	public String toString() {
		return "OutingWithSquadAvailability [availability=" + availability
				+ ", outing=" + outing + "]";
	}
	
}
