package shabtay.coupon.system.DBDAO;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import shabtay.coupon.system.common.ConstantList;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.connection.ConnectionPool;
import shabtay.coupon.system.connection.DBConnection;
import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.repository.CompanyRepository;
import shabtay.coupon.system.repository.CouponRepository;

/**
 * CompanyDBDAO implements the methods from Interface CompanyDAO and uses
 * CompanyRepository interface and CouponRepository interface to execute the
 * methods in the class
 * 
 * @author Shabtay Shalem
 *
 */

@Component
@Scope("prototype")
public class CompanyDBDAO implements CompanyDAO {

	private static Logger logger = LogManager.getLogger(CompanyDBDAO.class);
	
	@Autowired
	CompanyRepository companyRepo;

	@Autowired
	CouponRepository couponRepo;

	private long companyId;

	public CompanyDBDAO() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean login(String compName, String password) throws WrongLoginInputException, InterruptedException {
		for (Company company : getAllCompanies()) {
			if (company.getCompName().equals(compName) && (company.getPassword().equals(password))) {
				this.companyId = company.getId();
				logger.debug("Company DBDAO , Company Id -> " +  this.companyId);
				return true;
			}
		}
		throw new WrongLoginInputException(ConstantList.WRONG_LOGIN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void createCompany(Company company) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		companyRepo.save(company);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void removeCompany(Company company) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		companyRepo.delete(company);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void updateCompany(Company company) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		companyRepo.save(company);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws InterruptedException 
	 */
	@Override
	public Company loggedInCompany() throws InterruptedException {		
		return getCompany(this.companyId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Company getCompany(long id) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Company compById = companyRepo.findById(id);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return compById;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Collection<Company> getAllCompanies() throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Collection<Company> allCompanies = (Collection<Company>) companyRepo.findAll();
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return allCompanies;
	}
	
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	//------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Coupon findCouponById(long coupId) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Coupon CouponByName = companyRepo.getCouponById(this.companyId, coupId);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return CouponByName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Coupon findCouponByName(String title) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Coupon CouponByName = companyRepo.getCouponByTitle(this.companyId, title);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return CouponByName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Collection<Coupon> getCoupons() throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		logger.debug("Company DBDAO getCoupons method , Company Id -> " +  this.companyId);
		Collection<Coupon> allCoupons = (Collection<Coupon>) companyRepo.getAllCoupons(this.companyId);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return allCoupons;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Company getCompanyByName(String companyName) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Company companyByName = companyRepo.findByCompName(companyName);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return companyByName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public synchronized List<Coupon> findCompanyCouponByPrice(double minimumPrice, double maximumPrice)
			throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		List<Coupon> couponsByPrice = companyRepo.findCompanyCouponByPrice(this.companyId, minimumPrice, maximumPrice);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return couponsByPrice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized List<Coupon> findCompanyCouponByType(CouponType type) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		List<Coupon> couponsByType = companyRepo.findCompanyCouponByType(this.companyId, type);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return couponsByType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized List<Coupon> findCouponBetweenDates(Date startingDate, Date endingDate)
			throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		List<Coupon> couponBetwenDates = companyRepo.findCouponBetweenDates(this.companyId, startingDate, endingDate);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return couponBetwenDates;
	}


}
