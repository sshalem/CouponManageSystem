package shabtay.coupon.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.common.ClientType;
import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entry.CouponSystem;
import shabtay.coupon.system.exceptions.CompanyAlreadyExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.AdminFacade;

/**
 * This class is used for Initializing the DB with Companies
 * @author User
 *
 */
@Component
public class CreateCompanyDataBase {

	@Autowired
	CouponSystem couponSystem;

	/**
	 * Creating Companies and Updating DB
	 * @throws WrongLoginInputException in case Wrong Login Input 
	 * @throws InterruptedException 
	 */
	public void createDataBase() throws WrongLoginInputException, InterruptedException {

		/*
		 * Login to CouponSystem with admin , create Companies and add them DB
		 */
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);

		/*
		 * create RESTURANTS
		 */
		try {
			Company coffeeCoffee = new Company("CoffeCoffe", "123123", "coffeecoffee@gmail.com");
			adminFacade.createCompany(coffeeCoffee);

			Company aroma = new Company("Aroma", "456456", "aroma@gmail.com");
			adminFacade.createCompany(aroma);

			Company shufersal = new Company("shufersal", "sf876", "shufersal@gamil.com");
			adminFacade.createCompany(shufersal);

			Company bistro56 = new Company("Bistro56", "789789", "Bistro56@gmail.com");
			adminFacade.createCompany(bistro56);

			Company mega = new Company("mega", "mg1245", "mega@gamil.com");
			adminFacade.createCompany(mega);

			Company ramiLevi = new Company("ramiLevi", "rl876", "ramiLevi@gamil.com");
			adminFacade.createCompany(ramiLevi);

			Company market = new Company("market", "mark109", "market@gamil.com");
			adminFacade.createCompany(market);

		} catch (CompanyAlreadyExistException e) {
			e.printStackTrace();
		}

	}

}
