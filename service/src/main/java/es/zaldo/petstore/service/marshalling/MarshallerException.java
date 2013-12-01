package es.zaldo.petstore.service.marshalling;

/**
 * Will be thrown whenever an error happens while marshalling objects.
 *
 */
@SuppressWarnings("serial")
public class MarshallerException extends Exception {

	/**
	 * Constructor of the class.
	 *
	 * @param message Brief description of the error
	 * @param cause Root exception
	 */
	public MarshallerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor of the class.
	 *
	 * @param message Brief description of the error
	 */
	public MarshallerException(String message) {
		super(message);
	}

}
