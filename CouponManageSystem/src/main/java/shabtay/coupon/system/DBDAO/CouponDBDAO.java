package shabtay.coupon.system.DBDAO;

import java.util.Collection;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.connection.ConnectionPool;
import shabtay.coupon.system.connection.DBConnection;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.repository.CouponRepository;

/**
 * CouponDBDAO implements the methods from Interface CouponDAO and uses
 * CouponRepository interface to execute the methods in the class
 * 
 * @author Shabtay Shalem
 *
 */
@Component
public class CouponDBDAO implements CouponDAO {

	@Autowired
	CouponRepository couponRepo;

	public CouponDBDAO() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createCoupon(Coupon coupon) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		couponRepo.save(coupon);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();		
		couponRepo.delete(coupon);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		couponRepo.save(coupon);
		ConnectionPool.getInstance().returnConenction(dbConnection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Coupon getCoupon(long id) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Coupon couponById = couponRepo.findById(id);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return couponById;
	} 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Coupon> getAllCoupon() throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Collection<Coupon> allCoupons = (Collection<Coupon>) couponRepo.findAll();
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return allCoupons;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Coupon getCouponByTitle(String title) throws InterruptedException {
		DBConnection dbConnection = ConnectionPool.getInstance().getConnection();
		Coupon couponByTitle = couponRepo.findByTitle(title);
		ConnectionPool.getInstance().returnConenction(dbConnection);
		return couponByTitle;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteExpiredCoupons(Date today) {		
		couponRepo.deleteExpiredCoupons(today);
	}
}
