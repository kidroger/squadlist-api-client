package uk.co.squadlist.web.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;

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

	@JsonIgnore
	public boolean isExpired() {
		return expires != null && expires.before(DateTime.now().toDate());
	}
	
	@Override
	public String toString() {
		return "Subscription [tariff=" + tariff + ", expires=" + expires + "]";
	}

}
