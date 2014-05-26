package uk.co.squadlist.web.model;

import java.util.Date;

public class Outing {
	
	private String id;
	private Squad squad;
	private Date date;
	private String notes;
	
	public Outing() {
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Squad getSquad() {
		return squad;
	}
	public void setSquad(Squad squad) {
		this.squad = squad;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Outing other = (Outing) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Outing [id=" + id + ", squad=" + squad + ", date=" + date + ", notes=" + notes + "]";
	}
	
}
