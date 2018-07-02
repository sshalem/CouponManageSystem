package shabtay.coupon.system.exceptions;

/**
 * Defined custom Exception for catching scenario of "Wrong Login Input" 
 * This Exception extends checked Exception 
 * @author User
 *
 */
public class WrongLoginInputException extends Exception {

	public WrongLoginInputException(String message) {
		super(message);
	}

}
