package shabtay.coupon.system.DBDAO;

import java.util.Collection;
import java.util.Date;
import shabtay.coupon.system.entities.Coupon;

/**
 * This Interface methods is be Implemented by CouponDBDAO class
 * 
 * @author Shabtay Shalem
 *
 */
public interface CouponDAO {

	/**
	 * Create new Coupon
	 * 
	 * @param coupon
	 *            coupon to create
	 * @throws InterruptedException 
	 */
	void createCoupon(Coupon coupon) throws InterruptedException;

	/**
	 * Remove coupon from DB
	 * 
	 * @param coupon
	 *            coupon to be removed
	 * @throws InterruptedException 
	 */
	void removeCoupon(Coupon coupon) throws InterruptedException;

	/**
	 * Update Coupon details
	 * 
	 * @param coupon
	 *            coupon to be update
	 * @throws InterruptedException 
	 */
	void updateCoupon(Coupon coupon) throws InterruptedException;

	/**
	 * Get coupon details by Coupon Id
	 * 
	 * @param id
	 *            coupon Id
	 * @return Coupon
	 * @throws InterruptedException 
	 */
	Coupon getCoupon(long id) throws InterruptedException;

	/**
	 * Get all coupons
	 * 
	 * @return Collection of Coupons
	 * @throws InterruptedException 
	 */
	Collection<Coupon> getAllCoupon() throws InterruptedException;

	/**
	 * Get coupon details by its Title
	 * @param title coupon title
	 * @return Coupon
	 * @throws InterruptedException 
	 */
	Coupon getCouponByTitle(String title) throws InterruptedException;
	
	/**
	 * Delete coupon who's date is expired
	 * @param today today's date
	 */
	void deleteExpiredCoupons(Date today);

}
