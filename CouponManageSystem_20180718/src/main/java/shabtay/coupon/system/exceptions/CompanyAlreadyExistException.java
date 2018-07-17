package shabtay.coupon.system.exceptions;

/**
 * Defined custom Exception for catching scenario of "Company already exist" 
 * This Exception extends checked Exception 
 * @author User
 *
 */
public class CompanyAlreadyExistException extends Exception {

	public CompanyAlreadyExistException(String message) {
		super(message);
	}
}
