package shabtay.coupon.system.common;

/**
 * Constant List used in different location in program
 * @author User
 *
 */
public class ConstantList {

	public static String ADMIN_USER = "admin";
	public static String ADMIN_PASSWORD = "1234";
	public static String WRONG_LOGIN = " wrong UserName / Password / ClientType ";
	public static String NAME_EXIST = "  ... already exists... please type another name";
	public static String EXPIRED_COUPON = " Coupon Expired , unable to purches ";
	public static String COUPON_NOT_EXIST = " Coupon not exist , verify coupon name ";
	public static String AMOUNT_IS_ZERO = " No more coupons left to purchase ";
	public static String EXIST_COUPON = " not allowed to purchase same coupon... since this Coupon already purchased by Customer ";
	public static String NAME_OR_ID_NOT_EXIST = " ... Data Not Exist for NAME/ID you entered ... ";
	public static final int NUMBER_OF_CONNECTIONS = 5; 
	public static final int REDIS_SERVER_EXPIRATION_TIME = 10;
	

}
