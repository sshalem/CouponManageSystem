package shabtay.coupon.system.exceptions;

/**
 * Defined custom Exception for catching scenario of "Coupon already purchased by customer" 
 * This Exception extends checked Exception 
 * @author User
 *
 */
public class CouponAlreadyPurchsedByCustomerException extends Exception {

	public CouponAlreadyPurchsedByCustomerException(String message) {
		super(message);
	}

}
