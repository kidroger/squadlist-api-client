package uk.co.squadlist.web.model;

public class Boat {

	private String id, name, number, type, configuration, supplier, serialNumber, coxPosition;

	public Boat() {
	}

	public Boat(String name) {
		this.name = name;
	}

	public Boat(String name, String number) {
		this.name = name;
		this.number = number;
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCoxPosition() {
		return coxPosition;
	}

	public void setCoxPosition(String coxPosition) {
		this.coxPosition = coxPosition;
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
		Boat other = (Boat) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Boat [id=" + id + ", name=" + name + ", number=" + number
				+ ", type=" + type + ", configuration=" + configuration
				+ ", supplier=" + supplier + ", serialNumber=" + serialNumber
				+ ", coxPosition=" + coxPosition + "]";
	}

}