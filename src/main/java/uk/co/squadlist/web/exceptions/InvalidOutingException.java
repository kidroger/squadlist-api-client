package uk.co.squadlist.web.exceptions;

public class InvalidOutingException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private final String message;

	public InvalidOutingException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "InvalidOutingException [message=" + message + "]";
	}
	
}
