package by.diomov.newsportal.service.impl.validator;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Exception e) {
		super(e);
	}

	public ValidationException(String message, Exception e) {
		super(message, e);
	}
}
