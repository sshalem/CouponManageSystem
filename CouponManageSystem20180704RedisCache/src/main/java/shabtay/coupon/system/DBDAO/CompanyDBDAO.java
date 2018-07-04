package shabtay.coupon.system.DBDAO;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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

	@Autowired
	CompanyRepository companyRepo;

	@Autowired
	CouponRepository couponRepo;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Collection<Coupon> getCoupons() throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Collection<Coupon> allCoupons = (Collection<Coupon>) couponRepo.findAll();
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
	public synchronized List<Coupon> findCompanyCouponByPrice(long compId, double minimumPrice, double maximumPrice) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		List<Coupon> couponsByPrice = companyRepo.findCompanyCouponByPrice(compId, minimumPrice, maximumPrice);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return couponsByPrice;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized List<Coupon> findCompanyCouponByType(long compId, CouponType type) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		List<Coupon> couponsByType = companyRepo.findCompanyCouponByType(compId, type);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return couponsByType; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized List<Coupon> findCouponBetweenDates(long compId, Date startingDate, Date endingDate) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		List<Coupon> couponBetwenDates = companyRepo.findCouponBetweenDates(compId, startingDate, endingDate);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return couponBetwenDates;
	}

}
