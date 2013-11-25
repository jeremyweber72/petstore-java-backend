package es.zaldo.petstore.core.exceptions;

/**
 * This exception will be launched whenever an unexpected error happens while
 * making predictions on queries.
 */
@SuppressWarnings("serial")
public class PetsValidationException extends RuntimeException {

   /**
    * Constructor of the class.
    *
    * @param message Brief description of the error
    * @param cause Root exception
    */
   public PetsValidationException(String message, Throwable cause) {
       super(message, cause);
   }

   /**
    * Constructor of the class.
    *
    * @param message Brief description of the error
    */
   public PetsValidationException(String message) {
       super(message);
   }

}
