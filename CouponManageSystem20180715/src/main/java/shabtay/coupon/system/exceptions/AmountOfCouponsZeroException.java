package shabtay.coupon.system.exceptions;

/**
 * Defined custom Exception for catching scenario of "Amount of coupons is zero" 
 * This Exception extends checked Exception 
 * @author User
 *
 */
public class AmountOfCouponsZeroException extends Exception {

	public AmountOfCouponsZeroException(String message) {
		super(message);
	}

}
