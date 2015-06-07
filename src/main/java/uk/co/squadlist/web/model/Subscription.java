package uk.co.squadlist.web.model;

import java.util.Date;

public class Subscription {

	public Subscription() {
	}

	public Subscription(Tariff tariff, Date expires) {
		this.tariff = tariff;
		this.expires = expires;
	}

	private Tariff tariff;
	private Date expires;

	public Tariff getTariff() {
		return tariff;
	}
	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}

	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}

	@Override
	public String toString() {
		return "Subscription [tariff=" + tariff + ", expires=" + expires + "]";
	}

}
