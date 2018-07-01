package shabtay.coupon.system.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query; 
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import shabtay.coupon.system.entities.Coupon;

/**
 * CouponRepository extends CrudRepository
 * @author User
 *
 */
public interface CouponRepository extends CrudRepository<Coupon, Long> {

	Coupon findById(long id);
	
	Coupon findByTitle(String title);
	
	@Query("SELECT c FROM COUPON c WHERE c.endDate<:endDate")
	List<Coupon> GetCouponsByDate(@Param("endDate") Date today);
	
	/**
	 * This method deletes all coupons which there endDate is before current date 
	 * @param today - thats the curren
	 */
	// @Modifying - this annotation is added since it making delete operation, unlike the above query who just reads from DB
	// @Transactional - This annotation is also needed , again because we are doing a delete operation
	@Modifying		
	@Transactional
	@Query("DELETE FROM COUPON c WHERE c.endDate<:endDate")
	void deleteExpiredCoupons(@Param("endDate") Date today);

}
