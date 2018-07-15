package shabtay.coupon.system.exceptions;

/**
 * Defined custom Exception for catching scenario of "Customer already exist" 
 * This Exception extends checked Exception 
 * @author User
 *
 */
public class CustomerAlreadyExistException extends Exception {

	public CustomerAlreadyExistException(String message) {

		super(message);
	}

}
