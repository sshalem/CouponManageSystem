package shabtay.coupon.system.DBDAO;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.exceptions.WrongLoginInputException;

/**
 * This Interface methods is Implemented by CompanyDBDAO class
 * 
 * @author Shabtay Shalem
 *
 */

public interface CompanyDAO {

	Company loggedInCompany() throws InterruptedException;
	/**
	 * Creates new company
	 * 
	 * @param company
	 *            is the company to add
	 * @throws InterruptedException 
	 */
	void createCompany(Company company) throws InterruptedException;

	/**
	 * Removes a Company
	 * 
	 * @param company
	 *            The company to be removed
	 * @throws InterruptedException 
	 */
	void removeCompany(Company company) throws InterruptedException;

	/**
	 * Updates Company
	 * 
	 * @param company
	 *            The company to be updated
	 * @throws InterruptedException 
	 */
	void updateCompany(Company company) throws InterruptedException;

	/**
	 * Get Company details by Company Id
	 * 
	 * @param id
	 *            company Id
	 * @return Company 
	 * @throws InterruptedException 
	 */
	Company getCompany(long id) throws InterruptedException;

	/**
	 * Get all the Companies with their details (name , password, email) from Data Base
	 * 
	 * @return Collection of Company
	 * @throws InterruptedException 
	 */
	Collection<Company> getAllCompanies() throws InterruptedException;

	/**
	 * Get all coupons from DB
	 * 
	 * @return Collection of Coupons
	 * @throws InterruptedException 
	 */
	Collection<Coupon> getCoupons() throws InterruptedException;

	/**
	 * After Login to CompanyDBDAO is successfully then it is possible to use its methods
	 * 
	 * @param compName company name
	 * @param password Company password
	 * @return true or false
	 * @throws WrongLoginInputException in case login failed
	 * @throws InterruptedException 
	 */
	boolean login(String compName, String password) throws WrongLoginInputException, InterruptedException;

	Company getCompanyByName(String companyName) throws InterruptedException;

	/**
	 * Get List of coupons by their Coupon type
	 * 
	 * @param compId
	 *            companyId
	 * @param type
	 *            coupon type
	 * @return List of Coupon
	 * @throws InterruptedException 
	 */
	List<Coupon> findCompanyCouponByType(CouponType type) throws InterruptedException;

	/**
	 * Get list of coupons between range of prices
	 * 
	 * @param compId
	 *            company Id
	 * @param minimumPrice
	 *            minimum price to enter
	 * @param maximumPrice
	 *            maximum price to enter
	 * @return List of Coupon
	 * @throws InterruptedException 
	 */
	List<Coupon> findCompanyCouponByPrice(double minimumPrice, double maximumPrice) throws InterruptedException;

	/**
	 * get list of coupons between range of dates
	 * @param compId
	 *            COmpany Id
	 * @param startingDate
	 *            start date of coupon
	 * @param endingDate
	 *            end date of coupon
	 * @return List of Coupon
	 * @throws InterruptedException 
	 */
	List<Coupon> findCouponBetweenDates(Date startingDate, Date endingDate) throws InterruptedException;
	
	

	/**
	 * find Coupon By Name
	 * @param compId
	 *            COmpany Id
	 * @param title
	 * @return Coupon
	 * @throws InterruptedException 
	 */
	Coupon findCouponByName(String title) throws InterruptedException;
	
	
	
	Coupon findCouponById(long id) throws InterruptedException;

}
