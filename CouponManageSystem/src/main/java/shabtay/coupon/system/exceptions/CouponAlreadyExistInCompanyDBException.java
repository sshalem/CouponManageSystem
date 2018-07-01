package shabtay.coupon.system.exceptions;

/**
 * Defined custom Exception for catching scenario of "Coupon already exist in Company DB" 
 * This Exception extends checked Exception 
 * @author User
 *
 */
public class CouponAlreadyExistInCompanyDBException extends Exception {

	public CouponAlreadyExistInCompanyDBException(String message) {

		super(message);
	}

}
