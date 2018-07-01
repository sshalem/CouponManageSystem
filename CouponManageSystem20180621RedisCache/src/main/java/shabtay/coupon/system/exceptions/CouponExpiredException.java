package shabtay.coupon.system.exceptions;

/**
 * Defined custom Exception for catching scenario of "Coupon expired" 
 * This Exception extends checked Exception 
 * @author User
 *
 */
public class CouponExpiredException extends Exception {

	public CouponExpiredException(String message) {
		super(message);
	}

}
