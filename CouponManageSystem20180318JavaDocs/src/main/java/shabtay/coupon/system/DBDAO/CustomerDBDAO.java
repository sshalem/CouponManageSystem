package shabtay.coupon.system.DBDAO;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.common.ConstantList;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.connection.ConnectionPool;
import shabtay.coupon.system.connection.DBConnection;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entities.Customer;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.repository.CouponRepository;
import shabtay.coupon.system.repository.CustomerRepository;

/**
 * CustomerDBDAO implements the methods from Interface CustomerDAO and uses
 * CustomerRepository interface and CouponRepository interface to execute the
 * methods in the class
 * 
 * @author Shabtay Shalem
 *
 */
@Component
public class CustomerDBDAO implements CustomerDAO {

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	CouponRepository couponRepo;

	public CustomerDBDAO() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean login(String custName, String password) throws WrongLoginInputException, InterruptedException {

		for (Customer customer : getAllCustomer()) {
			if (customer.getCustName().equals(custName) && (customer.getPassword().equals(password))) {
				return true;
			}
		}
		throw new WrongLoginInputException(ConstantList.WRONG_LOGIN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createCustomer(Customer customer) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		customerRepo.save(customer);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCustomer(Customer customer) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		customerRepo.delete(customer);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCustomer(Customer customer) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();		
		customerRepo.save(customer);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Customer getCustomer(long id) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Customer customerById = customerRepo.findOne(id);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return customerById;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Customer> getAllCustomer() throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Collection<Customer> allCustomers = (Collection<Customer>) customerRepo.findAll();
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return allCustomers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Coupon> getCoupons() throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Collection<Coupon> allCoupons =  (Collection<Coupon>) couponRepo.findAll();
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return allCoupons;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Customer getCustomerByName(String custName) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Customer customerByName = customerRepo.findByCustName(custName);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return customerByName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Coupon> findAllPurchasedCouponsByPrice(long custId, double minimumPrice, double maximumPrice) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		List<Coupon> customerCouponsByPrice =  customerRepo.findAllPurchasedCouponsByPrice(custId, minimumPrice, maximumPrice);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return customerCouponsByPrice; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Coupon> findAllPurchasedCouponsType(long custId, CouponType type) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		List<Coupon> customerCouponByType = customerRepo.findAllPurchasedCouponsType(custId, type);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return customerCouponByType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Coupon findCouponInCustomerDB(Long custId, String couponTitle) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Coupon customerCoupon = customerRepo.findCouponInCustomerDB(custId, couponTitle);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return customerCoupon;
	} 

}
