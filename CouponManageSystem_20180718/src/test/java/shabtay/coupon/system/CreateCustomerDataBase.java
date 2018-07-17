package shabtay.coupon.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.common.ClientType;
import shabtay.coupon.system.entities.Customer;
import shabtay.coupon.system.entry.CouponSystem;
import shabtay.coupon.system.exceptions.CustomerAlreadyExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.AdminFacade;

/**
 * This class is used for Initializing the DB with Customers
 * @author User
 *
 */
@Component
public class CreateCustomerDataBase {

	@Autowired
	CouponSystem couponSystem;

	/**
	 * Creating Customer DB
	 * @throws WrongLoginInputException in case Wrong Login Input 
	 * @throws InterruptedException 
	 */
	public void createDataBase() throws WrongLoginInputException, InterruptedException {

		// Login via CouponSystem (AdminFacde)

		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);

		/*
		 * Load Customers to DB
		 */

		try {
			Customer shabtay = new Customer("shabtay", "121212");
			adminFacade.createCustomer(shabtay);

			Customer karin = new Customer("karin", "2154459");
			adminFacade.createCustomer(karin);

			Customer avigail = new Customer("avigail", "4567897");
			adminFacade.createCustomer(avigail);

			Customer ariel = new Customer("ariel", "784595");
			adminFacade.createCustomer(ariel);

			Customer odel = new Customer("odel", "1200034");
			adminFacade.createCustomer(odel);

		} catch (CustomerAlreadyExistException e) {
			e.printStackTrace();
		}

	}

}
