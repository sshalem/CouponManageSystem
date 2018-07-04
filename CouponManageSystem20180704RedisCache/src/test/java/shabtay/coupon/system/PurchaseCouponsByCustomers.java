package shabtay.coupon.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.DBDAO.CouponDBDAO;
import shabtay.coupon.system.common.ClientType;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entry.CouponSystem;
import shabtay.coupon.system.exceptions.AmountOfCouponsZeroException;
import shabtay.coupon.system.exceptions.CouponAlreadyPurchsedByCustomerException;
import shabtay.coupon.system.exceptions.CouponExpiredException;
import shabtay.coupon.system.exceptions.CouponNotExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.CustomerFacade;

/**
 * This class is used for Purchasing coupons by Customers and updating it in DB * 
 * @author User
 *
 */
@Component
public class PurchaseCouponsByCustomers {

	@Autowired
	CouponDBDAO couponDBDAO;
	@Autowired
	CouponSystem couponSystem;

	/**
	 * purchasing coupons per Customer for testing  
	 */
	public void purchaseCoupons() {

		try {
			CustomerFacade customerFacadeKarin = (CustomerFacade) couponSystem.login("karin", "2154459",
					ClientType.CUSTOMER);
			
			Coupon purchase_coffeeCoffee_israeli_Breakfast = couponDBDAO.getCouponByTitle("CoffeeCoffee israeli Breakfast");
			customerFacadeKarin.purchaseCoupon(purchase_coffeeCoffee_israeli_Breakfast);
			
			Coupon coffeeCoffee_Breakfast = couponDBDAO.getCouponByTitle("CoffeeCoffee Breakfast");
			customerFacadeKarin.purchaseCoupon(coffeeCoffee_Breakfast);
			
			Coupon coffeeCoffee_lunch = couponDBDAO.getCouponByTitle("CoffeeCoffee lunch");
			customerFacadeKarin.purchaseCoupon(coffeeCoffee_lunch);
			

			
			CustomerFacade customerFacadeShabtay = (CustomerFacade) couponSystem.login("shabtay", "121212",
					ClientType.CUSTOMER);
			Coupon aroma_Dinner = couponDBDAO.getCouponByTitle("Aroma Dinner");
			customerFacadeShabtay.purchaseCoupon(aroma_Dinner);

			
			
			CustomerFacade customerFacadeAvigail = (CustomerFacade) couponSystem.login("avigail", "4567897",
					ClientType.CUSTOMER);
			Coupon coffeeCoffee_israeli_Breakfast = couponDBDAO.getCouponByTitle("CoffeeCoffee israeli Breakfast");
			customerFacadeAvigail.purchaseCoupon(coffeeCoffee_israeli_Breakfast);



		} catch (CouponAlreadyPurchsedByCustomerException | AmountOfCouponsZeroException | CouponExpiredException
				| WrongLoginInputException | InterruptedException | CouponNotExistException e) {
			e.printStackTrace();			
		}

	}

}
