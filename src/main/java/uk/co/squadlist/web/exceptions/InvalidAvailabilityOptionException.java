package uk.co.squadlist.web.exceptions;

public class InvalidAvailabilityOptionException extends Exception {
	
	private static final long serialVersionUID = 1L;

	private final String message;

	public InvalidAvailabilityOptionException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return "InvalidAvailabilityOptionException [message=" + message + "]";
	}

}
