package shabtay.coupon.system.exceptions;

/**
 * Defined custom Exception for catching scenario of "Wrong Login Input" 
 * This Exception extends checked Exception 
 * @author User
 *
 */
public class NameOrIdNotExistException extends Exception {

	public NameOrIdNotExistException(String message) {
		super(message);
	}	

}
