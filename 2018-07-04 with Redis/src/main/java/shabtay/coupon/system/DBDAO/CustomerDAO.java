package shabtay.coupon.system.DBDAO;

import java.util.Collection;
import java.util.List;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entities.Customer;
import shabtay.coupon.system.exceptions.WrongLoginInputException;

/**
 * This Interface methods is Implemented by CustomerDBDAO class
 * 
 * @author Shabtay Shalem
 *
 */
public interface CustomerDAO {

	/**
	 * Creates new customer
	 * 
	 * @param customer
	 *            is the customer to add
	 * @throws InterruptedException 
	 */
	void createCustomer(Customer customer) throws InterruptedException;

	/**
	 * Remove customer
	 * @param customer The customer to be removed
	 * @throws InterruptedException 
	 */
	void removeCustomer(Customer customer) throws InterruptedException;

	/**
	 * Update customer  
	 * @param customer the customer to be updated
	 * @throws InterruptedException 
	 */
	void updateCustomer(Customer customer) throws InterruptedException;

	/**
	 * Get customer details by its id
	 * @param id
	 *            customer Id
	 * @return Customer
	 * @throws InterruptedException 
	 */
	Customer getCustomer(long id) throws InterruptedException;

	/**
	 * Get all the Customers details from Data Base
	 * 
	 * @return Collection of Customer
	 * @throws InterruptedException 
	 */
	Collection<Customer> getAllCustomer() throws InterruptedException;

	/**
	 * Get all Coupons
	 * 
	 * @return Collection of Coupons
	 * @throws InterruptedException 
	 */
	Collection<Coupon> getCoupons() throws InterruptedException;

	/**
	 * After Login to CustomerDBDAO is successfully then able to use its methods
	 * 
	 * @param custName
	 *            customer name
	 * @param password
	 *            customer password
	 * @return true or false
	 * @throws WrongLoginInputException
	 *             in case login failed
	 * @throws InterruptedException 
	 */
	boolean login(String custName, String password) throws WrongLoginInputException, InterruptedException;

	/**
	 * Get Customer details by its name
	 * 
	 * @param custName
	 *            customer name
	 * @return Customer
	 * @throws InterruptedException 
	 */
	Customer getCustomerByName(String custName) throws InterruptedException;

	/**
	 * 
	 * @param custId
	 *            customer Id
	 * @param minimumPrice
	 *            minimum price to enter
	 * @param maximumPrice
	 *            maximum price to enter
	 * @return List of Coupon by price
	 * @throws InterruptedException 
	 */
	List<Coupon> findAllPurchasedCouponsByPrice(long custId, double minimumPrice, double maximumPrice) throws InterruptedException;

	/**
	 * Get All purchased coupons by couponType
	 * 
	 * @param custId
	 *            customer ID
	 * @param type
	 *            CouponType
	 * @return List of Coupon by type
	 * @throws InterruptedException 
	 */
	List<Coupon> findAllPurchasedCouponsType(long custId, CouponType type) throws InterruptedException;

	/**
	 * Get specific coupon from Customer's purchaseCoupons DB by customer Id & coupon Title 
	 * @param custId - customer Id
	 * @param couponTitle - coupon title (name of coupon)
	 * @return Coupon
	 * @throws InterruptedException 
	 */
	Coupon findCouponInCustomerDB(Long custId, String couponTitle) throws InterruptedException;

}
