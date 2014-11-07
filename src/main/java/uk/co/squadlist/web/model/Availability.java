package uk.co.squadlist.web.model;

public class Availability {
	
	private Member member;
	private Outing outing;
	private AvailabilityOption availabilityOption;
	
	public Availability(Member member, Outing outing, AvailabilityOption availabilityOption) {
		this.member = member;
		this.outing = outing;
		this.availabilityOption = availabilityOption;
	}
	
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	
	public Outing getOuting() {
		return outing;
	}
	public void setOuting(Outing outing) {
		this.outing = outing;
	}
	
	public AvailabilityOption getAvailabilityOption() {
		return availabilityOption;
	}
	public void setAvailabilityOption(AvailabilityOption availabilityOption) {
		this.availabilityOption = availabilityOption;
	}
	
	@Override
	public String toString() {
		return "Availability [availabilityOption=" + availabilityOption + ", member=" + member + ", outing=" + outing + "]";
	}
	
}
