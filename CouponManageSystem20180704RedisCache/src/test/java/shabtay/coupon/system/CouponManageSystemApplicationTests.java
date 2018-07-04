package shabtay.coupon.system;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit4.SpringRunner;

import shabtay.coupon.system.common.ClientType;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entities.Customer;
import shabtay.coupon.system.entry.CouponSystem;
import shabtay.coupon.system.exceptions.AmountOfCouponsZeroException;
import shabtay.coupon.system.exceptions.CompanyAlreadyExistException;
import shabtay.coupon.system.exceptions.CouponAlreadyExistInCompanyDBException;
import shabtay.coupon.system.exceptions.CouponAlreadyPurchsedByCustomerException;
import shabtay.coupon.system.exceptions.CouponExpiredException;
import shabtay.coupon.system.exceptions.CouponNotExistException;
import shabtay.coupon.system.exceptions.CustomerAlreadyExistException;
import shabtay.coupon.system.exceptions.NameOrIdNotExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.AdminFacade;
import shabtay.coupon.system.facade.CompanyFacade;
import shabtay.coupon.system.facade.CustomerFacade;

/**
 * Here are defined all the test methods that check the functionality 
 * of the code
 * @author Shabtay Shalem
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CouponManageSystemApplicationTests {

	@Autowired
	UpdateCompaniesWithCoupons updateCompaniesWithCoupons;
	@Autowired
	CreateCompanyDataBase createCompanyDataBase;
	@Autowired
	CreateCustomerDataBase createCustomerDataBase;
	@Autowired
	PurchaseCouponsByCustomers purchaseCouponsByCustomers;

	@Autowired
	CouponSystem couponSystem;

	@Test
	public void contextLoads() {
	}	 
	
	/**
	 * test01_adminFacade_CreateCompany() - checks method createCompany() in
	 * AdminFacade class. Creates Company and assert checks if Company is exists
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws CompanyAlreadyExistException in case Company already exist
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */		
	
	@Test
	public void test01_adminFacade_CreateCompany() throws WrongLoginInputException, CompanyAlreadyExistException, InterruptedException, NameOrIdNotExistException {
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Company adidas = new Company("adidas", "adadi777", "adidas@gmail.com");
		adminFacade.createCompany(adidas);
		String expected = adidas.getCompName();
		String actual = adminFacade.getCompByName("adidas").getCompName();
		Assert.assertEquals(expected, actual);
		cleanDB();
	}

	/**
	 * test02_adminFacade_RemoveCompany() - checks method removeCompany() in
	 * AdminFacade class. Test Creates Company in DB , then Removes company from DB and then I try to get the Company 
	 * that I removed, I expect to get NameOrIdNotExistException which states that the Company does not exist
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws CompanyAlreadyExistException 
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test(expected = NameOrIdNotExistException.class)	
	public void test02_adminFacade_RemoveCompany() throws WrongLoginInputException, CompanyAlreadyExistException, InterruptedException, NameOrIdNotExistException {
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Company adidas = new Company("adidas", "adadi777", "adidas@gmail.com");
		adminFacade.createCompany(adidas);
		adminFacade.removeCompany(adminFacade.getCompByName("adidas")); 
		Company actual = adminFacade.getCompByName("adidas");
	}

	/**
	 * test03_adminFacade_UpdateCompany() - checks method updateCompany() in
	 * AdminFacade class. Test updates Company's  password
	 * and assert checks if password was updated 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test
//	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test03_adminFacade_UpdateCompany() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Company tempComany = adminFacade.getCompany(5);
		System.out.println(tempComany);
		tempComany.setPassword("1977");
		adminFacade.updateCompany(tempComany);
		String expectedPassword = "1977";
		String actualPassword = adminFacade.getCompany(5).getPassword();
		Assert.assertEquals(expectedPassword, actualPassword);	
		System.out.println(adminFacade.getCompany(5));
	}

	/**
	 * test04_adminFacade_GetCompany() - checks method getCompany() in AdminFacade
	 * class. Method returns Company by its Id
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test04_adminFacade_GetCompany() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		System.out.println("\n test04 \n Get Company details by Id :" + adminFacade.getCompany(5) + "\n");		
		long expected = 5;
		long actual = adminFacade.getCompany(5).getId();
		Assert.assertEquals(expected, actual);		
		cleanDB();
	}

	/**
	 * test05_adminFacade_GetAllCompanies() - checks method getAllCompanies() in
	 * AdminFacade class. Method get All companies from DB and assert checks if 
	 * NUMBER_OF_COMPANIES_CREATED (after initializeDB() is ran) equals to actual number of companies in DB
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test	
	public void test05_adminFacade_GetAllCompanies() throws WrongLoginInputException, InterruptedException {
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		System.out.println("\n test05 \n Get detailed List of All Companies : " + adminFacade.getAllCompanies() + "\n");
		Assert.assertEquals(ApplicationTestsConstants.NUMBER_OF_COMPANIES_CREATED, adminFacade.getAllCompanies().size());		
		cleanDB();
	} 

	/**
	 * test06_adminFacade_CreateCustomer() - checks method createCustomer() in
	 * AdminFacade class. Create customer and assert checks is customer is inserted in DB
	 * 
	 * @throws CustomerAlreadyExistException in case Customer already exist
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test06_adminFacade_CreateCustomer() throws CustomerAlreadyExistException, WrongLoginInputException, InterruptedException, NameOrIdNotExistException {
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Customer cust = new Customer("avraham", "1234");
		adminFacade.createCustomer(cust);
		String expected = cust.getCustName();
		String actual = adminFacade.getCustomerByName("avraham").getCustName();
		Assert.assertEquals(expected, actual);		
		cleanDB();
	}

	/**
	 * test07_adminFacade_RemoveCustomer() - Test  Removes customer from DB and then I try to get the customer 
	 * that I removed, I expect to get NameOrIdNotExistException which states that the customer does not exist
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test(expected = NameOrIdNotExistException.class)
	public void test07_adminFacade_RemoveCustomer() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		adminFacade.removeCustomer(adminFacade.getCustomerByName("shabtay"));
		adminFacade.getCustomerByName("shabtay").getCustName();		
		cleanDB();
	}
 
	/**
	 * test08_adminFacade_UpdateCustomer() - checks method updateCustomer() in
	 * AdminFacade class. 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test	
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test08_adminFacade_UpdateCustomer() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Customer cust = adminFacade.getCustomerByName("ariel");
		cust.setPassword("2013");
		adminFacade.updateCustomer(cust);
		String expected = "2013";
		String actual = adminFacade.getCustomerByName("ariel").getPassword();
		Assert.assertEquals(expected, actual);		
	}

	/**
	 * test09_adminFacade_GetCustomer() - checks method getCustomer() in
	 * AdminFacade class
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test09_adminFacade_GetCustomer() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		System.out.println("\n test09 \n Get Customer details by Id : " + adminFacade.getCustomer(5) + "\n");
		long expected = 5;
		long actual = adminFacade.getCustomer(5).getId();
		Assert.assertEquals(expected, actual);		
		cleanDB();
	}
 
	/**
	 * test10_adminFacade_GetAllCustomers() - checks method getAllCustomer() in
	 * AdminFacade class. Method get All Customers from DB and assert checks if 
	 * NUMBER_OF_CUSTOMERS_CREATED (after initializeDB() is ran) equals to actual number of companies in DB
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test	
	public void test10_adminFacade_GetAllCustomers() throws WrongLoginInputException, InterruptedException {
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		System.out.println("\n test10 \n Get detailed List of all Customers :" + adminFacade.getAllCustomer() + "\n");
		Assert.assertEquals(ApplicationTestsConstants.NUMBER_OF_CUSTOMERS_CREATED, adminFacade.getAllCustomer().size());	
		cleanDB();
	}
 
	/**
	 * test11_adminFacade_GetAllCoupon() - checks method getAllCoupon() in
	 * AdminFacade class. Method get All Coupons from DB and assert checks if 
	 * NUMBER_OF_COUPONS_CREATAED (after initializeDB() is ran) equals to actual number of companies in DB 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test
	public void test11_adminFacade_GetAllCoupon() throws WrongLoginInputException, InterruptedException {
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		System.out.println("\n test11 \n Get List of All Coupons of all Compnaies" + adminFacade.getAllCoupon());
		Assert.assertEquals(ApplicationTestsConstants.NUMBER_OF_COUPONS_CREATAED, adminFacade.getAllCoupon().size());		
		cleanDB();
	}

	/**
	 * test12_adminFacade_DublicatedCompanyExceptionCheck() - checks if
	 * CompanyAlreadyExistException is thrown while Creating new Company which has same name as Company in DB 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws CompanyAlreadyExistException in case Company Already exist
	 * @throws InterruptedException 
	 */
	@Test(expected = CompanyAlreadyExistException.class)	
	public void test12_adminFacade_DublicatedCompanyExceptionCheck() throws WrongLoginInputException, CompanyAlreadyExistException, InterruptedException {
		cleanDB();
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Company aromaDuplicated = new Company("Aroma", "4564", "aroma@gmail.com");
		adminFacade.createCompany(aromaDuplicated);		
	} 

	/**
	 * test13_adminFacade_DublicatedCustomerExceptionCheck() - checks if
	 * CustomerAlreadyExistException is thrown while Creating new Customer which has same name as Company in DB 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws CustomerAlreadyExistException in case Customer already exist in DB
	 * @throws InterruptedException 
	 */
	@Test(expected = CustomerAlreadyExistException.class)	
	public void test13_adminFacade_DublicatedCustomerExceptionCheck() throws WrongLoginInputException, CustomerAlreadyExistException, InterruptedException {		
		cleanDB();
		initializeDB();
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Customer karin = new Customer("karin", "1268712");
		adminFacade.createCustomer(karin);		
	}

	/**
	 * test14_companyFacade_CreateCoupon() - checks method createCoupon() in
	 * CompanyFacade class
	 * 
	 * @throws CouponAlreadyExistInCompanyDBException in case Coupon alreadyExist in Company DB
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test	
	public void test14_companyFacade_CreateCoupon()
			throws CouponAlreadyExistInCompanyDBException, WrongLoginInputException, InterruptedException, NameOrIdNotExistException {		 
		cleanDB();
		initializeDB();
		CompanyFacade companyFacde = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		Coupon testCoupon = new Coupon("Aroma ultimate breakfast", null, null, 20, CouponType.RESTURANTS, "the best",
				199, null, null);
		companyFacde.createCoupon(testCoupon);
		String expected = "Aroma ultimate breakfast";
		String actual = companyFacde.getCouponByName(expected).getTitle();
		Assert.assertEquals(expected, actual);		
		cleanDB();
	} 

	/**
	 * test15_companyFacade_RemoveCoupon() - checks method removeCoupon() in
	 * CompanyFacade class
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
		
	@Test(expected = NameOrIdNotExistException.class)
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test15_companyFacade_RemoveCoupon() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {		 
		initializeDB();
		System.out.println("\n test15 \n");
		CompanyFacade compF = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		Coupon coupon = compF.getCoupon(5);
		compF.removeCoupon(coupon);
		Coupon actual = compF.getCoupon(5);
		cleanDB();
	}

	/**
	 * test16_companyFacade_UpdateCoupon() - checks method updateCoupon() in
	 * CompanyFacade class. Test updates Coupon's amount
	 * and assert checks if amount was updated 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test16_companyFacade_UpdateCoupon() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {		
		initializeDB();
		System.out.println("\n test16 \n");
		CompanyFacade companyFacde = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		Coupon coupon = companyFacde.getCouponByName("Aroma express");
		coupon.setAmount(777);
		companyFacde.updateCoupon(coupon);
		int expected = 777;
		int actual = companyFacde.getCouponByName("Aroma express").getAmount();
		Assert.assertEquals(expected, actual);	
		cleanDB();
	}

	/**
	 * test17_companyFacade_GetCouponById() - checks method getCoupon() by passing
	 * it Id in CompanyFacade class. Get Coupon by id and assert checks if requested
	 * coupon by id is fetched 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test	
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test17_companyFacade_GetCouponById() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {		
		initializeDB();
		CompanyFacade companyFacde = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		Coupon coupon = companyFacde.getCoupon(7);
		System.out.println("\n test17 \n Get Company Coupon by Id" + coupon);
		long expected = 7;
		long actual = companyFacde.getCoupon(7).getId();
		Assert.assertEquals(expected, actual);	
		cleanDB();
	}

	/**
	 * test18_companyFacade_GetCouponByName() - checks method getCouponByName() in
	 * CompanyFacade class. Get coupon by its Name and assert checks if expected equals actual 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 */
	@Test
	public void test18_companyFacade_GetCouponByName() throws WrongLoginInputException, InterruptedException, NameOrIdNotExistException {		
		initializeDB();
		System.out.println("\n test18 \n");
		CompanyFacade companyFacde = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		String actual = companyFacde.getCouponByName("CoffeeCoffee lunch").getTitle();
		String expected = "CoffeeCoffee lunch";
		Assert.assertEquals(expected, actual);
		cleanDB();
	}

	/**
	 * test19_companyFacade_GetAllCoupon() - checks method getAllCoupon() in
	 * CompanyFacade class.Method get All coupons per Company "Aroma" from DB and assert checks if 
	 * number of expected is equals to actual number of coupons per Company
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test
	public void test19_companyFacade_GetAllCoupon() throws WrongLoginInputException, InterruptedException {		
		initializeDB();
		CompanyFacade companyFacde = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		System.out.println("\n test19 \n Get List Of Company's Coupons : " + companyFacde.getAllCoupon());
		long expected = 5;
		long actual = companyFacde.getAllCoupon().size();
		Assert.assertEquals(expected, actual);	
		cleanDB();
	}

	/**
	 * test20_companyFacade_GetCouponByType() - checks method getCouponByType() in
	 * CompanyFacade class. Initialized DB with coupons with type of RESTURANTS/FOOD . 
	 * Get all coupons (per company) by Coupon Type Resturants . Assert checks 
	 * if all coupon fetched are of the same type
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test
	public void test20_companyFacade_GetCouponByType() throws WrongLoginInputException, InterruptedException {		
		initializeDB();
		CompanyFacade companyFacde = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		System.out.println("\n test20 \n Get List of Company's Coupons By Type :"
				+ companyFacde.getCouponByType(CouponType.RESTURANTS));
		Assert.assertTrue(checkIfAllCouponTypeAreEqual(companyFacde.getCouponByType(CouponType.RESTURANTS) , CouponType.RESTURANTS));  
		cleanDB();
	}

	/**
	 * test21_companyFacade_GetAllCouponsByPrice() - checks method
	 * getAllCouponsByPrice() in CompanyFacade class
	 * Get all coupons (per company) by Coupon price between dates range
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test
	public void test21_companyFacade_GetAllCouponsByPrice() throws WrongLoginInputException, InterruptedException {		
		initializeDB();
		int lowerPrice = 200;
		int higherPrice = 400;
		CompanyFacade companyFacde = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		System.out.println("\n test21 \n List of Company's coupons by price between  Specified price range :"
				+ companyFacde.getAllCouponsByPrice(lowerPrice, higherPrice));
		Assert.assertTrue(checkIfAllCouponAreBetweenPrice(companyFacde.getAllCouponsByPrice(lowerPrice, higherPrice) , lowerPrice, higherPrice ));
		cleanDB();
	}	

	/**
	 * test22_companyFacade_DuplicatedCouponCheck() - checks if
	 * CouponAlreadyExistInCompanyDBException is thrown while Creating new Coupon exist in Company's DB 
	 
	 * @throws WrongLoginInputException in case login fails
	 * @throws CouponAlreadyExistInCompanyDBException  in case Coupon already exist in Company DB 
	 * @throws InterruptedException 
	 */
	@Test(expected = CouponAlreadyExistInCompanyDBException.class)	
	public void test22_companyFacade_DuplicatedCouponCheck() throws WrongLoginInputException, CouponAlreadyExistInCompanyDBException, InterruptedException {		
		initializeDB();
		CompanyFacade companyFacade = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		Coupon coup = new Coupon("Aroma Dinner", null, null, 30, CouponType.RESTURANTS, "Aroma Brekfast for one person",
				69, null, null);
		companyFacade.createCoupon(coup);		
	}

	/**
	 * test23_companyFacade_getCouponBetweenDates() - checks method
	 * getCouponBetweenDates() in CompanyFacade class. Get all coupons between dates 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test
	public void test23_companyFacade_getCouponBetweenDates() throws WrongLoginInputException, InterruptedException {		
		cleanDB();
		initializeDB();
		Date startD = new Date(2018 - 1900, 0, 1);
		Date endD = new Date(2018 - 1900, 7, 31);
		CompanyFacade compF = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		System.out.println("\n test23 \n List of Company's Coupons between Dates : "
				+ compF.getCouponBetweenDates(startD, endD) + " \n");		
		Assert.assertTrue(checkIfCouponsAreBetweenDates(compF.getCouponBetweenDates(startD, endD), startD, endD ));
		cleanDB();
	}	
	
	/**
	 * test24_customerFacade_PurchaseCoupon() - checks method purchaseCoupon() in
	 * CustomerFacade class. Customer purchase coupon and assert check if expected coupon by title equals actual coupon by title
	 * 
	 * @throws CouponAlreadyPurchsedByCustomerException in case Coupon already purchased by Customer
	 * @throws AmountOfCouponsZeroException in case Amount of Coupon is Zero
	 * @throws CouponExpiredException in case Coupon Date Expired
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 * @throws CouponNotExistException 
	 */
	@Test	
	public void test24_customerFacade_PurchaseCoupon() throws CouponAlreadyPurchsedByCustomerException,
			AmountOfCouponsZeroException, CouponExpiredException, WrongLoginInputException, InterruptedException, NameOrIdNotExistException, CouponNotExistException {
		initializeDB();
		CompanyFacade compF = (CompanyFacade) couponSystem.login("CoffeCoffe", "123123", ClientType.COMPANY);
		Coupon purchaseCoup = compF.getCouponByName("Aroma breakfast for 2");		
		CustomerFacade custF = (CustomerFacade) couponSystem.login("shabtay", "121212", ClientType.CUSTOMER);
		custF.purchaseCoupon(purchaseCoup);
		String expected = purchaseCoup.getTitle(); 
		String actual = custF.getCouponFromCustomerDB("Aroma breakfast for 2").getTitle();
		Assert.assertEquals(expected, actual);		
		cleanDB();
	}

	/**
	 * test25_customerFacade_GetAllPurchasedCoupons() - checks method
	 * getAllPurchasedCoupons() in CustomerFacade class. Get all coupons purchased by Customer 
	 * and assert checks if expected purchased coupon equals actual
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test
	public void test25_customerFacade_GetAllPurchasedCoupons() throws WrongLoginInputException, InterruptedException {		
		initializeDB();
		CustomerFacade customerFacade = (CustomerFacade) couponSystem.login("karin", "2154459", ClientType.CUSTOMER);
		System.out.println("\n test25 \n Get List of All purchased Coupons by Customer :"
				+ customerFacade.getAllPurchasedCoupons());
		long expected = 3;
		long actual = customerFacade.getAllPurchasedCoupons().size();
		Assert.assertEquals(expected, actual);	
		cleanDB();
	}

	/**
	 * test26_customerFacade_GetAllPurchasedCouponsByType() - checks method
	 * getAllPurchasedCouponsByType() in CustomerFacade class. 
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test
	public void test26_customerFacade_GetAllPurchasedCouponsByType() throws WrongLoginInputException, InterruptedException {		
		initializeDB();
		CustomerFacade customerFacade = (CustomerFacade) couponSystem.login("karin", "2154459", ClientType.CUSTOMER);
		System.out.println("\n test26 \n List of Customer's Coupons per coupon type"
				+ customerFacade.getAllPurchasedCouponsByType(CouponType.RESTURANTS));	 
		Assert.assertTrue(checkIfAllCouponTypeAreEqual(customerFacade.getAllPurchasedCouponsByType(CouponType.RESTURANTS) , CouponType.RESTURANTS));
		cleanDB();
	}

	/**
	 * test27_customerFacade_GetAllPurchasedCouponsByPrice() - checks method
	 * getAllPurchasedCouponsByPrice() in CustomerFacade class
	 * 
	 * @throws WrongLoginInputException in case login fails
	 * @throws InterruptedException 
	 */
	@Test
	public void test27_customerFacade_GetAllPurchasedCouponsByPrice() throws WrongLoginInputException, InterruptedException {		
		initializeDB();
		int lowerPrice = 50;
		int higherPrice = 120;
		CustomerFacade customerFacade = (CustomerFacade) couponSystem.login("karin", "2154459", ClientType.CUSTOMER);
		System.out.println("\n test27 \n List of  Customer's coupons by Price: "
				+ customerFacade.getAllPurchasedCouponsByPrice(lowerPrice, higherPrice));	
		Assert.assertTrue(checkIfAllCouponAreBetweenPrice(customerFacade.getAllPurchasedCouponsByPrice(lowerPrice, higherPrice) , lowerPrice, higherPrice ));
		cleanDB();
	}

	/**
	 * test28_customerFacade_DuplicatedCouponPurchaseByCustomerCheck() - checks if
	 * CouponAlreadyPurchsedByCustomerException is thrown while customer tries to purchase an already purchased coupon
	 * 
	 * @throws AmountOfCouponsZeroException in case Amount of Coupon is Zero
	 * @throws CouponExpiredException in case Coupon Date Expired
	 * @throws WrongLoginInputException in case login fails
	 * @throws CouponAlreadyPurchsedByCustomerException  in case Coupon Already Purchsed By Customer
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 * @throws CouponNotExistException 
	 */
	@Test(expected = CouponAlreadyPurchsedByCustomerException.class)
	public void test28_customerFacade_DuplicatedCouponPurchaseByCustomerCheck()
			throws AmountOfCouponsZeroException, CouponExpiredException, WrongLoginInputException, CouponAlreadyPurchsedByCustomerException, InterruptedException, NameOrIdNotExistException, CouponNotExistException {
		 
		initializeDB();
		CompanyFacade compF = (CompanyFacade) couponSystem.login("CoffeCoffe", "123123", ClientType.COMPANY);
		Coupon purchaseCoup = compF.getCouponByName("CoffeeCoffee israeli Breakfast");		
		CustomerFacade customerFacade = (CustomerFacade) couponSystem.login("karin", "2154459", ClientType.CUSTOMER);
		customerFacade.purchaseCoupon(purchaseCoup);		
	}

	/**
	 * test29_customerFacade_checkAmountIsZeroException() - checks if
	 * AmountOfCouponsZeroException is thrown if amount of coupons is zero
	 * 
	 * @throws CouponAlreadyExistInCompanyDBException in case Coupon alreadyExist in Company DB
	 * @throws CouponAlreadyPurchsedByCustomerException in case Coupon already purchased by Customer
	 * @throws CouponExpiredException in case Coupon Date Expired
	 * @throws WrongLoginInputException in case login fails
	 * @throws AmountOfCouponsZeroException  in case Amount Of Coupons Zero
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 * @throws CouponNotExistException 
	 */
	@Test(expected = AmountOfCouponsZeroException.class)
	public void test29_customerFacade_checkAmountIsZeroException() throws CouponAlreadyExistInCompanyDBException,
			CouponAlreadyPurchsedByCustomerException, CouponExpiredException, WrongLoginInputException, AmountOfCouponsZeroException, InterruptedException, NameOrIdNotExistException, CouponNotExistException {
		 
		cleanDB();
		initializeDB();	
		Date sd = new Date(2018 - 1900, 5, 1);
		Date ed = new Date(2018 - 1900, 7, 1);
		CompanyFacade compF = (CompanyFacade) couponSystem.login("CoffeCoffe", "123123", ClientType.COMPANY);
		Coupon coup = new Coupon("Coffee test", sd, ed, 0, CouponType.RESTURANTS, "test", 65, null, null);
		compF.createCoupon(coup);
		Coupon purchaseCoup = compF.getCouponByName("Coffee test");
		CustomerFacade custF = (CustomerFacade) couponSystem.login("karin", "2154459", ClientType.CUSTOMER);		
		custF.purchaseCoupon(purchaseCoup);
	}

	/**
	 * test30_customerFacade_checkCouponExpiredDateException() - checks if
	 * CouponExpiredException is thrown
	 * 
	 * @throws CouponAlreadyPurchsedByCustomerException  in case Coupon already purchased by Customer
	 * @throws AmountOfCouponsZeroException in case Amount of Coupon is Zero
	 * @throws WrongLoginInputException in case login fails
	 * @throws CouponExpiredException in case Coupon Expired
	 * @throws InterruptedException 
	 * @throws NameOrIdNotExistException 
	 * @throws CouponNotExistException 
	 */
	@Test(expected = CouponExpiredException.class)
	public void test30_customerFacade_checkCouponExpiredDateException()
			throws CouponAlreadyPurchsedByCustomerException, AmountOfCouponsZeroException, WrongLoginInputException, CouponExpiredException, InterruptedException, NameOrIdNotExistException, CouponNotExistException {

		cleanDB();
		initializeDB();
		Date endDate = new Date(2018 - 1900, 1, 1);
		CompanyFacade compF = (CompanyFacade) couponSystem.login("Aroma", "456456", ClientType.COMPANY);
		Coupon coup = compF.getCouponByName("Aroma breakfast for 2");
		coup.setEndDate(endDate);
		compF.updateCoupon(coup);
		Coupon purchaseCoup = compF.getCouponByName("Aroma breakfast for 2");
		CustomerFacade custF = (CustomerFacade) couponSystem.login("shabtay", "121212", ClientType.CUSTOMER);		
		custF.purchaseCoupon(purchaseCoup);		
	}

	/**
	 * test31_Check_For_WrongLoginInputException() - checks if
	 * WrongLoginInputException is thrown while attempting to login with ADMIN user/password 
	 * @throws WrongLoginInputException 
	 * @throws InterruptedException 
	 */
	@Test(expected = WrongLoginInputException.class) 
	public void test31_Check_For_Admin_WrongLoginInputException() throws WrongLoginInputException, InterruptedException {		
		AdminFacade adminFacade = (AdminFacade) couponSystem.login("admin", "1230004", ClientType.ADMIN);		
	}

	/**
	 * test32_Check_Company_WrongLoginInputException() - checks if
	 * WrongLoginInputException is thrown while attempting to login with COMPANY user/password
	 * @throws WrongLoginInputException 
	 * @throws InterruptedException 
	 */
	@Test(expected = WrongLoginInputException.class)
	public void test32_Check_Company_WrongLoginInputException() throws WrongLoginInputException, InterruptedException {		
		CompanyFacade compF = (CompanyFacade) couponSystem.login("CoffeCoffe", "home", ClientType.COMPANY);				
	}
	
	/**
	 * test33_Check_Customer_WrongLoginInputException() - checks if
	 * WrongLoginInputException is thrown while attempting to login with CUSTOMER user/password
	 * @throws WrongLoginInputException 
	 * @throws InterruptedException 
	 */
	@Test(expected = WrongLoginInputException.class)
	
	public void test33_Check_Customer_WrongLoginInputException() throws WrongLoginInputException, InterruptedException {		
		CustomerFacade custF = (CustomerFacade) couponSystem.login("shabtay", "122121", ClientType.CUSTOMER);		
	}
	
	/**
	 * This method is used in order to clean the DB from data
	 * Since I use @DirtiesContext annotation , it will automatically clean the cache of the DB 
	 * and all data will be cleared
	 * @throws WrongLoginInputException
	 * @throws InterruptedException
	 */
	@Test
	@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void test34_cleanDB() throws WrongLoginInputException, InterruptedException {	 	
				
	}
	
	// I use this test in order to Initialized DB with Companies, Customers, Coupons ,purchase coupons
	private void initializeDB() throws WrongLoginInputException, InterruptedException {
		createCompanyDataBase.createDataBase();
		createCustomerDataBase.createDataBase();
		updateCompaniesWithCoupons.createCoupons();
		purchaseCouponsByCustomers.purchaseCoupons();
	}
	
	private boolean checkIfAllCouponTypeAreEqual(List<Coupon> lisOfcouponByType, CouponType resturants) {		
		for (Coupon coupon : lisOfcouponByType) {
			if(!(coupon.getType().equals(resturants)))
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean checkIfAllCouponAreBetweenPrice(List<Coupon> listOfCouponsBetweenPrices, int lowerPrice, int higherPrice) {
		for (Coupon coupon : listOfCouponsBetweenPrices) {
			if(coupon.getPrice() <= lowerPrice || coupon.getPrice() >= higherPrice )
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean checkIfCouponsAreBetweenDates(List<Coupon> listOfCouponsBetweenDates, Date startD, Date endD) {
		for (Coupon coupon : listOfCouponsBetweenDates) {
			if(coupon.getStartDate().before(startD) || coupon.getEndDate().after(endD))
			{
				return false;
			}
		}
		return true;
	}
	
	private void cleanDB() throws WrongLoginInputException, InterruptedException {
		removeAllCompany();
		removeAllCustomers();
	}

	private void removeAllCompany() throws WrongLoginInputException, InterruptedException {
		AdminFacade af = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Collection<Company> companies = af.getAllCompanies();
		for (Company company : companies) {
			af.removeCompany(company);
		}
	}

	private void removeAllCustomers() throws WrongLoginInputException, InterruptedException {
		AdminFacade af = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
		Collection<Customer> customers = af.getAllCustomer();
		for (Customer customer : customers) {
			af.removeCustomer(customer);
		}
	}

}
