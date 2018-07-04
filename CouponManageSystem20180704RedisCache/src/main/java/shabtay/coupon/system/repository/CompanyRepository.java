package shabtay.coupon.system.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entities.Coupon;

/**
 * CompanyRepository extends CrudRepository
 * @author User
 *
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {

	Company findById(long id);

	Company findByCompName(String compname);

	/**
	 * This Query : Joins between Company entity and Coupons entity (using  comp.coupons AS cp ) ,
	 * then search for coupons per Company ID And CouponType
	 * @param compId
	 * @param type
	 * @return List of Coupon
	 */
	// This Query : Joins between Company entity and Coupons entity (using  comp.coupons AS cp ) ,
	// then search for coupons per Company ID And CouponType
	@Query("SELECT cp FROM COMPANY comp JOIN comp.coupons AS cp WHERE comp.id=:id AND cp.type=:couponType")
	List<Coupon> findCompanyCouponByType(@Param("id") long compId, @Param("couponType") CouponType type);

	/**
	 * This Query : Joins between Company entity and Coupons entity (using  comp.coupons AS cp ) ,
	 * then search for coupons per Company ID And between range of prices (minPrice, maxPrice) 
	 * @param compId
	 * @param minimumPrice
	 * @param maximumPrice
	 * @return List Of Coupon
	 */
	// This Query : Joins between Company entity and Coupons entity (using  comp.coupons AS cp ) ,
	// then search for coupons per Company ID And between range of prices (minPrice, maxPrice)  
	@Query("SELECT cp FROM COMPANY comp JOIN comp.coupons AS cp WHERE comp.id=:id AND cp.price>=:minPrice AND cp.price<=:maxPrice")
	List<Coupon> findCompanyCouponByPrice(@Param("id") long compId, @Param("minPrice") double minimumPrice,	@Param("maxPrice") double maximumPrice);

	/**
	 * This Query : Joins between Company entity and Coupons entity (using  comp.coupons AS cp ) ,
	 * then search for coupons per Company ID And between dates (startDate, endDate)
	 * @param compId
	 * @param startingDate
	 * @param endingDate
	 * @return List of Coupon
	 */
	// This Query : Joins between Company entity and Coupons entity (using  comp.coupons AS cp ) ,
	// then search for coupons per Company ID And between dates (startDate, endDate)  
	@Query("SELECT cp FROM COMPANY comp JOIN comp.coupons AS cp WHERE comp.id=:id AND cp.startDate>=:startDate AND cp.endDate<=:endDate")
	List<Coupon> findCouponBetweenDates(@Param("id") long compId, @Param("startDate") Date startingDate, @Param("endDate") Date endingDate);

}
