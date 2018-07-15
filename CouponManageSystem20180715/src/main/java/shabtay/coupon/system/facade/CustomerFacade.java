package shabtay.coupon.system.facade;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.DBDAO.CouponDBDAO;
import shabtay.coupon.system.DBDAO.CustomerDBDAO;
import shabtay.coupon.system.common.ConstantList;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entities.Customer;
import shabtay.coupon.system.exceptions.AmountOfCouponsZeroException;
import shabtay.coupon.system.exceptions.CouponAlreadyPurchsedByCustomerException;
import shabtay.coupon.system.exceptions.CouponExpiredException;
import shabtay.coupon.system.exceptions.CouponNotExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;

/**
 * Class CustomerFacade gives the Customer to perform the following actions ,
 * after login was successfully: purchaseCoupon, getAllPurchasedCoupons,
 * getAllPurchasedCouponsByType, getAllPurchasedCouponsByPrice,
 * getCouponInCustomerDB
 * 
 * 
 * @author Shabtay Shalem
 *
 */
@Component
public class CustomerFacade implements CouponClientFacade {

	private static Logger logger = LogManager.getLogger(CustomerFacade.class);
	
	@Autowired
	CouponDBDAO couponDBDAO;
	@Autowired
	CustomerDBDAO customerDBDAO;

	/**
	 * The Id to used once specific customer was logged in
	 */
	private long customerId;
	
	private boolean check = false;

	public CustomerFacade() {
		super();
	}

	/**
	 * if login was successful customer is able to perform all methods in this class
	 * @throws InterruptedException 
	 */
	@Override
	public CouponClientFacade login(String name, String password) throws WrongLoginInputException, InterruptedException {
		if (customerDBDAO.login(name, password) == true) {
			customerId = customerDBDAO.getCustomerByName(name).getId();
			return this;
		}
		return null;
	}

	/**
	 * Purchase coupon by Customer
	 * 
	 * @param coupon
	 *            the Coupon customer wants to purchase
	 * @throws CouponAlreadyPurchsedByCustomerException
	 *             thrown in case coupon already purchased by customer
	 * @throws AmountOfCouponsZeroException
	 *             thrown if coupon amount is zero
	 * @throws CouponExpiredException
	 *             thrown if coupon date is expired
	 * @throws InterruptedException 
	 * @throws CouponNotExistException 
	 */

	public void purchaseCoupon(Coupon coupon)
			throws CouponAlreadyPurchsedByCustomerException, AmountOfCouponsZeroException, CouponExpiredException, InterruptedException, CouponNotExistException {

		Collection<Coupon> customerCoupons = getAllPurchasedCoupons();
		Customer customer = customerDBDAO.getCustomer(customerId);
		Coupon couponFromDB = couponDBDAO.getCouponByTitle(coupon.getTitle());

		if (couponFromDB != null) {
			if (checkIfCouponDateExpired(couponFromDB.getEndDate())) {
				throw new CouponExpiredException(ConstantList.EXPIRED_COUPON);
			}
			if (couponFromDB.getAmount() == 0) {
				throw new AmountOfCouponsZeroException(ConstantList.AMOUNT_IS_ZERO);
			}
		}
		else {
			throw new CouponNotExistException(ConstantList.COUPON_NOT_EXIST);
		}
		
		// this.check is set to true  only during Purchase Coupon transaction  
		this.check = true;
		// here we check if the new coupon exist in Customer Coupon DB
		// If it exist (which means its not equal to null) an exception is thrown of an EXIST COUPON
		if ((getCouponFromCustomerDB(coupon.getTitle()) != null) ) {
			throw new CouponAlreadyPurchsedByCustomerException(ConstantList.EXIST_COUPON + customer.getCustName());
		}
		
		customerCoupons.add(couponFromDB);
		couponFromDB.setAmount(couponFromDB.getAmount() - 1);
		couponDBDAO.updateCoupon(couponFromDB);
		customer.setCoupons(customerCoupons);
		customerDBDAO.updateCustomer(customer);
		logger.debug("purchaseCoupon() executed for " + coupon);
	}

	/**
	 * Get all purchased coupons by customer
	 * 
	 * @return Collection of Coupon
	 * @throws InterruptedException 
	 */	
	public Collection<Coupon> getAllPurchasedCoupons() throws InterruptedException {
		logger.debug("getAllPurchasedCoupons() executed ");
		return customerDBDAO.getCoupons(this.customerId);
	}

	/**
	 * Get all purchased coupons by type per customer
	 * 
	 * @param couponType
	 *            couponType coupon type (See CouponType ENUM)
	 * @return List of Coupon
	 * @throws InterruptedException 
	 */	
	public List<Coupon> getAllPurchasedCouponsByType(CouponType couponType) throws InterruptedException {
		logger.debug("getAllPurchasedCouponsByType() executed ");
		return customerDBDAO.findAllPurchasedCouponsType(this.customerId, couponType);
	}

	/**
	 * Get all purchased coupons between range of prices per customer
	 * 
	 * @param minimumPrice
	 *            minimum price that was entered
	 * @param maximumPrice
	 *            minimum price that was entered
	 * @return List of Coupon
	 * @throws InterruptedException 
	 */	
	public List<Coupon> getAllPurchasedCouponsByPrice(double minimumPrice, double maximumPrice) throws InterruptedException {
		logger.debug("getAllPurchasedCouponsByPrice() executed ");
		return customerDBDAO.findAllPurchasedCouponsByPrice(this.customerId, minimumPrice, maximumPrice);
	}

	/**
	 * Get Coupon From Customer DB by title
	 * 
	 * @param title
	 *            name of coupon
	 * @return Coupon
	 * @throws InterruptedException 
	 * @throws CouponNotExistException 
	 */
	public Coupon getCouponFromCustomerDB(String title) throws InterruptedException, CouponNotExistException {
		if(this.check != true)
		{
			if(customerDBDAO.findCouponInCustomerDB(this.customerId, title) == null)
				throw new CouponNotExistException(ConstantList.COUPON_NOT_EXIST);
		}
		this.check = false;
		return customerDBDAO.findCouponInCustomerDB(this.customerId, title);		
	}	
	
	public Collection<Coupon> getAllCoupon() throws InterruptedException {
		logger.debug("getAllCoupon() executed ");
		return couponDBDAO.getAllCoupon();
	}

	private boolean checkIfCouponDateExpired(Date endDate) {
		Date currentDate = Calendar.getInstance().getTime();
		return endDate.before(currentDate);
	}

}
