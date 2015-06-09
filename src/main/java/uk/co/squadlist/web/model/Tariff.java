package uk.co.squadlist.web.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Tariff {

	PRE_JUNE_2015(null, BigDecimal.ZERO), ONE_MONTH_FREE_TRIAL(1, BigDecimal.ZERO), STANDARD_THREE_MONTHS(3, BigDecimal.valueOf(36));

	Integer duration;
	BigDecimal price;

	private Tariff(Integer duration, BigDecimal price) {
		this.duration = duration;
		this.price = price;
		price.setScale(2, RoundingMode.HALF_UP);
	}

	public Integer getDuration() {
		return duration;
	}

	public BigDecimal getPrice() {
		return price;
	}
	
	public String getDisplayPrice() {
		if (price.compareTo(BigDecimal.ZERO) == 0) {
			return "Free";
		}
		return "£" + price.toPlainString() + " inc VAT";
	}

}
