package uk.co.squadlist.web.model;

public enum Tariff {

	PRE_JUNE_2015(null, 0), ONE_MONTH_FREE_TRIAL(1, 0), STANDARD_THREE_MONTHS(3, 36);

	Integer duration;
	int price;

	private Tariff(Integer duration, int price) {
		this.duration = duration;
		this.price = price;
	}

	public Integer getDuration() {
		return duration;
	}

	public int getPrice() {
		return price;
	}

}
