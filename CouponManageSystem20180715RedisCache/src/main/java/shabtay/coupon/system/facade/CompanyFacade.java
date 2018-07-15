package shabtay.coupon.system.facade;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.DBDAO.CompanyDBDAO;
import shabtay.coupon.system.DBDAO.CouponDBDAO;
import shabtay.coupon.system.common.ConstantList;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.exceptions.CouponAlreadyExistInCompanyDBException;
import shabtay.coupon.system.exceptions.NameOrIdNotExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;

/**
 * Class CompanyFacade gives the Company to perfrom the following actions:
 * create new coupon, remove Coupon, update Coupon, get Coupon by Id, get Coupon
 * by type, get Coupon by name, get all coupons, get coupons by date, get
 * coupons by price
 * 
 * @author Shabtay Shalem
 *
 */
@Component
@Scope("prototype")
public class CompanyFacade implements CouponClientFacade {

	private static Logger logger = LogManager.getLogger(CompanyFacade.class);
	
	@Autowired
	CouponDBDAO couponDBDAO;

	@Autowired
	CompanyDBDAO companyDBDAO;

	/**
	 * The Id to used once specific company was logged in
	 */
//	private long companyId;

	public CompanyFacade() {
		super();
	}

	@Override
	public CouponClientFacade login(String name, String password) throws WrongLoginInputException, InterruptedException {
		if (companyDBDAO.login(name, password) == true) {
//			companyId = companyDBDAO.getCompanyByName(name).getId();
			return this;
		}
		return null;
	}

	/**
	 * Create New coupon for Company
	 * 
	 * @param newCoupon
	 *            the Coupon to be add to Compnany's DB
	 * @throws CouponAlreadyExistInCompanyDBException
	 *             in case Coupon already exist in DB
	 * @throws InterruptedException 
	 */
	public void createCoupon(Coupon newCoupon) throws CouponAlreadyExistInCompanyDBException, InterruptedException {

//		Company company = companyDBDAO.loggedInCompany();
//		Set<Coupon> coupons = companyDBDAO.loggedInCompany().getCoupons();

		if (couponDBDAO.getCouponByTitle(newCoupon.getTitle()) != null) {
			throw new CouponAlreadyExistInCompanyDBException(
					".... Coupon " + newCoupon.getTitle() + ConstantList.NAME_EXIST);
		}
		
//		coupons.add(newCoupon);
//		company.setCoupons(coupons);		
//		companyDBDAO.updateCompany(company);
		
		Company company = companyDBDAO.loggedInCompany();
		couponDBDAO.createCoupon(newCoupon);
		company.addCoupon(newCoupon);
		companyDBDAO.updateCompany(company);
		
		
		logger.debug("createCoupon() executed for " + newCoupon);
	}

	/**
	 * Removed Coupon from DB
	 * 
	 * @param coupon
	 *            the coupon to be removed
	 * @throws InterruptedException 
	 */
	public void removeCoupon(Coupon coupon) throws InterruptedException {
//		couponDBDAO.removeCoupon(couponDBDAO.getCouponByTitle(coupon.getTitle()));
		couponDBDAO.removeCoupon(coupon);
		logger.debug("removeCoupon() executed for " + coupon);
	}

	/**
	 * update Coupon from DB
	 * 
	 * @param coupon
	 *            the coupon to be updated
	 * @throws InterruptedException 
	 */
	public void updateCoupon(Coupon coupon) throws InterruptedException {
		couponDBDAO.updateCoupon(coupon);
		logger.debug("updateCoupon() executed for " + coupon);
	}

	/**
	 * Get Coupon details by Id
	 * 
	 * @param id
	 *            coupon Id
	 * @return Coupon
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	public Coupon getCoupon(long id) throws InterruptedException, NameOrIdNotExistException {
		if(companyDBDAO.findCouponById(id) == null)
			throw new NameOrIdNotExistException(ConstantList.NAME_OR_ID_NOT_EXIST);
		logger.debug("getCoupon() executed get coupon by Id");
		return companyDBDAO.findCouponById(id);
	}

	/**
	 * Return Coupon details by name
	 * 
	 * @param name
	 *            coupon name
	 * @return Coupon
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	public Coupon getCouponByName(String name) throws InterruptedException, NameOrIdNotExistException {
		if(companyDBDAO.findCouponByName(name) == null)
			throw new NameOrIdNotExistException(ConstantList.NAME_OR_ID_NOT_EXIST);
		logger.debug("getCouponByName() executed get coupon by Name");
		return companyDBDAO.findCouponByName(name);
	}
	
	/**
	 * Get all Coupons per Company
	 * 
	 * @return Collection of Coupon
	 * @throws InterruptedException 
	 */
	@Cacheable("getAllCoupons")
	public Collection<Coupon> getAllCoupon() throws InterruptedException {
		logger.debug("getAllCoupon() executed " + companyDBDAO.getCoupons());
		return companyDBDAO.getCoupons(); 
	}

	/**
	 * Get all coupons by type
	 * 
	 * @param couponType
	 *            coupon type (See CouponType ENUM)
	 * @return List of Coupon
	 * @throws InterruptedException 
	 */
	@Cacheable("getCouponByType")
	public List<Coupon> getCouponByType(CouponType couponType) throws InterruptedException {
		logger.debug("getCouponByType() executed ");
		return companyDBDAO.findCompanyCouponByType(couponType);
	}

	/**
	 * Get coupons by range of price
	 * 
	 * @param minimumPrice
	 *            minimum price to enter
	 * @param maximumPrice
	 *            maximum price to enter
	 * @return List of Coupon
	 * @throws InterruptedException 
	 */
	@Cacheable("getAllCouponsByPrice")
	public List<Coupon> getAllCouponsByPrice(double minimumPrice, double maximumPrice) throws InterruptedException {
		logger.debug("getAllCouponsByPrice() executed ");
		return companyDBDAO.findCompanyCouponByPrice(minimumPrice, maximumPrice);
	}

	/**
	 * Get coupons between dates
	 * 
	 * @param startingDate
	 *            starting Date
	 * @param endingDate
	 *            End date
	 * @return List of Coupon
	 * @throws InterruptedException 
	 */
	@Cacheable("getCouponBetweenDates")
	public List<Coupon> getCouponBetweenDates(Date startingDate, Date endingDate) throws InterruptedException {
		logger.debug("getCouponBetweenDates() executed ");
		return companyDBDAO.findCouponBetweenDates(startingDate, endingDate);
	}
	
}
