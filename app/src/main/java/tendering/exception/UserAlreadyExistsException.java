package tendering.exception;


public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException(String message) {
		super(message); // Passes the message to the RuntimeException constructor
	}
}