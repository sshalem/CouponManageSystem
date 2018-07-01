package shabtay.coupon.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entities.Customer;

/**
 * CustomerRepository extends CrudRepository
 * @author User
 *
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	Customer findById(long id); 
		
	Customer findByCustName(String custname);

	/**
	 * This Query : Joins between Customer entity and Coupons entity (using  cust.coupons AS cp ) ,
	 * then search for coupons per Customer ID And between range of prices (minPrice, maxPrice) 
	 * @param custId
	 * @param minimumPrice
	 * @param maximumPrice
	 * @return List of Coupon by Price
	 */
	// This Query : Joins between Customer entity and Coupons entity (using  cust.coupons AS cp ) ,
	// then search for coupons per Customer ID And between range of prices (minPrice, maxPrice)  
	@Query("SELECT cp FROM CUSTOMER cust JOIN cust.coupons AS cp WHERE cust.id=:id AND cp.price>=:minPrice AND cp.price<=:maxPrice")
	List<Coupon> findAllPurchasedCouponsByPrice(@Param("id") long custId, @Param("minPrice") double minimumPrice, @Param("maxPrice") double maximumPrice);

	
	/**
	 * This Query : Joins between Customer entity and Coupons entity (using  cust.coupons AS cp ) ,
	 * then search for coupons per Customer ID And CouponType 
	 * @param custId
	 * @param type
	 * @return List of Coupon by Type
	 */
	// This Query : Joins between Customer entity and Coupons entity (using  cust.coupons AS cp ) ,
	// then search for coupons per Customer ID And CouponType 
	@Query("SELECT cp FROM CUSTOMER cust JOIN cust.coupons AS cp WHERE cust.id=:id AND cp.type=:couponType")
	List<Coupon> findAllPurchasedCouponsType(@Param("id") long custId, @Param("couponType") CouponType type);

	
	/**
	 * This Query : Joins between Customer entity and Coupons entity (using  cust.coupons AS cp ) ,
	 * then search for coupons per Customer ID And Coupon Title  
	 * @param custId
	 * @param couponTitle
	 * @return Coupon 
	 */
	// This Query : Joins between Customer entity and Coupons entity (using  cust.coupons AS cp ) ,
	// then search for coupons per Customer ID And Coupon Title  
	@Query("SELECT cp FROM CUSTOMER cust JOIN cust.coupons AS cp WHERE cust.id=:id AND cp.title=:title")
	Coupon findCouponInCustomerDB(@Param("id") long custId, @Param("title") String couponTitle);

}
