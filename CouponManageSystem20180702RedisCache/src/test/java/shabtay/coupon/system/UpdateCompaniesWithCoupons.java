package shabtay.coupon.system;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.common.ClientType;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entry.CouponSystem;
import shabtay.coupon.system.exceptions.CouponAlreadyExistInCompanyDBException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.CompanyFacade;

/**
 * This class used in order to update Companies with coupons
 * @author User
 *
 */
@Component
public class UpdateCompaniesWithCoupons {

	@Autowired
	CouponSystem couponSystem;

	/**
	 * Creating Coupons for companies
	 * and adding them to Companies DB
	 * @throws WrongLoginInputException in case Wrong Login Input 
	 */
	public void createCoupons() throws WrongLoginInputException {

		/*
		 * CoffeeCoffee coupons
		 */
		

		Date sd1 = new Date(2018 - 1900, 0, 27);
		Date sd2 = new Date(2018 - 1900, 2, 1);
		Date sd3 = new Date(2018 - 1900, 2, 5);
		Date sd4 = new Date(2018 - 1900, 3, 1);
		Date sd5 = new Date(2018 - 1900, 4, 5);
		Date sd6 = new Date(2018 - 1900, 0, 1);
		Date sd7 = new Date(2018 - 1900, 2, 5);
		Date sd8 = new Date(2018 - 1900, 3, 1);
		Date sd9 = new Date(2018 - 1900, 4, 5);
		

		Date ed1 = new Date(2018 - 1900, 11, 1);
		Date ed2 = new Date(2018 - 1900, 9, 30);
		Date ed3 = new Date(2018 - 1900, 11, 31);
		Date ed4 = new Date(2018 - 1900, 7, 31);
		Date ed5 = new Date(2018 - 1900, 10, 30);
		Date ed6 = new Date(2018 - 1900, 8, 1);
		Date ed7 = new Date(2018 - 1900, 11, 31);
		Date ed8 = new Date(2018 - 1900, 8, 31);
		Date ed9 = new Date(2018 - 1900, 10, 30);
		
		
		Date sd = new Date(2018 - 1900, 0, 1);
		Date ed = new Date(2018 - 1900, 2, 1);

		// SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		// String curDateToStr = format.format(d1);

		/*
		 * CoffeeCoffee coupons
		 */

		try {
			
			/*
			 * Create coupons for CoffeeCoffee company 
			 */
			CompanyFacade CoffeCoffe = (CompanyFacade) couponSystem.login("CoffeCoffe", "123123", ClientType.COMPANY);

			Coupon coffeCoffe_Breakfast = new Coupon("CoffeeCoffee Breakfast", sd1, ed1, 10, CouponType.RESTURANTS,
					"Breakfast for one person", 65, null, null);
			CoffeCoffe.createCoupon(coffeCoffe_Breakfast);

			Coupon coffeCoffe_lunch = new Coupon("CoffeeCoffee lunch", sd2, ed2, 10, CouponType.RESTURANTS,
					"lunch for two persons", 105, null, null);
			CoffeCoffe.createCoupon(coffeCoffe_lunch);
			
			Coupon coffeCoffe_dinner = new Coupon("CoffeeCoffee dinner", sd3, ed3, 10, CouponType.RESTURANTS, "Dinner for two",
					125, null, null);
			CoffeCoffe.createCoupon(coffeCoffe_dinner);
			
			Coupon coffeCoffe_israeli_Breakfast = new Coupon("CoffeeCoffee israeli Breakfast", sd4, ed4, 10, CouponType.RESTURANTS,
					"Dinner for two", 125, null, null);
			CoffeCoffe.createCoupon(coffeCoffe_israeli_Breakfast);

			 
			/*
			 * Create coupons for Aroma company 
			 */
			CompanyFacade aroma = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);

			Coupon aroma_Brekfast = new Coupon("aroma", sd5, ed5, 10, CouponType.RESTURANTS, "Aroma Brekfast for one person", 69,
					null, null);
			aroma.createCoupon(aroma_Brekfast);
			Coupon aroma_Brekfast_for_2 = new Coupon("Aroma breakfast for 2", sd6, ed6, 10, CouponType.RESTURANTS,
					"Aroma Brekfast for two ", 129, null, null);
			aroma.createCoupon(aroma_Brekfast_for_2);
			Coupon aromaDinner = new Coupon("Aroma Dinner", sd7, ed7, 10, CouponType.RESTURANTS, "Aroma dinner", 229,
					null, null);
			aroma.createCoupon(aromaDinner);
			Coupon aroma_Family = new Coupon("Aroma Family", sd8, ed8, 10, CouponType.RESTURANTS, "Aroma dinner for family", 399,
					null, null);
			aroma.createCoupon(aroma_Family);
			Coupon aroma_Express = new Coupon("Aroma express", sd9, ed9, 10, CouponType.RESTURANTS, "Aroma express", 159, null,
					null);
			aroma.createCoupon(aroma_Express);

			
			
			/*
			 * Create coupons for shufersal company 
			 */
			CompanyFacade shufersal = (CompanyFacade) couponSystem.login("shufersal", "sf876", ClientType.COMPANY);

			Coupon vegtables = new Coupon("shufersal vegtables", sd1, ed5, 10, CouponType.FOOD, "vegtables", 69, null,
					null);
			shufersal.createCoupon(vegtables);

			Coupon fruits = new Coupon("shufersal fruits", sd2, ed6, 10, CouponType.FOOD, "fruits ", 129, null, null);
			shufersal.createCoupon(fruits);

			Coupon spageties = new Coupon("shufersal spageties", sd, ed, 10, CouponType.FOOD, "spageties", 229, null,
					null);
			shufersal.createCoupon(spageties);

		} catch (CouponAlreadyExistInCompanyDBException | InterruptedException e) {
			e.printStackTrace(); 
		}
	}

}
