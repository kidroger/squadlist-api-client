package uk.co.squadlist.web.model;

import java.util.Date;

public class Tariff {

	private String id;
	private String name;
	Integer duration;
	Date expires;

	public Tariff() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	@Override
	public String toString() {
		return "Tariff{" +
						"id='" + id + '\'' +
						", name='" + name + '\'' +
						", duration=" + duration +
						", expires=" + expires +
						'}';
	}

}
